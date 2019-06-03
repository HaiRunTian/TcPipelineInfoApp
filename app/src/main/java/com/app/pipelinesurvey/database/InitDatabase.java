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
 * 从xml文件解析 数据库插入语句
 *
 * @author
 */
public class InitDatabase {
    /**
     * 从xml解析数据库插入语句
     */
    public static List<String> init(Context context) {
        try {
            //从xml文件里 解析数据库插入语句
            List<String> _listSQL;
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("database.xml"), "sql_insert_table", "sql");

            return _listSQL;

        } catch (Exception e) {
            LogUtills.e("Sql pull error", e.toString());
        }
        return null;
    }

}
