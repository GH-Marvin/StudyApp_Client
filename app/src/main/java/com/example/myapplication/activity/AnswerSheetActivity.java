package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.example.myapplication.MyApplication;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.AnswerForm;
import com.example.myapplication.entity.AnswerResult;
import com.example.myapplication.entity.Choice;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.fragment.AnswerSheetFragment;
import com.example.myapplication.R;
import com.example.myapplication.entity.Question;
import com.example.myapplication.util.AnswerSheetUtil;
import com.example.myapplication.util.DialogUtil;
import com.example.myapplication.util.XToastUtils;
import com.example.myapplication.vo.ResultVO;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xuexiang.xui.XUI.getContext;

public class AnswerSheetActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    private ImageView back_btn;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TextView current_total , submit_btn;
    public static List<Question> questionList;
    private List<AnswerSheetFragment> fragmentList = new ArrayList<>();
    private Map<Integer, Choice> singleMap = new HashMap<>();
    private String test_id="";
    private String totalScore;
    private Map<Integer,List<Choice>> multipleMap = new HashMap<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            AnswerForm answerForm = (AnswerForm) msg.obj;
            Intent pre_intent = getIntent();
            Bundle bundle = pre_intent.getExtras();
            totalScore = bundle.getString("total_score");
            Intent intent = new Intent(AnswerSheetActivity.this,ScoreActivity.class);
            intent.putExtra("time",answerForm.getSaveTime());
            intent.putExtra("score",String.valueOf(answerForm.getScore()));
            intent.putExtra("test_id",test_id);
            intent.putExtra("total_score",totalScore);
            intent.putExtra("quest_list", (Serializable) questionList);
            intent.putExtra("answer_form",(Serializable) answerForm);
            String string = answerForm.getAnswersheetResult();
            String[] strings = string.split(";");
            List<AnswerResult> resultList = new ArrayList<>();
            for(int i = 0 ;i<strings.length;i++){
                AnswerResult answerResult = new AnswerResult();
                String[] temp = strings[i].split(":");
                answerResult.setQuestion(questionList.get(i));
                answerResult.setChecked(Integer.valueOf(temp[1]));
                resultList.add(answerResult);
            }
            for(int j=strings.length;j<questionList.size();j++){
                AnswerResult answerResult = new AnswerResult();
                answerResult.setQuestion(questionList.get(j));
                answerResult.setChecked(0);
                resultList.add(answerResult);
            }
            intent.putExtra("result" , (Serializable) resultList);
            startActivityForResult(intent, 3);
        }
    };
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }if(requestCode == 3 && resultCode == RESULT_CANCELED){
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_sheet);
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
                Question question = questionList.get(position);
                Collections.addAll(list,question.getType(),String.valueOf(position+1),question.getTitle(),String.valueOf(question.getScore()),String.valueOf(question.getQuestId()));
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("data_list",list);
                AnswerSheetFragment fragment = new AnswerSheetFragment();
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
                for(AnswerSheetFragment fragment:fragmentList){
                    List<Choice> single = fragment.mChoiceListAdapter.getSingle();
                    List<Choice> multiple = fragment.mChoiceListAdapter.getMultipleChoiceList();
                    if(single.size()!=0){
                        singleMap.put(single.get(0).getQuestId(),single.get(0));
                        Log.e("maps",String.valueOf(singleMap.size()));
                    }else if(multiple.size()!=0){
                        multipleMap.put(multiple.get(0).getQuestId(),multiple);
                        Log.e("mapm",String.valueOf(multipleMap.size()));
                    }

                }
                submitDialog();

            }
        });

        back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTwo();
            }
        });
    }
    private void submitDialog(){
        int num = questionList.size() - singleMap.size() - multipleMap.size();
        String msg = "";
        if(num>0){
            msg = "你还有"+num+"道题目没有完成，现在提交么？";
        }else if(num == 0){
            msg = "你已完成所有题目，现在提交么？";
        }
        AlertDialog  alertDialog =   new AlertDialog.Builder(this).setTitle("提交答卷")
                //设置消息内容
                .setMessage(msg)
                //设置按钮
                .setPositiveButton("现在提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        submitPost();

                    }
                }).setNegativeButton("继续测验", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        DialogUtil.buildDialogStyle(alertDialog);
        alertDialog.show();

    }
    private void submitPost(){
        SharedPreferences sharedPre=getSharedPreferences("config",MODE_PRIVATE);
        String user_id = sharedPre.getString("user_id","");
        if(user_id==null||user_id==""){
            user_id = "-1";
        }
        String cast = AnswerSheetUtil.castToString(singleMap,multipleMap);
        test_id = getIntent().getExtras().getString("test_id");
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        Map<String,String> map = new HashMap<>();
        map.put("answersheet",cast);
        map.put("test_id",test_id);
        map.put("user_id",user_id);
        String str = JSON.toJSONString(map);
        System.out.println(str);
        RequestBody body = RequestBody.create(JSON_TYPE, str);
        final Request request = new Request.Builder()
                .url(AppConfig.USER_SCORE_SUBMIT)
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","答卷请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","答卷请求接收成功");
                String responseStr = response.body().string();
                AnswerForm answerForm = new AnswerForm();
                answerForm =  JSON.parseObject(responseStr,AnswerForm.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = answerForm;
                mHandler.sendMessage(msg);
            }
        });
    }
    private void showTwo() {
        AlertDialog  alertDialog = new AlertDialog.Builder(this).setTitle("离开")
                .setMessage("离开计时将不会停止，计时结束将自动提交现有答卷，确认离开？").setPositiveButton("继续测验", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("离开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        dialogInterface.dismiss();
                    }
                }).create();
        DialogUtil.buildDialogStyle(alertDialog);
        alertDialog.show();

    }
    private void initData(){
        ArrayList<Question> questions = (ArrayList<Question>) getIntent().getSerializableExtra("questionList");
        questionList = questions;
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