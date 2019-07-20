package com.fengbiaoedu.njwl;
import cc.wulian.ihome.wan.NetSDK;
import cc.wulian.ihome.wan.entity.RegisterInfo;
import cc.wulian.ihome.wan.util.MD5Util;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * Created by Administrator on 2018/5/15.
 */
public class ConnectThreadPool {

    /***
     * 单例设计模式
     * 解决一个全局使用的类，频繁创建和销毁
     * 节省内存，有利于Java的垃圾回收机制
     * 通过线程同步来控制资源的并发访问
     * 控制实例产生的数量，达到节约资源的目的
     * 数据库连接池（控制资源）
     * **/
    private static ConnectThreadPool connectThreadPool = null;
    private  ScheduledThreadPoolExecutor scheduledThreadPool;
    private  Map<String,ConnectTask>  connectTaskMap;

    private  ConnectThreadPool(Integer poolSize){
        scheduledThreadPool = new ScheduledThreadPoolExecutor(poolSize);
        connectTaskMap = new HashMap<>();
    }

    public static ConnectThreadPool getThreadPool(){
        ConnectThreadPool inst = connectThreadPool;
        if(inst==null){
            synchronized (ConnectThreadPool.class){
                inst = connectThreadPool;
                if(inst==null){
                    inst = new ConnectThreadPool(500);
                    connectThreadPool = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 添加链接任务
     * @param connectTask  任务信息
     * @return false添加失败  true 添加成功
     */
    public synchronized void addTask(final ConnectTask connectTask){
        if(connectTaskMap.get(connectTask.getWgId())==null){
            connectTaskMap.put(connectTask.getWgId(),connectTask);
        }
        //每15秒执行一次
        if(!NetSDK.isConnecting(connectTask.getWgId()) && !NetSDK.isConnected(connectTask.getDeviceId())){
            /**
             * ScheduledThreadPoolExecutor定时任务，与Timer/TimerTask类似
             * 1.Timer/TimerTask只创建了一个线程。当你的任务执行的时间超过设置的延时时间将会产生一些问题
             * 2.Timer/TimerTask创建的线程没有处理异常，因此一旦抛出非受检异常，该线程会立即终止
             * ***********************/
            scheduledThreadPool.scheduleAtFixedRate(()->{
                if (NetSDK.isConnecting(connectTask.getWgId()) || NetSDK.isConnected(connectTask.getDeviceId())){
                    NetSDK.sendRefreshDevListMsg(connectTask.getWgId(), connectTask.getDeviceId());
                }else{
                    RegisterInfo mRegisterInfo = null;
                    if(StringUtils.isEmpty(connectTask.getDeviceId())){
                       mRegisterInfo = new RegisterInfo("862630030713128");
                    }else{
                        mRegisterInfo = new RegisterInfo(connectTask.getDeviceId());
                    }
                    if(StringUtils.isEmpty(connectTask.getPwd())){
                        connectTask.setPwd(connectTask.getWgId().substring(connectTask.getWgId().length()-6));
                    }
                    NetSDK.connect(connectTask.getWgId(), MD5Util.encrypt(connectTask.getPwd()), mRegisterInfo);
                    NetSDK.sendRefreshDevListMsg(connectTask.getWgId(), connectTask.getDeviceId());
                }
            },1,15, TimeUnit.SECONDS);
        }
    }
    /**
     * 停止运行
     */
    public void stop(){
        connectTaskMap.clear();
        scheduledThreadPool.shutdown();
    }
}

