package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.GraphActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Implementation of App Widget functionality.
 */
public class StockWidget extends AppWidgetProvider {


    public static String CLICK_ACTION;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_widget);

        //to launch MyStocksActivity
        Intent intent = new Intent(context , MyStocksActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0,intent,0);      //may cause issues
        views.setOnClickPendingIntent(R.id.widget , pendingIntent);


        // Set up the collection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);
        } else {
            setRemoteAdapterV11(context, views);
        }

        //can write code for individual clicks and opening that particular stock graphs

// this intent handles the click on any stock in the widget
        final Intent onClickIntent = new Intent(context, StockWidget.class);
        onClickIntent.setAction(StockWidget.CLICK_ACTION);

        final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list, onClickPendingIntent);



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        CLICK_ACTION = context.getResources().getString(R.string.click_action);
        Log.d("StockWidget: "  , "onUpdate called !");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);




        CLICK_ACTION = context.getResources().getString(R.string.click_action);

        final String action = intent.getAction();

        if (action.equals(CLICK_ACTION)) {
            final String stock_symbol = intent.getStringExtra("symbol");

//            Log.d("Widget", stock_symbol);

            Intent graphsActivityIntent = new Intent(context, GraphActivity.class);
            graphsActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            graphsActivityIntent.putExtra("stockName", stock_symbol);
            context.startActivity(graphsActivityIntent);
        }
        super.onReceive(context, intent);

    }









    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list, new Intent(context, WidgetServiceClass.class));
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context, WidgetServiceClass.class));
    }

}

