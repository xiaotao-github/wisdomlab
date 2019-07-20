package com.fengbiaoedu.pojo;

import lombok.Data;

/**
 * 临时保存要注册用户信息，用于关联到考勤机。包括id、学号和用户名。</br>
 * @author wucb
 *
 */
@Data
public class ClockUser {
	
	private Integer userId; //id
	
	private String  name;    //姓名
	
	private Integer type;  //类型  1(老师)，2(学生)
	
	private String username;  //学号
	
	private String originClockinId;  //原考勤机id
	
	private Integer classId;
	
	public ClockUser() {
		super();
	}

	public ClockUser(Integer userId, String name, Integer type, String username, String originClockinId,
			Integer classId) {
		super();
		this.userId = userId;
		this.name = name;
		this.type = type;
		this.username = username;
		this.originClockinId = originClockinId;
		this.classId = classId;
	}

	

	
	
	
}
