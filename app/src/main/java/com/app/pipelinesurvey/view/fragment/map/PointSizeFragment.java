package com.app.pipelinesurvey.view.fragment.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SuperMapConfig;

/**
 * @Author HaiRun
 * @Time 2019/3/3.12:56
 */

public class PointSizeFragment extends Fragment implements View.OnClickListener {

    private View m_rootView;
    private Button btnSub,btnAdd,btnConfig;
    private EditText edtPointSize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_rootView = inflater.inflate(R.layout.fragment_point_size, container, false);
        initView();

        return m_rootView;
    }

    private void initView() {
        btnSub = m_rootView.findViewById(R.id.btnsub);
        btnAdd = m_rootView.findViewById(R.id.btnAdd);
        btnConfig = m_rootView.findViewById(R.id.btnconfig);
        edtPointSize = m_rootView.findViewById(R.id.edtPointSize);
        edtPointSize.setText(String.valueOf(SuperMapConfig.User_Query_Point_Size));
        btnSub.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnConfig.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:

                SuperMapConfig.User_Query_Point_Size += 0.5;
                edtPointSize.setText(String.valueOf(SuperMapConfig.User_Query_Point_Size));
                break;
            case R.id.btnsub:
                if (SuperMapConfig.User_Query_Point_Size <= 0)return;
                SuperMapConfig.User_Query_Point_Size -= 0.5;
                if (SuperMapConfig.User_Query_Point_Size < 0)SuperMapConfig.User_Query_Point_Size = 1.0 ;
                edtPointSize.setText(String.valueOf(SuperMapConfig.User_Query_Point_Size));
                break;
            case R.id.btnconfig:
                SuperMapConfig.User_Query_Point_Size = Double.parseDouble(edtPointSize.getText().toString());

                break;
        }
    }
}
