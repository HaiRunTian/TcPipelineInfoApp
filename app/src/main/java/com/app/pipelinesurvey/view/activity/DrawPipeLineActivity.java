package com.app.pipelinesurvey.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.app.BaseInfo.Data.Line.TheTotalLine;
import com.app.BaseInfo.Data.LineFieldFactory;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.bean.PipeLineInfo;
import com.app.pipelinesurvey.bean.PointConfig;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.iview.IDrawPipeLineView;
import com.app.pipelinesurvey.view.iview.IQueryPipeLineView;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.mapping.Layer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DrawPipeLineActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, IDrawPipeLineView, IQueryPipeLineView {
    private TextView tvTitle;//标题
    private TextView tvSubmit;//提交
    private Button btnPreviousOne;//导入上一个数据
    private LinearLayout linearReturn;//返回
    private LinearLayout layoutLineMark;//管线备注
    private Spinner spEmbeddedWay;//埋设方式
    private Spinner spTextrure;//管材
    //    private Spinner spLineRemark;//线备注
    private Spinner spVolatage;//电压
    private Spinner spPressure;//压力
    private Spinner spState;//状态
    private EditText edtStartPoint;//终点点号
    private EditText edtEndPoint;//终点点号
    private EditText edtStartBurialDepth;//起点埋深
    private EditText edtEndBurialDepth;//终点埋深
    private EditText edtPipeLength;//管段长度
    private EditText edtBurialDifference;//埋深差值
    private EditText edtHoleCount;//总孔数
    private EditText edtUsedHoleCount;//已用孔数
    private EditText edtAmount;//电缆总数
    private EditText edtAperture;//孔径
    private EditText edtRow;//行
    private EditText edtCol;//列
    private EditText edtOwnershipUnit;//权属单位
    private EditText edtLineRemark;//线备注
    private EditText edtPuzzle;//疑难
    private EditText edtPipeSize;//管径
    private EditText edtSectionWidth;//宽
    private EditText edtSectionHeight;//高
    private ImageView imgvLineRemark;//下拉框按钮
    private LinearLayout layoutPipeSize, layoutSection;//管径 断面
    private ImageButton imgbtnExchange;//交换点号下拉按钮
    private ImageView imgvOwnershipUnit;//权属下拉按钮
    private ImageView imgvPipeSize;//管径下拉按钮
    private LinearLayout layoutOwnershipUnit;//交换点号
    private LinearLayout layoutDLLDPanel;//电力路灯面板
    private LinearLayout layoutPipeSizeEdt;//管径编辑布局
    private List<String> stateList;//状态数据
    private List<String> lineRemarkList;//线备注数据
    private List<String> embeddedWayList;//埋设方式数据
    private List<String> volatageList;//电压数据
    private List<String> pressureList;//压力数据
    private List<String> textureList;//管材数据
    private List<String> pipeSizeList;//管径数据
//    private List<String> pipeUnitList;//权属单位

    private String[] pipeUnitList;//权属单位
    //    private List<String> voltageList;//线备注数据
    private ArrayAdapter<String> m_adapter;//下拉数据适配器
    private ListPopupWindow _popupWindow;//下拉弹窗
    private String gpType;//管类
    private BaseFieldPInfos m_startPInfo = null;  //起点
    private BaseFieldPInfos m_endPInfo = null;  //终点
    private int m_smId = -1;
    private Button btnSave; //保存
    private BaseFieldLInfos m_baseFileLInfo;
//    private SharedPreferences.Editor m_editor;
    private StringBuffer m_stringBuffer;
    private int m_endSmId;
    private String m_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_pipe_line);
        LogUtills.i("DrawPipeLineActivity", "onCreate()" + this.toString());

        initView();
        initValue();
        initData();
    }

    private void initValue() {
//        SharedPreferences _sharedPreferences = getSharedPreferences("depth", MODE_PRIVATE);
//        String depth = _sharedPreferences.getString("pointDepth", "");
//        if (!depth.isEmpty()) setStartBurialDepth(depth);

        //管线类型
        gpType = getIntent().getStringExtra("gpType");
        m_code = gpType.substring(gpType.length() - 1);
        tvTitle.setText("加线" + "(" + gpType + ")");

        textureList = SpinnerDropdownListManager.getData("lineTexture", gpType);
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, textureList);
        spTextrure.setAdapter(m_adapter);
        lineRemarkList = SpinnerDropdownListManager.getData("lineRemark", gpType);
        stateList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.state));
        embeddedWayList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.embeddedway));

        if (gpType.equals("煤气-M") || gpType.equals("燃气-R"))
            //管径
            pipeSizeList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.pipesizeStandard));

        if (gpType.equals("煤气-M") || gpType.equals("燃气-R")) {
            pipeUnitList = getResources().getStringArray(R.array.ownershipUnit_rq_mq);
        } else if (gpType.equals("电信-D")) {
            pipeUnitList = getResources().getStringArray(R.array.ownershipUnit);
        }
        //电压数据
        volatageList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.voltage));
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, volatageList);
        spVolatage.setAdapter(m_adapter);
        spVolatage.setSelection(volatageList.size() - 1);
        //压力数据
        pressureList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.pressure));
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pressureList);
        spPressure.setAdapter(m_adapter);
        spPressure.setSelection(pressureList.size() - 1);

        if (gpType.equals("电力-L") || gpType.equals("路灯-S")//显示隐藏属性面板
                || gpType.equals("电信-D") || gpType.equals("有视-T")
                || gpType.equals("军队-B") || gpType.equals("交通-X")) {
            layoutDLLDPanel.setVisibility(View.VISIBLE);
        } else {
            layoutDLLDPanel.setVisibility(View.GONE);
        }
    }

    private void initData() {
        int _beginSmId = getIntent().getIntExtra("beginSmId", -1);  //起点点号
        //终点点号
        m_endSmId = getIntent().getIntExtra("endSmId", -1);
        m_smId = getIntent().getIntExtra("smId", -1);

        Layer _pLayer = DataHandlerObserver.ins().getTotalPtLayer(); //获取当前点图层
        Layer _pEndLayer = DataHandlerObserver.ins().getTotalLrLayer(); //获取当前点图层
        if (_pLayer == null || _pEndLayer == null) {
            LogUtills.i("Initial DrawPipeLineActivity Get Point Layer Or Line Layer Faild...");
            finish();
        }

        Recordset _startReset = DataHandlerObserver.ins().queryRecordsetBySmid(_beginSmId, true, false);
        _startReset.moveFirst();
        m_startPInfo = BaseFieldPInfos.createFieldInfo(_startReset);  //记录集转为管点类
        if (m_startPInfo == null) {
            LogUtills.e("Initial DrawPipeLineActivity Create Start BaseFieldPInfos fail, Smid:" + _beginSmId);
            _startReset.close();
            _startReset.dispose();
            finish();
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
            finish();
        }

        //获取起点与终点X、Y
        double _sX = _startReset.getDouble("smX");
        double _sY = _startReset.getDouble("smY");
        double _eX = _endReset.getDouble("smX");
        double _eY = _endReset.getDouble("smY");

        double _dx = _sX - _eX;
        double _dy = _sY - _eY;
        double _len = Math.sqrt(_dx * _dx + _dy * _dy);

        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_POINT_NAME_CONFIG);
        int _tempL = 0;
        if (_cursor.moveToNext()) {
             _tempL = _cursor.getInt(_cursor.getColumnIndex("PipeLength"));
        }
        if (_len > _tempL) {
            ToastUtil.showShort(DrawPipeLineActivity.this, "管线长度超出自定义长度" + _tempL +"米");
        }
        edtStartPoint.setText(m_startPInfo.id);  //起点点号
        edtEndPoint.setText(m_endPInfo.id);      //终点点号
