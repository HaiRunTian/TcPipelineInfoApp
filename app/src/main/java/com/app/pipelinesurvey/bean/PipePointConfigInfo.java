package com.app.pipelinesurvey.bean;

import java.io.Serializable;

/**
 * Created by Kevin on 2018-06-26.
 * 管点配置表
 */

public class PipePointConfigInfo implements Serializable{
    private int id;
    private String name;//附属物
    private String color;  //颜色
    private double scaleX; //符号放大倍数 X方向
    private double scaleY; //符号放大倍数 Y方向
    private double angle; //旋转角度
    private String symbolID; //符号ID
    private String standard; //从属标准
    private double minScaleVisible; //最小可见比例
    private double maxScaleVisble; //最大可见比例
    private double lineWidth; //符号线宽度
    private String pipeType;  //管线类型

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public String getSymbolID() {
        return symbolID;
    }

    public void setSymbolID(String symbolID) {
        this.symbolID = symbolID;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public double getMinScaleVisible() {
        return minScaleVisible;
    }

    public void setMinScaleVisible(double minScaleVisible) {
        this.minScaleVisible = minScaleVisible;
    }

    public double getMaxScaleVisble() {
        return maxScaleVisble;
    }

    public void setMaxScaleVisble(double maxScaleVisble) {
        this.maxScaleVisble = maxScaleVisble;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    public String getPipeType() {
        return pipeType;
    }

    public void setPipeType(String pipeType) {
        this.pipeType = pipeType;
    }
}
