package com.fengbiaoedu.ow;

import cc.wulian.ihome.wan.entity.DeviceEPInfo;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import cc.wulian.ihome.wan.util.ConstUtil;
import com.fengbiaoedu.mq.ActiveMQService;
import com.fengbiaoedu.njwl.Observers;
import com.fengbiaoedu.owmq.OWActiveMQService;
import com.fengbiaoedu.owservice.OWEquipmentService;
import com.fengbiaoedu.owvo.OWControlEuqData;
import com.fengbiaoedu.service.EquipmentService;
import com.fengbiaoedu.utils.RedisUtil;
import com.fengbiaoedu.utils.SpringUtil;
import com.fengbiaoedu.vo.ControlEuqData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OWDeviceManager {
    private static OWDeviceManager owDeviceManager;
    // 设备数据更新观察者
    private List<OWObservers> deviceDataObservers = new ArrayList<OWObservers>();
    // 设备上线观察者
    private List<OWObservers> deviceUpObservers = new ArrayList<OWObservers>();
    // 设备ID,DeviceInfo 在线设备
    private ConcurrentHashMap<String, DeviceInfo> mDeviceInfoMap = new ConcurrentHashMap<String, DeviceInfo>();
    //离线设备
    private ConcurrentHashMap<String, DeviceInfo> offlineDeviceInfoMap = new ConcurrentHashMap<String, DeviceInfo>();
    private RedisUtil redisUtil; //redis 存储库
    private OWEquipmentService owEquipmentService; //持久化到redis数据库
    private OWActiveMQService owActiveMQService;

    private OWDeviceManager() {
    }

    public  synchronized static OWDeviceManager getInstance() {
        if (owDeviceManager == null) {
            owDeviceManager = new OWDeviceManager();
            //设置对应的redisUtil;
            owDeviceManager.redisUtil =  SpringUtil.getBean(RedisUtil.class);
            owDeviceManager.owEquipmentService = SpringUtil.getBean(OWEquipmentService.class);
            owDeviceManager.owActiveMQService = SpringUtil.getBean(OWActiveMQService.class);
        }
        return owDeviceManager;
    }

    public ConcurrentHashMap<String, DeviceInfo> getDeviceInfoMap() {
        return mDeviceInfoMap;
    }

    /**
     * 添加某设备回调接口
     * @param devInfo
     */
    public void owAddDevice(DeviceInfo devInfo,Set<DeviceEPInfo> devEPInfoSet) {
		/*if (offlineDeviceInfoMap.containsKey(devInfo.getDevID())) {
			offlineDeviceInfoMap.remove(devInfo.getDevID());
		}
		if (!mDeviceInfoMap.containsKey(devInfo.getDevID())) {
			mDeviceInfoMap.put(devInfo.getDevID(), devInfo);
			notifyAllObservers(deviceUpObservers,devInfo);
		}*/
        //持久化到数据库
        owEquipmentService.owPersistanceToDataBase(devInfo,devEPInfoSet);
        //持久化到redis
        /**在线*/
        devInfo.setIsOnline("yes");
        redisUtil.addAndUpdateEuq(devInfo);
    }



    /**
     * 某设备下线回调接口
     * @param devInfo
     */
    public void owAddOfflineDevice(DeviceInfo devInfo) {
        /**下线*/
        devInfo.setIsOnline("no");
        redisUtil.addAndUpdateEuq(devInfo);
		/*if (mDeviceInfoMap.containsKey(devInfo.getDevID())) {
			mDeviceInfoMap.remove(devInfo.getDevID());
		}
		if (!offlineDeviceInfoMap.containsKey(devInfo.getDevID())) {
			offlineDeviceInfoMap.put(devInfo.getDevID(), devInfo);
			notifyAllObservers(deviceUpObservers,devInfo);
		}*/
    }

    /***
     * 更新设备数据
     * @param gwID
     * @param devID
     * @param devType
     * @param ep
     */
    public void owUpdateDeviceData(String gwID,String devID,String devType, DeviceEPInfo ep) {
        if(redisUtil.getEuqByGwIdAndDevId(gwID,devID)==null){
            DeviceInfo d = new DeviceInfo();
            d.setGwID(gwID);
            d.setDevID(devID);
            d.setDevEPInfo(ep);
            d.setType(devType);
            /*****添加两次？*****/
            redisUtil.addAndUpdateEuq(d);
            //mDeviceInfoMap.put(devID,d);
            redisUtil.addAndUpdateEuq(d);
            /***********/
            owNotifyAllObservers(deviceDataObservers,d);
        }else{
            DeviceInfo deviceInfo = redisUtil.getEuqByGwIdAndDevId(gwID,devID);
            deviceInfo.setDevEPInfo(ep);
            redisUtil.addAndUpdateEuq(deviceInfo);
            //mDeviceInfoMap.get(devID).setDevEPInfo(ep);
            owNotifyAllObservers(deviceDataObservers,mDeviceInfoMap.get(devID));
            /**把deviceInfo放到activeMQ中，广播发送设备变化的信息***/
            owActiveMQService.owSendTopic(deviceInfo);
        }
        /**调用判断是否是烟雾感应器*/
        owIsAlarm(gwID,devType,ep.getEpData());
    }
    //调用判断是否是烟雾感应器，是的话，判断是否是0101 是的话，报警
    public  void owIsAlarm(String gwID,String devType,String epData){
        if(ConstUtil.DEV_TYPE_FROM_GW_FIRE_SR.equals(devType)){
            if("0101".equals(epData)){ //判断出现火警
                //获取声光报警设备
                DeviceInfo deviceByEpType = this.owGetDeviceByEpType(gwID,ConstUtil.DEV_TYPE_FROM_GW_WARNING);
                if(deviceByEpType!=null){
                    /**ControlEuqData*****/
                    OWControlEuqData owControlEuqData = new OWControlEuqData();
                    owControlEuqData.setDevId(deviceByEpType.getDevID());
                    owControlEuqData.setGwId(deviceByEpType.getGwID());
                    /**控制设备*/
                    owEquipmentService.owControlEuq(owControlEuqData);
                }
            }
        }
    }

    /**根据类型获取在线设备信息*/
    public DeviceInfo owGetDeviceByEpType(String gwId,String epType){
        /**高级for遍历getDevices()方法的结果集合   在线设备*/
        for (DeviceInfo deviceInfo:owGetDevices()) {
            if(gwId.equals(deviceInfo.getGwID()) && epType.equals(deviceInfo.getType())){
                return  deviceInfo;
            }
        }
        return null;
    }

    /**获取在线设备*/
    public Collection<DeviceInfo> owGetDevices() {
        Collection<DeviceInfo> deviceInfoList = redisUtil.getDeviceInfos();
        Collection<DeviceInfo> deviceInfos = new ArrayList<>();
        for (DeviceInfo deviceInfo:deviceInfoList) {
            if("yes".equals(deviceInfo.getIsOnline())){
                deviceInfos.add(deviceInfo);
            }
        }
        return deviceInfos;
        //return mDeviceInfoMap.values();
    }

    /**获取离线设备*/
    public Collection<DeviceInfo> owGetOfflineDevices()
    {
        Collection<DeviceInfo> deviceInfoList = redisUtil.getDeviceInfos();
        Collection<DeviceInfo> deviceInfos = new ArrayList<>();
        for (DeviceInfo deviceInfo:deviceInfoList) {
            if("no".equals(deviceInfo.getIsOnline())){
                deviceInfos.add(deviceInfo);
            }
        }
        return deviceInfos;
        //return offlineDeviceInfoMap.values();
    }

    public void owAttachDeviceUpObserver(OWObservers obs) {
        deviceUpObservers.add(obs);
    }

    public void owAttachDeviceDataObserver(OWObservers obs) {
        deviceDataObservers.add(obs);
    }

    /***********/
    private void owNotifyAllObservers(List<OWObservers> owObservers, DeviceInfo deviceInfo) {
        for (OWObservers obs : owObservers) {
            obs.update(deviceInfo);
        }
    }


    /**
     * 获取对应网关的设备 包括在线设备和离线设备
     * @param wgId 网关id
     * @return 返回具体网关的所有设备
     */
    public List<DeviceInfo> owGetAllDeviceInfoByWgId(String wgId){
        List<DeviceInfo>  wgDeviceInfos = new ArrayList<>();
        //只需要wgId对应的设备
        /**getDevices()在线设备*/
        for (DeviceInfo deviceInfo:owGetDevices()) {
            if(wgId.equals(deviceInfo.getGwID())){
                wgDeviceInfos.add(deviceInfo);
            }
        }
        /**getOfflineDevices()离线设备*/
        for (DeviceInfo deviceInfo:owGetOfflineDevices()) {
            if(wgId.equals(deviceInfo.getGwID())){
                wgDeviceInfos.add(deviceInfo);
            }
        }
        return wgDeviceInfos;
    }

    /**
     * 根据网关id和设备id获取对应设备信息
     */
    public DeviceInfo owGetDeviceInfoByGwIdAndDevId(String gwId,String devId){
        for (DeviceInfo deviceInfo:owGetDevices()) {
            if(gwId.equals(deviceInfo.getGwID()) && devId.equals(deviceInfo.getDevID())){
                return  deviceInfo;
            }
        }
        return  null;
    }
}
