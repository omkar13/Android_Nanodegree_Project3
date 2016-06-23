package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteDatabase;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omkar on 22/6/16.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory{

//    List<String> collection = new ArrayList<>();
    private Context context;
    private Intent intent;

    private Cursor cursor = null;



    private static final String[] QUOTE_COLUMNS = {
            QuoteDatabase.QUOTES + "." + QuoteColumns._ID,
            QuoteColumns.SYMBOL,
            QuoteColumns.BIDPRICE,
            QuoteColumns.PERCENT_CHANGE,
            QuoteColumns.CHANGE,
            QuoteColumns.ISUP
    };

    static final int INDEX_STOCK_ID = 0;
    static final int INDEX_SYMBOL = 1;
    static final int INDEX_BIDPRICE = 2;
    static final int INDEX_PERCENT_CHANGE = 3;
    static final int INDEX_CHANGE = 4;
    static final int INDEX_ISUP = 5;




    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    private void initData(){

//        collection.clear();
//        for(int i=1;i<=10;i++){
//            collection.add("Listview item" + i);
//        }

    }

    @Override
    public void onCreate() {

//        initData();
    }

    @Override
    public void onDataSetChanged() {

        if(cursor!=null)
        {
            cursor.close();
        }

        final long identityToken= Binder.clearCallingIdentity();
        cursor=context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,QUOTE_COLUMNS,QuoteColumns.ISCURRENT
                + "=?",new String[] {"1"},null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if(cursor!=null)
        {
            cursor.close();
            cursor=null;
        }
    }

    @Override
    public int getCount() {
//        return collection.size();
        return cursor==null ? 0 : cursor.getCount();
    }


    //generates view for list item
    @Override
    public RemoteViews getViewAt(int position) {


        if(position== AdapterView.INVALID_POSITION || cursor==null || !cursor.moveToPosition(position))
        {
            return null;
        }

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        String symbol  = cursor.getString(INDEX_SYMBOL);
        double bidPrice = cursor.getDouble(INDEX_BIDPRICE);
        double percentChange = cursor.getDouble(INDEX_PERCENT_CHANGE);


        remoteView.setTextViewText(R.id.widget_stock_symbol , symbol);
        remoteView.setTextViewText(R.id.widget_stock_price , Double.toString(bidPrice));
        remoteView.setTextViewText(R.id.widget_stock_change , Double.toString(percentChange) + "%");


        // implement remoteViews.setOnClickFillInIntent(,)

        final Intent fillInIntent = new Intent();

        final Bundle extras = new Bundle();
        extras.putString("symbol", symbol);

        fillInIntent.putExtras(extras);
        remoteView.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);


        return remoteView;


    }

    @Override
    public RemoteViews getLoadingView() {
//        return null;
        return new RemoteViews(context.getPackageName(),R.layout.widget_layout);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if(cursor.moveToPosition(position))
            return cursor.getInt(INDEX_STOCK_ID);

        return position;
    }


    //check !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override
    public boolean hasStableIds() {
        return true;
    }
}
