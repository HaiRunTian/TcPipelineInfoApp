package com.app.pipelinesurvey.bean;

/**
 * @author HaiRun
 * @time 2019/4/28.14:53
 * 点符号
 */

public class Symbolbean {
    /**
     * 点名字
     */
    private String name;
    /**
     * 点ID
     */
    private int symId ;
    /**
     * 图标资源id
     */
    private int rsId;

    public Symbolbean(String name, int symId, int rsId) {
        this.name = name;
        this.symId = symId;
        this.rsId = rsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSymId() {
        return symId;
    }

    public void setSymId(int symId) {
        this.symId = symId;
    }

    public int getRsId() {
        return rsId;
    }

    public void setRsId(int rsId) {
        this.rsId = rsId;
    }

    @Override
    public String toString() {
        return "Symbolbean{" +
                "name='" + name + '\'' +
                ", symId=" + symId +
                ", rsId=" + rsId +
                '}';
    }
}
