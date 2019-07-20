package com.fengbiaoedu.owutils;

import cc.wulian.ihome.wan.NetSDK;
import com.fengbiaoedu.ow.OWConnectTask;
import com.fengbiaoedu.ow.OWConnectThreadPool;

public class OWConnectUtil {
    public static void isConnectedAndConnect(String wgId,String wpd,String deviceId){
        //判断是否连接  已连接
        if(NetSDK.isConnected(wgId) || NetSDK.isConnected(wgId)){
            NetSDK.sendRefreshDevListMsg(wgId,deviceId);
        }else{
            OWConnectThreadPool.getThreadPool().addTask(new OWConnectTask(wgId,wpd,deviceId));
        }
    }
}
