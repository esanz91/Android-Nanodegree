package com.esanz.nano.ezbaking.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.esanz.nano.ezbaking.R;
import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.service.ListWidgetService;
import com.esanz.nano.ezbaking.ui.MainActivity;
import com.esanz.nano.ezbaking.ui.RecipeDetailActivity;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId, Recipe recipe) {

        RemoteViews views = getRecipeRemoteViews(context, recipe);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getRecipeRemoteViews(@NonNull final Context context, final Recipe recipe) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ez_baking_widget);
        CharSequence widgetText = null == recipe ? context.getString(R.string.widget_title) : recipe.name;
        views.setTextViewText(R.id.widget_title, widgetText);

        Intent browseIntent = new Intent(context, MainActivity.class);
        if (null != recipe) {
            browseIntent = RecipeDetailActivity.createIntent(context, recipe);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, browseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_title, pendingIntent);
        views.setOnClickPendingIntent(R.id.empty_msg, pendingIntent);

        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        views.setEmptyView(R.id.widget_list_view, R.id.empty_msg);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null);
        }
    }

    public static void updateWidgetRecipe(Context context,
                                          AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds,
                                          Recipe recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

