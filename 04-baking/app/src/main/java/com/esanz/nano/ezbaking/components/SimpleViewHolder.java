package com.esanz.nano.ezbaking.components;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class SimpleViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mCachedViews = new SparseArray<>();

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public SimpleViewHolder(View itemView) {
        super(itemView);
    }

    public <V extends View> V get(int resId) {
        View view = mCachedViews.get(resId);
        if (null == view) {
            view = itemView.findViewById(resId);
            mCachedViews.put(resId, view);

        }
        return (V) view;
    }

    public void onItemClick(@IdRes int resId, OnItemClickListener listener) {
        onItemClick(get(resId), listener);
    }

    public void onItemClick(OnItemClickListener listener) {
        onItemClick(itemView, listener);
    }

    public void onItemClick(View view, OnItemClickListener listener) {
        view.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(v, position);
            }
        });
    }

}
