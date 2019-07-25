package com.app.pipelinesurvey.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @Author HaiRun
 * @Time 2019/4/9.9:27
 * 初始化窗体大小
 */

public class InitWindowSize {
    private static InitWindowSize  m_instance  = null;

    public synchronized static InitWindowSize ins() {
        if (m_instance == null) {
            m_instance = new InitWindowSize();
        }
        return m_instance;
    }

    public  void initWindowSize(Activity context, Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        windowParams.y = 100;
        window.setAttributes(windowParams);
        Dialog _dialog =dialog;
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.9), (int) (dm.heightPixels * 0.9));
        }
    }

    public  void initWindowSize(Activity context, Dialog dialog,double width, double height) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        windowParams.y = 100;
        window.setAttributes(windowParams);
        Dialog _dialog =dialog;
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * width), (int) (dm.heightPixels * height));
        }
    }
}
