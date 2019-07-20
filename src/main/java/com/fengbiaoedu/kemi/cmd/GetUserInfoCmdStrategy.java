package com.fengbiaoedu.kemi.cmd;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengbiaoedu.bean.LabClockInMachineUser;
import com.fengbiaoedu.bean.LabClockInUserInfoExample;
import com.fengbiaoedu.bean.LabClockInUserInfoExample.Criteria;
import com.fengbiaoedu.bean.LabClockInUserInfoWithBLOBs;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.mapper.LabClockInMachineUserMapper;
import com.fengbiaoedu.mapper.LabClockInUserInfoMapper;
import com.fengbiaoedu.pojo.ClockInUserInfoBO;
import com.fengbiaoedu.utils.ClockInUtils;
import com.fengbiaoedu.utils.SpringUtil;

public class GetUserInfoCmdStrategy extends BaseCmdStrategy {
	
	private ClockInUtils clockInUtils = SpringUtil.getBean(ClockInUtils.class);
	
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
	public Object processResponse(HashMap<String, Object> repProcessMap) {
		
		if(!(boolean) repProcessMap.get("result")) {
			return false;
		}
		CmdDTO cmdDto = (CmdDTO) repProcessMap.get("cmdDto");
		//获取二进制信息
		Object[] stringAnd1stBinary = clockInUtils.getStringAnd1stBinaryFromResponseBuffer(cmdDto.getResponseByte(),1);
		ClockInUserInfoBO user = new ClockInUserInfoBO();
		ObjectMapper objectMapper = new ObjectMapper();
		String userJson = (String) stringAnd1stBinary[0];
		byte[] enrollData = (byte[]) stringAnd1stBinary[1];
		try {
			if(userJson != "" && userJson != null) {
				user = objectMapper.readValue(userJson, ClockInUserInfoBO.class);
				//获取二进制Map
				/**
				 HashMap<String,byte[]> enrollDataMap = ClockInUtils.getEnrollDataMap(user,enrollData);
				 System.out.println(new String(enrollDataMap.get("backup_number_10"),"UTF-8"));
				 HashMap<String,Object> paramMap = new HashMap<>();
				 paramMap.put("enrollDataMap", enrollDataMap);
				 paramMap.put("user", user);
				 */
				 //获取根据工号获取学生用户id
				 //Integer studentId = SpringUtil.getBean(ClockInMachineUserMapper.class).getUserNumber(Integer.parseInt(user.getUser_id()));
				 LabClockInMachineUser clockInMachineUser = SpringUtil.getBean(LabClockInMachineUserMapper.class).selectByPrimaryKey(Long.parseLong(user.getUser_id()));
				 LabClockInUserInfoWithBLOBs clockInUserInfo = clockInUtils.getClockInUserInfoWithBLOBs(user, enrollData);
				 //clockInUserInfo.setStudentId(studentId);
				 clockInUserInfo.setFaceEnrollTime(ClockInUtils.stringToTime(user.getFace_enroll_time()));
				 clockInUserInfo.setPasswordEnrollTime(ClockInUtils.stringToTime(user.getPassword_enroll_time()));
				 clockInUserInfo.setFpEnrollTime(ClockInUtils.stringToTime(user.getFp_enroll_time()));
				 clockInUserInfo.setUpdateTime(new Date());
				 
				 LabClockInUserInfoExample clockInUserInfoExample = new LabClockInUserInfoExample();
				 Criteria createCriteria = clockInUserInfoExample.createCriteria();
				 createCriteria.andUserIdEqualTo(clockInMachineUser.getUserId());
				 SpringUtil.getBean(LabClockInUserInfoMapper.class).updateByExampleSelective(clockInUserInfo, clockInUserInfoExample);
				 
				 //SpringUtil.getBean(ClockInManageMapper.class).updateClockInUserInfo(paramMap);
				 
				 return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
