package com.example.myapplication.entity;

import java.io.Serializable;

public class PointRecord implements Serializable {
    private Integer prId;
    private Integer userId;
    private String createTime;
    private String eventName;
    private Integer gainPoint;

    public PointRecord() {
    }

    public PointRecord(Integer prId, Integer userId, String createTime, String eventName, Integer gainPoint) {
        this.prId = prId;
        this.userId = userId;
        this.createTime = createTime;
        this.eventName = eventName;
        this.gainPoint = gainPoint;
    }

    public Integer getPrId() {
        return prId;
    }

    public void setPrId(Integer prId) {
        this.prId = prId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getGainPoint() {
        return gainPoint;
    }

    public void setGainPoint(Integer gainPoint) {
        this.gainPoint = gainPoint;
    }
}
