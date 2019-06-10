package com.app.BaseInfo.Data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.app.BaseInfo.Data.Line.TheTotalLine;
import com.app.BaseInfo.Interface.IBaseInf;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.FieldInfos;
import com.supermap.data.FillGradientMode;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.data.Size2D;
import com.supermap.data.TextStyle;
import com.supermap.mapping.AlongLineDirection;
import com.supermap.mapping.OverLengthLabelMode;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeLabelItem;
import com.supermap.mapping.ThemeUnique;
import com.supermap.mapping.ThemeUniqueItem;

import java.lang.reflect.Field;

/**
 * Created by Los on 2018-06-15 13:45.
 * 线
 */

public class BaseFieldLInfos extends BaseFieldInfos implements IBaseInf, Parcelable {

    public BaseFieldLInfos() {
    }

    public BaseFieldLInfos(String name) {
        datasetName = name;
    }

    /**
     * 管线点号
     */
    public String benExpNum;
    /**
     * 连接点号
     */
    public String endExpNum;
    /**
     * 起点经度
     */
    public double startLongitude;
    /**
     * 终点经度
     */
    public double endLongitude;
    /**
     * 起点纬度
     */
    public double startLatitude;

    /**
     * 起点纬度
     */
    public double endLatitude;
    /**
     * 调查日期
     */
    public String exp_Date;
    /**
     * 起点埋深
     */
    public String benDeep;
    /**
     * 终点埋深
     */
    public String endDeep;
    /**
     * 埋设方式
     */
    public String buried;
    /**
     * 管径
     */
    public String pipeSize;
    /**
     * 断面
     */
    public String d_S;
    /**
     * 行X列
     */
    public String rowXCol;
    /**
     * 电缆根数
     */
    public String cabNum;
    /**
     * 权属单位
     */
    public String belong;
    /**
     * 电压
     */
    public String voltage;
    /**
     * 压力
     */
    public String pressure;
    /**
     * 管线材质
     */
    public String material;
    /**
     * 总孔数
     */
    public String totalHole;
    /**
     * 已用孔数
     */
    public String usedHole;
    /**
     * 孔径
     */
    public String holeDiameter;
    /**
     * 状态
     */
    public String state;
    /**
     * 管线备注
     */
    public String remark;
    /**
     * 自动编号
     */
    public String id;
    /**
     * 疑难问题
     */
    public String puzzle;
    /**
     * 标注
     */
    public String labelTag;
    /**
     * 管线长度
     */
    public String pipeLength;
    /**
     * 埋深差值
     */
    public String burialDifference;
    /**
     * 起点是否测量 0未测量 1测量
     */
    public String measureStart;
    /**
     * 终点点是否测量 0未测量 1测量
     */
    public String measureEnd;
    /**
     * 测量的时间
     */
    public String measureDate = "";

//
//    protected BaseFieldLInfos(Parcel in) {
//        pipeType           = in.readString();
//        benExpNum       = in.readString();
//        endExpNum         = in.readString();
//        startLongitude     = in.readDouble();
//        endLongitude       = in.readDouble();
//        startLatitude      = in.readDouble();
//        endLatitude        = in.readDouble();
//        exp_Date         = in.readString();
//        benDeep   = in.readString();
//        endDeep     = in.readString();
//        buried        = in.readString();
//        pipeSize           = in.readString();
//        d_S            = in.readString();
//        rowXCol            = in.readString();
//        cabNum             = in.readString();
//        voltage            = in.readString();
//        pressure           = in.readString();
//        material           = in.readString();
//        belong      = in.readString();
//        totalHole          = in.readString();
//        usedHole      = in.readString();
//        holeDiameter           = in.readString();
//        state              = in.readString();
//        remark             = in.readString();
//        id                 = in.readString();
//        puzzle             = in.readString();
//        labelTag           = in.readString();
//    }


    @Override
    public boolean Init() {
        code = datasetName.substring(datasetName.length() - 1);
        pipeType = datasetName;
        datasetName = "L_" + datasetName;
        return true;
    }

