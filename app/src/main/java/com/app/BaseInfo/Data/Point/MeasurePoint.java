package com.app.BaseInfo.Data.Point;

import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Size2D;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeUnique;

/**
 * Created by Administrator on 2018/11/9 0009.
 */

public class MeasurePoint extends BaseFieldPInfos {

    public MeasurePoint() {

        type = POINTTYPE.Type_Measure_Point;
        datasetName = SuperMapConfig.Layer_Measure;
        Init();
    }

    public MeasurePoint(String name) {
        super(name);
        type = POINTTYPE.Type_Measure_Point;
        datasetName = name;
        Init();
    }


    /**
     * 单值专题图
     * @return
     */
    @Override
    public ThemeUnique createDefaultThemeUnique() {
        LogUtills.i("begin " + this.getClass().getName() + " createDefaultThemeUnique....");
        super.createThemeUnique();

        String[] _objs = new String[]{"测量收点"};
        int[] _ids = new int[]{493};
        String[] _color = new String[]{"#FF0000"};

        //规程管点符号 大小 * 2.5
        Size2D _size2D1 = new Size2D(10, 10);

        Size2D[] _size2Ds = new Size2D[]{_size2D1};

        return createThemeUnique("symbolExpression",_objs, _ids, _color,_size2Ds);
    }

    /**
     * 标签专题图
     * @return
     */
    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        String _color = "#A52A2A";
        return createThemeLabel(_color);
    }
}
