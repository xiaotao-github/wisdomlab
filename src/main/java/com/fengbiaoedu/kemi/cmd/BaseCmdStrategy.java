package com.fengbiaoedu.kemi.cmd;
import java.util.HashMap;

import com.fengbiaoedu.kemi.ClockInManager;
import com.fengbiaoedu.mq.ActiveMQService;
import com.fengbiaoedu.utils.RedisUtil;
import com.fengbiaoedu.utils.SpringUtil;
public abstract class BaseCmdStrategy {
	
	/**
	 * 处理命令
	 * @param cmdDto 
	 * @param rep 
	 * @return
	 */
	public  Object processRequest(HashMap<String,Object> repProcessMap) {
		System.out.println("processRequest");
		return null;
	}
	
	/**
	 * 处理命令
	 * @param clockInServiceImpl 
	 * @param cmdDto 
	 * @param rep 
	 * @return
	 */
	public  Object processResponse(HashMap<String,Object> repProcessMap) {
		System.out.println("processResponse");
		return null;
	}
	
	/**
	 * 处理前端回调任务（考勤机请求操作结果）
	 * @param userOperateWorkKey
	 */
	public void processCallBackTask(String userOperateWorkKey,String msg) {
		boolean existKey = ClockInManager.USER_OPERATE_WORK_MAP.containsKey(userOperateWorkKey);
		if(existKey) {
			Integer operateCount = ClockInManager.USER_OPERATE_WORK_MAP.get(userOperateWorkKey);
			switch (operateCount) {
			case 1:
				ClockInManager.USER_OPERATE_WORK_MAP.remove(userOperateWorkKey);
				//发送消息队列，web端接受队列消息，提示完成写入用户信息
				SpringUtil.getBean(ActiveMQService.class).sendOperateClockMachineResult(userOperateWorkKey+","+msg);
				if(SpringUtil.getBean(RedisUtil.class).exists(userOperateWorkKey)) {
					SpringUtil.getBean(RedisUtil.class).remove(userOperateWorkKey);
				}
				break;

			default:
				ClockInManager.USER_OPERATE_WORK_MAP.put(userOperateWorkKey, --operateCount);
				break;
			}
		}
	}

}
