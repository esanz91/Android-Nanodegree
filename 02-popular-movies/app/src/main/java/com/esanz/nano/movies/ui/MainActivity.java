package com.esanz.nano.movies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.api.MovieRemoteDataSource;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.utils.MovieConstant;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.OnMovieClickListener {

    private final MovieAdapter movieAdapter = new MovieAdapter(this);

    private MovieViewModel movieViewModel;

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

        // TODO use dagger injection
        MovieRepository movieRepository = MovieRepository.getInstance(
                MovieRemoteDataSource.getInstance()
        );
        ViewModelFactory movieFactory = new ViewModelFactory(movieRepository);

        movieViewModel = ViewModelProviders.of(this, movieFactory).get(MovieViewModel.class);

        @MovieConstant.SortTypeDef int sortType = movieViewModel.getSortType();
        switch (sortType) {
            case MovieConstant.SortType.RATING:
                movieViewModel.loadTopRatedMovies();
                break;
            case MovieConstant.SortType.POPULARITY:
                movieViewModel.loadPopularMovies();
                break;
        }

        movieViewModel.movieListLiveData
                .observe(this, movieList -> {
                    if (null != movieList && null != movieList.movies && !movieList.movies.isEmpty()) {
                        movieAdapter.setMovies(movieList.movies);
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
    protected void onPause() {
        if (null != popupMenu) {
            popupMenu.dismiss();
            popupMenu = null;
        }
        super.onPause();
    }

    @Override
    public void onMoviePosterClick(Movie movie, Pair<View, String> moviePosterTransition) {
        Intent movieDetailIntent = MovieDetailActivity.createIntent(this, movie);

        // TODO add and perfect animation
        /*ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, moviePosterTransition);
        startActivity(movieDetailIntent, options.toBundle());*/

        startActivity(movieDetailIntent);
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
        }

        popupMenu.show();
    }
}
