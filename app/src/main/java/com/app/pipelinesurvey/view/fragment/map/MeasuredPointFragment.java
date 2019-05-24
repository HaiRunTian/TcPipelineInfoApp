package com.app.pipelinesurvey.view.fragment.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.InitDatabase;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Recordset;
import com.supermap.mapping.Layer;
import com.supermap.mapping.MapControl;

/**
 * @author HaiRun
 * 测量收点
 */

public class MeasuredPointFragment extends Fragment implements View.OnClickListener {
    /**
     * 显示未测点
     */
    private Button btnUnmeasuredPoint;
    /**
     * 显示或测量点标记
     */
    private Button btnHideMeasuredFlag;
    /**
     * 刷新
     */
    private Button btnFlushe;
    /**
     * 面板容器
     */
    private LinearLayout m_layoutContainer;
    private static TextView m_tvCheck;
    private static TextView m_waitCheck;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_measured_point, container, false);
        m_waitCheck = _view.findViewById(R.id.tvWaitCheck);
        m_tvCheck = _view.findViewById(R.id.tvCheck);
        btnUnmeasuredPoint = _view.findViewById(R.id.btnUnmeasuredPoint);
        btnUnmeasuredPoint.setOnClickListener(this);
        btnHideMeasuredFlag = _view.findViewById(R.id.btnHideMeasuredFlag);
        btnHideMeasuredFlag.setOnClickListener(this);
        m_layoutContainer = getActivity().findViewById(R.id.layoutMapContainer);
        btnFlushe = _view.findViewById(R.id.btnFlush);
        btnFlushe.setOnClickListener(this);

        return _view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        initData();
    }

    public  static void initData() {
        //总管点数
        int _meaPointCount = 0;
        //已测点数
        int _checkPoint = 0;
        //未测点数
        int _waitCheckPoint = 0;


        //读取出除去临时点的所有数据集
        Recordset _reSet = DataHandlerObserver.ins().QueryRecordsetBySql("exp_Num Not like 'T_%'",true,false);
        if (!_reSet.isEmpty()) {
            //追加数据集总数
            _meaPointCount = _reSet.getRecordCount();
        }
        _reSet.close();
        _reSet.dispose();


        Datasource _datasource = WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0);
        //测量收点数据集
        DatasetVector _ds = (DatasetVector) _datasource.getDatasets().get("P_" + SuperMapConfig.Layer_Measure);
        Recordset _reset = _ds.getRecordset(false, CursorType.STATIC);
        if (!_reset.isEmpty()) {
            _checkPoint = _reset.getRecordCount();
        }
        m_tvCheck.setText(_checkPoint + "");
        m_waitCheck.setText(_meaPointCount - _checkPoint + "");
        _reset.close();
        _reset.dispose();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUnmeasuredPoint:
                break;
            case R.id.btnHideMeasuredFlag:
                Layer _layer = WorkSpaceUtils.getInstance().getMapControl().getMap().getLayers().get("P_"+SuperMapConfig.Layer_Measure+ "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME);
                Layer _layerThemeLabel =WorkSpaceUtils.getInstance().getMapControl().getMap().getLayers().get("P_"+SuperMapConfig.Layer_Measure+ "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME +  "#1");
                Layer _layerTheme =WorkSpaceUtils.getInstance().getMapControl().getMap().getLayers().get("P_"+SuperMapConfig.Layer_Measure+ "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME +  "#2");
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
                //刷新
            case R.id.btnFlush:
                initData();
                break;
            default:
                break;
        }
    }
}
