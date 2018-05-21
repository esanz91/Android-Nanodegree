package com.esanz.nano.movies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.MovieRepository;
import com.esanz.nano.movies.repository.api.MovieRemoteDataSource;

public class MainActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO use dagger injection
        MovieRepository movieRepository = MovieRepository.getInstance(MovieRemoteDataSource.getInstance());
        ViewModelFactory movieFactory = new ViewModelFactory(movieRepository);

        movieViewModel = ViewModelProviders.of(this, movieFactory).get(MovieViewModel.class);
        movieViewModel.loadTopRatedMovies();
        movieViewModel.topRatedLiveData
                .observe(this, (topRatedResponse) -> {
                    // TODO show movies in RecyclerView
                    Log.d("zzz", "topRatedResponse");
                });
    }

}
