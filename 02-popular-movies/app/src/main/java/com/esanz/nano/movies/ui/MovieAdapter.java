package com.esanz.nano.movies.ui;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.model.Movie;
import com.esanz.nano.movies.ui.widget.CheckableFloatingActionButton;
import com.esanz.nano.movies.ui.widget.SimpleViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    public interface OnMovieClickListener {
        void onMoviePosterClick(Movie movie);

        void onFavoriteClick(Movie movie, boolean isFavorite);
    }

    @LayoutRes
    private final int layout = R.layout.list_item_poster;

    private List<Movie> movies;
    private OnMovieClickListener listener;

    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        SimpleViewHolder view = new SimpleViewHolder(itemView);

        view.onItemClick((v, position) -> listener.onMoviePosterClick(movies.get(position)));
        view.onItemClick(R.id.action, (v, position) -> {
            Movie movie = movies.get(position);
            listener.onFavoriteClick(movie, ((CheckableFloatingActionButton) v).isChecked());
        });
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        Movie movie = movies.get(position);
        if (null != movie) {
            ImageView imageView = holder.get(R.id.poster);
            Picasso.with(imageView.getContext())
                    .load(movie.getPosterUrlString())
                    .into(imageView);
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
