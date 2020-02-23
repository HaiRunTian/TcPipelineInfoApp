package com.app.pipelinesurvey.view.iview;

/**
 * Created by Kevin on 2018-06-05.
 */

public interface IDrawPipeLineView {

    /**
     * 设置管线点号
     */
    void setStartPoint();

    /**
     *设置连接点号
     */
    void setEndPoint();

    /**
     *获取管线点号
     */
    String getStartPoint();

    /**
     *获取连接点号
     */
    String getEndPoint();

    /**
     *获取起点埋深
     */
    String getStartBurialDepth();

    /**
     *获取终点埋深
     */
    String getEndBurialDepth();

    /**
     *获取管段长度
     */
    String getPipeLength();

    /**
     *获取埋深差值
     */
    String getBurialDifference();

    /**
     *获取埋设方式
     */
    String getEmbeddedWay();

    /**
     *获取管径
     */
    String getPipeSize();

    /**
     *获取断面宽
     */
    String getSectionWidth();

    /**
     *获取断面高
     */
    String getSectionHeight();

    /**
     *获取管材
     */
    String getTextrure();

    /**
     *获取總孔數
     */
    String getHoleCount();

    /**
     *获取已用孔数
     */
    String getUsedHoleCount();

    /**
     *获取电缆总数
     */
    String getAmount();

    /**
     *获取孔径
     */
    String getAperture();

    /**
     *获取行
     */
    String getRow();

    /**
     *获取列
     */
    String getCol();

    /**
     *获取电压
     */
    String getVoltage();

    /**
     *获取管线状态
     */
    String getState();

    /**
     *获取压力
     */
    String getPressure();

    /**
     *获取权属单位
     */
    String getOwnershipUnit();

    /**
     *获取线备注
     */
    String getLineRemark();

    /**
     *获取疑难问题
     */
    String getPuzzle();
}
