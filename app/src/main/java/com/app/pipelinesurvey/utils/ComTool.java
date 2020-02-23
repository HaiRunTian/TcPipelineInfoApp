package com.app.pipelinesurvey.utils;

import android.database.Cursor;

import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.bean.PointConfig;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.CursorType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.services.ServiceBase;

import java.util.regex.Pattern;

/**
 * Created by Los on 2019-03-16 17:31.
 */

public class ComTool {

    public static ComTool m_ins = null;

    public static ComTool Ins() {
        if (m_ins == null) {
            m_ins = new ComTool();
        }
        return m_ins;
    }

    public static Color colorByOxString(String value) {
        value = value.substring(1);
        int colorInt = Integer.valueOf(value, 16);
        return new Color(colorInt);
    }

    /**
     * 获取管点编号  获取管点编号  查看point_setting表有用户设置的格式
     * （组号长度  组号位置 流水号长度 是否临时点 管点状态）
     */
    public String[] getPointNumber(String code, Boolean isTemp, String stats) {

        //查询数据库表
        String _groupName = "";
        //组号位置
        int _groupLocal = 1;
        //流水号长度
        int _serialNum = 1;
        //查询数据库point_setting表，配置信息

        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
        while (_cursor.moveToNext()) {
            //获取组号
            _groupName = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
            //组号位置
            _groupLocal = _cursor.getInt(_cursor.getColumnIndex("GroupLocal"));
            //流水号长度
            _serialNum = _cursor.getInt(_cursor.getColumnIndex("SerialNum"));
        }

//        String _query = isTemp ? "exp_Num like 'T_%'" : "exp_Num Not like 'T_%'";
        String _query = isTemp ? "subsid = '临时点'" : "subsid != '临时点'";
        // 设置查询参数
        QueryParameter _parameter = new QueryParameter();
        _parameter.setAttributeFilter(_query);
        _parameter.setCursorType(CursorType.STATIC);
        //降序
        _parameter.setOrderBy(new String[]{"serialNum desc"});

        //数据量多，会耗时
        int _index = 0;
        String SerialNum = "";
        if (MaxExpNumID.getInstance().getId() == 0 || !SuperMapConfig.PROJECT_NAME.equals(MaxExpNumID.getInstance().getPrjName())) {
            Recordset _reset = DataHandlerObserver.ins().QueryRecordsetByParameter(_parameter, true);

            //数据集不为空
            if (!_reset.isEmpty()) {
                _reset.moveFirst();
                //获取到流水号
                _index = _reset.getInt32("serialNum");
            }
            //流水号+1
            MaxExpNumID.getInstance().setId(_index);
            _index++;
            SerialNum = String.format("%0" + _serialNum + "d", _index);
            MaxExpNumID.getInstance().setPrjName(SuperMapConfig.PROJECT_NAME);
        } else {
            SerialNum = String.format("%0" + _serialNum + "d", MaxExpNumID.getInstance().getId() + 1);
        }

        //重新编号
//        String SerialNum = String.format("%0" + _serialNum + "d", _index);
        LogUtills.i("SerialNum:" + SerialNum);
        String _newExpNum = "";
        // 有组号
        if (_groupName.length() != 0) {
            switch (_groupLocal) {
                case 1:
                    _newExpNum = (isTemp ? "T_" : "") + code + _groupName + SerialNum;
                    break;
                case 2:
                    _newExpNum = (isTemp ? "T_" : "") + _groupName + code + SerialNum;
                    break;
                case 3:
                    _newExpNum = (isTemp ? "T_" : "") + code + SerialNum + _groupName;
                    break;
                default:
                    break;
            }
        } else {
            //没有组号  J0001
            _newExpNum = (isTemp ? "T_" : "") + code + SerialNum;
        }

        return new String[]{_newExpNum, SerialNum};
    }

