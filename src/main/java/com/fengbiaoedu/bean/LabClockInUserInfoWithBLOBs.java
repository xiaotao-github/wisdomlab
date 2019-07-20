package com.fengbiaoedu.bean;

import java.io.Serializable;

public class LabClockInUserInfoWithBLOBs extends LabClockInUserInfo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4245949428557495301L;

    private byte[] enrollPhoto;

    private byte[] enrollFp1;

    private byte[] enrollFp2;

    private byte[] enrollFp3;

    private byte[] enrollFp4;

    private byte[] enrollFp5;

    private byte[] enrollFp6;

    private byte[] enrollFp7;

    private byte[] enrollFp8;

    private byte[] enrollFp9;

    private byte[] enrollFp10;

    private byte[] enrollPassword;

    private byte[] enrollFace;

    private byte[] enrollCard;

    public byte[] getEnrollPhoto() {
        return enrollPhoto;
    }

    public void setEnrollPhoto(byte[] enrollPhoto) {
        this.enrollPhoto = enrollPhoto;
    }

    public byte[] getEnrollFp1() {
        return enrollFp1;
    }

    public void setEnrollFp1(byte[] enrollFp1) {
        this.enrollFp1 = enrollFp1;
    }

    public byte[] getEnrollFp2() {
        return enrollFp2;
    }

    public void setEnrollFp2(byte[] enrollFp2) {
        this.enrollFp2 = enrollFp2;
    }

    public byte[] getEnrollFp3() {
        return enrollFp3;
    }

    public void setEnrollFp3(byte[] enrollFp3) {
        this.enrollFp3 = enrollFp3;
    }

    public byte[] getEnrollFp4() {
        return enrollFp4;
    }

    public void setEnrollFp4(byte[] enrollFp4) {
        this.enrollFp4 = enrollFp4;
    }

    public byte[] getEnrollFp5() {
        return enrollFp5;
    }

    public void setEnrollFp5(byte[] enrollFp5) {
        this.enrollFp5 = enrollFp5;
    }

    public byte[] getEnrollFp6() {
        return enrollFp6;
    }

    public void setEnrollFp6(byte[] enrollFp6) {
        this.enrollFp6 = enrollFp6;
    }

    public byte[] getEnrollFp7() {
        return enrollFp7;
    }

    public void setEnrollFp7(byte[] enrollFp7) {
        this.enrollFp7 = enrollFp7;
    }

    public byte[] getEnrollFp8() {
        return enrollFp8;
    }

    public void setEnrollFp8(byte[] enrollFp8) {
        this.enrollFp8 = enrollFp8;
    }

    public byte[] getEnrollFp9() {
        return enrollFp9;
    }

    public void setEnrollFp9(byte[] enrollFp9) {
        this.enrollFp9 = enrollFp9;
    }

    public byte[] getEnrollFp10() {
        return enrollFp10;
    }

    public void setEnrollFp10(byte[] enrollFp10) {
        this.enrollFp10 = enrollFp10;
    }

    public byte[] getEnrollPassword() {
        return enrollPassword;
    }

    public void setEnrollPassword(byte[] enrollPassword) {
        this.enrollPassword = enrollPassword;
    }

    public byte[] getEnrollFace() {
        return enrollFace;
    }

    public void setEnrollFace(byte[] enrollFace) {
        this.enrollFace = enrollFace;
    }

    public byte[] getEnrollCard() {
        return enrollCard;
    }

    public void setEnrollCard(byte[] enrollCard) {
        this.enrollCard = enrollCard;
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
        LabClockInUserInfoWithBLOBs other = (LabClockInUserInfoWithBLOBs) that;
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
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getEnrollPhoto() == null ? other.getEnrollPhoto() == null : this.getEnrollPhoto().equals(other.getEnrollPhoto()))
            && (this.getEnrollFp1() == null ? other.getEnrollFp1() == null : this.getEnrollFp1().equals(other.getEnrollFp1()))
            && (this.getEnrollFp2() == null ? other.getEnrollFp2() == null : this.getEnrollFp2().equals(other.getEnrollFp2()))
            && (this.getEnrollFp3() == null ? other.getEnrollFp3() == null : this.getEnrollFp3().equals(other.getEnrollFp3()))
            && (this.getEnrollFp4() == null ? other.getEnrollFp4() == null : this.getEnrollFp4().equals(other.getEnrollFp4()))
            && (this.getEnrollFp5() == null ? other.getEnrollFp5() == null : this.getEnrollFp5().equals(other.getEnrollFp5()))
            && (this.getEnrollFp6() == null ? other.getEnrollFp6() == null : this.getEnrollFp6().equals(other.getEnrollFp6()))
            && (this.getEnrollFp7() == null ? other.getEnrollFp7() == null : this.getEnrollFp7().equals(other.getEnrollFp7()))
            && (this.getEnrollFp8() == null ? other.getEnrollFp8() == null : this.getEnrollFp8().equals(other.getEnrollFp8()))
            && (this.getEnrollFp9() == null ? other.getEnrollFp9() == null : this.getEnrollFp9().equals(other.getEnrollFp9()))
            && (this.getEnrollFp10() == null ? other.getEnrollFp10() == null : this.getEnrollFp10().equals(other.getEnrollFp10()))
            && (this.getEnrollPassword() == null ? other.getEnrollPassword() == null : this.getEnrollPassword().equals(other.getEnrollPassword()))
            && (this.getEnrollFace() == null ? other.getEnrollFace() == null : this.getEnrollFace().equals(other.getEnrollFace()))
            && (this.getEnrollCard() == null ? other.getEnrollCard() == null : this.getEnrollCard().equals(other.getEnrollCard()));
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
        result = prime * result + ((getEnrollPhoto() == null) ? 0 : getEnrollPhoto().hashCode());
        result = prime * result + ((getEnrollFp1() == null) ? 0 : getEnrollFp1().hashCode());
        result = prime * result + ((getEnrollFp2() == null) ? 0 : getEnrollFp2().hashCode());
        result = prime * result + ((getEnrollFp3() == null) ? 0 : getEnrollFp3().hashCode());
        result = prime * result + ((getEnrollFp4() == null) ? 0 : getEnrollFp4().hashCode());
        result = prime * result + ((getEnrollFp5() == null) ? 0 : getEnrollFp5().hashCode());
        result = prime * result + ((getEnrollFp6() == null) ? 0 : getEnrollFp6().hashCode());
        result = prime * result + ((getEnrollFp7() == null) ? 0 : getEnrollFp7().hashCode());
        result = prime * result + ((getEnrollFp8() == null) ? 0 : getEnrollFp8().hashCode());
        result = prime * result + ((getEnrollFp9() == null) ? 0 : getEnrollFp9().hashCode());
        result = prime * result + ((getEnrollFp10() == null) ? 0 : getEnrollFp10().hashCode());
        result = prime * result + ((getEnrollPassword() == null) ? 0 : getEnrollPassword().hashCode());
        result = prime * result + ((getEnrollFace() == null) ? 0 : getEnrollFace().hashCode());
        result = prime * result + ((getEnrollCard() == null) ? 0 : getEnrollCard().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", enrollPhoto=").append(enrollPhoto);
        sb.append(", enrollFp1=").append(enrollFp1);
        sb.append(", enrollFp2=").append(enrollFp2);
        sb.append(", enrollFp3=").append(enrollFp3);
        sb.append(", enrollFp4=").append(enrollFp4);
        sb.append(", enrollFp5=").append(enrollFp5);
        sb.append(", enrollFp6=").append(enrollFp6);
        sb.append(", enrollFp7=").append(enrollFp7);
        sb.append(", enrollFp8=").append(enrollFp8);
        sb.append(", enrollFp9=").append(enrollFp9);
        sb.append(", enrollFp10=").append(enrollFp10);
        sb.append(", enrollPassword=").append(enrollPassword);
        sb.append(", enrollFace=").append(enrollFace);
        sb.append(", enrollCard=").append(enrollCard);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}