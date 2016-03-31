package com.next.myforismatic.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Next on 31.03.2016.
 */
public class QuoteContentProvider extends ContentProvider {

    static final String DB_NAME = "myDB";
    static final String QUOTE_TABLE = "quotes";

    static final String QUOTE_ID = "_id";
    static final String QUOTE_TEXT = "text";
    static final String QUOTE_AUTHOR = "author";
    static final String QUOTE_NAME = "name";
    static final String QUOTE_SENDER_LINK = "senderLink";
    static final String QUOTE_QUOTE_LINK = "quoteLink";

    static final String DB_CREATE = "CREATE TABLE " + QUOTE_TABLE + " (" + QUOTE_ID +
            " integer primary key autoincrement, " +
            QUOTE_TEXT + " text, " + QUOTE_AUTHOR + " text, " +
            QUOTE_NAME + " text, " + QUOTE_SENDER_LINK + " text, " +
            QUOTE_QUOTE_LINK + " text" + ")";

    static final String AUTHORITY = "com.next.myforismatic.providers";

    static final String QUOTE_PATH = "quotes";

    public static final Uri QUOTE_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + QUOTE_PATH);

    static final String QUOTE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + QUOTE_PATH;

    static final String QUOTE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + QUOTE_PATH;

    static final int URI_QUOTES = 1;
    static final int URI_QUOTES_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, QUOTE_PATH, URI_QUOTES);
        uriMatcher.addURI(AUTHORITY, QUOTE_PATH + "/#", URI_QUOTES_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (uriMatcher.match(uri)) {
            case URI_QUOTES:
                break;
            case URI_QUOTES_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    selection = QUOTE_ID + " = " + id;
                } else {
                    selection += " AND " + QUOTE_ID + " = " + id;
                }
                default:
                    throw new IllegalArgumentException("Wrong uri: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(QUOTE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), QUOTE_CONTENT_URI);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case URI_QUOTES:
                return QUOTE_CONTENT_TYPE;
            case URI_QUOTES_ID:
                return QUOTE_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (uriMatcher.match(uri) != URI_QUOTES) {
            throw new IllegalArgumentException("Wrong uri: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(QUOTE_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(uri, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);

        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        switch (uriMatcher.match(uri)){
            case URI_QUOTES:
                break;
            case URI_QUOTES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = QUOTE_ID + " = " + id;
                } else {
                    selection += selection + " AND " + QUOTE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(QUOTE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        switch (uriMatcher.match(uri)) {
            case URI_QUOTES:
                break;
            case URI_QUOTES_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    selection = QUOTE_ID + " = " + id;
                } else {
                    selection += QUOTE_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        int cnt = db.update(QUOTE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return cnt;
    }

    class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
