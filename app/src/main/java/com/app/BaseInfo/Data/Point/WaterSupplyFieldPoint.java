package com.app.BaseInfo.Data.Point;

import android.provider.CalendarContract;

import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.FillGradientMode;
import com.supermap.data.GeoStyle;
import com.supermap.data.Size2D;
import com.supermap.data.TextStyle;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeLabelItem;
import com.supermap.mapping.ThemeUnique;
import com.supermap.mapping.ThemeUniqueItem;

/**
 * Created by Los on 2018-06-14 22:22.
 * 给水
 */

public class WaterSupplyFieldPoint extends BaseFieldPInfos {
    public WaterSupplyFieldPoint(){
        type = POINTTYPE.Type_WaterSupply_J;
        datasetName = SuperMapConfig.Layer_WaterSupply;
        Init();
    }

    public WaterSupplyFieldPoint(String name){
        type = POINTTYPE.Type_WaterSupply_J;
        datasetName = name;

        Init();
    }

    @Override
    public ThemeUnique createThemeUnique() {
        super.createThemeUnique();
        // 构造单值专题图并进行相应设置
        ThemeUnique _theme = new ThemeUnique();
        _theme.setUniqueExpression("symbol");
        GeoStyle _defaultStyle = new GeoStyle();
        /*style.setFillForeColor(java.awt.Color.yellow);
        style.setFillBackColor(java.awt.Color.green);
        style.setFillGradientMode(FillGradientMode.RADIAL);*/
        _defaultStyle.setMarkerSize(new Size2D(4, 4));
        _defaultStyle.setLineColor(new Color(0,255,0));
        _defaultStyle.setMarkerSymbolID(408);
        _theme.setDefaultStyle(_defaultStyle);


//        String[] _objs = new String[] { "水表", "阀门", "窨井", "阀门井", "消防栓", "放水口", "探测点" };
//        int[] _ids = new int[] {6,4,2,2,3,11,1};
        String[] _colors = new String[]{
                "#1E90FF",
                "#1E90FF",
                "#1E90FF",
                "#1E90FF",
                "#1E90FF",
                "#1E90FF",
        };

        String[] _objs = new String[] { "水表", "阀门", "窨井", "阀门井", "消防栓", "放水口", "检修井","预留口","变径","出测区",
                "出地","入户","探测点" };
//        int[] _ids = new int[] {401,399,402,402,400,403,408};
        int[] _ids = new int[]{6, 4, 2, 2, 3, 11, 2, 5, 10, 7, 8, 9, 1};

        for(int i=0; i<_objs.length;++i){
            ThemeUniqueItem _item = new ThemeUniqueItem();
            _item.setVisible(true);
            _item.setUnique(_objs[i]);
            GeoStyle _style = new GeoStyle();
            _style.setMarkerSize(new Size2D(4, 4));
            //_style.setLineColor(_colors[i]);
            _style.setFillBackColor(new Color(0,255,0));
            _style.setFillGradientMode(FillGradientMode.RADIAL);
            _style.setMarkerSymbolID(_ids[i]);
            _item.setStyle(_style);
            _theme.add(_item);
        }


        return _theme;
    }

    @Override
    public ThemeUnique createDefaultThemeUnique() {
        LogUtills.i("begin WaterSupplyFieldPoint createDefaultThemeUnique....");

        super.createThemeUnique();
        //如果附属物是探测点，还要查看点特征
        String[] _objs = new String[] { "水表", "阀门", "窨井", "阀门井", "消防栓", "放水口", "检修井","预留口","变径","出测区",
                "出地","入户","探测点" };
//        int[] _ids = new int[] {401,399,402,402,400,403,408};
        int[] _ids = new int[]{6, 4, 2, 2, 3, 11, 2, 5, 10, 7, 8, 9, 1};
        String _color = "#00CCFF";


        //规程管点符号 大小 * 2.5
        Size2D _size2D1 = new Size2D(2.5, 2.5);//探测点、拐点、三通、四通、多通、一般管线点、井边点、井内点
        Size2D _size2D2 = new Size2D(2.5, 4.0);//变径
        Size2D _size2D3 = new Size2D(2.5, 5.5);//出地
        Size2D _size2D4 = new Size2D(4.0, 5.5);//阀门
        Size2D _size2D5 = new Size2D(4.0, 7.0);//预留口、非普查、出测区
        Size2D _size2D6 = new Size2D(4.0, 2.5);//雨水篦、污水篦、溢流井、出气井
        Size2D _size2D7 = new Size2D(4.0, 4.0);//化粪池、净化池、进水口、出水口、污水井、雨水井、闸门井、跌水井、通风井、冲洗井、水封井
        Size2D _size2D8 = new Size2D(5.5, 4.0);//排水泵、雨水篦
        Size2D _size2D9 = new Size2D(4.0, 6.0);//消防栓
        Size2D[] _size2Ds = new Size2D[]{_size2D7,_size2D4,_size2D7,_size2D7,_size2D9,_size2D7,_size2D7,_size2D5,_size2D2,_size2D5,
                _size2D3,_size2D1,_size2D1};

        return createThemeUnique(_objs, _ids, _color,_size2Ds);

    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        String _color = "#00CCFF";

        return createThemeLabel(_color);
    }

}
