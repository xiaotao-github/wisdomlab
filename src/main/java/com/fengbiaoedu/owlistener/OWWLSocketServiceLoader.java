package com.fengbiaoedu.owlistener;

import cc.wulian.ihome.wan.NetSDK;
import com.fengbiaoedu.njwl.ConnectThreadPool;
import com.fengbiaoedu.njwl.HandleCallBack;
import com.fengbiaoedu.ow.OWConnectThreadPool;
import com.fengbiaoedu.ow.OWHandleCallBack;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 物联设备Socket通信服务
 * Created by Administrator on 2018/5/14.
 */
@WebListener
public class OWWLSocketServiceLoader implements ServletContextListener {
/*    @Autowired
    private NjwlConfig njwlConfig;*/

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //初始化回调函数
        NetSDK.init(OWHandleCallBack.instance());
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

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        NetSDK.disconnectAll();
        OWConnectThreadPool.getThreadPool().stop();
    }
}
