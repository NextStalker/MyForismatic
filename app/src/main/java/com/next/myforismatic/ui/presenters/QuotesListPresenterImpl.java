package com.next.myforismatic.ui.presenters;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.next.myforismatic.R;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.providers.QuoteContentProvider;
import com.next.myforismatic.services.ForismaticIntentService;
import com.next.myforismatic.ui.common.QuoteListAdapter;
import com.next.myforismatic.ui.contracts.QuoteListContract;
import com.next.myforismatic.ui.quoteslist.QuoteListFragment;
import com.next.myforismatic.utils.CursorParse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by maslparu on 21.07.2016.
 */
public class QuotesListPresenterImpl implements QuoteListContract.Presenter, SwipeRefreshLayout.OnRefreshListener {

    private final int QUOTES_SIZE = 10;

    private Subscription subscription;
    private SwipeRefreshLayout swipeRefreshLayout;
    private QuoteListAdapter adapter;
    private RecyclerView recyclerView;

    private QuoteListFragment fragment;
    private QuoteListContract.QuoteListView view;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("message").equals("finish")) {
                restartLoader();
                LocalBroadcastManager.getInstance(fragment.getContext()).unregisterReceiver(messageReceiver);
            }
        }
    };

    @Override
    public void viewCreated(QuoteListContract.QuoteListView view) {
        fragment = (QuoteListFragment)view;
        this.view = view;

        recyclerView = (RecyclerView) fragment.getActivity().findViewById(R.id.fragment_quote_list_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getActivity()));
        adapter = new QuoteListAdapter();
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) fragment.getActivity().findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.blue, R.color.green, R.color.yellow, R.color.red);

        swipeRefreshLayout.postDelayed(() -> {
            if (ForismaticIntentService.isLoading) {
                registerReceiver();
                swipeRefreshLayout.setRefreshing(true);
            }
        }, 500);

        getQuotes();
    }

    @Override
    public void loadData() {
        registerReceiverAndStartService();
    }

    @Override
    public void viewDestroyed() {
        if (subscription != null) {
            subscription.unsubscribe();
        }

        LocalBroadcastManager.getInstance(fragment.getContext()).unregisterReceiver(messageReceiver);
    }

    private void restartLoader() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        //getQuotesInternet();
        getQuotes();
    }

    private void registerReceiverAndStartService() {
        registerReceiver();
        //getQuotesFromInternetService();
        getQuotes();
    }
/*
    private void getQuotesFromInternetService() {
        Activity activity = fragment.getActivity();
        Intent intent = new Intent(activity, ForismaticIntentService.class).putExtra("size", QUOTES_SIZE);
        activity.startService(intent);
    }
*/
    private void registerReceiver() {
        LocalBroadcastManager.getInstance(fragment.getContext()).registerReceiver(
                messageReceiver,
                new IntentFilter("endDownload")
        );
    }

    private void getQuotes() {
        if (adapter.getItemCount() == 0) {
            getQuotesDb();
        } else {
            getQuotesInternet();
        }

        if (adapter.getItemCount() == 0) {
            getQuotesInternet();
        }
    }

    private void getQuotesInternet() {

        subscription = observeQuotesFromInternet()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> swipeRefreshLayout.setRefreshing(false))
                .subscribe(this::setData);
    }

    private Observable<List<Quote>> observeQuotesFromInternet() {

        return Observable.fromCallable(() -> {
            List<Quote> quotes = getQuotesFromInternet();
            insertQuotes(quotes);
            return quotes;
        }).subscribeOn(Schedulers.io());
    }

    @NonNull
    private List<Quote> getQuotesFromInternet() {
        int size = QUOTES_SIZE;
        List<Quote> quotes = new ArrayList<>(size);
        try {
            for (int i = 0; i < size; i++) {
                Call<Quote> callQ = getQuote(i);
                Quote quote = callQ.execute().body();
                quotes.add(quote);
            }
        } catch (IOException ignored) {
            //not supported
        }
        return quotes;
    }

    private void getQuotesDb() {

        subscription = observeQuotesFromDb()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> swipeRefreshLayout.setRefreshing(false))
                .subscribe(this::setData);
    }

    private Observable<List<Quote>> observeQuotesFromDb() {
        return Observable.fromCallable(() -> {
            List<Quote> quotes = getQuotesFromDb();
            return quotes;
        }).subscribeOn(Schedulers.io());
    }

    @NonNull
    private List<Quote> getQuotesFromDb() {
        return CursorParse.parseQuotes(
                fragment.getContext().getContentResolver().query(
                        QuoteContentProvider.QUOTE_CONTENT_URI, null, null, null,
                        QuoteContentProvider.QUOTE_ID));
    }

    private Call<Quote> getQuote(int key) {
        return fragment.getForismaticService().getQuote("getQuote", "json", "ru", key);
    }

    private void insertQuotes(@NonNull List<Quote> quotes) {
        for (Quote quote : quotes) {
            ContentValues contentValues = quote.getContentValues();

            fragment.getContext().getContentResolver()
                    .insert(QuoteContentProvider.QUOTE_CONTENT_URI, contentValues);
        }
    }

    private void setData(List<Quote> quotes) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setQuotes(quotes);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        registerReceiverAndStartService();
    }

}
