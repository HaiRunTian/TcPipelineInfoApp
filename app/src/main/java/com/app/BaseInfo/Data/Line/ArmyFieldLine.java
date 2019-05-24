package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 11:30.
 * 军队
 */

public class ArmyFieldLine extends BaseFieldLInfos {
    public ArmyFieldLine(){
        type = POINTTYPE.Type_Army_B;
        datasetName = SuperMapConfig.Layer_Army;
        Init();
    }

    public ArmyFieldLine(String name){
        type = POINTTYPE.Type_Army_B;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel( new Color(51, 102, 0));
    }
}
