package com.exjobb.evaluate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;

import static maes.tech.intentanim.CustomIntent.customType;

public class StartScreenCoachActivity extends AppCompatActivity {
    private static final String TAG = "ListOfQuizzesActivity";
    private TextView tvWelcomeMessage;
    private Intent intent;
    private String pathReference, nameReference, phoneReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen_coach);
        Log.d(TAG, "onCreate: started");
        this.setTitle("Welcome");

        initVariables();
        getDataFromIntent();

        tvWelcomeMessage.setText(getString(R.string.welcome_msg, nameReference));
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: started");
        tvWelcomeMessage = findViewById(R.id.textView_Coach_Greeting);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pathReference = bundle.getString("coachPath");
            nameReference = bundle.getString("coachName");
            phoneReference = bundle.getString("coachPhone");
            Log.d(TAG, "getDataFromIntent: pathReference: " + pathReference +
                    "NameReference: " + nameReference + "phoneReference: " + phoneReference);
        }
    }

    public void viewTeamList(View view) {
        Log.i(TAG, "viewTeamList: started");
        intent = new Intent(this, ListOfPlayersActivity.class);
        intent.putExtra("coachPath", pathReference);
        intent.putExtra("coachName", nameReference);
        startActivity(intent);
        customType(this, "fadein-to-fadeout");
    }

    public void viewTeamStats(View view) {
        Log.i(TAG, "viewTeamList: started");
        intent = new Intent(this, StatisticsTeamActivity.class);
        intent.putExtra("coachPath", pathReference);
        startActivity(intent);
        customType(this, "fadein-to-fadeout");
    }
}
