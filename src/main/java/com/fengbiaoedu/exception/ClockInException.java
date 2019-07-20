package com.fengbiaoedu.exception;

import com.fengbiaoedu.enums.ErrorEnum;

/**
 * 打卡异常
 * Created by Administrator on 2018/6/26.
 */
public class ClockInException extends RuntimeException{

    private String msg;
    private int code;
    public ClockInException(int code,String msg){
        this.msg = msg;
        this.code = code;
    }
    public String getMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append("错误代码:"+code).append("错误信息:").append(msg);
        return builder.toString();
    }
    public int getCode() {
        return code;
    }
}
