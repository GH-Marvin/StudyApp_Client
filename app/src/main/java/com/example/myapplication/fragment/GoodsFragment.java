package com.example.myapplication.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.example.myapplication.R;
import com.example.myapplication.activity.GoodsDetailActivity;
import com.example.myapplication.adapter.GoodsFlowListAdapter;
import com.example.myapplication.adapter.GoodsListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.Goods;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class GoodsFragment extends Fragment {
    private RecyclerView recyclerView;
    private GoodsListAdapter mGoodsListAdapter;
    private GoodsFlowListAdapter mGoodsFlowListAdapter;
    private List<Goods> goodsList = new ArrayList<>();
    private String type;
    private String show_type;
    private String user_id;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            goodsList = (List<Goods>) msg.obj;
            if(show_type.equals("栅格")){
                mGoodsFlowListAdapter = new GoodsFlowListAdapter();
                mGoodsFlowListAdapter.setData(goodsList);
                recyclerView.setAdapter(mGoodsFlowListAdapter);
            }else {
                mGoodsListAdapter = new GoodsListAdapter();
                mGoodsListAdapter.setData(goodsList);
                recyclerView.setAdapter(mGoodsListAdapter);
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(getActivity());
        XUI.initTheme(getActivity());
        recyclerView = view.findViewById(R.id.goods_recyclerView);
        initBundle();
        initRecyclerView();
        initGoodsList();
        return view;
    }
    private void initBundle(){
        Bundle bundle = getArguments();;
        type = bundle.getString("type");
        SharedPreferences sharedPre = getActivity().getSharedPreferences("config",MODE_PRIVATE);
        user_id = sharedPre.getString("user_id","");
    }
    private void initRecyclerView(){
        //设置布局管理器
        SharedPreferences sharedPre=getActivity().getSharedPreferences("config",MODE_PRIVATE);
        show_type = sharedPre.getString("show_type", "");
        if(show_type.equals("栅格")){
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        }else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    private void initGoodsList(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.GOODS_FIND_ALL+"/"+type)
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
                List<Goods> goodsList = new ArrayList<>();
                goodsList = JSONArray.parseArray(responseStr, Goods.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = goodsList;
                mHandler.sendMessage(msg);
            }
        });
    }
}