package com.app.BaseInfo.Oper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TimeUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.Line.TheTotalLine;
import com.app.BaseInfo.Data.MAPACTIONTYPE2;
import com.app.BaseInfo.Data.PointFieldFactory;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.PtToLrLUtils;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.activity.DrawPipeLineActivity;
import com.app.pipelinesurvey.view.activity.MapActivity;
import com.app.pipelinesurvey.view.activity.QueryPipePointActivity;
import com.app.pipelinesurvey.view.fragment.map.DrawLineFragment;
import com.app.pipelinesurvey.view.fragment.map.DrawPointFragment;
import com.app.pipelinesurvey.view.fragment.map.DrawPointInLineFragment;
import com.app.pipelinesurvey.view.fragment.map.MeasuredPointFragment;
import com.app.pipelinesurvey.view.fragment.map.QueryLineFragment;
import com.app.pipelinesurvey.view.fragment.map.QueryPointFragment;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.FieldInfos;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Size2D;
import com.supermap.data.SpatialQueryMode;
import com.supermap.data.Workspace;
import com.supermap.mapping.Action;
import com.supermap.mapping.Layer;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.Selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 地图核心操作类
 * Created by Los on 2018-06-20 10:57.
 */
public class DataHandlerObserver {
    private static DataHandlerObserver s_ins = null;
    private MapControl m_mapCtrl = null;
    private Workspace m_workSpace = null;
    private MAPACTIONTYPE2 m_actionType2 = MAPACTIONTYPE2.Action_None;
    /**
     * 当前编辑的图层名字
     */
    private String m_currentLayerName = SuperMapConfig.Layer_WaterSupply;
    /**
     * 当前选中点的SmID,目前两个地方用到，画线，移动管点，Los 2019.3.18
     */
    private List<Integer> m_SmIDs = new ArrayList();
    /**
     * 当前选中线的SmID,主要是记录线重新选择起点或者终点，Tian 2019.3.21
     */
    private List<Integer> m_SmIDsL = new ArrayList<Integer>();
    private Integer m_addPtOfLineSmID = -1;
    private FragmentActivity m_context = null;
    /**
     * 查询管点，移动管点的smid
     */
    public int s_PointSmid = 0;
    double _startX = 0.0;
    double _startY = 0.0;
    double _endX = 0.0;
    double _endY = 0.0;
    private long lastTime = 0L;


    private DataHandlerObserver() {
        m_workSpace = WorkSpaceUtils.getInstance().getWorkspace();
        m_mapCtrl = WorkSpaceUtils.getInstance().getMapControl();
        m_mapCtrl.setOnTouchListener(new NewGeometryOnTouchListner());
    }

    public void setMapActionType(MAPACTIONTYPE2 type) {
        getPtThemeLayerByName2(). getSelection().clear();
        getTotalLrThemeLayer().getSelection().clear();
        m_mapCtrl.getMap().refresh();
        m_SmIDs.clear();
        m_addPtOfLineSmID = -1;
        m_actionType2 = type;
    }

    public void SetContext(FragmentActivity context) {
        m_context = context;
    }

    public void AddPointSmID(int id) {
        m_SmIDs.add(id);
    }

    /**
     * 重置
     */
    public void resetState() {
        setMapActionType(MAPACTIONTYPE2.Action_None);
    }

    public synchronized static DataHandlerObserver ins() {
        if (s_ins == null) {
            s_ins = new DataHandlerObserver();
        }
        return s_ins;
    }

    public static synchronized DataHandlerObserver ins(String type) {
        s_ins = new DataHandlerObserver();
        return s_ins;
    }

    public void setCurrentPipeType(String type) {
        m_currentLayerName = type;
//        switch (type) {
//            case "排水-P": {
//                m_currentLayerName = SuperMapConfig.Layer_Drainage;
//            }
//            break;
//            case "雨水-Y": {
//                m_currentLayerName = SuperMapConfig.Layer_Rain;
//            }
//            break;
//            case "污水-W": {
//                m_currentLayerName = SuperMapConfig.Layer_Sewage;
//            }
//            break;
//            case "给水-J": {
//                m_currentLayerName = SuperMapConfig.Layer_WaterSupply;
//            }
//            break;
//            case "煤气-M": {
//                m_currentLayerName = SuperMapConfig.Layer_CoalGas;
//            }
//            break;
//            case "燃气-R": {
//                m_currentLayerName = SuperMapConfig.Layer_Gas;
//            }
//            break;
//            case "电力-L": {
//                m_currentLayerName = SuperMapConfig.Layer_ElectricPower;
//            }
//            break;
//            case "路灯-S": {
//                m_currentLayerName = SuperMapConfig.Layer_StreetLamp;
//            }
//            break;
//            case "电信-D": {
//                m_currentLayerName = SuperMapConfig.Layer_Telecom;
//            }
//            break;
//            case "有视-T": {
//                m_currentLayerName = SuperMapConfig.Layer_CableTV;
//            }
//            break;
//            case "军队-B": {
//                m_currentLayerName = SuperMapConfig.Layer_Army;
//            }
//            break;
//            case "交通-X": {
//                m_currentLayerName = SuperMapConfig.Layer_Traffic;
//            }
//            break;
//            case "工业-G": {
//                m_currentLayerName = SuperMapConfig.Layer_Industry;
//            }
//            break;
//            case "不明-N": {
//                m_currentLayerName = SuperMapConfig.Layer_Unknown;
//            }
//            break;
//            case "其它-Q": {
//                m_currentLayerName = SuperMapConfig.Layer_Other;
//            }
//            break;
//            default:
//                break;
//        }
//        LogUtills.i("set the current layer type: " + m_currentLayerName);
    }

