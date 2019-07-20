package com.fengbiaoedu.njwl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fengbiaoedu.mq.ActiveMQService;
import com.fengbiaoedu.service.EquipmentService;
import com.fengbiaoedu.utils.RedisUtil;
import com.fengbiaoedu.utils.SpringUtil;
import com.fengbiaoedu.vo.ControlEuqData;

import cc.wulian.ihome.wan.entity.DeviceEPInfo;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import cc.wulian.ihome.wan.util.ConstUtil;


public class DeviceManager {
	private static DeviceManager manager;
	// 设备数据更新观察者
	private List<Observers> deviceDataObservers = new ArrayList<Observers>();
	// 设备上线观察者
	private List<Observers> deviceUpObservers = new ArrayList<Observers>();
	// 设备ID,DeviceInfo 在线设备
	private ConcurrentHashMap<String, DeviceInfo> mDeviceInfoMap = new ConcurrentHashMap<String, DeviceInfo>();
	//离线设备
	private ConcurrentHashMap<String, DeviceInfo> offlineDeviceInfoMap = new ConcurrentHashMap<String, DeviceInfo>();
	private RedisUtil redisUtil; //redis 存储库
	private EquipmentService equipmentService; //持久化到redis数据库
	private ActiveMQService activeMQService;

	private DeviceManager() {
	}

	public  synchronized static DeviceManager getInstance() {
		if (manager == null) {
			manager = new DeviceManager();
			//设置对应的redisUtil;
			manager.redisUtil =  SpringUtil.getBean(RedisUtil.class);
			manager.equipmentService = SpringUtil.getBean(EquipmentService.class);
			manager.activeMQService = SpringUtil.getBean(ActiveMQService.class);
		}
		return manager;
	}

	public ConcurrentHashMap<String, DeviceInfo> getDeviceInfoMap() {
		return mDeviceInfoMap;
	}

	/**
	 * 添加某设备回调接口
	 * @param devInfo
	 */
	public void addDevice(DeviceInfo devInfo,Set<DeviceEPInfo> devEPInfoSet) {
		/*if (offlineDeviceInfoMap.containsKey(devInfo.getDevID())) {
			offlineDeviceInfoMap.remove(devInfo.getDevID());
		}
		if (!mDeviceInfoMap.containsKey(devInfo.getDevID())) {
			mDeviceInfoMap.put(devInfo.getDevID(), devInfo);
			notifyAllObservers(deviceUpObservers,devInfo);
		}*/
		//持久化到数据库
		equipmentService.persistanceToDataBase(devInfo,devEPInfoSet);
		//持久化到redis
		/**在线*/
		devInfo.setIsOnline("yes");
		redisUtil.addAndUpdateEuq(devInfo);
	}



	/**
	 * 某设备下线回调接口
	 * @param devInfo
	 */
	public void addOfflineDevice(DeviceInfo devInfo) {
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
	public void updateDeviceData(String gwID,String devID,String devType, DeviceEPInfo ep) {
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
			notifyAllObservers(deviceDataObservers,d);
		}else{
			DeviceInfo deviceInfo = redisUtil.getEuqByGwIdAndDevId(gwID,devID);
			deviceInfo.setDevEPInfo(ep);
			redisUtil.addAndUpdateEuq(deviceInfo);
			//mDeviceInfoMap.get(devID).setDevEPInfo(ep);
			notifyAllObservers(deviceDataObservers,mDeviceInfoMap.get(devID));
			/**把deviceInfo放到activeMQ中，广播发送设备变化的信息***/
			activeMQService.sendTopic(deviceInfo);
		}
		/**调用判断是否是烟雾感应器*/
		isAlarm(gwID,devType,ep.getEpData());
	}
	//调用判断是否是烟雾感应器，是的话，判断是否是0101 是的话，报警
	public  void isAlarm(String gwID,String devType,String epData){
		if(ConstUtil.DEV_TYPE_FROM_GW_FIRE_SR.equals(devType)){
			if("0101".equals(epData)){ //判断出现火警
				//获取声光报警设备
				DeviceInfo deviceByEpType = this.getDeviceByEpType(gwID,ConstUtil.DEV_TYPE_FROM_GW_WARNING);
				if(deviceByEpType!=null){
					/**ControlEuqData*****/
					ControlEuqData controlEuqData = new ControlEuqData();
					controlEuqData.setDevId(deviceByEpType.getDevID());
					controlEuqData.setGwId(deviceByEpType.getGwID());
					/**控制设备*/
					equipmentService.controlEuq(controlEuqData);
				}
			}
		}
	}

	/**根据类型获取在线设备信息*/
	public DeviceInfo getDeviceByEpType(String gwId,String epType){
		/**高级for遍历getDevices()方法的结果集合   在线设备*/
		for (DeviceInfo deviceInfo:getDevices()) {
			if(gwId.equals(deviceInfo.getGwID()) && epType.equals(deviceInfo.getType())){
				return  deviceInfo;
			}
		}
		return null;
	}

	/**获取在线设备*/
	public Collection<DeviceInfo> getDevices() {
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
	public Collection<DeviceInfo> getOfflineDevices()
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

	public void attachDeviceUpObserver(Observers obs) {
		deviceUpObservers.add(obs);
	}

	public void attachDeviceDataObserver(Observers obs) {
		deviceDataObservers.add(obs);
	}

	/***********/
	private void notifyAllObservers(List<Observers> Observers,DeviceInfo deviceInfo) {
		for (Observers obs : Observers) {
			obs.update(deviceInfo);
		}
	}


	/**
	 * 获取对应网关的设备 包括在线设备和离线设备
	 * @param wgId 网关id
	 * @return 返回具体网关的所有设备
	 */
	public List<DeviceInfo> getAllDeviceInfoByWgId(String wgId){
		List<DeviceInfo>  wgDeviceInfos = new ArrayList<>();
		//只需要wgId对应的设备
		/**getDevices()在线设备*/
		for (DeviceInfo deviceInfo:getDevices()) {
			if(wgId.equals(deviceInfo.getGwID())){
				wgDeviceInfos.add(deviceInfo);
			}
		}
		/**getOfflineDevices()离线设备*/
		for (DeviceInfo deviceInfo:getOfflineDevices()) {
			if(wgId.equals(deviceInfo.getGwID())){
				wgDeviceInfos.add(deviceInfo);
			}
		}
		return wgDeviceInfos;
	}

	/**
	 * 根据网关id和设备id获取对应设备信息
	 */
	public DeviceInfo getDeviceInfoByGwIdAndDevId(String gwId,String devId){
		for (DeviceInfo deviceInfo:getDevices()) {
			if(gwId.equals(deviceInfo.getGwID()) && devId.equals(deviceInfo.getDevID())){
				return  deviceInfo;
			}
		}
		return  null;
	}
}
