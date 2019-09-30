package com.app.pipelinesurvey.view.fragment.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.utils.ExportDataUtils;
import com.app.pipelinesurvey.utils.InitWindowSize;
import com.app.pipelinesurvey.view.activity.MapActivity;
import com.app.pipelinesurvey.view.widget.CustomDatePicker;
import com.bumptech.glide.load.resource.bytes.ByteBufferRewinder;

import java.text.SimpleDateFormat;
import java.util.Date;
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
                new ExportDataUtils(getActivity()).exportData(tvStart.getText().toString(), tvEnd.getText().toString());
                getDialog().dismiss();
                break;
            //导出全部数据
            case R.id.btn_export_all:
                new ExportDataUtils(getActivity()).exportData();
                getDialog().dismiss();
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
