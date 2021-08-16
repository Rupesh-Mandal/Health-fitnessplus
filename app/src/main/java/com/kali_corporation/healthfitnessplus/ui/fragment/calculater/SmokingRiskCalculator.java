package com.kali_corporation.healthfitnessplus.ui.fragment.calculater;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kali_corporation.healthfitnessplus.R;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.kali_corporation.healthfitnessplus.sevice.general.MyApplication;
import com.kali_corporation.healthfitnessplus.sevice.utils.GlobalFunction;
import com.kali_corporation.healthfitnessplus.sevice.utils.SharedPreferenceManager;
import com.kali_corporation.healthfitnessplus.sevice.utils.TypefaceManager;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.interfaces.NetworkRequestCheckListener;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;

public class SmokingRiskCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    String end_date;
    EditText et_no_of_cigarettes;

    public int from_Day;

    public int from_Month;

    public int from_Year;
    GlobalFunction globalFunction;
    ImageView iv_back;
    ListView listViewGender;
    String msg = "";
    private PopupWindow popupWindowGender;
    SharedPreferenceManager sharedPreferenceManager;
    String start_date;
    int timePerDay;
    int totalTimeMin;
    int totalTimehr;
    int total_cigarettes;
    int total_days = 1;
    TextView tv_enddate;
    TextView tv_gender;
    TextView tv_genderunit;
    TextView tv_search_bloodsmokingrisk;
    TextView tv_smoking_risk;
    TextView tv_startdate;
    TypefaceManager typefaceManager;

    public SmokingRiskCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_smoking_risk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.globalFunction = new GlobalFunction(getActivity());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.et_no_of_cigarettes = (EditText) view.findViewById(R.id.et_no_of_cigarettes);
        this.tv_smoking_risk = (TextView) view.findViewById(R.id.tv_smoking_risk);
        this.tv_startdate = (TextView) view.findViewById(R.id.tv_startdate);
        this.tv_enddate = (TextView) view.findViewById(R.id.tv_enddate);
        this.tv_genderunit = (TextView) view.findViewById(R.id.tv_genderunit);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.tv_search_bloodsmokingrisk = (TextView) view.findViewById(R.id.tv_search_bloodsmokingrisk);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.adView = (AdView) view.findViewById(R.id.adView);

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);

        this.start_date = getDateTime();
        this.end_date = getDateTime();
        this.tv_startdate.setText(this.start_date);
        this.tv_enddate.setText(this.end_date);
        this.tv_smoking_risk.setTypeface(this.typefaceManager.getBold());
        this.tv_search_bloodsmokingrisk.setTypeface(this.typefaceManager.getBold());
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.tv_gender.setOnClickListener(showPopupWindowGender());
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SmokingRiskCalculator.this.onBackPressed();
            }
        });
        this.tv_search_bloodsmokingrisk.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SmokingRiskCalculator.this.et_no_of_cigarettes.getText().toString().trim().equals("")) {
                    Toast.makeText(getContext(), SmokingRiskCalculator.this.getString(R.string.Enter_no_of_cigarettes), Toast.LENGTH_SHORT).show();
                    return;
                }
                int random = ((int) (Math.random() * 2.0d)) + 1;
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("random_number==>");
                sb.append(random);
                printStream.println(sb.toString());
                if (random == 2) {
                    SmokingRiskCalculator.this.showIntertitial();
                } else {
                    SmokingRiskCalculator.this.calculate_smokingrisk();
                }
            }
        });
        this.tv_startdate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus != null) {
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
                Calendar instance = Calendar.getInstance();
                SmokingRiskCalculator.this.from_Year = instance.get(1);
                SmokingRiskCalculator.this.from_Month = instance.get(2);
                SmokingRiskCalculator.this.from_Day = instance.get(5);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        try {
                            SmokingRiskCalculator smoking_Risk_Calculator = SmokingRiskCalculator.this;
                            StringBuilder sb = new StringBuilder();
                            sb.append(i3);
                            sb.append("-");
                            sb.append(i2 + 1);
                            sb.append("-");
                            sb.append(i);
                            smoking_Risk_Calculator.start_date = sb.toString();
                            SmokingRiskCalculator.this.tv_startdate.setText(SmokingRiskCalculator.this.start_date);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, SmokingRiskCalculator.this.from_Year, SmokingRiskCalculator.this.from_Month, SmokingRiskCalculator.this.from_Day);
                datePickerDialog.show();
            }
        });
        this.tv_enddate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus != null) {
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
                Calendar instance = Calendar.getInstance();
                SmokingRiskCalculator.this.from_Year = instance.get(1);
                SmokingRiskCalculator.this.from_Month = instance.get(2);
                SmokingRiskCalculator.this.from_Day = instance.get(5);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        SmokingRiskCalculator smoking_Risk_Calculator = SmokingRiskCalculator.this;
                        StringBuilder sb = new StringBuilder();
                        sb.append(i3);
                        sb.append("-");
                        sb.append(i2 + 1);
                        sb.append("-");
                        sb.append(i);
                        smoking_Risk_Calculator.end_date = sb.toString();
                        SmokingRiskCalculator.this.tv_enddate.setText(SmokingRiskCalculator.this.end_date);
                    }
                }, SmokingRiskCalculator.this.from_Year, SmokingRiskCalculator.this.from_Month, SmokingRiskCalculator.this.from_Day);
                datePickerDialog.show();
            }
        });
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
                    SmokingRiskCalculator.this.calculate_smokingrisk();
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

    private String getDateTime() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    private OnClickListener showPopupWindowGender() {
        return new OnClickListener() {
            public void onClick(View view) {
                SmokingRiskCalculator.this.popupWindowGender().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowGender() {
        this.popupWindowGender = new PopupWindow(getContext());
        this.listViewGender = new ListView(getContext());
        this.listViewGender.setDividerHeight(0);
        this.listViewGender.setAdapter(this.adapter_gender);
        this.listViewGender.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SmokingRiskCalculator.this.tv_gender.setText((CharSequence) SmokingRiskCalculator.this.arraylist_gender.get(i));
                SmokingRiskCalculator.this.tv_genderunit.setText((CharSequence) SmokingRiskCalculator.this.arraylist_gender.get(i));
                SmokingRiskCalculator.this.dismissPopupGender();
            }
        });
        this.popupWindowGender.setFocusable(true);
        this.popupWindowGender.setWidth(this.tv_gender.getMeasuredWidth());
        this.popupWindowGender.setHeight(-2);
        this.popupWindowGender.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.img_smoking_risk));
        this.popupWindowGender.setContentView(this.listViewGender);
        return this.popupWindowGender;
    }


    public void dismissPopupGender() {
        if (this.popupWindowGender != null) {
            this.popupWindowGender.dismiss();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0096 A[Catch:{ Exception -> 0x0138 }] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00ae A[Catch:{ Exception -> 0x0138 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void calculate_smokingrisk() {
        Date date;
        Date date2;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date3 = null;
            try {
                date = simpleDateFormat.parse(this.start_date);
                try {
                    date2 = simpleDateFormat.parse(this.end_date);
                    System.out.println(date);
                } catch (ParseException e2) {

                    e2.printStackTrace();
                    date2 = date3;
                    this.total_days = (int) (((((date2.getTime() - date.getTime()) / 1000) / 60) / 60) / 24);
                    this.total_cigarettes = Integer.parseInt(this.et_no_of_cigarettes.getText().toString().trim());
                    this.timePerDay = this.total_cigarettes * 11;
                    this.totalTimeMin = this.timePerDay * this.total_days;
                    this.totalTimehr = this.totalTimeMin / 60;
                    if (this.totalTimehr < 0) {
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("msg->");
                    sb3.append(this.msg);
                    Log.d("msg->", sb3.toString());
                }
            } catch (ParseException e3) {

                date = null;
                e3.printStackTrace();
                date2 = date3;
                this.total_days = (int) (((((date2.getTime() - date.getTime()) / 1000) / 60) / 60) / 24);
                this.total_cigarettes = Integer.parseInt(this.et_no_of_cigarettes.getText().toString().trim());
                this.timePerDay = this.total_cigarettes * 11;
                this.totalTimeMin = this.timePerDay * this.total_days;
                this.totalTimehr = this.totalTimeMin / 60;
                if (this.totalTimehr < 0) {
                }
                StringBuilder sb32 = new StringBuilder();
                sb32.append("msg->");
                sb32.append(this.msg);
                Log.d("msg->", sb32.toString());
            }
            this.total_days = (int) (((((date2.getTime() - date.getTime()) / 1000) / 60) / 60) / 24);
            this.total_cigarettes = Integer.parseInt(this.et_no_of_cigarettes.getText().toString().trim());
            this.timePerDay = this.total_cigarettes * 11;
            this.totalTimeMin = this.timePerDay * this.total_days;
            this.totalTimehr = this.totalTimeMin / 60;
            if (this.totalTimehr < 0) {
                this.msg = getString(R.string.Please_select_valid_date);
                Toast.makeText(getContext().getApplicationContext(), this.msg, Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(getString(R.string.The_fact_that_you_smoked));
                sb5.append(" ");
                sb5.append(this.total_cigarettes);
                sb5.append(" ");
                sb5.append(getString(R.string.cigarettes_per_day_for_a_period_of));
                sb5.append(" ");
                sb5.append(this.total_days);
                sb5.append(" ");
                sb5.append(getString(R.string.means_that_cigarettes_have_taken));
                sb5.append(" ");
                sb5.append(this.totalTimehr);
                sb5.append(" ");
                sb5.append(getString(R.string.hours_of_your_life));
                this.msg = sb5.toString();
//                Intent intent = new Intent(this, Smoking_Risk_Result.class);
//                intent.putExtra("smoking_risk_msg", this.msg);
//                startActivity(intent);
                result(msg);
            }
            StringBuilder sb322 = new StringBuilder();
            sb322.append("msg->");
            sb322.append(this.msg);
            Log.d("msg->", sb322.toString());
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    private void result(String msg) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Smoking Risk")
                .setMessage(msg)
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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

    public void showIntertitial() {
        if (this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
            calculate_smokingrisk();
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
            calculate_smokingrisk();
        } else {
            MyApplication.interstitial.show();
        }
    }
}
