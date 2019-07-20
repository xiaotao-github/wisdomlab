package com.fengbiaoedu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fengbiaoedu.bean.LabClockInMachine;
import com.fengbiaoedu.bean.LabClockInMachineExample;
import com.fengbiaoedu.bean.LabClockInMachineUser;
import com.fengbiaoedu.bean.LabClockInUserInfo;
import com.fengbiaoedu.bean.LabClockInUserInfoExample;
import com.fengbiaoedu.bean.LabClockInUserInfoExample.Criteria;
import com.fengbiaoedu.bean.LabClockInUserInfoWithBLOBs;
import com.fengbiaoedu.config.ClockinConfig;
import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.kemi.ClockInManager;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.mapper.LabClockInMachineMapper;
import com.fengbiaoedu.mapper.LabClockInMachineUserMapper;
import com.fengbiaoedu.mapper.LabClockInManageMapper;
import com.fengbiaoedu.mapper.LabClockInUserInfoMapper;
import com.fengbiaoedu.pojo.ClockInUserInfoBO;
import com.fengbiaoedu.pojo.ClockUser;
import com.fengbiaoedu.redis.RedisLock;
import com.fengbiaoedu.service.LabClockInMachineUserService;
import com.fengbiaoedu.service.LabClockInManageService;
import com.fengbiaoedu.utils.ClockInUtils;
import com.fengbiaoedu.utils.RedisUtil;
import com.fengbiaoedu.vo.SysResult;
/**
 * 考勤机管理服务类
 * @author wucb
 *
 */
@Service
public class LabClockInManageServiceImpl implements LabClockInManageService {
	
	@Autowired
	LabClockInManageMapper clockInManagerMapper;
	
	@Autowired
	LabClockInUserInfoMapper clockInUserInfoMapper;
	
	@Autowired
	LabClockInMachineUserMapper clockInMachineUserMapper;
	
	@Autowired
	LabClockInMachineMapper clockInMachineMapper;
	
	@Autowired
	LabClockInMachineUserService clockInMachineUserService;
	
	@Autowired
	ClockInUtils clockInUtils;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	ClockinConfig clockinConfig;
	
	@Autowired
	RedisLock redisLock;
	
	/**
	 * 根据班级id获取要初始化的用户信息列表</br>
	 */
	@Override
	public List<ClockUser> getClockInUserByClassId(HashMap<String, Object> paramMap) {
		return clockInManagerMapper.getClockUserListByClassId(paramMap);
	}
	
	/**
	 * 根据班级id获取考勤用户信息列表</br>
	 */
	@Override
	public List<LabClockInUserInfoWithBLOBs> getClockInUserInfoByClassId(HashMap<String, Object> paramMap) {
		
		return clockInUserInfoMapper.selectByClassId(paramMap);
	}
	
	/**
	 * 根据用户id获取考勤机用户信息</br>
	 */
	@Override
	public List<LabClockInUserInfoWithBLOBs> getClockInUserInfoByUserId(Integer i) {
		LabClockInUserInfoExample example = new LabClockInUserInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(i);
		return clockInUserInfoMapper.selectByExampleWithBLOBs(example);
	}
	
	/**
	 * 根据实验室id获取要初始化的用户列表信息</br>
	 */
	@Override
	public List<ClockUser> getClockInUserByLabId(HashMap<String, Object> paramMap) {
		return clockInManagerMapper.getClockUserListByLabId(paramMap);
	}
	/**
	 * 得到考勤机用户信息列表通过考勤机id</br>
	 * @param clockinId 考勤机id</br>
	 * @return
	 */
	public List<LabClockInUserInfoWithBLOBs> getClockInUserInfoByClockinId(String clockinId) {
		
		return clockInUserInfoMapper.selectByClockInId(clockinId);
	}
	/**
	 * 得到考勤机用户信息列表通过userId列表</br>
	 * @param paramMap
	 * @return
	 */
	public List<LabClockInUserInfo> getClockInUserInfoByUserIdList(HashMap<String, Object> paramMap) {
		return clockInUserInfoMapper.selectClockInUserInfoByUserIdList(paramMap);
	}
	
