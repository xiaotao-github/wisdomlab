package com.fengbiaoedu.service.impl;

import cc.wulian.ihome.wan.NetSDK;
import cc.wulian.ihome.wan.entity.DeviceEPInfo;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import cc.wulian.ihome.wan.util.ConstUtil;
import com.fengbiaoedu.bean.Equipment;
import com.fengbiaoedu.convertor.DeviceInfoToEquipmentConvertor;
import com.fengbiaoedu.dto.EquipmentDTO;
import com.fengbiaoedu.enums.ErrorEnum;
import com.fengbiaoedu.enums.StealthEnum;
import com.fengbiaoedu.exception.WisdomException;
import com.fengbiaoedu.mapper.EquipmentMapper;
import com.fengbiaoedu.njwl.DeviceManager;
import com.fengbiaoedu.service.EquipmentService;
import com.fengbiaoedu.utils.ConnectUtil;
import com.fengbiaoedu.utils.DeviceTool;
import com.fengbiaoedu.utils.RedisUtil;
import com.fengbiaoedu.utils.StringNumberUtils;
import com.fengbiaoedu.vo.ControlEuqData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * Created by Administrator on 2018/5/15.
 */
@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 将设备持久化到数据库
     *
     1.判断设备中是否有子按键（分支）
     有--当成一个设备来添加
     无--添加当前设备
     *判断当前设备中是否有该设备
     * --有更新
     * --无添加
     *
     * @param deviceInfo 设备信息
     */
    public void persistanceToDataBase(DeviceInfo deviceInfo,Set<DeviceEPInfo> devEPInfoSet){

        for (DeviceEPInfo temp:devEPInfoSet) {
            Equipment equipment = new Equipment();
            equipment.setGwId(deviceInfo.getGwID());
            equipment.setDevId(deviceInfo.getDevID());
            equipment.setEp(temp.getEp());
            if(StringUtils.isEmpty(temp.getEpName())){
                if(StringUtils.isEmpty(deviceInfo.getName())){
                    equipment.setEpName(DeviceTool.getDevDefaultNameByType(deviceInfo.getType()));
                }else {
                    equipment.setEpName(deviceInfo.getName());
                }
            }else{
                equipment.setEpName(temp.getEpName());
            }
            equipment.setEpType(temp.getEpType());
            equipment.setUpdateTime(new Date());
            //按设备名称截取 插座顺序
           if(StringNumberUtils.isStartWithNumber(equipment.getEpName()))
               equipment.setSeatNum(StringNumberUtils.getStringStartNumber(equipment.getEpName()));
           //更新不了就插入该设备信息
            /***0.修改失败 >0.修改成功**/
            if(equipmentMapper.updateEquipment(equipment)<=0){
                equipment.setStealth(StealthEnum.EXIT.getCode());
                equipment.setCreateTime(equipment.getUpdateTime());
                equipmentMapper.insertSelective(equipment);
            }
        }
    }



    /**
     * 根据网关获取设备信息 包括上线设备和离线设备
     * @param wgId 网关id
     * @param wpd 网关密码
     * @param deviceId 设备id
     * @return List<DeviceInfo>
     */
    @Override
    public List<DeviceInfo> getEuq(String wgId,String wpd,String deviceId) {
        /**使用ConnectUtil连接网关*/
        ConnectUtil.isConnectedAndConnect(wgId,wpd,deviceId);
        List<DeviceInfo> result = new ArrayList<>();
        for (DeviceInfo deviceInfo :DeviceManager.getInstance().getAllDeviceInfoByWgId(wgId)) {
            deviceInfo.setIsOnline("yes");//在线设备
            if(StringUtils.isEmpty(deviceInfo.getName()))deviceInfo.setName(DeviceTool.getDevDefaultNameByType(deviceInfo.getType()));
            result.add(deviceInfo);
        }
        return result;
    }

    /**
     * 控制设备
     * @param controlEuqData 控制设备需要的数据信息
     * @throws WisdomException 不可控设备、参数错误抛出异常
     */
    @Override
    public void controlEuq(ControlEuqData controlEuqData) {
       /* if(DeviceTool.isNoQuickCtrlDevByType(controlEuqData.getEpType())){
            throw new WisdomException(302,controlEuqData.getEpType()+" 是不可控设备");
        }
        //校验数据
         if(controlEuqData==null ||
                controlEuqData.getDevId() ==null ||
                controlEuqData.getDevCtrlDataByType() ==null ||
                controlEuqData.getEpType()==null ||
                controlEuqData.getGwId()==null||
                controlEuqData.getEq()==null){
            throw  new WisdomException(ErrorEnum.PARAMERROR);
        }*/
        //远程控制
//        NetSDK.sendControlDevMsg(controlEuqData.getGwId(),
//                controlEuqData.getDevId(),
//                controlEuqData.getEp(),
//                controlEuqData.getEpType(),
//                "123456&30");
        DeviceInfo deviceInfo = DeviceManager.getInstance().getDeviceInfoByGwIdAndDevId(controlEuqData.getGwId(), controlEuqData.getDevId());
        // 我注释的 ConnectUtil.isConnectedAndConnect(deviceInfo.getGwID(),"","");
        if(deviceInfo!=null){
            deviceInfo.setEpData(controlEuqData.getEpData()+"");
            DeviceTool.controlDevice(deviceInfo,controlEuqData.getEp());
        }
    }

    /**
     * 获取可控设备的数据信息  包括离线设备和在线设备
     * @param gwId 网关id
     * @param pwd 网关密码
     * @param deviceId 设备id
     * @return 返回可控设备的list集合
     * @throws WisdomException 无可控设备数据 抛出异常
     */
    @Override
    public List<EquipmentDTO> getControlledEuq(String gwId, String pwd, String deviceId) {
        ConnectUtil.isConnectedAndConnect(gwId,pwd,deviceId);
        List<DeviceInfo> wgDeviceInfos = DeviceManager.getInstance().getAllDeviceInfoByWgId(gwId);
        if(!CollectionUtils.isEmpty(wgDeviceInfos)){
            List<DeviceInfo> controlledDevices = new ArrayList<>();
            for (DeviceInfo deviceInfo:wgDeviceInfos) {
                if(!DeviceTool.isNoQuickCtrlDevByType(deviceInfo.getType())){
                    controlledDevices.add(deviceInfo);
                }
            }
            if(!CollectionUtils.isEmpty(controlledDevices)){
                return DeviceInfoToEquipmentConvertor.convertor(controlledDevices);
            }
        }
        throw new WisdomException(ErrorEnum.NODATA);
    }

    /**
     *
     * 获取传感设备的数据
     * @param gwId 网关id
     * @param pwd 网关密码是
     * @param devId  要获取的具体设备id
     * @param type 类型 0.温湿度数据   1.光照数据  2.空气质量
     * @return 返回具体的设备信息
     * @throws  WisdomException 如果没有数据 、 参数错误 、抛出异常
     */
    @Override
    public EquipmentDTO getEqueData(String gwId, String pwd, String devId, Integer type) {
        //刷新一遍获取设备数据
        NetSDK.sendRefreshDevListMsg(gwId,devId);
       Collection<DeviceInfo> deviceInfos =  DeviceManager.getInstance().getDevices();
        if(type!=null){
                //0.是需要获取温湿度的数据 温湿度的类型是 17
                if(type == 0){
                    for (DeviceInfo deviceInfo:deviceInfos) {
                        //温湿度的类型是17
                        if(ConstUtil.DEV_TYPE_FROM_GW_TEMHUM.equals(deviceInfo.getType())){
                            return DeviceInfoToEquipmentConvertor.convertor(deviceInfo);
                        }
                    }
                }
            //1.光照强度 光照强度感应器类型是19
            if(type == 1){
                for (DeviceInfo deviceInfo:deviceInfos) {
                    //温湿度的类型是19
                    if(ConstUtil.DEV_TYPE_FROM_GW_LIGHT_S.equals(deviceInfo.getType())){
                        return DeviceInfoToEquipmentConvertor.convertor(deviceInfo);
                    }
                }
            }
                //2.空气质量
                if(type == 2){
                    for (DeviceInfo deviceInfo:deviceInfos) {
                        //空气质量类型是 42
                        if(ConstUtil.DEV_TYPE_FROM_GW_CTHV.equals(deviceInfo.getType())){
                            return DeviceInfoToEquipmentConvertor.convertor(deviceInfo);
                        }
                    }
                }
        }
        //检测数据是否正确
        if(gwId==null||pwd==null||devId==null){
            throw new WisdomException(ErrorEnum.PARAMERROR);
        }
        for (DeviceInfo deviceInfo:deviceInfos) {
            if(devId.equals(deviceInfo.getDevID())){
                return DeviceInfoToEquipmentConvertor.convertor(deviceInfo);
            }
        }
        throw new WisdomException(ErrorEnum.NODATA);
    }
    /**
     *获取所有传感设备集合
     * @param gwId 网关id
     * @param pwd 网关密码
     * @return 返回所有传感设备的集合
     */
    @Override
    public List<EquipmentDTO> getSenseEq(String gwId, String pwd) {
        List<DeviceInfo> deviceInfos = DeviceManager.getInstance().getAllDeviceInfoByWgId(gwId);
        List<DeviceInfo> temps = new ArrayList<>();
        for (DeviceInfo temp:deviceInfos) {
            if(DeviceTool.isSensorDevByType(temp.getType())){
                temps.add(temp);
            }
        }
        if(CollectionUtils.isEmpty(temps)){
            throw  new WisdomException(ErrorEnum.NODATA);
        }else{
            return DeviceInfoToEquipmentConvertor.convertor(temps);
        }
    }
    /*
     * 根据类型控制同等类型设备
     * */
     @Override
     public void getTypeControl(Integer commandType,String wgId,Integer code) {

             //创建一个设备在线集合用于控制设备
             List<ControlEuqData> colsit = new ArrayList<>();
             //获取该类型的所有设备id
             List<Equipment> list =  equipmentMapper.getTypeDv(commandType,wgId);
             //获取当前上线的设备，进行执行有上线的设备！
             for (int j=0 ;j<list.size();j++){
                 //获取到上线的设备
                 DeviceInfo dv= redisUtil.getEuqByGwIdAndDevId(wgId,list.get(j).getDevId());
                 if (dv!=null){
                     ControlEuqData c =new ControlEuqData();
                     c.setDevId(dv.getDevID());
                     c.setGwId(dv.getGwID());
                     colsit.add(c);
                 }
             }

             //在线集合为空，返回异常信息
             if (colsit.size()==0){
                 throw  new WisdomException(ErrorEnum.NODATA);
             }else{
                 //根据id 控制设备
                 for (int i = 0; i < colsit.size(); i++) {
                     ControlEuqData co = new ControlEuqData();
                     co.setDevId(colsit.get(i).getDevId());
                     co.setGwId(colsit.get(i).getGwId());
                     this.customControlEuq(co,code);
                 }
             }

     }

     /*
     情景模式自定义控制设备
     *code 0 开 1 关闭
     * */
     @Override
     public void customControlEuq(ControlEuqData controlEuqData,Integer code) {

         DeviceInfo deviceInfo = DeviceManager.getInstance().getDeviceInfoByGwIdAndDevId(controlEuqData.getGwId(), controlEuqData.getDevId());
         ConnectUtil.isConnectedAndConnect(deviceInfo.getGwID(),"","");
         if(deviceInfo!=null){
             deviceInfo.setEpData(controlEuqData.getEpData()+"");
             DeviceTool.customControlDevice(deviceInfo,controlEuqData.getEp(),code);
         }
     }
}
