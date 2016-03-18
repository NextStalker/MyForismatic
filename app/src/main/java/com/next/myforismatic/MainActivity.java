package com.next.myforismatic;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.next.myforismatic.adapters.QuoteListAdapter;
import com.next.myforismatic.models.Quote;

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

public class MainActivity extends AppCompatActivity {

    private Context context;
    //MyTask myTask;
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

        QuoteListAdapter quoteListAdapter = new QuoteListAdapter(quotes);
        recyclerView.setAdapter(quoteListAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initializeData(){
        quotes = new ArrayList<>();
        quotes.add(new Quote("цитата 1", "автор 1"));
        quotes.add(new Quote("цитата 2", "автор 2"));
        quotes.add(new Quote("цитата 3", "автор 3"));
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

        @GET("?method=getQuote&format=json&lang=ru")
        Call<Quote> getQuote();
    }
    private void getQuote() {
        Call<Quote> callQ = service.getQuote();
        callQ.enqueue(new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                Snackbar.make(root, response.body().getText(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                Snackbar.make(root, "Error", Snackbar.LENGTH_LONG).show();
            }
        });
        //Call<Quote> quote = service.getQuote();
        //myTask = new MyTask();
        //myTask.execute();
    }
/*
    class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return getQuote();
        }

        public String getQuote() {
            URL url = null;
            try {
                url = new URL("http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=ru");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(con.getInputStream());
            } catch (IOException e) {
                return null;
            }

            BufferedReader reader = new BufferedReader(inputStreamReader);
            String quote = null;
            try {
                quote = reader.readLine();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return quote;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(context, "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
            } else {
                JSONObject dataJsonObj = null;
                try{
                    dataJsonObj = new JSONObject(result);
                    String quoteText = dataJsonObj.getString("quoteText");
                    String quoteAuthor = dataJsonObj.getString("quoteAuthor");
                    String senderName = dataJsonObj.getString("senderName");
                    String senderLink = dataJsonObj.getString("senderLink");
                    String quoteLink = dataJsonObj.getString("quoteLink");

                    curQuote = new Quote(quoteText, quoteAuthor, senderName, senderLink, quoteLink);

                    setQuote(curQuote);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }*/
}
