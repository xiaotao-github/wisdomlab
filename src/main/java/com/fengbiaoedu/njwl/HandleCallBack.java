package com.fengbiaoedu.njwl;


import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cc.wulian.ihome.wan.MessageCallback;
import cc.wulian.ihome.wan.entity.AutoProgramTaskInfo;
import cc.wulian.ihome.wan.entity.DeviceEPInfo;
import cc.wulian.ihome.wan.entity.DeviceIRInfo;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import cc.wulian.ihome.wan.entity.GatewayInfo;
import cc.wulian.ihome.wan.entity.MonitorInfo;
import cc.wulian.ihome.wan.entity.RoomInfo;
import cc.wulian.ihome.wan.entity.RulesGroupInfo;
import cc.wulian.ihome.wan.entity.SceneInfo;
import cc.wulian.ihome.wan.entity.TaskInfo;
import cc.wulian.ihome.wan.util.ResultUtil;

/**
 * 物联NetSDK与服务器交互接口 1.实现MessageCallback
 */
public class HandleCallBack implements MessageCallback {
	
	private static HandleCallBack handleCallBack = new HandleCallBack();
	
	
	private HandleCallBack(){
		
	}
	public static HandleCallBack instance(){
		return handleCallBack;
	}
	
	
	

	/**
	 * 网关信息
	 */
	public static GatewayInfo currentGatewayInfo;
	public boolean isConnectSev = false;

	/**
	 * 网关连接是否成功标志
	 */
	public static boolean isConnectGw = false;


	/**
	 * 连接网关回调接口，如果结果码为14时，需要获取网关的在端并重新连接
	 * 
	 * @param result
	 *            连接结果码 1.结果为0：成功 ；结果为-1：失败 ；结果为11：网关不在线 ；结果为12: 网关ID错误
	 *            ；结果为13：网关密码错误 ；结果为14：网关在其他服务器
	 * @param gwID
	 *            网关ID
	 * @param gwInfo
	 *            网关信息包装类
	 */
	//
	@Override
	public void ConnectGateway(int result, String gwID, GatewayInfo gwInfo) {
		if (ResultUtil.RESULT_SUCCESS == result) {
			System.out.println("ConnectGateway连接成功，result：" + result + "，gwID：" + gwID
					+ "，GatewayInfo：" + gwInfo.toString());
			currentGatewayInfo = gwInfo;
			isConnectGw = true;
		}else{
			isConnectGw = false;
		}
	}

/*	private void checkGatewayResult(int result) {
		String msg;
		switch (result) {
		case ResultUtil.RESULT_CONNECTING:
			msg = "连接中connecting...";
			break;
		case ResultUtil.EXC_GW_OFFLINE:
			msg = "网关下线gateway offline";
			break;
		case ResultUtil.EXC_GW_USER_WRONG:
			msg = "网关名错误wrong gateway id";
			break;
		case ResultUtil.EXC_GW_PASSWORD_WRONG:
			msg = "密码错误wrong gateway password";
			break;
		case ResultUtil.EXC_GW_REMOTE_SERIP:
			msg = "gateway in other server";
			break;
		case ResultUtil.EXC_GW_OVER_CONNECTION:
			msg = "server connection has full";
			break;
		case ResultUtil.RESULT_EXCEPTION:
			msg = "连接异常connect exception";
			break;
		case ResultUtil.RESULT_SUCCESS:
			msg = "连接成功success";
			break;
		case ResultUtil.RESULT_FAILED:
			msg = "连接失败failed";
			break;
		default:
			msg = "连接未知异常";
			break;
		}
		System.out.println(msg);
	}*/

	/**
	 * 获取网关数据回调接口
	 * 
	 * @param result
	 * @param gwID
	 */
	@Override
	public void GatewayData(int result, String gwID) {
		System.out.println("网关数据GatewayData result：" + result + "，gwID：" + gwID);
	}

	/**
	 * 设备上线回调接口
	 * 
	 * @param devInfo
	 *            设备信息集合，包含设备的基本信息，不包含端口信息
	 * @param devEPInfoSet
	 *            设备端口信息集合，包含了所有端口下的设备信息（数据量大于等于1）
	 * @param isFirst
	 */
	@Override
	public void DeviceUp(DeviceInfo devInfo, Set<DeviceEPInfo> devEPInfoSet,
			boolean isFirst) {
		System.out.println("-->设备上线DeviceUp， devID:" + devInfo.getDevID());
		devInfo.setDevEPInfo((DeviceEPInfo) devEPInfoSet.toArray()[0]);
		DeviceManager.getInstance().addDevice(devInfo,devEPInfoSet);
	}

