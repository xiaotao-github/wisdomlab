package com.fengbiaoedu.service;

import java.util.HashMap;
import java.util.List;

import com.fengbiaoedu.bean.LabClockInUserInfoWithBLOBs;
import com.fengbiaoedu.pojo.ClockUser;
import com.fengbiaoedu.vo.SysResult;
/**
 * 考勤机管理服务接口 </br>
 * @author wucb
 *
 */
public interface LabClockInManageService {
	
	//通过班级id得到用于初始化的用户对象列表
	List<ClockUser> getClockInUserByClassId(HashMap<String, Object> paramMap);
	
	//通过班级id获取考勤机用户信息列表
	List<LabClockInUserInfoWithBLOBs> getClockInUserInfoByClassId(HashMap<String, Object> paramMap);
	
	//通过userId获取考勤机用户信息
	List<LabClockInUserInfoWithBLOBs> getClockInUserInfoByUserId(Integer i);
	
	//通过实验室id得到用于初始化的用户对象列表
	List<ClockUser> getClockInUserByLabId(HashMap<String, Object> paramMap);
	
	//写入用户列表到考勤机
	//void initUserListToMachine(HashMap<String, Object> paramMap);
	
	//写入用户信息到考勤机
	Boolean setUserInfoToMachine(HashMap<String, Object> paramMap);

	Boolean initUserInfo(HashMap<String, Object> paramMap);
	
	//批量删除用户
	SysResult batchDeleteUser(HashMap<String, Object> paramMap);
	
	//批量删除用户通过考勤机
	SysResult batchDeleteUserByMachine(HashMap<String, Object> paramMap);
	
	//删除考勤机
	SysResult delMachine(HashMap<String, Object> paramMap);
	
	

}
