package com.esanz.nano.ezbaking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.esanz.nano.ezbaking.respository.model.Recipe;
import com.esanz.nano.ezbaking.ui.MainActivity;
import com.esanz.nano.ezbaking.ui.RecipeDetailActivity;

public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ez_baking_widget);

        CharSequence widgetText = null == recipe ? context.getString(R.string.widget_title) : recipe.name;
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent intent = new Intent(context, MainActivity.class);
        if (null != recipe) {
            intent = RecipeDetailActivity.createIntent(context, recipe);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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

