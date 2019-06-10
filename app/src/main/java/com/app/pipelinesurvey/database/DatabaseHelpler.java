package com.app.pipelinesurvey.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.app.utills.LogUtills;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQLite帮助类
 * Created by Kevin on 2018-06-13.
 */

public class DatabaseHelpler extends SQLiteOpenHelper {
    /**
     * 当前数据库
     */
    public static String currentDB;
    //版本号
    private static final int VERSION = 4;
    /**
     * 建表语句
     */
    private List<String> createTableSQLList;
    /**
     * 数据库实例映像表
     */
    private static Map<String, DatabaseHelpler> dbMaps = new HashMap<>();
    private static DatabaseHelpler s_currentInstance;
    private Context m_context;

    /**
     * @param context   上下文
     * @param dbName    数据库名称
     * @param tableSQLs 数据库表集合
     * @author HaiRun
     * created at 2018/12/6 13:25
     */

    private DatabaseHelpler(Context context, String dbName, List<String> tableSQLs) {
        super(context, dbName, null, VERSION);
        m_context = context;
        currentDB = dbName;
        createTableSQLList = new ArrayList<>();
        //数据库建表语句列表
        createTableSQLList.addAll(tableSQLs);
    }

    public static DatabaseHelpler getInstance() {
        if (s_currentInstance == null) {
            throw new IllegalArgumentException("未实例化该类");
        }
        return s_currentInstance;
    }

    /**
     * 获取实例
     *
     * @param context   上下文
     * @param dbName    数据库名
     * @param tableSQLs 建表语句列表
     * @return databaseHelpler sqlite帮助类实例
     * @datetime 2018-06-13  11:22.
     */
    public static DatabaseHelpler getInstance(Context context, String dbName, List<String> tableSQLs) {
        DatabaseHelpler databaseHelpler = dbMaps.get(dbName);
        if (databaseHelpler == null) {
            synchronized (DatabaseHelpler.class) {
                if (databaseHelpler == null)
                    databaseHelpler = new DatabaseHelpler(context, dbName, tableSQLs);
                dbMaps.put(dbName, databaseHelpler);
            }
        }
        currentDB = dbName;
        s_currentInstance = databaseHelpler;
        return databaseHelpler;
    }

