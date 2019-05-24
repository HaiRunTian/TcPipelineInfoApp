package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 11:02.
 * 电信
 */

public class TelecomFieldLine extends BaseFieldLInfos {
    public TelecomFieldLine(){
        type = POINTTYPE.Type_Telecom_D;
        datasetName = SuperMapConfig.Layer_Telecom;
        Init();
    }

    public TelecomFieldLine(String name){
        type = POINTTYPE.Type_Telecom_D;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel( new Color(0, 255, 0));
    }
}
