package com.esanz.nano.ezbaking.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.esanz.nano.ezbaking.EzBakingApplication;
import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.provider.RecipeWidgetProvider;
import com.esanz.nano.ezbaking.utils.PreferenceUtils;

import timber.log.Timber;

public class RecipeWidgetIntentService extends IntentService {

    public static final String TAG = RecipeWidgetIntentService.class.getSimpleName();
    public static final String ACTION_UPDATE_WIDGET_RECIPE = "com.esanz.nano.ezbaking.action.update_widget_recipe";

    public RecipeWidgetIntentService() {
        super(TAG);
    }

    public static void startActionUpdateWidgetRecipe(Context context) {
        Intent intent = new Intent(context, RecipeWidgetIntentService.class);
        intent.setAction(ACTION_UPDATE_WIDGET_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (null != intent) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET_RECIPE.equals(action)) {
                handleActionUpdateWidgetRecipe();
            }
        }
    }

    private void handleActionUpdateWidgetRecipe() {
        int recipeId = PreferenceUtils.getLastSeenReciper(getApplicationContext());
        if (recipeId == -1) {
            Timber.d(TAG, "no recipe");
            return;
        }

        EzBakingApplication.RECIPE_REPOSITORY.getRecipe(recipeId)
                .subscribe(recipe -> {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
                    RecipeWidgetProvider.updateWidgetRecipe(this, appWidgetManager, appWidgetIds, recipe);
                });
    }

}
