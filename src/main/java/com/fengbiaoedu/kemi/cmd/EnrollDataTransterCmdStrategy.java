package com.fengbiaoedu.kemi.cmd;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fengbiaoedu.bean.LabClockInMachineUser;
import com.fengbiaoedu.bean.LabClockInMachineUserExample;
import com.fengbiaoedu.bean.LabClockInUserInfoExample;
import com.fengbiaoedu.bean.LabClockInUserInfoExample.Criteria;
import com.fengbiaoedu.bean.LabClockInUserInfoWithBLOBs;
import com.fengbiaoedu.config.ClockinConfig;
import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.kemi.ClockInManager;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.mapper.LabClockInMachineUserMapper;
import com.fengbiaoedu.mapper.LabClockInUserInfoMapper;
import com.fengbiaoedu.mq.ActiveMQService;
import com.fengbiaoedu.pojo.ClockInUserInfoBO;
import com.fengbiaoedu.redis.RedisLock;
import com.fengbiaoedu.utils.ClockInUtils;
import com.fengbiaoedu.utils.JsonUtils;
import com.fengbiaoedu.utils.RedisUtil;
import com.fengbiaoedu.utils.SpringUtil;
import com.fengbiaoedu.utils.Utils;

public class EnrollDataTransterCmdStrategy extends BaseCmdStrategy {
	
	private ClockInUtils clockInUtils = SpringUtil.getBean(ClockInUtils.class);
	
	@Override
	public Object processResponse(HashMap<String, Object> repProcessMap) {
		if(!(boolean) repProcessMap.get("result")) {
			return false;
		}
		CmdDTO cmdDto = (CmdDTO) repProcessMap.get("cmdDto");
		//获取二进制信息
		Object[] stringAnd1stBinary = clockInUtils.getStringAnd1stBinaryFromResponseBuffer(cmdDto.getResponseByte(),1);
		ClockInUserInfoBO user = new ClockInUserInfoBO();
		String userJson = (String) stringAnd1stBinary[0];
		byte[] enrollData = (byte[]) stringAnd1stBinary[1];
		try {
			if(userJson != "" && userJson != null) {
				
				//考勤机的传递过来的userjosn转换为ClockInUserInfoBo对象
				user = JsonUtils.getMapper().readValue(userJson, ClockInUserInfoBO.class);
				
				LabClockInMachineUser machineUser = new LabClockInMachineUser();
				LabClockInMachineUserExample machineUserExample= new LabClockInMachineUserExample();
				com.fengbiaoedu.bean.LabClockInMachineUserExample.Criteria machineUserCriteria = machineUserExample.createCriteria();
				machineUserCriteria.andEnrollIdEqualTo(Long.parseLong(user.getUser_id()));
				//这里的cmdDto.getTrans_id() =clockinId
				machineUserCriteria.andClockinIdEqualTo(cmdDto.getTrans_id());
				
				//查询考勤机用户是为，获取用户的userId，关联考勤用户信息表，更新考勤用户信息
				List<LabClockInMachineUser> machineUserList = SpringUtil.getBean(LabClockInMachineUserMapper.class).selectByExample(machineUserExample);
				if(machineUserList == null ||  machineUserList.isEmpty()) {
					//没有该用户,需要记录日志
					return false;
				}
				machineUser = machineUserList.get(0);
				
				//获取该用户在其他考勤机id列表
				List<LabClockInMachineUser> userList = SpringUtil.getBean(LabClockInMachineUserMapper.class).selectClockinIdListByUserId(machineUser.getUserId(),machineUser.getClockinId(),true);
				
				//保存每台考勤机加锁的value，方便解锁
				ConcurrentLinkedQueue<Long> timeQueue = new ConcurrentLinkedQueue<>();
				
				//加锁每一台考勤机
				if(userList!=null && !userList.isEmpty()) {
					for (LabClockInMachineUser clockInuser : userList) {
						Long time = System.currentTimeMillis() +SpringUtil.getBean(ClockinConfig.class).getClockinMachineLockTimeOut();
						timeQueue.offer(time);
						if(!SpringUtil.getBean(RedisLock.class).lock(SpringUtil.getBean(RedisUtil.class).getReqQueueLockKey(clockInuser.getClockinId()), String.valueOf(time))) {
							//重新向mq，发送消息
							SpringUtil.getBean(ActiveMQService.class).sendEnroolData(JsonUtils.getMapper().writeValueAsString(cmdDto));
							//解锁
							SpringUtil.getBean(RedisLock.class).unLock(SpringUtil.getBean(RedisUtil.class).getReqQueueLockKey(clockInuser.getClockinId()), String.valueOf(timeQueue.poll()));
							return false;
						}
				 	}
				}
				LabClockInUserInfoWithBLOBs clockInUserInfo = clockInUtils.getClockInUserInfoWithBLOBs(user, enrollData);
				
			    machineUser.setUpdateTime(new Date());
				machineUser.setEnrollPassword(clockInUserInfo.getEnrollPassword());
				machineUser.setUserPrivilege(clockInUserInfo.getUserPrivilege());
				 
				SpringUtil.getBean(LabClockInMachineUserMapper.class).updateByExampleSelective(machineUser,machineUserExample);
				 
				//设置考勤机用户更新时间
				clockInUserInfo.setUpdateTime(new Date());
				 
				LabClockInUserInfoExample clockInUserInfoExample = new LabClockInUserInfoExample();
				Criteria createCriteria = clockInUserInfoExample.createCriteria();
				createCriteria.andUserIdEqualTo(machineUser.getUserId());
				//更新考勤机用户指纹等信息
				SpringUtil.getBean(LabClockInUserInfoMapper.class).updateClockInUserInfo(clockInUserInfo, clockInUserInfoExample);
				
				//更新到其他考勤机，并解锁
				if(userList!=null && !userList.isEmpty()) {
					//如果当前用户没有指纹的话，当该用户之前有指纹的话，还是会传过来录入指纹时间，这会导致更新其他考勤机信息时会写入失败
					user.setFp_enroll_time(ClockInUtils.timeToString(clockInUserInfo.getFpEnrollTime()));
					
					for (LabClockInMachineUser clockInuser : userList) {
						
						//设置考勤机要写入的工号
						user.setUser_id(String.valueOf(clockInuser.getEnrollId()));
						//获取用户的json字节数组
						String json = JsonUtils.getMapper().writeValueAsString(user);
						byte[] jsonBytes = clockInUtils.CreateByteFromString(json, new byte[0]);
						//拼接字节数组并设置到cmdDto
						cmdDto.setResponseByte(Utils.byteMergerAll(jsonBytes,enrollData));
						
						ConcurrentLinkedQueue< CmdDTO> cmdQueue = new ConcurrentLinkedQueue<>();
						cmdQueue.add(new CmdDTO(clockInUtils.setAndGetTransId(clockInuser.getLabId()), cmdDto.getResponseByte(),KemiCmdEnum.SET_USER_INFO.getMsg(),clockInuser.getEnrollId()));
						//增加到更改指定考勤机的用户信息的队列
						SpringUtil.getBean(RedisUtil.class).putCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, clockInuser.getClockinId(), cmdQueue);
						
						//解锁
						SpringUtil.getBean(RedisLock.class).unLock(SpringUtil.getBean(RedisUtil.class).getReqQueueLockKey(clockInuser.getClockinId()), String.valueOf(timeQueue.poll()));
				 	}
				}
				return true;
			}
		} catch (IOException e) {//发生io异常在加锁前，不需要解锁
			System.out.println("io异常：json字符串转换成ClockInUserInfoBo对象时出现异常");
		}
		return false;
	}
}
