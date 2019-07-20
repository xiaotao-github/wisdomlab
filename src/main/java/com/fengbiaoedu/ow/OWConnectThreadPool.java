package com.fengbiaoedu.ow;
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
public class OWConnectThreadPool {

    /***
     * 单例设计模式
     * 解决一个全局使用的类，频繁创建和销毁
     * 节省内存，有利于Java的垃圾回收机制
     * 通过线程同步来控制资源的并发访问
     * 控制实例产生的数量，达到节约资源的目的
     * 数据库连接池（控制资源）
     * **/
    private static OWConnectThreadPool owConnectThreadPool = null;
    private  ScheduledThreadPoolExecutor scheduledThreadPool;
    private  Map<String,OWConnectTask>  owConnectTaskMap;

    private OWConnectThreadPool(Integer poolSize){
        scheduledThreadPool = new ScheduledThreadPoolExecutor(poolSize);
        owConnectTaskMap = new HashMap<>();
    }

    public static OWConnectThreadPool getThreadPool(){
        OWConnectThreadPool inst = owConnectThreadPool;
        if(inst==null){
            synchronized (OWConnectThreadPool.class){
                inst = owConnectThreadPool;
                if(inst==null){
                    inst = new OWConnectThreadPool(500);
                    owConnectThreadPool = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 添加链接任务
     * @param owConnectTask  任务信息
     * @return false添加失败  true 添加成功
     */
    public synchronized void addTask(final OWConnectTask owConnectTask){
        if(owConnectTaskMap.get(owConnectTask.getWgId())==null){
            owConnectTaskMap.put(owConnectTask.getWgId(),owConnectTask);
        }
        //每15秒执行一次
        if(!NetSDK.isConnecting(owConnectTask.getWgId()) && !NetSDK.isConnected(owConnectTask.getDeviceId())){
            /**
             * ScheduledThreadPoolExecutor定时任务，与Timer/TimerTask类似
             * 1.Timer/TimerTask只创建了一个线程。当你的任务执行的时间超过设置的延时时间将会产生一些问题
             * 2.Timer/TimerTask创建的线程没有处理异常，因此一旦抛出非受检异常，该线程会立即终止
             * ***********************/
            scheduledThreadPool.scheduleAtFixedRate(()->{
                if (NetSDK.isConnecting(owConnectTask.getWgId()) || NetSDK.isConnected(owConnectTask.getDeviceId())){
                    NetSDK.sendRefreshDevListMsg(owConnectTask.getWgId(), owConnectTask.getDeviceId());
                }else{
                    RegisterInfo mRegisterInfo = null;
                    if(StringUtils.isEmpty(owConnectTask.getDeviceId())){
                       mRegisterInfo = new RegisterInfo("862630030713128");
                    }else{
                        mRegisterInfo = new RegisterInfo(owConnectTask.getDeviceId());
                    }
                    if(StringUtils.isEmpty(owConnectTask.getPwd())){
                        owConnectTask.setPwd(owConnectTask.getWgId().substring(owConnectTask.getWgId().length()-6));
                    }
                    NetSDK.connect(owConnectTask.getWgId(), MD5Util.encrypt(owConnectTask.getPwd()), mRegisterInfo);
                    NetSDK.sendRefreshDevListMsg(owConnectTask.getWgId(), owConnectTask.getDeviceId());
                }
            },1,15, TimeUnit.SECONDS);
        }
    }
    /**
     * 停止运行
     */
    public void stop(){
        owConnectTaskMap.clear();
        scheduledThreadPool.shutdown();
    }
}

