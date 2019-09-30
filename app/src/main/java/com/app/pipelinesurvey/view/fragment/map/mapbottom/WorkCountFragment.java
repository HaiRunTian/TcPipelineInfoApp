package com.app.pipelinesurvey.view.fragment.map.mapbottom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.view.widget.CustomDatePicker;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HaiRun on 2018/11/23 0023.
 * 工作量统计
 */

public class WorkCountFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private View m_rootView;
    private TextView m_tvPointCount;
    private TextView m_tvPipeLength;
    private CustomDatePicker m_customDatePicker;
    private TextView m_tvAllPoint;
    private TextView m_tvAllPipe;
    private TextView m_tvDate;
    //管线单位
    private TextView m_tvM, m_tvM2, m_tvEmptyM, m_tvAllEmptyM;
    private TextView m_tvEmptyLength;
    private TextView m_tvAllEmptyLength;
    private final String TAG = "WorkCount";

    //定时刷新
    private static final int MSG_REFRESH = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    initAllCount();
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
        m_rootView = inflater.inflate(R.layout.fragment_work_count, container, false);
        m_tvAllPoint = m_rootView.findViewById(R.id.tvPointSum2);
        m_tvAllPipe = m_rootView.findViewById(R.id.tvPipeSum2);
        m_tvAllEmptyLength = m_rootView.findViewById(R.id.tvAllEmptySum);

        m_tvPointCount = m_rootView.findViewById(R.id.tvPointSum);
        m_tvPipeLength = m_rootView.findViewById(R.id.tvPipeSum);
        m_tvEmptyLength = m_rootView.findViewById(R.id.tvEmptySum);
        m_tvM = m_rootView.findViewById(R.id.tvM);
        m_tvM2 = m_rootView.findViewById(R.id.tvM2);
        m_tvEmptyM = m_rootView.findViewById(R.id.tvEmptyM);
        m_tvAllEmptyM = m_rootView.findViewById(R.id.tvAllEmptyM);
        m_tvDate = m_rootView.findViewById(R.id.tvDate2);
        initDatePicker();
        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_tvDate.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

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

    @Override
    public void onStart() {
        super.onStart();
        initCountForDate();
        initAllCount();

    }

    /**
     * 依据日期查询工作量
     */
    private void initCountForDate() {
        int _pointCount = 0;
        double _pipeLength = 0.0;
        double _emptyPipeL = 0.0;
        //临时点数量
        int _temPoint = 0;

        //计算当天的点
        DatasetVector _datasetVector = (DatasetVector) DataHandlerObserver.ins().getTotalPtLayer().getDataset();
        Recordset _reSet = _datasetVector.query("exp_Date = '" + m_tvDate.getText().toString() + "' and subsid != '临时点'", CursorType.STATIC);
//        String time = m_tvDate.getText().toString();
//        String sql = "(exp_Date between '" + time + " 00:00' and '" + time + " 59:59') and subsid != '临时点'";
//        Recordset _reSet = _datasetVector.query(sql, CursorType.STATIC);
        if (!_reSet.isEmpty()) {
            _pointCount = _reSet.getRecordCount();
        }

        //计算当天的线长度
        DatasetVector _ds = (DatasetVector) DataHandlerObserver.ins().getTotalLrLayer().getDataset();
        Recordset _reset = _ds.query("exp_Date = '" + m_tvDate.getText().toString() + "' and pipeType Not like '临时%'", CursorType.STATIC);
//        String sql2 = "(exp_Date between '" + time + " 00:00' and '" + time + " 59:59') and pipeType Not like '临时%'";
//        Recordset _reset = _ds.query(sql2, CursorType.STATIC);
        if (!_reset.isEmpty()) {
            _reset.moveFirst();
            while (!_reset.isEOF()) {
                double _length = Double.parseDouble(_reset.getString("pipeLength"));
                _pipeLength += _length;
                _reset.moveNext();
            }
        }
        //计算当天架空线长度
        Recordset _reset2 = _ds.query("exp_Date = '" + m_tvDate.getText().toString() + "' and buried = '架空' and subsid != '临时点'", CursorType.STATIC);
//        String sql3 = "(exp_Date between '" + time + " 00:00' and '" + time + " 59:59') and (buried = '架空') and (pipeType Not like '临时%')";
//        Recordset _reset2 = _ds.query(sql3, CursorType.STATIC);
        if (!_reset2.isEmpty()) {
            _reset2.moveFirst();
            while (!_reset2.isEOF()) {
                double _length = Double.parseDouble(_reset2.getString("pipeLength"));
                _emptyPipeL += _length;
                _reset2.moveNext();
            }
        }
        _reSet.close();
        _reSet.dispose();
        _reset.close();
        _reset.dispose();
        _reset2.close();
        _reset2.dispose();

        DecimalFormat df = new DecimalFormat("0.00");
        m_tvPointCount.setText(_pointCount + "");
        if (_pipeLength < 1000) {
            m_tvPipeLength.setText(df.format(_pipeLength) + "");
        } else {
            m_tvPipeLength.setText(df.format(_pipeLength / 1000) + "");
            m_tvM.setText("千米");
        }

        if (_emptyPipeL < 1000) {
            m_tvEmptyLength.setText(df.format(_emptyPipeL) + "");
        } else {
            m_tvEmptyLength.setText(df.format(_emptyPipeL / 1000) + "");
            m_tvM.setText("千米");
        }

    }

    /**
     * 查询全部工作量
     */
    private void initAllCount() {
        int _pointCount = 0;
        double _pipeLength = 0.0;
        double _emptyPipeL = 0.0;
        int _temPoint = 0;

        //计算总点数
        DatasetVector _datasetVector = (DatasetVector) DataHandlerObserver.ins().getTotalPtLayer().getDataset();
        Recordset _reSet = _datasetVector.query("subsid != '临时点'", CursorType.STATIC);
        if (!_reSet.isEmpty()) {
            _pointCount = _reSet.getRecordCount();
        }

        //计算线总长度
        DatasetVector _ds = (DatasetVector) DataHandlerObserver.ins().getTotalLrLayer().getDataset();
        Recordset _reset = _ds.query("pipeType Not like '临时%'", CursorType.STATIC);
        if (!_reset.isEmpty()) {
            _reset.moveFirst();
            while (!_reset.isEOF()) {
                double _length = Double.parseDouble(_reset.getString("pipeLength"));
                _pipeLength += _length;
                _reset.moveNext();
            }
        }

        //计算架空线总长度
        Recordset _reset2 = _ds.query("buried = '架空' and pipeType Not like '临时%'", CursorType.STATIC);
        if (!_reset2.isEmpty()) {
            _reset2.moveFirst();
            while (!_reset2.isEOF()) {
                double _length = Double.parseDouble(_reset2.getString("pipeLength"));
                _emptyPipeL += _length;
                _reset2.moveNext();
            }
        }

        _reSet.close();
        _reSet.dispose();
        _reset.close();
        _reset.dispose();
        _reset2.close();
        _reset2.dispose();

        //显示
        DecimalFormat df = new DecimalFormat("0.00");
        m_tvAllPoint.setText(_pointCount + "");
        if (_pipeLength < 1000) {
            m_tvAllPipe.setText(df.format(_pipeLength) + "");
        } else {
            m_tvAllPipe.setText(df.format(_pipeLength / 1000) + "");
            m_tvM2.setText("千米");

        }
        if (_emptyPipeL < 1000) {
            m_tvAllEmptyLength.setText(df.format(_emptyPipeL) + "");
        } else {
            m_tvAllEmptyLength.setText(df.format(_emptyPipeL / 1000) + "");
            m_tvAllEmptyM.setText("千米");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDate2:
                // 日期格式为yyyy-MM-dd
                m_customDatePicker.show(m_tvDate.getText().toString());
//                initCountForDate();
                break;
            default:
                break;
        }
    }
}
