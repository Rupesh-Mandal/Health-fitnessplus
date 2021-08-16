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
import java.util.ArrayList;

public class DailyWaterIntakeCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_weight;
    ArrayList<String> arraylist_weight = new ArrayList<>();
    EditText et_weight;
    GlobalFunction globalFunction;
    ImageView iv_back;
    ListView listViewWeight;
    private PopupWindow popupWindowWeight;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_calculate_waterintake;
    TextView tv_water_intake;
    TextView tv_weight;
    TypefaceManager typefaceManager;
    double water_intake;
    double weight;
    String weight_unit;

    public DailyWaterIntakeCalculator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waterintake_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.globalFunction = new GlobalFunction(getActivity());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.et_weight = (EditText) view.findViewById(R.id.et_weight);
        this.tv_weight = (TextView) view.findViewById(R.id.tv_weight);
        this.tv_calculate_waterintake = (TextView) view.findViewById(R.id.tv_calculate_waterintake);
        this.tv_water_intake = (TextView) view.findViewById(R.id.tv_water_intake);
        this.tv_water_intake.setTypeface(this.typefaceManager.getBold());
        this.tv_calculate_waterintake.setTypeface(this.typefaceManager.getBold());
        this.tv_weight.setOnClickListener(showPopupWindowWeight());
        this.arraylist_weight.clear();
        this.arraylist_weight.add(getString(R.string.lbs));
        this.arraylist_weight.add(getString(R.string.kg));
        this.adapter_weight = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_weight);
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.tv_calculate_waterintake.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (DailyWaterIntakeCalculator.this.et_weight.getText().toString().trim().equals("") || DailyWaterIntakeCalculator.this.et_weight.getText().toString().trim().equals(".")) {
                    Toast.makeText(getContext().getApplicationContext(), DailyWaterIntakeCalculator.this.getString(R.string.Enter_Weight), Toast.LENGTH_SHORT).show();
                    return;
                }
                DailyWaterIntakeCalculator.this.weight = Double.parseDouble(DailyWaterIntakeCalculator.this.et_weight.getText().toString().trim());
                int random = ((int) (Math.random() * 2.0d)) + 1;
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("random_number==>");
                sb.append(random);
                printStream.println(sb.toString());
                if (random == 2) {
                    DailyWaterIntakeCalculator.this.showIntertitial();
                } else {
                    DailyWaterIntakeCalculator.this.get_waterintake();
                }
            }
        });
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DailyWaterIntakeCalculator.this.onBackPressed();
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
                    DailyWaterIntakeCalculator.this.get_waterintake();
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

    public void get_waterintake() {
        this.weight_unit = this.tv_weight.getText().toString().trim();
        if (this.weight_unit.equalsIgnoreCase(getString(R.string.lbs))) {
            this.weight /= 2.204622d;
        }
        this.water_intake = this.weight / 0.024d;
        StringBuilder sb = new StringBuilder();
        sb.append("water_intake");
        sb.append(this.water_intake);
        Log.d("water_intake", sb.toString());
//        Intent intent = new Intent(this, Daily_WaterIntake_Result.class);
//        intent.putExtra("water_intake", this.water_intake);
//        startActivity(intent);
        resul(water_intake);
    }

    private void resul(double water_intake) {
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getString(R.string.Daily_Water_Intake));
        sb3.append("  : %.0f");
        sb2.append(String.format(sb3.toString(), new Object[]{water_intake}));
        sb2.append("ml");
        new AlertDialog.Builder(getActivity())
                .setTitle("Daily Water")
                .setMessage(sb2.toString())
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private OnClickListener showPopupWindowWeight() {
        return new OnClickListener() {
            public void onClick(View view) {
                DailyWaterIntakeCalculator.this.popupWindowWeight().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowWeight() {
        this.popupWindowWeight = new PopupWindow(getContext());
        this.listViewWeight = new ListView(getContext());
        this.listViewWeight.setDividerHeight(0);
        this.listViewWeight.setAdapter(this.adapter_weight);
        this.listViewWeight.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DailyWaterIntakeCalculator.this.tv_weight.setText((CharSequence) DailyWaterIntakeCalculator.this.arraylist_weight.get(i));
                DailyWaterIntakeCalculator.this.dismissPopupWeight();
            }
        });
        this.popupWindowWeight.setFocusable(true);
        this.popupWindowWeight.setWidth(this.tv_weight.getMeasuredWidth());
        this.popupWindowWeight.setHeight(-2);
        this.popupWindowWeight.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowWeight.setContentView(this.listViewWeight);
        return this.popupWindowWeight;
    }


    public void dismissPopupWeight() {
        if (this.popupWindowWeight != null) {
            this.popupWindowWeight.dismiss();
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
            get_waterintake();
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
            get_waterintake();
        } else {
            MyApplication.interstitial.show();
        }
    }
}
