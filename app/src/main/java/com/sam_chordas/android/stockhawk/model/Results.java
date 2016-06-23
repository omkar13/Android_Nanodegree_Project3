package com.sam_chordas.android.stockhawk.model;

import com.sam_chordas.android.stockhawk.model.Quote;

import java.util.ArrayList;

/**
 * Created by omkar on 20/6/16.
 */
public class Results {

    private ArrayList<Quote> quote = new ArrayList<Quote>();

    public ArrayList<Quote> getQuote() {
        return quote;
    }

    public void setQuote(ArrayList<Quote> quote) {
        this.quote = quote;
    }
}
