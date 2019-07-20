package com.fengbiaoedu.mapper;

import com.fengbiaoedu.bean.ScheduleStudentScore;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/6/26.
 */
public interface LabClockInMapper {

    /**
     * 根据打卡人id  和 打卡时间   获取 最近的课程安排时间
     * @param  username 打卡人 账号
     * @param thisTime 打卡时间
     *
     * @return
     */
    ScheduleStudentScore getByIdAndTime(@Param("thisTime") Date thisTime, @Param("userId") String  userId);

    /**
     * 更新学生打卡信息
     * @param studentScore
     */
    void save(ScheduleStudentScore studentScore);
    
    /**
     * 根据打卡人id  和 打卡时间   获取 最近的课程安排时间
     * @param  username 打卡人 账号
     * @param thisTime 打卡时间
     *
     * @return
     */
    List<ScheduleStudentScore> getByIdAndClockinTime(@Param("thisTime") String thisTime, @Param("userId") Integer  userId,@Param("minuteDiffer")Integer minuteDiffer);
    
   
}
