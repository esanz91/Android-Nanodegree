package com.esanz.nano.ezbaking.components;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class SimpleRecyclerAdapter<T> extends RecyclerView.Adapter<SimpleViewHolder> {

    private List<T> items;

    @LayoutRes
    int layout;

    public SimpleRecyclerAdapter(@LayoutRes final int layout) {
        this.layout = layout;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new SimpleViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return null != items ? items.size() : 0;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(final List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public T getItem(final int position) {
        return null != items && position < items.size() && position >= 0 ? items.get(position) : null;
    }

}
