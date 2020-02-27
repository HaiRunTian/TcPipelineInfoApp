package com.app.BaseInfo.Oper;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.Line.PsCheckLine;
import com.app.BaseInfo.Data.Line.TheTotalLine;
import com.app.BaseInfo.Data.Point.MeasurePoint;
import com.app.BaseInfo.Data.Point.TheTotalPoint;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.GeoStyle;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Recordset;
import com.supermap.data.Size2D;
import com.supermap.data.Workspace;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.Map;
import com.supermap.mapping.Selection;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeUnique;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2018/6/14 0014.
 * 初始化图层、专题图
 */
public class OperNotifyer {
    private Workspace m_workSpace;
    /**
     * 创建数据集
     * 可以依据用户勾选了哪一种管类再生成
     * @param ds
     */
    public static void AddSystemLayers(Datasource ds ) {
        List<BaseFieldInfos> _infos = new ArrayList();
        _infos.add(new TheTotalPoint());
        _infos.add(new TheTotalLine());
        _infos.add(new MeasurePoint());
        _infos.add(new PsCheckLine());
        for (int i = 0; i < _infos.size(); ++i) {
            DatasetVector _resultDv = CreateDataset(ds, _infos.get(i));
            //设置数据集的投影坐标系
//            _resultDv.setPrjCoordSys(prjCoordSysType);
            _resultDv.setPrjCoordSys(new PrjCoordSys(PrjCoordSysType.PCS_USER_DEFINED));
            //数据集添加到地图中
            AddAndSetLayerType(_resultDv, _infos.get(i));
        }
    }

    /**
     * 创建数据集  费时间
     *
     * @param ds   数据源
     * @param info 对象
     * @return 矢量数据集
     */
    private static DatasetVector CreateDataset(Datasource ds, BaseFieldInfos info) {
        // 以下代码示范通过矢量数据集信息创建矢量数据集
        Datasets datasets = ds.getDatasets();
        if (datasets.contains(info.datasetName)) {
            return (DatasetVector) datasets.get(info.datasetName);
        }
        //返回一个数据源中未被使用的数据集的名称。
        String name = datasets.getAvailableDatasetName(info.datasetName);
        // 设置矢量数据集的信息类
        DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
        if (info instanceof BaseFieldLInfos) {
            datasetVectorInfo.setType(DatasetType.LINE);
        } else if (info instanceof BaseFieldPInfos) {
            datasetVectorInfo.setType(DatasetType.POINT);
        } else {
            return null;
        }
        datasetVectorInfo.setEncodeType(EncodeType.NONE);
        datasetVectorInfo.setName(name);
        // 创建矢量数据集
        DatasetVector datasetVector = datasets.create(datasetVectorInfo);
        if (datasetVector == null) {
            LogUtills.i("数据集创建失败");
        }
        //数据集添加到图层
        AddFieldInfo(datasetVector, info);
        // 释放资源
        return datasetVector;
    }


    /**
     * @param dv   数据源
     * @param info 数据集字段信息类
     */
    private static void AddFieldInfo(DatasetVector dv, BaseFieldInfos info) {
        try {
            FieldInfos _infos = dv.getFieldInfos();
            Field[] _field = info.getClass().getFields();
            // 遍历所有属性
            for (int j = 0; j < _field.length; j++) {
                // 获取属性的名字
                String _name = _field[j].getName();
                String type = _field[j].getGenericType().toString();
                if (_name.length() > 20) {
                    continue;
                }
                // 如果type是类类型，则前面包含"class "，后面跟类名
                FieldInfo _fieldinfo = new FieldInfo();
                _fieldinfo.setCaption(_name);
                _fieldinfo.setName(_name);
                _fieldinfo.setDefaultValue("");
                switch (type) {
                    case "class com.app.BaseInfo.Data.POINTTYPE":
                    case "class java.lang.String": {
                        _fieldinfo.setType(FieldType.TEXT);
                    }
                    break;
                    case "float": {
                        _fieldinfo.setType(FieldType.SINGLE);
                    }
                    break;
                    case "double": {
                        _fieldinfo.setType(FieldType.DOUBLE);
                    }
                    break;
                    case "int": {
                        _fieldinfo.setType(FieldType.INT32);
                    }
                    break;
                    case "boolean":{
                        _fieldinfo.setType(FieldType.BOOLEAN);
                    }
                    break;
                    default:
                        LogUtills.i("没有创建成功字段名 = ", _name);
                        continue;
                }
                _infos.add(_fieldinfo);
                _fieldinfo.dispose();
            }
        } catch (Exception ex) {
            LogUtills.e("add field info: " + ex.toString());
        }
    }

