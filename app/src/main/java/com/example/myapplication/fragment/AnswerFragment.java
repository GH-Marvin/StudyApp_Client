package com.example.myapplication.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.MyApplication;
import com.example.myapplication.activity.AnswerStartActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.BaseActivity;
import com.example.myapplication.activity.SearchActivity;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.Course;

import com.example.myapplication.entity.Question;
import com.example.myapplication.entity.Test;
import com.example.myapplication.entity.UserTest;
import com.example.myapplication.util.XToastUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class AnswerFragment extends Fragment {
    private MyApplication myApplication;
    private EditText search;
    public static TextView pass_tv,fail_tv,notry_tv,cur_title_tv,cur_type_tv;
    public static LinearLayout test_list,cur_test_layout;
    private List<Test> tests;
    private String user_id;
    public static String passNum,failNum,notryNum,cur_title,cur_type;
    private ArrayList<Test> cur_tests;
    private UserTest userTest;
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
            SharedPreferences sharedPre=getActivity().getSharedPreferences("config",MODE_PRIVATE);
            user_id = sharedPre.getString("user_id","");
            initCurrentTest(user_id);
            up=false;//刷新一次即可，不需要一直刷新
        }
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            tests = new ArrayList<>();
            tests = (List<Test>) msg.obj;
            if(null != tests){
                for(Test t: tests){
                    initCard(t.getTest_id(),t.getTitle(),t.getDescription(),t.getScore(),t.getHard(),t.getQuest_num(),t.getLimit_time(),t.getLimit_degree(),t.getDeadline());
                }
            }
        }
    };
    private Handler mCurrentHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            userTest = (UserTest) msg.obj;
            SharedPreferences sharedPre=getActivity().getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPre.edit();
            if(userTest!=null) {
                edit.putString("pass", String.valueOf(userTest.getPassNum()));
                edit.putString("fail", String.valueOf(userTest.getFailNum()));
                edit.putString("notry", String.valueOf(userTest.getNotryNum()));
                if(userTest.getTest()!=null){
                    edit.putString("cur_title",String.valueOf(userTest.getTest().getTitle()));
                    edit.putString("cur_type",String.valueOf(userTest.getTest().getHard()));
                }
            }
            edit.apply();
            Message msg1 = mCurrentTestHandler.obtainMessage();
            mCurrentTestHandler.sendMessage(msg1);
        }
    };
    private Handler mCurrentTestHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            SharedPreferences sharedPre=getActivity().getSharedPreferences("config",MODE_PRIVATE);
            passNum = sharedPre.getString("pass", "");
            failNum = sharedPre.getString("fail","");
            notryNum = sharedPre.getString("notry","");
            cur_title = sharedPre.getString("cur_title","");
            cur_type = sharedPre.getString("cur_type","");
            if(passNum!=null&&passNum!=""){
                pass_tv.setText(passNum);
                fail_tv.setText(failNum);
                notry_tv.setText(notryNum);
            }

            if(cur_title!=null&&cur_title!="") {
                cur_test_layout.setVisibility(View.VISIBLE);
                cur_title_tv.setText(cur_title);
                cur_type_tv.setText(cur_type);
                switch (cur_type) {
                    case "简单":
                        cur_type_tv.setTextColor(Color.rgb(15, 160, 123));
                        break;
                    case "中等":
                        cur_type_tv.setTextColor(Color.rgb(243, 151, 102));
                        break;
                    default:
                        cur_type_tv.setTextColor(Color.rgb(239, 99, 93));
                        break;
                }
                cur_test_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AnswerStartActivity.class);
                        Test test = userTest.getTest();
                        Log.e("link",test.getTest_id().toString());
                        intent.putExtra("test_id",test.getTest_id().toString());
                        String thisTitle = test.getTest_id()+"."+test.getTitle();
                        intent.putExtra("title", thisTitle);
                        String thisScore = "(总分"+test.getScore()+")";
                        intent.putExtra("score", thisScore);
                        intent.putExtra("description",test.getDescription());
                        String thisNum = test.getQuest_num()+"道题";
                        intent.putExtra("num",thisNum);
                        intent.putExtra("time",test.getLimit_time());
                        String thisDegree = "允许尝试"+test.getLimit_degree()+"次";
                        intent.putExtra("degree",thisDegree);
                        intent.putExtra("deadline",test.getDeadline());
                        startActivity(intent);
                    }
                });
            }
        }
    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = view.findViewById(R.id.search);
        test_list = view.findViewById(R.id.test_list);
        pass_tv = view.findViewById(R.id.pass_tv);
        fail_tv = view.findViewById(R.id.fail_tv);
        notry_tv = view.findViewById(R.id.notry_tv);
        cur_test_layout = view.findViewById(R.id.cur_test_layout);
        cur_test_layout.setVisibility(View.GONE);
        cur_title_tv = view.findViewById(R.id.cur_title_tv);
        cur_type_tv = view.findViewById(R.id.cur_type_tv);
        initData();
        SharedPreferences sharedPre=getActivity().getSharedPreferences("config",MODE_PRIVATE);
        user_id = sharedPre.getString("user_id","");
        initCurrentTest(user_id);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    search.clearFocus();
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer, container, false);
    }

    private void initCard( int id, String title, final String description, final int score, String hard, final int quest_num, final String limit_time, final int limit_degree, final String deadline) {

        TextView topTextView = new TextView(getActivity());
        TextView bottomTextView = new TextView(getActivity());
        LinearLayout card_horizon = new LinearLayout(getActivity());
        LinearLayout image_horizon = new LinearLayout(getActivity());
        LinearLayout card_vertical = new LinearLayout(getActivity());
        ImageView card_img = new ImageView(getActivity());

        card_img.setBackground(getContext().getDrawable(R.drawable.ic_minus_circle));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        card_img.setLayoutParams(params);

        card_horizon.setOrientation(LinearLayout.HORIZONTAL);
        card_horizon.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) card_horizon.getLayoutParams();
        params1.setMargins(0,2,0,2);
        card_horizon.setPadding(65,45,0,45);
        card_horizon.setBackgroundColor(Color.WHITE);

        image_horizon.setOrientation(LinearLayout.VERTICAL);
        image_horizon.setLayoutParams(new ViewGroup.MarginLayoutParams(45, ViewGroup.LayoutParams.WRAP_CONTENT));
        image_horizon.setPadding(0,45,0,0);


        card_vertical.setOrientation(LinearLayout.VERTICAL);
        card_vertical.setPadding(60,0,0,0);
        card_vertical.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        final String thisTitle = id+"."+title;
        topTextView.setText(thisTitle);
        topTextView.setTextSize(15);
        topTextView.getPaint().setFakeBoldText(true);
        topTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,90));
        topTextView.setTextColor(Color.rgb(0, 0, 0));


        bottomTextView.setText(hard);
        bottomTextView.setTextSize(13);
        bottomTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,50));
        switch (hard){
            case "简单":
                bottomTextView.setTextColor(Color.rgb(15, 160, 123));
                break;
            case "中等":
                bottomTextView.setTextColor(Color.rgb(243, 151, 102));
                break;
            default:
                bottomTextView.setTextColor(Color.rgb(239, 99, 93));
                break;
        }


        image_horizon.addView(card_img);
        card_horizon.addView(image_horizon);
        card_vertical.addView(topTextView);
        card_vertical.addView(bottomTextView);
        card_horizon.addView(card_vertical);
        test_list.addView(card_horizon);


        card_horizon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPre=getActivity().getSharedPreferences("config",MODE_PRIVATE);
                String user_id = sharedPre.getString("user_id", "");
                if(user_id==null||user_id.equals("")){
                    new MaterialDialog.Builder(getActivity())
                            .content("您还未登录，继续测验将不会保存您的答题信息，确认测验？")
                            .positiveText("继续测验")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    Intent intent = new Intent(getActivity(), AnswerStartActivity.class);
                                    intent.putExtra("test_id",String.valueOf(id));
                                    intent.putExtra("title", thisTitle);
                                    String thisScore = "(总分"+score+")";
                                    intent.putExtra("score", thisScore);
                                    intent.putExtra("description",description);
                                    String thisNum = quest_num+"道题";
                                    intent.putExtra("num",thisNum);
                                    intent.putExtra("time",limit_time);
                                    String thisDegree = "允许尝试"+limit_degree+"次";
                                    intent.putExtra("degree",thisDegree);
                                    intent.putExtra("deadline",deadline);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .negativeText("取消")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }else {
                    Intent intent = new Intent(getActivity(), AnswerStartActivity.class);
                    intent.putExtra("test_id",String.valueOf(id));
                    intent.putExtra("title", thisTitle);
                    String thisScore = "(总分"+score+")";
                    intent.putExtra("score", thisScore);
                    intent.putExtra("description",description);
                    String thisNum = quest_num+"道题";
                    intent.putExtra("num",thisNum);
                    intent.putExtra("time",limit_time);
                    String thisDegree = "允许尝试"+limit_degree+"次";
                    intent.putExtra("degree",thisDegree);
                    intent.putExtra("deadline",deadline);
                    startActivity(intent);
                }

            }
        });
    }
   private void initData(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(AppConfig.TEST_FIND_ALL).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","查询测试列表请求失败");
                System.out.println(request);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Log.e("link","查询测试列表请求成功");
                List<Test> testList = new ArrayList<>();
                testList = JSONArray.parseArray(responseStr,Test.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = testList;
                mHandler.sendMessage(msg);

            }
        });
    }
    private void initCurrentTest(String user_id){
        if(user_id!=null&&user_id!="") {
            OkHttpClient mOkHttpClient = new OkHttpClient();
            final Request request = new Request.Builder().url(AppConfig.TEST_PAGE_INIT + "/" +user_id).build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e("link", "查询测试列表请求失败");
                    System.out.println(request);
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Response response) throws IOException {
                    String responseStr = response.body().string();
                    Log.e("link", "查询测试列表请求成功");
                    UserTest userTest = JSON.parseObject(responseStr,UserTest.class);
                    Message msg = mCurrentHandler.obtainMessage();
                    msg.obj = userTest;
                    mCurrentHandler.sendMessage(msg);
                }
            });
        }
    }
}