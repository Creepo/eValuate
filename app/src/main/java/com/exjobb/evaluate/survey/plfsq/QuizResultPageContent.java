package com.exjobb.evaluate.survey.plfsq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.activities.StartScreenPlayerActivity;
import com.exjobb.evaluate.data.ScoreHolderModel;

import static maes.tech.intentanim.CustomIntent.customType;

public class QuizResultPageContent extends AppCompatActivity {
    private static final String TAG = "QuizResultPageContent ";
    private TextView tvPsychological, tvPsychophysiological, tvPhysiological, tvPhysiologicalMedical, tvHealthResult;
    private ScoreHolderModel scoreHolder;
    private String pathReference, nameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quiz_result_page);
        Log.d(TAG, "onCreate: started");
        this.setTitle("Questionnaire Results");

        initVariables();
        getDataFromIntent();
        formattedString();
    }

    private void initVariables() {
        scoreHolder = new ScoreHolderModel();
        tvPsychological = findViewById(R.id.questionResultSectionOne);
        tvPsychophysiological = findViewById(R.id.questionResultSectionTwo);
        tvPhysiological = findViewById(R.id.questionResultSectionThree);
        tvPhysiologicalMedical = findViewById(R.id.questionResultSectionFour);
        tvHealthResult = findViewById(R.id.questionResult);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scoreHolder.setQuizScore(bundle.getIntegerArrayList("question15Answer"));
            pathReference = bundle.getString("playerPath");
            nameReference = bundle.getString("playerName");
            Log.d(TAG, "getDataFromIntent: list size: " + scoreHolder.getNoOfElements() + " Path: " + pathReference +
                    "name: " + nameReference);
        }
    }

    private void formattedString() {
        Log.i(TAG, "formattedString: started");
        String healthMessage, advice;

        if (scoreHolder.getPointsFromHolder(0) < 14) {
            healthMessage = "ALARMING";
            advice = "Immediately seek out the coach.";
        } else if (scoreHolder.getPointsFromHolder(0) < 18 && scoreHolder.getPointsFromHolder(0) > 14) {
            healthMessage = "AFFECTED";
            advice = "Consider conversing with the coach.";
        } else {
            healthMessage = "GOOD";
            advice = "Keep up the good work.";
        }

        String totalResult = getString(R.string.health_is, healthMessage, advice);

        // applying  the string to the textviews
        tvPsychological.setText(getString(R.string.psychological_score, scoreHolder.getPointsFromHolder(1)));
        tvPsychophysiological.setText(getString(R.string.psychophysiological_score, scoreHolder.getPointsFromHolder(2)));
        tvPhysiological.setText(getString(R.string.physiological_score, scoreHolder.getPointsFromHolder(3)));
        tvPhysiologicalMedical.setText(getString(R.string.physiological_medical_score, scoreHolder.getPointsFromHolder(4)));
        tvHealthResult.setText(totalResult);

    }

    public void goToPlayerScreen(View view) {
        Intent intent = new Intent(this, StartScreenPlayerActivity.class);
        intent.putExtra("playerPath", pathReference);
        intent.putExtra("playerName", nameReference);
        Log.d(TAG, "goToPlayerScreen: " + nameReference);
        startActivity(intent);
        customType(this, "fadein-to-fadeout");
    }
}
