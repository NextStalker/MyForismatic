package com.next.myforismatic;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGet = (Button) findViewById(R.id.button);
        btnGet.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textView);

        context = this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button){
            getQuote();
        }
    }
/*
    public interface ForismaticService {
        @GET("method=getQuote&format=text&lang=ru")
        Call<Quote> getQuote();
    }
*/
    public interface ForismaticService {
        @GET("?method=getQuote&format=json&lang=ru")
        Call<QuoteP> getQuoteP();
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.forismatic.com/api/1.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ForismaticService service = retrofit.create(ForismaticService.class);


/*
            .baseUrl("http://api.forismatic.com/api/1.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
*/
    //ForismaticService service = retrofit.create(ForismaticService.class);

    public class QuoteP {
        public String Text;
        public String Author;
        public String Name;
        public String SenderLink;
        public String QuoteLink;

        @Override
        public String toString() {
            if ((Author == null) || (Author == "")){
                return Text;
            }else{
                return Text + " (" + Author + ")";
            }
        }
    }

    private void getQuote() {
        Call<QuoteP> callQ = service.getQuoteP();
        callQ.enqueue(new Callback<QuoteP>() {
            @Override
            public void onResponse(Call<QuoteP> call, Response<QuoteP> response) {
                Toast.makeText(context, response.body().Text, Toast.LENGTH_LONG).show();
                setQuote(response.body());
            }

            @Override
            public void onFailure(Call<QuoteP> call, Throwable t) {

                Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
            }
        });
        //Call<Quote> quote = service.getQuote();
        //myTask = new MyTask();
        //myTask.execute();
    }

    private void setQuote(QuoteP quoteP){
        textView.setText(quoteP.toString());
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
