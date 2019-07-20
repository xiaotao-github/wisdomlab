package com.fengbiaoedu.bean;

import java.io.Serializable;
import java.util.Date;

public class LabClockInRecord implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2758914945232851591L;

	private Long id;

    private String clockinId;

    private Long enrollId;

    private String userName;

    private Integer userId;

    private Date clockingTime;

    private Byte verifyMode;

    private Byte clockinMode;

    private Date createTime;

    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClockinId() {
        return clockinId;
    }

    public void setClockinId(String clockinId) {
        this.clockinId = clockinId == null ? null : clockinId.trim();
    }

    public Long getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(Long enrollId) {
        this.enrollId = enrollId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getClockingTime() {
        return clockingTime;
    }

    public void setClockingTime(Date clockingTime) {
        this.clockingTime = clockingTime;
    }

    public Byte getVerifyMode() {
        return verifyMode;
    }

    public void setVerifyMode(Byte verifyMode) {
        this.verifyMode = verifyMode;
    }

    public Byte getClockinMode() {
        return clockinMode;
    }

    public void setClockinMode(Byte clockinMode) {
        this.clockinMode = clockinMode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LabClockInRecord other = (LabClockInRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getClockinId() == null ? other.getClockinId() == null : this.getClockinId().equals(other.getClockinId()))
            && (this.getEnrollId() == null ? other.getEnrollId() == null : this.getEnrollId().equals(other.getEnrollId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getClockingTime() == null ? other.getClockingTime() == null : this.getClockingTime().equals(other.getClockingTime()))
            && (this.getVerifyMode() == null ? other.getVerifyMode() == null : this.getVerifyMode().equals(other.getVerifyMode()))
            && (this.getClockinMode() == null ? other.getClockinMode() == null : this.getClockinMode().equals(other.getClockinMode()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getClockinId() == null) ? 0 : getClockinId().hashCode());
        result = prime * result + ((getEnrollId() == null) ? 0 : getEnrollId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getClockingTime() == null) ? 0 : getClockingTime().hashCode());
        result = prime * result + ((getVerifyMode() == null) ? 0 : getVerifyMode().hashCode());
        result = prime * result + ((getClockinMode() == null) ? 0 : getClockinMode().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", clockinId=").append(clockinId);
        sb.append(", enrollId=").append(enrollId);
        sb.append(", userName=").append(userName);
        sb.append(", userId=").append(userId);
        sb.append(", clockingTime=").append(clockingTime);
        sb.append(", verifyMode=").append(verifyMode);
        sb.append(", clockinMode=").append(clockinMode);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}