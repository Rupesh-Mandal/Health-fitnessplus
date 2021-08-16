package com.kali_corporation.healthfitnessplus.ui.fragment.calculater;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.kali_corporation.healthfitnessplus.R;

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
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
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


public class AlcoholCalculator extends Fragment {
    double BACinPer;
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_time;
    ArrayAdapter<String> adapter_weight;
    double alcohol_level;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_time = new ArrayList<>();
    ArrayList<String> arraylist_weight = new ArrayList<>();
    EditText et_alcohol_level;
    EditText et_drinkvolume;
    EditText et_timepassed;
    EditText et_weight;
    String gender;
    double gender_ratio;
    GlobalFunction globalFunction;
    ImageView iv_back;
    ListView listViewGender;
    ListView listViewHeight;
    ListView listViewWeight;
    private PopupWindow popupWindowGender;
    private PopupWindow popupWindowTime;
    private PopupWindow popupWindowWeight;
    SharedPreferenceManager sharedPreferenceManager;
    double timePassed;
    String timePassed_unit;
    double total_alcohol;
    TextView tv_alcohol;
    TextView tv_gender;
    TextView tv_genderunit;
    TextView tv_search_bloodalcohol_content;
    TextView tv_timeunit;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    double volume;
    double weight;
    String weight_unit;
    TextView tv_alcohol_result;
    public AlcoholCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alcohol_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.globalFunction = new GlobalFunction(getActivity());
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.et_drinkvolume = (EditText) view.findViewById(R.id.et_drinkvolume);
        this.et_alcohol_level = (EditText) view.findViewById(R.id.et_alcohol_level);
        this.et_timepassed = (EditText) view.findViewById(R.id.et_timepassed);
        this.et_weight = (EditText) view.findViewById(R.id.et_weight);
        this.tv_search_bloodalcohol_content = (TextView) view.findViewById(R.id.tv_search_bloodalcohol_content);
        this.tv_timeunit = (TextView) view.findViewById(R.id.tv_timeunit);
        this.tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.tv_genderunit = (TextView) view.findViewById(R.id.tv_genderunit);
        this.tv_alcohol = (TextView) view.findViewById(R.id.tv_alcohol);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        tv_alcohol_result=view.findViewById(R.id.tv_alcohol_result);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.tv_alcohol.setTypeface(this.typefaceManager.getBold());
        this.tv_search_bloodalcohol_content.setTypeface(this.typefaceManager.getBold());
        this.et_drinkvolume.setTypeface(this.typefaceManager.getLight());
        this.et_alcohol_level.setTypeface(this.typefaceManager.getLight());
        this.et_timepassed.setTypeface(this.typefaceManager.getLight());
        this.tv_timeunit.setTypeface(this.typefaceManager.getLight());
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_genderunit.setTypeface(this.typefaceManager.getLight());
        this.tv_gender.setTypeface(this.typefaceManager.getLight());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setFlags(67108864, 67108864);
        }
        this.iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.arraylist_time.clear();
        this.arraylist_time.add(getString(R.string.hour));
        this.arraylist_time.add(getString(R.string.Minute));
        this.arraylist_time.add(getString(R.string.Day));
        this.adapter_time = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_time);
        this.arraylist_weight.clear();
        this.arraylist_weight.add(getString(R.string.lbs));
        this.arraylist_weight.add(getString(R.string.kg));
        this.adapter_weight = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_weight);
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.tv_weightunit.setOnClickListener(showPopupWindowWeight());
        this.tv_timeunit.setOnClickListener(showPopupWindowTime());
        this.tv_genderunit.setOnClickListener(showPopupWindowGender());
        this.tv_search_bloodalcohol_content.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (et_drinkvolume.getText().toString().trim().equals("") || et_drinkvolume.getText().toString().trim().equals(".")) {
                    Toast.makeText(getContext().getApplicationContext(), getString(R.string.Enter_Drink_volume), Toast.LENGTH_SHORT).show();
                } else if (et_alcohol_level.getText().toString().trim().equals("") || et_alcohol_level.getText().toString().trim().equals(".")) {
                    Toast.makeText(getContext().getApplicationContext(), getString(R.string.Enter_Alcohol_level), Toast.LENGTH_SHORT).show();
                } else if (et_timepassed.getText().toString().trim().equals("") || et_timepassed.getText().toString().trim().equals(".")) {
                    Toast.makeText(getContext().getApplicationContext(), getString(R.string.Enter_time_passed_sence_drinking), Toast.LENGTH_SHORT).show();
                } else if (et_weight.getText().toString().trim().equals("") || et_weight.getText().toString().trim().equals(".")) {
                    Toast.makeText(getContext().getApplicationContext(), getString(R.string.Enter_weight), Toast.LENGTH_SHORT).show();
                } else {
                    volume = Double.parseDouble(et_drinkvolume.getText().toString().trim());
                    volume *= 0.033814d;
                    alcohol_level = Double.parseDouble(et_alcohol_level.getText().toString().trim());
                    total_alcohol = (volume * alcohol_level) / 100.0d;
                    get_genderratio();
                    get_weight();
                    get_time_passed();
                    BACinPer = (((total_alcohol * 5.14d) / weight) * gender_ratio) - (timePassed * 0.015d);
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb = new StringBuilder();
                    sb.append("random_number==>");
                    sb.append(random);
                    printStream.println(sb.toString());
                    if (random == 2) {
                        showIntertitial();
                        return;
                    }
//                    Intent intent = new Intent(getContext(), Alcohol_Result.class);
//                    intent.putExtra("BACinPer", BACinPer);
//                    startActivity(intent);
                    result(BACinPer);
                }
            }
        });
    }

    private void result(double baCinPer) {
        if (baCinPer < 0.0d) {
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.BAC_level));
            sb.append(" : 0");
            tv_alcohol_result.setText(sb.toString());
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getString(R.string.BAC_level));
            sb2.append(" : %.2f");
            tv_alcohol_result.setText(String.format(sb2.toString(), new Object[]{this.BACinPer}));
        }
    }

    private View.OnClickListener showPopupWindowTime() {
            return new View.OnClickListener() {
                public void onClick(View view) {
                    popupWindowTime().showAsDropDown(view, 0, 0);
                }
            };
        }

        private View.OnClickListener showPopupWindowWeight() {
            return new View.OnClickListener() {
                public void onClick(View view) {
                    popupWindowWeight().showAsDropDown(view, 0, 0);
                }
            };
        }

        private View.OnClickListener showPopupWindowGender() {
            return new View.OnClickListener() {
                public void onClick(View view) {
                    popupWindowGender().showAsDropDown(view, 0, 0);
                }
            };
        }


        public PopupWindow popupWindowTime() {
            this.popupWindowTime = new PopupWindow(getContext());
            this.listViewHeight = new ListView(getContext());
            this.listViewHeight.setDividerHeight(0);
            this.listViewHeight.setAdapter(this.adapter_time);
            this.listViewHeight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    tv_timeunit.setText((CharSequence) arraylist_time.get(i));
                    dismissPopupTime();
                }
            });
            this.popupWindowTime.setFocusable(true);
            this.popupWindowTime.setWidth(this.tv_timeunit.getMeasuredWidth());
            this.popupWindowTime.setHeight(-2);
            this.popupWindowTime.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
            this.popupWindowTime.setContentView(this.listViewHeight);
            return this.popupWindowTime;
        }


        public PopupWindow popupWindowWeight() {
            this.popupWindowWeight = new PopupWindow(getContext());
            this.listViewWeight = new ListView(getContext());
            this.listViewWeight.setDividerHeight(0);
            this.listViewWeight.setAdapter(this.adapter_weight);
            this.listViewWeight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    tv_weightunit.setText((CharSequence) arraylist_weight.get(i));
                    dismissPopupWeight();
                }
            });
            this.popupWindowWeight.setFocusable(true);
            this.popupWindowWeight.setWidth(this.tv_weightunit.getMeasuredWidth());
            this.popupWindowWeight.setHeight(-2);
            this.popupWindowWeight.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
            this.popupWindowWeight.setContentView(this.listViewWeight);
            return this.popupWindowWeight;
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
            this.popupWindowGender.setWidth(this.tv_genderunit.getMeasuredWidth());
            this.popupWindowGender.setHeight(-2);
            this.popupWindowGender.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
            this.popupWindowGender.setContentView(this.listViewGender);
            return this.popupWindowGender;
        }


        public void dismissPopupTime() {
            if (this.popupWindowTime != null) {
                this.popupWindowTime.dismiss();
            }
        }


        public void dismissPopupWeight() {
            if (this.popupWindowWeight != null) {
                this.popupWindowWeight.dismiss();
            }
        }


        public void dismissPopupGender() {
            if (this.popupWindowGender != null) {
                this.popupWindowGender.dismiss();
            }
        }

        public void get_genderratio() {
            this.gender = this.tv_gender.getText().toString().trim();
            if (this.gender.equalsIgnoreCase(getString(R.string.Male))) {
                this.gender_ratio = 0.73d;
            } else {
                this.gender_ratio = 0.66d;
            }
        }

        public void get_weight() {
            if (!this.et_weight.getText().toString().trim().equals(".")) {
                this.weight = Double.parseDouble(this.et_weight.getText().toString().trim());
                this.weight_unit = this.tv_weightunit.getText().toString().trim();
                if (this.weight_unit.equalsIgnoreCase(getString(R.string.kg))) {
                    this.weight *= 2.204622d;
                    return;
                }
                return;
            }
            this.weight = 1.0d;
        }

        public void get_time_passed() {
            if (!this.et_timepassed.getText().toString().equals(".")) {
                this.timePassed = Double.parseDouble(this.et_timepassed.getText().toString().trim());
                this.timePassed_unit = this.tv_timeunit.getText().toString().trim();
                if (this.timePassed_unit.equalsIgnoreCase(getString(R.string.Minute))) {
                    this.timePassed /= 60.0d;
                } else if (this.timePassed_unit.equalsIgnoreCase(getString(R.string.day))) {
                    this.timePassed *= 24.0d;
                }
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
//                Intent intent = new Intent(this, Alcohol_Result.class);
//                intent.putExtra("BACinPer", this.BACinPer);
//                startActivity(intent);
                result(BACinPer);
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
//                Intent intent2 = new Intent(this, Alcohol_Result.class);
//                intent2.putExtra("BACinPer", this.BACinPer);
//                startActivity(intent2);
                result(BACinPer);

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
                        MyApplication.interstitial.loadAd(new AdRequest.Builder().build());
                    }
                });
            }
            if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
                MyApplication.interstitial.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        MyApplication.interstitial.loadAd(new AdRequest.Builder().build());
//                        Intent intent = new Intent(getContext(), Alcohol_Result.class);
//                        intent.putExtra("BACinPer", BACinPer);
//                        startActivity(intent);
                        result(BACinPer);

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