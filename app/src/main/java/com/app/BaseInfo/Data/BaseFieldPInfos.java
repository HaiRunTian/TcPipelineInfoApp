package com.app.BaseInfo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.BaseInfo.Data.Point.TheTotalPoint;
import com.app.BaseInfo.Interface.IBaseInf;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.FieldInfos;
import com.supermap.data.FillGradientMode;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Recordset;
import com.supermap.data.Size2D;
import com.supermap.data.TextStyle;
import com.supermap.mapping.OverLengthLabelMode;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeLabelItem;
import com.supermap.mapping.ThemeUnique;
import com.supermap.mapping.ThemeUniqueItem;

import java.lang.reflect.Field;

/**
 * Created by Los on 2018-06-14 22:22.
 * 点
 */

//为满足数据库表设计，该类的成员变量不满足代码规范
public class BaseFieldPInfos extends BaseFieldInfos implements Parcelable, IBaseInf {
    /**
     *  物探点号
     */
    public String exp_Num = "";
    /**
     * 管偏
     */
    public String pipeOffset = "";
//    public String pipeType = "";
    /**
     *  点特征
     */
    public String feature = "";
    /**
     * 附属物
     */
    public String subsid = "";
    /**
     * 窨井规格
     */
    public String wellSize = "";
    /**
     * 窨井深度 cm
     */
    public String wellDeep = "";
    /**
     * 窨井水深 cm
     */
    public String wellWater = "";
    /**
     *  窨井淤泥 cm
     */
    public String wellMud = "";
    /**
     * 道路名称
     */
    public String road = "";
    /**
     *  状态
     */
    public String state = "";
    /**
     *  调查日期
     */
    public String exp_Date = "";
    /**
     * 井盖材质
     */
    public String wellCoverMaterial = "";
    /**
     * 井盖规格 井盖尺寸
     */
    public String wellCoverSize = "";
    /**
     *  建构筑物
     */
    public String buildingStructures = "";
    /**
     * 经度
     */
    public double longitude = 0.0;
    /**
     *  纬度
     */
    public double latitude = 0.0;
    /**
     * H高程
     */
    public String surf_H = "";
    /**
     *  物探组长
     */
    public String expGroup = "";
    /**
     * 管点备注
     */
    public String remark = "";
    /**
     * 照片
     */
    public String picture = "";
    /**
     *  自动编号
     */
    public String id = "";
    /**
     * 疑难问题
     */
    public String puzzle = "";
    /**
     * 状况
     */
    public String situation = "";
    /**
     *  起点方向埋深
     */
    public String startDirDepth = "";
    /**
     * 终点方向埋深
     */
    public String endDirDepth = "";
    /**
     * 深度
     */
    public String depth = "";
    /**
     * 符号专题图 作用点专题图表达式
     */
    public String symbol = "";
    /**
     * 符号专题图 作用点专题图表达式,新定制
     */
    public String symbolExpression = "";
    /**
     *  给选中的样式使用
     */
    public int symbolID = 0;
    /**
     * 给选中的样式使用
     */
    public double symbolSizeX = 0.0;
    /**
     * //给选中的样式使用
     */
    public double symbolSizeY = 0.0;
    /**
     * 流水号
     */
    public int serialNum = 1;
//    public String    rainGrateType      ="";     //雨篦类型
//    public String    rainGrateSize      ="";     //雨篦大小
//    public String    pipeNetType        ="";     //管网类型
//    public String    pump               ="";     //泵站类型
//    public String    sewagePoolType     ="";     //污水池类型
//    public String    pipeForm           ="";     //井室形状
//    public String    pipeSize           ="";     //井口尺寸


    public BaseFieldPInfos() {
    }

    public BaseFieldPInfos(String name) {
        datasetName = name;
    }

