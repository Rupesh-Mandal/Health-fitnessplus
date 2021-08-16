package com.kali_corporation.healthfitnessplus.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.ui.fragments.BottomSheetFragment;
import com.kali_corporation.healthfitnessplus.sevice.helpers.AlarmHelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kali_corporation.healthfitnessplus.sevice.helpers.SqliteHelper;
import com.kali_corporation.healthfitnessplus.sevice.utils.AppUtils;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import params.com.stepprogressview.StepProgressView;

public class Water_MainActivity extends AppCompatActivity implements View.OnClickListener {
    TypedValue typedValue;
    AlarmHelper alarmHelper;
    /* access modifiers changed from: private */
    public String dateNow;
    /* access modifiers changed from: private */
    public boolean doubleBackToExitPressedOnce;
    /* access modifiers changed from: private */
    public int inTook;
    /* access modifiers changed from: private */
    public boolean notificStatus;
    /* access modifiers changed from: private */
    public Integer selectedOption;
    /* access modifiers changed from: private */
    public SharedPreferences sharedPref;
    /* access modifiers changed from: private */
    public Snackbar snackbar;
    /* access modifiers changed from: private */
    public SqliteHelper sqliteHelper;
    /* access modifiers changed from: private */
    public int totalIntake;
    Snackbar access$getSnackbar$p;
    LinearLayout linearLayout;
    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    LinearLayout linearLayout4;
    LinearLayout linearLayout5;
    LinearLayout linearLayout6;