//
        int _burialDiffer = 0;
        //算出埋深差值
        if (m_endPInfo.depth.length() > 0 || m_startPInfo.depth.length() > 0)
            _burialDiffer = Integer.parseInt(m_endPInfo.depth) - Integer.parseInt(m_startPInfo.depth);
        edtBurialDifference.setText(String.valueOf(Math.abs(_burialDiffer)));//埋深差值
        edtPipeLength.setText(String.format("%.2f", _len));
        edtLineRemark.setText(PipeLineInfo.getPipeLineInfo().getLineRemark());//线备注
        edtPuzzle.setText(PipeLineInfo.getPipeLineInfo().getPuzzle());//疑难
        edtPipeSize.setText(m_startPInfo.wellCoverSize.toString());//管径
        _startReset.close();
        _endReset.close();
        _startReset.dispose();
        _endReset.dispose();
        //导入上一条管线数据 同类管相同就导入，否则不导入
        importData();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        linearReturn = (LinearLayout) findViewById(R.id.linearReturn);
        linearReturn.setOnClickListener(this);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        spEmbeddedWay = (Spinner) findViewById(R.id.spEmbeddedWay);
        spEmbeddedWay.setOnItemSelectedListener(this);
        spTextrure = (Spinner) findViewById(R.id.spTextrure);
        //        spLineRemark = findViewById(R.id.spLineRemark);
        //        spLineRemark.setOnItemSelectedListener(this);
        spVolatage = (Spinner) findViewById(R.id.spVoltage);
        spPressure = (Spinner) findViewById(R.id.spPressure);
        spState = (Spinner) findViewById(R.id.spState);
        layoutPipeSize = (LinearLayout) findViewById(R.id.layoutPipeSize);
        layoutSection = (LinearLayout) findViewById(R.id.layoutSection);
        layoutDLLDPanel = (LinearLayout) findViewById(R.id.layoutDLLDPanel);
        edtBurialDifference = (EditText) findViewById(R.id.edtBurialDifference);
        edtStartBurialDepth = (EditText) findViewById(R.id.edtStartBurialDepth);
        edtStartBurialDepth.addTextChangedListener(m_watcher);
        edtEndBurialDepth = (EditText) findViewById(R.id.edtEndBurialDepth);
        edtEndBurialDepth.addTextChangedListener(m_watcher);
        edtLineRemark = (EditText) findViewById(R.id.edtLineRemark);
        imgbtnExchange = (ImageButton) findViewById(R.id.imgbtnExchange);
        imgbtnExchange.setOnClickListener(this);
        edtStartPoint = (EditText) findViewById(R.id.edtStartPoint);
        edtEndPoint = (EditText) findViewById(R.id.edtEndPoint);
        imgvLineRemark = (ImageView) findViewById(R.id.imgvLineRemark);
        imgvLineRemark.setOnClickListener(this);
        layoutLineMark = (LinearLayout) findViewById(R.id.layoutLineMark);
        imgvOwnershipUnit = (ImageView) findViewById(R.id.imgvOwnershipUnit);
        imgvOwnershipUnit.setOnClickListener(this);
        edtOwnershipUnit = (EditText) findViewById(R.id.edtOwnershipUnit);
        edtPipeSize = (EditText) findViewById(R.id.edtPipeSize);
        edtSectionWidth = (EditText) findViewById(R.id.edtSectionWidth);
        edtSectionHeight = (EditText) findViewById(R.id.edtSectionHeight);
        edtPipeLength = (EditText) findViewById(R.id.edtPipeLength);
        edtHoleCount = (EditText) findViewById(R.id.edtHoleCount);
        edtUsedHoleCount = (EditText) findViewById(R.id.edtUsedHoleCount);
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        edtAperture = (EditText) findViewById(R.id.edtAperture);
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        edtRow = (EditText) findViewById(R.id.edtRow);
        edtCol = (EditText) findViewById(R.id.edtCol);
        edtPuzzle = (EditText) findViewById(R.id.edtPuzzle);

        imgvPipeSize = (ImageView) findViewById(R.id.imgvPipeSize);
        imgvPipeSize.setOnClickListener(this);
        layoutPipeSizeEdt = (LinearLayout) findViewById(R.id.layoutPipeSizeEdt);
        btnPreviousOne = (Button) findViewById(R.id.btnPreviousOne);
        btnPreviousOne.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.btnRemove);
        btnSave.setOnClickListener(this);
