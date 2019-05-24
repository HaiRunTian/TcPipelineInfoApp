package com.app.pipelinesurvey.bean;

/**
 * Created by HaiRun on 2018/12/22.
 */

public class PipeTypeChildInfo {
    public String name;
    public boolean check;

    public PipeTypeChildInfo(String name, boolean check) {
        this.name = name;
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