    @Override
    public boolean Init() {
        code = datasetName.substring(datasetName.length() - 1);
        datasetName = "P_" + datasetName;
        pipeType = datasetName;
        return true;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
     /*   parcel.writeString(submitName);
        parcel.writeString(datasetName);
        parcel.writeInt(sysId);
        parcel.writeString(code);
        parcel.writeString(shortCode);*/

        parcel.writeString(exp_Num);
        parcel.writeString(pipeType);
        parcel.writeString(feature);
        parcel.writeString(subsid);
        parcel.writeString(pipeOffset);
        parcel.writeString(wellSize);
        parcel.writeString(wellDeep);
        parcel.writeString(wellWater);
        parcel.writeString(wellMud);
        parcel.writeString(road);
        parcel.writeString(state);
        parcel.writeString(exp_Date);
        parcel.writeString(wellCoverMaterial);
        parcel.writeString(wellCoverSize);
        parcel.writeString(buildingStructures);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeString(surf_H);
        parcel.writeString(expGroup);
        parcel.writeString(remark);
        parcel.writeString(picture);
        parcel.writeString(id);
        parcel.writeString(puzzle);
        parcel.writeString(situation);
        parcel.writeString(startDirDepth);
        parcel.writeString(endDirDepth);
        parcel.writeString(depth);
        parcel.writeString(symbol);
        parcel.writeString(symbolExpression);
        parcel.writeInt(symbolID);
        parcel.writeDouble(symbolSizeX);
        parcel.writeDouble(symbolSizeY);
        parcel.writeString(code);
        parcel.writeString(submitName);
        parcel.writeString(datasetName);
        parcel.writeInt(sysId);
        parcel.writeString(shortCode);
        parcel.writeDouble(rangeExpression);
        parcel.writeInt(serialNum);


    }

    /**
     * 必须实现这个接口，它的作用是从 percel中读出来数据，顺序必须按照声明顺序
     */
    public static final Parcelable.Creator<BaseFieldPInfos> CREATOR = new Creator<BaseFieldPInfos>() {
        @Override
        public BaseFieldPInfos createFromParcel(Parcel source) {
            BaseFieldPInfos _field = new BaseFieldPInfos();


            _field.exp_Num = source.readString();
            _field.pipeType = source.readString();
            _field.feature = source.readString();
            _field.subsid = source.readString();
            _field.pipeOffset = source.readString();
            _field.wellSize = source.readString();
            _field.wellDeep = source.readString();
            _field.wellWater = source.readString();
            _field.wellMud = source.readString();
            _field.road = source.readString();
            _field.state = source.readString();
            _field.exp_Date = source.readString();
            _field.wellCoverMaterial = source.readString();
            _field.wellCoverSize = source.readString();
            _field.buildingStructures = source.readString();
            _field.longitude = source.readDouble();
            _field.latitude = source.readDouble();
            _field.surf_H = source.readString();
            _field.expGroup = source.readString();
            _field.remark = source.readString();
            _field.picture = source.readString();
            _field.id = source.readString();
            _field.puzzle = source.readString();
            _field.situation = source.readString();
            _field.startDirDepth = source.readString();
            _field.endDirDepth = source.readString();
            _field.depth = source.readString();
            _field.symbol = source.readString();
            _field.symbolExpression = source.readString();
            _field.symbolID = source.readInt();
            _field.symbolSizeX = source.readDouble();
            _field.symbolSizeY = source.readDouble();
            _field.code = source.readString();
            _field.submitName = source.readString();
            _field.datasetName = source.readString();
            _field.sysId = source.readInt();
            _field.shortCode = source.readString();
            _field.rangeExpression = source.readDouble();
            _field.serialNum = source.readInt();
            //终点方向埋深      = source.readString();
            return _field;
        }

        @Override
        public BaseFieldPInfos[] newArray(int size) {
            return new BaseFieldPInfos[size];
        }

    };

    private Color ColorByOxString(String value) {
        value = value.substring(1);
        int colorInt = Integer.valueOf(value, 16);
        return new Color(colorInt);
       /* LogUtills.i("r = "+color.getR()+", g = "+color.getG()+", b = "+color.getB());*/
    }


    /**
     *   子类调用，单值符号专题图
     */
    protected ThemeUnique createThemeUnique(String[] keys, int[] _sids, String color) {

        ThemeUnique _theme = new ThemeUnique();
        //表达式
        _theme.setUniqueExpression("symbol");
        GeoStyle _defaultStyle = new GeoStyle();
        _defaultStyle.setMarkerSize(new Size2D(5, 5));
        _defaultStyle.setLineColor(new Color(0, 255, 0));
        _defaultStyle.setLineSymbolID(3);
        _defaultStyle.setMarkerSymbolID(408);
        _theme.setDefaultStyle(_defaultStyle);

        //如果附属物是探测点，则要依据附属物作为第二条件赋予管点符号
        for (int i = 0; i < keys.length; ++i) {
            ThemeUniqueItem _item = new ThemeUniqueItem();
            _item.setVisible(true);
            _item.setUnique(keys[i]);
            GeoStyle _style = new GeoStyle();
            //符号大小
            _style.setMarkerSize(new Size2D(5, 5));
            _style.setLineColor(ColorByOxString(color));
            _style.setFillBackColor(new Color(0, 255, 0));
            _style.setFillGradientMode(FillGradientMode.RADIAL);
            _style.setMarkerSymbolID(_sids[i]);
            _item.setStyle(_style);
            _theme.add(_item);
        }
        return _theme;
    }

