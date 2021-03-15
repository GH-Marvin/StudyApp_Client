package com.example.myapplication.util;

public class UserInfoUtil {
    public static String hiddenNumber(String num){
        String start = num.substring(0,3);
        String end = num.substring(num.length()-4);
        return start + "***" + end;
    }
}
