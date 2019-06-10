package com.app.pipelinesurvey.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class AssetsUtils {
    private static WeakReference<AssetManager> sAssetManager = null;

    private static Boolean sInited = false;

    private static AssetsUtils sInstance = null;

    private AssetsUtils() {

    }

    public static void init(Context context) {
        if (!sInited) {
            AssetManager assets = context.getAssets();
            sAssetManager = new WeakReference<AssetManager>(assets);
            sInstance = new AssetsUtils();
            sInited = true;
        }
    }

    public static AssetsUtils getInstance() {
        if (!sInited) {
            throw new IllegalArgumentException("Please call getInserSql() firstly");
        }
        return sInstance;
    }

    public InputStream open(String fileName) {
        if (sInited) {
            try {
                return sAssetManager.get().open(fileName, AssetManager.ACCESS_STREAMING);
            } catch (Exception e) {
//				e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取assets中的文件夹里的所有文件
     *
     * @param dir
     * @return
     */
    public String[] opendDir(String dir) {
        if (sInited) {
            try {
                return sAssetManager.get().list(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