    public class NewGeometryOnTouchListner implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            try {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        _startX = motionEvent.getX();
                        _startY = motionEvent.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        _endX = motionEvent.getX();
                        _endY = motionEvent.getY();
                        if (Math.abs(_endY - _startY) < 5 && Math.abs(_endX - _startX) < 5) {
                            switch (m_actionType2) {

                                //添加与查询同个APP动作
                                case Action_CreatePoint: {
                                    if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                                        return false;
                                    }
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
//                                    if (_result != null || !_result.isEmpty()) {
//
//                                    }

                                    //创建管点
                                    if (_result == null || _result.isEmpty()) {
                                        //如果点击屏幕过快，禁止跳出重复窗体 0.8秒
                                        if (DateTimeUtil.isFastDoubleClick(lastTime)) {
                                            return false;
                                        }
                                        lastTime = System.currentTimeMillis();
                                        DrawPointFragment _fragment = new DrawPointFragment();
                                        Point _pt = new Point();
                                        _pt.setX((int) motionEvent.getX());
                                        _pt.setY((int) motionEvent.getY());
                                        Point2D _reP = m_mapCtrl.getMap().pixelToMap(_pt);
                                        Bundle _bundle = new Bundle();
                                        _bundle.putString("gpType", m_currentLayerName);
                                        _bundle.putDouble("x", _reP.getX());
                                        _bundle.putDouble("y", _reP.getY());
                                        _bundle.putInt("smId", -1);
                                        _fragment.setArguments(_bundle);
                                        _fragment.show(m_context.getSupportFragmentManager().beginTransaction(), "point");

                          /*  Point _pt = new Point();
                            _pt.setX((int) motionEvent.getX());
                            _pt.setY((int) motionEvent.getY());
                            Point2D _reP = m_mapCtrl.getMap().pixelToMap(_pt);
                            m_context.startActivity(new Intent(m_context, DrawPipePointActivity.class).
                                    putExtra("gpType", m_currentLayerName).
                                    putExtra("x", _reP.getX()).
                                    putExtra("y", _reP.getY()).
                                    putExtra("smId", -1));*/

                                    } else {  //查询点 点编辑
                                        s_PointSmid = _result.getID();
                                        //设置选中状态
                                        BaseFieldPInfos _infosP = setPtSelectionHighLigh(_result);
                                        LogUtills.i("Info=" + _infosP.toString());
                                        //设置图层样式
                                        setSplayerType(_infosP);

                                        QueryPointFragment _fragment = new QueryPointFragment();
                                        Bundle _bundle = new Bundle();
                                        _bundle.putString("gpType", m_currentLayerName);
                                        _bundle.putInt("smId", s_PointSmid);
                                        _bundle.putParcelable("info", _infosP);
                                        _fragment.setArguments(_bundle);
                                        _fragment.show(m_context.getSupportFragmentManager().beginTransaction(), "queryPoint");

                           /*   Intent _intent = new Intent(m_context, QueryPipePointActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("smId", s_PointSmid);
                            bundle.putParcelable("info", _infosP);
                            bundle.putString("gpType", m_currentLayerName);
                            _intent.putExtras(bundle);
                            m_context.startActivity(_intent);  */

                                        if (_result != null) {
                                            _result.close();
                                            _result.dispose();
                                        }
                                    }
                                }
                                break;
                                //创建第一个点 是否选择点  是 加线  否则 查询线
                                case Action_CreateLineStartPoint: {
                                    //是否选中点
                                    Recordset _resultPoint = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    //没有查询到点 继续查询线
                                    if (_resultPoint.isEmpty() || _resultPoint == null) {

                                        Recordset _resultLine = queryLineByMouseXMouseY2(motionEvent.getX(), motionEvent.getY());
                                        //如果选中线
                                        if (!_resultLine.isEmpty()) {
                                            //每次查询到线，把之前的记录clear掉，保持一条记录
                                            m_SmIDsL.clear();
                                            m_SmIDsL.add(_resultLine.getID());
                                            LogUtills.i("Action_CreateLineStartPoint Has Query Line...");
                                            _resultLine.moveFirst();
                                            int _smid = _resultLine.getID();
                                            BaseFieldLInfos _infoL = setLrSelectionHighLigh(_resultLine);
                                            if (_infoL == null) {
                                                LogUtills.e("Qurey Line Property Faild " + _resultLine.getID());
                                            }
                                            setSplayerType(_infoL);

                                            QueryLineFragment _fragment = new QueryLineFragment();
                                            Bundle bundle = new Bundle();
                                            //线对象
                                            bundle.putParcelable("_infoL", _infoL);
                                            //线smid
                                            bundle.putInt("smId", _smid);
                                            //线类型
                                            bundle.putString("gpType", m_currentLayerName);
                                            //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                                            bundle.putInt("actionType", 1);
                                            //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                                            bundle.putString("exp_Num", "0");
                                            //点smid
                                            bundle.putInt("SmIDP", -1);
                                            _fragment.setArguments(bundle);
                                            _fragment.show(m_context.getSupportFragmentManager().beginTransaction(), "queryline");

                                            ///
                              /*  Intent _intent = new Intent(m_context, QueryPipeLineActivity.class);
                                Bundle bundle = new Bundle();
                                //线对象
                                bundle.putParcelable("_infoL", _infoL);
                                //线smid
                                bundle.putInt("smId", _smid);
                                //线类型
                                bundle.putString("gpType", m_currentLayerName);
                                //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                                bundle.putInt("actionType", 1);
                                //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                                bundle.putString("exp_Num", "0");
                                //点smid
                                bundle.putInt("SmIDP", -1);
                                _intent.putExtras(bundle);
                                m_context.startActivity(_intent);*/

                                            _resultLine.close();
                                            _resultLine.dispose();
                                        } else {
                                            ToastUtil.showShort(m_context, "没有选择对象");
                                        }
                                    } else {
                                        ///判断是否查询到点
//                            Recordset _resultPoint = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY(), SuperMapConfig.User_Query_Point_Size);
//                            if (_resultPoint.isEmpty()) {
//                                LogUtills.i("Create Line Query Start Point Faild...");
//                                return false;
//                            } //没查到返回
//                            _result.moveFirst();
                                        //LogUtills.i("StartPoint Infomation: "+_result.toGeoJSON(true,1));
                                        String _startPId = _resultPoint.getString("exp_Num");
                                        if (_startPId.contains("T_")) {
                                            ToastUtil.show(m_context, "临时点不能作为起始点", Toast.LENGTH_SHORT);
                                            LogUtills.w("Temp Point Can Not As Line Start Point");
                                            return false;
                                        }

                                        BaseFieldPInfos _pInfos = setPtSelectionHighLigh(_resultPoint);
                                        m_SmIDs.add(_resultPoint.getID());
                                        _resultPoint.close();
                                        _resultPoint.dispose();
                                        ToastUtil.show(m_context, "选择中点：" + _startPId, Toast.LENGTH_SHORT);
                                        setSplayerType(_pInfos);
                                        //进入创建线第二个点的状态
                                        m_actionType2 = MAPACTIONTYPE2.Action_CreateLineDirectPoint;
                                    }
                                }
                                break;
                                case Action_CreateLineDirectPoint: {

                                    //选择第2个点，查询范围扩大，避免选择不到点
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    //第二个点未选中
                                    if (_result.isEmpty()) {
                                        //创建临时点
                                        Point _pt = new Point();
                                        _pt.setX((int) motionEvent.getX());
                                        _pt.setY((int) motionEvent.getY());
                                        Point2D _reP = m_mapCtrl.getMap().pixelToMap(_pt);
                                        String code = m_currentLayerName.substring(m_currentLayerName.indexOf("-") + 1);
                                        BaseFieldPInfos _temEndPt = PointFieldFactory.createTempInfo(_reP, m_currentLayerName, code);
                                        _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                        setPtSelectionHighLigh(_result);

                                        DrawLineFragment _fragmentLr = new DrawLineFragment();
                                        Bundle _bundle = new Bundle();
                                        _bundle.putInt("beginSmId", m_SmIDs.get(0));
                                        _bundle.putInt("endSmId", _result.getID());
                                        _bundle.putString("gpType", m_currentLayerName);
                                        _fragmentLr.setArguments(_bundle);
                                        _fragmentLr.show(m_context.getSupportFragmentManager().beginTransaction(), "line");

                                        ///
/*                            Intent _intent = new Intent(m_context, DrawPipeLineActivity.class);
                            _intent.putExtra("beginSmId", m_SmIDs.get(0));
                            _intent.putExtra("endSmId", _result.getID());
                            _intent.putExtra("gpType", m_currentLayerName);
                            LogUtills.i("Connect Line Type Name: " + m_currentLayerName);
                            // _intent.putExtra("smId", -1);
                            m_context.startActivity(_intent);*/

                                        setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);

                                        //连接临时线
/*                            Recordset _startResult = queryRecordsetBySmid(m_SmIDs.get(0), true, false);
                            BaseFieldPInfos _startPt = BaseFieldPInfos.createFieldInfo(_startResult);

                            BaseFieldLInfos _temLr = new TheTotalLine();//创建临时点
                            _temLr.pipeType = "临时_O";
                            _temLr.startLatitude = _startPt.latitude;
                            _temLr.startLongitude = _startPt.longitude;
                            _temLr.endLatitude = _temEndPt.latitude;
                            _temLr.endLongitude = _temEndPt.longitude;
                            _temLr.labelTag = "temp";
                            addRecords(_temLr);
                            //创建完回到第一步，创建连线的第一个点
                            setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);*/
                                    } else {
                                        //选中点则连线
                                        Selection _selection = getPtThemeLayerByName2().getSelection();
                                        int _ptSmid = _result.getID();
                                        if (_ptSmid == m_SmIDs.get(0)) {
                                            ToastUtil.show(m_context, "起始点与方向点相同，请重新选择方向点", 3);
                                            return false;
                                        }
                                        //获取起点编号
                                        Recordset _startPt = queryRecordsetBySmid(m_SmIDs.get(0), true, false);
                                        if (_startPt == null) {
                                            return false;
                                        }

                                        //重线判断
                                        String _duplicateLineCheck = "(benExpNum = '" + _startPt.getString("exp_Num") + "' and endExpNum = '" + _result.getString("exp_Num") + "') or " +
                                                "(endExpNum = '" + _startPt.getString("exp_Num") + "' and benExpNum = '" + _result.getString("exp_Num") + "')";
                                        LogUtills.i("_duplicateLineCheck = " + _duplicateLineCheck);
                                        Recordset _duplicateLine = QueryRecordsetBySql(_duplicateLineCheck, false, false);
                                        if (_duplicateLine.getRecordCount() > 0) {
                                            ToastUtil.showShort(m_context, "不允许重复连接，请重新选择方向点");
                                            LogUtills.e("The Start Point And the Direction Point Is Connect!");
                                            return false;
                                        }

                                        setPtSelectionHighLigh(_result);
                                        DrawLineFragment _fragmentLr = new DrawLineFragment();
                                        Bundle _bundle = new Bundle();
                                        _bundle.putInt("beginSmId", _startPt.getID());
                                        _bundle.putInt("endSmId", _result.getID());
                                        _bundle.putString("gpType", m_currentLayerName);
                                        _fragmentLr.setArguments(_bundle);
                                        _fragmentLr.show(m_context.getSupportFragmentManager().beginTransaction(), "line");

                            /*Intent _intent = new Intent(m_context, DrawPipeLineActivity.class);
                            _intent.putExtra("beginSmId", _startPt.getID());
                            _intent.putExtra("endSmId", _result.getID());
                            //String _lineType = _startPt.getString("datasetName").substring(2).replace("_", "-");
                            _intent.putExtra("gpType", m_currentLayerName);
                            LogUtills.i("Connect Line Type Name: " + m_currentLayerName);
                            // _intent.putExtra("smId", -1);
                            m_context.startActivity(_intent);*/

                                        setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);
                                    }
                                    if (_result != null) {
                                        _result.close();
                                        _result.dispose();
                                    }
                                }
                                break;

