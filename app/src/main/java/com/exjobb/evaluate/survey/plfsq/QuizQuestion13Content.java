package com.exjobb.evaluate.survey.plfsq;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.data.ScoreHolderModel;

import static maes.tech.intentanim.CustomIntent.customType;

public class QuizQuestion13Content extends AppCompatActivity {
    private static final String TAG = "QuizQuestion13Content";
    private EditText editText;
    private ScoreHolderModel scoreHolder;
    private String pathReference, nameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quiz_question_13);
        Log.d(TAG, "onCreate: started");
        this.setTitle("PLFS-Questionnaire");

        initVariables();
        getDataFromIntent();
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: initiated");
        scoreHolder = new ScoreHolderModel();
        editText = findViewById(R.id.editText_Question_13);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scoreHolder.setQuizScore(bundle.getIntegerArrayList("question12Answer"));
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

    public void goToQuestionFourteen(View view) {
        Log.i(TAG, "goToQuestionFourteen: pressed");
        getInput();

        Intent intent = new Intent(this, QuizQuestion14Content.class);
        intent.putExtra("question13Answer", scoreHolder.getQuizScore());
        intent.putExtra("playerPath", pathReference);
        intent.putExtra("playerName", nameReference);
        startActivity(intent);
        customType(this, "left-to-right");
    }
}
