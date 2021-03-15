package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.fragment.IndexFragment;
import com.example.myapplication.fragment.MineFragment;
import com.example.myapplication.R;
import com.example.myapplication.fragment.SearchFragment;
import com.example.myapplication.fragment.AnswerFragment;
import com.example.myapplication.util.XToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class BaseActivity extends AppCompatActivity {
    private String TAG = "BaseActivity";
    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;
    private AlertDialog.Builder builder;
    private int[] items={
        R.drawable.ic_home,R.drawable.ic_search,R.drawable.ic_answer,R.drawable.ic_mine
    };
    private String[] strings={"主页","搜索","题库","我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XUI.initTheme(this);
        setContentView(R.layout.activity_base);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        mTabLayout = findViewById(R.id.toolbar_viewPager2);
        mViewPager2 = findViewById(R.id.viewPager2);
        mViewPager2.setUserInputEnabled(false);
        FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return 4;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        Log.d(TAG,"主页");
                        return new IndexFragment();
                    case 1:
                        Log.d(TAG,"搜索");
                        return new SearchFragment();
                    case 2:
                        Log.d(TAG,"答题");
                        return new AnswerFragment();
                    default:
                        Log.d(TAG,"我的");
                        return new MineFragment();

                }
            }
        };
        mViewPager2.setAdapter(fragmentStateAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(strings[position]);
               tab.setIcon(items[position]);
            }
        }).attach();
    }
    public void click(View view) {
        XToastUtils.toast("开发人员正在开发测试中...");
    }
}