    /**
     * 临时点
     * 获取管点编号  获取管点编号  查看point_setting表有用户设置的格式
     * （组号长度  组号位置 流水号长度 是否临时点 管点状态）
     */
    public String[] getPointNumber(String code, String stats) {

        //查询数据库表
        String _groupName = "";
        //组号位置
        int _groupLocal = 1;
        //流水号长度
        int _serialNum = 1;
        //查询数据库point_setting表，配置信息

        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
        while (_cursor.moveToNext()) {
            //获取组号
            _groupName = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
            //组号位置
            _groupLocal = _cursor.getInt(_cursor.getColumnIndex("GroupLocal"));
            //流水号长度
            _serialNum = _cursor.getInt(_cursor.getColumnIndex("SerialNum"));
        }

        String _query = "subsid = '临时点'";
        // 设置查询参数
        QueryParameter _parameter = new QueryParameter();
        _parameter.setAttributeFilter(_query);
        _parameter.setCursorType(CursorType.STATIC);
        //降序
        _parameter.setOrderBy(new String[]{"serialNum desc"});
        Long startTime = System.currentTimeMillis();
        //数据量多，会耗时
        int _index = 0;
        String SerialNum = "";
        Recordset _reset = DataHandlerObserver.ins().QueryRecordsetByParameter(_parameter, true);
        Long endTiem = System.currentTimeMillis();
        //数据集不为空
        if (!_reset.isEmpty()) {
            _reset.moveFirst();
            //获取到流水号
            _index = _reset.getInt32("serialNum");
        }
        _index++;

        SerialNum = String.format("%0" + _serialNum + "d", _index);

        //重新编号
//        String SerialNum = String.format("%0" + _serialNum + "d", _index);
        LogUtills.i("SerialNum:" + SerialNum);
        String _newExpNum = "";
        // 有组号
        if (_groupName.length() != 0) {
            switch (_groupLocal) {
                case 1:
                    _newExpNum = "T_" + code + _groupName + SerialNum;
                    break;
                case 2:
                    _newExpNum = "T_" + _groupName + code + SerialNum;
                    break;
                case 3:
                    _newExpNum = "T_" + code + SerialNum + _groupName;
                    break;
                default:
                    break;
            }
        } else {
            //没有组号  J0001
            _newExpNum = "T_" + code + SerialNum;
        }

        return new String[]{_newExpNum, SerialNum};
    }

    /**
     * @param expNum 编号
     * @param isTemp 是否是临时点
     * @return 是否重号
     */
    public boolean isSameNum(String expNum, boolean isTemp) {
        boolean _isSame = false;
        Recordset _reset = DataHandlerObserver.ins().queryRecordsetByExpNum(expNum, false);
        if (!_reset.isEmpty()) {
            _isSame = true;
        }
        return _isSame;
    }

    /**
     * 返回流水号
     * 当用户自动更改了管点编号流水号后调用
     *
     * @Author HaiRun
     * @Time 2019/4/8 . 9:28
     */
    public int getSerialNum(String expNum, String state, String code) {

        Pattern _pattern = Pattern.compile("[a-zA-Z]");
        // 1管点编号后面以为是字母并且是状态是正常 然后去掉后面的字母
        if (_pattern.matcher(expNum.substring(expNum.length() - 1)).find() && (!state.equals("正常"))) {
            expNum = expNum.substring(0, expNum.length() - 1);
        }
        //查询数据库表
        String _groupName = "";
        int _groupLocal = 1;
        int _serialNum = 0;
        //查询数据库point_setting表，配置信息
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
        while (_cursor.moveToNext()) {
            //获取组号
            _groupName = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
            //组号位置
            _groupLocal = _cursor.getInt(_cursor.getColumnIndex("GroupLocal"));
            //流水号长度
            _serialNum = _cursor.getInt(_cursor.getColumnIndex("SerialNum"));
        }
        String _tempId = "";
        //组号位置 组号长度 管类代码长度
        if (expNum.contains("T_")){
            expNum = expNum.substring(2);
        }
        if (_groupName.length() != 0) {
            //有组号
            if (_groupLocal == 1 || _groupLocal == 2) {
                //判断组号位置  第一位 第二位  A1J0001  JA10001
                _tempId = expNum.substring(_groupName.length() + code.length());
            } else {
                //最后一位  J0001A1
                _tempId = expNum.substring(code.length(), expNum.length() - _groupName.length());
            }
        } else { //没有组号  J0001
            _tempId = expNum.substring(code.length());
        }
        LogUtills.i("Query Max exp_Num = " + expNum + ", _tempId:" + _tempId);
        _serialNum = Integer.valueOf(_tempId).intValue();
        return _serialNum;
    }

}
