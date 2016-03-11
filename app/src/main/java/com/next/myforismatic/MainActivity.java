package com.next.myforismatic;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGet;
    private Button btnPrev;
    private TextView textView;
    private Context context;
    MyTask myTask;
    private Quote curQuote;
    private Quote prevQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGet = (Button) findViewById(R.id.button);
        btnPrev = (Button) findViewById(R.id.buttonPrev);
        btnGet.setOnClickListener(this);
        btnPrev.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textView);

        context = this;

        if (curQuote == null){
            getQuote();
        }else{
            setQuote(curQuote);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button){
            getQuote();
        } else if (v.getId() == R.id.buttonPrev) {
            getPrevQuote();
        }
    }
/*
    public interface ForismaticService {
        @GET("method=getQuote&format=text&lang=ru")
        Call<Quote> getQuote();
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.forismatic.com/api/1.0/")
            .build();

    ForismaticService service = retrofit.create(ForismaticService.class);
*/
    private void getQuote(){
        //Call<Quote> quote = service.getQuote();
        prevQuote = curQuote;
        myTask = new MyTask();
        myTask.execute();
        //textView.setText(quote.toString());
    }

    private void getPrevQuote(){
        if (prevQuote == null){
            return;
        }else {
            setQuote(prevQuote);
        }
    }

    private void setQuote(Quote quote){
        textView.setText(quote.toString());
    }

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
    }
}
