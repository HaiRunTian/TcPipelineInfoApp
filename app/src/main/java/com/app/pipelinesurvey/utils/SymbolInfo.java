package com.app.pipelinesurvey.utils;

import android.support.annotation.NonNull;

/**
 * @author HaiRun
 * @time 2019/4/23.11:22
 */

public class SymbolInfo {

    public static SymbolInfo m_ins = null;

    public static SymbolInfo Ins() {
        if (m_ins == null) {
            m_ins = new SymbolInfo();
        }
        return m_ins;
    }

    /**
     * appentant 附属物
     * feature  点特质
     *
     * @Author HaiRun
     * @Time 2019/4/23 . 11:24
     */
    public String getSymbol(String type, String appendant, String feature) {
        String _symbol = "";
        switch (type) {
            case "给水-J": {
                _symbol = getJSymbol(appendant, feature);
            }
            break;
            case "雨水-Y":
            case "污水-W":
            case "排水-P": {
                _symbol = getYWPSymbol(appendant, feature);
            }
            break;

            case "交通-X":
            case "电信-D": {
                _symbol = getXDSymblo(appendant, feature);
            }
            break;

            case "电力-L": {
                _symbol = getLSymbol(appendant, feature);
            }
            break;

            case "燃气-R":
            case "煤气-M": {
                _symbol = getRMSymbol(appendant, feature);
            }
            break;

            case "路灯-S": {
                _symbol = getSSymbol(appendant, feature);

            }
            break;

            case "有视-T": {
                _symbol = getTSymbol(appendant, feature);

            }
            break;
            case "军队-B": {
                _symbol = getBSymbol(appendant, feature);

            }
            break;
            case "工业-G": {
                _symbol = getGSymbol(appendant, feature);

            }
            break;

            case "不明-N":
            case "其它-Q": {
                _symbol = getNQSymbol(appendant, feature);
//                _symbol = "探测点";
            }
            break;

            case "路灯-LS": {
                _symbol = getLSSymblol(appendant, feature);
            }
            break;
            case "信号-XH": {
                _symbol = getXHSymbol(appendant, feature);
            }
            default:
                _symbol = "探测点";
                break;
        }
        return _symbol;
    }


    /**
     * 深圳 信号
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 10:43
     */
    private String getXHSymbol(String appendant, String feature) {
        String _symbol = "";
        switch (appendant) {
            case "探测点":
            case "":
                _symbol = feature;
                break;
            default:
                _symbol = appendant;
                break;
        }
        return _symbol;
    }

    /**
     * 深圳 路灯
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 9:55
     */
    private String getLSSymblol(String appendant, String feature) {
        String _symbol = "";
        switch (appendant) {
            case "探测点":
            case "":
                _symbol = feature;
                break;
            default:
                _symbol = appendant;
                break;
        }
        return _symbol;
    }

    /**
     * 不明  其他
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 9:55
     */
    private String getNQSymbol(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "阀门":
                _symbol = "阀门";
                break;
            case "消防栓":
                _symbol = "消防栓";
                break;
            case "水表":
            case "水表井":
                _symbol = "水表";
                break;
            case "窨井":
            case "阀门井":
            case "消防井":
            case "检修井":
            case "未知井":
            case "通风井":
                _symbol = "窨井";
                break;
            case "放水口":
                _symbol = "放水口";
                break;

            default:
                switch (feature) {
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "变径":
                        _symbol = "变径";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "出地":
                        _symbol = "出地";
                        break;
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
                break;
        }
        return _symbol;
    }


