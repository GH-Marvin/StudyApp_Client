package com.example.myapplication.fragment;

import android.content.Intent;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.R;
import com.example.myapplication.adapter.AnswerListAdapter;
import com.example.myapplication.adapter.ChoiceListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.Choice;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ScoreItemFragment extends Fragment {
    private TextView quest_title;
    private TextView score_num;
    private TextView quest_type,answer_tv;
    private Map<String, List<Choice>> choiceMap;
    private RecyclerView recyclerView;
    public AnswerListAdapter mAnswerListAdapter = new AnswerListAdapter();
    private String answer_sheet;
    private String answer_sheet_result,quest_id;
    ArrayList<String> data_list;
    private ImageView check_iv;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            choiceMap = new HashMap<>();
            choiceMap = (Map<String, List<Choice>>) msg.obj;
            String true_answer="正确答案：";
            for(Choice choice:choiceMap.get(data_list.get(0))){
                if(choice.getIsAnswer()==1){
                    true_answer = true_answer + choice.getOptType() + "、";
                }
            }
            answer_tv.setText(true_answer.substring(0,true_answer.length()-1));
            mAnswerListAdapter.setData(choiceMap, answer_sheet, answer_sheet_result);
            recyclerView.setAdapter(mAnswerListAdapter);

        }
    };
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quest_title = view.findViewById(R.id.quest_title);
        quest_type = view.findViewById(R.id.quest_type);
        score_num = view.findViewById(R.id.score_num);
        check_iv = view.findViewById(R.id.check_iv);
        answer_tv = view.findViewById(R.id.answer_tv);
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
        answer_sheet = data_list.get(6);
        answer_sheet_result = data_list.get(5);
        quest_id = data_list.get(7);
        String[] answer_sheet_result_split = answer_sheet_result.split(";");
        if(!answer_sheet_result.equals(";")){
            for(int i=0;i<answer_sheet_result_split.length;i++){
                String[] quest = answer_sheet_result_split[i].split(":");
                if(quest[0].equals(quest_id)){
                    if(quest[1].equals("0")){
                        check_iv.setBackground(getActivity().getDrawable(R.color.choice_red));
                        break;
                    }if(quest[1].equals("1")){
                        check_iv.setBackground(getActivity().getDrawable(R.color.choice_green));
                        break;
                    }
                }else {
                    check_iv.setBackground(getActivity().getDrawable(R.color.choice_red));
                }
            }
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_item, container, false);
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