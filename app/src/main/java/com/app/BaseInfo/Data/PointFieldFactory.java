package com.app.BaseInfo.Data;

import android.database.Cursor;

import com.app.BaseInfo.Data.Point.MeasurePoint;
import com.app.BaseInfo.Data.Point.TheTotalPoint;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.PipeThemelabelUtil;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.supermap.data.Point2D;

/**
 * Created by Los on 2018-06-22 16:43.
 * 点工厂
 */
public class PointFieldFactory {

    public static BaseFieldPInfos Create(POINTTYPE type) {
        BaseFieldPInfos _info = null;
        switch (type) {

            default:
                break;
        }
        return _info;
    }

    public static BaseFieldPInfos Create(String name) {

        if (name.equals(SuperMapConfig.Layer_Measure)) {
            return new MeasurePoint();
        }
        return null;
    }

    public static BaseFieldPInfos CreateInfo(String typeName) {
        //需要配置点类表的信息；
        BaseFieldPInfos _pInfo = new BaseFieldPInfos();
        _pInfo.rangeExpression = PipeThemelabelUtil.Ins().getThemeItemValue(typeName);
        return _pInfo;

    }

    public static BaseFieldPInfos Create() {
        return new TheTotalPoint();
    }

    /**
     * 创建临时点
     *
     * @Params :
     * @author :HaiRun
     * @date :2020/1/3  9:10
     */
    public static BaseFieldPInfos createTempInfo(Point2D pt, String type, String code) {
        try {
            //创建临时点
            BaseFieldPInfos _tempPt = new TheTotalPoint();
            _tempPt.subsid = "临时点";
            //时间
            _tempPt.exp_Date = DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT);
            //状态
            _tempPt.situation = "正常";
            _tempPt.pipeType = type;
            _tempPt.code = "O";
            //样式
            _tempPt.symbolExpression = "O-临时";
            String[] _num = ComTool.Ins().getPointNumber(code, "");
            _tempPt.id = _num[0];
            _tempPt.serialNum = Integer.parseInt(_num[1]);
            _tempPt.exp_Num = _num[0];
            _tempPt.symbol = "探测点";
            _tempPt.latitude = pt.getY();
            _tempPt.longitude = pt.getX();
            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
            if (_cursor.moveToNext()) {
                _tempPt.expGroup = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
            }
            DataHandlerObserver.ins().createRecords2(_tempPt);
            return _tempPt;
        } catch (Exception e) {
            return null;
        }
    }
}
