package com.example.myapplication.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.activity.SearchActivity;
import com.example.myapplication.fragment.HeaderOneFragment;
import com.xuexiang.xui.widget.tabbar.VerticalTabLayout;
import com.xuexiang.xui.widget.tabbar.vertical.ITabView;
import com.xuexiang.xui.widget.tabbar.vertical.TabAdapter;
import com.xuexiang.xui.widget.tabbar.vertical.TabView;

import java.util.ArrayList;
import java.util.Collections;


public class SearchFragment extends Fragment implements VerticalTabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    private VerticalTabLayout mVerticalTabLayout;
    private ViewPager mViewPager;
    private ArrayList<String> mTitlesList;
    private ArrayList<Fragment> mFragmentsList;
    private EditText search_edit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = view.findViewById(R.id.viewPager);
        mVerticalTabLayout = view.findViewById(R.id.vertical);
        search_edit = view.findViewById(R.id.search_edit);
        search_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    search_edit.clearFocus();
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                }
            }
        });
        initView();
        initData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    private void initData() {
        mVerticalTabLayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {
//                return new TabView.TabBadge.Builder().setBadgeNumber(999).setBackgroundColor(0xff2faae5).build();
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public ITabView.TabTitle getTitle(int position) {
                return new TabView.TabTitle.Builder()
                        .setContent(mTitlesList.get(position))
                        .setTextSize(12)
                        .setTextColor(Color.rgb(54, 191, 255), Color.rgb(145, 145, 145))
                        .build();
            }

            @Override
            public int getBackground(int position) {
                return -1;
            }


        });
    }

    private void initView() {
        mTitlesList = new ArrayList<>();//初始化Tab栏数据
        Collections.addAll(mTitlesList, "Python", "Java", "C++", "CSS", ".NET", "JavaScript", "Swift", "PHP", "C#", "Groovy", "SQL", "Ruby");
        //创建适配器、适配器继承自FragmentPagerAdapter
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getParentFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {

                HeaderOneFragment fragment = new HeaderOneFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type",String.valueOf(position));
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);//绑定适配器
        //Tab和ViewPager相互联动
        mViewPager.setOnPageChangeListener(this);
        mVerticalTabLayout.addOnTabSelectedListener(this);
        mVerticalTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onTabSelected(TabView tab, int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabView tab, int position) {

    }

    @Override
    public void onTabReselected(TabView tab, int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mVerticalTabLayout.setTabSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}