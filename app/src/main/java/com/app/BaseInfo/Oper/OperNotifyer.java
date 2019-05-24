package com.app.BaseInfo.Oper;

import android.util.Log;
import android.util.TimeUtils;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.Line.ArmyFieldLine;
import com.app.BaseInfo.Data.Line.CableTVFieldLine;
import com.app.BaseInfo.Data.Line.CoalGasFieldLine;
import com.app.BaseInfo.Data.Line.DrainageFieldLine;
import com.app.BaseInfo.Data.Line.ElectricPowerFieldLine;
import com.app.BaseInfo.Data.Line.GasFieldLine;
import com.app.BaseInfo.Data.Line.IndustryFieldLine;
import com.app.BaseInfo.Data.Line.OtherFieldLine;
import com.app.BaseInfo.Data.Line.PipeLine;
import com.app.BaseInfo.Data.Line.RainFieldLine;
import com.app.BaseInfo.Data.Line.SewageFieldLine;
import com.app.BaseInfo.Data.Line.StreetLampFieldLine;
import com.app.BaseInfo.Data.Line.TelecomFieldLine;
import com.app.BaseInfo.Data.Line.TheTotalLine;
import com.app.BaseInfo.Data.Line.TrafficFieldLine;
import com.app.BaseInfo.Data.Line.UnknownFieldLine;
import com.app.BaseInfo.Data.Line.WaterSupplyFieldLine;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.BaseInfo.Data.Point.ArmyFieldPoint;
import com.app.BaseInfo.Data.Point.CableTVFieldPoint;
import com.app.BaseInfo.Data.Point.CoalGasFieldPoint;
import com.app.BaseInfo.Data.Point.DrainageFieldPoint;
import com.app.BaseInfo.Data.Point.ElectricPowerFieldPoint;
import com.app.BaseInfo.Data.Point.GasFieldPoint;
import com.app.BaseInfo.Data.Point.IndustryFieldPoint;
import com.app.BaseInfo.Data.Point.MeasurePoint;
import com.app.BaseInfo.Data.Point.OtherFieldPoint;
import com.app.BaseInfo.Data.Point.PipePoint;
import com.app.BaseInfo.Data.Point.RainFieldPoint;
import com.app.BaseInfo.Data.Point.SewageFieldPoint;
import com.app.BaseInfo.Data.Point.StreetLampFieldPoint;
import com.app.BaseInfo.Data.Point.TelecomFieldPoint;
import com.app.BaseInfo.Data.Point.TempPoint;
import com.app.BaseInfo.Data.Point.TheTotalPoint;
import com.app.BaseInfo.Data.Point.TrafficFieldPoint;
import com.app.BaseInfo.Data.Point.UnknownFieldPoint;
import com.app.BaseInfo.Data.Point.WaterSupplyFieldPoint;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.utils.DateTimeUtil;
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
import com.supermap.mapping.LayerSettingType;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.Selection;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeRange;
import com.supermap.mapping.ThemeUnique;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/6/14 0014.
 * 初始化图层、专题图
 */
public class OperNotifyer {

    private Workspace m_workSpace;

