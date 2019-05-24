package com.app.pipelinesurvey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.TimeUtils;

import com.app.pipelinesurvey.bean.AppInfo;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.PullXMLUtil;
import com.app.utills.LogUtills;

import org.greenrobot.greendao.database.Database;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * 初始化数据库
 * Created by Kevin on 2018-06-15.
 */

public class InitDatabase {

    //数据插入表格
    public static void init(Context context) {
        SQLiteDatabase db = DatabaseHelpler.getInstance().getReadableDatabase();
        db.beginTransaction();
        try {
            //初始化app info table
            initAppInfo(context);
            //初始化table
            List<String> _listSQL;
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("database.xml"), "sql_insert_table", "sql");
            //表格插入数据
            if (_listSQL != null && _listSQL.size() > 0) {
                for (String sql : _listSQL) {
                    LogUtills.i("Sql: " + sql);
                    DatabaseHelpler.getInstance().execSQL(sql);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtills.e("Sql inner error",e.toString());
        }finally {
          db.endTransaction();
        }
    }

    private static void initAppInfo(Context context) {
        try {
            try {
                AppInfo _appInfo = null;
                _appInfo = PullXMLUtil.parserXMLOfAppInfo(context.getAssets().open("app_info.xml"));
                ContentValues _values = new ContentValues();
                if (_appInfo != null) {
                    _values.put("app_name", _appInfo.getApp_name());
                    _values.put("version_code", _appInfo.getVersion_code());
                    _values.put("updated_date", _appInfo.getUpdated_date());
                    _values.put("dev_unit", _appInfo.getDev_unit());
                    _values.put("remark", _appInfo.getRemark());
                    DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_APP_INFO, _values);
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
