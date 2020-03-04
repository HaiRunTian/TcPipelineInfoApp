package com.app.BaseInfo.Oper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.MAPACTIONTYPE2;
import com.app.BaseInfo.Data.PointFieldFactory;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.PipeThemelabelUtil;
import com.app.pipelinesurvey.utils.PtToLrLUtils;
import com.app.pipelinesurvey.utils.SymbolInfo;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.activity.MapActivity;
import com.app.pipelinesurvey.view.fragment.map.mapdata.DrawLineFragment;
import com.app.pipelinesurvey.view.fragment.map.mapdata.DrawPointFragment;
import com.app.pipelinesurvey.view.fragment.map.mapdata.DrawPointInLineFragment;
import com.app.pipelinesurvey.view.fragment.map.mapdata.QueryLineFragment;
import com.app.pipelinesurvey.view.fragment.map.mapdata.QueryPointFragment;
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
import com.supermap.mapping.Action;
import com.supermap.mapping.Layer;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.Selection;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 地图核心操作类
 *
 * @author Los on 2018-06-20 10:57.
 */
public class DataHandlerObserver {
    private static DataHandlerObserver s_ins = null;
    private MapControl mMapCtrl = null;
    private MAPACTIONTYPE2 mActionType2 = MAPACTIONTYPE2.Action_None;
    /**
     * 当前编辑的图层名字
     */
    private String mCurrentLayerName = SuperMapConfig.Layer_Total;
    /**
     * 当前选中点的SmID,目前两个地方用到，画线，移动管点，Los 2019.3.18
     */
    private List<Integer> mSmIDs = new ArrayList<>();
    /**
     * 当前选中线的SmID,主要是记录线重新选择起点或者终点，Tian 2019.3.21
     */
    private List<Integer> mSmIDsL = new ArrayList<>();
    private Integer mAddPtOfLineSmID = -1;
    private FragmentActivity mContext = null;
    /**
     * 查询管点，移动管点的smid
     */
    private int mPointSmid = 0;
    private double mStartX = 0.0;
    private double mStartY = 0.0;
    private double mEndX = 0.0;
    private double mEndY = 0.0;
    private long mLastTime = 0L;

    /**
     * 构造函数
     */
    private DataHandlerObserver() {
        mMapCtrl = WorkSpaceUtils.getInstance().getMapControl();
        mMapCtrl.setOnTouchListener(new NewGeometryOnTouchListner());
    }

    /**
     * 设置动作状态
     *
     * @param type
     */
    public void setMapActionType(MAPACTIONTYPE2 type) {
        //清除点选择集
        getPtThemeLayerByName2().getSelection().clear();
        //清除线选择集
        getTotalLrThemeLayer().getSelection().clear();
        mMapCtrl.getMap().refresh();
        mSmIDs.clear();
        mAddPtOfLineSmID = -1;
        mActionType2 = type;
    }

    /**
     * 设置上下文
     */
    public void setContext(FragmentActivity context) {
        mContext = context;
    }

    /**
     * 添加点SMID，查询时需要
     */
    public void addPointSmID(int smid) {
        mSmIDs.add(smid);
    }

    /**
     * 单例
     *
     * @return
     */
    public synchronized static DataHandlerObserver ins() {
        if (s_ins == null) {
            s_ins = new DataHandlerObserver();
        }
        return s_ins;
    }

    /**
     * 带参数
     *
     * @param type
     * @return
     */
    public static synchronized DataHandlerObserver ins(String type) {
        s_ins = new DataHandlerObserver();
        return s_ins;
    }

    /**
     * 设置图层管线类型
     *
     * @param type
     */
    public void setCurrentPipeType(String type) {
        mCurrentLayerName = type;
    }

