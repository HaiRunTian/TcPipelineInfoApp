package com.app.pipelinesurvey.bean;

/**
 * Created by Kevin on 2018-06-07.
 */

public class PipeLineInfo {
    public String startPoint;//开始点号
    public String endPoint;//连接点号
    public String startBurialDepth;//开始埋深
    public String endBurialDepth;//终止埋深
    public String pipeLength;//管长
    public String burialDifference;//埋深差值
    public String embeddedWay;//埋设方式
    public String textrure;//管材
    public String holeCount;//孔数
    public String usedHoleCount;//已用孔数
    public String amount;//电缆数
    public String aperture;//孔径
    public String row;//行
    public String col;//列
    public String voltage;//电压
    public String state;//状态
    public String pressure;//压力
    public String ownershipUnit;//权属单位
    public String lineRemark;//线备注
    public String puzzle;//疑难
    public String pipeSize;//管径
    public String sectionWidth;//断面宽
    public String sectionHeight;//断面高
    public static PipeLineInfo s_pipeLineInfo;

    public static PipeLineInfo getPipeLineInfo() {
        if (s_pipeLineInfo == null) {
            s_pipeLineInfo = new PipeLineInfo();
        }
        return s_pipeLineInfo;
    }

    public String getPipeSize() {
        return pipeSize;
    }

    public void setPipeSize(String pipeSize) {
        this.pipeSize = pipeSize;
    }

    public String getSectionWidth() {
        return sectionWidth;
    }

    public void setSectionWidth(String sectionWidth) {
        this.sectionWidth = sectionWidth;
    }

    public String getSectionHeight() {
        return sectionHeight;
    }

    public void setSectionHeight(String sectionHeight) {
        this.sectionHeight = sectionHeight;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getStartBurialDepth() {
        return startBurialDepth;
    }

    public void setStartBurialDepth(String startBurialDepth) {
        this.startBurialDepth = startBurialDepth;
    }

    public String getEndBurialDepth() {
        return endBurialDepth;
    }

    public void setEndBurialDepth(String endBurialDepth) {
        this.endBurialDepth = endBurialDepth;
    }

    public String getPipeLength() {
        return pipeLength;
    }

    public void setPipeLength(String pipeLength) {
        this.pipeLength = pipeLength;
    }

    public String getBurialDifference() {
        return burialDifference;
    }

    public void setBurialDifference(String burialDifference) {
        this.burialDifference = burialDifference;
    }

    public String getEmbeddedWay() {
        return embeddedWay;
    }

    public void setEmbeddedWay(String embeddedWay) {
        this.embeddedWay = embeddedWay;
    }

    public String getTextrure() {
        return textrure;
    }

    public void setTextrure(String textrure) {
        this.textrure = textrure;
    }

    public String getHoleCount() {
        return holeCount;
    }

    public void setHoleCount(String holeCount) {
        this.holeCount = holeCount;
    }

    public String getUsedHoleCount() {
        return usedHoleCount;
    }

    public void setUsedHoleCount(String usedHoleCount) {
        this.usedHoleCount = usedHoleCount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getOwnershipUnit() {
        return ownershipUnit;
    }

    public void setOwnershipUnit(String ownershipUnit) {
        this.ownershipUnit = ownershipUnit;
    }

    public String getLineRemark() {
        return lineRemark;
    }

    public void setLineRemark(String lineRemark) {
        this.lineRemark = lineRemark;
    }

    public String getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(String puzzle) {
        this.puzzle = puzzle;
    }

    public static void setPipeLineInfo(PipeLineInfo pipeLineInfo) {
        s_pipeLineInfo = pipeLineInfo;
    }

    @Override
    public String toString() {
        return "PipeLineInfo{" +
                "startPoint='" + startPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", startBurialDepth='" + startBurialDepth + '\'' +
                ", endBurialDepth='" + endBurialDepth + '\'' +
                ", pipeLength='" + pipeLength + '\'' +
                ", burialDifference='" + burialDifference + '\'' +
                ", embeddedWay='" + embeddedWay + '\'' +
                ", textrure='" + textrure + '\'' +
                ", holeCount='" + holeCount + '\'' +
                ", usedHoleCount='" + usedHoleCount + '\'' +
                ", amount='" + amount + '\'' +
                ", aperture='" + aperture + '\'' +
                ", row='" + row + '\'' +
                ", col='" + col + '\'' +
                ", voltage='" + voltage + '\'' +
                ", state='" + state + '\'' +
                ", pressure='" + pressure + '\'' +
                ", ownershipUnit='" + ownershipUnit + '\'' +
                ", lineRemark='" + lineRemark + '\'' +
                ", puzzle='" + puzzle + '\'' +
                ", pipeSize='" + pipeSize + '\'' +
                ", sectionWidth='" + sectionWidth + '\'' +
                ", sectionHeight='" + sectionHeight + '\'' +
                '}';
    }
}
