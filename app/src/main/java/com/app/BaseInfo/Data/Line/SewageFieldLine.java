package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 15:23.
 * 污水
 */

public class SewageFieldLine extends BaseFieldLInfos {
    public SewageFieldLine(){
        type = POINTTYPE.Type_Sewage_W;
        datasetName = SuperMapConfig.Layer_Sewage;
        Init();
    }

    public SewageFieldLine(String name){
        type = POINTTYPE.Type_Sewage_W;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel(new Color(128, 0, 0));
    }
}
