package com.next.myforismatic.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.next.myforismatic.R;
import com.next.myforismatic.adapters.QuoteListAdapter;
import com.next.myforismatic.common.CursorParse;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.providers.QuoteContentProvider;

import java.util.List;

/**
 * Created by maslparu on 06.07.2016.
 */
public class AuthorQuoteListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private QuoteListAdapter adapter;
    private String authorName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_author_quote_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_author_quote_list_rv);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new QuoteListAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrieveQuotes();
    }

    private void retrieveQuotes() {
        getActivity().getSupportLoaderManager().initLoader(R.id.quote_cursor_loader, null, this).forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == R.id.quote_cursor_loader) {
            return new MyCursorLoader(getContext(), QuoteContentProvider.QUOTE_CONTENT_URI, authorName);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Quote> quotes = CursorParse.parseQuotes(data);

        //TODO Implement getQuotes
        /*
        if (quotes.size() == 0) {
            getQuotesFromInternet();
        } else {
            setQuotes(quotes);
        }
        */
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private static class MyCursorLoader extends CursorLoader {

        private Uri uri;
        private String authorName;

        public MyCursorLoader(Context context, Uri uri, String authorName) {
            super(context);
            this.uri = uri;
            this.authorName = authorName;
        }

        @Override
        public Cursor loadInBackground() {
            return getContext().getContentResolver().query(
                    uri, null, "author", new String[] {authorName},
                    QuoteContentProvider.QUOTE_ID
            );
        }

    }
}
