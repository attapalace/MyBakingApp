package com.examples.apps.atta.mybakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.examples.apps.atta.mybakingapp.MainActivity;
import com.examples.apps.atta.mybakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for(int i = 0; i < appWidgetIds.length; i ++) {
            Intent intent = new Intent(context, RecipeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            // Add a random integer to stop the Intent being ignored.
            //  This is needed for some API levels due to intent caching
            intent.putExtra("Random", Math.random() * 1000);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_listview);
            rv.setRemoteAdapter( R.id.ingredients_widget_list_view, intent);

            Intent detailIntent = new Intent(context , MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context , 0 , detailIntent ,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.widget_ingredients_title, pendingIntent);

            rv.setEmptyView(R.id.ingredients_widget_list_view,R.id.empty_text_view);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.ingredients_widget_list_view);
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
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context , RecipeWidgetProvider.class));
        onUpdate(context,appWidgetManager,appWidgetIds);
    }
}