    public class NewGeometryOnTouchListner implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            try {
                switch (motionEvent.getAction()) {
                    //手指按下
                    case MotionEvent.ACTION_DOWN:
                        mStartX = motionEvent.getX();
                        mStartY = motionEvent.getY();
                        break;
                    //手指移动屏幕
                    case MotionEvent.ACTION_MOVE:
                        mEndX = motionEvent.getX();
                        mEndY = motionEvent.getY();
                        if (Math.abs(mEndY - mStartY) > 5 || Math.abs(mEndX - mStartX) > 5) {
                            if (mActionType2 == MAPACTIONTYPE2.Action_MeasereXY) {
                                double x = motionEvent.getX();
                                double y = motionEvent.getY();
                                Point point = new Point((int) x, (int) y);
                                EventBus.getDefault().post(point);
                            }
                        }
                        break;
                    //手指弹起
                    case MotionEvent.ACTION_UP:
                        //如果点击屏幕过快，地图放大
                        if (DateTimeUtil.isFastDoubleClick(mLastTime)) {
                            mMapCtrl.getMap().zoom(3.0);
                            return false;
                        }
                        mEndX = motionEvent.getX();
                        mEndY = motionEvent.getY();
                        if (Math.abs(mEndY - mStartY) < 7 && Math.abs(mEndX - mStartX) < 7) {
                            switch (mActionType2) {
                                //添加点与查询点同个APP动作
                                case Action_CreatePoint: {
                                    //查询记录集
                                    long startTime = System.currentTimeMillis();
                                    LogUtills.i("Time1 = ", startTime + "");
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    //如果记录集为空，创建管点
                                    if (_result == null || _result.isEmpty()) {
                                        //赋值上次时间点
                                        mLastTime = System.currentTimeMillis();
                                        //弹出加点属性界面
                                        DrawPointFragment _fragment = new DrawPointFragment();
                                        Point _pt = new Point();
                                        _pt.setX((int) motionEvent.getX());
                                        _pt.setY((int) motionEvent.getY());
                                        Point2D _reP = mMapCtrl.getMap().pixelToMap(_pt);
                                        //bundle 传对象
                                        Bundle _bundle = new Bundle();
                                        _bundle.putString("gpType", mCurrentLayerName);
                                        _bundle.putDouble("x", _reP.getX());
                                        _bundle.putDouble("y", _reP.getY());
                                        _bundle.putInt("smId", -1);
                                        _fragment.setArguments(_bundle);
                                        _fragment.show(mContext.getSupportFragmentManager().beginTransaction(), "point");
                                        long endTime = System.currentTimeMillis() - startTime;
                                        LogUtills.i("Time1 = ", endTime + "");
                                    } else {
                                        //查询点 点编辑
                                        mPointSmid = _result.getID();
                                        //设置选中状态并返回点bean
                                        BaseFieldPInfos _infosP = setPtSelectionHighLigh(_result);
                                        //设置地图界面图层样式
//                                        setSplayerType(_infosP);
                                        //弹出点查询属性界面
                                        QueryPointFragment _fragment = new QueryPointFragment();
                                        Bundle _bundle = new Bundle();
                                        _bundle.putString("gpType", _infosP.pipeType);
                                        _bundle.putInt("smId", mPointSmid);
                                        _bundle.putParcelable("info", _infosP);
                                        _fragment.setArguments(_bundle);
                                        _fragment.show(mContext.getSupportFragmentManager().beginTransaction(), "queryPoint");
                                        Selection selection = getPtThemeLayerByName2().getSelection();
                                        selection.clear();
                                        if (_result != null) {
                                            _result.close();
                                            _result.dispose();
                                        }
                                    }
                                }

                                break;

                                //仅仅加点
                                case Action_AddPoint: {
                                    //赋值上次时间点
                                    mLastTime = System.currentTimeMillis();
                                    //弹出加点属性界面
                                    DrawPointFragment _fragment = new DrawPointFragment();
                                    Point _pt = new Point();
                                    _pt.setX((int) motionEvent.getX());
                                    _pt.setY((int) motionEvent.getY());
                                    Point2D _reP = mMapCtrl.getMap().pixelToMap(_pt);
                                    //bundle 传对象
                                    Bundle _bundle = new Bundle();
                                    _bundle.putString("gpType", mCurrentLayerName);
                                    _bundle.putDouble("x", _reP.getX());
                                    _bundle.putDouble("y", _reP.getY());
                                    _bundle.putInt("smId", -1);
                                    _fragment.setArguments(_bundle);
                                    _fragment.show(mContext.getSupportFragmentManager().beginTransaction(), "point");
                                }
                                break;

                                //仅仅查询
                                case Action_QueryPoint: {
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    if (!_result.isEmpty()) {
                                        //查询点 点编辑
                                        mPointSmid = _result.getID();
                                        //设置选中状态并返回点bean
                                        BaseFieldPInfos _infosP = setPtSelectionHighLigh(_result);
                                        //设置地图界面图层样式
//                                        setSplayerType(_infosP);
                                        //弹出点查询属性界面
                                        QueryPointFragment _fragment = new QueryPointFragment();
                                        Bundle _bundle = new Bundle();
                                        _bundle.putString("gpType", _infosP.pipeType);
                                        _bundle.putInt("smId", mPointSmid);
                                        _bundle.putParcelable("info", _infosP);
                                        _fragment.setArguments(_bundle);
                                        _fragment.show(mContext.getSupportFragmentManager().beginTransaction(), "queryPoint");
                                        Selection selection = getPtThemeLayerByName2().getSelection();
                                        selection.clear();
                                        if (_result != null) {
                                            _result.close();
                                            _result.dispose();
                                        }
                                    } else {
                                        ToastyUtil.showInfoShort(mContext, "请继续选择");
                                    }
                                }
                                break;

                                //创建第一个点 是否选择点  是 加线  否则 查询线
                                case Action_CreateLineStartPoint: {
                                    //查询是否选中点并返回点记录集
                                    Recordset _resultPoint = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    //没有查询到点 继续查询线
                                    if (_resultPoint.isEmpty() || _resultPoint == null) {
                                        //查询是否选中线并返回线记录集
                                        Recordset _resultLine = queryLineByMouseXMouseY2(motionEvent.getX(), motionEvent.getY());
                                        //如果选中线
                                        if (!_resultLine.isEmpty()) {
                                            //每次查询到线，把之前的记录clear掉，保持一条记录
                                            mSmIDsL.clear();
                                            mSmIDsL.add(_resultLine.getID());
                                            //把线记录集移到第一条记录
                                            _resultLine.moveFirst();
                                            //获取线的SMID
                                            int _smid = _resultLine.getID();
                                            //设置线选中样式并且返回线bean
                                            BaseFieldLInfos _infoL = setLrSelectionHighLigh(_resultLine);
                                            if (_infoL == null) {
                                                LogUtills.e("Qurey Line Property Faild " + _resultLine.getID());
                                            }
                                            //设置地图界面图层样式
//                                            setSplayerType(_infoL);
                                            //弹出线查询属性界面
                                            QueryLineFragment _fragment = new QueryLineFragment();
                                            Bundle bundle = new Bundle();
                                            //线对象
                                            bundle.putParcelable("_infoL", _infoL);
                                            //线smid
                                            bundle.putInt("smId", _smid);
                                            //线类型
                                            bundle.putString("gpType", _infoL.pipeType);
                                            //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                                            bundle.putInt("actionType", 1);
                                            //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                                            bundle.putString("exp_Num", "0");
                                            //点smid
                                            bundle.putInt("SmIDP", -1);
                                            _fragment.setArguments(bundle);
                                            _fragment.show(mContext.getSupportFragmentManager().beginTransaction(), "queryline");
                                            //关闭释放线记录集
                                            _resultLine.close();
                                            _resultLine.dispose();
                                        } else {
                                            ToastyUtil.showInfoShort(mContext, "没有查询到对象");
                                        }
                                    } else {
                                        //查询到点
                                        String _startPId = _resultPoint.getString("exp_Num");
                                        //判断起点是否是临时点，临时点不能作为起点
                                        if (_startPId.contains("T_")) {
                                            ToastyUtil.showWarningShort(mContext, "临时点不能作为起始点");
                                            LogUtills.w("Temp Point Can Not As Line Start Point");
                                            return false;
                                        }
                                        //设置管点选中样式并且返回点bean
                                        BaseFieldPInfos _pInfos = setPtSelectionHighLigh(_resultPoint);
                                        mSmIDs.add(_resultPoint.getID());
                                        _resultPoint.close();
                                        _resultPoint.dispose();
                                        ToastyUtil.showInfoShort(mContext, "选择中点：" + _startPId);
                                        //设置地图界面管类Spinner图层选中样式
                                        setSplayerType(_pInfos);
                                        //进入创建线第二个点的状态
                                        mActionType2 = MAPACTIONTYPE2.Action_CreateLineDirectPoint;
                                    }
                                }
                                break;

                                //选择第二个点 并且连线
                                case Action_CreateLineDirectPoint: {
                                    //选择第2个点，查询范围扩大，避免选择不到点
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    //第二个点未选中 创建临时点
                                    if (_result.isEmpty()) {
                                        //创建临时点
                                        Point _pt = new Point();
                                        _pt.setX((int) motionEvent.getX());
                                        _pt.setY((int) motionEvent.getY());
                                        Point2D _reP = mMapCtrl.getMap().pixelToMap(_pt);
                                        String code = mCurrentLayerName.substring(mCurrentLayerName.indexOf("-") + 1);
                                        //创建临时点
                                        BaseFieldPInfos _temEndPt = PointFieldFactory.createTempInfo(_reP, mCurrentLayerName, code);
                                        _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                        //设置点选中高亮显示
                                        setPtSelectionHighLigh(_result);
                                        //弹出加线的属性界面
                                        DrawLineFragment _fragmentLr = new DrawLineFragment();
                                        Bundle _bundle = new Bundle();
                                        _bundle.putInt("beginSmId", mSmIDs.get(0));
                                        _bundle.putInt("endSmId", _result.getID());
                                        _bundle.putString("gpType", mCurrentLayerName);
                                        _fragmentLr.setArguments(_bundle);
                                        _fragmentLr.show(mContext.getSupportFragmentManager().beginTransaction(), "line");
                                        //改变动作
                                        setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);
                                    } else {
                                        //选中点则连线
                                        Selection _selection = getPtThemeLayerByName2().getSelection();
                                        //第二个点的smid
                                        int _ptSmid = _result.getID();
                                        if (_ptSmid == mSmIDs.get(0)) {
                                            ToastyUtil.showWarningShort(mContext, "起始点与方向点相同，请重新选择方向点");
                                            return false;
                                        }
                                        //依据smid查询点记录集
                                        Recordset _startPt = queryRecordsetBySmid(mSmIDs.get(0), true, false);
                                        if (_startPt == null) {
                                            return false;
                                        }
                                        //重线判断  在起点和终点之间不能有线相连
                                        String _duplicateLineCheck = "(benExpNum = '" + _startPt.getString("exp_Num") + "' and endExpNum = '" + _result.getString("exp_Num") + "') or " +
                                                "(endExpNum = '" + _startPt.getString("exp_Num") + "' and benExpNum = '" + _result.getString("exp_Num") + "')";
                                        Recordset _duplicateLine = QueryRecordsetBySql(_duplicateLineCheck, false, false);
                                        if (!_duplicateLine.isEmpty()) {
                                            ToastyUtil.showWarningShort(mContext, "不允许重复连接，请重新选择方向点");
                                            return false;
                                        }
                                        //设置点高亮显示
                                        setPtSelectionHighLigh(_result);
                                        //弹出加线属性界面
                                        DrawLineFragment _fragmentLr = new DrawLineFragment();
                                        Bundle _bundle = new Bundle();
                                        _bundle.putInt("beginSmId", _startPt.getID());
                                        _bundle.putInt("endSmId", _result.getID());
                                        _bundle.putString("gpType", mCurrentLayerName);
                                        _fragmentLr.setArguments(_bundle);
                                        _fragmentLr.show(mContext.getSupportFragmentManager().beginTransaction(), "line");
                                        //修改动作 变为加线
                                        setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);
                                    }
                                    if (_result != null) {
                                        _result.close();
                                        _result.dispose();
                                    }
                                }
                                break;

                                //仅仅加线
                                case Action_AddLine: {
                                    //查询是否选中点并返回点记录集
                                    Recordset _resultPoint = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    //没有查询到点 继续查询线
                                    if (!_resultPoint.isEmpty()) {
                                        //查询到点
                                        String _startPId = _resultPoint.getString("exp_Num");
                                        //判断起点是否是临时点，临时点不能作为起点
                                        if (_startPId.contains("T_")) {
                                            ToastyUtil.showWarningShort(mContext, "临时点不能作为起始点");
                                            LogUtills.w("Temp Point Can Not As Line Start Point");
                                            return false;
                                        }
                                        //设置管点选中样式并且返回点bean
                                        BaseFieldPInfos _pInfos = setPtSelectionHighLigh(_resultPoint);
                                        mSmIDs.add(_resultPoint.getID());
                                        _resultPoint.close();
                                        _resultPoint.dispose();
                                        ToastyUtil.showInfoShort(mContext, "选择中点：" + _startPId);
                                        //设置地图界面管类Spinner图层选中样式
                                        setSplayerType(_pInfos);
                                        //进入创建线第二个点的状态
                                        mActionType2 = MAPACTIONTYPE2.Action_CreateLineDirectPoint;
                                    } else {
                                        ToastyUtil.showInfoShort(mContext, "没有选择点对象");
                                    }
                                }
                                break;

                                //仅仅编辑线
                                case Action_QueryLine: {
                                    //查询是否选中线并返回线记录集
                                    Recordset _resultLine = queryLineByMouseXMouseY2(motionEvent.getX(), motionEvent.getY());
                                    //如果选中线
                                    if (!_resultLine.isEmpty()) {
                                        //每次查询到线，把之前的记录clear掉，保持一条记录
                                        mSmIDsL.clear();
                                        mSmIDsL.add(_resultLine.getID());
                                        //把线记录集移到第一条记录
                                        _resultLine.moveFirst();
                                        //获取线的SMID
                                        int _smid = _resultLine.getID();
                                        //设置线选中样式并且返回线bean
                                        BaseFieldLInfos _infoL = setLrSelectionHighLigh(_resultLine);
                                        if (_infoL == null) {
                                            LogUtills.e("Qurey Line Property Faild " + _resultLine.getID());
                                        }
                                        //设置地图界面图层样式
//                                        setSplayerType(_infoL);
                                        //弹出线查询属性界面
                                        QueryLineFragment _fragment = new QueryLineFragment();
                                        Bundle bundle = new Bundle();
                                        //线对象
                                        bundle.putParcelable("_infoL", _infoL);
                                        //线smid
                                        bundle.putInt("smId", _smid);
                                        //线类型
                                        bundle.putString("gpType", _infoL.pipeType);
                                        //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                                        bundle.putInt("actionType", 1);
                                        //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                                        bundle.putString("exp_Num", "0");
                                        //点smid
                                        bundle.putInt("SmIDP", -1);
                                        _fragment.setArguments(bundle);
                                        _fragment.show(mContext.getSupportFragmentManager().beginTransaction(), "queryline");
                                        //关闭释放线记录集
                                        _resultLine.close();
                                        _resultLine.dispose();
                                    } else {
                                        ToastyUtil.showInfoShort(mContext, "没有查询到对象");
                                    }
                                }
                                break;

                                //线中加点
                                case Action_AddPointInLine_FindLine: {
                                    //查询线记录集
                                    Recordset _result = queryLineByMouseXMouseY2(motionEvent.getX(), motionEvent.getY());
                                    if (_result == null || _result.isEmpty()) {
                                        ToastyUtil.showInfoShort(mContext, "请选择要添加的点的线");
                                        LogUtills.d("Add Point In Line Query Line Faild...");
                                        return false;
                                    }
                                    //设置线高亮显示并返回线bean
                                    BaseFieldLInfos _infos = setLrSelectionHighLigh(_result);
                                    //设置地图界面图层
                                    setSplayerType(_infos);
                                    _result.moveFirst();
                                    mAddPtOfLineSmID = _result.getID();
                                    _result.close();
                                    _result.dispose();
                                    ToastyUtil.showSuccessShort(mContext, "获取管线成功...");
                                    //修改动作
                                    mActionType2 = MAPACTIONTYPE2.Action_AddPointInLine_FindPoint;
                                }
                                break;

                                //加点
                                case Action_AddPointInLine_FindPoint: {
                                    //查询线，如果为空，则线中加点
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    if (mAddPtOfLineSmID == -1) {
                                        LogUtills.e("Action AddPointInLine Find Line Faild...");
                                        return false;
                                    }
                                    BaseFieldPInfos _cenPInfo = null;
                                    if (!_result.isEmpty()) {
                                        ToastyUtil.showWarningShort(mContext, "不能选择已有点点号，请重新选择");
                                    } else {
                                        //没有选中，则创建临点，临时线
                                        Point _pt = new Point();
                                        _pt.setX((int) motionEvent.getX());
                                        _pt.setY((int) motionEvent.getY());
                                        Point2D _reP = mMapCtrl.getMap().pixelToMap(_pt);
                                        //线中加点面板
                                        DrawPointInLineFragment _fragment = new DrawPointInLineFragment();
                                        Bundle _bundle = new Bundle();
                                        _bundle.putDouble("y", _reP.getY());
                                        _bundle.putDouble("x", _reP.getX());
                                        _bundle.putString("gpType", mCurrentLayerName);
                                        _bundle.putInt("smId", mAddPtOfLineSmID);
                                        _fragment.setArguments(_bundle);
                                        _fragment.show(mContext.getSupportFragmentManager().beginTransaction(), "addpointtoline");
                                        mAddPtOfLineSmID = -1;
                                    }
                                    //重置线中加点动作
                                    setMapActionType(MAPACTIONTYPE2.Action_AddPointInLine_FindLine);
                                }
                                break;

                                //移动管点位置
                                case Action_EditPointLocation: {
                                    //当前位置
                                    Point _pt = new Point();
                                    _pt.setX((int) motionEvent.getX());
                                    _pt.setY((int) motionEvent.getY());
                                    Point2D _reP = mMapCtrl.getMap().pixelToMap(_pt);
                                    //查询记录集
                                    Recordset _sReset = queryRecordsetBySmid(mSmIDs.get(0), true, true);
                                    if (_sReset == null || _sReset.isEmpty()) {
                                        LogUtills.e("Edit Point Location Faild,ID=" + mSmIDs.get(0));
                                        return false;
                                    }

                                    //移动的点后位置的点
                                    BaseFieldPInfos _pSInfo = BaseFieldPInfos.createFieldInfo(_sReset);
                                    _pSInfo.longitude = _reP.getX();
                                    _pSInfo.latitude = _reP.getY();
                                    //一个点更新可用
                                    if (!editRecords(_pSInfo)) {
                                        ToastyUtil.showErrorShort(mContext, "移动点位置失败，很可能是选中的点出现了问题...");
                                        _sReset.close();
                                        _sReset.dispose();
                                        return false;
                                    }

                                    //更新为起始点为该点号的线
                                    String _query = "benExpNum = '" + _pSInfo.exp_Num + "'";
                                    //查询线记录集
                                    Recordset _sLResets = QueryRecordsetBySql(_query, false, true);
                                    _sLResets.moveFirst();
                                    //多个点更新，少去遍历属性，看能否提高效率;
                                    while (!_sLResets.isEOF()) {
                                        Point2Ds _2ds = new Point2Ds();
                                        //点的位置
                                        _2ds.add(_reP);
                                        //更新线的坐标
                                        _2ds.add(new Point2D(_sLResets.getDouble("endLongitude"), _sLResets.getDouble("endLatitude")));
                                        _sLResets.edit();
                                        _sLResets.setGeometry(new GeoLine(new Point2Ds(_2ds)));
                                        if (!_sLResets.update()) {
                                            ToastyUtil.showErrorShort(mContext, "更新线位置失败，读入数据集出错， Line ID=" + _sLResets.getID());
                                            return false;
                                        }
                                        _sLResets.edit();
                                        _sLResets.setString("benExpNum", _pSInfo.exp_Num);
                                        _sLResets.setDouble("startLongitude", _reP.getX());
                                        _sLResets.setDouble("startLatitude", _reP.getY());
                                        //改变线长度
                                        _sLResets.setString("pipeLength", String.format("%.2f", _sLResets.getDouble("SmLength")));
                                        if (!_sLResets.update()) {
                                            ToastyUtil.showErrorShort(mContext, "更新线长度失败， Line ID=" + _sLResets.getID());
                                        }
                                        _sLResets.moveNext();
                                    }

                                    //更新终点
                                    _query = " endExpNum = '" + _pSInfo.exp_Num + "'";
                                    _sLResets = QueryRecordsetBySql(_query, false, true);
                                    _sLResets.moveFirst();
                                    while (!_sLResets.isEOF()) {
                                        Point2Ds _2ds = new Point2Ds();
                                        _2ds.add(new Point2D(_sLResets.getDouble("startLongitude"), _sLResets.getDouble("startLatitude")));
                                        _2ds.add(_reP);
                                        _sLResets.edit();
                                        if (!_sLResets.update()) {
                                            ToastyUtil.showErrorShort(mContext, "更新线位置失败，读入数据集出错， Line ID=" + _sLResets.getID());
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
                                            ToastyUtil.showErrorShort(mContext, "更新线长度失败， Line ID=" + _sLResets.getID());
                                        }
                                        _sLResets.moveNext();
                                    }
                                    ToastyUtil.showSuccessShort(mContext, "更新线位置成功，点号:" + _pSInfo.exp_Num);
                                    setMapActionType(MAPACTIONTYPE2.Action_CreatePoint);
                                    setPtSelectionHighLigh(_sReset);
                                    Selection _selection = getPtThemeLayerByName2().getSelection();
                                    _selection.clear();
                                    if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                                        OperSql.getSingleton().inserPoint(_pSInfo.exp_Num, "移动管点");
                                    }
                                    //关闭释放数据集
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
                                        ToastyUtil.showInfoShort(mContext, "没有选择点，请重新选择");
                                        return false;
                                    }
                                    //查询点的编号
                                    String _exp_Num = _result.getString("exp_Num");
                                    Recordset _resetLine = queryRecordsetBySmid(mSmIDsL.get(0), false, false);
                                    String _benExpNum = _resetLine.getString("benExpNum");
                                    String _endExpNum = _resetLine.getString("endExpNum");

