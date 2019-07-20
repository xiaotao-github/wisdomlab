package com.fengbiaoedu.bean;

import java.io.Serializable;
import java.util.Date;

public class LabClockInMachineUser implements Serializable {
    /**
	 *JAVA序列化的机制是通过判断类的serialVersionUID来验证的版本一致的
     * 在进行反序列化时，JVM会把传来的字节流中的serialVersionUID于本地相应实体类的serialVersionUID进行比较；
     * 如果相同说明是一致的，可以进行反序列化，否则会出现反序列化版本一致的异常，即是InvalidCastException
     *
     * 具体序列化的过程
     * 序列化操作时会把系统当前类的serialVersionUID写入到序列化文件中，当反序列化时系统会自动检测文件中的serialVersionUID，判断它是否与当前类中的serialVersionUID一致。
     * 如果一致说明序列化文件的版本与当前类的版本是一样的，可以反序列化成功，否则就失败
     *
     * 两种显示的生成方式
     * 一是默认的1L，比如：private static final long serialVersionUID = 1L;
     * 二是根据包名，类名，继承关系，非私有的方法和属性，以及参数，返回值等诸多因子计算得出的（64位的哈希字段）
     * 基本上计算出来的这个值是唯一的。比如：private static final long  serialVersionUID = xxxxL
	 */
	private static final long serialVersionUID = 5488130068054582278L;

	private Long id;

    private Long enrollId;

    private String clockinId;

    private String userName;

    private Integer userId;

    private Integer operatorId;

    private Long labId;

    private Date createTime;

    private Date updateTime;

    private String userPrivilege;

    private Byte idFlag;

    private Boolean isDeleted;

    private byte[] enrollPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(Long enrollId) {
        this.enrollId = enrollId;
    }

    public String getClockinId() {
        return clockinId;
    }

    public void setClockinId(String clockinId) {
        this.clockinId = clockinId == null ? null : clockinId.trim();
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

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
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

    public String getUserPrivilege() {
        return userPrivilege;
    }

    public void setUserPrivilege(String userPrivilege) {
        this.userPrivilege = userPrivilege == null ? null : userPrivilege.trim();
    }

    public Byte getIdFlag() {
        return idFlag;
    }

    public void setIdFlag(Byte idFlag) {
        this.idFlag = idFlag;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public byte[] getEnrollPassword() {
        return enrollPassword;
    }

    public void setEnrollPassword(byte[] enrollPassword) {
        this.enrollPassword = enrollPassword;
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
        LabClockInMachineUser other = (LabClockInMachineUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getEnrollId() == null ? other.getEnrollId() == null : this.getEnrollId().equals(other.getEnrollId()))
            && (this.getClockinId() == null ? other.getClockinId() == null : this.getClockinId().equals(other.getClockinId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
            && (this.getLabId() == null ? other.getLabId() == null : this.getLabId().equals(other.getLabId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUserPrivilege() == null ? other.getUserPrivilege() == null : this.getUserPrivilege().equals(other.getUserPrivilege()))
            && (this.getIdFlag() == null ? other.getIdFlag() == null : this.getIdFlag().equals(other.getIdFlag()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getEnrollPassword() == null ? other.getEnrollPassword() == null : this.getEnrollPassword().equals(other.getEnrollPassword()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getEnrollId() == null) ? 0 : getEnrollId().hashCode());
        result = prime * result + ((getClockinId() == null) ? 0 : getClockinId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getLabId() == null) ? 0 : getLabId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUserPrivilege() == null) ? 0 : getUserPrivilege().hashCode());
        result = prime * result + ((getIdFlag() == null) ? 0 : getIdFlag().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getEnrollPassword() == null) ? 0 : getEnrollPassword().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", enrollId=").append(enrollId);
        sb.append(", clockinId=").append(clockinId);
        sb.append(", userName=").append(userName);
        sb.append(", userId=").append(userId);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", labId=").append(labId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", userPrivilege=").append(userPrivilege);
        sb.append(", idFlag=").append(idFlag);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", enrollPassword=").append(enrollPassword);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}