package com.app.BaseInfo.Data;

import android.database.Cursor;
import com.app.BaseInfo.Data.Line.TheTotalLine;
import com.app.pipelinesurvey.database.DatabaseHelpler;
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
            default:
                break;
        }
        return _info;
    }

    public static BaseFieldLInfos Create(String name) {
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
        _info.labelTag = "连接线(线中加点)";
        return _info;
    }
}
