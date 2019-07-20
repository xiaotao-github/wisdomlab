package com.fengbiaoedu.kemi.cmd;

import java.util.HashMap;

import org.springframework.transaction.annotation.Transactional;

import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.kemi.CmdDTO;
/**
 * 清空考勤机的所有考勤机记录</br>
 * @author Administrator
 *
 */
public class ClearLogDataCmdStrategy extends BaseCmdStrategy {
	
	@Override
	@Transactional
	public Boolean processResponse(HashMap<String, Object> repProcessMap) {
		
		//考勤机的执行结果
		Boolean result = (Boolean) repProcessMap.get("result");
		
		CmdDTO repCmdDTO = (CmdDTO) repProcessMap.get("cmdDto");
		if(repCmdDTO ==null) {
			return false;
		}
		//获取userOperateWorkKey 用户操作考勤机任务标识
		String userOperateWorkKey = repCmdDTO.getUserOperateWorkKey();
		if(userOperateWorkKey==null ) {
			return true;
		}
		//清空考勤机记录成功
		if(result) {
			
			String[] workKeyArray = userOperateWorkKey.split(",");
			
			//判断当前是删除考勤机，还是清空考勤机下的所有注册信息
			Boolean deleteMachine = KemiCmdEnum.DELETE_MACHINE.getMsg().equals(workKeyArray[0]);
			if(deleteMachine) {
				
				//处理回调前端回调任务
				processCallBackTask(userOperateWorkKey,KemiCmdEnum.DELETE_MACHINE_RESULT.getMsg());
			}
			return true;
		}
		return false;
	}
}
