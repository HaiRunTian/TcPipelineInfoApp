package com.app.pipelinesurvey.view.fragment.setting;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.PointSettingAdapter;
import com.app.pipelinesurvey.bean.setting.ChildPoint;
import com.app.pipelinesurvey.bean.setting.FatherPoint;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.utills.LogUtills;
import java.util.ArrayList;
import java.util.List;

/**
 * 点设置
 *
 * @author HaiRun
 * @time 2019/9/10.16:11
 */
public class PointSettingFragment extends Fragment {

    private ExpandableListView exlvPoint;
    private List<FatherPoint> fatherPoints;
    private List<ChildPoint> childPoints;
    private PointSettingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_point_setting, container, false);
        exlvPoint = view.findViewById(R.id.exlv_point);
        LogUtills.i("PointSettingFragment", "onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initValue();
        LogUtills.i("PointSettingFragment", "onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        initValue();
        initEvent();
        LogUtills.i("PointSettingFragment", "onStart");
    }

    private void initEvent() {
        exlvPoint.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

    }

    /**
     * 初始化数据
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/9/17  11:51
     */
    private void initValue() {
        fatherPoints = new ArrayList<>();
        FatherPoint fatherPoint = null;
        Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_PRJ_SHOW, "where prj_name = '"
                + SuperMapConfig.PROJECT_NAME + "' and show = 1 and city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
        while (query.moveToNext()) {
            fatherPoint = new FatherPoint();
            String pipetype = query.getString(query.getColumnIndex("pipetype"));
            Cursor cursorPoint = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_POINT_SETTING, "where prj_name = '"
                    + SuperMapConfig.PROJECT_NAME + "'and pipetype = '" + pipetype + "'and city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
            ChildPoint childPoint = null;
            while (cursorPoint.moveToNext()) {
                childPoints = new ArrayList<>();
                int wellsize = cursorPoint.getInt(cursorPoint.getColumnIndex("wellsize"));
                childPoint = new ChildPoint("窨井规格", "wellsize", wellsize);
                childPoints.add(childPoint);
                int welldepth = cursorPoint.getInt(cursorPoint.getColumnIndex("welldepth"));
                childPoint = new ChildPoint("窨井深度", "welldepth", welldepth);
                childPoints.add(childPoint);
                int wellwater = cursorPoint.getInt(cursorPoint.getColumnIndex("wellwater"));
                childPoint = new ChildPoint("窨井水深", "wellwater", wellwater);
                childPoints.add(childPoint);
                int wellmud = cursorPoint.getInt(cursorPoint.getColumnIndex("wellmud"));
                childPoint = new ChildPoint("窨井淤泥", "wellmud", wellmud);
                childPoints.add(childPoint);
                int welllidtexture = cursorPoint.getInt(cursorPoint.getColumnIndex("welllidtexture"));
                childPoint = new ChildPoint("井盖材质", "welllidtexture", welllidtexture);
                childPoints.add(childPoint);
                int welllidsize = cursorPoint.getInt(cursorPoint.getColumnIndex("welllidsize"));
                childPoint = new ChildPoint("井盖规格", "welllidsize", welllidsize);
                childPoints.add(childPoint);
                int state = cursorPoint.getInt(cursorPoint.getColumnIndex("state"));
                childPoint = new ChildPoint("管点状态", "state", state);
                childPoints.add(childPoint);
                int elevation = cursorPoint.getInt(cursorPoint.getColumnIndex("elevation"));
                childPoint = new ChildPoint("高程", "elevation", elevation);
                childPoints.add(childPoint);
                int offset = cursorPoint.getInt(cursorPoint.getColumnIndex("offset"));
                childPoint = new ChildPoint("管偏", "offset", offset);
                childPoints.add(childPoint);
                int building = cursorPoint.getInt(cursorPoint.getColumnIndex("building"));
                childPoint = new ChildPoint("建构筑物", "building", building);
                childPoints.add(childPoint);
                int roadname = cursorPoint.getInt(cursorPoint.getColumnIndex("roadname"));
                childPoint = new ChildPoint("道路名称", "roadname", roadname);
                childPoints.add(childPoint);
                int pointremark = cursorPoint.getInt(cursorPoint.getColumnIndex("pointremark"));
                childPoint = new ChildPoint("管点备注", "pointremark", pointremark);
                childPoints.add(childPoint);
                int puzzle = cursorPoint.getInt(cursorPoint.getColumnIndex("puzzle"));
                childPoint = new ChildPoint("疑难问题", "puzzle", puzzle);
                childPoints.add(childPoint);
                int x = cursorPoint.getInt(cursorPoint.getColumnIndex("x"));
                childPoint = new ChildPoint("X", "x", x);
                childPoints.add(childPoint);
                int y = cursorPoint.getInt(cursorPoint.getColumnIndex("y"));
                childPoint = new ChildPoint("Y", "y", y);
                childPoints.add(childPoint);

            }
            fatherPoint.setPipeType(pipetype);
            fatherPoint.setList(childPoints);
            fatherPoints.add(fatherPoint);
            cursorPoint.close();
        }
        query.close();
        adapter = new PointSettingAdapter(getActivity(),fatherPoints);
        exlvPoint.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtills.i("PointSettingFragment", "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtills.i("PointSettingFragment", "onDestroyView");
    }
}
