package com.app.BaseInfo.Data;

import android.database.Cursor;

import com.app.BaseInfo.Data.Line.TheTotalLine;
import com.app.BaseInfo.Data.Point.ArmyFieldPoint;
import com.app.BaseInfo.Data.Point.CableTVFieldPoint;
import com.app.BaseInfo.Data.Point.CoalGasFieldPoint;
import com.app.BaseInfo.Data.Point.DrainageFieldPoint;
import com.app.BaseInfo.Data.Point.ElectricPowerFieldPoint;
import com.app.BaseInfo.Data.Point.GasFieldPoint;
import com.app.BaseInfo.Data.Point.IndustryFieldPoint;
import com.app.BaseInfo.Data.Point.MeasurePoint;
import com.app.BaseInfo.Data.Point.OtherFieldPoint;
import com.app.BaseInfo.Data.Point.PipePoint;
import com.app.BaseInfo.Data.Point.RainFieldPoint;
import com.app.BaseInfo.Data.Point.SewageFieldPoint;
import com.app.BaseInfo.Data.Point.StreetLampFieldPoint;
import com.app.BaseInfo.Data.Point.TelecomFieldPoint;
import com.app.BaseInfo.Data.Point.TempPoint;
import com.app.BaseInfo.Data.Point.TheTotalPoint;
import com.app.BaseInfo.Data.Point.TrafficFieldPoint;
import com.app.BaseInfo.Data.Point.UnknownFieldPoint;
import com.app.BaseInfo.Data.Point.WaterSupplyFieldPoint;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.bean.PointConfig;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.PipeThemelabelUtil;
import com.app.utills.LogUtills;
import com.supermap.data.Point;
import com.supermap.data.Point2D;

/**
 * Created by Los on 2018-06-22 16:43.
 * 点工厂
 */

public class PointFieldFactory {

    public static BaseFieldPInfos Create(POINTTYPE type) {
        BaseFieldPInfos _info = null;
        switch (type) {
            case Type_WaterSupply_J: {
                _info = new WaterSupplyFieldPoint();
            }
            break;
            case Type_Sewage_W: {
                _info = new SewageFieldPoint();
            }
            break;
            case Type_Rain_Y: {
                _info = new RainFieldPoint();
            }
            break;
            case Type_Drainage_P: {
                _info = new DrainageFieldPoint();
            }
            break;
            case Type_CoalGas_M: {
                _info = new CoalGasFieldPoint();
            }
            break;
            case Type_Gas_R: {
                _info = new GasFieldPoint();
            }
            break;
            case Type_ElectricPower_L: {
                _info = new ElectricPowerFieldPoint();
            }
            break;
            case Type_StreetLamp_S: {
                _info = new StreetLampFieldPoint();
            }
            break;
            case Type_Telecom_D: {
                _info = new TelecomFieldPoint();
            }
            break;
            case Type_CableTV_T: {
                _info = new CableTVFieldPoint();
            }
            break;
            case Type_Army_B: {
                _info = new ArmyFieldPoint();
            }
            break;
            case Type_Traffic_X: {
                _info = new TrafficFieldPoint();
            }
            break;
            case Type_Industry_G: {
                _info = new IndustryFieldPoint();
            }
            break;
            case Type_Other_Q: {
                _info = new OtherFieldPoint();
            }
            break;
            case Type_Unknown_N: {
                _info = new UnknownFieldPoint();
            }
            break;
            case Type_Point: {
                _info = new PipePoint();
            }
            break;
            case Type_Measure_Point: {
                _info = new MeasurePoint();
            }
            break;
            case Type_Temp_T: {
                _info = new TempPoint();
            }
            break;

            default:
                break;
        }

        return _info;
    }

    public static BaseFieldPInfos Create(String name) {

//        if (name.equals(SuperMapConfig.Layer_WaterSupply)) return new WaterSupplyFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Sewage)) return new SewageFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Rain)) return new RainFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Drainage)) return new DrainageFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_CoalGas)) return new CoalGasFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Gas)) return new GasFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_ElectricPower)) return new ElectricPowerFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_StreetLamp)) return new StreetLampFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Telecom)) return new TelecomFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_CableTV)) return new CableTVFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Army)) return new ArmyFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Traffic)) return new TrafficFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Industry)) return new IndustryFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Other)) return new OtherFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Unknown)) return new UnknownFieldPoint();
//        if (name.equals(SuperMapConfig.Layer_Point)) return new PipePoint();
        if (name.equals(SuperMapConfig.Layer_Measure)) return new MeasurePoint();
//        if (name.equals(SuperMapConfig.Layer_Temp)) return new TempPoint();

        return null;
    }

    public static BaseFieldPInfos CreateInfo(String typeName) {
        //需要配置点类表的信息；
        BaseFieldPInfos _pInfo = new BaseFieldPInfos();
        _pInfo.rangeExpression = PipeThemelabelUtil.Ins().getThemeItemValue(typeName);
//        _pInfo.pipeType = typeName;
        return _pInfo;

        /*Cursor _cursor = DatabaseHelpler.getInstance().queryPoint(
                null,  "type=?",new String[]{typeName.trim().toString()}, null, null, null);
        LogUtills.i("Sql:"+_cursor.getCount());
        if(_cursor.moveToNext()){
            BaseFieldPInfos _info = new TheTotalLine();
            _info.pipeType = _cursor.getString(_cursor.getColumnIndex("name"));
            _info.shortCode = _cursor.getString(_cursor.getColumnIndex("typeShortCall"));
            _info.code = _cursor.getString(_cursor.getColumnIndex("shortCall"));
            return _info;
        }else {
            LogUtills.e("Can Not Find Type="+typeName+" Line Config Record,Create BaseFieldPInfos Faild...");
            return null;
        }*/
    }

    public static BaseFieldPInfos Create() {
        return new TheTotalPoint();
    }

    //创建临时点
    public static BaseFieldPInfos createTempInfo(Point2D pt, String type, String code) {
        BaseFieldPInfos _tempPt = new TheTotalPoint();//创建临时点
        _tempPt.subsid = "临时点";
        _tempPt.exp_Date = DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT); //时间
        _tempPt.situation = "正常"; //状态
        _tempPt.pipeType = type;
        _tempPt.code = "O";
        _tempPt.symbolExpression = "O-临时";//样式
        String[] _num = ComTool.Ins().getPointNumber(code, true, "");
        _tempPt.id = _num[0];
        _tempPt.serialNum = Integer.parseInt(_num[1]);
        //LogUtills.i("ExpNum=" + _tempPt.id);
        _tempPt.exp_Num = _num[0];
        _tempPt.symbol = "探测点";
        _tempPt.latitude = pt.getY();
        _tempPt.longitude = pt.getX();

        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
        if (_cursor.moveToNext()) {
            _tempPt.expGroup = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
        }
//        DataHandlerObserver.ins().createRecords(_tempPt);
        DataHandlerObserver.ins().createRecords2(_tempPt);
        return _tempPt;

    }
}
