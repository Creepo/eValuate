package com.exjobb.evaluate.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static maes.tech.intentanim.CustomIntent.customType;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText editText;
    private Intent intent;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");
        this.setTitle("eValuate");

        MainActivityPermissionsDispatcher.askPermissionsWithPermissionCheck(MainActivity.this);
        askPermissions();

        initVariables();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.SEND_SMS})
    void askPermissions() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: started");
        db = FirebaseFirestore.getInstance();
        editText = findViewById(R.id.editText_SignIn);
    }

    public void signIn(View view) {
        Log.i(TAG, "signIn: started");
        String input = editText.getText().toString().toUpperCase();
        Log.d(TAG, "signIn: input = " + input);

        verifyInputAndContinue(input);
    }

    /*
    this function checks the input against the database to determine if the id code is valid
    it searches all documents in a collection for a the inputted value against a defined key value/field
    if the input is valid then the corresponding activity will be launched
    additionally, the user's database pathReference will be sent with the intent

    currently, feedback is provided if the user types in the wrong credential
    this is done through comparing input and document id
     */

    private void verifyInputAndContinue(String input) {
        Log.i(TAG, "verifyInputAndContinue: started");
        String pathReference;
        if (input.contains("C")) {
            pathReference = "GIH";
            db.collection(pathReference)
                    .whereEqualTo("coachId", input)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                if (input.equals(documentSnapshot.getId())) {
                                    intent = new Intent(getApplicationContext(), StartScreenCoachActivity.class);
                                    intent.putExtra("coachPath", documentSnapshot.getReference().getPath());
                                    intent.putExtra("coachName", documentSnapshot.getString("name"));
                                    intent.putExtra("coachPhone", documentSnapshot.getString("phone"));
                                    Log.d(TAG, "onComplete: " + documentSnapshot.getId() + documentSnapshot.getReference().getPath());
                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    customType(this, "fadein-to-fadeout");
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid ID. Try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "User doesn't exist", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + e.toString());
                    });
        } else if (input.contains("P")) {
            pathReference = "Players";
            db.collectionGroup(pathReference)
                    .whereEqualTo("playerId", input)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                if (input.equals(documentSnapshot.getId())) {
                                    intent = new Intent(getApplicationContext(), StartScreenPlayerActivity.class);
                                    intent.putExtra("playerPath", documentSnapshot.getReference().getPath());
                                    intent.putExtra("playerName", documentSnapshot.getString("name"));
                                    Log.d(TAG, "onComplete: " + documentSnapshot.getId() + documentSnapshot.getReference().getPath());
                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    customType(this, "fadein-to-fadeout");
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid ID. Try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "User doesn't exist", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + e.toString());
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Error signing in", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "signIn: " + input);
        }
    }
}