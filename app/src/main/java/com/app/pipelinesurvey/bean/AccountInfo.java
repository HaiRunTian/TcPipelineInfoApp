package com.app.pipelinesurvey.bean;

/**
 * Created by Kevin on 2018-06-21.
 * 用户信息表
 */

public class AccountInfo {
    private String name;
    private String sex;
    private String phone;
    private String company;
    private String workGroup;
    private String department;
    public static AccountInfo s_accountInfo;

    public static AccountInfo getAccountInfo() {
        if (s_accountInfo == null) {
            s_accountInfo = new AccountInfo();
        }
        return s_accountInfo;
    }

    public static void setAccountInfo(AccountInfo accountInfo) {
        s_accountInfo = accountInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWorkGroup() {
        return workGroup;
    }

    public void setWorkGroup(String workGroup) {
        this.workGroup = workGroup;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
