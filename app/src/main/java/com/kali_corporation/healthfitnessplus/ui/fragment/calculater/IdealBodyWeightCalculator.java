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

public class IdealBodyWeightCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_bodyframe;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_height;
    float additional_height;
    ArrayList<String> arraylist_bodyframe = new ArrayList<>();
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_height = new ArrayList<>();
    String body_ftrame;
    float bodyframe_height;
    EditText et_height;
    String gender;
    GlobalFunction globalFunction;
    String height_unit;
    float ideal_body_weight;
    float inserted_height;
    ImageView iv_back;
    ListView listViewHeight;
    ListView listViewbodyframe;
    ListView listViewgender;
    private PopupWindow popupWindowHeight;
    private PopupWindow popupWindowbodyframe;
    private PopupWindow popupWindowgender;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_body_body_weight;
    TextView tv_body_frame;
    TextView tv_bodyframe;
    TextView tv_gender;
    TextView tv_heightunit;
    TextView tv_search_ideal_bodyweight;
    TextView tv_select;
    TypefaceManager typefaceManager;

    public IdealBodyWeightCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ideal_body_weight_calculator, container, false);
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
        this.et_height = (EditText) view.findViewById(R.id.et_height);
        this.tv_body_body_weight = (TextView) view.findViewById(R.id.tv_body_body_weight);
        this.tv_heightunit = (TextView) view.findViewById(R.id.tv_heightunit);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.tv_body_frame = (TextView) view.findViewById(R.id.tv_body_frame);
        this.tv_search_ideal_bodyweight = (TextView) view.findViewById(R.id.tv_search_ideal_bodyweight);
        this.tv_select = (TextView) view.findViewById(R.id.tv_select);
        this.tv_bodyframe = (TextView) view.findViewById(R.id.tv_bodyframe);
        this.et_height.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit.setTypeface(this.typefaceManager.getLight());
        this.tv_gender.setTypeface(this.typefaceManager.getLight());
        this.tv_body_frame.setTypeface(this.typefaceManager.getLight());
        this.tv_search_ideal_bodyweight.setTypeface(this.typefaceManager.getBold());
        this.tv_select.setTypeface(this.typefaceManager.getLight());
        this.tv_bodyframe.setTypeface(this.typefaceManager.getLight());
        this.tv_body_body_weight.setTypeface(this.typefaceManager.getBold());
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);

        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.tv_heightunit.setOnClickListener(showPopupWindowHeight());
        this.tv_gender.setOnClickListener(showPopupWindow_gender());
        this.tv_body_frame.setOnClickListener(showPopupWindow_bodyframe());
        this.arraylist_height.clear();
        this.arraylist_height.add(getString(R.string.feet));
        this.arraylist_height.add(getString(R.string.cm));
        this.arraylist_bodyframe.clear();
        this.arraylist_bodyframe.add(getString(R.string.Light));
        this.arraylist_bodyframe.add(getString(R.string.Medium));
        this.arraylist_bodyframe.add(getString(R.string.Heavy));
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.adapter_height = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_height);
        this.adapter_bodyframe = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_bodyframe);
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.tv_search_ideal_bodyweight.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (IdealBodyWeightCalculator.this.et_height.getText().toString().trim().equals("") || IdealBodyWeightCalculator.this.et_height.getText().toString().trim().equals(".")) {
                    IdealBodyWeightCalculator.this.et_height.setError(IdealBodyWeightCalculator.this.getString(R.string.Enter_Height));
                    return;
                }
                IdealBodyWeightCalculator.this.inserted_height = Float.parseFloat(IdealBodyWeightCalculator.this.et_height.getText().toString().trim());
                IdealBodyWeightCalculator.this.gender = IdealBodyWeightCalculator.this.tv_gender.getText().toString().trim();
                IdealBodyWeightCalculator.this.body_ftrame = IdealBodyWeightCalculator.this.tv_body_frame.getText().toString();
                IdealBodyWeightCalculator.this.height_unit = IdealBodyWeightCalculator.this.tv_heightunit.getText().toString();
                int random = ((int) (Math.random() * 2.0d)) + 1;
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("random_number==>");
                sb.append(random);
                printStream.println(sb.toString());
                if (random == 2) {
                    IdealBodyWeightCalculator.this.showIntertitial();
                } else {
                    IdealBodyWeightCalculator.this.calculate();
                }
            }
        });
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                IdealBodyWeightCalculator.this.onBackPressed();
            }
        });
    }

    private OnClickListener showPopupWindowHeight() {
        return new OnClickListener() {
            public void onClick(View view) {
                IdealBodyWeightCalculator.this.popupWindowHeight().showAsDropDown(view, 0, 0);
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
                sb2.append((String) IdealBodyWeightCalculator.this.arraylist_height.get(i));
                Log.d("arraylist_height", sb2.toString());
                IdealBodyWeightCalculator.this.tv_heightunit.setText((CharSequence) IdealBodyWeightCalculator.this.arraylist_height.get(i));
                IdealBodyWeightCalculator.this.dismissPopupHeight();
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

    private OnClickListener showPopupWindow_gender() {
        return new OnClickListener() {
            public void onClick(View view) {
                IdealBodyWeightCalculator.this.popupWindowgender().showAsDropDown(view, 0, 0);
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
                sb2.append((String) IdealBodyWeightCalculator.this.arraylist_gender.get(i));
                Log.d("arraylist_bodyframe", sb2.toString());
                IdealBodyWeightCalculator.this.tv_gender.setText((CharSequence) IdealBodyWeightCalculator.this.arraylist_gender.get(i));
                IdealBodyWeightCalculator.this.dismissPopupgender();
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

    private OnClickListener showPopupWindow_bodyframe() {
        return new OnClickListener() {
            public void onClick(View view) {
                IdealBodyWeightCalculator.this.popupWindowbodyframe().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow popupWindowbodyframe() {
        this.popupWindowbodyframe = new PopupWindow(getContext());
        this.listViewbodyframe = new ListView(getContext());
        this.listViewbodyframe.setDividerHeight(0);
        this.listViewbodyframe.setAdapter(this.adapter_bodyframe);
        this.listViewbodyframe.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                StringBuilder sb = new StringBuilder();
                sb.append("position->");
                sb.append(i);
                Log.d("position", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("arraylist_bodyframe->");
                sb2.append((String) IdealBodyWeightCalculator.this.arraylist_bodyframe.get(i));
                Log.d("arraylist_bodyframe", sb2.toString());
                IdealBodyWeightCalculator.this.tv_body_frame.setText((CharSequence) IdealBodyWeightCalculator.this.arraylist_bodyframe.get(i));
                IdealBodyWeightCalculator.this.dismissPopupTopics();
            }
        });
        this.popupWindowbodyframe.setFocusable(true);
        this.popupWindowbodyframe.setWidth(this.tv_body_frame.getMeasuredWidth());
        this.popupWindowbodyframe.setHeight(-2);
        this.popupWindowbodyframe.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowbodyframe.setContentView(this.listViewbodyframe);
        return this.popupWindowbodyframe;
    }


    public void dismissPopupTopics() {
        if (this.popupWindowbodyframe != null) {
            this.popupWindowbodyframe.dismiss();
        }
    }

    public void calculate() {
        if (this.height_unit.equalsIgnoreCase(getString(R.string.feet))) {
            this.bodyframe_height = this.inserted_height * 12.0f;
        } else {
            this.bodyframe_height = this.inserted_height * 0.393f;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("bodyframe_height");
        sb.append(this.bodyframe_height);
        Log.d("bodyframe_height", sb.toString());
        if (this.gender.equals(getString(R.string.Female))) {
            if (this.bodyframe_height > 60.0f) {
                this.additional_height = this.bodyframe_height - 60.0f;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("additional_height");
                sb2.append(this.additional_height);
                Log.d("additional_height", sb2.toString());
                this.ideal_body_weight = this.additional_height * 2.3f;
                this.ideal_body_weight += 45.5f;
            } else if (this.bodyframe_height <= 60.0f) {
                this.additional_height = 60.0f - this.bodyframe_height;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("additional_height");
                sb3.append(this.additional_height);
                Log.d("additional_height", sb3.toString());
                this.ideal_body_weight = this.additional_height * 2.3f;
                this.ideal_body_weight = 45.5f - this.ideal_body_weight;
            }
        } else if (this.bodyframe_height > 60.0f) {
            this.additional_height = this.bodyframe_height - 60.0f;
            this.ideal_body_weight = this.additional_height * 2.3f;
            this.ideal_body_weight += 50.0f;
        } else if (this.bodyframe_height <= 60.0f) {
            this.additional_height = 60.0f - this.bodyframe_height;
            this.ideal_body_weight = this.additional_height * 2.3f;
            this.ideal_body_weight = 50.0f - this.ideal_body_weight;
        }
        if (this.ideal_body_weight < 0.0f) {
            this.ideal_body_weight = 0.0f;
        }
//        Intent intent = new Intent(this, Ideal_Body_Weight_Result.class);
//        intent.putExtra("ideal_body_weight", this.ideal_body_weight);
//        startActivity(intent);
        result(ideal_body_weight);
    }

    private void result(float ideal_body_weight) {
        if (ideal_body_weight <= 0.0f) {
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.Your_ideal_body_weight_is));
            sb.append("0");
            sb.append(getString(R.string.kg));
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getString(R.string.Your_ideal_body_weight_range_is));
            sb2.append("0-0");
            sb2.append(getString(R.string.kg));
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb.toString());
            sb3.append("\n");
            sb3.append(sb2.toString());
            resultDailog(sb3.toString());
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getString(R.string.Your_ideal_body_weight_is));
        sb3.append(" ");
        sb3.append(Math.round(ideal_body_weight));
        sb3.append(getString(R.string.kg));
        StringBuilder sb4 = new StringBuilder();
        sb4.append(getString(R.string.Your_ideal_body_weight_range_is));
        sb4.append(" ");
        sb4.append(Math.round(ideal_body_weight) - 5);
        sb4.append("-");
        sb4.append(Math.round(ideal_body_weight) + 5);
        sb4.append(getString(R.string.kg));
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb3.toString());
        sb5.append("\n");
        sb5.append(sb4.toString());
        resultDailog(sb5.toString());

    }

    private void resultDailog(String toString) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Body Weight")
                .setMessage(toString)
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
                    IdealBodyWeightCalculator.this.calculate();
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

    public void onBackPressed() {
        this.adView.setVisibility(8);
        ActivityCompat.finishAfterTransition(getActivity());
    }
}
