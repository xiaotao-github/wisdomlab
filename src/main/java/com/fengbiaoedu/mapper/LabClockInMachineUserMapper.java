package com.fengbiaoedu.mapper;

import com.fengbiaoedu.bean.LabClockInMachineUser;
import com.fengbiaoedu.bean.LabClockInMachineUserExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LabClockInMachineUserMapper {
    int countByExample(LabClockInMachineUserExample example);

    int deleteByExample(LabClockInMachineUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(LabClockInMachineUser record);

    int insertSelective(LabClockInMachineUser record);

    List<LabClockInMachineUser> selectByExampleWithBLOBs(LabClockInMachineUserExample example);

    List<LabClockInMachineUser> selectByExample(LabClockInMachineUserExample example);

    LabClockInMachineUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LabClockInMachineUser record, @Param("example") LabClockInMachineUserExample example);

    int updateByExampleWithBLOBs(@Param("record") LabClockInMachineUser record, @Param("example") LabClockInMachineUserExample example);

    int updateByExample(@Param("record") LabClockInMachineUser record, @Param("example") LabClockInMachineUserExample example);

    int updateByPrimaryKeySelective(LabClockInMachineUser record);

    int updateByPrimaryKeyWithBLOBs(LabClockInMachineUser record);

    int updateByPrimaryKey(LabClockInMachineUser record);
    
    Integer save(HashMap<String,Object> map);

	Integer getUserNumber(Integer userId);

	Integer updateIdFalg(Long userId);

	Integer deleteByUserId(Long userId);

	Integer updateToDeleted(Long userId);
	/**
	 * 批量更新用户IdFlag </br>
	 * @param idforConvetorList 要转换的用户userIdList</br>
	 * @return
	 */
	Integer batchUpdateUserIdFlag(List<Long> idforConvetorList);
	
	/**
	 * 批量更新用户的考勤机登记号</br>
	 * @param idList 转换的用户userIdList</br>
	 * @return
	 */
	Integer batchUpdateUserEnrollId(List<Long> idList);
	/**
	 * 查询userid的考勤机id列表</br>
	 * @param userId
	 * @param clockinId 考勤机id</br>
	 * @param excludeCurrentClockinId  true排除当前考勤机    false不排除</br>
	 * @return
	 */
	List<LabClockInMachineUser> selectClockinIdListByUserId(@Param("userId")Integer userId, @Param("clockinId")String clockinId,@Param("exclude") boolean excludeCurrentClockinId);
	
	/**
	 * 获取考勤机存在userIdList的userIdList</br>
	 * @param userIdList
	 * @param clockinId
	 * @return
	 */
	List<Integer> selectUserIdListByUserIdList(@Param("list")List<Integer> userIdList, @Param("clockinId")String clockinId);
	/**
	 * 获取userId在几个考勤机中</br>
	 * @param userId
	 * @return
	 */
	Integer selectCountByUserId(@Param("userId")Integer userId);
	/**
	 * 根据考勤机id和userId列表获取考勤机用户列表
	 * @param clockinId
	 * @param userIdList
	 * @return
	 */
	List<LabClockInMachineUser> selectByUserIdListAndClockinId(@Param("clockinId")String clockinId,@Param("userIdList") List<Integer> userIdList);
}