    @Override
    public GeoStyle GetStyle() {
        return null;
    }

    @Override
    public void setId(String preStr, int index) {
    }

    @Override
    public ThemeLabel createThemeLabel() {
        LogUtills.i("begin " + this.getClass().getName() + "createThemeLabel....");
        ThemeLabel themeLabelMap = new ThemeLabel();
        themeLabelMap.setLabelExpression("labelTag");
        themeLabelMap.setRangeExpression("smid");

        // 为标签专题图的标签设置统一样式
        ThemeLabelItem themeLabelItem1 = new ThemeLabelItem();
        themeLabelItem1.setVisible(true);
        TextStyle textStyle1 = new TextStyle();
        /* textStyle1.setForeColor(new Color(193, 210, 240));*/
        textStyle1.setForeColor(new Color(0, 255, 0));
        textStyle1.setFontName("楷体");
        textStyle1.setFontHeight(16.0);
        textStyle1.setFontWidth(16.0);
        textStyle1.setSizeFixed(true);
        themeLabelItem1.setStyle(textStyle1);
        themeLabelMap.setAlongLineSpaceRatio(2.0);
        themeLabelMap.setAlongLine(true);
        themeLabelMap.setAlongLineDirection(AlongLineDirection.ALONG_LINE_NORMAL);
        themeLabelMap.setLabelRepeatInterval(30.0);
        themeLabelMap.setMaxTextHeight(30);
        themeLabelMap.setMaxTextWidth(30);
        themeLabelMap.setMinTextHeight(12);
        themeLabelMap.setMinTextWidth(12);
        themeLabelMap.setLeaderLineDisplayed(true);
        themeLabelMap.addToHead(themeLabelItem1);

        return themeLabelMap;
    }

    public ThemeLabel createThemeLabel(String[] pipeType, String[] color, double[] start, double[] end) {
        LogUtills.i("Create The Theme Layer...");
        ThemeLabel themeLabelMap = new ThemeLabel();
        themeLabelMap.setLabelExpression("labelTag");
        themeLabelMap.setRangeExpression("rangeExpression");
        themeLabelMap.setAlongLine(true);
        themeLabelMap.setFlowEnabled(true);
        themeLabelMap.setOverlapAvoided(false);
        themeLabelMap.setSmallGeometryLabeled(true);
        themeLabelMap.setOverLengthMode(OverLengthLabelMode.NEWLINE);
        for (int i = 0; i < pipeType.length; i++) {
            // 为标签专题图的标签设置统一样式
            ThemeLabelItem themeLabelItem1 = new ThemeLabelItem();
            themeLabelItem1.setVisible(true);
            LogUtills.i("Line themelable", "name = " + pipeType[i] + "start  = " + start[i] + "end = " + end[i]);
            themeLabelItem1.setStart(start[i]);
            themeLabelItem1.setEnd(end[i]);
            TextStyle textStyle1 = new TextStyle();
            textStyle1.setForeColor(ColorByOxString(color[i]));
            textStyle1.setFontName("楷体");
            textStyle1.setFontHeight(6.0);
            textStyle1.setSizeFixed(true);
            themeLabelItem1.setStyle(textStyle1);
            // 添加标签专题图子项到标签专题图对象中
            themeLabelMap.addToTail(themeLabelItem1);

        }

        return themeLabelMap;

    }

