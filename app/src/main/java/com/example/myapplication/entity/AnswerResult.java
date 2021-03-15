package com.example.myapplication.entity;

import java.io.Serializable;

public class AnswerResult implements Serializable {
    private Question question;
    private Integer checked;
    public AnswerResult(){}

    public AnswerResult(Question question, Integer checked) {
        this.question = question;
        this.checked = checked;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }
}
