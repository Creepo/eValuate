package com.exjobb.evaluate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.adapter.TeamRecyclerViewAdapter;
import com.exjobb.evaluate.data.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static maes.tech.intentanim.CustomIntent.customType;

public class ListOfPlayersActivity extends AppCompatActivity {
    private static final String TAG = "ListOfPlayersActivity";
    private FirebaseFirestore db;
    private CollectionReference playerReference;
    private TeamRecyclerViewAdapter adapter;
    private String pathReference, nameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_players);
        Log.d(TAG, "onCreate: started");
        this.setTitle("My Team");

        db = FirebaseFirestore.getInstance();
        getDataFromIntent();
        initRecyclerView();
    }

    // a collection reference is needed to load the data into the recyclerviewer
    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            playerReference = db.collection(bundle.getString("coachPath") + "/Players");
            pathReference = bundle.getString("coachPath") + "/Players";
            nameReference = bundle.getString("coachName");
            Log.d(TAG, "getDataFromIntent: reference: " + playerReference + "coachPath: " + pathReference);
        }
    }

    private void initRecyclerView() {
        Log.i(TAG, "initRecyclerView: started");
        Query query = playerReference.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        adapter = new TeamRecyclerViewAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_List_Of_Players);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void goToCreatePlayer(View view) {
        Log.i(TAG, "goToCreatePlayer: started");
        Intent intent = new Intent(getApplicationContext(), PlayerCreateActivity.class);
        intent.putExtra("coachPath", pathReference);
        intent.putExtra("coachName", nameReference);
        startActivity(intent);
        customType(this, "fadein-to-fadeout");
    }
}
