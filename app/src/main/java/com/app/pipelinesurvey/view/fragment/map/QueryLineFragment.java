package com.app.pipelinesurvey.view.fragment.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.app.BaseInfo.Data.MAPACTIONTYPE2;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.utils.AlertDialogUtil;
import com.app.pipelinesurvey.utils.InitWindowSize;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.iview.IDrawPipeLineView;
import com.app.pipelinesurvey.view.iview.IQueryPipeLineView;
import com.app.utills.LogUtills;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLine;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author HaiRun
 */
public class QueryLineFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, IQueryPipeLineView, IDrawPipeLineView {
    private TextView tvTitle;
    private TextView tvSubmit;
    private Button btnEdit;
    private LinearLayout linearReturn;
    private LinearLayout layoutLineMark;
    private Spinner spEmbeddedWay;
    private Spinner spTextrure;
    private Spinner spVolatage;
    private Spinner spPressure;
    private Spinner spState;
    private EditText edtStartPoint;
    private EditText edtEndPoint;
    private EditText edtStartBurialDepth;
    private EditText edtEndBurialDepth;
    private EditText edtPipeLength;
    private EditText edtBurialDifference;
    private EditText edtHoleCount;
    private EditText edtUsedHoleCount;
    private EditText edtAmount;
    private EditText edtAperture;
    private EditText edtRow;
    private EditText edtCol;
    private EditText edtOwnershipUnit;
    private EditText edtLineRemark;
    private EditText edtPuzzle;
    private EditText edtPipeSize;
    private EditText edtSectionWidth;
    private EditText edtSectionHeight;
    private ImageView imgvLineRemark;
    private LinearLayout layoutPipeSize, layoutSection;
    private ImageButton imgbtnExchange;
    private ImageView imgvOwnershipUnit;
    private ImageView imgvPipeSize;
    private LinearLayout layoutOwnershipUnit;
    private LinearLayout layoutDLLDPanel;
    private LinearLayout layoutPipeSizeEdt;
    /**
     * 状态数据
     */
    private List<String> stateList;
    /**
     * 线备注数据
     */
    private List<String> lineRemarkList;//
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
     * /管材数据
     */
    private List<String> textureList;
    /**
     * 管径数据
     */
    private List<String> pipeSizeList;
    /**
     * 线备注数据
     */
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
    /**
     * 线对象
     */
    private BaseFieldLInfos m_baseInfo = null;
    //    private boolean isEdit = true; //默认开启
    /**
     * 删除按钮
     */
    private Button btnDelLine;
    private int m_smId;
    private DatasetVector m_dsV;
    /**
     * 线数据集
     */
    private Recordset m_reSet;
    private Button btnGetStartPoint;
    private Button btnGetEndPoint;
    //起点编号
    String m_startpointID = "";
    //终点编号
    String m_endPointID = "";
    private Button btnSave;
    /**
     * 权属单位
     */
    private String[] pipeUnitList;
    private View m_view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.activity_query_pipe_line, container, false);
        initView(m_view);
        return m_view;
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        linearReturn = view.findViewById(R.id.linearReturn);
        linearReturn.setOnClickListener(this);
        tvSubmit = view.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        spEmbeddedWay = view.findViewById(R.id.spEmbeddedWay);
        spEmbeddedWay.setOnItemSelectedListener(this);
        spTextrure = view.findViewById(R.id.spTextrure);
        spVolatage = view.findViewById(R.id.spVoltage);
        spPressure = view.findViewById(R.id.spPressure);
        spState = view.findViewById(R.id.spState);
        layoutPipeSize = view.findViewById(R.id.layoutPipeSize);
        layoutSection = view.findViewById(R.id.layoutSection);
        layoutDLLDPanel = view.findViewById(R.id.layoutDLLDPanel);
        edtBurialDifference = view.findViewById(R.id.edtBurialDifference);
        edtStartBurialDepth = view.findViewById(R.id.edtStartBurialDepth);
        edtStartBurialDepth.addTextChangedListener(m_watcher);
        edtEndBurialDepth = view.findViewById(R.id.edtEndBurialDepth);
        edtEndBurialDepth.addTextChangedListener(m_watcher);
        edtLineRemark = view.findViewById(R.id.edtLineRemark);
        imgbtnExchange = view.findViewById(R.id.imgbtnExchange);
        imgbtnExchange.setOnClickListener(this);
        edtStartPoint = view.findViewById(R.id.edtStartPoint);
        edtEndPoint = view.findViewById(R.id.edtEndPoint);
        imgvLineRemark = view.findViewById(R.id.imgvLineRemark);
        imgvLineRemark.setOnClickListener(this);
        layoutLineMark = view.findViewById(R.id.layoutLineMark);
        imgvOwnershipUnit = view.findViewById(R.id.imgvOwnershipUnit);
        imgvOwnershipUnit.setOnClickListener(this);
        edtOwnershipUnit = view.findViewById(R.id.edtOwnershipUnit);
        edtOwnershipUnit.setOnClickListener(this);
        edtPipeSize = view.findViewById(R.id.edtPipeSize);
        edtSectionWidth = view.findViewById(R.id.edtSectionWidth);
        edtSectionHeight = view.findViewById(R.id.edtSectionHeight);
        edtPipeLength = view.findViewById(R.id.edtPipeLength);
        edtHoleCount = view.findViewById(R.id.edtHoleCount);
        edtUsedHoleCount = view.findViewById(R.id.edtUsedHoleCount);
        edtAmount = view.findViewById(R.id.edtAmount);
        edtAperture = view.findViewById(R.id.edtAperture);
        edtAmount = view.findViewById(R.id.edtAmount);
        edtRow = view.findViewById(R.id.edtRow);
        edtCol = view.findViewById(R.id.edtCol);
        edtPuzzle = view.findViewById(R.id.edtPuzzle);
        imgvPipeSize = view.findViewById(R.id.imgvPipeSize);
        imgvPipeSize.setOnClickListener(this);
        layoutPipeSizeEdt = view.findViewById(R.id.layoutPipeSizeEdt);
        btnDelLine = view.findViewById(R.id.btnRemove);
        btnDelLine.setOnClickListener(this);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);
        btnGetStartPoint = (Button) view.findViewById(R.id.btnGetStartPoint);
        btnGetEndPoint = (Button) view.findViewById(R.id.btnGetEndPoint);
        btnGetStartPoint.setOnClickListener(this);
        btnGetEndPoint.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValue();
    }

    private void initValue() {

        Bundle _bundle = getArguments();
        //动作状态  1查询线  2 重新获取起点  3重新获取终点
        int _actionType = _bundle.getInt("actionType", 0);
        //管线类型
        gpType = _bundle.getString("gpType", "");
        //线对象
        m_baseInfo = _bundle.getParcelable("_infoL");
        //线SmID
        m_smId = _bundle.getInt("smId", 0);
        //重新获取的点号编号
        String _expNum = _bundle.getString("exp_Num");
        //获取点smid
        int _smID = _bundle.getInt("SmIDP", 0);
        //线
        Point2Ds m_point2Ds = new Point2Ds();
        //线记录集
        m_reSet = DataHandlerObserver.ins().queryRecordsetBySmid(m_smId, false, true);

        if (m_reSet.isEOF()) {
            LogUtills.e("QueryPipeLineActivity Query SMID=" + m_smId + ", Get Recordset Faild...");
            m_reSet.close();
            m_reSet.dispose();
            getDialog().dismiss();
            getDialog().dismiss();
        }

        //获取线对象
        GeoLine _geoLine = (GeoLine) m_reSet.getGeometry();
        //返回此线几何对象中指定序号的子对象，以有序点集合的方式返回该子对象。
        Point2Ds _2dts = _geoLine.getPart(0);
        m_reSet.moveFirst();
        //简单查询编辑
        if (_actionType == 1) {
            m_startpointID = m_baseInfo.benExpNum;
            m_endPointID = m_baseInfo.endExpNum;

        } else if (_actionType == 2) {
            //重新选择起点
            m_startpointID = _expNum;
            m_endPointID = m_baseInfo.endExpNum;
            if (_2dts.getCount() != 2) {
            }
            //点数据集
            Recordset _reSetP = DataHandlerObserver.ins().queryRecordsetBySmid(_smID, true, true);
            BaseFieldPInfos m_newStartPoint = BaseFieldPInfos.createFieldInfo(_reSetP);
            m_point2Ds.add(new Point2D(m_newStartPoint.longitude, m_newStartPoint.latitude));
            m_point2Ds.add(_2dts.getItem(1));
            m_reSet.edit();
            //用于修改记录集当前位置的几何对象，覆盖原来的几何对象，成功则返回true。
            if (!m_reSet.setGeometry(new GeoLine(m_point2Ds))) {
                ToastUtil.showShort(getActivity(), "重新选择起点更新失败");
                return;
            }
            if (!m_reSet.update()) {
                ToastUtil.showShort(getActivity(), "重新选择起点更新失败");
                return;
            }
            m_reSet.edit();
            m_reSet.setString("benExpNum", m_startpointID);
            m_reSet.setDouble("startLongitude", m_newStartPoint.longitude);
            m_reSet.setDouble("startLatitude", m_newStartPoint.latitude);
            //更改线的长度
            m_reSet.setString("pipeLength", String.format("%.2f", m_reSet.getDouble("SmLength")));
            if (!m_reSet.update()) {
                ToastUtil.showShort(getActivity(), "重新选择起点线长度更新失败");
                return;
            }

        } else {//重新选择终点

            m_startpointID = m_baseInfo.benExpNum;
            m_endPointID = _expNum;
            Recordset _reSetP = DataHandlerObserver.ins().queryRecordsetBySmid(_smID, true, true);
            BaseFieldPInfos m_newEndPoint = BaseFieldPInfos.createFieldInfo(_reSetP);
            m_point2Ds.add(_2dts.getItem(0));
            m_point2Ds.add(new Point2D(m_newEndPoint.longitude, m_newEndPoint.latitude));
            m_reSet.edit();
            //用于修改记录集当前位置的几何对象，覆盖原来的几何对象，成功则返回true。
            if (!m_reSet.setGeometry(new GeoLine(m_point2Ds))) {
                ToastUtil.showShort(getActivity(), "重新选择终点更新失败");
                return;
            }
            if (!m_reSet.update()) {
                ToastUtil.showShort(getActivity(), "重新选择终点更新失败");
                return;
            }
            m_reSet.edit();
            m_reSet.setString("benExpNum", m_newEndPoint.id);
            m_reSet.setDouble("endLongitude", m_newEndPoint.longitude);
            m_reSet.setDouble("endLatitude", m_newEndPoint.latitude);
            //更改线的长度
            m_reSet.setString("pipeLength", String.format("%.2f", m_reSet.getDouble("SmLength")));
            if (!m_reSet.update()) {
                ToastUtil.showShort(getActivity(), "重新选择终点线更新失败");
                return;
            }
        }

        tvTitle.setText("编辑线" + "(" + gpType + ")");

        //管线材料
        textureList = SpinnerDropdownListManager.getData("lineTexture", gpType);
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, textureList);
        spTextrure.setAdapter(m_adapter);

        //管线备注
        lineRemarkList = SpinnerDropdownListManager.getData("lineRemark", gpType);
        stateList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.state));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, stateList);
        spState.setAdapter(m_adapter);

        embeddedWayList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.embeddedway));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, embeddedWayList);
        spEmbeddedWay.setAdapter(m_adapter);

        if (gpType.equals("煤气-M") || gpType.equals("燃气-R")) {
            pipeSizeList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.pipesizeStandard));
        }

        volatageList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.voltage));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, volatageList);
        spVolatage.setAdapter(m_adapter);
        spVolatage.setSelection(volatageList.size() - 1);

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

        if (gpType.equals("煤气-M") || gpType.equals("燃气-R")) {
            pipeUnitList = getResources().getStringArray(R.array.ownershipUnit_rq_mq);
        } else if (gpType.equals("电信-D")) {
            pipeUnitList = getResources().getStringArray(R.array.ownershipUnit);
        }
        setValueToView();
    }

    private void setValueToView() {
        setStartPoint();         //设置管线点号
        setEndPoint();           //设置连接点号
        setStartBurialDepth();   //设置起点埋深
        setEndBurialDepth();     //设置终点埋深
        setPipeLength();         //设置管线长度
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


    @Override
    public void onStart() {
        super.onStart();
        InitWindowSize.ins().initWindowSize(getActivity(), getDialog());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (m_reSet != null) {
            m_reSet.close();
            m_reSet.dispose();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearReturn:
                getDialog().dismiss();
                break;
            case R.id.btnEdit:
//                setViewEdit();
                break;
            case R.id.tvSubmit:
            case R.id.btnSave:
                checkViewData();
                boolean _result = false;
                _result = DataHandlerObserver.ins().editRecords(generateBaseFieldInfo());
                if (!_result) {
                    ToastUtil.show(getActivity(), "保存线数据失败", Toast.LENGTH_SHORT);
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

            case R.id.btnRemove:
                AlertDialogUtil.showDialog(getActivity(), "警告提示！", "是否确定删除管线？", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (m_reSet.getRecordCount() > 0 && !m_reSet.isEmpty()) {
                            if (m_reSet.delete()) {
                                WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
                                getDialog().dismiss();
                            } else {
                                ToastUtil.show(getActivity(), "删除线失败", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                });
                break;
            //起点与终点交换
            case R.id.imgbtnExchange:
                //起点与终点点号
                String temp = getStartPoint();
                edtStartPoint.setText(getEndPoint());
                edtEndPoint.setText(temp);
                //起点与终点埋深
                temp = edtStartBurialDepth.getText().toString();
                edtStartBurialDepth.setText(edtEndBurialDepth.getText().toString());
                edtEndBurialDepth.setText(temp);
                exchangePoint();

                break;

            //获取起点，保存终点，重新到地图中选取起点
            case R.id.btnGetStartPoint:
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_GetStartPoint);
                getDialog().dismiss();
                break;

            //获取终点，保存起点，重新到地图中选取起点
            case R.id.btnGetEndPoint:
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_GetEndPoint);
                getDialog().dismiss();
                break;

            case R.id.imgvPipeSize:
                if (gpType.equals("煤气-M") || gpType.equals("燃气-R")) {
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
                } else {
                    ToastUtil.showShort(getActivity(), "当前管类没有管径数据列表");
                }
                break;

            //权属单位
            case R.id.imgvOwnershipUnit:

                if (gpType.equals("煤气-M") || gpType.equals("燃气-R") || gpType.equals("电信-D")) {
                    showDialog(pipeUnitList, edtOwnershipUnit, "权属单位列表");

                } else {
                    ToastUtil.showShort(getActivity(), "当前管类没有权属单位数据列表");
                }
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
            default:
                break;
        }
        WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
    }

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

    private void exchangePoint() {
        m_reSet.moveFirst();
        m_reSet.edit();
        //排水、污水、雨水类型要换方向
        GeoLine _geoLine = (GeoLine) m_reSet.getGeometry();
        Point2Ds _point2Ds = _geoLine.getPart(0);
        Point2D _startPoint = _point2Ds.getItem(0);
        Point2D _endPoint = _point2Ds.getItem(1);
        Point2Ds _point2Ds1 = new Point2Ds();
        _point2Ds1.add(_endPoint);
        _point2Ds1.add(_startPoint);

        //更换坐标
        double startLatitude = m_reSet.getDouble("startLatitude");
        double startLongitude = m_reSet.getDouble("startLongitude");
        double endLatitude = m_reSet.getDouble("endLatitude");
        double endLongitude = m_reSet.getDouble("endLongitude");

        m_reSet.setDouble("startLatitude", endLatitude);
        m_reSet.setDouble("startLongitude", endLongitude);
        m_reSet.setDouble("endLatitude", startLatitude);
        m_reSet.setDouble("endLongitude", startLongitude);

        GeoLine _geoLine1 = new GeoLine(_point2Ds1);
        boolean isOk = m_reSet.setGeometry(_geoLine1);
        if (!m_reSet.update()) {
            LogUtills.i("line change point fail");
        }
        _geoLine.dispose();
        _geoLine1.dispose();
    }


    private BaseFieldLInfos generateBaseFieldInfo() {
        BaseFieldLInfos _info = null;
        String gpType = getArguments().getString("gpType");
        _info = LineFieldFactory.CreateInfo(gpType.substring(0, 2));
        if (_info == null) {
            LogUtills.e("DrawPipeLineActivity Create BaseFieldPInfos Fail...");
            return null;
        }

        _info.benExpNum = getStartPoint();
        _info.endExpNum = getEndPoint();
        _info.startLatitude = m_reSet.getDouble("startLatitude");
        _info.startLongitude = m_reSet.getDouble("startLongitude");
        _info.endLatitude = m_reSet.getDouble("endLatitude");
        _info.endLongitude = m_reSet.getDouble("endLongitude");
        _info.exp_Date = m_baseInfo.exp_Date;
        _info.benDeep = getStartBurialDepth();
        _info.endDeep = getEndBurialDepth();
        _info.buried = getEmbeddedWay();
        _info.pipeSize = getPipeSize();
        if (getSectionWidth().isEmpty() || getSectionWidth().length() == 0) {
            _info.d_S = "";
        } else {
            _info.d_S = getSectionWidth() + "X" + getSectionHeight();
        }
        _info.pipeLength = getPipeLength();

       /* if (_info.endExpNum.contains("T_"))
            _info.pipeType = "临时-O";*/

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
        //_info.remark = "";
        //_info.id = ;
        _info.puzzle = getPuzzle();
        _info.burialDifference = getBurialDifference();
        _info.labelTag = String.valueOf(m_smId) + "-" + _info.pipeType.toLowerCase() + "-" + _info.pipeSize.toString() + "-" + _info.d_S + "-" + _info.material.toString();
        _info.sysId = m_smId;

//        LogUtills.i("complete generateBaseFieldInfo.....(ps.x,ps.y),(pe.x,pe.y) =( " + _info.startLongitude + "," + _info.startLatitude + "),(" + _info.endLongitude + "," + _info.endLatitude + ")");
//        LogUtills.i("修改管线Smid = ", String.valueOf(m_smId));
        return _info;


    }

    private void setViewEdit(boolean state) {
        spEmbeddedWay.setEnabled(state);
        spTextrure.setEnabled(state);
        spPressure.setEnabled(state);
        spVolatage.setEnabled(state);
        spState.setEnabled(state);
        btnGetStartPoint.setEnabled(state);

        btnGetEndPoint.setEnabled(state);
        imgbtnExchange.setEnabled(state);
        edtStartBurialDepth.setEnabled(state);
        edtEndBurialDepth.setEnabled(state);
        edtPipeLength.setEnabled(state);
        edtBurialDifference.setEnabled(state);
        edtHoleCount.setEnabled(state);
        edtUsedHoleCount.setEnabled(state);
        edtAmount.setEnabled(state);
        edtAperture.setEnabled(state);
        edtRow.setEnabled(state);
        edtOwnershipUnit.setEnabled(state);
        edtLineRemark.setEnabled(state);
        edtPuzzle.setEnabled(state);
        edtPipeSize.setEnabled(state);
        edtSectionWidth.setEnabled(state);
        edtSectionHeight.setEnabled(state);
//        isEdit = state;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spEmbeddedWay:
                switch (spEmbeddedWay.getSelectedItem().toString()) {
                    case "管埋":
                       
                        layoutPipeSize.setVisibility(View.VISIBLE);
                        layoutSection.setVisibility(View.GONE);
                        break;
                    case "直埋":
                    case "架空":
                        layoutPipeSize.setVisibility(View.VISIBLE);
                        layoutSection.setVisibility(View.GONE);
                        break;
                    case "管块":
                    case "方沟":
                        layoutSection.setVisibility(View.VISIBLE);
                        layoutPipeSize.setVisibility(View.GONE);
                        break;
                        default:break;
                }
                break;
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
            double num2 = Double.parseDouble(num2Str);
            double num1 = Double.parseDouble(num1Str);
            int result = (int) (num1 - num2);
            edtBurialDifference.setText(String.valueOf(result));
        }
    };

    /**
     * 设置管线点号
     */
    @Override
    public void setStartPoint() {

        edtStartPoint.setText(m_startpointID);

    }

    /**
     * 设置连接点号
     */
    @Override
    public void setEndPoint() {
        edtEndPoint.setText(m_endPointID);
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
        if (temp.length() > 0 && !temp.equals("0.00")) {
            int s = Integer.parseInt(temp);
            DecimalFormat df2 = new DecimalFormat("####.00");
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
    public String getEndBurialDepth() {
        String temp = edtEndBurialDepth.getText().toString();
        if (temp.length() > 0 && !temp.equals("0.00")) {
            int s = Integer.parseInt(temp);
            DecimalFormat df2 = new DecimalFormat("####.00");
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
        if (temp.length() > 0) {
            int s = Integer.parseInt(temp);
            DecimalFormat df2 = new DecimalFormat("####.00");
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
        return spTextrure.getSelectedItem().toString() + "";
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
        String _startBurialDepth = m_baseInfo.benDeep;
        if (_startBurialDepth != null) {
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
        String _ednBurialDepth = m_baseInfo.endDeep;
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
        String _pipeLength = m_baseInfo.pipeLength;
        if (_pipeLength != null) {
            edtPipeLength.setText(_pipeLength);
        }
    }

    /**
     * 设置埋深差值
     */
    @Override
    public void setBurialDifference() {
        String _burialDifference = m_baseInfo.burialDifference;
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
        String _embeddedWay = m_baseInfo.buried;
        if (_embeddedWay != null) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spEmbeddedWay, _embeddedWay);
        }
    }

    /**
     * 设置管径
     */
    @Override
    public void setPipeSize() {
        String _pipeSize = m_baseInfo.pipeSize;
        if (_pipeSize != null) {
            edtPipeSize.setText(_pipeSize);
        }
    }

    /**
     * 设置断面宽
     */
    @Override
    public void setSectionWidth() {
        String _sectionW = m_baseInfo.d_S;
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
        String _sectionH = m_baseInfo.d_S;

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
        String _holeCount = m_baseInfo.totalHole;
        if (_holeCount != null) {
            edtHoleCount.setText(_holeCount);
        }
    }

    /**
     * 设置已用孔数
     */
    @Override
    public void setUsedHoleCount() {
        String _usedHoleCount = m_baseInfo.usedHole;
        if (_usedHoleCount != null) {
            edtUsedHoleCount.setText(_usedHoleCount);
        }
    }

    /**
     * 设置电缆总数
     */
    @Override
    public void setAmount() {
        String _amount = m_baseInfo.cabNum;
        if (_amount != null) {
            edtAmount.setText(_amount);
        }
    }

    /**
     * 设置孔径
     */
    @Override
    public void setAperture() {
        String _aperture = m_baseInfo.holeDiameter;
        if (_aperture != null) {
            edtAperture.setText(_aperture);
        }
    }

    /**
     * 设置行
     */
    @Override
    public void setRow() {
        String _row = m_baseInfo.rowXCol;
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
        String _col = m_baseInfo.rowXCol;
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
        String _voltage = m_baseInfo.voltage;
        if (_voltage != null) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spVolatage, _voltage);
        }
    }

    /**
     * 设置管线状态
     */
    @Override
    public void setState() {
        String _state = m_baseInfo.state;
        if (_state != null) {

            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spState, _state);
        }
    }

    /**
     * 设置管道压力
     */
    @Override
    public void setPressure() {
        String _pressure = m_baseInfo.pressure;
        if (_pressure != null) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spPressure, _pressure);
        }
    }

    /**
     * 设置权属单位
     */
    @Override
    public void setOwnershipUnit() {
        String _ownerShipUnit = m_baseInfo.belong;
        if (_ownerShipUnit != null) {
            edtOwnershipUnit.setText(_ownerShipUnit);
        }
    }

    /**
     * 设置线备注
     */
    @Override
    public void setLineRemark() {
        String _remark = m_baseInfo.remark;
        if (_remark != null) {
            edtLineRemark.setText(_remark);
        }

    }

    /**
     * 设置疑难问题
     */
    @Override
    public void setPuzzle() {
        String _puzzle = m_baseInfo.puzzle;
        if (_puzzle != null) {
            edtPuzzle.setText(_puzzle);
        }
    }

    /**
     * 设置管线材质
     */
    @Override
    public void setPipeTexture() {
        String _pipeTexture = m_baseInfo.material;
        if (_pipeTexture != null) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spTextrure, _pipeTexture);
        }
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

    @Override
    public void onResume() {
        super.onResume();
    }

}
