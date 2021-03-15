package com.example.myapplication.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.example.myapplication.adapter.CommentListAdapter;
import com.example.myapplication.adapter.ResultListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.AnswerForm;
import com.example.myapplication.entity.AnswerResult;
import com.example.myapplication.entity.Question;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private TextView score_tv,totalScore_tv,time_tv,submit_tv;
    private String test_id;
    private List<AnswerResult> result = new ArrayList<>();
    private RecyclerView recyclerView;
    private ResultListAdapter mResultListAdapter;
    private Button home_btn,again_btn;
    private ImageView back_iv;
    private AnswerForm answerForm;
    private List<Question> questionList = new ArrayList<>();
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        score_tv = findViewById(R.id.score_tv);
        totalScore_tv = findViewById(R.id.totalScore_tv);
        time_tv = findViewById(R.id.time_tv);
        home_btn = findViewById(R.id.home_btn);
        again_btn = findViewById(R.id.again_btn);
        back_iv = findViewById(R.id.back);
        submit_tv = findViewById(R.id.submit_tv);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        score_tv.setText("本次测验得分："+bundle.getString("score")+"分");
        totalScore_tv.setText(bundle.getString("total_score"));
        time_tv.setText(bundle.getString("time"));
        test_id = bundle.getString("test_id");
        questionList = (List<Question>) getIntent().getSerializableExtra("quest_list");
        result = (List<AnswerResult>) getIntent().getSerializableExtra("result");
        answerForm = (AnswerForm) getIntent().getSerializableExtra("answer_form");
        recyclerView = findViewById(R.id.result_recyclerView);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mResultListAdapter = new ResultListAdapter();
        mResultListAdapter.setData(result);
        recyclerView.setAdapter(mResultListAdapter);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        again_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this,ScoreItemActivity.class);
                intent.putExtra("questionList", (Serializable) questionList);
                intent.putExtra("answerForm", (Serializable) answerForm);
                startActivityForResult(intent, 3);
            }
        });
    }

}