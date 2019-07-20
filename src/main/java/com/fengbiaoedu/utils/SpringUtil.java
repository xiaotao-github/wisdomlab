package com.fengbiaoedu.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringApplication 的工具类  用于普通类获取application中的bean
 * Created by Administrator on 2018/5/18.
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtil.applicationContext==null){
            SpringUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 获取容器
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 根据名称获取对应bean
     * @param name bean的名称
     * @return
     */
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    /**
     * 根据是定类型获取bean
     * @param clazz 类型
     * @param <T> 泛型的指定类型
     * @return
     */
    public static <T>T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    public static <T>T getBean(Class<T> clazz,String name){
        return applicationContext.getBean(name,clazz);
    }






}
