package com.next.myforismatic.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import java.util.List;

public class NotificationCenter {

    //old code may be need refactoring or improve here!

    private final Context context;
    private final NotificationManager notificationManager;
    private NotificationCompat.Builder notifBuilderComp;
    private Intent mainIntent;
    private int flags;
    private int notifyID = 0;

    public NotificationCenter(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public NotificationCenter setNotifId(int id) {
        this.notifyID = id;
        return NotificationCenter.this;
    }

    public NotificationCenter initNotification(Intent mainIntent, int icon, String title, String text, boolean autoCancel, Bitmap large, String ticker) {
        this.mainIntent = mainIntent;
        notifBuilderComp = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(autoCancel)
                .setWhen(System.currentTimeMillis());
        if (mainIntent != null) notifBuilderComp.setContentIntent(
                PendingIntent.getActivity(context, 0, this.mainIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        );
        if (large != null) notifBuilderComp.setLargeIcon(large);
        if (ticker != null) notifBuilderComp.setTicker(ticker);

        setStartFlags();
        notifBuilderComp.setSound(null);
        return NotificationCenter.this;
    }

    public NotificationCenter setIntent(Intent intent) {
        mainIntent = intent;
        return NotificationCenter.this;
    }

    public NotificationCenter setProgress(int max, int progress, boolean intermediate) {
        notifBuilderComp.setProgress(max, progress, intermediate);
        return NotificationCenter.this;
    }

    public NotificationCenter setOnGoing(boolean onGoing) {
        notifBuilderComp.setOngoing(onGoing);
        return NotificationCenter.this;
    }

    public void setDefaultFlags() {
        flags = Notification.DEFAULT_ALL;
    }

    public void setStartFlags() {
        flags = Notification.DEFAULT_ALL;
        flags &= ~(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
    }

    public NotificationCenter setBigTextStyle(String title, String text, String sumaryText) {
        notifBuilderComp.setStyle(getBigTextStyle(title, text, sumaryText));
        return NotificationCenter.this;
    }

    public NotificationCenter setBigPictureStyle(String title, Bitmap bigBitmap, String sumaryText) {
        if (bigBitmap == null) return NotificationCenter.this;

        notifBuilderComp.setStyle(getBigPictureStyle(title, bigBitmap, sumaryText));
        return NotificationCenter.this;
    }

    public NotificationCenter setInboxStyle(String title, List<String> text, String sumaryText) {
        notifBuilderComp.setStyle(getInboxStyle(title, text, sumaryText));
        return NotificationCenter.this;
    }

    public NotificationCompat.BigTextStyle getBigTextStyle(String title, String text, String sumaryText) {
        NotificationCompat.BigTextStyle BigTextStyle = new NotificationCompat.BigTextStyle();
        BigTextStyle.setBigContentTitle(title);
        BigTextStyle.bigText(text);
        if (!TextUtils.isEmpty(sumaryText)) BigTextStyle.setSummaryText(sumaryText);
        return BigTextStyle;
    }

    public NotificationCompat.BigPictureStyle getBigPictureStyle(String title, Bitmap bigBitmap, String sumaryText) {
        NotificationCompat.BigPictureStyle BigPictureStyle = new NotificationCompat.BigPictureStyle();
        BigPictureStyle.setBigContentTitle(title);
        BigPictureStyle.bigPicture(bigBitmap);
        if (!TextUtils.isEmpty(sumaryText)) BigPictureStyle.setSummaryText(sumaryText);
        return BigPictureStyle;
    }

    public NotificationCompat.InboxStyle getInboxStyle(String title, List<String> text, String sumaryText) {
        NotificationCompat.InboxStyle InboxStyle = new NotificationCompat.InboxStyle();
        InboxStyle.setBigContentTitle(title);
        for (String string : text) {
            InboxStyle.addLine(string);
        }
        if (!TextUtils.isEmpty(sumaryText)) InboxStyle.setSummaryText(sumaryText);
        return InboxStyle;
    }

    public NotificationCenter setNumber(int count) {
        notifBuilderComp.setNumber(count);
        return NotificationCenter.this;
    }

    public NotificationCenter setDefaults() {
        flags |= Notification.DEFAULT_SOUND;
        flags |= Notification.DEFAULT_VIBRATE;
        return NotificationCenter.this;
    }

    public NotificationCenter setVibro() {
        flags |= Notification.DEFAULT_VIBRATE;
        return NotificationCenter.this;
    }

    public NotificationCenter setSoundDefault() {
        flags |= Notification.DEFAULT_SOUND;
        return NotificationCenter.this;
    }

    public NotificationCenter setSound(String path) {
        notifBuilderComp.setSound(Uri.parse(path));
        return NotificationCenter.this;
    }

    public NotificationCenter setSound(Uri uri) {
        notifBuilderComp.setSound(uri);
        return NotificationCenter.this;
    }

    public NotificationCenter addAction(int icon, String text, Intent intent, boolean cancelCurrent) {
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, cancelCurrent ? PendingIntent.FLAG_CANCEL_CURRENT : PendingIntent.FLAG_UPDATE_CURRENT);
        notifBuilderComp.addAction(icon, text, pi);
        return NotificationCenter.this;
    }

    public Notification getNotification() {
        notifBuilderComp.setDefaults(flags);
        return notifBuilderComp.build();
    }

    public NotificationCenter remove() {
        return remove(notifyID);
    }

    public NotificationCenter remove(int id) {
        notificationManager.cancel(id);
        return NotificationCenter.this;
    }

    public NotificationCenter removeAll() {
        notificationManager.cancelAll();
        return NotificationCenter.this;
    }

    public NotificationCenter show() {
        notificationManager.notify(notifyID, getNotification());
        return NotificationCenter.this;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public NotificationCompat.Builder getBuilder() {
        return notifBuilderComp;
    }

    public void removeAllNotifications() {
        notificationManager.cancelAll();
    }

}
