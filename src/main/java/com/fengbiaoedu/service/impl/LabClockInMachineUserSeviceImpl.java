package com.fengbiaoedu.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fengbiaoedu.bean.LabClockInMachineUser;
import com.fengbiaoedu.bean.LabClockInMachineUserExample;
import com.fengbiaoedu.mapper.LabClockInMachineUserMapper;
import com.fengbiaoedu.pojo.ClockUser;
import com.fengbiaoedu.service.LabClockInMachineUserService;

@Service
public class LabClockInMachineUserSeviceImpl implements LabClockInMachineUserService {
	
	@Autowired
	LabClockInMachineUserMapper clockInMachineUserMapper;

	/**
	 * 新增考勤机用户</br>
	 * 返回自增主键id</br>
	 */
	@Override
	public Long addClockInMachineUser(ClockUser user,String clockInId,Integer operatorId) {
		
		HashMap<String,Object> map = new HashMap<>();
		map.put("user", user);
		map.put("clockInId", clockInId);
		clockInMachineUserMapper.save(map);
		return (Long) map.get("id");
	}

	/**
	 * 更新idFlag
	 */
	@Override
	public void updateIdFalg(Long userId) {
		
		clockInMachineUserMapper.updateIdFalg(userId);
		
	}

	/**
	 * 得到考勤机用户通过考勤机id和userId</br>
	 * @param clockInId  考勤机id</br>
	 * @param userId
	 * @return
	 */
	@Override
	public LabClockInMachineUser getClockInMachineUserByClockInIdAndUserId(String clockInId, Integer userId) {
		LabClockInMachineUserExample example = new LabClockInMachineUserExample();
		com.fengbiaoedu.bean.LabClockInMachineUserExample.Criteria criteria = example.createCriteria();
		criteria.andClockinIdEqualTo(clockInId);
		criteria.andUserIdEqualTo(userId);
		List<LabClockInMachineUser> list = clockInMachineUserMapper.selectByExample(example);
		if(list!=null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

}