                                //线中加点
                                case Action_AddPointInLine_FindLine: {
                                    Recordset _result = queryLineByMouseXMouseY2(motionEvent.getX(), motionEvent.getY());
                                    if (_result == null || _result.isEOF()) {
                                        ToastUtil.showShort(m_context, "请选择要添加的点的线");
                                        LogUtills.d("Add Point In Line Query Line Faild...");
                                        return false;
                                    }
                                    //线高亮
                                    BaseFieldLInfos _infos = setLrSelectionHighLigh(_result);
                                    setSplayerType(_infos);
                                    _result.moveFirst();
                                    m_addPtOfLineSmID = _result.getID();
                                    _result.close();
                                    _result.dispose();
                                    ToastUtil.showShort(m_context, "获取管线成功...");
                                    m_actionType2 = MAPACTIONTYPE2.Action_AddPointInLine_FindPoint;
                                }
                                break;
                                //加点
                                case Action_AddPointInLine_FindPoint: {
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    if (m_addPtOfLineSmID == -1) {
                                        LogUtills.e("Action AddPointInLine Find Line Faild...");
                                        return false;
                                    }
                                    BaseFieldPInfos _cenPInfo = null;
                                    if (!_result.isEmpty()) {
                                        //dialog再次确定选中的点为要添加的终点
//                            _result.moveFirst();
//                            _cenPInfo = BaseFieldPInfos.createFieldInfo(_result);
//                            if (_cenPInfo == null) {
//                                LogUtills.e("Can not Create BaseFieldPInfos, ID=" + _result.getID());
//                                return false;
//                            }

                                    } else { //没有选中，则创建临点，临时线

                       /*     //BaseFieldPInfos _tPInfo = new BaseFieldPInfos.createFieldInfo();
                            //创建临时点：
                            Point _pt = new Point();
                            _pt.setX((int) motionEvent.getX());
                            _pt.setY((int) motionEvent.getY());
                            Point2D _reP = m_mapCtrl.getMap().pixelToMap(_pt);
                            String code = m_currentLayerName.substring(m_currentLayerName.indexOf("_") + 1);
                            _cenPInfo = PointFieldFactory.createTempInfo(_reP, m_currentLayerName.substring(0, 2), code);
                            if (_cenPInfo == null) {
                                LogUtills.e("Can not Create Temp BaseFieldPInfos, At Add Point In Line...");
                                return false;
                            }
                            */

                                        Point _pt = new Point();
                                        _pt.setX((int) motionEvent.getX());
                                        _pt.setY((int) motionEvent.getY());
                                        Point2D _reP = m_mapCtrl.getMap().pixelToMap(_pt);

                                        DrawPointInLineFragment _fragment = new DrawPointInLineFragment();
                                        Bundle _bundle = new Bundle();
                                        _bundle.putDouble("y", _reP.getY());
                                        _bundle.putDouble("x", _reP.getX());
                                        _bundle.putString("gpType", m_currentLayerName);
                                        _bundle.putInt("smId", m_addPtOfLineSmID);
                                        _fragment.setArguments(_bundle);
                                        _fragment.show(m_context.getSupportFragmentManager().beginTransaction(), "addpointtoline");

                           /* m_context.startActivity(new Intent(m_context, AddPointToLineActivity.class).
                                    putExtra("gpType", m_currentLayerName).
                                    putExtra("x", _reP.getX()).
                                    putExtra("y", _reP.getY()).
                                    putExtra("smId", m_addPtOfLineSmID)); //线SMID*/
                                        m_addPtOfLineSmID = -1;

                                    }

                      /*  Recordset _lineRecordset = queryRecordsetBySmid(m_addPtOfLineSmID, false, true);
                        if (_lineRecordset == null || _lineRecordset.isEOF()) {
                            LogUtills.i("Query Line Recordset Faild, ID:" + m_addPtOfLineSmID);
                            return false;
                        }
                        _lineRecordset.moveFirst();
                        BaseFieldLInfos _lineInfo = BaseFieldLInfos.createFieldInfo(_lineRecordset);
                        LogUtills.i("Line Type = " + _lineInfo.pipeType + ",Point Type = " + _cenPInfo.pipeType);
                        if (!(_lineInfo.pipeType.equals(_cenPInfo.pipeType))) {
                            ToastUtil.show(m_context, "线中加点，点的类型应与选中的线相同，请重新选点", Toast.LENGTH_SHORT);
                            LogUtills.i("Add Point in Line Type Is Differ Selection Point ");
                            return false;
                        }

                        //设置查询参数/
                        Recordset _start_reset = queryRecordsetByExpNum(_lineInfo.benExpNum, false);
                        BaseFieldPInfos _spInfo = BaseFieldPInfos.createFieldInfo(_start_reset);

                        //设置查询参数
                        Recordset _end_reset = queryRecordsetByExpNum(_lineInfo.endExpNum, false);
                        BaseFieldPInfos _epInfo = BaseFieldPInfos.createFieldInfo(_end_reset);

                        //起点连接线
                        BaseFieldLInfos _s_Info = LineFieldFactory.createFieldInfo(_spInfo, _cenPInfo);
                        _s_Info.sysId = _lineInfo.sysId; //第一条线更新
                        editRecords(_s_Info);

                        //终点连接线
                        BaseFieldLInfos _e_Info = LineFieldFactory.createFieldInfo(_cenPInfo, _epInfo);
                        addRecords(_e_Info);//第二条线添加
*/
                                    setMapActionType(MAPACTIONTYPE2.Action_AddPointInLine_FindLine);
                                }

