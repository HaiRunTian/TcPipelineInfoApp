package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 15:26.
 * 电力
 */

public class ElectricPowerFieldLine extends BaseFieldLInfos {
    public ElectricPowerFieldLine(){
        type = POINTTYPE.Type_ElectricPower_L;
        datasetName = SuperMapConfig.Layer_ElectricPower;
        Init();
    }

    public ElectricPowerFieldLine(String name){
        type = POINTTYPE.Type_ElectricPower_L;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel( new Color(255, 0, 0));
    }
}
