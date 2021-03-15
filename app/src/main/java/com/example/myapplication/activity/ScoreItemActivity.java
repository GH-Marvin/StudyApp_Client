package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.entity.AnswerForm;
import com.example.myapplication.entity.Choice;
import com.example.myapplication.entity.Question;
import com.example.myapplication.fragment.AnswerSheetFragment;
import com.example.myapplication.fragment.ScoreItemFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreItemActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private ImageView back_btn;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TextView current_total , submit_btn;
    public static List<Question> questionList;
    private List<ScoreItemFragment> fragmentList = new ArrayList<>();
    private Map<Integer, Choice> singleMap = new HashMap<>();
    private String test_id="";
    private String totalScore;
    private AnswerForm answerForm;
    private Map<Integer,List<Choice>> multipleMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_item);
        XUI.init(getApplication());
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        initData();
        current_total = findViewById(R.id.current_total);
        submit_btn = findViewById(R.id.submit_tv);
        current_total.setText("1/"+questionList.size());
        tabLayout = findViewById(R.id.toolbar_viewPager2);
        viewPager2 = findViewById(R.id.viewPager2);
        FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return questionList.size();
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                ArrayList<String> list = new ArrayList<>();
                //类型,题号,题目,分值,选项
                String finish_quest = answerForm.getAnswersheetResult();
                String answer_sheet = answerForm.getAnswersheet();
                String quest_id = String.valueOf(questionList.get(position).getQuestId());
                Question question = questionList.get(position);
                Collections.addAll(list,question.getType(),String.valueOf(position+1),question.getTitle(),String.valueOf(question.getScore()),String.valueOf(question.getQuestId()),finish_quest,answer_sheet,quest_id);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("data_list",list);
                ScoreItemFragment fragment = new ScoreItemFragment();
                fragmentList.add(fragment);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        viewPager2.setAdapter(fragmentStateAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(String.valueOf(position+1));
            }
        }).attach();
        tabLayout.addOnTabSelectedListener(this);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initData(){
        ArrayList<Question> questions = (ArrayList<Question>) getIntent().getSerializableExtra("questionList");
        questionList = questions;
        AnswerForm answerForm_this = (AnswerForm) getIntent().getSerializableExtra("answerForm");
        answerForm = answerForm_this;
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String current = String.valueOf(tab.getPosition()+1);
        String total = String.valueOf(questionList.size());
        current_total.setText(current+"/"+total);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}