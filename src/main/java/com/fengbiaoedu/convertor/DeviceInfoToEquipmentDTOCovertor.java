package com.fengbiaoedu.convertor;

import cc.wulian.ihome.wan.entity.DeviceEPInfo;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import cn.hutool.core.map.MapUtil;
import com.fengbiaoedu.dto.EquipmentDTO;
import com.fengbiaoedu.utils.DeviceTool;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/7/19.
 */
public class DeviceInfoToEquipmentDTOCovertor {

    /**
     * 解析设备参数  包括 设备名称 设备数据格式化  设备类型 设备id等
     * @param deviceInfo
     * @return
     */
    public static List<EquipmentDTO> convertor(DeviceInfo deviceInfo){
        List<EquipmentDTO> equipmentDTOList = new ArrayList<>();
        //EquipmentDTO equipmentDTO = new EquipmentDTO();
        //BeanUtils.copyProperties(deviceInfo,equipmentDTO);

        //设备名
        Map<String, DeviceEPInfo> deviceEPInfoMap = deviceInfo.getDeviceEPInfoMap();

        if(!MapUtil.isEmpty(deviceEPInfoMap)){
            Set<Map.Entry<String, DeviceEPInfo>> entries = deviceEPInfoMap.entrySet();
            for (Map.Entry<String, DeviceEPInfo> temp :entries) {
                EquipmentDTO equipmentDTO = new EquipmentDTO();
                BeanUtils.copyProperties(deviceInfo,equipmentDTO);
                equipmentDTO.setEp(temp.getKey());
                DeviceEPInfo value = temp.getValue();
                equipmentDTO.setEp(value.getEp());
                String epData = DeviceTool.getCloseOrOpenByTypeAndEpData(deviceInfo.getType(), value.getEpData());
                if(epData==null){
                    equipmentDTO.setEpData(value.getEpData());
                }else{
                    equipmentDTO.setEpData(epData);
                }
                equipmentDTO.setEp(deviceInfo.getDevID()+"_"+value.getEp());
                equipmentDTOList.add(equipmentDTO);
            }
        }else{
            EquipmentDTO equipmentDTO = new EquipmentDTO();
            BeanUtils.copyProperties(deviceInfo,equipmentDTO);
            equipmentDTO.setEp("14");
            equipmentDTO.setDevID(equipmentDTO.getDevID()+"_14");
            String epData = null;
            if(deviceInfo.getDevEPInfo()!=null){
                epData = DeviceTool.getCloseOrOpenByTypeAndEpData(deviceInfo.getType(), deviceInfo.getDevEPInfo().getEpData());
                if(epData==null){
                    epData = deviceInfo.getDevEPInfo().getEpData();
                }
            }
            equipmentDTO.setEpData(epData);
            equipmentDTOList.add(equipmentDTO);
        }
        return equipmentDTOList;
    }

    public static List<EquipmentDTO> convertor(List<DeviceInfo> deviceInfos){
        List<EquipmentDTO> dtos = new ArrayList<>();
        for (DeviceInfo deviceInfo:deviceInfos) {
            dtos.addAll(convertor(deviceInfo));
        }
        return dtos;
    }

}
