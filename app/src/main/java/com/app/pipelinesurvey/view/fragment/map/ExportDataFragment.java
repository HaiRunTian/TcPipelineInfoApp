package com.app.pipelinesurvey.view.fragment.map;

import android.app.DialogFragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.ExportDataUtils;
import com.app.pipelinesurvey.utils.InitWindowSize;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.widget.CustomDatePicker;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author HaiRun
 * @time 2019/9/29.14:54
 */
public class ExportDataFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvStart;
    private TextView tvEnd;
    private Button btnSelect;
    private Button btnAll;
    private CustomDatePicker m_customDatePicker;
    private CustomDatePicker m_customDatePicker2;
    private Spinner spRoadName;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_date_fragment, container, false);
        initview(view);

        return view;
    }

    private void initview(View view) {
        tvStart = view.findViewById(R.id.tvStart);
        tvEnd = view.findViewById(R.id.tvEnd);
        btnSelect = view.findViewById(R.id.btn_export_select);
        btnAll = view.findViewById(R.id.btn_export_all);
        spRoadName = view.findViewById(R.id.spRoadName);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("数据导出");
        TextView tvSubmit = view.findViewById(R.id.tvConfig);
        tvSubmit.setVisibility(View.GONE);
        TextView tvReturn = view.findViewById(R.id.tvReturn);
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
            btnSelect.setText("导出外检");
        }

        if ("1".equals(SuperMapConfig.PS_OUT_CHECK)) {
            View layout = view.findViewById(R.id.layout);
            layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEvent();
        initDatePicker();
        initDatePicker2();
    }


    private void initEvent() {
        btnSelect.setOnClickListener(this);
        btnAll.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);
        list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, list);
        spRoadName.setAdapter(adapter);
        if ("1".equals(SuperMapConfig.PS_OUT_CHECK)) {
            DatasetVector psLrDatasetVector = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("L_" + SuperMapConfig.Layer_PS);
            if (psLrDatasetVector == null) {
                LogUtills.i("psLrDatasetVector is null");
                return;
            }
            QueryParameter parameter = new QueryParameter();
            parameter.setResultFields(new String[]{"distinct roadName"});
            parameter.setCursorType(CursorType.STATIC);
            Recordset recordset = psLrDatasetVector.query(parameter);

            if (recordset.isEmpty()) {
                LogUtills.i("distinct isEmpty");
                return;
            }

            while (!recordset.isEOF()) {
                String roadName = recordset.getString("roadName");
                LogUtills.i("roadName = " + roadName);
                list.add(roadName);
                recordset.moveNext();
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void initDatePicker() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        tvStart.setText(now.split(" ")[0]);
        // 回调接口，获得选中的时间
        // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        m_customDatePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvStart.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", now);
        // 不显示时和分
        m_customDatePicker.showSpecificTime(false);
        // 不允许循环滚动
        m_customDatePicker.setIsLoop(false);
    }

    private void initDatePicker2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        tvEnd.setText(now.split(" ")[0]);
        // 回调接口，获得选中的时间
        // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        m_customDatePicker2 = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvEnd.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", now);
        // 不显示时和分
        m_customDatePicker2.showSpecificTime(false);
        // 不允许循环滚动
        m_customDatePicker2.setIsLoop(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        InitWindowSize.ins().initWindowSize(getActivity(), getDialog(), 0.9, 0.4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //导出部分数据
            case R.id.btn_export_select:
                try {
                    if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                        //导出外检，并且按照实际日期导出
                        String sql = "subsid != '临时点' and Edit like '外检%' and (exp_Date between '"
                                + tvStart.getText().toString() + "' and '" + tvEnd.getText().toString() + "')";
                        new ExportDataUtils(getActivity()).exportOutCheckData(sql, tvStart.getText().toString(), tvEnd.getText().toString());

                    } else {
                        String roadName = "";
                        //排水检测模式需要加入道路名导出
                        if ("1".equals(SuperMapConfig.PS_OUT_CHECK) && list.size() != 0) {
                            roadName = spRoadName.getSelectedItem().toString();
                        }
                        new ExportDataUtils(getActivity()).exportData(tvStart.getText().toString(), tvEnd.getText().toString(), roadName);
                    }
                } catch (Exception e) {
                    LogUtills.e(e.toString());
                }
                break;
            //导出全部数据
            case R.id.btn_export_all:
                try {
                    if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                        String sql = "subsid != '临时点'";
                        new ExportDataUtils(getActivity()).exportOutCheckData(sql);
                    } else {
                        new ExportDataUtils(getActivity()).exportData();
                    }
                    getDialog().dismiss();
                } catch (Exception e) {
                    LogUtills.e(e.toString());
                }
                break;
            case R.id.tvStart:
                m_customDatePicker.show(tvStart.getText().toString());
                break;
            case R.id.tvEnd:
                m_customDatePicker2.show(tvStart.getText().toString());
                break;
            default:
                break;
        }
    }
}
