package com.next.myforismatic.ui.authorquoteslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.next.myforismatic.R;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.ui.base.BaseFragment;
import com.next.myforismatic.ui.contracts.QuoteListContract;
import com.next.myforismatic.ui.presenters.AuthorQuoteListPresenterImpl;

import java.util.List;

/**
 * Created by maslparu on 06.07.2016.
 */
public class AuthorQuoteListFragment extends BaseFragment implements QuoteListContract.QuoteListView {

    private String author;
    private QuoteListContract.Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        presenter = new AuthorQuoteListPresenterImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_author_quote_list, container, false);

        presenter.viewCreated(this);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        presenter.viewDestroyed();

        super.onDestroy();
    }

    @Override
    public void setData(List<Quote> quotes) {
        presenter.loadData();
    }
}
