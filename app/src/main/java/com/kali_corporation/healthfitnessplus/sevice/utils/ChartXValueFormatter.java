package com.kali_corporation.healthfitnessplus.sevice.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.ArrayList;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
public final class ChartXValueFormatter extends ValueFormatter {
    private final ArrayList<String> dateArray;

    public ChartXValueFormatter(ArrayList<String> arrayList) {
        Intrinsics.checkParameterIsNotNull(arrayList, "dateArray");
        this.dateArray = arrayList;
    }

    public final ArrayList<String> getDateArray() {
        return this.dateArray;
    }

    public String getAxisLabel(float f, AxisBase axisBase) {
        String str = (String) CollectionsKt.getOrNull(this.dateArray, (int) f);
        return str != null ? str : "";
    }
}
