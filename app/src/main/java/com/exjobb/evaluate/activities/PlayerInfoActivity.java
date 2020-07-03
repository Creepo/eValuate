package com.exjobb.evaluate.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerInfoActivity extends AppCompatActivity {
    private static final String TAG = "ViewPlayerInfoActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tvName, tvPhone, tvNote;
    private String pathReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        this.setTitle("Profile");

        initVariables();
        getDataFromIntent();

        db.document(pathReference).get()
                .addOnSuccessListener(documentSnapshot -> {
                    tvName.setText(documentSnapshot.getString("name"));
                    tvPhone.setText(documentSnapshot.getString("phone"));
                    tvNote.setText(documentSnapshot.getString("note"));
                });
    }

    private void initVariables() {
        tvName = findViewById(R.id.textView_View_Name);
        tvPhone = findViewById(R.id.textView_View_Phone);
        tvNote = findViewById(R.id.textView_View_Note);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pathReference = bundle.getString("playerPath");
            Log.d(TAG, "getDataFromIntent: player: " + pathReference);
        }
    }
}
