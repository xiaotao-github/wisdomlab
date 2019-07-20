package com.fengbiaoedu.controller;

import cc.wulian.ihome.wan.entity.DeviceInfo;
import com.fengbiaoedu.convertor.DeviceInfoToEquipmentDTOCovertor;
import com.fengbiaoedu.dto.EquipmentDTO;
import com.fengbiaoedu.exception.WisdomException;
import com.fengbiaoedu.service.EquipmentService;
import com.fengbiaoedu.vo.ControlEuqData;
import com.fengbiaoedu.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */
@RestController
@RequestMapping("euqipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    /**
     * 获取指定网关的的指定设备下的设备信息
     * @param wgId 网关
     * @param pwd 密码
     * @param deviceId 设备id
     * @return 设备信息 设备数据 包括上线的设备和下线的设备
     */
    @GetMapping("getEuq/{wgId}/{pwd}/{deviceId}")
    public SysResult getEuq(@PathVariable("wgId") String wgId,
                            @PathVariable("pwd") String pwd,
                            @PathVariable("deviceId") String deviceId){
      List<DeviceInfo> equipmentDTOS =  equipmentService.getEuq(wgId,pwd,deviceId);
      return SysResult.ok(equipmentDTOS);
    }

    /**
     * 获取指定网关下所有设备信息，转换设备的数据
     * @param wgId
     * @param pwd
     * @return
     */
    @GetMapping("getEuqToCovertor/{wgId}/{pwd}")
    public SysResult getEuqToCovertor(@PathVariable("wgId") String wgId,
                            @PathVariable("pwd") String pwd     ){
        List<DeviceInfo> deviceInfos =  equipmentService.getEuq(wgId,pwd,"");
        //转换
        List<EquipmentDTO> equipmentDTOList = DeviceInfoToEquipmentDTOCovertor.convertor(deviceInfos);
        return SysResult.ok(equipmentDTOList);
    }


    //控制具体设备
    /**
     * 需要
     * gwID	网关ID
     devID	设备ID
     ep	设备端口
     epType	端口类型
     epData	设备控制命令
     */
    @PostMapping("controlEuq")
    public SysResult controlEuq(ControlEuqData controlEuqData){
            try{
                equipmentService.controlEuq(controlEuqData);
                return SysResult.ok();
            }catch (WisdomException we){
                return SysResult.build(we.getCode(),we.getMsg());
            }
    }

    /**
     * 获取所有可控设备
     * 1.设备名称   设备的数据信息
     */
    @GetMapping("getControlledEuq/{gwId}/{pwd}/{deviceId}")
    public SysResult getControlledEuq(@PathVariable("gwId")String gwId,
                                      @PathVariable("pwd")String pwd,
                                      @PathVariable("deviceId")String deviceId){
        try {
           List<EquipmentDTO> equipmentDTOList = equipmentService.getControlledEuq(gwId,pwd,deviceId);
           return SysResult.ok(equipmentDTOList);
        }catch (WisdomException we){
            return SysResult.build(we.getCode(),we.getMsg());
        }
    }
    //获取所有传感设备信息 标注：传感设备一般是不可控设备 会传递一些数据2
    @GetMapping("getSenseEqu/{gwId}/{pwd}")
    public SysResult getSenseEq(@PathVariable("gwId")String gwId,@PathVariable("pwd")String pwd){
        try {
            List<EquipmentDTO> equipmentDTOS = equipmentService.getSenseEq(gwId,pwd);
            return SysResult.ok(equipmentDTOS);
        }catch (WisdomException we){
            return SysResult.build(we.getCode(),we.getMsg());
        }
    }
    /**
     * 获取具体传感设备的数据信息
     * @param gwId 网关id
     * @param pwd 网关密码
     * @param devId  具体设备id
     * @param type 默认  0是温湿度   1 是光照   2.空气质量
     * @return
     */
    @GetMapping("getWenShiDuData/{gwId}/{pwd}/{devId}/{type}")
    public SysResult getEqueData(@PathVariable("gwId")String gwId,
                                     @PathVariable("pwd")String pwd,
                                     @PathVariable("devId") String devId,
                                     @PathVariable(required = false)Integer type){
        try {
            EquipmentDTO equipmentDTO = equipmentService.getEqueData(gwId,pwd,devId,type);
            return SysResult.ok(equipmentDTO);
        }catch (WisdomException we){
            return SysResult.build(we.getCode(),we.getMsg());
        }
    }
    //控制类型具体设备
    /**
     * 需要
     * typeId	设备类型
     * wgId  网管id
     * code 开关状态  0 开 1关
     */
    @PostMapping("typeControl/{commandType}/{wgId}/{code}")
    public SysResult typeControl(@PathVariable Integer commandType,@PathVariable String wgId,@PathVariable Integer code){
        try{
        equipmentService.getTypeControl(commandType,wgId,code);
            return SysResult.ok();
        }catch (WisdomException we){
            return SysResult.build(we.getCode(),we.getMsg());
        }
    }
}
