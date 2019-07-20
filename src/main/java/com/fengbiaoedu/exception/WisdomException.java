package com.fengbiaoedu.exception;

import com.fengbiaoedu.enums.ErrorEnum;
import lombok.Getter;

/**
 * Created by Administrator on 2018/5/16.
 */
@Getter
public class WisdomException extends RuntimeException {
    private String msg;
    private int code;
    public WisdomException(int code,String msg){
        this.msg = msg;
        this.code = code;
    }
    public WisdomException(ErrorEnum errorEnum){
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
    }
    public String getMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append("错误代码:"+code).append("错误信息:").append(msg);
       return builder.toString();
    }
}
