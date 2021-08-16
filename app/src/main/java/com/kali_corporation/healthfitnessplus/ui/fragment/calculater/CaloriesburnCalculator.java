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

public class CaloriesburnCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_distance;
    ArrayAdapter<String> adapter_runwalk;
    ArrayAdapter<String> adapter_weight;
    ArrayList<String> arraylist_distance = new ArrayList<>();
    ArrayList<String> arraylist_runwalk = new ArrayList<>();
    ArrayList<String> arraylist_weigth = new ArrayList<>();
    float calories_burn;
    float distance;
    String distanceunit;
    EditText et_distance;
    EditText et_weight;
    GlobalFunction globalFunction;
    ImageView iv_back;
    ListView listViewWeight;
    ListView listViewdistance;
    ListView listViewrunwalk;
    private PopupWindow popupWindowWeight;
    private PopupWindow popupWindowdistance;
    private PopupWindow popupWindowrunwalk;
    String runwalkunit;
    SharedPreferenceManager sharedPreferenceManager;
    String tips;
    float total_calories_burn;
    float total_distance;
    TextView tv_cal_burn;
    TextView tv_distance_unit;
    TextView tv_runwalk;
    TextView tv_runwalk_unit;
    TextView tv_search_burn_calories;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    float weight;
    String weightunit;


    public CaloriesburnCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calories_burn_calculator, container, false);
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
        this.tv_cal_burn = (TextView) view.findViewById(R.id.tv_cal_burn);
        this.et_weight = (EditText) view.findViewById(R.id.et_weight);
        this.et_distance = (EditText) view.findViewById(R.id.et_distance);
        this.tv_runwalk = (TextView) view.findViewById(R.id.tv_runwalk);
        this.tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        this.tv_distance_unit = (TextView) view.findViewById(R.id.tv_distance_unit);
        this.tv_runwalk_unit = (TextView) view.findViewById(R.id.tv_runwalk_unit);
        this.tv_search_burn_calories = (TextView) view.findViewById(R.id.tv_search_burn_calories);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.et_distance.setTypeface(this.typefaceManager.getLight());
        this.tv_runwalk.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_distance_unit.setTypeface(this.typefaceManager.getLight());
        this.tv_runwalk_unit.setTypeface(this.typefaceManager.getLight());
        this.tv_search_burn_calories.setTypeface(this.typefaceManager.getBold());
        this.tv_cal_burn.setTypeface(this.typefaceManager.getBold());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CaloriesburnCalculator.this.onBackPressed();
            }
        });
        this.arraylist_weigth.clear();
        this.arraylist_weigth.add(getString(R.string.kg));
        this.arraylist_weigth.add(getString(R.string.lbs));
        this.adapter_weight = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_weigth);
        this.arraylist_distance.clear();
        this.arraylist_distance.add(getString(R.string.miles));
        this.arraylist_distance.add(getString(R.string.km));
        this.adapter_distance = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_distance);
        this.arraylist_runwalk.clear();
        this.arraylist_runwalk.add(getString(R.string.Running));
        this.arraylist_runwalk.add(getString(R.string.Walking));
        this.adapter_runwalk = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_runwalk);
        this.tv_search_burn_calories.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (CaloriesburnCalculator.this.et_weight.getText().toString().trim().equals("") || CaloriesburnCalculator.this.et_weight.getText().toString().trim().equals(".")) {
                    CaloriesburnCalculator.this.et_weight.setError(CaloriesburnCalculator.this.getString(R.string.Enter_Weight));
                } else if (CaloriesburnCalculator.this.et_distance.getText().toString().trim().equals("")) {
                    CaloriesburnCalculator.this.et_distance.setError(CaloriesburnCalculator.this.getString(R.string.Enter_Distance_You_Run));
                } else {
                    CaloriesburnCalculator.this.weight = Float.parseFloat(CaloriesburnCalculator.this.et_weight.getText().toString());
                    CaloriesburnCalculator.this.distance = Float.parseFloat(CaloriesburnCalculator.this.et_distance.getText().toString());
                    CaloriesburnCalculator.this.weightunit = CaloriesburnCalculator.this.tv_weightunit.getText().toString();
                    CaloriesburnCalculator.this.distanceunit = CaloriesburnCalculator.this.tv_distance_unit.getText().toString();
                    CaloriesburnCalculator.this.runwalkunit = CaloriesburnCalculator.this.tv_runwalk_unit.getText().toString();
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb = new StringBuilder();
                    sb.append("random_number==>");
                    sb.append(random);
                    printStream.println(sb.toString());
                    if (random == 2) {
                        CaloriesburnCalculator.this.showIntertitial();
                    } else {
                        CaloriesburnCalculator.this.calculate();
                    }
                }
            }
        });
        this.tv_weightunit.setOnClickListener(showPopupWindow_Weight());
        this.tv_distance_unit.setOnClickListener(showPopupWindow_distance());
        this.tv_runwalk_unit.setOnClickListener(showPopupWindow_runwalk());
    }


    public void calculate() {
        String str = "inserted_weight";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("inserted_weight");
            sb.append(this.weight);
            Log.d(str, sb.toString());
            if (this.weightunit.equalsIgnoreCase(getString(R.string.lbs))) {
                this.weight = this.weight;
            } else {
                this.weight *= 2.2046f;
            }
            if (this.distanceunit.equalsIgnoreCase(getString(R.string.miles))) {
                this.total_distance = this.distance;
                if (this.runwalkunit.equalsIgnoreCase(getString(R.string.Walking))) {
                    this.calories_burn = this.weight * 0.53f;
                } else if (this.runwalkunit.equalsIgnoreCase(getString(R.string.Running))) {
                    this.calories_burn = this.weight * 0.75f;
                }
                this.total_calories_burn = this.total_distance * this.calories_burn;
            } else {
                this.total_distance = this.distance * 0.62137f;
                if (this.runwalkunit.equalsIgnoreCase(getString(R.string.Walking))) {
                    this.calories_burn = this.weight * 0.53f;
                } else if (this.runwalkunit.equalsIgnoreCase(getString(R.string.Running))) {
                    this.calories_burn = this.weight * 0.75f;
                }
                this.total_calories_burn = this.total_distance * this.calories_burn;
            }
            if (this.total_distance <= 3.0f) {
                this.tips = getString(R.string.You_better_start_working);
            } else if (this.total_distance >= 4.0f && this.total_distance <= 6.0f) {
                this.tips = getString(R.string.Nice_run_but_you_can_do_better);
            } else if (this.total_distance >= 7.0f && this.total_distance <= 10.0f) {
                this.tips = getString(R.string.Very_good_Push_above_next_time);
            } else if (this.total_distance >= 11.0f && this.total_distance <= 20.0f) {
                this.tips = getString(R.string.Great_Your_a_runner_keep_it_up);
            } else if (this.total_distance >= 21.0f && this.total_distance <= 25.0f) {
                this.tips = getString(R.string.Bill_Rogers_move_over);
            } else if (this.total_distance > 25.0f) {
                this.tips = getString(R.string.Your_my_hero_Have_a_jelly_doughnut);
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("tops");
            sb2.append(this.tips);
            Log.d("tops", sb2.toString());
//            Intent intent = new Intent(this, Calories_Burn_Result.class);
//            intent.putExtra("caloriesburn", this.total_calories_burn);
//            intent.putExtra("tips", this.tips);
//            startActivity(intent);
            result(total_calories_burn,tips);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void result(float total_calories_burn, String tips) {
        StringBuilder sb = new StringBuilder();
        sb.append("tips");
        sb.append(this.tips);
        Log.d("tips", sb.toString());
        try {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getString(R.string.Calories_burned));
            sb2.append(" : ");
            sb2.append(String.format("%.02f", new Object[]{Float.valueOf(total_calories_burn)}));

            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2.toString());
            sb3.append("\n");
            sb3.append(String.valueOf(tips));
            sb3.append("\n");
            sb3.append("");
            new AlertDialog.Builder(getActivity())
                    .setTitle("Calories Burn")
                    .setMessage(sb3.toString())
                    .setCancelable(false)

                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

        } catch (Exception unused) {
        }
    }

    private OnClickListener showPopupWindow_Weight() {
        return new OnClickListener() {
            public void onClick(View view) {
                CaloriesburnCalculator.this.popupWindowWeight().showAsDropDown(view, 0, 0);
            }
        };
    }

    private OnClickListener showPopupWindow_distance() {
        return new OnClickListener() {
            public void onClick(View view) {
                CaloriesburnCalculator.this.popupWindowdistance().showAsDropDown(view, 0, 0);
            }
        };
    }

    private OnClickListener showPopupWindow_runwalk() {
        return new OnClickListener() {
            public void onClick(View view) {
                CaloriesburnCalculator.this.popupWindowrunwalk().showAsDropDown(view, 0, 0);
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
                sb2.append((String) CaloriesburnCalculator.this.arraylist_weigth.get(i));
                Log.d("arraylist_weigth", sb2.toString());
                CaloriesburnCalculator.this.tv_weightunit.setText((CharSequence) CaloriesburnCalculator.this.arraylist_weigth.get(i));
                CaloriesburnCalculator.this.dismissPopupTopics();
            }
        });
        this.popupWindowWeight.setFocusable(true);
        this.popupWindowWeight.setWidth(this.tv_weightunit.getMeasuredWidth());
        this.popupWindowWeight.setHeight(-2);
        this.popupWindowWeight.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowWeight.setContentView(this.listViewWeight);
        return this.popupWindowWeight;
    }


    public PopupWindow popupWindowdistance() {
        this.popupWindowdistance = new PopupWindow(getContext());
        this.listViewdistance = new ListView(getContext());
        this.listViewdistance.setDividerHeight(0);
        this.listViewdistance.setAdapter(this.adapter_distance);
        this.listViewdistance.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                StringBuilder sb = new StringBuilder();
                sb.append("position->");
                sb.append(i);
                Log.d("position", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("arraylist_distance->");
                sb2.append((String) CaloriesburnCalculator.this.arraylist_distance.get(i));
                Log.d("arraylist_distance", sb2.toString());
                CaloriesburnCalculator.this.tv_distance_unit.setText((CharSequence) CaloriesburnCalculator.this.arraylist_distance.get(i));
                CaloriesburnCalculator.this.dismissPopupTopics1();
            }
        });
        this.popupWindowdistance.setFocusable(true);
        this.popupWindowdistance.setWidth(this.tv_distance_unit.getMeasuredWidth());
        this.popupWindowdistance.setHeight(-2);
        this.popupWindowdistance.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowdistance.setContentView(this.listViewdistance);
        return this.popupWindowdistance;
    }


    public PopupWindow popupWindowrunwalk() {
        this.popupWindowrunwalk = new PopupWindow(getContext());
        this.listViewrunwalk = new ListView(getContext());
        this.listViewrunwalk.setDividerHeight(0);
        this.listViewrunwalk.setAdapter(this.adapter_runwalk);
        this.listViewrunwalk.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                StringBuilder sb = new StringBuilder();
                sb.append("position->");
                sb.append(i);
                Log.d("position", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("arraylist_weigth->");
                sb2.append((String) CaloriesburnCalculator.this.arraylist_runwalk.get(i));
                Log.d("arraylist_weigth", sb2.toString());
                CaloriesburnCalculator.this.tv_runwalk_unit.setText((CharSequence) CaloriesburnCalculator.this.arraylist_runwalk.get(i));
                CaloriesburnCalculator.this.tv_runwalk.setText((CharSequence) CaloriesburnCalculator.this.arraylist_runwalk.get(i));
                CaloriesburnCalculator.this.dismissPopupTopics2();
            }
        });
        this.popupWindowrunwalk.setFocusable(true);
        this.popupWindowrunwalk.setWidth(this.tv_runwalk_unit.getMeasuredWidth());
        this.popupWindowrunwalk.setHeight(-2);
        this.popupWindowrunwalk.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowrunwalk.setContentView(this.listViewrunwalk);
        return this.popupWindowrunwalk;
    }


    public void dismissPopupTopics() {
        if (this.popupWindowWeight != null) {
            this.popupWindowWeight.dismiss();
        }
    }


    public void dismissPopupTopics1() {
        if (this.popupWindowdistance != null) {
            this.popupWindowdistance.dismiss();
        }
    }


    public void dismissPopupTopics2() {
        if (this.popupWindowrunwalk != null) {
            this.popupWindowrunwalk.dismiss();
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
                    CaloriesburnCalculator.this.calculate();
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
