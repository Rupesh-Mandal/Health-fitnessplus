package com.kali_corporation.healthfitnessplus.sevice.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kotlin.jvm.internal.Intrinsics;

public class AppUtils {
    public static String USERS_SHARED_PREF = "user_pref";
    public static int PRIVATE_MODE = 0;
    public static String WEIGHT_KEY = "weight";
    public static String WORK_TIME_KEY = "worktime";
    public static String TOTAL_INTAKE = "totalintake";
    public static String NOTIFICATION_STATUS_KEY = "notificationstatus";
    public static String NOTIFICATION_FREQUENCY_KEY = "notificationfrequency";
    public static String NOTIFICATION_MSG_KEY = "notificationmsg";
    public static String SLEEPING_TIME_KEY = "sleepingtime";
    public static String WAKEUP_TIME = "wakeuptime";
    public static String NOTIFICATION_TONE_URI_KEY = "notificationtone";
    public static String FIRST_RUN_KEY = "firstrun";

    /* compiled from: AppUtils.kt */
    public static final class Companion {
        private Companion() {
        }
        public final static double calculateIntake(int i, int i2) {
            return (((double) (i * 100)) / 3.0d) + ((double) ((i2 / 6) * 7));
        }

        public static final String getCurrentDate() {
            Calendar instance = Calendar.getInstance();
            Intrinsics.checkExpressionValueIsNotNull(instance, "Calendar.getInstance()");
            return new SimpleDateFormat("dd-MM-yyyy").format(instance.getTime());
        }

        public static final String getUSERS_SHARED_PREF() {
            return USERS_SHARED_PREF;
        }

        public static final int getPRIVATE_MODE() {
            return PRIVATE_MODE;
        }

        public static final String getWEIGHT_KEY() {
            return AppUtils.WEIGHT_KEY;
        }

        public static final String getWORK_TIME_KEY() {
            return AppUtils.WORK_TIME_KEY;
        }

        public static final String getTOTAL_INTAKE() {
            return AppUtils.TOTAL_INTAKE;
        }

        public static final String getNOTIFICATION_STATUS_KEY() {
            return AppUtils.NOTIFICATION_STATUS_KEY;
        }

        public final static String getNOTIFICATION_FREQUENCY_KEY() {
            return AppUtils.NOTIFICATION_FREQUENCY_KEY;
        }

        public static final String getNOTIFICATION_MSG_KEY() {
            return AppUtils.NOTIFICATION_MSG_KEY;
        }

        public final static String getSLEEPING_TIME_KEY() {
            return AppUtils.SLEEPING_TIME_KEY;
        }

        public final static String getWAKEUP_TIME() {
            return AppUtils.WAKEUP_TIME;
        }

        public final static String getNOTIFICATION_TONE_URI_KEY() {
            return AppUtils.NOTIFICATION_TONE_URI_KEY;
        }

        public final static String getFIRST_RUN_KEY() {
            return AppUtils.FIRST_RUN_KEY;
        }
    }
}
