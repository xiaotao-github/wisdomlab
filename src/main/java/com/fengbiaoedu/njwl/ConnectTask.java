package com.fengbiaoedu.njwl;


/**
 * Created by Administrator on 2018/5/15.
 */
public class ConnectTask{
    private String wgId;
    private String pwd;
    private String deviceId;

    public ConnectTask(String wgId, String pwd, String driverceId) {
        this.wgId = wgId;
        this.pwd = pwd;
        this.deviceId = driverceId;
    }

    public String getWgId() {
        return wgId;
    }

    public void setWgId(String wgId) {
        this.wgId = wgId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