                                break;
                                //移动管点位置
                                case Action_EditPointLocation: {
                                    //当前位置
                                    Point _pt = new Point();
                                    _pt.setX((int) motionEvent.getX());
                                    _pt.setY((int) motionEvent.getY());
                                    Point2D _reP = m_mapCtrl.getMap().pixelToMap(_pt);
                                    Recordset _sReset = queryRecordsetBySmid(m_SmIDs.get(0), true, true);
                                    if (_sReset == null || _sReset.isEmpty()) {
                                        LogUtills.e("Edit Point Location Faild,ID=" + m_SmIDs.get(0));
                                        ToastUtil.show(m_context, "移动点位置失败，很可能是选中的点出现了问题...", Toast.LENGTH_SHORT);
                                        return false;
                                    }

                                    //移动的点后位置的点
                                    BaseFieldPInfos _pSInfo = BaseFieldPInfos.createFieldInfo(_sReset);
                                    _pSInfo.longitude = _reP.getX();
                                    _pSInfo.latitude = _reP.getY();
                                    //一个点更新可用
                                    if (!editRecords(_pSInfo)) {
                                        ToastUtil.show(m_context, "移动点位置失败，很可能是选中的点出现了问题...", Toast.LENGTH_SHORT);
                                        _sReset.close();
                                        _sReset.dispose();
                                        return false;
                                    }

                                    //更新为起始点为该点号的线
                                    String _query = "benExpNum = '" + _pSInfo.exp_Num + "'";
//                        String _query = "benExpNum = '" + _pSInfo.exp_Num + "' or benExpNum like '" + _pSInfo.exp_Num.substring(0,_pSInfo.exp_Num.length()-1) + "%'";
                                    LogUtills.i("SqlQuery", _query);
                                    Recordset _sLResets = QueryRecordsetBySql(_query, false, true);
                                    _sLResets.moveFirst();
                                    //多个点更新，少去遍历属性，看能否提高效率;
                                    while (!_sLResets.isEOF()) {
                                        Point2Ds _2ds = new Point2Ds();
                                        //点的位置
                                        _2ds.add(_reP);
                                        _2ds.add(new Point2D(_sLResets.getDouble("endLongitude"), _sLResets.getDouble("endLatitude")));
                                        _sLResets.edit();
                                        _sLResets.setGeometry(new GeoLine(new Point2Ds(_2ds)));
                                        if (!_sLResets.update()) {
                                            ToastUtil.show(m_context, "更新线位置失败，读入数据集出错， Line ID=" + _sLResets.getID(), Toast.LENGTH_SHORT);
                                            return false;
                                        }
                                        _sLResets.edit();
                                        _sLResets.setString("benExpNum", _pSInfo.exp_Num);
                                        _sLResets.setDouble("startLongitude", _reP.getX());
                                        _sLResets.setDouble("startLatitude", _reP.getY());
                                        //改变线长度
                                        _sLResets.setString("pipeLength", String.format("%.2f", _sLResets.getDouble("SmLength")));
                                        if (!_sLResets.update()) {
                                            ToastUtil.show(m_context, "更新线长度失败， Line ID=" + _sLResets.getID(), Toast.LENGTH_SHORT);
                                        }
                                        _sLResets.moveNext();
                                    }

                                    //更新终点
                                    _query = " endExpNum = '" + _pSInfo.exp_Num + "'";
//                        _query = " endExpNum = '" + _pSInfo.exp_Num + "' or endExpNum like '" + _pSInfo.exp_Num.substring(0,_pSInfo.exp_Num.length()-1) + "%'";
                                    LogUtills.i("SqlQuery", _query);
                                    _sLResets = QueryRecordsetBySql(_query, false, true);
                                    _sLResets.moveFirst();
                                    while (!_sLResets.isEOF()) {
                                        Point2Ds _2ds = new Point2Ds();
                                        _2ds.add(new Point2D(_sLResets.getDouble("startLongitude"), _sLResets.getDouble("startLatitude")));
                                        _2ds.add(_reP);
                                        _sLResets.edit();
                                        if (!_sLResets.update()) {
                                            ToastUtil.show(m_context, "更新线位置失败，读入数据集出错， Line ID=" + _sLResets.getID(), Toast.LENGTH_SHORT);
                                            return false;
                                        }
                                        _sLResets.edit();
                                        _sLResets.setString("endExpNum", _pSInfo.exp_Num);
                                        _sLResets.setDouble("endLongitude", _reP.getX());
                                        _sLResets.setDouble("endLatitude", _reP.getY());
                                        _sLResets.setGeometry(new GeoLine(new Point2Ds(_2ds)));
                                        //改变线长度
                                        _sLResets.setString("pipeLength", String.format("%.2f", _sLResets.getDouble("SmLength")));
                                        if (!_sLResets.update()) {
                                            ToastUtil.show(m_context, "更新线长度失败， Line ID=" + _sLResets.getID(), Toast.LENGTH_SHORT);
                                        }
                                        _sLResets.moveNext();
                                    }
                                    ToastUtil.show(m_context, "更新线位置成功，点号:" + _pSInfo.exp_Num, Toast.LENGTH_SHORT);
                                    setMapActionType(MAPACTIONTYPE2.Action_CreatePoint);
                                    setPtSelectionHighLigh(_sReset);
                                    _sReset.close();
                                    _sReset.dispose();
                                    _sLResets.close();
                                    _sLResets.dispose();
                                }
                                break;

