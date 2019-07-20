package com.fengbiaoedu.bean;

import java.io.Serializable;
import java.util.Date;

public class LabClockInUserInfo implements Serializable {
	 private Long id;

	    private Integer userId;

	    private String name;

	    private String username;

	    private Byte type;

	    private Byte status;

	    private Integer classId;

	    private Integer operatorId;

	    private String userPrivilege;

	    private Date cardEnrollTime;

	    private Date faceEnrollTime;

	    private Date fpEnrollTime;

	    private Date passwordEnrollTime;

	    private Date createTime;

	    private Date updateTime;

	    private static final long serialVersionUID = 1L;

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public Integer getUserId() {
	        return userId;
	    }

	    public void setUserId(Integer userId) {
	        this.userId = userId;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name == null ? null : name.trim();
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username == null ? null : username.trim();
	    }

	    public Byte getType() {
	        return type;
	    }

	    public void setType(Byte type) {
	        this.type = type;
	    }

	    public Byte getStatus() {
	        return status;
	    }

	    public void setStatus(Byte status) {
	        this.status = status;
	    }

	    public Integer getClassId() {
	        return classId;
	    }

	    public void setClassId(Integer classId) {
	        this.classId = classId;
	    }

	    public Integer getOperatorId() {
	        return operatorId;
	    }

	    public void setOperatorId(Integer operatorId) {
	        this.operatorId = operatorId;
	    }

	    public String getUserPrivilege() {
	        return userPrivilege;
	    }

	    public void setUserPrivilege(String userPrivilege) {
	        this.userPrivilege = userPrivilege == null ? null : userPrivilege.trim();
	    }

	    public Date getCardEnrollTime() {
	        return cardEnrollTime;
	    }

	    public void setCardEnrollTime(Date cardEnrollTime) {
	        this.cardEnrollTime = cardEnrollTime;
	    }

	    public Date getFaceEnrollTime() {
	        return faceEnrollTime;
	    }

	    public void setFaceEnrollTime(Date faceEnrollTime) {
	        this.faceEnrollTime = faceEnrollTime;
	    }

	    public Date getFpEnrollTime() {
	        return fpEnrollTime;
	    }

	    public void setFpEnrollTime(Date fpEnrollTime) {
	        this.fpEnrollTime = fpEnrollTime;
	    }

	    public Date getPasswordEnrollTime() {
	        return passwordEnrollTime;
	    }

	    public void setPasswordEnrollTime(Date passwordEnrollTime) {
	        this.passwordEnrollTime = passwordEnrollTime;
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
	        LabClockInUserInfo other = (LabClockInUserInfo) that;
	        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
	            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
	            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
	            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
	            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
	            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
	            && (this.getClassId() == null ? other.getClassId() == null : this.getClassId().equals(other.getClassId()))
	            && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
	            && (this.getUserPrivilege() == null ? other.getUserPrivilege() == null : this.getUserPrivilege().equals(other.getUserPrivilege()))
	            && (this.getCardEnrollTime() == null ? other.getCardEnrollTime() == null : this.getCardEnrollTime().equals(other.getCardEnrollTime()))
	            && (this.getFaceEnrollTime() == null ? other.getFaceEnrollTime() == null : this.getFaceEnrollTime().equals(other.getFaceEnrollTime()))
	            && (this.getFpEnrollTime() == null ? other.getFpEnrollTime() == null : this.getFpEnrollTime().equals(other.getFpEnrollTime()))
	            && (this.getPasswordEnrollTime() == null ? other.getPasswordEnrollTime() == null : this.getPasswordEnrollTime().equals(other.getPasswordEnrollTime()))
	            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
	            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
	    }

	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
	        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
	        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
	        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
	        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
	        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
	        result = prime * result + ((getClassId() == null) ? 0 : getClassId().hashCode());
	        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
	        result = prime * result + ((getUserPrivilege() == null) ? 0 : getUserPrivilege().hashCode());
	        result = prime * result + ((getCardEnrollTime() == null) ? 0 : getCardEnrollTime().hashCode());
	        result = prime * result + ((getFaceEnrollTime() == null) ? 0 : getFaceEnrollTime().hashCode());
	        result = prime * result + ((getFpEnrollTime() == null) ? 0 : getFpEnrollTime().hashCode());
	        result = prime * result + ((getPasswordEnrollTime() == null) ? 0 : getPasswordEnrollTime().hashCode());
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
	        sb.append(", userId=").append(userId);
	        sb.append(", name=").append(name);
	        sb.append(", username=").append(username);
	        sb.append(", type=").append(type);
	        sb.append(", status=").append(status);
	        sb.append(", classId=").append(classId);
	        sb.append(", operatorId=").append(operatorId);
	        sb.append(", userPrivilege=").append(userPrivilege);
	        sb.append(", cardEnrollTime=").append(cardEnrollTime);
	        sb.append(", faceEnrollTime=").append(faceEnrollTime);
	        sb.append(", fpEnrollTime=").append(fpEnrollTime);
	        sb.append(", passwordEnrollTime=").append(passwordEnrollTime);
	        sb.append(", createTime=").append(createTime);
	        sb.append(", updateTime=").append(updateTime);
	        sb.append(", serialVersionUID=").append(serialVersionUID);
	        sb.append("]");
	        return sb.toString();
	    }
}