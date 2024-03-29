package com.fengbiaoedu.owmq;

import cc.wulian.ihome.wan.entity.DeviceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fengbiaoedu.bean.LabClockInRecord;
import com.fengbiaoedu.kemi.CmdDTO;
import com.fengbiaoedu.owconvertor.OWDeviceInfoToEquipmentConvertor;
import com.fengbiaoedu.owdto.OWEquipmentDTO;
import com.fengbiaoedu.owservice.OWEquipmentService;
import com.fengbiaoedu.owutils.OWDeviceTool;
import com.fengbiaoedu.owvo.OWControlEuqData;
import com.fengbiaoedu.service.ClockInService;
import com.fengbiaoedu.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.jms.Queue;
import javax.jms.Topic;
import java.io.IOException;

//@Service("activeMQService")
@Service
public class OWActiveMQService {
    @Autowired
    private JmsMessagingTemplate jmsTemplate;
    @Autowired
    private Topic queMsgTopic;
    @Autowired
    private OWEquipmentService owEquipmentService;

    /********************************/
    @Autowired
    private ClockInService clockInService;

    @Autowired
    private Queue clockInMsgQueue; //考勤打卡消息队列

    @Autowired
    private Queue enrollDataQueue; //考勤机实时登记数据传输消息队列

    @Autowired
    private Queue clockMachineRequestResultQueue; //考勤机请求响应结果消息队列

    @Autowired
    private Queue operateClockMachineResultQueue; //操作用户操作考勤机回调结果消息队列

    /**
     * 广播发送设备变化的信息
     * @param deviceInfo
     */
    public void owSendTopic(DeviceInfo deviceInfo){
        OWEquipmentDTO convertor = OWDeviceInfoToEquipmentConvertor.convertor(deviceInfo);
        String epData = OWDeviceTool.owGetCloseOrOpenByTypeAndEpData(convertor.getType(), convertor.getEpData());
        if(!StringUtils.isEmpty(epData)){
            convertor.setEpData(epData);
        }
        //封装调整devId--->   devid_ep
        convertor.setDevID(convertor.getDevID()+"_"+convertor.getEp());
        try {
            jmsTemplate.convertAndSend(queMsgTopic, JsonUtils.getMapper().writeValueAsString(convertor));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 考勤消息入列</br>
     * @param clockIn
     */
    public void owSendClockInMessage(String data) {
        jmsTemplate.convertAndSend(clockInMsgQueue, data);
    }
    /**
     * 考勤机实时信息登记入队
     * @param data
     */
    public void owSendEnroolData(String data) {
        jmsTemplate.convertAndSend(enrollDataQueue,data);
    }
    /**
     * 考勤机请求指令后，响应数据入队
     * @param data
     */
    public void owSendClockMachineRequestResultMessage(String data) {
        jmsTemplate.convertAndSend(clockMachineRequestResultQueue,data);
    }
    /**
     * 考勤机处理结果，信息入队
     * @param data
     */
    public void owSendOperateClockMachineResult(String data) {
        jmsTemplate.convertAndSend(operateClockMachineResultQueue, data);
    }

    /**
     * 监听接收控制设备的信息
     * @param data
     */
    @JmsListener(destination = "quecontrlqueue")
    public void owListenControllCommand(String  data){
        try {
            OWControlEuqData owControlEuqData = JsonUtils.getMapper().readValue(data, OWControlEuqData.class);
            owEquipmentService.owControlEuq(owControlEuqData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听考勤机考勤消息</br>
     * @param clockIn
     */
    @JmsListener(destination = "clockInMsgQueue")
    @Transactional
    public void owReceiveClockInMessage(String data) {
        LabClockInRecord clockInRecord;
        try {
            clockInRecord = JsonUtils.getMapper().readValue(data, LabClockInRecord.class);
            //插入考勤记录
            clockInService.insertClokInRecord(clockInRecord);
            //更新学生上课考勤状态
            clockInService.updateCloking(clockInRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听考勤机传输登记消息</br>
     * @param clockIn
     */
    @JmsListener(destination = "enrollDataQueue")
    public void owReceiveEnrollData(String data) {
        CmdDTO repCmdDTO;
        try {
            repCmdDTO = JsonUtils.getMapper().readValue(data, CmdDTO.class);
            //处理用户传输登记消息
            clockInService.processCmd(repCmdDTO,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 监听考勤机请求响应结果消息</br>
     * @param data
     */
    @JmsListener(destination="clockMachineRequestResultQueue")
    public void owReceiveClockMachineRequestResultMessage(String data) {
        CmdDTO repCmdDTO;
        try {
            repCmdDTO = JsonUtils.getMapper().readValue(data, CmdDTO.class);
            //请求考勤机请求响应结果消息
            clockInService.processCmd(repCmdDTO,repCmdDTO.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
