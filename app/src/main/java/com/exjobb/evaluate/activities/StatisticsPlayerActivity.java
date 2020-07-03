package com.exjobb.evaluate.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.adapter.PlayerSectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class StatisticsPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_player);
        PlayerSectionsPagerAdapter playerSectionsPagerAdapter = new PlayerSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(playerSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}