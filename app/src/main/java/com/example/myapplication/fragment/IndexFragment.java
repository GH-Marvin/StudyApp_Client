package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.activity.SearchActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseBanner;
import com.xuexiang.xui.widget.tabbar.EasyIndicator;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment {
    private SimpleImageBanner mSimpleImageBanner;
    private String TAG="IndexActivity";
    private EditText search;
    private TabLayout mTabLayout;
    private static String[] urls = new String[]{//640*360 360/640=0.5625
            "https://edu-image.nosdn.127.net/4482ad0662ca43daaa8093871159d1c1.png?imageView&quality=100",
            "https://edu-image.nosdn.127.net/9c70d66f8538409ba4a2095757f9cc82.png?imageView&quality=100&thumbnail=776y360",//无心法师:生死离别!月牙遭虐杀
            "https://edu-image.nosdn.127.net/fe917febb65b4017b12b1b814ad85111.png?imageView&quality=100",//花千骨:尊上沦为花千骨
            "https://edu-image.nosdn.127.net/0e16854ca8ae411daf18e981359a1f9c.png?imageView&quality=100",//综艺饭:胖轩偷看夏天洗澡掀波澜
            "https://edu-image.nosdn.127.net/62dcb5d34c094eeea23896d12ce50b20.png?imageView&quality=100",//碟中谍4:阿汤哥高塔命悬一线,超越不可能
    };
    private ViewPager2 mViewPager;
    private static String[] titles = new String[]{
            "学习办公",
            "全栈开发",
            "编程语言",
            "网页设计"
    };
    private List<BannerItem> mData;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        mSimpleImageBanner = view.findViewById(R.id.banner);
        sib_simple_usage();
        mViewPager =  view.findViewById(R.id.toolbar_viewPager);
        mTabLayout =  view.findViewById(R.id.tab_layout);
        search = view.findViewById(R.id.search);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    search.clearFocus();
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                }
            }
        });
        FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return 4;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                HeaderOneFragment fragment = new HeaderOneFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type",String.valueOf(position));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container,false);
        return view;
    }

    private void sib_simple_usage() {
        mSimpleImageBanner.setSource(mData)
                .setOnItemClickListener(new BaseBanner.OnItemClickListener<BannerItem>() {
                    @Override
                    public void onItemClick(View view, BannerItem item, int position) {
                    }
                })
                .setIsOnePageLoop(false)//设置当页面只有一条时，是否轮播
                .startScroll();//开始滚动
        mSimpleImageBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initData(){
        mData = new ArrayList<>(urls.length);

        for (int i = 0; i < urls.length; i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = urls[i];
            mData.add(item);
        }
    }
}
