package com.app.pipelinesurvey.view.fragment.setting;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.PipeTypeSettingAdapter;
import com.app.pipelinesurvey.bean.setting.PipeTypeStting;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.utills.LogUtills;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个工程管类是否显示
 *
 * @author HaiRun
 * @time 2019/9/19.15:43
 */
public class PipeTypeFragment extends Fragment {

    private View view;
    private ListView listView;
    private CheckBox cbAll;
    private List<PipeTypeStting> list;
    private PipeTypeSettingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pipe_type_setting, container, false);
        initView();
        return view;
    }

    private void initView() {
        listView = view.findViewById(R.id.lv_pipe_type);
        cbAll = view.findViewById(R.id.cbAllSelect);
    }

    @Override
    public void onStart() {
        super.onStart();
        initValue();
        initEvent();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initEvent() {
        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setShow(1);
                    }
                }else {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setShow(0);
                    }
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 初始化数据
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/9/19  15:49
     */
    private void initValue() {
        Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_PRJ_SHOW, "where prj_name = '"
                + SuperMapConfig.PROJECT_NAME + "' and city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
        list = new ArrayList<>();
        PipeTypeStting typeStting = null;
        while (query.moveToNext()) {
            typeStting = new PipeTypeStting();
            String pipeType = query.getString(query.getColumnIndex("pipetype"));
            int show = query.getInt(query.getColumnIndex("show"));
            typeStting.setPipeTyep(pipeType);
            typeStting.setShow(show);
            typeStting.setPrjName(SuperMapConfig.PROJECT_NAME);
            typeStting.setCode(query.getString(query.getColumnIndex("code")));
            list.add(typeStting);
        }
        query.close();

        adapter = new PipeTypeSettingAdapter(getActivity(), list);
        listView.setAdapter(adapter);
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
