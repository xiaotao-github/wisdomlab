package com.fengbiaoedu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * Created by Administrator on 2018/6/25.
 */
@Component
public class ClockinConfig {
    private static final String  FORMAT= "HH:mm";

    @Value("${course.A}")
    private String A; //1-2节
    @Value("${course.B}")
    private String B; //3-4节
    @Value("${course.C}")
    private String C; //午休
    @Value("${course.D}")
    private String D;//5-6节
    @Value("${course.E}")
    private String E; //7-8节
    @Value("${course.F}")
    private String F; //9-10节
    
    @Value("${clockinMachineLockTimeOut}") //考勤机锁超时时间
    private Long clockinMachineLockTimeOut;
    @Value("${userOperateWorkKeyTimeOut}")//考勤机用户操作任务key超时时间
    private Long userOperateWorkKeyTimeOut; 
    @Value("${classMinutes}")//每两小节多少分钟
    private Integer classMinutes;
    private Long startA;
    private Long endA;
    private Long startB;
    private Long endB;
    private Long startC;
    private Long endC;
    private Long startD;
    private Long endD;
    private Long startE;
    private Long endE;
    private Long startF;
    private Long endF;
    
    
    
    
    public Long getStartA() {
        if(startA==null){
            String time =  A.split("-")[0];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            startA =  dateTime.getTime();
        }
        return startA;
    }
    public Long getEndA() {
        if(endA==null){
            String time =  A.split("-")[1];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            endA =  dateTime.getTime();
        }
        return endA;
    }

    public Long getStartB() {
        if(startB==null){
            String time =  B.split("-")[0];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            startB =  dateTime.getTime();
        }
        return startB;
    }

    public Long getEndB() {
        if(endB==null){
            String time =  B.split("-")[1];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            endB =  dateTime.getTime();
        }
        return endB;
    }

    public Long getStartC() {
        if(startC==null){
            String time =  C.split("-")[0];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            startC =  dateTime.getTime();
        }
        return startC;
    }

    public Long getEndC() {
        if(endC==null){
            String time =  C.split("-")[1];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            endC =  dateTime.getTime();
        }
        return endC;
    }

    public Long getStartD() {
        if(startD==null){
            String time =  D.split("-")[0];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            startD =  dateTime.getTime();
        }
        return startD;
    }

    public Long getEndD() {
        if(endD==null){
            String time =  D.split("-")[1];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            endD =  dateTime.getTime();
        }
        return endD;
    }

    public Long getStartE() {
        if(startE==null){
            String time =  E.split("-")[0];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            startE =  dateTime.getTime();
        }
        return startE;
    }

    public Long getEndE() {
        if(endE==null){
            String time =  E.split("-")[1];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            endE =  dateTime.getTime();
        }
        return endE;
    }

    public Long getStartF() {
        if(startF==null){
            String time =  F.split("-")[0];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            startF =  dateTime.getTime();
        }
        return startF;
    }

    public Long getEndF() {
        if(endF==null){
            String time =  F.split("-")[1];
            DateTime dateTime = DateUtil.parse(time, FORMAT);
            endF =  dateTime.getTime();
        }
        return endF;
    }
	public Long getClockinMachineLockTimeOut() {
		return clockinMachineLockTimeOut;
	}
	public Long getUserOperateWorkKeyTimeOut() {
		return userOperateWorkKeyTimeOut;
	}
	public Integer getClassMinutes() {
		return classMinutes;
	}
	public void setClassMinutes(Integer classMinutes) {
		this.classMinutes = classMinutes;
	}
	
    
}
