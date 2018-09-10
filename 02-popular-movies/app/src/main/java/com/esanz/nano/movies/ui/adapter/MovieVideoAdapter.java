package com.esanz.nano.movies.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.model.MovieVideo;

import java.util.List;

public class MovieVideoAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    private List<MovieVideo> videos;

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_video, parent, false);
        SimpleViewHolder holder = new SimpleViewHolder(itemView);
        holder.onItemClick((v, position) -> {
            MovieVideo video = videos.get(position);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getYouTubeLink()));
            parent.getContext().startActivity(webIntent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        MovieVideo video = videos.get(position);
        holder.<TextView>get(R.id.title).setText(video.name);
    }

    @Override
    public int getItemCount() {
        return null != videos ? videos.size() : 0;
    }

    public void setVideos(List<MovieVideo> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

}
