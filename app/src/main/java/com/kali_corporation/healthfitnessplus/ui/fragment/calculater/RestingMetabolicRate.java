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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import java.util.ArrayList;

public class RestingMetabolicRate extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_height;
    ArrayAdapter<String> adapter_weight;
    double age;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_height = new ArrayList<>();
    ArrayList<String> arraylist_weigth = new ArrayList<>();
    double bmr;
    double bmr_height;
    double bmr_weight;
    EditText et_age;
    EditText et_height;
    EditText et_weight;
    Bundle extras;
    String gender;
    GlobalFunction globalFunction;
    String height_unit;
    float inserted_height;
    float inserted_weight;
    ImageView iv_back;
    ListView listViewGender;
    ListView listViewHeight;
    ListView listViewWeight;
    private PopupWindow popupWindowGender;
    private PopupWindow popupWindowHeight;
    private PopupWindow popupWindowWeight;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_bmr;
    TextView tv_gender;
    TextView tv_genderunit;
    TextView tv_heightunit;
    TextView tv_search_bmr;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    String weight_unit;

    public RestingMetabolicRate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resting_metabolic_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalFunction = new GlobalFunction(getActivity());
        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        globalFunction.set_locale_language();
        globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        et_height = (EditText) view.findViewById(R.id.et_height);
        et_weight = (EditText) view.findViewById(R.id.et_weight);
        et_age = (EditText) view.findViewById(R.id.et_age);
        tv_bmr = (TextView) view.findViewById(R.id.tv_bmr);
        tv_heightunit = (TextView) view.findViewById(R.id.tv_heightunit);
        tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        tv_search_bmr = (TextView) view.findViewById(R.id.tv_search_bmr);
        tv_genderunit = (TextView) view.findViewById(R.id.tv_genderunit);
        tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_bmr.setTypeface(this.typefaceManager.getBold());
        tv_search_bmr.setTypeface(this.typefaceManager.getBold());
        extras = getActivity().getIntent().getExtras();
        if (this.extras != null) {
            if (this.extras.getString("from", "bmr").equals("bmr")) {
                this.tv_bmr.setText(getString(R.string.bmr_Calculator));
                this.tv_search_bmr.setText(getString(R.string.Calculate_BMR));
//                this.rl_main.setBackgroundResource(R.drawable.background_gradient1);
            } else {
                this.tv_bmr.setText(getString(R.string.rmr_Calculator));
                this.tv_search_bmr.setText(getString(R.string.Calculate_RMR));
//                this.rl_main.setBackgroundResource(R.drawable.background_gradient7);
            }
        }
        et_height.setTypeface(this.typefaceManager.getLight());
        et_weight.setTypeface(this.typefaceManager.getLight());
        et_age.setTypeface(this.typefaceManager.getLight());
        tv_heightunit.setTypeface(this.typefaceManager.getLight());
        tv_weightunit.setTypeface(this.typefaceManager.getLight());
        tv_genderunit.setTypeface(this.typefaceManager.getLight());
        tv_gender.setTypeface(this.typefaceManager.getLight());
        height_unit = getString(R.string.feet);
        weight_unit = getString(R.string.lbs);
        tv_heightunit.setOnClickListener(showPopupWindowHeight());
        tv_weightunit.setOnClickListener(showPopupWindow_Weight());
        tv_gender.setOnClickListener(showPopupWindowGender());
        arraylist_gender.clear();
        arraylist_gender.add(getString(R.string.Male));
        arraylist_gender.add(getString(R.string.Female));
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        arraylist_height.clear();
        arraylist_height.add(getString(R.string.feet));
        arraylist_height.add(getString(R.string.cm));
        arraylist_weigth.clear();
        arraylist_weigth.add(getString(R.string.kg));
        arraylist_weigth.add(getString(R.string.lbs));
        adapter_height = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_height);
        adapter_weight = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_weigth);
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.tv_search_bmr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (et_age.getText().toString().trim().equals("")) {
                    et_age.setError(getString(R.string.Enter_Age));
                } else if (et_height.getText().toString().trim().equals("") || et_height.getText().toString().trim().equals(".")) {
                    et_height.setError(getString(R.string.Enter_Height));
                } else if (et_weight.getText().toString().trim().equals("") || et_weight.getText().toString().trim().equals(".")) {
                    et_weight.setError(getString(R.string.Enter_Weight));
                } else {
                    height_unit = tv_heightunit.getText().toString();
                    weight_unit = tv_weightunit.getText().toString();
                    inserted_weight = Float.parseFloat(et_weight.getText().toString());
                    inserted_height = Float.parseFloat(et_height.getText().toString());
                    age = (double) Integer.parseInt(et_age.getText().toString());
                    gender = tv_gender.getText().toString().trim();
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb = new StringBuilder();
                    sb.append("random_number==>");
                    sb.append(random);
                    printStream.println(sb.toString());
                    if (random == 2) {
                        showIntertitial();
                    } else {
                        calculate();
                    }
                }
            }
        });
        this.iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
        if (this.height_unit.equalsIgnoreCase(getString(R.string.cm))) {
            this.bmr_height = (double) this.inserted_height;
        } else {
            this.bmr_height = (double) (this.inserted_height / 0.032808f);
        }
        if (this.weight_unit.equalsIgnoreCase(getString(R.string.kg))) {
            this.bmr_weight = (double) this.inserted_weight;
        } else {
            this.bmr_weight = (double) (this.inserted_weight / 2.2046f);
        }
        if (this.gender.equalsIgnoreCase(getString(R.string.Male))) {
            this.bmr = (((this.bmr_weight * 10.0d) + (this.bmr_height * 6.25d)) - (this.age * 5.0d)) + 5.0d;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("bmr_male->");
            sb5.append(this.bmr);
            Log.d("bmr_male->", sb5.toString());
        } else {
            this.bmr = (((this.bmr_weight * 10.0d) + (this.bmr_height * 6.25d)) - (this.age * 5.0d)) - 161.0d;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("bmr_female->");
            sb6.append(this.bmr);
            Log.d("bmr_female->", sb6.toString());
        }
        result(bmr);
