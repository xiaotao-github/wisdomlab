package com.fengbiaoedu.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/15.
 */
@Data
public class EquipmentDTO implements Serializable{
    //网关id
    private String gwID;
    //设备id
    private String devID;
    //设备类型
    private String type;
    //场景id -- 暂时未用到
    private String category;
    //设备名称
    private String name;
    //房间id--暂时未用到
    private String roomID;
    //、是否在线
    private String isOnline;
    //设备状态
    private String epData;
    private String interval;
    //设备ep
    private String ep;
}
