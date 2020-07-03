package com.exjobb.evaluate.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.data.UserModel;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Random;

public class PlayerCreateActivity extends AppCompatActivity {
    private static final String TAG = "CreatePlayerActivity";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private FirebaseFirestore db;
    private EditText etName, etPhone, etNote;
    private String name, phone, note, pathReference, playerId, nameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_create);
        Log.d(TAG, "onCreate: started");
        this.setTitle("Add Player");

        initVariables();
        getDataFromIntent();
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: initiated");
        db = FirebaseFirestore.getInstance();
        etName = findViewById(R.id.editText_Name);
        etPhone = findViewById(R.id.editText_Phone);
        etNote = findViewById(R.id.editText_Note);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pathReference = bundle.getString("coachPath");
            nameReference = bundle.getString("coachName");
            Log.d(TAG, "getDataFromIntent: reference: " + pathReference);
        }
    }

    // load all players to see if any id matches the generated one
    public String generatePlayerId() {
        Log.i(TAG, "generatePlayerId: started");
        playerId = "P".concat(String.format("%04d", new Random().nextInt(1001)));
        Log.d(TAG, "generatePlayerId: " + playerId);

        db.collectionGroup("Players")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if (documentSnapshot.getId().equals(playerId)) {
                                playerId = "P".concat(String.format("%04d", new Random().nextInt(1001)));
                                Log.d(TAG, "generatePlayerId: " + documentSnapshot.getId() + documentSnapshot.getData() +
                                        playerId);
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: " + task.getException());
                    }
                });
        return playerId;
    }

    // this is boolean so we can interrupt the adding process if fields are empty
    private boolean validInput() {
        Log.i(TAG, "validInput: initiated");
        name = etName.getText().toString();
        phone = etPhone.getText().toString();
        note = etNote.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(note)) {
            etName.setError("Field cannot be empty.");
            etPhone.setError("Field cannot be empty.");
            etNote.setError("Field cannot be empty.");
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_button:
                createPlayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createPlayer() {
        Log.i(TAG, "createPlayer: started");
        playerId = generatePlayerId();
        Object timeStamp = FieldValue.serverTimestamp();

        if (validInput()) {
            db.collection(pathReference).document(playerId)
                    .set((new UserModel(playerId, name, phone, note, timeStamp)));
            Log.d(TAG, "createPlayer: playerId: " + playerId);
            Toast.makeText(this, "Player addedd", Toast.LENGTH_SHORT).show();
            finish();

            sendSMS(phone);
        }
    }

    private void sendSMS(String phone) {
        String message = getString(R.string.sms_register, name, playerId, nameReference);

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(
                        phone, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Login credientals sent to " + phone,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }
    }
}