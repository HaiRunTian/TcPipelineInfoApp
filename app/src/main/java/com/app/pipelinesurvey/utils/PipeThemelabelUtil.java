package com.app.pipelinesurvey.utils;

import com.app.pipelinesurvey.config.SuperMapConfig;

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
        if (SuperMapConfig.PROJECT_CITY_NAME.equals("惠州")) {
            switch (pipeType) {
                case "合流":
                case "PS":
                    _value = 0.5;
                    break;
                case "雨水":
                case "YS":
                    _value = 1.5;
                    break;
                case "污水":
                case "WS":
                    _value = 2.5;
                    break;
                case "给水":
                case "JS":
                    _value = 3.5;
                    break;
                case "燃气":
                case "RQ":
                    _value = 5.5;
                    break;
                case "高压":
                case "低压":
                case "LG":
                case "LD":
                    _value = 6.5;
                    break;
                case "路灯":
                case "LS":
                    _value = 7.5;
                    break;
                case "电信":
                case "监控":
                case "移动":
                case "联通":
                case "盈通":
                case "LJ":
                case "DX":
                case "DD":
                case "DL":
                case "DY":
                    _value = 8.5;
                    break;
                case "有视":
                case "军队":
                case "DT":
                case "DB":
                    _value = 9.5;
                    break;
                case "交通":
                case "LX":
                    _value = 11.5;
                    break;
                case "工业":
                case "GY":
                    _value = 12.5;
                    break;
                case "不明":
                case "其他":
                case "综合":
                case "BM":
                case "ZH":
                    _value = 13.5;
                    break;
                case "临时":
                case "O":
                    _value = 15.5;
                    break;
                default:
                    _value = 0.5;
                    break;
            }

        } else if (SuperMapConfig.PROJECT_CITY_NAME.equals("深圳")) {
            //深圳
            switch (pipeType) {
                case "排水":
                case "P":
                    _value = 0.5;
                    break;
                case "雨水":
                case "Y":
                    _value = 1.5;
                    break;
                case "污水":
                case "W":
                    _value = 2.5;
                    break;
                case "给水":
                case "J":
                    _value = 3.5;
                    break;
                case "煤气":
                case "M":
                    _value = 4.5;
                    break;
                case "燃气":
                case "R":
                    _value = 5.5;
                    break;
                case "电力":
                case "L":
                    _value = 6.5;
                    break;
                case "路灯":
                case "LD":
                    _value = 7.5;
                    break;
                case "电信":
                case "D":
                    _value = 8.5;
                    break;
                case "有视":
                case "T":
                    _value = 9.5;
                    break;
                case "军队":
                case "B":
                    _value = 10.5;
                    break;
                case "信号":
                case "XH":
                    _value = 11.5;
                    break;
                case "工业":
                case "G":
                    _value = 12.5;
                    break;
                case "不明":
                case "N":
                    _value = 13.5;
                    break;
                case "其它":
                case "Q":
                    _value = 14.5;
                    break;
                case "临时":
                case "O":
                    _value = 15.5;
                    break;
                case "合流":
                case "H":
                    _value = 16.5;
                    break;
                default:
                    _value = 0.5;
                    break;
            }
        } else if (SuperMapConfig.PROJECT_CITY_NAME.equals("正本清源")) {
            switch (pipeType){
                case "给水":
                case "JS":
                    _value = 0.5;
                    break;
                case "污水":
                case "WS":
                    _value = 1.5;
                    break;
                case "排水":
                case "PS":
                    _value = 2.5;
                    break;
                case "合流":
                case "HS":
                    _value = 3.5;
                    break;
                case "雨水":
                case "YS":
                    _value = 4.5;
                    break;
                case "燃气":
                case "RQ":
                    _value = 5.5;
                    break;
                case "工业":
                case "GY":
                    _value = 6.5;
                    break;
                case "电力":
                case "DL":
                    _value = 7.5;
                    break;
                case "供电":
                case "GD":
                    _value = 8.5;
                    break;
                case "信号":
                case "XH":
                    _value = 9.5;
                    break;
                case "路灯":
                case "LD":
                    _value = 10.5;
                    break;
                case "通信":
//                case "TX":
                    _value = 11.5;
                    break;
                case "电信":
                case "DX":
                    _value = 12.5;
                    break;
                case "联通":
                case "LX":
                    _value = 13.5;
                    break;
                case "移动":
                case "YX":
                    _value = 14.5;
                    break;
                case "铁通":
                case "TX":
                    _value = 15.5;
                    break;
                case "吉通":
                case "GX":
                    _value = 16.5;
                    break;
                case "网通":
                case "WX":
                    _value = 17.5;
                    break;
                case "盈通":
                case "IX":
                    _value = 18.5;
                    break;
                case "军用":
                case "JX":
                    _value = 19.5;
                    break;
                case "保密":
                case "BX":
                    _value = 20.5;
                    break;
                case "其他":
                case "DQ":
                    _value = 21.5;
                    break;
                case "监控":
                case "JK":
                    _value = 22.5;
                    break;
                case "电通":
                case "DT":
                    _value = 23.5;
                    break;
                case "广电":
                case "DS":
                    _value = 24.5;
                    break;
                case "临时":
                case "O":
                    _value = 25.5;
                    break;
                    default:
                    _value = 25.5;
                    break;
            }
        } else {
            //广州
            switch (pipeType) {
                case "排水":
                case "P":
                case "合流":
                case "H":
                    _value = 0.5;
                    break;
                case "雨水":
                case "Y":
                    _value = 1.5;
                    break;
                case "污水":
                case "W":
                    _value = 2.5;
                    break;
                case "给水":
                case "J":
                    _value = 3.5;
                    break;
                case "煤气":
                case "M":
                    _value = 4.5;
                    break;
                case "燃气":
                case "R":
                    _value = 5.5;
                    break;
                case "电力":
                case "L":
                    _value = 6.5;
                    break;
                case "路灯":
                case "S":
                    _value = 7.5;
                    break;
                case "电信":
                case "D":
                    _value = 8.5;
                    break;
                case "有视":
                case "T":
                    _value = 9.5;
                    break;
                case "军队":
                case "B":
                    _value = 10.5;
                    break;
                case "交通":
                case "X":
                    _value = 11.5;
                    break;
                case "工业":
                case "G":
                    _value = 12.5;
                    break;
                case "不明":
                case "N":
                    _value = 13.5;
                    break;
                case "其它":
                case "Q":
                    _value = 14.5;
                    break;
                case "临时":
                case "O":
                    _value = 15.5;
                    break;
                default:
                    _value = 0.5;
                    break;
            }
        }

        return _value;
    }
}
