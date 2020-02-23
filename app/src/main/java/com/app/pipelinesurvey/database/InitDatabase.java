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
     * 版本升级4 创建深圳模式点表线表
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
     * 版本升级4 插入深圳模式各表数据
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
     *
     * @return
     */
    public static List<String> getAlterSql() {
        //存放sql语句
        List<String> _list = new ArrayList<>();
        //附属物添加city字段
        String sqlAlterAppendant = "alter table appendant_info add column city varchar ";

        //点特征添加city字段
        String sqlAlterFeature_info = "alter table feature_info add column city varchar ";

        //管类添加city字段
        String sqlAlterPipe_info = "alter table pipe_info add column city varchar ";

        //管类材质添加city字段
        String sqlAlterPipe_texture = "alter table pipe_texture add column city varchar ";

        //管点备注添加city字段
        String sqlAlterPoint_remark = "alter table point_remark add column city varchar ";

        //给先添加的字段填入数据city
        String sqlUpdateAppendan_info = "update appendant_info set city = '广州'";

        //给先添加的字段填入数据city
        String sqlUpdateFeature_info = "update feature_info set city = '广州'";

        //给先添加的字段填入数据city
        String sqlUpdatePipe_info = "update pipe_info set city = '广州'";

        //给广州模式先添加管类材质表字段填入数据city
        String sqlUpdatePipe_texture = "update pipe_texture set city = '广州'";

        //给广州模式先添加的管点备注表字段填入数据city
        String sqlUpdatePoint_remark = "update point_remark set city = '广州'";

        //点特征二通换为三通
        String sqlUpdateFeature_info2 = "update feature_info set typename = '三通' where typename = '二通'";

        _list.add(sqlAlterAppendant);
        _list.add(sqlAlterFeature_info);
        _list.add(sqlAlterPipe_info);
        _list.add(sqlAlterPipe_texture);
        _list.add(sqlAlterPoint_remark);

        _list.add(sqlUpdateAppendan_info);
        _list.add(sqlUpdateFeature_info);
        _list.add(sqlUpdatePipe_info);
        _list.add(sqlUpdatePipe_texture);
        _list.add(sqlUpdatePoint_remark);
        _list.add(sqlUpdateFeature_info2);

        return _list;
    }

    /**
     * 版本升级5 创建现场检测记录表
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/6/26  15:29
     */
    public static List<String> getCteateSqlOf5(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("database_db_5.xml"), "sql_create_table", "sql_create");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据库6  点特征 附属物 表添加 show 字段
     *
     * @return
     */
    public static List<String> getAlterSqlOfSix() {
        //存放sql语句
        List<String> _list = new ArrayList<>();
        //附属物添加show字段
        String sqlAlterAppendant = "alter table appendant_info add column show varchar ";

        //点特征添加show字段
        String sqlAlterFeature_info = "alter table feature_info add column show varchar ";

        //管类添加show字段
        String sqlAlterPipe_info = "alter table pipe_info add column show varchar ";

        //标签专题图添加city字段
        String sqlAlterPipe_themelabel = "alter table pipe_themelabel add column city varchar ";

        //给先添加的字段填入数据show
        String sqlUpdateAppendan_info = "update appendant_info set show = '1'";

        //给先添加的字段填入数据show
        String sqlUpdateFeature_info = "update feature_info set show = '1'";

        //给先添加的字段填入数据show
        String sqlUpdatePipe_info = "update pipe_info set show = '1'";
        //给先添加的字段填入数据show
        String sqlUpdatePipe_themelabel = "update pipe_themelabel set city = '广州'";

        _list.add(sqlAlterAppendant);
        _list.add(sqlAlterFeature_info);
        _list.add(sqlAlterPipe_info);
        _list.add(sqlAlterPipe_themelabel);
        _list.add(sqlUpdateAppendan_info);
        _list.add(sqlUpdateFeature_info);
        _list.add(sqlUpdatePipe_info);
        _list.add(sqlUpdatePipe_themelabel);
        return _list;
    }


    /**
     * 版本升级6 创建深圳模式点表线表
     */
    public static List<String> getHuiZhouCteateSql(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("huizhou_db_6.xml"), "sql_create_table", "sql_create");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本升级6 插入惠州模式各表数据
     */
    public static List<String> getHZInserSql(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("huizhou_db_6.xml"), "sql_insert_table", "sql");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本升级7 创建点配置 线配置
     */
    public static List<String> getCteateSql7(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("database_db_7.xml"), "sql_create_table", "sql_create");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本  7  更新路灯杆的符号点
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/9/19  10:48
     */
    public static List<String> getAlterSqlOf7() {
        //存放sql语句
        List<String> _list = new ArrayList<>();
        //给先添加的字段填入数据show
        String sqlUpdate = "update default_point_huizhou set symbolID = 473 where name = 'LS-路灯杆'";
        _list.add(sqlUpdate);
        return _list;
    }

    /**
     * 版本  8 点线配置表插入一个管类
     * @Params :
     * @author :HaiRun
     * @date :2019/9/19  10:48
     */
    public static List<String> getAlterSqlOf8() {
        //存放sql语句
        List<String> _list = new ArrayList<>();
        //给先添加的字段填入数据show
        //点特征添加show字段
        String sqlAlterPointViewSetting = "alter table point_view_setting add column pipetype varchar";
        String sqlAlterPointViewSetting2 = "alter table point_view_setting add column city varchar";
        String sqlAlterLineViewSetting = "alter table line_view_setting add column pipetype varchar";
        String sqlAlterLineViewSetting2 = "alter table line_view_setting add column city varchar";
        //更新临时点颜色 修改为黑色
        String sqlUpdate1 = "update default_point_setting set color = '#000000' where pipe_type = '临时点-O'";
        String sqlUpdate2 = "update default_point_shenzhen set color = '#000000' where pipe_type = '临时点-O'";
        String sqlUpdate3 = "update default_point_huizhou set color = '#000000' where pipe_type = '临时点-O'";
        String sqlUpdate4 = "update default_line_setting set color = '#000000' where typename = '临时-O'";
        String sqlUpdate5 = "update default_line_shenzhen set color = '#000000' where typename = '临时-O'";
        String sqlUpdate6 = "update default_line_huizhou set color = '#000000' where typename = '临时-O'";
        String sqlUpdate7 = "update pipe_themelabel set color = '#000000' where pipetype = '临时-O'";
        _list.add(sqlAlterPointViewSetting);
        _list.add(sqlAlterLineViewSetting);
        _list.add(sqlAlterPointViewSetting2);
        _list.add(sqlAlterLineViewSetting2);
        _list.add(sqlUpdate1);
        _list.add(sqlUpdate2);
        _list.add(sqlUpdate3);
        _list.add(sqlUpdate4);
        _list.add(sqlUpdate5);
        _list.add(sqlUpdate6);
        _list.add(sqlUpdate7);
        return _list;
    }

    /**
     * 版本升级8 创建点线单个项目是否显示
     */
    public static List<String> getCteateSql8(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("database_db_8.xml"), "sql_create_table", "sql_create");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getInsertSqlOf9(Context context){
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("database_db_9.xml"), "sql_insert_table", "sql");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本  10 点线配置表插入一个管类
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/12/11  10:48
     */
    public static List<String> getAlterSqlOf10() {
        //存放sql语句
        List<String> _list = new ArrayList<>();
        //给先添加的字段填入数据show
        //点特征添加show字段
        String sqlAlterProjectInfo = "alter table project_info add column mode varchar";

        _list.add(sqlAlterProjectInfo);
        return _list;
    }

    /**
     * 版本升级10 创建点 线 外检模式表 项目
     */
    public static List<String> getCteateSql10(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("database_db_10.xml"), "sql_create_table", "sql_create");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 版本升级11 创建正本清源模式点表线表
     */
    public static List<String> getZhengBenCteateSql(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("zhengben_db_11.xml"), "sql_create_table", "sql_create");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本升级11 插入正本清源模式各表数据
     */
    public static List<String> getZhengBenInserSql(Context context) {
        List<String> _listSQL = null;
        try {
            _listSQL = PullXMLUtil.parserXML2SqlList(context.getAssets()
                    .open("zhengben_db_11.xml"), "sql_insert_table", "sql");
            return _listSQL;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本  8 点线配置表插入一个管类
     * @Params :
     * @author :HaiRun
     * @date :2019/9/19  10:48
     */
    public static List<String> getAlterSqlOf12() {
        //存放sql语句
        List<String> _list = new ArrayList<>();

        String sqlUpdate = "update default_line_zhengben set typename = '雨水-YS' where typeCode = 'YS'";


        _list.add(sqlUpdate);
        return _list;
    }
}
