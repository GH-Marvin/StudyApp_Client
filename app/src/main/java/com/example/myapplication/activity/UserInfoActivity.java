package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.constant.AppConfig;
import com.example.myapplication.entity.CommentVO;
import com.example.myapplication.entity.ProvinceInfo;
import com.example.myapplication.entity.UserInfo;
import com.example.myapplication.util.DialogUtil;
import com.example.myapplication.util.UserInfoUtil;
import com.example.myapplication.util.XToastUtils;
import com.example.myapplication.vo.ResultVO;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {
    private ImageView back_btn;
    private RadiusImageView headIcon_iv;
    private RelativeLayout show_type;
    private TextView checkbox_tv,nickName_tv,realName_tv,sex_tv,age_tv,birthday_tv,address_tv,identity_tv,phone_tv,point_tv;
    private String nickName;
    private Button nickName_btn,realName_btn;
    private UserInfo userInfo;
    private String user_id,input;
    private String[] mSexOption;
    private int sexSelectOption = 0;
    private String[] mAgeOption;
    private int ageSelectOption = 0;
    private TimePickerView mDatePicker;
    private List<ProvinceInfo> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    private boolean mHasLoaded;
    private Handler mJudgeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(msg.obj.equals("success")){
               updateUserInfo(input, user_id, "nickname");
           }else {
               XToastUtils.error(msg.obj.toString());
               input = "";
           }
        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            userInfo = (UserInfo) msg.obj;
            user_id = String.valueOf(userInfo.getUserId());
            nickName = userInfo.getNickname();
            setUserInfoView(userInfo);
            SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPre.edit();
            edit.putString("nickname",userInfo.getNickname());
            edit.apply();
            setResult(RESULT_OK);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XUI.initTheme(this);
        setContentView(R.layout.activity_user_info);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        StatusBarUtils.setStatusBarLightMode(this);
        show_type = findViewById(R.id.show_type);
        checkbox_tv = findViewById(R.id.checkbox_tv);
        headIcon_iv = findViewById(R.id.headIcon_iv);
        nickName_tv = findViewById(R.id.nickName_tv);
        realName_tv = findViewById(R.id.realName_tv);
        identity_tv = findViewById(R.id.identity_tv);
        phone_tv = findViewById(R.id.phone_tv);
        sex_tv = findViewById(R.id.sex_tv);
        age_tv = findViewById(R.id.age_tv);
        birthday_tv = findViewById(R.id.birthday_tv);
        address_tv = findViewById(R.id.address_tv);
        nickName_btn = findViewById(R.id.nickName_btn);
        realName_btn = findViewById(R.id.realName_btn);
        point_tv = findViewById(R.id.point_tv);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        nickName = bundle.getString("nickname");
        getUserInfo();
        clickEvent();
        back_btn = findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }




    private void getUserInfo(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.USER_INFO_FIND_BY_NICKNAME + "/" +nickName)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","??????????????????????????????");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","????????????????????????????????????");
                String responseStr = response.body().string();
                System.out.println(responseStr);
                UserInfo userInfo = JSON.parseObject(responseStr,UserInfo.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = userInfo;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void setUserInfoView(UserInfo userInfo){
        Glide.with(UserInfoActivity.this).load(userInfo.getUserIcon()).into(headIcon_iv);
        nickName_tv.setText(nickName);
        if(userInfo.getRealname()!=null){
            realName_tv.setText(userInfo.getRealname());
        }else {
            realName_tv.setText("?????????");
        }
        if(userInfo.getIdentity()!=null){
            identity_tv.setText(UserInfoUtil.hiddenNumber(userInfo.getIdentity()));
        }else {
            identity_tv.setText("?????????");
        }
        if(userInfo.getPhone()!=null){
            phone_tv.setText(UserInfoUtil.hiddenNumber(userInfo.getPhone()));
        }else {
            phone_tv.setText("?????????");
        }
        if(userInfo.getSex()!=null){
            sex_tv.setText(userInfo.getSex());
        }else {
            sex_tv.setText("?????????");
        }
        if(userInfo.getAge()!=null){
            age_tv.setText(userInfo.getAge()+"???");
        }else {
            age_tv.setText("?????????");
        }
        if(userInfo.getBirthday()!=null){
            birthday_tv.setText(new SimpleDateFormat("yyyy-MM-dd").format(userInfo.getBirthday()));
        }else {
            birthday_tv.setText("?????????");
        }
        if(userInfo.getAddress()!=null){
            address_tv.setText(userInfo.getAddress());
        }else {
            address_tv.setText("?????????");
        }
        if(userInfo.getPoint()!=null){
            point_tv.setText(userInfo.getPoint().toString()+"??????");
        }else {
            point_tv.setText("0??????");
        }
    }

    private void clickEvent(){
        nickName_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialog("????????????","????????????????????????~","??????","nickname",2,12);
            }
        });
        realName_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog("????????????","?????????????????????~","??????","realname",2,12);
            }
        });
        identity_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog("????????????","????????????????????????~","??????","identity",18,18);
            }
        });
        phone_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog("?????????","????????????????????????~","??????","phone",11,11);
            }
        });
        show_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.BottomListSheetBuilder(UserInfoActivity.this)
                        .setTitle("????????????")
                        .addItem("??????")
                        .addItem("??????")
                        .setIsCenter(true)
                        .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                            String type="";

                            switch (position){
                                case 0:
                                    type = "??????";
                                    break;
                                case 1:
                                    type = "??????";
                                    break;
                            }
                            SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPre.edit();
                            edit.putString("show_type",type);
                            edit.apply();
                            checkbox_tv.setText(type);
                            dialog.dismiss();
                        })
                        .build()
                        .show();
            }
        });
        sex_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSexPickerView();
            }
        });
        age_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAgePickerView();
            }
        });
        birthday_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        address_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getJson("province.json");
                List<ProvinceInfo> list = new ArrayList<>();
                try {
                    org.json.JSONObject dataJson = new JSONObject(str);
                    org.json.JSONArray provinces = dataJson.getJSONArray("provinces");
                    for(int i = 0; i < provinces.length(); i++){//??????
                        JSONObject province = provinces.getJSONObject(i);
                        ProvinceInfo provinceInfo = new ProvinceInfo();
                        provinceInfo.setName(province.getString("name"));
                        org.json.JSONArray cities = province.getJSONArray("city");
                        List<ProvinceInfo.City> cityList = new ArrayList<>();
                        for(int j = 0; j < cities.length(); j++){  //??????
                            //????????????
                            JSONObject city = cities.getJSONObject(j);
                            ProvinceInfo.City cityEntity = new ProvinceInfo.City();
                            cityEntity.setName(city.getString("name"));
                            JSONArray areas = city.getJSONArray("area");
                            List<String> areaList = new ArrayList<>();
                            for(int k = 0;k < areas.length(); k++){//???
                                areaList.add(areas.getString(k));
                            }
                            cityEntity.setArea(areaList);
                            cityList.add(cityEntity);
                        }
                        provinceInfo.setCityList(cityList);
                        list.add(provinceInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadData(list);
                showPickerView(false);
            }
        });
    }

    public String getJson(String path) {
        StringBuffer sb = new StringBuffer();
        AssetManager am = getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(am.open(path)));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }
    private void showSexPickerView() {
        mSexOption = ResUtils.getStringArray(R.array.sex_option);
        OptionsPickerView pvOptions = new OptionsPickerBuilder(UserInfoActivity.this, (v, options1, options2, options3) -> {
            updateUserInfo(mSexOption[options1],user_id,"sex");
            sexSelectOption = options1;
            return false;
        })
                .setTitleText("??????")
                .setSelectOptions(sexSelectOption)
                .build();
        pvOptions.setPicker(mSexOption);
        pvOptions.show();
    }
    private void showAgePickerView() {
        mAgeOption = ResUtils.getStringArray(R.array.age_option);
        OptionsPickerView pvOptions = new OptionsPickerBuilder(UserInfoActivity.this, (v, options1, options2, options3) -> {
            updateUserInfo(mAgeOption[options1],user_id,"age");
            ageSelectOption = options1;
            return false;
        })
                .setTitleText("??????")
                .setSelectOptions(ageSelectOption)
                .build();
        pvOptions.setPicker(mAgeOption);
        pvOptions.show();
    }
    private void showDatePicker() {
        if (mDatePicker == null) {
            mDatePicker = new TimePickerBuilder(UserInfoActivity.this, (date, v) -> updateUserInfo(new SimpleDateFormat("yyyy-MM-dd").format(date),user_id,"birthday"))
                    .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
                    .setTitleText("????????????")
                    .build();
        }
        mDatePicker.show();
    }
    private void loadData(List<ProvinceInfo> provinceInfos) {//????????????
        /**
         * ??????????????????
         */
        options1Items = provinceInfos;

        //???????????????????????????
        for (ProvinceInfo provinceInfo : provinceInfos) {
            //????????????????????????????????????
            List<String> cityList = new ArrayList<>();
            //??????????????????????????????????????????
            List<List<String>> areaList = new ArrayList<>();

            for (ProvinceInfo.City city : provinceInfo.getCityList()) {
                //????????????
                String cityName = city.getName();
                cityList.add(cityName);
                //??????????????????????????????
                List<String> cityAreaList = new ArrayList<>();
                //??????????????????????????????????????????????????????????????????null ?????????????????????????????????????????????
                if (city.getArea() == null || city.getArea().size() == 0) {
                    cityAreaList.add("");
                } else {
                    cityAreaList.addAll(city.getArea());
                }
                //??????????????????????????????
                areaList.add(cityAreaList);
            }

            /**
             * ??????????????????
             */
            options2Items.add(cityList);

            /**
             * ??????????????????
             */
            options3Items.add(areaList);
        }

        mHasLoaded = true;
    }

    private void showPickerView(boolean isDialog) {// ???????????????
        if (!mHasLoaded) {
            XToastUtils.toast("???????????????...");
            return;
        }

        int[] defaultSelectOptions = getDefaultCity();

        OptionsPickerView pvOptions = new OptionsPickerBuilder(UserInfoActivity.this, (v, options1, options2, options3) -> {
            //?????????????????????????????????????????????
            String tx = options1Items.get(options1).getPickerViewText() + " " +
                    options2Items.get(options1).get(options2) + " " +
                    options3Items.get(options1).get(options2).get(options3);
            XToastUtils.toast("???????????????????????????");
            updateUserInfo(tx,user_id,"address");
            return false;
        })

                .setTitleText("????????????")
                .setDividerColor(Color.BLACK)
                //????????????????????????????????????
                .isRestoreItem(true)
                //???????????????????????????
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .isDialog(isDialog)
                .setSelectOptions(defaultSelectOptions[0], defaultSelectOptions[1], defaultSelectOptions[2])
                .build();

        /*pvOptions.setPicker(options1Items);//???????????????
        pvOptions.setPicker(options1Items, options2Items);//???????????????*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//???????????????
        pvOptions.show();
    }

    private int[] getDefaultCity() {
        int[] res = new int[3];
        ProvinceInfo provinceInfo;
        List<ProvinceInfo.City> cities;
        ProvinceInfo.City city;
        List<String> ares;
        for (int i = 0; i < options1Items.size(); i++) {
            provinceInfo = options1Items.get(i);
            if ("?????????".equals(provinceInfo.getName())) {
                res[0] = i;
                cities = provinceInfo.getCityList();
                for (int j = 0; j < cities.size(); j++) {
                    city = cities.get(j);
                    if ("?????????".equals(city.getName())) {
                        res[1] = j;
                        ares = city.getArea();
                        for (int k = 0; k < ares.size(); k++) {
                            if ("?????????".equals(ares.get(k))) {
                                res[2] = k;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return res;
    }

    private void dialog(String title,String hint ,String btn,String col,int start_limit,int end_limit){

        FrameLayout linearLayout = new FrameLayout(UserInfoActivity.this);
        final EditText etInput = new EditText(UserInfoActivity.this);
        etInput.setInputType(InputType.TYPE_CLASS_TEXT);
        etInput.setHint(hint);
        etInput.setTextSize(14);
        etInput.setTextCursorDrawable(getDrawable(R.drawable.cursor));
        etInput.setBackground(getDrawable(R.drawable.comment_et));
        etInput.setPadding(20,30,20,30);
        linearLayout.addView(etInput);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) etInput.getLayoutParams();
        layoutParams.setMargins(DensityUtils.dp2px(UserInfoActivity.this, 25), 70, DensityUtils.dp2px(UserInfoActivity.this, 25), 0);
        etInput.setLayoutParams(layoutParams);
        AlertDialog alertDialog = new AlertDialog.Builder(UserInfoActivity.this)
                .setTitle(title)
                .setView(linearLayout)
                .setPositiveButton(btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        input = etInput.getText().toString();
                        if(input.length()>=start_limit&&input.length()<=end_limit){
                            if(col.equals("nickname")){
                                judgeNickName(input);
                            }else {
                                updateUserInfo(input, user_id, col);
                                dialog.dismiss();
                            }
                        }else {
                            XToastUtils.error("???????????????"+start_limit+"~"+end_limit+"???????????????????????????");
                            input = "";
                        }

                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).create();
        DialogUtil.buildDialogStyle(alertDialog);
        alertDialog.show();

    }
    private void judgeNickName(String nickname){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(AppConfig.NICKNAME_JUDGE+"/"+nickname)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("link","????????????????????????????????????");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("link","????????????????????????????????????");
                String responseStr = response.body().string();
                Message msg = mJudgeHandler.obtainMessage();
                Log.e("link",responseStr);
                msg.obj = responseStr;
                mJudgeHandler.sendMessage(msg);
            }
        });
    }
    private void updateUserInfo(String input,String user_id,String col){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        Map<String,String> map = new HashMap<>();
        map.put("user_id",user_id);
        map.put("input",input);
        map.put("col",col);
        String str = JSON.toJSONString(map);
        RequestBody body = RequestBody.create(JSON_TYPE, str);
        final Request request = new Request.Builder()
                .url(AppConfig.USER_UPDATE)
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
                UserInfo userInfo = JSON.parseObject(responseStr,UserInfo.class);
                Message msg = mHandler.obtainMessage();
                msg.obj = userInfo;
                mHandler.sendMessage(msg);
            }
        });
    }
}