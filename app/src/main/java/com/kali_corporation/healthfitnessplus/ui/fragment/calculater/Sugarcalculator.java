package com.kali_corporation.healthfitnessplus.ui.fragment.calculater;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.sevice.general.MyApplication;
import com.kali_corporation.healthfitnessplus.sevice.utils.GlobalFunction;
import com.kali_corporation.healthfitnessplus.sevice.utils.SharedPreferenceManager;
import com.kali_corporation.healthfitnessplus.sevice.utils.TypefaceManager;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.interfaces.NetworkRequestCheckListener;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.ArrayList;


public class Sugarcalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_sugar;
    ArrayList<String> arraylist_sugar = new ArrayList<>();
    Double blood_sugarval;
    EditText et_sugar_value;
    Double final_bloodsugar_val;
    GlobalFunction globalFunction;
    ImageView iv_back;
    ListView listViewsugar;
    private PopupWindow popupWindowsugar;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_caluculate_blood_sugar;
    TextView tv_sugar;
    TextView tv_sugar_unit;
    TypefaceManager typefaceManager;

    public Sugarcalculator() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sugar_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.globalFunction = new GlobalFunction(getActivity());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.tv_sugar = (TextView) view.findViewById(R.id.tv_sugar);
        this.tv_sugar_unit = (TextView) view.findViewById(R.id.tv_sugar_unit);
        this.et_sugar_value = (EditText) view.findViewById(R.id.et_sugar_value);
        this.tv_caluculate_blood_sugar = (TextView) view.findViewById(R.id.tv_caluculate_blood_sugar);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.tv_sugar_unit.setOnClickListener(showPopupWindowTime());
        this.arraylist_sugar.clear();
        this.arraylist_sugar.add(getString(R.string.mmol));
        this.arraylist_sugar.add(getString(R.string.mg));
        this.adapter_sugar = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_sugar);
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.tv_sugar.setTypeface(this.typefaceManager.getBold());
        this.tv_caluculate_blood_sugar.setTypeface(this.typefaceManager.getBold());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Sugarcalculator.this.onBackPressed();
            }
        });
        this.tv_caluculate_blood_sugar.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (Sugarcalculator.this.et_sugar_value.getText().toString().trim().equals("") || Sugarcalculator.this.et_sugar_value.getText().toString().trim().equals(".")) {
                    Toast.makeText(getContext().getApplicationContext(), Sugarcalculator.this.getString(R.string.Enter_Blood_sugar_value), Toast.LENGTH_SHORT).show();
                    return;
                }
                Sugarcalculator.this.blood_sugarval = Double.valueOf(Double.parseDouble(Sugarcalculator.this.et_sugar_value.getText().toString().toString()));
                if (Sugarcalculator.this.tv_sugar_unit.getText().toString().trim().equals(Sugarcalculator.this.getString(R.string.mmol))) {
                    Sugarcalculator.this.calculate_m1();
                } else {
                    Sugarcalculator.this.calculate_m2();
                }
                int random = ((int) (Math.random() * 2.0d)) + 1;
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("random_number==>");
                sb.append(random);
                printStream.println(sb.toString());
                if (random == 2) {
                    Sugarcalculator.this.showIntertitial();
                    return;
                }
//                Intent intent = new Intent(Sugar_calculator.this, Sugar_Result.class);
//                intent.putExtra("final_bloodsugar_val", Sugar_calculator.this.final_bloodsugar_val);
//                Sugar_calculator.this.startActivity(intent);
                result(final_bloodsugar_val);
            }
        });
    }

    private void result(Double final_bloodsugar_val) {
        if (final_bloodsugar_val.doubleValue() < 0.0d) {
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.Blood_Sugar));
            sb.append(" : 0 ]");
            sb.append(getString(R.string.mmol_a));
            openDailog(sb.toString());

        } else {
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(getString(R.string.Blood_Sugar));
            sb3.append(" : %.2f");
            sb2.append(String.format(sb3.toString(), new Object[]{this.final_bloodsugar_val}));
            sb2.append(" ");
            sb2.append(getString(R.string.mmol_a));
            openDailog(sb2.toString());
        }
    }

    private void openDailog(String sb) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Sugar")
                .setMessage(sb)
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void calculate_m1() {
        this.final_bloodsugar_val = Double.valueOf(this.blood_sugarval.doubleValue() * 18.0d);
    }

    public void calculate_m2() {
        this.final_bloodsugar_val = Double.valueOf(this.blood_sugarval.doubleValue() * 0.05556d);
    }

    private OnClickListener showPopupWindowTime() {
        return new OnClickListener() {
            public void onClick(View view) {
                Sugarcalculator.this.popupWindowTime().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowTime() {
        this.popupWindowsugar = new PopupWindow(getContext());
        this.listViewsugar = new ListView(getContext());
        this.listViewsugar.setDividerHeight(0);
        this.listViewsugar.setAdapter(this.adapter_sugar);
        this.listViewsugar.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Sugarcalculator.this.tv_sugar_unit.setText((CharSequence) Sugarcalculator.this.arraylist_sugar.get(i));
                Sugarcalculator.this.dismissPopupTime();
            }
        });
        this.popupWindowsugar.setFocusable(true);
        this.popupWindowsugar.setWidth(this.tv_sugar_unit.getMeasuredWidth());
        this.popupWindowsugar.setHeight(-2);
        this.popupWindowsugar.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowsugar.setContentView(this.listViewsugar);
        return this.popupWindowsugar;
    }


    public void dismissPopupTime() {
        if (this.popupWindowsugar != null) {
            this.popupWindowsugar.dismiss();
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
//            Intent intent = new Intent(this, Sugar_Result.class);
//            intent.putExtra("final_bloodsugar_val", this.final_bloodsugar_val);
//            startActivity(intent);
            result(final_bloodsugar_val);

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
//            Intent intent2 = new Intent(this, Sugar_Result.class);
//            intent2.putExtra("final_bloodsugar_val", this.final_bloodsugar_val);
//            startActivity(intent2);
            result(final_bloodsugar_val);

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
//                    Intent intent = new Intent(Sugar_calculator.this, Sugar_Result.class);
//                    intent.putExtra("final_bloodsugar_val", Sugar_calculator.this.final_bloodsugar_val);
//                    Sugar_calculator.this.startActivity(intent);
                    result(final_bloodsugar_val);

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
