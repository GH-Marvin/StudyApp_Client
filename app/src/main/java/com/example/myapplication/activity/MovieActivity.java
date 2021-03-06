package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CommentListAdapter;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.AnswerForm;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.CommentVO;
import com.example.myapplication.entity.Question;
import com.example.myapplication.entity.UserTest;
import com.example.myapplication.entity.Video;
import com.example.myapplication.util.DialogUtil;
import com.example.myapplication.util.XToastUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MovieActivity extends AppCompatActivity implements ShineButton.OnCheckedChangeListener{
    private JCVideoPlayerStandard  video1;
    private ShineButton favorite_sb;
    private ShineButton good_sb;
    private ShineButton bad_sb;
    private ImageView comment_iv;
    private ImageView back_btn;
    private RecyclerView recyclerView;
    private CommentListAdapter mCommentListAdapter;
    private String videoId;
    private String imageUrl;
    private String user_id;
    private  String comment = "";
    private boolean up = false;
    //????????????

    @Override
    public void onResume() {
        super.onResume();
        //??????????????????
        if(up){
            Log.e("answerFragment","Refresh_OK");
            SharedPreferences sharedPre = getSharedPreferences("config",MODE_PRIVATE);
            user_id = sharedPre.getString("user_id","");
            initCommentGet();
            up=false;//??????????????????????????????????????????
        }
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Video video = (Video) msg.obj;
            JCVideoPlayerStandard player = findViewById(R.id.video1);
            boolean setUp = player.setUp(video.getUrl(), JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            if (setUp) {
                Glide.with(MovieActivity.this).load(imageUrl).into(player.thumbImageView);
            }
        }
    };
    private Handler mCommentsHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            List<CommentVO> commentVOList = (List<CommentVO>) msg.obj;
            List<Comment> commentList = commentVOList.stream()
                    .map(e -> new Comment(
                            e.getUserIcon(),
                            e.getNickname(),
                            e.getTime(),
                            e.getContent(),
                            e.getGoodNum()
                    )).collect(Collectors.toList());
            mCommentListAdapter = new CommentListAdapter();
            mCommentListAdapter.setData(commentList);
            recyclerView.setAdapter(mCommentListAdapter);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        XUI.initTheme(this);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        SharedPreferences sharedPre = getSharedPreferences("config",MODE_PRIVATE);
        user_id = sharedPre.getString("user_id","");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        videoId= bundle.getString("video");
        imageUrl = bundle.getString("image");
        recyclerView = findViewById(R.id.recyclerView);
        //?????????????????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        initVideoGet();
        initCommentGet();
        initButton();
        back_btn = findViewById(R.id.backToHome);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void initVideoGet(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.VIDEO_FIND_BY_ID+"/"+videoId)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","??????id???"+videoId+"?????????????????????");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","????????????????????????");
                String responseStr = response.body().string();
                Video video =  JSON.parseObject(responseStr,Video.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = video;
                mHandler.sendMessage(msg);
            }
        });
    }
    private void initCommentGet(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.COMMENT_FIND_BY_ID+"/"+videoId)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","??????????????????????????????");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","????????????????????????");
                String responseStr = response.body().string();
                List<CommentVO> list = JSONArray.parseArray(responseStr,CommentVO.class);
                Message msg = mCommentsHandler.obtainMessage();
                msg.obj = list;
                mCommentsHandler.sendMessage(msg);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        up=true;//?????????????????????????????????
        JCVideoPlayer.releaseAllVideos();
    }


    @Override
    public void onCheckedChanged(ShineButton shineButton, boolean isChecked) {

    }

    private void initButton(){

        favorite_sb = findViewById(R.id.favorite);
        favorite_sb.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(ShineButton shineButton, boolean isChecked) {
                if(isChecked){
                    XToastUtils.toast("???????????????");
                }else {
                    XToastUtils.toast("????????????");
                }
            }
        });
        good_sb = findViewById(R.id.good);
        good_sb.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(ShineButton shineButton, boolean isChecked) {
                if(isChecked){
                    XToastUtils.toast("??????????????????????????????");
                }else {
                    XToastUtils.toast("????????????");
                }
            }
        });
        bad_sb = findViewById(R.id.bad);
        bad_sb.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(ShineButton shineButton, boolean isChecked) {
                if(isChecked){
                    XToastUtils.toast("????????????");
                }else {
                    XToastUtils.toast("???????????????");
                }
            }
        });
        comment_iv = findViewById(R.id.comment_iv);
        comment_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(MovieActivity.this)
                        .title("??????")
                        .inputRangeRes(2, 20, R.color.normal_blue)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("????????????????????????~", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                            }
                        })
                        .positiveText("??????")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String input =  dialog.getInputEditText().getText().toString();
                                displayComment(input,user_id,videoId);
                                XToastUtils.toast("???????????????");
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }
    private void displayComment(String comment,String user_id,String video_id){
        if(user_id!=null&&user_id!=""){
            OkHttpClient mOkHttpClient = new OkHttpClient();
            MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
            Map<String,String> map = new HashMap<>();
            map.put("user_id",user_id);
            map.put("video_id",video_id);
            map.put("comment",comment);
            String str = JSON.toJSONString(map);
            RequestBody body = RequestBody.create(JSON_TYPE, str);
            final Request request = new Request.Builder()
                    .url(AppConfig.COMMENT_UPDATE)
                    .post(body)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e("link","????????????");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.e("link","????????????");
                    String responseStr = response.body().string();
                    List<CommentVO> list = JSONArray.parseArray(responseStr,CommentVO.class);
                    Message msg = mCommentsHandler.obtainMessage();
                    msg.obj = list;
                    mCommentsHandler.sendMessage(msg);
                }
            });
        }
    }
}