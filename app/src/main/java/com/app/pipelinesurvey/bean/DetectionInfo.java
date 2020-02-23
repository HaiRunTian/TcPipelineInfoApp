package com.app.pipelinesurvey.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author HaiRun
 * @time 2019/7/1.14:03
 */
public class DetectionInfo implements Parcelable {
    public String prjCode;
    public String prjName;
    public String prjSite;
    public String original;
    public String apparatusName1;
    public String apparatusCode1;
    public String apparatusName2;
    public String apparatusCode2;
    public String detectionStandard;
    public String serialNum;
    public String detectionDate;
    public String pointName;
    public String workload;
    public String detectionMethod;
    public String detectionMap;
    public String groupLeader;
    public String groupMember1;
    public String groupMember2;
    public String date;
    public String remark;
    public DetectionInfo() {

    }

    protected DetectionInfo(Parcel in) {
        prjCode = in.readString();
        prjName = in.readString();
        prjSite = in.readString();
        original = in.readString();
        apparatusName1 = in.readString();
        apparatusCode1 = in.readString();
        apparatusName2 = in.readString();
        apparatusCode2 = in.readString();
        detectionStandard = in.readString();
        serialNum = in.readString();
        detectionDate = in.readString();
        pointName = in.readString();
        workload = in.readString();
        detectionMethod = in.readString();
        detectionMap = in.readString();
        groupLeader = in.readString();
        groupMember1 = in.readString();
        groupMember2 = in.readString();
        date = in.readString();
        remark = in.readString();
    }

    public static final Creator<DetectionInfo> CREATOR = new Creator<DetectionInfo>() {
        @Override
        public DetectionInfo createFromParcel(Parcel in) {
            return new DetectionInfo(in);
        }

        @Override
        public DetectionInfo[] newArray(int size) {
            return new DetectionInfo[size];
        }
    };

    public String getPrjCode() {
        return prjCode;
    }

    public void setPrjCode(String prjCode) {
        this.prjCode = prjCode;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public String getPrjSite() {
        return prjSite;
    }

    public void setPrjSite(String prjSite) {
        this.prjSite = prjSite;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getApparatusName1() {
        return apparatusName1;
    }

    public void setApparatusName1(String apparatusName1) {
        this.apparatusName1 = apparatusName1;
    }

    public String getApparatusCode1() {
        return apparatusCode1;
    }

    public void setApparatusCode1(String apparatusCode1) {
        this.apparatusCode1 = apparatusCode1;
    }

    public String getApparatusName2() {
        return apparatusName2;
    }

    public void setApparatusName2(String apparatusName2) {
        this.apparatusName2 = apparatusName2;
    }

    public String getApparatusCode2() {
        return apparatusCode2;
    }

    public void setApparatusCode2(String apparatusCode2) {
        this.apparatusCode2 = apparatusCode2;
    }

    public String getDetectionStandard() {
        return detectionStandard;
    }

    public void setDetectionStandard(String detectionStandard) {
        this.detectionStandard = detectionStandard;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getDetectionDate() {
        return detectionDate;
    }

    public void setDetectionDate(String detectionDate) {
        this.detectionDate = detectionDate;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public String getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(String detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public String getDetectionMap() {
        return detectionMap;
    }

    public void setDetectionMap(String detectionMap) {
        this.detectionMap = detectionMap;
    }

    public String getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(String groupLeader) {
        this.groupLeader = groupLeader;
    }

    public String getGroupMember1() {
        return groupMember1;
    }

    public void setGroupMember1(String groupMember1) {
        this.groupMember1 = groupMember1;
    }

    public String getGroupMember2() {
        return groupMember2;
    }

    public void setGroupMember2(String groupMember2) {
        this.groupMember2 = groupMember2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DetectionInfo{" +
                "prjCode='" + prjCode + '\'' +
                ", prjName='" + prjName + '\'' +
                ", prjSite='" + prjSite + '\'' +
                ", original='" + original + '\'' +
                ", apparatusName1='" + apparatusName1 + '\'' +
                ", apparatusCode1='" + apparatusCode1 + '\'' +
                ", apparatusName2='" + apparatusName2 + '\'' +
                ", apparatusCode2='" + apparatusCode2 + '\'' +
                ", detectionStandard='" + detectionStandard + '\'' +
                ", serialNum='" + serialNum + '\'' +
                ", detectionDate='" + detectionDate + '\'' +
                ", pointName='" + pointName + '\'' +
                ", workload='" + workload + '\'' +
                ", detectionMethod='" + detectionMethod + '\'' +
                ", detectionMap='" + detectionMap + '\'' +
                ", groupLeader='" + groupLeader + '\'' +
                ", groupMember1='" + groupMember1 + '\'' +
                ", groupMember2='" + groupMember2 + '\'' +
                ", date='" + date + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prjCode);
        dest.writeString(prjName);
        dest.writeString(prjSite);
        dest.writeString(original);
        dest.writeString(apparatusName1);
        dest.writeString(apparatusCode1);
        dest.writeString(apparatusName2);
        dest.writeString(apparatusCode2);
        dest.writeString(detectionStandard);
        dest.writeString(serialNum);
        dest.writeString(detectionDate);
        dest.writeString(pointName);
        dest.writeString(workload);
        dest.writeString(detectionMethod);
        dest.writeString(detectionMap);
        dest.writeString(groupLeader);
        dest.writeString(groupMember1);
        dest.writeString(groupMember2);
        dest.writeString(date);
        dest.writeString(remark);
    }
}
