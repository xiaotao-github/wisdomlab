package com.fengbiaoedu.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 考勤的学生成绩 对应
 * Created by Administrator on 2018/6/26.
 */
@Data
public class ScheduleStudentScore extends BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer scheduleStudentScoreId;   //成绩表主键
    private Integer scheduleId;               //课程表  CourseSchedule ID
    private Integer submitStatus;             //1.进行中   2.待批改（已提交） 3.已批改   4.重做中
    private String labReport;                //实验报告html
    private String reportFilePath;           //实验报告路径(文件存放路径)
    private String projectFile;              //工程文件
    private String gifFile;                  //Gif动态图
    private String otherFile;                //实验的其他附件
    private Integer submitterId;              //提交人ID
    private double score;                    //学生提交的分数
    private String remark;                   //教师评语
    private Integer goodReport;               //优秀报告
    private Date checkTime;                //批改时间
    private Date submitTime;              //提交时间
    //新增表字段
    private Integer signin;//考勤  0 未签到  1已签到
    private Date  signinTime;//签到时间
    private Date stipulateSgininTime;//规定打卡时间
    private String clockinId; //如果在智慧云实验室上课有考勤机id
}