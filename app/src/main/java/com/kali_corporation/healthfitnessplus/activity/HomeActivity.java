package com.kali_corporation.healthfitnessplus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.data.Entry;
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.sevice.adapters.WorkoutData;
import com.kali_corporation.healthfitnessplus.sevice.database.DatabaseOperations;
import com.kali_corporation.healthfitnessplus.sevice.helpers.SqliteHelper;
import com.kali_corporation.healthfitnessplus.sevice.utils.AppUtils;
import com.kali_corporation.healthfitnessplus.sevice.utils.AppUtilss;
import com.kali_corporation.healthfitnessplus.sevice.utils.Constants;
import com.kali_corporation.healthfitnessplus.ui.DayActivity;
import com.kali_corporation.healthfitnessplus.ui.SlidingAdapter;
import com.kali_corporation.healthfitnessplus.ui.SlidingModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.jvm.internal.Intrinsics;

public class HomeActivity extends AppCompatActivity {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ProgressBar progressBar, progressBar1;
    private TextView txtProgress, percentScore1, dayleft;
    public double k = 0.0d;
    public DatabaseOperations databaseOperations;
    public SharedPreferences launchDataPreferences;
    public ArrayList<String> arr;
    private int pStatus = 0;
    RelativeLayout rel_one, rel_two, rel_three, calulater_layout,reminder_layout;
    private static ViewPager mPager;
    private ArrayList<SlidingModel> imageModelArrayList;


    private SharedPreferences sharedPref;
    private SqliteHelper sqliteHelper;

    private float totalPercentage;
    me.itangqi.waveloadingview.WaveLoadingView waterLevelView;
    public List<WorkoutData> workoutDataList;
    public int workoutPosition = -1;
    public int daysCompletionConter = 0;
    String str = "";
    RelativeLayout i_plus;

    private int[] myImageList = new int[]{R.drawable.a,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a7,
            R.drawable.a8,
            R.drawable.a9,
            R.drawable.a10,
            R.drawable.a11,
            R.drawable.a12,
            R.drawable.a13,
            R.drawable.a14,
            R.drawable.a15,
            R.drawable.a16};


