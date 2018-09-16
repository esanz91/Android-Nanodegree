package com.esanz.nano.ezbaking.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    private final List<Recipe> recipes;

    public RecipesAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe, parent, false);
        SimpleViewHolder view = new SimpleViewHolder(itemView);
        // TODO set click listener on itemView
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.<TextView>get(R.id.name).setText(recipe.name);

        if (!TextUtils.isEmpty(recipe.image)) {
            Picasso.with(holder.itemView.getContext())
                    .load(recipe.image)
                    .into(holder.<ImageView>get(R.id.image));
        }
    }

    @Override
    public int getItemCount() {
        return null != recipes ? recipes.size() : 0;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

}
