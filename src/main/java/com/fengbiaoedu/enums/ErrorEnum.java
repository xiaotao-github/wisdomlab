package com.fengbiaoedu.enums;

import lombok.Getter;

/**
 * Created by Administrator on 2018/5/16.
 */
@Getter
public enum  ErrorEnum {
    PARAMERROR(400,"参数错误"),
    NODATA(401,"无数据信息");

    private int code; //错误代码
    private String msg; //错误信息

    private ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