    /**
     *  子类调用，单值符号专题图
     */
    protected ThemeUnique createThemeUnique(String[] keys, int[] _sids, String color, Size2D[] size2Ds) {

        ThemeUnique _theme = new ThemeUnique();
        //表达式
        _theme.setUniqueExpression("symbol");
        GeoStyle _defaultStyle = new GeoStyle();
        _defaultStyle.setMarkerSize(new Size2D(4, 4));
        _defaultStyle.setLineColor(ColorByOxString(color));
//        _defaultStyle.setFillBackColor(ColorByOxString(color));
//        _defaultStyle.setFillForeColor(ColorByOxString(color));
        _defaultStyle.setLineSymbolID(3);
        _defaultStyle.setMarkerSymbolID(301);
        _theme.setDefaultStyle(_defaultStyle);

        //如果附属物是探测点，则要依据附属物作为第二条件赋予管点符号
        for (int i = 0; i < keys.length; ++i) {
            ThemeUniqueItem _item = new ThemeUniqueItem();
            _item.setVisible(true);
            _item.setUnique(keys[i]);
            GeoStyle _style = new GeoStyle();
            _style.setMarkerSize(size2Ds[i]); //符号大小
            _style.setLineColor(ColorByOxString(color)); //线颜色
//          _style.setFillBackColor(ColorByOxString(color)); //背景色
//          _style.setFillForeColor(ColorByOxString(color)); //前景色
            _style.setFillGradientMode(FillGradientMode.RADIAL);
            _style.setMarkerSymbolID(_sids[i]);
            _style.setLineWidth(0.1);
            _item.setStyle(_style);
            _theme.add(_item);
        }
        return _theme;
    }

    //子类调用，单值符号专题图
    protected ThemeUnique createThemeUnique(String sysbolField, String[] keys, int[] _sids, String[] color, Size2D[] size2Ds) {

        ThemeUnique _theme = new ThemeUnique();
        //表达式
        _theme.setUniqueExpression(sysbolField);
        GeoStyle _defaultStyle = new GeoStyle();
        _defaultStyle.setMarkerSize(new Size2D(4, 4));
        _defaultStyle.setLineColor(ColorByOxString(color[0]));
        _defaultStyle.setLineSymbolID(3);
        _defaultStyle.setMarkerSymbolID(301);
        _theme.setDefaultStyle(_defaultStyle);


        //如果附属物是探测点，则要依据附属物作为第二条件赋予管点符号
        for (int i = 0; i < keys.length; ++i) {
            ThemeUniqueItem _item = new ThemeUniqueItem();
            _item.setVisible(true);
            _item.setUnique(keys[i]);
            GeoStyle _style = new GeoStyle();
            //符号大小
            _style.setMarkerSize(size2Ds[i]);
            //线颜色
            _style.setLineColor(ColorByOxString(color[i]));
            //背景色
            _style.setFillBackColor(ColorByOxString(color[i]));
            //前景色
            _style.setFillForeColor(ColorByOxString(color[i]));
            _style.setFillGradientMode(FillGradientMode.RADIAL);
            _style.setMarkerSymbolID(_sids[i]);
            _style.setLineWidth(0.1);
            _item.setStyle(_style);
            _theme.add(_item);
        }
        return _theme;
    }

