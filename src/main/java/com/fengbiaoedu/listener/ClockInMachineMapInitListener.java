package com.fengbiaoedu.listener;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fengbiaoedu.bean.LabClockInMachine;
import com.fengbiaoedu.bean.LabClockInMachineExample;
import com.fengbiaoedu.kemi.ClockInManager;
import com.fengbiaoedu.mapper.LabClockInMachineMapper;
import com.fengbiaoedu.utils.SpringUtil;
@Component("clockInMachineMapInitListener") 
public class ClockInMachineMapInitListener implements ApplicationListener<ApplicationEvent> {
	
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		if(event instanceof ContextRefreshedEvent) {
			ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
			if(applicationContext.getParent() ==null) {
				//查询考勤机列表作为map，保存到redis中
				LabClockInMachineExample example = new LabClockInMachineExample();
				List<LabClockInMachine> clockinMachineList = SpringUtil.getBean(LabClockInMachineMapper.class).selectByExample(example);
				if(clockinMachineList!=null && !clockinMachineList.isEmpty()) {
					for (LabClockInMachine machine : clockinMachineList) {
						ClockInManager.CLOCKIN_MACHINE_MAP.put(machine.getClockinId(), machine.getIsDeleted());
					}
				}
			}
		}
		
	}
}
