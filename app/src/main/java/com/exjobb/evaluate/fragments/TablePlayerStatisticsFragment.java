package com.exjobb.evaluate.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.adapter.StatisticsPlayerListViewAdapter;
import com.exjobb.evaluate.data.ListItemPlayerModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TablePlayerStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TablePlayerStatisticsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "TablePlayerStatistics";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;
    private StatisticsPlayerListViewAdapter adapter;
    private ListItemPlayerModel itemModel01, itemModel02, itemModel03, itemModel04, itemModel05, itemModel06, itemModel07,
            itemModel08, itemModel09, itemModel10, itemModel11, itemModel12, itemModel13, itemModel14, itemModel15, itemModelTotal;
    private Spinner spinner;
    private ImageButton imageButtonNext, imageButtonPrevious;
    private Map<String, String> map;
    private ArrayList<ListItemPlayerModel> itemModels;
    private String pathReference;

    public TablePlayerStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TablePlayerStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TablePlayerStatisticsFragment newInstance(String param1, String param2) {
        TablePlayerStatisticsFragment fragment = new TablePlayerStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table_player_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVariables(view);
        initListTable(view);
        initSpinner(view);
        getDataFromIntent();

        populateList();

        imageButtonNext.setOnClickListener(v -> {
            int position = spinner.getSelectedItemPosition();
            if (position < 51) {
                spinner.setSelection(position + 1);
            } else {
                spinner.setSelection(0);
            }
        });
        imageButtonPrevious.setOnClickListener(v -> {
            int position = spinner.getSelectedItemPosition();
            if (position > 0) {
                spinner.setSelection(position - 1);
            } else {
                spinner.setSelection(51);
            }
        });

    }

    private void initVariables(View view) {
        db = FirebaseFirestore.getInstance();
        imageButtonNext = view.findViewById(R.id.imageButton_Next_Week);
        imageButtonPrevious = view.findViewById(R.id.imageButton_Previous_Week);
        itemModels = new ArrayList<>();
        map = new HashMap<>();
    }

    private void initListTable(View view) {
        ListView listView = view.findViewById(R.id.listView_Statistic);
        adapter = new StatisticsPlayerListViewAdapter(getActivity(), itemModels);
        listView.setAdapter(adapter);
    }

    private void initSpinner(View view) {
        spinner = view.findViewById(R.id.spinner_Weeks);
        ArrayList<String> weeks = new ArrayList<>();

        // populate spinner with 52 weeks
        for (int i = 1; i < 53; i++) {
            weeks.add("Week " + i);
        }
        // using a pre-made layout item
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, weeks);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void getDataFromIntent() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            pathReference = bundle.getString("playerPath") + "/Scores/";
        }
    }

    private void initListData() {
        Log.i(TAG, "initListData: started");
        itemModel01.setQuestionCellMon(map.get("Mon1"));
        itemModel01.setQuestionCellTue(map.get("Tue1"));
        itemModel01.setQuestionCellWed(map.get("Wed1"));
        itemModel01.setQuestionCellThu(map.get("Thu1"));
        itemModel01.setQuestionCellFri(map.get("Fri1"));
        itemModel01.setQuestionCellSat(map.get("Sat1"));
        itemModel01.setQuestionCellSun(map.get("Sun1"));

        itemModel02.setQuestionCellMon(map.get("Mon2"));
        itemModel02.setQuestionCellTue(map.get("Tue2"));
        itemModel02.setQuestionCellWed(map.get("Wed2"));
        itemModel02.setQuestionCellThu(map.get("Thu2"));
        itemModel02.setQuestionCellFri(map.get("Fri2"));
        itemModel02.setQuestionCellSat(map.get("Sat2"));
        itemModel02.setQuestionCellSun(map.get("Sun2"));

        itemModel03.setQuestionCellMon(map.get("Mon3"));
        itemModel03.setQuestionCellTue(map.get("Tue3"));
        itemModel03.setQuestionCellWed(map.get("Wed3"));
        itemModel03.setQuestionCellThu(map.get("Thu3"));
        itemModel03.setQuestionCellFri(map.get("Fri3"));
        itemModel03.setQuestionCellSat(map.get("Sat3"));
        itemModel03.setQuestionCellSun(map.get("Sun3"));

        itemModel04.setQuestionCellMon(map.get("Mon4"));
        itemModel04.setQuestionCellTue(map.get("Tue4"));
        itemModel04.setQuestionCellWed(map.get("Wed4"));
        itemModel04.setQuestionCellThu(map.get("Thu4"));
        itemModel04.setQuestionCellFri(map.get("Fri4"));
        itemModel04.setQuestionCellSat(map.get("Sat4"));
        itemModel04.setQuestionCellSun(map.get("Sun4"));

        itemModel05.setQuestionCellMon(map.get("Mon5"));
        itemModel05.setQuestionCellTue(map.get("Tue5"));
        itemModel05.setQuestionCellWed(map.get("Wed5"));
        itemModel05.setQuestionCellThu(map.get("Thu5"));
        itemModel05.setQuestionCellFri(map.get("Fri5"));
        itemModel05.setQuestionCellSat(map.get("Sat5"));
        itemModel05.setQuestionCellSun(map.get("Sun5"));

        itemModel06.setQuestionCellMon(map.get("Mon6"));
        itemModel06.setQuestionCellTue(map.get("Tue6"));
        itemModel06.setQuestionCellWed(map.get("Wed6"));
        itemModel06.setQuestionCellThu(map.get("Thu6"));
        itemModel06.setQuestionCellFri(map.get("Fri6"));
        itemModel06.setQuestionCellSat(map.get("Sat6"));
        itemModel06.setQuestionCellSun(map.get("Sun6"));

        itemModel07.setQuestionCellMon(map.get("Mon7"));
        itemModel07.setQuestionCellTue(map.get("Tue7"));
        itemModel07.setQuestionCellWed(map.get("Wed7"));
        itemModel07.setQuestionCellThu(map.get("Thu7"));
        itemModel07.setQuestionCellFri(map.get("Fri7"));
        itemModel07.setQuestionCellSat(map.get("Sat7"));
        itemModel07.setQuestionCellSun(map.get("Sun7"));

        itemModel08.setQuestionCellMon(map.get("Mon8"));
        itemModel08.setQuestionCellTue(map.get("Tue8"));
        itemModel08.setQuestionCellWed(map.get("Wed8"));
        itemModel08.setQuestionCellThu(map.get("Thu8"));
        itemModel08.setQuestionCellFri(map.get("Fri8"));
        itemModel08.setQuestionCellSat(map.get("Sat8"));
        itemModel08.setQuestionCellSun(map.get("Sun8"));

        itemModel09.setQuestionCellMon(map.get("Mon9"));
        itemModel09.setQuestionCellTue(map.get("Tue9"));
        itemModel09.setQuestionCellWed(map.get("Wed9"));
        itemModel09.setQuestionCellThu(map.get("Thu9"));
        itemModel09.setQuestionCellFri(map.get("Fri9"));
        itemModel09.setQuestionCellSat(map.get("Sat9"));
        itemModel09.setQuestionCellSun(map.get("Sun9"));

        itemModel10.setQuestionCellMon(map.get("Mon10"));
        itemModel10.setQuestionCellTue(map.get("Tue10"));
        itemModel10.setQuestionCellWed(map.get("Wed10"));
        itemModel10.setQuestionCellThu(map.get("Thu10"));
        itemModel10.setQuestionCellFri(map.get("Fri10"));
        itemModel10.setQuestionCellSat(map.get("Sat10"));
        itemModel10.setQuestionCellSun(map.get("Sun10"));

        itemModel11.setQuestionCellMon(map.get("Mon11"));
        itemModel11.setQuestionCellTue(map.get("Tue11"));
        itemModel11.setQuestionCellWed(map.get("Wed11"));
        itemModel11.setQuestionCellThu(map.get("Thu11"));
        itemModel11.setQuestionCellFri(map.get("Fri11"));
        itemModel11.setQuestionCellSat(map.get("Sat11"));
        itemModel11.setQuestionCellSun(map.get("Sun11"));

        itemModel12.setQuestionCellMon(map.get("Mon12"));
        itemModel12.setQuestionCellTue(map.get("Tue12"));
        itemModel12.setQuestionCellWed(map.get("Wed12"));
        itemModel12.setQuestionCellThu(map.get("Thu12"));
        itemModel12.setQuestionCellFri(map.get("Fri12"));
        itemModel12.setQuestionCellSat(map.get("Sat12"));
        itemModel12.setQuestionCellSun(map.get("Sun12"));

        itemModel13.setQuestionCellMon(map.get("Mon13"));
        itemModel13.setQuestionCellTue(map.get("Tue13"));
        itemModel13.setQuestionCellWed(map.get("Wed13"));
        itemModel13.setQuestionCellThu(map.get("Thu13"));
        itemModel13.setQuestionCellFri(map.get("Fri13"));
        itemModel13.setQuestionCellSat(map.get("Sat13"));
        itemModel13.setQuestionCellSun(map.get("Sun13"));

        itemModel14.setQuestionCellMon(map.get("Mon14"));
        itemModel14.setQuestionCellTue(map.get("Tue14"));
        itemModel14.setQuestionCellWed(map.get("Wed14"));
        itemModel14.setQuestionCellThu(map.get("Thu14"));
        itemModel14.setQuestionCellFri(map.get("Fri14"));
        itemModel14.setQuestionCellSat(map.get("Sat14"));
        itemModel14.setQuestionCellSun(map.get("Sun14"));

        itemModel15.setQuestionCellMon(map.get("Mon15"));
        itemModel15.setQuestionCellTue(map.get("Tue15"));
        itemModel15.setQuestionCellWed(map.get("Wed15"));
        itemModel15.setQuestionCellThu(map.get("Thu15"));
        itemModel15.setQuestionCellFri(map.get("Fri15"));
        itemModel15.setQuestionCellSat(map.get("Sat15"));
        itemModel15.setQuestionCellSun(map.get("Sun15"));

        itemModelTotal.setQuestionCellMon(map.get("MonTotal"));
        itemModelTotal.setQuestionCellTue(map.get("TueTotal"));
        itemModelTotal.setQuestionCellWed(map.get("WedTotal"));
        itemModelTotal.setQuestionCellThu(map.get("ThuTotal"));
        itemModelTotal.setQuestionCellFri(map.get("FriTotal"));
        itemModelTotal.setQuestionCellSat(map.get("SatTotal"));
        itemModelTotal.setQuestionCellSun(map.get("SunTotal"));
    }

    private void populateList() {
        itemModel01 = new ListItemPlayerModel();
        itemModel01.setQuestion("Motivation to train");
        itemModels.add(itemModel01);
        itemModel02 = new ListItemPlayerModel();
        itemModel02.setQuestion("Excitement & happiness");
        itemModels.add(itemModel02);
        itemModel03 = new ListItemPlayerModel();
        itemModel03.setQuestion("Feeling of stress");
        itemModels.add(itemModel03);
        itemModel04 = new ListItemPlayerModel();
        itemModel04.setQuestion("Freshness & energized feeling");
        itemModels.add(itemModel04);
        itemModel05 = new ListItemPlayerModel();
        itemModel05.setQuestion("Sleep & rest quality");
        itemModels.add(itemModel05);
        itemModel06 = new ListItemPlayerModel();
        itemModel06.setQuestion("Feeling of recovery");
        itemModels.add(itemModel06);
        itemModel07 = new ListItemPlayerModel();
        itemModel07.setQuestion("Appetite & food intake");
        itemModels.add(itemModel07);
        itemModel08 = new ListItemPlayerModel();
        itemModel08.setQuestion("Explosiveness & speed");
        itemModels.add(itemModel08);
        itemModel09 = new ListItemPlayerModel();
        itemModel09.setQuestion("Endurance");
        itemModels.add(itemModel09);
        itemModel10 = new ListItemPlayerModel();
        itemModel10.setQuestion("Pain in muscles and joints");
        itemModels.add(itemModel10);
        itemModelTotal = new ListItemPlayerModel();
        itemModelTotal.setQuestion("Total");
        itemModels.add(itemModelTotal);
        itemModel11 = new ListItemPlayerModel();
        itemModel11.setQuestion("Weight");
        itemModels.add(itemModel11);
        itemModel12 = new ListItemPlayerModel();
        itemModel12.setQuestion("Resting heart rate");
        itemModels.add(itemModel12);
        itemModel13 = new ListItemPlayerModel();
        itemModel13.setQuestion("Orthostatic pulse");
        itemModels.add(itemModel13);
        itemModel14 = new ListItemPlayerModel();
        itemModel14.setQuestion("Handgrip strength");
        itemModels.add(itemModel14);
        itemModel15 = new ListItemPlayerModel();
        itemModel15.setQuestion("Countermovement jump");
        itemModels.add(itemModel15);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String thisWeek = parent.getItemAtPosition(position).toString();

        db.collection(pathReference + thisWeek + "/Days")
                .orderBy("docNumber")
                .get()
                .addOnCompleteListener(task -> {
                    if ((task.isSuccessful())) {
                        clearItems();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + "==>" + document.getData());

                            if ("MONDAY".equals(document.getId())) {
                                for (int i = 1; i < 16; i++) {
                                    map.put("Mon" + i, String.valueOf(document.get("Question " + i)));
                                }
                                map.put("MonTotal", String.valueOf(document.get("Total score")));
                            }

                            if ("TUESDAY".equals(document.getId())) {
                                for (int i = 1; i < 16; i++) {
                                    map.put("Tue" + i, String.valueOf(document.get("Question " + i)));
                                }
                                map.put("TueTotal", String.valueOf(document.get("Total score")));
                            }

                            if ("WEDNESDAY".equals(document.getId())) {
                                for (int i = 1; i < 16; i++) {
                                    map.put("Wed" + i, String.valueOf(document.get("Question " + i)));
                                }
                                map.put("WedTotal", String.valueOf(document.get("Total score")));
                            }

                            if ("THURSDAY".equals(document.getId())) {
                                for (int i = 1; i < 16; i++) {
                                    map.put("Thu" + i, String.valueOf(document.get("Question " + i)));
                                }
                                map.put("ThuTotal", String.valueOf(document.get("Total score")));
                            }

                            if ("FRIDAY".equals(document.getId())) {
                                for (int i = 1; i < 16; i++) {
                                    map.put("Fri" + i, String.valueOf(document.get("Question " + i)));
                                }
                                map.put("FriTotal", String.valueOf(document.get("Total score")));
                            }

                            if ("SATURDAY".equals(document.getId())) {
                                for (int i = 1; i < 16; i++) {
                                    map.put("Sat" + i, String.valueOf(document.get("Question " + i)));
                                }
                                map.put("SatTotal", String.valueOf(document.get("Total score")));
                            }

                            if ("SUNDAY".equals(document.getId())) {
                                for (int i = 1; i < 16; i++) {
                                    map.put("Sun" + i, String.valueOf(document.get("Question " + i)));
                                }
                                map.put("SunTotal", String.valueOf(document.get("Total score")));
                            }
                            initListData();
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, e.toString()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void clearItems() {
        itemModel01.clearList();
        itemModel02.clearList();
        itemModel03.clearList();
        itemModel04.clearList();
        itemModel05.clearList();
        itemModel06.clearList();
        itemModel07.clearList();
        itemModel08.clearList();
        itemModel09.clearList();
        itemModel10.clearList();
        itemModelTotal.clearList();
        itemModel11.clearList();
        itemModel12.clearList();
        itemModel13.clearList();
        itemModel14.clearList();
        itemModel15.clearList();
    }
}
