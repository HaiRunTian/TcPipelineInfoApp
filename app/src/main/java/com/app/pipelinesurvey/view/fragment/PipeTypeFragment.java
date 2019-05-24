package com.app.pipelinesurvey.view.fragment;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.PipeTypeExListViewAdapter;
import com.app.pipelinesurvey.bean.PipeTypeChildInfo;
import com.app.pipelinesurvey.bean.PipeTypeParentInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HaiRun on 2018/12/22.
 * 管类设置类  大类 小类
 */

public class PipeTypeFragment extends Fragment implements View.OnClickListener {

    private ExpandableListView m_exlistView;
    private ImageButton m_imgBtn;
    private ArrayList<PipeTypeParentInfo> m_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_pipe_type,container,false);
        m_exlistView = rootView.findViewById(R.id.exlistview);
        m_imgBtn = rootView.findViewById(R.id.imgBtnAdd);
        initEvent();
        return rootView;

    }

    private void initEvent() {
        m_imgBtn.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        PipeTypeExListViewAdapter adapter = new PipeTypeExListViewAdapter(getActivity(),m_list);
        m_exlistView.setAdapter(adapter);

    }

    private void initData() {
        m_list = new ArrayList<>();
        for (int _i = 0; _i < 5; _i++) {
            PipeTypeParentInfo _parentInfo = new PipeTypeParentInfo();
            _parentInfo.setName("电力");
            ArrayList<PipeTypeChildInfo> _childList = new ArrayList<>();
            _childList.add(new PipeTypeChildInfo("电力",false));
            _childList.add(new PipeTypeChildInfo("供电",false));
            _childList.add(new PipeTypeChildInfo("路灯",false));
            _parentInfo.setChildInfoList(_childList);
            m_list.add(_parentInfo);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtnAdd:

                break;
            default:break;
        }
    }
}
