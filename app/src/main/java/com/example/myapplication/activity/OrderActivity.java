package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.Goods;
import com.example.myapplication.entity.OrderMaster;
import com.example.myapplication.entity.UserInfo;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.io.IOException;

public class OrderActivity extends AppCompatActivity {
    private String user_id;
    private ImageView back_btn,goodIcon_iv;
    private TextView realName_tv,phone_tv,address_tv,goodsName_tv,goodsPrice_tv,pay_price,order_tv,update_time,total_price;
    private Button checkAll_btn,home_btn;
    private String order_id;
    private OrderMaster orderMaster;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            orderMaster = (OrderMaster) msg.obj;
            Glide.with(OrderActivity.this).load(orderMaster.getGoodsIcon()).into(goodIcon_iv);
            realName_tv.setText(orderMaster.getBuyerName());
            phone_tv.setText(orderMaster.getBuyerPhone());
            address_tv.setText(orderMaster.getBuyerAddress());
            goodsName_tv.setText(orderMaster.getGoodsName());
            goodsPrice_tv.setText(String.valueOf(orderMaster.getGoodsPrice())+".");
            pay_price.setText(String.valueOf(orderMaster.getGoodsPrice()));
            order_tv.setText(orderMaster.getOrderId());
            update_time.setText(orderMaster.getUpdateTime());
            total_price.setText(orderMaster.getGoodsPrice()+".00 积分");

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        XUI.initTheme(this);
        initBundle();
        initCompound();
        initBtn();
        initOrder(order_id);
    }
    private void initBundle(){
        SharedPreferences sharedPre = getSharedPreferences("config",MODE_PRIVATE);
        user_id = sharedPre.getString("user_id","");
        order_id = "";
        Bundle bundle = getIntent().getExtras();
        order_id = bundle.getString("order_id");
    }
    private void initCompound() {
        realName_tv = findViewById(R.id.realName_tv);
        phone_tv = findViewById(R.id.phone_tv);
        address_tv = findViewById(R.id.address_tv);
        goodsName_tv = findViewById(R.id.goodsName_tv);
        goodsPrice_tv = findViewById(R.id.goodsPrice_tv);
        pay_price = findViewById(R.id.pay_price);
        goodIcon_iv = findViewById(R.id.goodIcon_iv);
        order_tv = findViewById(R.id.order_tv);
        update_time = findViewById(R.id.update_time);
        total_price = findViewById(R.id.total_price);
    }
    private void initBtn(){
        back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        checkAll_btn = findViewById(R.id.checkAll_btn);
        checkAll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this,MyOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
        home_btn = findViewById(R.id.home_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this,GoodsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void initOrder(String order_id){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.Order_FIND_BY_ID + "/" +order_id)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","查询订单信息请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","查询订单信息请求接收成功");
                String responseStr = response.body().string();
                OrderMaster orderMaster = JSON.parseObject(responseStr,OrderMaster.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = orderMaster;
                mHandler.sendMessage(msg);
            }
        });
    }
}