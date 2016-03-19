package com.next.myforismatic.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.next.myforismatic.R;
import com.next.myforismatic.models.Quote;

import java.util.List;

/**
 * Created by maslparu on 18.03.2016.
 */
public class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.ViewHolder> {
    private List<Quote> quotes;

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        //private CardView cv;
        private TextView quote;
        private TextView author;

        public ViewHolder(View view){
            super(view);
            //cv = (CardView) view.findViewById(R.id.cv);
            quote = (TextView) itemView.findViewById(R.id.quote);
            author = (TextView) itemView.findViewById(R.id.author);
        }
    }

    public QuoteListAdapter(List<Quote> quotes){
        this.quotes = quotes;
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item , viewGroup, false);

        return new ViewHolder(v);
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
