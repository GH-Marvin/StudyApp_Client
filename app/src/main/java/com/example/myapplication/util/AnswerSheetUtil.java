package com.example.myapplication.util;

import android.text.TextUtils;

import com.example.myapplication.entity.Choice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnswerSheetUtil {
    public static String castToString(Map<Integer, Choice> singleMap, Map<Integer, List<Choice>> multipleMap){
        String cast = "";
        if(singleMap.size()!=0){
            for(Integer c: singleMap.keySet()){
                cast = cast + c + ":" + singleMap.get(c).getOptId() + ";";
            }
        }
        if(multipleMap.size()!=0){
            for(Integer i : multipleMap.keySet()){
                cast = cast + i + "-";
                for (Choice choice : multipleMap.get(i)){
                    cast = cast +choice.getOptId() +",";
                }
                cast = cast.substring(0,cast.length()-1);
                cast = cast.concat(";");
            }
        }
        return cast;
    }
}