    public BroadcastReceiver progressReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            double doubleExtra = intent.getDoubleExtra(AppUtilss.KEY_PROGRESS, 0.0d);
            try {
                ((WorkoutData) workoutDataList.get(workoutPosition)).setProgress((float) doubleExtra);
                k = 0.0d;
                daysCompletionConter = 0;
                for (int i = 0; i < Constants.TOTAL_DAYS; i++) {
                    double d = k;
                    double progress = (double) ((WorkoutData) workoutDataList.get(i)).getProgress();
                    Double.isNaN(progress);
                    k = (double) ((float) (d + ((progress * 4.348d) / 100.0d)));
                    if (((WorkoutData) workoutDataList.get(i)).getProgress() >= 99.0f) {
                        daysCompletionConter = daysCompletionConter + 1;
                    }
                }
                daysCompletionConter = daysCompletionConter + (daysCompletionConter / 3);
                progressBar1.setProgress((int) k);
                TextView g = percentScore1;
                StringBuilder sb = new StringBuilder();
                sb.append((int) k);
                sb.append("%");
                g.setText(sb.toString());
                TextView h = dayleft;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(Constants.TOTAL_DAYS - daysCompletionConter);
                sb2.append(" Days left");
                h.setText(sb2.toString());
//                MainFragment.this.adapter.notifyDataSetChanged();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("");
                sb3.append(doubleExtra);
                Log.i("progress broadcast", sb3.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void a(int i, MaterialDialog materialDialog, DialogAction dialogAction) {
        TextView textView = null;
        String str = null;
        try {
            materialDialog.dismiss();
            String str2 = (String) this.arr.get(i);
            if (this.workoutDataList != null) {
                this.workoutDataList.clear();
            }
            this.databaseOperations.insertExcDayData(str2, 0.0f);
            this.databaseOperations.insertExcCounter(str2, 0);
            this.workoutDataList = this.databaseOperations.getAllDaysProgress();
//            this.adapter = new AllDayAdapter(getActivity(), this.workoutDataList);
//            this.recyclerView.getRecycledViewPool().clear();
//            this.recyclerView.setAdapter(this.adapter);
            this.daysCompletionConter--;
            TextView textView2 = this.dayleft;
            StringBuilder sb = new StringBuilder();
            sb.append(Constants.TOTAL_DAYS - this.daysCompletionConter);
            sb.append(" Days left");
            textView2.setText(sb.toString());
            if (this.daysCompletionConter > 0) {
                this.progressBar1.setProgress((int) (k - 4.348d));
                textView = this.percentScore1;
                StringBuilder sb2 = new StringBuilder();
                sb2.append((int) (this.k - 4.348d));
                sb2.append("%");
                str = sb2.toString();
            } else {
                if (this.daysCompletionConter == 0) {
                    this.progressBar1.setProgress(0);
                    textView = this.percentScore1;
                    str = "0%";
                }
                Intent intent = new Intent(HomeActivity.this, DayActivity.class);
                intent.putExtra("day", str2);
                intent.putExtra("day_num", i);
                intent.putExtra("progress", ((WorkoutData) this.workoutDataList.get(i)).getProgress());
                startActivity(intent);
            }
            textView.setText(str);
            Intent intent2 = new Intent(HomeActivity.this, DayActivity.class);
            intent2.putExtra("day", str2);
            intent2.putExtra("day_num", i);
            intent2.putExtra("progress", ((WorkoutData) this.workoutDataList.get(i)).getProgress());
            startActivity(intent2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        mPager = findViewById(R.id.pager);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        waterLevelView = (me.itangqi.waveloadingview.WaveLoadingView) findViewById(R.id.waterLevelView);
        progressBar1 = (ProgressBar) findViewById(R.id.progress);
        viewPager();

        percentScore1 = (TextView) findViewById(R.id.percentScore);
        i_plus = (RelativeLayout) findViewById(R.id.i_plus);
        dayleft = (TextView) findViewById(R.id.daysLeft);

        launchDataPreferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        databaseOperations = new DatabaseOperations(HomeActivity.this);
        final String str = "thirtyday";
        boolean z = launchDataPreferences.getBoolean(str, false);
        String str2 = "daysInserted";
        boolean z2 = launchDataPreferences.getBoolean(str2, false);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        this.width = displayMetrics.widthPixels;
//        this.height = displayMetrics.heightPixels;
        if (!z2 && databaseOperations.CheckDBEmpty() == 0) {
            databaseOperations.insertExcALLDayData();
            SharedPreferences.Editor edit = launchDataPreferences.edit();
            edit.putBoolean(str2, true);
            edit.apply();
        }


        rel_two = (RelativeLayout) findViewById(R.id.rel_two);
        rel_three = (RelativeLayout) findViewById(R.id.rel_three);
        calulater_layout=findViewById(R.id.calculater_layout);
        reminder_layout=findViewById(R.id.reminder_layout);

        calulater_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                intent.putExtra("pos",0);
                intent.putExtra("title","Health Caculater");
                startActivity(intent);
            }
        });
        reminder_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                intent.putExtra("pos",1);
                intent.putExtra("title","Reminder");
                startActivity(intent);
            }
        });

        i_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // mainAcdsctivity.loadFragment_water(new Fragment_Walk_and_Step());
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                intent.putExtra("pos",3);
                intent.putExtra("title","Walk and Step");
                startActivity(intent);
            }
        });


        rel_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Water_MainActivity.class);
                startActivity(intent);
            }
        });

        rel_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // mainAcdsctivity.loadFragmentworkout(new Workout());
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                intent.putExtra("pos",2);
                intent.putExtra("title","Workout");
                startActivity(intent);

            }
        });


//        water report

        SharedPreferences var10001 = this.getSharedPreferences(AppUtils.Companion.getUSERS_SHARED_PREF(), AppUtils.Companion.getPRIVATE_MODE());
        Intrinsics.checkExpressionValueIsNotNull(var10001, "getSharedPreferences(App…F, AppUtils.PRIVATE_MODE)");
        this.sharedPref = var10001;
        this.sqliteHelper = new SqliteHelper(HomeActivity.this);
        ArrayList entries = new ArrayList();
        ArrayList dateArray = new ArrayList();
        SqliteHelper var10000 = this.sqliteHelper;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sqliteHelper");
        }
        Cursor cursor = var10000.getAllStats();
        if (cursor.moveToFirst()) {

            int i = 0;

            for (int var6 = cursor.getCount(); i < var6; ++i) {
                dateArray.add(cursor.getString(1));
                float percent = (float) cursor.getInt(2) / (float) cursor.getInt(3) * (float) 100;
                double number1 = percent;
                double number2 = (int) (Math.round(number1 * 100)) / 100.0;


                waterLevelView.setCenterTitle((number2) + " %");

                entries.add(new Entry((float) i, percent));
                cursor.moveToNext();
            }
        } else {
//            Toast.makeText(getActivity(), (CharSequence) "Empty", 1).show();
        }

        List<WorkoutData> list = this.workoutDataList;
        if (list != null) {
            list.clear();
        }
        this.workoutDataList = databaseOperations.getAllDaysProgress();
        this.progressBar1 = (ProgressBar) findViewById(R.id.progress);
