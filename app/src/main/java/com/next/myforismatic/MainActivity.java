package com.next.myforismatic;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.next.myforismatic.adapters.QuoteListAdapter;
import com.next.myforismatic.models.Quote;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    private Context context;
    MyTask myTask;
    private Quote curQuote;
    private ForismaticService service;

    private List<Quote> quotes;

    private RecyclerView recyclerView;

    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRetrofit();
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.activity_main_root);

        context = this;

        initializeData();

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initializeData(){
        quotes = new ArrayList<>();

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
                .baseUrl("http://api.forismatic.com/api/1.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        service = retrofit.create(ForismaticService.class);
    }

    public interface ForismaticService {
        @GET("?method=getQuote&format=json&lang=ru&key={id}")
        Call<Quote> getQuote(@Path("id") int myKey);
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getQuotes();
            return null;
        }

        private synchronized void getQuotes() {
            try {
                for(int i = 0; i < 10; i++){
                    Call<Quote> callQ = service.getQuote(i);
                    Quote quote = callQ.execute().body();
                    addQuote(quote);
                    //Call<Quote> callQNew = callQ.clone();
                    //Quote quote2 = callQNew.execute().body();

                    //addQuote(quote2);
                }
            } catch (IOException e) {

            }
        }

        private void addQuote(Quote quote){
            quotes.add(quote);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            QuoteListAdapter quoteListAdapter = new QuoteListAdapter(quotes);
            recyclerView.setAdapter(quoteListAdapter);
            recyclerView.setHasFixedSize(true);
        }
    }
}
