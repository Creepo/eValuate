package com.exjobb.evaluate.survey.plfsq;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.data.ScoreHolderModel;
import com.exjobb.evaluate.algorithm.ImageProcessing;

import java.util.concurrent.atomic.AtomicBoolean;

import static maes.tech.intentanim.CustomIntent.customType;

// courtsy of phisman3579

public class QuizQuestion11Content extends AppCompatActivity {
    private static final String TAG = "QuizQuestion11Content";
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];

    private static SurfaceHolder previewHolder;
    private static Camera camera;
    private static WakeLock wakeLock;

    private ScoreHolderModel scoreHolder;
    private static EditText editText;
    private String pathReference, nameReference;

    private static int averageIndex;
    private static int beatsIndex;
    private static double beats;
    private static long startTime;

    private static TYPE currentType = TYPE.GREEN;

    public static enum TYPE {
        GREEN, RED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quiz_question_11);
        Log.d(TAG, "onCreate: started");
        this.setTitle("PLFS-Questionnaire");

        initVariables();
        getDataFromIntent();
    }

    private void initVariables() {
        Log.i(TAG, "initVariables: initiated");
        scoreHolder = new ScoreHolderModel();
        editText = findViewById(R.id.editText_Question_11);

        SurfaceView preview = findViewById(R.id.preview);
        previewHolder = null;
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        camera = null;
        wakeLock = null;

        averageIndex = 0;
        beatsIndex = 0;
        beats = 0;
        startTime = 0;

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scoreHolder.setQuizScore(bundle.getIntegerArrayList("question10Answer"));
            pathReference = bundle.getString("playerPath");
            nameReference = bundle.getString("playerName");
            Log.d(TAG, "getDataFromIntent: list size: " + scoreHolder.getNoOfElements() + " Path: " + pathReference +
                    "name: " + nameReference);
        }
    }

    private void getInput() {
        Log.i(TAG, "getInput: started");
        // if user leaves field empty then default the value to 0}
        Integer parsedInput;
        if (TextUtils.isEmpty(editText.getText())) {
            parsedInput = 0;
            scoreHolder.addPointToHolder(parsedInput);
            Log.d(TAG, "goToQuestionTwelve: " + scoreHolder.getNoOfElements());
        } else {
            parsedInput = Integer.parseInt(editText.getText().toString());
            scoreHolder.addPointToHolder(parsedInput);
            Log.d(TAG, "goToQuestionTwelve: " + scoreHolder.getNoOfElements() +
                    " value parsed: " + parsedInput);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        camera = Camera.open();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private static PreviewCallback previewCallback = new PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), width, height);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 30) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                // Log.d(TAG, "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                editText.setText(String.valueOf(beatsAvg), TextView.BufferType.EDITABLE);
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }
        return result;
    }

    public void goToQuestionTwelve(View view) {
        Log.i(TAG, "goToQuestionTwelve: pressed");
        getInput();

        Intent intent = new Intent(this, QuizQuestion12Content.class);
        intent.putExtra("question11Answer", scoreHolder.getQuizScore());
        intent.putExtra("playerPath", pathReference);
        intent.putExtra("playerName", nameReference);
        startActivity(intent);
        customType(this, "left-to-right");
    }
}
