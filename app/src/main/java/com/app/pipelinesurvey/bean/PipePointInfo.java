package com.app.pipelinesurvey.bean;

/**
 * Created by Kevin on 2018-06-07.
 */

public class PipePointInfo {
    public String GPId;//物探点号
    public String featurePoints;//特征点
    public String appendant;//附属物
    public String wellSize;//井规格
    public String wellDepth;//井深
    public String wellWater;//水深
    public String wellMud;//淤泥
    public String wellLidTexture;//井盖材质
    public String wellLidSize;//井盖规格
    public String state;//状态
    public String elevation;//高程
    public String offset;//管偏
    public String buildingStructures;//建构筑物
    public String roadName;//路名
    public String pointRemark;//点备注
    public String puzzle;//疑难
    public String situation;//点情况

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public static void setPipePointInfo(PipePointInfo pipePointInfo) {
        s_pipePointInfo = pipePointInfo;
    }

    public static PipePointInfo s_pipePointInfo;

    public static PipePointInfo getPipePointInfo() {
        if (s_pipePointInfo == null) {
            s_pipePointInfo = new PipePointInfo();
        }
        return s_pipePointInfo;
    }
    public String getGPId() {
        return GPId;
    }

    public void setGPId(String GPId) {
        this.GPId = GPId;
    }

    public String getFeaturePoints() {
        return featurePoints;
    }

    public void setFeaturePoints(String featurePoints) {
        this.featurePoints = featurePoints;
    }

    public String getAppendant() {
        return appendant;
    }

    public void setAppendant(String appendant) {
        this.appendant = appendant;
    }

    public String getWellSize() {
        return wellSize;
    }

    public void setWellSize(String wellSize) {
        this.wellSize = wellSize;
    }

    public String getWellDepth() {
        return wellDepth;
    }

    public void setWellDepth(String wellDepth) {
        this.wellDepth = wellDepth;
    }

    public String getWellWater() {
        return wellWater;
    }

    public void setWellWater(String wellWater) {
        this.wellWater = wellWater;
    }

    public String getWellMud() {
        return wellMud;
    }

    public void setWellMud(String wellMud) {
        this.wellMud = wellMud;
    }

    public String getWellLidTexture() {
        return wellLidTexture;
    }

    public void setWellLidTexture(String wellLidTexture) {
        this.wellLidTexture = wellLidTexture;
    }

    public String getWellLidSize() {
        return wellLidSize;
    }

    public void setWellLidSize(String wellLidSize) {
        this.wellLidSize = wellLidSize;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getBuildingStructures() {
        return buildingStructures;
    }

    public void setBuildingStructures(String buildingStructures) {
        this.buildingStructures = buildingStructures;
    }
    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getPointRemark() {
        return pointRemark;
    }

    public void setPointRemark(String pointRemark) {
        this.pointRemark = pointRemark;
    }

    public String getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(String puzzle) {
        this.puzzle = puzzle;
    }
}
