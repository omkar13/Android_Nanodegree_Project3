package com.sam_chordas.android.stockhawk.service;

/**
 * Created by omkar on 20/6/16.
 */

import com.sam_chordas.android.stockhawk.model.RequestModel;

import retrofit2.Call;
import retrofit2.http.GET;

public class HistoricalDataService {

    public interface HistoricalDataAPI{

//        @GET("v1/public/yql?format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")

        @GET("v1/public/yql?q=select * from yahoo.finance.historicaldata where symbol = 'YHOO' and startDate = '2009-09-11' and endDate = '2010-03-10'&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")

        Call<RequestModel> getData(
           // @Query("q") String q
        );
    }

}

