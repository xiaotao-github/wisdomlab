package com.fengbiaoedu.enums;

import lombok.Getter;
/**
 * 定义了考勤机相关指令和一些考勤指令相关标识和用户操作考勤机回调的消息内容。
 * @author wucb
 *
 */
@Getter
public enum KemiCmdEnum {
	
	CLOCK_IN("realtime_glog"), //实时考勤指令
	//实时登记数据
	ENROLL_DATA_TRANSFER("realtime_enroll_data,EnrollDataTransterCmdStrategy"),
	//考勤机命令类包路径
	CMD_PACKAGE_PATH("com.fengbiaoedu.kemi.cmd."),
	
	/**考勤机发送指令**/ 
	//","前,为实际向考勤机发送的指令，","后，是指令发送后，处理考勤机响应的结果的类名
	//写入用户信息到考勤机
	SET_USER_INFO("SET_USER_INFO,SetUserInfoCmdStrategy"),
	//从考勤机得到用户信息
	GET_USER_INFO("GET_USER_INFO,GetUserInfoCmdStrategy"),
	//删除考勤机用户
	DELETE_USER("DELETE_USER,DeleteUserCmdStrategy"),
	//重启考勤机
	RESET_FK("RESET_FK,ResetFKCmdStrategy"),
	//清空注册数据
	CLEAR_ENROLL_DATA("CLEAR_ENROLL_DATA,ClearEnrollDataCmdStrategy"),
	//清空考勤机的考勤记录
	CLEAR_LOG_DATA("CLEAR_LOG_DATA,ClearLogDataCmdStrategy"),
	//得到考勤机的状态
	GET_DEVICE_STATUS("GET_DEVICE_STATUS,GetDeviceStatusCmdStrategy"),
	//获取考勤机用户列表
	GET_USER_ID_LIST("GET_USER_ID_LIST,GetUserIdListCmdStrategy"),
	//同步机器时间
	SET_TIME("SET_TIME,SetTimeCmdStrategy"),
	/**考勤机发送指令**/
	
	/**自定义，非发送考勤机指令**/
	OK("OK"),  //考勤机处理成功的标识
	REQUEST_MULTIPART_FLAG("1"), //flag为1，说明考勤机响应回来的是多段的，需要多次响应ok
	REQUEST_OVER("0"),   //flag 为0，说明考勤机响应结束
	DELETE_USER_BY_USERINFOLIST("deleteUserByUserInfoList"),  //标识通过考勤机录入用户总表删除
	DELETE_USER_BY_MACHINE("deleteUserByMachine"),  //标识通过考勤机下用户列表删除
	DELETE_MACHINE("deleteMachine"),   //删除考勤机
	/**自定义，非发送考勤机指令**/
	
	/**定义返回消息内容***/
	SET_USER_INFO_RESULT("用户已写入考勤机！"),
	DELETE_USER_RESULT("删除用户成功！"),
	DELETE_MACHINE_RESULT("删除考勤机成功！");
	
	private String msg;
	
	private KemiCmdEnum(String msg) {
		this.msg =msg;
	}
}
