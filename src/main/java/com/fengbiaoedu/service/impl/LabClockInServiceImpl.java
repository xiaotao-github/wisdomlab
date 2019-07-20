package com.fengbiaoedu.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fengbiaoedu.bean.LabClockInMachine;
import com.fengbiaoedu.bean.LabClockInMachineUser;
import com.fengbiaoedu.bean.LabClockInMachineUserExample;
import com.fengbiaoedu.bean.LabClockInMachineUserExample.Criteria;
import com.fengbiaoedu.bean.LabClockInRecord;
import com.fengbiaoedu.bean.ScheduleStudentScore;
import com.fengbiaoedu.config.ClockinConfig;
import com.fengbiaoedu.exception.ClockInException;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.kemi.cmd.BaseCmdStrategy;
import com.fengbiaoedu.kemi.cmd.CmdStrategyFactory;
import com.fengbiaoedu.mapper.LabClockInMachineMapper;
import com.fengbiaoedu.mapper.LabClockInMachineUserMapper;
import com.fengbiaoedu.mapper.LabClockInMapper;
import com.fengbiaoedu.mapper.LabClockInRecordMapper;
import com.fengbiaoedu.pojo.ClockIn;
import com.fengbiaoedu.service.ClockInService;
import com.fengbiaoedu.utils.ClockInUtils;
import com.fengbiaoedu.utils.ClockInUtils1;

import cn.hutool.core.date.DateUtil;

/**
 * 考勤服务类
 */
@Service
public class LabClockInServiceImpl implements ClockInService {

    @Autowired
    private LabClockInMapper clockInMapper;
    
    @Autowired
    private LabClockInMachineUserMapper clockInMachineUserMapper;
    
    @Autowired
    private LabClockInRecordMapper clockInRecordMapper;
    
    @Autowired
    private LabClockInMachineMapper clockInMachineMapper;
    @Autowired
    private ClockinConfig clockinConfig;


    //获取签到人最近的课程安排信息 然后根据

    public void updateCloking(ClockIn clockIn,String devId){
        if(clockIn==null || clockIn.getUser_id()==null || clockIn.getIo_time()==null){
            throw new ClockInException(400,"打卡信息不全");
        }
        //获取考勤user_id
        LabClockInMachineUserExample example = new LabClockInMachineUserExample();
        Criteria criteria = example.createCriteria();
        criteria.andClockinIdEqualTo(devId);
        criteria.andEnrollIdEqualTo(Long.parseLong(clockIn.getUser_id()));
        List<LabClockInMachineUser> clockInUserList = clockInMachineUserMapper.selectByExample(example);
        if(clockInUserList !=null && !clockInUserList.isEmpty()) {
        	clockIn.setUser_id(clockInUserList.get(0).getUserId().toString());
        }
        ScheduleStudentScore studentScore = clockInMapper.getByIdAndTime(DateUtil.parse(clockIn.getIo_time(),"yyyyMMddHHmmss"),clockIn.getUser_id());
        if(studentScore == null || studentScore.getStipulateSgininTime() == null){
            throw new ClockInException(302,"无实验安排成绩单,无法打卡");
        }else{
            studentScore = ClockInUtils1.clockIn(studentScore,clockIn);
            if(studentScore!=null){
                clockInMapper.save(studentScore);
            }
        }
    }
    /**
     * 处理考勤机请求响应信息或者考勤用户传输登记信息</br>
     * 根据CmdDTO中携带cmd，反射相应的处理类动态处理</br>
     * //cmd 由两部分组成，1为指令，2为指令处理类名，中间用","隔开
     */
	@Override
	@Transactional
	public void processCmd(CmdDTO repCmdDTO, Boolean result) {
		//反射动态生成相应的处理类
		BaseCmdStrategy  cmdStrategy = new CmdStrategyFactory().createCmdStrategy(repCmdDTO.getCmd());
		HashMap<String,Object> repProcessMap = new HashMap<>();
		repProcessMap.put("cmdDto", repCmdDTO);
		//考勤机处理结果
		repProcessMap.put("result", result);
		cmdStrategy.processResponse(repProcessMap);
		
	}
	/**
	 * 新增考勤记录
	 */
	@Override
	public void insertClokInRecord(LabClockInRecord clockInRecord) {
		//获取user_id,userName
		LabClockInMachineUserExample example = new LabClockInMachineUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andClockinIdEqualTo(clockInRecord.getClockinId());
		criteria.andEnrollIdEqualTo(clockInRecord.getEnrollId());
		List<LabClockInMachineUser> userList = clockInMachineUserMapper.selectByExample(example);
		LabClockInMachineUser user = new LabClockInMachineUser();
		if(userList==null || userList.isEmpty()) {
			return;
		}
		user =userList.get(0);
		clockInRecord.setUserName(user.getUserName());
		clockInRecord.setUserId(user.getUserId());
		//创建考勤记录
		clockInRecordMapper.insertSelective(clockInRecord);
		
	}
	
