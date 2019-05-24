package com.app.pipelinesurvey.bean;

/**
 * Created by HaiRun on 2018/11/29 0029.
 * 设置界面数据
 */

public class PointConfig {
    public String groupName="01";        //组号
    public int    groupLength=2;      //组号长度
    public int    local;            //组号位置
    public int    pointNumLenght=3;   //流水号长度
    public int    pipeLength=4;       //管线自定义长度
    public static PointConfig s_PointConfig;

    public static PointConfig getPointConfig() {
        if (s_PointConfig == null) {
            s_PointConfig = new PointConfig();
        }
        return s_PointConfig;
    }



    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupLength() {
        return groupLength;
    }

    public void setGroupLength(int groupLength) {
        this.groupLength = groupLength;
    }

    public int getLocal() {
        return local;
    }

    public void setLocal(int local) {
        this.local = local;
    }

    public int getPointNumLenght() {
        return pointNumLenght;
    }

    public void setPointNumLenght(int pointNumLenght) {
        this.pointNumLenght = pointNumLenght;
    }

    public int getPipeLength() {
        return pipeLength;
    }

    public void setPipeLength(int pipeLength) {
        this.pipeLength = pipeLength;
    }
}