                                //获取起点
                                case Action_GetStartPoint: {
                                    //查询地图上的点
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    if (_result.isEmpty()) {
                                        ToastUtil.showShort(m_context, "没有选择点，请重新选择");
                                        return false;
                                    }
                                    //查询点的编号
                                    String _exp_Num = _result.getString("exp_Num");
                                    Recordset _resetLine = queryRecordsetBySmid(m_SmIDsL.get(0), false, false);
                                    String _benExpNum = _resetLine.getString("benExpNum");
                                    String _endExpNum = _resetLine.getString("endExpNum");

                                    //判断新获取的起点是否是原来的起点
                                    if (_exp_Num.equals(_benExpNum)) {
                                        ToastUtil.showShort(m_context, "新获取起点和原来的点一样，请重新选择");
                                        return false;
                                    }

                                    //判断新获取的起点是否是原来线的终点d
                                    if (_exp_Num.equals(_endExpNum)) {
                                        ToastUtil.showShort(m_context, "新获取起点和原来线的终点一样，请重新选择");
                                        return false;
                                    }

                                    //判断重新选择的起点和终点是否已经有线
                                    String sql = "(benExpNum = '" + _exp_Num + "' and endExpNum = '" + _endExpNum + "') or (benExpNum = '" + _endExpNum + "' and endExpNum = '" + _exp_Num + "')";
                                    Recordset _recordset = DataHandlerObserver.ins().QueryRecordsetBySql(sql, false, false);
                                    if (!_recordset.isEmpty()) {
                                        ToastUtil.showShort(m_context, "两点已经有线了，不可重线");
                                        return false;
                                    }

                                    //设置高亮
                                    BaseFieldLInfos _infoL = setLrSelectionHighLigh(queryRecordsetBySmid(m_SmIDsL.get(0), false, false));
                                    //跳转
                                    QueryLineFragment _fragment = new QueryLineFragment();
                                    Bundle bundle = new Bundle();
                                    //线对象
                                    bundle.putParcelable("_infoL", _infoL);
                                    //线smid
                                    bundle.putInt("smId", m_SmIDsL.get(m_SmIDsL.size() - 1));
                                    //线类型
                                    bundle.putString("gpType", m_currentLayerName);
                                    //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                                    bundle.putInt("actionType", 2);
                                    //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                                    bundle.putString("exp_Num", _exp_Num);
                                    //点smid
                                    bundle.putInt("SmIDP", _result.getID());
                                    _fragment.setArguments(bundle);
                                    _fragment.show(m_context.getSupportFragmentManager().beginTransaction(), "queryline");

                       /* Intent _intent = new Intent(m_context, QueryPipeLineActivity.class);
                        Bundle bundle = new Bundle();
                        //线对象
                        bundle.putParcelable("_infoL", _infoL);
                        //线smid
                        bundle.putInt("smId", m_SmIDsL.get(m_SmIDsL.size() - 1));
                        //线类型
                        bundle.putString("gpType", m_currentLayerName);
                        //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                        bundle.putInt("actionType", 2);
                        //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                        bundle.putString("exp_Num", _exp_Num);
                        //点smid
                        bundle.putInt("SmIDP", _result.getID());
                        _intent.putExtras(bundle);
                        m_context.startActivity(_intent);*/
                                    _result.close();
                                    _result.dispose();

                                }
                                setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);
                                break;

                                //获取终点
                                case Action_GetEndPoint: {
                                    //查询地图上的点
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    if (_result.isEmpty()) {
                                        ToastUtil.showShort(m_context, "没有选择点，请重新选择");
                                        return false;
                                    }
                                    //查询点的编号
                                    String _exp_Num = _result.getString("exp_Num");
                                    Recordset _resetLine = queryRecordsetBySmid(m_SmIDsL.get(0), false, false);
                                    String _benExpNum = _resetLine.getString("benExpNum");
                                    String _endExpNum = _resetLine.getString("endExpNum");
                                    //判断新获取的起点是否是原来的起点
                                    if (_exp_Num.equals(_benExpNum)) {
                                        ToastUtil.showShort(m_context, "新获取终点和原来线的起点一样，请重新选择");
                                        return false;
                                    }

                                    //判断新获取的起点是否是原来线的终点d
                                    if (_exp_Num.equals(_endExpNum)) {
                                        ToastUtil.showShort(m_context, "新获取终点点和原来线的终点一样，请重新选择");
                                        return false;
                                    }

                                    //判断重新选择的起点和终点是否已经有线
                                    String sql = "(benExpNum = '" + _exp_Num + "' and endExpNum = '" + _benExpNum + "') or (benExpNum = '" + _benExpNum + "' and endExpNum = '" + _exp_Num + "')";
                                    Recordset _recordset = DataHandlerObserver.ins().QueryRecordsetBySql(sql, false, false);
                                    if (!_recordset.isEmpty()) {
                                        ToastUtil.showShort(m_context, "两点已经有线了，不可重线");
                                        return false;
                                    }


                                    BaseFieldLInfos _infoL = setLrSelectionHighLigh(queryRecordsetBySmid(m_SmIDsL.get(0), false, false));
                                    //跳转
                                    QueryLineFragment _fragment = new QueryLineFragment();
                                    Bundle bundle = new Bundle();
                                    //线对象
                                    bundle.putParcelable("_infoL", _infoL);
                                    //线smid
                                    bundle.putInt("smId", m_SmIDsL.get(m_SmIDsL.size() - 1));
                                    //线类型
                                    bundle.putString("gpType", m_currentLayerName);
                                    //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                                    bundle.putInt("actionType", 3);
                                    //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                                    bundle.putString("exp_Num", _exp_Num);
                                    //点smid
                                    bundle.putInt("SmIDP", _result.getID());
                                    _fragment.setArguments(bundle);
                                    _fragment.show(m_context.getSupportFragmentManager().beginTransaction(), "queryline");


                      /*  Intent _intent = new Intent(m_context, QueryPipeLineActivity.class);
                        Bundle bundle = new Bundle();
                        //线对象
                        bundle.putParcelable("_infoL", _infoL);
                        //线smid
                        bundle.putInt("smId", m_SmIDsL.get(m_SmIDsL.size() - 1));
                        //线类型
                        bundle.putString("gpType", m_currentLayerName);
                        //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                        bundle.putInt("actionType", 3);
                        //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                        bundle.putString("exp_Num", _exp_Num);
                        //点smid
                        bundle.putInt("SmIDP", _result.getID());
                        _intent.putExtras(bundle);
                        m_context.startActivity(_intent);*/
                                    _result.close();
                                    _result.dispose();
                                }
                                setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);
                                break;

                                //测量收点
                                // TODO 收点的同时，设置线起点终点是否已经测了，用来统计已测管线工作量，在线中加入两个字段 起点测量  终点测量
                                case Action_MeasurePoint: {
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    if (_result.isEmpty()) {
                                        LogUtills.i("measure point not select point");
                                        return false;
                                    }
                                    BaseFieldPInfos infos = setPtSelectionHighLigh(_result);
                                    Datasource _datasource = m_mapCtrl.getMap().getWorkspace().getDatasources().get(0);
                                    //获取测量收点数据集
                                    DatasetVector datasetVector = (DatasetVector) _datasource.getDatasets().get("P_" + SuperMapConfig.Layer_Measure);
                                    //如果已经测量过，再次点击取消
                                    Recordset _reSet = datasetVector.query("exp_Num = '" + infos.exp_Num + "'", CursorType.DYNAMIC);
                                    if (!_reSet.isEmpty()) {
                                        _reSet.delete();
                                        MeasuredPointFragment.initData();
                                        m_mapCtrl.getMap().refresh();
                                        return false;
                                    }

                                    Point _pt = new Point();
                                    _pt.setX((int) motionEvent.getX());
                                    _pt.setY((int) motionEvent.getY());
                                    Point2D _reP = m_mapCtrl.getMap().pixelToMap(_pt);
                                    BaseFieldPInfos _info = PointFieldFactory.Create(SuperMapConfig.Layer_Measure);
                                    _info.symbolExpression = "测量收点";
                                    _info.exp_Num = infos.exp_Num;
                                    _info.longitude = infos.longitude;
                                    _info.latitude = infos.latitude;
                                    if (!createRecords(_info)) {
                                        LogUtills.i("create measure point fail");
                                    }


                                    m_mapCtrl.getMap().refresh();
                                    if (_result != null) {
                                        _result.close();
                                        _result.dispose();
                                    }

                                }
                                MeasuredPointFragment.initData();
                                break;
                                default:
                                    break;
                            }
                        }
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * 改变地图界面图层
     *
     * @Author HaiRun
     * @Time 2019/4/23 . 9: 30
     */
    private void setSplayerType(BaseFieldInfos infos) {
        if (!infos.pipeType.equals(m_currentLayerName)) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(MapActivity.spLayers, infos.pipeType);
            m_currentLayerName = infos.pipeType;
        }
    }


    /**
     * 通过sql语句查询记录集
     *
     * @param sqlStr
     * @param isPoint
     * @param isEdit
     * @return
     */
    public Recordset QueryRecordsetBySql(String sqlStr, boolean isPoint, boolean isEdit) {
        DatasetVector _dv = null;
        if (isPoint) {
            _dv = (DatasetVector) getTotalPtLayer().getDataset();
        } else {
            _dv = (DatasetVector) getTotalLrLayer().getDataset();
        }
        if (_dv == null) {
            return null;
        }
        //将查询条件传入到Query方法，查询记录集
        return _dv.query(sqlStr, isEdit ? CursorType.DYNAMIC : CursorType.STATIC);
    }

    /**
     * 通过QueryParameter 语句查询记录集
     *
     * @param param
     * @param isPoint
     * @return
     */
    public Recordset QueryRecordsetByParameter(QueryParameter param, boolean isPoint) {
        DatasetVector _dv = null;
        if (isPoint) {
            _dv = (DatasetVector) getTotalPtLayer().getDataset();
        } else {
            _dv = (DatasetVector) getTotalLrLayer().getDataset();
        }
        if (_dv == null) {
            return null;
        }
        //将查询条件传入到Query方法，查询记录集
        return _dv.query(param);
    }

    /**
     * 更新一个点的深度
     * @param sql
     * @param depth
     * @return
     */
    public boolean updateRecordSetBySql(String sql,String depth){
        DatasetVector _dv = (DatasetVector) getTotalPtLayer().getDataset();
        Recordset _recordset  = _dv.query(sql,CursorType.DYNAMIC);
        if (_recordset.isEmpty()){
            return true;
        }
        _recordset.moveFirst();
        _recordset.edit();
        _recordset.setString("depth",depth);

        return _recordset.update();
    }

    /**
     * 设置点高亮
     *
     * @param result
     * @return
     */
    private BaseFieldPInfos setPtSelectionHighLigh(Recordset result) {
        BaseFieldPInfos _infosP = BaseFieldPInfos.createFieldInfo(result);
        //设置选中状态
        Selection _selection = getPtThemeLayerByName2().getSelection();
        boolean isOk = _selection.fromRecordset(result);
        if (!isOk) {
            LogUtills.w("Set The Point Seletion Status faild...");
        }
        GeoStyle _style = _selection.getStyle();
        _style.setMarkerSymbolID(_infosP.symbolID);
        _style.setLineColor(new Color(255, 0, 0));
        _style.setMarkerSize(new Size2D(_infosP.symbolSizeX * 1.3, _infosP.symbolSizeY * 1.3));
        _selection.setStyle(_style);
        m_mapCtrl.getMap().refresh();
        return _infosP;
    }


    /**
     * 设置线高亮
     *
     * @param result
     * @return
     */
    private BaseFieldLInfos setLrSelectionHighLigh(Recordset result) {
        BaseFieldLInfos _infosP = BaseFieldLInfos.createFieldInfo(result);
        //设置选中状态
        Selection _selection = getTotalLrThemeLayer().getSelection();
        GeoStyle _style = _selection.getStyle();
        _style.setLineWidth(0.5);
        _style.setLineColor(new Color(255, 0, 0));
        _style.setLineSymbolID(0);
        _selection.setStyle(_style);
        boolean isOk = _selection.fromRecordset(result);
        if (!isOk) {
            LogUtills.i("Set The Line High Light Faild...");
        }
        m_mapCtrl.getMap().refresh();
        return _infosP;
    }

    /**
     * 编辑点
     */
    public boolean editRecords(BaseFieldPInfos pInfos) {
        if (pInfos == null) {
            return false;
        }

        Layer _pLayer = getTotalPtLayer();
        if (_pLayer == null) {
            return false;
        }

        DatasetVector dv = (DatasetVector) _pLayer.getDataset();
        FieldInfos _infos = dv.getFieldInfos();
        int[] _smids = new int[]{pInfos.sysId};
        Recordset _result = dv.query(_smids, CursorType.DYNAMIC);
        _result.moveFirst();
        if (_result.isEmpty()) {
            return false;
        }
        //锁定编辑的记录
        _result.edit();
        java.util.Map<String, Object> _values = OperNotifyer.getFieldMaps(pInfos);
        Set set = _values.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry mapentry = (java.util.Map.Entry) iterator.next();
            if (_infos.get(mapentry.getKey().toString()) == null) {
                continue;
            }
            if (mapentry.getKey().toString().equals("type")) {
                _result.setObject(mapentry.getKey().toString(), mapentry.getValue().toString());
                continue;
            }
            _result.setObject(mapentry.getKey().toString(), mapentry.getValue());
        }
        _result.setGeometry(new GeoPoint(pInfos.longitude, pInfos.latitude));
        _result.update();
        _result.close();
        _result.dispose();
        return true;
    }

    /**
     * 编辑管线
     */
    public boolean editRecords(BaseFieldLInfos pInfos) {
        if (pInfos == null) {
            LogUtills.e("Edit BaseFieldLInfos Faild, BaseFieldLInfos Is Null....");
            return false;
        }

        Layer _pLayer = getTotalLrLayer();
        if (_pLayer == null) {
            return false;
        }

        DatasetVector _dv = (DatasetVector) _pLayer.getDataset();
        FieldInfos _infos = _dv.getFieldInfos();
        int[] _smids = new int[]{pInfos.sysId};
        Recordset _result = _dv.query(_smids, CursorType.DYNAMIC);
        _result.moveFirst();
        if (_result.isEmpty()) {
            return false;
        }
        //锁定编辑的记录
        _result.edit();
        java.util.Map<String, Object> _values = OperNotifyer.getFieldMaps(pInfos);
        Set set = _values.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry mapentry = (java.util.Map.Entry) iterator.next();
            if (_infos.get(mapentry.getKey().toString()) == null) {
                continue;
            }
            if (mapentry.getKey().toString().equals("type")) {

                _result.setObject(mapentry.getKey().toString(), mapentry.getValue().toString());
                continue;
            }
            _result.setObject(mapentry.getKey().toString(), mapentry.getValue());
        }

        Point2Ds _p2ds = new Point2Ds();
        _p2ds.add(new Point2D(pInfos.startLongitude, pInfos.startLatitude));
        _p2ds.add(new Point2D(pInfos.endLongitude, pInfos.endLatitude));
        if (!_result.setGeometry(new GeoLine(_p2ds))) {
            ToastUtil.showShort(m_context, "线更改失败");
            return false;
        }
        if (!_result.update()) {
            ToastUtil.showShort(m_context, "线更加失败");
            return false;
        }
        //更改线长度
        _result.edit();
        _result.setString("pipeLength", String.format("%.2f", _result.getDouble("SmLength")));
        if (!_result.update()) {
            ToastUtil.showShort(m_context, "线长度更新失败");
            return false;
        }
        //关闭记录集，释放几何对象、记录集
        _result.close();
        _result.dispose();
        return true;
    }

    public boolean setAction(Action action) {
        m_mapCtrl.setAction(action);
        return true;
    }


    /**
     * 根据数据集名称获取图层
     */
    private Layer getLayerByName(String name) {

        String _p_name = name + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME;
        Layer _temp = WorkSpaceUtils.getInstance().getMapControl().getMap().getLayers().get(_p_name);
        if (_temp == null) {
            return null;
        }
        return _temp;
    }

    /**
     * 获取点图层
     *
     * @return
     */
    public Layer getTotalPtLayer() {
        String _p_name = "P_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME;
        Layer _temp = m_mapCtrl.getMap().getLayers().get(_p_name);
        if (_temp == null) {
            LogUtills.w("Query Point Layer " + _p_name + "Is  Null...");
            return null;
        }
        return _temp;
    }

    /**
     * 获取点单值专题图图层
     *
     * @return
     */
    public Layer getTotalPtThemeLayer() {
        String _p_name = "P_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#1";
        //LogUtills.i("query layer = " + _p_name);
        Layer _temp = m_mapCtrl.getMap().getLayers().get(_p_name);
        if (_temp == null) {
            LogUtills.w("Query Point Layer " + _p_name + "Is  Null...");
            return null;
        }
        return _temp;
    }


    /**
     * 获取线图层
     *
     * @return
     */
    public Layer getTotalLrLayer() {
        String _p_name = "L_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME;
        Layer _temp = m_mapCtrl.getMap().getLayers().get(_p_name);
        if (_temp == null) {
            LogUtills.w("Query Line Layer " + _p_name + "Is  Null...");
            return null;
        }
        return _temp;
    }

    /**
     * 获取线单值专题图图层
     *
     * @return
     */
    public Layer getTotalLrThemeLayer() {
        String _p_name = "L_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#2";
        Layer _temp = m_mapCtrl.getMap().getLayers().get(_p_name);
        if (_temp == null) {
            LogUtills.w("Query Line Layer " + _p_name + "Is  Null...");
            return null;
        }
        return _temp;
    }


    /**
     * @return 获取图层
     */
    public Layer getPtLayerByName(String dsName) {

        String _targetName = "P_" + dsName + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME;
        Layer _temp = m_mapCtrl.getMap().getLayers().get(_targetName);
        if (_temp == null) {
            LogUtills.e("Find The Point " + _targetName + " Layer Faild");
            return null;
        }
        if (!_temp.isSelectable()) {
            _temp.setSelectable(true);
        }
        if (!_temp.isEditable()) {
            _temp.setEditable(true);
        }
        return _temp;
    }


    /**
     * @return 获取图层
     */
    public List<Layer> getPtLayerByName(List<String> list) {
        List<Layer> _list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String _targetName = "P_" + list.get(i) + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME;
            Layer _temp = m_mapCtrl.getMap().getLayers().get(_targetName);
            if (_temp == null) {
                return null;

            }
            if (!_temp.isSelectable()) {
                _temp.setSelectable(true);
            }
            if (!_temp.isEditable()) {
                _temp.setEditable(true);
            }
            _list.add(_temp);
        }
        return _list;
    }