	/**
	 * 新增考勤机
	 */
	@Override
	public void insertClockinMachine(String clockinId) {
		
		LabClockInMachine machine = new LabClockInMachine();
		machine.setClockinId(clockinId);
		machine.setIsDeleted(true);
		clockInMachineMapper.insert(machine);
	}
	
	//获取签到人最近的课程安排信息 然后修改考勤状态
	@Override
	public void updateCloking(LabClockInRecord clockInRecord) {
		
		if(clockInRecord==null || clockInRecord.getUserId()==null || clockInRecord.getClockingTime()==null){
            //throw new ClockInException(400,"打卡信息不全");
			return;
        }
		//这里获取的是与当前考勤时间相差不超过89分钟的两个学生上课成绩分数记录
        List<ScheduleStudentScore> list = clockInMapper.getByIdAndClockinTime(ClockInUtils.timeToString(clockInRecord.getClockingTime()),clockInRecord.getUserId(),clockinConfig.getClassMinutes()-1);
        if(list==null || list.isEmpty()) {
        	return ;
        }
        ScheduleStudentScore studentScore = null;
        //存在连续上课的情况，且当前考勤时间刚好前一节课的中间时间
        if(list.size() ==2) {
        	//第一个课的指定上课毫秒数
        	Long timeOf1 = list.get(0).getStipulateSgininTime().getTime();
        	//第二个课的指定上课毫秒数
        	Long timeOf2 = list.get(1).getStipulateSgininTime().getTime();
        	
        	//第一个比第二个上课时间早
        	if(timeOf1<timeOf2) {
        		//第一个课加上上课的分钟数，如果考勤时间大于第一个课，说明是第二个课考勤，反之是第一个
        		if(timeOf1+1000*60*clockinConfig.getClassMinutes()<clockInRecord.getClockingTime().getTime()) {
        			studentScore =list.get(1);
        		}else {
        			studentScore =list.get(0);
        		}
        	}else {  //第二个比第一个上课时间早
        		//第二个课加上上课的分钟数，如果考勤时间大于第二个课，说明是第一个课考勤，反之是第二个
        		if(timeOf2+1000*60*clockinConfig.getClassMinutes()<clockInRecord.getClockingTime().getTime()) {
        			studentScore =list.get(0);
        		}else {
        			studentScore =list.get(1);
        		}
        	}
        }else { 
        	studentScore =list.get(0);
        }
        //判断是否在要上课的实验室打卡
        if(!clockInRecord.getClockinId().equals(studentScore.getClockinId())) {
        	return;
        }
        if(studentScore.getSignin()!=null) { //已考勤，无需重复考勤
        	return;
        }
    	studentScore = ClockInUtils.clockIn(studentScore, clockInRecord);
        clockInMapper.save(studentScore);
    }
		



}
