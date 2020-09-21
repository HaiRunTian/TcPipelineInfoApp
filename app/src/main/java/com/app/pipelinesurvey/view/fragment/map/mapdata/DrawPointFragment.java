package com.app.pipelinesurvey.view.fragment.map.mapdata;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.PointFieldFactory;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.BaseInfo.Oper.OperSql;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.CameraUtils;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.MaxExpNumID;
import com.app.pipelinesurvey.utils.MyAlertDialog;
import com.app.pipelinesurvey.utils.SymbolInfo;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.iview.IDrawPipePointView;
import com.app.pipelinesurvey.view.iview.IQueryPipePointView;
import com.app.pipelinesurvey.view.widget.AppendanSpinner;
import com.app.pipelinesurvey.view.widget.FeaturePointsSpinner;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author HaiRun
 * @date
 */
public class DrawPointFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener, IDrawPipePointView, IQueryPipePointView, AdapterView.OnItemLongClickListener {
    /**
     * 编辑管线
     */
    private TextView tvTitle;
    /**
     * 提交
     */
    private TextView tvSubmit;
    /**
     * 导入上一个数据
     */
    private Button btnPreviousOne;
    /**
     * 点号
     */
    private EditText edtGpId;
    /**
     * 点号2
     */
    private Spinner spSituation;
    /**
     * 状态
     */
    private Spinner spState;
    /**
     * 井盖材质
     */
    private Spinner spWellLidTexture;
    /**
     * 井规格
     */
    private EditText edtWellSize;
    /**
     * 井深度
     */
    private EditText edtWellDepth;
    /**
     * 水深
     */
    private EditText edtWellWater;
    /**
     * 淤泥
     */
    private EditText edtWellMud;
    /**
     * 井盖规格
     */
    private EditText edtWellLidSize;
    /**
     * 高程
     */
    private EditText edtElevation;
    /**
     * 管偏
     */
    private EditText edtOffset;
    /**
     * 建构筑物
     */
    private EditText edtBuildingStructures;
    /**
     * 路名
     */
    private EditText edtRoadName;
    /**
     * 点备注
     */
    private EditText edtPointRemark;
    /**
     * 疑难
     */
    private EditText edtPuzzle;
    /**
     * 下拉框按钮
     */
    private ImageView imgvPointRemark;
    /**
     * 附属物相关面板
     */
    private LinearLayout linearAppendantPanel;
    /**
     * 返回
     */
    private LinearLayout linearReturn;
    /**
     * 管点备注布局
     */
    private LinearLayout layoutLineMark;
    /**
     * 动画
     */
    private Animation m_animation;
    /**
     * 特征点数据
     */
    private List<String> featurePointsList;
    /**
     * 附属物数据
     */
    private List<String> appendantList;
    /**
     * 点状况数据
     */
    private List<String> situationList;
    /**
     * 编辑管线
     */
    private List<String> stateList;
    /**
     * 井盖材质数据
     */
    private List<String> wellLidTextureList;
    /**
     * 点备注数据
     */
    private List<String> pointRemarkList;

    private ArrayAdapter<String> m_adapter;
    /**
     * 动画开关
     */
    private boolean animSwitch = false;

