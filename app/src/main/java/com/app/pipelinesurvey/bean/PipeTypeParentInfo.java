package com.app.pipelinesurvey.bean;

import java.util.List;

/**
 * Created by HaiRun on 2018/12/22.
 */

public class PipeTypeParentInfo {
    public String name;
    public List<PipeTypeChildInfo> m_childInfoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PipeTypeChildInfo> getChildInfoList() {
        return m_childInfoList;
    }

    public void setChildInfoList(List<PipeTypeChildInfo> childInfoList) {
        m_childInfoList = childInfoList;
    }
}
