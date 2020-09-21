package com.app.BaseInfo.Data.Line;

import android.database.Cursor;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.FillGradientMode;
import com.supermap.data.GeoStyle;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeUnique;
import com.supermap.mapping.ThemeUniqueItem;

/**
 * @author HaiRun
 * @time 2020/2/24.9:15
 * 排水检测类
 */
public class PsCheckLine extends BaseFieldLInfos {
    public String videoNumber;
    public String flow;
    public String wellNumber;
    public String wellState;
    public String flowState;
    public String defectLength;
    public String defectCode;
    public String defectGrade;
    public String checkMan;
    public String checkWay;
    public String checkLocal;
    public String roadName;

    public PsCheckLine(){
        type = POINTTYPE.Type_PS;
        datasetName = SuperMapConfig.Layer_PS;
        Init();
    }

    public PsCheckLine(String name){
        type = POINTTYPE.Type_PS;
        datasetName = name;
        Init();
    }

    /**
     * 标准专题图
     * @return
     */
    @Override
    public ThemeLabel createThemeLabel() {
        super.createThemeLabel();
        try {
            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_THEME,"where city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'" );
            int _num = _cursor.getCount();
            String[] _keys = new String[_num];
            String[] _colors = new String[_num];
            double[] _starts = new double[_num];
            double[] _ends = new double[_num];
            LogUtills.i("Query Cursor: " + _num);
            int _index = 0;
            while (_cursor.moveToNext()) {
                String _name = _cursor.getString(_cursor.getColumnIndex("pipetype"));
                String _color = _cursor.getString(_cursor.getColumnIndex("color"));
                double _start = _cursor.getDouble(_cursor.getColumnIndex("start"));
                double _end = _cursor.getDouble(_cursor.getColumnIndex("end"));
                _keys[_index] = _name;
                _colors[_index] = _color;
                _starts[_index] = _start;
                _ends[_index] = _end;
                _index++;
            }
            _cursor.close();
            return createThemeLabel(_keys,_colors,_starts,_ends);
        } catch (Exception e) {
            LogUtills.e(e.toString());
        }
        return null;
    }

    @Override
    public ThemeUnique createDefaultThemeUnique() {
        // 构造单值专题图并进行相应设置
        ThemeUnique _theme = new ThemeUnique();

        String[] _keys = new String[2];
        _keys[0] = "顺";
        _keys[1] = "逆";
        int[] _symbolIds = new int[2];
        _symbolIds[0] = 1;
        _symbolIds[1] = 3;
        double[] _widths = new double[2];
        _widths[0] = 1.5;
        _widths[1] = 1.5;
        String[] _colors = new String[2];
        _colors[0] = "#008000";
        _colors[1] = "#0000CD";
        //这个要改
        _theme.setUniqueExpression("flow");
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
        _defaultStyle.setLineWidth(1);
        _defaultStyle.setLineColor(new Color(0, 255, 0));
        _defaultStyle.setLineSymbolID(0);
        _theme.setDefaultStyle(_defaultStyle);
        return _theme;
    }

    @Override
    public String toString() {
        return "PsCheckLine{" +
                "videoNumber='" + videoNumber + '\'' +
                ", flow='" + flow + '\'' +
                ", wellNumber='" + wellNumber + '\'' +
                ", wellState='" + wellState + '\'' +
                ", flowState='" + flowState + '\'' +
                ", defectLength='" + defectLength + '\'' +
                ", defectCode='" + defectCode + '\'' +
                ", defectGrade='" + defectGrade + '\'' +
                ", roadName='" + roadName + '\'' +
                ", checkMan='" + checkMan + '\'' +
                ", checkLocal='" + checkLocal + '\'' +
                ", checkWay='" + checkWay + '\'' +
                ", remark='" + remark + '\'' +
                ", belong='" + belong + '\'' +
                ", benDeep='" + benDeep + '\'' +
                ", benExpNum='" + benExpNum + '\'' +
                ", burialDifference='" + burialDifference + '\'' +
                ", buried='" + buried + '\'' +
                ", cabNum='" + cabNum + '\'' +
                ", d_S='" + d_S + '\'' +
                ", endDeep='" + endDeep + '\'' +
                ", endExpNum='" + endExpNum + '\'' +
                ", endLatitude=" + endLatitude +
                ", endLongitude=" + endLongitude +
                ", exp_Date='" + exp_Date + '\'' +
                ", holeDiameter='" + holeDiameter + '\'' +
                ", id='" + id + '\'' +
                ", labelTag='" + labelTag + '\'' +
                ", material='" + material + '\'' +
                ", measureDate='" + measureDate + '\'' +
                ", measureStart='" + measureStart + '\'' +
                ", measureEnd='" + measureEnd + '\'' +
                ", pipeLength='" + pipeLength + '\'' +
                ", pipeSize='" + pipeSize + '\'' +
                ", pressure='" + pressure + '\'' +
                ", puzzle='" + puzzle + '\'' +
                ", remark='" + remark + '\'' +
                ", rowXCol='" + rowXCol + '\'' +
                ", startLatitude=" + startLatitude +
                ", startLongitude=" + startLongitude +
                ", state='" + state + '\'' +
                ", totalHole='" + totalHole + '\'' +
                ", usedHole='" + usedHole + '\'' +
                ", voltage='" + voltage + '\'' +
                ", PsCheQiBenX=" + PsCheQiBenX +
                ", PsCheQiBenY=" + PsCheQiBenY +
                ", PsCheQiEndX=" + PsCheQiEndX +
                ", PsCheQiEndY=" + PsCheQiEndY +
                ", PsCheQiBenLe='" + PsCheQiBenLe + '\'' +
                ", PsCheQiEndLe='" + PsCheQiEndLe + '\'' +
                ", submitName='" + submitName + '\'' +
                ", datasetName='" + datasetName + '\'' +
                ", type=" + type +
                ", sysId=" + sysId +
                ", code='" + code + '\'' +
                ", shortCode='" + shortCode + '\'' +
                ", rangeExpression=" + rangeExpression +
                ", pipeType='" + pipeType + '\'' +
                ", Edit='" + Edit + '\'' +
                '}';
    }
}
