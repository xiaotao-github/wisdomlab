package com.fengbiaoedu.pojo;

/**
 * 封装考勤机登记数据,用于转换成相应的json</br>
 * 考勤机登记数据json格式如下：</br>
 * "enroll_data_array":[
	{“backup_number”:<3.5.1>,”enroll_data”:”BIN_2”},
	{“backup_number”:<3.5.2>,”enroll_data”:”BIN_3”},
	…,
	{“backup_number”:<3.5.k>,”enroll_data”:”BIN_k+1”},
	]
      说明：
 * backup_number字段： 表示登记数据的类型的数字。设置以如下数值中一个。
    0 ~ 9 : 用户10个指头的指纹数据
    10 : 用户的密码
    11 : 用户的ID卡号
    12 : 用户的人脸数据
    enroll_data 字段：表示注册数据跟第几个2进制数据对应的关系
    BIN_2：表示第二个二进制，
    BIN_3：表示第三个二进制，
    BIN_k+1：表示第k+1个二进制
 * 
 * @author Administrator
 *
 */
public class EnrollData {
	
	private Integer backup_number;
	
	private String enroll_data ;
	
	public EnrollData() {
		super();
	}

	public EnrollData(Integer backup_number, String enroll_data) {
		super();
		this.backup_number = backup_number;
		this.enroll_data = enroll_data;
	}

	public Integer getBackup_number() {
		return backup_number;
	}

	public void setBackup_number(Integer backup_number) {
		this.backup_number = backup_number;
	}

	public String getEnroll_data() {
		return enroll_data;
	}

	public void setEnroll_data(String enroll_data) {
		this.enroll_data = enroll_data;
	}

	@Override
	public String toString() {
		return "EnrollData [backup_number=" + backup_number + ", enroll_data=" + enroll_data + "]";
	}
	
	
}
