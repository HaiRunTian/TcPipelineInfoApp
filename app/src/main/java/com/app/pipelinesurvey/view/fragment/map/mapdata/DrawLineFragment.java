package com.app.pipelinesurvey.view.fragment.map.mapdata;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.LineFieldFactory;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.BaseInfo.Oper.OperSql;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.PipeLineInfo;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.fragment.map.ps.DrawPsLineFragment;
import com.app.pipelinesurvey.view.iview.IDrawPipeLineView;
import com.app.pipelinesurvey.view.iview.IQueryPipeLineView;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.mapping.Layer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private AutoCompleteTextView edtPipeSize;
    /**
     * 宽
     */
    private AutoCompleteTextView edtSectionWidth;
    /**
     * 高
     */
    private AutoCompleteTextView edtSectionHeight;
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
//    private ImageView imgvPipeSize;
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
//    private LinearLayout layoutPipeSizeEdt;
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
    /**
     * 起点
     */
    private BaseFieldPInfos m_startPInfo = null;
    /**
     * 终点
     */
    private BaseFieldPInfos m_endPInfo = null;
    private Button btnSave;
    /**
     * 线
     */
    private BaseFieldLInfos m_baseFileLInfo;
    private StringBuffer m_stringBuffer;
    private int m_endSmId;
    private String m_code;
    private View m_view;
    private TextView tvOwnershipUnit;
    private TextView tvVoltage;
    private TextView tvHoleCount;
    private TextView tvUsedHole;
    private TextView tvAmount;
    private EditText edtTopBottom;
    //起点高程
    private int startH;
    //终点高程
    private int endH;
    private Button btnAddPs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
