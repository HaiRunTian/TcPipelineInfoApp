package com.app.BaseInfo.Data.Line;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;

/**
 * Created by HaiRun on 2018-07-18 10:35.
 *  追加管线
 */

public class PipeLine extends BaseFieldLInfos{

    public PipeLine() {
        type = POINTTYPE.Type_Line;
        datasetName = SuperMapConfig.Layer_Line;
        Init();
    }

    public PipeLine(String name) {
        super(name);
        type = POINTTYPE.Type_Line;
        datasetName = SuperMapConfig.Layer_Line;
        Init();
    }
}
