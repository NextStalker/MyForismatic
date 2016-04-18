package com.next.myforismatic.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.next.myforismatic.api.ForismaticApplication;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.providers.QuoteContentProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by maslparu on 18.04.2016.
 */
public class ForismaticIntentService extends IntentService {

    final String LOG_TAG = "myLogs";

    public ForismaticIntentService() {
        super("myName");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG_TAG, "Start");

        int size = intent.getIntExtra("size", 0);

        List<Quote> quotes = getQuotes(size);

        for (Quote quote : quotes) {
            ContentValues contentValues = quote.getContentValues();

            getApplicationContext().getContentResolver().insert(QuoteContentProvider.QUOTE_CONTENT_URI, contentValues);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("endDownload").putExtra("message", "finish"));
        Log.d(LOG_TAG, "onDestroy");
    }

    @NonNull
    private List<Quote> getQuotes(int size) {
        List<Quote> quotes = new ArrayList<>(size);
        try {
            for (int i = 0; i < size; i++) {
                Call<Quote> callQ = getQuote(i);
                Quote quote = callQ.execute().body();
                quotes.add(quote);
                Log.d(LOG_TAG, "quote#" + i);
            }
        } catch (IOException ignored) {
            //not supported
        }
        return quotes;
    }

    public Call<Quote> getQuote(int key) {
        return ((ForismaticApplication)getApplicationContext()).getForismaticService().getQuote("getQuote", "json", "ru", key);
    }
}
