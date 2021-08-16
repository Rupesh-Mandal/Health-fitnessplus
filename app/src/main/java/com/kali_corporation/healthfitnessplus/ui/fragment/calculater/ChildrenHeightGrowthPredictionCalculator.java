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

public class ChildrenHeightGrowthPredictionCalculator extends Fragment {
    String TAG = getClass().getSimpleName();
    AdView adView;
    ArrayAdapter<String> adapter_gender;
    ArrayAdapter<String> adapter_height;
    ArrayList<String> arraylist_gender = new ArrayList<>();
    ArrayList<String> arraylist_height = new ArrayList<>();
    float child_predicted_height;
    EditText et_fathers_height;
    EditText et_mother_height;
    float fathers_height;
    String fathers_height_unit;
    String gender;
    GlobalFunction globalFunction;
    float inserted_fathers_height;
    float inserted_mothers_height;
    ImageView iv_back;
    ListView listViewHeight;
    ListView listViewgender;
    String mothers_height_unit;
    float mothes_height;
    private PopupWindow popupWindowHeight_mother;
    private PopupWindow popupWindowgender;
    SharedPreferenceManager sharedPreferenceManager;
    TextView tv_children_height_growth;
    TextView tv_gender;
    TextView tv_heightunit_father;
    TextView tv_heightunit_mother;
    TextView tv_search_predicted_height;
    TextView tv_select_gender;
    TypefaceManager typefaceManager;

    public ChildrenHeightGrowthPredictionCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_children_height_prediction_calculator, container, false);
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
        this.tv_children_height_growth = (TextView) view.findViewById(R.id.tv_children_height_growth);
        this.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        this.tv_heightunit_mother = (TextView) view.findViewById(R.id.tv_heightunit_mother);
        this.tv_heightunit_father = (TextView) view.findViewById(R.id.tv_heightunit_father);
        this.tv_search_predicted_height = (TextView) view.findViewById(R.id.tv_search_predicted_height);
        this.tv_select_gender = (TextView) view.findViewById(R.id.tv_select_gender);
        this.et_fathers_height = (EditText) view.findViewById(R.id.et_fathers_height);
        this.et_mother_height = (EditText) view.findViewById(R.id.et_mother_height);
        this.tv_gender.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit_mother.setTypeface(this.typefaceManager.getLight());
        this.tv_heightunit_father.setTypeface(this.typefaceManager.getLight());
        this.tv_search_predicted_height.setTypeface(this.typefaceManager.getBold());
        this.tv_select_gender.setTypeface(this.typefaceManager.getLight());
        this.et_fathers_height.setTypeface(this.typefaceManager.getLight());
        this.et_mother_height.setTypeface(this.typefaceManager.getLight());
        this.tv_children_height_growth.setTypeface(this.typefaceManager.getBold());
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().addFlags(67108864);
        }
        this.arraylist_gender.clear();
        this.arraylist_gender.add(getString(R.string.Male));
        this.arraylist_gender.add(getString(R.string.Female));
        this.arraylist_height.clear();
        this.arraylist_height.add(getString(R.string.feet));
        this.arraylist_height.add(getString(R.string.cm));
        this.tv_gender.setOnClickListener(showPopupWindow_gender());
        this.tv_heightunit_mother.setOnClickListener(showPopupWindowHeight_mother());
        this.tv_heightunit_father.setOnClickListener(showPopupWindowHeight_father());
        this.adapter_gender = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_gender);
        this.adapter_height = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text1, this.arraylist_height);
        this.adView = (AdView) view.findViewById(R.id.adView);
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new Builder().build();
        mAdView.loadAd(adRequest);
        this.tv_search_predicted_height.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ChildrenHeightGrowthPredictionCalculator.this.et_mother_height.getText().toString().trim().equals("") || ChildrenHeightGrowthPredictionCalculator.this.et_mother_height.getText().toString().trim().equals(".")) {
                    ChildrenHeightGrowthPredictionCalculator.this.et_mother_height.setError(ChildrenHeightGrowthPredictionCalculator.this.getString(R.string.Enter_Mothers_Height));
                } else if (ChildrenHeightGrowthPredictionCalculator.this.et_fathers_height.getText().toString().trim().equals("") || ChildrenHeightGrowthPredictionCalculator.this.et_fathers_height.getText().toString().trim().equals(".")) {
                    ChildrenHeightGrowthPredictionCalculator.this.et_fathers_height.setError(ChildrenHeightGrowthPredictionCalculator.this.getString(R.string.Enter_Fathers_Height));
                } else {
                    ChildrenHeightGrowthPredictionCalculator.this.gender = ChildrenHeightGrowthPredictionCalculator.this.tv_gender.getText().toString();
                    ChildrenHeightGrowthPredictionCalculator.this.inserted_mothers_height = Float.parseFloat(ChildrenHeightGrowthPredictionCalculator.this.et_mother_height.getText().toString());
                    ChildrenHeightGrowthPredictionCalculator.this.inserted_fathers_height = Float.parseFloat(ChildrenHeightGrowthPredictionCalculator.this.et_fathers_height.getText().toString());
                    ChildrenHeightGrowthPredictionCalculator.this.mothers_height_unit = ChildrenHeightGrowthPredictionCalculator.this.tv_heightunit_mother.getText().toString();
                    ChildrenHeightGrowthPredictionCalculator.this.fathers_height_unit = ChildrenHeightGrowthPredictionCalculator.this.tv_heightunit_father.getText().toString();
                    int random = ((int) (Math.random() * 2.0d)) + 1;
                    PrintStream printStream = System.out;
                    StringBuilder sb = new StringBuilder();
                    sb.append("random_number==>");
                    sb.append(random);
                    printStream.println(sb.toString());
                    if (random == 2) {
                        ChildrenHeightGrowthPredictionCalculator.this.showIntertitial();
                    } else {
                        ChildrenHeightGrowthPredictionCalculator.this.calculate();
                    }
                }
            }
        });
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ChildrenHeightGrowthPredictionCalculator.this.onBackPressed();
            }
        });
    }

    private OnClickListener showPopupWindow_gender() {
        return new OnClickListener() {
            public void onClick(View view) {
                ChildrenHeightGrowthPredictionCalculator.this.popupWindowgender().showAsDropDown(view, 0, 0);
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
                sb2.append((String) ChildrenHeightGrowthPredictionCalculator.this.arraylist_gender.get(i));
                Log.d("arraylist_weigth", sb2.toString());
                ChildrenHeightGrowthPredictionCalculator.this.tv_gender.setText((CharSequence) ChildrenHeightGrowthPredictionCalculator.this.arraylist_gender.get(i));
                ChildrenHeightGrowthPredictionCalculator.this.dismissPopupgender();
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

    private OnClickListener showPopupWindowHeight_mother() {
        return new OnClickListener() {
            public void onClick(View view) {
                ChildrenHeightGrowthPredictionCalculator.this.showPopupWindowHeight_mother1().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow showPopupWindowHeight_mother1() {
        this.popupWindowHeight_mother = new PopupWindow(getContext());
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
                sb2.append((String) ChildrenHeightGrowthPredictionCalculator.this.arraylist_height.get(i));
                Log.d("arraylist_height", sb2.toString());
                ChildrenHeightGrowthPredictionCalculator.this.tv_heightunit_mother.setText((CharSequence) ChildrenHeightGrowthPredictionCalculator.this.arraylist_height.get(i));
                ChildrenHeightGrowthPredictionCalculator.this.dismissPopupHeightmother();
            }
        });
        this.popupWindowHeight_mother.setFocusable(true);
        this.popupWindowHeight_mother.setWidth(this.tv_heightunit_mother.getMeasuredWidth());
        this.popupWindowHeight_mother.setHeight(-2);
        this.popupWindowHeight_mother.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowHeight_mother.setContentView(this.listViewHeight);
        return this.popupWindowHeight_mother;
    }


    public void dismissPopupHeightmother() {
        if (this.popupWindowHeight_mother != null) {
            this.popupWindowHeight_mother.dismiss();
        }
    }

    private OnClickListener showPopupWindowHeight_father() {
        return new OnClickListener() {
            public void onClick(View view) {
                ChildrenHeightGrowthPredictionCalculator.this.showPopupWindowHeight_father1().showAsDropDown(view, 0, 0);
            }
        };
    }


    public PopupWindow showPopupWindowHeight_father1() {
        this.popupWindowHeight_mother = new PopupWindow(getContext());
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
                sb2.append((String) ChildrenHeightGrowthPredictionCalculator.this.arraylist_height.get(i));
                Log.d("arraylist_height", sb2.toString());
                ChildrenHeightGrowthPredictionCalculator.this.tv_heightunit_father.setText((CharSequence) ChildrenHeightGrowthPredictionCalculator.this.arraylist_height.get(i));
                ChildrenHeightGrowthPredictionCalculator.this.dismissPopupHeightmfatehr();
            }
        });
        this.popupWindowHeight_mother.setFocusable(true);
        this.popupWindowHeight_mother.setWidth(this.tv_heightunit_father.getMeasuredWidth());
        this.popupWindowHeight_mother.setHeight(-2);
        this.popupWindowHeight_mother.setBackgroundDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(), 17170443));
        this.popupWindowHeight_mother.setContentView(this.listViewHeight);
        return this.popupWindowHeight_mother;
    }


    public void dismissPopupHeightmfatehr() {
        if (this.popupWindowHeight_mother != null) {
            this.popupWindowHeight_mother.dismiss();
        }
    }

    public void calculate() {
        if (this.mothers_height_unit.equalsIgnoreCase(getString(R.string.feet))) {
            this.mothes_height = this.inserted_mothers_height;
        } else {
            this.mothes_height = this.inserted_mothers_height * 0.032808f;
        }
        if (this.fathers_height_unit.equalsIgnoreCase(getString(R.string.feet))) {
            this.fathers_height = this.inserted_fathers_height;
        } else {
            this.fathers_height = this.inserted_fathers_height * 0.032808f;
        }
        if (this.gender.equalsIgnoreCase(getString(R.string.Male))) {
            this.child_predicted_height = (this.mothes_height + this.fathers_height) / 2.0f;
            this.child_predicted_height *= 12.0f;
            this.child_predicted_height += 5.0f;
            this.child_predicted_height /= 12.0f;
        } else {
            this.child_predicted_height = (this.mothes_height + this.fathers_height) / 2.0f;
            this.child_predicted_height *= 12.0f;
            this.child_predicted_height -= 5.0f;
            this.child_predicted_height /= 12.0f;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("child_predicted_height");
        sb.append(this.child_predicted_height);
        Log.d("child_predicted_height", sb.toString());
//        Intent intent = new Intent(this, Children_Predicted_Height_Result.class);
//        intent.putExtra("child_predicted_height", this.child_predicted_height);
//        startActivity(intent);
        result(child_predicted_height);
    }

    private void result(float child_predicted_height) {
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.Your_Childs_Predicted_Height));
        sb.append(" ");
        sb.append(String.format("%.02f", new Object[]{child_predicted_height}));
        sb.append(" ");
        sb.append(getString(R.string.Feet_At_21_Years));
        new AlertDialog.Builder(getActivity())
                .setTitle("Growth Prediction")
                .setMessage(sb.toString())
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
                    ChildrenHeightGrowthPredictionCalculator.this.calculate();
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
        this.adView.setVisibility(View.GONE);
        ActivityCompat.finishAfterTransition(getActivity());
    }
}