//        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.activity_draw_pipe_line, container, false);
        initView(m_view);
        initLayoutView(m_view);
        return m_view;
    }

    /**
     * 初始化每一行view,根据数据库表用户设置是否显示
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/8/23  16:58
     */
    private void initLayoutView(View m_view) {
        View layout_start_depth = m_view.findViewById(R.id.layout_start_depth);
        View layout_end_depth = m_view.findViewById(R.id.layout_end_depth);
        View layout_pipe_length = m_view.findViewById(R.id.layout_pipe_length);
        View layout_bureal_diff = m_view.findViewById(R.id.layout_bureal_diff);
        View layout_embedded_way = m_view.findViewById(R.id.layout_embedded_way);
        View layout_texture = m_view.findViewById(R.id.layout_texture);
        View layoutPipeSize = m_view.findViewById(R.id.layoutPipeSize);
        View layoutSection = m_view.findViewById(R.id.layoutSection);
        View layout_hole_count = m_view.findViewById(R.id.layout_hole_count);
        View layout_used_count = m_view.findViewById(R.id.layout_used_count);
        View layout_amount = m_view.findViewById(R.id.layout_amount);
        View layout_aperture = m_view.findViewById(R.id.layout_aperture);
        View layout_row_col = m_view.findViewById(R.id.layout_row_col);
        View layout_voltage = m_view.findViewById(R.id.layout_voltage);
        View layout_state = m_view.findViewById(R.id.layout_state);
        View layout_pressure = m_view.findViewById(R.id.layout_pressure);
        View layout_owner_ship_unit = m_view.findViewById(R.id.layout_owner_ship_unit);
        View layout_line_remark = m_view.findViewById(R.id.layout_line_remark);
        View layout_puzzle = m_view.findViewById(R.id.layout_puzzle);

        //TODO  根据数据库表 判断是否显示view
        String type = getArguments().getString("gpType");
        Cursor cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_LINE_SETTING, "where prj_name = '"
                + SuperMapConfig.PROJECT_NAME + "' and pipetype = '" + type + "'");
        while (cursor.moveToNext()) {
            int start_depth = cursor.getInt(cursor.getColumnIndex("start_depth"));
            int end_depth = cursor.getInt(cursor.getColumnIndex("end_depth"));
            int pipe_length = cursor.getInt(cursor.getColumnIndex("pipe_length"));
            int bureal_diff = cursor.getInt(cursor.getColumnIndex("bureal_diff"));
            int embedded_way = cursor.getInt(cursor.getColumnIndex("embedded_way"));
            int texture = cursor.getInt(cursor.getColumnIndex("texture"));
            int pipe_size = cursor.getInt(cursor.getColumnIndex("pipe_size"));
            int secttion = cursor.getInt(cursor.getColumnIndex("secttion"));
            int hole_count = cursor.getInt(cursor.getColumnIndex("hole_count"));
            int used_count = cursor.getInt(cursor.getColumnIndex("used_count"));
            int amount = cursor.getInt(cursor.getColumnIndex("amount"));
            int aperture = cursor.getInt(cursor.getColumnIndex("aperture"));
            int row_col = cursor.getInt(cursor.getColumnIndex("row_col"));
            int voltage = cursor.getInt(cursor.getColumnIndex("voltage"));
            int state = cursor.getInt(cursor.getColumnIndex("state"));
            int pressure = cursor.getInt(cursor.getColumnIndex("pressure"));
            int owner_ship_unit = cursor.getInt(cursor.getColumnIndex("owner_ship_unit"));
            int line_remark = cursor.getInt(cursor.getColumnIndex("line_remark"));
            int puzzle = cursor.getInt(cursor.getColumnIndex("puzzle"));

            layout_start_depth.setVisibility(start_depth == 1 ? View.VISIBLE : View.GONE);
            layout_end_depth.setVisibility(end_depth == 1 ? View.VISIBLE : View.GONE);
            layout_pipe_length.setVisibility(pipe_length == 1 ? View.VISIBLE : View.GONE);
            layout_bureal_diff.setVisibility(bureal_diff == 1 ? View.VISIBLE : View.GONE);
            layout_embedded_way.setVisibility(embedded_way == 1 ? View.VISIBLE : View.GONE);
            layout_texture.setVisibility(texture == 1 ? View.VISIBLE : View.GONE);
            layoutPipeSize.setVisibility(pipe_size == 1 ? View.VISIBLE : View.GONE);
            layoutSection.setVisibility(secttion == 1 ? View.VISIBLE : View.GONE);
            layout_hole_count.setVisibility(hole_count == 1 ? View.VISIBLE : View.GONE);
            layout_used_count.setVisibility(used_count == 1 ? View.VISIBLE : View.GONE);
            layout_amount.setVisibility(amount == 1 ? View.VISIBLE : View.GONE);
            layout_aperture.setVisibility(aperture == 1 ? View.VISIBLE : View.GONE);
            layout_row_col.setVisibility(row_col == 1 ? View.VISIBLE : View.GONE);
            layout_voltage.setVisibility(voltage == 1 ? View.VISIBLE : View.GONE);
            layout_state.setVisibility(state == 1 ? View.VISIBLE : View.GONE);
            layout_pressure.setVisibility(pressure == 1 ? View.VISIBLE : View.GONE);
            layout_owner_ship_unit.setVisibility(owner_ship_unit == 1 ? View.VISIBLE : View.GONE);
            layout_line_remark.setVisibility(line_remark == 1 ? View.VISIBLE : View.GONE);
            layout_puzzle.setVisibility(puzzle == 1 ? View.VISIBLE : View.GONE);

        }
        cursor.close();
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
        edtPipeSize = (AutoCompleteTextView) view.findViewById(R.id.edtPipeSize);
        edtSectionWidth = (AutoCompleteTextView) view.findViewById(R.id.edtSectionWidth);
        edtSectionHeight = (AutoCompleteTextView) view.findViewById(R.id.edtSectionHeight);
        edtPipeLength = (EditText) view.findViewById(R.id.edtPipeLength);
        edtHoleCount = (EditText) view.findViewById(R.id.edtHoleCount);
        edtUsedHoleCount = (EditText) view.findViewById(R.id.edtUsedHoleCount);
        edtAmount = (EditText) view.findViewById(R.id.edtAmount);
        edtAperture = (EditText) view.findViewById(R.id.edtAperture);
        edtAmount = (EditText) view.findViewById(R.id.edtAmount);
        edtRow = (EditText) view.findViewById(R.id.edtRow);
        edtCol = (EditText) view.findViewById(R.id.edtCol);
        edtPuzzle = (EditText) view.findViewById(R.id.edtPuzzle);
