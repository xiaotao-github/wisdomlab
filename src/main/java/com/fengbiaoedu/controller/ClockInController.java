package com.fengbiaoedu.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fengbiaoedu.config.ClockinConfig;
import com.fengbiaoedu.enums.KemiCmdEnum;
import com.fengbiaoedu.kemi.ClockInManager;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.mq.ActiveMQService;
import com.fengbiaoedu.redis.RedisLock;
import com.fengbiaoedu.service.ClockInService;
import com.fengbiaoedu.utils.ClockInUtils;
import com.fengbiaoedu.utils.JsonUtils;
import com.fengbiaoedu.utils.RedisUtil;

/**
 * 请求和响应考勤机控制器
 * 根据考勤机发送过来的请求头中的request_code，响应考勤机。
 * 注意包括 
 * 		1考勤打卡
 * 		2考勤机实时登记数据传输
 * 		3向考勤机发送相应的指令
 * 		4处理发送指令后的考勤机的操作结果，进行响应，直到数据传输完成。
 * 注意：具体的指令可以看 com.fengbiaoedu.enums.KemiCmdEnum.java中的相关指令。
 * @author wucb
 *
 */
@RestController
public class ClockInController   {
	
    @Autowired
    private ClockInService clockInService;
    
    @Autowired
    private ActiveMQService activeMQService;
    
    @Autowired
    private ClockInUtils clockInUtils;
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Autowired
    private RedisLock redisLock;
    
    @Autowired
    private ClockinConfig clockinConfig;
    
