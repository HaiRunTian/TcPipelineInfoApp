package com.app.pipelinesurvey.view.fragment.LinePoint;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.LineAdapter;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.SQLUtils;
import com.app.pipelinesurvey.view.activity.linepoint.LineAllocationActivity;

import java.util.ArrayList;

/**
 * 线
 * Created by Kevin on 2019-03-15.
 */

public class lineFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ListView line;
    private ArrayList<String> lineList;
    private LineAdapter lintAdapter;
    private String lineTable;
    private int status;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_line, container, false);
            initView();
            lineTable = getActivity().getIntent().getStringExtra("line");
            String name = getActivity().getIntent().getStringExtra("name");
            status = SQLUtils.getStatus(name);
            initData();
        }
        return view;
    }
    private void initData() {
        if (status==1) {
            lineList= SQLUtils.getLine(lineTable);
        }else {

            lineList= SQLUtils.getAll(SQLConfig.TABLE_NAME_PIPE_INFO);
        }
        lintAdapter = new LineAdapter(getActivity(), lineList,lineTable);
        line.setAdapter(lintAdapter);
        lintAdapter.notifyDataSetChanged();

        line.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = lineList.get(position);
                Intent intent = new Intent(getActivity(), LineAllocationActivity.class);
                intent.putExtra("lineTable", lineTable);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        lintAdapter.notifyDataSetChanged();
        super.onResume();
    }
    private void initView() {
        lineList = new ArrayList<>();
        line = ((ListView)view.findViewById(R.id.line));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}
