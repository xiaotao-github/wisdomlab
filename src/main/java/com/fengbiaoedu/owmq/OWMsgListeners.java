package com.fengbiaoedu.owmq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Created by Administrator on 2018/7/18.
 */
@Configuration
@EnableJms
public class OWMsgListeners {
    /**从配置文件中获取到对应的MQ**/
    @Value("${activemq.listenequmsg}")
    private String listenequmsg;
    @Value("${activemq.quecontrlqueue}")
    private String quecontrlqueue;
    
    @Value("${activemq.clockInMsgQueue}")
    private String clockInMsgQueue;
    
    @Value("${activemq.enrollDataQueue}")
    private String enrollDataQueue;
    
    @Value("${activemq.clockMachineRequestResultQueue}")
    private String clockMachineRequestResultQueue;
    @Value("${activemq.operateClockMachineResultQueue}")
    private String operateClockMachineResultQueue;


    /***
     * topic 是一对多发布，一条消息可以多个消费者订阅；没有订阅的，就没法接收之前的消息，理论上是消失了。其实也可以配置topic的持久化，但是必须先订阅
       queue:是 一条消息只可以一个消费者，若多个消费者阅读也只是一个消费者可以接受；如果发布一条消息没有消费者阅读，消息会保存起来，直至有消费者订阅
        点对点（point to point， queue）和发布/订阅（publish/subscribe，topic）。主要区别就是是否能重复消费。
     */


    @Bean
    public Queue queControllQueue(){
        return new ActiveMQQueue(quecontrlqueue);
    }
    @Bean
    public Topic queMsgTopic(){
        return new ActiveMQTopic(listenequmsg);
    }
    
    //用于考勤信息的队列
    @Bean
    public Queue clockInMsgQueue() {
    	return new ActiveMQQueue(clockInMsgQueue);
    }
    
    //操作考勤机指令的队列
    @Bean
    public Queue enrollDataQueue() {
    	return new ActiveMQQueue(enrollDataQueue);
    }
    
    @Bean
    public Queue clockMachineRequestResultQueue() {
    	return new ActiveMQQueue(clockMachineRequestResultQueue);
    }
    @Bean
    public Queue operateClockMachineResultQueue() {
    	return new ActiveMQQueue(operateClockMachineResultQueue);
    }
}
