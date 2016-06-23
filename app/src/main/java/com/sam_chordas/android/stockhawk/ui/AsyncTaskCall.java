package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by omkar on 21/6/16.
 */
public class AsyncTaskCall extends AsyncTask<String , Void , LineSet>{


    private String TAG = "AsyncTaskCall";
//    private final String baseUrl = "https://query.yahooapis.com/";
    private String stockSymbol;

    private float min,max,avg;
//    private String url="https://query.yahooapis.com/v1/public/yql";
    private String url;
    private String Search="format";
    private String SearchVal="json";
    private String QueryKey="q";
    private String Diag ="diagnostics";
    private String DiagVal="true";
    private String Env="env";
    private String EnvVal="store://datatables.org/alltableswithkeys";
    private String Call="callback";
    private String CallVal="";
    private Uri buildUri;

    private LineChartView mChart;
    private View graphView;
    private Context context;

    private String period;

    public AsyncTaskCall(View graphView , LineChartView chartView , String stockSymbol , Context context  ,String period){
        mChart = chartView;
        this.stockSymbol = stockSymbol;
        this.context = context;
        url = context.getString(R.string.baseYQLURL);
        this.period = period;
        this.graphView = graphView;
    }


    @Override
    protected LineSet doInBackground(String... arr){

        InputStream is = null;
        LineSet lineSet = null;

        try {
//            URL url = new URL("https://query.yahooapis.com/v1/public/yql?q=select * from yahoo.finance.historicaldata where symbol = \'YHOO\' and startDate = \'2009-09-11\' and endDate = \'2010-03-10\'&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=");



            Calendar cal = Calendar.getInstance();
            Date currentDate = cal.getTime();

            if(period.equals("year"))
                cal.add(Calendar.YEAR , -1);
            else if(period.equals("month"))
                cal.add(Calendar.MONTH , -1);
            else if(period.equals("week"))
                cal.add(Calendar.DAY_OF_YEAR , -7);

            Date monthEarlierDate = cal.getTime();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String currentDateString = dateFormat.format(currentDate);
            String monthEarlierDateString = dateFormat.format(monthEarlierDate);

//            String query="Select * from yahoo.finance.historicaldata where symbol ='" + stockSymbol + "' and startDate = '2016-01-01' and endDate = '2016-01-25'";
            String query="Select * from yahoo.finance.historicaldata where symbol ='" + stockSymbol + "' and startDate = '" + monthEarlierDateString + "' and endDate = '" + currentDateString + "'";

            buildUri=Uri.parse(url).buildUpon()
                    .appendQueryParameter(QueryKey,query)
                    .appendQueryParameter(Search,SearchVal)
                    .appendQueryParameter(Diag,DiagVal)
                    .appendQueryParameter(Env,EnvVal)
                    .appendQueryParameter(Call,CallVal)
                    .build();



            URL url = new URL(buildUri.toString());


            Log.i(TAG , "the url used : " + buildUri.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();

            Log.d(TAG, "the response of connection is : " + response);

            is = conn.getInputStream();

            //content has been converted to string
            String contentAsString = convertInputToString(is);

            //now let us parse the json

            if(contentAsString != null){
                lineSet = parseJSON(contentAsString);
            }
            else
            {
                Log.e(TAG , "json string null");
                return null;
            }
            conn.disconnect();
        }



        catch (MalformedURLException e){
            Log.d(TAG , "MalformedURLException" + e.getMessage());
        }
        catch (IOException e){
            Log.d(TAG ,"IOException" +  e.getMessage());
        }

        catch (JSONException e){
            Log.d(TAG ,"JSONException" +  e.getMessage());
        }


        return lineSet;
    }

    @Override
    protected void onPostExecute(LineSet dataset){


        Log.i(TAG , "max: " + Float.toString(max));
        Log.i(TAG , "min:"  + Float.toString(min));

        int stepSize = (int)(Math.floor(max-min)/10.0);

        Log.i(TAG , "stepSize : "  + Integer.toString(stepSize));

        if(stepSize == 0)
            stepSize = 1;

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        dataset.setDotsColor(Color.RED);
        dataset.setColor(Color.GREEN);

        mChart.dismiss();
        mChart.addData(dataset);
        mChart.setAxisBorderValues((int) Math.floor(min), (int) Math.ceil(max));
        mChart.setAxisColor(Color.WHITE);
        mChart.setLabelsColor(Color.WHITE);
        mChart.setStep(stepSize);
//        mChart.setStep(1);
        mChart.setGrid(ChartView.GridType.FULL, paint);
        mChart.show();


        //update the min,max average and stock symbol fields.

        TextView graphHeadingView = (TextView)graphView.findViewById(R.id.graph_heading);

        if(period.equals(context.getString(R.string.graph_period_year)))
            graphHeadingView.setText(stockSymbol + " : Last one year performance");
        else if(period.equals(context.getString(R.string.graph_period_month)))
            graphHeadingView.setText(stockSymbol + " : Last one month performance");
        else if(period.equals(context.getString(R.string.graph_period_week)))
            graphHeadingView.setText(stockSymbol + " : Last one week performance");


        TextView minPriceView = (TextView)graphView.findViewById(R.id.graph_stock_min);
        minPriceView.setText("Min : " + min + "$");

        TextView maxPriceView = (TextView)graphView.findViewById(R.id.graph_stock_max);
        maxPriceView.setText("Max : " + max + "$");

        TextView avgPriceView = (TextView)graphView.findViewById(R.id.graph_stock_avg);
        avgPriceView.setText("Avg : " + avg + "$");

    }


    private String convertInputToString(InputStream inputStream) throws IOException{
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(inputStream));        //UTF-8 req ??

        StringBuffer buffer = new StringBuffer();
        String line;

        while((line = reader.readLine())!=null){
            // new line doesn't affect json parsing. It will make debugging easy. good practice.
            buffer.append(line + "\n");
        }

        String s = buffer.toString();

        return s;


    }

    private LineSet parseJSON(String jsonString) throws JSONException{

        float stockPrice ;
        JSONObject quote;
        String date;

        Log.d(TAG , "in parseJSON function");
        Log.d(TAG , jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray quotes = jsonObject.getJSONObject("query").getJSONObject("results").getJSONArray("quote");


        LineSet dataset = new LineSet();

        float sum = 0;

        for(int i=quotes.length() - 1 ;i>=0;i--){
            quote = quotes.getJSONObject(i);
            date = quotes.getJSONObject(i).getString("Date");
            stockPrice = (float)quote.getDouble("Close");

            if(i==quotes.length()-1){
                min = max = stockPrice;
            }
            else{
                if(stockPrice>max){
                    max = stockPrice;
                }
                else if(stockPrice<min)
                    min = stockPrice;
            }

            Log.d(TAG, date + " " + stockPrice);
//            dataset.addPoint(date.substring(8), stockPrice);

            dataset.addPoint("", stockPrice);
            sum = sum + stockPrice;
        }

        avg = sum/quotes.length();

        return dataset;
    }




}