    /**
     * 创建数据集
     * 可以依据用户勾选了哪一种管类再生成
     *
     * @param ds
     */
    public static void AddSystemLayers(Datasource ds) {
        List<BaseFieldInfos> _infos = new ArrayList();
      /*  _infos.add(new WaterSupplyFieldPoint());
        _infos.add(new SewageFieldPoint());
        _infos.add(new RainFieldPoint());
        _infos.add(new DrainageFieldPoint());
        _infos.add(new CoalGasFieldPoint());
        _infos.add(new GasFieldPoint());
        _infos.add(new ElectricPowerFieldPoint());
        _infos.add(new StreetLampFieldPoint());
        _infos.add(new TelecomFieldPoint());
        _infos.add(new CableTVFieldPoint());
        _infos.add(new ArmyFieldPoint());
        _infos.add(new TrafficFieldPoint());
        _infos.add(new IndustryFieldPoint());
        _infos.add(new OtherFieldPoint());
        _infos.add(new UnknownFieldPoint());
        _infos.add(new WaterSupplyFieldLine());
        _infos.add(new SewageFieldLine());
        _infos.add(new RainFieldLine());*/
        /*_infos.add(new DrainageFieldLine());
        _infos.add(new CoalGasFieldLine());
        _infos.add(new GasFieldLine());
        _infos.add(new ElectricPowerFieldLine());
        _infos.add(new StreetLampFieldLine());
        _infos.add(new TelecomFieldLine());
        _infos.add(new CableTVFieldLine());
        _infos.add(new ArmyFieldLine());
        _infos.add(new TrafficFieldLine());
        _infos.add(new IndustryFieldLine());
        _infos.add(new OtherFieldLine());
        _infos.add(new UnknownFieldLine());
        _infos.add(new TempPoint()); */
        _infos.add(new TheTotalPoint());
        _infos.add(new TheTotalLine());
        _infos.add(new MeasurePoint());
//        _infos.add(new PipePoint());
//        _infos.add(new PipeLine());

        for (int i = 0; i < _infos.size(); ++i) {
            DatasetVector _resultDv = CreateDataset(ds, _infos.get(i));
            //设置数据集的投影坐标系
            _resultDv.setPrjCoordSys(new PrjCoordSys(PrjCoordSysType.PCS_USER_DEFINED));
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
        // 假设打开一个工作空间 workspace 对象，工作空间中存在一个数据源 datasource 对象
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
        if (datasetVector != null) {
        }
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


    private static void AddAndSetLayerType(DatasetVector dv, BaseFieldInfos info) {

        Map map = WorkSpaceUtils.getInstance().getMapControl().getMap();
        Layer _temp = map.getLayers().add(dv, true);
        _temp.setVisible(true);
        //图层符号随地图缩放
//        _temp.setSymbolScalable(true);

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
//            _resultLayer.setSymbolScalable(true);
            _resultLayer.setVisible(true);
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
//            _resultLayer.setSymbolScalable(true);
            _resultLayer.setVisible(true);
            map.refresh();
        }

        if (info instanceof BaseFieldLInfos) {
            LayerSettingVector _vector = new LayerSettingVector();
            GeoStyle geoStyle_P = new GeoStyle();
//            geoStyle_P.setMarkerSize(new Size2D(2.5, 2.5));
            geoStyle_P.setLineSymbolID(0);
            geoStyle_P.setLineWidth(0.1);
            geoStyle_P.setLineColor(new Color(255, 0, 0));
            _vector.setStyle(geoStyle_P);
            _temp.setAdditionalSetting(_vector);
            BaseFieldLInfos _temp_info = (BaseFieldLInfos) info;
            ThemeLabel _theme = _temp_info.createThemeLabel();
            //标签专题图
            Layer _resultLayer = map.getLayers().add(dv, _theme, true);
            if (_resultLayer == null) {
                LogUtills.w("Add ThemeLable Layer Faild...");
            }
//            _resultLayer.setSymbolScalable(true);
            _resultLayer.setVisible(true);
            //单值专题图
            ThemeUnique _uniqueTheme = _temp_info.createDefaultThemeUnique();
//            _resultLayer.setSymbolScalable(true);
            _resultLayer = map.getLayers().add(dv, _uniqueTheme, true);
            Selection _selection = _resultLayer.getSelection();
            GeoStyle _style = new GeoStyle();
            _style.setLineColor(new Color(255, 0, 0));
            _style.setLineWidth(0.5);
            _style.setLineSymbolID(0);
            _selection.setStyle(_style);
            _resultLayer.setVisible(true);
        }
    }


    public static void AddAndSetLayerType(List<DatasetVector> dvs, List<BaseFieldInfos> infos) {
    }


    public static java.util.Map<java.lang.String, java.lang.Object> getFieldMaps(BaseFieldInfos info) {
        try {
            java.util.Map<java.lang.String, java.lang.Object> _values = new HashMap<String, Object>();
            Field[] _field = info.getClass().getFields();
            // 遍历所有属性
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
