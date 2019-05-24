package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 11:25.
 * 有视
 */

public class CableTVFieldLine extends BaseFieldLInfos {
    public CableTVFieldLine(){
        type = POINTTYPE.Type_CableTV_T;
        datasetName = SuperMapConfig.Layer_CableTV;
        Init();
    }

    public CableTVFieldLine(String name){
        type = POINTTYPE.Type_CableTV_T;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel(new Color(0, 0, 255));
    }
}
