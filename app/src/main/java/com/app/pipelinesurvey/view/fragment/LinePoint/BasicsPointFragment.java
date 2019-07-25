package com.app.pipelinesurvey.view.fragment.LinePoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.BasicsDapter;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.SQLUtils;
import com.app.pipelinesurvey.view.activity.linepoint.AddBasicsActivity;
import com.app.utills.LogUtills;
import java.util.ArrayList;

/**
 * 基础管类
 * Created by linshen on 2019年5月7日14:25:04.
 */

public class BasicsPointFragment extends Fragment implements View.OnClickListener {
    private View view;
    private GridView gridView;
    private TextView btnAdd;
    private BasicsDapter dapter;
    private ArrayList<String> list;
    private String table;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_basics_point, container, false);
            initView();

            table = SQLConfig.TABLE_NAME_PIPE_INFO;
            initSql(table);
            initData(table);
        }
        return view;
    }

    private void initSql(String table) {
        //管點
        list = SQLUtils.getAll(table);
        initData(table);
    }

    private void initData(String table) {
        //设置进来就显示的第一个管类
        dapter = new BasicsDapter(getActivity(), list, table);
        gridView.setAdapter(dapter);
        dapter.notifyDataSetInvalidated();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                Intent intent = new Intent(getActivity(), AddBasicsActivity.class);
                intent.putExtra("table", table);
                intent.putExtra("type","管类");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void initView() {
        list = new ArrayList<>();
        gridView = ((GridView) view.findViewById(R.id.gridView));
        btnAdd = ((TextView) view.findViewById(R.id.btnAdd));
        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        LogUtills.i("A", "onResume");
        initSql(table);
        dapter.notifyDataSetInvalidated();
        super.onResume();
    }


}
