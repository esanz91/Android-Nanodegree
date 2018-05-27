package com.esanz.nano.movies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.api.MovieRemoteDataSource;

public class MainActivity extends AppCompatActivity {

    private final MovieAdapter movieAdapter = new MovieAdapter();

    private MovieViewModel movieViewModel;

    private RecyclerView movieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieView = findViewById(R.id.movies);
        movieView.setAdapter(movieAdapter);

        // TODO use dagger injection
        MovieRepository movieRepository = MovieRepository.getInstance(MovieRemoteDataSource.getInstance());
        ViewModelFactory movieFactory = new ViewModelFactory(movieRepository);

        movieViewModel = ViewModelProviders.of(this, movieFactory).get(MovieViewModel.class);
        movieViewModel.loadTopRatedMovies();
        movieViewModel.topRatedLiveData
                .observe(this, (topRatedResponse) -> {
                    movieAdapter.setMovies(topRatedResponse.movies);
                });
    }

}
