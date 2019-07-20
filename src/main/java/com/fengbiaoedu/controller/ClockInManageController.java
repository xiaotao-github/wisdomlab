package com.fengbiaoedu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fengbiaoedu.config.ClockinConfig;
import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.mapper.LabClockInMachineUserMapper;
import com.fengbiaoedu.redis.RedisLock;
import com.fengbiaoedu.service.LabClockInMachineUserService;
import com.fengbiaoedu.service.LabClockInManageService;
import com.fengbiaoedu.utils.ClockInUtils;
import com.fengbiaoedu.utils.RedisUtil;
import com.fengbiaoedu.vo.SysResult;
/**
 * 考勤机管理控制器</br>
 * 处理用户在页面上操作考勤机</br>
 * 包括：1录入用户到考勤机
 * 		2通过用户录入总表删除考勤机用户
 * 		3通过考勤机删除考勤机用户
 * 		4删除考勤机
 * @author wucb
 *
 */
@RestController
@RequestMapping("/clockInManage")
public class ClockInManageController {
	
	@Autowired
	LabClockInManageService clockInManagerService;
	
	@Autowired
	LabClockInMachineUserService clockInMachineUserService;
	
	@Autowired
	LabClockInMachineUserMapper clockInMachineUserMapper;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	ClockInUtils clockInUtils;
	
	@Autowired
	RedisLock redisLock;
	
	@Autowired
	ClockinConfig clockinConfig;
	
