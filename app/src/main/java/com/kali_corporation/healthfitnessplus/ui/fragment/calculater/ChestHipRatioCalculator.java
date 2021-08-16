package com.kali_corporation.healthfitnessplus.ui.fragment.calculater;

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

import org.jetbrains.annotations.NotNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
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
import java.io.PrintStream;
import java.util.ArrayList;

public class ChestHipRatioCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_wrist;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_wrist = new ArrayList<>();
    float bmi_hip;
    float bmi_waist;
    double chr;
    double chr_percentage;
    EditText et_height;
    EditText et_weight;
    String gender = "";
    GlobalFunction globalFunction;
    String height_unit;
    float inserted_hip;
    float inserted_waist;
    ImageView iv_back;
    ListView listViewGender;
    ListView listViewHeight;
    private PopupWindow popupWindowGender;

    public PopupWindow popupWindowHeight;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_chest_to_hip;
    TextView tv_gender;
    TextView tv_genderunit;
    TextView tv_heightunit;
    TextView tv_search_whr;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    String weight_unit;


    public ChestHipRatioCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chest_hip_ratio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.globalFunction = new GlobalFunction(getActivity());
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.adView = (AdView) view.findViewById(R.id.adView);

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);

        this.et_height = (EditText) view.findViewById(R.id.et_height);
        this.et_weight = (EditText) view.findViewById(R.id.et_weight);
        this.tv_chest_to_hip = (TextView) view.findViewById(R.id.tv_chest_to_hip);
        this.tv_heightunit = (TextView) view.findViewById(R.id.tv_heightunit);
        this.tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        this.tv_search_whr = (TextView) view.findViewById(R.id.tv_search_whr);
        this.tv_genderunit = (TextView) view.findViewById(R.id.tv_genderunit);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.tv_chest_to_hip.setTypeface(this.typefaceManager.getBold());
        this.et_height.setTypeface(this.typefaceManager.getLight());
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_search_whr.setTypeface(this.typefaceManager.getBold());
        this.tv_genderunit.setTypeface(this.typefaceManager.getLight());
        this.tv_gender.setTypeface(this.typefaceManager.getLight());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.height_unit = getString(R.string.feet);
        this.weight_unit = getString(R.string.lbs);
        this.tv_heightunit.setOnClickListener(showPopupWindowHeight(true));
        this.tv_weightunit.setOnClickListener(showPopupWindowHeight(false));
        this.tv_gender.setOnClickListener(showPopupWindowGender());
        this.arraylist_wrist.clear();
        this.arraylist_wrist.add(getString(R.string.cm));
        this.arraylist_wrist.add(getString(R.string.inch));
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.adapter_wrist = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_wrist);
        this.tv_search_whr.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ChestHipRatioCalculator.this.et_height.getText().toString().trim().equals("") || ChestHipRatioCalculator.this.et_height.getText().toString().trim().equals(".")) {
                    ChestHipRatioCalculator.this.et_height.setError(ChestHipRatioCalculator.this.getString(R.string.Enter_Height));
                } else if (ChestHipRatioCalculator.this.et_weight.getText().toString().trim().equals("") || ChestHipRatioCalculator.this.et_weight.getText().toString().trim().equals(".")) {
                    ChestHipRatioCalculator.this.et_weight.setError(ChestHipRatioCalculator.this.getString(R.string.Enter_Weight));
                } else {
                    ChestHipRatioCalculator.this.height_unit = ChestHipRatioCalculator.this.tv_heightunit.getText().toString();
                    ChestHipRatioCalculator.this.weight_unit = ChestHipRatioCalculator.this.tv_weightunit.getText().toString();
                    ChestHipRatioCalculator.this.inserted_waist = Float.parseFloat(ChestHipRatioCalculator.this.et_height.getText().toString());
                    ChestHipRatioCalculator.this.inserted_hip = Float.parseFloat(ChestHipRatioCalculator.this.et_weight.getText().toString());
                    StringBuilder sb = new StringBuilder();
                    sb.append("inserted_waist");
                    sb.append(ChestHipRatioCalculator.this.inserted_waist);
                    Log.d("inserted_waist", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("inserted_height");
                    sb2.append(ChestHipRatioCalculator.this.inserted_hip);
                    Log.d("inserted_height", sb2.toString());
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("random_number==>");
                    sb3.append(random);
                    printStream.println(sb3.toString());
                    if (random == 2) {
                        ChestHipRatioCalculator.this.showIntertitial();
                    } else {
                        ChestHipRatioCalculator.this.calculate();
                    }
                }
            }
        });
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ChestHipRatioCalculator.this.onBackPressed();
            }
        });
    }


    public void calculate() {
        StringBuilder sb = new StringBuilder();
        sb.append("inserted_waist");
        sb.append(this.inserted_waist);
        Log.d("inserted_waist", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("inserted_hip");
        sb2.append(this.inserted_hip);
        Log.d("inserted_hip", sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("height_unit");
        sb3.append(this.height_unit);
        Log.d("height_unit", sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("weight_unit");
        sb4.append(this.weight_unit);
        Log.d("weight_unit", sb4.toString());
        if (this.height_unit.equalsIgnoreCase(getString(R.string.inch))) {
            this.bmi_waist = this.inserted_waist;
        } else {
            this.bmi_waist = this.inserted_waist / 2.54f;
        }
        if (this.weight_unit.equalsIgnoreCase(getString(R.string.inch))) {
            this.bmi_hip = this.inserted_hip;
        } else {
            this.bmi_hip = this.inserted_hip / 2.54f;
        }
        this.gender = this.tv_gender.getText().toString().trim();
        this.chr = (double) (this.bmi_waist / this.bmi_hip);
        this.chr_percentage = (double) ((this.bmi_waist / this.bmi_hip) * 100.0f);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("chr->");
        sb5.append(String.format("%.2f", new Object[]{Double.valueOf(this.chr)}));
        Log.d("chr->", sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        sb6.append("chr_percentage->");
        sb6.append(String.format("%.2f", new Object[]{Double.valueOf(this.chr_percentage)}));
        Log.d("chr_percentage->", sb6.toString());
        StringBuilder sb7 = new StringBuilder();
        sb7.append("gender->");
        sb7.append(this.gender);
        Log.d("gender->", sb7.toString());
//        Intent intent = new Intent(this, Chest_Hip_Ratio_Result.class);
//        intent.putExtra("chr", String.format("%.2f", new Object[]{Double.valueOf(this.chr)}));
//        intent.putExtra("chr_percentage", String.format("%.2f", new Object[]{Double.valueOf(this.chr_percentage)}));
//        startActivity(intent);
        result(chr,chr_percentage);
    }

    private void result(double chr, double chr_percentage) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(chr);
        Log.d("chr->", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(chr_percentage);
        Log.d("chr_percentage->", sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getString(R.string.your_chr));
        sb3.append(" ");
        sb3.append(chr);

        StringBuilder sb4 = new StringBuilder();
        sb4.append(getString(R.string.chr_percentage));
        sb4.append(" ");
        sb4.append(chr_percentage);

        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb3.toString());
        sb5.append("\n");
        sb5.append(sb4.toString());
        new AlertDialog.Builder(getActivity())
                .setTitle("Hip Ratio")
                .setMessage(sb5.toString())
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
            calculate();
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
            calculate();
        } else {
            MyApplication.interstitial.show();
        }
    }

    private OnClickListener showPopupWindowHeight(final boolean z) {
        return new OnClickListener() {
            public void onClick(View view) {
                ChestHipRatioCalculator.this.popupWindowHeight(z).showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowHeight(final boolean z) {
        this.popupWindowHeight = new PopupWindow(getContext());
        this.listViewHeight = new ListView(getContext());
        this.listViewHeight.setDividerHeight(0);
        if (z) {
            this.listViewHeight.setAdapter(this.adapter_wrist);
        } else {
            this.listViewHeight.setAdapter(this.adapter_wrist);
        }
        this.listViewHeight.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (z) {
                    ChestHipRatioCalculator.this.tv_heightunit.setText((CharSequence) ChestHipRatioCalculator.this.arraylist_gender.get(i));
                } else {
                    ChestHipRatioCalculator.this.tv_weightunit.setText((CharSequence) ChestHipRatioCalculator.this.arraylist_wrist.get(i));
                }
                ChestHipRatioCalculator.this.popupWindowHeight.dismiss();
            }
        });
        this.popupWindowHeight.setFocusable(true);
        this.popupWindowHeight.setWidth(this.tv_heightunit.getMeasuredWidth());
        this.popupWindowHeight.setHeight(-2);
        this.popupWindowHeight.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.img_chest_to_hip_ratio));
        this.popupWindowHeight.setContentView(this.listViewHeight);
        return this.popupWindowHeight;
    }

    private OnClickListener showPopupWindowGender() {
        return new OnClickListener() {
            public void onClick(View view) {
                ChestHipRatioCalculator.this.popupWindowGender().showAsDropDown(view, 0, 0);
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
                ChestHipRatioCalculator.this.tv_gender.setText((CharSequence) ChestHipRatioCalculator.this.arraylist_gender.get(i));
                ChestHipRatioCalculator.this.tv_genderunit.setText((CharSequence) ChestHipRatioCalculator.this.arraylist_gender.get(i));
                ChestHipRatioCalculator.this.dismissPopupGender();
            }
        });
        this.popupWindowGender.setFocusable(true);
        this.popupWindowGender.setWidth(this.tv_gender.getMeasuredWidth());
        this.popupWindowGender.setHeight(-2);
        this.popupWindowGender.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.img_chest_to_hip_ratio));
        this.popupWindowGender.setContentView(this.listViewGender);
        return this.popupWindowGender;
    }


    public void dismissPopupGender() {
        if (this.popupWindowGender != null) {
            this.popupWindowGender.dismiss();
        }
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
                    ChestHipRatioCalculator.this.calculate();
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
}
