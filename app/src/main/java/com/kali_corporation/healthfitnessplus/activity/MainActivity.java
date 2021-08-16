package com.kali_corporation.healthfitnessplus.activity;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.ui.fragment.Fragment_Calculate;
import com.kali_corporation.healthfitnessplus.ui.fragment.Fragment_Reminder;
import com.kali_corporation.healthfitnessplus.ui.fragment.Fragment_Walk_and_Step;
import com.kali_corporation.healthfitnessplus.ui.fragment.Fragment_Workout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ArrayList<Fragment> fragment=new ArrayList<>();
    String title;
    int position;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (VERSION.SDK_INT > 21) {
            StrictMode.setThreadPolicy(new Builder().permitAll().build());
        }
        setContentView((int) R.layout.activity_main);
        position=getIntent().getIntExtra("pos",0);
        title=getIntent().getStringExtra("title");
        toolbar=findViewById(R.id.toolbar);
        fragment.add(new Fragment_Calculate());
        fragment.add(new Fragment_Reminder());
        fragment.add(new Fragment_Workout());
        fragment.add(new Fragment_Walk_and_Step());
        openFragment(fragment.get(position),title);
    }

    public void openFragment(Fragment fragment, String title) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.nav_host_fragment, fragment);
        beginTransaction.addToBackStack(null);
        beginTransaction.commit();
        toolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
//        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
//        startActivity(intent);
        finish();
    }
}
