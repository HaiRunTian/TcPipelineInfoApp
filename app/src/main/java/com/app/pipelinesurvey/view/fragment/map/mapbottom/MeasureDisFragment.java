package com.app.pipelinesurvey.view.fragment.map.mapbottom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.utills.LogUtills;
import com.supermap.data.Color;
import com.supermap.data.Point;
import com.supermap.mapping.Action;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MeasureListener;
import com.supermap.mapping.MeasureStateListener;

import java.text.DecimalFormat;

/**
 * @author HaiRun.
 * 测量距离长度
 */

public class MeasureDisFragment extends Fragment implements View.OnClickListener, MeasureListener, MeasureStateListener {
    private Button btnReset;
    private Button btnWithdraw;
    private TextView tvDistance;
    private MapControl m_mapControl;
    private RadioButton rbtnDis;
    private RadioButton rbtnArea;
    private TextView tvMeasureItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_distance_measurement, container, false);
        btnReset = _view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);
        btnWithdraw = _view.findViewById(R.id.btnWithdraw);
        btnWithdraw.setOnClickListener(this);
        tvDistance = _view.findViewById(R.id.tvDistance);
        tvMeasureItem = _view.findViewById(R.id.measure_item);
        rbtnDis = _view.findViewById(R.id.rbtn_dis);
        rbtnArea = _view.findViewById(R.id.rbtn_area);
        rbtnDis.setChecked(true);
        rbtnArea.setOnClickListener(this);
        rbtnDis.setOnClickListener(this);
        m_mapControl = WorkSpaceUtils.getInstance().getMapControl();
        m_mapControl.setStrokeWidth(0.3);
        m_mapControl.setStrokeColor(m_mapControl.getResources().getColor(R.color.red));
        m_mapControl.addMeasureListener(this);
        //面积量算
        m_mapControl.addMeasureStateListener(this);
        return _view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //重置
            case R.id.btnReset:
                m_mapControl.setAction(Action.PAN);
                tvDistance.setText("");
                if (rbtnDis.isChecked()) {
                    m_mapControl.setAction(Action.MEASURELENGTH);
                } else {
                    m_mapControl.setAction(Action.MEASUREAREA);
                }
                break;
            //回退
            case R.id.btnWithdraw:
                WorkSpaceUtils.getInstance().getMapControl().undo();
                break;
            //面积
            case R.id.rbtn_area:
                tvDistance.setText("");
                m_mapControl.setAction(Action.MEASUREAREA);
                tvMeasureItem.setText("面积:");
                break;
            //距离
            case R.id.rbtn_dis:
                tvDistance.setText("");
                m_mapControl.setAction(Action.MEASURELENGTH);
                tvMeasureItem.setText("距离:");
                break;
            default:
                break;
        }
    }

    /**
     * 长度
     *
     * @auther HaiRun
     * created at 2018/7/31 8:57
     */
    @Override
    public void lengthMeasured(double v, Point point) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (v < 1000) {
            tvDistance.setText(" " + df.format(v) + "米");
        } else {
            tvDistance.setText(" " + df.format(v / 1000) + "千米");
        }
    }

    /**
     * 面积
     * @auther HaiRun
     * created at 2018/7/31 8:57
     */
    @Override
    public void areaMeasured(double v, Point point) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (v < 1000000) {
            tvDistance.setText(" " + df.format(v) + "平方米");
        } else {
            tvDistance.setText(" " + df.format(v / 1000000) + "平方公里");
        }
    }

    /**
     * 角度
     *
     * @auther HaiRun
     * created at 2018/7/31 8:56
     */
    @Override
    public void angleMeasured(double v, Point point) {

    }

    @Override
    public void isSelfIntersect() {

    }
}
