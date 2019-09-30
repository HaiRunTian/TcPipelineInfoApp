package com.app.pipelinesurvey.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by hairun.tian on 2018/1/29 0029.
 * 获取软件版本号
 */

public class GetVersionNum {

    private static GetVersionNum s_getVersionNum = null;

    public  synchronized static GetVersionNum ins() {
        if (s_getVersionNum == null) {
            s_getVersionNum = new GetVersionNum();
        }
        return s_getVersionNum;
    }

    /**
     * 获取本地软件版本号
     */
    public int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public  String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
