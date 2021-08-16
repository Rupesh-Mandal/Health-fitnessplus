package com.kali_corporation.healthfitnessplus.sevice.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.sevice.helpers.NotificationHelper;
import com.kali_corporation.healthfitnessplus.sevice.utils.AppUtils;

import kotlin.jvm.internal.Intrinsics;


public final class NotifierReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppUtils.Companion.getUSERS_SHARED_PREF(), AppUtils.Companion.getPRIVATE_MODE());
        String string = sharedPreferences.getString(AppUtils.Companion.getNOTIFICATION_TONE_URI_KEY(), RingtoneManager.getDefaultUri(2).toString());
        String string2 = context.getResources().getString(R.string.app_name);
        Intrinsics.checkExpressionValueIsNotNull(string2, "context.resources.getString(R.string.app_name)");
        String string3 = sharedPreferences.getString(AppUtils.Companion.getNOTIFICATION_MSG_KEY(), context.getResources().getString(R.string.pref_notification_message_value));
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.notify(1, string3 != null ? notificationHelper.getNotification(string2, string3, string) : null);
    }
}
