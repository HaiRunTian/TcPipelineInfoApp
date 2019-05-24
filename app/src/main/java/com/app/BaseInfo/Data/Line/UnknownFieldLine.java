package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.supermap.data.Color;
import com.supermap.mapping.ThemeLabel;

/**
 * Created by Los on 2018-06-26 11:53.
 * 不明
 */

public class UnknownFieldLine extends BaseFieldLInfos {
    public UnknownFieldLine(){
        type = POINTTYPE.Type_Unknown_N;
        datasetName = SuperMapConfig.Layer_Unknown;
        Init();
    }

    public UnknownFieldLine(String name){
        type = POINTTYPE.Type_Unknown_N;
        datasetName = name;
        Init();
    }

    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        return createThemeLabel(new Color(0, 0, 0));
    }
}