	/**
	 * 得到考勤机用户信息列表通过userId列表</br>
	 * @param paramMap
	 * key 
	 * 		String[] clockinIdArray 考勤机id数组</br>
	 * 		String operatorId   操作人id</br>
	 * 		List<Integer> userIdList  用户id列表</br>
	 * 		String userOperateWorkKey  用户操作考勤机任务key </br>
	 * @return
	 */
	public List<LabClockInUserInfoWithBLOBs> getClockInUserInfoWithBLOBsByUserIdList(HashMap<String,Object>paramMap){
		LabClockInUserInfoExample example = new LabClockInUserInfoExample();
		Criteria criteria = example.createCriteria();
		List<Integer> userIdList =  (List<Integer>) paramMap.get("userIdList");
		criteria.andUserIdIn(userIdList);
		return clockInUserInfoMapper.selectByExampleWithBLOBs(example);
	}
	/**
	 * 获取考勤机存在userIdList的userIdList</br>
	 */
	public List<Integer> getClockinUserIdListByUserList(List<Integer> userIdList,String clockinId){
		
		return clockInMachineUserMapper.selectUserIdListByUserIdList(userIdList,clockinId);
	}
	
	/**
	 * 根据classId获取要初始化的考勤用户列表信息</br>
	 * @param paramMap </br>
	 * 		  key classId value(String) classId
	 * 
	 * @return
	 */
	public List<LabClockInUserInfo> getClockInUserInfoListByClassId(HashMap<String, Object> paramMap) {
		return clockInUserInfoMapper.selectClockInUserInfoByClassId(paramMap);
	}
	
	
	/**
	 * 写入用户信息到多个考勤机</br>
	 * key 
	 * 		String[] clockinIdArray 考勤机id数组</br>
	 * 		String operatorId   操作人id</br>
	 * 		List<Integer> userIdList  用户id列表</br>
	 * 		String userOperateWorkKey  用户操作考勤机任务key </br>
	 */
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@Transactional
	@Override
	public Boolean setUserInfoToMachine(HashMap<String, Object> paramMap) {
		//获取操作的考勤机idlist
		String[] clockinIdList = (String[]) paramMap.get("clockinIdArray");
		//获取userIdList的考勤用户数据
		List<LabClockInUserInfoWithBLOBs> clockUserInfoList = getClockInUserInfoWithBLOBsByUserIdList(paramMap);
		
		Boolean exist  = clockUserInfoList !=null && !clockUserInfoList.isEmpty();
		//要写入的考勤机用户列表不为空
		if(exist) {
			String userOperateWorkKey = (String) paramMap.get("userOperateWorkKey");
			//所有考勤机需要录入的用户统计
			Integer userInputCount = 0;
			paramMap.put("userInputCount", userInputCount);
			//将List转为Map，已userId为key,list的元素为值
			LinkedHashMap<String,LabClockInUserInfoWithBLOBs>  clockUserInfoMap = new LinkedHashMap<>(clockUserInfoList.size());
			for (LabClockInUserInfoWithBLOBs clockUserInfo : clockUserInfoList) {
				clockUserInfoMap.put(String.valueOf(clockUserInfo.getUserId()), clockUserInfo);
			}
			
			//遍历考勤机id列表，逐台考勤机写入考勤机用户信息
			for(Object clockinId:clockinIdList) {
				paramMap.put("clockinId", clockinId);
				
				//获取实验室id
				LabClockInMachineExample example = new LabClockInMachineExample();
				com.fengbiaoedu.bean.LabClockInMachineExample.Criteria criteria = example.createCriteria();
				criteria.andClockinIdEqualTo((String)clockinId);
				List<LabClockInMachine> machineList = clockInMachineMapper.selectByExample(example);
				if(machineList!=null && !machineList.isEmpty()) {
					paramMap.put("labId", machineList.get(0).getLabId());
				}
				//加入考勤机请求队列
				setUserInfoByClockinId(paramMap, (String)clockinId, (LinkedHashMap<String, LabClockInUserInfoWithBLOBs>) clockUserInfoMap.clone());
			}
			userInputCount = (Integer) paramMap.get("userInputCount");
			paramMap.put("userInputCount", userInputCount);
			if(userInputCount!=0) {
				//加入到用户操作任务map中
				ClockInManager.USER_OPERATE_WORK_MAP.put(userOperateWorkKey, userInputCount);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * paramMap </br>
	 * 			key operatorId value(Integer) </br>
	 * 			key classId    value(String)  </br>
	 */
	@Override
	public Boolean initUserInfo(HashMap<String, Object> paramMap) {
		
		List<LabClockInUserInfo> userList = getClockInUserInfoListByClassId(paramMap);
		
		if(userList==null && userList.isEmpty()) {
			return false;
		}
		paramMap.put("list", userList);
		return clockInUserInfoMapper.batchInsert(paramMap) >0;
	}
	
	/**
	 * 写入用户信息到考勤机,添加响应的考勤机命令队列</br>
	 * @param paramMap
	 * 		String[] clockinIdArray 考勤机id数组</br>
	 * 		String operatorId   操作人id</br>
	 * 		List<Integer> userIdList  用户id列表</br>
	 * 		String userOperateWorkKey  用户操作考勤机任务key </br>
	 * @param clockinId  考勤机id</br>
	 * @param clockinInfoMap 需要写入的所有考勤机用户Map</br>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean setUserInfoByClockinId(HashMap<String,Object>paramMap,String clockinId,LinkedHashMap<String,LabClockInUserInfoWithBLOBs> clockinInfoMap) {
		
		List<Integer> userIdList = (List<Integer>) paramMap.get("userIdList");
		String userOperateWorkKey = (String) paramMap.get("userOperateWorkKey");
		//获取该考勤机下包含userIdList的userIdList
		List<Integer> clockinMachineUserIdList = getClockinUserIdListByUserList(userIdList, clockinId);
		if(clockinMachineUserIdList != null && !clockinMachineUserIdList.isEmpty()) {
			//考勤机用户去重
			for(Integer userId : clockinMachineUserIdList) {
				clockinInfoMap.remove(String.valueOf(userId));
			}
		}
		
		if(!clockinInfoMap.isEmpty()) {
			
			ConcurrentLinkedQueue< CmdDTO> cmdQueue = new ConcurrentLinkedQueue<>();
			List<Long> idList =new ArrayList<>();
			//用于更新idFlag
			List<Long> idforConvetorList = new ArrayList<>();
			long id =0L; 
			
			//遍历考勤机用户信息，依次新增考勤机用户，依次构建考勤机请求队列
			for( LabClockInUserInfoWithBLOBs clockUserInfo :clockinInfoMap.values()) {
				
				//新增到数据库，并获得考勤机用户表主键
				paramMap.put("user", clockUserInfo);
				//新增考勤机用户
				clockInMachineUserMapper.save(paramMap);
				
				//获取考勤机用户自增id
				LabClockInMachineUser clockInMachineUser = clockInMachineUserService.getClockInMachineUserByClockInIdAndUserId((String)paramMap.get("clockinId"), clockUserInfo.getUserId());
				id =clockInMachineUser.getId();
				
				//大于Math.pow(10,10)-1，加入list,方便进行id_flag的更改
				if(id>Math.pow(10, 10)-1) {
					idforConvetorList.add(id);
				}else {
					idList.add(id);
				}
				//构建请求队列信息
				ClockInUserInfoBO user = new ClockInUserInfoBO ();
				//获取符合考勤机接收的对象
				user = ClockInUtils.getClockInUserInfo(clockUserInfo);
				//将userId转换为考勤机工号
				user.setUser_id(String.valueOf(ClockInUtils.getUserId(id)));
				
				//获取返回字节数组
				byte[] responseByte = clockInUtils.getClockInUserInfoBytes(user,clockUserInfo);
				cmdQueue.add(new CmdDTO(clockInUtils.setAndGetTransId(clockInMachineUser.getLabId()), responseByte,KemiCmdEnum.SET_USER_INFO.getMsg(),id,userOperateWorkKey));
			}
			//批量更新id_Flag的值
			if(!idforConvetorList.isEmpty()) {
				clockInMachineUserMapper.batchUpdateUserIdFlag(idforConvetorList);
			}
			//批量更新考勤机用户登记号
			if(!idList.isEmpty()) {
				clockInMachineUserMapper.batchUpdateUserEnrollId(idList);
			}
			//请求队列加入redis
			redisUtil.putCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, (String)paramMap.get("clockinId"), cmdQueue);
			
			//累积需要录入考勤机的用户个数
			paramMap.put("userInputCount", (Integer)paramMap.get("userInputCount")+clockinInfoMap.size());
			clockinInfoMap =null;
			return true;
		}
		clockinInfoMap =null;
		return false;
	}
	
	/**
	 * 批量删除考勤机用户,通过用户录入总表</br>
	 */
	@Override
	public SysResult batchDeleteUser(HashMap<String, Object> paramMap) {
		List<Integer> userIdList = (List<Integer>) paramMap.get("userIdList");
		String userOperateWorkKey = (String) paramMap.get("userOperateWorkKey");
		//根据userIdlist获取考勤机列表,判断要操作的考勤机是否有任务未完成
		List<String> allClockInIdList = getClockInIdListByUserIdList(userIdList);
		
		if(allClockInIdList == null ||  allClockInIdList.isEmpty()) {
			//直接删除考勤机用户信息
			clockInUserInfoMapper.batchDelete(userIdList);
			redisUtil.remove(userOperateWorkKey);
			return SysResult.build(301, "已完成删除");
		}
		//判断要操作的考勤机是否有任务未完成
		ConcurrentLinkedQueue<Long> timeQueue = new ConcurrentLinkedQueue<>();
		//加锁
		for(String clockinId:allClockInIdList) {
			long time = System.currentTimeMillis()+clockinConfig.getClockinMachineLockTimeOut();
			if(!redisLock.lock(redisUtil.getRepQueueLockKey((String)clockinId),String.valueOf(time) )) {
				return SysResult.build(102, "哎呀喂，要删除的用户所在的考勤机当前正忙，请稍候再试！");
			}
			//加入list中，方便后面解锁
			timeQueue.offer(time);
		}
		//所有考勤机需要录入的用户统计
		Integer operateCount = 0;
		paramMap.put("operateCount", operateCount);
		try {
			for(Integer userId:userIdList) {
				//根据userId获取相应的考勤机列表，依次加入队列
				List<LabClockInMachine> clockInMachineList = getClockInMachineListByUserId(userId);
				if(clockInMachineList ==null || clockInMachineList.isEmpty()) {
					continue;
				}
				paramMap.put("clockInMachineList", clockInMachineList);
				//遍历考勤机列表，依次加入请求队列中
				clockInUtils.addReqCmdNoBinaryByMachineList(paramMap, KemiCmdEnum.DELETE_USER.getMsg());
			}
			operateCount = (Integer) paramMap.get("operateCount");
			paramMap.put("operateCount", operateCount);
			if(operateCount!=0) {
				
				ClockInManager.USER_OPERATE_WORK_MAP.put(userOperateWorkKey, operateCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//解锁
			for(String clockinId:allClockInIdList) {
				redisLock.unLock(redisUtil.getRepQueueLockKey(clockinId), String.valueOf(timeQueue.poll()));
			}
		}
		return  SysResult.build(200, "正在删除考勤机用户，等稍后！");
	}
	/**
	 * 批量删除考勤机用户通过考勤机</br>
	 */
	@Override
	public SysResult batchDeleteUserByMachine(HashMap<String, Object> paramMap) {
		List<Integer> userIdList = (List<Integer>) paramMap.get("userIdList");
		String userOperateWorkKey = (String) paramMap.get("userOperateWorkKey");
		String operatorId = (String) paramMap.get("operatorId");
		String clockinId = (String) paramMap.get("clockinId");
		String labId = (String) paramMap.get("labId");
		//判断要操作的考勤机是否有任务未完成
		//加锁
		long time = System.currentTimeMillis()+clockinConfig.getClockinMachineLockTimeOut();
		if(!redisLock.lock(redisUtil.getRepQueueLockKey((String)clockinId),String.valueOf(time) )) {
			return SysResult.build(102, "哎呀喂，要删除的用户所在的考勤机当前正忙，请稍候再试！");
		}
		//加入list中，方便后面解锁
		//所有考勤机需要录入的用户统计
		Integer operateCount = 0;
		paramMap.put("operateCount", operateCount);
		//获取userIdList的machineUserList
		List<LabClockInMachineUser> clockMachineUserList = getClockMachineUserListByUserIdListAndClockinId(clockinId,userIdList);
		if(clockMachineUserList ==null || clockMachineUserList.isEmpty()) {
			redisUtil.remove(userOperateWorkKey);
			return SysResult.build(103, "所选的用户已删除，无需重复操作！");
		}
		paramMap.put("clockMachineUserList", clockMachineUserList);
		try {
			//遍历考勤机用户列表，依次加入请求队列中
			clockInUtils.addReqCmdByMachineUserList(paramMap, KemiCmdEnum.DELETE_USER.getMsg());
			operateCount = (Integer) paramMap.get("operateCount");
			paramMap.put("operateCount", operateCount);
			if(operateCount!=0) {
				
				ClockInManager.USER_OPERATE_WORK_MAP.put(userOperateWorkKey, operateCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//解锁
			redisLock.unLock(redisUtil.getRepQueueLockKey(clockinId), String.valueOf(time));
		}
		return  SysResult.build(200, "正在删除考勤机用户，等稍后！");
	}
	
	/**
	 * 获取考勤机用户列表通过userId列表和考勤机id</br>
	 * @param clockinId </br>
	 * @param userIdList 用户userId列表</br>
	 * @return
	 */
	private List<LabClockInMachineUser> getClockMachineUserListByUserIdListAndClockinId(String clockinId, List<Integer> userIdList) {
		return clockInMachineUserMapper.selectByUserIdListAndClockinId(clockinId,userIdList);
		 
		
	}
	/**
	 * 根据userIdlist获取获取考勤机id</br>
	 * @param userIdList  用户id列表</br>
	 * @return
	 */
	private List<String> getClockInIdListByUserIdList(List<Integer> userIdList) {
		
		return clockInMachineMapper.getClockInIdListByUserIdList(userIdList);
	}
	
	/**
	 * 获取userId获取考勤机列表</br>
	 * @param userId
	 * @return
	 */
	private  List<LabClockInMachine> getClockInMachineListByUserId(Integer userId){
		
		return clockInMachineMapper.selectByUserId(userId);
		
	}
	
	/**
	 * 删除考勤机</br>
	 */
	@Override
	public SysResult delMachine(HashMap<String, Object> paramMap) {
		
		String clockId = (String) paramMap.get("clockId");
		String userOperateWorkKey = (String) paramMap.get("userOperateWorkKey");
		String operatorId = (String) paramMap.get("operatorId");
		
		//需要向考勤机发送指令的次数
		Integer operateCount =0;
		paramMap.put("operateCount", operateCount);
		
		//当前时间的考勤机锁超时时间
		long time = System.currentTimeMillis()+clockinConfig.getClockinMachineLockTimeOut();
		
		//加锁，判断当前考勤机是否正在忙
		if(!redisLock.lock(redisUtil.getRepQueueLockKey(clockId),String.valueOf(time) )) {
			return SysResult.build(102, "哎呀喂，要删除的用户所在的考勤机当前正忙，请稍候再试！");
		}
		try {
			//添加删除考勤机的cmd队列，删除考勤机会像考勤机发送清空用户登记数据和清空考勤记录两个指令
			clockInUtils.addReqCmdByDeleteMachine(paramMap, KemiCmdEnum.DELETE_MACHINE.getMsg());
			operateCount = (Integer) paramMap.get("operateCount");
			paramMap.put("operateCount", operateCount);
			
			if(operateCount!=0) { //需要向考勤机发送指令的次数不为0
				//加入到用户操作任务map中
				ClockInManager.USER_OPERATE_WORK_MAP.put(userOperateWorkKey, operateCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//解锁
			redisLock.unLock(redisUtil.getRepQueueLockKey(clockId), String.valueOf(time));
		}
		return  SysResult.build(200, "正在删除考勤机，等稍后！");
	}
	
}
