package com.app.pipelinesurvey.view.fragment.map.mapbottom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Point2D;
import com.supermap.data.Recordset;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

import java.util.List;

/**
 * 管点定位查询
 * @author HaiRun
 */
public class QueryPointLocalFragment extends Fragment implements View.OnClickListener {
    private Button btnSubmit;
    private LinearLayout m_layoutContainer;
    private EditText edtPointId;
    public static String s_SqlQueryPoint;
    private int m_smid_;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_querypoint, container, false);
        btnSubmit = _view.findViewById(R.id.btnSubmit);
        edtPointId = _view.findViewById(R.id.edtPointId);
        btnSubmit.setOnClickListener(this);
        m_layoutContainer = getActivity().findViewById(R.id.layoutMapContainer);
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
            case R.id.btnSubmit:
                //模糊查询 忽略大小写 全部转换为小写
                String sqlLower = "lower(exp_Num) like '%'||lower('" + edtPointId.getText().toString() + "')||'%'";
//                String sqlUpper = "upper(exp_Num) like '%'||upper('" + edtPointId.getText().toString() + "')||'%'";
                LogUtills.i(sqlLower);
                Recordset _reSet = DataHandlerObserver.ins().queryRecordsetBySql(sqlLower, false);
                if (!_reSet.isEmpty()) {
                    MapControl _mapControl = WorkSpaceUtils.getInstance().getMapControl();
                    //设置选择样式
                    DataHandlerObserver.ins().setPtSelectionHighLigh(_reSet);
                    Map _map = _mapControl.getMap();
                    double _x = _reSet.getDouble("SmX");
                    double _y = _reSet.getDouble("SmY");
                    Point2D _point2D = new Point2D(_x, _y);
                    double[] _scale = _map.getVisibleScales();
                    _map.setScale(_scale[_scale.length - 1]);
                    _map.setCenter(_point2D);
                    _map.refresh();
                    _reSet.close();
                    _reSet.dispose();

                } else {
                    ToastUtil.show(getActivity(), "没有找到对应点号，请重选输入查询", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }
}
