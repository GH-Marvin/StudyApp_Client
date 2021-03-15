package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.GoodsListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.AnswerForm;
import com.example.myapplication.entity.Goods;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.util.UserInfoUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsDetailActivity extends AppCompatActivity {
    private TextView realName_tv,point_tv,phone_tv,address_tv,goodsName_tv,goodsDesc_tv,goodsTag_tv1,goodsTag_tv2,goodsPrice_tv,pay_price;
    private Button pay_btn;
    private ImageView goodIcon_iv,back;
    private String user_id;
    private String goods_id,nicknameValue;
    private RelativeLayout address_layout;
    private Context mContext;
    private Goods goods;
    private UserInfo userInfo;
    private boolean up = false;
    private Handler mPostHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Intent intent = new Intent(GoodsDetailActivity.this,OrderActivity.class);
            intent.putExtra("order_id",result);
            Log.e("link","orderId:"+result);
            startActivity(intent);
        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            goods = (Goods) msg.obj;
            Glide.with(GoodsDetailActivity.this).load(goods.getGoodsIcon()).into(goodIcon_iv);
            goodsName_tv.setText(goods.getGoodsName());
            goodsDesc_tv.setText(goods.getGoodsDescription());
            String[] tags = goods.getGoodsTag().split("&");
            goodsTag_tv1.setText(tags[0]);
            goodsTag_tv2.setText(tags[1]);
            goodsPrice_tv.setText(String.valueOf(goods.getGoodsPrice())+".");
            pay_price.setText(String.valueOf(goods.getGoodsPrice()));
        }
    };
    private Handler mUserHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            userInfo = (UserInfo) msg.obj;
            nicknameValue = userInfo.getNickname();
            setUserInfoView(userInfo);
        }
    };
    @Override
    public void onPause() {
        super.onPause();
        up=true;//不可见的时候将刷新开启
    }

    @Override
    public void onResume() {
        super.onResume();
        //判断是否刷新
        if(up){
            Log.e("detailFragment","Refresh_OK");
            SharedPreferences sharedPre=getSharedPreferences("config",MODE_PRIVATE);
            String user_id = sharedPre.getString("user_id","");
            initUserInfo(user_id);
            up=false;//刷新一次即可，不需要一直刷新
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        XUI.initTheme(this);
        initBundle();
        initCompound();
        initGoodsList();
        initUserInfo(user_id);
    }

    private void initBundle(){
        SharedPreferences sharedPre = getSharedPreferences("config",MODE_PRIVATE);
        user_id = sharedPre.getString("user_id","");
        Bundle bundle = getIntent().getExtras();
        goods_id = bundle.getString("goods_id");
    }

    private void initCompound(){
        realName_tv = findViewById(R.id.realName_tv);
        phone_tv = findViewById(R.id.phone_tv);
        address_tv = findViewById(R.id.address_tv);
        goodsName_tv = findViewById(R.id.goodsName_tv);
        goodsDesc_tv = findViewById(R.id.realName_tv);
        goodsTag_tv1 = findViewById(R.id.goodsTag_tv1);
        goodsTag_tv2 = findViewById(R.id.goodsTag_tv2);
        goodsPrice_tv = findViewById(R.id.goodsPrice_tv);
        pay_price = findViewById(R.id.pay_price);
        pay_btn = findViewById(R.id.pay_btn);
        point_tv = findViewById(R.id.point_tv);
        goodIcon_iv = findViewById(R.id.goodIcon_iv);
        address_layout = findViewById(R.id.address_layout);
        address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodsDetailActivity.this, UserInfoActivity.class);
                intent.putExtra("nickname",nicknameValue);
                startActivityForResult(intent,3);
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userInfo.getPoint()>=Integer.valueOf(goods.getGoodsPrice())){
                    if(Integer.valueOf(goods.getGoodsStock())>0){
                        String msg = "你确定使用"+goods.getGoodsPrice()+"积分兑换"+goods.getGoodsName()+"吗？";
                        new MaterialDialog.Builder(view.getContext())
                                .icon(getDrawable(R.drawable.ic_shop_car))
                                .title("确认支付")
                                .content(msg)
                                .positiveText("确认兑换")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        //TODO
                                        dialog.dismiss();
                                        postGoods();

                                    }
                                })
                                .negativeText("取消")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }else {
                        dialog("该商品库存紧张，无法兑换！","确定");
                    }
                }else {
                    dialog("你的积分不足，无法兑换此商品！","确定");
                }
            }
        });
    }
    private void initGoodsList(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.GOODS_FIND_BY_ID+"/"+goods_id)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","查询商品请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","查询商品请求成功");
                String responseStr = response.body().string();
                Goods goods = JSON.parseObject(responseStr, Goods.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = goods;
                mHandler.sendMessage(msg);
            }
        });
    }
    private void initUserInfo(String user_id){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.USER_INFO_FIND_BY_ID + "/" +user_id)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","查询用户信息请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","查询用户信息请求接收成功");
                String responseStr = response.body().string();
                System.out.println(responseStr);
                UserInfo userInfo = JSON.parseObject(responseStr,UserInfo.class);
                Message msg = mUserHandler.obtainMessage();
                msg.obj = userInfo;
                mUserHandler.sendMessage(msg);
            }
        });
    }
    private void setUserInfoView(UserInfo userInfo){
        if(userInfo.getRealname()!=null){
            realName_tv.setText(userInfo.getRealname());
        }else {
            realName_tv.setText("未填写");
        }
        if(userInfo.getPhone()!=null){
            phone_tv.setText(userInfo.getPhone());
        }else {
            phone_tv.setText("未填写");
        }
        if(userInfo.getAddress()!=null){
            address_tv.setText(userInfo.getAddress());
        }else {
            address_tv.setText("未填写");
        }
        if(userInfo.getPoint()!=null){
            point_tv.setText(userInfo.getPoint().toString()+".00");
        }else {
            point_tv.setText("0");
        }
    }
    private void dialog(String msg,String btn){
        new MaterialDialog.Builder(GoodsDetailActivity.this)
                .content(msg)
                .positiveText(btn)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void postGoods(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        Map<String,String> map = new HashMap<>();
        map.put("user_id",user_id);
        map.put("goods_id",goods_id);
        String str = JSON.toJSONString(map);
        RequestBody body = RequestBody.create(JSON_TYPE, str);
        final Request request = new Request.Builder()
                .url(AppConfig.GOODS_DETAIL_SUBMIT)
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","接收成功");
                String responseStr = response.body().string();
                String result = responseStr;
                Message msg = mPostHandler.obtainMessage();
                msg.obj = result;
                mPostHandler.sendMessage(msg);
            }
        });
    }
}