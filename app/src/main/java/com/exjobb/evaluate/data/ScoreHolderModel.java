package com.exjobb.evaluate.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreHolderModel implements Parcelable {

    private ArrayList<Integer> quizScore;

    public ScoreHolderModel(ArrayList<Integer> quizScore) {
        this.quizScore = quizScore;
    }

    public ScoreHolderModel() {
        setQuizScore(new ArrayList<>());
    }

    protected ScoreHolderModel(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ScoreHolderModel> CREATOR = new Creator<ScoreHolderModel>() {
        @Override
        public ScoreHolderModel createFromParcel(Parcel in) {
            return new ScoreHolderModel(in);
        }

        @Override
        public ScoreHolderModel[] newArray(int size) {
            return new ScoreHolderModel[size];
        }
    };

    public ArrayList<Integer> getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(ArrayList<Integer> quizScore) {
        this.quizScore = quizScore;
    }

    public int getNoOfElements() {
        return quizScore.size();
    }

    public void addPointToHolder(Integer quizPoint) {
        quizScore.add(quizPoint);
    }

    public Double getPointsFromHolder(int index) {
        Double totalScore = 0.0;
        List<Integer> subList;

        switch (index) {
            case 0:
                subList = quizScore.subList(0, 9);
                for (double score : subList) {
                    totalScore += score;
                }
                //totalScore = totalScore / 10;
                break;
            case 1:
                subList = quizScore.subList(0, 4);
                for (double score : subList) {
                    totalScore += score;
                }
                //totalScore = totalScore / 4;
                break;
            case 2:
                subList = quizScore.subList(4, 7);
                for (double score : subList) {
                    totalScore += score;
                }
                //totalScore = totalScore / 3;
                break;
            case 3:
                subList = quizScore.subList(7, 9);
                for (double score : subList) {
                    totalScore += score;
                }
                //totalScore = totalScore / 2;
                break;
            case 4:
                totalScore = Double.parseDouble(String.valueOf(quizScore.get(9)));
                break;
        }
        return totalScore;
    }

    public Map<String, Object> convertToMap(String coachId, String playerId, String week, String day) {
        Map<String, Object> scores = new HashMap<>();
        int j = 1;
        for (int points : quizScore) {
            scores.put("Question " + j++, points);
        }

        scores.put("Section 1", getPointsFromHolder(1));
        scores.put("Section 2", getPointsFromHolder(2));
        scores.put("Section 3", getPointsFromHolder(3));
        scores.put("Section 4", getPointsFromHolder(4));
        scores.put("Total score", getPointsFromHolder(0));
        scores.put("coachId", coachId);
        scores.put("playerId", playerId);
        scores.put("week", week);
        scores.put("day", day);
        scores.put("timestamp", FieldValue.serverTimestamp());

        switch (day) {
            case "MONDAY":
                scores.put("docNumber", 1);
                break;
            case "TUESDAY":
                scores.put("docNumber", 2);
                break;
            case "WEDNESDAY":
                scores.put("docNumber", 3);
                break;
            case "THURSDAY":
                scores.put("docNumber", 4);
                break;
            case "FRIDAY":
                scores.put("docNumber", 5);
                break;
            case "SATURDAY":
                scores.put("docNumber", 6);
                break;
            case "SUNDAY":
                scores.put("docNumber", 7);
                break;
        }

        return scores;
    }
}
