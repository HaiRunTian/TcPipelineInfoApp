package com.app.BaseInfo.Data.Point;

import android.database.Cursor;

import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.GeoStyle;
import com.supermap.data.Size2D;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeUnique;

/**
 * Created by Los on 2019-03-08 13:55.
 */

public class TheTotalPoint extends BaseFieldPInfos {
    private String m_tabName;

    public TheTotalPoint() {
        type = POINTTYPE.Type_All_A;
        datasetName = SuperMapConfig.Layer_Total;
        Init();
    }

    public TheTotalPoint(String name) {
        type = POINTTYPE.Type_All_A;
        datasetName = name;
        Init();
    }

    /**
     * 单值专题图
     * @return
     */
    @Override
    public ThemeUnique createDefaultThemeUnique() {
        try {
            LogUtills.i("begin " + this.getClass().getName() + " createDefaultThemeUnique....");
            super.createThemeUnique();
            String tabName = "";
           /* //TODO 根据标准名称 查找点配置表
            Cursor _cursorStand = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO, "where name = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
            //查询此标准的点表名
            while (_cursorStand.moveToNext()) {
                tabName = _cursorStand.getString(_cursorStand.getColumnIndex("pointsettingtablesymbol"));
            }

            if (tabName.length() == 0) {
                LogUtills.i("begin " + this.getClass().getName() + "tabName = null ");
            }*/

            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_DEFAULT_POINT_SETTING,
                    new String[]{"name", "symbolID", "scaleX", "scaleY", "color"}, null, null, null, null, null);
            int _num = _cursor.getCount();
            String[] _keys = new String[_num];
            int[] _symbolIds = new int[_num];
            Size2D[] _size2ds = new Size2D[_num];
            String[] _colors = new String[_num];
            LogUtills.i("Query Cursor: " + _num);
            int _index = 0;
            while (_cursor.moveToNext()) {
                String _name = _cursor.getString(_cursor.getColumnIndex("name"));
                int _symbolId = _cursor.getInt(_cursor.getColumnIndex("symbolID"));
                double _scaleX = _cursor.getDouble(_cursor.getColumnIndex("scaleX"));
                double _scaleY = _cursor.getDouble(_cursor.getColumnIndex("scaleY"));
                String _color = _cursor.getString(_cursor.getColumnIndex("color"));
                _keys[_index] = _name;
                _symbolIds[_index] = _symbolId;
                _size2ds[_index] = new Size2D(_scaleX , _scaleY);
                _colors[_index] = _color;
                _index++;
            }
            _cursor.close();

            return createThemeUnique("symbolExpression", _keys, _symbolIds, _colors, _size2ds);

        } catch (Exception e) {
            LogUtills.e(e.toString());
        }

        return null;
    }

    /**
     *  标签专题图
     * @Params :
     * @author :HaiRun
     * @date   :2019/6/20  16:08
     */
    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();

        try {
            //查询数据库表，设置点线有颜色
            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_THEME,"where city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
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
            return createThemeLabel(_keys, _colors, _starts, _ends);
        } catch (Exception e) {
            LogUtills.e(e.toString());
        }

        return null;
    }

    /**
     * 默认风格
     * @return
     */
    @Override
    public GeoStyle createDefaultSelectStyle() {
        GeoStyle _style = new GeoStyle();
        _style.setLineColor(new Color(255, 0, 0));
        _style.setLineWidth(0.5);
        _style.setMarkerSize(new Size2D(8, 8));
        _style.setFillBackOpaque(true);
        return _style;
    }

    @Override
    public String toString() {
        return "TheTotalPoint{" +
                "submitName='" + submitName + '\'' +
                ", datasetName='" + datasetName + '\'' +
                ", type=" + type +
                ", sysId=" + sysId +
                ", code='" + code + '\'' +
                ", shortCode='" + shortCode + '\'' +
                ", rangeExpression=" + rangeExpression +
                ", pipeType='" + pipeType + '\'' +
                ", exp_Num='" + exp_Num + '\'' +
                ", pipeOffset='" + pipeOffset + '\'' +
                ", feature='" + feature + '\'' +
                ", subsid='" + subsid + '\'' +
                ", wellSize='" + wellSize + '\'' +
                ", wellDeep='" + wellDeep + '\'' +
                ", wellWater='" + wellWater + '\'' +
                ", wellMud='" + wellMud + '\'' +
                ", road='" + road + '\'' +
                ", state='" + state + '\'' +
                ", exp_Date='" + exp_Date + '\'' +
                ", wellCoverMaterial='" + wellCoverMaterial + '\'' +
                ", wellCoverSize='" + wellCoverSize + '\'' +
                ", buildingStructures='" + buildingStructures + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", surf_H='" + surf_H + '\'' +
                ", expGroup='" + expGroup + '\'' +
                ", remark='" + remark + '\'' +
                ", picture='" + picture + '\'' +
                ", id='" + id + '\'' +
                ", puzzle='" + puzzle + '\'' +
                ", situation='" + situation + '\'' +
                ", startDirDepth='" + startDirDepth + '\'' +
                ", endDirDepth='" + endDirDepth + '\'' +
                ", depth='" + depth + '\'' +
                ", symbol='" + symbol + '\'' +
                ", symbolExpression='" + symbolExpression + '\'' +
                ", symbolID=" + symbolID +
                ", symbolSizeX=" + symbolSizeX +
                ", symbolSizeY=" + symbolSizeY +
                ", serialNum=" + serialNum +
                '}';
    }
}
