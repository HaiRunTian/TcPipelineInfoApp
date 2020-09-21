package com.app.pipelinesurvey.view.fragment.map.mapbottom;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.widget.CustomDatePicker;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.Recordset;
import com.supermap.mapping.Layer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * @author HaiRun
 * 测量收点
 */

public class MeasuredPointFragment extends Fragment implements View.OnClickListener {
    /**
     * 显示或测量点标记
     */
    private Button btnHideMeasuredFlag, btnWaitCheck;
    /**
     * 面板容器
     */
    private LinearLayout m_layoutContainer;
    /**
     * 未测量点
     */
    private static TextView m_waitCheck;
    /**
     * 日期选择
     */
    private TextView m_tvDate;
    /**
     * 当天测量点
     */
    private TextView m_tvPoint;
    /**
     * 当天测量线
     */
    private TextView m_tvPipeSum;
    /**
     * 总测量点
     */
    private static TextView m_tvAllPoint;
    /**
     * 总测量线
     */
    private TextView m_tvM;
    private static TextView m_tvM2;
    private static TextView m_tvPipeSum2;

    private CustomDatePicker m_customDatePicker;

    //定时间隔刷新
    private static final int MSG_REFRESH = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    initAllData();
                    initCountForDate();
                    mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 1000);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 1000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_measured_point, container, false);
        m_waitCheck = _view.findViewById(R.id.tvWaitCheck);
        btnHideMeasuredFlag = _view.findViewById(R.id.btnHideMeasuredFlag);
        btnHideMeasuredFlag.setOnClickListener(this);
        m_layoutContainer = getActivity().findViewById(R.id.layoutMapContainer);
        m_tvDate = _view.findViewById(R.id.tvDate2);
        m_tvPoint = _view.findViewById(R.id.tvPointSum);
        m_tvPipeSum = _view.findViewById(R.id.tvPipeSum);
        m_tvAllPoint = _view.findViewById(R.id.tvPointSum2);
        m_tvPipeSum2 = _view.findViewById(R.id.tvPipeSum2);
        m_tvM = _view.findViewById(R.id.tvM);
        m_tvM2 = _view.findViewById(R.id.tvM2);
        btnWaitCheck = _view.findViewById(R.id.btnWaitCheck);
        m_tvDate.setOnClickListener(this);
        btnWaitCheck.setOnClickListener(this);
        initDatePicker();
        return _view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        initAllData();
    }

    /**
     * 时间日期选择器
     */
    private void initDatePicker() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        m_tvDate.setText(now.split(" ")[0]);
        // 回调接口，获得选中的时间
        // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        m_customDatePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                m_tvDate.setText(time.split(" ")[0]);
                initCountForDate();
            }
        }, "2010-01-01 00:00", now);
        // 不显示时和分
        m_customDatePicker.showSpecificTime(false);
        // 不允许循环滚动
        m_customDatePicker.setIsLoop(false);

    }

    /**
     * 查询当天测量工作量
     *
     * @Author HaiRun
     * @Time 2019/6/3 . 14:41
     */
    private void initCountForDate() {
        //当天测量长度
        double pipeLengthDate = 0.0;
        //当天测量管点数
        int checkPointDate = 0;
        //查询数据源
        Datasource _datasource = WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0);
        //已测量收点数据集
        DatasetVector _ds = (DatasetVector) _datasource.getDatasets().get("P_" + SuperMapConfig.Layer_Measure);
        //通过sql查询当天测量收点的数量
        Recordset _reset = _ds.query("exp_Date = '" + m_tvDate.getText().toString() + "'", CursorType.STATIC);
        if (!_reset.isEmpty()) {
            checkPointDate = _reset.getRecordCount();
        }


        //查询当天已测量收点管线的总长度
        String sql2 = "measureStart = '1' and measureEnd = '1' and measureDate = '" + m_tvDate.getText().toString() + "'";
        Recordset recordSet = DataHandlerObserver.ins().QueryRecordsetBySql(sql2, false, false);
        if (!recordSet.isEmpty()) {
            recordSet.moveFirst();
            while (!recordSet.isEOF()) {
                pipeLengthDate = pipeLengthDate + recordSet.getDouble("pipeLength");
                recordSet.moveNext();
            }
        }

        //显示
        m_tvPoint.setText(checkPointDate + "");
        DecimalFormat df = new DecimalFormat("0.00");
        if (pipeLengthDate < 1000) {
            m_tvPipeSum.setText(df.format(pipeLengthDate) + "");
        } else {
            m_tvPipeSum.setText(df.format(pipeLengthDate / 1000) + "");
            m_tvM.setText("千米");
        }

        //关闭释放数据集
        _reset.close();
        _reset.dispose();
        recordSet.close();
        recordSet.dispose();
    }


    /**
     * 总测量
     */
    public static void initAllData() {
        //总管点数
        int _meaPointCount = 0;
        //总已测点数
        int _checkPoint = 0;
        //未测点数
        int _waitCheckPoint = 0;
        //总测量的长度
        double checkLength = 0.0;

        //读取出除去临时点的所有数据集
        Recordset _reSet = DataHandlerObserver.ins().QueryRecordsetBySql("subsid != '临时点'", true, false);
        if (!_reSet.isEmpty()) {
            //追加数据集总数
            _meaPointCount = _reSet.getRecordCount();
        }
        _reSet.close();
        _reSet.dispose();


        Datasource _datasource = WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0);
        //已测量收点数据集
        DatasetVector _ds = (DatasetVector) _datasource.getDatasets().get("P_" + SuperMapConfig.Layer_Measure);
        Recordset _reset = _ds.getRecordset(false, CursorType.STATIC);
        if (!_reset.isEmpty()) {
            _checkPoint = _reset.getRecordCount();
        }

        //已测量收点管线的总长度
        Recordset recordSet = DataHandlerObserver.ins().QueryRecordsetBySql("measureStart = '1' and measureEnd = '1'", false, false);
        if (!recordSet.isEmpty()) {
            recordSet.moveFirst();
            while (!recordSet.isEOF()) {
                checkLength = checkLength + recordSet.getDouble("pipeLength");
                recordSet.moveNext();
            }
        }

        //显示
        m_waitCheck.setText(_meaPointCount - _checkPoint + "");
        m_tvAllPoint.setText(_checkPoint + "");
        DecimalFormat df = new DecimalFormat("0.00");

        if (checkLength < 1000) {
            m_tvPipeSum2.setText(df.format(checkLength) + "");
        } else {
            m_tvPipeSum2.setText(df.format(checkLength / 1000) + "");
            m_tvM2.setText("千米");
        }

        recordSet.close();
        recordSet.dispose();
        _reset.close();
        _reset.dispose();
    }

    @Override
    public void onResume() {
        super.onResume();
        initAllData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHideMeasuredFlag:
                Layer _layer = WorkSpaceUtils.getInstance().getMapControl().getMap().getLayers().get("P_" + SuperMapConfig.Layer_Measure + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME);
                Layer _layerThemeLabel = WorkSpaceUtils.getInstance().getMapControl().getMap().getLayers().get("P_" + SuperMapConfig.Layer_Measure + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#1");
                Layer _layerTheme = WorkSpaceUtils.getInstance().getMapControl().getMap().getLayers().get("P_" + SuperMapConfig.Layer_Measure + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#2");
                if (btnHideMeasuredFlag.getText().toString().equals("隐藏已测标识")) {
                    _layer.setVisible(false);
                    _layerTheme.setVisible(false);
                    _layerThemeLabel.setVisible(false);
                    btnHideMeasuredFlag.setText("显示已测标识");

                } else {
                    _layer.setVisible(true);
                    _layerTheme.setVisible(true);
                    _layerThemeLabel.setVisible(true);
                    btnHideMeasuredFlag.setText("隐藏已测标识");
                }
                WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
                break;

            case R.id.tvDate2:
                // 日期格式为yyyy-MM-dd
                m_customDatePicker.show(m_tvDate.getText().toString());
//                initCountForDate();
                break;
                //显示未测点号列表
            case R.id.btnWaitCheck:
                Recordset recordset = DataHandlerObserver.ins().queryRecordsetBySql("MeasuerPoint != '1'", false);
                if (!recordset.isEmpty()){
                    StringBuffer stringBuffer = new StringBuffer("未测点号:");
                    while (!recordset.isEOF()){
                        String exp_num = recordset.getString("exp_Num");
                        stringBuffer.append(exp_num+",");
                        recordset.moveNext();
                    }
                    ToastyUtil.showWarningLong(getActivity(),stringBuffer.substring(0,stringBuffer.length()-1).toString());
                }else {
                    ToastyUtil.showSuccessShort(getActivity(),"全部测量完成");
                }

                break;
            default:
                break;
        }
    }
}
