package com.fengbiaoedu.owcontroller;

import cc.wulian.ihome.wan.entity.DeviceInfo;
import com.fengbiaoedu.convertor.DeviceInfoToEquipmentDTOCovertor;
import com.fengbiaoedu.dto.EquipmentDTO;
import com.fengbiaoedu.exception.WisdomException;
import com.fengbiaoedu.owconvertor.OWDeviceInfoToEquipmentDTOCovertor;
import com.fengbiaoedu.owdto.OWEquipmentDTO;
import com.fengbiaoedu.owservice.OWEquipmentService;
import com.fengbiaoedu.owvo.OWControlEuqData;
import com.fengbiaoedu.owvo.OWSysResult;
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
@RequestMapping("oweuqipment")
public class OWEquipmentController {
    @Autowired
    private OWEquipmentService owEquipmentService;

    /**
     * 获取指定网关的的指定设备下的设备信息
     * @param wgId 网关
     * @param pwd 密码
     * @param deviceId 设备id
     * @return 设备信息 设备数据 包括上线的设备和下线的设备
     */
    @GetMapping("owGetEuq/{wgId}/{pwd}/{deviceId}")
    public OWSysResult owGetEuq(@PathVariable("wgId") String wgId,
                                @PathVariable("pwd") String pwd,
                                @PathVariable("deviceId") String deviceId){
      List<DeviceInfo> EquipmentDTOS =  owEquipmentService.owGetEuq(wgId,pwd,deviceId);
      return OWSysResult.ok(EquipmentDTOS);
    }

    /**
     * 获取指定网关下所有设备信息，转换设备的数据
     * @param wgId
     * @param pwd
     * @return
     */
    @GetMapping("owGetEuqToCovertor/{wgId}/{pwd}")
    public OWSysResult owGetEuqToCovertor(@PathVariable("wgId") String wgId,
                            @PathVariable("pwd") String pwd     ){
        List<DeviceInfo> deviceInfos =  owEquipmentService.owGetEuq(wgId,pwd,"");
        //转换
        List<OWEquipmentDTO> equipmentDTOList = OWDeviceInfoToEquipmentDTOCovertor.convertor(deviceInfos);
        return OWSysResult.ok(equipmentDTOList);
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
    @PostMapping("owControlEuq")
    public OWSysResult owControlEuq(OWControlEuqData owControlEuqData){
            try{
                owEquipmentService.owControlEuq(owControlEuqData);
                return OWSysResult.ok();
            }catch (WisdomException we){
                return OWSysResult.build(we.getCode(),we.getMsg());
            }
    }

    /**
     * 获取所有可控设备
     * 1.设备名称   设备的数据信息
     */
    @GetMapping("owGetControlledEuq/{gwId}/{pwd}/{deviceId}")
    public OWSysResult owGetControlledEuq(@PathVariable("gwId")String gwId,
                                      @PathVariable("pwd")String pwd,
                                      @PathVariable("deviceId")String deviceId){
        try {
           List<OWEquipmentDTO> equipmentDTOList = owEquipmentService.owGetControlledEuq(gwId,pwd,deviceId);
           return OWSysResult.ok(equipmentDTOList);
        }catch (WisdomException we){
            return OWSysResult.build(we.getCode(),we.getMsg());
        }
    }
    //获取所有传感设备信息 标注：传感设备一般是不可控设备 会传递一些数据2
    @GetMapping("owGetSenseEq/{gwId}/{pwd}")
    public OWSysResult owGetSenseEq(@PathVariable("gwId")String gwId,@PathVariable("pwd")String pwd){
        try {
            List<OWEquipmentDTO> equipmentDTOS = owEquipmentService.owGetSenseEq(gwId,pwd);
            return OWSysResult.ok(equipmentDTOS);
        }catch (WisdomException we){
            return OWSysResult.build(we.getCode(),we.getMsg());
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
    @GetMapping("owGetEqueData/{gwId}/{pwd}/{devId}/{type}")
    public OWSysResult owGetEqueData(@PathVariable("gwId")String gwId,
                                     @PathVariable("pwd")String pwd,
                                     @PathVariable("devId") String devId,
                                     @PathVariable(required = false)Integer type){
        try {
            OWEquipmentDTO owEquipmentDTO = owEquipmentService.owGetEqueData(gwId,pwd,devId,type);
            return OWSysResult.ok(owEquipmentDTO);
        }catch (WisdomException we){
            return OWSysResult.build(we.getCode(),we.getMsg());
        }
    }
    //控制类型具体设备
    /**
     * 需要
     * typeId	设备类型
     * wgId  网管id
     * code 开关状态  0 开 1关
     */
    @PostMapping("owTypeControl/{commandType}/{wgId}/{code}")
    public OWSysResult owTypeControl(@PathVariable Integer commandType,@PathVariable String wgId,@PathVariable Integer code){
        try{
        owEquipmentService.owGetTypeControl(commandType,wgId,code);
            return OWSysResult.ok();
        }catch (WisdomException we){
            return OWSysResult.build(we.getCode(),we.getMsg());
        }
    }
}
