package com.app.pipelinesurvey.utils;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *  @描述 ActivityUtil 活动工具类
 *  @作者  Kevin
 *  @创建日期 2018/2/28  15:49.
 */

public class ActivityUtil {

    private static List<AppCompatActivity> s_activities =  new ArrayList<AppCompatActivity>();

    public static void addActivity(AppCompatActivity activity){
        s_activities.add(activity);
    }

    public static void removeActivity(AppCompatActivity activity) {
        s_activities.remove(activity);
    }

    public static void finishAllActivity() {
        if (s_activities.size()>0) {
            for (AppCompatActivity _activity : s_activities) {
                if (!_activity.isFinishing()) {
                    _activity.finish();
                }
            }
        }
    }
}
