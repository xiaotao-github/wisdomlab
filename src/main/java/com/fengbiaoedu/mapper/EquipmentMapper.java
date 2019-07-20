package com.fengbiaoedu.mapper;

import com.fengbiaoedu.bean.Equipment;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Administrator on 2018/7/12.
 */
public interface EquipmentMapper {

	/**
	 * 修改设备
	 * 
	 * @param equipment
	 * @return 0.修改失败 >0.修改成功
	 */
	@Update("update equipment set ep_type=#{epType},ep_name=#{epName},update_time=#{updateTime},seat_num=#{seatNum} where gw_id=#{gwId} and dev_id=#{devId} and ep = #{ep}")
	Integer updateEquipment(Equipment equipment);

	void insertSelective(Equipment equipment);

	/*
	 * 根据类型获取对应的设备id
	 */
	@Select("select * from equipment WHERE lab_ep_type=#{commandType} AND gw_id =#{wgId} AND stealth = 2 ")
	List<Equipment> getTypeDv(@Param("commandType") Integer commandType, @Param("wgId") String wgId);
}
