package com.kali_corporation.healthfitnessplus.sevice.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Closeable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SqliteHelper extends SQLiteOpenHelper {
    @NotNull
    private final Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Aqua";
    private static final String TABLE_STATS = "stats";
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_INTOOK = "intook";
    private static final String KEY_TOTAL_INTAKE = "totalintake";

    public SqliteHelper(@NotNull Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory)null, DATABASE_VERSION);
        this.context = context;
    }
//    @NotNull
//    public static final SqliteHelper.Companion Companion = new SqliteHelper.Companion((DefaultConstructorMarker)null);

    public void onCreate(@Nullable SQLiteDatabase db) {
        String CREATE_STATS_TABLE = "CREATE TABLE " + TABLE_STATS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT UNIQUE," + KEY_INTOOK + " INT," + KEY_TOTAL_INTAKE + " INT" + ")";
        if (db != null) {
            db.execSQL(CREATE_STATS_TABLE);
        }

    }

    public void onUpgrade(@Nullable SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db == null) {

        }

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
        this.onCreate(db);
    }

    public final long addAll(@NotNull String date, int intook, int totalintake) {
        if (this.checkExistance(date) == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_DATE, date);
            values.put(KEY_INTOOK, intook);
            values.put(KEY_TOTAL_INTAKE, totalintake);
            SQLiteDatabase db = this.getWritableDatabase();
            long response = db.insert(TABLE_STATS, (String)null, values);
            db.close();
            return response;
        } else {
            return -1L;
        }
    }

    public final int getIntook(@NotNull String date) {
        String selectQuery = "SELECT " + KEY_INTOOK + " FROM " + TABLE_STATS + " WHERE " + KEY_DATE + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Closeable var4 = (Closeable)db.rawQuery(selectQuery, new String[]{date});
        boolean var5 = false;
        Throwable var6 = (Throwable)null;

        int var9;
        try {
            Cursor it = (Cursor)var4;
            boolean var8 = false;
            if (!it.moveToFirst()) {
                return 0;
            }

            var9 = it.getInt(it.getColumnIndex(KEY_INTOOK));
        } catch (Throwable var12) {
            var6 = var12;
            throw var12;
        }
        return var9;
    }

    public final int addIntook(@NotNull String date, int selectedOption) {
        int intook = this.getIntook(date);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_INTOOK, intook + selectedOption);
        int response = db.update(TABLE_STATS, contentValues, KEY_DATE + " = ?", new String[]{date});
        db.close();
        return response;
    }

    public final int checkExistance(@NotNull String date) {
        String selectQuery = "SELECT " + KEY_INTOOK + " FROM " + TABLE_STATS + " WHERE " + KEY_DATE + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Closeable var4 = (Closeable)db.rawQuery(selectQuery, new String[]{date});
        boolean var5 = false;
        Throwable var6 = (Throwable)null;

        int var9;
        try {
            Cursor it = (Cursor)var4;
            boolean var8 = false;
            if (!it.moveToFirst()) {
                return 0;
            }
            var9 = it.getCount();
        } catch (Throwable var12) {
            var6 = var12;
            throw var12;
        }

        return var9;
    }

    @NotNull
    public final Cursor getAllStats() {
        String selectQuery = "SELECT * FROM " + TABLE_STATS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor var10000 = db.rawQuery(selectQuery, (String[])null);
        return var10000;
    }

    public final int updateTotalIntake(@NotNull String date, int totalintake) {
        this.getIntook(date);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TOTAL_INTAKE, totalintake);
        int response = db.update(TABLE_STATS, contentValues, KEY_DATE + " = ?", new String[]{date});
        db.close();
        return response;
    }

    @NotNull
    public final Context getContext() {
        return this.context;
    }

//    public SqliteHelper(@NotNull Context context) {
//        super();
//        Intrinsics.checkParameterIsNotNull(context, "context");
//        super(context, DATABASE_NAME, (CursorFactory)null, DATABASE_VERSION);
//        this.context = context;
//    }



    public static final class Companion {
        private Companion() {
        }
    }
}
