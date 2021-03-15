package com.example.myapplication.entity;

import java.io.Serializable;

public class Choice  {
    private Integer optId;
    private String name;
    private Integer questId;
    private Integer isAnswer;
    private String optType;

    public Choice() {
    }

    public Choice(Integer optId, String name, Integer questId, Integer isAnswer, String optType) {
        this.optId = optId;
        this.name = name;
        this.questId = questId;
        this.isAnswer = isAnswer;
        this.optType = optType;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuestId() {
        return questId;
    }

    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    public Integer getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(Integer isAnswer) {
        this.isAnswer = isAnswer;
    }
}
