package com.app.BaseInfo.Oper;

import android.content.ContentValues;
import android.database.Cursor;

import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HaiRun
 * @time 2019/12/12.10:17
 * 外检模式，更新数据库点表 线表
 * 插入新数据
 * point_update  line_update
 */
public class OperSql {

    private static OperSql operSql = null;

    private OperSql() {
    }

    public static OperSql getSingleton() {
        if (operSql == null) {
            operSql = new OperSql();
        }
        return operSql;
    }

    /**
     * 点数据 更改 更改点号插入更改表
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/12/12  10:29
     */
    public void inserPoint(String expNum, double x, double y, String state) {
        ContentValues point = new ContentValues();
        point.put("prjName", SuperMapConfig.PROJECT_NAME);
        point.put("expNum", expNum);
        point.put("state", state);
        point.put("x", x);
        point.put("y", y);
        point.put("dateTime", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT));
        DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_POINT_UPDATE, point);
    }

    /**
     * 线数据 更改 更改点号插入更改表
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/12/12  10:30
     */
    public void inserLine(String benExpNum, String endExpNum, int lineId, String state) {
        ContentValues line = new ContentValues();
        line.put("prjName", SuperMapConfig.PROJECT_NAME);
        line.put("benExpNum", benExpNum);
        line.put("endExpNum", endExpNum);
        line.put("lineId", lineId);
        line.put("state", state);
        line.put("dateTime", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT));
        DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_LINE_UPDATE, line);
    }

    /**
     * 通过项目名称查询对应的点数据
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/12/16  14:07
     */
    public List<List<String>> queryPoint(String prjName) {
        if (!prjName.isEmpty()) {
            Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_POINT_UPDATE, "where prjName = '" + prjName + "'");
            List<List<String>> list = new ArrayList<>();
            while (query.moveToNext()) {
                List<String> map = new ArrayList<>();
                String prjName1 = query.getString(query.getColumnIndex("prjName"));
                String expNum = query.getString(query.getColumnIndex("expNum"));
                String x = String.valueOf(query.getDouble(query.getColumnIndex("x")));
                String y = String.valueOf(query.getDouble(query.getColumnIndex("y")));
                String state = query.getString(query.getColumnIndex("state"));
                String dateTime = query.getString(query.getColumnIndex("dateTime"));
                map.add(prjName1);
                map.add(expNum);
                map.add(x);
                map.add(y);
                map.add(state);
                map.add(dateTime);
                list.add(map);
            }
            query.close();
            return list;
        } else {
            return null;
        }
    }

    /**
     * 通过项目名称查询对应的点数据
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/12/16  14:07
     */
    public List<List<String>> queryPoint(String prjName, String startTime, String endTime) {
        if (!prjName.isEmpty()) {
            Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_POINT_UPDATE, "where prjName = '" + prjName + "' and (dateTime between '"
                    + startTime + " 00:00:00" + "' and '" + endTime + " 23:59:00" + "')");
            List<List<String>> list = new ArrayList<>();
            while (query.moveToNext()) {
                List<String> map = new ArrayList<>();
                String prjName1 = query.getString(query.getColumnIndex("prjName"));
                String expNum = query.getString(query.getColumnIndex("expNum"));
                String x = String.valueOf(query.getDouble(query.getColumnIndex("x")));
                String y = String.valueOf(query.getDouble(query.getColumnIndex("y")));
                String state = query.getString(query.getColumnIndex("state"));
                String dateTime = query.getString(query.getColumnIndex("dateTime"));
                map.add(prjName1);
                map.add(expNum);
                map.add(x);
                map.add(y);
                map.add(state);
                map.add(dateTime);
                list.add(map);
            }
            query.close();
            return list;
        } else {
            return null;
        }
    }

    /**
     * 通过项目名称查询对应的线数据
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/12/16  14:07
     */
    public List<List<String>> queryLine(String prjName) {
        if (!prjName.isEmpty()) {
            Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_LINE_UPDATE, "where prjName = '" + prjName + "'");
            List<List<String>> list = new ArrayList<>();
            while (query.moveToNext()) {
                List<String> map = new ArrayList<>();
                String prjName1 = query.getString(query.getColumnIndex("prjName"));
                String benExpNum = query.getString(query.getColumnIndex("benExpNum"));
                String endExpNum = query.getString(query.getColumnIndex("endExpNum"));
                String state = query.getString(query.getColumnIndex("state"));
                String dateTime = query.getString(query.getColumnIndex("dateTime"));
                map.add(prjName1);
                map.add(benExpNum);
                map.add(endExpNum);
                map.add(state);
                map.add(dateTime);
                list.add(map);
            }
            query.close();
            return list;
        } else {
            return null;
        }
    }

    /**
     * 通过项目名称查询对应的线数据
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/12/16  14:07
     */
    public List<List<String>> queryLine(String prjName, String startTime, String endTime) {
        if (!prjName.isEmpty()) {
            Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_LINE_UPDATE, "where prjName = '" + prjName + "' and (dateTime between '"
                    + startTime + " 00:00:00" + "' and '" + endTime + " 23:59:00" + "')");
            List<List<String>> list = new ArrayList<>();
            while (query.moveToNext()) {
                List<String> map = new ArrayList<>();
                String prjName1 = query.getString(query.getColumnIndex("prjName"));
                String benExpNum = query.getString(query.getColumnIndex("benExpNum"));
                String endExpNum = query.getString(query.getColumnIndex("endExpNum"));
                String state = query.getString(query.getColumnIndex("state"));
                String dateTime = query.getString(query.getColumnIndex("dateTime"));
                map.add(prjName1);
                map.add(benExpNum);
                map.add(endExpNum);
                map.add(state);
                map.add(dateTime);
                list.add(map);
            }
            query.close();
            return list;
        } else {
            return null;
        }
    }
}