	/**
	 * 离线设备回调接口
	 */
	@Override
	public void offlineDevicesBack(DeviceInfo devcieInfo,
			Set<DeviceEPInfo> deviceEpInfoSet) {
		System.out.println("offlineDevicesBack");
		DeviceManager.getInstance().addOfflineDevice(devcieInfo);

	}

	/**
	 * 设备下线回调接口
	 * 
	 *            网关ID
	 *            设备ID
	 */

	@Override
	public void DeviceDown(String arg0, String arg1, String arg2) {
		System.out.println("offlineDevicesBack");
		DeviceInfo d = new DeviceInfo();
		d.setGwID(arg0);
		d.setDevID(arg1);
		DeviceManager.getInstance().addOfflineDevice(d);
	}

	/**
	 * 获取设备数据回调接口
	 * 
	 * @param gwID
	 *            网关ID
	 * @param devID
	 *            设备ID
	 * @param devType
	 * @param devEPInfo
	 *            设备端口信息
	 */
	@Override
	public void DeviceData(String gwID, String devID, String devType,
			DeviceEPInfo devEPInfo) {
		DeviceManager.getInstance().updateDeviceData(gwID,devID,devType,devEPInfo);

	}

	@Override
	public void cloudConfigMsg(String var1, String var2, String var3,
			JSONObject var4) {
		System.out.println("cloudConfigMsg");
	}


	/**
	 * SDK抛出的异常全部通过此接口传出
	 * 
	 * @param e
	 */
	@Override
	public void HandleException(Exception e) {
		System.out.println("Exception" + e.getCause() + e.getMessage());
	}

	@Override
	public void PushUserChatAll(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, String arg6) {
		// TODO Auto-generated method stub
		System.err.println("PushUserChatAll");
	}

	@Override
	public void PushUserChatMsg(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, String arg6) {
		// TODO Auto-generated method stub
		System.err.println("PushUserChatMsg");
	}

	@Override
	public void PushUserChatSome(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, String arg6, String arg7) {
		// TODO Auto-generated method stub
		System.err.println("PushUserChatSome");
	}

	@Override
	public void GetDevAlarmNum(String arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub
		System.err.println("GetDevAlarmNum");
	}

	@Override
	public void GetDevRecordInfo(String var1, String var2, String var3,
			JSONArray var4) {
		System.out.println("GetDevRecordInfo");
	}

	@Override
	public void DeviceHardData(String arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub
		System.err.println("DeviceHardData" + arg0 + ";" + arg1 + ";" + arg2 + ";"
				+ arg3);
	}

	@Override
	public void PermitDevJoin(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		System.err.println("PermitDevJoin");
	}

	@Override
	public void GetRouterConfigMsg(String var1, String var2, String var3,
			JSONObject var4) {
		System.out.println("GetRouterConfigMsg");
	}

	@Override
	public void getDreamFlowerConfigMsg(String var1, String var2, String var3,
			JSONObject var4) {
		System.out.println("getDreamFlowerConfigMsg");
	}

	@Override
	public void getTimezonConfigMsg(String var1, String var2, JSONObject var3) {
		System.out.println("getTimezonConfigMsg");
	}

	@Override
	public void QueryDevRelaInfo(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		System.err.println("QueryDevRelaInfo");
	}

	@Override
	public void SetTimerSceneInfo(String var1, String var2, String var3,
			String var4, String var5, JSONArray var6) {
		System.out.println("SetTimerSceneInfo");
	}

	@Override
	public void GetTimerSceneInfo(String var1, JSONArray var2) {
		System.out.println("GetTimerSceneInfo");
	}

	@Override
	public void ReportTimerSceneInfo(String var1, JSONArray var2) {
		System.out.println("ReportTimerSceneInfo");
	}

	@Override
	public void reqeustOrSetTwoStateConfigration(String var1, String var2,
			String var3, String var4, JSONArray var5) {
		System.out.println("reqeustOrSetTwoStateConfigration");
	}

