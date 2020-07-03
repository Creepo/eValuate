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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphPlayerStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphPlayerStatisticsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "GraphPlayerStatistics";
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
    private BarData barData;
    private BarDataSet dataSetSection1, dataSetSection2, dataSetSection3, dataSetSection4;
    private XAxis xAxis;
    private ArrayList<String> xAxisLabels;
    private String pathReference;
    private int count;

    public GraphPlayerStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphPlayerStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphPlayerStatisticsFragment newInstance(String param1, String param2) {
        GraphPlayerStatisticsFragment fragment = new GraphPlayerStatisticsFragment();
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
        return inflater.inflate(R.layout.fragment_graph_player_statistics, container, false);
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
        barData = new BarData();
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

    private void styleBar() {
        barChart.setFitBars(true);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setHighlightPerDragEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setAutoScaleMinMaxEnabled(true);

        Description description = barChart.getDescription();
        description.setEnabled(false);

        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.resetAxisMinimum();
        xAxis.resetAxisMaximum();
    }

    private void configureAndSetBars() {
        float barSpace = 0.02f;
        float groupSpace = 0.3f;
        barData = new BarData(dataSetSection1, dataSetSection2, dataSetSection3, dataSetSection4);
        barData.setBarWidth(0.15f);
        barChart.setData(barData);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * count);
        barChart.groupBars(0, groupSpace, barSpace);
    }

    private void clearChart() {
        barChart.fitScreen();
        xAxisLabels = new ArrayList<>();
        if (barChart.getBarData() != null) {
            barChart.getBarData().clearValues();
        }
        barChart.clear();
        barChart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String thisWeek = parent.getItemAtPosition(position).toString();
        ArrayList<BarEntry> entryListSection1 = new ArrayList<>();
        ArrayList<BarEntry> entryListSection2 = new ArrayList<>();
        ArrayList<BarEntry> entryListSection3 = new ArrayList<>();
        ArrayList<BarEntry> entryListSection4 = new ArrayList<>();

        db.collection(pathReference + thisWeek + "/Days")
                .orderBy("docNumber")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        count = 0;
                        clearChart();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + "=>" + document.getData());

                            if ("MONDAY".equals(document.getId())) {
                                entryListSection1.add(new BarEntry(0f, Float.parseFloat(String.valueOf(document.get("Section 1")))));
                                entryListSection2.add(new BarEntry(0f, Float.parseFloat(String.valueOf(document.get("Section 2")))));
                                entryListSection3.add(new BarEntry(0f, Float.parseFloat(String.valueOf(document.get("Section 3")))));
                                entryListSection4.add(new BarEntry(0f, Float.parseFloat(String.valueOf(document.get("Section 4")))));
                                xAxisLabels.add("Mon");
                                count++;
                            }

                            if ("TUESDAY".equals(document.getId())) {
                                entryListSection1.add(new BarEntry(1f, Float.parseFloat(String.valueOf(document.get("Section 1")))));
                                entryListSection2.add(new BarEntry(1f, Float.parseFloat(String.valueOf(document.get("Section 2")))));
                                entryListSection3.add(new BarEntry(1f, Float.parseFloat(String.valueOf(document.get("Section 3")))));
                                entryListSection4.add(new BarEntry(1f, Float.parseFloat(String.valueOf(document.get("Section 4")))));
                                xAxisLabels.add("Tue");
                                count++;
                            }

                            if ("WEDNESDAY".equals(document.getId())) {
                                entryListSection1.add(new BarEntry(2f, Float.parseFloat(String.valueOf(document.get("Section 1")))));
                                entryListSection2.add(new BarEntry(2f, Float.parseFloat(String.valueOf(document.get("Section 2")))));
                                entryListSection3.add(new BarEntry(2f, Float.parseFloat(String.valueOf(document.get("Section 3")))));
                                entryListSection4.add(new BarEntry(2f, Float.parseFloat(String.valueOf(document.get("Section 4")))));
                                xAxisLabels.add("Wed");
                                count++;
                            }

                            if ("THURSDAY".equals(document.getId())) {
                                entryListSection1.add(new BarEntry(3f, Float.parseFloat(String.valueOf(document.get("Section 1")))));
                                entryListSection2.add(new BarEntry(3f, Float.parseFloat(String.valueOf(document.get("Section 2")))));
                                entryListSection3.add(new BarEntry(3f, Float.parseFloat(String.valueOf(document.get("Section 3")))));
                                entryListSection4.add(new BarEntry(3f, Float.parseFloat(String.valueOf(document.get("Section 4")))));
                                xAxisLabels.add("Thu");
                                count++;
                            }

                            if ("FRIDAY".equals(document.getId())) {
                                entryListSection1.add(new BarEntry(4f, Float.parseFloat(String.valueOf(document.get("Section 1")))));
                                entryListSection2.add(new BarEntry(4f, Float.parseFloat(String.valueOf(document.get("Section 2")))));
                                entryListSection3.add(new BarEntry(4f, Float.parseFloat(String.valueOf(document.get("Section 3")))));
                                entryListSection4.add(new BarEntry(4f, Float.parseFloat(String.valueOf(document.get("Section 4")))));
                                xAxisLabels.add("Fri");
                                count++;
                            }

                            if ("SATURDAY".equals(document.getId())) {
                                entryListSection1.add(new BarEntry(5f, Float.parseFloat(String.valueOf(document.get("Section 1")))));
                                entryListSection2.add(new BarEntry(5f, Float.parseFloat(String.valueOf(document.get("Section 2")))));
                                entryListSection3.add(new BarEntry(5f, Float.parseFloat(String.valueOf(document.get("Section 3")))));
                                entryListSection4.add(new BarEntry(5f, Float.parseFloat(String.valueOf(document.get("Section 4")))));
                                xAxisLabels.add("Sat");
                                count++;
                            }

                            if ("SUNDAY".equals(document.getId())) {
                                entryListSection1.add(new BarEntry(6f, Float.parseFloat(String.valueOf(document.get("Section 1")))));
                                entryListSection2.add(new BarEntry(6f, Float.parseFloat(String.valueOf(document.get("Section 2")))));
                                entryListSection3.add(new BarEntry(6f, Float.parseFloat(String.valueOf(document.get("Section 3")))));
                                entryListSection4.add(new BarEntry(6f, Float.parseFloat(String.valueOf(document.get("Section 4")))));
                                xAxisLabels.add("Sun");
                                count++;
                            }
                        }

                        dataSetSection1 = new BarDataSet(entryListSection1, "Psychological");
                        dataSetSection1.setValueTextSize(10f);
                        dataSetSection1.setColor(Color.BLUE);
                        dataSetSection2 = new BarDataSet(entryListSection2, "Psychophysiological");
                        dataSetSection2.setValueTextSize(10f);
                        dataSetSection2.setColor(Color.RED);
                        dataSetSection3 = new BarDataSet(entryListSection3, "Physiological");
                        dataSetSection3.setValueTextSize(10f);
                        dataSetSection3.setColor(Color.GREEN);
                        dataSetSection4 = new BarDataSet(entryListSection4, "Physio-medical");
                        dataSetSection4.setValueTextSize(10f);
                        dataSetSection4.setColor(Color.YELLOW);

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
