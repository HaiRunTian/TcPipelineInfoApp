package com.app.pipelinesurvey.database;

import android.content.Context;

import com.app.pipelinesurvey.utils.PullXMLUtil;
import com.app.utills.LogUtills;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
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
    public static List<String> getInserSql(Context context) {
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

    /**
     *   版本升级4 创建深圳模式点表线表
     */
    public static List<String> getShenZhenCteateSql(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("shenzhen_db.xml"), "sql_create_table", "sql_create");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *   版本升级4 插入深圳模式各表数据
     */
    public static List<String> getShenZhenInserSql(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("shenzhen_db.xml"), "sql_insert_table", "sql");

            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据库 点特征 附属物 表添加 city 字段
     * @return
     */
    public static List<String> getAlterSql(){
        //存放sql语句
        List<String> _list = new ArrayList<>();
        //附属物添加city字段
        String sqlAlterAppendant = "alter table appendant_info add column city varchar ";

        //点特征添加city字段
        String sqlAlterFeature_info= "alter table feature_info add column city varchar ";

        //管类添加city字段
        String sqlAlterPipe_info= "alter table pipe_info add column city varchar ";

        //给先添加的字段填入数据city
        String sqlUpdateAppendan_info = "update appendant_info set city = '广州'";

        //给先添加的字段填入数据city
        String sqlUpdateFeature_info = "update feature_info set city = '广州'";

        //给先添加的字段填入数据city
        String sqlUpdatePipe_info = "update pipe_info set city = '广州'";

        _list.add(sqlAlterAppendant);
        _list.add(sqlAlterFeature_info);
        _list.add(sqlAlterPipe_info);
        _list.add(sqlUpdateAppendan_info);
        _list.add(sqlUpdateFeature_info);
        _list.add(sqlUpdatePipe_info);

        return _list;

    }



}
