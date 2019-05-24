package com.app.BaseInfo.Data.Line;

import android.database.Cursor;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2019-03-08 13:49.
 */

public class  TheTotalLine extends BaseFieldLInfos {
    public TheTotalLine(){
        type = POINTTYPE.Type_All_A;
        datasetName = SuperMapConfig.Layer_Total;
        Init();
    }

    public TheTotalLine(String name){
        type = POINTTYPE.Type_All_A;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        try {
            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_THEME);
            int _num = _cursor.getCount();
            String[] _keys = new String[_num];
            String[] _colors = new String[_num];
            double[] _starts = new double[_num];
            double[] _ends = new double[_num];
            LogUtills.i("Query Cursor: " + _num);
            int _index = 0;
            while (_cursor.moveToNext()) {
                String _name = _cursor.getString(_cursor.getColumnIndex("pipetype"));
                String _color = _cursor.getString(_cursor.getColumnIndex("color"));
                double _start = _cursor.getDouble(_cursor.getColumnIndex("start"));
                double _end = _cursor.getDouble(_cursor.getColumnIndex("end"));
                _keys[_index] = _name;
                _colors[_index] = _color;
                _starts[_index] = _start;
                _ends[_index] = _end;
                _index++;
            }
            return createThemeLabel(_keys,_colors,_starts,_ends);
        } catch (Exception e) {
            LogUtills.e(e.toString());
        }
        return null;
    }

    @Override
    public String toString() {
        return "TheTotalLine{" +
                "submitName='" + submitName + '\'' +
                ", datasetName='" + datasetName + '\'' +
                ", type=" + type +
                ", sysId=" + sysId +
                ", code='" + code + '\'' +
                ", shortCode='" + shortCode + '\'' +
                ", rangeExpression=" + rangeExpression +
                ", pipeType='" + pipeType + '\'' +
                ", benExpNum='" + benExpNum + '\'' +
                ", endExpNum='" + endExpNum + '\'' +
                ", startLongitude=" + startLongitude +
                ", endLongitude=" + endLongitude +
                ", startLatitude=" + startLatitude +
                ", endLatitude=" + endLatitude +
                ", exp_Date='" + exp_Date + '\'' +
                ", benDeep='" + benDeep + '\'' +
                ", endDeep='" + endDeep + '\'' +
                ", buried='" + buried + '\'' +
                ", pipeSize='" + pipeSize + '\'' +
                ", d_S='" + d_S + '\'' +
                ", rowXCol='" + rowXCol + '\'' +
                ", cabNum='" + cabNum + '\'' +
                ", belong='" + belong + '\'' +
                ", voltage='" + voltage + '\'' +
                ", pressure='" + pressure + '\'' +
                ", material='" + material + '\'' +
                ", totalHole='" + totalHole + '\'' +
                ", usedHole='" + usedHole + '\'' +
                ", holeDiameter='" + holeDiameter + '\'' +
                ", state='" + state + '\'' +
                ", remark='" + remark + '\'' +
                ", id='" + id + '\'' +
                ", puzzle='" + puzzle + '\'' +
                ", labelTag='" + labelTag + '\'' +
                ", pipeLength='" + pipeLength + '\'' +
                ", burialDifference='" + burialDifference + '\'' +
                '}';
    }
}