//        this.progressBar1.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progressbar_drawable));

        for (int i = 0; i < Constants.TOTAL_DAYS; i++) {
            double d = this.k;
            double progress = (double) ((WorkoutData) this.workoutDataList.get(i)).getProgress();
            Double.isNaN(progress);
            this.k = (double) ((float) (d + ((progress * 4.348d) / 100.0d)));
            if (((WorkoutData) this.workoutDataList.get(i)).getProgress() >= 99.0f) {
                this.daysCompletionConter++;
            }
        }


        int i2 = this.daysCompletionConter;
        this.daysCompletionConter = i2 + (i2 / 3);
        this.progressBar1.setProgress((int) this.k);
        TextView textView = this.percentScore1;
        StringBuilder sb = new StringBuilder();
        sb.append((int) this.k);
        sb.append("%");
        textView.setText(sb.toString());
        TextView textView2 = this.dayleft;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Constants.TOTAL_DAYS - this.daysCompletionConter);
        sb2.append(" Days left");
        textView2.setText(sb2.toString());


        if (z) {
            SharedPreferences.Editor edit2 = this.launchDataPreferences.edit();
            edit2.putBoolean(str, false);
            edit2.apply();
            restartExcercise();
            this.daysCompletionConter = 0;
        }
        registerReceiver(this.progressReceiver, new IntentFilter(AppUtilss.WORKOUT_BROADCAST_FILTER));
        if (this.daysCompletionConter > 4) {


        }



    }

    private void viewPager() {
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        //        walk and steps report

        mPager.setAdapter(new SlidingAdapter(HomeActivity.this, imageModelArrayList));

        NUM_PAGES = imageModelArrayList.size();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, false);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 4000);

    }

//    public SharedPreferences getSharedPreferences(String users_shared_pref, int private_mode) {
//        return getSharedPreferences(users_shared_pref, private_mode);
//    }


    public void restartExcercise() {
        SharedPreferences.Editor edit = this.launchDataPreferences.edit();
        String str = "daysInserted";
        edit.putBoolean(str, false);
        edit.apply();
        for (int i = 0; i < 30; i++) {
            String str2 = (String) this.arr.get(i);
            this.databaseOperations.insertExcDayData(str2, 0.0f);
            this.databaseOperations.insertExcCounter(str2, 0);
        }
        edit.putBoolean(str, true);
        edit.apply();
        List<WorkoutData> list = this.workoutDataList;
        if (list != null) {
            list.clear();
        }
        this.workoutDataList = this.databaseOperations.getAllDaysProgress();

        this.progressBar1.setProgress(0);
        this.percentScore1.setText("0%");
        TextView textView = this.dayleft;
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.TOTAL_DAYS);
        sb.append(" Days left");
        textView.setText(sb.toString());
    }

    private ArrayList<SlidingModel> populateList() {
        ArrayList<SlidingModel> list = new ArrayList<>();
        for (int i = 0; i < myImageList.length; i++) {
            SlidingModel imageModel = new SlidingModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }
        return list;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 0) {
            this.databaseOperations.deleteTable();
            SharedPreferences.Editor edit = this.launchDataPreferences.edit();
            String str = "daysInserted";
            edit.putBoolean(str, false);
            edit.apply();
            edit.putBoolean(str, true);
            edit.apply();
            List<WorkoutData> list = this.workoutDataList;
            if (list != null) {
                list.clear();
            }
            this.workoutDataList = this.databaseOperations.getAllDaysProgress();
//            this.adapter = new AllDayAdapter(getActivity(), this.workoutDataList);
//            this.recyclerView.getRecycledViewPool().clear();
//            this.recyclerView.setAdapter(this.adapter);
            this.progressBar.setProgress(0);
            this.percentScore1.setText("0%");
            TextView textView = this.dayleft;
            StringBuilder sb = new StringBuilder();
            sb.append(Constants.TOTAL_DAYS);
            sb.append(" Days left");
            textView.setText(sb.toString());
        }
        super.onActivityResult(i, i2, intent);
    }

//    @Override
//    public void onBackPressed() {
//        StepDetectionServiceHelper.stopAllIfNotRequired(this.getApplicationContext());
////        StepDetectionServiceHelper.startAllIfEnabled(true, MainActivity.this);
//
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.adview_layout_exit);
//        ((GifImageView) dialog.findViewById(R.id.GifImageView)).setGifImageResource(R.drawable.rate);
//        ((Button) dialog.findViewById(R.id.btnno)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        ((Button) dialog.findViewById(R.id.btnrate)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                try {
//                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
//                } catch (ActivityNotFoundException unused) {
//                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
//                }
//            }
//        });
//        ((Button) dialog.findViewById(R.id.btnyes)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                dialog.dismiss();
//                finish();
//                System.exit(1);
//
//
//            }
//        });
//        dialog.show();
//    }
}