package com.next.myforismatic.ui.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.next.myforismatic.R;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.providers.QuoteContentProvider;
import com.next.myforismatic.ui.authorquoteslist.AuthorQuoteListFragment;
import com.next.myforismatic.ui.common.QuoteListAdapter;
import com.next.myforismatic.ui.contracts.QuoteListContract;
import com.next.myforismatic.utils.CursorParse;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by maslparu on 04.08.2016.
 */
public class AuthorQuoteListPresenterImpl implements QuoteListContract.Presenter {

    private AuthorQuoteListFragment fragment;

    private Subscription subscription;
    private RecyclerView recyclerView;
    private QuoteListAdapter adapter;

    private String author;

    @Override
    public void viewCreated(QuoteListContract.QuoteListView view) {
        fragment = (AuthorQuoteListFragment) view;

        Bundle bundle = fragment.getArguments();

        if (bundle != null) {
            author = bundle.getString(QuoteContentProvider.QUOTE_AUTHOR);
            fragment.getActivity().setTitle(author);
        }

        recyclerView = (RecyclerView) fragment.getActivity().findViewById(R.id.fragment_author_quote_list_rv);

        adapter = new QuoteListAdapter();
        adapter.setAuthorQuotes(true);
        recyclerView.setAdapter(adapter);

        loadData();
    }

    @Override
    public void loadData() {
        subscription = observeQuotesByAuthor()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setData);
    }

    private Observable<List<Quote>> observeQuotesByAuthor() {
        return Observable.fromCallable(() -> {
            List<Quote> quotes = getQuotesByAuthor();
            return quotes;
        }).subscribeOn(Schedulers.io());
    }

    @NonNull
    private List<Quote> getQuotesByAuthor() {
        return CursorParse.parseQuotes(
                fragment.getActivity().getContentResolver().query(
                        QuoteContentProvider.QUOTE_CONTENT_URI,
                        null,
                        QuoteContentProvider.QUOTE_AUTHOR,
                        new String[] { author },
                        QuoteContentProvider.QUOTE_ID
                ));
    }

    @Override
    public void viewDestroyed() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void setData(List<Quote> quotes) {
        adapter.setQuotes(quotes);
    }
}
