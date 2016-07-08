package com.next.myforismatic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.next.myforismatic.fragments.AuthorQuoteListFragment;
import com.next.myforismatic.providers.QuoteContentProvider;

/**
 * Created by maslparu on 08.07.2016.
 */
public class AuthorQuoteListActivity extends AppCompatActivity {

    public static final String TAG = AuthorQuoteListFragment.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_quote_list);

        String author = getIntent().getStringExtra(QuoteContentProvider.QUOTE_AUTHOR);

        Bundle bundle = new Bundle();
        bundle.putString(QuoteContentProvider.QUOTE_AUTHOR, author);

        AuthorQuoteListFragment authorQuoteListFragment = new AuthorQuoteListFragment();
        authorQuoteListFragment.setArguments(bundle);

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.author_quotes_container, authorQuoteListFragment, TAG)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
