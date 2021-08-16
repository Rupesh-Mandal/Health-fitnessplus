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

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.ArrayList;

public class CholestrolCalculator extends Fragment {

    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_cholestrol;
    ArrayList<String> arraylist_cholestrol = new ArrayList<>();
    EditText et_hdl;
    EditText et_ldl;
    EditText et_triglyceride;
    GlobalFunction globalFunction;
    ImageView iv_back;
    ListView listViewHeight;
    private PopupWindow popupWindowTime;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_cholestrol;
    TextView tv_hdl;
    TextView tv_ldl;
    TextView tv_search_cholestrol;
    TextView tv_triglyceride;
    TypefaceManager typefaceManager;

    public CholestrolCalculator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cholestrol_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.globalFunction = new GlobalFunction(getActivity());
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.tv_cholestrol = (TextView) view.findViewById(R.id.tv_cholestrol);
        this.tv_hdl = (TextView) view.findViewById(R.id.tv_hdl);
        this.tv_ldl = (TextView) view.findViewById(R.id.tv_ldl);
        this.tv_triglyceride = (TextView) view.findViewById(R.id.tv_triglyceride);
        this.tv_search_cholestrol = (TextView) view.findViewById(R.id.tv_search_cholestrol);
        this.et_hdl = (EditText) view.findViewById(R.id.et_hdl);
        this.et_ldl = (EditText) view.findViewById(R.id.et_ldl);
        this.et_triglyceride = (EditText) view.findViewById(R.id.et_triglyceride);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.et_hdl.setTypeface(this.typefaceManager.getLight());
        this.et_ldl.setTypeface(this.typefaceManager.getLight());
        this.tv_cholestrol.setTypeface(this.typefaceManager.getBold());
        this.tv_hdl.setTypeface(this.typefaceManager.getLight());
        this.tv_ldl.setTypeface(this.typefaceManager.getLight());
        this.tv_search_cholestrol.setTypeface(this.typefaceManager.getBold());
        this.tv_triglyceride.setTypeface(this.typefaceManager.getLight());
        this.et_triglyceride.setTypeface(this.typefaceManager.getLight());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.tv_hdl.setOnClickListener(showPopupWindowTime(0));
        this.tv_ldl.setOnClickListener(showPopupWindowTime(1));
        this.tv_triglyceride.setOnClickListener(showPopupWindowTime(2));
        this.arraylist_cholestrol.clear();
        this.arraylist_cholestrol.add(getString(R.string.mgdl));
        this.arraylist_cholestrol.add(getString(R.string.mgol_a));
        this.adapter_cholestrol = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_cholestrol);
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CholestrolCalculator.this.onBackPressed();
            }
        });
        this.tv_search_cholestrol.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (CholestrolCalculator.this.et_hdl.getText().toString().trim().equals("")) {
                    CholestrolCalculator.this.et_hdl.setError(CholestrolCalculator.this.getString(R.string.enter_hdl));
                } else if (CholestrolCalculator.this.et_ldl.getText().toString().trim().equals("")) {
                    CholestrolCalculator.this.et_ldl.setError(CholestrolCalculator.this.getString(R.string.enter_ldl));
                } else if (CholestrolCalculator.this.et_triglyceride.getText().toString().trim().equals("")) {
                    CholestrolCalculator.this.et_triglyceride.setError(CholestrolCalculator.this.getString(R.string.enter_triglyceride));
                } else {
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb = new StringBuilder();
                    sb.append("random_number==>");
                    sb.append(random);
                    printStream.println(sb.toString());
                    if (random == 2) {
                        CholestrolCalculator.this.showIntertitial();
                    } else {
                        CholestrolCalculator.this.calculate();
                    }
                }
            }
        });
    }

    public void calculate() {
        float f;
        float f2;
        float f3;
        if (this.tv_hdl.getText().toString().trim().equals(getString(R.string.mgdl))) {
            f = Float.parseFloat(this.et_hdl.getText().toString().trim());
        } else {
            f = Float.parseFloat(this.et_hdl.getText().toString().trim()) * 18.0f;
        }
        if (this.tv_ldl.getText().toString().trim().equals(getString(R.string.mgdl))) {
            f2 = Float.parseFloat(this.et_ldl.getText().toString().trim());
        } else {
            f2 = Float.parseFloat(this.et_ldl.getText().toString().trim()) * 18.0f;
        }
        if (this.tv_triglyceride.getText().toString().trim().equals(getString(R.string.mgdl))) {
            f3 = Float.parseFloat(this.et_triglyceride.getText().toString().trim());
        } else {
            f3 = Float.parseFloat(this.et_triglyceride.getText().toString().trim()) * 18.0f;
        }
        double d = (double) (f + f2 + (f3 / 5.0f));
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(d);
        Log.d("cholestrol->", sb.toString());
//        Intent intent = new Intent(this, Cholestrol_Result.class);
//        intent.putExtra("cholestrol", d);
//        startActivity(intent);
        result(d);
    }

    private void result(double d) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(d);
        Log.d("cholestrol->", sb.toString());

        StringBuilder sb2 = new StringBuilder();
        sb2.append(getString(R.string.your_cholestrol));
        sb2.append(" : ");
        sb2.append(String.format("%.0f", new Object[]{Double.valueOf(d)}));
        new AlertDialog.Builder(getActivity())
                .setTitle("Cholestrol")
                .setMessage(sb2.toString())
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private OnClickListener showPopupWindowTime(final int i) {
        return new OnClickListener() {
            public void onClick(View view) {
                CholestrolCalculator.this.popupWindowTime(i).showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowTime(final int i) {
        this.popupWindowTime = new PopupWindow(getContext());
        this.listViewHeight = new ListView(getContext());
        this.listViewHeight.setDividerHeight(0);
        this.listViewHeight.setAdapter(this.adapter_cholestrol);
        this.listViewHeight.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (i == 0) {
                    CholestrolCalculator.this.tv_hdl.setText((CharSequence) CholestrolCalculator.this.arraylist_cholestrol.get(i));
                } else if (i == 1) {
                    CholestrolCalculator.this.tv_ldl.setText((CharSequence) CholestrolCalculator.this.arraylist_cholestrol.get(i));
                } else {
                    CholestrolCalculator.this.tv_triglyceride.setText((CharSequence) CholestrolCalculator.this.arraylist_cholestrol.get(i));
                }
                CholestrolCalculator.this.dismissPopupTime();
            }
        });
        this.popupWindowTime.setFocusable(true);
        this.popupWindowTime.setWidth(this.tv_hdl.getMeasuredWidth());
        this.popupWindowTime.setHeight(-2);
        this.popupWindowTime.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowTime.setContentView(this.listViewHeight);
        return this.popupWindowTime;
    }


    public void dismissPopupTime() {
        if (this.popupWindowTime != null) {
            this.popupWindowTime.dismiss();
        }
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
                    CholestrolCalculator.this.calculate();
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
