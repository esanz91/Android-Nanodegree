package com.esanz.nano.ezbaking.ui.adapter;

import android.content.Context;
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
import com.esanz.nano.ezbaking.service.RecipeWidgetIntentService;
import com.esanz.nano.ezbaking.ui.RecipeDetailActivity;
import com.esanz.nano.ezbaking.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    private List<Recipe> recipes;

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.list_item_recipe, parent, false);
        SimpleViewHolder holder = new SimpleViewHolder(itemView);
        holder.onItemClick((v, position) -> {
            Recipe recipe = recipes.get(position);
            PreferenceUtils.storeLastSeenRecipe(context, recipe.id);
            RecipeWidgetIntentService.startActionUpdateWidgetRecipe(context);
            context.startActivity(RecipeDetailActivity.createIntent(context, recipe));
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.<TextView>get(R.id.name).setText(recipe.name);

        ImageView imageView = holder.get(R.id.image);
        Picasso.with(imageView.getContext())
                .load(!TextUtils.isEmpty(recipe.image) ? recipe.image : null)
                .placeholder(R.drawable.recipe_placeholder)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return null != recipes ? recipes.size() : 0;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

}
