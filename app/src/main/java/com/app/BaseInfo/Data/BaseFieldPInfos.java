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


public class BaseFieldPInfos extends BaseFieldInfos implements Parcelable, IBaseInf {
    /**
     * 建构筑物
     */
    public String buildingStructures = "";
    /**
     * 深度
     */
    public String depth = "";
    /**
     * 终点方向埋深
     */
    public String endDirDepth = "";
    /**
     * 物探组长
     */
    public String expGroup = "";
    /**
     * 调查日期
     */
    public String exp_Date = "";
    /**
     * 物探点号
     */
    public String exp_Num = "";
    /**
     * 点特征
     */
    public String feature = "";
    /**
     * 自动编号
     */
    public String id = "";
    /**
     * 纬度
     */
    public double latitude = 0.0;
    /**
     * 经度
     */
    public double longitude = 0.0;
    /**
     * 照片
     */
    public String picture = "";
    /**
     * 管偏
     */
    public String pipeOffset = "";
    /**
     * 疑难问题
     */
    public String puzzle = "";
    /**
     * 管点备注
     */
    public String remark = "";
    /**
     * 道路名称
     */
    public String road = "";
    /**
     * 流水号
     */
    public int serialNum = 1;
    /**
     * 状况
     */
    public String situation = "";
    /**
     * 起点方向埋深
     */
    public String startDirDepth = "";
    /**
     * 状态
     */
    public String state = "";
    /**
     * 附属物
     */
    public String subsid = "";
    /**
     * H高程
     */
    public String surf_H = "";
    /**
     * 符号专题图 作用点专题图表达式
     */
    public String symbol = "";
    /**
     * 符号专题图 作用点专题图表达式,新定制
     */
    public String symbolExpression = "";
    /**
     * 给选中的样式使用
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
     * 井盖材质
     */
    public String wellCoverMaterial = "";
    /**
     * 井盖规格 井盖尺寸
     */
    public String wellCoverSize = "";
    /**
     * 窨井深度 cm
     */
    public String wellDeep = "";
    /**
     * 窨井淤泥 cm
     */
    public String wellMud = "";
    /**
     * 窨井规格
     */
    public String wellSize = "";
    /**
     * 窨井水深 cm
     */
    public String wellWater = "";
    //排水扯旗
    public double ExpX = 0.000;
    public double ExpY = 0.000;
    public double MapX = 0.000;
    public double MapY = 0.000;
    public String Explain1 = "";
    public double PsCheQiX = 0.000;
    public double PsCheQiY = 0.000;
    public String PsCheQiLeftR = "";
    public double ExpCheQiY = 0.000;
    public double ExpCheQiX = 0.000;
    public double ExplainX = 0.000;
    public double ExplainY = 0.000;
    public double ExplaninAng = 0.0;
    public double PpdAng = 0.0;
    public double PpdX = 0.0;
    public double PpdY = 0.0;
    public double DotleadAng = 0.0;
    //标记测量点号 0 = 未测量  1：已测量
    public String MeasuerPoint = "0";
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
        parcel.writeString(buildingStructures);
        parcel.writeString(depth);
        parcel.writeString(endDirDepth);
        parcel.writeString(expGroup);
        parcel.writeString(exp_Date);
        parcel.writeString(exp_Num);
        parcel.writeString(feature);
        parcel.writeString(id);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(picture);
        parcel.writeString(pipeOffset);
        parcel.writeString(puzzle);
        parcel.writeString(remark);
        parcel.writeString(road);
        parcel.writeInt(serialNum);
        parcel.writeString(startDirDepth);
        parcel.writeString(state);
        parcel.writeString(subsid);
        parcel.writeString(surf_H);
        parcel.writeString(symbol);
        parcel.writeString(symbolExpression);
        parcel.writeInt(symbolID);
        parcel.writeDouble(symbolSizeX);
        parcel.writeDouble(symbolSizeY);
        parcel.writeString(wellCoverMaterial);
        parcel.writeString(wellCoverSize);
        parcel.writeString(wellDeep);
        parcel.writeString(wellMud);
        parcel.writeString(wellSize);
        parcel.writeString(wellWater);
        parcel.writeString(code);
        parcel.writeString(datasetName);
        parcel.writeString(pipeType);
        parcel.writeDouble(rangeExpression);
        parcel.writeString(shortCode);
        parcel.writeString(submitName);
        parcel.writeInt(sysId);
        parcel.writeDouble(ExpX);
        parcel.writeDouble(ExpY);
        parcel.writeDouble(MapX);
        parcel.writeDouble(MapY);
        parcel.writeString(Explain1);
        parcel.writeDouble(PsCheQiX);
        parcel.writeDouble(PsCheQiY);
        parcel.writeString(PsCheQiLeftR);
        parcel.writeDouble(ExpCheQiY);
        parcel.writeDouble(ExpCheQiX);
        parcel.writeDouble(ExplainX);
        parcel.writeDouble(ExplainY);
        parcel.writeDouble(ExplaninAng);
        parcel.writeDouble(PpdAng);
        parcel.writeDouble(PpdX);
        parcel.writeDouble(PpdY);
        parcel.writeDouble(DotleadAng);
        parcel.writeString(MeasuerPoint);
        parcel.writeString(Edit);
    }

    /**
     * 必须实现这个接口，它的作用是从 percel中读出来数据，顺序必须按照声明顺序
     */
    public static final Parcelable.Creator<BaseFieldPInfos> CREATOR = new Creator<BaseFieldPInfos>() {
        @Override
        public BaseFieldPInfos createFromParcel(Parcel source) {
            BaseFieldPInfos _field = new BaseFieldPInfos();
            _field.buildingStructures = source.readString();
            _field.depth = source.readString();
            _field.endDirDepth = source.readString();
            _field.expGroup = source.readString();
            _field.exp_Date = source.readString();
            _field.exp_Num = source.readString();
            _field.feature = source.readString();
            _field.id = source.readString();
            _field.latitude = source.readDouble();
            _field.longitude = source.readDouble();
            _field.picture = source.readString();
            _field.pipeOffset = source.readString();
            _field.puzzle = source.readString();
            _field.remark = source.readString();
            _field.road = source.readString();
            _field.serialNum = source.readInt();
            _field.startDirDepth = source.readString();
            _field.state = source.readString();
            _field.subsid = source.readString();
            _field.surf_H = source.readString();
            _field.symbol = source.readString();
            _field.symbolExpression = source.readString();
            _field.symbolID = source.readInt();
            _field.symbolSizeX = source.readDouble();
            _field.symbolSizeY = source.readDouble();
            _field.wellCoverMaterial = source.readString();
            _field.wellCoverSize = source.readString();
            _field.wellDeep = source.readString();
            _field.wellMud = source.readString();
            _field.wellSize = source.readString();
            _field.wellWater = source.readString();
            _field.code = source.readString();
            _field.datasetName = source.readString();
            _field.pipeType = source.readString();
            _field.datasetName = source.readString();
            _field.rangeExpression = source.readInt();
            _field.shortCode = source.readString();
            _field.submitName = source.readString();
            _field.sysId = source.readInt();
            _field.ExpX = source.readDouble();
            _field.ExpY = source.readDouble();
            _field.MapX = source.readDouble();
            _field.MapY = source.readDouble();
            _field.Explain1 = source.readString();
            _field.PsCheQiX = source.readDouble();
            _field.PsCheQiY = source.readDouble();
            _field.PsCheQiLeftR = source.readString();
            _field.ExpCheQiY = source.readDouble();
            _field.ExpCheQiX = source.readDouble();
            _field.ExplainX = source.readDouble();
            _field.ExplainY = source.readDouble();
            _field.ExplaninAng = source.readDouble();
            _field.PpdAng = source.readDouble();
            _field.PpdX = source.readDouble();
            _field.PpdY = source.readDouble();
            _field.DotleadAng = source.readDouble();
            _field.MeasuerPoint = source.readString();
            _field.Edit = source.readString();

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
    }

    /**
     * 子类调用，单值符号专题图
     * @Params :
     * @author :HaiRun
     * @date :2019/6/20  16:08
     */
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
            // 设置点的颜色
            _style.setPointColor(ColorByOxString(color[i]));
            //设置当前填充背景是否不透明。
            _style.setFillBackOpaque(false);
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
            LogUtills.i("Point themelable", "name = " + pipeType[i] + "start  = " + start[i] + "end = " + end[i]);
            themeLabelItem1.setStart(start[i]);
            themeLabelItem1.setEnd(end[i]);
            TextStyle textStyle1 = new TextStyle();
            textStyle1.setForeColor(ColorByOxString(color[i]));
            textStyle1.setFontName("楷体");
            textStyle1.setFontHeight(3.5);
            textStyle1.setSizeFixed(true);
            themeLabelItem1.setStyle(textStyle1);
            // 添加标签专题图子项到标签专题图对象中
            themeLabelMap.addToTail(themeLabelItem1);
            themeLabelMap.setOverlapAvoided(true);
            themeLabelMap.setSmallGeometryLabeled(true);
            themeLabelMap.setOverLengthMode(OverLengthLabelMode.NEWLINE);
            //设置是否流动显示标签。
            themeLabelMap.setFlowEnabled(true);
            //设置当前标签专题图是否固定标记文本的偏移量。
            themeLabelMap.setOffsetFixed(true);
            //参数为True表示以全方向文本避让,否则以四方向文本避让
            themeLabelMap.setAllDirectionsOverlappedAvoided(true);
            themeLabelMap.setOffsetX("15");
            themeLabelMap.setOffsetY("15");
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
        return null;
    }

    /**
     * 标签专题图
     * 测量收点 调用
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
        textStyle1.setFontHeight(3.5);
        textStyle1.setFontWidth(3.5);
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
            reset.moveFirst();
            FieldInfos _infos = reset.getFieldInfos();
            for (int i = 0; i < _fields.length; ++i) {
                Field _field = _fields[i];
                //返回此 Field 对象表示的字段的名称。
                String _field_name = _field.getName();
                //不包含此字段
                if (_infos.get(_field_name) == null || _field_name.equals("type")) {
                    continue;
                }
                //指定对象变量上此 Field 对象表示的字段设置为指定的新值。
                if (reset.getObject(_field.getName()) == null) {
                    continue;
                }
                //指定对象变量上此 Field 对象表示的字段设置为指定的新值。
                _field.set(_info, reset.getObject(_field.getName()));
            }
            // 枚举类型重新配置
            if (reset.getString("type").length() != 0) {
                _info.type = POINTTYPE.valueOf(reset.getString("type"));
            }else {
                _info.type = POINTTYPE.Type_None;
            }
            LogUtills.i("Generator The BaseFieldPInfos Successfully, ID=" + reset.getID());
            return _info;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtills.i("Generator The BaseFieldPInfos Fail" + e.toString());
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
                if (reset.getObject(_field.getName()) == null){
                    continue;
                }
                _field.set(_info, reset.getObject(_field.getName()));
            }
            // 枚举类型重新配置
            if (reset.getString("type").length() != 0) {
                _info.type = POINTTYPE.valueOf(reset.getString("type"));
            }else {
                _info.type = POINTTYPE.Type_None;
            }
            LogUtills.i("Generator The BaseFieldPInfos Successfully, ID=" + reset.getID());

            return _info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
