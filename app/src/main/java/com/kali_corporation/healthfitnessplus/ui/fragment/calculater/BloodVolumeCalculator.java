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

public class BloodVolumeCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_height;
    ArrayAdapter<String> adapter_weight;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_height = new ArrayList<>();
    ArrayList<String> arraylist_weigth = new ArrayList<>();
    double blood_volume;
    double bmr_height;
    double bmr_weight;
    EditText et_height;
    EditText et_weight;
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
    TextView tv_bloodvolume;
    TextView tv_gender;
    TextView tv_genderunit;
    TextView tv_heightunit;
    TextView tv_search_bvc;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    String weight_unit;

    public BloodVolumeCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_volume_calc, container, false);
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
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.et_height = (EditText) view.findViewById(R.id.et_height);
        this.et_weight = (EditText) view.findViewById(R.id.et_weight);
        this.tv_bloodvolume = (TextView) view.findViewById(R.id.tv_bloodvolume);
        this.tv_heightunit = (TextView) view.findViewById(R.id.tv_heightunit);
        this.tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        this.tv_search_bvc = (TextView) view.findViewById(R.id.tv_search_bvc);
        this.tv_genderunit = (TextView) view.findViewById(R.id.tv_genderunit);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.tv_bloodvolume.setTypeface(this.typefaceManager.getBold());
        this.et_height.setTypeface(this.typefaceManager.getLight());
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_search_bvc.setTypeface(this.typefaceManager.getBold());
        this.tv_genderunit.setTypeface(this.typefaceManager.getLight());
        this.tv_gender.setTypeface(this.typefaceManager.getLight());
        this.height_unit = getString(R.string.feet);
        this.weight_unit = getString(R.string.lbs);
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BloodVolumeCalculator.this.onBackPressed();
            }
        });
        this.tv_heightunit.setOnClickListener(showPopupWindowHeight());
        this.tv_weightunit.setOnClickListener(showPopupWindow_Weight());
        this.tv_gender.setOnClickListener(showPopupWindowGender());
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.arraylist_height.clear();
        this.arraylist_height.add(getString(R.string.feet));
        this.arraylist_height.add(getString(R.string.cm));
        this.arraylist_weigth.clear();
        this.arraylist_weigth.add(getString(R.string.kg));
        this.arraylist_weigth.add(getString(R.string.lbs));
        this.adapter_height = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_height);
        this.adapter_weight = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_weigth);
        this.tv_search_bvc.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (BloodVolumeCalculator.this.et_height.getText().toString().trim().equals("") || BloodVolumeCalculator.this.et_height.getText().toString().trim().equals(".")) {
                    BloodVolumeCalculator.this.et_height.setError(BloodVolumeCalculator.this.getString(R.string.Enter_Height));
                } else if (BloodVolumeCalculator.this.et_weight.getText().toString().trim().equals("") || BloodVolumeCalculator.this.et_weight.getText().toString().trim().equals(".")) {
                    BloodVolumeCalculator.this.et_weight.setError(BloodVolumeCalculator.this.getString(R.string.Enter_Weight));
                } else {
                    BloodVolumeCalculator.this.height_unit = BloodVolumeCalculator.this.tv_heightunit.getText().toString();
                    BloodVolumeCalculator.this.weight_unit = BloodVolumeCalculator.this.tv_weightunit.getText().toString();
                    BloodVolumeCalculator.this.inserted_weight = Float.parseFloat(BloodVolumeCalculator.this.et_weight.getText().toString());
                    BloodVolumeCalculator.this.inserted_height = Float.parseFloat(BloodVolumeCalculator.this.et_height.getText().toString());
                    BloodVolumeCalculator.this.gender = BloodVolumeCalculator.this.tv_gender.getText().toString().trim();
                    StringBuilder sb = new StringBuilder();
                    sb.append("inserted_weight");
                    sb.append(BloodVolumeCalculator.this.inserted_weight);
                    Log.d("inserted_weight", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("inserted_height");
                    sb2.append(BloodVolumeCalculator.this.inserted_height);
                    Log.d("inserted_height", sb2.toString());
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("random_number==>");
                    sb3.append(random);
                    printStream.println(sb3.toString());
                    if (random == 2) {
                        BloodVolumeCalculator.this.showIntertitial();
                    } else {
                        BloodVolumeCalculator.this.calculate();
                    }
                }
            }
        });
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BloodVolumeCalculator.this.onBackPressed();
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
        this.bmr_height /= 100.0d;
        this.bmr_height = this.bmr_height * this.bmr_height * this.bmr_height;
        if (this.gender.equalsIgnoreCase(getString(R.string.Male))) {
            this.blood_volume = (this.bmr_height * 0.3669d) + (this.bmr_weight * 0.03219d) + 0.6041d;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("blood_volume_male->");
            sb5.append(this.blood_volume);
            Log.d("blood_volume_male->", sb5.toString());
        } else {
            this.blood_volume = (this.bmr_height * 0.35d) + (this.bmr_weight * 0.03308d) + 0.1833d;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("blood_volume_female->");
            sb6.append(this.blood_volume);
            Log.d("blood_volume_female->", sb6.toString());
        }
//        Intent intent = new Intent(this, BloodVolume_Result.class);
//        intent.putExtra("blood_volume", this.blood_volume);
//        startActivity(intent);
        result(blood_volume);
    }

    private void result(double blood_volume) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(blood_volume);
        Log.d("bmr_val->", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getString(R.string.Your_bloodvolume_is));
        sb3.append(" :\n");
        sb3.append(String.format("%.02f", new Object[]{Double.valueOf(this.blood_volume)}));
        sb2.append(String.valueOf(sb3.toString()));
        sb2.append(" ");
        sb2.append(getString(R.string.liter));

        new AlertDialog.Builder(getActivity())
                .setTitle("Blood Volume")
                .setMessage(sb2.toString())
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

    public void onBackPressed() {
        this.adView.setVisibility(View.GONE);
        ActivityCompat.finishAfterTransition(getActivity());
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

    private OnClickListener showPopupWindowHeight() {
        return new OnClickListener() {
            public void onClick(View view) {
                BloodVolumeCalculator.this.popupWindowHeight().showAsDropDown(view, 0, 0);
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
                BloodVolumeCalculator.this.tv_heightunit.setText((CharSequence) BloodVolumeCalculator.this.arraylist_height.get(i));
                BloodVolumeCalculator.this.dismissPopupHeight();
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
                BloodVolumeCalculator.this.popupWindowWeight().showAsDropDown(view, 0, 0);
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
                sb2.append((String) BloodVolumeCalculator.this.arraylist_weigth.get(i));
                Log.d("arraylist_weigth", sb2.toString());
                BloodVolumeCalculator.this.tv_weightunit.setText((CharSequence) BloodVolumeCalculator.this.arraylist_weigth.get(i));
                BloodVolumeCalculator.this.dismissPopupTopics();
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
        this.listViewGender.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                BloodVolumeCalculator.this.tv_gender.setText((CharSequence) BloodVolumeCalculator.this.arraylist_gender.get(i));
                BloodVolumeCalculator.this.tv_genderunit.setText((CharSequence) BloodVolumeCalculator.this.arraylist_gender.get(i));
                BloodVolumeCalculator.this.dismissPopupGender();
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

    private OnClickListener showPopupWindowGender() {
        return new OnClickListener() {
            public void onClick(View view) {
                BloodVolumeCalculator.this.popupWindowGender().showAsDropDown(view, 0, 0);
            }
        };
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
                    BloodVolumeCalculator.this.calculate();
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
