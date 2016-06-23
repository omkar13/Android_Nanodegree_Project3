package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;

public class GraphActivity extends AppCompatActivity {


    private LineChartView mChart;
    private View graphView;

//    private final String[] mLabels= {"Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep"};
//    private final float[][] mValues = {{3.5f, 4.7f, 4.3f, 8f, 6.5f, 9.9f, 7f, 8.3f, 7.0f},
//            {4.5f, 2.5f, 2.5f, 9f, 4.5f, 9.5f, 5f, 8.3f, 1.8f}};

    private String baseUrl;

//    private final String baseUrl = "https://query.yahooapis.com/";
    private String q;
    private String TAG = "GraphActivity";
    private String stockName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        mChart = (LineChartView) findViewById(R.id.linechart);
        baseUrl = getResources().getString(R.string.baseURL);
        graphView = (View) findViewById(R.id.graph_layout);

        stockName = getIntent().getStringExtra("stockName");
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//// set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//// add your other interceptors …
//
//// add logging as last interceptor
//        httpClient.addInterceptor(logging);  // <-- this is the important line!
//
//
//
//
//        Retrofit retrofit = new Retrofit.Builder()
//                            .baseUrl(baseUrl)
//                            .addConverterFactory(GsonConverterFactory.create())
////                            .client(httpClient.build())
//                            .build();
//
//
//        Call<RequestModel> call;
//
//        final String q = "select * from yahoo.finance.historicaldata where symbol = \'YHOO\' and startDate = \'2009-09-11\' and endDate = \'2010-03-10\'";
//
//        HistoricalDataService.HistoricalDataAPI historicalDataAPI = retrofit.create(HistoricalDataService.HistoricalDataAPI.class);
//
////        call = historicalDataAPI.getData(q);
//        call = historicalDataAPI.getData();
//
//        call.enqueue(new Callback<RequestModel>() {
//            @Override
//            public void onResponse(Response<RequestModel> response) {
//
//                Log.d(TAG , "response received: " + response.toString());
//
//
//                Query query = response.body().getQuery();
//                Results results = query.getResults();
//
//                ArrayList<Quote> quotes = results.getQuote();
//
//                LineSet dataset = new LineSet();
//
//                Log.d(TAG , Integer.toString(quotes.size()));
//
//                for(int i=0;i<quotes.size();i++){
//                    Log.d(TAG, quotes.get(i).getClose());
////                    dataset.addPoint(quotes.get(i).getDate() , quotes.get(i).getHigh());
//                }
//
//
//
//                Paint paint = new Paint();
//                paint.setColor(Color.WHITE);
//
//                dataset.setDotsColor(Color.RED);
//                dataset.setColor(Color.GREEN);
//
//                mChart.dismiss();
//                mChart.addData(dataset);
//                mChart.setAxisBorderValues(0, 30);
//                mChart.setAxisColor(Color.WHITE);
//                mChart.setLabelsColor(Color.WHITE);
//                mChart.setStep(3);
//                mChart.setGrid(ChartView.GridType.FULL, paint);
//                mChart.show();
//
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e(TAG, t.getMessage());
//            }
//        });
//
        AsyncTaskCall asyncTaskCall = new AsyncTaskCall(graphView , mChart,stockName , getApplicationContext() , getResources().getString(R.string.graph_period_month));
        asyncTaskCall.execute(q);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.graph_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_period_year) {
            AsyncTaskCall asyncTaskCall = new AsyncTaskCall(graphView , mChart,stockName , getApplicationContext() , getResources().getString(R.string.graph_period_year));
            asyncTaskCall.execute(q);
            return true;
        }


        if (id == R.id.action_period_month) {
            AsyncTaskCall asyncTaskCall = new AsyncTaskCall(graphView ,mChart,stockName , getApplicationContext() , getResources().getString(R.string.graph_period_month));
            asyncTaskCall.execute(q);
            return true;
        }
        if (id == R.id.action_period_week) {
            AsyncTaskCall asyncTaskCall = new AsyncTaskCall(graphView ,mChart,stockName , getApplicationContext() , getResources().getString(R.string.graph_period_week));
            asyncTaskCall.execute(q);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