/*    public Layer getPtThemeLayerByName(String datasetName) {
        //标签专题图图层名字
        String _targetName = "P_" + datasetName + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#2";
        Layer _temp = m_mapCtrl.getMap().getLayers().get(_targetName);

        if (_temp == null) {

            return null;
        }
        return _temp;
    }*/

    private Layer getPtThemeLayerByName2() {
        //标签专题图图层名字
        String _targetName = "P_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#2";
        Layer _temp = m_mapCtrl.getMap().getLayers().get(_targetName);
        if (_temp == null) {
            LogUtills.w("Get The Theme Layer " + _targetName + " Layer Faild");
            return null;
        }
        return _temp;
    }


    /**
     * 创建记录集
     *
     * @param pInfos
     * @return
     */
    private boolean createRecords(BaseFieldPInfos pInfos) {
        try {
            if (pInfos == null) {
                return false;
            }
            Layer _pLayer = getLayerByName(pInfos.datasetName);
            if (_pLayer == null) {
                return false;
            }
            DatasetVector dv = (DatasetVector) _pLayer.getDataset();
            FieldInfos _infos = dv.getFieldInfos();
            Recordset _result = dv.getRecordset(true, CursorType.DYNAMIC);
            _result.moveFirst();
            //锁定编辑的记录
            _result.edit();
            java.util.Map<String, Object> _new_values = new HashMap();
            java.util.Map<String, Object> _values = OperNotifyer.getFieldMaps(pInfos);
            Set set = _values.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                java.util.Map.Entry mapentry = (java.util.Map.Entry) iterator.next();
                if (_infos.get(mapentry.getKey().toString()) == null) {
                    continue;
                }
                if (mapentry.getKey().toString().equals("type")) {
                    _new_values.put(mapentry.getKey().toString(), mapentry.getValue().toString());
                    continue;
                }
                _new_values.put(mapentry.getKey().toString(), mapentry.getValue());
            }
            _result.addNew(new GeoPoint(pInfos.longitude, pInfos.latitude), _new_values);
            pInfos.sysId = _result.getID();
            _result.setString("id", pInfos.id);
            _result.setInt32("sysId", _result.getID());
            boolean isOk = _result.update();
            if (isOk) {
                _result.close();
                _result.dispose();
                return true;
            } else {
                _result.close();
                _result.dispose();
                return false;
            }
        } catch (Exception e) {

            return false;
        }
    }

    /**
     * 创建点记录集
     *
     * @param pInfos
     * @return
     */
    public boolean createRecords2(BaseFieldPInfos pInfos) {

        if (pInfos == null) {
            LogUtills.w("CreateRecords BaseFieldPInfos is null....");
            return false;
        }
        Layer _pLayer = getTotalPtLayer();
        if (_pLayer == null) {
            return false;
        }
        DatasetVector dv = (DatasetVector) _pLayer.getDataset();
        FieldInfos _infos = dv.getFieldInfos();
        Recordset _result = dv.getRecordset(true, CursorType.DYNAMIC);
        _result.moveFirst();
        _result.edit(); //锁定编辑的记录
        java.util.Map<String, Object> _new_values = new HashMap();
        java.util.Map<String, Object> _values = OperNotifyer.getFieldMaps(pInfos);
        Set set = _values.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry mapentry = (java.util.Map.Entry) iterator.next();
            if (_infos.get(mapentry.getKey().toString()) == null) {
                continue;
            }
            if (mapentry.getKey().toString().equals("type")) {
                _new_values.put(mapentry.getKey().toString(), mapentry.getValue().toString());
                continue;
            }
            _new_values.put(mapentry.getKey().toString(), mapentry.getValue());
        }
        _result.addNew(new GeoPoint(pInfos.longitude, pInfos.latitude), _new_values);
        pInfos.sysId = _result.getID();
        _result.setString("id", pInfos.id);
        _result.setInt32("sysId", _result.getID());
        boolean isOk = _result.update();
        if (_result != null) {
            _result.close();
            _result.dispose();
        }

        return isOk;

    }


    /**
     * 创建线记录集
     */
    public boolean addRecords(BaseFieldLInfos pInfos) {
        if (pInfos == null) {
            LogUtills.e("Create The Line Find The BaseFieldLInfos Is Null...");
            return false;
        }

        Layer _pLayer = getTotalLrLayer();
        if (_pLayer == null) return false;

        DatasetVector _dv = (DatasetVector) _pLayer.getDataset();
        FieldInfos _infos = _dv.getFieldInfos();
        Recordset _result = _dv.getRecordset(true, CursorType.DYNAMIC);
        _result.moveFirst();
        _result.edit(); //锁定编辑的记录
        java.util.Map<String, Object> _values = OperNotifyer.getFieldMaps(pInfos);
        java.util.Map<String, Object> _new_values = new HashMap();
        Set set = _values.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry mapentry = (java.util.Map.Entry) iterator.next();
            if (_infos.get(mapentry.getKey().toString()) == null) {
                LogUtills.i("Can Not Get Field Name = " + mapentry.getKey().toString());
                continue;
            }

            if (mapentry.getKey().toString().equals("type")) {
                _new_values.put(mapentry.getKey().toString(), mapentry.getValue().toString());
                continue;
            }

            _new_values.put(mapentry.getKey().toString(), mapentry.getValue());
        }

        Point2Ds _p2ds = new Point2Ds();
        _p2ds.add(new Point2D(pInfos.startLongitude, pInfos.startLatitude));
        _p2ds.add(new Point2D(pInfos.endLongitude, pInfos.endLatitude));

        GeoLine _geoLine = new GeoLine(_p2ds);
        _result.addNew(_geoLine, _new_values);
        _result.setInt32("sysId", _result.getID());
        String _labelTag = String.valueOf(_result.getID()) + pInfos.labelTag;
        _result.setString("labelTag", _labelTag);
        _result.update();

        // 关闭记录集，释放几何对象、记录集
        _result.close();
        _result.dispose();
        _geoLine.dispose();
        return true;
    }


    /**
     * 通过 编号查询记录集
     *
     * @param expNum
     * @param isEdit
     * @return 范围查询
     */
    public Recordset queryRecordsetByExpNum(String expNum, boolean isEdit) {
        String _query = "exp_Num = '" + expNum + "'";
        QueryParameter _parameter = new QueryParameter();
        _parameter.setAttributeFilter(_query);
        _parameter.setCursorType(isEdit ? CursorType.DYNAMIC : CursorType.STATIC);
        //SQL语句决定了只能查点
        return QueryRecordsetByParameter(_parameter, true);
    }

    /**
     * 通过smid查询数据集
     *
     * @param smid
     * @param isPoint
     * @param isEdit
     * @return
     */
    public Recordset queryRecordsetBySmid(int smid, boolean isPoint, boolean isEdit) {
        DatasetVector _dv = null;
        if (isPoint) {
            _dv = (DatasetVector) getTotalPtLayer().getDataset();
        } else {
            _dv = (DatasetVector) getTotalLrLayer().getDataset();
        }

        if (_dv == null) {
            return null;
        }
        return _dv.query(new int[]{smid}, isEdit ? CursorType.DYNAMIC : CursorType.STATIC);
    }


    /**
     * 查询点数据集
     *
     * @param x
     * @param y
     * @return
     */
    public Recordset queryPointByMouseXMouseY3(float x, float y) {

        // BaseFieldPInfos _p = new TheTotalPoint();
        double _scale = m_mapCtrl.getMap().getScale();

        //根据比例尺 配置缓冲区 100
        settingQueryBuffer(_scale);

        Point _pt = new Point();
        _pt.setX((int) x);
        _pt.setY((int) y);
        Point2D _reP = m_mapCtrl.getMap().pixelToMap(_pt);
        GeoPoint _geoPt = new GeoPoint(_reP);

        Rectangle2D _rect = new Rectangle2D(new Point2D(_reP.getX() - SuperMapConfig.Query_Buffer, _reP.getY() - SuperMapConfig.Query_Buffer),
                new Point2D(_reP.getX() + SuperMapConfig.Query_Buffer, _reP.getY() + SuperMapConfig.Query_Buffer));

        QueryParameter _queryParameter = new QueryParameter();
        _queryParameter.setSpatialQueryObject(_rect);
        _queryParameter.setSpatialQueryMode(SpatialQueryMode.CONTAIN);
        //_queryParameter.setAttributeFilter(_query);
        _queryParameter.setHasGeometry(false);
        _queryParameter.setCursorType(CursorType.STATIC);

//        _queryParameter.setResultFields(new String[]{"SmID,", " (("+_reP.getY()+"-latitude)^2+("+_reP.getX()+"-longitude)^2 )"+ " as dis"});
        //String sql = "(Power("+_reP.getY()+"-SmY,2)+Power("+_reP.getX()+"-SmX,2)) as dis ";
        String sql = "((" + _reP.getX() + "-SmX) * (" + _reP.getX() + "-SmX) + (" + _reP.getY() + "-SmY) * (" + _reP.getY() + "-SmY) ) as dis";
        _queryParameter.setResultFields(new String[]{"SmID", sql, "exp_Num"});
        _queryParameter.setOrderBy(new String[]{"dis asc"});

        Layer _layer = getTotalPtLayer();
        if (_layer == null) {
            return null;
        }
        DatasetVector _dv = (DatasetVector) _layer.getDataset();
        //先按地图比例尺缩放查询有没有数据 可能有多个
//        Recordset _reset = _dv.query(_queryParameter);
        Recordset _reset = _dv.query(_geoPt, SuperMapConfig.Query_Buffer, CursorType.STATIC);
        //如果有数据，则算出最近那个 升序
        if (!_reset.isEmpty()) {
            //再从刚才查找的范围里找出数据
            DatasetVector _dataset = _reset.getDataset();
            _reset = _dataset.query(_queryParameter);
        }

        if (_reset.isEmpty()) {
            return _reset;
        }
        _reset.moveFirst();
        int _smid = _reset.getID();
        //返回一个记录集
        return _dv.query(new int[]{_reset.getID()}, CursorType.STATIC);
    }


    /**
     * 根据地图比例尺设置缓冲范围
     *
     * @param _scale
     */
    private void settingQueryBuffer(double _scale) {
        //分的等级越细越精准
        //1:5000
        if (_scale <= 0.0002) {
            SuperMapConfig.Query_Buffer = 30;
            //1:1000
        } else if (0.0002 < _scale && _scale <= 0.001) {
            SuperMapConfig.Query_Buffer = 20;
            //1:500
        } else if (0.001 < _scale && _scale <= 0.002) {
            SuperMapConfig.Query_Buffer = 4.0;
            //1:400
        } else if (0.002 < _scale && _scale <= 0.0025) {
            SuperMapConfig.Query_Buffer = 3.0;
            //1:300
        } else if (0.0025 < _scale && _scale <= 0.003) {
            SuperMapConfig.Query_Buffer = 2.0;
            //1:250
        } else if (0.003 < _scale && _scale <= 0.004) {
            SuperMapConfig.Query_Buffer = 1.0;
            //1:200
        } else if (0.004 < _scale && _scale <= 0.005) {
            SuperMapConfig.Query_Buffer = 0.8;
            //1:150
        } else if (0.005 < _scale && _scale <= 0.0067) {
            SuperMapConfig.Query_Buffer = 0.7;
            //1:100
        } else if (0.0067 < _scale && _scale <= 0.01) {
            SuperMapConfig.Query_Buffer = 0.5;
            //1:50
        } else if (0.01 < _scale && _scale <= 0.02) {
            SuperMapConfig.Query_Buffer = 0.3;
            //1:20
        } else if (0.02 < _scale && _scale <= 0.05) {
            SuperMapConfig.Query_Buffer = 0.3;
        } else {
            SuperMapConfig.Query_Buffer = 0.1;
        }
    }

    /**
     * 查询线数据集
     *
     * @param x
     * @param y
     * @return re
     */
    public Recordset queryLineByMouseXMouseY2(float x, float y) {
        Point _pt = new Point();
        _pt.setX((int) x);
        _pt.setY((int) y);
        Point2D _reP = m_mapCtrl.getMap().pixelToMap(_pt);
        GeoPoint _geoPt = new GeoPoint(_reP);
        Layer _layer = getTotalLrLayer();
        if (_layer == null) {
            return null;
        }
        DatasetVector _dv = (DatasetVector) _layer.getDataset();
        //根据比例尺 配置缓冲区 100
        double _scale = m_mapCtrl.getMap().getScale();
        settingQueryBuffer(_scale);
        Recordset _recordset = _dv.query(_geoPt, SuperMapConfig.Query_Buffer, CursorType.DYNAMIC);
        if (_recordset.getRecordCount() == 1) {
            return _recordset;
        }
        //如果有多个线数据集，则返回距离平面点最近那条线
        double _tempL = 10000.0;
        int _index = 0;
        _recordset.moveFirst();
        while (!_recordset.isEOF()) {
            // 点到线最短的距离 返回最短距离的线的id
            double _len = PtToLrLUtils.Ins().pointToLine(_recordset, _reP);
            if (_len < _tempL) {
                _index = _recordset.getID();
                _tempL = _len;
            }
            _recordset.moveNext();
        }
        return _dv.query(new int[]{_index}, CursorType.DYNAMIC);
    }


}
