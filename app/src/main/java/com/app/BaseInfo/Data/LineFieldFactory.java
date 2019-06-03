package com.app.BaseInfo.Data;

import android.database.Cursor;

import com.app.BaseInfo.Data.Line.ArmyFieldLine;
import com.app.BaseInfo.Data.Line.CableTVFieldLine;
import com.app.BaseInfo.Data.Line.CoalGasFieldLine;
import com.app.BaseInfo.Data.Line.DrainageFieldLine;
import com.app.BaseInfo.Data.Line.ElectricPowerFieldLine;
import com.app.BaseInfo.Data.Line.GasFieldLine;
import com.app.BaseInfo.Data.Line.IndustryFieldLine;
import com.app.BaseInfo.Data.Line.OtherFieldLine;
import com.app.BaseInfo.Data.Line.PipeLine;
import com.app.BaseInfo.Data.Line.RainFieldLine;
import com.app.BaseInfo.Data.Line.SewageFieldLine;
import com.app.BaseInfo.Data.Line.StreetLampFieldLine;
import com.app.BaseInfo.Data.Line.TelecomFieldLine;
import com.app.BaseInfo.Data.Line.TheTotalLine;
import com.app.BaseInfo.Data.Line.TrafficFieldLine;
import com.app.BaseInfo.Data.Line.UnknownFieldLine;
import com.app.BaseInfo.Data.Line.WaterSupplyFieldLine;
import com.app.BaseInfo.Data.Point.UnknownFieldPoint;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.PipeThemelabelUtil;
import com.app.utills.LogUtills;

/**
 * Created by Los on 2018-06-23 17:17.
 * 线工厂
 */

public class LineFieldFactory {
    public static BaseFieldLInfos Create(POINTTYPE type) {
        BaseFieldLInfos _info = null;
        switch (type) {
         /*   case Type_WaterSupply_J: {
                _info = new WaterSupplyFieldLine();
            }
            break;
            case Type_Sewage_W: {
                _info = new SewageFieldLine();
            }
            break;
            case Type_Rain_Y: {
                _info = new RainFieldLine();
            }
            break;
            case Type_Drainage_P: {
                _info = new DrainageFieldLine();
            }
            break;
            case Type_CoalGas_M: {
                _info = new CoalGasFieldLine();
            }
            break;
            case Type_Gas_R: {
                _info = new GasFieldLine();
            }
            break;
            case Type_ElectricPower_L: {
                _info = new ElectricPowerFieldLine();
            }
            break;
            case Type_StreetLamp_S: {
                _info = new StreetLampFieldLine();
            }
            break;
            case Type_Telecom_D: {
                _info = new TelecomFieldLine();
            }
            break;
            case Type_CableTV_T: {
                _info = new CableTVFieldLine();
            }
            break;
            case Type_Army_B: {
                _info = new ArmyFieldLine();
            }
            break;
            case Type_Traffic_X: {
                _info = new TrafficFieldLine();
            }
            break;
            case Type_Industry_G: {
                _info = new IndustryFieldLine();
            }
            break;
            case Type_Unknown_N: {
                _info = new UnknownFieldLine();
            }
            break;
            case Type_Other_Q: {
                _info = new OtherFieldLine();
            }
            break;
            case Type_Line:{
                _info = new PipeLine();
            }*/
            default:
                break;
        }

        return _info;
    }

    public static BaseFieldLInfos Create(String name) {

       /* if (name.equals(SuperMapConfig.Layer_WaterSupply))    return new WaterSupplyFieldLine();
        if (name.equals(SuperMapConfig.Layer_Sewage))         return new SewageFieldLine();
        if (name.equals(SuperMapConfig.Layer_Rain))           return new RainFieldLine();
        if (name.equals(SuperMapConfig.Layer_Drainage))       return new DrainageFieldLine();
        if (name.equals(SuperMapConfig.Layer_CoalGas))        return new CoalGasFieldLine();
        if (name.equals(SuperMapConfig.Layer_Gas))            return new GasFieldLine();
        if (name.equals(SuperMapConfig.Layer_ElectricPower))  return new ElectricPowerFieldLine();
        if (name.equals(SuperMapConfig.Layer_StreetLamp))     return new StreetLampFieldLine();
        if (name.equals(SuperMapConfig.Layer_Telecom))        return new TelecomFieldLine();
        if (name.equals(SuperMapConfig.Layer_CableTV))        return new CableTVFieldLine();
        if (name.equals(SuperMapConfig.Layer_Army))           return new ArmyFieldLine();
        if (name.equals(SuperMapConfig.Layer_Traffic))        return new TrafficFieldLine();
        if (name.equals(SuperMapConfig.Layer_Industry))       return new IndustryFieldLine();
        if (name.equals(SuperMapConfig.Layer_Unknown))        return new UnknownFieldLine();
        if (name.equals(SuperMapConfig.Layer_Other))          return new OtherFieldLine();
        if (name.equals(SuperMapConfig.Layer_Line))           return new PipeLine();*/

        return null;
    }

    public static BaseFieldLInfos Create() {
        return new TheTotalLine();
    }

    public static BaseFieldLInfos CreateInfo(String typeName){
        Cursor _cursor = DatabaseHelpler.getInstance().queryLine(
                null,  "name=?",new String[]{typeName.trim().toString()}, null, null, null);
        LogUtills.i("Sql:"+_cursor.getCount());
        if(_cursor.moveToNext()){
            BaseFieldLInfos _info = new TheTotalLine();
            _info.pipeType = _cursor.getString(_cursor.getColumnIndex("typename"));
            _info.shortCode = _cursor.getString(_cursor.getColumnIndex("typeShortCall"));
            _info.code = _cursor.getString(_cursor.getColumnIndex("typeCode"));
            _info.rangeExpression = PipeThemelabelUtil.Ins().getThemeItemValue(typeName);
            return _info;
        }else {
            LogUtills.e("Can Not Find Type="+typeName+" Line Config Record,Create BaseFieldPInfos Faild...");
            return null;
        }
    }

    public static BaseFieldLInfos createFieldInfo(BaseFieldPInfos startInfo, BaseFieldPInfos endInfo){
        BaseFieldLInfos _info = LineFieldFactory.CreateInfo(startInfo.pipeType.substring(0,2));
        if(_info == null){
            LogUtills.e("Create "+_info.pipeType+", BaseFieldLInfos Faild...");
            return  null;
        }
        _info.startLongitude = startInfo.longitude;
        _info.startLatitude  = startInfo.latitude;
        _info.benExpNum      = startInfo.exp_Num;
        _info.endLatitude    = endInfo.latitude;
        _info.endLongitude   = endInfo.longitude;
        _info.endExpNum      = endInfo.exp_Num;
        //_info.labelTag = String.valueOf(m_smId) + "-" + _info.code.toLowerCase() + "-" + _info.pipeSize.toString() + "-" + _info.material.toString();
        _info.labelTag = "连接线(线中加点)";
        return _info;
    }
}
