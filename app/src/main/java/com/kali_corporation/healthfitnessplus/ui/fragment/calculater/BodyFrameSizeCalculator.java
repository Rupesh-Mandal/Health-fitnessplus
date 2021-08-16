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

public class BodyFrameSizeCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_height;
    ArrayAdapter<String> adapter_wrist;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_height = new ArrayList<>();
    ArrayList<String> arraylist_wrist = new ArrayList<>();
    float bmi_height;
    float bmi_wrist;
    String body_frame = "";
    EditText et_height;
    EditText et_weight;
    String gender = "";
    GlobalFunction globalFunction;
    String height_unit;
    float inserted_height;
    float inserted_weight;
    ImageView iv_back;
    String large_bodyframe = "Large";
    ListView listViewGender;
    ListView listViewHeight;
    String medium_bodyframe = "Medium";
    private PopupWindow popupWindowGender;

    public PopupWindow popupWindowHeight;
    SharedPreferenceManager sharedPreferenceManager;
    String small_bodyframe = "Small";
    TextView tv_body_frame;
    TextView tv_gender;
    TextView tv_genderunit;
    TextView tv_heightunit;
    TextView tv_search_bmi;
    TextView tv_weightunit;
    TypefaceManager typefaceManager;
    String weight_unit;

    public BodyFrameSizeCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_frame_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.globalFunction = new GlobalFunction(getActivity());
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());
        this.typefaceManager = new TypefaceManager(getContext().getAssets(), getContext());
        this.globalFunction.set_locale_language();
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.small_bodyframe = getString(R.string.Body_frame_text_small);
        this.medium_bodyframe = getString(R.string.Body_frame_text_medium);
        this.large_bodyframe = getString(R.string.Body_frame_text_large);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.et_height = (EditText) view.findViewById(R.id.et_height);
        this.et_weight = (EditText) view.findViewById(R.id.et_weight);
        this.tv_body_frame = (TextView) view.findViewById(R.id.tv_body_frame);
        this.tv_heightunit = (TextView) view.findViewById(R.id.tv_heightunit);
        this.tv_weightunit = (TextView) view.findViewById(R.id.tv_weightunit);
        this.tv_search_bmi = (TextView) view.findViewById(R.id.tv_search_bmi);
        this.tv_genderunit = (TextView) view.findViewById(R.id.tv_genderunit);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.iv_back = (ImageView) view.findViewById(R.id.iv_back);
        this.tv_body_frame.setTypeface(this.typefaceManager.getBold());
        this.et_height.setTypeface(this.typefaceManager.getLight());
        this.et_weight.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_weightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_search_bmi.setTypeface(this.typefaceManager.getBold());
        this.tv_genderunit.setTypeface(this.typefaceManager.getLight());
        this.tv_gender.setTypeface(this.typefaceManager.getLight());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.height_unit = getString(R.string.feet);
        this.weight_unit = getString(R.string.lbs);
        this.tv_heightunit.setOnClickListener(showPopupWindowHeight(true));
        this.tv_weightunit.setOnClickListener(showPopupWindowHeight(false));
        this.tv_gender.setOnClickListener(showPopupWindowGender());
        this.arraylist_height.clear();
        this.arraylist_height.add(getString(R.string.feet));
        this.arraylist_height.add(getString(R.string.cm));
        this.arraylist_wrist.clear();
        this.arraylist_wrist.add(getString(R.string.cm));
        this.arraylist_wrist.add(getString(R.string.inch));
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.adapter_height = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_height);
        this.adapter_wrist = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_wrist);
        this.tv_search_bmi.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (BodyFrameSizeCalculator.this.et_height.getText().toString().trim().equals("") || BodyFrameSizeCalculator.this.et_height.getText().toString().trim().equals(".")) {
                    BodyFrameSizeCalculator.this.et_height.setError(BodyFrameSizeCalculator.this.getString(R.string.Enter_Height));
                } else if (BodyFrameSizeCalculator.this.et_weight.getText().toString().trim().equals("") || BodyFrameSizeCalculator.this.et_weight.getText().toString().trim().equals(".")) {
                    BodyFrameSizeCalculator.this.et_weight.setError(BodyFrameSizeCalculator.this.getString(R.string.Enter_Weight));
                } else {
                    BodyFrameSizeCalculator.this.height_unit = BodyFrameSizeCalculator.this.tv_heightunit.getText().toString();
                    BodyFrameSizeCalculator.this.weight_unit = BodyFrameSizeCalculator.this.tv_weightunit.getText().toString();
                    BodyFrameSizeCalculator.this.inserted_weight = Float.parseFloat(BodyFrameSizeCalculator.this.et_weight.getText().toString());
                    BodyFrameSizeCalculator.this.inserted_height = Float.parseFloat(BodyFrameSizeCalculator.this.et_height.getText().toString());
                    StringBuilder sb = new StringBuilder();
                    sb.append("inserted_weight");
                    sb.append(BodyFrameSizeCalculator.this.inserted_weight);
                    Log.d("inserted_weight", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("inserted_height");
                    sb2.append(BodyFrameSizeCalculator.this.inserted_height);
                    Log.d("inserted_height", sb2.toString());
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("random_number==>");
                    sb3.append(random);
                    printStream.println(sb3.toString());
                    if (random == 2) {
                        BodyFrameSizeCalculator.this.showIntertitial();
                    } else {
                        BodyFrameSizeCalculator.this.calculate();
                    }
                }
            }
        });
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BodyFrameSizeCalculator.this.onBackPressed();
            }
        });
    }

    private OnClickListener showPopupWindowHeight(final boolean z) {
        return new OnClickListener() {
            public void onClick(View view) {
                BodyFrameSizeCalculator.this.popupWindowHeight(z).showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowHeight(final boolean z) {
        this.popupWindowHeight = new PopupWindow(getContext());
        this.listViewHeight = new ListView(getContext());
        this.listViewHeight.setDividerHeight(0);
        if (z) {
            this.listViewHeight.setAdapter(this.adapter_height);
        } else {
            this.listViewHeight.setAdapter(this.adapter_wrist);
        }
        this.listViewHeight.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (z) {
                    BodyFrameSizeCalculator.this.tv_heightunit.setText((CharSequence) BodyFrameSizeCalculator.this.arraylist_height.get(i));
                } else {
                    BodyFrameSizeCalculator.this.tv_weightunit.setText((CharSequence) BodyFrameSizeCalculator.this.arraylist_wrist.get(i));
                }
                BodyFrameSizeCalculator.this.popupWindowHeight.dismiss();
            }
        });
        this.popupWindowHeight.setFocusable(true);
        this.popupWindowHeight.setWidth(this.tv_heightunit.getMeasuredWidth());
        this.popupWindowHeight.setHeight(-2);
        this.popupWindowHeight.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowHeight.setContentView(this.listViewHeight);
        return this.popupWindowHeight;
    }

    private OnClickListener showPopupWindowGender() {
        return new OnClickListener() {
            public void onClick(View view) {
                BodyFrameSizeCalculator.this.popupWindowGender().showAsDropDown(view, 0, 0);
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
                BodyFrameSizeCalculator.this.tv_gender.setText((CharSequence) BodyFrameSizeCalculator.this.arraylist_gender.get(i));
                BodyFrameSizeCalculator.this.tv_genderunit.setText((CharSequence) BodyFrameSizeCalculator.this.arraylist_gender.get(i));
                BodyFrameSizeCalculator.this.dismissPopupGender();
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
        if (this.weight_unit.equalsIgnoreCase(getString(R.string.inch))) {
            this.bmi_wrist = this.inserted_weight;
        } else {
            this.bmi_wrist = this.inserted_weight / 2.54f;
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("");
        sb5.append(this.bmi_wrist);
        Log.d("wrist_in_inch->", sb5.toString());
        this.gender = this.tv_gender.getText().toString().trim();
        if (this.gender.equalsIgnoreCase("female")) {
            if (((double) this.bmi_height) < 5.2d) {
                if (((double) this.bmi_wrist) < 5.5d) {
                    this.body_frame = this.small_bodyframe;
                } else if (((double) this.bmi_wrist) >= 5.5d && ((double) this.bmi_wrist) < 5.75d) {
                    this.body_frame = this.medium_bodyframe;
                } else if (((double) this.bmi_wrist) >= 5.75d) {
                    this.body_frame = this.large_bodyframe;
                }
            } else if (((double) this.bmi_height) >= 5.2d || ((double) this.bmi_height) <= 5.5d) {
                if (((double) this.bmi_wrist) < 6.0d) {
                    this.body_frame = this.small_bodyframe;
                } else if (((double) this.bmi_wrist) >= 6.0d && ((double) this.bmi_wrist) < 6.25d) {
                    this.body_frame = this.medium_bodyframe;
                } else if (((double) this.bmi_wrist) >= 6.25d) {
                    this.body_frame = this.large_bodyframe;
                }
            } else if (((double) this.bmi_height) > 5.5d) {
                if (((double) this.bmi_wrist) < 6.25d) {
                    this.body_frame = this.small_bodyframe;
                } else if (((double) this.bmi_wrist) >= 6.25d && ((double) this.bmi_wrist) < 6.5d) {
                    this.body_frame = this.medium_bodyframe;
                } else if (((double) this.bmi_wrist) >= 6.5d) {
                    this.body_frame = this.large_bodyframe;
                }
            }
        } else if (((double) this.bmi_height) > 5.5d) {
            if (((double) this.bmi_wrist) < 6.5d) {
                this.body_frame = this.small_bodyframe;
            } else if (((double) this.bmi_wrist) >= 6.5d && ((double) this.bmi_wrist) < 7.5d) {
                this.body_frame = this.medium_bodyframe;
            } else if (((double) this.bmi_wrist) >= 7.5d) {
                this.body_frame = this.large_bodyframe;
            }
        } else if (((double) this.bmi_wrist) < 6.5d) {
            this.body_frame = this.small_bodyframe;
        } else if (((double) this.bmi_wrist) >= 6.5d && ((double) this.bmi_wrist) < 7.5d) {
            this.body_frame = this.medium_bodyframe;
        } else if (((double) this.bmi_wrist) >= 7.5d) {
            this.body_frame = this.large_bodyframe;
        }
        StringBuilder sb6 = new StringBuilder();
        sb6.append("body_frame->");
        sb6.append(this.body_frame);
        Log.d("body_frame->", sb6.toString());
//        Intent intent = new Intent(this, BodyFrame_Result.class);
//        intent.putExtra("body_frame", this.body_frame);
//        startActivity(intent);
        result(body_frame);
    }

    private void result(String body_frame) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.body_frame);
        Log.d("body_frame->", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getString(R.string.Body_frame_text));
        sb2.append(" : ");
        sb2.append(body_frame);
        new AlertDialog.Builder(getActivity())
                .setTitle("Body Frame")
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
                    BodyFrameSizeCalculator.this.calculate();
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
