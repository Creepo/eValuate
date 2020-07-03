package com.exjobb.evaluate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerEditActivity extends AppCompatActivity {
    private static final String TAG = "EditPlayerActivity";
    private FirebaseFirestore db;
    private EditText etName, etPhone, etNote;
    private String name, phone, note, pathReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_edit);
        this.setTitle("Edit Player");

        initVariables();
        getDataFromIntent();
        makeEditableHaveText();
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: started");
        db = FirebaseFirestore.getInstance();
        etName = findViewById(R.id.editText_Edit_Name);
        etPhone = findViewById(R.id.editText_Edit_Phone);
        etNote = findViewById(R.id.editText_Edit_Note);
    }

    private void makeEditableHaveText() {
        Log.i(TAG, "makeEditableHaveText: started");
        db.document(pathReference).get()
                .addOnSuccessListener(documentSnapshot -> {
                    etName.setText(documentSnapshot.getString("name"), TextView.BufferType.EDITABLE);
                    etPhone.setText(documentSnapshot.getString("phone"), TextView.BufferType.EDITABLE);
                    etNote.setText(documentSnapshot.getString("note"), TextView.BufferType.EDITABLE);
                });
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pathReference = bundle.getString("playerPath");
            Log.d(TAG, "getDataFromIntent: reference: " + pathReference);
        }
    }

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
                editAndSavePlayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editAndSavePlayer() {
        Log.i(TAG, "editAndSavePlayer: started");
        if (validInput()) {
            db.document(pathReference)
                    .update("name", name, "phone", phone, "note", note);
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