//        imgvPipeSize = (ImageView) view.findViewById(R.id.imgvPipeSize);
//        imgvPipeSize.setOnClickListener(this);
//        layoutPipeSizeEdt = (LinearLayout) view.findViewById(R.id.layoutPipeSizeEdt);
        btnPreviousOne = (Button) view.findViewById(R.id.btnPreviousOne);
        btnPreviousOne.setOnClickListener(this);
        btnSave = (Button) view.findViewById(R.id.btnRemove);
        btnSave.setOnClickListener(this);
        //管底高差
        edtTopBottom = view.findViewById(R.id.edtTopBottom);
        //设置必填项*号
        //权属单位
        tvOwnershipUnit = view.findViewById(R.id.tvOwnershipUnit);
        tvVoltage = view.findViewById(R.id.tvVoltage);
        tvHoleCount = view.findViewById(R.id.tvHoleCount);
        tvUsedHole = view.findViewById(R.id.tvUsedHole);
        tvAmount = view.findViewById(R.id.tvAmount);
        btnAddPs = view.findViewById(R.id.btnPs);
        btnAddPs.setOnClickListener(this);
        if ("1".equals(SuperMapConfig.PS_OUT_CHECK)) {
            btnAddPs.setVisibility(View.VISIBLE);
        }
        //起点点号
        TextView tvStartPoint = view.findViewById(R.id.tvStartPoint);
        setViewDrawable(tvStartPoint);
        //终点点号
        TextView tvEndPoint = view.findViewById(R.id.tvEndPoint);
        setViewDrawable(tvEndPoint);
        //起点埋深
        TextView tvStartDepth = view.findViewById(R.id.tvStartDepth);
        setViewDrawable(tvStartDepth);
        //终点埋深
        TextView tvEndDepth = view.findViewById(R.id.tvEndDepth);
        setViewDrawable(tvEndDepth);
        //管线材料
        TextView tvTextrue = view.findViewById(R.id.tvTexture);
        setViewDrawable(tvTextrue);
        //管径
        TextView tvPipeSize = view.findViewById(R.id.tvPipeSize);
        setViewDrawable(tvPipeSize);
        //断面
        TextView tvDs = view.findViewById(R.id.tvD_S);
        setViewDrawable(tvDs);

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

        //获取当前点图层
        Layer _pLayer = DataHandlerObserver.ins().getTotalPtLayer();

        //获取当前点图层
        Layer _pEndLayer = DataHandlerObserver.ins().getTotalLrLayer();
        if (_pLayer == null || _pEndLayer == null) {
            LogUtills.i("Initial DrawPipeLineActivity Get Point Layer Or Line Layer Faild...");
            getDialog().dismiss();
        }

        //起点记录集
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
        //起点高程
        String surf_h = m_startPInfo.surf_H;
        if (surf_h.isEmpty()) {
            surf_h = "0";
        }
        //转成double 厘米转成米
        double starth = Double.valueOf(surf_h) * 100;

        startH = (int) starth;

        //终点记录集
        Recordset _endReset = DataHandlerObserver.ins().queryRecordsetBySmid(m_endSmId, true, false);
        _endReset.moveFirst();
        //记录集转为管点类
        m_endPInfo = BaseFieldPInfos.createFieldInfo(_endReset);
        if (m_endPInfo == null) {
            LogUtills.e("Initial DrawPipeLineActivity Create End BaseFieldPInfos fail, Smid:" + m_endSmId);
            _startReset.close();
            _startReset.dispose();
            _endReset.close();
            _endReset.dispose();
            getDialog().dismiss();
        }
        //终点高程
        String surf_h1 = m_endPInfo.surf_H;

        if (surf_h1.isEmpty()) {
            surf_h1 = "0";
        }
        //转成double 厘米转成米
        double endh = Double.valueOf(surf_h1) * 100;
        endH = (int) endh;
        //获取管线长度
        double _len = getPipeLen(_startReset, _endReset);
        //起点点号
        edtStartPoint.setText(m_startPInfo.id);
        //终点点号
        edtEndPoint.setText(m_endPInfo.id);
        //管线长度
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
        //计算线的长度
        double _len = Math.sqrt(_dx * _dx + _dy * _dy);
        //查询数据库自定义的管线长度提示值
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
        int _tempL = 0;
        if (_cursor.moveToNext()) {
            _tempL = _cursor.getInt(_cursor.getColumnIndex("PipeLength"));
        }
        _cursor.close();
        //管线长度超过了数据库线长度的提示值
        if (_len > _tempL) {
            ToastyUtil.showWarningShort(getActivity(), "管线长度超出自定义长度" + _tempL + "米");
        }
        return _len;
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                //返回
                case R.id.linearReturn:
                    //如果添加的是临时点临时线，返回时要删除临时点
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
                    WorkSpaceUtils.getInstance().saveMap();
                    getDialog().dismiss();
                    break;

                //保存 提交 提交前判断有些选项数据是否合理
                case R.id.btnRemove:
                case R.id.tvSubmit:
                    boolean _result = false;
                    //判断哪些值没有填写
                    checkViewData();
                    //检查哪些必填值没有填写
                    if (!checkValueRight()) {
                        return;
                    }
                    //往线记录集添加记录
                    _result = DataHandlerObserver.ins().addRecords(generateBaseFieldInfo());
                    if (!_result) {
                        ToastyUtil.showErrorShort(getActivity().getBaseContext(), "保存点数据失败...");
                        return;
                    } else {
                        //更新起点终点的深度
                        //更新点数据集起点深度值
                        boolean _fistPoint = DataHandlerObserver.ins().updateRecordSetBySql("exp_Num = '" + getStartPoint() + "'", getStartBurialDepth());
                        //更新点数据集起点深度值
                        boolean _secondPoint = DataHandlerObserver.ins().updateRecordSetBySql("exp_Num = '" + getEndPoint() + "'", getEndBurialDepth());
                        if (_fistPoint && _secondPoint) {
                            LogUtills.i("save point depth ", "ok");
                        }
                        WorkSpaceUtils.getInstance().saveMap();
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
                    //管底高差交换
                    int i = startH;
                    startH = endH;
                    endH = i;

                    //埋深修改
                    temp = edtStartBurialDepth.getText().toString();
                    edtStartBurialDepth.setText(edtEndBurialDepth.getText().toString());
                    edtEndBurialDepth.setText(temp);
                    break;

                //管线备注下拉按钮
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
                    if (gpType.contains("煤气") || gpType.contains("燃气") || gpType.contains("电信") || gpType.contains("监控") || gpType.contains("移动") ||
                            gpType.contains("联通") || gpType.contains("盈通")) {
                        showDialog(pipeUnitList, edtOwnershipUnit, "权属单位列表");
                        _popupWindow.show();
                    } else {
                        ToastyUtil.showInfoShort(getActivity(), "当前管类没有权属单位数据列表");
                    }
                    break;
                //排水外检
                case R.id.btnPs:
                    DrawPsLineFragment drawLineFragment = new DrawPsLineFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("startPoint", m_startPInfo);
                    bundle.putParcelable("endPointInfo", m_endPInfo);
                    bundle.putString("bePoint", m_startPInfo.exp_Num);
                    bundle.putString("endPoint", m_endPInfo.exp_Num);
                    bundle.putString("beDeep", getStartBurialDepth());
                    bundle.putString("endDeep", getEndBurialDepth());
                    bundle.putString("textrure", getTextrure());
                    bundle.putString("pipeSize", getPipeSize());
                    drawLineFragment.setArguments(bundle);
                    drawLineFragment.show(getActivity().getFragmentManager(), "psLine");
                    break;

                //管径
//                case R.id.imgvPipeSize:
                   /* if (gpType.contains("煤气") || gpType.contains("燃气")) {
//                        if (spTextrure.getSelectedItem().toString().equals("钢") && (gpType.equals("煤气") || gpType.equals("燃气"))) {
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
//                        }
                    } else {
                        ToastyUtil.showInfoShort(getActivity(), "当前管类没有管径数据列表");
                    }*/
//                    break;

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
                ToastyUtil.showInfoShort(getActivity(), "已用孔数不能大于总孔数");
                return false;
            }
        }

     /*   //起点埋深不能超过20米
        if (Double.valueOf(getStartBurialDepth()) > 20.0) {
            ToastyUtil.showInfoShort(getActivity(), "起点埋深超过20米， ");
            return false;
        }

        //终点埋深不能超过20米
        if (Double.valueOf(getEndBurialDepth()) > 20.0) {
            ToastyUtil.showInfoShort(getActivity(), "终点埋深超过20米，请检查数据");
            return false;
        }*/

        //排水类管线，埋设方式为方沟，管线材料不能为塑料
        if ("排水".equals(gpType.substring(0, 2))) {
            if (spEmbeddedWay.getSelectedItem().toString().equals("方沟") && spTextrure.getSelectedItem().toString().equals("塑料")) {
                ToastyUtil.showInfoShort(getActivity(), "排水埋设方式为方沟，管线材料不能为塑料");
                return false;
            }
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
            if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                OperSql.getSingleton().inserLine(getStartPoint(), getEndPoint(), 0, "外检:新增");
                _info.Edit = "外检:新增";

                //点表
                OperSql.getSingleton().inserPoint(getStartPoint(),_info.startLongitude,_info.startLatitude,"外检:添加线-线起点");
                OperSql.getSingleton().inserPoint(getEndPoint(),_info.endLongitude,_info.endLatitude,"外检:添加线-线终点");

                Recordset recordset = DataHandlerObserver.ins().queryRecordsetByExpNum(getStartPoint(), true);
                if (!recordset.isEmpty()){
                    recordset.edit();
                    recordset.setString("Edit","外检:添加线-线起点");
                    recordset.setString("exp_Date",DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));
                    recordset.update();
                }

                Recordset recordset2 = DataHandlerObserver.ins().queryRecordsetByExpNum(getEndPoint(), true);
                if (!recordset2.isEmpty()){
                    recordset2.edit();
                    recordset2.setString("Edit","外检:添加线-线终点");
                    recordset2.setString("exp_Date",DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));
                    recordset2.update();
                }

                if (recordset != null){
                    recordset.close();
                    recordset.dispose();
                }

                if (recordset2 != null){
                    recordset2.close();
                    recordset2.dispose();
                }

            }
            _info.state = getState();
            _info.remark = getLineRemark();
            _info.puzzle = getPuzzle();
            _info.burialDifference = getBurialDifference();
            String ds = (_info.pipeSize.toString().trim().length() == 0) ? _info.d_S : _info.pipeSize;
            if (getEndPoint().contains("T_")) {
                _info.labelTag = "-" + "临时-o-" + ds + "-" + _info.material.toString();
            } else {
                _info.labelTag = "-" + _info.pipeType.toLowerCase() + "-" + ds + "-" + _info.material.toString();
            }
            LogUtills.e(_info.toString());
        } catch (Exception e) {
            LogUtills.e(e.getMessage());
            LogUtills.e(_info.toString());
            return null;
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
            ToastyUtil.showInfoShort(getActivity(), "您未录入过此类型管点数据");
        }

        reSet.close();
        reSet.dispose();
    }

    /**
     * 初始化数据
     */
    private void setValueToView() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @Author HaiRun
     * @Time 2019/5/29 . 15:10
     */
    private void initValue() {

        //管线类型
        gpType = getArguments().getString("gpType");
        //从下标第三位截取管类代码
        m_code = gpType.substring(3);

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
//        if (gpType.contains("煤气") || gpType.contains("燃气")) {
        //管径
        pipeSizeList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.pipesizeStandard));
        edtPipeSize.setThreshold(1);
        edtPipeSize.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, pipeSizeList));
        //断面
        edtSectionWidth.setThreshold(1);
        edtSectionHeight.setThreshold(1);
        edtSectionWidth.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, pipeSizeList));
        edtSectionHeight.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, pipeSizeList));
