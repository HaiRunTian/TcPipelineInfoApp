package com.app.pipelinesurvey.bean.setting;

/**
 * @author HaiRun
 * @time 2019/9/20.9:39
 */
public class PipeTypeStting {
    private String pipeTyep;
    private int show;
    private String code;
    private String prjName;

    public PipeTypeStting() {
    }

    public String getPipeTyep() {
        return pipeTyep;
    }

    public void setPipeTyep(String pipeTyep) {
        this.pipeTyep = pipeTyep;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    @Override
    public String toString() {
        return "PipeTypeStting{" +
                "pipeTyep='" + pipeTyep + '\'' +
                ", show=" + show +
                ", code=" + code +
                ", prjName=" + prjName +
                '}';
    }
}
