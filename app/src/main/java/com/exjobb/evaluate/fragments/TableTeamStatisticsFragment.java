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
import com.exjobb.evaluate.adapter.StatisticsTeamListViewAdapter;
import com.exjobb.evaluate.data.ListItemTeamModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableTeamStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableTeamStatisticsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "TableTeamStatisticsFrag";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;
    private StatisticsTeamListViewAdapter adapter;
    private ListItemTeamModel itemModel01, itemModel02, itemModel03, itemModel04, itemModel05, itemModel06, itemModel07,
            itemModel08, itemModel09, itemModel10, itemModelTotal;
    private Spinner spinner;
    private ImageButton imageButtonNext, imageButtonPrevious;
    private ArrayList<ListItemTeamModel> itemModels;
    private boolean stateMon, stateTue, stateWed, stateThu, stateFri, stateSat, stateSun;
    private String pathReference;
    private Double questionMon01, questionMon02, questionMon03, questionMon04, questionMon05, questionMon06, questionMon07, questionMon08, questionMon09, questionMon10,
            questionTue01, questionTue02, questionTue03, questionTue04, questionTue05, questionTue06, questionTue07, questionTue08, questionTue09, questionTue10,
            questionWed01, questionWed02, questionWed03, questionWed04, questionWed05, questionWed06, questionWed07, questionWed08, questionWed09, questionWed10,
            questionThu01, questionThu02, questionThu03, questionThu04, questionThu05, questionThu06, questionThu07, questionThu08, questionThu09, questionThu10,
            questionFri01, questionFri02, questionFri03, questionFri04, questionFri05, questionFri06, questionFri07, questionFri08, questionFri09, questionFri10,
            questionSat01, questionSat02, questionSat03, questionSat04, questionSat05, questionSat06, questionSat07, questionSat08, questionSat09, questionSat10,
            questionSun01, questionSun02, questionSun03, questionSun04, questionSun05, questionSun06, questionSun07, questionSun08, questionSun09, questionSun10,
            questionMonTotal, questionTueTotal, questionWedTotal, questionThuTotal, questionFriTotal, questionSatTotal, questionSunTotal;

    public TableTeamStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableTeamStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TableTeamStatisticsFragment newInstance(String param1, String param2) {
        TableTeamStatisticsFragment fragment = new TableTeamStatisticsFragment();
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
        return inflater.inflate(R.layout.fragment_table_team_statistics, container, false);
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
        Log.i(TAG, "initVariables: started");
        db = FirebaseFirestore.getInstance();
        imageButtonNext = view.findViewById(R.id.imageButton_Next_Week);
        imageButtonPrevious = view.findViewById(R.id.imageButton_Previous_Week);
        itemModels = new ArrayList<>();
    }

    private void initListTable(View view) {
        Log.i(TAG, "initListTable: started");
        ListView listView = view.findViewById(R.id.listView_Statistic);
        adapter = new StatisticsTeamListViewAdapter(getActivity(), itemModels);
        listView.setAdapter(adapter);
    }

    private void initSpinner(View view) {
        Log.i(TAG, "initSpinner: started");
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
            pathReference = bundle.getString("coachPath") + "/Players/";
            Log.d(TAG, "getDataFromIntent: player: " + pathReference);
        }
    }

    private void populateList() {
        Log.i(TAG, "populateList: started");
        itemModel01 = new ListItemTeamModel();
        itemModel01.setQuestion("Motivation to train");
        itemModels.add(itemModel01);
        itemModel02 = new ListItemTeamModel();
        itemModel02.setQuestion("Excitement & happiness");
        itemModels.add(itemModel02);
        itemModel03 = new ListItemTeamModel();
        itemModel03.setQuestion("Feeling of stress");
        itemModels.add(itemModel03);
        itemModel04 = new ListItemTeamModel();
        itemModel04.setQuestion("Freshness & energized feeling");
        itemModels.add(itemModel04);
        itemModel05 = new ListItemTeamModel();
        itemModel05.setQuestion("Sleep & rest quality");
        itemModels.add(itemModel05);
        itemModel06 = new ListItemTeamModel();
        itemModel06.setQuestion("Feeling of recovery");
        itemModels.add(itemModel06);
        itemModel07 = new ListItemTeamModel();
        itemModel07.setQuestion("Appetite & food intake");
        itemModels.add(itemModel07);
        itemModel08 = new ListItemTeamModel();
        itemModel08.setQuestion("Explosiveness & speed");
        itemModels.add(itemModel08);
        itemModel09 = new ListItemTeamModel();
        itemModel09.setQuestion("Endurance");
        itemModels.add(itemModel09);
        itemModel10 = new ListItemTeamModel();
        itemModel10.setQuestion("Pain in muscles and joints");
        itemModels.add(itemModel10);
        itemModelTotal = new ListItemTeamModel();
        itemModelTotal.setQuestion("Total");
        itemModels.add(itemModelTotal);
    }

    private void setListData() {
        itemModel01.setQuestionCellMon(String.valueOf(questionMon01));
        itemModel01.setQuestionCellTue(String.valueOf(questionTue01));
        itemModel01.setQuestionCellWed(String.valueOf(questionWed01));
        itemModel01.setQuestionCellThu(String.valueOf(questionThu01));
        itemModel01.setQuestionCellFri(String.valueOf(questionFri01));
        itemModel01.setQuestionCellSat(String.valueOf(questionSat01));
        itemModel01.setQuestionCellSun(String.valueOf(questionSun01));

        itemModel02.setQuestionCellMon(String.valueOf(questionMon02));
        itemModel02.setQuestionCellTue(String.valueOf(questionTue02));
        itemModel02.setQuestionCellWed(String.valueOf(questionWed02));
        itemModel02.setQuestionCellThu(String.valueOf(questionThu02));
        itemModel02.setQuestionCellFri(String.valueOf(questionFri02));
        itemModel02.setQuestionCellSat(String.valueOf(questionSat02));
        itemModel02.setQuestionCellSun(String.valueOf(questionSun02));

        itemModel03.setQuestionCellMon(String.valueOf(questionMon03));
        itemModel03.setQuestionCellTue(String.valueOf(questionTue03));
        itemModel03.setQuestionCellWed(String.valueOf(questionWed03));
        itemModel03.setQuestionCellThu(String.valueOf(questionThu03));
        itemModel03.setQuestionCellFri(String.valueOf(questionFri03));
        itemModel03.setQuestionCellSat(String.valueOf(questionSat03));
        itemModel03.setQuestionCellSun(String.valueOf(questionSun03));

        itemModel04.setQuestionCellMon(String.valueOf(questionMon04));
        itemModel04.setQuestionCellTue(String.valueOf(questionTue04));
        itemModel04.setQuestionCellWed(String.valueOf(questionWed04));
        itemModel04.setQuestionCellThu(String.valueOf(questionThu04));
        itemModel04.setQuestionCellFri(String.valueOf(questionFri04));
        itemModel04.setQuestionCellSat(String.valueOf(questionSat04));
        itemModel04.setQuestionCellSun(String.valueOf(questionSun04));

        itemModel05.setQuestionCellMon(String.valueOf(questionMon05));
        itemModel05.setQuestionCellTue(String.valueOf(questionTue05));
        itemModel05.setQuestionCellWed(String.valueOf(questionWed05));
        itemModel05.setQuestionCellThu(String.valueOf(questionThu05));
        itemModel05.setQuestionCellFri(String.valueOf(questionFri05));
        itemModel05.setQuestionCellSat(String.valueOf(questionSat05));
        itemModel05.setQuestionCellSun(String.valueOf(questionSun05));

        itemModel06.setQuestionCellMon(String.valueOf(questionMon06));
        itemModel06.setQuestionCellTue(String.valueOf(questionTue06));
        itemModel06.setQuestionCellWed(String.valueOf(questionWed06));
        itemModel06.setQuestionCellThu(String.valueOf(questionThu06));
        itemModel06.setQuestionCellFri(String.valueOf(questionFri06));
        itemModel06.setQuestionCellSat(String.valueOf(questionSat06));
        itemModel06.setQuestionCellSun(String.valueOf(questionSun06));

        itemModel07.setQuestionCellMon(String.valueOf(questionMon07));
        itemModel07.setQuestionCellTue(String.valueOf(questionTue07));
        itemModel07.setQuestionCellWed(String.valueOf(questionWed07));
        itemModel07.setQuestionCellThu(String.valueOf(questionThu07));
        itemModel07.setQuestionCellFri(String.valueOf(questionFri07));
        itemModel07.setQuestionCellSat(String.valueOf(questionSat07));
        itemModel07.setQuestionCellSun(String.valueOf(questionSun07));

        itemModel08.setQuestionCellMon(String.valueOf(questionMon08));
        itemModel08.setQuestionCellTue(String.valueOf(questionTue08));
        itemModel08.setQuestionCellWed(String.valueOf(questionWed08));
        itemModel08.setQuestionCellThu(String.valueOf(questionThu08));
        itemModel08.setQuestionCellFri(String.valueOf(questionFri08));
        itemModel08.setQuestionCellSat(String.valueOf(questionSat08));
        itemModel08.setQuestionCellSun(String.valueOf(questionSun08));

        itemModel09.setQuestionCellMon(String.valueOf(questionMon09));
        itemModel09.setQuestionCellTue(String.valueOf(questionTue09));
        itemModel09.setQuestionCellWed(String.valueOf(questionWed09));
        itemModel09.setQuestionCellThu(String.valueOf(questionThu09));
        itemModel09.setQuestionCellFri(String.valueOf(questionFri09));
        itemModel09.setQuestionCellSat(String.valueOf(questionSat09));
        itemModel09.setQuestionCellSun(String.valueOf(questionSun09));

        itemModel10.setQuestionCellMon(String.valueOf(questionMon10));
        itemModel10.setQuestionCellTue(String.valueOf(questionTue10));
        itemModel10.setQuestionCellWed(String.valueOf(questionWed10));
        itemModel10.setQuestionCellThu(String.valueOf(questionThu10));
        itemModel10.setQuestionCellFri(String.valueOf(questionFri10));
        itemModel10.setQuestionCellSat(String.valueOf(questionSat10));
        itemModel10.setQuestionCellSun(String.valueOf(questionSun10));

        itemModelTotal.setQuestionCellMon(String.valueOf(questionMonTotal));
        itemModelTotal.setQuestionCellTue(String.valueOf(questionTueTotal));
        itemModelTotal.setQuestionCellWed(String.valueOf(questionWedTotal));
        itemModelTotal.setQuestionCellThu(String.valueOf(questionThuTotal));
        itemModelTotal.setQuestionCellFri(String.valueOf(questionFriTotal));
        itemModelTotal.setQuestionCellSat(String.valueOf(questionSatTotal));
        itemModelTotal.setQuestionCellSun(String.valueOf(questionSunTotal));
    }

    private void initListData(ArrayList<QueryDocumentSnapshot> docListMon, ArrayList<QueryDocumentSnapshot> docListTue, ArrayList<QueryDocumentSnapshot> docListWed,
                              ArrayList<QueryDocumentSnapshot> docListThu, ArrayList<QueryDocumentSnapshot> docListFri, ArrayList<QueryDocumentSnapshot> docListSat,
                              ArrayList<QueryDocumentSnapshot> docListSun) {
        Log.i(TAG, "initListData: started");
        if (stateMon) {
            questionMon01 = questionMon01 / docListMon.size();
            questionMon02 = questionMon02 / docListMon.size();
            questionMon03 = questionMon03 / docListMon.size();
            questionMon04 = questionMon04 / docListMon.size();
            questionMon05 = questionMon05 / docListMon.size();
            questionMon06 = questionMon06 / docListMon.size();
            questionMon07 = questionMon07 / docListMon.size();
            questionMon08 = questionMon08 / docListMon.size();
            questionMon09 = questionMon09 / docListMon.size();
            questionMon10 = questionMon10 / docListMon.size();
            questionMonTotal = questionMonTotal / docListMon.size();
        }

        if (stateTue) {
            questionTue01 = questionTue01 / docListTue.size();
            questionTue02 = questionTue02 / docListTue.size();
            questionTue03 = questionTue03 / docListTue.size();
            questionTue04 = questionTue04 / docListTue.size();
            questionTue05 = questionTue05 / docListTue.size();
            questionTue06 = questionTue06 / docListTue.size();
            questionTue07 = questionTue07 / docListTue.size();
            questionTue08 = questionTue08 / docListTue.size();
            questionTue09 = questionTue09 / docListTue.size();
            questionTue10 = questionTue10 / docListTue.size();
            questionTueTotal = questionTueTotal / docListTue.size();
        }

        if (stateWed) {
            questionWed01 = questionWed01 / docListWed.size();
            questionWed02 = questionWed02 / docListWed.size();
            questionWed03 = questionWed03 / docListWed.size();
            questionWed04 = questionWed04 / docListWed.size();
            questionWed05 = questionWed05 / docListWed.size();
            questionWed06 = questionWed06 / docListWed.size();
            questionWed07 = questionWed07 / docListWed.size();
            questionWed08 = questionWed08 / docListWed.size();
            questionWed09 = questionWed09 / docListWed.size();
            questionWed10 = questionWed10 / docListWed.size();
            questionWedTotal = questionWedTotal / docListWed.size();
        }

        if (stateThu) {
            questionThu01 = questionThu01 / docListThu.size();
            questionThu02 = questionThu02 / docListThu.size();
            questionThu03 = questionThu03 / docListThu.size();
            questionThu04 = questionThu04 / docListThu.size();
            questionThu05 = questionThu05 / docListThu.size();
            questionThu06 = questionThu06 / docListThu.size();
            questionThu07 = questionThu07 / docListThu.size();
            questionThu08 = questionThu08 / docListThu.size();
            questionThu09 = questionThu09 / docListThu.size();
            questionThu10 = questionThu10 / docListThu.size();
            questionThuTotal = questionThuTotal / docListThu.size();
        }

        if (stateFri) {
            questionFri01 = questionFri01 / docListFri.size();
            questionFri02 = questionFri02 / docListFri.size();
            questionFri03 = questionFri03 / docListFri.size();
            questionFri04 = questionFri04 / docListFri.size();
            questionFri05 = questionFri05 / docListFri.size();
            questionFri06 = questionFri06 / docListFri.size();
            questionFri07 = questionFri07 / docListFri.size();
            questionFri08 = questionFri08 / docListFri.size();
            questionFri09 = questionFri09 / docListFri.size();
            questionFri10 = questionFri10 / docListFri.size();
            questionFriTotal = questionFriTotal / docListFri.size();
        }

        if (stateSat) {
            questionSat01 = questionSat01 / docListSat.size();
            questionSat02 = questionSat02 / docListSat.size();
            questionSat03 = questionSat03 / docListSat.size();
            questionSat04 = questionSat04 / docListSat.size();
            questionSat05 = questionSat05 / docListSat.size();
            questionSat06 = questionSat06 / docListSat.size();
            questionSat07 = questionSat07 / docListSat.size();
            questionSat08 = questionSat08 / docListSat.size();
            questionSat09 = questionSat09 / docListSat.size();
            questionSat10 = questionSat10 / docListSat.size();
            questionSatTotal = questionSatTotal / docListSat.size();
        }

        if (stateSun) {
            questionSun01 = questionSun01 / docListSun.size();
            Log.d(TAG, "initListData: " + questionSun01);
            questionSun02 = questionSun02 / docListSun.size();
            questionSun03 = questionSun03 / docListSun.size();
            questionSun04 = questionSun04 / docListSun.size();
            questionSun05 = questionSun05 / docListSun.size();
            questionSun06 = questionSun06 / docListSun.size();
            questionSun07 = questionSun07 / docListSun.size();
            questionSun08 = questionSun08 / docListSun.size();
            questionSun09 = questionSun09 / docListSun.size();
            questionSun10 = questionSun10 / docListSun.size();
            questionSunTotal = questionSunTotal / docListSun.size();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String coachId = FirebaseFirestore.getInstance().collection(pathReference).getParent().getId();
        String thisWeek = parent.getItemAtPosition(position).toString();
        ArrayList<QueryDocumentSnapshot> docListMon = new ArrayList<>();
        ArrayList<QueryDocumentSnapshot> docListTue = new ArrayList<>();
        ArrayList<QueryDocumentSnapshot> docListWed = new ArrayList<>();
        ArrayList<QueryDocumentSnapshot> docListThu = new ArrayList<>();
        ArrayList<QueryDocumentSnapshot> docListFri = new ArrayList<>();
        ArrayList<QueryDocumentSnapshot> docListSat = new ArrayList<>();
        ArrayList<QueryDocumentSnapshot> docListSun = new ArrayList<>();

        db.collectionGroup("Days")
//                .orderBy("docNumber", Query.Direction.DESCENDING)
                .whereEqualTo("coachId", coachId)
                .whereEqualTo("week", thisWeek)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        clearVariables();
                        clearItems();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + "=>" + document.getData());

                            if ("MONDAY".equals(document.getId())) {
                                docListMon.add(document);
                                questionMon01 += document.getDouble("Question 1");
                                questionMon02 += document.getDouble("Question 2");
                                questionMon03 += document.getDouble("Question 3");
                                questionMon04 += document.getDouble("Question 4");
                                questionMon05 += document.getDouble("Question 5");
                                questionMon06 += document.getDouble("Question 6");
                                questionMon07 += document.getDouble("Question 7");
                                questionMon08 += document.getDouble("Question 8");
                                questionMon09 += document.getDouble("Question 9");
                                questionMon10 += document.getDouble("Question 10");
                                questionMonTotal += document.getDouble("Total score");
                                stateMon = true;
                            }

                            if ("TUESDAY".equals(document.getId())) {
                                docListTue.add(document);
                                questionTue01 += document.getDouble("Question 1");
                                questionTue02 += document.getDouble("Question 2");
                                questionTue03 += document.getDouble("Question 3");
                                questionTue04 += document.getDouble("Question 4");
                                questionTue05 += document.getDouble("Question 5");
                                questionTue06 += document.getDouble("Question 6");
                                questionTue07 += document.getDouble("Question 7");
                                questionTue08 += document.getDouble("Question 8");
                                questionTue09 += document.getDouble("Question 9");
                                questionTue10 += document.getDouble("Question 10");
                                questionTueTotal += document.getDouble("Total score");
                                stateTue = true;
                            }

                            if ("WEDNESDAY".equals(document.getId())) {
                                docListWed.add(document);
                                questionWed01 += document.getDouble("Question 1");
                                questionWed02 += document.getDouble("Question 2");
                                questionWed03 += document.getDouble("Question 3");
                                questionWed04 += document.getDouble("Question 4");
                                questionWed05 += document.getDouble("Question 5");
                                questionWed06 += document.getDouble("Question 6");
                                questionWed07 += document.getDouble("Question 7");
                                questionWed08 += document.getDouble("Question 8");
                                questionWed09 += document.getDouble("Question 9");
                                questionWed10 += document.getDouble("Question 10");
                                questionWedTotal += document.getDouble("Total score");
                                stateWed = true;
                            }

                            if ("THURSDAY".equals(document.getId())) {
                                docListThu.add(document);
                                questionThu01 += document.getDouble("Question 1");
                                questionThu02 += document.getDouble("Question 2");
                                questionThu03 += document.getDouble("Question 3");
                                questionThu04 += document.getDouble("Question 4");
                                questionThu05 += document.getDouble("Question 5");
                                questionThu06 += document.getDouble("Question 6");
                                questionThu07 += document.getDouble("Question 7");
                                questionThu08 += document.getDouble("Question 8");
                                questionThu09 += document.getDouble("Question 9");
                                questionThu10 += document.getDouble("Question 10");
                                questionThuTotal += document.getDouble("Total score");
                                stateThu = true;
                            }

                            if ("FRIDAY".equals(document.getId())) {
                                docListFri.add(document);
                                questionFri01 += document.getDouble("Question 1");
                                questionFri02 += document.getDouble("Question 2");
                                questionFri03 += document.getDouble("Question 3");
                                questionFri04 += document.getDouble("Question 4");
                                questionFri05 += document.getDouble("Question 5");
                                questionFri06 += document.getDouble("Question 6");
                                questionFri07 += document.getDouble("Question 7");
                                questionFri08 += document.getDouble("Question 8");
                                questionFri09 += document.getDouble("Question 9");
                                questionFri10 += document.getDouble("Question 10");
                                questionFriTotal += document.getDouble("Total score");
                                stateFri = true;
                            }

                            if ("SATURDAY".equals(document.getId())) {
                                docListSat.add(document);
                                questionSat01 += document.getDouble("Question 1");
                                questionSat02 += document.getDouble("Question 2");
                                questionSat03 += document.getDouble("Question 3");
                                questionSat04 += document.getDouble("Question 4");
                                questionSat05 += document.getDouble("Question 5");
                                questionSat06 += document.getDouble("Question 6");
                                questionSat07 += document.getDouble("Question 7");
                                questionSat08 += document.getDouble("Question 8");
                                questionSat09 += document.getDouble("Question 9");
                                questionSat10 += document.getDouble("Question 10");
                                questionSatTotal += document.getDouble("Total score");
                                stateSat = true;
                            }

                            if ("SUNDAY".equals(document.getId())) {
                                docListSun.add(document);
                                questionSun01 += document.getDouble("Question 1");
                                Log.d(TAG, "onItemSelected: " + questionSat01);
                                questionSun02 += document.getDouble("Question 2");
                                questionSun03 += document.getDouble("Question 3");
                                questionSun04 += document.getDouble("Question 4");
                                questionSun05 += document.getDouble("Question 5");
                                questionSun06 += document.getDouble("Question 6");
                                questionSun07 += document.getDouble("Question 7");
                                questionSun08 += document.getDouble("Question 8");
                                questionSun09 += document.getDouble("Question 9");
                                questionSun10 += document.getDouble("Question 10");
                                questionSunTotal += document.getDouble("Total score");
                                stateSun = true;
                            }
                        }
                        initListData(docListMon, docListTue, docListWed, docListThu, docListFri,
                                docListSat, docListSun);
                        setListData();
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
    }

    private void clearVariables() {
        Log.i(TAG, "clearVariables: started");
        stateMon = false;
        stateTue = false;
        stateWed = false;
        stateThu = false;
        stateFri = false;
        stateSat = false;
        stateSun = false;

        questionMon01 = 0.0;
        questionMon02 = 0.0;
        questionMon03 = 0.0;
        questionMon04 = 0.0;
        questionMon05 = 0.0;
        questionMon06 = 0.0;
        questionMon07 = 0.0;
        questionMon08 = 0.0;
        questionMon09 = 0.0;
        questionMon10 = 0.0;
        questionMonTotal = 0.0;

        questionTue01 = 0.0;
        questionTue02 = 0.0;
        questionTue03 = 0.0;
        questionTue04 = 0.0;
        questionTue05 = 0.0;
        questionTue06 = 0.0;
        questionTue07 = 0.0;
        questionTue08 = 0.0;
        questionTue09 = 0.0;
        questionTue10 = 0.0;
        questionTueTotal = 0.0;

        questionWed01 = 0.0;
        questionWed02 = 0.0;
        questionWed03 = 0.0;
        questionWed04 = 0.0;
        questionWed05 = 0.0;
        questionWed06 = 0.0;
        questionWed07 = 0.0;
        questionWed08 = 0.0;
        questionWed09 = 0.0;
        questionWed10 = 0.0;
        questionWedTotal = 0.0;

        questionThu01 = 0.0;
        questionThu02 = 0.0;
        questionThu03 = 0.0;
        questionThu04 = 0.0;
        questionThu05 = 0.0;
        questionThu06 = 0.0;
        questionThu07 = 0.0;
        questionThu08 = 0.0;
        questionThu09 = 0.0;
        questionThu10 = 0.0;
        questionThuTotal = 0.0;

        questionFri01 = 0.0;
        questionFri02 = 0.0;
        questionFri03 = 0.0;
        questionFri04 = 0.0;
        questionFri05 = 0.0;
        questionFri06 = 0.0;
        questionFri07 = 0.0;
        questionFri08 = 0.0;
        questionFri09 = 0.0;
        questionFri10 = 0.0;
        questionFriTotal = 0.0;

        questionSat01 = 0.0;
        questionSat02 = 0.0;
        questionSat03 = 0.0;
        questionSat04 = 0.0;
        questionSat05 = 0.0;
        questionSat06 = 0.0;
        questionSat07 = 0.0;
        questionSat08 = 0.0;
        questionSat09 = 0.0;
        questionSat10 = 0.0;
        questionSatTotal = 0.0;

        questionSun01 = 0.0;
        questionSun02 = 0.0;
        questionSun03 = 0.0;
        questionSun04 = 0.0;
        questionSun05 = 0.0;
        questionSun06 = 0.0;
        questionSun07 = 0.0;
        questionSun08 = 0.0;
        questionSun09 = 0.0;
        questionSun10 = 0.0;
        questionSunTotal = 0.0;
    }
}
