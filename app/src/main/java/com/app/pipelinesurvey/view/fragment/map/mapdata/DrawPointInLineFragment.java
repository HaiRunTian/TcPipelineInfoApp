package com.app.pipelinesurvey.view.fragment.map.mapdata;

import android.annotation.TargetApi;
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
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.app.BaseInfo.Oper.OperNotifyer;
import com.app.BaseInfo.Oper.OperSql;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.PipePointInfo;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.CameraUtils;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.InitWindowSize;
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
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLine;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
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
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author HaiRun
 */
public class DrawPointInLineFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener, IDrawPipePointView, IQueryPipePointView, AdapterView.OnItemLongClickListener {
    /**
     * 标题
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
    //    private Spinner spPointRemark;//点备注
    /**
     * 点号1
     */
    private EditText edtGpId;
    /**
     * 点号2
     */
    private Spinner spSituation;
    /**
     * 附属物
     */
    private AppendanSpinner spAppendant;
    /**
     * 特征点
     */
    private FeaturePointsSpinner spFeaturePoints;
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
     * 状态数据
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
    /**
     * 标题
     */
    private ListPopupWindow _popupWindow;
    /**
     * 添加照片按钮
     */
    private Button btnAddPic;
    /**
     * 照片展示
     */
    private GridView grdPicCon;
    private int m_lineSmid = -1;
    private double m_pointX = 0.0;
    private double m_pointY = 0.0;
    private Button btnSave;
    /**
     * 临时拍照图片
     */
    private Bitmap picBitmap;
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
     * //适配器
     */
    private SimpleAdapter simpleAdapter;
    private String m_gpID;
    private int m_lastReSetSmid = -1;
    private EditText edtDepth;
    private String m_gpType;
    private int m_picIndex = 0;
    private String m_code;
    private String[] m_num;
    private View m_view;
    File m_pictureName;
    private EditText edtX;
    private EditText edtY;
    private BaseFieldPInfos baseFieldPInfos;
    private boolean importLast = false;
    private TextView tvWellSize;
    private TextView tvWellLidTexture;
    private View layout_well_size;
    private View layout_well_depth;
    private View layout_well_water;
    private View layout_well_mud;
    private View layout_well_lid_texture;
    private View layout_well_lid_size;
    private View layout_dc;
    private CheckBox cbDC;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.activity_add_point_to_line, container, false);
        initView(m_view);
        initlayoutView(m_view);
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

        //TODO 根据数据库表 判断view是否显示
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

    }

    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        spAppendant = (AppendanSpinner) view.findViewById(R.id.spAppendant);
        spAppendant.setOnItemSelectedListener(this);
        spFeaturePoints = (FeaturePointsSpinner) view.findViewById(R.id.spFeaturePoints);
        linearAppendantPanel = (LinearLayout) view.findViewById(R.id.linearAppendantPanel);
        linearReturn = (LinearLayout) view.findViewById(R.id.linearReturn);
        linearReturn.setOnClickListener(this);
        tvSubmit = (TextView) view.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        edtPointRemark = (EditText) view.findViewById(R.id.edtPointRemark);
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
        edtOffset.addTextChangedListener(m_watcher);
        spSituation.setOnItemSelectedListener(this);
        edtX = view.findViewById(R.id.edtX);
        edtY = view.findViewById(R.id.edtY);
        cbDC = view.findViewById(R.id.cb_dc);
        initTakePicArea();

        TextView tvPointNum = view.findViewById(R.id.tvPointNum);
        setViewDrawable(tvPointNum);
        TextView tvAppendant = view.findViewById(R.id.tvAppendant);
        setViewDrawable(tvAppendant);
        TextView tvFeaturePoints = view.findViewById(R.id.tvFeaturePoints);
        setViewDrawable(tvFeaturePoints);
        TextView tvDepth = view.findViewById(R.id.tvDepth);
        setViewDrawable(tvDepth);
        //窨井规格  立管规格
        tvWellSize = view.findViewById(R.id.tvWellSize);
        //窨井 立管材质
        tvWellLidTexture = view.findViewById(R.id.tvWellLidTexture);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValue();
        initID();
        EventBus.getDefault().register(this);
    }

    private void initID() {
        m_gpType = getArguments().getString("gpType");
        m_code = m_gpType.substring(3);
        m_num = ComTool.Ins().getPointNumber(m_code, false, "");
        edtGpId.setText(m_num[0]);
    }

    private void initValue() {
        String _gpType = getArguments().getString("gpType");
        tvTitle.setText("线中加点" + "(" + _gpType + ")");
        m_pointX = getArguments().getDouble("x", 0.0);
        m_pointY = getArguments().getDouble("y", 0.0);
        edtX.setText(String.valueOf(m_pointX));
        edtY.setText(String.valueOf(m_pointY));
        //管线SMID
        m_lineSmid = getArguments().getInt("smId", -1);
        //点特征
        featurePointsList = SpinnerDropdownListManager.getData("feature", _gpType);
        spFeaturePoints.setMemoryCount(5);
        spFeaturePoints.setData(null, (ArrayList<String>) featurePointsList);
        SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spFeaturePoints, " ");
        //附属物
        appendantList = SpinnerDropdownListManager.getData("subsid", _gpType);
        spAppendant.setMemoryCount(5);
        spAppendant.setData(null, (ArrayList<String>) appendantList);
        SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spAppendant, " ");
        //管点备注
        pointRemarkList = SpinnerDropdownListManager.getData("pointRemark", _gpType);
        situationList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.situation));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, situationList);
        spSituation.setAdapter(m_adapter);
        //管点状态
        stateList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.state));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, stateList);
        spState.setAdapter(m_adapter);
        //管材
        wellLidTextureList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.wellLidTexture));
    }

    /**
     * 导入上一条记录集记录
     */
    private void importData() {
        QueryParameter _parameter = new QueryParameter();
        _parameter.setAttributeFilter("code = '" + m_code + "'");
        _parameter.setCursorType(CursorType.STATIC);
        _parameter.setOrderBy(new String[]{"SmID asc"});
        Recordset _reSet = DataHandlerObserver.ins().QueryRecordsetByParameter(_parameter, true);
        if (!_reSet.isEmpty()) {
            _reSet.moveLast();
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
        }

    }

    private void importDataToView(EditText view, String str) {
        if (str.length() > 0) {
            view.setText(str);
        } else {
            view.setText("");
        }

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
                        }
                    } else {
                        tvWellSize.setText("窨井规格");
                        tvWellLidTexture.setText("窨井材质");
                        if (layout_well_depth.getVisibility() != View.VISIBLE) {
                            layout_well_depth.setVisibility(View.VISIBLE);
                            layout_well_water.setVisibility(View.VISIBLE);
                            layout_well_mud.setVisibility(View.VISIBLE);
                            layout_well_lid_size.setVisibility(View.VISIBLE);
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearReturn:
                getDialog().dismiss();
                break;
            case R.id.tvSubmit:
            case R.id.btnRemove:
                boolean _result = false;
                //判断重号
                if (ComTool.Ins().isSameNum(getGPId(), false)) {
                    ToastyUtil.showErrorShort(getActivity(), "点号重复，请重新编号");
                    return;
                }
                if (edtDepth.getText().toString().length() == 0) {
                    ToastyUtil.showErrorShort(getActivity(), "深度必填");
                    return;

                }
                _result = DataHandlerObserver.ins().createRecords2(generateBaseFieldInfo());
                if (!_result) {
                    ToastyUtil.showErrorShort(getActivity().getBaseContext(), "保存点数据失败...");
                    return;
                } else {
                    //查询刚添加点的记录集
                    Recordset _reSet = DataHandlerObserver.ins().queryRecordsetByExpNum(getGPId(), false);
                    if (!_reSet.isEmpty()) {
                        _reSet.moveFirst();
                        BaseFieldPInfos _centerPoint = BaseFieldPInfos.createFieldInfo(_reSet);
                        //线记录集
                        DatasetVector _lineDateset = (DatasetVector) DataHandlerObserver.ins().getTotalLrLayer().getDataset();
                        Recordset _lineRecordset = DataHandlerObserver.ins().queryRecordsetBySmid(m_lineSmid, false, true);
                        _lineRecordset.moveFirst();

                        //外检模式
                        if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                            //获取线起点终点
                            String benExpNum = _lineRecordset.getString("benExpNum");
                            String endExpNum = _lineRecordset.getString("endExpNum");
                            //记录删除管线
                            OperSql.getSingleton().inserLine(benExpNum, endExpNum, 0, "线中加点-删除");
                            OperSql.getSingleton().inserLine(benExpNum, _centerPoint.exp_Num, 0, "线中加点-加线");
                            OperSql.getSingleton().inserLine(_centerPoint.exp_Num, endExpNum, 0, "线中加点-加线");
                        }

                        //记录集转map
                        Map<String, Object> _maps = OperNotifyer.getFieldMaps(_lineRecordset);
                        if (_maps.isEmpty()) {
                            ToastyUtil.showInfoShort(getActivity(), "发生未知错误，请重新选择，线中加点");
                            return;
                        }
                        GeoLine _geoLine = (GeoLine) _lineRecordset.getGeometry();

                        //当前数据的集合  返回此线几何对象中指定序号的子对象，以有序点集合的方式返回该子对象
                        Point2Ds _2dts = _geoLine.getPart(0);
                        if (_2dts.getCount() != 2) {
                            return;
                        }
                        //第一条线
                        Point2Ds _startPt = new Point2Ds();
                        _startPt.add(_2dts.getItem(0));
                        _startPt.add(new Point2D(_centerPoint.longitude, _centerPoint.latitude));

                        //第二条线
                        Point2Ds _endPt = new Point2Ds();
                        _endPt.add(new Point2D(_centerPoint.longitude, _centerPoint.latitude));
                        _endPt.add(_2dts.getItem(1));
                        //第一条记录集
                        _lineRecordset.edit();//benExpNum endExpNum
                        _lineRecordset.setGeometry(new GeoLine(_startPt));
                        if (!_lineRecordset.update()) {
                            ToastyUtil.showErrorShort(getActivity(), "第一条线更新失败");
                            return;
                        }

                        _lineRecordset.edit();
                        _lineRecordset.setString("endExpNum", _centerPoint.exp_Num);
                        _lineRecordset.setDouble("endLongitude", _centerPoint.longitude);
                        _lineRecordset.setDouble("endLatitude", _centerPoint.latitude);
                        //改变终点埋深
                        _lineRecordset.setString("endDeep", getDepth());
                        //改变长度
                        _lineRecordset.setString("pipeLength", String.format("%.2f", _lineRecordset.getDouble("SmLength")));
                        if (!_lineRecordset.update()) {
                            ToastyUtil.showErrorShort(getActivity(), "第一条线长度更新失败");
                            return;
                        }
                        //第二条线记录集
                        Recordset _addNewRecordset = _lineDateset.getRecordset(true, CursorType.DYNAMIC);
                        _addNewRecordset.edit();
                        if (!_addNewRecordset.addNew(new GeoLine(_endPt), _maps)) {
                            ToastyUtil.showErrorShort(getActivity(), "第二条线添加失败");
                        }

                        if (!_addNewRecordset.update()) {
                            ToastyUtil.showErrorShort(getActivity(), "第二条线更新失败");
                        }

                        _addNewRecordset.edit();
                        _addNewRecordset.setInt32("sysid", _addNewRecordset.getID());
                        _addNewRecordset.setString("benExpNum", _centerPoint.exp_Num);
                        _addNewRecordset.setDouble("startLongitude", _centerPoint.longitude);
                        _addNewRecordset.setDouble("startLatitude", _centerPoint.latitude);
                        //改变起点埋深
                        _addNewRecordset.setString("benDeep", getDepth());
                        //改变长度
                        _addNewRecordset.setString("pipeLength", String.format("%.2f", _addNewRecordset.getDouble("SmLength")));
                        if (!_addNewRecordset.update()) {
                            ToastyUtil.showErrorShort(getActivity(), "第二条线长度更新失败");
                            return;
                        }
                        _reSet.close();
                        _reSet.dispose();
                        _lineRecordset.close();
                        _lineRecordset.dispose();
                        _addNewRecordset.close();
                        _addNewRecordset.dispose();
                    }
                }
                MaxExpNumID.getInstance().setId(MaxExpNumID.getInstance().getId() + 1);
                getDialog().dismiss();
                break;
            case R.id.imgvPointRemark:
                _popupWindow = new ListPopupWindow(getActivity());
                _popupWindow.setWidth(layoutLineMark.getWidth() - 5);
                m_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, pointRemarkList);
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
        WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
    }

    /**
     * @auther HaiRun
     * created at 2018/7/24 10:29
     */
    private void openCamera() {
        try {
            m_picIndex++;
            //照片名字
            m_pictureName = new File(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/Picture", edtGpId.getText().toString() + "_" + m_picIndex + ".jpg");
            m_pictureName.createNewFile();
            Uri fileUri = FileProvider.getUriForFile(getContext(), "com.app.pipelinesurvey", m_pictureName);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BaseFieldPInfos generateBaseFieldInfo() {

        BaseFieldPInfos _info = null;
        String gpType = getArguments().getString("gpType");
        String _layerType = gpType.trim().substring(3);
        _info = PointFieldFactory.CreateInfo(gpType.substring(0, 2));
        if (_info == null) {
            return null;
        }
        _info.pipeType = gpType;
        _info.code = _layerType;
        _info.shortCode = _layerType;
        _info.exp_Num = getGPId();
        _info.feature = getFeaturePoints();
        _info.subsid = getAppendant();
        _info.pipeOffset = getOffset();
        _info.wellSize = getWellSize();
        _info.wellDeep = getWellDepth();
        _info.wellWater = getWellWater();
        _info.wellMud = getWellMud();
        _info.road = getRoadName();
        if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
            _info.Edit = "外检-线中加点-" + getState();
        }
        _info.state = getState();
        _info.depth = getDepth();
        String date = DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT);
        _info.exp_Date = date;
        _info.wellCoverMaterial = getWellLidTexture();
        _info.wellCoverSize = getWellLidSize();
        _info.buildingStructures = getBuildingStructures();
        _info.longitude = m_pointX;
        _info.latitude = m_pointY;
        _info.surf_H = getElevation();
        Cursor _cursor1 = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
        if (_cursor1.moveToNext()) {
            _info.expGroup = _cursor1.getString(_cursor1.getColumnIndex("GroupNum"));
        }
        if (getAppendant().contains("立管")) {
            if (cbDC.isChecked()) {
                _info.remark = getWellLidTexture() + getWellSize() + ",直流地面" + "-" + getPointRemark();
            } else {
                _info.remark = getWellLidTexture() + getWellSize() + "-" + getPointRemark();
            }
        } else {
            _info.remark = getPointRemark();
        }
        _info.picture = getPictureName();
        _info.id = getGPId();
        _info.puzzle = getPuzzle();
        _info.situation = getSituation();
        //系统smid
        _info.sysId = m_lastReSetSmid;
        _info.picture = getPictureName();
        _info.symbol = SymbolInfo.Ins().getSymbol(gpType, getAppendant(), getFeaturePoints());
        if (_layerType.length() == 2) {
            _layerType = _layerType.substring(0, 1);
        }
        _info.symbolExpression = _layerType + "-" + _info.symbol;
        //判断用户是否修改了管点编号
        if (!m_num[0].equals(getGPId())) {
            _info.serialNum = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
        } else {
            _info.serialNum = Integer.parseInt(m_num[1]);
        }

        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_DEFAULT_POINT_SETTING,
                new String[]{"symbolID", "scaleX", "scaleY"}, "name=?", new String[]{_info.symbolExpression.trim().toString()}, null, null, null);
        LogUtills.i("Sql:" + _cursor.getCount());
        if (_cursor.moveToNext()) {
            _info.symbolID = _cursor.getInt(_cursor.getColumnIndex("symbolID"));
            _info.symbolSizeX = _cursor.getDouble(_cursor.getColumnIndex("scaleX"));
            _info.symbolSizeY = _cursor.getDouble(_cursor.getColumnIndex("scaleY"));
        }
        LogUtills.i(_info.toString());
        return _info;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.getId() != -1) {
            edtPointRemark.setText(pointRemarkList.get(position));
            _popupWindow.dismiss();
        } else {
            viewPicture(position);
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
                }
            } else {
                tvWellSize.setText("窨井规格");
                tvWellLidTexture.setText("窨井材质");
                if (layout_well_depth.getVisibility() != View.VISIBLE) {
                    layout_well_depth.setVisibility(View.VISIBLE);
                    layout_well_water.setVisibility(View.VISIBLE);
                    layout_well_mud.setVisibility(View.VISIBLE);
                    layout_well_lid_size.setVisibility(View.VISIBLE);
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

        if (_pointRemark.contains("直流地面")) {
            cbDC.setChecked(true);
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
            edtWellDepth.setText(new DecimalFormat().format(temp).replace(",",""));
        }
    }

    @Override
    public void setWellWater() {
        String _wellWater = baseFieldPInfos.wellWater;
        if (!_wellWater.isEmpty()) {
            double s = Double.parseDouble(_wellWater);
            double temp = s * 100;
            edtWellWater.setText(new DecimalFormat().format(temp).replace(",",""));
        }
    }

    @Override
    public void setWellMud() {
        String _wellMud = baseFieldPInfos.wellMud;
        if (!_wellMud.isEmpty()) {
            double s = Double.parseDouble(_wellMud);
            double temp = s * 100;
            edtWellMud.setText(new DecimalFormat().format(temp).replace(",",""));
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

    @Override
    public String getOffset() {
        return edtOffset.getText().toString();
    }

    @Override
    public String getBuildingStructures() {
        return edtBuildingStructures.getText().toString();
    }

    @Override
    public String getRoadName() {
        return edtRoadName.getText().toString();
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
        String _temp = edtDepth.getText().toString();
        if (_temp.length() > 0) {
            int s = Integer.parseInt(_temp);
            DecimalFormat df2 = new DecimalFormat("###.00");
            if (_temp.length() > 2) {
                return df2.format(s / 100d);
            } else {
                return "0" + df2.format(s / 100d);
            }
        } else {
            return "0";
        }
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

        return true;
    }


    //手机拍照回调
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO:
                    picBitmap = BitmapFactory.decodeFile(m_pictureName.getAbsolutePath());
                    picBitmap = CameraUtils.comp(picBitmap);
                    if (picBitmap != null) {
                        //拍摄返回的图片name
                        pictureName = m_pictureName.getName();
                        picNames.add(pictureName);
                        picFiles.add(m_pictureName);
                        HashMap<String, Object> _map = new HashMap<>();
                        _map.put("itemImage", picBitmap);
                        _map.put("remark", pictureName);
                        imageItem.add(_map);
                        refreshGridviewAdapter();

                    } else {
                        ToastyUtil.showInfoShort(getActivity(), "图片名不允许带特殊符号");
                    }

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            ToastyUtil.showErrorShort(getActivity(), "new safety log error e:=" + e.getMessage());
        }
    }


    /**
     * 刷新图片区域gridview
     */
    private void refreshGridviewAdapter() {
        if (simpleAdapter == null) {
            simpleAdapter = new SimpleAdapter(getActivity(), imageItem,
                    R.layout.layout_griditem_addpic, new String[]{"itemImage", "remark"}, new int[]{R.id.imageView1, R.id.tvPicName});
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
                        return true;
                    }
                    return false;
                }
            });
            //主线程绑定adapter刷新数据
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
     * 初始化拍照区域
     */
    private void initTakePicArea() {
        picFiles = new ArrayList<>();
        picNames = new ArrayList<>();
        imageItem = new ArrayList<HashMap<String, Object>>();
    }


    /**
     * 查看图片
     */
    private void viewPicture(int position) {
        if (picFiles.get(position) != null) {
            //打开照片查看
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(picFiles.get(position)), CameraUtils.IMAGE_UNSPECIFIED);
            startActivity(intent);
        }
    }

    private TextWatcher m_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            String _temp = edtElevation.getText().toString();
            if (_temp.length() == 0) {
                _temp = "0";
            }
            double str = Double.parseDouble(_temp);
            edtElevation.setText(String.format("%.3f", str));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        m_watcher = null;
        EventBus.getDefault().unregister(this);
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
            ToastyUtil.showInfoShort(getActivity(), "error " + e.getMessage());
        }
        return "";
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
                }
            } else {
                tvWellSize.setText("窨井规格");
                tvWellLidTexture.setText("窨井材质");
                if (layout_well_depth.getVisibility() != View.VISIBLE) {
                    layout_well_depth.setVisibility(View.VISIBLE);
                    layout_well_water.setVisibility(View.VISIBLE);
                    layout_well_mud.setVisibility(View.VISIBLE);
                    layout_well_lid_size.setVisibility(View.VISIBLE);
                }
            }
        } else {
            linearAppendantPanel.setVisibility(View.GONE);
        }
        animSwitch = true;
    }

}
