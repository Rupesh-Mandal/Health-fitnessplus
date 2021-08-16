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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.sevice.general.MyApplication;
import com.kali_corporation.healthfitnessplus.sevice.utils.GlobalFunction;
import com.kali_corporation.healthfitnessplus.sevice.utils.SharedPreferenceManager;
import com.kali_corporation.healthfitnessplus.sevice.utils.TypefaceManager;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.interfaces.NetworkRequestCheckListener;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

public class BloodPressureCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    String diastolic_val;
    EditText et_diastolic_pressure;
    EditText et_systolic_pressure;
    GlobalFunction globalFunction;
    ImageView iv_back;
    SharedPreferenceManager sharedPreferenceManager;
    String systolic_val;
    TextView tv_bloodpressure;
    TextView tv_calculate_bloodpressure;
    TypefaceManager typefaceManager;



    public BloodPressureCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bloodpressure_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.globalFunction = new GlobalFunction(getActivity());
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.et_systolic_pressure = (EditText) view.findViewById(R.id.et_systolic_pressure);
        this.et_diastolic_pressure = (EditText) view.findViewById(R.id.et_diastolic_pressure);
        this.tv_bloodpressure = (TextView) view.findViewById(R.id.tv_bloodpressure);
        this.tv_calculate_bloodpressure = (TextView) view.findViewById(R.id.tv_calculate_bloodpressure);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.adView = (AdView) view.findViewById(R.id.adView);

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.tv_bloodpressure.setTypeface(this.typefaceManager.getBold());
        this.tv_calculate_bloodpressure.setTypeface(this.typefaceManager.getBold());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.tv_calculate_bloodpressure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (BloodPressureCalculator.this.et_systolic_pressure.getText().toString().trim().equals("") || BloodPressureCalculator.this.et_systolic_pressure.getText().toString().trim().equals(".")) {
                    Toast.makeText(getContext().getApplicationContext(), BloodPressureCalculator.this.getString(R.string.Enter_systolic_value), Toast.LENGTH_SHORT).show();
                } else if (BloodPressureCalculator.this.et_diastolic_pressure.getText().toString().trim().equals("") || BloodPressureCalculator.this.et_diastolic_pressure.getText().toString().trim().equals(".")) {
                    Toast.makeText(getContext().getApplicationContext(), BloodPressureCalculator.this.getString(R.string.Enter_diastolic_value), Toast.LENGTH_SHORT).show();
                } else {
                    BloodPressureCalculator.this.systolic_val = BloodPressureCalculator.this.et_systolic_pressure.getText().toString().trim();
                    BloodPressureCalculator.this.diastolic_val = BloodPressureCalculator.this.et_diastolic_pressure.getText().toString().trim();
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb = new StringBuilder();
                    sb.append("random_number==>");
                    sb.append(random);
                    printStream.println(sb.toString());
                    if (random == 2) {
                        showIntertitial();
                        return;
                    }
//                    Intent intent = new Intent(BloodPressure_Calculator.this, BloodPressure_Result.class);
//                    intent.putExtra("systolic_val", Float.parseFloat(BloodPressure_Calculator.this.systolic_val));
//                    intent.putExtra("diastolic_val", Float.parseFloat(BloodPressure_Calculator.this.diastolic_val));
//                    BloodPressure_Calculator.this.startActivity(intent);
                    result(systolic_val,diastolic_val);
                }
            }
        });
    }

    private void result(String systolic_val, String diastolic_val) {
        String syastolic_result = "";
        String diastolic_result="";

        if (Float.valueOf(systolic_val).floatValue() < 120.0f) {
            syastolic_result = getString(R.string.Normal_Blood_Pressure);
        } else if (Float.valueOf(systolic_val).floatValue() >= 120.0f && Float.valueOf(systolic_val).floatValue() <= 139.0f) {
            syastolic_result = getString(R.string.Prehypertension);
        } else if (Float.valueOf(systolic_val).floatValue() >= 140.0f && Float.valueOf(systolic_val).floatValue() <= 159.0f) {
            syastolic_result = getString(R.string.High_Blood_Pressure_Stage_1);
        } else if (Float.valueOf(systolic_val).floatValue() >= 160.0f && Float.valueOf(systolic_val).floatValue() <= 180.0f) {
            syastolic_result = getString(R.string.High_Blood_Pressure_Stage_2);
        } else if (Float.valueOf(systolic_val).floatValue() > 180.0f) {
            syastolic_result = getString(R.string.Hypertensive_Crisis_Emergency_care_needed);
        }
        if (Float.valueOf(diastolic_val).floatValue() < 80.0f) {
            diastolic_result = getString(R.string.Normal_Blood_Pressure);
        } else if (Float.valueOf(diastolic_val).floatValue() >= 80.0f && Float.valueOf(diastolic_val).floatValue() <= 89.0f) {
            diastolic_result = getString(R.string.Prehypertension);
        } else if (Float.valueOf(diastolic_val).floatValue() >= 90.0f && Float.valueOf(diastolic_val).floatValue() <= 99.0f) {
            diastolic_result = getString(R.string.High_Blood_Pressure_Stage_1);
        } else if (Float.valueOf(diastolic_val).floatValue() >= 100.0f && Float.valueOf(diastolic_val).floatValue() <= 110.0f) {
            diastolic_result = getString(R.string.High_Blood_Pressure_Stage_2);
        } else if (Float.valueOf(diastolic_val).floatValue() > 110.0f) {
            diastolic_result = getString(R.string.Hypertensive_Crisis_Emergency_care_needed);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.upper_result));
        sb.append("\n");
        sb.append(syastolic_result);

        StringBuilder sb2 = new StringBuilder();
        sb2.append(getString(R.string.lower_result));
        sb2.append("\n");
        sb2.append(diastolic_result);

        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb.toString());
        sb3.append("\n");
        sb3.append("\n");
        sb3.append(sb2.toString());

        new AlertDialog.Builder(getActivity())
                .setTitle("Blood Pressure")
                .setMessage(sb3.toString())
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public void onResume() {
        super.onResume();
        if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue() && MyApplication.interstitial != null && !MyApplication.interstitial.isLoaded() && !MyApplication.interstitial.isLoading()) {
            ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                public void onNoResponse() {
                }

                public void onResponseObtained() {
                    MyApplication.interstitial.loadAd(new AdRequest.Builder().build());
                }
            });
        }
        if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
            MyApplication.interstitial.setAdListener(new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    MyApplication.interstitial.loadAd(new AdRequest.Builder().build());
//                    Intent intent = new Intent(BloodPressure_Calculator.this, BloodPressure_Result.class);
//                    intent.putExtra("systolic_val", BloodPressure_Calculator.this.systolic_val);
//                    intent.putExtra("diastolic_val", BloodPressure_Calculator.this.diastolic_val);
//                    BloodPressure_Calculator.this.startActivity(intent);
                    result(systolic_val,diastolic_val);

                }

                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    if (MyApplication.interstitial != null && !MyApplication.interstitial.isLoading()) {
                        ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                            public void onNoResponse() {
                            }

                            public void onResponseObtained() {
                                MyApplication.interstitial.loadAd(new AdRequest.Builder().build());
                            }
                        });
                    }
                }
            });
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

    public void showIntertitial() {
        if (this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
//            Intent intent = new Intent(this, BloodPressure_Result.class);
//            intent.putExtra("systolic_val", this.systolic_val);
//            intent.putExtra("diastolic_val", this.diastolic_val);
//            startActivity(intent);
            result(systolic_val,diastolic_val);

        } else if (MyApplication.interstitial == null || !MyApplication.interstitial.isLoaded()) {
            if (!MyApplication.interstitial.isLoading()) {
                ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                    public void onNoResponse() {
                    }

                    public void onResponseObtained() {
                        MyApplication.interstitial.loadAd(new AdRequest.Builder().build());
                    }
                });
            }
//            Intent intent2 = new Intent(this, BloodPressure_Result.class);
//            intent2.putExtra("systolic_val", this.systolic_val);
//            intent2.putExtra("diastolic_val", this.diastolic_val);
//            startActivity(intent2);
            result(systolic_val,diastolic_val);

        } else {
            MyApplication.interstitial.show();
        }
    }
}