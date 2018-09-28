package com.esanz.nano.ezbaking.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.components.SimpleViewHolder;
import com.esanz.nano.ezbaking.respository.RecipeDetail;
import com.esanz.nano.ezbaking.respository.model.Ingredient;
import com.esanz.nano.ezbaking.respository.model.SectionHeader;
import com.esanz.nano.ezbaking.respository.model.Step;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    public interface OnStepClickListener {
        void onStepClick(final Step step);
    }

    private List<RecipeDetail> details;
    private OnStepClickListener listener;

    public DetailsAdapter(OnStepClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = -1;

        switch (viewType) {
            case RecipeDetail.TYPE_HEADER:
                layout = R.layout.list_item_header;
                break;
            case RecipeDetail.TYPE_INGREDIENT:
                layout = R.layout.list_item_ingredient;
                break;
            case RecipeDetail.TYPE_STEP:
                layout = R.layout.list_item_step;
                break;
        }

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        SimpleViewHolder holder = new SimpleViewHolder(itemView);

        if (RecipeDetail.TYPE_STEP == viewType) {
            holder.onItemClick(((view, position) -> {
                listener.onStepClick((Step) details.get(position));
            }));
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        RecipeDetail detail = details.get(position);

        switch (holder.getItemViewType()) {
            case RecipeDetail.TYPE_HEADER:
                SectionHeader header = (SectionHeader) detail;
                holder.<TextView>get(R.id.header).setText(header.name);
                break;
            case RecipeDetail.TYPE_INGREDIENT:
                Ingredient ingredient = (Ingredient) detail;
                holder.<TextView>get(R.id.ingredient).setText(ingredient.name);
                holder.<TextView>get(R.id.label).setText(ingredient.getLabel());
                break;
            case RecipeDetail.TYPE_STEP:
                Step step = (Step) detail;
                holder.<TextView>get(R.id.step).setText(step.shortDescription);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return null != details ? details.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return details.get(position).getType();
    }

    public void setDetails(final List<RecipeDetail> details) {
        this.details = details;
        notifyDataSetChanged();
    }

}
