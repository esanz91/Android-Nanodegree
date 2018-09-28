package com.esanz.nano.ezbaking.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.components.SimpleRecyclerAdapter;
import com.esanz.nano.ezbaking.components.SimpleViewHolder;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.service.RecipeWidgetIntentService;
import com.esanz.nano.ezbaking.ui.RecipeDetailActivity;
import com.esanz.nano.ezbaking.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

public class RecipesAdapter extends SimpleRecyclerAdapter<Recipe> {

    public RecipesAdapter() {
        super(R.layout.list_item_recipe);
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SimpleViewHolder holder = super.onCreateViewHolder(parent, viewType);
        final Context context = parent.getContext();

        holder.onItemClick((v, position) -> {
            Recipe recipe = getItem(position);
            PreferenceUtils.storeLastSeenRecipe(context, recipe.id);
            RecipeWidgetIntentService.startActionUpdateWidgetRecipe(context);
            context.startActivity(RecipeDetailActivity.createIntent(context, recipe));
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        Recipe recipe = getItem(position);

        holder.<TextView>get(R.id.name).setText(recipe.name);

        ImageView imageView = holder.get(R.id.image);
        Picasso.with(imageView.getContext())
                .load(!TextUtils.isEmpty(recipe.image) ? recipe.image : null)
                .placeholder(R.drawable.recipe_placeholder)
                .into(imageView);
    }

}
