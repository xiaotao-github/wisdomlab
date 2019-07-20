package com.fengbiaoedu.owconvertor;

import cc.wulian.ihome.wan.entity.DeviceInfo;
import com.fengbiaoedu.dto.EquipmentDTO;
import com.fengbiaoedu.owdto.OWEquipmentDTO;
import com.fengbiaoedu.owutils.OWDeviceTool;
import com.fengbiaoedu.utils.DeviceTool;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/5/15.
 */
public class OWDeviceInfoToEquipmentConvertor {
    /**
     * 解析设备参数  包括 设备名称 设备数据格式化  设备类型 设备id等
     * @param deviceInfo
     * @return
     */
    public static OWEquipmentDTO convertor(DeviceInfo deviceInfo){
        OWEquipmentDTO owEquipmentDTO = new OWEquipmentDTO();
        BeanUtils.copyProperties(deviceInfo,owEquipmentDTO);
        //设备名
        owEquipmentDTO.setName(OWDeviceTool.owGetDevDefaultNameByType(deviceInfo.getType()));
        if(deviceInfo.getDevEPInfo()!=null){
            owEquipmentDTO.setEp(deviceInfo.getDevEPInfo().getEp());
            if(deviceInfo.getDevEPInfo().getEpData()!=null){
             //   equipmentDTO.setEpData(OWDeviceTool.owGetDevDataText(deviceInfo.getDevEPInfo().getEpType(),deviceInfo.getDevEPInfo().getEpData(),deviceInfo.getDevEPInfo().getEpStatus()));
                String epData = deviceInfo.getDevEPInfo().getEpData();
                if(epData.endsWith("0")){
                    owEquipmentDTO.setEpData("0");
                }else if(epData.endsWith("1")){
                    owEquipmentDTO.setEpData("1");
                }else{
                    owEquipmentDTO.setEpData(deviceInfo.getDevEPInfo().getEpData());
                }
            }
        }
        if(StringUtils.isEmpty(owEquipmentDTO.getEp())){
            owEquipmentDTO.setEp("14");
        }
        return owEquipmentDTO;
    }

    public static List<OWEquipmentDTO> convertor(List<DeviceInfo> deviceInfos){
         return deviceInfos.stream().map(e->convertor(e)).collect(Collectors.toList());
    }

}
