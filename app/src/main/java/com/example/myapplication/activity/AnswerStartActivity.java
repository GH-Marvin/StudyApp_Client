package com.example.myapplication.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CommentListAdapter;
import com.example.myapplication.adapter.ScoreListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.AnswerForm;
import com.example.myapplication.entity.Question;
import com.example.myapplication.entity.Test;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.fragment.MineFragment;
import com.example.myapplication.util.XToastUtils;
import com.example.myapplication.vo.ResultVO;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xuexiang.xui.XUI.getContext;

public class AnswerStartActivity extends AppCompatActivity {
    private ImageView back_btn;
    private Button start_btn;
    private TextView title_tv,score_tv,description_tv,num_tv,time_tv,degree_tv,deadline_tv;
    private String test_id;
    private List<Question> questionList;
    private String score;
    private String user_id;
    private RecyclerView recyclerView;
    private ScoreListAdapter mScoreListAdapter;
    private boolean up = false;
    //刷新监听
    @Override
    public void onPause() {
        super.onPause();
        up=true;//不可见的时候将刷新开启
    }

    @Override
    public void onResume() {
        super.onResume();
        //判断是否刷新
        if(up){
            Log.e("answerFragment","Refresh_OK");
            SharedPreferences sharedPre = getSharedPreferences("config",MODE_PRIVATE);
            user_id = sharedPre.getString("user_id","");
            initScoreList(user_id);
            up=false;//刷新一次即可，不需要一直刷新
        }
    }
    private Handler mScoreHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            List<AnswerForm> answerFormList = (List<AnswerForm>) msg.obj;
            Log.e("link",String.valueOf(answerFormList.size()));
            if(answerFormList!=null&&answerFormList.size()!=0){
                mScoreListAdapter = new ScoreListAdapter();
                mScoreListAdapter.setData(answerFormList);
                recyclerView.setAdapter(mScoreListAdapter);
                mScoreListAdapter.setOnItemClickListener(new ScoreListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("link","跳转至查看答卷页面");
                        Intent intent = new Intent(AnswerStartActivity.this,ScoreItemActivity.class);
                        intent.putExtra("questionList", (Serializable) questionList);
                        intent.putExtra("answerForm", (Serializable) mScoreListAdapter.getData(position));
                        startActivityForResult(intent, 3);
                    }
                });
            }
        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            questionList = new ArrayList<>();
            questionList = (List<Question>) msg.obj;

        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            finish();
        }else if (requestCode == 3 && requestCode == RESULT_CANCELED){

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_start);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        recyclerView = findViewById(R.id.recyclerView);
        initTextView();
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences sharedPre = getSharedPreferences("config",MODE_PRIVATE);
        user_id = sharedPre.getString("user_id","");
        initQuestionList();
        initScoreList(user_id);
        back_btn = findViewById(R.id.backToAnswer);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        start_btn = findViewById(R.id.start);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questionList!=null){
                    Intent intent = new Intent(AnswerStartActivity.this, AnswerSheetActivity.class);
                    intent.putExtra("questionList", (Serializable) questionList);
                    intent.putExtra("test_id",test_id);
                    intent.putExtra("total_score",score);
                    startActivityForResult(intent, 3);
                }
            }
        });


    }
    private void initTextView(){
        title_tv = findViewById(R.id.title_tv);
        score_tv = findViewById(R.id.score_tv);
        description_tv = findViewById(R.id.description_tv);
        num_tv = findViewById(R.id.num_tv);
        time_tv = findViewById(R.id.time_tv);
        degree_tv = findViewById(R.id.degree_tv);
        deadline_tv = findViewById(R.id.deadline_tv);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        test_id = bundle.getString("test_id");
        Log.e("link",test_id);
        String title = bundle.getString("title");
        score = bundle.getString("score");
        String description = bundle.getString("description");
        String num = bundle.getString("num");
        String time = bundle.getString("time");
        String degree = bundle.getString("degree");
        String deadline = bundle.getString("deadline");
        title_tv.setText(title);
        score_tv.setText(score);
        description_tv.setText(description);
        num_tv.setText(num);
        time_tv.setText(time);
        degree_tv.setText(degree);
        deadline_tv.setText(deadline);

    }
    private void initQuestionList(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.TEST_FIND_QUESTION_BY_TEST_ID+"/"+test_id)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","查询id为"+test_id+"的测试题目列表请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","查询题目列表请求成功");
                String responseStr = response.body().string();
                List<Question> testList = new ArrayList<>();
                testList = JSONArray.parseArray(responseStr,Question.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = testList;
                mHandler.sendMessage(msg);
            }
        });
    }
    private void initScoreList(String user_id){
        if(user_id!=null&&user_id!=""){
            OkHttpClient mOkHttpClient = new OkHttpClient();
            MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
            Map<String,String> map = new HashMap<>();
            map.put("user_id",user_id);
            map.put("test_id",test_id);
            String str = JSON.toJSONString(map);
            RequestBody body = RequestBody.create(JSON_TYPE, str);
            final Request request = new Request.Builder()
                    .url(AppConfig.SCORE_LIST_GET)
                    .post(body)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e("link","请求失败");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.e("link","接收成功");
                    String responseStr = response.body().string();
                    List<AnswerForm> answerFormList = new ArrayList<>();
                    answerFormList = JSON.parseArray(responseStr,AnswerForm.class);
                    Message msg = mScoreHandler.obtainMessage();
                    msg.obj = answerFormList;
                    mScoreHandler.sendMessage(msg);
                }
            });
        }
    }
}