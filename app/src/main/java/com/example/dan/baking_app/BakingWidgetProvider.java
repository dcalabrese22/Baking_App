package com.example.dan.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.dan.baking_app.helpers.Constants;
import com.example.dan.baking_app.objects.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    String mRecipeName;
    ArrayList<Ingredient> mIngredients;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        Intent intent = new Intent(context, WidgetAdapterService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Construct the RemoteViews object
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        rv.setRemoteAdapter(R.id.widget_listview, intent);
        rv.setEmptyView(R.id.widget_listview, R.id.widget_empty);

        Intent openDetail = new Intent(context, RecipeDetailActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openDetail, 0);
        rv.setPendingIntentTemplate(R.id.widget_listview, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), BakingWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
        if (intent.getAction().equals(Constants.UPDATE_MY_WIDGET) ||
                intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onReceive(context, intent);

    }


}
