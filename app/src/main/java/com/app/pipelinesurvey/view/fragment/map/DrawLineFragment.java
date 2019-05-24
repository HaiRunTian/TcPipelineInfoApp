package com.app.pipelinesurvey.view.fragment.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.LineFieldFactory;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.PipeLineInfo;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.InitWindowSize;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.iview.IDrawPipeLineView;
import com.app.pipelinesurvey.view.iview.IQueryPipeLineView;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.mapping.Layer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * @author HaiRun
 */
public class DrawLineFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, IDrawPipeLineView, IQueryPipeLineView {
    /**
     * 标题
     */
    private TextView tvTitle;
    /**
     * 提交
     */
    private TextView tvSubmit;
    /**
     * 导入上一个数据
     */
    private Button btnPreviousOne;
    /**
     * 返回
     */
    private LinearLayout linearReturn;
    /**
     * 管线备注
     */
    private LinearLayout layoutLineMark;
    /**
     * 埋设方式
     */
    private Spinner spEmbeddedWay;
    /**
     * 管材
     */
    private Spinner spTextrure;
    ///
    //private Spinner spLineRemark;//线备注

    /**
     * 压力
     */
    private Spinner spPressure;
    /**
     * 电压
     */
    private Spinner spVolatage;
    /**
     * 状态
     */
    private Spinner spState;
    /**
     * 终点点号
     */
    private EditText edtStartPoint;
    /**
     * 终点点号
     */
    private EditText edtEndPoint;
    /**
     * 起点埋深
     */
    private EditText edtStartBurialDepth;
    /**
     * 终点埋深
     */
    private EditText edtEndBurialDepth;
    /**
     * 管段长度
     */
    private EditText edtPipeLength;
    /**
     * 埋深差值
     */
    private EditText edtBurialDifference;
    /**
     * 总孔数
     */
    private EditText edtHoleCount;
    /**
     * 已用孔数
     */
    private EditText edtUsedHoleCount;
    /**
     * 电缆总数
     */
    private EditText edtAmount;
    /**
     * 孔径
     */
    private EditText edtAperture;
    /**
     * 行
     */
    private EditText edtRow;
    /**
     * 列
     */
    private EditText edtCol;
    /**
     * 权属单位
     */
    private EditText edtOwnershipUnit;
    /**
     * 线备注
     */
    private EditText edtLineRemark;
    /**
     * 疑难
     */
    private EditText edtPuzzle;
    /**
     * 管径
     */
    private EditText edtPipeSize;
    /**
     * 宽
     */
    private EditText edtSectionWidth;
    /**
     * 高
     */
    private EditText edtSectionHeight;
    /**
     * 下拉框按钮
     */
    private ImageView imgvLineRemark;
    /**
     * 标题
     */
    private LinearLayout layoutPipeSize, layoutSection;
    /**
     * 交换点号下拉按钮
     */
    private ImageButton imgbtnExchange;
    /**
     * 权属下拉按钮
     */
    private ImageView imgvOwnershipUnit;
    /**
     * 管径下拉按钮
     */
    private ImageView imgvPipeSize;
    /**
     * 交换点号
     */
    private LinearLayout layoutOwnershipUnit;
    /**
     * 电力路灯面板
     */
    private LinearLayout layoutDLLDPanel;
    /**
     * 管径编辑布局
     */
    private LinearLayout layoutPipeSizeEdt;
    /**
     * 状态数据
     */
    private List<String> stateList;
    /**
     * 线备注数据
     */
    private List<String> lineRemarkList;
    /**
     * 埋设方式数据
     */
    private List<String> embeddedWayList;
    /**
     * 电压数据
     */
    private List<String> volatageList;
    /**
     * 压力数据
     */
    private List<String> pressureList;
    /**
     * 管材数据
     */
    private List<String> textureList;
    /**
     * 管径数据
     */
    private List<String> pipeSizeList;

