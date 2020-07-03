package com.exjobb.evaluate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;

import static maes.tech.intentanim.CustomIntent.customType;

public class StartScreenPlayerActivity extends AppCompatActivity {
    private static final String TAG = "StartScreenPlayer";
    private TextView tvWelcomeMessage;
    private Intent intent;
    private String pathReference, nameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen_player);
        Log.d(TAG, "onCreate: started");
        this.setTitle("Welcome");

        initVariables();
        getDataFromIntent();

        tvWelcomeMessage.setText(getString(R.string.welcome_msg, nameReference));
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: started");
        tvWelcomeMessage = findViewById(R.id.textView_Player_Greeting);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pathReference = bundle.getString("playerPath");
            nameReference = bundle.getString("playerName");
            Log.d(TAG, "getDataFromIntent: pathReference: " + pathReference + "nameReference: " + nameReference);
        }
    }

    public void goToListOfQuizzes(View view) {
        Log.i(TAG, "goToListOfQuizzes: started");
        intent = new Intent(this, ListOfQuizzesActivity.class);
        intent.putExtra("playerPath", pathReference);
        intent.putExtra("playerName", nameReference);
        startActivity(intent);
        customType(this, "fadein-to-fadeout");
    }

    public void goToPlayerStats(View view) {
        Log.i(TAG, "goToListOfQuizzes: started");
        intent = new Intent(this, StatisticsPlayerActivity.class);
        intent.putExtra("playerPath", pathReference);
        startActivity(intent);
        customType(this, "fadein-to-fadeout");
    }
}
