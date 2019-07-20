package com.fengbiaoedu.mapper;

import java.util.HashMap;
import java.util.List;

import com.fengbiaoedu.bean.LabClockInUserInfoWithBLOBs;
import com.fengbiaoedu.pojo.ClockUser;

public interface LabClockInManageMapper {
	
	List<ClockUser> getClockUserListByClassId(HashMap<String, Object> map);
	
	void updateClockInUserInfo(HashMap<String,Object>paramMap);

	List<LabClockInUserInfoWithBLOBs> getClockInUserInfoListByClassId(HashMap<String, Object> paramMap);

	List<ClockUser> getClockUserListByLabId(HashMap<String, Object> paramMap);

}
