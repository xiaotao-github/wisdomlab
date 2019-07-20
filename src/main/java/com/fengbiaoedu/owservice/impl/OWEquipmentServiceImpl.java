package com.fengbiaoedu.owservice.impl;

import cc.wulian.ihome.wan.NetSDK;
import cc.wulian.ihome.wan.entity.DeviceEPInfo;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import cc.wulian.ihome.wan.util.ConstUtil;
import com.alibaba.fastjson.JSONObject;
import com.fengbiaoedu.enums.ErrorEnum;
import com.fengbiaoedu.enums.StealthEnum;
import com.fengbiaoedu.exception.WisdomException;
import com.fengbiaoedu.ow.OWDeviceManager;
import com.fengbiaoedu.owbean.OWEquipment;
import com.fengbiaoedu.owbean.OWQuestBean;
import com.fengbiaoedu.owconvertor.OWDeviceInfoToEquipmentConvertor;
import com.fengbiaoedu.owdto.OWEquipmentDTO;
import com.fengbiaoedu.owmapper.OWEquipmentMapper;
import com.fengbiaoedu.owservice.OWEquipmentService;
import com.fengbiaoedu.owutils.JSONUtils;
import com.fengbiaoedu.owutils.OWConnectUtil;
import com.fengbiaoedu.owutils.OWDeviceTool;
import com.fengbiaoedu.owvo.OWControlEuqData;
import com.fengbiaoedu.utils.JsonUtils;
import com.fengbiaoedu.utils.RedisUtil;
import com.fengbiaoedu.utils.StringNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * Created by Administrator on 2018/5/15.
 */
@Service
public class OWEquipmentServiceImpl implements OWEquipmentService {
    @Autowired
    private OWEquipmentMapper owEquipmentMapper;
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
    public void owPersistanceToDataBase(DeviceInfo deviceInfo,Set<DeviceEPInfo> devEPInfoSet){
        for (DeviceEPInfo temp:devEPInfoSet) {
            OWEquipment owEquipment = new OWEquipment();
            owEquipment.setGwId(deviceInfo.getGwID());
            owEquipment.setDevId(deviceInfo.getDevID());
            owEquipment.setEp(temp.getEp());
            if(StringUtils.isEmpty(temp.getEpName())){
                if(StringUtils.isEmpty(deviceInfo.getName())){
                    owEquipment.setEpName(OWDeviceTool.owGetDevDefaultNameByType(deviceInfo.getType()));
                }else {
                    owEquipment.setEpName(deviceInfo.getName());
                }
            }else{
                owEquipment.setEpName(temp.getEpName());
            }
            owEquipment.setEpType(temp.getEpType());
            owEquipment.setUpdateTime(new Date());
            //按设备名称截取 插座顺序
           if(StringNumberUtils.isStartWithNumber(owEquipment.getEpName()))
               owEquipment.setSeatNum(StringNumberUtils.getStringStartNumber(owEquipment.getEpName()));
           //更新不了就插入该设备信息
            /***0.修改失败 >0.修改成功**/
            if(owEquipmentMapper.updateEquipment(owEquipment)<=0){
                owEquipment.setStealth(StealthEnum.EXIT.getCode());
                owEquipment.setCreateTime(owEquipment.getUpdateTime());
                owEquipmentMapper.insertSelective(owEquipment);
            }
        }
    }

    /***测试JSONUtils***/
    public static void main(String[] args) {
        OWQuestBean owQuestBean = new OWQuestBean();
        Map map = new HashMap();
        map.put("username","owon");
        map.put("password","123456");
        owQuestBean.setArgument(map);
        owQuestBean.setType("login");
        owQuestBean.setSequence(1017);
        System.out.println(JSONUtils.beanToJson(owQuestBean));
    }


    /**
     * 根据网关获取设备信息 包括上线设备和离线设备
     * @param wgId 网关id
     * @param wpd 网关密码
     * @param deviceId 设备id
     * @return List<DeviceInfo>
     */
    @Override
    public List<DeviceInfo> owGetEuq(String wgId,String wpd,String deviceId) {
        OWConnectUtil.isConnectedAndConnect(wgId,wpd,deviceId);
        List<DeviceInfo> result = new ArrayList<>();
        for (DeviceInfo deviceInfo : OWDeviceManager.getInstance().owGetAllDeviceInfoByWgId(wgId)) {
            deviceInfo.setIsOnline("yes");//在线设备
            if(StringUtils.isEmpty(deviceInfo.getName()))deviceInfo.setName(OWDeviceTool.owGetDevDefaultNameByType(deviceInfo.getType()));
            result.add(deviceInfo);
        }
        return result;
    }

