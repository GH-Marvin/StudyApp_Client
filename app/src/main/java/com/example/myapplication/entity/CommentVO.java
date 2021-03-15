package com.example.myapplication.entity;

public class CommentVO {
    private Integer commentId;
    private String userIcon;
    private String nickname;
    private String time;
    private String content;
    private Integer goodNum;
    private Integer videoId;

    public CommentVO() {
    }

    public CommentVO(Integer commentId, String userIcon, String nickname, String time, String content, Integer goodNum, Integer videoId) {
        this.commentId = commentId;
        this.userIcon = userIcon;
        this.nickname = nickname;
        this.time = time;
        this.content = content;
        this.goodNum = goodNum;
        this.videoId = videoId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public Integer getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }
}
