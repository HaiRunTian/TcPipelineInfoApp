package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.FillGradientMode;
import com.supermap.data.GeoStyle;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeUnique;
import com.supermap.mapping.ThemeUniqueItem;

/**
 * Created by Los on 2018-06-19 17:55.
 * 给水
 */

public class WaterSupplyFieldLine extends BaseFieldLInfos {
    public WaterSupplyFieldLine(){
        type = POINTTYPE.Type_WaterSupply_J;
        datasetName = SuperMapConfig.Layer_WaterSupply;
        Init();
    }

    public WaterSupplyFieldLine(String name){
        type = POINTTYPE.Type_WaterSupply_J;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
            super.createThemeLabel();
        return createThemeLabel( new Color(0, 204, 255));
    }
}
