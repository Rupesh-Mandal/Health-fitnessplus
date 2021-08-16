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

public class BodyfatCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_height;
    ArrayAdapter<String> adapter_weight;
    int age;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_height = new ArrayList<>();
    ArrayList<String> arraylist_weigth = new ArrayList<>();
    float bmi;
    float bmi_height;
    float bmi_weight;
    Double bodyfat;
    EditText et_age;
    EditText et_height;
    EditText et_weight;
    String gender;
    GlobalFunction globalFunction;
    String height_unit;
    float inserted_height;
    float inserted_weight;
    ImageView iv_back;
    ListView listViewHeight;
    ListView listViewWeight;
    ListView listViewgender;
    private PopupWindow popupWindowHeight;
    private PopupWindow popupWindowWeight;
    private PopupWindow popupWindowgender;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_bodyfat;
    TextView tv_gender;
    TextView tv_heightunit;
    TextView tv_search_bodyfat;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    String weight_unit;

    public BodyfatCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_bodyfat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.globalFunction = new GlobalFunction(getActivity());
        this.globalFunction.set_locale_language();
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.et_height = (EditText) view.findViewById(R.id.et_height);
        this.et_weight = (EditText) view.findViewById(R.id.et_weight);
        this.et_age = (EditText) view.findViewById(R.id.et_age);
        this.tv_bodyfat = (TextView) view.findViewById(R.id.tv_bodyfat);
        this.tv_heightunit = (TextView) view.findViewById(R.id.tv_heightunit);
        this.tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        this.tv_search_bodyfat = (TextView) view.findViewById(R.id.tv_search_bodyfat);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.tv_bodyfat.setTypeface(this.typefaceManager.getBold());
        this.et_height.setTypeface(this.typefaceManager.getLight());
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.et_age.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_search_bodyfat.setTypeface(this.typefaceManager.getLight());
        this.tv_gender.setTypeface(this.typefaceManager.getLight());
        this.height_unit = getString(R.string.feet);
        this.weight_unit = getString(R.string.lbs);
        this.gender = getString(R.string.Male);
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BodyfatCalculator.this.onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.tv_heightunit.setOnClickListener(showPopupWindowHeight());
        this.tv_weightunit.setOnClickListener(showPopupWindow_Weight());
        this.tv_gender.setOnClickListener(showPopupWindow_gender());
        this.arraylist_height.clear();
        this.arraylist_height.add(getString(R.string.feet));
        this.arraylist_height.add(getString(R.string.cm));
        this.arraylist_weigth.clear();
        this.arraylist_weigth.add(getString(R.string.kg));
        this.arraylist_weigth.add(getString(R.string.lbs));
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_height = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_height);
        this.adapter_weight = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_weigth);
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.tv_search_bodyfat.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (BodyfatCalculator.this.et_age.getText().toString().trim().equals("") || BodyfatCalculator.this.et_age.getText().toString().trim().equals(".")) {
                    BodyfatCalculator.this.et_age.setError(BodyfatCalculator.this.getString(R.string.Enter_Age));
                } else if (BodyfatCalculator.this.et_height.getText().toString().trim().equals("") || BodyfatCalculator.this.et_height.getText().toString().trim().equals(".")) {
                    BodyfatCalculator.this.et_height.setError(BodyfatCalculator.this.getString(R.string.Enter_Height));
                } else if (BodyfatCalculator.this.et_weight.getText().toString().trim().equals("") || BodyfatCalculator.this.et_weight.getText().toString().trim().equals(".")) {
                    BodyfatCalculator.this.et_weight.setError(BodyfatCalculator.this.getString(R.string.Enter_Weight));
                } else {
                    BodyfatCalculator.this.height_unit = BodyfatCalculator.this.tv_heightunit.getText().toString();
                    BodyfatCalculator.this.weight_unit = BodyfatCalculator.this.tv_weightunit.getText().toString();
                    BodyfatCalculator.this.gender = BodyfatCalculator.this.tv_gender.getText().toString();
                    BodyfatCalculator.this.inserted_weight = Float.parseFloat(BodyfatCalculator.this.et_weight.getText().toString());
                    BodyfatCalculator.this.inserted_height = Float.parseFloat(BodyfatCalculator.this.et_height.getText().toString());
                    BodyfatCalculator.this.age = Integer.parseInt(BodyfatCalculator.this.et_age.getText().toString());
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb = new StringBuilder();
                    sb.append("random_number==>");
                    sb.append(random);
                    printStream.println(sb.toString());
                    if (random == 2) {
                        BodyfatCalculator.this.showIntertitial();
                    } else {
                        BodyfatCalculator.this.calculate();
                    }
                }
            }
        });
    }

    private OnClickListener showPopupWindowHeight() {
        return new OnClickListener() {
            public void onClick(View view) {
                BodyfatCalculator.this.popupWindowHeight().showAsDropDown(view, 0, 0);
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
                sb2.append((String) BodyfatCalculator.this.arraylist_height.get(i));
                Log.d("arraylist_height", sb2.toString());
                BodyfatCalculator.this.tv_heightunit.setText((CharSequence) BodyfatCalculator.this.arraylist_height.get(i));
                BodyfatCalculator.this.dismissPopupHeight();
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
                BodyfatCalculator.this.popupWindowWeight().showAsDropDown(view, 0, 0);
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
                sb2.append((String) BodyfatCalculator.this.arraylist_weigth.get(i));
                Log.d("arraylist_weigth", sb2.toString());
                BodyfatCalculator.this.tv_weightunit.setText((CharSequence) BodyfatCalculator.this.arraylist_weigth.get(i));
                BodyfatCalculator.this.dismissPopupTopics();
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
        StringBuilder sb5 = new StringBuilder();
        sb5.append("bmi_weight");
        sb5.append(this.bmi_weight);
        Log.d("bmi_weight", sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        sb6.append("bmi_height");
        sb6.append(this.bmi_height);
        Log.d("bmi_height", sb6.toString());
        this.bmi = calculateBMI(this.bmi_weight, this.bmi_height);
        StringBuilder sb7 = new StringBuilder();
        sb7.append("bmi value");
        sb7.append(this.bmi);
        Log.d("bmi value", sb7.toString());
        calculate_bodyfat();
    }

    private float calculateBMI(float f, float f2) {
        double d = (double) f;
        Double.isNaN(d);
        double d2 = d * 4.88d;
        double d3 = (double) (f2 * f2);
        Double.isNaN(d3);
        return (float) (d2 / d3);
    }

    public void calculate_bodyfat() {
        StringBuilder sb = new StringBuilder();
        sb.append("bmiiiii");
        sb.append(this.bmi);
        Log.d("bmiiiii", sb.toString());
        if (this.age < 15) {
            if (this.gender.equalsIgnoreCase(getString(R.string.Female))) {
                double d = (double) this.bmi;
                Double.isNaN(d);
                double d2 = d * 1.51d;
                double d3 = (double) this.age;
                Double.isNaN(d3);
                this.bodyfat = Double.valueOf(((d2 + (d3 * 0.7d)) - 0.0d) + 1.4d);
            } else if (this.gender.equalsIgnoreCase(getString(R.string.Male))) {
                double d4 = (double) this.bmi;
                Double.isNaN(d4);
                double d5 = d4 * 1.51d;
                double d6 = (double) this.age;
                Double.isNaN(d6);
                this.bodyfat = Double.valueOf(((d5 + (d6 * 0.7d)) - 3.6d) + 1.4d);
            }
        } else if (this.age > 15) {
            if (this.gender.equalsIgnoreCase(getString(R.string.Female))) {
                double d7 = (double) this.bmi;
                Double.isNaN(d7);
                double d8 = d7 * 1.2d;
                double d9 = (double) this.age;
                Double.isNaN(d9);
                this.bodyfat = Double.valueOf(((d8 + (d9 * 0.23d)) - 0.0d) - 5.4d);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("bodyyyy fat");
                sb2.append(this.bodyfat);
                Log.d("bodyyyy fat", sb2.toString());
            } else if (this.gender.equalsIgnoreCase(getString(R.string.Male))) {
                double d10 = (double) this.bmi;
                Double.isNaN(d10);
                double d11 = d10 * 1.2d;
                double d12 = (double) this.age;
                Double.isNaN(d12);
                this.bodyfat = Double.valueOf(((d11 + (d12 * 0.23d)) - 10.8d) - 5.4d);
            }
        }
//        Intent intent = new Intent(this, Bodyfat_Result.class);
//        intent.putExtra("age", this.age);
//        intent.putExtra("gender", this.gender);
//        intent.putExtra("bodyfat", this.bodyfat);
//        startActivity(intent);
        result(age,gender,bodyfat);
    }

    private void result(int age, String gender, Double bodyfat) {
        String result="";
        String recomended="";

        if (this.gender.equalsIgnoreCase(getString(R.string.Male))) {
            if (bodyfat < 10.0d) {
                result = getString(R.string.Very_Low);
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.Recommended_Bodyfat));
                sb.append(" :13-17");
                recomended=sb.toString();
            } else if (bodyfat >= 10.0d && this.bodyfat < 13.0d) {
                result = getString(R.string.Low);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getString(R.string.Recommended_Bodyfat));
                sb2.append(" :13-17");
                recomended=sb2.toString();
            } else if (bodyfat >= 13.0d && this.bodyfat < 17.0d) {
                result = getString(R.string.Average);
                StringBuilder sb3 = new StringBuilder();
                sb3.append(getString(R.string.Recommended_Bodyfat));
                sb3.append(" :13-17");
                recomended=sb3.toString();
            } else if (bodyfat >= 17.0d && this.bodyfat < 25.0d) {
                result = getString(R.string.High);
                StringBuilder sb4 = new StringBuilder();
                sb4.append(getString(R.string.Recommended_Bodyfat));
                sb4.append(" :13-17");
                recomended=sb4.toString();
            } else if (bodyfat >= 25.0d) {
                result = getString(R.string.Very_High);
                StringBuilder sb5 = new StringBuilder();
                sb5.append(getString(R.string.Recommended_Bodyfat));
                sb5.append(" :13-17");
                recomended=sb5.toString();
            }
        } else if (gender.equals("Female")) {
            if (bodyfat < 17.0d) {
                result = getString(R.string.Very_Low);
                StringBuilder sb6 = new StringBuilder();
                sb6.append(getString(R.string.Recommended_Bodyfat));
                sb6.append(" :20-27");
                recomended=sb6.toString();
            } else if (bodyfat >= 17.0d && this.bodyfat < 20.0d) {
                result = getString(R.string.Low);
                StringBuilder sb7 = new StringBuilder();
                sb7.append(getString(R.string.Recommended_Bodyfat));
                sb7.append(" :20-27");
                recomended=sb7.toString();
            } else if (bodyfat >= 20.0d && this.bodyfat < 27.0d) {
                result = getString(R.string.Average);
                StringBuilder sb8 = new StringBuilder();
                sb8.append(getString(R.string.Recommended_Bodyfat));
                sb8.append(" :20-27");
                recomended=sb8.toString();
            } else if (bodyfat >= 27.0d && this.bodyfat < 31.0d) {
                result = getString(R.string.High);
                StringBuilder sb9 = new StringBuilder();
                sb9.append(getString(R.string.Recommended_Bodyfat));
                sb9.append(" :20-27");
                recomended=sb9.toString();
            } else if (bodyfat >= 31.0d) {
                result = getString(R.string.Very_High);
                StringBuilder sb10 = new StringBuilder();
                sb10.append(getString(R.string.Recommended_Bodyfat));
                sb10.append(" :20-27");
                recomended=sb10.toString();
            }
        }
        StringBuilder sb11 = new StringBuilder();
        sb11.append(getString(R.string.Your_Body_Fat_is));
        sb11.append(" : ");
        sb11.append(String.format("%.02f", new Object[]{Double.valueOf(this.bodyfat)}));

        StringBuilder sb12 = new StringBuilder();
        sb12.append(sb11.toString());
        sb12.append("\n");
        sb12.append(result);
        sb12.append("\n");
        sb12.append(recomended);

        new AlertDialog.Builder(getActivity())
                .setTitle("Body Fat")
                .setMessage(sb12.toString())
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private OnClickListener showPopupWindow_gender() {
        return new OnClickListener() {
            public void onClick(View view) {
                BodyfatCalculator.this.popupWindowgender().showAsDropDown(view, 0, 0);
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
                StringBuilder sb = new StringBuilder();
                sb.append("position->");
                sb.append(i);
                Log.d("arraylist_gender", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("arraylist_gender->");
                sb2.append((String) BodyfatCalculator.this.arraylist_gender.get(i));
                Log.d("arraylist_weigth", sb2.toString());
                BodyfatCalculator.this.tv_gender.setText((CharSequence) BodyfatCalculator.this.arraylist_gender.get(i));
                BodyfatCalculator.this.dismissPopupgender();
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
                    BodyfatCalculator.this.calculate();
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
