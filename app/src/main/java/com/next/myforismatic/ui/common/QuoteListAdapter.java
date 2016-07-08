package com.next.myforismatic.ui.common;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.next.myforismatic.ui.authorquoteslist.AuthorQuoteListActivity;
import com.next.myforismatic.R;
import com.next.myforismatic.models.Quote;
import com.next.myforismatic.providers.QuoteContentProvider;

import java.util.Collections;
import java.util.List;

/**
 * Created by maslparu on 18.03.2016.
 */
public class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.ViewHolder> {

    private boolean isAuthorQuotes;

    @NonNull
    private List<Quote> quotes = Collections.emptyList();

    public void setQuotes(@NonNull List<Quote> quotes) {
        this.quotes = quotes;
        notifyDataSetChanged();
    }

    public void setAuthorQuotes(boolean authorQuotes) {
        isAuthorQuotes = authorQuotes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.view_quote_item, viewGroup, false),
                isAuthorQuotes
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    @NonNull
    private Quote getItem(int position) {
        return quotes.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView quoteText;
        private final TextView authorText;
        private final boolean isAuthorQuotes;

        private String authorName;

        public ViewHolder(@NonNull View itemView, boolean authorQuotes) {
            super(itemView);
            isAuthorQuotes = authorQuotes;
            quoteText = (TextView) itemView.findViewById(R.id.quote);
            authorText = (TextView) itemView.findViewById(R.id.author);
        }

        public void bind(@NonNull Quote quote) {
            authorName = quote.getAuthor();
            if (!isAuthorQuotes) {
                itemView.setOnClickListener(this);
            }
            quoteText.setText(quote.getText());
            authorText.setText(
                    itemView.getContext().getString(
                            R.string.author_format,
                            quote.getAuthor()
                    )
            );
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Intent intent = new Intent(context, AuthorQuoteListActivity.class);
            intent.putExtra(QuoteContentProvider.QUOTE_AUTHOR, authorName);
            context.startActivity(intent);
        }

    }

}
