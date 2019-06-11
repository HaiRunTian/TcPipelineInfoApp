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

    @Override
    public ThemeUnique createDefaultThemeUnique() {
        try {
            LogUtills.i("begin " + this.getClass().getName() + " createDefaultThemeUnique....");
            super.createThemeUnique();
            String tabName = "";
            //TODO 根据标准名称 查找点配置表
            Cursor _cursorStand = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO, "where name = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
            //查询此标准的点表名
            while (_cursorStand.moveToNext()) {
                tabName = _cursorStand.getString(_cursorStand.getColumnIndex("pointsettingtablesymbol"));
            }

            if (tabName.length() == 0) {
                LogUtills.i("begin " + this.getClass().getName() + "tabName = null ");
            }

            Cursor _cursor = DatabaseHelpler.getInstance().query(tabName,
                    new String[]{"name", "symbolID", "scaleX", "scaleY", "color"}, null, null, null, null, null);
            int _num = _cursor.getCount();
            String[] _keys = new String[_num];
            int[] _symbolIds = new int[_num];
          /*double[] _scaleXs    = new double[_num];
            double[] _scaleYs    = new double[_num];*/
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

            /*   LogUtills.i("query point config infomation: "+ "Name;"+_name+",Symbolid:"+_symbolId+
                        ",_ScaleX:"+_scaleX+",_ScaleY:"+_scaleY+",_color:"+_color);*/
            }
            _cursorStand.close();
            _cursor.close();

            return createThemeUnique("symbolExpression", _keys, _symbolIds, _colors, _size2ds);

        } catch (Exception e) {
            LogUtills.e(e.toString());
        }

        return null;
       /* super.createThemeUnique();
//        String[] _objs = new String[] {  "电杆", "人孔井", "手孔井", "接线箱", "摄像头", "红绿灯", "信号灯", "水泥杆", "铁塔", "探测点"};
//        int[] _ids = new int[]        {   436,    419,      420,      430,      438,       437,     443,      436,      443,    443};

         String[] _objs = new String[]{"探测点", "电杆", "人孔井", "手孔井", "接线箱", "电话亭", "摄像头", "红绿灯", "信号灯", "水泥杆", "铁塔"
                , "预留口", "出测区", "上杆", "出地", "入户"};
        int[] _ids = new int[]{41, 50, 42, 43, 44, 49, 41, 41, 41, 41, 41, 46, 48, 45, 63, 47};

       String _color = "#00CC66";

        //规程管点符号 大小 * 2.5
        Size2D _size2D1 = new Size2D(2.5, 2.5);//探测点、拐点、三通、四通、多通、一般管线点、井边点、井内点、入户
        Size2D _size2D2 = new Size2D(2.5, 4.0);//变径
        Size2D _size2D3 = new Size2D(2.5, 5.5);//出地 上杆
        Size2D _size2D4 = new Size2D(3.0, 5.5);//阀门 接线箱
        Size2D _size2D5 = new Size2D(3.0, 7.0);//预留口、非普查、出测区
        Size2D _size2D6 = new Size2D(4.0, 2.5);//雨水篦、污水篦、溢流井、出气井、调压箱
        Size2D _size2D7 = new Size2D(4.0, 4.0);//化粪池、净化池、进水口、出水口、污水井、雨水井、闸门井、跌水井、通风井、冲洗井、水封井
        Size2D _size2D8 = new Size2D(5.5, 4.0);//排水泵、雨水篦
        Size2D[] _size2Ds = new Size2D[]{_size2D1,_size2D3,_size2D7,_size2D7,_size2D4,_size2D3,_size2D7,_size2D7,_size2D7,_size2D7,_size2D7,_size2D7,
                _size2D5,_size2D5,_size2D3,_size2D3,_size2D1};



        return createThemeUnique(_objs, _ids, _color,_size2Ds);*/
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();

        try {
            //查询数据库表，设置点线有颜色
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
            return createThemeLabel(_keys, _colors, _starts, _ends);
        } catch (Exception e) {
            LogUtills.e(e.toString());
        }

        return null;
    }

    @Override
    public GeoStyle createDefaultSelectStyle() {
        GeoStyle _style = new GeoStyle();
        // _style.setFillBackColor(new Color(255,255,0));
        // _style.setFillForeColor(new Color(255,255,0));
        _style.setLineColor(new Color(255, 0, 0));
        _style.setLineWidth(0.8);
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
