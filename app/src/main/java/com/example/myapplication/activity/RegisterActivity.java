package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.myapplication.R;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.fragment.MineFragment;
import com.example.myapplication.util.XToastUtils;
import com.example.myapplication.vo.ResultVO;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private ImageView back_btn;
    private EditText username;
    private EditText password;
    private EditText check_password;
    private Button register_btn;
    private ResultVO<UserInfo> resultVO;
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
                intent.putExtra("userInfo", (Serializable) userInfo);
                setResult(RESULT_OK, intent);
                finish();
                XToastUtils.info("注册成功,请登录！");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        //绑定
        back_btn = findViewById(R.id.back);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        check_password = findViewById(R.id.check_password);
        register_btn = findViewById(R.id.register_btn);
        init_js();
        back_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().length()==0||password.getText().length()==0||check_password.getText().length()==0){
                    XToastUtils.error("账号/密码/确认密码不能为空！");
                }else {
                    if (username.getText().length() < 6) {
                        XToastUtils.error("用户名格式有误！");
                    } else if (password.getText().length() < 6 || password.getText().length() > 16) {
                        XToastUtils.error("密码长度应在6-16之间！");
                    } else {
                        if(!password.getText().toString().equals(check_password.getText().toString())){
                            XToastUtils.error("密码和确认密码不一致！");
                        }else {
                            OkHttpClient mOkHttpClient = new OkHttpClient();
                            MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
                            Map<String, String> map = new HashMap<>();
                            map.put("username", username.getText().toString());
                            map.put("password", password.getText().toString());
                            String str = JSON.toJSONString(map);
                            System.out.println(str);
                            RequestBody body = RequestBody.create(JSON_TYPE, str);
                            final Request request = new Request.Builder()
                                    .url(AppConfig.USER_REGISTER)
                                    .post(body)
                                    .build();
                            Call call = mOkHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                    Log.e("link", "注册请求失败");
                                }

                                @Override
                                public void onResponse(Response response) throws IOException {
                                    Log.e("link", "注册请求接收成功");
                                    String responseStr = response.body().string();
                                    ResultVO<UserInfo> resultVO = new ResultVO<>();
                                    System.out.println(responseStr.charAt(7));
                                    resultVO = JSON.parseObject(responseStr, new TypeReference<ResultVO<UserInfo>>() {
                                    });
                                    Message msg = mHandler.obtainMessage();
                                    msg.obj = resultVO;
                                    mHandler.sendMessage(msg);
                                }
                            });
                        }
                    }
                }
            }
        });

    }
    private void init_js(){
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                    username.setCompoundDrawables(left, null, right, null);
                } else {
                    // 失去焦点时还原颜色
                    left = v.getResources().getDrawable(R.drawable.ic_username);
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                    username.setCompoundDrawables(left, null, null , null);
                }

            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                    password.setCompoundDrawables(left, null, right , null);
                } else {
                    // 失去焦点时还原颜色
                    left = v.getResources().getDrawable(R.drawable.ic_password);
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                    password.setCompoundDrawables(left, null, null , null);
                }

            }
        });
        check_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                    check_password.setCompoundDrawables(left, null, right , null);
                } else {
                    // 失去焦点时还原颜色
                    left = v.getResources().getDrawable(R.drawable.ic_password);
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                    check_password.setCompoundDrawables(left, null, null , null);
                }

            }
        });
    }
}