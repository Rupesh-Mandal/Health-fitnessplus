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
import java.io.PrintStream;
import java.util.ArrayList;
public class TrademillCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    EditText et_minute;
    String gender;
    GlobalFunction globalFunction;
    ImageView iv_back;
    ListView listViewGender;
    private PopupWindow popupWindowGender;
    SharedPreferenceManager sharedPreferenceManager;
    Double trademill;
    Double trademill_time;
    TextView tv_calculate_trademill;
    TextView tv_gender;
    TextView tv_genderunit;
    TextView tv_trademill;
    TypefaceManager typefaceManager;

    public TrademillCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trademil_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.globalFunction = new GlobalFunction(getActivity());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.tv_genderunit = (TextView) view.findViewById(R.id.tv_genderunit);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.tv_calculate_trademill = (TextView) view.findViewById(R.id.tv_calculate_trademill);
        this.et_minute = (EditText) view.findViewById(R.id.et_minute);
        this.tv_trademill = (TextView) view.findViewById(R.id.tv_trademill);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.tv_trademill.setTypeface(this.typefaceManager.getBold());
        this.tv_calculate_trademill.setTypeface(this.typefaceManager.getBold());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.tv_gender.setOnClickListener(showPopupWindowGender());
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.tv_calculate_trademill.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (TrademillCalculator.this.et_minute.getText().toString().trim().equals("")) {
                    Toast.makeText(getContext().getApplicationContext(), TrademillCalculator.this.getString(R.string.Enter_time), Toast.LENGTH_SHORT).show();
                    return;
                }
                TrademillCalculator.this.gender = TrademillCalculator.this.tv_gender.getText().toString().trim();
                TrademillCalculator.this.trademill_time = Double.valueOf(Double.parseDouble(TrademillCalculator.this.et_minute.getText().toString().trim()));
                int random = ((int) (Math.random() * 2.0d)) + 1;
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("random_number==>");
                sb.append(random);
                printStream.println(sb.toString());
                if (random == 2) {
                    TrademillCalculator.this.showIntertitial();
                } else {
                    TrademillCalculator.this.gettrademill();
                }
            }
        });
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TrademillCalculator.this.onBackPressed();
            }
        });
    }

    public void gettrademill() {
        if (this.gender.equalsIgnoreCase(getString(R.string.Male))) {
            this.trademill = Double.valueOf(((14.8d - (this.trademill_time.doubleValue() * 1.379d)) + ((this.trademill_time.doubleValue() * 0.451d) * this.trademill_time.doubleValue())) - (((this.trademill_time.doubleValue() * 0.012d) * this.trademill_time.doubleValue()) * this.trademill_time.doubleValue()));
        } else {
            this.trademill = Double.valueOf((this.trademill_time.doubleValue() * 4.38d) - 3.9d);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("trademill");
        sb.append(this.trademill);
        Log.d("trademill", sb.toString());
//        Intent intent = new Intent(this, Trademill_Result.class);
//        intent.putExtra("trademill", this.trademill);
//        startActivity(intent);
        result(trademill);
    }

    private void result(Double trademill) {
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.Bruce_trade_mill));
        sb.append("%.2f");
        new AlertDialog.Builder(getActivity())
                .setTitle("Trademill")
                .setMessage(String.format(sb.toString(), new Object[]{this.trademill}))
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private OnClickListener showPopupWindowGender() {
        return new OnClickListener() {
            public void onClick(View view) {
                TrademillCalculator.this.popupWindowGender().showAsDropDown(view, 0, 0);
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
                TrademillCalculator.this.tv_gender.setText((CharSequence) TrademillCalculator.this.arraylist_gender.get(i));
                TrademillCalculator.this.tv_genderunit.setText((CharSequence) TrademillCalculator.this.arraylist_gender.get(i));
                TrademillCalculator.this.dismissPopupGender();
            }
        });
        this.popupWindowGender.setFocusable(true);
        this.popupWindowGender.setWidth(this.tv_gender.getMeasuredWidth());
        this.popupWindowGender.setHeight(-2);
        this.popupWindowGender.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowGender.setContentView(this.listViewGender);
        return this.popupWindowGender;
    }


    public void dismissPopupGender() {
        if (this.popupWindowGender != null) {
            this.popupWindowGender.dismiss();
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
            gettrademill();
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
            gettrademill();
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
                    TrademillCalculator.this.gettrademill();
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
