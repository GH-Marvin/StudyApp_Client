package com.example.myapplication.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ChoiceListAdapter;
import com.example.myapplication.adapter.CommentListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.Choice;
import com.example.myapplication.entity.Course;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.widget.button.SmoothCheckBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnswerSheetFragment extends Fragment {
    private TextView quest_title;
    private TextView score_num;
    private TextView quest_type;
    private Map<String,List<Choice>> choiceMap;
    private RecyclerView recyclerView;
    public  ChoiceListAdapter mChoiceListAdapter = new ChoiceListAdapter();
    ArrayList<String> data_list;
    private ChoiceListAdapter getChoiceListAdapter(){
        return mChoiceListAdapter;
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            choiceMap = new HashMap<>();
            choiceMap = (Map<String, List<Choice>>) msg.obj;
            mChoiceListAdapter.setData(choiceMap);
            mChoiceListAdapter.setOnItemClickListener(new ChoiceListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                }
            });

            recyclerView.setAdapter(mChoiceListAdapter);
        }
    };
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quest_title = view.findViewById(R.id.quest_title);
        quest_type = view.findViewById(R.id.quest_type);
        score_num = view.findViewById(R.id.score_num);
        Bundle bundle = getArguments();
        data_list = bundle.getStringArrayList("data_list");
        recyclerView = view.findViewById(R.id.choice_recyclerView);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        initOptionList();
        quest_type.setText(data_list.get(0));
        if(data_list.get(0).equals("单选")){
        }else {
            quest_type.setBackgroundDrawable( view.getResources().getDrawable(R.drawable.multiple));
            quest_type.setTextColor(Color.rgb(239, 99, 93));
        }

        quest_title.setText(data_list.get(1)+"、"+data_list.get(2));
        score_num.setText("("+data_list.get(3)+"分)");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer_sheet, container, false);
    }

    private void initOptionList(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        int quest_id = Integer.valueOf(data_list.get(4));
        System.out.println("题目id："+quest_id);
        final Request request = new Request.Builder()
                .url(AppConfig.TEST_FIND_CHOICE_BY_QUEST_ID+"/"+String.valueOf(quest_id))
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","查询选项列表请求失败");
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","查询选项列表请求成功");
                String responseStr = response.body().string();
                Map<String,List<Choice>> entities = new HashMap<>();
                entities = com.alibaba.fastjson.JSONObject.parseObject(responseStr)
                        .entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> JSONObject.parseArray(String.valueOf(entry.getValue()), Choice.class)));
                Message msg = mHandler.obtainMessage();
                msg.obj = entities;
                mHandler.sendMessage(msg);
            }
        });
    }
}