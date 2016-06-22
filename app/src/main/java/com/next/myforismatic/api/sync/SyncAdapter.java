package com.next.myforismatic.api.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by Next on 12.05.2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    @NonNull
    private final ContentResolver contentResolver;

    public SyncAdapter(@NonNull Context context) {
        this(context, true);
    }

    public SyncAdapter(@NonNull Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

    }
}
