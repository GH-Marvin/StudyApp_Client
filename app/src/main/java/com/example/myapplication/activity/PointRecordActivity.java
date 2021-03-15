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
import com.example.myapplication.adapter.OrderMasterListAdapter;
import com.example.myapplication.adapter.PointRecordListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.OrderMaster;
import com.example.myapplication.entity.PointRecord;
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

public class PointRecordActivity extends AppCompatActivity {
    private String user_id,point;
    private ImageView back_btn;
    private RecyclerView recyclerView;
    private TextView point_tv;
    private List<PointRecord> pointRecordList = new ArrayList<>();
    private PointRecordListAdapter mPointRecordListAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            pointRecordList = (List<PointRecord>) msg.obj;
            mPointRecordListAdapter = new PointRecordListAdapter();
            mPointRecordListAdapter.setData(pointRecordList);
            recyclerView.setAdapter(mPointRecordListAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_record);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        XUI.initTheme(this);
        initBtn();
        initBundle();
        initRecyclerView();
        initPointList();
    }

    private void initBundle(){
        SharedPreferences sharedPre = getSharedPreferences("config",MODE_PRIVATE);
        user_id = sharedPre.getString("user_id","");
        point = sharedPre.getString("point","");
        point_tv = findViewById(R.id.point_tv);
        point_tv.setText(point);
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
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void initPointList(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.POINT_RECORD_FIND_BY_USER_ID+"/"+user_id)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","查询积分列表请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","查询积分列表请求成功");
                String responseStr = response.body().string();
                List<PointRecord> pointRecordList = new ArrayList<>();
                pointRecordList = JSONArray.parseArray(responseStr, PointRecord.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = pointRecordList;
                mHandler.sendMessage(msg);
            }
        });
    }
}