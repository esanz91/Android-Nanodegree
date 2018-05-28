package com.esanz.nano.movies.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "movie";

    private ImageView backdropView;
    private ImageView posterView;

    // TODO store movies in Room and just pass in the id
    public static Intent createIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setSupportActionBar(findViewById(R.id.toolbar));

        posterView = findViewById(R.id.poster);
        backdropView = findViewById(R.id.backdrop);

        bindMovie(getIntent().getParcelableExtra(EXTRA_MOVIE));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindMovie(Movie movie) {
        getSupportActionBar().setTitle(movie.title);
        Picasso.with(this)
                .load(movie.getBackdropUrlString())
                .into(backdropView);
        Picasso.with(this)
                .load(movie.getPosterUrlString())
                .into(posterView);
    }

}
