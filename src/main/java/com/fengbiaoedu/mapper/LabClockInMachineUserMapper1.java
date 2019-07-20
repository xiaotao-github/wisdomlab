package com.fengbiaoedu.mapper;

import java.util.HashMap;

public interface LabClockInMachineUserMapper1 {
	
	Integer save(HashMap<String,Object> map);

	Integer getUserNumber(Integer userId);

	Integer updateIdFalg(Long userId);

	Integer deleteByPrimaryKey(Long userId);

	Integer updateToDeleted(Long userId);
}
