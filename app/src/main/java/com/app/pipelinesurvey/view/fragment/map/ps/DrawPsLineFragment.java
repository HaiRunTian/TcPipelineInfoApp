package com.app.pipelinesurvey.view.fragment.map.ps;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.view.iview.IDrawPsLineView;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLine;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HaiRun
 * @time 2020/1/15.13:47
 */
public class DrawPsLineFragment extends DialogFragment implements IDrawPsLineView, View.OnClickListener {

    private TextView tvReturn;
    private TextView tvTitle;
    private TextView tvSubmit;
    private EditText edtImageNo;
    private EditText edtBeginPoint;
    private EditText edtEndPoint;
    private EditText edtBeginDepth;
    private EditText edtEndDepth;
    private CheckBox cbFair;
    private CheckBox cbEddy;
    private EditText edtPipeMater;
    private EditText edtPipeSize;
    private CheckBox cbYS;
    private CheckBox cbWS;
    private CheckBox cbHS;
    private EditText edtWellNo;
    private EditText edtWellStatus;
    private EditText edtWaterStatus;
    private EditText edtDefectDis;
    private Spinner spDefectCode;
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;
    private EditText edtRoadName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ps_pipe_line_layout, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化view
     *
     * @Params :
     * @author :HaiRun
     * @date :2020/1/15  13:59
     */
    private void initView(View view) {
        tvReturn = view.findViewById(R.id.tvReturn);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("排水外检");
        tvSubmit = view.findViewById(R.id.tvSubmit);
        edtImageNo = view.findViewById(R.id.edt_img_no);
        edtBeginPoint = view.findViewById(R.id.edt_begin_no);
        edtEndPoint = view.findViewById(R.id.edt_end_no);
        edtBeginDepth = view.findViewById(R.id.edt_begin_depth);
        edtEndDepth = view.findViewById(R.id.edt_end_depth);
        cbFair = view.findViewById(R.id.cb_flow_fair);
        cbEddy = view.findViewById(R.id.cb_flow_eddy);
        edtPipeMater = view.findViewById(R.id.edt_pipe_mater);
        edtPipeSize = view.findViewById(R.id.edt_pipe_size);
        cbYS = view.findViewById(R.id.cb_ys);
        cbWS = view.findViewById(R.id.cb_ws);
        cbHS = view.findViewById(R.id.cb_hs);
        edtWellNo = view.findViewById(R.id.edt_well_no);
        edtWellStatus = view.findViewById(R.id.edt_well_status);
        edtWaterStatus = view.findViewById(R.id.edt_water_status);
        edtDefectDis = view.findViewById(R.id.edt_defect_distance);
        spDefectCode = view.findViewById(R.id.sp_defect_code);
        cb1 = view.findViewById(R.id.cb1);
        cb2 = view.findViewById(R.id.cb2);
        cb3 = view.findViewById(R.id.cb3);
        cb4 = view.findViewById(R.id.cb4);
        edtRoadName = view.findViewById(R.id.edt_romake);
        tvSubmit.setOnClickListener(this);
        tvReturn.setOnClickListener(this);
        view.findViewById(R.id.btn_del).setVisibility(View.GONE);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValue();
    }