    /**
     * 权属单位
     */
    private String[] pipeUnitList;
    ///
    //    private List<String> voltageList;//线备注数据
    /**
     * 下拉数据适配器
     */
    private ArrayAdapter<String> m_adapter;
    /**
     * 下拉弹窗
     */
    private ListPopupWindow _popupWindow;
    /**
     * 管类
     */
    private String gpType;
    private BaseFieldPInfos m_startPInfo = null;
    private BaseFieldPInfos m_endPInfo = null;
    private int m_smId = -1;
    private Button btnSave;
    private BaseFieldLInfos m_baseFileLInfo;
    private SharedPreferences.Editor m_editor;
    private StringBuffer m_stringBuffer;
    private int m_endSmId;
    private String m_code;
    private View m_view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.activity_draw_pipe_line, container, false);
        initView(m_view);

        return m_view;
    }

    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        linearReturn = (LinearLayout) view.findViewById(R.id.linearReturn);
        linearReturn.setOnClickListener(this);
        tvSubmit = (TextView) view.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        spEmbeddedWay = (Spinner) view.findViewById(R.id.spEmbeddedWay);
        spEmbeddedWay.setOnItemSelectedListener(this);
        spTextrure = (Spinner) view.findViewById(R.id.spTextrure);
        //        spLineRemark = findViewById(R.id.spLineRemark);
        //        spLineRemark.setOnItemSelectedListener(getActivity());
        spVolatage = (Spinner) view.findViewById(R.id.spVoltage);
        spPressure = (Spinner) view.findViewById(R.id.spPressure);
        spState = (Spinner) view.findViewById(R.id.spState);
        layoutPipeSize = (LinearLayout) view.findViewById(R.id.layoutPipeSize);
        layoutSection = (LinearLayout) view.findViewById(R.id.layoutSection);
        layoutDLLDPanel = (LinearLayout) view.findViewById(R.id.layoutDLLDPanel);
        edtBurialDifference = (EditText) view.findViewById(R.id.edtBurialDifference);
        edtStartBurialDepth = (EditText) view.findViewById(R.id.edtStartBurialDepth);
        edtStartBurialDepth.addTextChangedListener(m_watcher);
        edtEndBurialDepth = (EditText) view.findViewById(R.id.edtEndBurialDepth);
        edtEndBurialDepth.addTextChangedListener(m_watcher);
        edtLineRemark = (EditText) view.findViewById(R.id.edtLineRemark);
        imgbtnExchange = (ImageButton) view.findViewById(R.id.imgbtnExchange);
        imgbtnExchange.setOnClickListener(this);
        edtStartPoint = (EditText) view.findViewById(R.id.edtStartPoint);
        edtEndPoint = (EditText) view.findViewById(R.id.edtEndPoint);
        imgvLineRemark = (ImageView) view.findViewById(R.id.imgvLineRemark);
        imgvLineRemark.setOnClickListener(this);
        layoutLineMark = (LinearLayout) view.findViewById(R.id.layoutLineMark);
        imgvOwnershipUnit = (ImageView) view.findViewById(R.id.imgvOwnershipUnit);
        imgvOwnershipUnit.setOnClickListener(this);
        edtOwnershipUnit = (EditText) view.findViewById(R.id.edtOwnershipUnit);
        edtPipeSize = (EditText) view.findViewById(R.id.edtPipeSize);
        edtSectionWidth = (EditText) view.findViewById(R.id.edtSectionWidth);
        edtSectionHeight = (EditText) view.findViewById(R.id.edtSectionHeight);
        edtPipeLength = (EditText) view.findViewById(R.id.edtPipeLength);
        edtHoleCount = (EditText) view.findViewById(R.id.edtHoleCount);
        edtUsedHoleCount = (EditText) view.findViewById(R.id.edtUsedHoleCount);
        edtAmount = (EditText) view.findViewById(R.id.edtAmount);
        edtAperture = (EditText) view.findViewById(R.id.edtAperture);
        edtAmount = (EditText) view.findViewById(R.id.edtAmount);
        edtRow = (EditText) view.findViewById(R.id.edtRow);
        edtCol = (EditText) view.findViewById(R.id.edtCol);
        edtPuzzle = (EditText) view.findViewById(R.id.edtPuzzle);
        imgvPipeSize = (ImageView) view.findViewById(R.id.imgvPipeSize);
        imgvPipeSize.setOnClickListener(this);
        layoutPipeSizeEdt = (LinearLayout) view.findViewById(R.id.layoutPipeSizeEdt);
        btnPreviousOne = (Button) view.findViewById(R.id.btnPreviousOne);
        btnPreviousOne.setOnClickListener(this);
        btnSave = (Button) view.findViewById(R.id.btnRemove);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValue();
        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        //起点点号
        int _beginSmId = getArguments().getInt("beginSmId", -1);
        //终点点号
        m_endSmId = getArguments().getInt("endSmId", -1);

        m_smId = getArguments().getInt("smId", -1);
        //获取当前点图层
        Layer _pLayer = DataHandlerObserver.ins().getTotalPtLayer();
        //获取当前点图层
        Layer _pEndLayer = DataHandlerObserver.ins().getTotalLrLayer();
        if (_pLayer == null || _pEndLayer == null) {
            LogUtills.i("Initial DrawPipeLineActivity Get Point Layer Or Line Layer Faild...");
            getDialog().dismiss();
        }

        Recordset _startReset = DataHandlerObserver.ins().queryRecordsetBySmid(_beginSmId, true, false);
        _startReset.moveFirst();
        //记录集转为管点类
        m_startPInfo = BaseFieldPInfos.createFieldInfo(_startReset);
        if (m_startPInfo == null) {
            LogUtills.e("Initial DrawPipeLineActivity Create Start BaseFieldPInfos fail, Smid:" + _beginSmId);
            _startReset.close();
            _startReset.dispose();
            getDialog().dismiss();
        }

        Recordset _endReset = DataHandlerObserver.ins().queryRecordsetBySmid(m_endSmId, true, false);
        _endReset.moveFirst();
        m_endPInfo = BaseFieldPInfos.createFieldInfo(_endReset);
        if (m_endPInfo == null) {
            LogUtills.e("Initial DrawPipeLineActivity Create End BaseFieldPInfos fail, Smid:" + m_endSmId);
            _startReset.close();
            _startReset.dispose();
            _endReset.close();
            _endReset.dispose();
            getDialog().dismiss();
        }
        //获取管线长度
        double _len = getPipeLen(_startReset, _endReset);

        //起点点号
        edtStartPoint.setText(m_startPInfo.id);
        //终点点号
        edtEndPoint.setText(m_endPInfo.id);


        edtPipeLength.setText(String.format("%.2f", _len));
        //线备注
        edtLineRemark.setText(PipeLineInfo.getPipeLineInfo().getLineRemark());
        //疑难
        edtPuzzle.setText(PipeLineInfo.getPipeLineInfo().getPuzzle());
        //管径
        edtPipeSize.setText(m_startPInfo.wellCoverSize.toString());
        _startReset.close();
        _endReset.close();
        _startReset.dispose();
        _endReset.dispose();
        //导入上一条管线数据 同类管相同就导入，否则不导入
        importData();
    }

    /**
     * 获取管线长度
     *
     * @param startReset
     * @param endReset
     * @return
     */
    private double getPipeLen(Recordset startReset, Recordset endReset) {
        //获取起点与终点X、Y
        double _sX = startReset.getDouble("smX");
        double _sY = startReset.getDouble("smY");
        double _eX = endReset.getDouble("smX");
        double _eY = endReset.getDouble("smY");

        double _dx = _sX - _eX;
        double _dy = _sY - _eY;

        double _len = Math.sqrt(_dx * _dx + _dy * _dy);

        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
        int _tempL = 0;
        if (_cursor.moveToNext()) {
            _tempL = _cursor.getInt(_cursor.getColumnIndex("PipeLength"));
        }
        if (_len > _tempL) {
            ToastUtil.showShort(getActivity(), "管线长度超出自定义长度" + _tempL + "米");
        }
        return _len;
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.linearReturn:
                    String _tem = "T_";
                    if (m_endPInfo.exp_Num.contains(_tem)) {
                        Recordset _endReset = DataHandlerObserver.ins().queryRecordsetBySmid(m_endSmId, true, true);
                        _endReset.moveFirst();
                        if (_endReset.delete()) {
                            LogUtills.i("DrawPipeLineActivity", "成功删除当前记录");
                        }
                        _endReset.close();
                        _endReset.dispose();
                    }

                    getDialog().dismiss();
                    break;
                //保存 提交 提交前判断有些选项数据是否合理
                case R.id.btnRemove:
                case R.id.tvSubmit:
                    boolean _result = false;
                    checkViewData();

                    if (!checkValueRight()) {
                        return;
                    }
                    _result = DataHandlerObserver.ins().addRecords(generateBaseFieldInfo());
                    if (!_result) {
                        ToastUtil.showShort(getActivity().getBaseContext(), "保存点数据失败...");
                        return;
                    } else {
                        //更新点数据集起点深度值
                        boolean _fistPoint = DataHandlerObserver.ins().updateRecordSetBySql("exp_Num = '" + getStartPoint() + "'", getStartBurialDepth());
                        //更新点数据集起点深度值
                        boolean _secondPoint = DataHandlerObserver.ins().updateRecordSetBySql("exp_Num = '" + getEndPoint() + "'", getEndBurialDepth());
                        if (_fistPoint && _secondPoint) {
                            LogUtills.i("save point depth ", "ok");
                        }
                        getDialog().dismiss();
                    }
                    break;

                //起点和终点交换，线方向改变
                case R.id.imgbtnExchange:
                    //点号修改
                    String temp = getStartPoint();
                    edtStartPoint.setText(getEndPoint());
                    edtEndPoint.setText(temp);
                    //对象修改
                    BaseFieldPInfos tempP = m_startPInfo;
                    m_startPInfo = m_endPInfo;
                    m_endPInfo = tempP;
                    //埋深修改
                    temp = edtStartBurialDepth.getText().toString();
                    edtStartBurialDepth.setText(edtEndBurialDepth.getText().toString());
                    edtEndBurialDepth.setText(temp);
                    break;

                case R.id.imgvLineRemark:
                    _popupWindow = new ListPopupWindow(getActivity());
                    _popupWindow.setWidth(layoutLineMark.getWidth() - 5);
                    m_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_text, lineRemarkList);
                    _popupWindow.setAdapter(m_adapter);
                    _popupWindow.setAnchorView(edtLineRemark);
                    _popupWindow.setModal(true);
                    _popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            edtLineRemark.setText(lineRemarkList.get(position));
                            _popupWindow.dismiss();
                        }
                    });
                    _popupWindow.show();
                    break;
                //权属单位
                case R.id.imgvOwnershipUnit:
                    if (gpType.equals("煤气-M") || gpType.equals("燃气-R") || gpType.equals("电信-D")) {
                        showDialog(pipeUnitList, edtOwnershipUnit, "权属单位列表");

                        _popupWindow.show();
                    } else {
                        ToastUtil.showShort(getActivity(), "当前管类没有权属单位数据列表");
                    }
                    break;
                case R.id.imgvPipeSize:
                    if (gpType.equals("煤气-M") || gpType.equals("燃气-R")) {
                        if (spTextrure.getSelectedItem().toString().equals("钢") && gpType.equals("煤气-M")) {
                            _popupWindow = new ListPopupWindow(getActivity());
                            _popupWindow.setWidth(layoutPipeSizeEdt.getWidth() - 5);
                            m_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_text, pipeSizeList);
                            _popupWindow.setAdapter(m_adapter);
                            _popupWindow.setAnchorView(edtPipeSize);
                            _popupWindow.setModal(true);
                            _popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    edtPipeSize.setText(pipeSizeList.get(position));
                                    _popupWindow.dismiss();
                                }
                            });
                            _popupWindow.show();
                        }
                    } else {
                        ToastUtil.showShort(getActivity(), "当前管类没有管径数据列表");
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
    }

    /**
     * 检测一些数据是否合理，不合理则提示用户修改
     *
     * @return
     */
    private boolean checkValueRight() {
        //已用孔数不但能大于总孔数
        if (!getHoleCount().isEmpty() && !getUsedHoleCount().isEmpty()) {
            if (Integer.valueOf(getUsedHoleCount()) > Integer.valueOf(getHoleCount())) {
                ToastUtil.showShort(getActivity(), "已用孔数不能大于总孔数");
                return false;
            }
        }
        //埋深不能超过20米
        if (Double.valueOf(getStartBurialDepth()) > 20.0 ) {
            ToastUtil.showShort(getActivity(), "起点埋深超过20米，请检查数据");
            return false;
        }
        //埋深不能超过20米
        if (Double.valueOf(getEndBurialDepth()) > 20.0) {
            ToastUtil.showShort(getActivity(), "终点埋深超过20米，请检查数据");
            return false;
        }


        return true;
    }

    @Nullable
    private BaseFieldLInfos generateBaseFieldInfo() {
        BaseFieldLInfos _info = null;
        try {
            String gpType = getArguments().getString("gpType");
            if (getEndPoint().contains("T_")) {
                _info = LineFieldFactory.CreateInfo("临时");
            } else {
                _info = LineFieldFactory.CreateInfo(gpType.substring(0, 2));
            }
            if (_info == null) {
                LogUtills.e("DrawPipeLineActivity Create BaseFieldPInfos Fail...");
                return null;
            }
            _info.benExpNum = getStartPoint();
            _info.endExpNum = getEndPoint();
            _info.startLatitude = m_startPInfo.latitude;
            _info.startLongitude = m_startPInfo.longitude;
            _info.endLatitude = m_endPInfo.latitude;
            _info.endLongitude = m_endPInfo.longitude;
            _info.exp_Date = DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT);
            _info.benDeep = getStartBurialDepth();
            _info.endDeep = getEndBurialDepth();
            _info.buried = getEmbeddedWay();
            _info.pipeSize = getPipeSize();
            //断面
            if (getSectionWidth().isEmpty() || getSectionWidth().length() == 0) {
                _info.d_S = "";
            } else {
                _info.d_S = getSectionWidth() + "X" + getSectionHeight();
            }
            _info.pipeLength = getPipeLength();

            if (_info.endExpNum.contains("T_")) {
                _info.pipeType = "临时-O";
            }

            if (getRow().isEmpty() || getRow().length() == 0) {
                _info.rowXCol = "";
            } else {
                _info.rowXCol = getRow() + "X" + getCol();
            }
            _info.cabNum = getAmount();
            _info.voltage = getVoltage();
            _info.pressure = getPressure();
            _info.material = getTextrure();
            _info.belong = getOwnershipUnit();
            _info.totalHole = getHoleCount();
            _info.usedHole = getUsedHoleCount();
            _info.holeDiameter = getAperture();
            _info.state = getState();
            _info.remark = getLineRemark();
            _info.puzzle = getPuzzle();
            _info.burialDifference = getBurialDifference();
            if (getEndPoint().contains("T_")) {
                _info.labelTag = "-" + "临时-o" + _info.pipeSize.toString() + "-" + _info.d_S + "-" + _info.material.toString();
            } else {
                _info.labelTag = "-" + _info.pipeType.toLowerCase() + "-" + _info.pipeSize.toString() + "-" + _info.d_S + "-" + _info.material.toString();
            }
        } catch (Exception e) {
            LogUtills.e(e.getMessage());
        }
        return _info;


    }

    /**
     * 导入上一条数据
     */
    private void importData() {
        QueryParameter _parameter = new QueryParameter();
        _parameter.setAttributeFilter("benExpNum = '" + getStartPoint() + "' or endExpNum = '" + getStartPoint() + "'");
        _parameter.setCursorType(CursorType.STATIC);
        _parameter.setOrderBy(new String[]{"SmID asc"});
        Recordset reSet = DataHandlerObserver.ins().QueryRecordsetByParameter(_parameter, false);
        if (!reSet.isEmpty()) {
            reSet.moveLast();
            m_baseFileLInfo = BaseFieldLInfos.createFieldInfo(reSet);
            setValueToView();
        } else {
            ToastUtil.showShort(getActivity(), "您未录入过此类型管点数据");
        }

        reSet.close();
        reSet.dispose();
    }


    /**
     * 初始化数据
     */
    private void setValueToView() {
        setStartBurialDepth();   //设置起点埋深
        setEmbeddedWay();        //设置埋设方式
        setPipeTexture();        //设置管线材质
        setPipeSize();           //设置管径大小
        setSectionWidth();       //设置断面宽
        setSectionHeight();      //设置断面高
        setHoleCount();          //设置总孔数
        setUsedHoleCount();      //设置已用孔数
        setAmount();             //设置电缆总数
        setAperture();           //设置套管孔径
        setRow();                //设置行
        setCol();                //设置列
        setVoltage();            //设置电缆电压
        setState();              //设置管线状态
        setPressure();           //设置管道压力
        setOwnershipUnit();      //设置权属单位
        setLineRemark();         //设置管线备注
        setPuzzle();             //设置疑难问题
    }

    private void initValue() {

        //管线类型
        gpType = getArguments().getString("gpType");
        m_code = gpType.substring(gpType.length() - 1);
        tvTitle.setText("加线" + "(" + gpType + ")");
        //管线材料
        textureList = SpinnerDropdownListManager.getData("lineTexture", gpType);
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, textureList);
        spTextrure.setAdapter(m_adapter);

        //管线备注
        lineRemarkList = SpinnerDropdownListManager.getData("lineRemark", gpType);

        //管线状态
        stateList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.state));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, stateList);
        spState.setAdapter(m_adapter);

        //埋深方式
        embeddedWayList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.embeddedway));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, embeddedWayList);
        spEmbeddedWay.setAdapter(m_adapter);

        //管径
        if (gpType.equals("煤气-M") || gpType.equals("燃气-R")) {
            //管径
            pipeSizeList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.pipesizeStandard));
        }

        //权属单位
        if (gpType.equals("煤气-M") || gpType.equals("燃气-R")) {
            pipeUnitList = getResources().getStringArray(R.array.ownershipUnit_rq_mq);
        } else if (gpType.equals("电信-D")) {
            pipeUnitList = getResources().getStringArray(R.array.ownershipUnit);
        }
        //电压数据
        volatageList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.voltage));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, volatageList);
        spVolatage.setAdapter(m_adapter);
        spVolatage.setSelection(volatageList.size() - 1);
        //压力数据
        pressureList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.pressure));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, pressureList);
        spPressure.setAdapter(m_adapter);
        spPressure.setSelection(pressureList.size() - 1);

        //显示隐藏属性面板
        if (gpType.equals("电力-L") || gpType.equals("路灯-S")
                || gpType.equals("电信-D") || gpType.equals("有视-T")
                || gpType.equals("军队-B") || gpType.equals("交通-X")) {
            layoutDLLDPanel.setVisibility(View.VISIBLE);
        } else {
            layoutDLLDPanel.setVisibility(View.GONE);
        }

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (m_endPInfo.exp_Num.contains("T_")) {
                        Recordset _endReset = DataHandlerObserver.ins().queryRecordsetBySmid(m_endSmId, true, true);
                        _endReset.moveFirst();
                        if (_endReset.delete()) {
                        }
                        _endReset.close();
                        _endReset.dispose();
                    }
                    getDialog().dismiss();
                    WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
                    return true;
                } else {
                    //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
                    return false;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //初始化窗口大小
        InitWindowSize.ins().initWindowSize(getActivity(), getDialog());
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spEmbeddedWay:
                switch (spEmbeddedWay.getSelectedItem().toString()) {
                    case "管埋":
                        layoutPipeSize.setVisibility(View.VISIBLE);
                        layoutSection.setVisibility(View.GONE);
                        edtSectionWidth.setText("");
                        edtSectionHeight.setText("");
                        break;
                    case "直埋":
                    case "架空":
                        edtLineRemark.setText(spEmbeddedWay.getSelectedItem().toString());
                        layoutPipeSize.setVisibility(View.VISIBLE);
                        layoutSection.setVisibility(View.GONE);
                        edtSectionWidth.setText("");
                        edtSectionHeight.setText("");
                        break;
                    case "管块":
                    case "方沟":
                        layoutSection.setVisibility(View.VISIBLE);
                        layoutPipeSize.setVisibility(View.GONE);
                        edtPipeSize.setText("");
                        break;
                    default:
                        break;
                }
                //            case R.id.spLineRemark:
                //                edtLineRemark.setText(spLineRemark.getSelectedItem().toString());
                //                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //埋深差值
    private TextWatcher m_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String num1Str = edtStartBurialDepth.getText().toString();
            String num2Str = edtEndBurialDepth.getText().toString();
            if (num1Str.length() == 0) {
                num1Str = "0";
            }
            if (num2Str.length() == 0) {
                num2Str = "0";
            }

//            int num2 = Integer.parseInt(num2Str);
//            int num1 = Integer.parseInt(num1Str);
            int num2 = Integer.parseInt(num2Str);
            int num1 = Integer.parseInt(num1Str);
            int result = num1 - num2;
            edtBurialDifference.setText(String.valueOf(result));
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    Bundle _bundle = data.getExtras();
                    ArrayList<String> _list = (ArrayList<String>) _bundle.get("data");
                    if (_list.size() > 0) {
                        String result = "";
                        for (String item : _list) {
                            result += item + "+";
                        }
                        edtOwnershipUnit.setText(result.substring(0, result.length() - 1));
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setStartPoint() {

    }

    @Override
    public void setEndPoint() {

    }

    @Override
    public String getStartPoint() {
        return edtStartPoint.getText().toString();
    }

    @Override
    public String getEndPoint() {
        return edtEndPoint.getText().toString();
    }

    @Override
    public String getStartBurialDepth() {

        String temp = edtStartBurialDepth.getText().toString();

        if (temp.length() > 0) {
            int s = Integer.parseInt(temp);
            DecimalFormat df2 = new DecimalFormat("###.00");
            if (temp.length() > 2) {
                return df2.format(s / 100d);
            } else {
                return "0" + df2.format(s / 100d);
            }
        }
        return "0";

    }

    @Override
    public String getEndBurialDepth() {

        String temp = edtEndBurialDepth.getText().toString();
        if (temp.length() > 0) {
            int s = Integer.parseInt(temp);
            DecimalFormat df2 = new DecimalFormat("###.00");
            if (temp.length() > 2) {
                return df2.format(s / 100d);
            } else {
                return "0" + df2.format(s / 100d);
            }
        } else {
            return "0";
        }

    }

    @Override
    public String getPipeLength() {
        return edtPipeLength.getText().toString();
    }

    @Override
    public String getBurialDifference() {

        String temp = edtBurialDifference.getText().toString();
        if (temp.isEmpty()) {
            return "0";
        }
        int s = Integer.parseInt(temp);
        DecimalFormat df2 = new DecimalFormat("###.00");
        if (temp.length() > 2) {
            return df2.format(s / 100d);
        } else if (temp.length() > 0) {
            return "0" + df2.format(s / 100d);
        } else {
            return "0";
        }
    }

    @Override
    public String getEmbeddedWay() {
        return spEmbeddedWay.getSelectedItem().toString();
    }

    @Override
    public String getPipeSize() {
        return edtPipeSize.getText().toString();
    }

    @Override
    public String getSectionWidth() {
        return edtSectionWidth.getText().toString().trim();
    }

    @Override
    public String getSectionHeight() {
        return edtSectionHeight.getText().toString().trim();
    }

    @Override
    public String getTextrure() {
        return spTextrure.getSelectedItem().toString();
    }

    @Override
    public String getHoleCount() {
        return edtHoleCount.getText().toString();
    }

    @Override
    public String getUsedHoleCount() {
        return edtUsedHoleCount.getText().toString();
    }

    @Override
    public String getAmount() {
        return edtAmount.getText().toString();
    }

    @Override
    public String getAperture() {
        return edtAperture.getText().toString();
    }

    @Override
    public String getRow() {
        return edtRow.getText().toString().trim();
    }

    @Override
    public String getCol() {
        return edtCol.getText().toString().trim();
    }

    @Override
    public String getVoltage() {
        return spVolatage.getSelectedItem().toString();
    }

    @Override
    public String getState() {
        return spState.getSelectedItem().toString();
    }

    @Override
    public String getPressure() {
        return spPressure.getSelectedItem().toString();
    }

    @Override
    public String getOwnershipUnit() {
        return edtOwnershipUnit.getText().toString();
    }

    @Override
    public String getLineRemark() {
        return edtLineRemark.getText().toString();
    }

    @Override
    public String getPuzzle() {
        return edtPuzzle.getText().toString();
    }

    /**
     * 设置起点埋深
     */
    @Override
    public void setStartBurialDepth() {
        String _startBurialDepth = m_startPInfo.depth;
        if (_startBurialDepth != null && !_startBurialDepth.isEmpty()) {
            double s = Double.parseDouble(_startBurialDepth);
            int temp = (int) (s * 100);
            String depth = String.valueOf(temp);
            edtStartBurialDepth.setText(depth);
        }
    }

    /**
     * 设置终点埋深
     */
    @Override
    public void setEndBurialDepth() {
        String _ednBurialDepth = m_baseFileLInfo.endDeep;
        if (_ednBurialDepth != null) {
            double s = Double.parseDouble(_ednBurialDepth);
            int temp = (int) (s * 100);
            String depth = String.valueOf(temp);
            edtEndBurialDepth.setText(depth);
        }
    }

    /**
     * 设置管段长度
     */
    @Override
    public void setPipeLength() {
        String _pipeLength = m_baseFileLInfo.pipeLength;
        if (_pipeLength != null) {
            edtPipeLength.setText(_pipeLength);
        }
    }

    /**
     * 设置埋深差值
     */
    @Override
    public void setBurialDifference() {
        String _burialDifference = m_baseFileLInfo.burialDifference;
        if (_burialDifference != null) {
            double s = Double.parseDouble(_burialDifference);
            int temp = (int) (s * 100);
            String depth = String.valueOf(temp);
            edtBurialDifference.setText(depth);
        }
    }

    /**
     * 设置埋设方式
     */
    @Override
    public void setEmbeddedWay() {
        String _embeddedWay = m_baseFileLInfo.buried;
        if (_embeddedWay != null) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spEmbeddedWay, _embeddedWay);
        }
    }

    /**
     * 设置管径
     */
    @Override
    public void setPipeSize() {
        String _pipeSize = m_baseFileLInfo.pipeSize;
        if (_pipeSize != null) {
            edtPipeSize.setText(_pipeSize);
        }
    }

    /**
     * 设置断面宽
     */
    @Override
    public void setSectionWidth() {
        String _sectionW = m_baseFileLInfo.d_S;
        if (_sectionW.length() > 0) {
            int _index = _sectionW.indexOf("X");
            String _temp = _sectionW.substring(0, _index);
            edtSectionWidth.setText(_temp);
        }
    }

    /**
     * 设置断面高
     */
    @Override
    public void setSectionHeight() {
        String _sectionH = m_baseFileLInfo.d_S;
        if (_sectionH.length() > 0) {
            int _index = _sectionH.indexOf("X");
            String _temp = _sectionH.substring(_index + 1);
            edtSectionHeight.setText(_temp);
        }
    }

    /**
     * 设置总孔数
     */
    @Override
    public void setHoleCount() {
        String _holeCount = m_baseFileLInfo.totalHole;
        if (_holeCount != null) {
            edtHoleCount.setText(_holeCount);
        }
    }

    /**
     * 设置已用孔数
     */
    @Override
    public void setUsedHoleCount() {
        String _usedHoleCount = m_baseFileLInfo.totalHole;
        if (_usedHoleCount != null) {
            edtUsedHoleCount.setText(_usedHoleCount);
        }
    }

    /**
     * 设置电缆总数
     */
    @Override
    public void setAmount() {
        String _amount = m_baseFileLInfo.cabNum;
        if (_amount != null) {
            edtAmount.setText(_amount);
        }
    }

    /**
     * 设置孔径
     */
    @Override
    public void setAperture() {
        String _aperture = m_baseFileLInfo.holeDiameter;
        if (_aperture != null) {
            edtAperture.setText(_aperture);
        }
    }

    /**
     * 设置行
     */
    @Override
    public void setRow() {
        String _row = m_baseFileLInfo.rowXCol;
        if (_row.length() > 0) {
            int _index = _row.indexOf("X");
            String _temp = _row.substring(0, _index);
            edtRow.setText(_temp);
        }
    }

    /**
     * 设置列
     */
    @Override
    public void setCol() {
        String _col = m_baseFileLInfo.rowXCol;
        if (_col.length() > 0) {
            int _index = _col.indexOf("X");
            String _temp = _col.substring(_index + 1);
            edtCol.setText(_temp);
        }
    }

    /**
     * 设置电压
     */
    @Override
    public void setVoltage() {
        String _voltage = m_baseFileLInfo.voltage;
        if (_voltage != null) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spVolatage, _voltage);
        }
    }

    /**
     * 设置管线状态
     */
    @Override
    public void setState() {
        String _state = m_baseFileLInfo.state;
        if (_state != null) {

            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spState, _state);
        }
    }

    /**
     * 设置管道压力
     */
    @Override
    public void setPressure() {
        String _pressure = m_baseFileLInfo.pressure;
        if (_pressure != null) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spPressure, _pressure);
        }
    }

    /**
     * 设置权属单位
     */
    @Override
    public void setOwnershipUnit() {
        String _ownerShipUnit = m_baseFileLInfo.belong;
        if (_ownerShipUnit != null) {
            edtOwnershipUnit.setText(_ownerShipUnit);
        }
    }

    /**
     * 设置线备注
     */
    @Override
    public void setLineRemark() {
        String _remark = m_baseFileLInfo.remark;
        if (_remark != null) {
            edtLineRemark.setText(_remark);
        }

    }

    /**
     * 设置疑难问题
     */
    @Override
    public void setPuzzle() {
        String _puzzle = m_baseFileLInfo.puzzle;
        if (_puzzle != null) {
            edtPuzzle.setText(_puzzle);
        }
    }

    /**
     * 设置管线材质
     */
    @Override
    public void setPipeTexture() {
        String _pipeTexture = m_baseFileLInfo.material;
        if (_pipeTexture != null) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spTextrure, _pipeTexture);
        }
    }

    ///
   /* public void setStartBurialDepth(String depth) {
//        String _startBurialDepth = m_baseFileLInfo.benDeep;
        if (depth != null) {
//            LogUtills.i("i起点埋深 = ", depth);
            double s = Double.parseDouble(depth);
            int temp = (int) (s * 100);
            String h = String.valueOf(temp);
            edtStartBurialDepth.setText(h);
        }
    }*/


    /**
     * 判断数据是否写完毕，未填写完毕则提示
     *
     * @Author HaiRun
     * @Time 2019/4/15 . 14:27
     */
    public void checkViewData() {

        StringBuffer str = new StringBuffer();

        if (edtStartBurialDepth.getText().toString().isEmpty()) {
            str.append("起点埋深、");
        }
        if (edtEndBurialDepth.getText().toString().isEmpty()) {
            str.append("终点埋深、");
        }

        if (edtPipeSize.getText().toString().isEmpty()) {
            if (getEmbeddedWay().equals("管埋") || getEmbeddedWay().equals("直埋") || getEmbeddedWay().equals("架空")) {
                str.append("管径、");
            } else {
                str.append("断面、");
            }
        }
        //显示隐藏属性面板
        if (gpType.equals("电力-L") || gpType.equals("路灯-S")
                || gpType.equals("电信-D") || gpType.equals("有视-T")
                || gpType.equals("军队-B") || gpType.equals("交通-X")) {

            if (edtHoleCount.getText().toString().isEmpty()) {
                str.append("总孔数、");
            }

            if (edtUsedHoleCount.getText().toString().isEmpty()) {
                str.append("已用孔数、");
            }

            if (edtAmount.getText().toString().isEmpty()) {
                str.append("电缆根数、");
            }

            if (edtAperture.getText().toString().isEmpty()) {
                str.append("套管孔径、");
            }

            if (edtRow.getText().toString().isEmpty()) {
                str.append("行 X 列、");
            }

            if (spVolatage.getSelectedItem().toString().isEmpty()) {
                str.append("电缆电压、");
            }

            if (edtOwnershipUnit.getText().toString().isEmpty()) {
                str.append("权属单位、");
            }
        }

        if (str.length() > 0) {
            Toast.makeText(getActivity().getBaseContext(), str.toString().substring(0, str.length() - 1) + "数据没有填写", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * 管线条件设置
     *
     * @author HaiRun
     * created at 2018/12/4 8:53
     */
    public boolean checkOutValue() {
        //管材
        String _textrure = spTextrure.getSelectedItem().toString();
        //埋设方式
        String _embeddeWay = spEmbeddedWay.getSelectedItem().toString();
        //总孔数
        String _holeCount = getHoleCount();
        //已用孔数
        String _useHoleCount = getUsedHoleCount();
        //管径
        String _pipeSize = getPipeSize();
        //电缆数
        String _amount = getAmount();

        if (gpType.equals("电力-L") || gpType.equals("电信-D") || gpType.equals("路灯-S")) {
            //	3电力、电信、路灯管材不为空管、空沟。电缆根数不能为O或空值，出现时提示。
            if (!_textrure.equals("空管") || !_textrure.equals("空沟")) {
                if (_amount.equals("0") || _amount.isEmpty()) {
                    ToastUtil.show(getActivity(), "电缆根数不能为空或者为0或空值", Toast.LENGTH_SHORT);
                    edtAmount.setError("必填项不能为空或0");
                    return false;
                }
            } else {
                //	4.电力、电信、路灯管材为空管、空沟时电缆根数为O，出现不为0时提示。
                edtAmount.setText("0");

            }

            //6、电力、电信、路灯埋设方式为架空、直埋时管径不为空值，总孔数、已用孔为空值。电缆根数为0或空值。
            // 注意架空和直埋时管材不能为空管或空值。出现这些问题时提示。
            if (_embeddeWay.equals("架空") || _embeddeWay.equals("直埋")) {
                if (!_pipeSize.isEmpty()) {
                    edtPipeSize.setError("管径应该为空");
                    return false;
                }
                if (_holeCount.isEmpty() || _useHoleCount.isEmpty()) {
                    edtHoleCount.setText("不能为空");
                    edtUsedHoleCount.setText("不能为空");
                }
                if (_amount.equals("0") || _amount.isEmpty()) {
                    edtAmount.setText("电缆根数不能为空");
                }

                edtHoleCount.setText("");
                edtUsedHoleCount.setText("");
                edtAmount.setText("0");

                if (_textrure.equals("空管") || _textrure.isEmpty()) {
                    ToastUtil.show(getActivity(), "管材不能为空管", Toast.LENGTH_SHORT);
                    return false;
                }
            }
            /*7 电力、电信、路灯 埋设方式为方沟时总孔数、已用孔为空值。管材为空管或空沟时电缆根数为0，
            不为空管空沟时电缆根数不能为0。出现这些问题时提示。
            */
            if (_embeddeWay.equals("方沟")) {
                edtHoleCount.setText("");
                edtUsedHoleCount.setText("");
                if (_textrure.equals("空管") || _textrure.equals("空沟")) {
                    edtAmount.setText("0");
                } else {
                    if (getAmount().equals("0")) {
                        edtAmount.setError("电缆数不能为0");
                        return false;
                    }
                }
            }
              /*
              8、电力、电信、路灯 埋设方式为管埋时 管材为空管或空沟时总孔数为“1”、已用孔为“0”、
              电缆根数为“0”。 管材不为空管或空沟时总孔数为“1”、已用孔为“1”、 电缆根数不为“0”或
              空值。出现这些问题时提示。
               */
            if (_embeddeWay.equals("管埋")) {

                if (_holeCount.equals("1")) {
                    edtHoleCount.setError("总孔数不能为1");
                    return false;
                }
                if (_amount.equals("0")) {
                    edtAmount.setError("电缆根数不能为0");
                    return false;
                }

                if (_textrure.equals("空管") || _textrure.equals("空沟")) {

                    if (_useHoleCount.equals("0")) {
                        edtUsedHoleCount.setError("已用孔数不能为0");
                        return false;
                    }

                } else {
                    if (_useHoleCount.equals("1")) {
                        edtUsedHoleCount.setError("已用孔数不能为1");
                        return false;
                    }
                }
            }

              /*
              9、电力、电信、路灯 埋设方式为管块时 管材为空管或空沟时总孔数为>“2”、已用孔为“0”、
               电缆根数为“0”。 管材不为空管或空沟时总孔数总孔数为>“2”、已用孔为不能为“0或空值”且已
               用孔不能>总孔数、 电缆根数不为“0”或空值且电缆根数不能<已用孔。出现这些问题时提示。
               */
            if (_embeddeWay.equals("管块")) {
                if (_textrure.equals("空管") || _textrure.equals("空沟")) {
                    edtUsedHoleCount.setText("0");
                    edtAmount.setText("0");
                    if (Integer.valueOf(_holeCount) < 2) {
                        edtHoleCount.setError("总孔数要大于2");
                        return false;
                    }
                } else {
                    if (Integer.valueOf(_holeCount) < 2) {
                        edtHoleCount.setError("总孔数要大于2");
                        return false;
                    }
                    if (_useHoleCount.equals("0") || _useHoleCount.isEmpty()) {
                        edtUsedHoleCount.setError("已用孔不能为0或者空值");
                    }
                    if (Integer.valueOf(_useHoleCount) > Integer.valueOf(_holeCount)) {
                        edtUsedHoleCount.setError("已用孔数不能大于总孔数");
                    }
                    if (_amount.equals("0") || _amount.isEmpty()) {
                        edtAmount.setError("电缆根数不能为0或者空值");
                    }

                    if (Integer.valueOf(_amount) < Integer.valueOf(_useHoleCount)) {
                        edtAmount.setError("电缆更熟不能小于已用孔数");
                        edtUsedHoleCount.setError("已用孔数不能大于电缆数");
                    }

                }
            }
        }


        //5电力、路灯管材不为空管、空沟时电压不能为不能为O或空值. 管材为空管、空沟时电压为空值，出现这些问题时提示
        if (gpType.equals("电力-L") || gpType.equals("路灯-S")) {
            if (!_textrure.equals("空管") || !_textrure.equals("空沟")) {
                String _volatage = spVolatage.getSelectedItem().toString();
                if (_volatage.equals("0") || _volatage.length() == 0) {
                    ToastUtil.show(getActivity(), "电压不能为空或者为0", Toast.LENGTH_SHORT);
                }
            } else {
                spVolatage.setSelection(0, false);
            }
        }
        return true;
    }

    private void showDialog(final String[] data, final TextView textView, String title) {
        textView.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(title);
        final boolean[] selectItems = new boolean[data.length];
        for (int i = 0; i < data.length; i++) {
            selectItems[i] = false;
        }
        /**
         * 第一个参数指定我们要显示的一组下拉多选框的数据集合
         * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
         * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
         * 第三个参数给每一个多选项绑定一个监听器
         */
        builder.setMultiChoiceItems(data, selectItems, new DialogInterface.OnMultiChoiceClickListener() {
            StringBuffer sb = new StringBuffer(100);

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    //选择的选项保存到sb中
                    sb.append(data[which] + "+");
                }
                String s = sb.toString();
                String data = s.substring(0, s.length() - 1);
                textView.setText(data);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                textView.setText("");
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


}
