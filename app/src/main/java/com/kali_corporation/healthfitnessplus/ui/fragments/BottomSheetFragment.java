package com.kali_corporation.healthfitnessplus.ui.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.activity.Water_MainActivity;
import com.kali_corporation.healthfitnessplus.sevice.helpers.SqliteHelper;
import com.kali_corporation.healthfitnessplus.sevice.utils.AppUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import com.kali_corporation.healthfitnessplus.sevice.helpers.AlarmHelper;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    /* access modifiers changed from: private */
    public String currentToneUri = "";
    /* access modifiers changed from: private */
    public String customTarget = "";
    private final Context mCtx;
    /* access modifiers changed from: private */
    public int notificFrequency;
    /* access modifiers changed from: private */
    public String notificMsg = "";
    /* access modifiers changed from: private */
    public SharedPreferences sharedPref;
    /* access modifiers changed from: private */
    public long sleepingTime;
    /* access modifiers changed from: private */
    public long wakeupTime;
    /* access modifiers changed from: private */
    public String weight = "";
    /* access modifiers changed from: private */
    public String workTime = "";

    View view;

    public BottomSheetFragment(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "mCtx");
        this.mCtx = context;
    }
    public final Context getMCtx() {
        return this.mCtx;
    }
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        return layoutInflater.inflate(R.layout.bottom_sheet_fragment, viewGroup, false);
    }

    public void onViewCreated(View vew, Bundle bundle) {
        view=vew;
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        SharedPreferences sharedPreferences = this.mCtx.getSharedPreferences(AppUtils.Companion.getUSERS_SHARED_PREF(), AppUtils.Companion.getPRIVATE_MODE());
        Intrinsics.checkExpressionValueIsNotNull(sharedPreferences, "mCtx.getSharedPreference…F, AppUtils.PRIVATE_MODE)");
        this.sharedPref = sharedPreferences;
        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.etWeight);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout, "etWeight");
        EditText editText = textInputLayout.getEditText();
        if (editText == null) {
            Intrinsics.throwNpe();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("");
        SharedPreferences sharedPreferences2 = this.sharedPref;
        if (sharedPreferences2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        sb.append(sharedPreferences2.getInt(AppUtils.Companion.getWEIGHT_KEY(), 0));
        editText.setText(sb.toString());
        TextInputLayout textInputLayout2 = (TextInputLayout) view.findViewById(R.id.etWorkTime);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout2, "etWorkTime");
        EditText editText2 = textInputLayout2.getEditText();
        if (editText2 == null) {
            Intrinsics.throwNpe();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        SharedPreferences sharedPreferences3 = this.sharedPref;
        if (sharedPreferences3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        sb2.append(sharedPreferences3.getInt(AppUtils.Companion.getWORK_TIME_KEY(), 0));
        editText2.setText(sb2.toString());
        TextInputLayout textInputLayout3 = (TextInputLayout) view.findViewById(R.id.etTarget);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout3, "etTarget");
        EditText editText3 = textInputLayout3.getEditText();
        if (editText3 == null) {
            Intrinsics.throwNpe();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        SharedPreferences sharedPreferences4 = this.sharedPref;
        if (sharedPreferences4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        sb3.append(sharedPreferences4.getInt(AppUtils.Companion.getTOTAL_INTAKE(), 0));
        editText3.setText(sb3.toString());
        TextInputLayout textInputLayout4 = (TextInputLayout) view.findViewById(R.id.etNotificationText);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout4, "etNotificationText");
        EditText editText4 = textInputLayout4.getEditText();
        if (editText4 == null) {
            Intrinsics.throwNpe();
        }
        SharedPreferences sharedPreferences5 = this.sharedPref;
        if (sharedPreferences5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        editText4.setText(sharedPreferences5.getString(AppUtils.Companion.getNOTIFICATION_MSG_KEY(), "Hey... Lets drink some water...."));
        SharedPreferences sharedPreferences6 = this.sharedPref;
        if (sharedPreferences6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        this.currentToneUri = sharedPreferences6.getString(AppUtils.Companion.getNOTIFICATION_TONE_URI_KEY(), RingtoneManager.getDefaultUri(2).toString());
        TextInputLayout textInputLayout5 = (TextInputLayout) view.findViewById(R.id.etRingtone);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout5, "etRingtone");
        EditText editText5 = textInputLayout5.getEditText();
        if (editText5 == null) {
            Intrinsics.throwNpe();
        }
        editText5.setText(RingtoneManager.getRingtone(this.mCtx, Uri.parse(this.currentToneUri)).getTitle(this.mCtx));
        ((RadioRealButtonGroup) view.findViewById(R.id.radioNotificItervel)).setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int i) {
                int i2 = 30;
                if (i != 0) {
                    if (i == 1) {
                        i2 = 45;
                    } else if (i == 2) {
                        i2 = 60;
                    }
                }
                notificFrequency = i2;
            }
        });
        SharedPreferences sharedPreferences7 = this.sharedPref;
        if (sharedPreferences7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        int i = sharedPreferences7.getInt(AppUtils.Companion.getNOTIFICATION_FREQUENCY_KEY(), 30);
        this.notificFrequency = i;
        if (i == 30) {
            RadioRealButtonGroup radioRealButtonGroup = (RadioRealButtonGroup) view.findViewById(R.id.radioNotificItervel);
            Intrinsics.checkExpressionValueIsNotNull(radioRealButtonGroup, "radioNotificItervel");
            radioRealButtonGroup.setPosition(0);
        } else if (i == 45) {
            RadioRealButtonGroup radioRealButtonGroup2 = (RadioRealButtonGroup) view.findViewById(R.id.radioNotificItervel);
            Intrinsics.checkExpressionValueIsNotNull(radioRealButtonGroup2, "radioNotificItervel");
            radioRealButtonGroup2.setPosition(1);
        } else if (i != 60) {
            RadioRealButtonGroup radioRealButtonGroup3 = (RadioRealButtonGroup) view.findViewById(R.id.radioNotificItervel);
            Intrinsics.checkExpressionValueIsNotNull(radioRealButtonGroup3, "radioNotificItervel");
            radioRealButtonGroup3.setPosition(0);
            this.notificFrequency = 30;
        } else {
            RadioRealButtonGroup radioRealButtonGroup4 = (RadioRealButtonGroup) view.findViewById(R.id.radioNotificItervel);
            Intrinsics.checkExpressionValueIsNotNull(radioRealButtonGroup4, "radioNotificItervel");
            radioRealButtonGroup4.setPosition(2);
        }
        TextInputLayout textInputLayout6 = (TextInputLayout) view.findViewById(R.id.etRingtone);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout6, "etRingtone");
        EditText editText6 = textInputLayout6.getEditText();
        if (editText6 == null) {
            Intrinsics.throwNpe();
        }
        editText6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                intent.putExtra("android.intent.extra.ringtone.TYPE", 2);
                intent.putExtra("android.intent.extra.ringtone.TITLE", "Select ringtone for notifications:");
                intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", false);
                intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                intent.putExtra("android.intent.extra.ringtone.EXISTING_URI",currentToneUri);
                startActivityForResult(intent, 999);
            }
        });
        SharedPreferences sharedPreferences8 = this.sharedPref;
        if (sharedPreferences8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        this.wakeupTime = sharedPreferences8.getLong(AppUtils.Companion.getWAKEUP_TIME(), 1558323000000L);
        SharedPreferences sharedPreferences9 = this.sharedPref;
        if (sharedPreferences9 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        this.sleepingTime = sharedPreferences9.getLong(AppUtils.Companion.getSLEEPING_TIME_KEY(), 1558369800000L);
        Calendar instance = Calendar.getInstance();
        Intrinsics.checkExpressionValueIsNotNull(instance, "cal");
        instance.setTimeInMillis(this.wakeupTime);
        TextInputLayout textInputLayout7 = (TextInputLayout) view.findViewById(R.id.etWakeUpTime);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout7, "etWakeUpTime");
        EditText editText7 = textInputLayout7.getEditText();
        if (editText7 == null) {
            Intrinsics.throwNpe();
        }
        String format = String.format("%02d:%02d", Arrays.copyOf(new Object[]{Integer.valueOf(instance.get(11)), Integer.valueOf(instance.get(12))}, 2));
        Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)");
        editText7.setText(format);
        instance.setTimeInMillis(this.sleepingTime);
        TextInputLayout textInputLayout8 = (TextInputLayout) view.findViewById(R.id.etSleepTime);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout8, "etSleepTime");
        EditText editText8 = textInputLayout8.getEditText();
        if (editText8 == null) {
            Intrinsics.throwNpe();
        }
        String format2 = String.format("%02d:%02d", Arrays.copyOf(new Object[]{Integer.valueOf(instance.get(11)), Integer.valueOf(instance.get(12))}, 2));
        Intrinsics.checkExpressionValueIsNotNull(format2, "java.lang.String.format(format, *args)");
        editText8.setText(format2);
        TextInputLayout textInputLayout9 = (TextInputLayout) view.findViewById(R.id.etWakeUpTime);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout9, "etWakeUpTime");
        EditText editText9 = textInputLayout9.getEditText();
        if (editText9 == null) {
            Intrinsics.throwNpe();
        }
        editText9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                Intrinsics.checkExpressionValueIsNotNull(instance, "calendar");
                instance.setTimeInMillis(wakeupTime);
                TimePickerDialog timePickerDialog = new TimePickerDialog(mCtx, new TimePickerDialog.OnTimeSetListener() {

                    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
                        Calendar instance = Calendar.getInstance();
                        instance.set(11, i);
                        instance.set(12, i2);
                        Intrinsics.checkExpressionValueIsNotNull(instance, "time");
                        wakeupTime = instance.getTimeInMillis();
                        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.etWakeUpTime);
                        Intrinsics.checkExpressionValueIsNotNull(textInputLayout, "etWakeUpTime");
                        EditText editText = textInputLayout.getEditText();
                        if (editText == null) {
                            Intrinsics.throwNpe();
                        }
                        String format = String.format("%02d:%02d", Arrays.copyOf(new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}, 2));
                        Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)");
                        editText.setText(format);
                    }
                }, instance.get(11), instance.get(12), false);
                timePickerDialog.setTitle("Select Wakeup Time");
                timePickerDialog.show();
            }
        });
        TextInputLayout textInputLayout10 = (TextInputLayout) view.findViewById(R.id.etSleepTime);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout10, "etSleepTime");
        EditText editText10 = textInputLayout10.getEditText();
        if (editText10 == null) {
            Intrinsics.throwNpe();
        }
        editText10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                Intrinsics.checkExpressionValueIsNotNull(instance, "calendar");
                instance.setTimeInMillis(sleepingTime);
                TimePickerDialog timePickerDialog = new TimePickerDialog(mCtx, new TimePickerDialog.OnTimeSetListener() {

                    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
                        Calendar instance = Calendar.getInstance();
                        instance.set(11, i);
                        instance.set(12, i2);
                        Intrinsics.checkExpressionValueIsNotNull(instance, "time");
                        sleepingTime = instance.getTimeInMillis();
                        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.etSleepTime);
                        Intrinsics.checkExpressionValueIsNotNull(textInputLayout, "etSleepTime");
                        EditText editText = textInputLayout.getEditText();
                        if (editText == null) {
                            Intrinsics.throwNpe();
                        }
                        String format = String.format("%02d:%02d", Arrays.copyOf(new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}, 2));
                        Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)");
                        editText.setText(format);
                    }
                }, instance.get(11), instance.get(12), false);
                timePickerDialog.setTitle("Select Sleeping Time");
                timePickerDialog.show();
            }
        });
        ((Button) view.findViewById(R.id.btnUpdate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = sharedPref.getInt(AppUtils.Companion.getTOTAL_INTAKE(), 0);
                TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.etWeight);
                Intrinsics.checkExpressionValueIsNotNull(textInputLayout, "etWeight");
                EditText editText = textInputLayout.getEditText();
                if (editText == null) {
                    Intrinsics.throwNpe();
                }
                Intrinsics.checkExpressionValueIsNotNull(editText, "etWeight.editText!!");
                weight = editText.getText().toString();
                TextInputLayout textInputLayout2 = (TextInputLayout) view.findViewById(R.id.etWorkTime);
                Intrinsics.checkExpressionValueIsNotNull(textInputLayout2, "etWorkTime");
                EditText editText2 = textInputLayout2.getEditText();
                if (editText2 == null) {
                    Intrinsics.throwNpe();
                }
                Intrinsics.checkExpressionValueIsNotNull(editText2, "etWorkTime.editText!!");
                workTime = editText2.getText().toString();
                TextInputLayout textInputLayout3 = (TextInputLayout) view.findViewById(R.id.etNotificationText);
                Intrinsics.checkExpressionValueIsNotNull(textInputLayout3, "etNotificationText");
                EditText editText3 = textInputLayout3.getEditText();
                if (editText3 == null) {
                    Intrinsics.throwNpe();
                }
                Intrinsics.checkExpressionValueIsNotNull(editText3, "etNotificationText.editText!!");
                notificMsg = editText3.getText().toString();
                TextInputLayout textInputLayout4 = (TextInputLayout) view.findViewById(R.id.etTarget);
                Intrinsics.checkExpressionValueIsNotNull(textInputLayout4, "etTarget");
                EditText editText4 = textInputLayout4.getEditText();
                if (editText4 == null) {
                    Intrinsics.throwNpe();
                }
                Intrinsics.checkExpressionValueIsNotNull(editText4, "etTarget.editText!!");
                customTarget = editText4.getText().toString();
                if (TextUtils.isEmpty(notificMsg)) {
                    Toast.makeText(getMCtx(), "Please a notification message", 0).show();
                } else if (notificFrequency == 0) {
                    Toast.makeText(getMCtx(), "Please select a notification frequency", 0).show();
                } else if (TextUtils.isEmpty(weight)) {
                    Toast.makeText(getMCtx(), "Please input your weight", 0).show();
                } else if (Integer.parseInt(weight) > 200 || Integer.parseInt(weight) < 20) {
                    Toast.makeText(getMCtx(), "Please input a valid weight", 0).show();
                } else if (TextUtils.isEmpty(workTime)) {
                    Toast.makeText(getMCtx(), "Please input your workout time", 0).show();
                } else if (Integer.parseInt(workTime) > 500 || Integer.parseInt(workTime) < 0) {
                    Toast.makeText(getMCtx(), "Please input a valid workout time", 0).show();
                } else if (TextUtils.isEmpty(customTarget)) {
                    Toast.makeText(getMCtx(), "Please input your custom target", 0).show();
                } else {
                    SharedPreferences.Editor edit = sharedPref.edit();
                    edit.putInt(AppUtils.Companion.getWEIGHT_KEY(), Integer.parseInt(weight));
                    edit.putInt(AppUtils.Companion.getWORK_TIME_KEY(), Integer.parseInt(workTime));
                    edit.putLong(AppUtils.Companion.getWAKEUP_TIME(), wakeupTime);
                    edit.putLong(AppUtils.Companion.getSLEEPING_TIME_KEY(), sleepingTime);
                    edit.putString(AppUtils.Companion.getNOTIFICATION_MSG_KEY(), notificMsg);
                    edit.putInt(AppUtils.Companion.getNOTIFICATION_FREQUENCY_KEY(), notificFrequency);
                    SqliteHelper sqliteHelper = new SqliteHelper(getMCtx());
                    if (i != Integer.parseInt(customTarget)) {
                        edit.putInt(AppUtils.Companion.getTOTAL_INTAKE(), Integer.parseInt(customTarget));
                        String currentDate = AppUtils.Companion.getCurrentDate();
                        if (currentDate == null) {
                            Intrinsics.throwNpe();
                        }
                        sqliteHelper.updateTotalIntake(currentDate, Integer.parseInt(customTarget));
                    } else {
                        double calculateIntake = AppUtils.Companion.calculateIntake(Integer.parseInt(weight), Integer.parseInt(workTime));
                        DecimalFormat decimalFormat = new DecimalFormat("#");
                        decimalFormat.setRoundingMode(RoundingMode.CEILING);
                        String total_intake = AppUtils.Companion.getTOTAL_INTAKE();
                        String format = decimalFormat.format(calculateIntake);
                        Intrinsics.checkExpressionValueIsNotNull(format, "df.format(totalIntake)");
                        edit.putInt(total_intake, Integer.parseInt(format));
                        String currentDate2 = AppUtils.Companion.getCurrentDate();
                        if (currentDate2 == null) {
                            Intrinsics.throwNpe();
                        }
                        String format2 = decimalFormat.format(calculateIntake);
                        Intrinsics.checkExpressionValueIsNotNull(format2, "df.format(totalIntake)");
                        sqliteHelper.updateTotalIntake(currentDate2, Integer.parseInt(format2));
                    }
                    edit.apply();
                    Toast.makeText(getMCtx(), "Values updated successfully", 0).show();
                    AlarmHelper alarmHelper = new AlarmHelper();
                    alarmHelper.cancelAlarm(getMCtx());
                    alarmHelper.setAlarm(getMCtx(), (long) sharedPref.getInt(AppUtils.Companion.getNOTIFICATION_FREQUENCY_KEY(), 30));
                    BottomSheetFragment.this.dismiss();
                    Water_MainActivity water_MainActivity = (Water_MainActivity) BottomSheetFragment.this.getActivity();
                    if (water_MainActivity == null) {
                        Intrinsics.throwNpe();
                    }
                    water_MainActivity.updateValues();
                }
            }
        });
    }



    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == 999) {
            if (intent == null) {
                Intrinsics.throwNpe();
            }
            Parcelable parcelableExtra = intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (parcelableExtra != null) {
                Uri uri = (Uri) parcelableExtra;
                this.currentToneUri = uri.toString();
                SharedPreferences sharedPreferences = this.sharedPref;
                if (sharedPreferences == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
                }
                sharedPreferences.edit().putString(AppUtils.Companion.getNOTIFICATION_TONE_URI_KEY(), this.currentToneUri).apply();
                Ringtone ringtone = RingtoneManager.getRingtone(this.mCtx, uri);
                TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.etRingtone);
                Intrinsics.checkExpressionValueIsNotNull(textInputLayout, "etRingtone");
                EditText editText = textInputLayout.getEditText();
                if (editText == null) {
                    Intrinsics.throwNpe();
                }
                editText.setText(ringtone.getTitle(this.mCtx));
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.net.Uri");
        }
    }

}
