package com.fengbiaoedu.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

public class LabClockInMachine implements Serializable {
    private Integer id;

    private String clockinId;

    private Long labId;

    private String fkName;

    private Integer operatorId;

    private String supportedEnrollData;

    private Boolean isDeleted;

    private Short totalUserCount;

    private Short userCount;

    private Short managerCount;

    private Short fpCount;

    private Short faceCount;

    private Short passwordCount;

    private Short idcardCount;

    private Integer totalLogCount;

    private String firmware;

    private Date createTime;

    private Date updateTime;
    //临时
    private Long machineUserId; //表示一个用户在考勤机用户的自增主键，只能用在带一个userId联表查询中
    
    
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClockinId() {
        return clockinId;
    }

    public void setClockinId(String clockinId) {
        this.clockinId = clockinId == null ? null : clockinId.trim();
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
    }

    public String getFkName() {
        return fkName;
    }

    public void setFkName(String fkName) {
        this.fkName = fkName == null ? null : fkName.trim();
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getSupportedEnrollData() {
        return supportedEnrollData;
    }

    public void setSupportedEnrollData(String supportedEnrollData) {
        this.supportedEnrollData = supportedEnrollData == null ? null : supportedEnrollData.trim();
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Short getTotalUserCount() {
        return totalUserCount;
    }

    public void setTotalUserCount(Short totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    public Short getUserCount() {
        return userCount;
    }

    public void setUserCount(Short userCount) {
        this.userCount = userCount;
    }

    public Short getManagerCount() {
        return managerCount;
    }

    public void setManagerCount(Short managerCount) {
        this.managerCount = managerCount;
    }

    public Short getFpCount() {
        return fpCount;
    }

    public void setFpCount(Short fpCount) {
        this.fpCount = fpCount;
    }

    public Short getFaceCount() {
        return faceCount;
    }

    public void setFaceCount(Short faceCount) {
        this.faceCount = faceCount;
    }

    public Short getPasswordCount() {
        return passwordCount;
    }

    public void setPasswordCount(Short passwordCount) {
        this.passwordCount = passwordCount;
    }

    public Short getIdcardCount() {
        return idcardCount;
    }

    public void setIdcardCount(Short idcardCount) {
        this.idcardCount = idcardCount;
    }

    public Integer getTotalLogCount() {
        return totalLogCount;
    }

    public void setTotalLogCount(Integer totalLogCount) {
        this.totalLogCount = totalLogCount;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware == null ? null : firmware.trim();
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
    
	public Long getMachineUserId() {
		return machineUserId;
	}

	public void setMachineUserId(Long machineUserId) {
		this.machineUserId = machineUserId;
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
        LabClockInMachine other = (LabClockInMachine) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getClockinId() == null ? other.getClockinId() == null : this.getClockinId().equals(other.getClockinId()))
            && (this.getLabId() == null ? other.getLabId() == null : this.getLabId().equals(other.getLabId()))
            && (this.getFkName() == null ? other.getFkName() == null : this.getFkName().equals(other.getFkName()))
            && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
            && (this.getSupportedEnrollData() == null ? other.getSupportedEnrollData() == null : this.getSupportedEnrollData().equals(other.getSupportedEnrollData()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getTotalUserCount() == null ? other.getTotalUserCount() == null : this.getTotalUserCount().equals(other.getTotalUserCount()))
            && (this.getUserCount() == null ? other.getUserCount() == null : this.getUserCount().equals(other.getUserCount()))
            && (this.getManagerCount() == null ? other.getManagerCount() == null : this.getManagerCount().equals(other.getManagerCount()))
            && (this.getFpCount() == null ? other.getFpCount() == null : this.getFpCount().equals(other.getFpCount()))
            && (this.getFaceCount() == null ? other.getFaceCount() == null : this.getFaceCount().equals(other.getFaceCount()))
            && (this.getPasswordCount() == null ? other.getPasswordCount() == null : this.getPasswordCount().equals(other.getPasswordCount()))
            && (this.getIdcardCount() == null ? other.getIdcardCount() == null : this.getIdcardCount().equals(other.getIdcardCount()))
            && (this.getTotalLogCount() == null ? other.getTotalLogCount() == null : this.getTotalLogCount().equals(other.getTotalLogCount()))
            && (this.getFirmware() == null ? other.getFirmware() == null : this.getFirmware().equals(other.getFirmware()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getClockinId() == null) ? 0 : getClockinId().hashCode());
        result = prime * result + ((getLabId() == null) ? 0 : getLabId().hashCode());
        result = prime * result + ((getFkName() == null) ? 0 : getFkName().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getSupportedEnrollData() == null) ? 0 : getSupportedEnrollData().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getTotalUserCount() == null) ? 0 : getTotalUserCount().hashCode());
        result = prime * result + ((getUserCount() == null) ? 0 : getUserCount().hashCode());
        result = prime * result + ((getManagerCount() == null) ? 0 : getManagerCount().hashCode());
        result = prime * result + ((getFpCount() == null) ? 0 : getFpCount().hashCode());
        result = prime * result + ((getFaceCount() == null) ? 0 : getFaceCount().hashCode());
        result = prime * result + ((getPasswordCount() == null) ? 0 : getPasswordCount().hashCode());
        result = prime * result + ((getIdcardCount() == null) ? 0 : getIdcardCount().hashCode());
        result = prime * result + ((getTotalLogCount() == null) ? 0 : getTotalLogCount().hashCode());
        result = prime * result + ((getFirmware() == null) ? 0 : getFirmware().hashCode());
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
        sb.append(", labId=").append(labId);
        sb.append(", fkName=").append(fkName);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", supportedEnrollData=").append(supportedEnrollData);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", totalUserCount=").append(totalUserCount);
        sb.append(", userCount=").append(userCount);
        sb.append(", managerCount=").append(managerCount);
        sb.append(", fpCount=").append(fpCount);
        sb.append(", faceCount=").append(faceCount);
        sb.append(", passwordCount=").append(passwordCount);
        sb.append(", idcardCount=").append(idcardCount);
        sb.append(", totalLogCount=").append(totalLogCount);
        sb.append(", firmware=").append(firmware);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}