    public ThemeLabel createThemeLabel(Color color) {
        LogUtills.i("Create The Theme Layer...");
        ThemeLabel themeLabelMap = new ThemeLabel();
        themeLabelMap.setLabelExpression("labelTag");
        themeLabelMap.setRangeExpression("rangeExpression");
        // 为标签专题图的标签设置统一样式
        ThemeLabelItem themeLabelItem1 = new ThemeLabelItem();
        themeLabelItem1.setVisible(true);
        TextStyle textStyle1 = new TextStyle();
        textStyle1.setForeColor(new Color(193, 210, 240));
        textStyle1.setFontName("楷体");
        textStyle1.setFontHeight(45.0);
        textStyle1.setFontWidth(0.0);
        themeLabelItem1.setStyle(textStyle1);
        themeLabelMap.setAlongLineSpaceRatio(2.0);
        themeLabelMap.setAlongLine(true);
        themeLabelMap.setAlongLineDirection(AlongLineDirection.ALONG_LINE_NORMAL);
        themeLabelMap.setLabelRepeatInterval(200.0);
        themeLabelMap.setMaxTextHeight(60);
        themeLabelMap.setMaxTextWidth(60);
        themeLabelMap.setMinTextHeight(30);
        themeLabelMap.setMinTextWidth(30);
        themeLabelMap.setFlowEnabled(true);
        // 添加标签专题图子项到标签专题图对象中
        themeLabelMap.setLeaderLineDisplayed(true);
        themeLabelMap.addToHead(themeLabelItem1);
        return themeLabelMap;

    }


    private Color ColorByOxString(String value) {
        value = value.substring(1);
        int colorInt = Integer.valueOf(value, 16);
        return new Color(colorInt);
        /* LogUtills.i("r = "+color.getR()+", g = "+color.getG()+", b = "+color.getB());*/
    }

    @Override
    public ThemeUnique createThemeUnique() {
        // 构造单值专题图并进行相应设置
        ThemeUnique _theme = new ThemeUnique();
        _theme.setUniqueExpression("pipeType");
        String[] _objs = new String[]{"给水_J", "临时"};
        int[] _ids = new int[]{0, 1};
        for (int i = 0; i < _objs.length; ++i) {
            ThemeUniqueItem _item = new ThemeUniqueItem();
            _item.setVisible(true);
            _item.setUnique(_objs[i]);
            GeoStyle _style = new GeoStyle();
            _style.setFillBackColor(new Color(0, 255, 0));
            _style.setFillGradientMode(FillGradientMode.RADIAL);
            _style.setLineColor(new Color(255, 0, 0));
            _style.setLineWidth(0.4);
            _style.setLineSymbolID(_ids[i]);
            _item.setStyle(_style);
            _theme.add(_item);
        }

        GeoStyle _defaultStyle = new GeoStyle();
        _defaultStyle.setLineWidth(2);
        _defaultStyle.setLineColor(new Color(0, 255, 0));
        _defaultStyle.setLineSymbolID(0);
        _theme.setDefaultStyle(_defaultStyle);
        return _theme;
    }

    public ThemeUnique createThemeUnique(String color) {
        // 构造单值专题图并进行相应设置
        ThemeUnique _theme = new ThemeUnique();
        _theme.setUniqueExpression("pipeType");
        String[] _objs = new String[]{"给水_J", "临时"};
        int[] _ids = new int[]{0, 1};
        for (int i = 0; i < _objs.length; ++i) {
            ThemeUniqueItem _item = new ThemeUniqueItem();
            _item.setVisible(true);
            _item.setUnique(_objs[i]);
            GeoStyle _style = new GeoStyle();
            _style.setFillBackColor(new Color(0, 255, 0));
            _style.setFillGradientMode(FillGradientMode.RADIAL);

            _style.setLineColor(new Color(255, 0, 0));
            _style.setLineWidth(0.4);
            _style.setLineSymbolID(_ids[i]);
            _item.setStyle(_style);
            _theme.add(_item);
        }

        GeoStyle _defaultStyle = new GeoStyle();
        _defaultStyle.setLineWidth(2);
        _defaultStyle.setLineColor(new Color(0, 255, 0));
        _defaultStyle.setLineSymbolID(0);
        _theme.setDefaultStyle(_defaultStyle);
//        LogUtills.i("add geoline style successfully...");
        return _theme;
    }