//        m_editor = getSharedPreferences("depth", MODE_PRIVATE).edit();
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.linearReturn:
                    if (m_endPInfo.exp_Num.contains("T_")) {
                        Recordset _endReset = DataHandlerObserver.ins().queryRecordsetBySmid(m_endSmId, true, true);
                        _endReset.moveFirst();
                        if (_endReset.delete()) {
                            LogUtills.i("DrawPipeLineActivity", "成功删除当前记录");
                        }
                        _endReset.close();
                        _endReset.dispose();
                    }

                    finish();
                    break;
                //保存 提交 提交前判断有些选项数据是否合理
                case R.id.btnRemove:
                case R.id.tvSubmit:
                    boolean _result = false;
//                if (checkOutValue()) {
                    _result = DataHandlerObserver.ins().addRecords(generateBaseFieldInfo());
//                }
                    if (!_result) {
                        ToastUtil.showShort(this.getBaseContext(), "保存点数据失败...");
                        return;
                    }else {
                        ToastUtil.show(this.getBaseContext(), "添加线成功", Toast.LENGTH_SHORT);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
                                    WorkSpaceUtils.getInstance().getWorkspace().save();
//                                        WorkSpaceUtils.getInstance().getWorkspace().dispose();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtil.show(DrawPipeLineActivity.this, e.toString(), Toast.LENGTH_SHORT);
                                }
                            }
                        }).start();
                    }
