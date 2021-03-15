package com.example.myapplication.entity;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {
    private Integer uiId;
    private Integer userId;
    private String nickname;
    private String realname;
    private String sex;
    private Integer age;
    private Date birthday;
    private String identity;
    private String address;
    private String userIcon;
    private String phone;
    private Integer point;
    public UserInfo(){}

    public UserInfo(Integer uiId, Integer userId, String nickname, String realname, String sex, Integer age, Date birthday, String identity, String address, String userIcon, String phone, Integer point) {
        this.uiId = uiId;
        this.userId = userId;
        this.nickname = nickname;
        this.realname = realname;
        this.sex = sex;
        this.age = age;
        this.birthday = birthday;
        this.identity = identity;
        this.address = address;
        this.userIcon = userIcon;
        this.phone = phone;
        this.point = point;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUiId() {
        return uiId;
    }

    public void setUiId(Integer uiId) {
        this.uiId = uiId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
