package com.app.utills;

import android.util.Log;

/**
 * Log统一管理类
 */

public class LogUtills {

    private LogUtills() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 是否需要打印bug，可以在application的onCreate函数里面初始化
     */
    private static boolean isDebug = false;
    private static final String TAG = "TcPipe";

    public static String getToaskInfo() {
        final Throwable t = new Throwable();
        final StackTraceElement[] elements = t.getStackTrace();
        return elements[1].getClassName() + ", method: " + elements[1].getMethodName() + " ";
    }

    /**
     * 下面四个是默认tag的函数
     */
    public static void i(String msg) {
        if (isDebug) {
            final Throwable t = new Throwable();
            final StackTraceElement[] elements = t.getStackTrace();
            String _className = elements[1].getClassName();
            int _index = _className.lastIndexOf('.') + 1;
            Log.i("info:" + TAG, "class: " + _className.substring(_index) + ", method: " + elements[1].getMethodName() + ", line number:" + elements[1].getLineNumber() + ":" + msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d("debug:" + TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            final Throwable t = new Throwable();
            final StackTraceElement[] elements = t.getStackTrace();
            String _className = elements[1].getClassName();
            int _index = _className.lastIndexOf('.') + 1;
            Log.e("error:" + TAG, "class: " + _className.substring(_index) + ", method: " + elements[1].getMethodName() + ", line number:" + elements[1].getLineNumber() + ": " + msg);
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            final Throwable t = new Throwable();
            final StackTraceElement[] elements = t.getStackTrace();
            String _className = elements[1].getClassName();
            int _index = _className.lastIndexOf('.') + 1;
            Log.w("warm:" + TAG, "class: " + _className.substring(_index) + ", method: " + elements[1].getMethodName() + ", line number:" + elements[1].getLineNumber() + ": " + msg);
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
        }
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }
}