    private void initView() {
        linearLayout = (LinearLayout) findViewById(R.id.op50ml);
        linearLayout2 = (LinearLayout) findViewById(R.id.op100ml);
        linearLayout3 = (LinearLayout) findViewById(R.id.op150ml);
        linearLayout4 = (LinearLayout) findViewById(R.id.op200ml);
        linearLayout5 = (LinearLayout) findViewById(R.id.op250ml);
        linearLayout6 = (LinearLayout) findViewById(R.id.opCustom);

    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.water_activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(AppUtils.Companion.getUSERS_SHARED_PREF(), AppUtils.Companion.getPRIVATE_MODE());
        Intrinsics.checkExpressionValueIsNotNull(sharedPreferences, "getSharedPreferences(App…F, AppUtils.PRIVATE_MODE)");
        this.sharedPref = sharedPreferences;
        Context context = this;
        this.sqliteHelper = new SqliteHelper(context);
        SharedPreferences sharedPreferences2 = this.sharedPref;
        if (sharedPreferences2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        int i = sharedPreferences2.getInt(AppUtils.Companion.getTOTAL_INTAKE(), 0);
        this.totalIntake = i;
        if (i <= 0) {
            startActivity(new Intent(context, InitUserInfoActivity.class));
            finish();
        }
        String currentDate = AppUtils.Companion.getCurrentDate();
        if (currentDate == null) {
            Intrinsics.throwNpe();
        }
        this.dateNow = currentDate;
    }

    public final void updateValues() {
        SharedPreferences sharedPreferences = this.sharedPref;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        this.totalIntake = sharedPreferences.getInt(AppUtils.Companion.getTOTAL_INTAKE(), 0);
        SqliteHelper sqliteHelper2 = this.sqliteHelper;
        if (sqliteHelper2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sqliteHelper");
        }
        String str = this.dateNow;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dateNow");
        }
        int intook = sqliteHelper2.getIntook(str);
        this.inTook = intook;
        setWaterLevel(intook, this.totalIntake);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        typedValue = new TypedValue();
        Context applicationContext = getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "applicationContext");
        applicationContext.getTheme().resolveAttribute(16843534, typedValue, true);
        SharedPreferences sharedPreferences = this.sharedPref;
        if (sharedPreferences == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
        }
        this.notificStatus = sharedPreferences.getBoolean(AppUtils.Companion.getNOTIFICATION_STATUS_KEY(), true);
        alarmHelper = new AlarmHelper();
        Context context = this;
        if (!alarmHelper.checkAlarm(context) && this.notificStatus) {
            ((FloatingActionButton) findViewById(R.id.btnNotific)).setImageDrawable(getDrawable(R.drawable.ic_bell));
            SharedPreferences sharedPreferences2 = this.sharedPref;
            if (sharedPreferences2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
            }
            alarmHelper.setAlarm(context, (long) sharedPreferences2.getInt(AppUtils.Companion.getNOTIFICATION_FREQUENCY_KEY(), 30));
        }
        if (this.notificStatus) {
            ((FloatingActionButton) findViewById(R.id.btnNotific)).setImageDrawable(getDrawable(R.drawable.ic_bell));
        } else {
            ((FloatingActionButton) findViewById(R.id.btnNotific)).setImageDrawable(getDrawable(R.drawable.ic_bell_disabled));
        }
        SqliteHelper sqliteHelper2 = this.sqliteHelper;
        if (sqliteHelper2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sqliteHelper");
        }
        String str = this.dateNow;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dateNow");
        }
        sqliteHelper2.addAll(str, 0, this.totalIntake);
        updateValues();
        initView();
        ((ImageView) findViewById(R.id.btnMenu)).setOnClickListener(this);
        ((FloatingActionButton) findViewById(R.id.fabAdd)).setOnClickListener(this);
        ((FloatingActionButton) findViewById(R.id.btnNotific)).setOnClickListener(this);
        ((FloatingActionButton) findViewById(R.id.btnStats)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.op50ml)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.op100ml)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.op150ml)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.op200ml)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.op250ml)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.opCustom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(snackbar == null || (access$getSnackbar$p = snackbar) == null)) {
                    access$getSnackbar$p.dismiss();
                }
                View inflate = LayoutInflater.from(Water_MainActivity.this).inflate(R.layout.custom_input_dialog, (ViewGroup) null);
                AlertDialog.Builder builder = new AlertDialog.Builder(Water_MainActivity.this);
                builder.setView(inflate);
                View findViewById = inflate.findViewById(R.id.etCustomInput);
                if (findViewById != null) {
                    final TextInputLayout textInputLayout = (TextInputLayout) findViewById;
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditText editText = textInputLayout.getEditText();
                            if (editText == null) {
                                Intrinsics.throwNpe();
                            }
                            Intrinsics.checkExpressionValueIsNotNull(editText, "userInput.editText!!");
                            String obj = editText.getText().toString();
                            if (!TextUtils.isEmpty(obj)) {
                                TextView textView = (TextView) findViewById(R.id.tvCustom);
                                Intrinsics.checkExpressionValueIsNotNull(textView, "tvCustom");
                                textView.setText(obj + " ml");
                                selectedOption = Integer.valueOf(Integer.parseInt(obj));
                            }
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

                    AlertDialog create = builder.create();
                    Intrinsics.checkExpressionValueIsNotNull(create, "alertDialogBuilder.create()");
                    create.show();
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout, "op50ml");
                    linearLayout.setBackground(getDrawable(typedValue.resourceId));
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout2, "op100ml");
                    linearLayout2.setBackground(getDrawable(typedValue.resourceId));
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout3, "op150ml");
                    linearLayout3.setBackground(getDrawable(typedValue.resourceId));
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout4, "op200ml");
                    linearLayout4.setBackground(getDrawable(typedValue.resourceId));
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout5, "op250ml");
                    linearLayout5.setBackground(getDrawable(typedValue.resourceId));
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout6, "opCustom");
                    linearLayout6.setBackground(getDrawable(R.drawable.option_select_bg));
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type com.google.android.material.textfield.TextInputLayout");

            }
        });
    }


    /* access modifiers changed from: private */
    public final void setWaterLevel(int i, int i2) {
        YoYo.with(Techniques.SlideInDown).duration(500).playOn((TextView) findViewById(R.id.tvIntook));
        TextView textView = (TextView) findViewById(R.id.tvIntook);
        Intrinsics.checkExpressionValueIsNotNull(textView, "tvIntook");
        textView.setText(String.valueOf(i));
        TextView textView2 = (TextView) findViewById(R.id.tvTotalIntake);
        Intrinsics.checkExpressionValueIsNotNull(textView2, "tvTotalIntake");
        textView2.setText('/' + i2 + " ml");
        YoYo.with(Techniques.Pulse).duration(500).playOn((StepProgressView) findViewById(R.id.intakeProgress));
        ((StepProgressView) findViewById(R.id.intakeProgress)).setCurrentProgress((int) ((((float) i) / ((float) i2)) * ((float) 100)));
        if ((i * 100) / i2 > 140) {
            Snackbar.make((View) (ConstraintLayout) findViewById(R.id.main_activity_parent), (CharSequence) "You achieved the goal", -1).show();
        }
    }

    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMenu:
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(this);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                break;
            case R.id.fabAdd:
                if (selectedOption != null) {
                    if ((inTook * 100) / totalIntake <= 140) {
                        SqliteHelper access$getSqliteHelper$p = sqliteHelper;
                        String access$getDateNow$p = dateNow;
                        Integer access$getSelectedOption$p = selectedOption;
                        if (access$getSelectedOption$p == null) {
                            Intrinsics.throwNpe();
                        }
                        if (access$getSqliteHelper$p.addIntook(access$getDateNow$p, access$getSelectedOption$p.intValue()) > 0) {
                            int access$getInTook$p = inTook;
                            Integer access$getSelectedOption$p2 = selectedOption;
                            if (access$getSelectedOption$p2 == null) {
                                Intrinsics.throwNpe();
                            }
                            inTook = access$getInTook$p + access$getSelectedOption$p2.intValue();
                            setWaterLevel(inTook, totalIntake);
                            Snackbar.make(v, (CharSequence) "Your water intake was saved...!!", -1).show();
                        }
                    } else {
                        Snackbar.make(v, (CharSequence) "You already achieved the goal", -1).show();
                    }
                    selectedOption = null;
                    TextView textView = (TextView) findViewById(R.id.tvCustom);
                    Intrinsics.checkExpressionValueIsNotNull(textView, "tvCustom");
                    textView.setText("Custom");
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.op50ml);
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout, "op50ml");
                    linearLayout.setBackground(getDrawable(typedValue.resourceId));
                    LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.op100ml);
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout2, "op100ml");
                    linearLayout2.setBackground(getDrawable(typedValue.resourceId));
                    LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.op150ml);
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout3, "op150ml");
                    linearLayout3.setBackground(getDrawable(typedValue.resourceId));
                    LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.op200ml);
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout4, "op200ml");
                    linearLayout4.setBackground(getDrawable(typedValue.resourceId));
                    LinearLayout linearLayout5 = (LinearLayout) findViewById(R.id.op250ml);
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout5, "op250ml");
                    linearLayout5.setBackground(getDrawable(typedValue.resourceId));
                    LinearLayout linearLayout6 = (LinearLayout) findViewById(R.id.opCustom);
                    Intrinsics.checkExpressionValueIsNotNull(linearLayout6, "opCustom");
                    linearLayout6.setBackground(getDrawable(typedValue.resourceId));
                    return;
                }
                YoYo.with(Techniques.Shake).duration(700).playOn((CardView) findViewById(R.id.cardView));
                Snackbar.make(v, (CharSequence) "Please select an option", -1).show();
                break;
            case R.id.btnNotific:
                notificStatus = !notificStatus;
                sharedPref.edit().putBoolean(AppUtils.Companion.getNOTIFICATION_STATUS_KEY(), notificStatus).apply();
                if (notificStatus) {
                    ((FloatingActionButton) findViewById(R.id.btnNotific)).setImageDrawable(getDrawable(R.drawable.ic_bell));
                    Snackbar.make(v, (CharSequence) "Notification Enabled..", -1).show();
                    AlarmHelper alarmHelper = this.alarmHelper;
                    alarmHelper.setAlarm(Water_MainActivity.this, (long) sharedPref.getInt(AppUtils.Companion.getNOTIFICATION_FREQUENCY_KEY(), 30));
                    return;
                }
                ((FloatingActionButton) findViewById(R.id.btnNotific)).setImageDrawable(getDrawable(R.drawable.ic_bell_disabled));
                Snackbar.make(v, (CharSequence) "Notification Disabled..", -1).show();
                alarmHelper.cancelAlarm(Water_MainActivity.this);
                break;
            case R.id.btnStats:
                startActivity(new Intent(Water_MainActivity.this, StatsActivity.class));
                break;
            case R.id.op50ml:
                if (!(snackbar == null || (access$getSnackbar$p = snackbar) == null)) {
                    access$getSnackbar$p.dismiss();
                }
                selectedOption = 50;
                Intrinsics.checkExpressionValueIsNotNull(linearLayout, "op50ml");
                linearLayout.setBackground(getDrawable(R.drawable.option_select_bg));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout2, "op100ml");
                linearLayout2.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout3, "op150ml");
                linearLayout3.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout4, "op200ml");
                linearLayout4.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout5, "op250ml");
                linearLayout5.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout6, "opCustom");
                linearLayout6.setBackground(getDrawable(typedValue.resourceId));
                break;
            case R.id.op100ml:
                if (!(snackbar == null || (access$getSnackbar$p = snackbar) == null)) {
                    access$getSnackbar$p.dismiss();
                }
                selectedOption = 100;
                Intrinsics.checkExpressionValueIsNotNull(linearLayout, "op50ml");
                linearLayout.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout2, "op100ml");
                linearLayout2.setBackground(getDrawable(R.drawable.option_select_bg));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout3, "op150ml");
                linearLayout3.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout4, "op200ml");
                linearLayout4.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout5, "op250ml");
                linearLayout5.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout6, "opCustom");
                linearLayout6.setBackground(getDrawable(typedValue.resourceId));
                break;
            case R.id.op150ml:
                if (!(snackbar == null || (access$getSnackbar$p = snackbar) == null)) {
                    access$getSnackbar$p.dismiss();
                }
                selectedOption = 150;
                Intrinsics.checkExpressionValueIsNotNull(linearLayout, "op50ml");
                linearLayout.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout2, "op100ml");
                linearLayout2.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout3, "op150ml");
                linearLayout3.setBackground(getDrawable(R.drawable.option_select_bg));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout4, "op200ml");
                linearLayout4.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout5, "op250ml");
                linearLayout5.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout6, "opCustom");
                linearLayout6.setBackground(getDrawable(typedValue.resourceId));
                break;
            case R.id.op200ml:
                if (!(snackbar == null || (access$getSnackbar$p = snackbar) == null)) {
                    access$getSnackbar$p.dismiss();
                }
                selectedOption = Integer.valueOf(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                Intrinsics.checkExpressionValueIsNotNull(linearLayout, "op50ml");
                linearLayout.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout2, "op100ml");
                linearLayout2.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout3, "op150ml");
                linearLayout3.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout4, "op200ml");
                linearLayout4.setBackground(getDrawable(R.drawable.option_select_bg));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout5, "op250ml");
                linearLayout5.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout6, "opCustom");
                linearLayout6.setBackground(getDrawable(typedValue.resourceId));
                break;
            case R.id.op250ml:
                if (!(snackbar == null || (access$getSnackbar$p = snackbar) == null)) {
                    access$getSnackbar$p.dismiss();
                }
                selectedOption = Integer.valueOf(ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                Intrinsics.checkExpressionValueIsNotNull(linearLayout, "op50ml");
                linearLayout.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout2, "op100ml");
                linearLayout2.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout3, "op150ml");
                linearLayout3.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout4, "op200ml");
                linearLayout4.setBackground(getDrawable(typedValue.resourceId));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout5, "op250ml");
                linearLayout5.setBackground(getDrawable(R.drawable.option_select_bg));
                Intrinsics.checkExpressionValueIsNotNull(linearLayout6, "opCustom");
                linearLayout6.setBackground(getDrawable(typedValue.resourceId));
                break;
        }
    }
}
