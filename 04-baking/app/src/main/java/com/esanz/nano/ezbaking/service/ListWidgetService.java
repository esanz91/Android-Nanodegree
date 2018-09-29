package com.esanz.nano.ezbaking.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.esanz.nano.ezbaking.EzBakingApplication;
import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.model.Ingredient;
import com.esanz.nano.ezbaking.utils.PreferenceUtils;

import java.util.List;


public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private List<Ingredient> mIngredients;
        private Context mContext;

        public ListRemoteViewsFactory(@NonNull final Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            int recipeId = PreferenceUtils.getLastSeenReciper(mContext);
            EzBakingApplication.RECIPE_REPOSITORY.getRecipe(recipeId)
                    .subscribe(recipe -> mIngredients = recipe.ingredients);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return null != mIngredients ? mIngredients.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (mIngredients.size() == 0 || position >= mIngredients.size()) return null;

            Ingredient ingredient = mIngredients.get(position);
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_item_ingredient);
            views.setTextViewText(R.id.ingredient, ingredient.name);
            views.setTextViewText(R.id.label, ingredient.getLabel());

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
