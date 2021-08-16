package com.kali_corporation.healthfitnessplus.sevice.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.media.AudioAttributes.Builder;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Log;
import com.kali_corporation.healthfitnessplus.activity.Water_MainActivity;
import com.kali_corporation.healthfitnessplus.sevice.utils.AppUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NotificationHelper {
    private NotificationManager notificationManager;
    private final String CHANNEL_ONE_ID;
    private final String CHANNEL_ONE_NAME;
    @NotNull
    private final Context ctx;

    public NotificationHelper(@NotNull Context ctx) {
        this.ctx = ctx;
        this.CHANNEL_ONE_ID = "com.crazytrends.healthmanager.CHANNELONE";
        this.CHANNEL_ONE_NAME = "Channel One";
    }

    private final void createChannels() {
        if (VERSION.SDK_INT >= 26) {
            SharedPreferences prefs = this.ctx.getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE);
            String notificationsNewMessageRingtone = prefs.getString(AppUtils.NOTIFICATION_TONE_URI_KEY, RingtoneManager.getDefaultUri(2).toString());
            NotificationChannel notificationChannel = new NotificationChannel(this.CHANNEL_ONE_ID, (CharSequence)this.CHANNEL_ONE_NAME, 4);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.setShowBadge(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(1);
            if (notificationsNewMessageRingtone == null) {
            }

            CharSequence var4 = (CharSequence)notificationsNewMessageRingtone;
            boolean var5 = false;
            if (var4.length() > 0) {
                AudioAttributes audioAttributes = (new Builder()).setContentType(2).setUsage(5).build();
                notificationChannel.setSound(Uri.parse(notificationsNewMessageRingtone), audioAttributes);
            }

            NotificationManager var10000 = this.getManager();
            if (var10000 == null) {
            }

            var10000.createNotificationChannel(notificationChannel);
        }

    }

    @Nullable
    public final androidx.core.app.NotificationCompat.Builder getNotification(@NotNull String title, @NotNull String body, @Nullable String notificationsTone) {
        this.createChannels();
        androidx.core.app.NotificationCompat.Builder notification = (new androidx.core.app.NotificationCompat.Builder(this.ctx.getApplicationContext(), this.CHANNEL_ONE_ID)).setContentTitle((CharSequence)title).setContentText((CharSequence)body).setLargeIcon(BitmapFactory.decodeResource(this.ctx.getResources(), 1500053)).setSmallIcon(700166).setAutoCancel(true);
        notification.setShowWhen(true);
        notification.setSound(Uri.parse(notificationsTone));
        Intent notificationIntent = new Intent(this.ctx, Water_MainActivity.class);
        notificationIntent.setFlags(603979776);
        PendingIntent contentIntent = PendingIntent.getActivity(this.ctx, 99, notificationIntent, 134217728);
        notification.setContentIntent(contentIntent);
        return notification;
    }

    private final boolean shallNotify() {
        SharedPreferences prefs = this.ctx.getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE);
        SqliteHelper sqliteHelper = new SqliteHelper(this.ctx);
        String var10001 = this.getCurrentDate();
        if (var10001 == null) {
        }

        int percent = sqliteHelper.getIntook(var10001) * 100 / prefs.getInt(AppUtils.TOTAL_INTAKE, 0);
        boolean doNotDisturbOff = true;
        long startTimestamp = prefs.getLong(AppUtils.WAKEUP_TIME, 0L);
        long stopTimestamp = prefs.getLong(AppUtils.SLEEPING_TIME_KEY, 0L);
        if (startTimestamp > 0L && stopTimestamp > 0L) {
            Calendar var10000 = Calendar.getInstance();
            Date now = var10000.getTime();
            Date start = new Date(startTimestamp);
            Date stop = new Date(stopTimestamp);
            doNotDisturbOff = this.compareTimes(now, start) >= 0L && this.compareTimes(now, stop) <= 0L;
        }

        return doNotDisturbOff && percent < 100;
    }

    private final long compareTimes(Date currentTime, Date timeToRun) {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentTime);
        Calendar runCal = Calendar.getInstance();
        runCal.setTime(timeToRun);
        runCal.set(5, currentCal.get(5));
        runCal.set(2, currentCal.get(2));
        runCal.set(1, currentCal.get(1));
        return currentCal.getTimeInMillis() - runCal.getTimeInMillis();
    }

    public final void notify(long id, @Nullable androidx.core.app.NotificationCompat.Builder notification) {
        if (this.shallNotify()) {
            NotificationManager var10000 = this.getManager();
            if (var10000 == null) {

            }

            int var10001 = (int)id;
            if (notification == null) {

            }

            var10000.notify(var10001, notification.build());
        } else {
            Log.i("AquaDroid", "dnd period");
        }

    }

    private final NotificationManager getManager() {
        if (this.notificationManager == null) {
            Object var10001 = this.ctx.getSystemService("notification");
            if (var10001 == null) {
            }

            this.notificationManager = (NotificationManager)var10001;
        }

        return this.notificationManager;
    }

    @Nullable
    public final String getCurrentDate() {
        Calendar var10000 = Calendar.getInstance();
        Date c = var10000.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c);
    }

    @NotNull
    public final Context getCtx() {
        return this.ctx;
    }

}
