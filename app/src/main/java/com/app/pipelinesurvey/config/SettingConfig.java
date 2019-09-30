package com.app.pipelinesurvey.config;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.app.pipelinesurvey.bean.Symbolbean;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HaiRun
 * @time 2019/9/17.17:45
 */
public class SettingConfig {
    private static SettingConfig settingConfig = null;
    private List<Symbolbean> m_symbolInfos  = null;
    public  synchronized static SettingConfig ins() {
        if (settingConfig == null) {
            settingConfig = new SettingConfig();
        }
        return settingConfig;
    }

    public  Map<String,String> setPontMap(){
        Map<String,String> map = new HashMap<>();
        map.put("窨井规格","wellsize");
        map.put("窨井深度","welldepth");
        map.put("窨井水深","wellwater");
        map.put("窨井淤泥","wellmud");
        map.put("井盖材质","welllidtexture");
        map.put("井盖规格","welllidsize");
        map.put("管点状态","state");
        map.put("高程","elevation");
        map.put("管偏","offset");
        map.put("建构筑物","building");
        map.put("道路名称","roadname");
        map.put("管点备注","pointremark");
        map.put("疑难问题","puzzle");
        map.put("X","x");
        map.put("Y","y");
        return map;
    }

    public  Map<String,String> setLineMap(){
        Map<String,String> map = new HashMap<>();
        map.put("起点埋深","start_depth");
        map.put("终点埋深","end_depth");
        map.put("管线长度","pipe_length");
        map.put("埋深差值","bureal_diff");
        map.put("埋设方式","embedded_way");
        map.put("管线材料","texture");
        map.put("管径","pipe_size");
        map.put("断面","secttion");
        map.put("总孔数","hole_count");
        map.put("已用孔数","used_count");
        map.put("电缆根数","amount");
        map.put("套管孔径","aperture");
        map.put("行 X 列","row_col");
        map.put("电缆电压","voltage");
        map.put("管线状态","state");
        map.put("管道压力","pressure");
        map.put("权属单位","owner_ship_unit");
        map.put("管线备注","line_remark");
        map.put("疑难问题","puzzle");
        return map;
    }

    /**
     * 管线配置
     * @Params :
     * @author :HaiRun
     * @date   :2019/9/18  10:34
     */
    public void getLineContentValues(String prjName) {
        Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_TYPE, "where city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
       while (query.moveToNext()) {
           String pipeType = query.getString(query.getColumnIndex("pipe_type"));
           ContentValues contentValues1 = new ContentValues();
           contentValues1.put("prj_name", prjName);
           contentValues1.put("start_depth", 1);
           contentValues1.put("end_depth", 1);
           contentValues1.put("pipe_length", 1);
           contentValues1.put("bureal_diff", 1);
           contentValues1.put("embedded_way", 1);
           contentValues1.put("texture", 1);
           contentValues1.put("pipe_size", 1);
           contentValues1.put("secttion", 1);
           contentValues1.put("hole_count", 1);
           contentValues1.put("used_count", 1);
           contentValues1.put("amount", 1);
           contentValues1.put("aperture", 1);
           contentValues1.put("row_col", 1);
           contentValues1.put("voltage", 1);
           contentValues1.put("state", 1);
           contentValues1.put("pressure", 1);
           contentValues1.put("owner_ship_unit", 1);
           contentValues1.put("line_remark", 1);
           contentValues1.put("puzzle", 1);
           contentValues1.put("pipetype",pipeType);
           contentValues1.put("city",SuperMapConfig.PROJECT_CITY_NAME);
           DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_LINE_SETTING,contentValues1);
       }
    }

    /**
     * 管点配置
     * @Params :
     * @author :HaiRun
     * @date   :2019/9/18  10:35
     */
    public void getContentValues(String prjName) {
        Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_TYPE, "where city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
        while (query.moveToNext()) {
            String pipeType = query.getString(query.getColumnIndex("pipe_type"));
            ContentValues contentValues = new ContentValues();
            contentValues.put("prj_name", prjName);
            contentValues.put("wellsize", 1);
            contentValues.put("welldepth", 1);
            contentValues.put("wellwater", 1);
            contentValues.put("wellmud", 1);
            contentValues.put("welllidtexture", 1);
            contentValues.put("welllidsize", 1);
            contentValues.put("state", 1);
            contentValues.put("elevation", 1);
            contentValues.put("offset", 1);
            contentValues.put("building", 1);
            contentValues.put("roadname", 1);
            contentValues.put("pointremark", 1);
            contentValues.put("puzzle", 1);
            contentValues.put("x", 1);
            contentValues.put("y", 1);
            contentValues.put("pipetype",pipeType);
            contentValues.put("city",SuperMapConfig.PROJECT_CITY_NAME);
            DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_POINT_SETTING,contentValues);
        }
    }

    public void getPipeContentValues(String prjName){
        Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_TYPE, "where city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
        while (query.moveToNext()){
            String pipeType = query.getString(query.getColumnIndex("pipe_type"));
            String code = query.getString(query.getColumnIndex("serial_no"));
            ContentValues values = new ContentValues();
            values.put("pipetype",pipeType);
            values.put("code",code);
            values.put("prj_name",prjName);
            values.put("show",1);
            values.put("city",SuperMapConfig.PROJECT_CITY_NAME);
            //插入到管类展示表
            DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_PIPE_PRJ_SHOW,values);
        }
    }

}
