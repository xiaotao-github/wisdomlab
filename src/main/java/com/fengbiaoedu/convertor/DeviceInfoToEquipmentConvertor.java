package com.fengbiaoedu.convertor;

import cc.wulian.ihome.wan.entity.DeviceInfo;
import com.fengbiaoedu.dto.EquipmentDTO;
import com.fengbiaoedu.utils.DeviceTool;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/5/15.
 */
public class DeviceInfoToEquipmentConvertor {
    /**
     * 解析设备参数  包括 设备名称 设备数据格式化  设备类型 设备id等
     * @param deviceInfo
     * @return
     */
    public static EquipmentDTO convertor(DeviceInfo deviceInfo){
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        BeanUtils.copyProperties(deviceInfo,equipmentDTO);
        //设备名
        equipmentDTO.setName(DeviceTool.getDevDefaultNameByType(deviceInfo.getType()));
        if(deviceInfo.getDevEPInfo()!=null){
            equipmentDTO.setEp(deviceInfo.getDevEPInfo().getEp());
            if(deviceInfo.getDevEPInfo().getEpData()!=null){
             //   equipmentDTO.setEpData(DeviceTool.getDevDataText(deviceInfo.getDevEPInfo().getEpType(),deviceInfo.getDevEPInfo().getEpData(),deviceInfo.getDevEPInfo().getEpStatus()));
                String epData = deviceInfo.getDevEPInfo().getEpData();
                if(epData.endsWith("0")){
                    equipmentDTO.setEpData("0");
                }else if(epData.endsWith("1")){
                    equipmentDTO.setEpData("1");
                }else{
                    equipmentDTO.setEpData(deviceInfo.getDevEPInfo().getEpData());
                }
            }
        }
        if(StringUtils.isEmpty(equipmentDTO.getEp())){
            equipmentDTO.setEp("14");
        }
        return equipmentDTO;
    }

    public static List<EquipmentDTO> convertor(List<DeviceInfo> deviceInfos){
         return deviceInfos.stream().map(e->convertor(e)).collect(Collectors.toList());
    }

}
