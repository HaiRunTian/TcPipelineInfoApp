package com.app.pipelinesurvey.utils;

/**
 * 当前项目点号最大号
 * @author HaiRun
 * @time 2019/10/15.15:35
 */
public class MaxExpNumID {
    private static String prjName;
    private static int id = 0;

    public static MaxExpNumID maxExpNumID = null;

    public static MaxExpNumID getInstance(){
        if (maxExpNumID == null){
            maxExpNumID = new MaxExpNumID();
        }
        return maxExpNumID;
    }
    public MaxExpNumID() {

    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
