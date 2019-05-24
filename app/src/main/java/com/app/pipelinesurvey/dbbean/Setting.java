package com.app.pipelinesurvey.dbbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目设置
 * @Author HaiRun
 * @Time 2019/2/14.15:27
 */
@Entity
public class Setting {
    @Id(autoincrement = true)
    private long id;
    private String groupName; //组号
    private int pipeLength; //管线长度提醒
    private int groupLocal; //组号的位置
    private int numLength;//流水号长度
    @Generated(hash = 978424239)
    public Setting(long id, String groupName, int pipeLength, int groupLocal,
            int numLength) {
        this.id = id;
        this.groupName = groupName;
        this.pipeLength = pipeLength;
        this.groupLocal = groupLocal;
        this.numLength = numLength;
    }
    @Generated(hash = 909716735)
    public Setting() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public int getPipeLength() {
        return this.pipeLength;
    }
    public void setPipeLength(int pipeLength) {
        this.pipeLength = pipeLength;
    }
    public int getGroupLocal() {
        return this.groupLocal;
    }
    public void setGroupLocal(int groupLocal) {
        this.groupLocal = groupLocal;
    }
    public int getNumLength() {
        return this.numLength;
    }
    public void setNumLength(int numLength) {
        this.numLength = numLength;
    }

}
