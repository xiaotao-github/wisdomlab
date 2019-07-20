package com.fengbiaoedu.enums;


/**
 * 考勤状况 枚举类
 * Created by Administrator on 2018/6/26.
 */
public enum SigninEnum {


    NOT(0,"未考勤"),
    NORMAL(1,"正常考勤"),
    DELAY(2,"迟到");

    private int code;
    private String msg;

    SigninEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
