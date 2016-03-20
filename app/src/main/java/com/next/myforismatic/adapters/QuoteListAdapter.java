package com.next.myforismatic.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.next.myforismatic.R;
import com.next.myforismatic.models.Quote;

import java.util.Collections;
import java.util.List;

/**
 * Created by maslparu on 18.03.2016.
 */
public class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.ViewHolder> {

    @NonNull
    private List<Quote> quotes = Collections.emptyList();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView quote;
        private TextView author;

        public ViewHolder(View view) {
            super(view);
            quote = (TextView) itemView.findViewById(R.id.quote);
            author = (TextView) itemView.findViewById(R.id.author);
        }
    }

    public void setQuotes(@NonNull List<Quote> quotes) {
        this.quotes = quotes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_qoute_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.quote.setText(quotes.get(position).getText());
        viewHolder.author.setText(quotes.get(position).getAuthor());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
