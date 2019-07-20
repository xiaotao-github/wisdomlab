package com.fengbiaoedu.service;

import com.fengbiaoedu.bean.LabClockInMachineUser;
import com.fengbiaoedu.pojo.ClockUser;

public interface LabClockInMachineUserService {
	
	public Long addClockInMachineUser(ClockUser user,String clockInId, Integer operatorId) ;
	
	public void updateIdFalg(Long userId);
	
	/**
	 * 得到考勤机用户通过考勤机id和userId</br>
	 * @param clockInId  考勤机id</br>
	 * @param userId
	 * @return
	 */
	public LabClockInMachineUser getClockInMachineUserByClockInIdAndUserId(String clockInId,Integer userId);
	
}
