package com.kali_corporation.healthfitnessplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.sevice.utils.AppUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class InitUserInfoActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;
    private SharedPreferences sharedPref;
    private long wakeupTime = 0;
    private long sleepingTime = 0;
    private String weight = "";
    private String workTime = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = getWindow();
            Intrinsics.checkExpressionValueIsNotNull(window, "window");
            View decorView = window.getDecorView();
            Intrinsics.checkExpressionValueIsNotNull(decorView, "window.decorView");
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_init_user_info);
        SharedPreferences sharedPreferences = getSharedPreferences(AppUtils.Companion.getUSERS_SHARED_PREF(), AppUtils.Companion.getPRIVATE_MODE());
        Intrinsics.checkExpressionValueIsNotNull(sharedPreferences, "getSharedPreferences(App…F, AppUtils.PRIVATE_MODE)");
        sharedPref = sharedPreferences;
        sharedPref = sharedPreferences;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        wakeupTime = sharedPreferences.getLong(AppUtils.Companion.getWAKEUP_TIME(), 1558323000000L);
        SharedPreferences sharedPreferences2 = sharedPref;
        if (sharedPreferences2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        sleepingTime = sharedPreferences2.getLong(AppUtils.Companion.getSLEEPING_TIME_KEY(), 1558369800000L);

        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.etWakeUpTime);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout, "etWakeUpTime");
        EditText editText = textInputLayout.getEditText();
        if (editText == null) {
            Intrinsics.throwNpe();
        }
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                Intrinsics.checkExpressionValueIsNotNull(instance, "calendar");
                instance.setTimeInMillis(wakeupTime);
                TimePickerDialog timePickerDialog = new TimePickerDialog(InitUserInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
                        Calendar instance = Calendar.getInstance();
                        instance.set(11, i);
                        instance.set(12, i2);
                        Intrinsics.checkExpressionValueIsNotNull(instance, "time");
                        wakeupTime = instance.getTimeInMillis();
                        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.etWakeUpTime);
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
        TextInputLayout textInputLayout2 = (TextInputLayout) findViewById(R.id.etSleepTime);
        Intrinsics.checkExpressionValueIsNotNull(textInputLayout2, "etSleepTime");
        EditText editText2 = textInputLayout2.getEditText();
        if (editText2 == null) {
            Intrinsics.throwNpe();
        }
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                Intrinsics.checkExpressionValueIsNotNull(instance, "calendar");
                instance.setTimeInMillis(sleepingTime);
                TimePickerDialog timePickerDialog = new TimePickerDialog(InitUserInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
                        Calendar instance = Calendar.getInstance();
                        instance.set(11, i);
                        instance.set(12, i2);
                        Intrinsics.checkExpressionValueIsNotNull(instance, "time");
                        sleepingTime = instance.getTimeInMillis();
                        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.etSleepTime);
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



        findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object systemService = getSystemService("input_method");
                if (systemService != null) {
                    RelativeLayout relativeLayout = findViewById(R.id.init_user_info_parent_layout);
                    Intrinsics.checkExpressionValueIsNotNull(relativeLayout, "init_user_info_parent_layout");
                    ((InputMethodManager) systemService).hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
                    TextInputLayout textInputLayout = findViewById(R.id.etWeight);
                    Intrinsics.checkExpressionValueIsNotNull(textInputLayout, "etWeight");
                    EditText editText = textInputLayout.getEditText();
                    if (editText == null) {
                        Intrinsics.throwNpe();
                    }
                    Intrinsics.checkExpressionValueIsNotNull(editText, "etWeight.editText!!");
                    weight = editText.getText().toString();

                    TextInputLayout textInputLayout2 = findViewById(R.id.etWorkTime);
                    Intrinsics.checkExpressionValueIsNotNull(textInputLayout2, "etWorkTime");
                    EditText editText2 = textInputLayout2.getEditText();
                    if (editText2 == null) {
                        Intrinsics.throwNpe();
                    }
                    Intrinsics.checkExpressionValueIsNotNull(editText2, "etWorkTime.editText!!");
                    workTime = editText2.getText().toString();
                    if (TextUtils.isEmpty(weight)) {
                        Snackbar.make(v, (CharSequence) "Please input your weight", -1).show();
                    } else if (Integer.parseInt(weight) > 200 || Integer.parseInt(weight) < 20) {
                        Snackbar.make(v, (CharSequence) "Please input a valid weight", -1).show();
                    } else if (TextUtils.isEmpty(workTime)) {
                        Snackbar.make(v, (CharSequence) "Please input your workout time", -1).show();
                    } else if (Integer.parseInt(workTime) > 500 || Integer.parseInt(workTime) < 0) {
                        Snackbar.make(v, (CharSequence) "Please input a valid workout time", -1).show();
                    } else {
                        SharedPreferences.Editor edit = sharedPref.edit();
                        edit.putInt(AppUtils.Companion.getWEIGHT_KEY(), Integer.parseInt(weight));
                        edit.putInt(AppUtils.Companion.getWORK_TIME_KEY(), Integer.parseInt(workTime));
                        edit.putLong(AppUtils.Companion.getWAKEUP_TIME(), wakeupTime);
                        edit.putLong(AppUtils.Companion.getSLEEPING_TIME_KEY(), sleepingTime);
                        edit.putBoolean(AppUtils.Companion.getFIRST_RUN_KEY(), false);
                        double calculateIntake = AppUtils.Companion.calculateIntake(Integer.parseInt(weight), Integer.parseInt(workTime));
                        DecimalFormat decimalFormat = new DecimalFormat("#");
                        decimalFormat.setRoundingMode(RoundingMode.CEILING);
                        String total_intake = AppUtils.Companion.getTOTAL_INTAKE();
                        String format = decimalFormat.format(calculateIntake);
                        Intrinsics.checkExpressionValueIsNotNull(format, "df.format(totalIntake)");
                        edit.putInt(total_intake, Integer.parseInt(format));
                        edit.apply();
                        startActivity(new Intent(InitUserInfoActivity.this, Water_MainActivity.class));
                        finish();
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Window window = getWindow();
        Intrinsics.checkExpressionValueIsNotNull(window, "this.window");
        Snackbar.make(window.getDecorView().findViewById(16908290), (CharSequence) "Please click BACK again to exit", -1).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }
}