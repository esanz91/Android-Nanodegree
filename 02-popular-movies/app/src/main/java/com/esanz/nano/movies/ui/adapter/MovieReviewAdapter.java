package com.esanz.nano.movies.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esanz.nano.movies.R;
import com.esanz.nano.movies.repository.model.MovieReview;
import com.esanz.nano.movies.utils.MovieStringUtils;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    private List<MovieReview> reviews;

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);
        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        MovieReview review = reviews.get(position);
        if (null != review) {
            holder.<TextView>get(R.id.author).setText(review.author + ":");
            holder.<TextView>get(R.id.content).setText(review.content);
            String link = holder.itemView.getContext().getString(R.string.action_read_more, review.url);
            Spannable spannable = MovieStringUtils.removeUrlUnderline((Spannable) Html.fromHtml(link));
            holder.<TextView>get(R.id.read_more).setText(spannable);
            holder.<TextView>get(R.id.read_more).setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public int getItemCount() {
        return null != reviews ? reviews.size() : 0;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }
}
