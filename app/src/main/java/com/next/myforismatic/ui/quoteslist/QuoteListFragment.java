package com.next.myforismatic.ui.quoteslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.next.myforismatic.R;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.ui.base.BaseFragment;
import com.next.myforismatic.ui.contracts.QuoteListContract;
import com.next.myforismatic.ui.presenters.QuotesListPresenterImpl;

import java.util.List;

/**
 * @author Konstantin Abramov on 20.03.16.
 */
public class QuoteListFragment extends BaseFragment implements QuoteListContract.QuoteListView {

    private QuotesListPresenterImpl presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        presenter = new QuotesListPresenterImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quote_list, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.app_name);

        presenter.viewCreated(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.load_more_item) {
            presenter.loadData();
            return true;
        } else {
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
