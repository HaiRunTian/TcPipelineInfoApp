package com.app.pipelinesurvey.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Kevin on 2018-05-15.
 */

public class AppUtil {
    /** 处理当app在后台时通过点击icon激活
     *  @param  activity 传入的活动
     *  @return   null
     *  @datetime 2018-05-15  11:25.
     */
    public static void handleClickAppIconInBackground(Activity activity) {
        /*判断app在后台时再次点击app图标启动程序*/
        if ((activity.getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            activity.finish();
            return;
        }
    }
}