    /**
     * 标签专题图
     *
     * @auther HaiRun
     * created at 2018/8/15 10:30
     */
    public ThemeLabel createThemeLabel(String[] pipeType, String[] color, double[] start, double[] end) {
        ThemeLabel themeLabelMap = new ThemeLabel();
        // 设置标注字段表达式。
        themeLabelMap.setLabelExpression("exp_Num");
        // 设置分段字段表达式
        themeLabelMap.setRangeExpression("rangeExpression");
        for (int i = 0; i < pipeType.length; i++) {
            // 为标签专题图的标签设置统一样式
            ThemeLabelItem themeLabelItem1 = new ThemeLabelItem();
            themeLabelItem1.setVisible(true);
            LogUtills.i("Point themelable","name = " + pipeType[i] +"start  = "  +start[i] +  "end = "+ end[i]);
            themeLabelItem1.setStart(start[i]);
            themeLabelItem1.setEnd(end[i]);
            TextStyle textStyle1 = new TextStyle();
            textStyle1.setForeColor(ColorByOxString(color[i]));
            textStyle1.setFontName("楷体");
            textStyle1.setFontHeight(4.0);
            textStyle1.setFontWidth(4.0);
            textStyle1.setBold(false);
            textStyle1.setSizeFixed(true);
            themeLabelItem1.setStyle(textStyle1);
            // 添加标签专题图子项到标签专题图对象中
            themeLabelMap.addToTail(themeLabelItem1);
            themeLabelMap.setOverlapAvoided(false);
            themeLabelMap.setSmallGeometryLabeled(true);
            themeLabelMap.setOverLengthMode(OverLengthLabelMode.NEWLINE);
            themeLabelMap.setFlowEnabled(true);
            themeLabelMap.setOffsetX("12");
            themeLabelMap.setOffsetFixed(true);
//            themeLabelMap.setMaxTextHeight(5);
//            themeLabelMap.setMaxTextWidth(5);
//            themeLabelMap.setMinTextHeight(1);
//            themeLabelMap.setMinTextWidth(1);

        }

        return themeLabelMap;
    }

    @Override
    public GeoStyle GetStyle() {
        return null;
    }

    /**
     * 弃用
     *
     * @param preStr
     * @param index  设置ID
     */
    @Override
    public void setId(String preStr, int index) {
        id = preStr + code + String.format("%02d", SuperMapConfig.User_Group_Index) + String.format("%03d", index);
    }

    /**
     * 标签专题图
     */
    @Override
    public ThemeLabel createThemeLabel() {
        LogUtills.i("begin " + this.getClass().getName() + "createThemeLabel....");
        ThemeLabel themeLabelMap = new ThemeLabel();
        // 设置标注字段表达式。
        themeLabelMap.setLabelExpression("id");
        // 设置分段字段表达式。 显示不同的颜色
        themeLabelMap.setRangeExpression("rangeExpression");

        // 为标签专题图的标签设置统一样式
        ThemeLabelItem themeLabelItem1 = new ThemeLabelItem();
        themeLabelItem1.setVisible(true);
        TextStyle textStyle1 = new TextStyle();
        textStyle1.setForeColor(new Color(255, 10, 10));
        textStyle1.setFontName("楷体");
        textStyle1.setFontHeight(3.0);
        textStyle1.setFontWidth(3.0);
        textStyle1.setSizeFixed(true);
        themeLabelItem1.setStyle(textStyle1);
        // 添加标签专题图子项到标签专题图对象中
        themeLabelMap.addToHead(themeLabelItem1);
        return themeLabelMap;
    }

    /**
     *   标签专题图
     */
    public ThemeLabel createThemeLabel(String color) {
        LogUtills.i("begin " + this.getClass().getName() + "createThemeLabel....");
        ThemeLabel themeLabelMap = new ThemeLabel();
        // 设置标注字段表达式。
        themeLabelMap.setLabelExpression("id");
        // 设置分段字段表达式。 显示不同的颜色
        themeLabelMap.setRangeExpression("rangeExpression");

        // 为标签专题图的标签设置统一样式
        ThemeLabelItem themeLabelItem1 = new ThemeLabelItem();
        themeLabelItem1.setVisible(true);
        TextStyle textStyle1 = new TextStyle();
        textStyle1.setForeColor(new Color(255, 10, 10));
        textStyle1.setFontName("楷体");
        textStyle1.setFontHeight(3.0);
        textStyle1.setFontWidth(3.0);
        textStyle1.setSizeFixed(true);
        themeLabelItem1.setStyle(textStyle1);
        // 添加标签专题图子项到标签专题图对象中
        themeLabelMap.addToHead(themeLabelItem1);
        return themeLabelMap;
    }

    @Override
    public ThemeUnique createThemeUnique() {
        return null;
    }

    @Override
    public ThemeUnique createDefaultThemeUnique() {
        return null;
    }

    public GeoStyle createDefaultSelectStyle() {
        return null;
    }

