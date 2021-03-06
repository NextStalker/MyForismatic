package com.next.myforismatic.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.next.myforismatic.ForismaticApplication;
import com.next.myforismatic.api.ForismaticService;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.providers.QuoteContentProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import timber.log.Timber;

/**
 * Created by maslparu on 18.04.2016.
 */
public class ForismaticIntentService extends IntentService {

    @Inject
    ForismaticService forismaticService;

    public static boolean isLoading;

    //private ForismaticService forismaticService;

    public ForismaticIntentService() {
        super(ForismaticIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ForismaticApplication.getComponent().inject(this);
        //forismaticService = ((ForismaticApplication) getApplicationContext()).getForismaticService();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        isLoading = true;
        Timber.d("Start");

        int size = intent.getIntExtra("size", 0);

        List<Quote> quotes = getQuotes(size);

        for (Quote quote : quotes) {
            ContentValues contentValues = quote.getContentValues();
            getApplicationContext().getContentResolver().insert(QuoteContentProvider.QUOTE_CONTENT_URI, contentValues);
        }

        if (quotes.size() == size) {
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(
                            new Intent("endDownload").putExtra("message", "finish")
                    );
        }
        isLoading = false;
    }

    @NonNull
    private List<Quote> getQuotes(int size) {

        Cursor cursor = getApplicationContext().getContentResolver().query(QuoteContentProvider.QUOTE_CONTENT_URI, new String[] {QuoteContentProvider.QUOTE_ID}, null, null, null);

        int count = cursor.getCount();

        cursor.close();

        List<Quote> quotes = new ArrayList<>(size);
        try {
            for (int i = count; i < size + count; i++) {
                Call<Quote> callQ = getQuote(i);
                Quote quote = callQ.execute().body();
                quotes.add(quote);
                Timber.d("quote# %d", i);
            }
        } catch (IOException ignored) {
            //not supported
        }
        return quotes;
    }

    public Call<Quote> getQuote(int key) {
        return forismaticService.getQuote("getQuote", "json", "ru", key);
    }

    @Override
    public void onDestroy() {
        isLoading = false;
        super.onDestroy();
    }

}
