package com.next.myforismatic.adapters;

import android.content.Context;
import android.content.Intent;
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

/**
 * Created by maslparu on 18.03.2016.
 */
public class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.ViewHolder> {

    public static final String TAG = AuthorQuoteListFragment.class.getSimpleName();

    @NonNull
    private List<Quote> quotes = Collections.emptyList();

    private FragmentManager fragmentManager;
    private Context context;

    private static QuoteListAdapter _uniqueInstance;

    private QuoteListAdapter(FragmentManager fragmentManager, Context context){
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public static QuoteListAdapter getInstance(FragmentManager fragmentManager, Context context) {
        if (_uniqueInstance == null) {
            _uniqueInstance = new QuoteListAdapter(fragmentManager, context);
        }

        return _uniqueInstance;
    }

    public static QuoteListAdapter getInstance() {
        return getInstance(null, null);
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

            Intent intent = new Intent(context, AuthorQuoteListActivity.class);
            intent.putExtra(QuoteContentProvider.QUOTE_AUTHOR, textView.getText().toString());
            context.startActivity(intent);
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
