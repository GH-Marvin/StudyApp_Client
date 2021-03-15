package com.example.myapplication.entity;

public class Video {
    private Integer videoId;
    private String url;

    public Video() {
    }

    public Video(Integer videoId, String url) {
        this.videoId = videoId;
        this.url = url;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