    /**
     * 接收考勤机的信息
     * @param req
     * @param rep
     * @throws Exception 
     */
    @SuppressWarnings("unused")//屏蔽java编译中的一些警告信息
    @RequestMapping("/")
    public void receiveMsg(HttpServletRequest req, HttpServletResponse rep) throws Exception  {
    	//考勤机id
    	String clockinId = req.getHeader("dev_id");
    	
    	/*机器发送的块的序号。
    	如果结果数据大分割成几个块
    	而一次传输一个块。
    	第一块的序号是1，
    	第二是2，最后快的序号是0。
    	*/
    	String blkNo = req.getHeader("blk_no");
    	//考勤机任务识别号
    	String transId = req.getHeader("trans_id");
    	
    	//判断是否有新进来的考勤机,有的话添加到ClockInManager.CLOCKIN_MACHINE_MAP，和添加到数据库
    	if(!ClockInManager.CLOCKIN_MACHINE_MAP.containsKey(clockinId)) {
    		ClockInManager.CLOCKIN_MACHINE_MAP.put(clockinId, false);
    		clockInService.insertClockinMachine(clockinId);
    	}
    	
    	//保存在redis，防止考勤机发送相同请求，重复操作的key
    	String key =clockInUtils.getRequestLockKey(clockinId, blkNo, transId);
    	//向考勤机发送指令的队列的锁的超时时间
    	Long timeReqQueue = System.currentTimeMillis()+clockinConfig.getClockinMachineLockTimeOut();
    	//向考勤机响应的队列的锁的超时时间
    	Long timeRepQueue = System.currentTimeMillis()+clockinConfig.getClockinMachineLockTimeOut();
    	//防止考勤机发送相同请求的key的锁的超时时间
    	Long timeKey = System.currentTimeMillis() +clockinConfig.getClockinMachineLockTimeOut();
    	
     	//用户打卡
        if(KemiCmdEnum.CLOCK_IN.getMsg().equals(req.getHeader("request_code"))){
        	///当前不是重复请求时，进行处理
        	if(redisLock.lock(key, String.valueOf(timeKey))){
        		//格式化打卡数据
        		String clockInRecord = clockInUtils.getClockInMessage(req.getInputStream(), clockinId);
            	//入队
                if(clockInRecord!=null){
                	//加入消息队列，考勤打卡异步处理
                	activeMQService.sendClockInMessage(clockInRecord);
                	rep.setHeader("Content-type","application/octet-stream");
                	rep.setHeader("response_code", "OK");
                }
                //解锁
                redisLock.unLock(key, String.valueOf(timeKey));
        	}
            return;
        }
    	
    	//考勤机用户登记信息
        //cmd 由两部分组成，1为指令，2为指令处理类名，中间用","隔开
        if(KemiCmdEnum.ENROLL_DATA_TRANSFER.getMsg().split(",")[0].equals(req.getHeader("request_code"))) {
        	
        	///当前不是重复请求时，进行处理
        	if(redisLock.lock(key, String.valueOf(timeKey))){
        		//处理考勤机实时登记数据，获取CmdDTO，用于考勤机请求响应，mq消息传递的中间对象
        		CmdDTO repCmdDTO = clockInUtils.handleEnrollDataTransfer(clockinId,blkNo,req.getInputStream());
             	if(repCmdDTO!=null ) {
             		//根据请求指令处理响应信息
             		if("0".equals(blkNo)) { //如果用户登记消息发送完毕
             			//加入消息队列，异步处理登记数据传输
             			activeMQService.sendEnroolData(JsonUtils.getMapper().writeValueAsString(repCmdDTO));
            		}
    				rep.setHeader("Content-type","application/octet-stream");
    				//响应ok
    				rep.setHeader("response_code", "OK");
    			}
             	//解锁
             	redisLock.unLock(key, String.valueOf(timeKey));
        	}
         	return;
        }
        
        //处理考勤机响应的请求
        if(!req.getHeader("request_code").equals("receive_cmd") && redisUtil.existRepCmdQueue(req.getHeader("dev_id")) ) {
        	//当如果有不存在操作响应队列或者当前不是重复请求时，进行处理
        	if(redisLock.lock(key, String.valueOf(timeKey))
        			&& redisLock.lock(redisUtil.getRepQueueLockKey(clockinId), String.valueOf(timeRepQueue))){
        		//处理考勤机响应请求，获取CmdDTO，用于考勤机请求响应，mq消息传递的中间对象
        		CmdDTO repCmdDTO = clockInUtils.handleRepCmd(clockinId,transId,blkNo,req.getInputStream());
   	       	 	if(repCmdDTO!=null ) {
   	       	 		//根据请求指令处理响应信息
   	       	 		if("0".equals(blkNo)) { //如果响应消息发送完毕
   	       	 			//考勤机是否处理成功
   	       	 			Boolean result = "OK".equals(req.getHeader("cmd_return_code"));
   	       	 			repCmdDTO.setResult(result);
   	       	 			repCmdDTO.setClockinId(clockinId);
   	       	 			//异步处理考勤机响应结果
   	       	 			activeMQService.sendClockMachineRequestResultMessage(JsonUtils.getMapper().writeValueAsString(repCmdDTO));
   	       	 		}
   				  
   					rep.setHeader("Content-type","application/octet-stream");
   					//响应ok
   					rep.setHeader("response_code", "OK");
   	       	 	}
        	}
        	//解锁
        	redisLock.unLock(redisUtil.getRepQueueLockKey(clockinId), String.valueOf(timeRepQueue));
        	redisLock.unLock(key, String.valueOf(timeKey));
        	return;
        }  
        
     	//向考勤机发送指令
        if(req.getHeader("request_code").equals("receive_cmd") && redisUtil.existReqCmdQueue(req.getHeader("dev_id")) ) {
        	
        	///当如果有不存在操作请求队列和响应队列或者当前不是重复请求时，进行处理
        	if(redisLock.lock(redisUtil.getReqQueueLockKey(clockinId), String.valueOf(timeReqQueue))
        			&& redisLock.lock(redisUtil.getRepQueueLockKey(clockinId), String.valueOf(timeRepQueue))
        			&& redisLock.lock(key, String.valueOf(timeKey))){
        	 	CmdDTO repCmdDTO = clockInUtils.handleReqCmd(clockinId);
    			
           	 	if(repCmdDTO!=null) {
           	 		
           	 		//根据请求指令处理响应信息
           	 		rep.setHeader("Content-type","application/octet-stream");
           	 		rep.setHeader("response_code", "OK");
    				
           	 		//获取指令
           	 		String cmd = repCmdDTO.getCmd();  
           	 		//cmd 由两部分组成，1为指令，2为指令处理类名，中间用","隔开
           	 		rep.setHeader("cmd_code",cmd.split(",")[0]);
           	 		rep.setHeader("trans_id",repCmdDTO.getTrans_id());
           	 		if(repCmdDTO.getResponseByte()!=null) {
           	 			rep.setHeader("Content-Length",repCmdDTO.getResponseByte().length+"");
           	 			ServletOutputStream outputStream = rep.getOutputStream();
           	 			outputStream.write(repCmdDTO.getResponseByte());
           	 		}
           		 
           	 		//特殊指令，重启机器
           	 		if(cmd.split(",")[0].equals("RESET_FK")) {
           	 			rep.setHeader("response_code", "RESET_FK");
           	 		}
           	 		rep.flushBuffer();
           	 	}
        	}
        	//解锁
        	redisLock.unLock(redisUtil.getReqQueueLockKey(clockinId), String.valueOf(timeReqQueue));
        	redisLock.unLock(redisUtil.getRepQueueLockKey(clockinId), String.valueOf(timeRepQueue));
        	redisLock.unLock(key, String.valueOf(timeKey));
        	return;
       	
        }
        
    }
   
}
