package com.fengbiaoedu.pojo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 
 * 封装考勤机请求响应数据</br>
 * @author Administrator
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class ClockInUserInfoBO {
	//{"user_id":"1","user_name":"张三","user_privilege":"user"} 注意用户名要转成unicode码；
	//考勤机用户登记号
	private String user_id ;  
	//考勤机用户名
	private String user_name;
	//考勤机用户权限
	private String user_privilege;
	//登记数据列表
	private List<EnrollData> enroll_data_array;  
	//考勤机用户照片
	private String user_photo;
	//考勤机用户ID卡登记时间
	private String card_enroll_time;
	//考勤机用户人脸登记时间
	private String face_enroll_time;
	//考勤机用户指纹登记时间
	private String fp_enroll_time;
	//考勤机用户密码登记时间
	private String password_enroll_time;
	
	public ClockInUserInfoBO () {
		
		super();
	}
	
	public ClockInUserInfoBO(String user_id, String user_name, String user_privilege, List<EnrollData> enroll_data_array,
			String user_photo, String card_enroll_time, String face_enroll_time, String fp_enroll_time,
			String password_enroll_time) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.user_privilege = user_privilege;
		this.enroll_data_array = enroll_data_array;
		this.user_photo = user_photo;
		this.card_enroll_time = card_enroll_time;
		this.face_enroll_time = face_enroll_time;
		this.fp_enroll_time = fp_enroll_time;
		this.password_enroll_time = password_enroll_time;
	}

	
}
