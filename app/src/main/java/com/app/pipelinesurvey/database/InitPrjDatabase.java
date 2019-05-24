package com.app.pipelinesurvey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.app.pipelinesurvey.bean.AppInfo;
import com.app.pipelinesurvey.config.SharedPrefManager;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.PullXMLUtil;
import com.app.utills.LogUtills;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * 初始化项目数据库
 * Created by HaiRun on 2019-3-18.
 */

public class InitPrjDatabase {

    //数据插入表格
    public static void init(Context context, String dbName) {

        SQLiteDatabase db = null;
        try {
            //穿件数据库表list
            List<String> _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("prjdbconfig.xml"), "sql_create_table", "sql_create");
            //创建数据库并且创建表格
            DatabaseHelpler.getInstance(context, dbName + ".db", _listSQL).getWritableDatabase();
            db = DatabaseHelpler.getInstance().getReadableDatabase();
            db.beginTransaction();
            //数据库表插入数据
            SharedPrefManager _manager = new SharedPrefManager(context,
                    SharedPrefManager.FILE_CONFIG);
            //第一次创建数据库，插入表格默认数据，以后就不用再插入
            boolean isInited = (boolean) _manager.getSharedPreference(dbName, false);
            if (!isInited) {
                _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                        .open("prjdbconfig.xml"), "sql_insert_table", "sql");
                if (_listSQL != null && _listSQL.size() > 0) {
                    for (String sql : _listSQL) {
                        LogUtills.i("insert Sql=",sql);
                        DatabaseHelpler.getInstance().execSQL(sql);

                    }
                }
                db.setTransactionSuccessful();
                //更新工程表工程名
                String _sql = "update " + SQLConfig.TABLE_NAME_POINT_NAME_CONFIG + " set Name = '" + dbName + "' where id = 1";
                DatabaseHelpler.getInstance().execSQL(_sql);
                _manager.put(dbName, true);
            }
        } catch (Exception e) {
            LogUtills.w(e.toString());
        } finally {
            db.endTransaction();
        }
    }

}
