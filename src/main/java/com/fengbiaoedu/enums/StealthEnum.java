package com.fengbiaoedu.enums;

/**
 * Created by Administrator on 2018/7/12.
 */
public enum  StealthEnum {

    EXIT(2,"未删除"),
    NOTEXIT(1,"删除");

    private int code;
    private String msg;

    StealthEnum(int code, String msg) {
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
