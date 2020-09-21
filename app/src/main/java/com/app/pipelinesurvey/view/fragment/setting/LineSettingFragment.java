package com.app.pipelinesurvey.view.fragment.setting;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.LineSettingAdapter;
import com.app.pipelinesurvey.bean.setting.ChildPoint;
import com.app.pipelinesurvey.bean.setting.FatherPoint;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 线界面配置
 *
 * @author HaiRun
 * @time 2019/9/17.10:51
 */
public class LineSettingFragment extends Fragment {

    private ExpandableListView expandableListView;
    private List<FatherPoint> fatherPoints;
    private List<ChildPoint> childPoints;
    private LineSettingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_setting, container, false);
        expandableListView = view.findViewById(R.id.exlv_line);
        expandableListView.setGroupIndicator(null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initValue();
    }

    @Override
    public void onStart() {
        super.onStart();
        initValue();

    }

    /**
     * 初始化数据
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/9/17  11:52
     */
    private void initValue() {
        fatherPoints = new ArrayList<>();
        FatherPoint fatherPoint = null;
        Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_PRJ_SHOW, "where prj_name = '"
                + SuperMapConfig.PROJECT_NAME + "' and show = 1 and city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
        while (query.moveToNext()) {
            fatherPoint = new FatherPoint();
            String pipetype = query.getString(query.getColumnIndex("pipetype"));
            Cursor cursorLine = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_LINE_SETTING, "where prj_name = '" + SuperMapConfig.PROJECT_NAME
                    + "'and pipetype = '" + pipetype + "'and city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
            ChildPoint childPoint = null;
            while (cursorLine.moveToNext()) {
                childPoints = new ArrayList<>();
                int start_depth = cursorLine.getInt(cursorLine.getColumnIndex("start_depth"));
                childPoint = new ChildPoint("起点埋深","start_depth",start_depth);
                childPoints.add(childPoint);
                int end_depth = cursorLine.getInt(cursorLine.getColumnIndex("end_depth"));
                childPoint = new ChildPoint("终点埋深","end_depth",end_depth);
                childPoints.add(childPoint);
                int pipe_length = cursorLine.getInt(cursorLine.getColumnIndex("pipe_length"));
                childPoint = new ChildPoint("管线长度","pipe_length",pipe_length);
                childPoints.add(childPoint);
                int bureal_diff = cursorLine.getInt(cursorLine.getColumnIndex("bureal_diff"));
                childPoint = new ChildPoint("埋深差值","bureal_diff",bureal_diff);
                childPoints.add(childPoint);
                int embedded_way = cursorLine.getInt(cursorLine.getColumnIndex("embedded_way"));
                childPoint = new ChildPoint("埋设方式","embedded_way",embedded_way);
                childPoints.add(childPoint);
                int texture = cursorLine.getInt(cursorLine.getColumnIndex("texture"));
                childPoint = new ChildPoint("管线材料","texture",texture);
                childPoints.add(childPoint);
                int pipe_size = cursorLine.getInt(cursorLine.getColumnIndex("pipe_size"));
                childPoint = new ChildPoint("管径","pipe_size",pipe_size);
                childPoints.add(childPoint);
                int secttion = cursorLine.getInt(cursorLine.getColumnIndex("secttion"));
                childPoint = new ChildPoint("断面","secttion",secttion);
                childPoints.add(childPoint);
                int hole_count = cursorLine.getInt(cursorLine.getColumnIndex("hole_count"));
                childPoint = new ChildPoint("总孔数","hole_count",hole_count);
                childPoints.add(childPoint);
                int used_count = cursorLine.getInt(cursorLine.getColumnIndex("used_count"));
                childPoint = new ChildPoint("已用孔数","used_count",used_count);
                childPoints.add(childPoint);
                int amount = cursorLine.getInt(cursorLine.getColumnIndex("amount"));
                childPoint = new ChildPoint("电缆根数","amount",amount);
                childPoints.add(childPoint);
                int aperture = cursorLine.getInt(cursorLine.getColumnIndex("aperture"));
                childPoint = new ChildPoint("套管孔径","aperture",aperture);
                childPoints.add(childPoint);
                int row_col = cursorLine.getInt(cursorLine.getColumnIndex("row_col"));
                childPoint = new ChildPoint("行 X 列","row_col",row_col);
                childPoints.add(childPoint);
                int voltage = cursorLine.getInt(cursorLine.getColumnIndex("voltage"));
                childPoint = new ChildPoint("电缆电压","voltage",voltage);
                childPoints.add(childPoint);
                int state = cursorLine.getInt(cursorLine.getColumnIndex("state"));
                childPoint = new ChildPoint("管线状态","state",state);
                childPoints.add(childPoint);
                int pressure = cursorLine.getInt(cursorLine.getColumnIndex("pressure"));
                childPoint = new ChildPoint("管道压力","pressure",pressure);
                childPoints.add(childPoint);
                int owner_ship_unit = cursorLine.getInt(cursorLine.getColumnIndex("owner_ship_unit"));
                childPoint = new ChildPoint("权属单位","owner_ship_unit",owner_ship_unit);
                childPoints.add(childPoint);
                int line_remark = cursorLine.getInt(cursorLine.getColumnIndex("line_remark"));
                childPoint = new ChildPoint("管线备注","line_remark",line_remark);
                childPoints.add(childPoint);
                int puzzle = cursorLine.getInt(cursorLine.getColumnIndex("puzzle"));
                childPoint = new ChildPoint("疑难问题","puzzle",puzzle);
                childPoints.add(childPoint);
            }
            fatherPoint.setList(childPoints);
            fatherPoint.setPipeType(pipetype);
            fatherPoints.add(fatherPoint);
            cursorLine.close();
        }
        query.close();
        adapter = new LineSettingAdapter(getActivity(),fatherPoints);
        expandableListView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
            initValue();
            adapter.notifyDataSetChanged();

    }
}
