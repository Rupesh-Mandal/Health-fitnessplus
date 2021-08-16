package com.kali_corporation.healthfitnessplus.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.sevice.helpers.SqliteHelper;
import com.kali_corporation.healthfitnessplus.sevice.utils.AppUtils;
import com.kali_corporation.healthfitnessplus.sevice.utils.ChartXValueFormatter;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;
import me.itangqi.waveloadingview.WaveLoadingView;

public class StatsActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private SqliteHelper sqliteHelper;
    private float totalGlasses;
    private float totalPercentage;


    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_stats);
        SharedPreferences sharedPreferences = getSharedPreferences(AppUtils.Companion.getUSERS_SHARED_PREF(), AppUtils.Companion.getPRIVATE_MODE());
        Intrinsics.checkExpressionValueIsNotNull(sharedPreferences, "getSharedPreferences(App…F, AppUtils.PRIVATE_MODE)");
        this.sharedPref = sharedPreferences;
        Context context = this;
        this.sqliteHelper = new SqliteHelper(context);
        ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        SqliteHelper sqliteHelper2 = this.sqliteHelper;
        if (sqliteHelper2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sqliteHelper");
        }
        Cursor allStats = sqliteHelper2.getAllStats();
        if (allStats.moveToFirst()) {
            int count = allStats.getCount();
            for (int i = 0; i < count; i++) {
                arrayList2.add(allStats.getString(1));
                float f = (((float) allStats.getInt(2)) / ((float) allStats.getInt(3))) * ((float) 100);
                this.totalPercentage += f;
                this.totalGlasses += (float) allStats.getInt(2);
                arrayList.add(new Entry((float) i, f));
                allStats.moveToNext();
            }
        }
        if (!arrayList.isEmpty()) {
            LineChart lineChart = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart, "chart");
            Description description = lineChart.getDescription();
            Intrinsics.checkExpressionValueIsNotNull(description, "chart.description");
            description.setEnabled(false);
            ((LineChart) findViewById(R.id.chart)).animateY(1000, Easing.Linear);
            LineChart lineChart2 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart2, "chart");
            lineChart2.getViewPortHandler().setMaximumScaleX(1.5f);
            LineChart lineChart3 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart3, "chart");
            lineChart3.getXAxis().setDrawGridLines(false);
            LineChart lineChart4 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart4, "chart");
            XAxis xAxis = lineChart4.getXAxis();
            Intrinsics.checkExpressionValueIsNotNull(xAxis, "chart.xAxis");
            xAxis.setPosition(XAxis.XAxisPosition.TOP);
            LineChart lineChart5 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart5, "chart");
            XAxis xAxis2 = lineChart5.getXAxis();
            Intrinsics.checkExpressionValueIsNotNull(xAxis2, "chart.xAxis");
            xAxis2.setGranularityEnabled(true);
            LineChart lineChart6 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart6, "chart");
            Legend legend = lineChart6.getLegend();
            Intrinsics.checkExpressionValueIsNotNull(legend, "chart.legend");
            legend.setEnabled(false);
            ((LineChart) findViewById(R.id.chart)).fitScreen();
            LineChart lineChart7 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart7, "chart");
            lineChart7.setAutoScaleMinMaxEnabled(true);
            LineChart lineChart8 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart8, "chart");
            lineChart8.setScaleX(1.0f);
            ((LineChart) findViewById(R.id.chart)).setPinchZoom(true);
            LineChart lineChart9 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart9, "chart");
            lineChart9.setScaleXEnabled(true);
            LineChart lineChart10 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart10, "chart");
            lineChart10.setScaleYEnabled(false);
            LineChart lineChart11 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart11, "chart");
            YAxis axisLeft = lineChart11.getAxisLeft();
            Intrinsics.checkExpressionValueIsNotNull(axisLeft, "chart.axisLeft");
            axisLeft.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            LineChart lineChart12 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart12, "chart");
            XAxis xAxis3 = lineChart12.getXAxis();
            Intrinsics.checkExpressionValueIsNotNull(xAxis3, "chart.xAxis");
            xAxis3.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            LineChart lineChart13 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart13, "chart");
            lineChart13.getAxisLeft().setDrawAxisLine(false);
            LineChart lineChart14 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart14, "chart");
            lineChart14.getXAxis().setDrawAxisLine(false);
            ((LineChart) findViewById(R.id.chart)).setDrawMarkers(false);
            LineChart lineChart15 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart15, "chart");
            XAxis xAxis4 = lineChart15.getXAxis();
            Intrinsics.checkExpressionValueIsNotNull(xAxis4, "chart.xAxis");
            xAxis4.setLabelCount(5);
            LineChart lineChart16 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart16, "chart");
            YAxis axisRight = lineChart16.getAxisRight();
            axisRight.setDrawGridLines(false);
            axisRight.setDrawZeroLine(false);
            axisRight.setDrawAxisLine(false);
            axisRight.setDrawLabels(false);
            LineDataSet lineDataSet = new LineDataSet(arrayList, "Label");
            lineDataSet.setDrawCircles(false);
            lineDataSet.setLineWidth(2.5f);
            lineDataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(getDrawable(R.drawable.graph_fill_gradiant));
            lineDataSet.setDrawValues(false);
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData lineData = new LineData(lineDataSet);
            LineChart lineChart17 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart17, "chart");
            XAxis xAxis5 = lineChart17.getXAxis();
            Intrinsics.checkExpressionValueIsNotNull(xAxis5, "chart.xAxis");
            xAxis5.setValueFormatter(new ChartXValueFormatter(arrayList2));
            LineChart lineChart18 = (LineChart) findViewById(R.id.chart);
            Intrinsics.checkExpressionValueIsNotNull(lineChart18, "chart");
            lineChart18.setData(lineData);
            ((LineChart) findViewById(R.id.chart)).invalidate();
            SharedPreferences sharedPreferences2 = this.sharedPref;
            if (sharedPreferences2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
            }
            int i2 = sharedPreferences2.getInt(AppUtils.Companion.getTOTAL_INTAKE(), 0);
            SqliteHelper sqliteHelper3 = this.sqliteHelper;
            if (sqliteHelper3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sqliteHelper");
            }
            String currentDate = AppUtils.Companion.getCurrentDate();
            if (currentDate == null) {
                Intrinsics.throwNpe();
            }
            int intook = i2 - sqliteHelper3.getIntook(currentDate);
            if (intook > 0) {
                TextView textView = (TextView) findViewById(R.id.remainingIntake);
                Intrinsics.checkExpressionValueIsNotNull(textView, "remainingIntake");
                textView.setText(intook + " ml");
            } else {
                TextView textView2 = (TextView) findViewById(R.id.remainingIntake);
                Intrinsics.checkExpressionValueIsNotNull(textView2, "remainingIntake");
                textView2.setText("0 ml");
            }
            TextView textView3 = (TextView) findViewById(R.id.targetIntake);
            Intrinsics.checkExpressionValueIsNotNull(textView3, "targetIntake");
            StringBuilder sb = new StringBuilder();
            SharedPreferences sharedPreferences3 = this.sharedPref;
            if (sharedPreferences3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
            }
            sb.append(sharedPreferences3.getInt(AppUtils.Companion.getTOTAL_INTAKE(), 0));
            sb.append(" ml");
            textView3.setText(sb.toString());
            SqliteHelper sqliteHelper4 = this.sqliteHelper;
            if (sqliteHelper4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sqliteHelper");
            }
            String currentDate2 = AppUtils.Companion.getCurrentDate();
            if (currentDate2 == null) {
                Intrinsics.throwNpe();
            }
            int intook2 = sqliteHelper4.getIntook(currentDate2) * 100;
            SharedPreferences sharedPreferences4 = this.sharedPref;
            if (sharedPreferences4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sharedPref");
            }
            int i3 = intook2 / sharedPreferences4.getInt(AppUtils.Companion.getTOTAL_INTAKE(), 0);
            WaveLoadingView waveLoadingView = (WaveLoadingView) findViewById(R.id.waterLevelView);
            Intrinsics.checkExpressionValueIsNotNull(waveLoadingView, "waterLevelView");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(i3);
            sb2.append('%');
            waveLoadingView.setCenterTitle(sb2.toString());
            WaveLoadingView waveLoadingView2 = (WaveLoadingView) findViewById(R.id.waterLevelView);
            Intrinsics.checkExpressionValueIsNotNull(waveLoadingView2, "waterLevelView");
            waveLoadingView2.setProgressValue(i3);
        }
    }


}
