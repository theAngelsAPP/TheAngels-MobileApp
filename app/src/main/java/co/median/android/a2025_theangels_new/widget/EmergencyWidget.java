package co.median.android.a2025_theangels_new.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import co.median.android.a2025_theangels_new.R;

public class EmergencyWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_emergency);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
