package com.next.myforismatic.api.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.next.myforismatic.providers.QuoteContentProvider;

import timber.log.Timber;

public class SyncManager {

    /**
     * Sync period in seconds
     */
    private static final long DEFAULT_SYNC_PERIOD = 10 * 3600;
    private static final String DUMMY_ACCOUNT_NAME = "Forismatic";

    /**
     * Request a manual sync and to start immediately
     */
    public static void requestSync(@NonNull Account account) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // makes sure sync occurs immediately
        if (!ContentResolver.isSyncPending(account, QuoteContentProvider.AUTHORITY) &&
                !ContentResolver.isSyncActive(account, QuoteContentProvider.AUTHORITY)) {
            ContentResolver.requestSync(account, QuoteContentProvider.AUTHORITY, bundle);
        }
    }

    @Nullable
    public static Account getAccount(@NonNull Context context) {
        Account[] accounts = getAccounts(context);
        if (accounts.length > 0) {
            return accounts[0];
        } else {
            return null;
        }
    }

    @NonNull
    public static Account[] getAccounts(@NonNull Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = {};
        try {
            accounts = accountManager.getAccountsByType(SyncService.ACCOUNT_TYPE);
        } catch (Exception ignored) {
        }
        return accounts;
    }

    public static void createAccountAndStartSync(@NonNull Context context) {
        try {
            AccountManager accountManager = AccountManager.get(context);
            Account account = getAccount(context);
            Bundle extras = new Bundle();
            if (account == null) {
                account = createAccount(accountManager, DUMMY_ACCOUNT_NAME);
                ContentResolver.addPeriodicSync(account, QuoteContentProvider.AUTHORITY, extras, DEFAULT_SYNC_PERIOD);
            }
            requestSync(account);
        } catch (Exception e) {
            Timber.e(e, "failed to add default account and start sync");
        }
    }

    private static Account createAccount(@NonNull AccountManager accountManager,
                                         @NonNull String accountName) {
        Account account = new Account(accountName, SyncService.ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(account, null, null);

        ContentResolver.setIsSyncable(account, QuoteContentProvider.AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, QuoteContentProvider.AUTHORITY, true);
        return account;
    }

}
