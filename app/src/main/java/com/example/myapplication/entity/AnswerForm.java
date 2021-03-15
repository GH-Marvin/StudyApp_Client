package com.example.myapplication.entity;

import java.io.Serializable;

public class AnswerForm implements Serializable {


    private String formId;
    private String saveTime;
    private Integer testId;
    private String answersheet;
    private Integer score;
    private Integer userId;
    private String answersheetResult;

    public AnswerForm() {
    }

    public AnswerForm(String formId, String saveTime, Integer testId, String answersheet, Integer score, Integer userId, String answersheetResult) {
        this.formId = formId;
        this.saveTime = saveTime;
        this.testId = testId;
        this.answersheet = answersheet;
        this.score = score;
        this.userId = userId;
        this.answersheetResult = answersheetResult;
    }

    public String getAnswersheetResult() {
        return answersheetResult;
    }

    public void setAnswersheetResult(String answersheetResult) {
        this.answersheetResult = answersheetResult;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getAnswersheet() {
        return answersheet;
    }

    public void setAnswersheet(String answersheet) {
        this.answersheet = answersheet;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}