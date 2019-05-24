package com.app.pipelinesurvey.view.iview;

/**
 * Created by Kevin on 2018-06-27.
 */

public interface IPointAttrConfigView {
    /**
     * 点附属物或其它的名字
     * @return
     */
    String getName();

    /**
     * 颜色
     * @return
     */
    String getColor();

    /**
     * 横向缩放
     * @return
     */
    String getScaleX();

    /**
     * 竖向缩放
     * @return
     */
    String getScaleY();

    /**
     * 旋转角度
     * @return
     */
    String getAngle();

    /**
     * 符号id
     * @return
     */
    String getSymbolID();

    /**
     * 从属的标准
     * @return
     */
    String getStandard();

    /**
     * 最小缩放比例
     * @return
     */
    String getMinScaleVisible();

    /**
     * 最大缩放比例
     * @return
     */
    String getMaxScaleVisble();

    /**
     * 线宽度
     * @return
     */
    String getLineWidth();

    /**
     * 管线类型
     * @return
     */
    String getPipeType();
}
