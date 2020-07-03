package com.exjobb.evaluate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.survey.plfsq.QuizQuestion01Content;

import java.util.ArrayList;

import static maes.tech.intentanim.CustomIntent.customType;

public class ListOfQuizzesActivity extends AppCompatActivity {
    private static final String TAG = "ListOfQuizzesActivity";
    private ListView quizListView;
    private Intent intent;
    private ArrayList<String> quizzes;
    private String pathReference, nameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_quizzes);
        Log.d(TAG, "onCreate: started");
        this.setTitle("Quiz List");

        quizzes = new ArrayList<>();
        quizzes.add("Perceived load and fitness scale (PLFS-Q)");
        quizzes.add("Rate this app quiz");
        quizzes.add("404");

        getDataFromIntent();
        initListAdapter();

        // function looks at index of list to choose the next activity
        quizListView.setOnItemClickListener((parent, view, position, id) -> {
            Log.i(TAG, "onItemClick: started");

            switch (position) {
                case 0:
                    intent = new Intent(getApplicationContext(), QuizQuestion01Content.class);
                    Toast.makeText(getApplicationContext(), "Survey started", Toast.LENGTH_SHORT).show();
                    intent.putExtra("playerPath", pathReference);
                    intent.putExtra("playerName", nameReference);
                    startActivity(intent);
                    customType(this, "fadein-to-fadeout");
                    Log.d(TAG, "onItemClick: position: " + position);
                    return;
                case 1:
                    Toast.makeText(getApplicationContext(), "Survey unavailable", Toast.LENGTH_SHORT).show();
                    return;
                default:
                    Toast.makeText(getApplicationContext(), "404", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pathReference = bundle.getString("playerPath");
            nameReference = bundle.getString("playerName");
            Log.d(TAG, "getDataFromIntent: pathReference: " + pathReference);
        }
    }

    // using a default android layout list item here
    private void initListAdapter() {
        Log.i(TAG, "initListAdapter: started");
        ArrayAdapter<String> quizListAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_activated_1, quizzes);

        quizListView = findViewById(R.id.listView_Quizzes);
        quizListView.setAdapter(quizListAdapter);
    }
}
