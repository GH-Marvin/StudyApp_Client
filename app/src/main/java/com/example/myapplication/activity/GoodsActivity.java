package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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

import com.alibaba.fastjson.JSONArray;
import com.example.myapplication.R;
import com.example.myapplication.adapter.GoodsListAdapter;
import com.example.myapplication.adapter.ScoreListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.AnswerForm;
import com.example.myapplication.entity.AnswerResult;
import com.example.myapplication.entity.Goods;
import com.example.myapplication.entity.Question;
import com.example.myapplication.fragment.GoodsFragment;
import com.example.myapplication.fragment.HeaderOneFragment;
import com.example.myapplication.util.DialogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsActivity extends AppCompatActivity {
    private ImageView back_btn;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private String user_id;
    private TextView order_tv;
    private static String[] titles = new String[]{
            "手机",
            "手环",
            "书籍",
            "食品",
            "服装"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        XUI.initTheme(this);
        SharedPreferences sharedPre = getSharedPreferences("config",MODE_PRIVATE);
        user_id = sharedPre.getString("user_id","");
        initViewPager();
        initBtn();

    }
    private void initViewPager(){
        mViewPager = findViewById(R.id.toolbar_viewPager);
        mTabLayout = findViewById(R.id.tab_layout);
        FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return titles.length;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                GoodsFragment fragment = new GoodsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type",String.valueOf(position+1));
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        mViewPager.setAdapter(fragmentStateAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();
    }

    private void initBtn(){
        back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        order_tv = findViewById(R.id.order_tv);
        order_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id!=""&&user_id!=null){
                    Intent intent = new Intent(GoodsActivity.this,MyOrderActivity.class);
                    startActivity(intent);
                }else {
                    DialogUtil.dialogConfirm("你还未登录，无法查看！","确定",GoodsActivity.this);
                }
            }
        });
    }
}