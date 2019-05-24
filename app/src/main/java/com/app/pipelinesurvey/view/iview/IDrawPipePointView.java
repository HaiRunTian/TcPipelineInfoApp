package com.app.pipelinesurvey.view.iview;

/**
 * Created by Kevin on 2018-06-05.
 */

public interface IDrawPipePointView {
    /**
     * 设置物探点号
     */
    void setGPId();

    /**
     *
     * @return 取物探点号
     */
    String getGPId();

    /**
     *
     * @return  取物探点号状况
     */
    String getSituation();
    /**
     *
     * @return 获取特征点
     */
    String getFeaturePoints();
    /**
     *
     * @return  /获取附属物
     */
    String getAppendant();//获取附属物
    /**
     *
     * @return  获取管点状态
     */
    String getState();
    /**
     *
     * @return  获取高程
     */
    String getElevation();
     /**
     *
     * @return  获取管偏
     */
    String getOffset();

    /**
     *
     * @return 获取建构筑物
     */
    String getBuildingStructures();

    /**
     *
     * @return 获取路名
     */
    String getRoadName();

    /**
     *
     * @return 获取点备注
     */
    String getPointRemark();
    /**
     *
     * @return 获取疑难问题
     */
    String getPuzzle();
    /**
     *
     * @return 获取管径
     */
    String getWellSize();
    /**
     *
     * @return 获取井深
     */
    String getWellDepth();
    /**
     *
     * @return 获取水深
     */
    String getWellWater();
    /**
     *
     * @return 获取淤泥
     */
    String getWellMud();
    /**
     *
     * @return 获取井盖材质
     */
    String getWellLidTexture();
    /**
     *
     * @return 获取井盖规格
     */
    String getWellLidSize();

    /**
     * 获取照片名字
     * @return
     */
    String getPictureName();

    /**
     * 获取管点深度
     * @return
     */
    String getDepth();
}
