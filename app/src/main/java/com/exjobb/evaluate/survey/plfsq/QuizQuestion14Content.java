package com.exjobb.evaluate.survey.plfsq;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.activities.MainActivity;
import com.exjobb.evaluate.data.ScoreHolderModel;

import static maes.tech.intentanim.CustomIntent.customType;

public class QuizQuestion14Content extends AppCompatActivity {
    private static final String TAG = "QuizQuestion14Reworked";
    private EditText editText;
    private ScoreHolderModel scoreHolder;
    private String pathReference, nameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quiz_question_14);
        Log.d(TAG, "onCreate: started");
        // this prevents the app from starting up on this activity upon relaunch
        if (savedInstanceState != null) {
            startActivity(new Intent(this, MainActivity.class));
        }

        this.setTitle("PLFS-Questionnaire");

        initVariables();
        getDataFromIntent();
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: initiated");
        scoreHolder = new ScoreHolderModel();
        editText = findViewById(R.id.editText_Question_14);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scoreHolder.setQuizScore(bundle.getIntegerArrayList("question13Answer"));
            pathReference = bundle.getString("playerPath");
            nameReference = bundle.getString("playerName");
            Log.d(TAG, "getDataFromIntent: list size: " + scoreHolder.getNoOfElements() + " Path: " + pathReference +
                    "name: " + nameReference);
        }
    }

    private void getInput() {
        Log.i(TAG, "getInput: started");
        // if user leaves field empty then default the value to 0}
        Integer parsedInput;
        if (TextUtils.isEmpty(editText.getText())) {
            parsedInput = 0;
            scoreHolder.addPointToHolder(parsedInput);
            Log.d(TAG, "goToQuestionTwelve: " + scoreHolder.getNoOfElements());
        } else {
            parsedInput = Integer.parseInt(editText.getText().toString());
            scoreHolder.addPointToHolder(parsedInput);
            Log.d(TAG, "goToQuestionTwelve: " + scoreHolder.getNoOfElements() +
                    " value parsed: " + parsedInput);
        }
    }

    public void goToQuestionFifteen(View view) {
        Log.i(TAG, "goToQuestionFifteen: pressed");
        getInput();

        Intent intent = new Intent(this, QuizQuestion15Content.class);
        intent.putExtra("question14Answer", scoreHolder.getQuizScore());
        intent.putExtra("playerPath", pathReference);
        intent.putExtra("playerName", nameReference);
        startActivity(intent);
        customType(this, "left-to-right");
    }
}
