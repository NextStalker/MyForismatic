package com.next.myforismatic.api.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

    public static final String ACCOUNT_TYPE = "com.next.myforismatic.quotes";
    public static final String SERVICE_NAME = "forismatic";
    public static final String FEATURE_SERVICE_NAME = "service_" + SERVICE_NAME;

    private static SyncAdapter syncAdapter = null;
    private static final Object syncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (syncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

}
