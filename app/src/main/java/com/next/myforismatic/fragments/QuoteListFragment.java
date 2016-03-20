package com.next.myforismatic.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.next.myforismatic.R;
import com.next.myforismatic.adapters.QuoteListAdapter;
import com.next.myforismatic.models.Quote;

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
public class QuoteListFragment extends Fragment {

    private MyTask myTask;
    private ForismaticService service;

    private RecyclerView recyclerView;
    private QuoteListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRetrofit();
        initializeData();
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
        adapter = new QuoteListAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initializeData() {
        myTask = new MyTask();
        myTask.execute();
    }

    private void initRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClientBuilder.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.forismatic.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        service = retrofit.create(ForismaticService.class);
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

    class MyTask extends AsyncTask<Void, Void, List<Quote>> {

        @Override
        protected List<Quote> doInBackground(Void... params) {
            List<Quote> quotes = getQuotes();
            //insert to DB
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

}
