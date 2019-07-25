package com.app.pipelinesurvey.view.fragment.map;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.AppInfo;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.InitWindowSize;
import com.app.utills.LogUtills;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author HaiRun
 * @time 2019/6/26.10:35
 * 现场检测记录表
 */
public class MapSettingFragment extends DialogFragment implements View.OnClickListener {
    private View view;
    private EditText edtPrjCode;
    private EditText edtPrjName;
    private EditText edtPrjSite;
    private EditText edtOriginal;
    private EditText edtApparatus_name;
    private EditText edtApparatus_code;
    private EditText edtDetection_standard;
    private EditText edtDetection_method;
    private EditText edtDetection_map;
    private EditText edtGroup_leader;
    private EditText edtGroup_mumber1;
    private EditText edtGroup_mumber2;
    private EditText edtDate;
    private EditText edtRemark;
    private TextView tvReturn;
    private final String TABLE_NAME = "log_sheet";
    private boolean update = false;
    private String prjName;
    private ImageView imgvOwnershipUnit;
    private String[] method = null;
    private TextView tvSubmit;
    /**
     * 判断是否是新建项目 3 打开已经存在项目
     */
    private String type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map_setting, container, false);
        initView();
        return view;
    }

    /**
     * 初始化view
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/6/26  10:40
     */
    private void initView() {
        edtPrjCode = view.findViewById(R.id.edtPrjCode);
        edtPrjName = view.findViewById(R.id.edtPrjName);
        edtPrjSite = view.findViewById(R.id.edtPrjSite);
        edtOriginal = view.findViewById(R.id.edtOriginal);
        edtApparatus_name = view.findViewById(R.id.edtApparatus_name);
        edtApparatus_code = view.findViewById(R.id.edtApparatus_code);
        edtDetection_standard = view.findViewById(R.id.edtDetection_standard);
        edtDetection_method = view.findViewById(R.id.edtDetection_method);
        edtDetection_map = view.findViewById(R.id.edtDetection_map);
        edtGroup_leader = view.findViewById(R.id.edtGroup_leader);
        edtGroup_mumber1 = view.findViewById(R.id.edtGroup_mumber1);
        edtGroup_mumber2 = view.findViewById(R.id.edtGroup_mumber2);
        edtDate = view.findViewById(R.id.edtDate);
        edtRemark = view.findViewById(R.id.edtRemark);
        tvReturn = view.findViewById(R.id.tvReturn);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        imgvOwnershipUnit = view.findViewById(R.id.imgvOwnershipUnit);
        tvSubmit = view.findViewById(R.id.tvSubmit);
        tvTitle.setText("现场检测记录表设置");

    }

    @Override
    public void onStart() {
        super.onStart();
        InitWindowSize.ins().initWindowSize(getActivity(), getDialog());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEvent();
        initData();
    }

    private void initEvent() {
        tvReturn.setOnClickListener(this);
        imgvOwnershipUnit.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    /**
     * 初始化数据
     * @Params :
     * @author :HaiRun
     * @date :2019/6/26  11:18
     */
    private void initData() {
        method = new String[]{"调查", "夹钳", "感应", "直连", "测量", "RTK"};
        //工程名称
        prjName = getArguments().getString("prj_name");
        type = getArguments().getString("type");
        setDataToView(edtOriginal, prjName);
        edtDate.setText(DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));

        //查询当天的项目 工作是否已经插入了，如果有，则更新，否则插入
        Cursor query0 = DatabaseHelpler.getInstance().query(TABLE_NAME, "where original = '" + prjName + "' and date = '" +
                DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT) + "' order by date desc");
        if (query0.getCount() > 0) {
            update = true;
        }
        query0.close();
        //查询数据并且排序  降序
        Cursor query = null;
        if (type.equals("3")) {
            //打开原有项目，数据默认此项目上一条
            query = DatabaseHelpler.getInstance().query(TABLE_NAME, "where original = '" + prjName + "' order by id desc");
        } else {
            //新建项目，数据默认上个项目最后一条
            query = DatabaseHelpler.getInstance().query(TABLE_NAME, "order by id desc");
        }
        if (query.getCount() > 0) {
            query.moveToFirst();
            //工程代码
            String prjCode = query.getString(query.getColumnIndex("prj_code"));
            setDataToView(edtPrjCode, prjCode);
            //工程名称
            String prjName = query.getString(query.getColumnIndex("prj_name"));
            setDataToView(edtPrjName, prjName);
            //工程地点
            String prjSite = query.getString(query.getColumnIndex("prj_site"));
            setDataToView(edtPrjSite, prjSite);
            //仪器名称1
            String apparatusName1 = query.getString(query.getColumnIndex("apparatus_name1"));
            setDataToView(edtApparatus_name, apparatusName1);
            //仪器名称2
            String apparatusName2 = query.getString(query.getColumnIndex("apparatus_name2"));
            //仪器编号1
            String apparatusCode1 = query.getString(query.getColumnIndex("apparatus_code1"));
            setDataToView(edtApparatus_code, apparatusCode1);
            //仪器编号2
            String apparatusCode2 = query.getString(query.getColumnIndex("apparatus_code2"));
            //检测标准
            String detectionStandard = query.getString(query.getColumnIndex("detection_standard"));
            setDataToView(edtDetection_standard, detectionStandard);
            //检测日期
            String detectionDate = query.getString(query.getColumnIndex("detection_date"));
//            setDataToView(edtDate,detectionDate);
            //点号
            String pointName = query.getString(query.getColumnIndex("point_name"));
            //工作量
            String workload = query.getString(query.getColumnIndex("workload"));
            //检测方法
            String detectionMethod = query.getString(query.getColumnIndex("detection_method"));
            setDataToView(edtDetection_method, detectionMethod);
            //检测图幅
            String detectionMap = query.getString(query.getColumnIndex("detection_map"));
            setDataToView(edtDetection_map, detectionMap);
            //组长
            String groupLeader = query.getString(query.getColumnIndex("group_leader"));
            setDataToView(edtGroup_leader, groupLeader);
            //组员1
            String groupMumber1 = query.getString(query.getColumnIndex("group_mumber1"));
            setDataToView(edtGroup_mumber1, groupMumber1);
            //组员2
            String groupMember2 = query.getString(query.getColumnIndex("group_member2"));
            setDataToView(edtGroup_mumber2, groupMember2);
            //备注
            String remark = query.getString(query.getColumnIndex("remark"));
            setDataToView(edtRemark, remark);
        }

        query.close();

    }

    /**
     * 设置数据到EditView
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/6/26  16:29
     */
    private void setDataToView(EditText view, String data) {
        view.setText(data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReturn:
            case R.id.tvSubmit:
                saveDataToDB();
                getDialog().dismiss();
                break;
            case R.id.imgvOwnershipUnit:
                showDialog(method, edtDetection_method, "检测方法");
                break;
            default:
                break;
        }
    }

    /**
     * 保存数据数据库
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/6/26  16:54
     */
    private void saveDataToDB() {
        ContentValues values = new ContentValues();
        values.put("prj_name", getViewData(edtPrjName));
        values.put("prj_code", getViewData(edtPrjCode));
        values.put("prj_site", getViewData(edtPrjSite));
        values.put("original", getViewData(edtOriginal));
        values.put("apparatus_name1", getViewData(edtApparatus_name));
        values.put("apparatus_code1", getViewData(edtApparatus_code));
        values.put("detection_standard", getViewData(edtDetection_standard));
        values.put("detection_date", getViewData(edtDate));
        values.put("detection_method", getViewData(edtDetection_method));
        values.put("detection_map", getViewData(edtDetection_map));
        values.put("group_leader", getViewData(edtGroup_leader));
        values.put("group_mumber1", getViewData(edtGroup_mumber1));
        values.put("group_member2", getViewData(edtGroup_mumber2));
        values.put("remark", getViewData(edtRemark));
        values.put("date", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));
        if (update) {
            DatabaseHelpler.getInstance().update(TABLE_NAME, values, "original = ? and date = ?", new String[]{prjName, DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT)});
            LogUtills.i("sql update");
        } else {
            DatabaseHelpler.getInstance().insert(TABLE_NAME, values);
            LogUtills.i("sql insert");
        }
    }

    private String getViewData(EditText view) {
        String str = "";
        str = view.getText().toString().trim();
        return str;
    }

    /**
     * 多选框
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/2  11:23
     */
    private void showDialog(final String[] data, final EditText textView, String title) {
        textView.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(title);
        final boolean[] selectItems = new boolean[data.length];
        for (int i = 0; i < data.length; i++) {
            selectItems[i] = false;
        }
        /**
         * 第一个参数指定我们要显示的一组下拉多选框的数据集合
         * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
         * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
         * 第三个参数给每一个多选项绑定一个监听器
         */
        builder.setMultiChoiceItems(data, selectItems, new DialogInterface.OnMultiChoiceClickListener() {
            StringBuffer sb = new StringBuffer(100);

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    //选择的选项保存到sb中
                    sb.append(data[which] + "+");
                }
                String s = sb.toString();
                String data = s.substring(0, s.length() - 1);
                textView.setText(data);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                textView.setText("");
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
