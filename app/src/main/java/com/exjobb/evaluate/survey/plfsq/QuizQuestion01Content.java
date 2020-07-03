package com.exjobb.evaluate.survey.plfsq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;

import static maes.tech.intentanim.CustomIntent.customType;

public class QuizQuestion01Content extends AppCompatActivity {
    private static final String TAG = "QuizQuestion01Content";
    private Button buttonNext;
    private RadioGroup radioGroupOne;
    private Integer points;
    private String pathReference, nameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quiz_question_01);
        Log.d(TAG, "onCreate: started");
        this.setTitle("PLFS-Questionnaire");

        initVariables();
        getDataFromIntent();

        radioGroupOne.setOnCheckedChangeListener((group, checkedId) -> {
            Log.i(TAG, "onCheckedChanged: started");

            switch (checkedId) {
                case R.id.radioButton_Much_Better:
                    points = 5;
                    buttonNext.setEnabled(true);
                    Log.d(TAG, "onCheckedChanged: " + group.getChildAt(checkedId));
                    break;
                case R.id.radioButton_Better:
                    points = 4;
                    buttonNext.setEnabled(true);
                    Log.d(TAG, "onCheckedChanged: " + group.getChildAt(checkedId));
                    break;
                case R.id.radioButton_Normal:
                    points = 3;
                    buttonNext.setEnabled(true);
                    Log.d(TAG, "onCheckedChanged: " + group.getChildAt(checkedId));
                    break;
                case R.id.radioButton_Worse:
                    points = 2;
                    buttonNext.setEnabled(true);
                    Log.d(TAG, "onCheckedChanged: " + group.getChildAt(checkedId));
                    break;
                case R.id.radioButton_Much_Worse:
                    points = 1;
                    buttonNext.setEnabled(true);
                    Log.d(TAG, "onCheckedChanged: " + group.getChildAt(checkedId));
                    break;
            }
        });
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: initiated");
        buttonNext = findViewById(R.id.button_Next_Question);
        radioGroupOne = findViewById(R.id.radioGroup_Question_01);
    }

    private void getDataFromIntent() {
        Log.i(TAG, "getDataFromIntent: started");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pathReference = bundle.getString("playerPath");
            nameReference = bundle.getString("playerName");
            Log.d(TAG, "getDataFromIntent: Path: " + pathReference + "name: " + nameReference);
        }
    }

    public void goToQuestionTwo(View view) {
        Log.i(TAG, "goToQuestionTwo: pressed");
        Intent intent = new Intent(this, QuizQuestion02Content.class);
        intent.putExtra("question01Answer", points);
        intent.putExtra("playerPath", pathReference);
        intent.putExtra("playerName", nameReference);
        startActivity(intent);
        customType(this, "left-to-right");
    }
}
