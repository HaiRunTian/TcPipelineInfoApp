package com.app.pipelinesurvey.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kevin on 2018-06-22.
 */

public class DateTimeUtil {
    public static final String FULL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FULL_DATE_TIME_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyyMMdd_HHmmss";
    public static final String FULL_DATE_FORMAT2 = "yyyyMMdd";
    /**
     *  按格式取当前时间文本
     */
    public static String setCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        String currentTime = sdf.format(date);
        return currentTime;
    }


    /**
     * 屏幕触屏时间
     * @param lastClickTime
     * @return
     */
    public static boolean isFastDoubleClick(long lastClickTime) {
        long startTime = System.currentTimeMillis();
        long time = startTime - lastClickTime;
        return time <= 800;
    }





}
