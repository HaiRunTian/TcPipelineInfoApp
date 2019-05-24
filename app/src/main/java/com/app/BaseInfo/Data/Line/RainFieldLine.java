package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 15:23.
 * 雨水
 */

public class RainFieldLine extends BaseFieldLInfos {
    public RainFieldLine(){
        type = POINTTYPE.Type_Rain_Y;
        datasetName = SuperMapConfig.Layer_Rain;
        Init();
    }

    public RainFieldLine(String name){
        type = POINTTYPE.Type_Rain_Y;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel( new Color(128, 0, 0));
    }
}