    /**
     * 初始化数据
     * @Params :
     * @author :HaiRun
     * @date   :2020/2/27  15:35
     */
    private void initValue() {
        Bundle bundle = getArguments();
        BaseFieldPInfos startPoint = bundle.getParcelable("startPoint");
        switch (startPoint.pipeType.substring(0, 2)) {
            case "雨水":
                cbYS.setChecked(true);
                break;
            case "污水":
                cbWS.setChecked(true);
                break;
            case "合流":
                cbHS.setChecked(true);
                break;
            default:
                cbYS.setChecked(true);
                break;
        }
        cbFair.setChecked(true);
        String bePoint = bundle.getString("bePoint");
        String endPoint = bundle.getString("endPoint");
        String beDeep = bundle.getString("beDeep");
        String endDeep = bundle.getString("endDeep");
        String pipeSize = bundle.getString("pipeSize");
        String textrure = bundle.getString("textrure");
        tvTitle.setText("排水检测");
        edtBeginPoint.setText(bePoint);
        edtEndPoint.setText(endPoint);
        edtBeginDepth.setText(beDeep);
        edtEndDepth.setText(endDeep);
        edtPipeSize.setText(pipeSize);
        edtPipeMater.setText(textrure);
        edtRoadName.setText(SuperMapConfig.ROAD_NAME);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public String getImageNo() {
        return edtImageNo.getText().toString() + "";
    }

    @Override
    public String getBeginPoint() {
        return edtBeginPoint.getText().toString() + "";
    }

    @Override
    public String getEndPoint() {
        return edtEndPoint.getText().toString() + "";
    }

    @Override
    public String getBeginDepth() {
        return edtBeginDepth.getText().toString() + "";
    }

    @Override
    public String getEndDepth() {
        return edtEndDepth.getText().toString() + "";
    }

    @Override
    public String getFlow() {
        String flow = "";
        if (cbFair.isChecked()) {
            flow = cbFair.getText().toString();
        }
        if (cbEddy.isChecked()) {
            flow = cbEddy.getText().toString();
        }
        return flow;
    }

    @Override
    public String getPipeMater() {
        return edtPipeMater.getText().toString() + "";
    }

    @Override
    public String getPipeSize() {
        return edtPipeSize.getText().toString() + "";
    }

    @Override
    public String getPipeType() {
        String pipeTpe = "";
        if (cbYS.isChecked()) {
            pipeTpe = cbYS.getText().toString();
        }
        if (cbWS.isChecked()) {
            pipeTpe = cbWS.getText().toString();
        }
        if (cbHS.isChecked()) {
            pipeTpe = cbHS.getText().toString();
        }
        return pipeTpe;
    }

    @Override
    public String getWellNo() {
        return edtWellNo.getText().toString() + "";
    }

    @Override
    public String getWaterStatus() {
        return edtWaterStatus.getText().toString() + "";
    }

    @Override
    public String getWellStatus() {
        return edtWellStatus.getText().toString() + "";
    }

    @Override
    public String getDefectDis() {
        return edtDefectDis.getText().toString() + "";
    }

      @Override
    public String getDefectCode() {
        return spDefectCode.getSelectedItem().toString() + "";
    }

    public String getRoadName() {
        return edtRoadName.getText().toString() + "";
    }

    @Override
    public String getGrade() {
        if (cb1.isChecked()) {
            return cb1.getText().toString() + "";
        }
        if (cb2.isChecked()) {
            return cb2.getText().toString() + "";
        }
        if (cb3.isChecked()) {
            return cb3.getText().toString() + "";
        }
        if (cb4.isChecked()) {
            return cb4.getText().toString() + "";
        }
        return "";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                if (submitData()) {
                    ToastyUtil.showSuccessShort(getActivity(), "添加成功");
                    getDialog().dismiss();
                } else {
                    ToastyUtil.showErrorShort(getActivity(), "添加失败");
                }
                break;

            case R.id.tvReturn:
                getDialog().dismiss();
                break;

            default:
                break;
        }
    }

    /**
     * 提交保存数据集
     *
     * @Params :
     * @author :HaiRun
     * @date :2020/2/25  9:02
     */
    private Boolean submitData() {
        DatasetVector vector = DataHandlerObserver.ins().getPsLrDatasetVector();
        if (vector == null){
            ToastyUtil.showErrorShort(getActivity(),"排水外检数据集为空");
            return false;
        }
        Recordset recordset = vector.getRecordset(false, CursorType.DYNAMIC);
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("benExpNum", getBeginPoint());
            map.put("endExpNum", getEndPoint());
            map.put("videoNumber", getImageNo());
            map.put("benDeep", getBeginDepth());
            map.put("endDeep", getEndDepth());
            map.put("flow", getFlow());
            map.put("material", getPipeMater());
            map.put("pipeType", getPipeType());
            map.put("pipeSize", getPipeSize());
            map.put("wellNumber", getWellNo());
            map.put("wellState", getWellStatus());
            map.put("flowState", getWaterStatus());
            map.put("defectLength", getDefectDis());
            map.put("defectCode", getDefectCode());
            map.put("defectGrade", getGrade());
            map.put("exp_Date", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));
            map.put("roadName",getRoadName());
            SuperMapConfig.ROAD_NAME = getRoadName();
            Bundle bundle = getArguments();
            if (bundle != null) {
                BaseFieldPInfos startPointInfo = getArguments().getParcelable("startPoint");
                BaseFieldPInfos endPointInfo = getArguments().getParcelable("endPointInfo");
                LogUtills.i("Draw", startPointInfo.toString());
                Point2Ds p2ds = new Point2Ds();
                p2ds.add(new Point2D(startPointInfo.longitude, startPointInfo.latitude));
                double longitude = endPointInfo.longitude;
                p2ds.add(new Point2D(longitude, endPointInfo.latitude));
                GeoLine geoLine = new GeoLine(p2ds);
                recordset.addNew(geoLine, map);
            }
            boolean update = recordset.update();
            recordset.close();
            recordset.dispose();
            return update;
        } catch (Exception e) {
            ToastyUtil.showErrorShort(getActivity(), e.toString());
            return false;
        }
    }
}
