package com.fengbiaoedu.kemi.cmd;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;

import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.mapper.LabClockInMachineUserMapper;
import com.fengbiaoedu.utils.SpringUtil;

public class SetUserInfoCmdStrategy extends BaseCmdStrategy {
	
	@Override
	public HttpServletResponse processRequest(HashMap<String,Object> repProcessMap) {
		
		HttpServletResponse rep = (HttpServletResponse) repProcessMap.get("HttpServletResponse");
		CmdDTO repCmdDTO = (CmdDTO) repProcessMap.get("cmdDto");
		if(repCmdDTO!=null ) {
			try {
				ServletOutputStream outputStream = rep.getOutputStream();
				outputStream.write(repCmdDTO.getResponseByte());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("setUserInfoCmdRequest");
		return rep;
	}
	
	
	@Override
	@Transactional
	public Boolean processResponse(HashMap<String,Object> repProcessMap) {
		Boolean result = (Boolean) repProcessMap.get("result");
		//添加用户失败，删除已在数据库添加的记录
		System.out.println(result);
		CmdDTO repCmdDTO = (CmdDTO) repProcessMap.get("cmdDto");
		if(repCmdDTO ==null) {
			return false;
		}
		//获取userOperateWorkKey
		String userOperateWorkKey = repCmdDTO.getUserOperateWorkKey();
		//为null，表示考勤机用户登记数据的操作，不用回调给前端，直接返回
		if(userOperateWorkKey==null && result) {
			return true;
		}
		//写入考勤机失败
		if(!result && userOperateWorkKey!=null) {
			SpringUtil.getBean(LabClockInMachineUserMapper.class).deleteByPrimaryKey(repCmdDTO.getUser_id());
			System.out.println("setUserInfoCmdResponse");
			return false;
		}
		//处理回调前端回调任务
		processCallBackTask(userOperateWorkKey,KemiCmdEnum.SET_USER_INFO_RESULT.getMsg());
		return true;
	}

	

	

}
