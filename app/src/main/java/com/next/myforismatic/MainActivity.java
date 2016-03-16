package com.next.myforismatic;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGet;
    private TextView textView;
    private Context context;
    //MyTask myTask;
    private Quote curQuote;
    private ForismaticService service;

    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRetrofit();
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.activity_main_root);

        btnGet = (Button) findViewById(R.id.button);
        btnGet.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textView);

        context = this;
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            getQuote();
        }
    }

    public interface ForismaticService {

        @GET("?method=getQuote&format=json&lang=ru")
        Call<QuoteP> getQuoteP();

    }

    public class QuoteP {

        /**
         * {
         * "quoteText":"У человека, влюбленного в себя, мало соперников.",
         * "quoteAuthor":"Георг Лихтенберг",
         * "senderName":"",
         * "senderLink":"",
         * "quoteLink":"http://forismatic.com/ru/4dd7ea9607/"
         * }
         **/

        @SerializedName("quoteText")
        public String text;
        @SerializedName("quoteAuthor")
        public String author;
        @SerializedName("senderName")
        public String name;
        @SerializedName("senderLink")
        public String senderLink;
        @SerializedName("quoteLink")
        public String quoteLink;

        public String getText() {
            return text;
        }

        public String getAuthor() {
            return TextUtils.isEmpty(author) ? "Аноним" : author;
        }

        @Override
        public String toString() {
            return "QuoteP{" +
                    "text='" + text + '\'' +
                    ", author='" + author + '\'' +
                    ", name='" + name + '\'' +
                    ", senderLink='" + senderLink + '\'' +
                    ", quoteLink='" + quoteLink + '\'' +
                    '}';
        }

    }

    private void getQuote() {
        Call<QuoteP> callQ = service.getQuoteP();
        callQ.enqueue(new Callback<QuoteP>() {
            @Override
            public void onResponse(Call<QuoteP> call, Response<QuoteP> response) {
                Snackbar.make(root, response.body().getText(), Snackbar.LENGTH_LONG).show();
                setQuote(response.body());
            }

            @Override
            public void onFailure(Call<QuoteP> call, Throwable t) {
                Snackbar.make(root, "Error", Snackbar.LENGTH_LONG).show();
            }
        });
        //Call<Quote> quote = service.getQuote();
        //myTask = new MyTask();
        //myTask.execute();
    }

    private void setQuote(QuoteP quoteP) {
        textView.setText(quoteP.getText() + "\n( " + quoteP.getAuthor() + " )");
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
