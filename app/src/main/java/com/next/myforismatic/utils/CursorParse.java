package com.next.myforismatic.utils;

import android.database.Cursor;

import com.next.myforismatic.models.Quote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Next on 04.04.2016.
 */
public class CursorParse {
    public static List<Quote> parseQuotes(Cursor cursor) {

        List<Quote> list = new ArrayList<>();

        try {

            if (cursor.moveToFirst()) {
                do {
                    Quote quote = new Quote(cursor.getString(cursor.getColumnIndex("text")), cursor.getString(cursor.getColumnIndex("author")));
                    list.add(quote);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return list;
    }
}
