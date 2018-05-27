package com.esanz.nano.movies.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie, Pair<View, String> transition);
    }

    @LayoutRes
    private final int layout = R.layout.list_item_poster;

    private List<Movie> movies;
    private OnMovieClickListener listener;

    public class MovieViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private View itemView;
        private ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.imageView = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        public void bindMovie(Movie movie) {
            Picasso.with(imageView.getContext())
                    .load(movie.getPosterUrlString())
                    .into(imageView);
        }


        @Override
        public void onClick(View v) {
            Movie movie = movies.get(getAdapterPosition());
            Pair<View, String> transition = new Pair<>(imageView, "movie_poster");
            listener.onMovieClick(movie, transition);
        }
    }

    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        if (null != movie) {
            holder.bindMovie(movie);
        }
    }

    @Override
    public int getItemCount() {
        return null != movies ? movies.size() : 0;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

}
