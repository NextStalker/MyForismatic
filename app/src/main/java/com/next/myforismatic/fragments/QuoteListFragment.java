package com.next.myforismatic.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.next.myforismatic.R;
import com.next.myforismatic.adapters.QuoteListAdapter;
import com.next.myforismatic.common.CursorParse;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.providers.QuoteContentProvider;
import com.next.myforismatic.services.ForismaticIntentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Konstantin Abramov on 20.03.16.
 */
public class QuoteListFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private final int QUOTES_SIZE_FIRST_RUN = 10;
    private final int QUOTES_SIZE = 100;

    private RecyclerView recyclerView;
    private QuoteListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Subscription subscription;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("message").equals("finish")) {
                restartLoader();
                LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(messageReceiver);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quote_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_quote_list_rv);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new QuoteListAdapter(getFragmentManager(), getActivity());
        recyclerView.setAdapter(adapter);

        getActivity().setTitle(R.string.app_name);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.blue, R.color.green, R.color.yellow, R.color.red);

        swipeRefreshLayout.postDelayed(() -> {
            if (ForismaticIntentService.isLoading) {
                registerReceiver();
                swipeRefreshLayout.setRefreshing(true);
            }
        }, 500);

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        if (loaderManager.getLoader(R.id.quote_cursor_loader) == null) {
            retrieveQuotes();
        } else {
            restartLoader();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.load_more_item) {
            registerReceiverAndStartService();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    private void restartLoader() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getActivity().getSupportLoaderManager().restartLoader(R.id.quote_cursor_loader, null, this);
    }

    private void retrieveQuotes() {
        getActivity().getSupportLoaderManager().initLoader(R.id.quote_cursor_loader, null, this).forceLoad();
    }

    private Observable<List<Quote>> observeQuotesFromInternet() {
        return Observable.fromCallable(() -> {
            List<Quote> quotes = getQuotes();
            insertQuotes(quotes);
            return quotes;
        }).subscribeOn(Schedulers.io());
    }

    private void getQuotesFromInternet() {
        swipeRefreshLayout.setRefreshing(true);
        subscription = observeQuotesFromInternet()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> swipeRefreshLayout.setRefreshing(false))
                .subscribe(this::setQuotes);
    }

    private void getQuotesFromInternetOld() {
        new MyTask().execute();
    }

    private void registerReceiverAndStartService() {
        registerReceiver();
        getQuotesFromInternetService();
    }

    private void registerReceiver() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                messageReceiver,
                new IntentFilter("endDownload")
        );
    }

    private void getQuotesFromInternetService() {
        Activity activity = getActivity();
        Intent intent = new Intent(activity, ForismaticIntentService.class).putExtra("size", QUOTES_SIZE);
        activity.startService(intent);
    }

    private Call<Quote> getQuote(int key) {
        return getForismaticService().getQuote("getQuote", "json", "ru", key);
    }

    @NonNull
    private List<Quote> getQuotes() {
        int size = QUOTES_SIZE_FIRST_RUN;
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

    private void insertQuotes(@NonNull List<Quote> quotes) {
        for (Quote quote : quotes) {
            ContentValues contentValues = quote.getContentValues();

            getContext().getContentResolver()
                    .insert(QuoteContentProvider.QUOTE_CONTENT_URI, contentValues);
        }
    }

    private void setQuotes(@NonNull List<Quote> quotes) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setQuotes(quotes);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == R.id.quote_cursor_loader) {
            return new MyCursorLoader(getContext(), QuoteContentProvider.QUOTE_CONTENT_URI);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Quote> quotes = CursorParse.parseQuotes(data);

        if (quotes.size() == 0) {
            getQuotesFromInternet();
        } else {
            setQuotes(quotes);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        registerReceiverAndStartService();
    }

    private class MyTask extends AsyncTask<Void, Void, List<Quote>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<Quote> doInBackground(Void... params) {
            List<Quote> quotes = getQuotes();
            insertQuotes(quotes);
            return quotes;
        }

        @Override
        protected void onPostExecute(List<Quote> quotes) {
            if (isAdded()) {
                setQuotes(quotes);
            }
        }
    }

    private static class MyCursorLoader extends CursorLoader {

        private Uri uri;

        public MyCursorLoader(Context context, Uri uri) {
            super(context);
            this.uri = uri;
        }

        @Override
        public Cursor loadInBackground() {
            return getContext().getContentResolver().query(
                    uri, null, null, null,
                    QuoteContentProvider.QUOTE_ID
            );
        }

    }

}
