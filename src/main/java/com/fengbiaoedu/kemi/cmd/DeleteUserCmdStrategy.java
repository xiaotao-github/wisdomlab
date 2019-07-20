package com.fengbiaoedu.kemi.cmd;

import java.util.HashMap;

import org.springframework.transaction.annotation.Transactional;

import com.fengbiaoedu.bean.LabClockInMachineUser;
import com.fengbiaoedu.bean.LabClockInUserInfoExample;
import com.fengbiaoedu.bean.LabClockInUserInfoExample.Criteria;
import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.mapper.LabClockInMachineUserMapper;
import com.fengbiaoedu.mapper.LabClockInUserInfoMapper;
import com.fengbiaoedu.utils.SpringUtil;

public class DeleteUserCmdStrategy extends BaseCmdStrategy {


	@Override
	@Transactional
	public Boolean processResponse(HashMap<String, Object> repProcessMap) {
		
		Boolean result = (Boolean) repProcessMap.get("result");
		CmdDTO repCmdDTO = (CmdDTO) repProcessMap.get("cmdDto");
		if(repCmdDTO ==null) {
			return false;
		}
		//获取userOperateWorkKey
		String userOperateWorkKey = repCmdDTO.getUserOperateWorkKey();
		if(userOperateWorkKey==null ) {
			return true;
		}
		//删除用户成功，在数据库删除相关记录
		if(result) {
			//伪删除
			//SpringUtil.getBean(LabClockInMachineUserMapper.class).updateToDeleted(repCmdDTO.getUser_id());
			//根据machineUser主键，获取userId
			Long id = repCmdDTO.getUser_id();
			LabClockInMachineUser user = SpringUtil.getBean(LabClockInMachineUserMapper.class).selectByPrimaryKey(id);
			
			String[] workKeyArray = userOperateWorkKey.split(",");
			
			//判断当前是通过考勤机用户录入总表删除，还是通过考勤机下的用户列表删除
			Boolean deleteByUserInfoList = KemiCmdEnum.DELETE_USER_BY_USERINFOLIST.getMsg().equals(workKeyArray[0]);
			if(deleteByUserInfoList) {//通过考勤机用户录入总表删除
				//删除考勤机下的用户
				SpringUtil.getBean(LabClockInMachineUserMapper.class).deleteByPrimaryKey(id);
				
				//判断当前用户是否还有在别的考勤机中，有,则不进行删除考勤机用户录入总表相关的userInfo
				Boolean existMachine = SpringUtil.getBean(LabClockInMachineUserMapper.class).selectCountByUserId(user.getUserId())>0;
				//删除相应的用户总表信息
				if(!existMachine) {
					LabClockInUserInfoExample  example  = new LabClockInUserInfoExample();
					Criteria criteria = example.createCriteria();
					criteria.andUserIdEqualTo(user.getUserId());
					SpringUtil.getBean(LabClockInUserInfoMapper.class).deleteByExample(example);
				}
				
			}else {//通过考勤机删除
				//删除考勤机下的用户
				SpringUtil.getBean(LabClockInMachineUserMapper.class).deleteByPrimaryKey(id);
			}
			System.out.println("删除用户"+repCmdDTO.getUser_id());
			//处理回调前端回调任务
			processCallBackTask(userOperateWorkKey,KemiCmdEnum.DELETE_USER_RESULT.getMsg());
			return true;
		}
		return false;
	}
	
	
}
