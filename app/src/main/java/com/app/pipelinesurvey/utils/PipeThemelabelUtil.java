package com.app.pipelinesurvey.utils;

/**
 * @Author HaiRun
 * @Time 2019/3/28.11:28
 * 管线标签专题图分段值设置
 */

public class PipeThemelabelUtil {

    public static PipeThemelabelUtil m_ins = null;

    public static PipeThemelabelUtil Ins() {
        if (m_ins == null) {
            m_ins = new PipeThemelabelUtil();
        }

        return m_ins;
    }

    public double getThemeItemValue(String pipeType) {
        double _value = 0.0;

        switch (pipeType) {
            case "排水":
                _value = 0.5;
                break;
            case "雨水":
                _value = 1.5;
                break;
            case "污水":
                _value = 2.5;
                break;
            case "给水":
                _value = 3.5;
                break;
            case "煤气":
                _value = 4.5;
                break;
            case "燃气":
                _value = 5.5;
                break;
            case "电力":
                _value = 6.5;
                break;
            case "路灯":
                _value = 7.5;
                break;
            case "电信":
                _value = 8.5;
                break;
            case "有视":
                _value = 9.5;
                break;
            case "军队":
                _value = 10.5;
                break;
            case "交通":
                _value = 11.5;
                break;
            case "工业":
                _value = 12.5;
                break;
            case "不明":
                _value = 13.5;
                break;
            case "其它":
                _value = 14.5;
                break;
            case "临时":
                _value = 15.5;
                break;
            default:
                _value = 0.5;
                break;
        }

        return _value;
    }
}
