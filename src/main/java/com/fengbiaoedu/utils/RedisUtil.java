package com.fengbiaoedu.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.kemi.ClockInManager;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.redis.RedisLock;

import cc.wulian.ihome.wan.entity.DeviceInfo;

/**
 * Created by Administrator on 2018/5/17.
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String EQUPREFIX = "equ_up";
    //写入数据
    public boolean set(final String key,Object value){
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    //写入设置超时时间的key
    public boolean set(final String key,Object value,Long timeout){
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, timeout, TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    //读取数据
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        if(result==null){
            return null;
        }
        return result;
    }
    //删除数据

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }


    //关于设备操作的一些实用方法 其中key 是 前缀 + wgid+devId

    /**
     * 根据网关id 获取所有指定的所有设备
     * @param gwId 网关id
     * @return 返回设备信息
     */
    public Collection<DeviceInfo> getDeviceInfoByGwId(String gwId){
        String pattern = EQUPREFIX+"-"+gwId+"*";
        Set<String> keys = redisTemplate.keys(pattern);
        Collection<DeviceInfo> deviceInfos = new HashSet<>();
        ValueOperations<Serializable,DeviceInfo> operations =  redisTemplate.opsForValue();
        for (String key :keys) {
            DeviceInfo deviceInfo = operations.get(key);
            if (deviceInfo!=null){
                deviceInfos.add(deviceInfo);
            }
        }
        return deviceInfos;
    }

    /**
     * 获取所有设备
     * @return 返回设备信息
     */
    public Collection<DeviceInfo> getDeviceInfos(){
        //被解析成String
        Set<String> keys = redisTemplate.keys(EQUPREFIX+"*");
        Collection<DeviceInfo> deviceInfos = new HashSet<>();
        ValueOperations<Serializable,DeviceInfo> operations =  redisTemplate.opsForValue();
        for (String key :keys) {
            DeviceInfo deviceInfo = operations.get(key);
            if (deviceInfo!=null){
                deviceInfos.add(deviceInfo);
            }
        }
        return deviceInfos;
    }
    /**
     * 根据设备id 获取指定设备
     * @param gwId 网关id
     * @param devId 设备id
     * @return
     */
    public DeviceInfo getEuqByGwIdAndDevId(String gwId,String devId){
        if(StringUtils.isEmpty(gwId)|| StringUtils.isEmpty(devId))return null;
        String key = generateKey(gwId,devId);
        ValueOperations<Serializable,DeviceInfo> operations =  redisTemplate.opsForValue();
        DeviceInfo deviceInfo = operations.get(key);
        if(deviceInfo==null){
            return null;
        }else{
            return deviceInfo;
        }
    }
    /**
     * 添加、修改设备信息
     * @param deviceInfo  设备信息
     * @return
     */
    public boolean addAndUpdateEuq(DeviceInfo deviceInfo){
        if(!StringUtils.isEmpty(deviceInfo.getDevID()) && !StringUtils.isEmpty(deviceInfo.getGwID())){
                String key = generateKey(deviceInfo.getGwID(),deviceInfo.getDevID());
                //判断设备是否存在
                ValueOperations<Serializable,Object> operations= redisTemplate.opsForValue();
                operations.set(key,deviceInfo);
                //TODO存储到数据库
            return true;
        }
        return false;
    }

    /**
     * 清除指定设备
     * @param gwId 网关id
     * @param devId 设备id
     * @return false：删除失败  truw：删除成功
     */
    public boolean removeEuqByGwIdAndDevId(String gwId,String devId){
        if(StringUtils.isEmpty(gwId)|| StringUtils.isEmpty(devId))return false;
        String key = generateKey(gwId,devId);
        if(exists(key)) redisTemplate.delete(key);
        return true;
    }

    //生成对应设备的key
    private String generateKey(String gwId,String devId){
        return EQUPREFIX+ "-"+ gwId+"-"+devId;
    }
    /**
     * 添加修改请求命令队列</br>
     * @param clockinId 考勤机id</br>
     * @param cmdQueue 该考勤机请求命令队列</br>
     * @return
     */
    public boolean addAndUpdateCmdTransIdDataMap(String prefix,String clockinId,ConcurrentLinkedQueue< CmdDTO> cmdQueue) {
		if(StringUtils.isEmpty(clockinId) || cmdQueue==null ) {
			return false;
		}
        ValueOperations<Serializable,Object> operations= redisTemplate.opsForValue();
        String key = generateKeyOfClockIn(prefix, clockinId, "");
        operations.set(key,cmdQueue);
    	return true;
    }
    
    /**
     * 获取所有设备
     * @return 返回设备信息
     */
    public ConcurrentHashMap<String,ConcurrentLinkedQueue<CmdDTO>> getCmdTransIdDataMap(String prefix){
        //被解析成String
        Set<String> keys = redisTemplate.keys(prefix+"*");
        ConcurrentHashMap<String,ConcurrentLinkedQueue<CmdDTO>> map = new ConcurrentHashMap<>();
        ValueOperations<Serializable,Object> operations =  redisTemplate.opsForValue();
        for (String key :keys) {
        	ConcurrentLinkedQueue<CmdDTO> queue = (ConcurrentLinkedQueue<CmdDTO>) operations.get(key);
        }
        return map;
    }
    /**
     * 
     * @param prefix 前缀</br>
     * @param clockinId
     * @param transId
     * @return
     */
    public   String generateKeyOfClockIn(String prefix,String clockinId,String transId) {
    	return prefix+"-"+clockinId+transId;
    }
    
    /**
     * 判断是否存在考勤机命令响应队列</br>
     * @param devId
     * @return
     */
	public boolean existRepCmdQueue(String devId) {
		String key = generateKeyOfClockIn(ClockInManager.REP_CMD_QUEUE_KEY, devId, "");
		boolean exists = exists(key);
		return exists;
		
	}
	
	/**
	 * 判断是否存在考勤机请求命令队列</br>
	 * @param devId
	 * @return
	 */
	public  boolean existReqCmdQueue(String devId) {
		String key = generateKeyOfClockIn(ClockInManager.REQ_CMD_QUEUE_KEY, devId, "");
		return exists(key);
		
	}
	/**
	 * 获取考勤机命令队列</br>
	 * @param prefix
	 * @param devId
	 * @return
	 */
	public ConcurrentLinkedQueue<CmdDTO> getCmdQueue(String prefix,String devId) {
		String key = generateKeyOfClockIn(prefix, devId, "");
		JSONArray jsonArray = (JSONArray) get(key);
		if(jsonArray==null) {
			return null;
		}
		List<CmdDTO> parseArray = JSONObject.parseArray(jsonArray.toJSONString(), CmdDTO.class);
		ConcurrentLinkedQueue<CmdDTO> concurrentLinkedQueue = new ConcurrentLinkedQueue<CmdDTO>();
		concurrentLinkedQueue.addAll(parseArray);
		return concurrentLinkedQueue;
	}
	
	public void removeCmdQueue(String prefix,String devId) {
		String key = generateKeyOfClockIn(prefix, devId, "");
		remove(key);
	}
	/**
	 * 更新队列
	 * @param prefix
	 * @param devId
	 * @param queue
	 */
	public void updateCmdQueue(String prefix,String devId,ConcurrentLinkedQueue<CmdDTO> queue) {
		String key = generateKeyOfClockIn(prefix, devId, "");
		remove(key);
		set(key, queue);
	}
	
	/**
	 * 添加考勤机命令队列</br>
	 * @param prefix  前缀</br>
	 * @param devId   考勤机设备id</br>
	 * @param queue 要添加的队列</br>
	 */
	public void putCmdQueue(String prefix,String devId,ConcurrentLinkedQueue<CmdDTO> queue) {
		String key = generateKeyOfClockIn(prefix, devId, "");
		if(exists(key)) {
			ConcurrentLinkedQueue<CmdDTO> cmdQueue = getCmdQueue(prefix, devId);
			cmdQueue.addAll(queue);
			remove(key);
			set(key, cmdQueue);
			
			queue =null;
			cmdQueue =null;
		}else {
			set(key,queue);
		}
		
	}
	
	/**
	 * 添加考勤机一次请求，存在多次响应的数据</br>
	 * @param transId
	 * @param cmdDTO
	 */
	public void putMultipartTransIdData(String transId,CmdDTO cmdDTO) {
		remove(transId);
		set(transId, cmdDTO);
	}
    
    @SuppressWarnings("unchecked")
	public void pullMap(String key,Map<String, ?> map) {
    	if(redisTemplate.hasKey(key)) {
    		redisTemplate.delete(key);
    	}
    	redisTemplate.opsForHash().putAll(key, map);
    }
    /**
     * key存在，返回flase，不存在创建，返回true</br>
     * @param key 
     * @param value
     * @return
     */
    public boolean setNX(final String key, final String value) {
        return redisTemplate.opsForValue().setIfAbsent(key,value);
    }
    
    /**
     * 判断是否存在考勤机请求队列的key</br>
     * @param clockinId
     * @return
     */
    public boolean isExistsReqCmdQueueLock(String clockinId) {
    	
		return exists(ClockInManager.REQ_CMD_QUEUE_KEY+clockinId+RedisLock.LOCK_SUFFIX);
    	
    }
    
    /**
     * 生成考勤机请求队列的key</br>
     * @param clockinId
     * @return
     */
    public String getReqQueueLockKey(String clockinId) {
    	return ClockInManager.REQ_CMD_QUEUE_KEY+clockinId+RedisLock.LOCK_SUFFIX;
    }
    
    /**
     * 生成考勤机响应队列的key</br>
     * @param clockinId
     * @return
     */
    public String getRepQueueLockKey(String clockinId) {
    	return ClockInManager.REP_CMD_QUEUE_KEY+clockinId+RedisLock.LOCK_SUFFIX;
    }
    
    /**
     * 生成用户操作任务的key</br>
     * 主要用来前端回调的</br>
     * @param cmdMsg
     * @param operatorId
     * @param vcoocUserId
     * @return
     */
	public String getUserOperateWorkKey(String cmdMsg,String operatorId, String vcoocUserId) {
		return cmdMsg.split(",")[0]+","+operatorId+","+vcoocUserId;
		
	}
	/**
	 * 判断是否存在用户操作任务的key</br>
	 * @param userOperateWorkKey
	 * @return
	 */
	public Boolean existUserUnCompletedWork(String userOperateWorkKey) {
		return exists(userOperateWorkKey);
		
	}
    
}
