package com.fengbiaoedu.mapper;

import com.fengbiaoedu.bean.LabClockInMachine;
import com.fengbiaoedu.bean.LabClockInMachineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LabClockInMachineMapper {
    int countByExample(LabClockInMachineExample example);

    int deleteByExample(LabClockInMachineExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LabClockInMachine record);

    int insertSelective(LabClockInMachine record);

    List<LabClockInMachine> selectByExample(LabClockInMachineExample example);

    LabClockInMachine selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LabClockInMachine record, @Param("example") LabClockInMachineExample example);

    int updateByExample(@Param("record") LabClockInMachine record, @Param("example") LabClockInMachineExample example);

    int updateByPrimaryKeySelective(LabClockInMachine record);

    int updateByPrimaryKey(LabClockInMachine record);

	List<String> getClockInIdListByUserIdList(@Param("userIdList")List<Integer> userIdList);

	List<LabClockInMachine> selectByUserId(@Param("userId")Integer userId);
}