    /**
     * @return 默认专题图
     */
    @Override
    public ThemeUnique createDefaultThemeUnique() {
        // 构造单值专题图并进行相应设置
        ThemeUnique _theme = new ThemeUnique();

        String tabName = "";
        //TODO 根据标准名称 查找点配置表
        Cursor _cursorStand = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO, "where name = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
        //查询此标准的点表名
        while (_cursorStand.moveToNext()) {
            tabName = _cursorStand.getString(_cursorStand.getColumnIndex("linesettintable"));
        }

        if (tabName.length() == 0) {
            LogUtills.i("begin " + this.getClass().getName() + "tabName = null ");
        }
        Cursor _cursor = DatabaseHelpler.getInstance().query(tabName,
                new String[]{"typename", "symbolID", "width", "color"}, null, null, null, null, null);

        LogUtills.i("Sql:" + _cursor.toString());
        int _num = _cursor.getCount();
        if (_num == 0) {
            LogUtills.e("Query Line Config Table " + SQLConfig.TABLE_DEFAULT_LINE_SETTING + " Faild...");
            return null;
        }

        String[] _keys = new String[_num];
        int[] _symbolIds = new int[_num];
        double[] _widths = new double[_num];
        String[] _colors = new String[_num];
        int _index = 0;
        while (_cursor.moveToNext()) {
            String _name = _cursor.getString(_cursor.getColumnIndex("typename"));
            int _symbolId = _cursor.getInt(_cursor.getColumnIndex("symbolID"));
            double _width = _cursor.getDouble(_cursor.getColumnIndex("width"));
            String _color = _cursor.getString(_cursor.getColumnIndex("color"));

            _keys[_index] = _name;
            _symbolIds[_index] = _symbolId;
            _widths[_index] = _width;
            _colors[_index] = _color;

            _index++;
            LogUtills.i("Query Line Config Infomation: " + "Name;" + _name + ",Symbolid:" + _symbolId +
                    ",Width:" + _width + ",_color:" + _color);
        }
        _cursorStand.close();
        _cursor.close();

        //这个要改
        _theme.setUniqueExpression("pipeType");
        for (int i = 0; i < _keys.length; ++i) {
            ThemeUniqueItem _item = new ThemeUniqueItem();
            _item.setVisible(true);
            _item.setUnique(_keys[i]);
            GeoStyle _style = new GeoStyle();
            _style.setFillGradientMode(FillGradientMode.RADIAL);
            _style.setLineColor(ComTool.colorByOxString(_colors[i]));
            _style.setLineWidth(_widths[i]);
            _style.setLineSymbolID(_symbolIds[i]);
            _item.setStyle(_style);
            _theme.add(_item);
        }

        GeoStyle _defaultStyle = new GeoStyle();
        _defaultStyle.setLineWidth(0.5);
        _defaultStyle.setLineColor(new Color(0, 255, 0));
        _defaultStyle.setLineSymbolID(0);
        _theme.setDefaultStyle(_defaultStyle);
        LogUtills.i("Set Line Symbol Style Successfully...");
        return _theme;


    }

    public static BaseFieldLInfos createFieldInfo(Recordset reset) {
        try {
            if (reset.isEmpty()) {
                LogUtills.e("CreateFieldInfo Recordset Is Empty");
                return null;
            }
            BaseFieldLInfos _info = null;
            _info = LineFieldFactory.Create();
            if (_info == null) {
                LogUtills.e("CreateFieldInfo Can Not Find The Layer Of " + reset.getString("datasetName"));
                return null;
            }

            Field[] _fields = _info.getClass().getFields();
            FieldInfos _infos = reset.getFieldInfos();

            for (int i = 0; i < _fields.length; ++i) {
                Field _field = _fields[i];
                String _field_name = _field.getName();
                if (_infos.get(_field_name) == null || _field_name.equals("type")) {
                    continue;
                }
                _field.set(_info, reset.getObject(_field.getName()));
            }
            // 枚举类型重新配置
            _info.type = POINTTYPE.valueOf(reset.getString("type"));
            return _info;
        } catch (Exception e) {
            LogUtills.e(e.toString());
            return null;
        }
    }

