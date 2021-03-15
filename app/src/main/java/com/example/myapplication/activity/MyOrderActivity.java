package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.example.myapplication.R;
import com.example.myapplication.adapter.GoodsListAdapter;
import com.example.myapplication.adapter.OrderMasterListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.Goods;
import com.example.myapplication.entity.OrderMaster;
import com.example.myapplication.util.DialogUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity {
    private String user_id;
    private ImageView back_btn;
    private RecyclerView recyclerView;
    private List<OrderMaster> orderMasterList = new ArrayList<>();
    private OrderMasterListAdapter mOrderMasterListAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            orderMasterList = (List<OrderMaster>) msg.obj;
            mOrderMasterListAdapter = new OrderMasterListAdapter();
            mOrderMasterListAdapter.setData(orderMasterList);
            recyclerView.setAdapter(mOrderMasterListAdapter);
            mOrderMasterListAdapter.setOnItemClickListener(new OrderMasterListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(MyOrderActivity.this,OrderActivity.class);
                    intent.putExtra("order_id",orderMasterList.get(position).getOrderId());
                    startActivity(intent);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        XUI.initTheme(this);
        initBtn();
        initBundle();
        initRecyclerView();
        initMyOrderList();
    }

    private void initBundle(){
        SharedPreferences sharedPre = getSharedPreferences("config",MODE_PRIVATE);
        user_id = sharedPre.getString("user_id","");
    }
    private void initBtn(){
        back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initRecyclerView(){
        //设置布局管理器
        recyclerView = findViewById(R.id.order_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void initMyOrderList(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.ORDER_FIND_BY_USER_ID+"/"+user_id)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","查询商品列表请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","查询商品列表请求成功");
                String responseStr = response.body().string();
                List<OrderMaster> orderMasterList = new ArrayList<>();
                orderMasterList = JSONArray.parseArray(responseStr, OrderMaster.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = orderMasterList;
                mHandler.sendMessage(msg);
            }
        });
    }
}