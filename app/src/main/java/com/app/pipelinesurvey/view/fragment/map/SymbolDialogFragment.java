package com.app.pipelinesurvey.view.fragment.map;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.SymbolAdapter;
import com.app.pipelinesurvey.bean.Symbolbean;
import com.app.pipelinesurvey.config.GetSymbolConfig;
import com.app.pipelinesurvey.utils.InitWindowSize;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.utills.LogUtills;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HaiRun
 * @time 2019/4/17.8:49
 */

public class SymbolDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final java.lang.String TAG = "SymbolDialogFragment" ;
    List<Symbolbean> _list;
    private RecyclerView m_recyclerView;
    private View m_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.fragment_symbol, container, false);
        initViews();
        LogUtills.i(TAG,"onCreateView()");
        return m_view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
    }

    private void initViews() {
        TextView tvReturn = m_view.findViewById(R.id.tvReturn);
        tvReturn.setOnClickListener(this);
        TextView tvTitle = m_view.findViewById(R.id.tvTitle);
        tvTitle.setText("符号库面板");
        TextView tv = m_view.findViewById(R.id.tvConfig);
        tv.setVisibility(View.GONE);
        m_recyclerView = m_view.findViewById(R.id.recycler_view);
        int spanCount = 5;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        m_recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtills.i(TAG,"onViewCreated()");
        initData();

        _list = GetSymbolConfig.ins().getSymbolConfig();
        LogUtills.i(TAG,"symbol size = "+_list.size() + "");
        final SymbolAdapter _adapter = new SymbolAdapter(_list);
        m_recyclerView.setAdapter(_adapter);
        _adapter.setOnItemClickListener(new SymbolAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                LogUtills.i(TAG,"选择了" + _list.get(position).toString());
                EventBus.getDefault().post(_list.get(position));
                getDialog().dismiss();
            }
        });
    }

    private void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
//        InitWindowSize.ins().initWindowSize(getActivity(), getDialog());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case  R.id.tvReturn:
            getDialog().dismiss();
            break;
            default:break;
        }
    }
}
