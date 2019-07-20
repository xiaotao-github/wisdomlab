package com.fengbiaoedu.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/25.
 */
public class ClockIn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7131614864621133334L;
	private String fk_bin_data_lib;
    private int io_mode;
    private String io_time;
    private String log_image;
    private String user_id;
    private int verify_mode;
    private String devId;
    
    public String getFk_bin_data_lib() {
        return fk_bin_data_lib;
    }
    public void setFk_bin_data_lib(String fk_bin_data_lib) {
        this.fk_bin_data_lib = fk_bin_data_lib;
    }
    public int getIo_mode() {
        return io_mode;
    }
    public void setIo_mode(int io_mode) {
        this.io_mode = io_mode;
    }
    public String getIo_time() {
        return io_time;
    }
    public void setIo_time(String io_time) {
        this.io_time = io_time;
    }
    public String getLog_image() {
        return log_image;
    }
    public void setLog_image(String log_image) {
        this.log_image = log_image;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public int getVerify_mode() {
        return verify_mode;
    }
    public void setVerify_mode(int verify_mode) {
        this.verify_mode = verify_mode;
    }
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	@Override
	public String toString() {
		return "ClockIn [fk_bin_data_lib=" + fk_bin_data_lib + ", io_mode=" + io_mode + ", io_time=" + io_time
				+ ", log_image=" + log_image + ", user_id=" + user_id + ", verify_mode=" + verify_mode + ", devId="
				+ devId + "]";
	}
    
}
