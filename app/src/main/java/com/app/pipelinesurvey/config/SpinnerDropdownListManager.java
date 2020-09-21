package com.app.pipelinesurvey.config;

import android.content.Context;
import android.database.Cursor;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hairun
 */

public class SpinnerDropdownListManager {

    /**
     * 数组转list
     *
     * @param arrays
     * @return
     */
    public static List<String> getData(String[] arrays) {
        List<String> _list = new ArrayList<>();
        for (String item : arrays) {
            _list.add(item);
        }
        return _list;
    }

    /**
     * 可以通过管类代码 查询数据库表 获取到特征点附属物
     *
     * @Author HaiRun
     * @Time 2019/3/30 . 10:17
     */
    public static List<String> getData(String type, String value) {
        List<String> list = new ArrayList<>();

        switch (type) {
            //特征点TABLE_NAME_FEATURE_INFO
            case "feature": {
                Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_FEATURE_INFO, "where code = '" + value.substring(3) + "' and city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
                while (_cursor.moveToNext()) {
                    String _var = _cursor.getString(_cursor.getColumnIndex("name"));
                    list.add(_var);
                }
                _cursor.close();
            }
            break;
            //附属物
            case "subsid": {
                Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_APPENDANT_INFO, "where code = '" + value.substring(3) + "' and city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
                while (_cursor.moveToNext()) {
                    String _var = _cursor.getString(_cursor.getColumnIndex("name"));
                    list.add(_var);
                }
                _cursor.close();
            }
            break;
            //管点备注
            case "pointRemark":
            {
                Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_POINT_REMARK, "where pipe_type = '" + value + "' and city = '"+ SuperMapConfig.PROJECT_CITY_NAME + "'");
                while (_cursor.moveToNext()) {
                    String _var = _cursor.getString(_cursor.getColumnIndex("remark"));
                    list.add(_var);
                }
                _cursor.close();
            }
            break;
            //管线备注
            case "lineRemark":
            {
                Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_LINE_REMARK, "where pipe_type = '" + value + "'");
                while (_cursor.moveToNext()) {
                    String _var = _cursor.getString(_cursor.getColumnIndex("remark"));
                    list.add(_var);
                }
                _cursor.close();
            }
            break;
            //管线材料
            case "lineTexture": {
                Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_TEXTURE, "where pipe_type = '" + value + "' and city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
                while (_cursor.moveToNext()) {
                    String _var = _cursor.getString(_cursor.getColumnIndex("texture"));
                    list.add(_var);
                }
                _cursor.close();
            }
            break;
            case "type":{
                Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_INFO, "where city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
                while (_cursor.moveToNext()) {
                    String _var = _cursor.getString(_cursor.getColumnIndex("code"));
                    list.add(_var);
                }
                _cursor.close();
            }
            break;
            default:
                break;
        }
        list.add(" ");
        return list;
    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner 要设置的Spinner
     * @param value   要匹配的值
     */
    public static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        //得到SpinnerAdapter对象
        SpinnerAdapter apsAdapter = spinner.getAdapter();
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
//                spinner.setSelection(i,true);// 默认选中项
                // 默认选中项
                spinner.setSelection(i);
                break;
            }
        }
    }
}
