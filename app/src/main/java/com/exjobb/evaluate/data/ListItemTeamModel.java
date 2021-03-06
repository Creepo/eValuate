package com.exjobb.evaluate.data;

import java.text.DecimalFormat;

public class ListItemTeamModel {
    private String question, questionCellMon, questionCellTue, questionCellWed, questionCellThu, questionCellFri, questionCellSat, questionCellSun;

    public ListItemTeamModel(String question, String questionCellMon, String questionCellTue, String questionCellWed, String questionCellThu, String questionCellFri, String questionCellSat, String questionCellSun) {
        this.question = question;
        this.questionCellMon = questionCellMon;
        this.questionCellTue = questionCellTue;
        this.questionCellWed = questionCellWed;
        this.questionCellThu = questionCellThu;
        this.questionCellFri = questionCellFri;
        this.questionCellSat = questionCellSat;
        this.questionCellSun = questionCellSun;
    }

    public ListItemTeamModel() {
        this.question = "0";
        this.questionCellMon = "0";
        this.questionCellTue = "0";
        this.questionCellWed = "0";
        this.questionCellThu = "0";
        this.questionCellFri = "0";
        this.questionCellSat = "0";
        this.questionCellSun = "0";
    }

    public void clearList() {
        setQuestionCellMon("0");
        setQuestionCellTue("0");
        setQuestionCellWed("0");
        setQuestionCellThu("0");
        setQuestionCellFri("0");
        setQuestionCellSat("0");
        setQuestionCellSun("0");
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionCellMon() {
        return questionCellMon;
    }

    public void setQuestionCellMon(String questionCellMon) {
        this.questionCellMon = new DecimalFormat("#.##").format(Double.parseDouble(questionCellMon));
    }

    public String getQuestionCellTue() {
        return questionCellTue;
    }

    public void setQuestionCellTue(String questionCellTue) {
        this.questionCellTue = new DecimalFormat("#.##").format(Double.parseDouble(questionCellTue));
    }

    public String getQuestionCellWed() {
        return questionCellWed;
    }

    public void setQuestionCellWed(String questionCellWed) {
        this.questionCellWed = new DecimalFormat("#.##").format(Double.parseDouble(questionCellWed));
    }

    public String getQuestionCellThu() {
        return questionCellThu;
    }

    public void setQuestionCellThu(String questionCellThu) {
        this.questionCellThu = new DecimalFormat("#.##").format(Double.parseDouble(questionCellThu));
    }

    public String getQuestionCellFri() {
        return questionCellFri;
    }

    public void setQuestionCellFri(String questionCellFri) {
        this.questionCellFri = new DecimalFormat("#.##").format(Double.parseDouble(questionCellFri));
    }

    public String getQuestionCellSat() {
        return questionCellSat;
    }

    public void setQuestionCellSat(String questionCellSat) {
        this.questionCellSat = new DecimalFormat("#.##").format(Double.parseDouble(questionCellSat));
    }

    public String getQuestionCellSun() {
        return questionCellSun;
    }

    public void setQuestionCellSun(String questionCellSun) {
        this.questionCellSun = new DecimalFormat("#.##").format(Double.parseDouble(questionCellSun));
    }
}
