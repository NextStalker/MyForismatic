package com.next.myforismatic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.next.myforismatic.fragments.AuthorQuoteListFragment;

/**
 * Created by maslparu on 08.07.2016.
 */
public class AuthorQuoteListActivity extends AppCompatActivity {

    public static final String TAG = AuthorQuoteListFragment.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_quote_list);

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.author_quotes_container, new AuthorQuoteListFragment(), TAG)
                    .commit();
        }


    }
}
