package com.app.pipelinesurvey.bean;

/**
 * Created by Kevin on 2018-06-26.
 * 软件信息表
 */

public class AppInfo {
    String app_name;
    String version_code;
    String updated_date;
    String dev_unit;
    String remark;

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getDev_unit() {
        return dev_unit;
    }

    public void setDev_unit(String dev_unit) {
        this.dev_unit = dev_unit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
