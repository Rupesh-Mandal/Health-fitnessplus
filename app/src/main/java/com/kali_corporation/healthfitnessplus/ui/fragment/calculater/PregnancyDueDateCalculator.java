package com.kali_corporation.healthfitnessplus.ui.fragment.calculater;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kali_corporation.healthfitnessplus.R;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.kali_corporation.healthfitnessplus.sevice.general.MyApplication;
import com.kali_corporation.healthfitnessplus.sevice.utils.DateUtil;
import com.kali_corporation.healthfitnessplus.sevice.utils.GlobalFunction;
import com.kali_corporation.healthfitnessplus.sevice.utils.SharedPreferenceManager;
import com.kali_corporation.healthfitnessplus.sevice.utils.TypefaceManager;
import com.kali_corporation.healthfitnessplus.sevice.calendar.SNPCalendarView;
import com.kali_corporation.healthfitnessplus.sevice.calendar.onSNPCalendarViewListener;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.interfaces.NetworkRequestCheckListener;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PregnancyDueDateCalculator extends Fragment {

    String TAG = getClass().getSimpleName();
    AdView adView;
    String eligieble_date;
    GlobalFunction globalFunction;
    ImageView iv_back;
    SNPCalendarView mFCalendarView;
    String prev_date;
    SharedPreferenceManager sharedPreferenceManager;
    String todays_date;
    TextView tv_date;
    TextView tv_pregnancy;
    TextView tv_search_date;
    TypefaceManager typefaceManager;

    public PregnancyDueDateCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pregnancy_due_date_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.globalFunction = new GlobalFunction(getActivity());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.mFCalendarView = (SNPCalendarView) view.findViewById(R.id.mFCalendarView);
        this.tv_pregnancy = (TextView) view.findViewById(R.id.tv_pregnancy);
        this.tv_search_date = (TextView) view.findViewById(R.id.tv_search_date);
        this.tv_date = (TextView) view.findViewById(R.id.tv_date);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.tv_date.setFocusable(true);
        this.tv_date.setFocusableInTouchMode(true);
        this.tv_date.requestFocus();
        this.tv_pregnancy.setTypeface(this.typefaceManager.getBold());
        this.tv_date.setTypeface(this.typefaceManager.getLight());
        this.tv_search_date.setTypeface(this.typefaceManager.getBold());
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PregnancyDueDateCalculator.this.onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.todays_date = getDateTime();
        StringBuilder sb = new StringBuilder();
        sb.append("todays_date->");
        sb.append(this.todays_date);
        Log.d("todays_date", sb.toString());
        get_eligieble_date(this.todays_date);
        this.tv_search_date.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int random = ((int) (Math.random() * 2.0d)) + 1;
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("random_number==>");
                sb.append(random);
                printStream.println(sb.toString());
                if (random == 2) {
                    PregnancyDueDateCalculator.this.showIntertitial();
                    return;
                }
//                Intent intent = new Intent(Pregnancy_Due_Date_Calculator.this, Blood_Donation_Result.class);
//                intent.putExtra("prevdate", Pregnancy_Due_Date_Calculator.this.prev_date);
//                intent.putExtra("nextdate", Pregnancy_Due_Date_Calculator.this.eligieble_date);
//                intent.putExtra("flag", "1");
//                Pregnancy_Due_Date_Calculator.this.startActivity(intent);
                result(prev_date,eligieble_date);

            }
        });
        this.mFCalendarView.setOnCalendarViewListener(new onSNPCalendarViewListener() {
            public void onDisplayedMonthChanged(int i, int i2, String str) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(" month:");
                stringBuffer.append(i);
                stringBuffer.append(" year:");
                stringBuffer.append(i2);
                stringBuffer.append(" monthStr: ");
                stringBuffer.append(str);
            }

            public void onDateChanged(String str) {
                String str2 = "date";
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("date->");
                    sb.append(str);
                    Log.d(str2, sb.toString());
                    PregnancyDueDateCalculator.this.get_eligieble_date(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public void get_eligieble_date(String str) {
        try {
            Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            Date addDays = DateUtil.addDays(parse, 280);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM yyyy");
            this.eligieble_date = simpleDateFormat.format(addDays);
            this.prev_date = simpleDateFormat2.format(parse);
            StringBuilder sb = new StringBuilder();
            sb.append("eligieble_date->");
            sb.append(this.eligieble_date);
            Log.d("eligieble_date", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("prev_date->");
            sb2.append(this.prev_date);
            Log.d("prev_date", sb2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        this.adView.setVisibility(View.GONE);
        ActivityCompat.finishAfterTransition(getActivity());
    }


    public void onResume() {
        super.onResume();
        if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue() && MyApplication.interstitial != null && !MyApplication.interstitial.isLoaded() && !MyApplication.interstitial.isLoading()) {
            ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                public void onNoResponse() {
                }

                public void onResponseObtained() {
                    MyApplication.interstitial.loadAd(new Builder().build());
                }
            });
        }
        if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
            MyApplication.interstitial.setAdListener(new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    MyApplication.interstitial.loadAd(new Builder().build());
//                    Intent intent = new Intent(Pregnancy_Due_Date_Calculator.this, Blood_Donation_Result.class);
//                    intent.putExtra("prevdate", Pregnancy_Due_Date_Calculator.this.prev_date);
//                    intent.putExtra("nextdate", Pregnancy_Due_Date_Calculator.this.eligieble_date);
//                    intent.putExtra("flag", "1");
//                    Pregnancy_Due_Date_Calculator.this.startActivity(intent);
                    result(prev_date,eligieble_date);

                }

                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    if (MyApplication.interstitial != null && !MyApplication.interstitial.isLoading()) {
                        ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                            public void onNoResponse() {
                            }

                            public void onResponseObtained() {
                                MyApplication.interstitial.loadAd(new Builder().build());
                            }
                        });
                    }
                }
            });
        }
    }

    public void showIntertitial() {
        if (this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
//            Intent intent = new Intent(this, Blood_Donation_Result.class);
//            intent.putExtra("prevdate", this.prev_date);
//            intent.putExtra("nextdate", this.eligieble_date);
//            intent.putExtra("flag", "1");
//            startActivity(intent);
            result(prev_date,eligieble_date);

        } else if (MyApplication.interstitial == null || !MyApplication.interstitial.isLoaded()) {
            if (!MyApplication.interstitial.isLoading()) {
                ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                    public void onNoResponse() {
                    }

                    public void onResponseObtained() {
                        MyApplication.interstitial.loadAd(new Builder().build());
                    }
                });
            }
//            Intent intent2 = new Intent(this, Blood_Donation_Result.class);
//            intent2.putExtra("prevdate", this.prev_date);
//            intent2.putExtra("nextdate", this.eligieble_date);
//            intent2.putExtra("flag", "1");
//            startActivity(intent2);
            result(prev_date,eligieble_date);
        } else {
            MyApplication.interstitial.show();
        }
    }
    private void result(String prev_date, String eligieble_date) {
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getString(R.string.Date_of_the_first_day_of_your_last_period_is));
        sb3.append(" : ");
        sb3.append(prev_date);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(getString(R.string.Estimated_Expected_Date_of_Delivery));
        sb4.append(" : \n");
        sb4.append(eligieble_date);
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb3.toString());
        sb5.append("\n");
        sb5.append("\n");
        sb5.append(sb4.toString());


        new AlertDialog.Builder(getActivity())
                .setTitle("Blood Donation")
                .setMessage(sb5.toString())
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
