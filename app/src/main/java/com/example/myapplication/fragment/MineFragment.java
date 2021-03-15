package com.example.myapplication.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.activity.BaseActivity;
import com.example.myapplication.activity.GoodsActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.PointRecordActivity;
import com.example.myapplication.activity.UserInfoActivity;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.util.DialogUtil;
import com.example.myapplication.util.XToastUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.XUI;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class MineFragment extends Fragment {
    private MyApplication myApplication;
    private boolean isGetData = false;
    private RelativeLayout login_area;
    private ImageView msg, head_iv;
    private TextView nickname_tv, point_tv;
    private LinearLayout answer_record, download, favorite;
    private RelativeLayout balance, discount_coupon, free, goods_shop;
    private LinearLayout option;
    private String nicknameValue = "";
    private boolean up = false;
    private Handler mUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UserInfo userInfo = (UserInfo) msg.obj;
            SharedPreferences sharedPre=getActivity().getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPre.edit();
            edit.putString("point",String.valueOf(userInfo.getPoint()));
            edit.apply();
            point_tv.setText(userInfo.getPoint()+".00");
        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            SharedPreferences sharedPre=getActivity().getSharedPreferences("config",MODE_PRIVATE);
            String nickname = sharedPre.getString("nickname", "");
            String head = sharedPre.getString("head_icon","");
            String point = sharedPre.getString("point","");
            nickname_tv = getActivity().findViewById(R.id.nickname_tv);
            head_iv = getActivity().findViewById(R.id.head_iv);
            if(nickname!=null&&!nickname.equals("")) {
                nickname_tv.setText(nickname.toString());
                Glide.with(getActivity()).load(head).into(head_iv);
                point_tv.setText(point+".00");
                Log.e("nicknameSet","页面设置昵称成功");
                nicknameValue = nickname;
            }
        }};
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            Message msg = mHandler.obtainMessage();
            msg.obj = nicknameValue;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //判断是否刷新
        if(up){
            if(nicknameValue!=null&&nicknameValue!=""){
                SharedPreferences sharedPre=getActivity().getSharedPreferences("config",MODE_PRIVATE);
                String nickname = sharedPre.getString("nickname", "");
                updatePoint(nickname);
            }
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onPause() {
        super.onPause();
        up=true;//不可见的时候将刷新开启
        isGetData=false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container,false);
        XUI.initTheme(getActivity());
        login_area = view.findViewById(R.id.login_area);
        goods_shop = view.findViewById(R.id.goods_shop);
        point_tv = view.findViewById(R.id.point_tv);
        goods_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GoodsActivity.class);
                startActivity(intent);
            }
        });
        login_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nicknameValue!=null&&nicknameValue!=""){
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    intent.putExtra("nickname",nicknameValue);
                    startActivityForResult(intent,3);
                }else {
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    intent.putExtra("nickname","");
                    startActivityForResult(intent,3);
                }

            }
        });
        option = view.findViewById(R.id.option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nicknameValue!=null&&nicknameValue!=""){
                    SharedPreferences sharedPre=getActivity().getSharedPreferences("config",MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPre.edit();
                    edit.clear().commit();
                    nicknameValue = "";
                    nickname_tv.setText("未登录");
                    head_iv.setImageDrawable(getContext().getDrawable(R.drawable.head));
                    point_tv.setText("0.00");
                    AnswerFragment.pass_tv.setText("0");
                    AnswerFragment.fail_tv.setText("0");
                    AnswerFragment.notry_tv.setText("0");
                    AnswerFragment.passNum = "";
                    AnswerFragment.failNum = "";
                    AnswerFragment.notryNum = "";
                    AnswerFragment.cur_title = "";
                    AnswerFragment.cur_type = "";
                    AnswerFragment.cur_test_layout.setVisibility(View.GONE);
                    XToastUtils.toast("账号退出成功！");
                } else {
                    XToastUtils.error("您还未登录，无需登出");
                }
            }
        });
        balance = view.findViewById(R.id.balance);
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nicknameValue!=""&&nicknameValue!=null){
                    Intent intent = new Intent(getActivity(), PointRecordActivity.class);
                    startActivity(intent);
                }else {
                    DialogUtil.dialogConfirm("你还未登录，无法访问！","确定",getActivity());
                }
            }
        });
        Message msg = mHandler.obtainMessage();
        msg.obj = nicknameValue;
        mHandler.sendMessage(msg);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        msg = getActivity().findViewById(R.id.msg);
        answer_record = getActivity().findViewById(R.id.answer_record);
        download = getActivity().findViewById(R.id.download);
        favorite = getActivity().findViewById(R.id.favorite);
        balance = getActivity().findViewById(R.id.balance);
        discount_coupon = getActivity().findViewById(R.id.discount_coupon);
        free = getActivity().findViewById(R.id.free);

    }
    private void updatePoint(String nickName){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.USER_INFO_FIND_BY_NICKNAME + "/" +nickName)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","查询用户信息请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","查询用户信息请求接收成功");
                String responseStr = response.body().string();
                UserInfo userInfo = JSON.parseObject(responseStr,UserInfo.class);
                Message msg = mUpdateHandler.obtainMessage();
                msg.obj = userInfo;
                mUpdateHandler.sendMessage(msg);
            }
        });
    }

}