package com.next.myforismatic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.next.myforismatic.fragments.QuoteListFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = QuoteListFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            showQuoteListFragment();
        }
    }

    private void showQuoteListFragment() {
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main_root, new QuoteListFragment(), TAG)
                    .commit();
        }
    }

}
