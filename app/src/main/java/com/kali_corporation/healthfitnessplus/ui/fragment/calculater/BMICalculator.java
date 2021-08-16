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

public class BMICalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_height;
    ArrayAdapter<String> adapter_weight;
    int age;
    ArrayList<String> arraylist_height = new ArrayList<>();
    ArrayList<String> arraylist_weigth = new ArrayList<>();
    float bmi;
    float bmi_height;
    float bmi_weight;
    EditText et_age;
    EditText et_height;
    EditText et_weight;
    GlobalFunction globalFunction;
    String height_unit;
    float inserted_height;
    float inserted_weight;
    ImageView iv_back;
    ListView listViewHeight;
    ListView listViewWeight;
    private PopupWindow popupWindowHeight;
    private PopupWindow popupWindowWeight;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_bmi;
    TextView tv_heightunit;
    TextView tv_search_bmi;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    String weight_unit;


    public BMICalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bmi_calculator, container, false);
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
        this.et_age = (EditText) view.findViewById(R.id.et_age);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.tv_bmi = (TextView) view.findViewById(R.id.tv_bmi);
        this.tv_heightunit = (TextView) view.findViewById(R.id.tv_heightunit);
        this.tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        this.tv_search_bmi = (TextView) view.findViewById(R.id.tv_search_bmi);
        this.et_height.setTypeface(this.typefaceManager.getLight());
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.et_age.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_search_bmi.setTypeface(this.typefaceManager.getBold());
        this.tv_bmi.setTypeface(this.typefaceManager.getBold());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BMICalculator.this.onBackPressed();
            }
        });
        this.height_unit = getString(R.string.feet);
        this.weight_unit = getString(R.string.lbs);
        this.tv_heightunit.setOnClickListener(showPopupWindowHeight());
        this.tv_weightunit.setOnClickListener(showPopupWindow_Weight());
        this.arraylist_height.clear();
        this.arraylist_height.add(getString(R.string.feet));
        this.arraylist_height.add(getString(R.string.cm));
        this.arraylist_weigth.clear();
        this.arraylist_weigth.add(getString(R.string.kg));
        this.arraylist_weigth.add(getString(R.string.lbs));
        this.adapter_height = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_height);
        this.adapter_weight = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_weigth);
        this.tv_search_bmi.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (BMICalculator.this.et_age.getText().toString().trim().equals("")) {
                    BMICalculator.this.et_age.setError(BMICalculator.this.getString(R.string.Enter_Age));
                } else if (BMICalculator.this.et_height.getText().toString().trim().equals("") || BMICalculator.this.et_height.getText().toString().trim().equals(".")) {
                    BMICalculator.this.et_height.setError(BMICalculator.this.getString(R.string.Enter_Height));
                } else if (BMICalculator.this.et_weight.getText().toString().trim().equals("") || BMICalculator.this.et_weight.getText().toString().trim().equals(".")) {
                    BMICalculator.this.et_weight.setError(BMICalculator.this.getString(R.string.Enter_Weight));
                } else {
                    BMICalculator.this.height_unit = BMICalculator.this.tv_heightunit.getText().toString();
                    BMICalculator.this.weight_unit = BMICalculator.this.tv_weightunit.getText().toString();
                    BMICalculator.this.inserted_weight = Float.parseFloat(BMICalculator.this.et_weight.getText().toString());
                    BMICalculator.this.inserted_height = Float.parseFloat(BMICalculator.this.et_height.getText().toString());
                    BMICalculator.this.age = Integer.parseInt(BMICalculator.this.et_age.getText().toString());
                    StringBuilder sb = new StringBuilder();
                    sb.append("inserted_weight");
                    sb.append(BMICalculator.this.inserted_weight);
                    Log.d("inserted_weight", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("inserted_height");
                    sb2.append(BMICalculator.this.inserted_height);
                    Log.d("inserted_height", sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("age");
                    sb3.append(BMICalculator.this.age);
                    Log.d("age", sb3.toString());
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("random_number==>");
                    sb4.append(random);
                    printStream.println(sb4.toString());
                    if (random == 2) {
                        BMICalculator.this.showIntertitial();
                    } else {
                        BMICalculator.this.calculate();
                    }
                }
            }
        });
    }

    private OnClickListener showPopupWindowHeight() {
        return new OnClickListener() {
            public void onClick(View view) {
                BMICalculator.this.popupWindowHeight().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowHeight() {
        this.popupWindowHeight = new PopupWindow(getContext());
        this.listViewHeight = new ListView(getContext());
        this.listViewHeight.setDividerHeight(0);
        this.listViewHeight.setAdapter(this.adapter_height);
        this.listViewHeight.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                BMICalculator.this.tv_heightunit.setText((CharSequence) BMICalculator.this.arraylist_height.get(i));
                BMICalculator.this.dismissPopupHeight();
            }
        });
        this.popupWindowHeight.setFocusable(true);
        this.popupWindowHeight.setWidth(this.tv_heightunit.getMeasuredWidth());
        this.popupWindowHeight.setHeight(-2);
        this.popupWindowHeight.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowHeight.setContentView(this.listViewHeight);
        return this.popupWindowHeight;
    }


    public void dismissPopupHeight() {
        if (this.popupWindowHeight != null) {
            this.popupWindowHeight.dismiss();
        }
    }

    private OnClickListener showPopupWindow_Weight() {
        return new OnClickListener() {
            public void onClick(View view) {
                BMICalculator.this.popupWindowWeight().showAsDropDown(view, 0, 0);
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
                StringBuilder sb = new StringBuilder();
                sb.append("position->");
                sb.append(i);
                Log.d("position", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("arraylist_weigth->");
                sb2.append((String) BMICalculator.this.arraylist_weigth.get(i));
                Log.d("arraylist_weigth", sb2.toString());
                BMICalculator.this.tv_weightunit.setText((CharSequence) BMICalculator.this.arraylist_weigth.get(i));
                BMICalculator.this.dismissPopupTopics();
            }
        });
        this.popupWindowWeight.setFocusable(true);
        this.popupWindowWeight.setWidth(this.tv_weightunit.getMeasuredWidth());
        this.popupWindowWeight.setHeight(-2);
        this.popupWindowWeight.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowWeight.setContentView(this.listViewWeight);
        return this.popupWindowWeight;
    }


    public void dismissPopupTopics() {
        if (this.popupWindowWeight != null) {
            this.popupWindowWeight.dismiss();
        }
    }


    public void calculate() {
        StringBuilder sb = new StringBuilder();
        sb.append("inserted_weight");
        sb.append(this.inserted_weight);
        Log.d("inserted_weight", sb.toString());
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
        if (this.height_unit.equalsIgnoreCase(getString(R.string.feet))) {
            this.bmi_height = this.inserted_height;
        } else {
            this.bmi_height = this.inserted_height * 0.032808f;
        }
        if (this.weight_unit.equalsIgnoreCase(getString(R.string.lbs))) {
            this.bmi_weight = this.inserted_weight;
        } else {
            this.bmi_weight = this.inserted_weight * 2.2046f;
        }
        this.bmi = calculateBMI(this.bmi_weight, this.bmi_height);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("bmi value");
        sb5.append(this.bmi);
        Log.d("bmi value", sb5.toString());
//        Intent intent = new Intent(this, BMI_Result.class);
//        intent.putExtra("age", this.age);
//        intent.putExtra("bmi", this.bmi);
//        startActivity(intent);
        result(age,bmi);
    }

    private void result(int age, float bmi) {
        String result="";
        String recomended="";

        if (age < 17) {
            if (bmi > 21.0f && bmi <= 26.0f) {
                result = getString(R.string.Overweight);
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.Recommended_BMI_Value));
                sb.append(" : 15-20");
                recomended=(sb.toString());
            } else if (bmi > 26.0f && bmi <= 34.0f) {
                result = getString(R.string.Obese);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getString(R.string.Recommended_BMI_Value));
                sb2.append(" : 15-20");
                recomended=(sb2.toString());
            } else if (bmi > 34.0f) {
                result = getString(R.string.Extremely_Obese);
                StringBuilder sb3 = new StringBuilder();
                sb3.append(getString(R.string.Recommended_BMI_Value));
                sb3.append(" : 15-20");
                recomended=(sb3.toString());
            } else if (bmi < 15.0f || bmi > 20.0f) {
                result = getString(R.string.Under_Weight);
                StringBuilder sb4 = new StringBuilder();
                sb4.append(getString(R.string.Recommended_BMI_Value));
                sb4.append(" : 15-20");
                recomended=(sb4.toString());
            } else {
                result = getString(R.string.ok);
                StringBuilder sb5 = new StringBuilder();
                sb5.append(getString(R.string.Recommended_BMI_Value));
                sb5.append(" : 15-20");
                recomended=(sb5.toString());
            }
        } else if (age < 17 || age >= 35) {
            if (age >= 35) {
                if (bmi > 27.0f && bmi <= 30.0f) {
                    result = getString(R.string.Overweight);
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(getString(R.string.Recommended_BMI_Value));
                    sb6.append(" : 19-26");
                    recomended=(sb6.toString());
                } else if (bmi > 30.0f && bmi <= 40.0f) {
                    result = getString(R.string.Obese);
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(getString(R.string.Recommended_BMI_Value));
                    sb7.append(" : 19-26");
                    recomended=(sb7.toString());
                } else if (bmi > 40.0f) {
                    result = getString(R.string.Extremely_Obese);
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(getString(R.string.Recommended_BMI_Value));
                    sb8.append(" : 19-26");
                    recomended=(sb8.toString());
                } else if (bmi < 19.0f || bmi > 26.0f) {
                    result = getString(R.string.Under_Weight);
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(getString(R.string.Recommended_BMI_Value));
                    sb9.append(" : 19-26");
                    recomended=(sb9.toString());
                } else {
                    result = getString(R.string.ok);
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(getString(R.string.Recommended_BMI_Value));
                    sb10.append(" : 19-26");
                    recomended=(sb10.toString());
                }
            }
        } else if (bmi > 25.0f && bmi <= 30.0f) {
            result = getString(R.string.Overweight);
            StringBuilder sb11 = new StringBuilder();
            sb11.append(getString(R.string.Recommended_BMI_Value));
            sb11.append(" : 18-24");
            recomended=(sb11.toString());
        } else if (bmi > 30.0f && bmi <= 40.0f) {
            result = getString(R.string.Obese);
            StringBuilder sb12 = new StringBuilder();
            sb12.append(getString(R.string.Recommended_BMI_Value));
            sb12.append(": 18-24");
            recomended=(sb12.toString());
        } else if (bmi > 40.0f) {
            result = getString(R.string.Extremely_Obese);
            StringBuilder sb13 = new StringBuilder();
            sb13.append(getString(R.string.Recommended_BMI_Value));
            sb13.append(" : 18-24");
            recomended=(sb13.toString());
        } else if (bmi < 18.0f || bmi > 24.0f) {
            result = getString(R.string.Under_Weight);
            StringBuilder sb14 = new StringBuilder();
            sb14.append(getString(R.string.Recommended_BMI_Value));
            sb14.append(" : 18-24");
            recomended=(sb14.toString());
        } else {
            result = getString(R.string.ok);
            StringBuilder sb15 = new StringBuilder();
            sb15.append(getString(R.string.Recommended_BMI_Value));
            sb15.append(" : 18-24");
            recomended=(sb15.toString());
        }
        StringBuilder sb16 = new StringBuilder();
        sb16.append(getString(R.string.Your_BMI_is));
        sb16.append(" : ");
        sb16.append(String.format("%.02f", new Object[]{this.bmi}));

        StringBuilder sb17 = new StringBuilder();
        sb17.append(sb16.toString());
        sb17.append("\n");
        sb17.append(result);
        sb17.append("\n");
        sb17.append(recomended);
        new AlertDialog.Builder(getActivity())
                .setTitle("BMI")
                .setMessage(sb17.toString())
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private float calculateBMI(float f, float f2) {
        double d = (double) f;
        Double.isNaN(d);
        double d2 = d * 4.88d;
        double d3 = (double) (f2 * f2);
        Double.isNaN(d3);
        return (float) (d2 / d3);
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
                    BMICalculator.this.calculate();
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
}