	@Override
	public void GetMonitorInfo(String arg0, Set<MonitorInfo> arg1) {
		// TODO Auto-generated method stub
		System.err.println("GetMonitorInfo");
	}

	@Override
	public void GetRoomInfo(String arg0, Set<RoomInfo> arg1) {
		// TODO Auto-generated method stub
		System.out.println("GetRoomInfo");
	}

	@Override
	public void GetSceneInfo(String arg0, Set<SceneInfo> arg1) {
		// TODO Auto-generated method stub
		System.err.println("GetSceneInfo");

	}

	@Override
	public void readOfflineDevices(String gwID, String status) {
		// TODO Auto-generated method stub
		System.out.println("readOfflineDevices");
	}

	@Override
	public void GetMigrationTaskMsg(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		System.err.println("GetMigrationTaskMsg");
	}

	@Override
	public void SetCombindDevInfo(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, String arg6) {

		System.out.println("SetCombindDevInfo");
	}

	@Override
	public void GetCombindDevInfo(String var1, JSONArray var2) {
		System.out.println("GetCombindDevInfo");
	}

	@Override
	public void SetBindSceneInfo(String var1, String var2, String var3,
			JSONArray var4) {
		System.out.println("SetBindSceneInfo");
	}

	@Override
	public void sendControlGroupDevices(String arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub
		System.out.println("sendControlGroupDevices");
	}

	@Override
	public void GetBindSceneInfo(String var1, String var2, JSONArray var3) {

		System.out.println("GetBindSceneInfo");
	}

	@Override
	public void SetBindDevInfo(String var1, String var2, String var3,
			JSONArray var4) {
		System.out.println("SetBindDevInfo");
	}

	@Override
	public void GetBindDevInfo(String var1, String var2, JSONArray var3) {
		System.out.println("GetBindDevInfo");
	}

	@Override
	public void SetDeviceInfo(String arg0, DeviceInfo arg1, DeviceEPInfo arg2) {
		// TODO Auto-generated method stub
		System.out.println("SetDeviceInfo");
	}

	@Override
	public void SetMonitorInfo(MonitorInfo arg0) {
		// TODO Auto-generated method stub
		System.err.println("SetMonitorInfo");
	}

	@Override
	public void SetRoomInfo(String arg0, RoomInfo arg1) {
		// TODO Auto-generated method stub
		System.err.println("SetRoomInfo");
	}

	@Override
	public void SetSceneInfo(String arg0, SceneInfo arg1) {
		// TODO Auto-generated method stub
		System.err.println("SetSceneInfo");
	}

	@Override
	public void setGatewayInfo(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, String arg6, String arg7,
			String arg8, String arg9) {
		// TODO Auto-generated method stub
		System.out.println("setGatewayInfo");

	}

	@Override
	public void MiniGatewayWifiSetting(String arg0, String arg1, String arg2,
			String arg3, String arg4) {
		// TODO Auto-generated method stub

	}

	//获取设备信号
	@Override
	public void QueryDevRssiInfo(String arg0, String arg1, String arg2,
			String arg3) {






	}

	@Override
	public void commondDeviceConfiguration(String arg0, String arg1,
			String arg2, long arg3, String arg4, String arg5) {
		// TODO Auto-generated method stub

	}

	@Override
	public void GetAutoProgramTaskInfo(String arg0,
			List<AutoProgramTaskInfo> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void GetDeviceIRInfo(String arg0, String arg1, String arg2,
			String arg3, Set<DeviceIRInfo> arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void GetTaskInfo(String arg0, String arg1, String arg2,
			Set<TaskInfo> arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SetAutoProgramTaskInfo(String arg0, String arg1,
			AutoProgramTaskInfo arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SetDeviceIRInfo(String arg0, String arg1, String arg2,
			String arg3, String arg4, Set<DeviceIRInfo> arg5) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SetTaskInfo(String arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, String arg6, Set<TaskInfo> arg7) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAutoProgramRulesEffectStatus(String arg0, String arg1,
			List<RulesGroupInfo> arg2) {
		// TODO Auto-generated method stub

	}
	@Override
	public void NewDoorLockAccountSetting(String arg0, String arg1, String arg2, JSONObject arg3) {
		// TODO Auto-generated method stub
		
	}
}