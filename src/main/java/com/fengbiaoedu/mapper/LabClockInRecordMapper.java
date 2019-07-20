package com.fengbiaoedu.mapper;

import com.fengbiaoedu.bean.LabClockInRecord;
import com.fengbiaoedu.bean.LabClockInRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LabClockInRecordMapper {
    int countByExample(LabClockInRecordExample example);

    int deleteByExample(LabClockInRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(LabClockInRecord record);

    int insertSelective(LabClockInRecord record);

    List<LabClockInRecord> selectByExample(LabClockInRecordExample example);

    LabClockInRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LabClockInRecord record, @Param("example") LabClockInRecordExample example);

    int updateByExample(@Param("record") LabClockInRecord record, @Param("example") LabClockInRecordExample example);

    int updateByPrimaryKeySelective(LabClockInRecord record);

    int updateByPrimaryKey(LabClockInRecord record);
}