    /**
     * 把适量数据集添加到地图图层
     * @param dv
     * @param info
     */
    private static void AddAndSetLayerType(DatasetVector dv, BaseFieldInfos info) {
        Map map = WorkSpaceUtils.getInstance().getMapControl().getMap();
        Layer _temp = map.getLayers().add(dv, true);
        _temp.setSymbolScalable(false);
        _temp.setVisible(false);
        //点图层
        if (info instanceof BaseFieldPInfos) {
            LayerSettingVector _vector = new LayerSettingVector();
            //设置点图层风格
            GeoStyle geoStyle_P = new GeoStyle();
            geoStyle_P.setMarkerSize(new Size2D(0.5, 0.5));
            geoStyle_P.setMarkerSymbolID(1);
            geoStyle_P.setFillBackColor(new Color(255, 0, 0));
            geoStyle_P.setLineColor(new Color(255, 0, 0));
            geoStyle_P.setFillForeColor(new Color(255, 0, 0));
            _vector.setStyle(geoStyle_P);
            _temp.setAdditionalSetting(_vector);
            BaseFieldPInfos _temp_info = (BaseFieldPInfos) info;
            //标签专题图
            ThemeLabel _theme = _temp_info.createThemeLabel();
            //设置该图层标签专题图
            Layer _resultLayer = map.getLayers().add(dv, _theme, true);
            //设置最小可见
            _resultLayer.setMinVisibleScale(1/3500.0);
            _resultLayer.setVisible(true);
//            _resultLayer.setSymbolScalable(true);
            //设置该图层单值专题图 符号库
            ThemeUnique _uniqueTheme = _temp_info.createDefaultThemeUnique();
            _resultLayer = map.getLayers().add(dv, _uniqueTheme, true);
            //设置单值专题图选中样式
            Selection _selection = _resultLayer.getSelection();
            GeoStyle _style = _uniqueTheme.getDefaultStyle();
            _style.setLineColor(new Color(255, 0, 0));
            _style.setMarkerSize(new Size2D(((BaseFieldPInfos) info).symbolSizeX, ((BaseFieldPInfos) info).symbolSizeY));
            _style.setMarkerSymbolID(((BaseFieldPInfos) info).symbolID);
            _selection.setStyle(_style);
            _resultLayer.setVisible(true);
            _resultLayer.setSymbolScalable(true);
            map.refresh();
        }

        if (info instanceof BaseFieldLInfos) {
            //对象是排水外检，则不加入图层
            if (info instanceof PsCheckLine){
                return;
            }

            BaseFieldLInfos _temp_info = (BaseFieldLInfos) info;
            ThemeLabel _theme = _temp_info.createThemeLabel();
            //标签专题图
            Layer _resultLayer = map.getLayers().add(dv, _theme, true);
            if (_resultLayer == null) {
                LogUtills.w("Add ThemeLable Layer Faild...");
            }
            _resultLayer.setVisible(false);
            //单值专题图
            ThemeUnique _uniqueTheme = _temp_info.createDefaultThemeUnique();
            _resultLayer = map.getLayers().add(dv, _uniqueTheme, true);
            Selection _selection = _resultLayer.getSelection();
            GeoStyle _style = new GeoStyle();
            _style.setLineColor(new Color(255, 0, 0));
            _style.setLineWidth(0.2);
            _style.setLineSymbolID(0);
            _selection.setStyle(_style);
            _resultLayer.setSymbolScalable(true);
            _resultLayer.setVisible(true);
        }
    }

    /**
     * 通过bean获取数据集字段 与 值map
     * @param info
     * @return
     */
    public static java.util.Map<java.lang.String, java.lang.Object> getFieldMaps(BaseFieldInfos info) {
        try {
            java.util.Map<java.lang.String, java.lang.Object> _values = new HashMap<String, Object>();
            Field[] _field = info.getClass().getFields();
            // 遍历所有属性 会把所有的字段存进去，一些系统自带的字段夜壶存进去
            for (int j = 0; j < _field.length; j++) {
                //获取value值
                Object _obj = _field[j].get(info);
                String _name = _field[j].getName();
                _values.put(_name, _obj);
            }
            return _values;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过记录集获取 字段名和值
     * @param result
     * @return
     */
    public static java.util.Map<java.lang.String, java.lang.Object> getFieldMaps(Recordset result) {
        try {
            if (result.isEmpty()) {
                return null;
            }
            java.util.Map<java.lang.String, java.lang.Object> _values = new HashMap<String, Object>();
            FieldInfos _infos = result.getFieldInfos();
            for (int i = 0; i < _infos.getCount(); ++i) {
                if (_infos.get(i).isSystemField()) {
                    continue;
                }
                String _fieldName = _infos.get(i).getName();
                Object _value = result.getObject(_fieldName);
                _values.put(_fieldName, _value);
            }
            return _values;

        } catch (Exception e) {
            return null;
        }
    }

}
