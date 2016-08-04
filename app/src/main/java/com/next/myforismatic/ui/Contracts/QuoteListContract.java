package com.next.myforismatic.ui.contracts;

import com.next.myforismatic.models.Quote;

import java.util.List;

/**
 * Created by maslparu on 21.07.2016.
 */
public interface QuoteListContract {

    interface QuoteListView {
        void setData(List<Quote> quotes);
    }

    interface Presenter {

        void viewCreated(QuoteListView view);

        void loadData();

        void viewDestroyed();
    }
}
