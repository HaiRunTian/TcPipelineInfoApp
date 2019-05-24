package com.app.pipelinesurvey.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.app.pipelinesurvey.greendao.gen.DaoMaster;

/**
 * @Author HaiRun
 * @Time 2019/2/15.14:39
 */

public class DBManager {
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    private  static String dbName = " ";

    public DBManager(Context context,String dbName) {
        this.context = context;
        this.dbName = dbName;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }


    /**
     * 获取单例引用
     *
     * @param context·
     * @return
     */
    public static DBManager getInstance(Context context,String dbName) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context,dbName);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }



}
