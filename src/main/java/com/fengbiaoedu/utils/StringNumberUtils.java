package com.fengbiaoedu.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串数字处理工具类
 * Created by Administrator on 2018/7/12.
 */
public class StringNumberUtils {
    //0-9的数字
    private static final String  regEx1 = "[^0-9]";
    private static final Pattern pattern;
    static {
        pattern = Pattern.compile(regEx1);
    }

    //判断是否以数字开头
    public static boolean isStartWithNumber(String str){
        if(StringUtils.isEmpty(str))return false;
        Matcher matcher = pattern.matcher(str.charAt(0) + "");
        return  !matcher.matches();
    }
    //截取字符串前面的数字
    public static int getStringStartNumber(String str){
        String result = "";
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)>=48&&str.charAt(i)<=57)result+=str.charAt(i);
        }
        try{
            return Integer.parseInt(result);
        }catch (Exception e){
            return 999;
        }
    }
}
