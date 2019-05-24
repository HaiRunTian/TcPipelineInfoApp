package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 15:25.
 * 煤气
 */

public class CoalGasFieldLine extends BaseFieldLInfos {
    public CoalGasFieldLine(){
        type = POINTTYPE.Type_CoalGas_M;
        datasetName = SuperMapConfig.Layer_CoalGas;
        Init();
    }

    public CoalGasFieldLine(String name){
        type = POINTTYPE.Type_CoalGas_M;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel( new Color(255, 153, 0));
    }
}
