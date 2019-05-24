package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 11:32.
 * 交通
 */

public class TrafficFieldLine extends BaseFieldLInfos {
    public TrafficFieldLine(){
        type = POINTTYPE.Type_All_A;
        datasetName = SuperMapConfig.Layer_Traffic;
        Init();
    }

    public TrafficFieldLine(String name){
        type = POINTTYPE.Type_All_A;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel( new Color(0, 204, 102));
    }
}