	/**
	 * 录入用户信息到考勤机</br>
	 * @param userIdList 要录入的userIdList </br>
	 * @param clockIdList 考勤机id列表   </br>
	 * @param operatorId  操作人id</br>
	 * @param vcoocUserId
	 * @return
	 */
	@PostMapping("/writeInUserInfo")
	public SysResult writeInUserInfo(String userIdList,String clockIdList,String operatorId,String vcoocUserId) {
		//参数判空
		if(StringUtils.isEmpty(userIdList) || StringUtils.isEmpty(clockIdList) || StringUtils.isEmpty(operatorId) 
				|| StringUtils.isEmpty(vcoocUserId)) {
			return SysResult.build(400, "参数异常");
		}
		//获取用户操作考勤机任务key
		String   userOperateWorkKey = redisUtil.getUserOperateWorkKey(KemiCmdEnum.SET_USER_INFO.getMsg(),operatorId,vcoocUserId);
		//判断当前用户操作考勤机的此次指令，之前是否还有没有完成的指令
		if(redisUtil.existUserUnCompletedWork(userOperateWorkKey)) {
			return SysResult.build(101, "您要之前写入考勤机的任务尚未完成，请稍后再试！");
		}
		//设置用户操作考勤机任务标识，并设置好过期时间
		redisUtil.set(userOperateWorkKey, null, clockinConfig.getUserOperateWorkKeyTimeOut());
		
		String[] userIdArray = userIdList.split(",");
		String[] clockinIdArray = clockIdList.split(",");
		
		List<Integer> userList = new ArrayList<>();
		for(String userId:userIdArray) {
			userList.add(Integer.valueOf(userId));
		}
		//队列用于保存多个考勤机锁的超时时间
		ConcurrentLinkedQueue<Long> timeQueue = new ConcurrentLinkedQueue<>();
		//加锁
		for(Object clockinId:clockinIdArray) {
			//当前时间的考勤机锁超时时间
			long time = System.currentTimeMillis()+clockinConfig.getClockinMachineLockTimeOut();
			//加锁，判断当前考勤机是否正在忙
			if(!redisLock.lock(redisUtil.getRepQueueLockKey((String)clockinId),String.valueOf(time))) {
				return SysResult.build(102, "哎呀喂，要初始化的考勤机当前正忙，请稍候再试！");
			}
			//加入队列中，方便后面解锁
			timeQueue.offer(time);
		}
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("clockinIdArray", clockinIdArray);
		paramMap.put("operatorId", operatorId);
		paramMap.put("userIdList", userList);
		paramMap.put("userOperateWorkKey", userOperateWorkKey);
		try {
			//写入用户信息到考勤机
			clockInManagerService.setUserInfoToMachine(paramMap);
			Integer userInputCount = (Integer) paramMap.get("userInputCount");
			//需要向考勤机发送指令的次数不为0
			if(userInputCount ==0) {
				redisUtil.remove(userOperateWorkKey);
				return SysResult.build(103, "您选取的用户都已经写入考勤机了，无需重复操作");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//解锁
			for(Object clockinId:clockinIdArray) {
				redisLock.unLock(redisUtil.getRepQueueLockKey((String)clockinId), String.valueOf(timeQueue.poll()));
			}
		}
		return SysResult.build(200, "考勤机正在写入数据请稍后");
		
	}
	
	/**获取考勤机用户信息
	@GetMapping("/getUserInfo")
	public void getUserInfo() {
		ConcurrentLinkedQueue< CmdDTO> cmdQueue = new ConcurrentLinkedQueue<>();
		String devId ="0000000001";
		Long labId =1L;
		ClockInUserInfoBO user = new ClockInUserInfoBO ();
		user.setUser_id("181");
		//对象转Json字符串
		ObjectMapper objectMapper = new ObjectMapper();
		String str;
		try {
			str = objectMapper.writeValueAsString(user);
			byte [] responseByte = new byte[0];
			responseByte = clockInUtils.CreateByteFromString(str, responseByte);
			cmdQueue.add(new CmdDTO(clockInUtils.setAndGetTransId(labId), responseByte,KemiCmdEnum.GET_USER_INFO.getMsg(),Long.parseLong(user.getUser_id())));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		redisUtil.putCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, devId, cmdQueue);
	}
	*/
	
	/**
	 * 通过用户录入总表删除考勤机的用户<br>
	 * @param userIdList
	 * @param operatorId
	 * @param vcoocUserId
	 * @return
	 */
	@PostMapping("/deleteMechineUserByUserInfoList")
	public SysResult deleteMechineUserByUserInfoList(String userIdList,String operatorId,String vcoocUserId) {
		//参数判空
		if(StringUtils.isEmpty(userIdList) || StringUtils.isEmpty(operatorId) 
				|| StringUtils.isEmpty(vcoocUserId)) {
			return SysResult.build(400, "参数异常");
		}
		//判断当前用户操作考勤机的此次指令，之前是否还有没有完成的指令
		String   userOperateWorkKey = redisUtil.getUserOperateWorkKey(KemiCmdEnum.DELETE_USER_BY_USERINFOLIST.getMsg(),operatorId,vcoocUserId);
		if(redisUtil.existUserUnCompletedWork(userOperateWorkKey)) {
			return SysResult.build(101, "您要之前操作考勤机的任务尚未完成，请稍后再试！");
		}
		redisUtil.set(userOperateWorkKey, null, clockinConfig.getUserOperateWorkKeyTimeOut());//设置10分钟失效
		
		String[] userIdArray = userIdList.split(",");
		
		List<Integer> userList = new ArrayList<>();
		for(String userId:userIdArray) {
			userList.add(Integer.valueOf(userId));
		}
		
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("operatorId", operatorId);
		paramMap.put("userIdList", userList);
		paramMap.put("userOperateWorkKey", userOperateWorkKey);
		
		return clockInManagerService.batchDeleteUser(paramMap);
	}
	
	/**
	 * 通过考勤机删除考勤机的用户<br>
	 * @param userIdList
	 * @param operatorId
	 * @param vcoocUserId
	 * @return
	 */
	@PostMapping("/deleteUserByMachine")
	public SysResult deleteUserByMachine(String userIdList,String operatorId,String vcoocUserId,String clockinId,String labId) {
		//参数判空
		if(StringUtils.isEmpty(userIdList) || StringUtils.isEmpty(operatorId) 
				|| StringUtils.isEmpty(vcoocUserId) || StringUtils.isEmpty(clockinId)
				|| StringUtils.isEmpty(labId)) {
			return SysResult.build(400, "参数异常");
		}
		String   userOperateWorkKey = redisUtil.getUserOperateWorkKey(KemiCmdEnum.DELETE_USER_BY_MACHINE.getMsg(),operatorId,vcoocUserId);
		//判断当前用户操作考勤机的此次指令，之前是否还有没有完成的指令
		if(redisUtil.existUserUnCompletedWork(userOperateWorkKey)) {
			return SysResult.build(101, "您要之前操作考勤机的任务尚未完成，请稍后再试！");
		}
		redisUtil.set(userOperateWorkKey, null, clockinConfig.getUserOperateWorkKeyTimeOut());//设置10分钟失效
		
		String[] userIdArray = userIdList.split(",");
		
		List<Integer> userList = new ArrayList<>();
		for(String userId:userIdArray) {
			userList.add(Integer.valueOf(userId));
		}
		
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("operatorId", operatorId);
		paramMap.put("userIdList", userList);
		paramMap.put("clockinId", clockinId);
		paramMap.put("labId", labId);
		paramMap.put("userOperateWorkKey", userOperateWorkKey);
		
		return clockInManagerService.batchDeleteUserByMachine(paramMap);
		//addReqCmdNoBinaryByClockInUserInfoBO(userIdArray, labId, devId, KemiCmdEnum.DELETE_USER.getMsg());
	}
	/**
	 * 删除考勤机,这其实将考勤机is_deteled为1</br>
	 * @param operatorId 操作人id</br>
	 * @param clockId 考勤机id</br>
	 * @param vcoocUserId
	 * @param labId  实验室id</br>
	 * @return
	 */
	@PostMapping("/delMachine")
	public SysResult  delMachine(String operatorId,String clockId,String vcoocUserId,String labId) {
		
		//参数判空
		if(StringUtils.isEmpty(operatorId) || StringUtils.isEmpty(operatorId) 
				|| StringUtils.isEmpty(vcoocUserId) || StringUtils.isEmpty(labId)) {
			return SysResult.build(400, "参数异常");
		}
		//判断当前用户操作考勤机的此次指令，之前是否还有没有完成的指令
		String   userOperateWorkKey = redisUtil.getUserOperateWorkKey(KemiCmdEnum.DELETE_MACHINE.getMsg(),operatorId,vcoocUserId);
		if(redisUtil.existUserUnCompletedWork(userOperateWorkKey)) {
			return SysResult.build(101, "您要之前操作考勤机的任务尚未完成，请稍后再试！");
		}
		//设置用户操作考勤机任务标识，并设置好过期时间
		redisUtil.set(userOperateWorkKey, null, clockinConfig.getUserOperateWorkKeyTimeOut());
		
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("operatorId", operatorId);
		paramMap.put("clockId", clockId);
		paramMap.put("userOperateWorkKey", userOperateWorkKey);
		paramMap.put("labId", labId);
		return clockInManagerService.delMachine(paramMap);
		
	}
	
}
