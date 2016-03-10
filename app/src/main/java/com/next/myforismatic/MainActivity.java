package com.next.myforismatic;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGet;
    private TextView textView;
    private Context context;
    MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGet = (Button) findViewById(R.id.button);
        btnGet.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textView);

        context = this;

        GetQuote();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button){
            GetQuote();
        }
    }

    public void GetQuote(){
        myTask = new MyTask();
        myTask.execute();
    }

    class MyTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return GetQuote();
        }

        public String GetQuote() {
            URL url = null;
            try {
                url = new URL("http://api.forismatic.com/api/1.0/?method=getQuote&format=text&lang=ru");
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
            if (result == null)
                Toast.makeText(context, "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
            else
                textView.setText(result);
        }
    }
}
