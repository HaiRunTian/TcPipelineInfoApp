package com.app.BaseInfo.Data.Point;

import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Size2D;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeUnique;

/**
 * Created by Los on 2018-06-14 22:22.
 * 燃气
 */

public class GasFieldPoint extends BaseFieldPInfos {

    public GasFieldPoint(){
        type = POINTTYPE.Type_Gas_R;
        datasetName = SuperMapConfig.Layer_Gas;
        Init();
    }

    public GasFieldPoint(String name){
        type = POINTTYPE.Type_Gas_R;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeUnique createDefaultThemeUnique() {
        LogUtills.i("begin "+this.getClass().getName()+" createDefaultThemeUnique....");
        super.createThemeUnique();
//        String[] _objs = new String[] { "窨井", "阀门", "阀门井", "凝水缸", "调压箱", "探测点" };
//        int[] _ids = new int[] {445,399,455,448,448,453 };

        String[] _objs = new String[]{"探测点","窨井", "阀门", "阀门井", "凝水缸", "调压箱","预留口","变径","出测区","出地","入户" };
        int[] _ids = new int[]{         484,    485  ,   479,     485,    478,     477  ,   12   ,   481,    483,    482,   480};
        String _color = "#FF9900";


        //规程管点符号 大小 * 2.5
        Size2D _size2D1 = new Size2D(2.5, 2.5);//探测点、拐点、三通、四通、多通、一般管线点、井边点、井内点、入户
        Size2D _size2D2 = new Size2D(2.5, 4.0);//变径
        Size2D _size2D3 = new Size2D(2.5, 5.5);//出地 上杆
        Size2D _size2D4 = new Size2D(4.0, 5.5);//阀门
        Size2D _size2D5 = new Size2D(4.0, 7.0);//预留口、非普查、出测区
        Size2D _size2D6 = new Size2D(4.0, 2.5);//雨水篦、污水篦、溢流井、出气井、调压箱
        Size2D _size2D7 = new Size2D(4.0, 4.0);//化粪池、净化池、进水口、出水口、污水井、雨水井、闸门井、跌水井、通风井、冲洗井、水封井
        Size2D _size2D8 = new Size2D(5.5, 4.0);//排水泵、雨水篦
        Size2D[] _size2Ds = new Size2D[]{_size2D1,_size2D7,_size2D7,_size2D7,_size2D6,_size2D7,_size2D5,_size2D2,_size2D5,_size2D3,_size2D1};

        return createThemeUnique(_objs, _ids, _color,_size2Ds);
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        String _color = "#FF9900";

        return createThemeLabel(_color);
    }
}
