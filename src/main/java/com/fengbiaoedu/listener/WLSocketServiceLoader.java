package com.fengbiaoedu.listener;

import cc.wulian.ihome.wan.NetSDK;
import cc.wulian.ihome.wan.entity.DeviceInfo;
import cc.wulian.ihome.wan.entity.RegisterInfo;
import cc.wulian.ihome.wan.util.MD5Util;
import com.fengbiaoedu.njwl.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 物联设备Socket通信服务
 * Created by Administrator on 2018/5/14.
 */
@WebListener
public class WLSocketServiceLoader implements ServletContextListener {
/*    @Autowired
    private NjwlConfig njwlConfig;*/

    /**服务器起动时加载内容*/
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //初始化回调函数
        NetSDK.init(HandleCallBack.instance());
       /* DeviceManager.getInstance().attachDeviceDataObserver(new Observers() {
            @Override
            public void update(DeviceInfo deviceInfo) {
                if("2".equals(deviceInfo.getType())){
                    System.out.println("-----------------------------------有人在闯入红外线");
                }
                if("43".equals(deviceInfo.getType())){
                    System.out.println("-----------------------------------烟雾警报");

                }
            }
        });*/


    }

    /**服务器关闭时加载内容*/
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        NetSDK.disconnectAll();
        ConnectThreadPool.getThreadPool().stop();
    }
}