//        Intent intent = new Intent(this, BMR_Result.class);
//        intent.putExtra("bmr", this.bmr);
//        intent.putExtra("from", this.extras.getString("from", "bmr"));
//        startActivity(intent);
    }

    private void result(double bmr2) {
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getString(R.string.Your_BMR_is));
        sb3.append(" :  \n");
        sb3.append(String.format("%.0f", new Object[]{Double.valueOf(bmr2)}));
        sb2.append(String.valueOf(sb3.toString()));
        sb2.append(" ");
        sb2.append(getString(R.string.cal_per_day));
        new AlertDialog.Builder(getActivity())
                .setTitle("Your Metabolic Rate is")
                .setMessage(String.format("%.0f", new Object[]{Double.valueOf(bmr2)})+" "+ getString(R.string.cal_per_day))
                .setCancelable(false)

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private View.OnClickListener showPopupWindowHeight() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                popupWindowHeight().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowHeight() {
        this.popupWindowHeight = new PopupWindow(getContext());
        this.listViewHeight = new ListView(getContext());
        this.listViewHeight.setDividerHeight(0);
        this.listViewHeight.setAdapter(this.adapter_height);
        this.listViewHeight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                tv_heightunit.setText((CharSequence) arraylist_height.get(i));
                dismissPopupHeight();
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

    private View.OnClickListener showPopupWindow_Weight() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                popupWindowWeight().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowWeight() {
        this.popupWindowWeight = new PopupWindow(getContext());
        this.listViewWeight = new ListView(getContext());
        this.listViewWeight.setDividerHeight(0);
        this.listViewWeight.setAdapter(this.adapter_weight);
        this.listViewWeight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                StringBuilder sb = new StringBuilder();
                sb.append("position->");
                sb.append(i);
                Log.d("position", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("arraylist_weigth->");
                sb2.append((String) arraylist_weigth.get(i));
                Log.d("arraylist_weigth", sb2.toString());
                tv_weightunit.setText((CharSequence) arraylist_weigth.get(i));
                dismissPopupTopics();
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


    public PopupWindow popupWindowGender() {
        this.popupWindowGender = new PopupWindow(getContext());
        this.listViewGender = new ListView(getContext());
        this.listViewGender.setDividerHeight(0);
        this.listViewGender.setAdapter(this.adapter_gender);
        this.listViewGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                tv_gender.setText((CharSequence) arraylist_gender.get(i));
                tv_genderunit.setText((CharSequence) arraylist_gender.get(i));
                dismissPopupGender();
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

    private View.OnClickListener showPopupWindowGender() {
        return new View.OnClickListener() {
            public void onClick(View view) {
                popupWindowGender().showAsDropDown(view, 0, 0);
            }
        };
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
                        MyApplication.interstitial.loadAd(new AdRequest.Builder().build());
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
                    MyApplication.interstitial.loadAd(new AdRequest.Builder().build());
                }
            });
        }
        if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
            MyApplication.interstitial.setAdListener(new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    MyApplication.interstitial.loadAd(new AdRequest.Builder().build());
                    calculate();
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
}