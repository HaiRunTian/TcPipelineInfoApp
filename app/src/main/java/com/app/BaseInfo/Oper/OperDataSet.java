package com.app.BaseInfo.Oper;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.pipelinesurvey.utils.PipeThemelabelUtil;
import com.app.pipelinesurvey.utils.SymbolInfo;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfos;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.mapping.Layer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author HaiRun
 * @time 2019/8/2.9:34
 * 操作记录集类  用于增删改查
 */
public class OperDataSet {
    private static OperDataSet operDataSet = null;

    public OperDataSet() {
    }

    public static OperDataSet getSingleton() {
        if (operDataSet == null) {
            operDataSet = new OperDataSet();
        }
        return operDataSet;
    }

    /**
     * 通过bean类生成点记录集  批量增加
     * @Params :
     * @author :HaiRun
     * @date :2019/8/2  9:40
     */
    public boolean createPointSetByBean(List<Map<String, Object>> baseFieldPInfos) {

        Layer _pLayer = DataHandlerObserver.ins().getTotalPtLayer();
        if (_pLayer == null) {
            return false;
        }
        try {
            DatasetVector dv = (DatasetVector) _pLayer.getDataset();
            FieldInfos _infos = dv.getFieldInfos();
            Recordset _result = dv.getRecordset(true, CursorType.DYNAMIC);
            // 获得记录集对应的批量更新对象
            Recordset.BatchEditor editor = _result.getBatch();
            // 设置批量更新每次提交的记录数目
            editor.setMaxRecordCount(1000);
            // 从 World 数据集中读取几何对象和字段值，批量更新到 example 数据集中
            editor.begin();
            //循环遍历
            for (int i = 0; i < baseFieldPInfos.size(); i++) {
                Map<String, Object> map = baseFieldPInfos.get(i);
                Object longitude =map.get("longitude");
                Object latitude =map.get("latitude");
                Double x = Double.parseDouble(longitude.toString());
                Double y = Double.parseDouble(latitude.toString());
                double longitudeX = x == null?0:x;
                double latitudeY = y == null?0:y;
                boolean ok = _result.addNew(new GeoPoint(longitudeX, latitudeY), map);
                //标签专题图颜色显示字段
                String pipeType = (String) map.get("pipeType");
                String subsid = (String) map.get("subsid");
                String feature = (String) map.get("feature");
                String code = (String) map.get("code");
                _result.setDouble("rangeExpression", PipeThemelabelUtil.Ins().getThemeItemValue(pipeType.substring(0, 2)));
                String str = SymbolInfo.Ins().getSymbol(pipeType, subsid, feature);
//                LogUtills.i("symbolExpression", str);
                str = (str.trim().length() == 0) ? "探测点" : str;
                _result.setString("symbolExpression", code + "-" + str);
//                LogUtills.i("symbolExpression", code + "-" + str + "=========" + pipeType.substring(0, 2) + PipeThemelabelUtil.Ins().getThemeItemValue(pipeType.substring(0, 2)));
            }
            // 批量操作统一提交
            editor.update();
            // 释放记录集
            _result.dispose();
            return true;
        } catch (Exception e) {
            LogUtills.e("PoerDataSetJava",e.toString());
            return false;
        }
    }

    /**
     * 通过bean类生成点记录集  批量增加
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/8/2  9:40
     */
    public boolean createLineSetByBean(List<Map<String, Object>> baseFieldLInfos) {

        Layer _pLayer = DataHandlerObserver.ins().getTotalLrLayer();
        if (_pLayer == null) {
            return false;
        }
        try {
            DatasetVector _dv = (DatasetVector) _pLayer.getDataset();
            FieldInfos _infos = _dv.getFieldInfos();
            Recordset _result = _dv.getRecordset(true, CursorType.DYNAMIC);
            Recordset.BatchEditor editor = _result.getBatch();
            editor.setMaxRecordCount(1000);
            editor.begin();
            for (int i = 0; i < baseFieldLInfos.size(); i++) {
                Map<String, Object> map = baseFieldLInfos.get(i);
                Point2Ds _p2ds = new Point2Ds();

                Object startX = map.get("startLongitude");
                Object startY = map.get("startLatitude");
                Object endX = map.get("endLongitude");
                Object endY = map.get("endLatitude");

                Double x1 = Double.parseDouble(startX.toString());
                Double y1 = Double.parseDouble(startY.toString());
                Double x2 = Double.parseDouble(endX.toString());
                Double y2 = Double.parseDouble(endY.toString());

                double startLongitude = x1 == null?0:x1;
                double startLatitude = y1 == null?0:y1;
                double endLongitude = x2 == null?0:x2;
                double endLatitude = y2 == null?0:y2;

                _p2ds.add(new Point2D(startLongitude, startLatitude));
                _p2ds.add(new Point2D(endLongitude, endLatitude));
                GeoLine _geoLine = new GeoLine(_p2ds);
                boolean ok = _result.addNew(_geoLine, map);
                _result.setInt32("sysId", _result.getID());
                //标签专题图颜色显示字段
                String pipeType = (String) map.get("pipeType");
                String pipeSize = (String) map.get("pipeSize");
                String d_S = (String) map.get("d_S");
                String material = (String) map.get("material");
                _result.setDouble("rangeExpression", PipeThemelabelUtil.Ins().getThemeItemValue(pipeType.substring(0, 2)));
                //标签专题图字体显示字段
                String ds = (pipeSize.trim().length() != 0) ? pipeSize : d_S;
                //标签专题图显示的文字信息
                _result.setString("labelTag",pipeType.substring(pipeType.length() - 1) + "-" + ds + "-" + material);
                //释放对象
                _geoLine.dispose();
            }
            //批量更新
            editor.update();
            // 关闭记录集，释放几何对象、记录集
            _result.dispose();
            return true;
        } catch (Exception e) {
            LogUtills.e("PoerDataSetJava",e.toString());
            return false;
        }
    }
}
