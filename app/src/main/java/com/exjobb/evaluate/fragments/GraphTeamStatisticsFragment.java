package com.exjobb.evaluate.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exjobb.evaluate.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphTeamStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphTeamStatisticsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "GraphTeamStatisticsFrag";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;
    private BarChart barChart;
    private Spinner spinner;
    private ImageButton imageButtonNext, imageButtonPrevious;
    private BarDataSet dataSetSection1, dataSetSection2, dataSetSection3, dataSetSection4;
    private XAxis xAxis;
    private ArrayList<BarEntry> entryListSection1;
    private ArrayList<BarEntry> entryListSection2;
    private ArrayList<BarEntry> entryListSection3;
    private ArrayList<BarEntry> entryListSection4;
    private ArrayList<String> xAxisLabels;
    private boolean stateMon, stateTue, stateWed, stateThu, stateFri, stateSat, stateSun;
    private String pathReference;
    private int count;
    private float index, section01Mon, section02Mon, section03Mon, section04Mon, section01Tue, section02Tue, section03Tue, section04Tue,
            section01Wed, section02Wed, section03Wed, section04Wed, section01Thu, section02Thu, section03Thu, section04Thu,
            section01Fri, section02Fri, section03Fri, section04Fri, section01Sat, section02Sat, section03Sat, section04Sat,
            section01Sun, section02Sun, section03Sun, section04Sun;

    public GraphTeamStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphTeamStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphTeamStatisticsFragment newInstance(String param1, String param2) {
        GraphTeamStatisticsFragment fragment = new GraphTeamStatisticsFragment();
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
        return inflater.inflate(R.layout.fragment_graph_team_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVariables(view);
        initSpinner(view);
        getDataFromIntent();

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

        styleBar();
    }

    private void initVariables(View view) {
        db = FirebaseFirestore.getInstance();
        imageButtonNext = view.findViewById(R.id.imageButton_Next_Week);
        imageButtonPrevious = view.findViewById(R.id.imageButton_Previous_Week);
        barChart = view.findViewById(R.id.barChartStatistics);
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
            pathReference = bundle.getString("coachPath") + "/Players/";
            Log.d(TAG, "getDataFromIntent: player: " + pathReference);
        }
    }

    private void styleBar() {
        barChart.setFitBars(true);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setHighlightPerDragEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setAutoScaleMinMaxEnabled(true);

        Description description = barChart.getDescription();
        description.setEnabled(false);
        //description.setText("Average value for each section");

        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.resetAxisMinimum();
        xAxis.resetAxisMaximum();
    }

    private void initData(ArrayList<QueryDocumentSnapshot> docListMon, ArrayList<QueryDocumentSnapshot> docListTue, ArrayList<QueryDocumentSnapshot> docListWed,
                          ArrayList<QueryDocumentSnapshot> docListThu, ArrayList<QueryDocumentSnapshot> docListFri, ArrayList<QueryDocumentSnapshot> docListSat,
                          ArrayList<QueryDocumentSnapshot> docListSun) {

        section01Mon = section01Mon / docListMon.size();
        section02Mon = section02Mon / docListMon.size();
        section03Mon = section03Mon / docListMon.size();
        section04Mon = section04Mon / docListMon.size();

        if (stateMon) {
            index++;
            entryListSection1.add(new BarEntry(index, section01Mon));
            entryListSection2.add(new BarEntry(index, section02Mon));
            entryListSection3.add(new BarEntry(index, section03Mon));
            entryListSection4.add(new BarEntry(index, section04Mon));
            xAxisLabels.add("Mon");
            count++;
        }

        section01Tue = section01Tue / docListTue.size();
        section02Tue = section02Tue / docListTue.size();
        section03Tue = section03Tue / docListTue.size();
        section04Tue = section04Tue / docListTue.size();

        if (stateTue) {
            index++;
            entryListSection1.add(new BarEntry(index, section01Tue));
            entryListSection2.add(new BarEntry(index, section02Tue));
            entryListSection3.add(new BarEntry(index, section03Tue));
            entryListSection4.add(new BarEntry(index, section04Tue));
            xAxisLabels.add("Tue");
            count++;
        }

        section01Wed = section01Wed / docListWed.size();
        section02Wed = section02Wed / docListWed.size();
        section03Wed = section03Wed / docListWed.size();
        section04Wed = section04Wed / docListWed.size();

        if (stateWed) {
            index++;
            entryListSection1.add(new BarEntry(index, section01Wed));
            entryListSection2.add(new BarEntry(index, section02Wed));
            entryListSection3.add(new BarEntry(index, section03Wed));
            entryListSection4.add(new BarEntry(index, section04Wed));
            xAxisLabels.add("Wed");
            count++;
        }

        section01Thu = section01Thu / docListThu.size();
        section02Thu = section02Thu / docListThu.size();
        section03Thu = section03Thu / docListThu.size();
        section04Thu = section04Thu / docListThu.size();

        if (stateThu) {
            index++;
            entryListSection1.add(new BarEntry(index, section01Thu));
            entryListSection2.add(new BarEntry(index, section02Thu));
            entryListSection3.add(new BarEntry(index, section03Thu));
            entryListSection4.add(new BarEntry(index, section04Thu));
            xAxisLabels.add("Thu");
            count++;
        }

        section01Fri = section01Fri / docListFri.size();
        section02Fri = section02Fri / docListFri.size();
        section03Fri = section03Fri / docListFri.size();
        section04Fri = section04Fri / docListFri.size();

        if (stateFri) {
            index++;
            entryListSection1.add(new BarEntry(index, section01Fri));
            entryListSection2.add(new BarEntry(index, section02Fri));
            entryListSection3.add(new BarEntry(index, section03Fri));
            entryListSection4.add(new BarEntry(index, section04Fri));
            xAxisLabels.add("Fri");
            count++;
        }

        section01Sat = section01Sat / docListSat.size();
        section02Sat = section02Sat / docListSat.size();
        section03Sat = section03Sat / docListSat.size();
        section04Sat = section04Sat / docListSat.size();

        if (stateSat) {
            index++;
            entryListSection1.add(new BarEntry(index, section01Sat));
            entryListSection2.add(new BarEntry(index, section02Sat));
            entryListSection3.add(new BarEntry(index, section03Sat));
            entryListSection4.add(new BarEntry(index, section04Sat));
            xAxisLabels.add("Sat");
            count++;
        }

        section01Sun = section01Sun / docListSun.size();
        Log.d(TAG, "initData: " + section01Sun);
        section02Sun = section02Sun / docListSun.size();
        Log.d(TAG, "initData: " + section02Sun);
        section03Sun = section03Sun / docListSun.size();
        Log.d(TAG, "initData: " + section03Sun);
        section04Sun = section04Sun / docListSun.size();
        Log.d(TAG, "initData: " + section04Sun);

        if (stateSun) {
            index++;
            entryListSection1.add(new BarEntry(index, section01Sun));
            Log.d(TAG, "onItemSelected: " + entryListSection1);
            entryListSection2.add(new BarEntry(index, section02Sun));
            entryListSection3.add(new BarEntry(index, section03Sun));
            entryListSection4.add(new BarEntry(index, section04Sun));
            xAxisLabels.add("Sun");
            count++;
        }

        dataSetSection1 = new BarDataSet(entryListSection1, "Psychological");
        dataSetSection1.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetSection1.setValueTextSize(10f);
        dataSetSection1.setColor(Color.BLUE);
        dataSetSection2 = new BarDataSet(entryListSection2, "Psychophysiological");
        dataSetSection2.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetSection2.setValueTextSize(10f);
        dataSetSection2.setColor(Color.RED);
        dataSetSection3 = new BarDataSet(entryListSection3, "Physiological");
        dataSetSection3.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetSection3.setValueTextSize(10f);
        dataSetSection3.setColor(Color.GREEN);
        dataSetSection4 = new BarDataSet(entryListSection4, "Physio-medical");
        dataSetSection4.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetSection4.setValueTextSize(10f);
        dataSetSection4.setColor(Color.YELLOW);
    }

    private void configureAndSetBars() {
        float barSpace = 0.02f;
        float groupSpace = 0.3f;

        BarData barData = new BarData(dataSetSection1, dataSetSection2, dataSetSection3, dataSetSection4);
        barData.setBarWidth(0.15f);
        barChart.setData(barData);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * count);
        barChart.groupBars(0, groupSpace, barSpace);
    }

    private void clearChartManually() {
/*
        YAxis yAxisLeft, yAxisRight;
        yAxisLeft = barChart.getAxisLeft();
        yAxisRight = barChart.getAxisRight();
*/

        barChart.fitScreen();
        if (barChart.getBarData() != null) {
            barChart.getBarData().clearValues();
        }
        barChart.clear();
        barChart.invalidate();

/*
        barChart.getDescription().setEnabled(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);
        barChart.setNoDataText("No charting data available");
        barChart.setNoDataTextColor(Color.YELLOW);
*/
    }

    private void clearVariables() {

        entryListSection1 = new ArrayList<>();
        entryListSection2 = new ArrayList<>();
        entryListSection3 = new ArrayList<>();
        entryListSection4 = new ArrayList<>();
        xAxisLabels = new ArrayList<>();

        count = 0;
        index = -1f;

        stateMon = false;
        stateTue = false;
        stateWed = false;
        stateThu = false;
        stateFri = false;
        stateSat = false;
        stateSun = false;

        section01Mon = 0.0f;
        section02Mon = 0.0f;
        section03Mon = 0.0f;
        section04Mon = 0.0f;

        section01Tue = 0.0f;
        section02Tue = 0.0f;
        section03Tue = 0.0f;
        section04Tue = 0.0f;

        section01Wed = 0.0f;
        section02Wed = 0.0f;
        section03Wed = 0.0f;
        section04Wed = 0.0f;

        section01Thu = 0.0f;
        section02Thu = 0.0f;
        section03Thu = 0.0f;
        section04Thu = 0.0f;

        section01Fri = 0.0f;
        section02Fri = 0.0f;
        section03Fri = 0.0f;
        section04Fri = 0.0f;

        section01Sat = 0.0f;
        section02Sat = 0.0f;
        section03Sat = 0.0f;
        section04Sat = 0.0f;

        section01Sun = 0.0f;
        section02Sun = 0.0f;
        section03Sun = 0.0f;
        section04Sun = 0.0f;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String coachId = FirebaseFirestore.getInstance().collection(pathReference).getParent().getId();
        Log.d(TAG, "onItemSelected: " + coachId);
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
                        clearChartManually();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + "=>" + document.getData());

                            if ("MONDAY".equals(document.getId())) {
                                docListMon.add(document);
                                section01Mon += document.getDouble("Section 1");
                                section02Mon += document.getDouble("Section 2");
                                section03Mon += document.getDouble("Section 3");
                                section04Mon += document.getDouble("Section 4");
                                stateMon = true;
                            }

                            if ("TUESDAY".equals(document.getId())) {
                                docListTue.add(document);
                                section01Tue += document.getDouble("Section 1");
                                section02Tue += document.getDouble("Section 2");
                                section03Tue += document.getDouble("Section 3");
                                section04Tue += document.getDouble("Section 4");
                                stateTue = true;
                            }

                            if ("WEDNESDAY".equals(document.getId())) {
                                docListWed.add(document);
                                section01Wed += document.getDouble("Section 1");
                                section02Wed += document.getDouble("Section 2");
                                section03Wed += document.getDouble("Section 3");
                                section04Wed += document.getDouble("Section 4");
                                stateWed = true;
                            }

                            if ("THURSDAY".equals(document.getId())) {
                                docListThu.add(document);
                                section01Thu += document.getDouble("Section 1");
                                section02Thu += document.getDouble("Section 2");
                                section03Thu += document.getDouble("Section 3");
                                section04Thu += document.getDouble("Section 4");
                                stateThu = true;
                            }

                            if ("FRIDAY".equals(document.getId())) {
                                docListFri.add(document);
                                section01Fri += document.getDouble("Section 1");
                                section02Fri += document.getDouble("Section 2");
                                section03Fri += document.getDouble("Section 3");
                                section04Fri += document.getDouble("Section 4");
                                stateFri = true;
                            }

                            if ("SATURDAY".equals(document.getId())) {
                                docListSat.add(document);
                                section01Sat += document.getDouble("Section 1");
                                section02Sat += document.getDouble("Section 2");
                                section03Sat += document.getDouble("Section 3");
                                section04Sat += document.getDouble("Section 4");
                                stateSat = true;
                            }

                            if ("SUNDAY".equals(document.getId())) {
                                docListSun.add(document);
                                Log.d(TAG, "onItemSelected: " + docListSun);
                                section01Sun += document.getDouble("Section 1");
                                Log.d(TAG, "onItemSelected: " + section01Sun);
                                section02Sun += document.getDouble("Section 2");
                                Log.d(TAG, "onItemSelected: " + section02Sun);
                                section03Sun += document.getDouble("Section 3");
                                Log.d(TAG, "onItemSelected: " + section03Sun);
                                section04Sun += document.getDouble("Section 4");
                                Log.d(TAG, "onItemSelected: " + section04Sun);
                                stateSun = true;
                            }
                        }
                        initData(docListMon, docListTue, docListWed, docListThu, docListFri,
                                docListSat, docListSun);
                        configureAndSetBars();
                        barChart.invalidate();
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, e.toString()));
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
