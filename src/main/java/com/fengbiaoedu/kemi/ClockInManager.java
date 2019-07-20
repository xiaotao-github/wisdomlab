package com.fengbiaoedu.kemi;

import java.util.concurrent.ConcurrentHashMap;
/**
 * 考勤机管理器
 * 
 * @author wucb
 *
 */
public class ClockInManager {
	
	private static ClockInManager clockInManager;
	
	// 请求命令队列前缀
	public  static final String REQ_CMD_QUEUE_KEY = "reqCmdQueue";
	
	//响应命令队列前缀
	public  static final String REP_CMD_QUEUE_KEY = "repCmdqueue";
	
	
	/*用户操作任务map，保存着用户操作任务需要向考勤机发送的操作次数，为0时，会删除key
	 Key:用户操作考勤机任务标识，value为此次操作任务需要发送的考勤机指令数。
	在处理考勤机响应时，会根据CmdDTo对象中的userOperateWorkKey从map中获取，依次递减，并重新赋值到map，
	直到为指令数0，加入ActiveMq用户操作考勤机结果队列（operateClockMachineResultQueue）。
	 */
	
	public static final ConcurrentHashMap<String,Integer> USER_OPERATE_WORK_MAP =new ConcurrentHashMap<>();
	
	/*
	 * 考勤机map,保存着所有考勤机的Map，在项目一启动后，就去数据库查询，保存到Map。
	 * 目的：为了新增考勤机去重，添加考勤机时可以选择，新增的未绑定实验室的考勤机。
	      做法：在考勤机向服务器询问，就判断是否有新进来的考勤机,有的话添加到ClockInManager.CLOCKIN_MACHINE_MAP，
		和添加到数据库，添加数据库时，is_deleted=1,默认是删除。
	 */
	public static final ConcurrentHashMap<String,Boolean> CLOCKIN_MACHINE_MAP = new ConcurrentHashMap<>();
	
	static {
		clockInManager = new ClockInManager();
	}
	public  static  ClockInManager getInstance() {
		
		return clockInManager;
	}
	
}