    /**
     * 创建数据库表格
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建数据 广州模式表格和插入数据 版本号3
        createSqlAndInserDataForGZ(db);

        //更新点特征 附属物表 字段，添加字段city 并且赋值 广州 版本号4
        alterSqlAndInserDataForGZ(db);

        //创建深圳模式表格和插入数据 版本号4
        createSqlAndInserDataForSZ(db);
        LogUtills.i("Sql onCreate","onCreate()");


    }

    /**
     * 更新点特征 附属物表 字段，添加字段city 并且赋值 广州 版本号4
     * @param db
     */
    private void alterSqlAndInserDataForGZ(SQLiteDatabase db) {
        List<String> list = InitDatabase.getAlterSql();
        for (String sql : list) {
            LogUtills.i(" Sql alter or update=", sql);
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                LogUtills.e(" Sql alter or update ", e.getMessage());
            }
        }
    }

    /**
     * 创建数据 广州模式表格和插入数据
     * @param db
     */
    private void createSqlAndInserDataForGZ(SQLiteDatabase db) {
        //创建广州数据库
        for (String sql : createTableSQLList) {
            LogUtills.i(" Sql cteate=", sql);
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                LogUtills.e(" Sql create error ", e.getMessage());
            }
        }

        //插入广州模式
        List<String> sqls = InitDatabase.getInserSql(m_context);
        //插入数据语句
        for (String sql : sqls) {
            LogUtills.i(" Sql inner =", sql);
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                LogUtills.e(" Sql inner error ", e.getMessage());
            }
        }
    }

    /**
     *   创建深圳模式表格和插入数据
     */
    private void createSqlAndInserDataForSZ(SQLiteDatabase db) {
        //升级版本4 新建深圳模式点线 配置表
        List<String> crateSql = InitDatabase.getShenZhenCteateSql(m_context);
        if (crateSql != null) {
            for (String sql : crateSql) {
                LogUtills.i(" Sql   cteate=", sql);
                try {
                    db.execSQL(sql);
                } catch (Exception e) {
                    LogUtills.e(" Sql create error ", e.getMessage());
                }
            }
        }

        //升级版本4  插入深圳模式配置表

        List<String> inserSqlShenzhen = InitDatabase.getShenZhenInserSql(m_context);
        //插入数据语句
        for (String sql : inserSqlShenzhen) {
            LogUtills.i(" Sql inner =", sql);
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                LogUtills.e(" Sql inner error ", e.getMessage());
            }
        }
    }

    /**
     * 数据库升级版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:


            case 2:


            case 3:
                //更新点特征 附属物表 字段，添加字段city 并且赋值 广州 版本号4
                alterSqlAndInserDataForGZ(db);

                //创建深圳模式表格和插入数据 版本号4
                createSqlAndInserDataForSZ(db);
                LogUtills.i("Sql onUpdate","onUpdate()");
            case 4:


            default:
                break;
        }
    }

    /**
     * 执行sql语句
     *
     * @param sql      sql语句
     * @param bindArgs 文本值
     * @return null
     * @datetime 2018-06-13  14:55.
     */
    public void execSQL(String sql, Object[] bindArgs) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase db = databaseHelpler.getWritableDatabase();
            db.execSQL(sql, bindArgs);
        }
    }

    /**
     * 执行sql语句
     *
     * @param sql sql语句
     * @return null
     * @datetime 2018-06-13  14:55.
     */
    public void execSQL(String sql) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase db = databaseHelpler.getWritableDatabase();
            LogUtills.i("SQL", sql);
            db.execSQL(sql);
        }
    }

    /**
     * 块查询
     *
     * @param sql      sql语句
     * @param bindArgs 文本值
     * @return cursor cursor对象
     * @datetime 2018-06-13  14:56.
     */
    public Cursor rawQuery(String sql, String[] bindArgs) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, bindArgs);
            return cursor;
        }
    }

    /**
     * 插入操作
     *
     * @param table         插入的表名
     * @param contentValues 字段及值
     * @return null
     * @datetime 2018-06-13  14:57.
     */
    public void insert(String table, ContentValues contentValues) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getWritableDatabase();
            database.insert(table, null, contentValues);
        }
    }

    /**
     * 更新操作
     *
     * @param table       表名
     * @param values      字段及值
     * @param whereClause 提交
     * @param whereArgs   文本值
     * @return null
     * @datetime 2018-06-13  14:58.
     */
    public void update(String table, ContentValues values, String whereClause, String[]
            whereArgs) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getWritableDatabase();
            database.update(table, values, whereClause, whereArgs);
        }
    }

    /**
     * 删除操作
     *
     * @param table       表名
     * @param whereClause 提交
     * @param whereArgs   文本值
     * @return null
     * @datetime 2018-06-13  14:58.
     */
    public void delete(String table, String whereClause, String[] whereArgs) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getWritableDatabase();
            database.delete(table, whereClause, whereArgs);
        }
    }

    /**
     * 点表查询操作
     *
     * @param columns       字段名
     * @param selection     where字段
     * @param selectionArgs where字段对应值
     * @param groupBy       分组条件
     * @param having        筛选条件
     * @param orderBy       排序条件
     * @return cursor Cursor对象
     * @datetime 2018-06-13  14:58.
     */
    public Cursor queryPoint(String[] columns, String selection, String[] selectionArgs, String
            groupBy, String having,
                             String orderBy) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getReadableDatabase();
            Cursor cursor = database.query(SQLConfig.TABLE_DEFAULT_POINT_SETTING, columns, selection, selectionArgs, groupBy, having, orderBy);
            return cursor;
        }
    }

    /**
     * 线表查询操作
     *
     * @param columns       字段名
     * @param selection     where字段
     * @param selectionArgs where字段对应值
     * @param groupBy       分组条件
     * @param having        筛选条件
     * @param orderBy       排序条件
     * @return cursor Cursor对象
     * @datetime 2018-06-13  14:58.
     */
    public Cursor queryLine(String[] columns, String selection, String[] selectionArgs, String
            groupBy, String having,
                            String orderBy) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getReadableDatabase();
            Cursor cursor = database.query(SQLConfig.TABLE_DEFAULT_LINE_SETTING, columns, selection, selectionArgs, groupBy, having, orderBy);
            return cursor;
        }
    }

    /**
     * 查询操作
     *
     * @param table         表名
     * @param columns       字段名
     * @param selection     where字段
     * @param selectionArgs where字段对应值
     * @param groupBy       分组条件
     * @param having        筛选条件
     * @param orderBy       排序条件
     * @return cursor Cursor对象
     * @datetime 2018-06-13  14:58.
     */
    public Cursor query(String table, String[] columns, String selection, String[]
            selectionArgs, String groupBy, String having,
                        String orderBy) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getReadableDatabase();
            Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            return cursor;
        }
    }

    /**
     * 查询操作
     *
     * @param table         表名
     * @param columns       列名
     * @param selection     where字段
     * @param selectionArgs where字段值
     * @param groupBy       分组条件
     * @param having        筛选条件
     * @param orderBy       排序条件
     * @param limit         限制返回数据数
     * @return cursor Cursor对象
     * @datetime 2018-06-13  15:14.
     */
    public Cursor query(String table, String[] columns, String selection, String[]
            selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getReadableDatabase();
            Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            return cursor;
        }
    }


    /**
     * 原生查询
     *
     * @param tableName 表名
     * @param tableName 查询条件
     * @return cursor Cursor对象
     * @datetime 2018-06-13  15:17.
     */
    public Cursor query(String tableName, String sqlString) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from " + tableName + " " + sqlString, null);
            return cursor;
        }
    }

    /**
     * 原生查询 查询表格全部内容
     *
     * @param tableName 表名
     * @param
     * @return cursor Cursor对象
     * @datetime 2018-06-13  15:17.
     */
    public Cursor query(String tableName) {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        synchronized (databaseHelpler) {
            SQLiteDatabase database = databaseHelpler.getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from " + tableName, null);
            return cursor;
        }
    }

    /**
     * 关闭及清除当前操作的DB
     *
     * @return null
     * @datetime 2018-06-13  15:18.
     */
    public void clear() {
        DatabaseHelpler databaseHelpler = dbMaps.get(currentDB);
        databaseHelpler.close();
        dbMaps.remove(databaseHelpler);
    }

    /**
     * 导出DB文件到外部根目录
     *
     * @param context 上下文
     * @param dbName  数据库名（xx.db）
     * @return null
     * @datetime 2018-06-14  11:47.
     */
    public static boolean exportDBFile2External(Context context, String dbName) {
        //找到文件的路径  /data/data/包名/databases/数据库名称
        LogUtills.i("程序数据路径 = ", context.getFilesDir().toString());
        File dbFile = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/" +
                context.getPackageName() + "/databases/" + dbName);
        File dbSaveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "PipeLineDB/");
        if (!dbSaveFile.exists())
            dbSaveFile.mkdirs();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //文件复制到sd卡中
            if (dbFile.exists()) {
                fis = new FileInputStream(dbFile);
                fos = new FileOutputStream(dbSaveFile.getAbsolutePath() + "/" + dbName);
                int len = 0;
                byte[] buffer = new byte[2048];
                while (-1 != (len = fis.read(buffer))) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            //关闭数据流
            try {
                if (fos != null)
                    fos.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}


