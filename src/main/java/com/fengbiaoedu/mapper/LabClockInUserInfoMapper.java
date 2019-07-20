package com.fengbiaoedu.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fengbiaoedu.bean.LabClockInUserInfo;
import com.fengbiaoedu.bean.LabClockInUserInfoExample;
import com.fengbiaoedu.bean.LabClockInUserInfoWithBLOBs;

public interface LabClockInUserInfoMapper {
    int countByExample(LabClockInUserInfoExample example);

    int deleteByExample(LabClockInUserInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(LabClockInUserInfoWithBLOBs record);

    int insertSelective(LabClockInUserInfoWithBLOBs record);

    List<LabClockInUserInfoWithBLOBs> selectByExampleWithBLOBs(LabClockInUserInfoExample example);

    List<LabClockInUserInfo> selectByExample(LabClockInUserInfoExample example);

    LabClockInUserInfoWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LabClockInUserInfoWithBLOBs record, @Param("example") LabClockInUserInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") LabClockInUserInfoWithBLOBs record, @Param("example") LabClockInUserInfoExample example);

    int updateByExample(@Param("record") LabClockInUserInfo record, @Param("example") LabClockInUserInfoExample example);

    int updateByPrimaryKeySelective(LabClockInUserInfoWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LabClockInUserInfoWithBLOBs record);

    int updateByPrimaryKey(LabClockInUserInfo record);
    
    int updateClockInUserInfo(@Param("record") LabClockInUserInfoWithBLOBs record, @Param("example") LabClockInUserInfoExample example);
    
	List<LabClockInUserInfoWithBLOBs> selectByClassId(HashMap<String, Object> paramMap);
	/**
	 * 批量初始化新增考勤用户信息
	 * @param paramMap
	 * @return
	 */
	int batchInsert(HashMap<String, Object> paramMap);
	
	
	/**
	 * 根据考勤id获取考勤用户信息列表</br>
	 * @param clockinId
	 * @return
	 */
	List<LabClockInUserInfoWithBLOBs> selectByClockInId(String clockinId);
	
	/**
	 * 根据userIdList获取考勤用户信息列表</br>
	 * @param paramMap
	 * @return
	 */
	List<LabClockInUserInfo> selectClockInUserInfoByUserIdList(HashMap<String, Object> paramMap);
	
	/**
	 *   根据classId获取要初始化的考勤用户列表信息</br>
	 * @param paramMap </br>
	 * 		  key classId value(String) classId
	 * @return
	 */
	List<LabClockInUserInfo> selectClockInUserInfoByClassId(HashMap<String, Object> paramMap);

	Integer batchDelete(@Param("userIdList")List<Integer> userIdList);
}