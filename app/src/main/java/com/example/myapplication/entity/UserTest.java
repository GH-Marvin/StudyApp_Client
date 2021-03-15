package com.example.myapplication.entity;

public class UserTest {
    private Integer utId;
    private Integer userId;
    private Integer passNum;
    private Integer failNum;
    private Integer notryNum;
    private Test test;

    public UserTest() {
    }

    public UserTest(Integer utId, Integer userId, Integer passNum, Integer failNum, Integer notryNum, Test test) {
        this.utId = utId;
        this.userId = userId;
        this.passNum = passNum;
        this.failNum = failNum;
        this.notryNum = notryNum;
        this.test = test;
    }

    public Integer getUtId() {
        return utId;
    }

    public void setUtId(Integer utId) {
        this.utId = utId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPassNum() {
        return passNum;
    }

    public void setPassNum(Integer passNum) {
        this.passNum = passNum;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public Integer getNotryNum() {
        return notryNum;
    }

    public void setNotryNum(Integer notryNum) {
        this.notryNum = notryNum;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
