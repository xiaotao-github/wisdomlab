package com.fengbiaoedu.utils;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengbiaoedu.bean.ScheduleStudentScore;
import com.fengbiaoedu.config.ClockinConfig;
import com.fengbiaoedu.enums.SigninEnum;
import com.fengbiaoedu.pojo.ClockIn;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

/**
 * Created by Administrator on 2018/6/26.
 */

@Component
public class ClockInUtils1 {

    private static ClockinConfig clockinConfig;
    private static final  ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public ClockInUtils1(ClockinConfig clockinConfig) {
        ClockInUtils1.clockinConfig = clockinConfig;
    }

    //格式化打卡数据
    public static ClockIn parse(ServletInputStream sis){
        try {
            byte[] buffer = new byte[1024];
            sis.read(buffer, 0, 1024);
            String hexString = HexStringToBytesArray.toHexString(buffer);
            //转十进制字节数组
            // = HexStringToBytesArray.toByteArray(byteArray);
            //System.out.println(hexString);
            //截取花括号  { ==> 7B    }==>7D
            hexString = hexString.split("7b")[1].split("7d")[0];
            byte[] mydata = HexStringToBytesArray.toByteArray(hexString);
            String data = "{"+(new String(mydata,"utf-8"))+"}";
            return MAPPER.readValue(data, ClockIn.class);
        } catch (IOException e) {
            return  null;
        }
    }
    
    public static String  parseDevId(ServletInputStream sis, int contentLength) {
    	try {
            byte[] buffer = new byte[contentLength];
            sis.read(buffer, 0, contentLength);
           
            String hexString = HexStringToBytesArray.toHexString(buffer);
            //截取花括号  { ==> 7B    }==>7D
			hexString = hexString.split("7b")[1].split("7d")[0];
			byte[] mydata = HexStringToBytesArray.toByteArray(hexString);
           String data = "{"+(new String(mydata,"utf-8"))+"}";
            return data;
        } catch (IOException e) {
            return  null;
        }
    }



    /**
     * 判断打卡时间是否相差 2小时  相差两小时不能打卡  返回null
     * 判断打卡时间是否迟到
     * 封装成实体类返回
     * @param studentScore  考勤对应的成绩表
     * @param clockIn 考勤信息
     * @return
     */
    public  static ScheduleStudentScore clockIn(ScheduleStudentScore studentScore, ClockIn clockIn){
        //如果是已经签过的，不能再重签 返回null
    	Integer signin = studentScore.getSignin();
    	int code = SigninEnum.NORMAL.getCode();
        /**if(studentScore.getSignin() == SigninEnum.NORMAL.getCode()
                || studentScore.getSignin() == SigninEnum.DELAY.getCode()){
            return  null;
        }*/
        //标准上课时间
        Date stipulateSgininTime = studentScore.getStipulateSgininTime();
        //打卡时间
        Date clockTime = DateUtil.parse(clockIn.getIo_time());
        //判断是否相差两个小时 相差两个小时不能打卡 返回 null
        if( DateUtil.between(stipulateSgininTime, clockTime, DateUnit.HOUR)>=2L){
            return  null;
        }
        //如果签到时间大于标准考勤时间 则 判定为迟到  否则考勤正常
        if(clockTime.getTime() > stipulateSgininTime.getTime()){
            studentScore.setSignin(SigninEnum.DELAY.getCode());
        }else{
            studentScore.setSignin(SigninEnum.NORMAL.getCode());
        }
        studentScore.setSigninTime(clockTime);
        studentScore.setUpdateTime(DateUtil.date());
        return  studentScore;
    }
}
