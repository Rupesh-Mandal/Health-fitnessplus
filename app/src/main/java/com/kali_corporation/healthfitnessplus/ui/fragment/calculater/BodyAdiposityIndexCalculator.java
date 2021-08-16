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


public class BodyAdiposityIndexCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_height;
    ArrayAdapter<String> adapter_wrist;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_height = new ArrayList<>();
    ArrayList<String> arraylist_wrist = new ArrayList<>();
    double bai;
    EditText et_height;
    EditText et_weight;
    GlobalFunction globalFunction;
    float height;
    String height_unit;
    float inserted_height;
    float inserted_waist;
    ImageView iv_back;
    ListView listViewHeight;

    public PopupWindow popupWindowHeight;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_body_adopisity_index;
    TextView tv_heightunit;
    TextView tv_search_bai;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    float waist;
    String weight_unit;


    public BodyAdiposityIndexCalculator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_adopisity_index, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.globalFunction = new GlobalFunction(getActivity());
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.et_height = (EditText) view.findViewById(R.id.et_height);
        this.et_weight = (EditText) view.findViewById(R.id.et_weight);
        this.tv_heightunit = (TextView) view.findViewById(R.id.tv_heightunit);
        this.tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        this.tv_search_bai = (TextView) view.findViewById(R.id.tv_search_bai);
        this.tv_body_adopisity_index = (TextView) view.findViewById(R.id.tv_body_adopisity_index);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.tv_body_adopisity_index.setTypeface(this.typefaceManager.getBold());
        this.et_height.setTypeface(this.typefaceManager.getLight());
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_search_bai.setTypeface(this.typefaceManager.getBold());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.height_unit = getString(R.string.feet);
        this.weight_unit = getString(R.string.lbs);
        this.tv_heightunit.setOnClickListener(showPopupWindowHeight(true));
        this.tv_weightunit.setOnClickListener(showPopupWindowHeight(false));
        this.arraylist_wrist.clear();
        this.arraylist_wrist.add(getString(R.string.cm));
        this.arraylist_wrist.add(getString(R.string.inch));
        this.arraylist_height.clear();
        this.arraylist_height.add(getString(R.string.cm));
        this.arraylist_height.add(getString(R.string.feet));
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.adapter_wrist = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_wrist);
        this.adapter_height = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_height);
        this.tv_search_bai.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (BodyAdiposityIndexCalculator.this.et_height.getText().toString().trim().equals("") || BodyAdiposityIndexCalculator.this.et_height.getText().toString().trim().equals(".")) {
                    BodyAdiposityIndexCalculator.this.et_height.setError(BodyAdiposityIndexCalculator.this.getString(R.string.Enter_Height));
                } else if (BodyAdiposityIndexCalculator.this.et_weight.getText().toString().trim().equals("") || BodyAdiposityIndexCalculator.this.et_weight.getText().toString().trim().equals(".")) {
                    BodyAdiposityIndexCalculator.this.et_weight.setError(BodyAdiposityIndexCalculator.this.getString(R.string.Enter_Weight));
                } else {
                    BodyAdiposityIndexCalculator.this.height_unit = BodyAdiposityIndexCalculator.this.tv_heightunit.getText().toString();
                    BodyAdiposityIndexCalculator.this.weight_unit = BodyAdiposityIndexCalculator.this.tv_weightunit.getText().toString();
                    BodyAdiposityIndexCalculator.this.inserted_height = Float.parseFloat(BodyAdiposityIndexCalculator.this.et_height.getText().toString());
                    BodyAdiposityIndexCalculator.this.inserted_waist = Float.parseFloat(BodyAdiposityIndexCalculator.this.et_weight.getText().toString());
                    StringBuilder sb = new StringBuilder();
                    sb.append("inserted_waist");
                    sb.append(BodyAdiposityIndexCalculator.this.inserted_waist);
                    Log.d("inserted_waist", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("inserted_height");
                    sb2.append(BodyAdiposityIndexCalculator.this.inserted_height);
                    Log.d("inserted_height", sb2.toString());
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("random_number==>");
                    sb3.append(random);
                    printStream.println(sb3.toString());
                    if (random == 2) {
                        BodyAdiposityIndexCalculator.this.showIntertitial();
                    } else {
                        BodyAdiposityIndexCalculator.this.calculate();
                    }
                }
            }
        });
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BodyAdiposityIndexCalculator.this.onBackPressed();
            }
        });
    }

    private OnClickListener showPopupWindowHeight(final boolean z) {
        return new OnClickListener() {
            public void onClick(View view) {
                BodyAdiposityIndexCalculator.this.popupWindowHeight(z).showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowHeight(final boolean z) {
        this.popupWindowHeight = new PopupWindow(getContext());
        this.listViewHeight = new ListView(getContext());
        this.listViewHeight.setDividerHeight(0);
        if (z) {
            this.listViewHeight.setAdapter(this.adapter_height);
        } else {
            this.listViewHeight.setAdapter(this.adapter_wrist);
        }
        this.listViewHeight.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (z) {
                    BodyAdiposityIndexCalculator.this.tv_heightunit.setText((CharSequence) BodyAdiposityIndexCalculator.this.arraylist_height.get(i));
                } else {
                    BodyAdiposityIndexCalculator.this.tv_weightunit.setText((CharSequence) BodyAdiposityIndexCalculator.this.arraylist_wrist.get(i));
                }
                BodyAdiposityIndexCalculator.this.popupWindowHeight.dismiss();
            }
        });
        this.popupWindowHeight.setFocusable(true);
        this.popupWindowHeight.setWidth(this.tv_heightunit.getMeasuredWidth());
        this.popupWindowHeight.setHeight(-2);
        this.popupWindowHeight.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowHeight.setContentView(this.listViewHeight);
        return this.popupWindowHeight;
    }


    public void calculate() {
        StringBuilder sb = new StringBuilder();
        sb.append("inserted_waist");
        sb.append(this.inserted_waist);
        Log.d("inserted_waist", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("inserted_height");
        sb2.append(this.inserted_height);
        Log.d("inserted_height", sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("height_unit");
        sb3.append(this.height_unit);
        Log.d("height_unit", sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("weight_unit");
        sb4.append(this.weight_unit);
        Log.d("weight_unit", sb4.toString());
        if (this.weight_unit.equalsIgnoreCase(getString(R.string.cm))) {
            this.waist = this.inserted_waist;
        } else {
            this.waist = this.inserted_waist * 2.54f;
        }
        if (this.height_unit.equalsIgnoreCase(getString(R.string.cm))) {
            this.height = this.inserted_height;
        } else {
            this.height = this.inserted_height / 0.032808f;
        }
        this.waist /= 100.0f;
        this.height /= 100.0f;
        double d = (double) (this.waist * 100.0f);
        double d2 = (double) this.height;
        double sqrt = Math.sqrt((double) this.height);
        Double.isNaN(d2);
        double d3 = d2 * sqrt;
        Double.isNaN(d);
        this.bai = d / d3;
        this.bai -= 18.0d;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("bai->");
        sb5.append(this.bai);
        Log.d("bai->", sb5.toString());
//        Intent intent = new Intent(this, Body_Adiposity_Index_Result.class);
//        intent.putExtra("bai", String.format("%.2f", new Object[]{Double.valueOf(this.bai)}));
//        startActivity(intent);
        result(bai);
    }

    private void result(double bai) {
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.bai_is));
        sb.append(" ");
        sb.append(String.format("%.2f", new Object[]{Double.valueOf(bai)}));
        sb.append("%");
        new AlertDialog.Builder(getActivity())
                .setTitle("Body Adiposity")
                .setMessage(sb.toString())
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
        this.adView.setVisibility(8);
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
                    BodyAdiposityIndexCalculator.this.calculate();
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
