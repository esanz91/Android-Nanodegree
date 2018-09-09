package com.esanz.nano.movies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.esanz.nano.movies.MovieApplication;
import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.ui.adapter.MovieAdapter;
import com.esanz.nano.movies.ui.viewModel.MovieListViewModel;
import com.esanz.nano.movies.ui.viewModel.MovieListViewModelFactory;
import com.esanz.nano.movies.utils.MovieConstant;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.OnMovieClickListener {

    private static final int REQUEST_DETAILS = 1;

    private final MovieAdapter movieAdapter = new MovieAdapter(this);

    private MovieListViewModel movieViewModel;

    private PopupMenu popupMenu;
    private RecyclerView movieView;
    private View emptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        emptyStateView = findViewById(R.id.empty_state);
        movieView = findViewById(R.id.movies);
        movieView.setAdapter(movieAdapter);

        MovieListViewModelFactory movieFactory = new MovieListViewModelFactory(
                MovieApplication.movieRepository);

        movieViewModel = ViewModelProviders.of(this, movieFactory)
                .get(MovieListViewModel.class);

        @MovieConstant.SortTypeDef int sortType = movieViewModel.getSortType();
        switch (sortType) {
            case MovieConstant.SortType.RATING:
                movieViewModel.loadTopRatedMovies();
                break;
            case MovieConstant.SortType.POPULARITY:
                movieViewModel.loadPopularMovies();
                break;
            case MovieConstant.SortType.FAVORITES:
                movieViewModel.loadFavoriteMovies();
        }

        movieViewModel.movieListLiveData
                .observe(this, movieList -> {
                    if (null != movieList && !movieList.isEmpty()) {
                        movieAdapter.setMovies(movieList);
                        emptyStateView.setVisibility(View.GONE);
                        movieView.setVisibility(View.VISIBLE);
                    } else {
                        emptyStateView.setVisibility(View.VISIBLE);
                        movieView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by:
                showSortByPopupMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DETAILS &&
                resultCode == MovieDetailActivity.RESULT_RELOAD_FAVORITES &&
                movieViewModel.getSortType() == MovieConstant.SortType.FAVORITES) {
            movieViewModel.loadFavoriteMovies();
        }
    }

    @Override
    protected void onPause() {
        if (null != popupMenu) {
            popupMenu.dismiss();
            popupMenu = null;
        }
        super.onPause();
    }

    @Override
    public void onMoviePosterClick(Movie movie) {
        Intent movieDetailIntent = MovieDetailActivity.createIntent(this, movie);
        startActivityForResult(movieDetailIntent, REQUEST_DETAILS);
    }

    @Override
    public void onFavoriteClick(Movie movie, boolean isFavorite) {
        if (isFavorite) {
            movieViewModel.addFavorite(movie);
        } else {
            movieViewModel.removeFavorite(movie);
        }
    }

    private void showSortByPopupMenu() {
        View menuItem = findViewById(R.id.sort_by);
        popupMenu = new PopupMenu(this, menuItem);
        popupMenu.inflate(R.menu.sort_options);
        popupMenu.setOnMenuItemClickListener(item -> {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.sort_by_rating:
                    movieViewModel.loadTopRatedMovies();
                    return true;
                case R.id.sort_by_popularity:
                    movieViewModel.loadPopularMovies();
                    return true;
                case R.id.sort_by_favorite:
                    movieViewModel.loadFavoriteMovies();
                    return true;
            }
            return false;
        });

        @MovieConstant.SortTypeDef int sortType = movieViewModel.getSortType();
        switch (sortType) {
            case MovieConstant.SortType.RATING:
                popupMenu.getMenu().findItem(R.id.sort_by_rating).setChecked(true);
                break;
            case MovieConstant.SortType.POPULARITY:
                popupMenu.getMenu().findItem(R.id.sort_by_popularity).setChecked(true);
                break;
            case MovieConstant.SortType.FAVORITES:
                popupMenu.getMenu().findItem(R.id.sort_by_favorite).setChecked(true);
                break;
        }

        popupMenu.show();
    }
}
