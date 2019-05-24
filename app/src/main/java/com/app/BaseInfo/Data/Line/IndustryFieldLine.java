package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 11:41.
 * 工业
 */

public class IndustryFieldLine extends BaseFieldLInfos {
    public IndustryFieldLine(){
        type = POINTTYPE.Type_Industry_G;
        datasetName = SuperMapConfig.Layer_Industry;
        Init();
    }

    public IndustryFieldLine(String name){
        type = POINTTYPE.Type_Industry_G;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel( new Color(255, 0, 255));
    }
}
