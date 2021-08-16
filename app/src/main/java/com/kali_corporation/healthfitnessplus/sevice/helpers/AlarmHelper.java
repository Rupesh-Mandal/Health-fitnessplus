package com.kali_corporation.healthfitnessplus.sevice.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import com.kali_corporation.healthfitnessplus.sevice.recievers.BootReceiver;
import com.kali_corporation.healthfitnessplus.sevice.recievers.NotifierReceiver;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public final class AlarmHelper {
    private AlarmManager alarmManager;
    private final String ACTION_BD_NOTIFICATION = "com.crazytrends.healthmanager.NOTIFICATION";

    public final void setAlarm(@NotNull Context context, long notificationFrequency) {
        long notificationFrequencyMs = TimeUnit.MINUTES.toMillis(notificationFrequency);
        Object var10001 = context.getSystemService("alarm");
        if (var10001 == null) {
        } else {
            this.alarmManager = (AlarmManager)var10001;
            Intent alarmIntent = new Intent(context, NotifierReceiver.class);
            alarmIntent.setAction(this.ACTION_BD_NOTIFICATION);
            PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.i("AlarmHelper", "Setting Alarm Interval to: " + notificationFrequency + " minutes");
            AlarmManager var10000 = this.alarmManager;
            if (var10000 == null) {

            }

            var10000.setRepeating(0, System.currentTimeMillis(), notificationFrequencyMs, pendingAlarmIntent);
            ComponentName receiver = new ComponentName(context, BootReceiver.class);
            context.getPackageManager().setComponentEnabledSetting(receiver, 1, 1);
        }
    }

    public final void cancelAlarm(@NotNull Context context) {
        Object var10001 = context.getSystemService("alarm");
        if (var10001 == null) {
        } else {
            this.alarmManager = (AlarmManager)var10001;
            Intent alarmIntent = new Intent(context, NotifierReceiver.class);
            alarmIntent.setAction(this.ACTION_BD_NOTIFICATION);
            PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 134217728);
            AlarmManager var10000 = this.alarmManager;
            if (var10000 == null) {
            }

            var10000.cancel(pendingAlarmIntent);
            ComponentName receiver = new ComponentName(context, BootReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver, 2, 1);
            Log.i("AlarmHelper", "Cancelling alarms");
        }
    }

    public final boolean checkAlarm(@NotNull Context context) {
        Intent alarmIntent = new Intent(context, NotifierReceiver.class);
        alarmIntent.setAction(this.ACTION_BD_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null;
    }
}
