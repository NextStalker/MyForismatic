package com.next.myforismatic.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.next.myforismatic.AuthorQuoteListActivity;
import com.next.myforismatic.R;
import com.next.myforismatic.fragments.AuthorQuoteListFragment;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.providers.QuoteContentProvider;

import java.util.Collections;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by maslparu on 18.03.2016.
 */
public class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.ViewHolder> {

    public static final String TAG = AuthorQuoteListFragment.class.getSimpleName();

    @NonNull
    private List<Quote> quotes = Collections.emptyList();

    private FragmentManager fragmentManager;

    private static QuoteListAdapter _uniqueInstance;

    private QuoteListAdapter(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public static QuoteListAdapter getInstance(FragmentManager fragmentManager) {
        if (_uniqueInstance == null) {
            _uniqueInstance = new QuoteListAdapter(fragmentManager);
        }

        return _uniqueInstance;
    }

    public void setQuotes(@NonNull List<Quote> quotes) {
        this.quotes = quotes;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.view_quote_item, viewGroup, false);

        ViewHolder.IMyViewHolderClicks myViewHolderClicks = view -> {
            TextView textView = (TextView) view.findViewById(R.id.author);

            Bundle bundle = new Bundle();
            bundle.putString(QuoteContentProvider.QUOTE_AUTHOR, textView.getText().toString());
            Intent intent = new Intent();
            startActivity(new AuthorQuoteListActivity(), intent, bundle);
        };

        return new ViewHolder(v, myViewHolderClicks);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.quote.setText(quotes.get(position).getText());
        viewHolder.author.setText(
                viewHolder.itemView.getContext().getString(
                        R.string.author_format,
                        quotes.get(position).getAuthor()
                )
        );
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView quote;
        private TextView author;
        private IMyViewHolderClicks listener;

        public ViewHolder(View view, IMyViewHolderClicks listener) {
            super(view);
            this.listener = listener;
            quote = (TextView) itemView.findViewById(R.id.quote);
            author = (TextView) itemView.findViewById(R.id.author);
            author.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onAuthorClick(v);
        }

        public interface IMyViewHolderClicks {
            void onAuthorClick(View view);
        }
    }

}