    /**
     * 控制设备
     * @param owControlEuqData 控制设备需要的数据信息
     * @throws WisdomException 不可控设备、参数错误抛出异常
     */
    @Override
    public void owControlEuq(OWControlEuqData owControlEuqData) {
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
        DeviceInfo deviceInfo = OWDeviceManager.getInstance().owGetDeviceInfoByGwIdAndDevId(owControlEuqData.getGwId(), owControlEuqData.getDevId());
        // 我注释的 OWConnectUtil.isConnectedAndConnect(deviceInfo.getGwID(),"","");
        if(deviceInfo!=null){
            deviceInfo.setEpData(owControlEuqData.getEpData()+"");
            OWDeviceTool.owControlDevice(deviceInfo,owControlEuqData.getEp());
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
    public List<OWEquipmentDTO> owGetControlledEuq(String gwId, String pwd, String deviceId) {
        OWConnectUtil.isConnectedAndConnect(gwId,pwd,deviceId);
        List<DeviceInfo> wgDeviceInfos = OWDeviceManager.getInstance().owGetAllDeviceInfoByWgId(gwId);
        if(!CollectionUtils.isEmpty(wgDeviceInfos)){
            List<DeviceInfo> controlledDevices = new ArrayList<>();
            for (DeviceInfo deviceInfo:wgDeviceInfos) {
                if(!OWDeviceTool.owIsNoQuickCtrlDevByType(deviceInfo.getType())){
                    controlledDevices.add(deviceInfo);
                }
            }
            if(!CollectionUtils.isEmpty(controlledDevices)){
                return OWDeviceInfoToEquipmentConvertor.convertor(controlledDevices);
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
    public OWEquipmentDTO owGetEqueData(String gwId, String pwd, String devId, Integer type) {
        //刷新一遍获取设备数据
        NetSDK.sendRefreshDevListMsg(gwId,devId);
       Collection<DeviceInfo> deviceInfos =  OWDeviceManager.getInstance().owGetDevices();
        if(type!=null){
                //0.是需要获取温湿度的数据 温湿度的类型是 17
                if(type == 0){
                    for (DeviceInfo deviceInfo:deviceInfos) {
                        //温湿度的类型是17
                        if(ConstUtil.DEV_TYPE_FROM_GW_TEMHUM.equals(deviceInfo.getType())){
                            return OWDeviceInfoToEquipmentConvertor.convertor(deviceInfo);
                        }
                    }
                }
            //1.光照强度 光照强度感应器类型是19
            if(type == 1){
                for (DeviceInfo deviceInfo:deviceInfos) {
                    //温湿度的类型是19
                    if(ConstUtil.DEV_TYPE_FROM_GW_LIGHT_S.equals(deviceInfo.getType())){
                        return OWDeviceInfoToEquipmentConvertor.convertor(deviceInfo);
                    }
                }
            }
                //2.空气质量
                if(type == 2){
                    for (DeviceInfo deviceInfo:deviceInfos) {
                        //空气质量类型是 42
                        if(ConstUtil.DEV_TYPE_FROM_GW_CTHV.equals(deviceInfo.getType())){
                            return OWDeviceInfoToEquipmentConvertor.convertor(deviceInfo);
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
                return OWDeviceInfoToEquipmentConvertor.convertor(deviceInfo);
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
    public List<OWEquipmentDTO> owGetSenseEq(String gwId, String pwd) {
        /****************OWDeviceManager********************/
        List<DeviceInfo> deviceInfos = OWDeviceManager.getInstance().owGetAllDeviceInfoByWgId(gwId);
        List<DeviceInfo> temps = new ArrayList<>();
        for (DeviceInfo temp:deviceInfos) {
            if(OWDeviceTool.owIsSensorDevByType(temp.getType())){
                temps.add(temp);
            }
        }
        if(CollectionUtils.isEmpty(temps)){
            throw  new WisdomException(ErrorEnum.NODATA);
        }else{
            return OWDeviceInfoToEquipmentConvertor.convertor(temps);
        }
    }
    /*
     * 根据类型控制同等类型设备
     * */
     @Override
     public void owGetTypeControl(Integer commandType,String wgId,Integer code) {

             //创建一个设备在线集合用于控制设备
             List<OWControlEuqData> colsit = new ArrayList<>();
             //获取该类型的所有设备id
             List<OWEquipment> list =  owEquipmentMapper.getTypeDv(commandType,wgId);
             //获取当前上线的设备，进行执行有上线的设备！
             for (int j=0 ;j<list.size();j++){
                 //获取到上线的设备
                 DeviceInfo dv= redisUtil.getEuqByGwIdAndDevId(wgId,list.get(j).getDevId());
                 if (dv!=null){
                     OWControlEuqData c =new OWControlEuqData();
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
                     OWControlEuqData co = new OWControlEuqData();
                     co.setDevId(colsit.get(i).getDevId());
                     co.setGwId(colsit.get(i).getGwId());
                     this.owCustomControlEuq(co,code);
                 }
             }

     }

     /*
     情景模式自定义控制设备
     *code 0 开 1 关闭
     * */
     @Override
     public void owCustomControlEuq(OWControlEuqData owControlEuqData,Integer code) {

         DeviceInfo deviceInfo = OWDeviceManager.getInstance().owGetDeviceInfoByGwIdAndDevId(owControlEuqData.getGwId(), owControlEuqData.getDevId());
         OWConnectUtil.isConnectedAndConnect(deviceInfo.getGwID(),"","");
         if(deviceInfo!=null){
             deviceInfo.setEpData(owControlEuqData.getEpData()+"");
             OWDeviceTool.owCustomControlDevice(deviceInfo,owControlEuqData.getEp(),code);
         }
     }
}
