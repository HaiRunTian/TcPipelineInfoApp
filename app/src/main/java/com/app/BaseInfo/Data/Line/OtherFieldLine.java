package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 12:08.
 * 其它
 */

public class OtherFieldLine extends BaseFieldLInfos {
    public OtherFieldLine(){
        type = POINTTYPE.Type_Other_Q;
        datasetName = SuperMapConfig.Layer_Other;
        Init();
    }

    public OtherFieldLine(String name){
        type = POINTTYPE.Type_Other_Q;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel( new Color(255, 0, 0));
    }
}