    /**
     * 工业
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 9:54
     */
    private String getGSymbol(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "阀门":
                _symbol = "阀门";
                break;
            case "窨井":
            case "阀门井":
            case "检测井":
            case "检修井":
                _symbol = "窨井";
                break;
            case "放水器":
                _symbol = "放水器";
                break;
            case "调压箱":
                _symbol = "调压箱";
                break;
            default:
                switch (feature) {
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "变径":
                        _symbol = "变径";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "出地":
                        _symbol = "出地";
                        break;
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }

                break;

        }
        return _symbol;
    }

    private String getBSymbol(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "人孔井":
                _symbol = "人孔井";
                break;
            case "手孔井":
                _symbol = "手孔井";
                break;
            case "接线箱":
                _symbol = "接线箱";
                break;
            default:
                switch (feature) {
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "上杆":
                        _symbol = "上杆";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "出地":
                        _symbol = "出地";
                        break;
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
                break;

        }
        return _symbol;
    }

    /**
     * 有视电视
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 9:53
     */
    private String getTSymbol(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "人孔":
            case "人孔井":
                _symbol = "人孔井";
                break;
            case "手孔":
            case "手孔井":
                _symbol = "手孔井";
                break;
            case "接线箱":
                _symbol = "接线箱";
                break;
            default:
                switch (feature) {
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "上杆":
                        _symbol = "上杆";
                        break;
                    case "出地":
                        _symbol = "出地";
                        break;
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
                break;

        }
        return _symbol;
    }

    /**
     * 路灯
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 9:53
     */
    private String getSSymbol(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "地灯":
                _symbol = "地灯";
                break;
            case "人孔":
            case "人孔井":
            case "检修井":
                _symbol = "人孔井";
                break;
            case "手孔":
            case "手孔井":
                _symbol = "手孔井";
                break;
            case "接线箱":
            case "交接箱":
            case "配电箱":
                _symbol = "接线箱";
                break;
            case "路灯":
            case "路灯杆":
                _symbol = "路灯杆";
                break;
            default:
                switch (feature) {
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "上杆":
                        _symbol = "上杆";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "出地":
                        _symbol = "出地";
                        break;
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
                break;
        }
        return _symbol;
    }

    /**
     * 燃气  煤气
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 9:53
     */
    private String getRMSymbol(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "阀门":
                _symbol = "阀门";
                break;
            case "窨井":
            case "阀门井":
            case "检测井":
            case "检修井":
                _symbol = "窨井";
                break;
            case "放水器":
                _symbol = "放水器";
                break;
            case "凝水缸":
            case "抽水缸":
                _symbol = "凝水缸";
                break;
            case "调压箱":
                _symbol = "调压箱";
                break;
            default:
                switch (feature) {
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "变径":
                        _symbol = "变径";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "出地":
                        _symbol = "出地";
                        break;
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
                break;

        }
        return _symbol;
    }

    /**
     * 电力
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 9:52
     */
    private String getLSymbol(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "地灯":
                _symbol = "地灯";
                break;
            case "窨井":
            case "人孔":
            case "人孔井":
            case "检修井":
                _symbol = "窨井";
                break;
            case "接线箱":
            case "控制柜":
            case "交接箱":
                _symbol = "接线箱";
                break;
            case "变电箱":
            case "变压箱":
                _symbol = "变电箱";
                break;
            case "路灯":
            case "路灯杆":
                _symbol = "路灯杆";
                break;
            case "信号灯":
                _symbol = "信号灯";
                break;
            default:
                switch (feature) {
                    case "信号灯":
                        _symbol = "信号灯";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "上杆":
                        _symbol = "上杆";
                        break;
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "出地":
                        _symbol = "出地";
                        break;
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
                break;

        }
        return _symbol;
    }

    /**
     * 交通电信
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 9:51
     */
    private String getXDSymblo(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "人孔":
            case "人孔井":
                _symbol = "人孔井";
                break;
            case "手孔":
            case "手孔井":
                _symbol = "手孔井";
                break;
            case "接线箱":
            case "交接箱":
            case "配电箱":
                _symbol = "接线箱";
                break;
            case "电话亭":
                _symbol = "电话亭";
                break;
            case "监控器":
                _symbol = "监控器";
                break;
            case "摄像头":
                _symbol = "摄像头";
                break;
            case "红绿灯":
                _symbol = "红绿灯";
                break;
            case "信号灯":
                _symbol = "信号灯";
                break;
            default:
                switch (feature) {
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "上杆":
                        _symbol = "上杆";
                        break;
                    case "出地":
                        _symbol = "出地";
                        break;
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
                break;

        }
        return _symbol;
    }

    /**
     * 雨水污水排水
     *
     * @Author HaiRun
     * @Time 2019/6/13 . 9:51
     */
    private String getYWPSymbol(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "窨井":
            case "检查井":
                _symbol = "窨井";
                break;
            case "雨水井":
            case "检修井":
                _symbol = "窨井";
                break;
            case "接户井":
                _symbol = "接户井";
                break;
            case "雨篦井":
            case "污水篦":
            case "雨水篦":
            case "进水井":
            case "雨水口":
                _symbol = "雨水篦";
                break;
            case "闸阀井":
                _symbol = "闸阀井";
                break;
            case "溢流井":
                _symbol = "溢流井";
                break;
            case "透气井":
                _symbol = "透气井";
                break;
            case "计量井":
                _symbol = "计量井";
                break;
            case "拍门井":
                _symbol = "拍门井";
                break;
            case "沉沙井":
                _symbol = "沉沙井";
                break;
            case "检测井":
            case "特殊功能井":
                _symbol = "检测井";
                break;
            case "化粪池":
                _symbol = "化粪池";
                break;
            case "水控闸":
                _symbol = "水控闸";
                break;
            case "电控闸":
                _symbol = "电控闸";
                break;
            case "手控闸":
                _symbol = "手控闸";
                break;
            case "立管点":
                _symbol = "立管点";
                break;

            default:
                switch (feature) {
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "进出水口":
                    case "出水口":
                    case "入水口":
                    case "进水口":
                        _symbol = "进出水口";
                        break;
                    case "户出":
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
                break;
        }
        return _symbol;
    }


    /**
     * 给水
     *
     * @param appendant
     * @param feature
     * @return
     */
    private String getJSymbol(String appendant, String feature) {
        String _symbol;
        switch (appendant) {
            case "阀门":
                _symbol = "阀门";
                break;
            case "消防栓":
                _symbol = "消防栓";
                break;
            case "水表":
            case "水表井":
                _symbol = "水表";
                break;
            case "窨井":
            case "阀门井":
            case "消防井":
            case "检修井":
                _symbol = "窨井";
                break;
            case "放水口":
                _symbol = "放水口";
                break;

            case "探测点":
            default:
                switch (feature) {
                    case "预留口":
                        _symbol = "预留口";
                        break;
                    case "变径":
                        _symbol = "变径";
                        break;
                    case "非普查区":
                    case "出测区":
                        _symbol = "出测区";
                        break;
                    case "出地":
                        _symbol = "出地";
                        break;
                    case "入户":
                        _symbol = "入户";
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
                break;

        }
        return _symbol;
    }
}
