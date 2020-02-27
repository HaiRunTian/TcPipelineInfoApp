package com.app.pipelinesurvey.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * 描述 toasty 提示显示
 *
 * @author HaiRun
 * @time 2019/7/9.9:22
 */
public class ToastyUtil {
    private static boolean init = false;

    private static void initToasty() {
        if (!init) {
            Toasty.Config.getInstance()
                    .tintIcon(true)
                    .setToastTypeface(Typeface.DEFAULT)
                    .setTextSize(10)
                    .allowQueue(true)
                    .apply();
            init = true;
        }
    }

    /**
     * toast  错误  shrot
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/9  9:31
     */
    public static void showErrorShort(Context context, String error) {
        initToasty();
        Toasty.error(context, error, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * toast  错误  long
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/9  9:31
     */
    public static void showErrorLong(Context context, String error) {
        initToasty();
        Toasty.error(context, error, Toast.LENGTH_LONG, true).show();
    }

    /**
     * toast  success  shrot
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/9  9:31
     */
    public static void showSuccessShort(Context context, String success) {
        initToasty();
        Toasty.success(context, success, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * toast  success  shrot
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/9  9:32
     */
    public static void showSuccessLong(Context context, String success) {
        initToasty();
        Toasty.success(context, success, Toast.LENGTH_SHORT, true).show();
    }


    /**
     * toast  info  shrot
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/9  9:31
     */
    public static void showInfoShort(Context context, String info) {
        initToasty();
        Toasty.info(context, info, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * toast  info  shrot
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/9  9:31
     */
    public static void showInfoLong(Context context, String info) {
        initToasty();
        Toasty.info(context, info, Toast.LENGTH_LONG, true).show();
    }

    /**
     * toast  info  shrot
     *
     * @params :context
     * @author :HaiRun
     * @date :2019/7/9  9:31
     */
    public static void showWarningShort(Context context, String warning) {
        initToasty();
        Toasty.warning(context, warning, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * toast  info  shrot
     *
     * @params :context
     * @author :HaiRun
     * @date :2019/7/9  9:31
     */
    public static void showWarningLong(Context context, String warning) {
        initToasty();
        Toasty.warning(context, warning, Toast.LENGTH_LONG, true).show();
    }


}