    /**
     * 数据导出调用
     *
     * @Author HaiRun
     * @Time 2019/3/10 . 10:52
     */
    public static BaseFieldLInfos createFieldInfo(Recordset reset, int status) {
        try {
            if (reset.isEmpty()) {
                return null;
            }
            BaseFieldLInfos _info = null;
            _info = LineFieldFactory.Create();

            if (_info == null) {
                return null;
            }
            Field[] _fields = _info.getClass().getFields();
            FieldInfos _infos = reset.getFieldInfos();
            Field _field = null;
            String _field_name = "";

            for (int i = 0; i < _fields.length; ++i) {
                 _field = _fields[i];
                //字段名
                 _field_name = _field.getName();
                //不包含此字段
                if (_infos.get(_field_name) == null || _field_name.equals("type")) {
                    continue;
                }
                _field.set(_info, reset.getObject(_field.getName()));
            }
            // 枚举类型重新配置
            _info.type = POINTTYPE.valueOf(reset.getString("type"));
            return _info;
        } catch (Exception e) {

            return null;
        }finally {
          /*  if (reset != null){
                reset.close();
                reset.dispose();
            }*/
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(pipeType);
        parcel.writeString(benExpNum);
        parcel.writeString(endExpNum);
        parcel.writeDouble(startLongitude);
        parcel.writeDouble(endLongitude);
        parcel.writeDouble(startLatitude);
        parcel.writeDouble(endLatitude);
        parcel.writeString(exp_Date);
        parcel.writeString(benDeep);
        parcel.writeString(endDeep);
        parcel.writeString(buried);
        parcel.writeString(pipeSize);
        parcel.writeString(d_S);
        parcel.writeString(rowXCol);
        parcel.writeString(cabNum);
        parcel.writeString(voltage);
        parcel.writeString(pressure);
        parcel.writeString(material);
        parcel.writeString(belong);
        parcel.writeString(totalHole);
        parcel.writeString(usedHole);
        parcel.writeString(holeDiameter);
        parcel.writeString(state);
        parcel.writeString(remark);
        parcel.writeString(id);
        parcel.writeString(puzzle);
        parcel.writeString(labelTag);
        parcel.writeString(pipeLength);
        parcel.writeString(burialDifference);
        parcel.writeDouble(rangeExpression);
        parcel.writeString(measureStart);
        parcel.writeString(measureEnd);
        parcel.writeString(measureDate);

    }

    /**
     * 必须实现这个接口，它的作用是从 percel中读出来数据，顺序必须按照声明顺序
     */
    public static final Parcelable.Creator<BaseFieldLInfos> CREATOR = new Creator<BaseFieldLInfos>() {
        @Override
        public BaseFieldLInfos createFromParcel(Parcel source) {
            BaseFieldLInfos _field = new BaseFieldLInfos();
            _field.pipeType = source.readString();
            _field.benExpNum = source.readString();
            _field.endExpNum = source.readString();
            _field.startLongitude = source.readDouble();
            _field.endLongitude = source.readDouble();
            _field.startLatitude = source.readDouble();
            _field.endLatitude = source.readDouble();
            _field.exp_Date = source.readString();
            _field.benDeep = source.readString();
            _field.endDeep = source.readString();
            _field.buried = source.readString();
            _field.pipeSize = source.readString();
            _field.d_S = source.readString();
            _field.rowXCol = source.readString();
            _field.cabNum = source.readString();
            _field.voltage = source.readString();
            _field.pressure = source.readString();
            _field.material = source.readString();
            _field.belong = source.readString();
            _field.totalHole = source.readString();
            _field.usedHole = source.readString();
            _field.holeDiameter = source.readString();
            _field.state = source.readString();
            _field.remark = source.readString();
            _field.id = source.readString();
            _field.puzzle = source.readString();
            _field.labelTag = source.readString();
            _field.pipeLength = source.readString();
            _field.burialDifference = source.readString();
            _field.rangeExpression = source.readDouble();
            _field.measureStart = source.readString();
            _field.measureEnd = source.readString();
            _field.measureDate = source.readString();

            return _field;
        }

        @Override
        public BaseFieldLInfos[] newArray(int size) {
            return new BaseFieldLInfos[size];
        }

    };


}
