// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.data.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import co.median.android.a2025_theangels_new.R;

// =======================================
// EmergencyWidget - Basic AppWidgetProvider to update emergency widget layout
// =======================================
public class EmergencyWidget extends AppWidgetProvider {

    // =======================================
    // onUpdate - Called when the widget needs to be updated
    // =======================================
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Inflate and apply the layout for the widget
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_emergency);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
