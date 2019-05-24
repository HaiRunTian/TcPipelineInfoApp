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
 * 雨水
 */

public class RainFieldPoint extends BaseFieldPInfos {

    public RainFieldPoint(String name){
        super(name);
        type = POINTTYPE.Type_Rain_Y;
        datasetName = name;
        Init();
    }

    public RainFieldPoint(){
        super();
        type = POINTTYPE.Type_Rain_Y;
        datasetName = SuperMapConfig.Layer_Rain;
        Init();
    }


    @Override
    public ThemeUnique createDefaultThemeUnique() {
        LogUtills.i("begin WaterSupplyFieldPoint createDefaultThemeUnique....");

        super.createThemeUnique();
//        String[] _objs = new String[] { "窨井", "检查井", "沉沙井", "接户井", "雨水篦", "污水篦", "化粪池", "立管点", "探测点" };
//        int[] _ids = new int[] {409,409,409,409,410,410,411,415,415};

        String[] _objs = new String[] {"窨井","检查井","沉沙井","接户井","雨水篦","污水篦","化粪池","立管点",
                "入户","进出水口","出测区","预留口","探测点"};
        int[] _ids = new int[] {33,33,33,33,36,36,40,491,38,39,37,35,32};

        String _color = "#A52A2A";

        //规程管点符号 大小 * 2.5
        Size2D _size2D1 = new Size2D(2.5, 2.5);//探测点、拐点、三通、四通、多通、一般管线点、井边点、井内点
        Size2D _size2D2 = new Size2D(2.5, 4.0);//变径
        Size2D _size2D3 = new Size2D(2.5, 5.5);//出地
        Size2D _size2D4 = new Size2D(3.0, 5.5);//阀门
        Size2D _size2D5 = new Size2D(3.0, 6.0);//预留口、非普查
        Size2D _size2D6 = new Size2D(4.0, 2.5);//雨水篦、污水篦、溢流井、出气井
        Size2D _size2D7 = new Size2D(4.0, 4.0);//化粪池、净化池、进水口、出水口、污水井、雨水井、闸门井、跌水井、通风井、冲洗井、水封井
        Size2D _size2D8 = new Size2D(5.5, 4.0);//排水泵、雨水篦
        Size2D[] _size2Ds = new Size2D[]{_size2D7,_size2D7,_size2D7,_size2D7,_size2D6,_size2D6,_size2D7,_size2D3,_size2D1,_size2D7,
                _size2D5,_size2D5,_size2D1};

        return createThemeUnique(_objs, _ids, _color,_size2Ds);
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        String _color = "#A52A2A";

        return createThemeLabel(_color);
    }

}
