package com.fengbiaoedu.service;

import cc.wulian.ihome.wan.entity.DeviceEPInfo;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import com.fengbiaoedu.dto.EquipmentDTO;
import com.fengbiaoedu.vo.ControlEuqData;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/15.
 */
public interface EquipmentService {

    /**
     * 根据网关获取设备信息 包括上线设备和离线设备
     *
     * @param wgId     网关id
     * @param wpd      网关密码
     * @param deviceId 设备id
     * @return List<DeviceInfo>
     */
    List<DeviceInfo> getEuq(String wgId, String wpd, String deviceId);

    /**
     * 控制设备
     *
     * @param controlEuqData 控制设备需要的数据信息
     */
    void controlEuq(ControlEuqData controlEuqData);

    /**
     * 获取可控设备的数据信息
     *
     * @param gwId     网关id
     * @param pwd      网关密码
     * @param deviceId 设备id
     * @return 返回可控设备的list集合
     */
    List<EquipmentDTO> getControlledEuq(String gwId, String pwd, String deviceId);

    /**
     * 获取具体设备的数据信息
     *
     * @param gwId  网关id
     * @param pwd   网关密码是
     * @param devId 要获取的具体设备id
     * @param type  类型 0.温湿度数据   1.光照数据     2.空气质量
     * @return 返回封装好的数据EquipmentDTO集合
     */
    EquipmentDTO getEqueData(String gwId, String pwd, String devId, Integer type);

    /**
     * 获取所有传感设备
     *
     * @param gwId 网关id
     * @param pwd  网关密码
     * @return 传感设备集合
     */
    List<EquipmentDTO> getSenseEq(String gwId, String pwd);

    /**
     * 将设备持久化到数据库
     * <p>
     * 1.判断设备中是否有子按键（分支）
     * 有--当成一个设备来添加
     * 无--添加当前设备
     * 判断当前设备中是否有该设备
     * --有更新
     * --无添加
     *
     * @param deviceInfo 设备信息
     */
    void persistanceToDataBase(DeviceInfo deviceInfo, Set<DeviceEPInfo> devEPInfoSet);
    
    /*
     * commandType 设备类型
     * */
     void getTypeControl(Integer commandType,String wgId,Integer code);

     /*自定义控制设备(场景控制)
     * */
     void customControlEuq(ControlEuqData controlEuqData,Integer code);
}

