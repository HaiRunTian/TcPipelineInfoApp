package com.app.pipelinesurvey.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.app.pipelinesurvey.bean.LineAllocationInfo;
import com.app.pipelinesurvey.bean.StandardInfo;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.utills.LogUtills;

import java.util.ArrayList;

/**
 * @author
 */

public class SQLUtils {
    /**
     * 创建标准表，管点表，管线表
     */
    public static int setStandardtable(ContentValues _values, String pointTable, String lineTable) {
        try {
            DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_STANDARD_INFO, _values);
            DatabaseHelpler.getInstance().execSQL(
                    "create table if not exists " + pointTable +
                            "(id integer primary key autoincrement,name varchar,color varchar,scaleX double,scaleY double," +
                            "angle double,symbolID integer, standard varchar,minScaleVisible double,maxScaleVisible double," +
                            "lineWidth double,pipe_type varchar,city varchar)");
            DatabaseHelpler.getInstance().execSQL(
                    "create table if not exists " + lineTable +
                            "(id integer primary key autoincrement,name varchar,width double,color varchar," +
                            "symbolID integer,typename varchar,typeCode varchar,remark varchar,typeShortCall varchar," +
                            "standard varchar,city varchar)");
            return 1;
        } catch (Exception e) {
            LogUtills.e("Sql  error =  " ,e.getMessage());
            return 0;
        }
    }

    /**
     * 查询所有管点 管线标准表
     */
    public static ArrayList<String> getAll(String table) {
        ArrayList<String> list = new ArrayList<>();
        Cursor _cursor = DatabaseHelpler.getInstance().query(table,
                new String[]{"name"}, null, null, null, null, null);
        list.clear();
        while (_cursor.moveToNext()) {
            String name = _cursor.getString(_cursor.getColumnIndex("name"));
            if (!list.contains(name)) {
                list.add(name);
            }
        }
        return list;
    }

    /**
     * 获取状态
     *
     * @param name
     * @return
     */
    public static int getStatus(String name) {
        int status = 0;
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO,
                "where name = '" + name + "'");
        if (_cursor != null) {
            while (_cursor.moveToNext()) {
                status = Integer.parseInt(_cursor.getString(_cursor.getColumnIndex("status")));
                LogUtills.i("AA  =" + status);
            }
        }
        return status;
    }

    /**
     * 查询已经保存好的管类
     */
    public static ArrayList<String> getPoint(String table) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = DatabaseHelpler.getInstance().rawQuery("select distinct(pipe_type) from " + table, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String point = cursor.getString(cursor.getColumnIndex("pipe_type"));
                list.add(point);
            }
        }
        return list;
    }

    /**
     * 查询已经保存好的附属物
     */
    public static boolean getPointAdjunct(String table, String type, String name) {

        Cursor cursor = DatabaseHelpler.getInstance().query(
                table, "where name ='" + name + "' and pipe_type = '" + type + "'");
        LogUtills.i("A", name + "------" + type);
        if (cursor.getCount() != 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询已经保存好的附属物
     */
    public static ArrayList<String> getPt(String table) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = DatabaseHelpler.getInstance().query(
                table, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String point = cursor.getString(cursor.getColumnIndex("pipe_type"));
                list.add(point);
            }
        }
        return list;
    }

    /**
     * 查询已经保存好的管类
     */

    public static ArrayList<String> getLine(String table) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = DatabaseHelpler.getInstance().query(
                table, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String point = cursor.getString(cursor.getColumnIndex("typename"));
                list.add(point);
            }
        }
        return list;
    }


    /**
     * 查询所有表存不存在这个表
     *
     * @param pointTable
     * @param lineTable
     * @return
     */
    public static ArrayList<String> getStandardtable(String pointTable, String lineTable) {
        ArrayList<String> table = new ArrayList<>();
        //查询库里面存不存在这个表
        Cursor cursor = DatabaseHelpler.getInstance().rawQuery("select name from sqlite_master where type ='table' and name in('" + pointTable + "','" + lineTable + "')", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            table.add(name);
        }
        return table;
    }

    /**
     * 查询
     */
    public static LineAllocationInfo getLineInfo(String lineTable, String name) {
        LineAllocationInfo lineAllocationInfo = new LineAllocationInfo();
        Cursor query = DatabaseHelpler.getInstance().query(
                lineTable, null, null, null, null, null, null);
        if (query.getCount() != 0) {
            Cursor _cursor = DatabaseHelpler.getInstance().query(
                    lineTable, "where typename = '" + name + "'");
            if (_cursor != null) {
                while (_cursor.moveToNext()) {
                    int id = Integer.parseInt(_cursor.getString(_cursor.getColumnIndex("id")));
                    String lineName = _cursor.getString(_cursor.getColumnIndex("name"));
                    String width = (_cursor.getString(_cursor.getColumnIndex("width")));
                    String color = _cursor.getString(_cursor.getColumnIndex("color"));
                    String symbolID = _cursor.getString(_cursor.getColumnIndex("symbolID"));
                    String typeCode = _cursor.getString(_cursor.getColumnIndex("typeCode"));
                    String remark = _cursor.getString(_cursor.getColumnIndex("remark"));
                    String city = _cursor.getString(_cursor.getColumnIndex("city"));
                    lineAllocationInfo.setId(id);
                    lineAllocationInfo.setName(lineName);
                    lineAllocationInfo.setWidth(width);
                    lineAllocationInfo.setColor(color);
                    lineAllocationInfo.setSymbolID(symbolID);
                    lineAllocationInfo.setTypeCode(typeCode);
                    lineAllocationInfo.setRemark(remark);
                    lineAllocationInfo.setCity(city);
                    //设置颜色
//                    initSpinner();
//                    switch (color) {
//                        case "#00CCFF":
//                            spColor.setSelection(0);
//                            break;
//                        case "#A52A2A":
//                            spColor.setSelection(1);
//                            break;
//                        case "#FF9900":
//                            spColor.setSelection(2);
//                            break;
//                        case "#FF0000":
//                            spColor.setSelection(3);
//                            break;
//                        case "#FF6600":
//                            spColor.setSelection(4);
//                            break;
//                        case "#00FF00":
//                            spColor.setSelection(5);
//                            break;
//                        case "#0000FF":
//                            spColor.setSelection(6);
//                            break;
//                        case "#00CC66":
//                            spColor.setSelection(7);
//                            break;
//                        case "#336600":
//                            spColor.setSelection(8);
//                            break;
//                        case "#FF00FF":
//                            spColor.setSelection(9);
//                            break;
//                        default:
//                            spColor.setSelection(10);
//                            break;
//                    }
                }
            }
        }
        return lineAllocationInfo;
    }

    public static ArrayList<String> getFeature(String typenameNull, String name, String showFearture) {
        ArrayList<String> list = new ArrayList<>();
        Cursor _cursor = DatabaseHelpler.getInstance().query(
                SQLConfig.TABLE_NAME_APPENDANT_INFO, typenameNull);
        while (_cursor.moveToNext()) {
            String type = _cursor.getString(_cursor.getColumnIndex(name));
            int isShowFearture = _cursor.getInt(_cursor.getColumnIndex(showFearture));
            if (isShowFearture == 1) {
                Cursor query = DatabaseHelpler.getInstance().query(
                        SQLConfig.TABLE_NAME_FEATURE_INFO,
                        typenameNull);
                while (query.moveToNext()) {
                    String m_name = query.getString(query.getColumnIndex(name));
                    list.add(m_name);
                }
            } else {
                list.add(type);
            }
        }
        return list;
    }


    /**
     * 增加
     */
    public static StandardInfo getStandardInfo(String name) {
        StandardInfo standardInfo = new StandardInfo();
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO, "where name = '" + name + "'");
        if (_cursor != null) {
            while (_cursor.moveToNext()) {
                String _name = _cursor.getString(_cursor.getColumnIndex("name"));
                String creator = _cursor.getString(_cursor.getColumnIndex("creator"));
                String createtime = _cursor.getString(_cursor.getColumnIndex("createtime"));
                String point = _cursor.getString(_cursor.getColumnIndex("pointsettingtablesymbol"));
                String line = _cursor.getString(_cursor.getColumnIndex("linesettintable"));
                standardInfo.setName(_name);
                standardInfo.setCreator(creator);
                standardInfo.setTime(createtime);
                standardInfo.setPoint(point);
                standardInfo.setLine(line);
            }
        }
        return standardInfo;
    }


    public static int setLineInfo(String table, int id, ContentValues _values) {
        Cursor query = DatabaseHelpler.getInstance().query(
                table, "where id=" + id);
        if (query.getCount() == 0) {
            DatabaseHelpler.getInstance().insert(table, _values);
            return 1;
        } else {
            DatabaseHelpler.getInstance().update(table, _values, "id=" + id, null);
            return 2;
        }
    }
}
