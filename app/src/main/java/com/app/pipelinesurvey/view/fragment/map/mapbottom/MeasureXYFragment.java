package com.app.pipelinesurvey.view.fragment.map.mapbottom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.utills.LogUtills;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.mapping.Action;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MeasureListener;
import com.supermap.mapping.MeasureStateListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

/**
 * @author HaiRun.
 * 测量坐标系
 */

public class MeasureXYFragment extends Fragment implements View.OnClickListener {
    private TextView tvDistance;
    private MapControl m_mapControl;
    private TextView tvY;
    private TextView tvX;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_xy_measurement, container, false);
        tvX = _view.findViewById(R.id.tvX);
        tvY = _view.findViewById(R.id.tvY);
        m_mapControl = WorkSpaceUtils.getInstance().getMapControl();
        return _view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataXY(Point2D point2D) {
        DecimalFormat df = new DecimalFormat("0.00000");
        String x2 = df.format(point2D.getX());
        String y2 = df.format(point2D.getY());
        tvX.setText(x2);
        tvY.setText(y2);
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
