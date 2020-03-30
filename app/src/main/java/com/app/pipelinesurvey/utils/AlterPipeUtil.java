package com.app.pipelinesurvey.utils;

import com.app.pipelinesurvey.database.DatabaseHelpler;

/**
 * @author HaiRun
 * @time 2020/3/30.15:19
 * 修改管类工具类
 */
public class AlterPipeUtil {
    public static AlterPipeUtil m_ins = null;

    public static AlterPipeUtil Ins() {
        if (m_ins == null) {
            m_ins = new AlterPipeUtil();
        }
        return m_ins;
    }

    /**
     * 获取当前城市管类代码长度
     * @Params :
     * @author :HaiRun
     * @date   :2020/3/30  15:21
     */
    public int getPipeCodeLength() {
        return 0;
    }
}
