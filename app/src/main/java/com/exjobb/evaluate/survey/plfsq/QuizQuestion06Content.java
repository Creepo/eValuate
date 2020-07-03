package com.exjobb.evaluate.survey.plfsq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.data.ScoreHolderModel;

import static maes.tech.intentanim.CustomIntent.customType;

public class QuizQuestion06Content extends AppCompatActivity {
    private static final String TAG = "QuizQuestion06Content";
    private Button buttonNext;
    private RadioGroup radioGroupSix;
    private ScoreHolderModel scoreHolder;
    private Integer points;
    private String pathReference, nameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quiz_question_06);
        Log.d(TAG, "onCreate: started");
        this.setTitle("PLFS-Questionnaire");

        initVariables();
        getDataFromIntent();

        radioGroupSix.setOnCheckedChangeListener((group, checkedId) -> {
            Log.i(TAG, "onCheckedChanged: started");

            switch (checkedId) {
                case R.id.radioButton_Much_Better:
                    points = 5;
                    scoreHolder.addPointToHolder(points);
                    Log.d(TAG, "OnCheckedChanged: " + scoreHolder.getNoOfElements());
                    buttonNext.setEnabled(true);
                    Log.d(TAG, "onCheckedChanged: " + group.getChildAt(checkedId));
                    break;
                case R.id.radioButton_Better:
                    points = 4;
                    scoreHolder.addPointToHolder(points);
                    Log.d(TAG, "OnCheckedChanged: " + scoreHolder.getNoOfElements());
                    buttonNext.setEnabled(true);
                    Log.d(TAG, "onCheckedChanged: " + group.getChildAt(checkedId));
                    break;
                case R.id.radioButton_Normal:
                    points = 3;
                    scoreHolder.addPointToHolder(points);
                    Log.d(TAG, "OnCheckedChanged: " + scoreHolder.getNoOfElements());
                    buttonNext.setEnabled(true);
                    Log.d(TAG, "onCheckedChanged: " + group.getChildAt(checkedId));
                    break;
                case R.id.radioButton_Worse:
                    points = 2;
                    scoreHolder.addPointToHolder(points);
                    Log.d(TAG, "OnCheckedChanged: " + scoreHolder.getNoOfElements());
                    buttonNext.setEnabled(true);
                    Log.d(TAG, "onCheckedChanged: " + group.getChildAt(checkedId));
                    break;
                case R.id.radioButton_Much_Worse:
                    points = 1;
                    scoreHolder.addPointToHolder(points);
                    Log.d(TAG, "OnCheckedChanged: " + scoreHolder.getNoOfElements());
                    buttonNext.setEnabled(true);
                    break;
            }
        });
    }

    private void initVariables(){
        Log.i(TAG, "initVariables: initiated");
        scoreHolder = new ScoreHolderModel();
        buttonNext = findViewById(R.id.button_Next_Question);
        radioGroupSix = findViewById(R.id.radioGroup_Question_06);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scoreHolder.setQuizScore(bundle.getIntegerArrayList("question05Answer"));
            pathReference = bundle.getString("playerPath");
            nameReference = bundle.getString("playerName");
            Log.d(TAG, "getDataFromIntent: list size: " + scoreHolder.getNoOfElements() + " Path: " + pathReference +
                    "name: " + nameReference);
        }
    }

    public void goToQuestionSeven(View view) {
        Log.i(TAG, "goToQuestionSeven: pressed");
        Intent intent = new Intent(this, QuizQuestion07Content.class);
        intent.putExtra("question06Answer", scoreHolder.getQuizScore());
        intent.putExtra("playerPath", pathReference);
        intent.putExtra("playerName", nameReference);
        startActivity(intent);
        customType(this, "left-to-right");
    }
}
