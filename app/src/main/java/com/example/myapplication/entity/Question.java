package com.example.myapplication.entity;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private Integer questId;
    private String title;
    private Integer score;
    private String type;

    public Question() {
    }

    public Question(Integer questId, String title, Integer score, String type) {
        this.questId = questId;
        this.title = title;
        this.score = score;
        this.type = type;
    }

    public Integer getQuestId() {
        return questId;
    }

    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