//                    m_editor.putString("pointDepth", getEndBurialDepth());
//                    m_editor.commit();
                    finish();
                    break;

                //起点与终点交换
                case R.id.imgbtnExchange:
                    //起点和终点交换，线方向改变
                    String temp = getStartPoint();
                    edtStartPoint.setText(getEndPoint());
                    edtEndPoint.setText(temp);

                    BaseFieldPInfos tempP = m_startPInfo;
                    m_startPInfo = m_endPInfo;
                    m_endPInfo = tempP;

                    temp = edtStartBurialDepth.getText().toString();
                    edtStartBurialDepth.setText(edtEndBurialDepth.getText().toString());
                    edtEndBurialDepth.setText(temp);
                    break;

                case R.id.imgvLineRemark:
                    _popupWindow = new ListPopupWindow(this);
                    _popupWindow.setWidth(layoutLineMark.getWidth() - 5);
                    m_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lineRemarkList);
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
//                if (gpType.equals("电信-D") || gpType.equals("煤气-M") || gpType.equals("燃气-R")) {
//                    Intent _intent = new Intent(this, LayersActivity.class);
//                    _intent.putExtra("from", DrawPipeLineActivity.class.getSimpleName());
//                    startActivityForResult(_intent, 1);
//                } else {
//                    ToastUtil.showShort(this, "当前管类没有权属单位列表");
//                }

                    if (gpType.equals("煤气-M") || gpType.equals("燃气-R") || gpType.equals("电信-D")) {
                        showDialog(pipeUnitList, edtOwnershipUnit, "权属单位列表");
//                    m_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pipeUnitList);
//                    _popupWindow = new ListPopupWindow(this);
//                    _popupWindow.setWidth(layoutPipeSizeEdt.getWidth() - 5);
//                    _popupWindow.setAdapter(m_adapter);
//                    _popupWindow.setAnchorView(edtPipeSize);
//                    _popupWindow.setModal(true);
//                    _popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            edtOwnershipUnit.setText(pipeUnitList[position]);
//                            _popupWindow.dismiss();
//                        }
//                    });
                        _popupWindow.show();
                    } else {
                        ToastUtil.showShort(this, "当前管类没有权属单位数据列表");
                    }
                    break;
                case R.id.imgvPipeSize:
                    if (gpType.equals("煤气-M") || gpType.equals("燃气-R")) {
                        _popupWindow = new ListPopupWindow(this);
                        _popupWindow.setWidth(layoutPipeSizeEdt.getWidth() - 5);
                        m_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pipeSizeList);
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
                        ToastUtil.showShort(this, "当前管类没有管径数据列表");
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void importData() {
        QueryParameter _parameter = new QueryParameter();
        _parameter.setAttributeFilter("code = '" + m_code + "' and (benExpNum = '" + getStartPoint() +"' or endExpNum = '" + getStartPoint() + "')");
        _parameter.setCursorType(CursorType.STATIC);
        _parameter.setOrderBy(new String[]{"SmID asc"});
        Recordset reSet = DataHandlerObserver.ins().QueryRecordsetByParameter(_parameter, false);
        if (!reSet.isEmpty()) {
            reSet.moveLast();
            m_baseFileLInfo = BaseFieldLInfos.createFieldInfo(reSet);
            setValueToView();
        } else {
            ToastUtil.showShort(this, "您未录入过此类型管点数据");
        }

        reSet.close();
        reSet.dispose();
    }


    private void setValueToView() {

//        setStartPoint();         //设置管线点号
//        setEndPoint();           //设置连接点号
        setStartBurialDepth();   //设置起点埋深
//        setEndBurialDepth();     //设置终点埋深
//        setPipeLength();         //设置管线长度
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

    private void importDataToView(EditText view, String str) {
        if (str.length() > 0) {
            view.setText(str);
        } else view.setText("");
    }

    @Nullable
    private BaseFieldLInfos generateBaseFieldInfo() {
        BaseFieldLInfos _info = null;
        try {


        String gpType = getIntent().getStringExtra("gpType");
        _info = LineFieldFactory.CreateInfo(gpType.substring(0, 2));
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
        _info.benDeep = getStartBurialDepth();  //m_startPInfo.startDirDepth;
        _info.endDeep = getEndBurialDepth();//m_startPInfo.endDirDepth;
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
        if (getEndPoint().contains("T_")){
            _info.labelTag ="-"+"临时-" + _info.pipeSize.toString() + _info.d_S + "-" + _info.material.toString();
        }else {
            _info.labelTag ="-"+ m_code.toLowerCase() + "-" + _info.pipeSize.toString() + _info.d_S + "-" + _info.material.toString();
        }
        }catch (Exception e){
            LogUtills.i(e.getMessage());
        }

        //LogUtills.i("complete generateBaseFieldInfo.....(ps.x,ps.y),(pe.x,pe.y) =( " + _info.startLongitude + "," + _info.startLatitude + "),(" + _info.endLongitude + "," + _info.endLatitude + ")");
        return _info;


      /*  _info.exp_Num = getGPId();
        _info.pipeType = _info.datasetName;
        _info.feature = getFeaturePoints();
        _info.subsid = getAppendant();
        _info.pipeOffset = getOffset();
        _info.wellSize = getWellSize();
        _info.wellDeep = getWellDepth();
        _info.wellWater = getWellWater();
        _info.wellMud = getWellMud();
        _info.road = getRoadName();
        _info.state = getState();
      *//*  SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        LogUtills.i(new Date().toString());*//*
        _info.exp_Date = new Date().toString();//format1.format(new Date()).toString();
        _info.wellCoverMaterial = getWellLidTexture();
        _info.wellCoverSize = getWellLidSize();
        _info.buildingStructures = getBuildingStructures();
        *//*_info.longitude = a;
        _info.latitude = a;*//*
        _info.surf_H = getElevation();
        _info.expGroup = "";
        _info.remark = getPointRemark();
        _info.picture = "";
        //_info.id = a;
        _info.puzzle = getPuzzle();
        _info.situation = getSituation();
     *//*   _info.startDirDepth = a;
        _info.endDirDepth = a;*/

    }

    private void saveTemplet() {
        //        PipeLineInfo.getPipeLineInfo().setStartPoint(getStartPoint());
        //        PipeLineInfo.getPipeLineInfo().setEndPoint(getEndPoint());
        PipeLineInfo.getPipeLineInfo().setStartBurialDepth(getStartBurialDepth());
        PipeLineInfo.getPipeLineInfo().setEndBurialDepth(getEndBurialDepth());
        //        PipeLineInfo.getPipeLineInfo().setPipeLength(getPipeLength());
        PipeLineInfo.getPipeLineInfo().setBurialDifference(getBurialDifference());
        PipeLineInfo.getPipeLineInfo().setEmbeddedWay(getEmbeddedWay());
        PipeLineInfo.getPipeLineInfo().setTextrure(getTextrure());
        PipeLineInfo.getPipeLineInfo().setHoleCount(getHoleCount());
        PipeLineInfo.getPipeLineInfo().setUsedHoleCount(getUsedHoleCount());
        PipeLineInfo.getPipeLineInfo().setAmount(getAmount());
        PipeLineInfo.getPipeLineInfo().setAperture(getAperture());
        PipeLineInfo.getPipeLineInfo().setRow(getRow());
        PipeLineInfo.getPipeLineInfo().setCol(getCol());
        PipeLineInfo.getPipeLineInfo().setVoltage(getVoltage());
        PipeLineInfo.getPipeLineInfo().setState(getState());
        PipeLineInfo.getPipeLineInfo().setPressure(getPressure());
        PipeLineInfo.getPipeLineInfo().setOwnershipUnit(getOwnershipUnit());
        PipeLineInfo.getPipeLineInfo().setLineRemark(getLineRemark());
        PipeLineInfo.getPipeLineInfo().setPuzzle(getPuzzle());
        PipeLineInfo.getPipeLineInfo().setPipeSize(getPipeSize());
        PipeLineInfo.getPipeLineInfo().setSectionWidth(getSectionWidth());
        PipeLineInfo.getPipeLineInfo().setSectionHeight(getSectionHeight());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spEmbeddedWay:
                switch (spEmbeddedWay.getSelectedItem().toString()) {
                    case "管埋":
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
            if (num1Str.length() == 0)
                num1Str = "0";
            if (num2Str.length() == 0)
                num2Str = "0";

//            int num2 = Integer.parseInt(num2Str);
//            int num1 = Integer.parseInt(num1Str);
            int num2 = Integer.parseInt(num2Str);
            int num1 = Integer.parseInt(num1Str);
            int result = num2 - num1;
            edtBurialDifference.setText(String.valueOf(Math.abs(result)));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
//        LogUtills.i("起点埋深", df2.format(s / 100d));
            if (temp.length() > 2) {
//                LogUtills.i("起点埋深", df2.format(s / 100d));
                return df2.format(s / 100d);
            } else {
//                LogUtills.i("起点埋深", "0" + df2.format(s / 100d));
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
//        LogUtills.i("终点埋深", df2.format(s / 100d));
            if (temp.length() > 2) {
//                LogUtills.i("终点埋深", df2.format(s / 100d));
                return df2.format(s / 100d);
            } else {
//                LogUtills.i("终点埋深", "0" + df2.format(s / 100d));
                return "0" + df2.format(s / 100d);
            }
        } else
            return "0";

    }

    @Override
    public String getPipeLength() {
        return edtPipeLength.getText().toString();
    }

    @Override
    public String getBurialDifference() {

        String temp = edtBurialDifference.getText().toString();
        int s = Integer.parseInt(temp);
        DecimalFormat df2 = new DecimalFormat("###.00");

        if (temp.length() > 2) {
//            LogUtills.i("埋深差值", df2.format(s / 100d));
            return df2.format(s / 100d);
        } else if (temp.length() > 0) {
//            LogUtills.i("埋深差值", "0" + df2.format(s / 100d));
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
        String _startBurialDepth = m_baseFileLInfo.endDeep;
        if (_startBurialDepth != null) {
//            LogUtills.i("i起点埋深 = ", _startBurialDepth);
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
//            LogUtills.i("终点埋深 = ", _ednBurialDepth);
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
//            LogUtills.i("i管线长度 = ", _pipeLength);

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
//            LogUtills.i("i埋深差值 = ", _burialDifference);
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
//            LogUtills.i("i埋设方式 = ", _embeddedWay);
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
//            LogUtills.i("i管径大小 = ", _pipeSize);
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
//            LogUtills.i("i断面宽 = ", _temp);
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
//            LogUtills.i("i断面高 = ", _temp);
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
     * 管线条件设置
     *
     * @author HaiRun
     * created at 2018/12/4 8:53
     */
    public boolean checkOutValue() {
        String _textrure = spTextrure.getSelectedItem().toString();  //管材
        String _embeddeWay = spEmbeddedWay.getSelectedItem().toString();  //埋设方式
        String _holeCount = getHoleCount();//总孔数
        String _useHoleCount = getUsedHoleCount();//已用孔数
        String _pipeSize = getPipeSize();//管径
        String _amount = getAmount(); //电缆数

        if (gpType.equals("电力-L") || gpType.equals("电信-D") || gpType.equals("路灯-S")) {
            //	3电力、电信、路灯管材不为空管、空沟。电缆根数不能为O或空值，出现时提示。
            if (!_textrure.equals("空管") || !_textrure.equals("空沟")) {
                if (_amount.equals("0") || _amount.isEmpty()) {
                    ToastUtil.show(DrawPipeLineActivity.this, "电缆根数不能为空或者为0或空值", Toast.LENGTH_SHORT);
                    edtAmount.setError("必填项不能为空或0");
                    return false;
                }
            } else { //	4.电力、电信、路灯管材为空管、空沟时电缆根数为O，出现不为0时提示。
                edtAmount.setText("0");

            }

            //6、电力、电信、路灯埋设方式为架空、直埋时管径不为空值，总孔数、已用孔为空值。电缆根数为0或空值。
            // 注意架空和直埋时管材不能为空管或空值。出现这些问题时提示。
            if (_embeddeWay.equals("架空") || _embeddeWay.equals("直埋")) {
                if (!_pipeSize.isEmpty()) {
//                    ToastUtil.show(DrawPipeLineActivity.this, "管径不能为空", Toast.LENGTH_SHORT);
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
                    ToastUtil.show(DrawPipeLineActivity.this, "管材不能为空管", Toast.LENGTH_SHORT);
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
                    ToastUtil.show(DrawPipeLineActivity.this, "电压不能为空或者为0", Toast.LENGTH_SHORT);
//                        edtAmount.setError("必填项且不能为0");
                }
            } else {
                spVolatage.setSelection(0, false);
            }
        }
        return true;
    }

    private void showDialog(final String[] data, final TextView textView, String title) {
        textView.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
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
                    sb.append(data[which] + "#");
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
    public void onBackPressed() {
        super.onBackPressed();
        if (m_endPInfo.exp_Num.contains("T_")) {
            Recordset _endReset = DataHandlerObserver.ins().queryRecordsetBySmid(m_endSmId, true, true);
            _endReset.moveFirst();
            if (_endReset.delete()) {
            }
            _endReset.close();
            _endReset.dispose();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
