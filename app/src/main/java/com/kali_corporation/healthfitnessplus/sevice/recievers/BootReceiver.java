package com.kali_corporation.healthfitnessplus.sevice.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.kali_corporation.healthfitnessplus.sevice.helpers.AlarmHelper;
import com.kali_corporation.healthfitnessplus.sevice.utils.AppUtils;

import kotlin.jvm.internal.Intrinsics;

public final class BootReceiver extends BroadcastReceiver {
    private final AlarmHelper alarm = new AlarmHelper();

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && Intrinsics.areEqual((Object) intent.getAction(), (Object) "android.intent.action.BOOT_COMPLETED")) {
            if (context == null) {
                Intrinsics.throwNpe();
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtils.Companion.getUSERS_SHARED_PREF(), AppUtils.Companion.getPRIVATE_MODE());
            int i = sharedPreferences.getInt(AppUtils.Companion.getNOTIFICATION_FREQUENCY_KEY(), 60);
            boolean z = sharedPreferences.getBoolean("notifications_new_message", true);
            this.alarm.cancelAlarm(context);
            if (z) {
                this.alarm.setAlarm(context, (long) i);
            }
        }
    }
}
