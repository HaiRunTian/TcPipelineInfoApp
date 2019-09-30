package com.app.pipelinesurvey.bean.setting;

/**
 * @author HaiRun
 * @time 2019/9/20.11:02
 */
public class ChildPoint {
    /**
     * 中文字段名字
     */
    private String name;
    /**
     * 数据库字段名字
     */
    private String sqlName;
    /**
     * 是否显示
     */
    private int show;


    public ChildPoint() {
    }

    public ChildPoint(String name, String sqlName, int show) {
        this.name = name;
        this.sqlName = sqlName;
        this.show = show;
    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "ChildPoint{" +
                "name='" + name + '\'' +
                ", sqlName='" + sqlName + '\'' +
                ", show=" + show +
                '}';
    }
}
