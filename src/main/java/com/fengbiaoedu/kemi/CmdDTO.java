package com.fengbiaoedu.kemi;

import java.io.Serializable;

import lombok.Data;
/**
 * 用于考勤机请求响应，mq消息传递的中间对象
 * @author wucb
 *
 */
@Data
public class CmdDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String trans_id; //考勤机任务识别号 如何构建见com.fengbiaoedu.utils.ClockInUtils.setAndGetTransId(Long labId)
	
	private byte[] responseByte; //考勤机请求或者响应的字节数组
	
	private String cmd; //cmd 由两部分组成，1为指令，2为指令处理类名，中间用","隔开
	
	private Long user_id;  //考勤机用户信息表自增主键id，为了方便，数据库数据回顾
	
	private Boolean result; //响应结果
	
	private String userOperateWorkKey ;  //区分是哪个user操作的指令，null则是考勤发送过来的生成的
	
	private String clockinId; //考勤机id
	
	public CmdDTO() {
		
	}

	public CmdDTO(String trans_id, byte[] responseByte, String cmd, Long user_id) {
		super();
		this.trans_id = trans_id;
		this.responseByte = responseByte;
		this.cmd = cmd;
		this.user_id = user_id;
	}

	public CmdDTO(String trans_id, byte[] responseByte, String cmd, Long user_id,
			String userOperateWorkKey) {
		super();
		this.trans_id = trans_id;
		this.responseByte = responseByte;
		this.cmd = cmd;
		this.user_id = user_id;
		this.userOperateWorkKey = userOperateWorkKey;
	}

	public CmdDTO(String trans_id, byte[] responseByte, String cmd, Long user_id, 
			String userOperateWorkKey, String clockinId) {
		super();
		this.trans_id = trans_id;
		this.responseByte = responseByte;
		this.cmd = cmd;
		this.user_id = user_id;
		this.userOperateWorkKey = userOperateWorkKey;
		this.clockinId = clockinId;
	}
	
	
	
	
	

	

}
