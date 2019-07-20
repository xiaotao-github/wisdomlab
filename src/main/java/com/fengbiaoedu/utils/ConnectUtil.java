package com.fengbiaoedu.utils;

import cc.wulian.ihome.wan.NetSDK;
import com.fengbiaoedu.njwl.ConnectTask;
import com.fengbiaoedu.njwl.ConnectThreadPool;

/**
 * Created by Administrator on 2018/5/17.
 */
public class ConnectUtil {

    public static void isConnectedAndConnect(String wgId,String wpd,String deviceId){
        //判断是否连接  已连接
        if(NetSDK.isConnected(wgId) || NetSDK.isConnected(wgId)){
            NetSDK.sendRefreshDevListMsg(wgId,deviceId);
        }else{
            ConnectThreadPool.getThreadPool().addTask(new ConnectTask(wgId,wpd,deviceId));
        }
    }
}
