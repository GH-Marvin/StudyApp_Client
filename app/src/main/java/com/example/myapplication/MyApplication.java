package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.xuexiang.xui.XUI;

public class MyApplication extends Application {

    private static Context mContext;
    private MyApplication application;
    private String userId;
    private String nickname;
    private String head_icon;
    private static final String NAME = "-1";
    private static final String NICKNAME = "未登录";
    private static final String HEADICON = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3397181885,164788655&fm=15&gp=0.jpg";
    //onCreate--->程序创建的时候执行
    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
        mContext = getApplicationContext();
        Log.e("MyApplication", "MyApplication----onCreate()方法！");
        setUserId(NAME);
        setNickname(NICKNAME);
        setHead_icon(HEADICON);

    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_icon() {
        return head_icon;
    }

    public void setHead_icon(String head_icon) {
        this.head_icon = head_icon;
    }
}