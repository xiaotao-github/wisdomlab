package com.fengbiaoedu.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


/**
 * 实验室 设备信息 实体类
 * Created by Administrator on 2018/7/12.
 */
@Data
public class Equipment extends BaseBean{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer equId; //主键id 自增
    @Column(name = "gw_id")
    private String gwId;//网关id  非空
    @Column(name = "dev_id",unique = true)
    private String devId;//设备id
    @Column(name = "ep")
    private String ep;//端口 默认14
    @Column(name = "ep_type")
    private String epType;//设备类型
    @Column(name = "ep_name")
    private String epName;//设备名称
    @Column(name = "seat_num")
    private int seatNum;//座位序号
    @Column(name = "lab_ep_type")
    private Integer labEpType = 0;//自定义类型  0.其他（默认） 1.灯控 2.红外 3.工位 4.插座 5.摄像头 6.窗户 7.窗帘 8.门 9.电源总开关 10.传感设备
    @Column(name = "stealth")
    private Integer stealth;//伪删除
}
