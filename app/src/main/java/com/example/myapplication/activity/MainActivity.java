package com.example.myapplication.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.Test;
import com.example.myapplication.entity.User;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.entity.UserTest;
import com.example.myapplication.fragment.MineFragment;
import com.example.myapplication.util.XToastUtils;
import com.example.myapplication.vo.ResultVO;
import com.example.myapplication.vo.UserVO;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MyApplication myApplication;
    private ImageView close_btn;
    private EditText username_et;
    private EditText password_et;
    private Button login_btn;
    private TextView register_btn;
    private ImageView qq;
    private ImageView wechat;
    private Drawable[] user_icon;
    private Drawable[] pass_icon;
    private Toast myToast;
    private LinearLayout login_layout;
    private String TAG="MainActivity";
    private int bg_count = 0;
    private ResultVO resultVO;
    private UserInfo data;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            resultVO = new ResultVO();
            resultVO = (ResultVO) msg.obj;
            if(resultVO.getCode()==1){
                XToastUtils.error(resultVO.getMsg());
            }else if(resultVO.getCode()==0){
                UserInfo userInfo = new UserInfo();
                userInfo = (UserInfo) resultVO.getData();
                Intent intent = getIntent();
                setResult(RESULT_OK,intent);
                SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPre.edit();
                edit.putString("nickname",userInfo.getNickname());
                edit.putString("head_icon",userInfo.getUserIcon());
                edit.putString("user_id",String.valueOf(userInfo.getUserId()));
                edit.putString("point",String.valueOf(userInfo.getPoint()));
                edit.apply();
                XToastUtils.info(userInfo.getNickname()+",登录成功！");
                finish();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            UserInfo userInfo = (UserInfo) data.getSerializableExtra("userInfo");
            SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPre.edit();
            edit.putString("nickname",userInfo.getNickname());
            edit.putString("head_icon",userInfo.getUserIcon());
            edit.putString("user_id",String.valueOf(userInfo.getUserId()));
            edit.putString("point",String.valueOf(userInfo.getPoint()));
            edit.apply();
            setResult(RESULT_OK);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username_et = findViewById(R.id.username);
        password_et = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        register_btn = findViewById(R.id.register);
        qq = findViewById(R.id.login_by_QQ);
        close_btn = findViewById(R.id.close);
        wechat = findViewById(R.id.login_by_wechat);
        login_layout = findViewById(R.id.login_linear);
        // 设置顶部状态栏为透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        init();

    }

    private void init(){
        username_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Drawable left;
                Drawable right;
                if (hasFocus) {
                    // 当用户名输入框获取焦点时对应icon变色/高亮
                    left = v.getResources().getDrawable(R.drawable.ic_username_highlight);
                    right = v.getResources().getDrawable(R.drawable.ic_no);
                    right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                    username_et.setCompoundDrawables(left, null, right, null);
                } else {
                    // 失去焦点时还原颜色
                    left = v.getResources().getDrawable(R.drawable.ic_username);
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                    username_et.setCompoundDrawables(left, null, null , null);
                }

            }
        });
        password_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Drawable left;
                Drawable right;
                if (hasFocus) {
                    // 当用户名输入框获取焦点时对应icon变色/高亮
                    left = v.getResources().getDrawable(R.drawable.ic_password_highlight);
                    right = v.getResources().getDrawable(R.drawable.ic_eyeclose);
                    right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                    password_et.setCompoundDrawables(left, null, right , null);
                } else {
                    // 失去焦点时还原颜色
                    left = v.getResources().getDrawable(R.drawable.ic_password);
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                    password_et.setCompoundDrawables(left, null, null , null);
                }

            }
        });
        close_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 3);
            }
        });
        // 登录
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username_et.getText().length()==0||password_et.getText().length()==0){
                    XToastUtils.error("账号或密码不能为空！");
                }else {
                    if(username_et.getText().length()<6){
                        XToastUtils.error("用户名格式有误！");
                    }
                    else if (password_et.getText().length()<6||password_et.getText().length()>16){
                        XToastUtils.error("密码长度应在6-16之间！");
                    }else {
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
                        Map<String,String> map = new HashMap<>();
                        map.put("username",username_et.getText().toString());
                        map.put("password",password_et.getText().toString());
                        String str = JSON.toJSONString(map);
                        System.out.println(str);
                        RequestBody body = RequestBody.create(JSON_TYPE, str);
                        final Request request = new Request.Builder()
                                .url(AppConfig.USER_LOGIN)
                                .post(body)
                                .build();
                        Call call = mOkHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Log.e("link","登录请求失败");
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                Log.e("link","登录请求接收成功");
                                String responseStr = response.body().string();
                                ResultVO<UserInfo> resultVO = new ResultVO<>();
                                System.out.println(responseStr.charAt(7));
                                resultVO =  JSON.parseObject(responseStr,new TypeReference<ResultVO<UserInfo>>(){});
                                Message msg = mHandler.obtainMessage();
                                msg.obj = resultVO;
                                mHandler.sendMessage(msg);
                            }
                        });
                    }

                }


            }
        });

    }

}