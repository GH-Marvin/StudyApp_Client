package com.example.myapplication.entity;

public class Comment {
    private String head;
    private String name;
    private String time;
    private String content;
    private Integer goodNum;

    public Comment() {
    }

    public Comment(String head, String name, String time, String content, Integer goodNum) {
        this.head = head;
        this.name = name;
        this.time = time;
        this.content = content;
        this.goodNum = goodNum;

    }

    public Integer getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
    }


    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