//        }

        //权属单位
        if (gpType.contains("煤气") || gpType.contains("燃气")) {
            pipeUnitList = getResources().getStringArray(R.array.ownershipUnit_rq_mq);
        } else if (gpType.contains("电信") || gpType.contains("监控") || gpType.contains("移动") ||
                gpType.contains("联通") || gpType.contains("盈通")) {
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

        /*//显示隐藏属性面板
        switch (gpType) {
            case "电力":
            case "路灯":
            case "电信":
            case "有视":
            case "军队":
            case "交通":
            case "高压":
            case "低压":
            case "监控":
            case "移动":
            case "联通":
            case "盈通":
                layoutDLLDPanel.setVisibility(View.VISIBLE);
                break;
            default:
                layoutDLLDPanel.setVisibility(View.GONE);
                break;
        }*/
        //显示隐藏属性面板
        if (gpType.contains("电力") || gpType.contains("路灯")
                || gpType.contains("电信") || gpType.contains("有视")
                || gpType.contains("军队") || gpType.contains("交通")
                || gpType.contains("高压") || gpType.contains("低压") || gpType.contains("监控") || gpType.contains("移动") ||
                gpType.contains("联通") || gpType.contains("盈通") || gpType.contains("供电") || gpType.contains("信号")
                || gpType.contains("铁通") || gpType.contains("吉通") || gpType.contains("网通") || gpType.contains("盈通")
                || gpType.contains("军用") || gpType.contains("保密") || gpType.contains("其他") || gpType.contains("监控")
                || gpType.contains("电通") || gpType.contains("广通") || gpType.contains("广电")) {
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
                    WorkSpaceUtils.getInstance().saveMap();
                    return true;
                } else {
                    //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
                    return false;
                }
            }
        });

        if (gpType.contains("高压") || gpType.contains("低压")) {
            setViewDrawable(tvOwnershipUnit);
        }

        if (gpType.contains("电力") || gpType.contains("高压") || gpType.contains("低压") || gpType.contains("路灯")) {
            setViewDrawable(tvVoltage);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //初始化窗口大小
//        InitWindowSize.ins().initWindowSize(getActivity(), getDialog());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            //埋设方式
            case R.id.spEmbeddedWay:
                switch (spEmbeddedWay.getSelectedItem().toString()) {
                    case "管埋":
                        edtLineRemark.setText("");
                        layoutPipeSize.setVisibility(View.VISIBLE);
                        layoutSection.setVisibility(View.GONE);
                        edtSectionWidth.setText("");
                        edtSectionHeight.setText("");

                        //电力线管块 已用孔数  总孔数  电缆根数设置星号
                        setViewDrawable(tvHoleCount);
                        setViewDrawable(tvUsedHole);
                        setViewDrawable(tvAmount);

                        break;
                    case "直埋":
                    case "架空":
                        edtLineRemark.setText(spEmbeddedWay.getSelectedItem().toString());
                        layoutPipeSize.setVisibility(View.VISIBLE);
                        layoutSection.setVisibility(View.GONE);
                        edtSectionWidth.setText("");
                        edtSectionHeight.setText("");

                        //电力线管块 已用孔数  总孔数  电缆根数设置星号
                        setViewDrawable(tvAmount);
                        setViewDrawableNull(tvHoleCount);
                        setViewDrawableNull(tvUsedHole);

                        break;
                    case "管块":
                        //电力线管块 已用孔数  总孔数  电缆根数设置星号
                        setViewDrawable(tvHoleCount);
                        setViewDrawable(tvUsedHole);
                        setViewDrawable(tvAmount);

                        edtLineRemark.setText("");
                        layoutSection.setVisibility(View.VISIBLE);
                        layoutPipeSize.setVisibility(View.GONE);
                        edtPipeSize.setText("");
                        break;
                    case "方沟":
                        edtLineRemark.setText("");
                        layoutSection.setVisibility(View.VISIBLE);
                        layoutPipeSize.setVisibility(View.GONE);
                        edtPipeSize.setText("");
                        //电力线管块 已用孔数  总孔数  电缆根数设置星号
                        setViewDrawable(tvAmount);
                        setViewDrawableNull(tvHoleCount);
                        setViewDrawableNull(tvUsedHole);

                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 埋深差值 依据起点埋深和终点埋深差值改变
     */
    private TextWatcher m_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //埋深差值
            try {
                String num1Str = edtStartBurialDepth.getText().toString();
                String num2Str = edtEndBurialDepth.getText().toString();
                if (num1Str.length() == 0) {
                    num1Str = "0";
                }
                if (num2Str.length() == 0) {
                    num2Str = "0";
                }
                int num2 = 0;
                int num1 = 0;
                if (!num1Str.equals("-") || !num1Str.equals("--")) {
                    num1 = Integer.parseInt(num1Str);
                }
                if (!num2Str.equals("-") || !num1Str.equals("--")) {
                    num2 = Integer.parseInt(num2Str);
                }

                int result = num1 - num2;
                edtBurialDifference.setText(String.valueOf(result));

                //管底高差 （起点高程 - 起点埋深） - （终点高程 - 终点埋深）
                int temp = (startH - num1) - (endH - num2);
                LogUtills.i(startH + "---" + endH + "----" + num1 + "-----" + num2);
                edtTopBottom.setText(String.valueOf(temp));
            } catch (Exception e) {
//                ToastyUtil.showErrorShort(getActivity(),e.toString());
            }
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
            String sign = "";
            if (temp.contains("-")) {
                sign = "-";
                temp = temp.substring(1);
            }
            int s = Integer.parseInt(temp);
            //厘米单位转换成米
            DecimalFormat df2 = new DecimalFormat("###.00");
            if (temp.length() > 2) {
                return sign + df2.format(s / 100d);
            } else {
                return sign + "0" + df2.format(s / 100d);
            }
        }
        return "0.00";
    }

    @Override
    public String getEndBurialDepth() {
        String temp = edtEndBurialDepth.getText().toString();
        if (temp.length() > 0) {
            String sign = "";
            if (temp.contains("-")) {
                sign = "-";
                temp = temp.substring(1);
            }

            int s = Integer.parseInt(temp);
            DecimalFormat df2 = new DecimalFormat("###.00");
            if (temp.length() > 2) {
                return sign + df2.format(s / 100d);
            } else {
                return sign + "0" + df2.format(s / 100d);
            }
        } else {
            return "0.00";
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
            return "0.00";
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
            String sign = "";
            if (_startBurialDepth.contains("-")) {
                sign = "-";
                _startBurialDepth = _startBurialDepth.substring(1);
            }
            double s = Double.parseDouble(_startBurialDepth);
            double temp = s * 100;
            String depth = sign + new DecimalFormat().format(temp).replace(",", "");
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
            String sign = "";
            if (_ednBurialDepth.contains("-")) {
                sign = "-";
                _ednBurialDepth = _ednBurialDepth.substring(1);
            }
            double s = Double.parseDouble(_ednBurialDepth);
            double temp = s * 100;
            edtEndBurialDepth.setText(sign + new DecimalFormat().format(temp).replace(",", ""));
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
            double temp = s * 100;
            edtBurialDifference.setText(new DecimalFormat().format(temp).replace(",", ""));
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
        String _usedHoleCount = m_baseFileLInfo.usedHole;
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
        if (gpType.contains("电力") || gpType.contains("高压") || gpType.contains("低压") || gpType.contains("路灯")
                || gpType.contains("电信") || gpType.contains("有视")
                || gpType.contains("军队") || gpType.contains("交通") || gpType.contains("监控") || gpType.contains("移动") ||
                gpType.contains("联通") || gpType.contains("盈通")) {

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

                if (!gpType.contains("电力")) {
                    str.append("套管孔径、");
                }
            }

            if (edtRow.getText().toString().isEmpty()) {
                if (!gpType.contains("电力")) {
                    str.append("行 X 列、");
                }
            }

            if (spVolatage.getSelectedItem().toString().isEmpty()) {
                str.append("电缆电压、");
            }

            if (edtOwnershipUnit.getText().toString().isEmpty()) {
                str.append("权属单位、");
            }
        }

        if (str.length() > 0) {
            ToastyUtil.showInfoShort(getActivity().getBaseContext(), str.toString().substring(0, str.length() - 1) + "数据没有填写");
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

        if (gpType.contains("电力") || gpType.contains("高压") || gpType.contains("低压") || gpType.contains("电信")
                || gpType.contains("监控") || gpType.contains("移动") ||
                gpType.contains("联通") || gpType.contains("盈通") || gpType.contains("路灯")) {
            //	3电力、电信、路灯管材不为空管、空沟。电缆根数不能为O或空值，出现时提示。
            if (!_textrure.equals("空管") || !_textrure.equals("空沟")) {
                if (_amount.equals("0") || _amount.isEmpty()) {
                    ToastyUtil.showInfoShort(getActivity(), "电缆根数不能为空或者为0或空值");
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
                    ToastyUtil.showErrorShort(getActivity(), "管材不能为空管");
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
        if (gpType.contains("电力") || gpType.contains("高压") || gpType.contains("低压") || gpType.contains("路灯")) {
            if (!_textrure.equals("空管") || !_textrure.equals("空沟")) {
                String _volatage = spVolatage.getSelectedItem().toString();
                if (_volatage.equals("0") || _volatage.length() == 0) {
                    ToastyUtil.showInfoShort(getActivity(), "电压不能为空或者为0");
                }
            } else {
                spVolatage.setSelection(0, false);
            }
        }
        return true;
    }

    /**
     * 多选
     * @Params :
     * @author :HaiRun
     * @date :2019/7/10  15:12
     */
    private void showDialog(final String[] data, final TextView textView, String title) {
        textView.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(title);
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        final boolean[] selectItems = new boolean[data.length];
        for (int i = 0; i < data.length; i++) {
            selectItems[i] = false;
            map.put(i, false);
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
//                    sb.append(data[which] + "+");
                    map.put(which, true);
                }
//                String s = sb.toString();
//                String data = s.substring(0, s.length() - 1);
//                textView.setText(data);
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
                StringBuffer sb = new StringBuffer(100);
                for (int i = 0; i < map.size(); i++) {
                    if (map.get(i)) {
                        sb.append(data[i] + "+");
                    }
                }
                String s = sb.toString();
                String data = s.substring(0, s.length() - 1);
                textView.setText(data);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 设置TextView右边星号
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/6/24  16:27
     */
    private void setViewDrawable(TextView textView) {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_must_fill_16);
        drawable.setBounds(0, 0, 20, 20);
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * 设置TextView右边星号
     * @Params :
     * @author :HaiRun
     * @date :2019/6/24  16:27
     */
    private void setViewDrawableNull(TextView textView) {
        textView.setCompoundDrawables(null, null, null, null);
    }

}
