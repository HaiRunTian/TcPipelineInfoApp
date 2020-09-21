package com.app.pipelinesurvey.view.fragment.map.mapbottom;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.utills.LogUtills;
import com.supermap.data.Point2D;
import com.supermap.data.Recordset;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

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

                Recordset _reSet = DataHandlerObserver.ins().queryRecordsetBySql(sqlLower, true);
                LogUtills.i("记录集长度 = " + _reSet.getRecordCount());
                if (!_reSet.isEmpty()) {
                    _reSet.moveFirst();
                    _reSet.edit();
                    MapControl _mapControl = WorkSpaceUtils.getInstance().getMapControl();
                    Map _map = _mapControl.getMap();
                    double _x = _reSet.getDouble("longitude");
                    double _y = _reSet.getDouble("latitude");
                    Point2D _point2D = new Point2D(_x, _y);
                    double[] _scale = _map.getVisibleScales();
                    _map.setScale(_scale[_scale.length - 1]);
                    _map.setCenter(_point2D);
                    _map.refresh();
                    _reSet.close();
                    _reSet.dispose();
                } else {
                    ToastyUtil.showInfoShort(getActivity(), "没有找到对应点号，请重选输入查询");
                }
                break;
            default:
                break;
        }
    }
}
