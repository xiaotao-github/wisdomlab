package com.fengbiaoedu.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fengbiaoedu.bean.LabClockInMachine;
import com.fengbiaoedu.bean.LabClockInMachineUser;
import com.fengbiaoedu.bean.LabClockInRecord;
import com.fengbiaoedu.bean.LabClockInUserInfoWithBLOBs;
import com.fengbiaoedu.bean.ScheduleStudentScore;
import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.enums.SigninEnum;
import com.fengbiaoedu.kemi.ClockInManager;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.pojo.ClockIn;
import com.fengbiaoedu.pojo.ClockInUserInfoBO;
import com.fengbiaoedu.pojo.EnrollData;
import com.fengbiaoedu.redis.RedisLock;

import cn.hutool.core.date.DateUtil;
/**
 * 考勤工具类
 * 包括：
 * 1：对考勤机请求队列处理，响应队列处理，考勤机任务识别码生成，考勤机工号生成，处理考勤机考勤打卡，封装课程学生分数的考勤状态等考勤机相关的工具方法。
 * 2：考勤机请求体二进制格式的字节数组生成，考勤机响应的请求体二进制格式字节数组的解析。等考勤机数据格式转换工具方法。
 * 
 * 
 * @author wucb
 *
 */
@Service
public class ClockInUtils {
	
	private static AtomicInteger count = new AtomicInteger(1000);
	
	@Autowired
	private  RedisUtil redisUtil;
	private static void increase() {
		count.incrementAndGet();
	}
	
	private static  HashMap<String,Integer> backNumberMap = new HashMap<>() ;
	
	/****
	 * backup_number字段： 表示登记数据的类型的数字。设置以如下数值中一个。
    	0 ~ 9 : 用户10个指头的指纹数据
    	10 : 用户的密码
    	11 : 用户的ID卡号
    	12 : 用户的人脸数据
	 */
	static{
		backNumberMap.put("fp1", 0);
		backNumberMap.put("fp2", 1);
		backNumberMap.put("fp3", 2);
		backNumberMap.put("fp4", 3);
		backNumberMap.put("fp5", 4);
		backNumberMap.put("fp6", 5);
		backNumberMap.put("fp7", 6);
		backNumberMap.put("fp8", 7);
		backNumberMap.put("fp9", 8);
		backNumberMap.put("fp10", 9);
		backNumberMap.put("password", 10);
		backNumberMap.put("card", 11);
		backNumberMap.put("face", 12);
	 
	}
	
	/**
	 * 获取字符串的请求字节数组</br>
	 * @param cmdParmText 请求字符串 </br>
	 * @param abytBuffer   </br>
	 * @return  true 
	 */
	public  byte[] CreateByteFromString(String cmdParmText, byte[] abytBuffer){
		 
		if (cmdParmText.length() == 0)
			return abytBuffer;
		
        try {
        	//将字符串转换成字节分配，最后贴上0。.
            // 前4个字节是字符串的长度。
            byte[] byteText = cmdParmText.getBytes("UTF-8");
            //长度加1，用于在最后设置为0
            byte[] byteTextLen = Utils.intToBytes(byteText.length+1);
            abytBuffer = new byte[4 + byteText.length + 1];
            
            //复制字符串长度的字节数组到返回字节数组中
            System.arraycopy(byteTextLen, 0, abytBuffer, 0, byteTextLen.length);
            //复制字符串内容的字节数组到返回字节数组中
            System.arraycopy(byteText, 0, abytBuffer, 4, byteText.length);
            //最后设置为0
            abytBuffer[4 + byteText.length] = 0;
            return abytBuffer;
        }catch(Exception e) {
            abytBuffer = new byte[0];
            return abytBuffer;
        }
	}
	
	/**
	 * 将机器响应返回的byte[]分成字符串和二进制数组</br>
	 * @param abytBSComm 从机器获取得来的byte[] </br>
	 * @param type  1：包括二进制长度信息，适合不定长二进制信息， 0：不包括二进制长度信息，适合定长二进制信息</br>
	 * @return 字符串和二进制数组</br>
	 */
    public  Object[] getStringAnd1stBinaryFromResponseBuffer(byte[] abytBSComm,int type){
         
    	String str = "";
    	byte[] binary = new byte[0];
        if (abytBSComm.length < 4)
            return new Object[] {str,binary};

        try{
            int lenText = Utils.bytesToInt(abytBSComm, 0);//获取字符串长度
            if (lenText > abytBSComm.length - 4)
            	return new Object[] {str,binary};

            if (lenText == 0){
            	str = "";
            }else{
                if (abytBSComm[4 + lenText - 1] == 0) 
                	str = new String(abytBSComm, 4, lenText - 1,"UTF-8");
                else
                	str = new String(abytBSComm, 4, lenText,"UTF-8");
            }
            //二进制长度起始索引
            int posBin = 4 + lenText;
            
            //二进制长度
            int lenBin = Utils.bytesToInt(abytBSComm, posBin);
            if (lenBin < 1)
            	return new Object[] {str,binary};

            if (lenBin > abytBSComm.length - posBin - 4)
            	return new Object[] {str,binary};
             
            //System.arraycopy(abytBSComm, posBin+4,  binary, 0,lenBin);
            if(type ==1) {
            	lenBin = abytBSComm.length-lenText-4;
            	binary = new byte[lenBin];
                System.arraycopy(abytBSComm, posBin,  binary, 0,lenBin);
            }else {
            	binary = new byte[lenBin];
                System.arraycopy(abytBSComm, posBin+4,  binary, 0,lenBin);
            }
            return new Object[] {str,binary};
        }catch(Exception e) {
        	return null;
        }
    }
    
