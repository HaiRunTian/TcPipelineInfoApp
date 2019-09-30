package com.app.pipelinesurvey.bean.setting;

import java.util.List;

/**
 * @author HaiRun
 * @time 2019/9/20.11:01
 */
public class FatherPoint {
    private String pipeType;
    private List<ChildPoint> list;

    public FatherPoint() {
    }

    public String getPipeType() {
        return pipeType;
    }

    public void setPipeType(String pipeType) {
        this.pipeType = pipeType;
    }

    public List<ChildPoint> getList() {
        return list;
    }

    public void setList(List<ChildPoint> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "FatherPoint{" +
                "pipeType='" + pipeType + '\'' +
                ", list=" + list +
                '}';
    }
}
