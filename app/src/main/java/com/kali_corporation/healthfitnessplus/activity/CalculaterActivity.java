package com.kali_corporation.healthfitnessplus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.AlcoholCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.BMICalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.BloodPressureCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.BloodDonationCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.BloodVolumeCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.BmrCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.BodyAdiposityIndexCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.BodyFrameSizeCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.BodyfatCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.BodySurfaceAreaCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.CaloriesburnCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.ChestHipRatioCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.ChildGrowthCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.ChildrenHeightGrowthPredictionCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.CholestrolCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.DailyCaloriesIntakeCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.DailyWaterIntakeCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.HeartRateCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.IdealBodyWeightCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.LeanBodyMassCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.MenstrualOvulationCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.PregnancyDueDateCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.RestingMetabolicRate;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.SmokingRiskCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.Sugarcalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.TrademillCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.WaistHeightRatioCalculator;
import com.kali_corporation.healthfitnessplus.ui.fragment.calculater.WaistHipRatioCalculator;

import java.util.ArrayList;

public class CalculaterActivity extends AppCompatActivity {

    ArrayList<Fragment> fragment=new ArrayList<>();
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_caculater);
        position=getIntent().getIntExtra("pos",0);
        fragment.add(new BmrCalculator());
        fragment.add(new AlcoholCalculator());
        fragment.add(new BloodDonationCalculator());
        fragment.add(new BloodPressureCalculator());
        fragment.add(new Sugarcalculator());
        fragment.add(new BloodVolumeCalculator());
        fragment.add(new BodyAdiposityIndexCalculator());
        fragment.add(new BodyfatCalculator());
        fragment.add(new BodyFrameSizeCalculator());
        fragment.add(new BMICalculator());
        fragment.add(new BodySurfaceAreaCalculator());
        fragment.add(new TrademillCalculator());
        fragment.add(new CaloriesburnCalculator());
        fragment.add(new ChestHipRatioCalculator());
        fragment.add(new ChildrenHeightGrowthPredictionCalculator());
        fragment.add(new CholestrolCalculator());
        fragment.add(new DailyCaloriesIntakeCalculator());
        fragment.add(new DailyWaterIntakeCalculator());
        fragment.add(new HeartRateCalculator());
        fragment.add(new IdealBodyWeightCalculator());
        fragment.add(new ChildGrowthCalculator());
        fragment.add(new LeanBodyMassCalculator());
        fragment.add(new MenstrualOvulationCalculator());
        fragment.add(new PregnancyDueDateCalculator());
        fragment.add(new RestingMetabolicRate());
        fragment.add(new SmokingRiskCalculator());
        fragment.add(new WaistHeightRatioCalculator());
        fragment.add(new WaistHipRatioCalculator());
        openFragment(fragment.get(position));

    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.nav_host_fragment, fragment);
        beginTransaction.addToBackStack(null);
        beginTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}