    /**
     * @param reset 记录集
     * @return BaseFieldPInfo
     * 记录集转为bean对象
     */
    public static BaseFieldPInfos createFieldInfo(Recordset reset) {
        try {
            if (reset.isEmpty()) {
                LogUtills.e("CreateFieldInfo Recordset Is Empty...");
                return null;
            }

            BaseFieldPInfos _info = PointFieldFactory.Create();
            if (_info == null) {
                LogUtills.e("CreateFieldInfo Can Not Find The Layer Of " + reset.getString("datasetName"));
                return null;
            }
            //返回一个包含某些 Field 对象的数组，这些对象反映此 Class 对象所表示的类或接口的所有可访问公共字段。
            Field[] _fields = _info.getClass().getFields();
            //返回记录集里的字段信息集合对象。
            FieldInfos _infos = reset.getFieldInfos();
            for (int i = 0; i < _fields.length; ++i) {
                Field _field = _fields[i];
                //返回此 Field 对象表示的字段的名称。
                String _field_name = _field.getName();
                //不包含此字段
                if (_infos.get(_field_name) == null || _field_name.equals("type")) {
                    //LogUtills.i(" Recodest No Contain The Field "+_field_name+", In BaseFieldPInfos");
                    continue;
                }
                //指定对象变量上此 Field 对象表示的字段设置为指定的新值。
                _field.set(_info, reset.getObject(_field.getName()));
            }
            // 枚举类型重新配置
            _info.type = POINTTYPE.valueOf(reset.getString("type"));
            LogUtills.i("Generator The BaseFieldPInfos Successfully, ID=" + reset.getID());
            return _info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param reset 记录集
     * @return BaseFieldPInfo
     * 记录集转为bean对象
     * s数据导出调用
     */
    public static BaseFieldPInfos createFieldInfo(Recordset reset, int statue) {
        BaseFieldPInfos _info = null;
        try {
            if (reset.isEmpty()) {
                LogUtills.e("CreateFieldInfo Recordset Is Empty...");
                return null;
            }

             _info = PointFieldFactory.Create();
            if (_info == null) {
                LogUtills.e("CreateFieldInfo Can Not Find The Layer Of " + reset.getString("datasetName"));
                return null;
            }
            //返回一个包含某些 Field 对象的数组，这些对象反映此 Class 对象所表示的类或接口的所有可访问公共字段。
            Field[] _fields = _info.getClass().getFields();
            //返回记录集里的字段信息集合对象。
            FieldInfos _infos = reset.getFieldInfos();
            Field _field = null;
            String _field_name = "";
            for (int i = 0; i < _fields.length; ++i) {
                 _field = _fields[i];
                //返回此 Field 对象表示的字段的名称。
                 _field_name = _field.getName();
                //不包含此字段
                if (_infos.get(_field_name) == null || _field_name.equals("type")) {
                    //LogUtills.i(" Recodest No Contain The Field "+_field_name+", In BaseFieldPInfos");
                    continue;
                }
                //指定对象变量上此 Field 对象表示的字段设置为指定的新值。
                _field.set(_info, reset.getObject(_field.getName()));
            }
            // 枚举类型重新配置
            _info.type = POINTTYPE.valueOf(reset.getString("type"));
            LogUtills.i("Generator The BaseFieldPInfos Successfully, ID=" + reset.getID());

            return _info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
          /*  if (reset != null){
                reset.close();
                reset.dispose();
            }*/
        }
    }

    @Override
    public String toString() {
        return "BaseFieldPInfos{" +
                "exp_Num='" + exp_Num + '\'' +
                ", pipeOffset='" + pipeOffset + '\'' +
                ", feature='" + feature + '\'' +
                ", subsid='" + subsid + '\'' +
                ", wellSize='" + wellSize + '\'' +
                ", wellDeep='" + wellDeep + '\'' +
                ", wellWater='" + wellWater + '\'' +
                ", wellMud='" + wellMud + '\'' +
                ", road='" + road + '\'' +
                ", state='" + state + '\'' +
                ", exp_Date='" + exp_Date + '\'' +
                ", wellCoverMaterial='" + wellCoverMaterial + '\'' +
                ", wellCoverSize='" + wellCoverSize + '\'' +
                ", buildingStructures='" + buildingStructures + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", surf_H='" + surf_H + '\'' +
                ", expGroup='" + expGroup + '\'' +
                ", remark='" + remark + '\'' +
                ", picture='" + picture + '\'' +
                ", id='" + id + '\'' +
                ", puzzle='" + puzzle + '\'' +
                ", situation='" + situation + '\'' +
                ", startDirDepth='" + startDirDepth + '\'' +
                ", endDirDepth='" + endDirDepth + '\'' +
                ", depth='" + depth + '\'' +
                ", symbol='" + symbol + '\'' +
                ", symbolExpression='" + symbolExpression + '\'' +
                ", symbolID=" + symbolID +
                ", symbolSizeX=" + symbolSizeX +
                ", symbolSizeY=" + symbolSizeY +
                ", serialNum=" + serialNum +
                '}';
    }
}
