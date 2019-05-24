package com.app.pipelinesurvey.dbbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目表
 * @Author HaiRun
 * @Time 2019/2/14.15:09
 */

@Entity
public class Items {
    @Id(autoincrement = true)
    private long id; //主键ID
    private String itemName; //项目名称
    private String cityName; //城市名称
    private String creator; //创建人
    private String change_man; //修改人
    private String createTime; //创建时间
    private String last_Time; //最后修改时间
    private String itemAddress; //项目地址
    private String baseMap_path; //项目地图路径
    @Generated(hash = 2094475011)
    public Items(long id, String itemName, String cityName, String creator,
            String change_man, String createTime, String last_Time,
            String itemAddress, String baseMap_path) {
        this.id = id;
        this.itemName = itemName;
        this.cityName = cityName;
        this.creator = creator;
        this.change_man = change_man;
        this.createTime = createTime;
        this.last_Time = last_Time;
        this.itemAddress = itemAddress;
        this.baseMap_path = baseMap_path;
    }
    @Generated(hash = 1040818858)
    public Items() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getItemName() {
        return this.itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getCityName() {
        return this.cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCreator() {
        return this.creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getChange_man() {
        return this.change_man;
    }
    public void setChange_man(String change_man) {
        this.change_man = change_man;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getLast_Time() {
        return this.last_Time;
    }
    public void setLast_Time(String last_Time) {
        this.last_Time = last_Time;
    }
    public String getItemAddress() {
        return this.itemAddress;
    }
    public void setItemAddress(String itemAddress) {
        this.itemAddress = itemAddress;
    }
    public String getBaseMap_path() {
        return this.baseMap_path;
    }
    public void setBaseMap_path(String baseMap_path) {
        this.baseMap_path = baseMap_path;
    }


}
