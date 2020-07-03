package com.exjobb.evaluate.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.adapter.TeamSectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class StatisticsTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_team);
        TeamSectionsPagerAdapter teamSectionsPagerAdapter = new TeamSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(teamSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}