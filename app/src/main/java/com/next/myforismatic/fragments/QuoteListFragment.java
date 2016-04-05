package com.next.myforismatic.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.next.myforismatic.R;
import com.next.myforismatic.adapters.QuoteListAdapter;
import com.next.myforismatic.common.CursorParse;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.providers.QuoteContentProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Konstantin Abramov on 20.03.16.
 */
public class QuoteListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ForismaticService service;

    private RecyclerView recyclerView;
    private QuoteListAdapter adapter;

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
        adapter = new QuoteListAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getQuotes();
    }

    private void getQuotes() {
        getActivity().getSupportLoaderManager().initLoader(R.id.quote_cursor_loader, null, this);
        getActivity().getSupportLoaderManager().getLoader(R.id.quote_cursor_loader).forceLoad();
    }

    private void getQuotesFromInternet() {
        new MyTask().execute();
    }

    private void initRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.forismatic.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        service = retrofit.create(ForismaticService.class);
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
        List<Quote> list = CursorParse.parseQuotes(data);

        if (list.size() == 0) {
            initRetrofit();
            getQuotesFromInternet();
        } else {
            adapter.setQuotes(list);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    public interface ForismaticService {

        @GET("api/1.0/")
        Call<Quote> getQuote(
                @Query("method") String method,
                @Query("format") String format,
                @Query("lang") String lang,
                @Query("key") int key
        );

    }

    public Call<Quote> getQuote(int key) {
        return service.getQuote("getQuote", "json", "ru", key);
    }

    private class MyTask extends AsyncTask<Void, Void, List<Quote>> {

        @Override
        protected List<Quote> doInBackground(Void... params) {
            List<Quote> quotes = getQuotes();

            for (Quote quote : quotes) {
                ContentValues cv = new ContentValues();
                cv.put("text", quote.getText());
                cv.put("author", quote.getAuthor());
                cv.put("name", quote.getName());
                cv.put("senderLink", quote.getSenderLink());
                cv.put("quoteLink", quote.getQuoteLink());
                getContext().getContentResolver()
                        .insert(QuoteContentProvider.QUOTE_CONTENT_URI, cv);
            }

            return quotes;
        }

        @NonNull
        private List<Quote> getQuotes() {
            int size = 10;
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

        @Override
        protected void onPostExecute(List<Quote> quotes) {
            adapter.setQuotes(quotes);
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
            return getContext().getContentResolver().query(uri, null, null, null, null);
        }

    }

}