                                    //判断新获取的起点是否是原来的起点
                                    if (_exp_Num.equals(_benExpNum)) {
                                        ToastyUtil.showWarningShort(mContext, "新获取起点和原来的点一样，请重新选择");
                                        return false;
                                    }

                                    //判断新获取的起点是否是原来线的终点d
                                    if (_exp_Num.equals(_endExpNum)) {
                                        ToastyUtil.showWarningShort(mContext, "新获取起点和原来线的终点一样，请重新选择");
                                        return false;
                                    }

                                    //判断重新选择的起点和终点是否已经有线
                                    String sql = "(benExpNum = '" + _exp_Num + "' and endExpNum = '" + _endExpNum + "') or (benExpNum = '" + _endExpNum + "' and endExpNum = '" + _exp_Num + "')";
                                    Recordset _recordset = DataHandlerObserver.ins().QueryRecordsetBySql(sql, false, false);
                                    if (!_recordset.isEmpty()) {
                                        ToastyUtil.showWarningShort(mContext, "两点已经有线了，不可重线");
                                        return false;
                                    }
                                    //设置高亮
                                    BaseFieldLInfos _infoL = setLrSelectionHighLigh(queryRecordsetBySmid(mSmIDsL.get(0), false, false));
                                    //跳转
                                    QueryLineFragment _fragment = new QueryLineFragment();
                                    Bundle bundle = new Bundle();
                                    //线对象
                                    bundle.putParcelable("_infoL", _infoL);
                                    //线smid
                                    bundle.putInt("smId", mSmIDsL.get(mSmIDsL.size() - 1));
                                    //线类型
                                    bundle.putString("gpType", mCurrentLayerName);
                                    //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                                    bundle.putInt("actionType", 2);
                                    //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                                    bundle.putString("exp_Num", _exp_Num);
                                    //点smid
                                    bundle.putInt("SmIDP", _result.getID());
                                    _fragment.setArguments(bundle);
                                    _fragment.show(mContext.getSupportFragmentManager().beginTransaction(), "queryline");
                                    if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                                        OperSql.getSingleton().inserLine(_infoL.benExpNum, _infoL.endExpNum, mSmIDsL.get(mSmIDsL.size() - 1), "重新获取起点" + _exp_Num + "->" + _infoL.endExpNum);
                                    }
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
                                        ToastyUtil.showWarningShort(mContext, "没有选择点，请重新选择");
                                        return false;
                                    }
                                    //查询点的编号
                                    String _exp_Num = _result.getString("exp_Num");
                                    Recordset _resetLine = queryRecordsetBySmid(mSmIDsL.get(0), false, false);
                                    String _benExpNum = _resetLine.getString("benExpNum");
                                    String _endExpNum = _resetLine.getString("endExpNum");
                                    //判断新获取的起点是否是原来的起点
                                    if (_exp_Num.equals(_benExpNum)) {
                                        ToastyUtil.showWarningShort(mContext, "新获取终点和原来线的起点一样，请重新选择");
                                        return false;
                                    }

                                    //判断新获取的起点是否是原来线的终点d
                                    if (_exp_Num.equals(_endExpNum)) {
                                        ToastyUtil.showWarningShort(mContext, "新获取终点点和原来线的终点一样，请重新选择");
                                        return false;
                                    }

                                    //判断重新选择的起点和终点是否已经有线
                                    String sql = "(benExpNum = '" + _exp_Num + "' and endExpNum = '" + _benExpNum + "') or (benExpNum = '" + _benExpNum + "' and endExpNum = '" + _exp_Num + "')";
                                    Recordset _recordset = DataHandlerObserver.ins().QueryRecordsetBySql(sql, false, false);
                                    if (!_recordset.isEmpty()) {
                                        ToastyUtil.showWarningShort(mContext, "两点已经有线了，不可重线");
                                        return false;
                                    }
                                    BaseFieldLInfos _infoL = setLrSelectionHighLigh(queryRecordsetBySmid(mSmIDsL.get(0), false, false));
                                    //跳转
                                    QueryLineFragment _fragment = new QueryLineFragment();
                                    Bundle bundle = new Bundle();
                                    //线对象
                                    bundle.putParcelable("_infoL", _infoL);
                                    //线smid
                                    bundle.putInt("smId", mSmIDsL.get(mSmIDsL.size() - 1));
                                    //线类型
                                    bundle.putString("gpType", mCurrentLayerName);
                                    //类型  1 代表正常查询线   2代表重新获取起点 3代表重新获取终点
                                    bundle.putInt("actionType", 3);
                                    //点号 用来区分 重新获取起点 终点  传送过去的点号编号
                                    bundle.putString("exp_Num", _exp_Num);
                                    //点smid
                                    bundle.putInt("SmIDP", _result.getID());
                                    _fragment.setArguments(bundle);
                                    _fragment.show(mContext.getSupportFragmentManager().beginTransaction(), "queryline");
                                    if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                                        OperSql.getSingleton().inserLine(_infoL.benExpNum, _infoL.endExpNum, mSmIDsL.get(mSmIDsL.size() - 1), "重新获取终点" + _infoL.benExpNum + "->" + _exp_Num);
                                    }
                                    _result.close();
                                    _result.dispose();
                                }
                                setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);
                                break;

                                //测量收点
                                //  收点的同时，设置线起点终点是否已经测了，用来统计已测管线工作量，在线中加入两个字段 起点测量  终点测量
                                case Action_MeasurePoint: {
                                    Recordset _result = queryPointByMouseXMouseY3(motionEvent.getX(), motionEvent.getY());
                                    if (_result.isEmpty()) {
                                        LogUtills.i("measure point not select point");
                                        return false;
                                    }
                                    BaseFieldPInfos infos = setPtSelectionHighLigh(_result);
                                    if (infos.subsid.equals("临时点")) {
                                        ToastyUtil.showWarningShort(mContext, "临时点不用测量");
                                        return false;
                                    }

                                    Recordset _reSet = QueryRecordsetMesureBySql("exp_Num = '" + infos.exp_Num + "'", true);
                                    if (!_reSet.isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setTitle("删除提示")
                                                .setMessage("是否取消已经测量好的点号" + infos.exp_Num + "?")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        _reSet.delete();
                                                        //取消测量收点  改变线已经测量过的标记 0未测量  1已测量
                                                        //查询点数据集，是否已经测量
                                                        Recordset recordset = queryRecordsetByExpNum(infos.exp_Num, true);
                                                        updatePointMeasureType(recordset, "0");
                                                        //查询起点和编号连接的点
                                                        Recordset lineSetStart = QueryRecordsetBySql("benExpNum ='" + infos.exp_Num + "'", false, true);
                                                        updateLineMeasureType(lineSetStart, "0", true);

                                                        //查询终点和编号连接的点
                                                        Recordset lineSetEnd = QueryRecordsetBySql("endExpNum ='" + infos.exp_Num + "'", false, true);
                                                        updateLineMeasureType(lineSetEnd, "0", false);

                                                        lineSetStart.close();
                                                        lineSetEnd.close();
                                                        _reSet.close();
                                                        _reSet.dispose();
                                                        lineSetStart.dispose();
                                                        lineSetEnd.dispose();
                                                        mMapCtrl.getMap().refresh();
                                                    }
                                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        builder.create().show();

                                        return false;
                                    }

                                    Point _pt = new Point();
                                    _pt.setX((int) motionEvent.getX());
                                    _pt.setY((int) motionEvent.getY());
                                    Point2D _reP = mMapCtrl.getMap().pixelToMap(_pt);
                                    BaseFieldPInfos _info = PointFieldFactory.Create(SuperMapConfig.Layer_Measure);
                                    _info.symbolExpression = "测量收点";
                                    _info.exp_Num = infos.exp_Num;
                                    _info.longitude = infos.longitude;
                                    _info.latitude = infos.latitude;
                                    _info.exp_Date = DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT);
                                    _info.serialNum = infos.serialNum;
                                    if (!createRecords(_info)) {
                                        LogUtills.i("create measure point fail");
                                    } else {
                                        //测量成功后，标记线的起点或者终点已经测量
                                        Recordset recordset = queryRecordsetByExpNum(infos.exp_Num, true);
                                        updatePointMeasureType(recordset, "1");
                                        //查询起点和编号连接的点
                                        Recordset lineSetStart = QueryRecordsetBySql("benExpNum ='" + infos.exp_Num + "'", false, true);
                                        updateLineMeasureType(lineSetStart, "1", true);
                                        //查询终点和编号连接的点
                                        Recordset lineSetEnd = QueryRecordsetBySql("endExpNum ='" + infos.exp_Num + "'", false, true);
                                        updateLineMeasureType(lineSetEnd, "1", false);
                                        lineSetStart.close();
                                        lineSetEnd.close();
                                        _reSet.close();
                                        _reSet.dispose();
                                        lineSetStart.dispose();
                                        lineSetEnd.dispose();
                                    }
                                    mMapCtrl.getMap().refresh();
                                    if (_result != null) {
                                        _result.close();
                                        _result.dispose();
                                    }
                                }
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
     * 测量收点，更新线的测量时间点
     *
     * @param recordSet
     */
    private void updateLineMeasureType(Recordset recordSet, String statue, Boolean isStart) {
        if (!recordSet.isEmpty()) {
            recordSet.moveFirst();
            while (!recordSet.isEOF()) {
                recordSet.edit();
                if (isStart) {
                    recordSet.setString("measureStart", statue);
                } else {
                    recordSet.setString("measureEnd", statue);
                }
                recordSet.setString("measureDate", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));

                if (recordSet.update()) {
                    LogUtills.i("measurepoint line update success");
                }
                recordSet.moveNext();
            }
        }
    }

    /**
     * 测量收点，更新点的测量时间点
     *
     * @param recordSet
     */
    private void updatePointMeasureType(Recordset recordSet, String statue) {
        if (!recordSet.isEmpty()) {
            recordSet.edit();
            recordSet.setString("MeasuerPoint", statue);
            if (!recordSet.update()) {
                ToastyUtil.showErrorShort(mContext, "测量点号失败");
            }
        }
    }

    /**
     * 改变地图界面图层
     *
     * @Author HaiRun
     * @Time 2019/4/23 . 9: 30
     */
    private void setSplayerType(BaseFieldInfos infos) {
        if (!infos.pipeType.equals(mCurrentLayerName)) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(MapActivity.spLayers, infos.pipeType);
            mCurrentLayerName = infos.pipeType;
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
     * 通过sql语句查询测量收点记录集
     *
     * @param sqlStr
     * @param isEdit
     * @return
     */
    public Recordset QueryRecordsetMesureBySql(String sqlStr, boolean isEdit) {
        Datasource _datasource = mMapCtrl.getMap().getWorkspace().getDatasources().get(0);
        //获取测量收点数据集
        DatasetVector datasetVector = (DatasetVector) _datasource.getDatasets().get("P_" + SuperMapConfig.Layer_Measure);
        if (datasetVector == null) {
            return null;
        }
        //将查询条件传入到Query方法，查询记录集
        return datasetVector.query(sqlStr, isEdit ? CursorType.DYNAMIC : CursorType.STATIC);
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
     *
     * @param sql
     * @param depth
     * @return
     */
    public boolean updateRecordSetBySql(String sql, String depth) {
        DatasetVector _dv = (DatasetVector) getTotalPtLayer().getDataset();
        Recordset _recordset = _dv.query(sql, CursorType.DYNAMIC);
        if (_recordset.isEmpty()) {
            return true;
        }
        _recordset.moveFirst();
        _recordset.edit();
        _recordset.setString("depth", depth);
        return _recordset.update();
    }

    /**
     * 设置点高亮
     *
     * @param result
     * @return
     */
    public BaseFieldPInfos setPtSelectionHighLigh(Recordset result) {
        BaseFieldPInfos _infosP = BaseFieldPInfos.createFieldInfo(result);
        LogUtills.i("poitn", _infosP.toString());
        //设置选中状态
        Selection _selection = getPtThemeLayerByName2().getSelection();
        boolean isOk = _selection.fromRecordset(result);
        if (!isOk) {
            LogUtills.w("Set The Point Seletion Status faild...");
        }
        GeoStyle _style = _selection.getStyle();
        if (String.valueOf(_infosP.symbolID).length() == 0) {
            _style.setMarkerSymbolID(1);
            _style.setMarkerSize(new Size2D(2.5, 2.5));
        } else {
            _style.setMarkerSize(new Size2D(_infosP.symbolSizeX * 1.3, _infosP.symbolSizeY * 1.3));
            _style.setMarkerSymbolID(_infosP.symbolID);
        }
        _style.setLineColor(new Color(255, 0, 0));
        _style.setLineWidth(0.5);
        _selection.setStyle(_style);
        mMapCtrl.getMap().refresh();
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
        mMapCtrl.getMap().refresh();
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

        //更新线，有可能起点终点更换了位置，所以哟啊重新设置起点终点坐标
        Point2Ds _p2ds = new Point2Ds();
        _p2ds.add(new Point2D(pInfos.startLongitude, pInfos.startLatitude));
        _p2ds.add(new Point2D(pInfos.endLongitude, pInfos.endLatitude));
        if (!_result.setGeometry(new GeoLine(_p2ds))) {
            ToastyUtil.showErrorShort(mContext, "线更改失败");
            return false;
        }
        if (!_result.update()) {
            ToastyUtil.showErrorShort(mContext, "线更加失败");
            return false;
        }
        //更改线长度
        _result.edit();
        _result.setString("pipeLength", String.format("%.2f", _result.getDouble("SmLength")));
        if (!_result.update()) {
            ToastyUtil.showErrorShort(mContext, "线长度更新失败");
            return false;
        }
        //关闭记录集，释放几何对象、记录集
        _result.close();
        _result.dispose();
        return true;
    }

    /**
     * 设置动作
     *
     * @param action
     * @return
     */
    public boolean setAction(Action action) {
        mMapCtrl.setAction(action);
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
        Layer _temp = mMapCtrl.getMap().getLayers().get(_p_name);
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
        Layer _temp = mMapCtrl.getMap().getLayers().get(_p_name);
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
        Layer _temp = mMapCtrl.getMap().getLayers().get(_p_name);
        if (_temp == null) {
            LogUtills.w("Query Line Layer " + _p_name + "Is  Null...");
            return null;
        }
        return _temp;
    }

    /**
     * 获取Ps线图层
     *
     * @return
     */
    public DatasetVector getPsLrDatasetVector() {
        DatasetVector datasetVector = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources()
                .get(0).getDatasets().get("L_" + SuperMapConfig.Layer_PS);
        if (datasetVector == null){
            LogUtills.e("Query DatasetVector " + SuperMapConfig.Layer_PS + "Is  Null...");
            return null;
        }
        return datasetVector;
    }

    /**
     * 获取线单值专题图图层
     *
     * @return
     */
    public Layer getTotalLrThemeLayer() {
        String _p_name = "L_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#2";
        Layer _temp = mMapCtrl.getMap().getLayers().get(_p_name);
        if (_temp == null) {
            LogUtills.w("Query Line Layer " + _p_name + "Is  Null...");
            return null;
        }
        return _temp;
    }

    public Layer getPtThemeLayerByName2() {
        //标签专题图图层名字
        String _targetName = "P_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#2";
        Layer _temp = mMapCtrl.getMap().getLayers().get(_targetName);
        if (_temp == null) {
            LogUtills.w("Get The Theme Layer " + _targetName + " Layer Faild");
            return null;
        }
        return _temp;
    }

    /**
     * 创建测量记录集
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
     * @return boolean
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
        //标签专题图颜色显示字段
        _result.setDouble("rangeExpression", PipeThemelabelUtil.Ins().getThemeItemValue(pInfos.pipeType.substring(0, 2)));
        //符号显示 格式 Code + 附属物或者点特征
//        String str = (pInfos.subsid.trim().length() == 0) ? pInfos.feature : pInfos.subsid;
        String str = SymbolInfo.Ins().getSymbol(pInfos.pipeType, pInfos.subsid, pInfos.feature);
        LogUtills.i("symbolExpression", str);
        str = (str.trim().length() == 0) ? "探测点" : str;
        _result.setString("symbolExpression", pInfos.code + "-" + str);
        LogUtills.i("symbolExpression", pInfos.code + "-" + str + "=========" + pInfos.pipeType.substring(0, 2) + PipeThemelabelUtil.Ins().getThemeItemValue(pInfos.pipeType.substring(0, 2)));
        boolean isOk = _result.update();
        if (_result != null) {
            _result.close();
            _result.dispose();
        }
        return isOk;
    }

    /**
     * 创建线记录集
     *
     * @param pInfos
     * @return boolean
     */
    public boolean addRecords(BaseFieldLInfos pInfos) {
        if (pInfos == null) {
            LogUtills.e("Create The Line Find The BaseFieldLInfos Is Null...");
            return false;
        }
        Layer _pLayer = getTotalLrLayer();
        if (_pLayer == null) {
            return false;
        }
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
//        String _labelTag = String.valueOf(_result.getID()) + pInfos.labelTag;
//        _result.setString("labelTag", _labelTag);
        //标签专题图颜色显示字段
        _result.setDouble("rangeExpression", PipeThemelabelUtil.Ins().getThemeItemValue(pInfos.pipeType.substring(0, 2)));
        //标签专题图字体显示字段
        String ds = (pInfos.pipeSize.trim().length() != 0) ? pInfos.pipeSize : pInfos.d_S;
        _result.setString("labelTag", pInfos.pipeType.substring(pInfos.pipeType.lastIndexOf("-") + 1) + "-" + ds + "-" + pInfos.material);
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
     * 通过 编号查询记录集
     *
     * @param sql
     * @param isEdit
     * @return 范围查询
     */
    public Recordset queryRecordsetBySql(String sql, boolean isEdit) {
        QueryParameter _parameter = new QueryParameter();
        _parameter.setAttributeFilter(sql);
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
    private Recordset queryPointByMouseXMouseY3(float x, float y) {
        double _scale = mMapCtrl.getMap().getScale();
        //根据比例尺 配置缓冲区 100
        settingQueryBuffer(_scale);
        Point _pt = new Point();
        _pt.setX((int) x);
        _pt.setY((int) y);
        Point2D _reP = mMapCtrl.getMap().pixelToMap(_pt);
        GeoPoint _geoPt = new GeoPoint(_reP);
        //设置矩形查询范围
        Rectangle2D _rect = new Rectangle2D(new Point2D(_reP.getX() - SuperMapConfig.Query_Buffer, _reP.getY() - SuperMapConfig.Query_Buffer),
                new Point2D(_reP.getX() + SuperMapConfig.Query_Buffer, _reP.getY() + SuperMapConfig.Query_Buffer));
        //设置查询参数
        QueryParameter _queryParameter = new QueryParameter();
        _queryParameter.setSpatialQueryObject(_rect);
        _queryParameter.setSpatialQueryMode(SpatialQueryMode.CONTAIN);
        _queryParameter.setHasGeometry(false);
        _queryParameter.setCursorType(CursorType.STATIC);

//        _queryParameter.setResultFields(new String[]{"SmID,", " ((" + _reP.getY() + "-latitude)^2+(" + _reP.getX() + "-longitude)^2 )" + " as dis"});
//        String sql = "(Power(" + _reP.getY() + "-SmY,2)+Power(" + _reP.getX() + "-SmX,2)) as dis ";

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
     * @return Recordset
     */
    public Recordset queryLineByMouseXMouseY2(float x, float y) {
        Point _pt = new Point();
        _pt.setX((int) x);
        _pt.setY((int) y);
        Point2D _reP = mMapCtrl.getMap().pixelToMap(_pt);
        GeoPoint _geoPt = new GeoPoint(_reP);
        Layer _layer = getTotalLrLayer();
        if (_layer == null) {
            return null;
        }
        DatasetVector _dv = (DatasetVector) _layer.getDataset();
        //根据比例尺 配置缓冲区 100
        double _scale = mMapCtrl.getMap().getScale();
        //设置查询缓冲区
        settingQueryBuffer(_scale);
        //查询缓冲区内的记录集
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

    /**
     * 添加排水外检
     * @Params :
     * @author :HaiRun
     * @date   :2020/2/25  9:35
     */
    public boolean addPsRecords(BaseFieldLInfos pInfos) {
        if (pInfos == null) {
            LogUtills.e("Create The Line Find The BaseFieldLInfos Is Null...");
            return false;
        }
        DatasetVector _dv =  getPsLrDatasetVector();
        if (_dv == null){
            ToastyUtil.showErrorShort(mContext,"排水外检数据集为空");
            return false;
        }
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
//        String _labelTag = String.valueOf(_result.getID()) + pInfos.labelTag;
//        _result.setString("labelTag", _labelTag);
        //标签专题图颜色显示字段
        _result.setDouble("rangeExpression", PipeThemelabelUtil.Ins().getThemeItemValue(pInfos.pipeType.substring(0, 2)));
        //标签专题图字体显示字段
        String ds = (pInfos.pipeSize.trim().length() != 0) ? pInfos.pipeSize : pInfos.d_S;
        _result.setString("labelTag", pInfos.pipeType.substring(pInfos.pipeType.lastIndexOf("-") + 1) + "-" + ds + "-" + pInfos.material);
        _result.update();
        // 关闭记录集，释放几何对象、记录集
        _result.close();
        _result.dispose();
        _geoLine.dispose();
        return true;
    }

    /**
     * 编辑Ps管线
     * @Params :
     * @author :HaiRun
     * @date   :2020/2/25  9:58
     */
    public boolean editPsRecords(BaseFieldLInfos pInfos) {
        if (pInfos == null) {
            LogUtills.e("Edit BaseFieldLInfos Faild, BaseFieldLInfos Is Null....");
            return false;
        }

        DatasetVector _dv = getPsLrDatasetVector();
        if (_dv == null){
            ToastyUtil.showErrorShort(mContext,"排水外检数据集为空");
            return false;
        }
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

        //更新线，有可能起点终点更换了位置，所以哟啊重新设置起点终点坐标
        Point2Ds _p2ds = new Point2Ds();
        _p2ds.add(new Point2D(pInfos.startLongitude, pInfos.startLatitude));
        _p2ds.add(new Point2D(pInfos.endLongitude, pInfos.endLatitude));
        if (!_result.setGeometry(new GeoLine(_p2ds))) {
            ToastyUtil.showErrorShort(mContext, "线更改失败");
            return false;
        }
        if (!_result.update()) {
            ToastyUtil.showErrorShort(mContext, "线更加失败");
            return false;
        }
        //更改线长度
        _result.edit();
        _result.setString("pipeLength", String.format("%.2f", _result.getDouble("SmLength")));
        if (!_result.update()) {
            ToastyUtil.showErrorShort(mContext, "线长度更新失败");
            return false;
        }
        //关闭记录集，释放几何对象、记录集
        _result.close();
        _result.dispose();
        return true;
    }

}
