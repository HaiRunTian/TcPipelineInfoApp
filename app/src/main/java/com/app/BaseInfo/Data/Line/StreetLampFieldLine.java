package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-15 14:01.
 * 路灯
 */

public class StreetLampFieldLine extends BaseFieldLInfos {
    public StreetLampFieldLine(){
        type = POINTTYPE.Type_StreetLamp_S;
        datasetName = SuperMapConfig.Layer_StreetLamp;
        Init();
    }

    public StreetLampFieldLine(String name){
        type = POINTTYPE.Type_StreetLamp_S;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel(new Color(255, 102, 0));
    }
}
