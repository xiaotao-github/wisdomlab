package com.fengbiaoedu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


import cc.wulian.ihome.wan.NetSDK;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import cc.wulian.ihome.wan.util.ConstUtil;
import cc.wulian.ihome.wan.util.StringUtil;
import org.springframework.util.StringUtils;

public class DeviceTool {
	static Properties mProperties;
	
	static{
		mProperties = new Properties();
		
		InputStream is = DeviceTool.class.getClassLoader().getResourceAsStream("static/njwl/devices_ch.properties");
		try {
			mProperties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 是否是警报设备
	public static boolean isAlarmDevByType(String devType) {
		if (ConstUtil.DEV_TYPE_FROM_GW_WARNING.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_MOTION.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTACT.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_EMERGENCY.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_MOTION_F.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_FIRE.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_NH3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_GAS.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_GAS_VALVE.equals(devType)) {
			return true;
		} else {
			return false;
		}
	}

	// 是否是多控设备
	public static boolean isMutiCtrlDevByType(String devType) {
		if (ConstUtil.DEV_TYPE_FROM_GW_BUTTON_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BUTTON_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BUTTON_4.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTROL_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTROL_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_4.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOCK_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOCK_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DUAL_D_LIGHT.equals(devType)) {
			return true;
		} else {
			return false;
		}
	}

	// 是不是绑定场景设备
	public static boolean isBindSceneByType(String devType) {
		if (ConstUtil.DEV_TYPE_FROM_GW_TOUCH_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_TOUCH_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_TOUCH_4.equals(devType)) {
			return true;
		} else {
			return false;
		}
	}

	// 是否是传感器设备
	public static boolean isSensorDevByType(String devType) {
		if (ConstUtil.DEV_TYPE_FROM_GW_LIGHT_S.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_TEMHUM.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_FLOW.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CTHV.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CO2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_VOC.equals(devType)) {
			return true;
		} else {
			return false;
		}
	}

	// 是不是  不可控设备?
	public static boolean isNoQuickCtrlDevByType(String devType) {
		if (
				//ConstUtil.DEV_TYPE_FROM_GW_IR_CONTROL.equals(devType)
				ConstUtil.DEV_TYPE_FROM_GW_TOUCH_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_TOUCH_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_TOUCH_4.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_SCALE.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CARPARK.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BUTTON_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BUTTON_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BUTTON_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_EXTENDER.equals(devType)
				|| isSensorDevByType(devType)) {
			return true;
		} else {
			return false;
		}
	}

	// 获取设备名
	public static String getDevDefaultNameByType(String devType) {
		String C = null;
		if (StringUtil.isNullOrEmpty(devType)) {
			return "unknow";
		} else {
			try {
				String CR = mProperties.getProperty(devType);
				if (CR == null) {
					return "unknow";
				}
				C = new String(CR.getBytes("ISO-8859-1"), "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// C = mProperties.getProperty(devType);
		}
		return C;
	}

	static String getResString(String key) {
		if (StringUtil.isNullOrEmpty(key)) {
			return null;
		} else {
			return mProperties.getProperty(key);
		}
	}
	//TODO 获取通过数据和类型
	public static boolean getSendCtrlStatusByByTypeAndData(String epData,
			String devType, String epStatus, boolean isCtrl) {
		if (isAlarmDevByType(devType)) {

			if (ConstUtil.DEV_TYPE_FROM_GW_GAS_VALVE.equals(devType)||
					ConstUtil.DEV_TYPE_FROM_GW_WARNING.equals(devType)
					) {
				if (isCtrl) {
					if (epData.endsWith("1")) {
						return true;
					} else {
						return false;
					}
				} else {
					if (epData.endsWith("1")) {
						return true;
					} else {
						if (epData.startsWith("1")) {
							return true;
						} else {
							return false;
						}
					}
				}
			} else {
				if (isCtrl) {
					if (!StringUtil.isNullOrEmpty(epStatus)
							&& epStatus.startsWith("1")) {
						return true;
					} else {
						return false;
					}
				} else {
					if (epData.endsWith("1")) {
						return true;
					} else {
						if (!StringUtil.isNullOrEmpty(epStatus)
								&& epStatus.startsWith("0")) {
							return true;
						} else {
							return false;
						}
					}
				}
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_GAS_VALVE.equals(devType)) {
			if (epData.endsWith("1")) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_LIGHT.equals(devType)) {
			if (epData.startsWith("1")) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_D_LIGHT.equals(devType)) {
			if ("0".equals(epData)) {
				return false;
			} else {
				return true;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DUAL_D_LIGHT.equals(devType)) {
			if ("000".equals(epData)) {
				return false;
			} else {
				return true;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_EMS.equals(devType)) {
			if (epData.startsWith("0101")) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOCK.equals(devType)) {
			if (epData.startsWith("1")) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOOR_CONTROL.equals(devType)) {
			if ("2".equals(epData) || "4".equals(epData)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_BARRIER.equals(devType)) {
			if ("2".equals(epData)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_WATER_VALVE.equals(devType)) {
			boolean normal = "11".equals(epData);
			boolean timer = epData.startsWith("2");
			if (normal || timer) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOCK_1.equals(devType)) {
			if ("1".equals(epData)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOCK_2.equals(devType)) {
			if ("1".equals(epData)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOCK_3.equals(devType)) {
			if ("1".equals(epData)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_BUTTON_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTROL_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_1.equals(devType)) {
			if ("1".equals(epData)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_BUTTON_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTROL_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_2.equals(devType)) {
			if ("1".equals(epData)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_BUTTON_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTROL_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_3.equals(devType)) {
			if ("1".equals(epData)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_BUTTON_4.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_4.equals(devType)) {
			if ("1".equals(epData)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_SHADE.equals(devType)) {
			if (epData.startsWith("2")) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_BLIND.equals(devType)) {
			if (epData.startsWith("2")) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK.equals(devType)) {
			if (epData.length() < 5) {
				return false;
			}
			if (epData.startsWith("3") || epData.startsWith("1")) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_2.equals(devType)) {
			if (epData.length() < 5) {
				return false;
			}
			String first = epData.substring(0, 1);
			if ("1".equals(first)) {
				return true;
			} else {
				return false;
			}
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_3.equals(devType)) {
			if (epData.startsWith("1")) {
				return true;
			} else {
				return false;
			}
		}else if(ConstUtil.DEV_TYPE_FROM_GW_IR_CONTROL.equals(devType)){
			//TODO
			if(epData.endsWith("2")){
				return true;
			}else{
				return false;
			}

		}else {
			return false;
		}
	}

	/**
	 * 将数据格式化
	 * @param epType  设备类型
	 * @param epData 设备数据
	 * @param epStatus 设备状态
	 * @return
	 */
	public static String getDevDataText(String epType, String epData,
			String epStatus) {
		String first;
		String second;
		String third;
		StringBuffer sb = new StringBuffer();
		if (StringUtil.isNullOrEmpty(epData)) {
			return "";
		}

		// 可燃气阀门，类型值10
		if (isAlarmDevByType(epType)) {
			if (ConstUtil.DEV_TYPE_FROM_GW_GAS_VALVE.equals(epType)) {
				// alarm
				sb.append(epData.startsWith("1") ? getResString("device_baojing")
						: getResString("device_daijing"));
				sb.append("\t");
				sb.append(epData.endsWith("0") ? getResString("device_guan")
						: getResString("device_ka"));
			} else {
				sb.append(epData.startsWith("0") ? getResString("device_daijing")
						: getResString("device_baojing"));
				sb.append("\t");
				sb.append("0".equals(epStatus) ? getResString("device_cafang")
						: getResString("device_bufang"));
			}
		}

		// 类型值11

		else if (ConstUtil.DEV_TYPE_FROM_GW_LIGHT.equals(epType)) {
			sb.append("0".equals(epData) ? getResString("device_guan")
					: getResString("device_kai"));
		}

		// 一位调光灯，类型值12

		else if (ConstUtil.DEV_TYPE_FROM_GW_D_LIGHT.equals(epType)) {
			int epDataInt = StringUtil.toInteger(epData);
			sb.append(0 == epDataInt ? getResString("device_guan")
					: 100 == epDataInt ? getResString("device_kai") : epDataInt
							+ "%");
		}
		// 移位计量插座，类型值15
		else if (ConstUtil.DEV_TYPE_FROM_GW_EMS.equals(epType)) {
			sb.append(epData.startsWith("0101") ? getResString("device_kai")
					: epData.startsWith("0100") ? getResString("device_guan")
							: getResString("device_exception"));
		}
		// 移动普通插座，类型值16
		else if (ConstUtil.DEV_TYPE_FROM_GW_DOCK.equals(epType)) {
			sb.append(epData.startsWith("0") ? getResString("device_guan")
					: getResString("device_kai"));
		}
		// 温湿度检测器，类型值17
		else if (ConstUtil.DEV_TYPE_FROM_GW_TEMHUM.equals(epType)) {
			if (epData.contains(",")) {
				String[] temp = epData.split(",");
				sb.append(temp[0]).append("℃");
				sb.append(" ");
				sb.append(temp[1]).append("%RH");
			}
		}

		// 二痒化碳，类型值18
		else if (ConstUtil.DEV_TYPE_FROM_GW_CO2.equals(epType)
				|| ConstUtil.DEV_TYPE_FROM_GW_VOC.equals(epType)) {
			sb.append(epData).append("PPM");
		}
		// 光强检测器，类型值19
		else if (ConstUtil.DEV_TYPE_FROM_GW_LIGHT_S.equals(epType)) {
			sb.append(epData).append("LUX");
		}
		// 类型值26

		else if (ConstUtil.DEV_TYPE_FROM_GW_DOOR_CONTROL.equals(epType)) {
			sb.append("2".equals(epData) ? getResString("device_kai") : "3"
					.equals(epData) ? getResString("device_guan") : "4"
					.equals(epData) ? getResString("device_stop")
					: getResString("device_exception"));
		}

		// 类型值27
		else if (ConstUtil.DEV_TYPE_FROM_GW_BARRIER.equals(epType)) {
			sb.append("2".equals(epData) ? getResString("device_kai") : "3"
					.equals(epData) ? getResString("device_guan") : "4"
					.equals(epData) ? getResString("device_stop")
					: getResString("device_exception"));
		}
		// 水阀，类型值28
		else if (ConstUtil.DEV_TYPE_FROM_GW_WATER_VALVE.equals(epType)) {
			boolean normal = "11".equals(epData);
			boolean timer = epData.startsWith("2");
			sb.append(normal || timer ? getResString("device_kai")
					: getResString("device_guan"));
		} else if (isBindSceneByType(epType)) {
			sb.append(" ");
		}
		// 类型值41
		else if (ConstUtil.DEV_TYPE_FROM_GW_FLOW.equals(epType)) {
			sb.append(epData).append("M3");
		}

		// 电子秤，类型值45
		else if (ConstUtil.DEV_TYPE_FROM_GW_SCALE.equals(epType)) {
			sb.append(epData).append("KG");
		}

		// 车位检测器，类型值46
		else if (ConstUtil.DEV_TYPE_FROM_GW_CARPARK.equals(epType)) {
			if (epData.contains(",")) {
				String[] temp = epData.split(",");
				first = temp[0];
				second = temp[1];

				sb.append(first)
						.append("MM")
						.append("\t")
						.append("0".equals(second) ? getResString("device_park_no_obstacle")
								: "1".equals(second) ? getResString("device_park_has_obstacle")
										: getResString("device_exception"));
			}
		}

		// 一位墙面插座，类型值50或52或57
		// 或一位照明开关，类型值61

		else if (ConstUtil.DEV_TYPE_FROM_GW_DOCK_1.equals(epType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BUTTON_1.equals(epType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTROL_1.equals(epType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_1.equals(epType)
				|| isMutiCtrlDevByType(epType)) {
			sb.append(epData.endsWith("0") ? getResString("device_guan")
					: epData.endsWith("1") ? getResString("device_kai")
							: getResString("device_exception"));
		}

		// 窗帘控制器或百叶窗，类型值65或66
		else if (ConstUtil.DEV_TYPE_FROM_GW_SHADE.equals(epType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BLIND.equals(epType)) {
			sb.append("3".equals(epData) ? getResString("device_guan") : "2"
					.equals(epData) ? getResString("device_kai")
					: getResString("device_stop"));
		}

		// 一代门锁或三代门锁，类型值67或69
		else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK.equals(epType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_3.equals(epType)) {
			if (epData.length() >= 5) {
				first = epData.substring(0, 1);
				second = epData.substring(1, 3);
				third = epData.substring(3, 5);

				sb.append("1".equals(first) ? getResString("device_jiesuo")
						: "2".equals(first) ? getResString("device_shangsuo")
								: "3".equals(first) ? getResString("device_jiesuoyanshi")
										: getResString("device_exception"));
				sb.append(" ");

				sb.append("10".equals(second) ? getResString("device_yibaoxian")
						: "11".equals(second) ? getResString("device_weibaoxian")
								: getResString("device_exception"));
				sb.append(" ");

				sb.append("21".equals(third) ? getResString("device_suoyikai")
						: "22".equals(third) ? getResString("device_suoyiguan")
								: "23".equals(third) ? getResString("device_baojing")
										: getResString("device_exception"));
			}
		}

		// 二代门锁，类型值68
		else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_2.equals(epType)) {
			if (epData.length() >= 5) {
				first = epData.substring(0, 1);
				second = epData.substring(1, 3);
				third = epData.substring(3, 5);

				sb.append("1".equals(first) ? getResString("device_jiesuo")
						: "2".equals(first) ? getResString("device_shangsuo")
								: getResString("device_exception"));
				sb.append(" ");

				sb.append("10".equals(second) ? getResString("device_yibaoxian")
						: "11".equals(second) ? getResString("device_weibaoxian")
								: "25".equals(second) ? getResString("device_qiangzhishangsuo")
										: "26".equals(second) ? getResString("device_zidongshangsuo")
												: "30".equals(second) ? getResString("device_mimajiesuo")
														: "31".equals(second) ? getResString("device_niukou")
																+ "1"
																+ getResString("device_jiesuo")
																: "32".equals(second) ? getResString("device_niukou")
																		+ "2"
																		+ getResString("device_jiesuo")
																		: "33".equals(second) ? getResString("device_niukou")
																				+ "3"
																				+ getResString("device_jiesuo")
																				: "34".equals(second) ? getResString("device_niukou")
																						+ "4"
																						+ getResString("device_jiesuo")
																						: getResString("device_exception"));
				sb.append(" ");

				sb.append("23".equals(third) ? getResString("device_ruqinbaojing")
						: "24".equals(third) ? getResString("device_jiechubaojing")
								: "29".equals(third) ? getResString("device_pohuaibaojing")
										: getResString("device_exception"));
			}
		}
		if(StringUtils.isEmpty(sb.toString())){
			return epData;
		}else{
			return sb.toString();
		}
	}

	public static String getDevCtrlDataByType(String devType, int ctrlType) {
		if (ctrlType == 0) {
			return getOpenCtrlData(devType);
		} else if (ctrlType == 1) {
			return getCloseCtrlData(devType);
		} else if (ctrlType == 2) {
			return getStopOrDelayCtrlData(devType);
		} else {
			//KLog.i("控制返空null");
			return null;
		}
	}
	//开启设备的控制命令
	private static String getOpenCtrlData(String devType) {
		//KLog.i("开");
		if (ConstUtil.DEV_TYPE_FROM_GW_DOCK.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOCK_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BUTTON_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTROL_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_1.equals(devType)
				|| isMutiCtrlDevByType(devType)) {
			if (ConstUtil.DEV_TYPE_FROM_GW_DUAL_D_LIGHT.equals(devType)) {
				return "100";
			} else {
				return "1";
			}
		} else if (isAlarmDevByType(devType)) {
			return "1";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_LIGHT.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_D_LIGHT.equals(devType)) {
			return "100";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DUAL_D_LIGHT.equals(devType)) {
			return "100";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_EMS.equals(devType)) {
			return "11";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOOR_CONTROL.equals(devType)) {
			return "2";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_BARRIER.equals(devType)) {
			return "2";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_WATER_VALVE.equals(devType)) {
			return "11";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_SHADE.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BLIND.equals(devType)) {
			return "2";
		}
		// DOORLOCK one use 3s for close door
		else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK.equals(devType)) {
			return "3";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_4.equals(devType)) {
			return "1";
			//TODO 空调开  -- 测试开空调的命令是否固定
		}else if(ConstUtil.DEV_TYPE_FROM_GW_IR_CONTROL.equals(devType)){
			return "265248";
			//return "21";
		}else{
	//		KLog.i("开，控制返空null");
			return null;
		}
	}

	//关闭设备命令
	private static String getCloseCtrlData(String devType) {
	//	KLog.i("关");
		if (ConstUtil.DEV_TYPE_FROM_GW_DOCK.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOCK_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BUTTON_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTROL_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_1.equals(devType)
				|| isMutiCtrlDevByType(devType)) {
			if (ConstUtil.DEV_TYPE_FROM_GW_DUAL_D_LIGHT.equals(devType)) {
				return "000";
			} else {
				return "0";
			}
		} else if (isAlarmDevByType(devType)) {
			return "0";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_LIGHT.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_D_LIGHT.equals(devType)) {
			return "0";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DUAL_D_LIGHT.equals(devType)) {
			return "000";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_EMS.equals(devType)) {
			return "10";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOOR_CONTROL.equals(devType)) {
			return "3";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_BARRIER.equals(devType)) {
			return "3";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_WATER_VALVE.equals(devType)) {
			return "10";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_SHADE.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BLIND.equals(devType)) {
			return "3";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK.equals(devType)) {
			// always send open 3s
			return "3";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_4.equals(devType)) {
			return "2";
			//TODO 空调关 测试命令是否固定
		}else if(ConstUtil.DEV_TYPE_FROM_GW_IR_CONTROL.equals(devType)){
			return "265148";
			//return "20";
		}else {
		//	KLog.i("关，控制返空null");
			return null;
		}
	}
	//根据命令判断设备的开关状态
	public static String getCloseOrOpenByTypeAndEpData(String devType,String epData) {
		if (ConstUtil.DEV_TYPE_FROM_GW_DOCK.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOCK_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BUTTON_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_CONTROL_1.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_LIGHT_1.equals(devType)
				|| isMutiCtrlDevByType(devType)) {
			if (ConstUtil.DEV_TYPE_FROM_GW_DUAL_D_LIGHT.equals(devType)) {
				return "000".equals(epData)?"0":"1";
			} else {
				return "0".equals(epData)?"0":"1";
			}
			//是否报警设备
		} else if (isAlarmDevByType(devType)) {
			//return "0".equals(epData)?"1":"0";
			return epData;
		} else if (ConstUtil.DEV_TYPE_FROM_GW_LIGHT.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_D_LIGHT.equals(devType)) {
			return "0".equals(epData)?"0":"1";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DUAL_D_LIGHT.equals(devType)) {
			return "000".equals(epData)?"0":"1";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_EMS.equals(devType)) {
			return "10".equals(epData)?"0":"1";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOOR_CONTROL.equals(devType)) {
			return "3".equals(epData)?"1":"0";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_BARRIER.equals(devType)) {
			return "3".equals(epData)?"1":"0";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_WATER_VALVE.equals(devType)) {
			return "10".equals(epData)?"1":"0";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_SHADE.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BLIND.equals(devType)) {
			return "3".equals(epData)?"1":"0";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK.equals(devType)) {
			// always send open 3s
			return "3".equals(epData)?"1":"0";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_2.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_3.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_DOORLOCK_4.equals(devType)) {
			return "2".equals(epData)?"1":"0";
			//TODO 空调关 测试命令是否固定
		}else if(ConstUtil.DEV_TYPE_FROM_GW_IR_CONTROL.equals(devType)){
			return "265148".equals(epData)?"1":"0";
		}else{
			return epData;
		}
	}



	private static String getStopOrDelayCtrlData(String devType) {
	//	KLog.i("停");
		if (ConstUtil.DEV_TYPE_FROM_GW_BARRIER.equals(devType)) {
			return "4";
		} else if (ConstUtil.DEV_TYPE_FROM_GW_SHADE.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_BLIND.equals(devType)) {
			return "1";
		} else {
		//	KLog.i("停，控制返空null");
			return null;

		}
	}

	// called by MainActivity List long click
	public static void  controlDevice(DeviceInfo deviceInfo,String ep) {
		String gwId = deviceInfo.getGwID();
		String devID = deviceInfo.getDevID();
		// String devType = deviceInfo.getType();
		String devType = deviceInfo.getDevEPInfo().getEpType();
		String data = deviceInfo.getDevEPInfo().getEpData();
		String epStatus = deviceInfo.getDevEPInfo().getEpStatus();

		// 是否可控设备
		if (DeviceTool.isNoQuickCtrlDevByType(devType))
			return;
		// 设备状态，开或关
		boolean isOpened = DeviceTool.getSendCtrlStatusByByTypeAndData(data,
				devType, epStatus, true);
		int ctrlType = isOpened ? 1 : 0;
		// 设备是四位场景开关或红外控制器
		if (ConstUtil.DEV_TYPE_FROM_GW_TOUCH_4.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_IR_CONTROL.equals(devType)) {
			// 设备是四位场景开关或红外控制器
			if( ConstUtil.DEV_TYPE_FROM_GW_IR_CONTROL.equals(devType)){
				NetSDK.sendControlDevMsg(gwId, devID, ep, devType,DeviceTool.getDevCtrlDataByType(devType, ctrlType));
			}


		} else if (DeviceTool.isAlarmDevByType(devType)) {
			// 设备是警报感应设备
			if (ConstUtil.DEV_TYPE_FROM_GW_GAS_VALVE.equals(devType)||
					ConstUtil.DEV_TYPE_FROM_GW_WARNING.equals(devType)
					) {
				// 设备是可燃气门
			//	KLog.i("NetSDK.sendControlDevMsg");// LOG
				NetSDK.sendControlDevMsg(gwId, devID, "14", devType,
						DeviceTool.getDevCtrlDataByType(devType, ctrlType));
			}
		} else {
			//KLog.i("NetSDK.sendControlDevMsg");// LOG
			NetSDK.sendControlDevMsg(gwId, devID, ep==null?"14":ep, devType,
					DeviceTool.getDevCtrlDataByType(devType, ctrlType));
		}
	}

	//TODO  获取设备的Icon 
	public static int getIconDevByType(String Type) {
		int res = 0;
		/*switch (Integer.valueOf(Type)) {
		case 1:
			res = R.drawable.device_warning_normal;
			break;
		case 2:
			res = R.drawable.device_motion_light_sensor_normal;
			break;
		case 12:
			res = R.drawable.device_d_light_big;
			break;
		case 17:
			res = R.drawable.device_temp_normal_big;
			break;
		case 18:
			res = R.drawable.device_co2_normal_big;
			break;
		case 19:
			res = R.drawable.device_light_sensor_normal_big;
			break;
		case 22:
			res = R.drawable.device_ir_control_normal;
			break;

		case 37:
			res = R.drawable.device_bind_scene_normal_6;
			break;
		case 43:
			res = R.drawable.device_smoke_normal;
			break;
		case 50:
			res = R.drawable.device_dock_open_big;
			break;
		case 61:
			res = R.drawable.device_button_1_open_big;
			break;

		}*/
		return 01000;
	}
	/*
	* 执行自定义控制
	*
	* */

	// called by MainActivity List long click
	public static void  customControlDevice(DeviceInfo deviceInfo,String ep,Integer code) {
		String gwId = deviceInfo.getGwID();
		String devID = deviceInfo.getDevID();
		// String devType = deviceInfo.getType();
		String devType = deviceInfo.getDevEPInfo().getEpType();
		String data = deviceInfo.getDevEPInfo().getEpData();
		String epStatus = deviceInfo.getDevEPInfo().getEpStatus();

		// 是否可控设备
		if (DeviceTool.isNoQuickCtrlDevByType(devType))
			return;
		int ctrlType = code;
		// 设备是四位场景开关或红外控制器
		if (ConstUtil.DEV_TYPE_FROM_GW_TOUCH_4.equals(devType)
				|| ConstUtil.DEV_TYPE_FROM_GW_IR_CONTROL.equals(devType)) {
			// 设备是四位场景开关或红外控制器
			if( ConstUtil.DEV_TYPE_FROM_GW_IR_CONTROL.equals(devType)){
				NetSDK.sendControlDevMsg(gwId, devID, ep, devType,DeviceTool.getDevCtrlDataByType(devType, ctrlType));
			}


		} else if (DeviceTool.isAlarmDevByType(devType)) {
			// 设备是警报感应设备
			if (ConstUtil.DEV_TYPE_FROM_GW_GAS_VALVE.equals(devType)||
					ConstUtil.DEV_TYPE_FROM_GW_WARNING.equals(devType)
					) {
				// 设备是可燃气门
				//	KLog.i("NetSDK.sendControlDevMsg");// LOG
				NetSDK.sendControlDevMsg(gwId, devID, "14", devType,
						DeviceTool.getDevCtrlDataByType(devType, ctrlType));
			}
		} else {
			//KLog.i("NetSDK.sendControlDevMsg");// LOG
			NetSDK.sendControlDevMsg(gwId, devID, ep==null?"14":ep, devType,
					DeviceTool.getDevCtrlDataByType(devType, ctrlType));
		}
	}
}