    /**
     * 获取考勤机的用户id列表</br>
     * @param strAndListByte   将机器响应返回的byte[]分成字符串和二进制数组</br>
     * @return
     */
    public  List<Integer> getUserIdList(Object[] strAndListByte) {
    	List<Integer> userIdList = new ArrayList<>();
    	if(strAndListByte==null) {
    		return new ArrayList<>();
    	}
		String str = (String)strAndListByte[0];
		try {
			JsonNode readTree = JsonUtils.getMapper().readTree(str);
			Integer userIdCount = readTree.get("user_id_count").asInt();
			Integer oneUserIdSize = readTree.get("one_user_id_size").asInt();
			byte[] userIdByte = (byte[]) strAndListByte[1];
			if(userIdByte ==null || userIdCount*oneUserIdSize!=userIdByte.length) {
				return new ArrayList<>();
			}else {
				
				int index =0;
				int srcPos = 0;
				while(index<userIdByte.length){
					
					byte[] userId = new byte[oneUserIdSize];
					System.arraycopy(userIdByte,srcPos, userId, 0, oneUserIdSize);
					
					userIdList.add(Utils.bytesToInt(userId, 0));
					
					srcPos+=oneUserIdSize;
					index =srcPos;
					System.out.println("userId: "+Utils.bytesToInt(userId, 0));
					
				}
				return userIdList;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
 	}
    /**
     * 获取响应数据中，useridList(考勤机注册的工号列表)
     * @param strAndListByte
     * @return
     */
    public  List<Integer> getEnrollData(Object[] strAndListByte) {
    	List<Integer> userIdList = new ArrayList<>();
    	if(strAndListByte==null) {
    		return new ArrayList<>();
    	}
		String str = (String)strAndListByte[0];
		byte[] userEnroollDataByte = (byte[]) strAndListByte[1];
		try {
			JsonNode readTree = JsonUtils.getMapper().readTree(str);
			ArrayNode enroollDataArray = (ArrayNode) readTree.get("enroll_data_array");
			int length = 0;
			for (JsonNode jsonNode : enroollDataArray) {
				JsonNode backupNumber = jsonNode.get("backup_number");
				
				JsonNode enrollData = jsonNode.get("enroll_data");
			}
			int bytesToInt = Utils.bytesToInt(userEnroollDataByte, 0);
			if(userEnroollDataByte ==null ) {
				return new ArrayList<>();
			}else {
				
				return userIdList;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
    }
    /**
     * 考勤机用户登记信息的Map</br>
     * @param user
     * @param bytEnrollData
     * @return
     */
    public  HashMap<String,byte[]> getEnrollDataMap(ClockInUserInfoBO user, byte[] bytEnrollData) {
		
		HashMap<String, byte[]> binaryMap = new HashMap<>();
    	binaryMap.put("user_photo", null);
    	binaryMap.put("backup_number_0", null);
    	binaryMap.put("backup_number_1", null);
    	binaryMap.put("backup_number_2", null);
    	binaryMap.put("backup_number_3", null);
    	binaryMap.put("backup_number_4", null);
    	binaryMap.put("backup_number_5", null);
    	binaryMap.put("backup_number_6", null);
    	binaryMap.put("backup_number_7", null);
    	binaryMap.put("backup_number_8", null);
    	binaryMap.put("backup_number_9", null);
    	binaryMap.put("backup_number_10", null); //密码
    	binaryMap.put("backup_number_11", null);
    	binaryMap.put("backup_number_12", null); //人脸
    	 
		int posBin =0;
		int lenBin =0;
		byte[] binary = new byte[0];

		//测试长度
		int i = 0;
		
    	if(bytEnrollData != null && bytEnrollData.length > 0 ) {
    		
    		String user_photo = user.getUser_photo();
    		//添加用户头像到Map中
    		
    		if(user_photo != null) {
    			lenBin = Utils.bytesToInt(bytEnrollData, 0);
    			binary = new byte[lenBin];
    			posBin += 4;
    			System.arraycopy(bytEnrollData, posBin, binary, 0, lenBin);
    			binaryMap.put("user_photo",binary);
    			
    			posBin = posBin + lenBin;
    			
    			i++;
    			System.out.println("第"+i+"个二进制长度："+lenBin);
    		}
    		
    		List<EnrollData> enroll_data_array = user.getEnroll_data_array();
            if(enroll_data_array.size()>0 && enroll_data_array != null) {
            	
	           	for (EnrollData enrollData : enroll_data_array) {
	           		lenBin = Utils.bytesToInt(bytEnrollData, posBin );
	           		binary = new byte[lenBin];
	           		posBin += 4;
	    			System.arraycopy(bytEnrollData, posBin, binary, 0, lenBin);
	           		
	    			//添加相应的二进制类型到Map中
	    			binaryMap.put("backup_number_"+enrollData.getBackup_number(), binary);
	    			
	    			posBin = posBin + lenBin;
	    			
	    			i++;
	    			System.out.println("第"+i+"个二进制长度："+lenBin);
           		}
            }
    	}
        
    	return binaryMap;
	}
	
    /**
     * 将考勤机响应的数据转换成LabClockInUserInfoWithBLOBs，用于保存到数据库</br>
     * @param user
     * @param bytEnrollData
     * @return
     */
    public  LabClockInUserInfoWithBLOBs getClockInUserInfoWithBLOBs(ClockInUserInfoBO user, byte[] bytEnrollData) {
		
    	LabClockInUserInfoWithBLOBs clockInUserInfoWithBLOBs  = new LabClockInUserInfoWithBLOBs();
			/**
			HashMap<String, byte[]> binaryMap = new HashMap<>();
	    	binaryMap.put("user_photo", null);
	    	binaryMap.put("backup_number_0", null);
	    	binaryMap.put("backup_number_1", null);
	    	binaryMap.put("backup_number_2", null);
	    	binaryMap.put("backup_number_3", null);
	    	binaryMap.put("backup_number_4", null);
	    	binaryMap.put("backup_number_5", null);
	    	binaryMap.put("backup_number_6", null);
	    	binaryMap.put("backup_number_7", null);
	    	binaryMap.put("backup_number_8", null);
	    	binaryMap.put("backup_number_9", null);
	    	binaryMap.put("backup_number_10", null); //密码
	    	binaryMap.put("backup_number_11", null);
	    	binaryMap.put("backup_number_12", null); //人脸
	    	 */
			int posBin =0;
			int lenBin =0;
			byte[] binary = new byte[0];
			
			//测试长度
			int i = 0;
		//设置ClockUserInfo
		clockInUserInfoWithBLOBs.setUpdateTime(new Date());
		clockInUserInfoWithBLOBs.setUserPrivilege(user.getUser_privilege());
		
    	if(bytEnrollData != null && bytEnrollData.length > 0 ) {
    		
    		String user_photo = user.getUser_photo();
    		
    		//添加用户头像到Map中
    		if(user_photo != null) {
    			lenBin = Utils.bytesToInt(bytEnrollData, 0);
    			binary = new byte[lenBin];
    			posBin += 4;
    			System.arraycopy(bytEnrollData, posBin, binary, 0, lenBin);
    			//binaryMap.put("user_photo",binary);
    			clockInUserInfoWithBLOBs.setEnrollPhoto(binary);
    			
    			posBin = posBin + lenBin;
    			i++;
			}
    		
    		List<EnrollData> enroll_data_array = user.getEnroll_data_array();
            if(enroll_data_array.size()>0 && enroll_data_array != null) {
            	
            	for (EnrollData enrollData : enroll_data_array) {
            		lenBin = Utils.bytesToInt(bytEnrollData, posBin );
	           		binary = new byte[lenBin];
	           		posBin += 4;
	    			System.arraycopy(bytEnrollData, posBin, binary, 0, lenBin);
	           		
	    			//添加相应的二进制类型
	    			switch (enrollData.getBackup_number()) {
					case 0:
						clockInUserInfoWithBLOBs.setEnrollFp1(binary);
						clockInUserInfoWithBLOBs.setFpEnrollTime(ClockInUtils.stringToTime(user.getFp_enroll_time()));
						break;
					case 1:
						clockInUserInfoWithBLOBs.setEnrollFp2(binary);
						break;
					case 2:
						clockInUserInfoWithBLOBs.setEnrollFp3(binary);
						break;
					case 3:
						clockInUserInfoWithBLOBs.setEnrollFp4(binary);
						break;
					case 4:
						clockInUserInfoWithBLOBs.setEnrollFp5(binary);
						break;
					case 5:
						clockInUserInfoWithBLOBs.setEnrollFp6(binary);
						break;
					case 6:
						clockInUserInfoWithBLOBs.setEnrollFp7(binary);
						break;
					case 7:
						clockInUserInfoWithBLOBs.setEnrollFp8(binary);
						break;
					case 8:
						clockInUserInfoWithBLOBs.setEnrollFp9(binary);
						break;
					case 9:
						clockInUserInfoWithBLOBs.setEnrollFp10(binary);
						break;
					case 10:
						clockInUserInfoWithBLOBs.setEnrollPassword(binary);
						clockInUserInfoWithBLOBs.setPasswordEnrollTime(ClockInUtils.stringToTime(user.getPassword_enroll_time()));
						break;
					case 11:
						clockInUserInfoWithBLOBs.setEnrollCard(binary);
						clockInUserInfoWithBLOBs.setCardEnrollTime(ClockInUtils.stringToTime(user.getCard_enroll_time()));
						break;
					case 12:
						clockInUserInfoWithBLOBs.setEnrollFace(binary);
						clockInUserInfoWithBLOBs.setFaceEnrollTime(ClockInUtils.stringToTime(user.getFace_enroll_time()));
						break;

					default:
						break;
					}
	    			posBin = posBin + lenBin;
	    			
	    			i++;
           		}
            }
    	}
        return clockInUserInfoWithBLOBs;
	}
	
    /**
	 * 处理向考勤机发送的指令</br>
	 * @param m
	 * @return
	 */
    public  CmdDTO handleReqCmd(String dev_id) {
		
    	//获取当前考勤机请求的相关命令队列，如果为null,直接返回null
//		ConcurrentLinkedQueue<CmdDTO> reqCmdQueue =  reqCmdTransIdDataMap.get(dev_id);
    	ConcurrentLinkedQueue<CmdDTO> reqCmdQueue =  redisUtil.getCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, dev_id);
		if(reqCmdQueue == null || reqCmdQueue.isEmpty()) {
			//删除指令队列
			redisUtil.removeCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, dev_id);
			return null;
		}
		//获取队列的最先请求的命令
		CmdDTO cmdDto = reqCmdQueue.poll();
		if(reqCmdQueue.isEmpty()) {
			//删除指令队列
			redisUtil.removeCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, dev_id);
		}else {
			
			String userOperateWorkKey = cmdDto.getUserOperateWorkKey();
   	 		//统计用户的指令需要操作考勤机的次数没有完成，直接return null
   	 		if(userOperateWorkKey!=null && !ClockInManager.USER_OPERATE_WORK_MAP.containsKey(userOperateWorkKey)) {
   	 			return null;
   	 		}
   	 		//更新指令队列
			redisUtil.updateCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, dev_id, reqCmdQueue);
		}
		//匹配相关命令，如果不匹配，直接返回null,命令集合放到redis中
		if(cmdDto == null) {
			return null;
		}else {
			
			//添加到响应的队列中
			ConcurrentLinkedQueue<CmdDTO> repCmdQueue = redisUtil.getCmdQueue(ClockInManager.REP_CMD_QUEUE_KEY, dev_id);
			if(repCmdQueue!=null && !repCmdQueue.isEmpty()) {
				repCmdQueue.offer(cmdDto);
			}else {
				ConcurrentLinkedQueue<CmdDTO> newRepCmdQueue = new ConcurrentLinkedQueue<>();
				newRepCmdQueue.offer(cmdDto);
				//更新指令队列
				redisUtil.updateCmdQueue(ClockInManager.REP_CMD_QUEUE_KEY, dev_id, newRepCmdQueue);
			}
			return cmdDto;
		}
	}
	/**
	 * 处理考勤机响应</br>
	 * @param devId
	 * @param transId
	 * @param blkNo
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public  CmdDTO handleRepCmd(String devId, String transId, String blkNo, ServletInputStream is) throws IOException {
		
		ConcurrentLinkedQueue<CmdDTO> repCmdQueue = redisUtil.getCmdQueue(ClockInManager.REP_CMD_QUEUE_KEY, devId);
		
		//是否存在该考勤机的响应队列
		Boolean exist = (repCmdQueue != null) || !repCmdQueue.isEmpty();
		if(exist) {
			CmdDTO cmdDto = repCmdQueue.peek();
			if(cmdDto!=null) {
				//是否是同一任务
				Boolean sameThansId = transId.equals(cmdDto.getTrans_id());
				if(sameThansId) {
					//获取传输过来的数据
					//获取用户字符串信息和二进制信息
					byte[] byteArray = Utils.readInputStream(is);
					
					//multipartTransIdDataMap中是否有给transId的数据
					//Boolean existMultipartTransIdDataMap =clockInManager.getMultipartTransIdDataMap().get(transId) != null;
					Boolean existMultipartTransIdData =redisUtil.exists(transId);
					//multipartTransIdDataMap存在transId的key的数据就拼接字节数组
					if(existMultipartTransIdData) {
						//CmdDTO preCmdDto = clockInManager.getMultipartTransIdDataMap().get(transId);
						CmdDTO preCmdDto = (CmdDTO) redisUtil.get(transId);
						byte[] byteMergerAll = Utils.byteMergerAll(preCmdDto==null? new byte[0]:preCmdDto.getResponseByte(),byteArray);
						cmdDto.setResponseByte(byteMergerAll);
					}else {
						cmdDto.setResponseByte(byteArray);
					}
					
					//是否传输结束
					Boolean over = blkNo.equals("0");
					if(over) {
						//结束就出列
						repCmdQueue.poll();
						//multipartTransIdDataMap存在transId的key的数据就删除
						if(existMultipartTransIdData) {
//							clockInManager.getMultipartTransIdDataMap().remove(transId);
							redisUtil.remove(transId);
						}
						//队列是否为空,则移除
						if(repCmdQueue.isEmpty()) {
//							clockInManager.getRepCmdTransIdDataMap().remove(devId);
							redisUtil.removeCmdQueue(ClockInManager.REP_CMD_QUEUE_KEY, devId);
						}
					}else {
						
						//新增或者覆盖到MultipartTransIdDataMap中
//						clockInManager.getMultipartTransIdDataMap().put(transId, cmdDto);
						redisUtil.putMultipartTransIdData(transId, cmdDto);
					}
					
					return cmdDto;
				}
			}
		}
		return null;
	}
	
	/**
	 * trans_id有实验室id+当前服务器的毫秒数+四位自增数</br>
	 * 在并发不是非常高的情况下应该不会重复</br>
	 * @param devId 考勤机的设备id</br>
	 * @return
	 */
	public  String setAndGetTransId(Long labId) {
		
		if(count.get()>9999) {
			count.set(1000);
		}
		increase();
		String currentTimeMillis = System.currentTimeMillis()+"";
		return labId+ currentTimeMillis.substring(currentTimeMillis.length()-6, currentTimeMillis.length())+count.get();
	}
	/**
	 * 日期转字符串
	 * @param date
	 * @return
	 */
	public static String timeToString(Date date) {
		if(date ==null) {
			return "00000000000000";
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 字符串转日期
	 * @param dateString
	 * @return
	 */
	public static Date stringToTime(String dateString) {
		if("00000000000000".equals(dateString)) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			return sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 生成考勤机工号</br>
	 * @param userId
	 * @return
	 */
	public static Long getUserId (Long userId) {
		//考勤机最长只能存储9位
		return userId%999999999;
	}
	
	/**
	 * 获取向考勤机请求，符合考勤机属性要求的对象，转成json字符串即可请求</br>
	 * @param clockInUserInfoWithBLOBs
	 * @return
	 */
	public static ClockInUserInfoBO getClockInUserInfo(LabClockInUserInfoWithBLOBs clockInUserInfoWithBLOBs) {
		
		ClockInUserInfoBO clockInUserInfoBO = new ClockInUserInfoBO();
		clockInUserInfoBO.setUser_name(clockInUserInfoWithBLOBs.getName());
		clockInUserInfoBO.setUser_privilege(clockInUserInfoWithBLOBs.getUserPrivilege());
		clockInUserInfoBO.setCard_enroll_time(ClockInUtils.timeToString(clockInUserInfoWithBLOBs.getCardEnrollTime()));
		clockInUserInfoBO.setFace_enroll_time(ClockInUtils.timeToString(clockInUserInfoWithBLOBs.getFaceEnrollTime()));
		clockInUserInfoBO.setFp_enroll_time(ClockInUtils.timeToString(clockInUserInfoWithBLOBs.getFpEnrollTime()));
		clockInUserInfoBO.setPassword_enroll_time(ClockInUtils.timeToString(clockInUserInfoWithBLOBs.getPasswordEnrollTime()));
		
		int binIndex =1;
		if(clockInUserInfoWithBLOBs.getEnrollPhoto()!=null) {
			clockInUserInfoBO.setUser_photo("BIN_"+binIndex);
			binIndex++;
		}
		
		List<EnrollData> enroll_data_array = new LinkedList<>();
		
		if(clockInUserInfoWithBLOBs.getEnrollFp1()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp1"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFp2()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp2"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFp3()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp3"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFp4()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp4"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFp5()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp5"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFp6()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp6"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFp7()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp7"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFp8()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp8"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFp9()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp9"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFp10()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("fp10"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollPassword()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("password"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollCard()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("card"), "BIN_"+binIndex));
			binIndex++;
		}
		if(clockInUserInfoWithBLOBs.getEnrollFace()!=null) {
			enroll_data_array.add( new EnrollData(backNumberMap.get("face"), "BIN_"+binIndex));
			binIndex++;
		}
		
		clockInUserInfoBO.setEnroll_data_array(enroll_data_array);
		return clockInUserInfoBO;
	}
	/**
	 * 生成向考勤机请求的二进制字节数组</br>
	 * @param user
	 * @param clockInUserInfoWithBLOBs
	 * @return
	 */
	public  byte[] getClockInUserInfoBytes(ClockInUserInfoBO user,LabClockInUserInfoWithBLOBs clockInUserInfoWithBLOBs)  {
		
		byte [] responseByte = new byte[0];
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			String jsonStr = objectMapper.writeValueAsString(user);
			//获取字符串的请求字节数组
			responseByte = CreateByteFromString(jsonStr, responseByte);
			//拼接byte[]
			if(clockInUserInfoWithBLOBs.getEnrollPhoto() != null) {
				//获取用户图片字节数组
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollPhoto();
				//获取用户图片的字节长度的字节数组
				byte[] photoLength = Utils.intToBytes(photo.length);
				//依次合并字节数组
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp1()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp1();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp2()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp2();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp3()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp3();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp4()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp4();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp5()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp5();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp6()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp6();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp7()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp7();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp8()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp8();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp9()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp9();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFp10()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFp10();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollPassword()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollPassword();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollCard()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollCard();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
			if(clockInUserInfoWithBLOBs.getEnrollFace()!=null) {
				byte[] photo = clockInUserInfoWithBLOBs.getEnrollFace();
				byte[] photoLength = Utils.intToBytes(photo.length);
				responseByte = Utils.byteMergerAll(responseByte,photoLength,photo);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return responseByte;
		
	}
	/**
	 * 处理考勤机登记数据请求</br>
	 * @param devId
	 * @param blkNo
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public  CmdDTO handleEnrollDataTransfer(String devId,String blkNo, ServletInputStream inputStream) throws IOException {
		//用户0 直接   多个 0  有1
		//是否存在多个请求传输数据
		Boolean existMultipartData = blkNo.equals("1");
	    byte[] byteArray = new byte[0];
		byteArray = Utils.readInputStream(inputStream);
		ClockInUserInfoBO user = new ClockInUserInfoBO();
		String transId = devId; //这里将考勤机id赋给任务id，是为了处理考勤机多用户同步更新时，便于获取该用户出当前考勤机的其他考勤机列表
		CmdDTO cmdDto = new CmdDTO();
		if(existMultipartData) {
			//将机器响应返回的byte[]分成字符串和二进制数组
			Object[] stringAnd1stBinary = getStringAnd1stBinaryFromResponseBuffer(byteArray,1);
     		String userJson = (String) stringAnd1stBinary[0];
     		//获取考勤机传输的json字符串
     		user = JsonUtils.jsonToClockInUserInfoBo(userJson);
     		if(user.getUser_id() == null) {
     			return null;
     		}
     		cmdDto.setCmd(KemiCmdEnum.ENROLL_DATA_TRANSFER.getMsg());
     		cmdDto.setResponseByte(byteArray);
     		cmdDto.setUser_id(Long.parseLong(user.getUser_id()));//此处保存的考勤机用户的登记号
     		cmdDto.setTrans_id(transId);
		}
		
		//multipartTransIdDataMap存在transId的key的数据就拼接字节数组
//		Boolean existMultipartTransIdDataMap = clockInManager.getMultipartTransIdDataMap().get(transId) != null;
		Boolean existMultipartTransIdData = redisUtil.exists(transId);
		if(existMultipartTransIdData) {
//			CmdDTO preCmdDto = clockInManager.getMultipartTransIdDataMap().get(transId);
			CmdDTO preCmdDto = (CmdDTO) redisUtil.get(transId);
			byte[] byteMergerAll = Utils.byteMergerAll(preCmdDto==null? new byte[0]:preCmdDto.getResponseByte(),byteArray);
			cmdDto.setResponseByte(byteMergerAll);
			cmdDto.setCmd(preCmdDto.getCmd());
			cmdDto.setUser_id(preCmdDto.getUser_id());
			cmdDto.setTrans_id(preCmdDto.getTrans_id());
		}else {
			cmdDto.setResponseByte(byteArray);
		}
		
		//是否传输结束
		Boolean over = blkNo.equals("0");
		if(over) {
			//multipartTransIdDataMap存在transId的key的数据就删除，表示多个请求，已到最后一个请求。
			if(existMultipartTransIdData) {
//				clockInManager.getMultipartTransIdDataMap().remove(transId);
				redisUtil.remove(transId);
			}else {
				//直接是一个请求就完成
				Object[] stringAnd1stBinary = getStringAnd1stBinaryFromResponseBuffer(byteArray,1);
	     		@SuppressWarnings("unused")
				ObjectMapper objectMapper = new ObjectMapper();
	     		String userJson = (String) stringAnd1stBinary[0];
	     		//获取考勤机传输的json字符串
	     		user = JsonUtils.jsonToClockInUserInfoBo(userJson);
	     		if(user.getUser_id() == null) {
	     			return null;
	     		}
	     		cmdDto.setCmd(KemiCmdEnum.ENROLL_DATA_TRANSFER.getMsg());
	     		cmdDto.setResponseByte(byteArray);
	     		cmdDto.setUser_id(Long.parseLong(user.getUser_id()));
	     		cmdDto.setTrans_id(transId);
				
			}
		}else {
			//新增或者覆盖到MultipartTransIdDataMap中
//			clockInManager.getMultipartTransIdDataMap().put(transId, cmdDto);
			redisUtil.putMultipartTransIdData(transId, cmdDto);
		}
		return cmdDto;
	}
	
    //格式化打卡数据
    public static ClockIn parse(ServletInputStream sis){
        try {
        	 String str = "";
        	 byte[] binary = Utils.readInputStream(sis);
             if (binary.length < 4) {
            	 return null;
             }

             int lenText = Utils.bytesToInt(binary, 0);//获取字符串长度
             if (lenText > binary.length - 4) {
            	 return null;
             }
             if (lenText == 0){
            	 str = "";
             }else{
                 if (binary[4 + lenText - 1] == 0)  
                	 str = new String(binary, 4, lenText - 1,"UTF-8");
                 else
                	 str = new String(binary, 4, lenText,"UTF-8");
             }
             return JsonUtils.getMapper().readValue(str, ClockIn.class);
        } catch (IOException e) {
            return  null;
        }
    }
    
    //格式化打卡数据
    public  String getClockInMessage(ServletInputStream sis,String devId){
        try {
        	 String str = "";
        	 byte[] binary = Utils.readInputStream(sis);
             if (binary.length < 4) {
            	 return null;
             }

             int lenText = Utils.bytesToInt(binary, 0);//获取字符串长度
             if (lenText > binary.length - 4) {
            	 return null;
             }
             if (lenText == 0){
            	 str = "";
             }else{
                 if (binary[4 + lenText - 1] == 0) 
                	 str = new String(binary, 4, lenText - 1,"UTF-8");
                 else
                	 str = new String(binary, 4, lenText,"UTF-8");
             }
             ClockIn clockIn = JsonUtils.getMapper().readValue(str, ClockIn.class);
             LabClockInRecord clockInRecord = new LabClockInRecord();
             
             if(clockIn!=null) {
            	 clockInRecord.setClockingTime(stringToTime(clockIn.getIo_time()));
                 clockInRecord.setEnrollId(Long.parseLong(clockIn.getUser_id()));
                 clockInRecord.setClockinMode((byte)clockIn.getIo_mode());
                 clockInRecord.setCreateTime(new Date());
                 clockInRecord.setVerifyMode((byte)clockIn.getVerify_mode());
                 clockInRecord.setClockinId(devId);
             }else {
				return null;
			}
             return JsonUtils.getMapper().writeValueAsString(clockInRecord);
        } catch (IOException e) {
            return  null;
        }
    }
    /**
     * 转换成用于提交到考勤机的byte[]</br>
     * @param userInfoBo 要转换的ClockInUserInfoBO</br>
     * @return
     */
    public byte[] getBytesByClockInUserInfoBO(ClockInUserInfoBO userInfoBo) {
    	ObjectMapper objectMapper = new ObjectMapper();
    	byte [] responseByte = new byte[0];
		String str;
		try {
			str = objectMapper.writeValueAsString(userInfoBo);
			responseByte = CreateByteFromString(str, responseByte);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return responseByte;
    }
    /**
     * 获取考勤机每次请求时的锁的key，反正重复请求造成的重复操作</br>
     * @param clockinId 考勤机Id</br>
     * @param blkNo 当前请求标识</br>
     * @param transId 当前请求任务id</br>
     * @return
     */
    public String getRequestLockKey(String clockinId,String blkNo,String transId) {
    	StringBuffer  buffer  = new StringBuffer();
    	buffer.append(clockinId).append(blkNo).append(transId).append(RedisLock.LOCK_SUFFIX);
    	return buffer.toString();
    }
    
    /**
     * 添加到响应队列，字节数组不包含指纹，密码，人脸，Id卡等二进制数据</br>
     */
	public void addReqCmdNoBinaryByMachineList(Map<String,Object> paramMap,String kemiCmd) {
		
		List<LabClockInMachine> clockInMachineList  = (List<LabClockInMachine>) paramMap.get("clockInMachineList");
		Integer operateCount = (Integer) paramMap.get("operateCount");
		String userOperateWorkKey = (String) paramMap.get("userOperateWorkKey");
		String str;
		try {
			for (LabClockInMachine machine : clockInMachineList) {
				
				ConcurrentLinkedQueue< CmdDTO> cmdQueue = new ConcurrentLinkedQueue<>();
				ClockInUserInfoBO user = new ClockInUserInfoBO ();
				user.setUser_id(String.valueOf(machine.getMachineUserId()));
				str = JsonUtils.getMapper().writeValueAsString(user);
				byte [] responseByte = new byte[0];
				responseByte = CreateByteFromString(str, responseByte);
				cmdQueue.add(new CmdDTO(setAndGetTransId(machine.getLabId()), responseByte,kemiCmd,machine.getMachineUserId(),userOperateWorkKey));
				redisUtil.putCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, machine.getClockinId(), cmdQueue);
				operateCount++;
			}
			paramMap.put("operateCount", operateCount);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
     * 增加删除考勤机的请求队列</br>
     */
	public void addReqCmdByDeleteMachine(Map<String,Object> paramMap,String kemiCmd) {
		
		String clockId  = (String) paramMap.get("clockId"); //考勤机id
		Integer operateCount = (Integer) paramMap.get("operateCount"); //用户操作考勤机任务需要向考勤机发送的指令个数
		String userOperateWorkKey = (String) paramMap.get("userOperateWorkKey");
		String labId = (String) paramMap.get("labId");
		String operatorId = (String) paramMap.get("operatorId");
		
		ConcurrentLinkedQueue< CmdDTO> cmdQueue = new ConcurrentLinkedQueue<>();
		if(KemiCmdEnum.DELETE_MACHINE.getMsg().equals(kemiCmd)) { //删除考勤机，需要同时执行清除注册数据，和清除考勤记录两个命令
			//添加清空考勤机登记数据指令队列
			cmdQueue.add(new CmdDTO(setAndGetTransId(Long.valueOf(labId)), null,KemiCmdEnum.CLEAR_ENROLL_DATA.getMsg(),Long.valueOf(operatorId),userOperateWorkKey));
			//添加清空考勤机考勤记录指令队列
			cmdQueue.add(new CmdDTO(setAndGetTransId(Long.valueOf(labId)), null,KemiCmdEnum.CLEAR_LOG_DATA.getMsg(),Long.valueOf(operatorId),userOperateWorkKey));
			operateCount=operateCount+2; //实际就是只有2个需要发送的指令
		}else {
			//添加考勤机请求队列
			cmdQueue.add(new CmdDTO(setAndGetTransId(Long.valueOf(labId)), null,kemiCmd,null,userOperateWorkKey));
			operateCount++; //依次累加
		}
		//新增到考勤机命令请求队列中
		redisUtil.putCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, clockId, cmdQueue);
		paramMap.put("operateCount", operateCount);
		
	}
	
	/**
	 * 添加考勤机请求队列通过(单个考勤机中的)考勤机用户列表</br>
	 * @param paramMap
	 * 		String[] clockinIdArray 考勤机id数组</br>
	 * 		List<LabClockInMachineUser> clockMachineUserList  考勤机用户列表</br>
	 * 		String operatorId   操作人id</br>
	 * 		List<Integer> userIdList  用户id列表</br>
	 * 		String userOperateWorkKey  用户操作考勤机任务key </br>
	 * 		String labId  实验室Id 用户构建考勤机请求的任务id</br>
	 * 		Integer operateCount  用户操作考勤机的发送的指令次数 </br>
	 * @param kemiCmd 向考勤机发送的指令</br>
	 */
	public void addReqCmdByMachineUserList(HashMap<String, Object> paramMap, String kemiCmd) {
		String clockinId  = (String) paramMap.get("clockinId");
		Integer operateCount = (Integer) paramMap.get("operateCount");
		String userOperateWorkKey = (String) paramMap.get("userOperateWorkKey");
		String labId = (String) paramMap.get("labId");
		String operatorId = (String) paramMap.get("operatorId");
		ConcurrentLinkedQueue< CmdDTO> cmdQueue = new ConcurrentLinkedQueue<>();
		List<LabClockInMachineUser> clockMachineUserList = (List<LabClockInMachineUser>) paramMap.get("clockMachineUserList");
		String str ="";
		try {
			for (LabClockInMachineUser labClockInMachineUser : clockMachineUserList) {
				ClockInUserInfoBO user = new ClockInUserInfoBO ();
				//考勤机中保存的其实考勤机用户的登记号，并不是用户本身的userId
				user.setUser_id(String.valueOf(labClockInMachineUser.getEnrollId()));
				str = JsonUtils.getMapper().writeValueAsString(user);
				byte [] responseByte = new byte[0];
				responseByte = CreateByteFromString(str, responseByte);
				cmdQueue.add(new CmdDTO(setAndGetTransId(Long.valueOf(labId)), responseByte,kemiCmd,labClockInMachineUser.getId(),userOperateWorkKey));
				operateCount++;
			}
			redisUtil.putCmdQueue(ClockInManager.REQ_CMD_QUEUE_KEY, clockinId, cmdQueue);
			paramMap.put("operateCount", operateCount);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	 /**
     * 封装课程学生分数的考勤状态
     * @param studentScore  考勤对应的成绩表
     * @param clockIn 考勤信息
     * @return
     */
    public  static ScheduleStudentScore clockIn(ScheduleStudentScore studentScore, LabClockInRecord clockInRecord){
        //如果是已经签过的，不能再重签 返回null
    	Integer signin = studentScore.getSignin();
    	int code = SigninEnum.NORMAL.getCode();
    	
        //标准上课时间
        Date stipulateSgininTime = studentScore.getStipulateSgininTime();
        /*判断是否相差两个小时 相差两个小时不能打卡 返回 null
        if( DateUtil.between(stipulateSgininTime, clockInRecord.getClockingTime(), DateUnit.HOUR)>=2L){
            return  null;
        }
        //上课迟到
        if(stipulateSgininTime.getTime() - clockInRecord.getClockingTime().getTime()<0) {
        	
        }else { //没有迟到
        	
        }
        */
        //如果签到时间大于标准考勤时间 则 判定为迟到  否则考勤正常
        if(clockInRecord.getClockingTime().getTime() > stipulateSgininTime.getTime()){
            studentScore.setSignin(SigninEnum.DELAY.getCode());
        }else{
            studentScore.setSignin(SigninEnum.NORMAL.getCode());
        }
        studentScore.setSigninTime(clockInRecord.getClockingTime());
        studentScore.setUpdateTime(DateUtil.date());
        return  studentScore;
    }
	
	
}
