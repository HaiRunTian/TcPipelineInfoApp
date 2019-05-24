package com.app.pipelinesurvey.view.iview;

/**
 * Created by Kevin on 2018-06-05.
 */

public interface IQueryPipeLineView {

    /**
     *设置管线点号
     */
    void setStartPoint();
    /**
     *设置连接点号
     */
    void setEndPoint();
    /**
     *设置起点埋深
     */
    void setStartBurialDepth();
    /**
     *设置终点埋深
     */
    void setEndBurialDepth();
    /**
     *设置管段长度
     */
    void setPipeLength();
    /**
     *设置埋深差值
     */
    void setBurialDifference();
    /**
     *设置埋设方式
     */
    void setEmbeddedWay();
    /**
     *设置管径
     */
    void setPipeSize();
    /**
     *设置断面宽
     */
    void setSectionWidth();
    /**
     *设置断面高
     */
    void setSectionHeight();
    /**
     *设置總孔數
     */
    void setHoleCount();
    /**
     *设置已用孔数
     */
    void setUsedHoleCount();
    /**
     *设置电缆总数
     */
    void setAmount();
    /**
     *设置孔径
     */
    void setAperture();
    /**
     *设置行
     */
    void setRow();
    /**
     *设置列
     */
    void setCol();
    /**
     *设置电压
     */
    void setVoltage();
    /**
     *设置管线状态
     */
    void setState();
    /**
     *设置压力
     */
    void setPressure();
    /**
     *设置权属单位
     */
    void setOwnershipUnit();
    /**
     *设置线备注
     */
    void setLineRemark();
    /**
     *设置疑难问题
     */
    void setPuzzle();
    /**
     *设置管线材质
     */
    void setPipeTexture();
}
