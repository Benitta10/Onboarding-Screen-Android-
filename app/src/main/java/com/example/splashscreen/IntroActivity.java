package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {


    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    Button btnGetStarted;
    int position = 0;
    Animation btnAnimation;
    TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // before launch check if this activity is open or not
        if(restorePrefData()){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
            
        }



        // Initialize views
        btnNext = findViewById(R.id.button_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.btn_animation);
        tvSkip = findViewById(R.id.tv_skip);

        // Fill the list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Welcome to Sit still","Let us help you find a workspace near you.",R.drawable.img1));
        mList.add(new ScreenItem("hello","You no longer have to wait around to book a slot at co working space and spend a lot of money.",R.drawable.img2));
        mList.add(new ScreenItem("Bookings", "Select from a list of places around you.\n" +
                                                    "Reserve seats.\n"+
                                                    "Pay via the app.\n"+
                                                    "Redeem the whole amount at the venue.",R.drawable.img3));

        //Setup View Pager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // Setup Tab with view pager
        tabIndicator.setupWithViewPager(screenPager);

        // Next button click action
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()){

                    position++;
                    screenPager.setCurrentItem(position);

                }

                if(position == mList.size()-1){
                    loadLastScreen();
                }
            }
        });

        // Tab layout listener

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == mList.size() - 1){

                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Get started Button action

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

                // set boolean value
                savePrefData();
                finish();

            }
        });

        // skip button click listener

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });

    }

   private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("IsIntroOpened",false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("IsIntroOpened", true);
        editor.commit();


    }


    private void loadLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);

        // setup animation
        btnGetStarted.setAnimation(btnAnimation);


    }
}
