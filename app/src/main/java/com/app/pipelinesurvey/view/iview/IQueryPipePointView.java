package com.app.pipelinesurvey.view.iview;

import java.util.List;

/**
 * Created by hairun on 2018-07-10 17:31.
 */

public interface IQueryPipePointView {

    /**
     * 设置物探点号
     */
    void setGPId();

    /**
     * 设置物探点号状况
     */
    void setSituation();

    /**
     * 设置特征点
     */
    void setFeaturePoints();

    /**
     * 设置附属物
     */
    void setAppendant();

    /**
     *设置管点状态
     */
    void setState();

    /**
     *设置高程
     */
    void setElevation();

    /**
     *设置管偏
     */
    void setOffset();

    /**
     *设置建构筑物
     */
    void setBuildingStructures();

    /**
     *设置路名
     */
    void setRoadName();

    /**
     *设置点备注
     */
    void setPointRemark();
    /**
     *设置疑难问题
     */

    void setPuzzle();

    /**
     *设置管径
     */
    void setWellSize();

    /**
     *设置井深
     */
    void setWellDepth();

    /**
     *设置水深
     */
    void setWellWater();

    /**
     *设置淤泥
     */
    void setWellMud();

    /**
     *设置井盖材质
     */
    void setWellLidTexture();
    
    /**
     *设置井盖规格
     */
    void setWellLidSize();

    /**
     * 获取经度
     * @return
     */
    double getLongitude();

    /**
     * 获取纬度
     * @return
     */
    double getLatitude();

    /**
     *  从数据中获取照片名字
     * @return
     */
    List<String> getPicturefromReSet();
}
