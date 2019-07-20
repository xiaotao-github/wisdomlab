package com.fengbiaoedu.owconvertor;

import cc.wulian.ihome.wan.entity.DeviceEPInfo;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import cn.hutool.core.map.MapUtil;
import com.fengbiaoedu.dto.EquipmentDTO;
import com.fengbiaoedu.owdto.OWEquipmentDTO;
import com.fengbiaoedu.utils.DeviceTool;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/7/19.
 */
public class OWDeviceInfoToEquipmentDTOCovertor {

    /**
     * 解析设备参数  包括 设备名称 设备数据格式化  设备类型 设备id等
     * @param deviceInfo
     * @return
     */
    public static List<OWEquipmentDTO> convertor(DeviceInfo deviceInfo){
        List<OWEquipmentDTO> equipmentDTOList = new ArrayList<>();
        //EquipmentDTO equipmentDTO = new EquipmentDTO();
        //BeanUtils.copyProperties(deviceInfo,equipmentDTO);

        //设备名
        Map<String, DeviceEPInfo> deviceEPInfoMap = deviceInfo.getDeviceEPInfoMap();

        if(!MapUtil.isEmpty(deviceEPInfoMap)){
            Set<Map.Entry<String, DeviceEPInfo>> entries = deviceEPInfoMap.entrySet();
            for (Map.Entry<String, DeviceEPInfo> temp :entries) {
                OWEquipmentDTO owEquipmentDTO = new OWEquipmentDTO();
                BeanUtils.copyProperties(deviceInfo,owEquipmentDTO);
                owEquipmentDTO.setEp(temp.getKey());
                DeviceEPInfo value = temp.getValue();
                owEquipmentDTO.setEp(value.getEp());
                String epData = DeviceTool.getCloseOrOpenByTypeAndEpData(deviceInfo.getType(), value.getEpData());
                if(epData==null){
                    owEquipmentDTO.setEpData(value.getEpData());
                }else{
                    owEquipmentDTO.setEpData(epData);
                }
                owEquipmentDTO.setEp(deviceInfo.getDevID()+"_"+value.getEp());
                equipmentDTOList.add(owEquipmentDTO);
            }
        }else{
            OWEquipmentDTO owEquipmentDTO = new OWEquipmentDTO();
            BeanUtils.copyProperties(deviceInfo,owEquipmentDTO);
            owEquipmentDTO.setEp("14");
            owEquipmentDTO.setDevID(owEquipmentDTO.getDevID()+"_14");
            String epData = null;
            if(deviceInfo.getDevEPInfo()!=null){
                epData = DeviceTool.getCloseOrOpenByTypeAndEpData(deviceInfo.getType(), deviceInfo.getDevEPInfo().getEpData());
                if(epData==null){
                    epData = deviceInfo.getDevEPInfo().getEpData();
                }
            }
            owEquipmentDTO.setEpData(epData);
            equipmentDTOList.add(owEquipmentDTO);
        }
        return equipmentDTOList;
    }

    public static List<OWEquipmentDTO> convertor(List<DeviceInfo> deviceInfos){
        List<OWEquipmentDTO> dtos = new ArrayList<>();
        for (DeviceInfo deviceInfo:deviceInfos) {
            dtos.addAll(convertor(deviceInfo));
        }
        return dtos;
    }

}
