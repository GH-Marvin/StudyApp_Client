package com.example.myapplication.entity;

public class Course {
    private Integer courseId;
    private String imgUrl;
    private String title;
    private String type;
    private String content;
    private Integer videoId;

    public Course(){

    }

    public Course(Integer courseId, String imgUrl, String title, String type, String content, Integer videoId) {
        this.courseId = courseId;
        this.imgUrl = imgUrl;
        this.title = title;
        this.type = type;
        this.content = content;
        this.videoId = videoId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }
}

