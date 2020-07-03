package com.exjobb.evaluate.survey.plfsq;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.data.ScoreHolderModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;

import java.util.Calendar;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;

public class QuizQuestion15Content extends AppCompatActivity {
    private static final String TAG = "QuizQuestion15Reworked";
    private FirebaseFirestore db;
    private EditText editText;
    private ScoreHolderModel scoreHolder;
    private LocalDate localDate;
    private String pathReference, nameReference, coachPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quiz_question_15);
        AndroidThreeTen.init(this);
        Log.d(TAG, "onCreate: started");
        this.setTitle("PLFS-Questionnaire");

        initVariables();
        getDataFromIntent();
        getCoachNumber();
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: initiated");
        db = FirebaseFirestore.getInstance();
        localDate = LocalDate.now();
        scoreHolder = new ScoreHolderModel();
        editText = findViewById(R.id.editText_Question_15);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scoreHolder.setQuizScore(bundle.getIntegerArrayList("question14Answer"));
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

    public void goToQuestionResult(View view) {
        Log.i(TAG, "goToQuestionResult: pressed");
        getInput();
        String week = "Week " + Calendar.getInstance(Locale.FRANCE).get(Calendar.WEEK_OF_YEAR);
        String day = localDate.getDayOfWeek().toString();
        Log.d(TAG, "goToQuestionResult: day: " + day + " of week: " + week);
        String coachId = FirebaseFirestore.getInstance().document(pathReference).getParent().getParent().getId();
        String playerId = FirebaseFirestore.getInstance().document(pathReference).getId();
        Log.d(TAG, "goToQuestionResult: coach: " + coachId + "player: " + playerId);

        saveToScoresDB(coachId, playerId, week, day);
        sendSMS(coachPhone, day, week);

        Intent intent = new Intent(this, QuizResultPageContent.class);
        intent.putExtra("question15Answer", scoreHolder.getQuizScore());
        intent.putExtra("playerPath", pathReference);
        intent.putExtra("playerName", nameReference);
        startActivity(intent);
        customType(this, "left-to-right");
    }

    private void saveToScoresDB(String coachId, String playerId, String week, String day) {
        Log.i(TAG, "saveToScoresDB: started");
        db.document(pathReference).collection("Scores").document(week).collection("Days").document(day)
                .set(scoreHolder.convertToMap(coachId, playerId, week, day));
        Toast.makeText(this, "Results saved", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void sendSMS(String phone, String day, String week) {
        String message = getString(R.string.sms_update, nameReference, day, week, scoreHolder.getPointsFromHolder(0));

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(
                        phone, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Coach notified",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }
    }

    private void getCoachNumber() {
        DocumentReference documentReference = db.document(pathReference).getParent().getParent();
        documentReference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            coachPhone = document.getString("phone");
                            Log.d(TAG, "getCoachNumber: " + coachPhone);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
    }
}