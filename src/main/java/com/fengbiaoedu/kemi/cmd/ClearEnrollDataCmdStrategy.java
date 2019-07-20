package com.fengbiaoedu.kemi.cmd;

import java.util.Date;
import java.util.HashMap;

import org.springframework.transaction.annotation.Transactional;

import com.fengbiaoedu.bean.LabClockInMachine;
import com.fengbiaoedu.bean.LabClockInMachineExample;
import com.fengbiaoedu.bean.LabClockInMachineExample.Criteria;
import com.fengbiaoedu.bean.LabClockInMachineUserExample;
import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.mapper.LabClockInMachineMapper;
import com.fengbiaoedu.mapper.LabClockInMachineUserMapper;
import com.fengbiaoedu.utils.SpringUtil;
/**
 * 情空考勤机的所有登记数据
 * @author Administrator
 *
 */
public class ClearEnrollDataCmdStrategy extends BaseCmdStrategy {
	
	@Override
	@Transactional
	public Boolean processResponse(HashMap<String, Object> repProcessMap) {
		
		Boolean result = (Boolean) repProcessMap.get("result");
		CmdDTO repCmdDTO = (CmdDTO) repProcessMap.get("cmdDto");
		if(repCmdDTO ==null) {
			return false;
		}
		//获取userOperateWorkKey  用户操作考勤机任务标识
		String userOperateWorkKey = repCmdDTO.getUserOperateWorkKey();
		if(userOperateWorkKey==null ) {
			return true;
		}
		//清空考勤机用户成功，在数据库删除相关记录
		if(result) {
			
			String[] workKeyArray = userOperateWorkKey.split(",");
			
			//判断当前是删除考勤机，还是清空考勤机下的所有注册信息
			Boolean deleteMachine = KemiCmdEnum.DELETE_MACHINE.getMsg().equals(workKeyArray[0]);
			if(deleteMachine) {//通过考勤机用户录入总表删除
				
				//更新考勤机标识为已删除
				LabClockInMachineExample example = new LabClockInMachineExample();
				Criteria criteria = example.createCriteria();
				criteria.andClockinIdEqualTo(repCmdDTO.getClockinId());
				LabClockInMachine  machine = new LabClockInMachine();
				machine.setIsDeleted(true);
				machine.setUpdateTime(new Date());
				SpringUtil.getBean(LabClockInMachineMapper.class).updateByExampleSelective(machine, example);
				
				//删除考勤机下的所有用户
				LabClockInMachineUserExample userExample = new LabClockInMachineUserExample();
				com.fengbiaoedu.bean.LabClockInMachineUserExample.Criteria userCriteria = userExample.createCriteria();
				userCriteria.andClockinIdEqualTo(repCmdDTO.getClockinId());
				SpringUtil.getBean(LabClockInMachineUserMapper.class).deleteByExample(userExample);
				//处理回调前端回调任务
				processCallBackTask(userOperateWorkKey,KemiCmdEnum.DELETE_MACHINE_RESULT.getMsg());
			}
			return true;
		}
		return false;
	}

}
