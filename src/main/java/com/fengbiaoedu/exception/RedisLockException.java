package com.fengbiaoedu.exception;

public class RedisLockException extends RuntimeException {
	
	private String msg;
    private int code;
    public RedisLockException(int code,String msg){
        this.msg = msg;
        this.code = code;
    }
    
    public RedisLockException() {
		super();
	}

	public String getMessage(){
        /**
         * StringBuilder
         * **/
        StringBuilder builder = new StringBuilder();
        builder.append("错误代码:"+code).append("错误信息:").append(msg);
        return builder.toString();
    }
    
    public int getCode() {
        return code;
    }

}
