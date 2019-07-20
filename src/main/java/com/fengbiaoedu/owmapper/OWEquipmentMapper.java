package com.fengbiaoedu.owmapper;

import com.fengbiaoedu.owbean.OWEquipment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Administrator on 2018/7/12.
 */
public interface OWEquipmentMapper {

	/**
	 * 修改设备
	 * 
	 * @param owEquipment
	 * @return 0.修改失败 >0.修改成功
	 */
	@Update("update equipment set ep_type=#{epType},ep_name=#{epName},update_time=#{updateTime},seat_num=#{seatNum} where gw_id=#{gwId} and dev_id=#{devId} and ep = #{ep}")
	Integer updateEquipment(OWEquipment owEquipment);

	void insertSelective(OWEquipment owEquipment);

	/*
	 * 根据类型获取对应的设备id
	 */
	@Select("select * from equipment WHERE lab_ep_type=#{commandType} AND gw_id =#{wgId} AND stealth = 2 ")
	List<OWEquipment> getTypeDv(@Param("commandType") Integer commandType, @Param("wgId") String wgId);
}
