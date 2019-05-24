package com.app.BaseInfo.Data.Point;

import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;

/**
 * Created by HaiRun on 2018-07-18 10:35.
 * 追加管点
 */

public class PipePoint extends BaseFieldPInfos{

    public PipePoint() {

        type = POINTTYPE.Type_Point;
        datasetName = SuperMapConfig.Layer_Point;
        Init();
    }

    public PipePoint(String name) {
        super(name);
        type = POINTTYPE.Type_Point;
        datasetName = name;
        Init();
    }
}
