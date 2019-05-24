package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-21 20:02.
 * 排水
 */

public class DrainageFieldLine extends BaseFieldLInfos {
    public DrainageFieldLine(){
        type = POINTTYPE.Type_Drainage_P;
        datasetName = SuperMapConfig.Layer_Drainage;
        Init();
    }

    public DrainageFieldLine(String name){
        type = POINTTYPE.Type_Drainage_P;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel( new Color(128, 0, 0));
    }
}
