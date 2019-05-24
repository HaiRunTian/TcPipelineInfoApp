package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-15 14:00.
 * 燃气
 */

public class GasFieldLine extends BaseFieldLInfos {
    public GasFieldLine(){
        type = POINTTYPE.Type_Gas_R;
        datasetName = SuperMapConfig.Layer_Gas;
        Init();
    }

    public GasFieldLine(String name){
        type = POINTTYPE.Type_Gas_R;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel(  new Color(255, 153, 0));
    }
}
