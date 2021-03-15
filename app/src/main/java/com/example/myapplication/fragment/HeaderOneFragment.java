package com.example.myapplication.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.myapplication.activity.BaseActivity;
import com.example.myapplication.activity.MovieActivity;
import com.example.myapplication.R;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.Course;
import com.example.myapplication.util.JsonUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.thecode.aestheticdialogs.AestheticDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HeaderOneFragment extends Fragment {
    private LinearLayout card_lay1;
    private LinearLayout list;
    private Map<String,List<Course>> courseMap;
    private Bundle bundle;
    private Intent intent;
    private String videoUrl;
    private ArrayList<Course> course_list;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            courseMap = (Map<String, List<Course>>) msg.obj;
            if(null != courseMap){
                Bundle bundle = getArguments();
                String typeInt = bundle.getString("type");
                String type = getType(typeInt);
                List<Course> current_courses = courseMap.get(type);
                for(Course c: current_courses){
                    System.out.println(c.getCourseId());
                    initCard(c.getCourseId(),c.getImgUrl(),c.getTitle(),c.getContent(),c.getVideoId());
                }
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header_one, container,false);
        list = view.findViewById(R.id.list_layout);
        initData();
        return view;
    }
    private void initCard(Integer id , final String imgUrl, String title, String content, final Integer videoId) {

        TextView topTextView = new TextView(getActivity());
        TextView bottomTextView = new TextView(getActivity());
        LinearLayout card_horizon = new LinearLayout(getActivity());
        card_horizon.setId(id);
        LinearLayout card_vertical = new LinearLayout(getActivity());
        ImageView card_img = new ImageView(getActivity());
        Glide.with(getActivity()).load(imgUrl).into(card_img);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(420, 260);
        card_img.setLayoutParams(params);
        card_img.setScaleType(ImageView.ScaleType.FIT_XY);
        card_horizon.setOrientation(LinearLayout.HORIZONTAL);
        card_horizon.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) card_horizon.getLayoutParams();
        params1.setMargins(0,30,0,30);
        card_vertical.setOrientation(LinearLayout.VERTICAL);
        card_vertical.setPadding(20,0,0,0);
        card_vertical.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        topTextView.setText(title);
        topTextView.setTextSize(14);
        topTextView.getPaint().setFakeBoldText(true);
        topTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,210));
        topTextView.setTextColor(Color.rgb(69, 69, 69));
        bottomTextView.setText(content);
        bottomTextView.setTextSize(12);
        bottomTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,50));
        bottomTextView.setTextColor(Color.rgb(194, 194, 194));
        card_horizon.addView(card_img);
        card_vertical.addView(topTextView);
        card_vertical.addView(bottomTextView);
        card_horizon.addView(card_vertical);
        list.addView(card_horizon);
        card_horizon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), MovieActivity.class);
                intent.putExtra("video",String.valueOf(videoId));
                intent.putExtra("image",imgUrl);
                startActivity(intent);

            }
        });
    }
    private void initData(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(AppConfig.COURSE_FIND_ALL_TO_MAP).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","请求失败");
                System.out.println(request);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                System.out.println("课程和视频get请求成功");
                Log.e("link","请求成功");
                Map<String,List<Course>> courseEntities = new HashMap<>();
                courseEntities = com.alibaba.fastjson.JSONObject.parseObject(responseStr)
                        .entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> JSONObject.parseArray(String.valueOf(entry.getValue()), Course.class)));

                Message msg = mHandler.obtainMessage();
                msg.obj = courseEntities;
                mHandler.sendMessage(msg);

            }
        });



    }
    private String getType(String type){
        switch (type){
            case "0":
                return "python";
            case "1":
                return "java";
            case "2":
                return "c";
            default:
                return "css";
        }
    }
}