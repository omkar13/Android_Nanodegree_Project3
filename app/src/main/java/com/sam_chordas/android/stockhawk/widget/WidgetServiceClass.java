package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.widget.WidgetDataProvider;

/**
 * Created by omkar on 22/6/16.
 */
public class WidgetServiceClass extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        //return remote view factory

        return new WidgetDataProvider(this , intent);
    }
}