    private ListPopupWindow _popupWindow;
    /**
     * 添加照片按钮
     */
    private Button btnAddPic;
    /**
     * 照片展示
     */
    private GridView grdPicCon;
    private int m_smId = -1;
    private double m_pointX = 0.0;
    private double m_pointY = 0.0;
    private Button btnSave;
    /**
     * 最终生成的img文件
     */
    private File resultImgFile;
    /**
     * 照片全名xx.jpg
     */
    private String pictureName = "";
    /**
     * 临时图片文件名数组
     */
    private List<String> picNames;
    /**
     * 临时图片文件数组
     */
    private List<File> picFiles;
    /**
     * 适配器数据
     */
    private ArrayList<HashMap<String, Object>> imageItem;
    /**
     * 适配器
     */
    private SimpleAdapter simpleAdapter;
    private String m_gpId;
    private int m_lastReSetSmid = -1;
    private EditText edtDepth;
    /**
     * 照片名字自增
     */
    private int m_PicIndex = 0;
    /**
     * 管类代码
     */
    private String m_code;
    /**
     * 流水号数组
     */
    private String[] m_num;
    private View m_view;
    private File m_pictureName;
    private FeaturePointsSpinner spFeaturePoints;
    private AppendanSpinner spAppendant;
    private String gpType;
    private EditText edtX;
    private EditText edtY;
    private boolean importLast = false;
    private BaseFieldPInfos baseFieldPInfos;
    private String picFileName;
    private long startTime;
    private TextView tvWellSize;
    private TextView tvWellLidTexture;
    private View layout_well_size;
    private View layout_well_depth;
    private View layout_well_water;
    private View layout_well_mud;
    private View layout_well_lid_texture;
    private View layout_well_lid_size;
    private View layout_dc;
    private CheckBox cbDc;
    /**
     * 系统自增ID,判断用户是否修改了ID
     */
    private String initID;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
//        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.activity_draw_pipe_point, container, false);
        initView(m_view);
        startTime = System.currentTimeMillis();
        LogUtills.i("Time21 = ", startTime + "");
        return m_view;
    }

    /**
     * 初始化每一行view,根据数据库表用户设置是否显示
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/8/23  16:58
     */
    private void initlayoutView(View m_view) {

        View laayoutGpid = m_view.findViewById(R.id.layout_gpid);
        View layoutFeature = m_view.findViewById(R.id.layout_feature);
        View layoutAppendant = m_view.findViewById(R.id.layout_appendant);
        layout_well_size = m_view.findViewById(R.id.layout_well_size);
        layout_well_depth = m_view.findViewById(R.id.layout_well_depth);
        layout_well_water = m_view.findViewById(R.id.layout_well_water);
        layout_well_mud = m_view.findViewById(R.id.layout_well_mud);
        layout_well_lid_texture = m_view.findViewById(R.id.layout_well_lid_texture);
        layout_well_lid_size = m_view.findViewById(R.id.layout_well_lid_size);
        layout_dc = m_view.findViewById(R.id.layout_dc);
        View layout_state = m_view.findViewById(R.id.layout_state);
        View layout_elevation = m_view.findViewById(R.id.layout_elevation);
        View layout_depth = m_view.findViewById(R.id.layout_depth);
        View layout_offset = m_view.findViewById(R.id.layout_offset);
        View layout_building_structures = m_view.findViewById(R.id.layout_building_structures);
        View layout_road_name = m_view.findViewById(R.id.layout_road_name);
        View layout_point_remark = m_view.findViewById(R.id.layout_point_remark);
        View layout_puzzle = m_view.findViewById(R.id.layout_puzzle);
        View layout_x = m_view.findViewById(R.id.layout_x);
        View layout_y = m_view.findViewById(R.id.layout_y);

        // 根据数据库表 判断view师傅显示
        String _gpType = getArguments().getString("gpType");
        Cursor cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_POINT_SETTING, "where prj_name = '"
                + SuperMapConfig.PROJECT_NAME + "' and pipetype = '" + _gpType + "'");
        while (cursor.moveToNext()) {
            int wellsize = cursor.getInt(cursor.getColumnIndex("wellsize"));
            int welldepth = cursor.getInt(cursor.getColumnIndex("welldepth"));
            int wellwater = cursor.getInt(cursor.getColumnIndex("wellwater"));
            int wellmud = cursor.getInt(cursor.getColumnIndex("wellmud"));
            int welllidtexture = cursor.getInt(cursor.getColumnIndex("welllidtexture"));
            int welllidsize = cursor.getInt(cursor.getColumnIndex("welllidsize"));
            int state = cursor.getInt(cursor.getColumnIndex("state"));
            int elevation = cursor.getInt(cursor.getColumnIndex("elevation"));
            int offset = cursor.getInt(cursor.getColumnIndex("offset"));
            int building = cursor.getInt(cursor.getColumnIndex("building"));
            int roadname = cursor.getInt(cursor.getColumnIndex("roadname"));
            int pointremark = cursor.getInt(cursor.getColumnIndex("pointremark"));
            int puzzle = cursor.getInt(cursor.getColumnIndex("puzzle"));
            int x = cursor.getInt(cursor.getColumnIndex("x"));
            int y = cursor.getInt(cursor.getColumnIndex("y"));

            layout_well_size.setVisibility(wellsize == 1 ? View.VISIBLE : View.GONE);
            layout_well_depth.setVisibility(welldepth == 1 ? View.VISIBLE : View.GONE);
            layout_well_water.setVisibility(wellwater == 1 ? View.VISIBLE : View.GONE);
            layout_well_mud.setVisibility(wellmud == 1 ? View.VISIBLE : View.GONE);
            layout_well_lid_texture.setVisibility(welllidtexture == 1 ? View.VISIBLE : View.GONE);
            layout_well_lid_size.setVisibility(welllidsize == 1 ? View.VISIBLE : View.GONE);
            layout_state.setVisibility(state == 1 ? View.VISIBLE : View.GONE);
            layout_elevation.setVisibility(elevation == 1 ? View.VISIBLE : View.GONE);
            layout_offset.setVisibility(offset == 1 ? View.VISIBLE : View.GONE);
            layout_building_structures.setVisibility(building == 1 ? View.VISIBLE : View.GONE);
            layout_road_name.setVisibility(roadname == 1 ? View.VISIBLE : View.GONE);
            layout_point_remark.setVisibility(pointremark == 1 ? View.VISIBLE : View.GONE);
            layout_puzzle.setVisibility(puzzle == 1 ? View.VISIBLE : View.GONE);
            layout_x.setVisibility(x == 1 ? View.VISIBLE : View.GONE);
            layout_y.setVisibility(y == 1 ? View.VISIBLE : View.GONE);
        }
        cursor.close();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initlayoutView(m_view);
        initValue();
        initID();
        long endTime = System.currentTimeMillis() - startTime;
        LogUtills.i("Time22 = ", endTime + "");
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化管点编号ID
     */
    private void initID() {
        //管线种类和代码 排水-P
        gpType = getArguments().getString("gpType");
        //从第三位开始截取管类代码
        m_code = gpType.substring(3);
        LogUtills.i("Code=" + m_code);
        //初始化管点编号
        m_num = ComTool.Ins().getPointNumber(m_code, false, "");
        //设置编号到view中
        edtGpId.setText(m_num[0]);
        initID = m_num[0];
    }

    /**
     * 初始化数据
     */
    private void initValue() {
        String _gpType = getArguments().getString("gpType");
        tvTitle.setText("加点" + "(" + _gpType + ")");
        m_pointX = getArguments().getDouble("x", 0.0);
        m_pointY = getArguments().getDouble("y", 0.0);
        edtX.setText(String.valueOf(m_pointX));
        edtY.setText(String.valueOf(m_pointY));
        m_smId = getArguments().getInt("smid", -1);

        //点特征
        featurePointsList = SpinnerDropdownListManager.getData("feature", _gpType);
        spFeaturePoints.setMemoryCount(5);
        spFeaturePoints.setData(null, (ArrayList<String>) featurePointsList);
        spFeaturePoints.setSelection(featurePointsList.size() - 1);
        SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spFeaturePoints, " ");
        //附属物
        appendantList = SpinnerDropdownListManager.getData("subsid", _gpType);
        spAppendant.setMemoryCount(5);
        spAppendant.setData(null, (ArrayList<String>) appendantList);
        SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spAppendant, " ");
        //点备注
        pointRemarkList = SpinnerDropdownListManager.getData("pointRemark", _gpType);
        //编号状态
        situationList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.situation));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, situationList);
        spSituation.setAdapter(m_adapter);
        //管点状态
        stateList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.state));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, stateList);
        spState.setAdapter(m_adapter);
        //井盖材质
        wellLidTextureList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.wellLidTexture));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, wellLidTextureList);
        spWellLidTexture.setAdapter(m_adapter);

    }

    /**
     * @Params :
     * @author :HaiRun
     * @date :2020/5/28  9:02
     */
    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        linearAppendantPanel = (LinearLayout) view.findViewById(R.id.linearAppendantPanel);
        linearReturn = (LinearLayout) view.findViewById(R.id.linearReturn);
        linearReturn.setOnClickListener(this);
        tvSubmit = (TextView) view.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        edtPointRemark = (EditText) view.findViewById(R.id.edtPointRemark);
        edtPointRemark.setText("");
        imgvPointRemark = (ImageView) view.findViewById(R.id.imgvPointRemark);
        imgvPointRemark.setOnClickListener(this);
        layoutLineMark = (LinearLayout) view.findViewById(R.id.layoutPointMark);
        edtGpId = (EditText) view.findViewById(R.id.edtGpId);
        spSituation = (Spinner) view.findViewById(R.id.spSituation);
        edtWellSize = (EditText) view.findViewById(R.id.edtWellSize);
        edtWellDepth = (EditText) view.findViewById(R.id.edtWellDepth);
        edtWellWater = (EditText) view.findViewById(R.id.edtWellWater);
        edtWellMud = (EditText) view.findViewById(R.id.edtWellMud);
        spWellLidTexture = (Spinner) view.findViewById(R.id.spWellLidTexture);
        edtWellLidSize = (EditText) view.findViewById(R.id.edtWellLidSize);
        spState = (Spinner) view.findViewById(R.id.spState);
        edtOffset = (EditText) view.findViewById(R.id.edtOffset);
        edtElevation = (EditText) view.findViewById(R.id.edtElevation);
        edtBuildingStructures = (EditText) view.findViewById(R.id.edtBuildingStructures);
        edtRoadName = (EditText) view.findViewById(R.id.edtRoadName);
        edtPuzzle = (EditText) view.findViewById(R.id.edtPuzzle);
        btnPreviousOne = (Button) view.findViewById(R.id.btnPreviousOne);
        btnPreviousOne.setOnClickListener(this);
        edtDepth = (EditText) view.findViewById(R.id.edtDepth);
        btnAddPic = (Button) view.findViewById(R.id.btnAddPic);
        btnAddPic.setOnClickListener(this);
        grdPicCon = (GridView) view.findViewById(R.id.gridView1);
        grdPicCon.setOnItemLongClickListener(this);
        grdPicCon.setOnItemClickListener(this);
        btnSave = (Button) view.findViewById(R.id.btnRemove);
        btnSave.setOnClickListener(this);
        spSituation.setOnItemSelectedListener(this);
        spFeaturePoints = (FeaturePointsSpinner) view.findViewById(R.id.spFeaturePoints);
        spAppendant = (AppendanSpinner) view.findViewById(R.id.spAppendant);
        spAppendant.setOnItemSelectedListener(this);
        edtX = view.findViewById(R.id.edtX);
        edtY = view.findViewById(R.id.edtY);
        cbDc = view.findViewById(R.id.cb_dc);
        initTakePicArea();
        //必填项设置红星
        TextView tvPointNum = view.findViewById(R.id.tvPointNum);
        setViewDrawable(tvPointNum);
        TextView tvAppendant = view.findViewById(R.id.tvAppendant);
        setViewDrawable(tvAppendant);
        TextView tvFeaturePoints = view.findViewById(R.id.tvFeaturePoints);
        setViewDrawable(tvFeaturePoints);
        //窨井规格  立管规格
        tvWellSize = view.findViewById(R.id.tvWellSize);
        //窨井 立管材质
        tvWellLidTexture = view.findViewById(R.id.tvWellLidTexture);

        cbDc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    if (getPointRemark().contains("直流地面")) {
                        edtPointRemark.setText(getPointRemark().replace(",直流地面", ""));
                    }
                } else {
                    edtPointRemark.setText(getWellLidTexture() + getWellSize() + ",直流地面");
                }
            }
        });

    }

    private void initTakePicArea() {
        picFiles = new ArrayList<>();
        picNames = new ArrayList<>();
        imageItem = new ArrayList<HashMap<String, Object>>();
    }

    @Override
    public void onStart() {
        super.onStart();
//        InitWindowSize.ins().initWindowSize(getActivity(), getDialog());

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spAppendant:
                String itemStr = spAppendant.getSelectedItem().toString();
                //附属物取“孔”“井”时铺开隐藏面板
                if (itemStr.contains("孔") || itemStr.contains("井") || itemStr.contains("篦") || itemStr.contains("立管")) {
                    if (linearAppendantPanel.getVisibility() != View.VISIBLE) {
                        linearAppendantPanel.setVisibility(View.VISIBLE);
                        if (animSwitch) {
                            m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_autotv_show);
                            linearAppendantPanel.startAnimation(m_animation);
                        }
                    }
                    if (itemStr.contains("立管")) {
                        tvWellSize.setText("立管规格");
                        tvWellLidTexture.setText("立管材质");
                        if (layout_well_depth.getVisibility() != View.GONE) {
                            layout_well_depth.setVisibility(View.GONE);
                            layout_well_water.setVisibility(View.GONE);
                            layout_well_mud.setVisibility(View.GONE);
                            layout_well_lid_size.setVisibility(View.GONE);
                            layout_dc.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvWellSize.setText("窨井规格");
                        tvWellLidTexture.setText("窨井材质");
                        if (layout_well_depth.getVisibility() != View.VISIBLE) {
                            layout_well_depth.setVisibility(View.VISIBLE);
                            layout_well_water.setVisibility(View.VISIBLE);
                            layout_well_mud.setVisibility(View.VISIBLE);
                            layout_well_lid_size.setVisibility(View.VISIBLE);
                            layout_dc.setVisibility(View.GONE);

                        }
                    }
                } else {
                    linearAppendantPanel.setVisibility(View.GONE);
                }
                animSwitch = true;
                break;
            //管点编号状态
            case R.id.spSituation:
                //如果是导入上一个，可以不用处理
                if (!importLast) {
                    updateIdAndPointRemark();
                } else {
                    importLast = false;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 根据用户点击管点编号状态修改管点编号和管点备注
     */
    private void updateIdAndPointRemark() {

        String _situation = spSituation.getSelectedItem().toString();
        String pointRemark = edtPointRemark.getText().toString();
        if (!_situation.contains("正常")) {
            //查询管点备注字符 ，如果之前管点备注已经有这种状态，则替换掉
            if (pointRemark.length() > 0) {
                //多层判断最后一位字母是不是跟状态字母有关系 去掉最后一个字母
                if (pointRemark.contains("满水") || pointRemark.contains("满泥") || pointRemark.contains("信号不良")
                        || pointRemark.contains("推测") || pointRemark.contains("井埋") || pointRemark.contains("打不开")) {
                    edtGpId.setText(getGPId().substring(0, getGPId().length() - 1));
                    String remark = "";
                    if (pointRemark.contains("满水")) {
                        remark = pointRemark.replace("满水", _situation.substring(2));
                    }
                    if (pointRemark.contains("满泥")) {
                        remark = pointRemark.replace("满泥", _situation.substring(2));
                    }
                    if (pointRemark.contains("信号不良")) {
                        remark = pointRemark.replace("信号不良", _situation.substring(2));
                    }
                    if (pointRemark.contains("推测")) {
                        remark = pointRemark.replace("推测", _situation.substring(2));
                    }
                    if (pointRemark.contains("井埋")) {
                        remark = pointRemark.replace("井埋", _situation.substring(2));
                    }
                    if (pointRemark.contains("打不开")) {
                        remark = pointRemark.replace("打不开", _situation.substring(2));
                    }
                    edtPointRemark.setText(remark);
                } else {
                    edtPointRemark.setText(_situation.substring(2) + pointRemark);
                }

            } else {
                edtPointRemark.setText(_situation.substring(2));
            }

            //判断最后一位是否是字母结尾
            Pattern _pattern = Pattern.compile("[a-zA-Z]");
            if (_pattern.matcher(getGPId().substring(getGPId().length() - 1)).find()) {
                edtGpId.setText(getGPId().substring(0, getGPId().length() - 1) + spSituation.getSelectedItem().toString().substring(0, 1));
            } else {
                edtGpId.setText(getGPId() + spSituation.getSelectedItem().toString().substring(0, 1));
            }

        } else {
            //  修改管点代码字母  判断最后一位是否是字母结尾
            Pattern _pattern = Pattern.compile("[a-zA-Z]");
            if (_pattern.matcher(getGPId().substring(getGPId().length() - 1)).find()) {
                //再判断这个字母是不是状态类型的字母   T S Y Z X
                String code = getGPId().substring(getGPId().length() - 1);
                if (code.equals("S") || code.equals("Y") || code.equals("Z") || code.equals("X")) {
                    //多层判断最后一位字母是不是跟状态字母有关系 去掉最后一个字母
                    if (pointRemark.contains("满水") || pointRemark.contains("满泥") || pointRemark.contains("信号不良")
                            || pointRemark.contains("推测") || pointRemark.contains("井埋") || pointRemark.contains("打不开")) {
                        edtGpId.setText(getGPId().substring(0, getGPId().length() - 1));
                    }
                }
            }
            //修改管线备注  如果之前是不正常状态转为正常状态
            if (pointRemark.length() > 0) {
                String remark = "";
                if (pointRemark.contains("满水")) {
                    remark = pointRemark.replace("满水", " ");
                }
                if (pointRemark.contains("满泥")) {
                    remark = pointRemark.replace("满泥", " ");
                }
                if (pointRemark.contains("信号不良")) {
                    remark = pointRemark.replace("信号不良", " ");
                }
                if (pointRemark.contains("推测")) {
                    remark = pointRemark.replace("推测", " ");
                }
                if (pointRemark.contains("井埋")) {
                    remark = pointRemark.replace("井埋", " ");
                }
                if (pointRemark.contains("打不开")) {
                    remark = pointRemark.replace("打不开", " ");
                }
                remark = remark.trim();
                edtPointRemark.setText(remark);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearReturn:
                getDialog().dismiss();
                break;
            //数据提交保存
            case R.id.btnRemove:
            case R.id.tvSubmit:
                try {
                    boolean _result = false;
                    //保存数据 M_SMID 已弃用，待修改
                    if (m_smId == -1) {
                        //判断重号
                        if (ComTool.Ins().isSameNum(getGPId(), false)) {
                            ToastyUtil.showErrorShort(getActivity(), "点号重复，请重新编号");
                            return;
                        }
//                        if (checkValue()) {
//                            ToastyUtil.showInfoShort(getActivity(), "井深必填，请检查是否填写了井深");
//                            return;
//                        }
                        _result = DataHandlerObserver.ins().createRecords2(generateBaseFieldInfo());
                        if (!_result) {
                            ToastyUtil.showErrorShort(getActivity(), "保存点数据失败...");
                            return;
                        }
                        //地图刷新
                        WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
                        if (!initID.equals(getGPId())) {
                            int id = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
                            MaxExpNumID.getInstance().setId(id);
                        } else {
                            MaxExpNumID.getInstance().setId(MaxExpNumID.getInstance().getId() + 1);
                        }
                        WorkSpaceUtils.getInstance().saveMap();
                        getDialog().dismiss();
                    }
                } catch (Exception e) {
                    LogUtills.e(e.toString());
                }
                break;
            case R.id.imgvPointRemark:
                _popupWindow = new ListPopupWindow(getActivity());
                _popupWindow.setWidth(layoutLineMark.getWidth() - 5);
                m_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_text, pointRemarkList);
                _popupWindow.setAdapter(m_adapter);
                _popupWindow.setAnchorView(edtPointRemark);
                _popupWindow.setModal(true);
                _popupWindow.setOnItemClickListener(this);
                _popupWindow.show();
                break;
            //导入上一个
            case R.id.btnPreviousOne:
                importLast = true;
                importData();
                break;
            //开启相机拍照
            case R.id.btnAddPic:
                openCamera();
                break;
            default:
                break;
        }
    }

    /**
     * 检测窨井深度有没有填写
     *
     * @return
     */
    private boolean checkValue() {
        boolean value = false;
        if (gpType.contains("雨水") || gpType.contains("污水") || gpType.contains("排水")) {
            if (getAppendant().contains("井")) {
                if (getWellDepth().length() == 0) {
                    value = true;
                }
            }
        }
        return value;
    }

    /**
     * 手机拍照
     *
     * @auther HaiRun
     * created at 2018/7/24 10:29
     */
    private void openCamera() {

        //照片名字
        try {
            m_PicIndex++;
            if (!FileUtils.getInstance().isDirExsit(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH)) {
                FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH);
            }
            picFileName = SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH + getGPId() + "_" + m_PicIndex + ".jpg";
            File m_pictureName = new File(picFileName);
            if (!m_pictureName.exists()) {
                m_pictureName.createNewFile();
            }
            Uri fileUri = FileProvider.getUriForFile(getContext(), "com.app.pipelinesurvey", m_pictureName);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 赋值设置数据
     *
     * @return
     */
    private BaseFieldPInfos generateBaseFieldInfo() {
        BaseFieldPInfos _info = null;
        try {
            String gpType = getArguments().getString("gpType");
            //截取管类代码  给水-J  给水-JS 从第三位开始
            String _layerType = gpType.trim().substring(3);
            _info = PointFieldFactory.CreateInfo(gpType.substring(0, 2));
            if (_info == null) {
                return null;
            }
            _info.pipeType = gpType;
            //管类代码
            _info.shortCode = _layerType;
            _info.exp_Num = getGPId();
            //管类代码
            _info.code = m_code;
            _info.feature = getFeaturePoints();
            _info.subsid = getAppendant();
            _info.pipeOffset = getOffset();
            _info.wellSize = getWellSize();
            _info.wellDeep = getWellDepth();
            _info.wellWater = getWellWater();
            _info.wellMud = getWellMud();
            _info.road = getRoadName();
            //外检模式保存删除点线数据到对应到两张表
            if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                //记录点
                OperSql.getSingleton().inserPoint(getGPId(), m_pointX, m_pointY, "外检:新增");

                _info.Edit = "外检:新增";
            }
            _info.state = getState();
            String date = DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT);
            _info.exp_Date = date;
            _info.wellCoverMaterial = getWellLidTexture();
            _info.wellCoverSize = getWellLidSize();
            _info.buildingStructures = getBuildingStructures();
            _info.longitude = m_pointX;
            _info.latitude = m_pointY;
            _info.surf_H = getElevation();
            //组号
            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
            if (_cursor.moveToNext()) {
                _info.expGroup = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
            }
            _cursor.close();
            _info.remark = getPointRemark();
            _info.picture = getPictureName();
            //标签专题图表达式
            _info.id = getGPId();
            _info.puzzle = getPuzzle();
            _info.situation = getSituation();
            _info.sysId = m_smId;
            _info.depth = getDepth();
            _info.symbol = SymbolInfo.Ins().getSymbol(gpType, getAppendant(), getFeaturePoints());
            _info.symbolExpression = _layerType + "-" + _info.symbol;
            LogUtills.i(_info.toString());
            //判断用户是否修改了管点编号
            if (!m_num[0].equals(getGPId())) {
                _info.serialNum = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
            } else {
                _info.serialNum = Integer.parseInt(m_num[1]);
            }

            Cursor _cursor2 = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_DEFAULT_POINT_SETTING,
                    new String[]{"symbolID", "scaleX", "scaleY"}, "name=?", new String[]{_info.symbolExpression.trim().toString()}, null, null, null);

            LogUtills.i("Sql:" + _cursor2.getCount());
            if (_cursor2.moveToNext()) {
                _info.symbolID = _cursor2.getInt(_cursor2.getColumnIndex("symbolID"));
                _info.symbolSizeX = _cursor2.getDouble(_cursor2.getColumnIndex("scaleX"));
                _info.symbolSizeY = _cursor2.getDouble(_cursor2.getColumnIndex("scaleY"));
            }
            _cursor2.close();
        } catch (Exception e) {
            LogUtills.i(e.getMessage());
        }
        LogUtills.i(_info.toString());
        return _info;
    }

    /**
     * 导入上一条记录集记录
     */
    private void importData() {
        QueryParameter _parameter = new QueryParameter();
        _parameter.setAttributeFilter("code = '" + m_code + "'");
        _parameter.setCursorType(CursorType.STATIC);
        _parameter.setOrderBy(new String[]{"SmID desc"});
        Recordset _reSet = DataHandlerObserver.ins().QueryRecordsetByParameter(_parameter, true);
        if (!_reSet.isEmpty()) {
//            _reSet.moveLast();
            baseFieldPInfos = BaseFieldPInfos.createFieldInfo(_reSet);
            setSituation();
            setAppendant();
            setFeaturePoints();
            setState();
            setWellLidTexture();
            setElevation();
            setOffset();
            setBuildingStructures();
            setRoadName();
            setPointRemark();
            setPuzzle();

        } else {
            ToastyUtil.showInfoShort(getActivity(), "您未录入过此类型管点数据");
            LogUtills.i("您未录入过此类型管点数据");
        }
        _reSet.close();
        _reSet.dispose();
    }

    private void importDataToView(EditText view, String str) {
        if (str.length() > 0) {
            view.setText(str);
        } else {
            view.setText("");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.getId() != -1) {
            String _str = edtPointRemark.getText().toString();
            edtPointRemark.setText(_str + "-" + pointRemarkList.get(position));
            _popupWindow.dismiss();
        } else {
            viewPicture(position);
        }
    }

    /**
     * 查看图片
     */
    private void viewPicture(int position) {
        if (picFiles.get(position) != null) {
            //打开照片查看
            File file = picFiles.get(position);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri _uri;
            if (Build.VERSION.SDK_INT >= 24) {
                _uri = FileProvider.getUriForFile(getContext().getApplicationContext(), "com.app.pipelinesurvey", file);
            } else {
                _uri = Uri.fromFile(file);
            }

            intent.setDataAndType(_uri, CameraUtils.IMAGE_UNSPECIFIED);
            startActivity(intent);

        }
    }

    @Override
    public void setGPId() {
    }

    @Override
    public void setSituation() {
        String situation = baseFieldPInfos.situation;
        if (situation.length() != 0) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spSituation, situation);
        }
    }

    @Override
    public void setFeaturePoints() {
        String _featurePoint = baseFieldPInfos.feature;
        if (_featurePoint.length() != 0) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spFeaturePoints, _featurePoint);
        }
    }

    @Override
    public void setAppendant() {
        String _appendant = baseFieldPInfos.subsid;
        if (_appendant.length() != 0) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spAppendant, _appendant);
        }
        //设置附属物是否可见
        if (_appendant.contains("孔") || _appendant.contains("井") || _appendant.contains("篦") || _appendant.contains("立管")) {
            if (linearAppendantPanel.getVisibility() != View.VISIBLE) {
                linearAppendantPanel.setVisibility(View.VISIBLE);
                if (animSwitch) {
                    m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_autotv_show);
                    linearAppendantPanel.startAnimation(m_animation);
                }
            }
            setWellSize();
            setWellDepth();
            setWellWater();
            setWellMud();
            setWellLidTexture();
            setWellLidSize();
            if (_appendant.contains("立管")) {
                tvWellSize.setText("立管规格");
                tvWellLidTexture.setText("立管材质");
                if (layout_well_depth.getVisibility() != View.GONE) {
                    layout_well_depth.setVisibility(View.GONE);
                    layout_well_water.setVisibility(View.GONE);
                    layout_well_mud.setVisibility(View.GONE);
                    layout_well_lid_size.setVisibility(View.GONE);
                    layout_dc.setVisibility(View.VISIBLE);
                }
            } else {
                tvWellSize.setText("窨井规格");
                tvWellLidTexture.setText("窨井材质");
                if (layout_well_depth.getVisibility() != View.VISIBLE) {
                    layout_well_depth.setVisibility(View.VISIBLE);
                    layout_well_water.setVisibility(View.VISIBLE);
                    layout_well_mud.setVisibility(View.VISIBLE);
                    layout_well_lid_size.setVisibility(View.VISIBLE);
                    layout_dc.setVisibility(View.GONE);
                }
            }
        } else {
            linearAppendantPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void setState() {
        String _statue = baseFieldPInfos.state;
        if (_statue.length() != 0) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spState, _statue);
        }
    }

    @Override
    public void setElevation() {
        String _evevation = baseFieldPInfos.surf_H;
        if (_evevation != null) {
            edtElevation.setText(_evevation + "");
        }
    }

    @Override
    public void setOffset() {
        String _offset_local = baseFieldPInfos.pipeOffset;
        if (_offset_local.length() != 0) {
            edtOffset.setText(_offset_local);
        }
    }

    @Override
    public void setBuildingStructures() {
        String _buildingStructures = baseFieldPInfos.buildingStructures;
        if (_buildingStructures.length() != 0) {
            edtBuildingStructures.setText(_buildingStructures);
        }
    }

    @Override
    public void setRoadName() {
        String _roadName = baseFieldPInfos.road;
        if (_roadName.length() != 0) {
            edtRoadName.setText(_roadName);
        }
    }

    @Override
    public void setPointRemark() {
        String _pointRemark = baseFieldPInfos.remark;
        if (_pointRemark.length() != 0) {
            edtPointRemark.setText(_pointRemark);
        }
    }

    @Override
    public void setPuzzle() {
        String _puzzle = baseFieldPInfos.puzzle;
        if (_puzzle.length() != 0) {
            edtPuzzle.setText(_puzzle);
        }
    }

    @Override
    public void setWellSize() {
        String _wellSize = baseFieldPInfos.wellSize;
        if (_wellSize.length() != 0) {
            edtWellSize.setText(_wellSize);
        }
    }

    @Override
    public void setWellDepth() {
        String _wellDepth = baseFieldPInfos.wellDeep;
        if (!_wellDepth.isEmpty()) {
            double s = Double.parseDouble(_wellDepth);
            double temp = s * 100;
            edtWellDepth.setText(new DecimalFormat().format(temp).replace(",", ""));
        }
    }

    @Override
    public void setWellWater() {
        String _wellWater = baseFieldPInfos.wellWater;
        if (!_wellWater.isEmpty()) {
            double s = Double.parseDouble(_wellWater);
            double temp = s * 100;
            edtWellWater.setText(new DecimalFormat().format(temp).replace(",", ""));
        }
    }

    @Override
    public void setWellMud() {
        String _wellMud = baseFieldPInfos.wellMud;
        if (!_wellMud.isEmpty()) {
            double s = Double.parseDouble(_wellMud);
            double temp = s * 100;
            edtWellMud.setText(new DecimalFormat().format(temp).replace(",", ""));
        }
    }

    @Override
    public void setWellLidTexture() {
        String _wellLidTexture = baseFieldPInfos.wellCoverMaterial;
        if (_wellLidTexture.length() != 0) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spWellLidTexture, _wellLidTexture);
        }
    }

    @Override
    public void setWellLidSize() {
        String _wellLIdSize = baseFieldPInfos.wellCoverSize;
        if (_wellLIdSize.length() != 0) {
            edtWellLidSize.setText(_wellLIdSize);
        }
    }

    @Override
    public double getLongitude() {
        return 0;
    }

    @Override
    public double getLatitude() {
        return 0;
    }

    @Override
    public List<String> getPicturefromReSet() {
        return null;
    }

    @Override
    public String getGPId() {
        return edtGpId.getText().toString();
    }

    @Override
    public String getSituation() {
        return spSituation.getSelectedItem().toString();
    }

    @Override
    public String getFeaturePoints() {
        return spFeaturePoints.getSelectedItem().toString();
    }

    @Override
    public String getAppendant() {
        return spAppendant.getSelectedItem().toString();
    }

    @Override
    public String getState() {
        return spState.getSelectedItem().toString();
    }

    @Override
    public String getElevation() {
        String str = edtElevation.getText().toString();
        if (str.length() > 0) {
            double d = Double.valueOf(str);
            return String.format("%.3f", d);
        } else {
            return "0.000";
        }

    }

    /**
     * @param view
     * @return 厘米转换成米返回
     */
    private String getValueByView(EditText view) {
        String value = view.getText().toString();
        try {
            if (value.length() > 0) {
                double d = Double.valueOf(value);
                double s = d / 100;
                return String.valueOf(s);
            } else {
                return "";
            }
        } catch (Exception e) {
            ToastyUtil.showErrorShort(getActivity(), "error " + e.getMessage());
        }
        return "";
    }

    @Override
    public String getOffset() {
        return edtOffset.getText().toString() + "";
    }

    @Override
    public String getBuildingStructures() {
        return edtBuildingStructures.getText().toString() + "";
    }

    @Override
    public String getRoadName() {
        return edtRoadName.getText().toString() + "";
    }

    @Override
    public String getPointRemark() {
        String s = edtPointRemark.getText().toString().trim();
        if (s.startsWith("-")) {
            s = s.substring(1);
        }
        if (s.endsWith("-")) {
            s = s.substring(0, s.length() - 1);
        }
        LogUtills.i("点备注 = ", s);
        return s;
    }

    @Override
    public String getPuzzle() {
        return edtPuzzle.getText().toString();
    }

    @Override
    public String getWellSize() {
        return edtWellSize.getText().toString();
    }

    @Override
    public String getWellDepth() {
        return getValueByView(edtWellDepth);
    }

    @Override
    public String getWellWater() {
        return getValueByView(edtWellWater);
    }

    @Override
    public String getWellMud() {
        return getValueByView(edtWellMud);
    }

    @Override
    public String getWellLidTexture() {
        return spWellLidTexture.getSelectedItem().toString();
    }

    @Override
    public String getWellLidSize() {
        return edtWellLidSize.getText().toString();
    }

    @Override
    public String getPictureName() {
        String jointPictureName = "";
        if (picFiles.size() == 0) {
            jointPictureName = "";
        } else if (picFiles.size() == 1) {
            jointPictureName = picFiles.get(0).getName();
        } else {
            for (File _picFile : picFiles) {
                jointPictureName += _picFile.getName() + "#";
            }
            jointPictureName = jointPictureName.substring(0, jointPictureName.length() - 1);
        }
        return jointPictureName;
    }

    @Override
    public String getDepth() {
        return "0";
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        MyAlertDialog.showAlertDialog(getActivity(), "删除提示", "确定删除改照片？", "确定", "取消", true,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //从手机卡删除
                        FileUtils.getInstance().deleteFile(picFiles.get(position));
                        imageItem.remove(position);
                        picNames.remove(position);
                        picFiles.remove(position);
                        refreshGridviewAdapter();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return false;
    }

    /**
     * 手机拍照回调
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            switch (requestCode) {
                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO:
                    Bitmap picBitmap = null;
                    File file = new File(picFileName);
                    if (!file.exists()) {
                        ToastyUtil.showErrorShort(getActivity(), "照片不存在");
                    }
                    picBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    picBitmap = CameraUtils.comp(picBitmap);
                    if (picBitmap != null) {
                        pictureName = file.getName();
                        picNames.add(pictureName);
                        picFiles.add(file);
                        HashMap<String, Object> _map = new HashMap<>();
                        _map.put("itemImage", picBitmap);
                        _map.put("picName", pictureName);
                        imageItem.add(_map);
                        refreshGridviewAdapter();
                    } else {
                        ToastyUtil.showErrorShort(getActivity(), "图片名不允许带特殊符号");
                    }

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LogUtills.e("camera", "new safety log error e:=" + e.getMessage().toString() + "");
            ToastyUtil.showErrorShort(getActivity(), "new safety log error e:=" + e.getMessage());
        }
    }

    /**
     * 刷新图片区域gridview
     */
    private void refreshGridviewAdapter() {
        if (simpleAdapter == null) {
            simpleAdapter = new SimpleAdapter(getActivity(), imageItem,
                    R.layout.layout_griditem_addpic, new String[]{"itemImage", "picName"}, new int[]{R.id.imageView1, R.id.tvPicName});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(final View view, final Object data, String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {//绑定视图
                                ImageView i = (ImageView) view;
                                i.setImageBitmap((Bitmap) data);
                            }
                        });
                        return true;
                    } else if (view instanceof TextView) {
                        TextView tv = (TextView) view;
                        tv.setText(textRepresentation);
                    }
                    return false;
                }
            });

            /**
             * 主线程绑定adapter刷新数据
             */
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    grdPicCon.setAdapter(simpleAdapter);
                    simpleAdapter.notifyDataSetChanged();
                }
            });
        } else {
            simpleAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置TextView右边星号
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/6/24  16:27
     */
    private void setViewDrawable(TextView textView) {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_must_fill_16);
        drawable.setBounds(0, 0, 20, 20);
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(String s) {
        String itemStr = s;
        //附属物取“孔”“井”时铺开隐藏面板
        if (itemStr.contains("孔") || itemStr.contains("井") || itemStr.contains("篦") || itemStr.contains("立管")) {
            if (linearAppendantPanel.getVisibility() != View.VISIBLE) {
                linearAppendantPanel.setVisibility(View.VISIBLE);
                if (animSwitch) {
                    m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_autotv_show);
                    linearAppendantPanel.startAnimation(m_animation);
                }
            }
            if (itemStr.contains("立管")) {
                tvWellSize.setText("立管规格");
                tvWellLidTexture.setText("立管材质");
                if (layout_well_depth.getVisibility() != View.GONE) {
                    layout_well_depth.setVisibility(View.GONE);
                    layout_well_water.setVisibility(View.GONE);
                    layout_well_mud.setVisibility(View.GONE);
                    layout_well_lid_size.setVisibility(View.GONE);
                    layout_dc.setVisibility(View.VISIBLE);
                }
            } else {
                tvWellSize.setText("窨井规格");
                tvWellLidTexture.setText("窨井材质");
                if (layout_well_depth.getVisibility() != View.VISIBLE) {
                    layout_well_depth.setVisibility(View.VISIBLE);
                    layout_well_water.setVisibility(View.VISIBLE);
                    layout_well_mud.setVisibility(View.VISIBLE);
                    layout_well_lid_size.setVisibility(View.VISIBLE);
                    layout_dc.setVisibility(View.GONE);
                }
            }
        } else {
            linearAppendantPanel.setVisibility(View.GONE);
        }
        animSwitch = true;
    }
}
