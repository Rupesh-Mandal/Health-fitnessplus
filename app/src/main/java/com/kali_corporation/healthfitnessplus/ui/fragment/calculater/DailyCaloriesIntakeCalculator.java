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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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

public class DailyCaloriesIntakeCalculator extends Fragment {
    Double BMR;
    String TAG = getClass().getSimpleName();
    String activity_level = "";
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_height;
    ArrayAdapter<String> adapter_weight;
    int age;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_height = new ArrayList<>();
    ArrayList<String> arraylist_weigth = new ArrayList<>();
    Float cal_height;
    Float cal_weight;
    EditText et_age;
    EditText et_height;
    EditText et_weight;
    String gender;
    GlobalFunction globalFunction;
    String height_unit;
    Float inserted_height;
    Float inserted_weight;
    ImageView iv_back;
    ListView listViewHeight;
    ListView listViewWeight;
    ListView listViewgender;
    private PopupWindow popupWindowHeight;
    private PopupWindow popupWindowWeight;
    private PopupWindow popupWindowgender;
    RadioGroup rg_activity_level;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_bodyfat;
    TextView tv_gender;
    TextView tv_heightunit;
    TextView tv_search_calories_intake;
    TextView tv_select_level;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    String weight_unit;


    public DailyCaloriesIntakeCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calories_intake_calculator, container, false);
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
        this.rg_activity_level = (RadioGroup) view.findViewById(R.id.rg_activity_level);
        this.et_height = (EditText) view.findViewById(R.id.et_height);
        this.et_weight = (EditText) view.findViewById(R.id.et_weight);
        this.et_age = (EditText) view.findViewById(R.id.et_age);
        this.tv_heightunit = (TextView) view.findViewById(R.id.tv_heightunit);
        this.tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        this.tv_bodyfat = (TextView) view.findViewById(R.id.tv_bodyfat);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.tv_search_calories_intake = (TextView) view.findViewById(R.id.tv_search_calories_intake);
        this.tv_select_level = (TextView) view.findViewById(R.id.tv_select_level);
        this.tv_bodyfat.setTypeface(this.typefaceManager.getBold());
        this.tv_search_calories_intake.setTypeface(this.typefaceManager.getBold());
        this.et_height.setTypeface(this.typefaceManager.getLight());
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.et_age.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_gender.setTypeface(this.typefaceManager.getLight());
        this.tv_select_level.setTypeface(this.typefaceManager.getLight());
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.tv_heightunit.setOnClickListener(showPopupWindowHeight());
        this.tv_weightunit.setOnClickListener(showPopupWindow_Weight());
        this.tv_gender.setOnClickListener(showPopupWindow_gender());
        this.arraylist_height.clear();
        this.arraylist_height.add("Feet");
        this.arraylist_height.add("Cm");
        this.arraylist_weigth.clear();
        this.arraylist_weigth.add("Kg");
        this.arraylist_weigth.add("LBS");
        this.arraylist_gender.clear();
        this.arraylist_gender.add("Male");
        this.arraylist_gender.add("Female");
        this.adapter_height = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_height);
        this.adapter_weight = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_weigth);
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DailyCaloriesIntakeCalculator.this.onBackPressed();
            }
        });
        this.rg_activity_level.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                DailyCaloriesIntakeCalculator.this.activity_level = String.valueOf(radioButton.getText());
            }
        });
        this.tv_search_calories_intake.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (DailyCaloriesIntakeCalculator.this.et_age.getText().toString().trim().equals("")) {
                    DailyCaloriesIntakeCalculator.this.et_age.setError("Enter Age");
                } else if (DailyCaloriesIntakeCalculator.this.et_height.getText().toString().trim().equals("") || DailyCaloriesIntakeCalculator.this.et_height.getText().toString().trim().equals(".")) {
                    DailyCaloriesIntakeCalculator.this.et_height.setError("Enter Height");
                } else if (DailyCaloriesIntakeCalculator.this.et_weight.getText().toString().trim().equals("") || DailyCaloriesIntakeCalculator.this.et_weight.getText().toString().trim().equals(".")) {
                    DailyCaloriesIntakeCalculator.this.et_weight.setError("Enter Weight");
                } else if (DailyCaloriesIntakeCalculator.this.activity_level.equals("")) {
                    Toast.makeText(getContext().getApplicationContext(), "Select Activity Level", 0).show();
                } else {
                    DailyCaloriesIntakeCalculator.this.height_unit = DailyCaloriesIntakeCalculator.this.tv_heightunit.getText().toString().trim();
                    DailyCaloriesIntakeCalculator.this.weight_unit = DailyCaloriesIntakeCalculator.this.tv_weightunit.getText().toString().trim();
                    if (!DailyCaloriesIntakeCalculator.this.et_height.getText().toString().equals(".")) {
                        DailyCaloriesIntakeCalculator.this.inserted_height = Float.valueOf(Float.parseFloat(DailyCaloriesIntakeCalculator.this.et_height.getText().toString().trim()));
                    }
                    if (!DailyCaloriesIntakeCalculator.this.et_weight.getText().toString().equals(".")) {
                        DailyCaloriesIntakeCalculator.this.inserted_weight = Float.valueOf(Float.parseFloat(DailyCaloriesIntakeCalculator.this.et_weight.getText().toString().trim()));
                    }
                    DailyCaloriesIntakeCalculator.this.age = Integer.parseInt(DailyCaloriesIntakeCalculator.this.et_age.getText().toString());
                    DailyCaloriesIntakeCalculator.this.gender = DailyCaloriesIntakeCalculator.this.tv_gender.getText().toString().trim();
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb = new StringBuilder();
                    sb.append("random_number==>");
                    sb.append(random);
                    printStream.println(sb.toString());
                    if (random == 2) {
                        DailyCaloriesIntakeCalculator.this.showIntertitial();
                    } else {
                        DailyCaloriesIntakeCalculator.this.calculate();
                    }
                }
            }
        });
    }

    private OnClickListener showPopupWindowHeight() {
        return new OnClickListener() {
            public void onClick(View view) {
                DailyCaloriesIntakeCalculator.this.popupWindowHeight().showAsDropDown(view, 0, 0);
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
                StringBuilder sb = new StringBuilder();
                sb.append("position->");
                sb.append(i);
                Log.d("position", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("arraylist_height->");
                sb2.append((String) DailyCaloriesIntakeCalculator.this.arraylist_height.get(i));
                Log.d("arraylist_height", sb2.toString());
                DailyCaloriesIntakeCalculator.this.tv_heightunit.setText((CharSequence) DailyCaloriesIntakeCalculator.this.arraylist_height.get(i));
                DailyCaloriesIntakeCalculator.this.dismissPopupHeight();
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
                DailyCaloriesIntakeCalculator.this.popupWindowWeight().showAsDropDown(view, 0, 0);
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
                DailyCaloriesIntakeCalculator.this.tv_weightunit.setText((CharSequence) DailyCaloriesIntakeCalculator.this.arraylist_weigth.get(i));
                DailyCaloriesIntakeCalculator.this.dismissPopupTopics();
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

    private OnClickListener showPopupWindow_gender() {
        return new OnClickListener() {
            public void onClick(View view) {
                DailyCaloriesIntakeCalculator.this.popupWindowgender().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowgender() {
        this.popupWindowgender = new PopupWindow(getContext());
        this.listViewgender = new ListView(getContext());
        this.listViewgender.setDividerHeight(0);
        this.listViewgender.setAdapter(this.adapter_gender);
        this.listViewgender.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DailyCaloriesIntakeCalculator.this.tv_gender.setText((CharSequence) DailyCaloriesIntakeCalculator.this.arraylist_gender.get(i));
                DailyCaloriesIntakeCalculator.this.dismissPopupgender();
            }
        });
        this.popupWindowgender.setFocusable(true);
        this.popupWindowgender.setWidth(this.tv_gender.getMeasuredWidth());
        this.popupWindowgender.setHeight(-2);
        this.popupWindowgender.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowgender.setContentView(this.listViewgender);
        return this.popupWindowgender;
    }


    public void dismissPopupgender() {
        if (this.popupWindowgender != null) {
            this.popupWindowgender.dismiss();
        }
    }

    public void calculate() {
        try {
            if (this.height_unit.equals("Feet")) {
                this.cal_height = Float.valueOf(this.inserted_height.floatValue() * 12.0f);
            } else {
                this.cal_height = Float.valueOf(this.inserted_height.floatValue() * 0.032808f);
                this.cal_height = Float.valueOf(this.cal_height.floatValue() * 12.0f);
            }
            if (this.weight_unit.equals("LBS")) {
                this.cal_weight = this.inserted_weight;
            } else {
                this.cal_weight = Float.valueOf(this.inserted_weight.floatValue() * 2.2046f);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("cal_weight");
            sb.append(this.cal_weight);
            Log.d("cal_weight", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("cal_height");
            sb2.append(this.cal_height);
            Log.d("cal_height", sb2.toString());
            if (this.gender.equals("Female")) {
                double floatValue = (double) this.cal_weight.floatValue();
                Double.isNaN(floatValue);
                double d = (floatValue * 4.35d) + 655.0d;
                double floatValue2 = (double) this.cal_height.floatValue();
                Double.isNaN(floatValue2);
                double d2 = d + (floatValue2 * 4.7d);
                double d3 = (double) this.age;
                Double.isNaN(d3);
                this.BMR = Double.valueOf(d2 - (d3 * 4.7d));
            } else {
                double floatValue3 = (double) this.cal_weight.floatValue();
                Double.isNaN(floatValue3);
                double d4 = (floatValue3 * 6.23d) + 66.0d;
                double floatValue4 = (double) this.cal_height.floatValue();
                Double.isNaN(floatValue4);
                double d5 = d4 + (floatValue4 * 12.7d);
                double d6 = (double) this.age;
                Double.isNaN(d6);
                this.BMR = Double.valueOf(d5 - (d6 * 6.8d));
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("BMR");
            sb3.append(this.BMR);
            Log.d("BMR", sb3.toString());
            int intValue = this.BMR.intValue();
//            Intent intent = new Intent(this, Daily_Calories_Intake_Result.class);
//            intent.putExtra("BMR", intValue);
//            intent.putExtra("ActivityLevel", this.activity_level);
//            startActivity(intent);
            result(intValue,activity_level);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void result(int BMR, String activity_level) {
        StringBuilder sb = new StringBuilder();
        sb.append("BMIII");
        sb.append(BMR);
        Log.d("BMIII", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getString(R.string.Your_BMR_value_is));
        sb2.append(" \n");
        sb2.append(String.valueOf(BMR));
        sb2.append(" ");
        sb2.append(getString(R.string.calories));
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getString(R.string.This_means_that_your_body_will_burn));
        sb3.append(" ");
        sb3.append(String.valueOf(BMR));
        sb3.append(" ");
        sb3.append(getString(R.string.calories_each_day_if_you_engage_in_no_activity_for_the_entire_day));

        StringBuilder sb4 = new StringBuilder();
        sb4.append(sb2.toString());
        sb4.append("\n");
        sb4.append(sb3.toString());

        new AlertDialog.Builder(getActivity())
                .setTitle("Daily Calories")
                .setMessage(sb4.toString())
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
                    DailyCaloriesIntakeCalculator.this.calculate();
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

    public void onBackPressed() {
        this.adView.setVisibility(View.GONE);
        ActivityCompat.finishAfterTransition(getActivity());
    }
}
