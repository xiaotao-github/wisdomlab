package com.fengbiaoedu.service;

import com.fengbiaoedu.bean.LabClockInRecord;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.pojo.ClockIn;

/**
 * 考勤服务接口
 */
public interface ClockInService {

    //更新考勤信息
    void updateCloking(ClockIn clockIn, String devId);
    
    /**
     * 新增考勤记录
     * @param clockInRecord 考勤机记录
     */
    void insertClokInRecord(LabClockInRecord clockInRecord);
    
    /**
     * 处理考勤机请求响应信息或者考勤用户传输登记信息</br>
     * 根据CmdDTO中携带cmd，反射相应的处理类动态处理</br>
     * //cmd 由两部分组成，1为指令，2为指令处理类名，中间用","隔开
     */
	void processCmd(CmdDTO repCmdDTO, Boolean result);
	
	/**
	 * 新增考勤机
	 * @param clockinId 考勤机id
	 */
	void insertClockinMachine(String clockinId);
	
	/**
	 * 获取签到人最近的课程安排信息 然后修改考勤状态
	 * @param clockInRecord 考勤机记录
	 */
	void updateCloking(LabClockInRecord clockInRecord);

}
