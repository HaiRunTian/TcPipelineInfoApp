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
import com.app.BaseInfo.Data.MAPACTIONTYPE2;
import com.app.BaseInfo.Data.PointFieldFactory;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.BaseInfo.Oper.OperSql;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.AlertDialogUtil;
import com.app.pipelinesurvey.utils.CameraUtils;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.MaxExpNumID;
import com.app.pipelinesurvey.utils.MyAlertDialog;
import com.app.pipelinesurvey.utils.PipeThemelabelUtil;
import com.app.pipelinesurvey.utils.SymbolInfo;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.iview.IDrawPipePointView;
import com.app.pipelinesurvey.view.iview.IQueryPipePointView;
import com.app.pipelinesurvey.view.widget.AppendanSpinner;
import com.app.pipelinesurvey.view.widget.FeaturePointsSpinner;
import com.app.utills.LogUtills;
import com.supermap.data.Recordset;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 管点定位
 *
 * @author
 */
public class QueryPointFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, IQueryPipePointView, IDrawPipePointView, AdapterView.OnItemClickListener {
    private static final String TAG = "QueryPointFragment";
    private TextView tvTitle;
    private TextView tvSubmit;
    private Button btnPreviousOne;
    private EditText edtGpId;
    private Spinner spSituation;
    private AppendanSpinner spAppendant;
    private FeaturePointsSpinner spFeaturePoints;
    private Spinner spState;
    private Spinner spWellLidTexture;
    private EditText edtWellSize;
    private EditText edtWellDepth;
    private EditText edtWellWater;
    private EditText edtWellMud;
    private EditText edtWellLidSize;
    private EditText edtElevation;
    private EditText edtOffset;
    private EditText edtBuildingStructures;
    private EditText edtRoadName;
    private EditText edtPointRemark;
    private EditText edtPuzzle;
    private ImageView imgvPointRemark;
    private LinearLayout linearAppendantPanel;
    private LinearLayout linearReturn;
    private LinearLayout layoutLineMark;
    private Button btnChangeStyle;
    private Animation m_animation;
    private View tv;
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
    private ListPopupWindow _popupWindow;
    private BaseFieldPInfos m_basePInfo;
    private Button btnDelPoint;
    private Button btnMove;
    private Spinner spType;
    /**
     * 开启相机
     */
    private Button btnOpenCamera;
    private int[] m_smid;
    private Bundle m_bundle;
    private Button btnSave;
    private boolean previousOne = false;
    /**
     * 物探点号，如果用户修改了点号类型，用于判断
     */
    private String firstGpId = "";
    private List<String> m_listPicName;
    /**
     * 适配器数据
     */
    private ArrayList<HashMap<String, Object>> imageItem;
    /**
     * 适配器
     */
    private SimpleAdapter simpleAdapter;
    private GridView m_gridView;
    private Button m_btnAddPic;
    /**
     * 临时图片文件名数组
     */
    private List<String> picNames;
    private EditText edtDepth;
    /**
     * 最终生成的img文件
     */
    private File resultImgFile;
    /**
     * 照片全名xx.jpg
     */
    private String pictureName = "";
    /**
     * 临时图片文件数组
     */
    private List<File> picFiles;
    /**
     * 照片名字自增
     */
    private int m_picIndex = 0;
    private String m_gpType;
    private String m_code;
    private String[] m_num = new String[]{};
    private View m_view;
    File m_pictureName;
    /**
     * 原始点号
     */
    private String initPointExp = "";
    private int _smId;
    /**
     * 第一次进来
     */
    private boolean firstInit = true;
    private boolean firstInit2 = true;
    private EditText edtX;
    private EditText edtY;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.activity_query_pipe_point, container, false);
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
        cursor.close();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValue();
        EventBus.getDefault().register(this);
    }


    /**
     * 初始化值
     *
     * @Params :
     * @author :HaiRun
     * @date :2020/5/28  9:29
     */
    private void initValue() {
        m_bundle = getArguments();
        m_basePInfo = m_bundle.getParcelable("info");
        LogUtills.i("queryActivity " + m_basePInfo.toString());
        int _smId = m_bundle.getInt("smId", 0);
        m_smid = new int[]{_smId};
        //初始管类代码
        m_code = m_basePInfo.pipeType.substring(3);
        //如果是修改管点信息，则需要获取选中观点的类型来更改属性。考虑下拉框给用户选择管类，则必须考虑管类不同而导致的，附属物、管点类型的不同而做的变化
//        int _typeIndex = m_basePInfo.pipeType.indexOf("_") + 1;
        m_gpType = m_basePInfo.pipeType.replace("_", "-");
        LogUtills.i("Query Point Info Type: " + m_basePInfo.pipeType + ", Change Type:" + m_gpType);
        tvTitle.setText("编辑点" + "(" + m_gpType + ")");

        //特征点
        featurePointsList = SpinnerDropdownListManager.getData("feature", m_gpType);
        spFeaturePoints.setMemoryCount(5);
        spFeaturePoints.setData(null, (ArrayList<String>) featurePointsList);
        //附属物
        appendantList = SpinnerDropdownListManager.getData("subsid", m_gpType);
        spAppendant.setMemoryCount(5);
        spAppendant.setData(null, (ArrayList<String>) appendantList);
        //管点备注
        pointRemarkList = SpinnerDropdownListManager.getData("pointRemark", m_gpType);
        //物探点号状态
        situationList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.situation));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, situationList);
        spSituation.setAdapter(m_adapter);
        //修改管类
        situationList = SpinnerDropdownListManager.getData("type", "");
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, situationList);
        spType.setAdapter(m_adapter);
        SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spType, m_code);
        //管点状态
        stateList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.state));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, stateList);
        spState.setAdapter(m_adapter);
        //井盖材质
        wellLidTextureList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.wellLidTexture));
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, wellLidTextureList);
        spWellLidTexture.setAdapter(m_adapter);
        setValueToView();

    }

    /**
     * 初始化view
     *
     * @Params :
     * @author :HaiRun
     * @date :2020/5/28  8:56
     */
    private void setValueToView() {
        try {
            setGPId();
            setSituation();
            setFeaturePoints();
            setAppendant();
            setState();
            setElevation();
            setOffset();
            setBuildingStructures();
            setRoadName();
            setPointRemark();
            setPuzzle();
            setWellSize();
            setWellDepth();
            setWellMud();
            setWellWater();
            setWellLidSize();
            setX();
            setY();
            m_listPicName = getPicturefromReSet();
            //从本地读取照片，放到gridView中
            if (m_listPicName.size() > 0) {
                m_picIndex = m_listPicName.size();

                for (String _s : m_listPicName) {
                    boolean isExsit = FileUtils.getInstance().isFileExsit(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH + "/" + _s);
                    if (!isExsit) {
                        ToastyUtil.showInfoShort(getActivity(), "此图片不存在 = " + _s);
                        continue;
                    }
                    picNames.add(_s);
                    picFiles.add(new File(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH + "/" + _s));
                    Bitmap _bitmap = CameraUtils.getimage(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH + "/" + _s);
                    HashMap<String, Object> _map = new HashMap<>();
                    _map.put("itemImage", _bitmap);
                    _map.put("picName", _s);
                    imageItem.add(_map);
                    refreshGridviewAdapter();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvSubmit = view.findViewById(R.id.tvSubmit);
        btnPreviousOne = view.findViewById(R.id.btnPreviousOne);
        edtGpId = view.findViewById(R.id.edtGpId);
        spSituation = view.findViewById(R.id.spSituation);
        spType = view.findViewById(R.id.SpType);
        spAppendant = view.findViewById(R.id.spAppendant);
        spFeaturePoints = view.findViewById(R.id.spFeaturePoints);
        spState = view.findViewById(R.id.spState);
        spWellLidTexture = view.findViewById(R.id.spWellLidTexture);
        edtWellSize = view.findViewById(R.id.edtWellSize);
        edtWellDepth = view.findViewById(R.id.edtWellDepth);
        edtWellWater = view.findViewById(R.id.edtWellWater);
        edtWellMud = view.findViewById(R.id.edtWellMud);
        edtWellLidSize = view.findViewById(R.id.edtWellLidSize);
        edtElevation = view.findViewById(R.id.edtElevation);
        edtOffset = view.findViewById(R.id.edtOffset);
        edtBuildingStructures = view.findViewById(R.id.edtBuildingStructures);
        edtRoadName = view.findViewById(R.id.edtRoadName);
        edtPointRemark = view.findViewById(R.id.edtPointRemark);
        edtPuzzle = view.findViewById(R.id.edtPuzzle);
        imgvPointRemark = view.findViewById(R.id.imgvPointRemark);
        linearAppendantPanel = view.findViewById(R.id.linearAppendantPanel);
        linearReturn = view.findViewById(R.id.linearReturn);
        layoutLineMark = view.findViewById(R.id.layoutPointMark);
        btnDelPoint = view.findViewById(R.id.btnRemove);
        btnMove = view.findViewById(R.id.btnMove);
        m_gridView = view.findViewById(R.id.gridView1);
        m_btnAddPic = view.findViewById(R.id.btnAddPic);
        btnSave = view.findViewById(R.id.btnSave);
        edtDepth = view.findViewById(R.id.edtDepth);
        btnChangeStyle = view.findViewById(R.id.btnChangeStyle);
        btnOpenCamera = view.findViewById(R.id.btnAddPic);
        tv = view.findViewById(R.id.layout);
        edtX = view.findViewById(R.id.edtX);
        edtY = view.findViewById(R.id.edtY);
        cbDc = view.findViewById(R.id.cb_dc);
        initShowPicArea();
        btnOpenCamera.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        linearReturn.setOnClickListener(this);
        btnPreviousOne.setOnClickListener(this);
        imgvPointRemark.setOnClickListener(this);
        spAppendant.setOnItemSelectedListener(this);
        btnDelPoint.setOnClickListener(this);
        btnMove.setOnClickListener(this);
        m_btnAddPic.setOnClickListener(this);
        btnPreviousOne.setOnClickListener(this);
        btnChangeStyle.setOnClickListener(this);
        spSituation.setOnItemSelectedListener(this);
        spType.setOnItemSelectedListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.linearReturn:
                getDialog().dismiss();
                break;

            //提交 保存 如果修改了管类，则要删除原先点，
            case R.id.tvSubmit:
            case R.id.btnSave:
                boolean _result = false;
                //编辑保存数据数据
                if (!initPointExp.equals(getGPId())) {
                    //判断重号
                    if (ComTool.Ins().isSameNum(getGPId(), false)) {
                        ToastyUtil.showErrorShort(getActivity(), "点号重复，请重新编号");
                        return;
                    }
                }
                _result = DataHandlerObserver.ins().editRecords(generateBaseFieldInfo());
                if (!_result) {
                    ToastyUtil.showErrorShort(getActivity(), "保存点数据失败");
                    return;
                } else {
                    //如果管线点号修改了
                    if (!initPointExp.equals(getGPId())) {
                        int id = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
                        //如果修改的点号大于当前的点号，则修改
                        if (id > MaxExpNumID.getInstance().getId()) {
                            MaxExpNumID.getInstance().setId(id);
                        }
                        //更新与此点相关的线
                        updaLine();
                    }
                    WorkSpaceUtils.getInstance().saveMap();
                    getDialog().dismiss();
                }
                WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
                break;

            //删除点 删除线
            case R.id.btnRemove:
                AlertDialogUtil.showDialog(getActivity(), "警告提示！", "是否确定删除管点" + getGPId() + "？若是删除，和此点相连的管线也会删除，请确认。", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _smId = m_bundle.getInt("smId", 0);
                        //点
                        Recordset _reSet = DataHandlerObserver.ins().queryRecordsetBySmid(_smId, true, true);
                        if (_reSet.isEmpty()) {
                            LogUtills.i("Delete Point Faild, ID=" + _smId);
                            return;
                        }
                        _reSet.edit();
                        if (_reSet.delete()) {
                            _reSet.update();
//                            ToastyUtil.showSuccessShort(getActivity(), "删除点成功");
                            //线 此点作为终点
                            Recordset _resetL = DataHandlerObserver.ins().QueryRecordsetBySql("endExpNum = '" + getGPId() + "'", false, true);
                            if (!_resetL.isEmpty()) {
                                //外检模式需要添加更改记录表
                                if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                                    while (!_resetL.isEOF()) {
                                        String benExpNum = _resetL.getString("benExpNum");
                                        int sysId = _resetL.getInt32("sysId");
                                        OperSql.getSingleton().inserLine(benExpNum, getGPId(), sysId, "外检:删除管线");
                                        _resetL.moveNext();
                                    }
                                }
                                _resetL.deleteAll();
                                _resetL.update();
                            }
                            // 线 此点作为起点
                            _resetL = DataHandlerObserver.ins().QueryRecordsetBySql("benExpNum = '" + getGPId() + "'", false, true);
                            if (!_resetL.isEmpty()) {
                                //外检模式需要添加更改记录表
                                if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                                    while (!_resetL.isEOF()) {
                                        String endExpNum = _resetL.getString("endExpNum");
                                        int sysId = _resetL.getInt32("sysId");
                                        OperSql.getSingleton().inserLine(getGPId(), endExpNum, sysId, "外检:删除管线");
                                        _resetL.moveNext();
                                    }
                                }
                                _resetL.deleteAll();
                                _resetL.update();
                            }
                            _resetL.close();
                            _resetL.dispose();

                            //外检模式保存删除点线数据到对应到两张表
                            if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                                OperSql.getSingleton().inserPoint(initPointExp,m_basePInfo.longitude,m_basePInfo.latitude, "外检:删除管点");
                            }
                            WorkSpaceUtils.getInstance().saveMap();
                            getDialog().dismiss();
                        } else {
                            ToastyUtil.showErrorShort(getActivity(), "删除点失败");
                        }

                        _reSet.close();
                        _reSet.dispose();
                        int serialNum = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
                        int id = MaxExpNumID.getInstance().getId();
                        //删除最大号
                        if (serialNum == id) {
                            MaxExpNumID.getInstance().setId(id - 1);
                        }
                        WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
                    }
                });
                break;
            //导入上一个
            case R.id.btnPreviousOne:
                previousOne = true;
                setValueToView();
                break;

            //临时点转实点
            case R.id.btnChangeStyle:
                if (getGPId().contains("T_")) {
                    m_num = ComTool.Ins().getPointNumber(m_gpType.substring(m_gpType.indexOf("-") + 1), false, "");
                    String _endExpNum = getGPId();
                    edtGpId.setText(m_num[0]);
                    MaxExpNumID.getInstance().setId(Integer.parseInt(m_num[1]));
                    //更新线
                    Recordset _recordset = DataHandlerObserver.ins().QueryRecordsetBySql("endExpNum = '" + _endExpNum + "'", false, true);
                    if (!_recordset.isEmpty()) {
                        while (!_recordset.isEOF()) {
                            _recordset.edit();
                            _recordset.setString("endExpNum", m_num[0]);
                            _recordset.setString("pipeType", m_basePInfo.pipeType);
                            _recordset.setString("labelTag", _recordset.getString("labelTag").replace("临时-o", m_basePInfo.pipeType.toLowerCase()));
                            _recordset.setDouble("rangeExpression", PipeThemelabelUtil.Ins().getThemeItemValue(m_basePInfo.pipeType.substring(0, 2)));
                            _recordset.update();
                            _recordset.moveNext();
                        }
                    }
                    _recordset.close();
                    _recordset.dispose();
                }
                break;

            //移动管点
            case R.id.btnMove:
                //先保存数据  再移动
                boolean b = DataHandlerObserver.ins().editRecords(generateBaseFieldInfo());
                if (!b) {
                    ToastyUtil.showErrorLong(getActivity(), "点号移动失败");
                    return;
                }
                //如果点号修改了，就更新线的起点与终点
                if (!initPointExp.equals(getGPId())) {
                    updaLine();
                }
                //应先保存管点信息；弹出对话框，是否保存属性信息（坐标信息除外）
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_EditPointLocation);
                _smId = m_bundle.getInt("smId", 0);
                DataHandlerObserver.ins().addPointSmID(_smId);
                WorkSpaceUtils.getInstance().saveMap();
                getDialog().dismiss();
                break;
            case R.id.btnAddPic:
                openCamera();
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

            default:
                break;
        }
        WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
    }

    /**
     * 更新线的起点和终点
     */
    private void updaLine() {
        //更新起点
        Recordset reSetbenExpNum = DataHandlerObserver.ins().QueryRecordsetBySql("benExpNum = '" + initPointExp + "'", false, true);
        if (!reSetbenExpNum.isEmpty()) {
            reSetbenExpNum.moveFirst();
            while (!reSetbenExpNum.isEOF()) {
                reSetbenExpNum.edit();
                reSetbenExpNum.setString("Edit", "外检:起点修改");
                reSetbenExpNum.setString("benExpNum", getGPId());
                reSetbenExpNum.setString("exp_Date", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));
                if (!reSetbenExpNum.update()) {
                    ToastyUtil.showWarningLong(getActivity(), "线起点点更新失败");
                }
                //外检模式需要添加更改记录表
                if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                    String endExpNum = reSetbenExpNum.getString("endExpNum");
                    int sysId = reSetbenExpNum.getInt32("sysId");
                    OperSql.getSingleton().inserLine(initPointExp, endExpNum, sysId, "外检:起点点号修改" + initPointExp + "->" + getGPId());

                    String expNum = reSetbenExpNum.getString("endExpNum");
                    OperSql.getSingleton().inserPoint(expNum,m_basePInfo.longitude,m_basePInfo.latitude,"外检:关联线起点点号修改");
                    Recordset recordset = DataHandlerObserver.ins().queryRecordsetByExpNum(expNum, true);
                    if (!recordset.isEmpty()) {
                        recordset.edit();
                        recordset.setString("Edit", "外检:关联线起点点号修改");
                        recordset.setString("exp_Date", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));
                        recordset.update();
                    }
                    if (recordset != null) {
                        recordset.close();
                        recordset.dispose();
                    }
                }

                reSetbenExpNum.moveNext();
            }
        }

        if (reSetbenExpNum != null) {
            reSetbenExpNum.close();
            reSetbenExpNum.dispose();
        }


        //更新终点
        Recordset reSetendExpNum = DataHandlerObserver.ins().QueryRecordsetBySql("endExpNum = '" + initPointExp + "'", false, true);
        if (!reSetendExpNum.isEmpty()) {
            reSetendExpNum.moveFirst();
            while (!reSetendExpNum.isEOF()) {
                reSetendExpNum.edit();
                reSetendExpNum.setString("endExpNum", getGPId());
                reSetendExpNum.setString("Edit", "外检:终点修改");
                reSetendExpNum.setString("exp_Date", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));
                if (!reSetendExpNum.update()) {
                    ToastyUtil.showWarningLong(getActivity(), "线终点更新失败");
                }

                //外检模式需要添加更改记录表
                if (SuperMapConfig.OUTCHECK.equals(SuperMapConfig.PrjMode)) {
                    String benExpNum = reSetendExpNum.getString("benExpNum");
                    int sysId = reSetendExpNum.getInt32("sysId");
                    OperSql.getSingleton().inserLine(benExpNum, initPointExp, sysId, "外检:终点点号修改" + initPointExp + "->" + getGPId());

                    String expNum = reSetendExpNum.getString("benExpNum");
                    OperSql.getSingleton().inserPoint(expNum,m_basePInfo.longitude,m_basePInfo.latitude,"外检:关联线起点点号修改");
                    Recordset recordset = DataHandlerObserver.ins().queryRecordsetByExpNum(expNum, true);
                    if (!recordset.isEmpty()) {
                        recordset.edit();
                        recordset.setString("Edit", "外检:关联线起点点号修改");
                        recordset.setString("exp_Date", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT));
                        recordset.update();
                    }

                    if (recordset != null) {
                        recordset.close();
                        recordset.dispose();
                    }
                }
                reSetendExpNum.moveNext();
            }
        }

        if (reSetendExpNum != null) {
            reSetendExpNum.close();
            reSetendExpNum.dispose();
        }
    }


    private BaseFieldPInfos generateBaseFieldInfo() {
        BaseFieldPInfos _info = null;
        try {
            String gpType = m_bundle.getString("gpType", "");
            //管类更改地方
            String _layerType = gpType.trim().substring(3);

            _info = PointFieldFactory.CreateInfo(gpType.substring(0, 2));
            if (_info == null) {
                LogUtills.e(" Create BaseFieldPInfos Fail...");
                return null;
            }
            //判断管类是否修改了
            int codeLength = m_code.length();
            //需要判断组号的位置
            String code = getPipeCode();

            //判断管类修改了
            if (!m_code.equals(code)) {
                //修改点
                _info.code = code;
                _info.shortCode = code;
                //查询数据库表格 找出的对应的管类
                Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_INFO, "where city ='"
                        + SuperMapConfig.PROJECT_CITY_NAME + "' and code = '" + code + "'");
                String type = m_basePInfo.pipeType;
                while (query.moveToNext()) {
                    type = query.getString(query.getColumnIndex("name"));
                }
                query.close();
                _info.pipeType = type + "-" + code;
                _info.rangeExpression = PipeThemelabelUtil.Ins().getThemeItemValue(type);
                _info.symbol = SymbolInfo.Ins().getSymbol(_info.pipeType, getAppendant(), getFeaturePoints());
                _info.symbolExpression = code + "-" + _info.symbol;
                //修改线的管类
                Recordset recordset = DataHandlerObserver.ins().QueryRecordsetBySql("benExpNum = '" + initPointExp + "'", false, true);
                while (!recordset.isEOF()) {
                    recordset.edit();
                    recordset.setString("pipeType", _info.pipeType);
                    recordset.setString("shortCode", _info.code);
                    recordset.setString("code", _info.code);
                    recordset.setDouble("rangeExpression", _info.rangeExpression);
                    recordset.update();
                    recordset.moveNext();
                }
            } else {
                _info.code = m_code;
                _info.shortCode = m_code;
                _info.pipeType = m_basePInfo.pipeType;
                _info.symbol = SymbolInfo.Ins().getSymbol(gpType, getAppendant(), getFeaturePoints());
                _info.symbolExpression = code + "-" + _info.symbol;
            }
            _info.exp_Num = getGPId();
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
                if (initPointExp.equals(getGPId())) {
                    OperSql.getSingleton().inserPoint(initPointExp,m_basePInfo.longitude,m_basePInfo.latitude, "外检:数据修改");
                } else {
                    //管线记录插入到点
                    OperSql.getSingleton().inserPoint(initPointExp, m_basePInfo.longitude,m_basePInfo.latitude,"外检:点号修改:" + initPointExp + ":" + getGPId());
                }
                //点标记修改
                _info.Edit = "外检:修改管点:" + getState();
                //线也需要标记修改
            }
            _info.state = getState();
            _info.exp_Date = DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT);
            _info.wellCoverMaterial = getWellLidTexture();
            _info.wellCoverSize = getWellLidSize();
            _info.buildingStructures = getBuildingStructures();
            _info.longitude = getLongitude();
            _info.latitude = getLatitude();
            _info.surf_H = getElevation();
            _info.expGroup = m_basePInfo.expGroup;
            _info.remark = getPointRemark();
            //照片名字
            _info.picture = getPictureName();
            _info.id = getGPId();
            _info.puzzle = getPuzzle();
            _info.situation = getSituation();
            _info.sysId = m_smid[0];
            _info.depth = m_basePInfo.depth;
            //是否是临时点转实点
            if (m_num.length == 2) {
                //判断用户是否修改了管点编号
                if (!m_num[0].equals(getGPId())) {
                    _info.serialNum = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
                } else {
                    _info.serialNum = Integer.parseInt(m_num[1]);
                }
            } else {
                //正常点号修改
                //判断用户是否修改了点流水号
                if (!initPointExp.equals(getGPId())) {
                    _info.serialNum = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
                } else {
                    _info.serialNum = m_basePInfo.serialNum;
                }
            }

            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_DEFAULT_POINT_SETTING,
                    new String[]{"symbolID", "scaleX", "scaleY"}, "name=?", new String[]{_info.symbolExpression.trim().toString()}, null, null, null);
            LogUtills.i("Sql:" + _cursor.getCount());
            if (_cursor.moveToNext()) {
                _info.symbolID = _cursor.getInt(_cursor.getColumnIndex("symbolID"));
                _info.symbolSizeX = _cursor.getDouble(_cursor.getColumnIndex("scaleX"));
                _info.symbolSizeY = _cursor.getDouble(_cursor.getColumnIndex("scaleY"));
            }
            _cursor.close();
            _info.ExpX = m_basePInfo.ExpX;
            _info.ExpY = m_basePInfo.ExpY;
            _info.MapX = m_basePInfo.MapX;
            _info.MapY = m_basePInfo.MapY;
            _info.Explain1 = m_basePInfo.Explain1;
            _info.PsCheQiX = m_basePInfo.PsCheQiX;
            _info.PsCheQiY = m_basePInfo.PsCheQiY;
            _info.PsCheQiLeftR = m_basePInfo.PsCheQiLeftR;
            _info.ExpCheQiY = m_basePInfo.ExpCheQiY;
            _info.ExpCheQiX = m_basePInfo.ExpCheQiX;
            _info.ExplainX = m_basePInfo.ExplainX;
            _info.ExplainY = m_basePInfo.ExplainY;
            _info.ExplaninAng = m_basePInfo.ExplaninAng;
            _info.PpdAng = m_basePInfo.PpdAng;
            _info.PpdX = m_basePInfo.PpdX;
            _info.PpdY = m_basePInfo.PpdY;
            _info.DotleadAng = m_basePInfo.DotleadAng;
            _info.MeasuerPoint = m_basePInfo.MeasuerPoint;
            LogUtills.i(_info.toString());
        } catch (Exception e) {
            LogUtills.e(e.getMessage());
        }
        return _info;
    }

    /**
     * @auther HaiRun
     * created at 2018/7/24 10:29
     */
    private void openCamera() {
        try {
            //照片名字
            m_picIndex++;
            m_pictureName = new File(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/Picture", edtGpId.getText().toString() + "_" + m_picIndex + ".jpg");
            m_pictureName.createNewFile();
            Uri fileUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), "com.app.pipelinesurvey", m_pictureName);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setViewEdit(boolean state) {
        spAppendant.setEnabled(state);
        spFeaturePoints.setEnabled(state);
        spSituation.setEnabled(state);
        spState.setEnabled(state);
        spWellLidTexture.setEnabled(state);
        edtGpId.setEnabled(state);
        edtWellLidSize.setEnabled(state);
        edtWellDepth.setEnabled(state);
        edtWellWater.setEnabled(state);
        edtWellMud.setEnabled(state);
        edtWellLidSize.setEnabled(state);
        edtElevation.setEnabled(state);
        edtOffset.setEnabled(state);
        edtBuildingStructures.setEnabled(state);
        edtRoadName.setEnabled(state);
        edtPointRemark.setEnabled(state);
        edtPuzzle.setEnabled(state);

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
                if (firstInit) {
                    firstInit = false;
                } else {
                    updateIdAndPointRemark();
                }
                break;
            case R.id.SpType:
                if (firstInit2) {
                    firstInit2 = false;
                } else {
                    //判断管类是否修改了
                    if (!getGPId().contains("T_")) {
                        String code = getPipeCode();
                        // TODO 组号长度位置 需要替换的位置，如果组号和管类代码一样，就会出现修改管类有问题
                        StringBuffer buffer = new StringBuffer(getGPId());
                        //知道管类代码位置 长度
//                    String expNum = getGPId().replace(code, spType.getSelectedItem().toString());
                        int start = ComTool.Ins().getPipeCodeLocal(getGPId());
                        int end = start + code.length();
                        buffer.replace(start, end, spType.getSelectedItem().toString());
                        edtGpId.setText(buffer.toString());
                    }
                }
                break;
            default:
                break;
        }
    }

    private String getPipeCode() {
        //管类代码长度
        int codeLength = m_code.length();
        //管类代码的位置以及长度
        //组号
        String _groupName = "";
        //组号位置
        int _groupLocal = 1;
        //查询数据库point_setting表，配置信息
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + SuperMapConfig.PROJECT_NAME + "'");
        while (_cursor.moveToNext()) {
            //获取组号
            _groupName = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
            //组号位置
            _groupLocal = _cursor.getInt(_cursor.getColumnIndex("GroupLocal"));

        }
        _cursor.close();
        //组号长度
        int gpoupNameLength = _groupName.length();
        String code = "";
        switch (_groupLocal) {
            //管类代码在首位 组号在中间
            case 1:
                // 管类代码在首位  流水号在中间 组号在末尾
            case 3:
                code = getGPId().substring(0, codeLength);
                break;
            //组号在首位 管类代码在中间
            case 2:
                code = getGPId().substring(gpoupNameLength, gpoupNameLength + codeLength);
                break;
            default:
                code = getGPId().substring(0, codeLength);
                break;
        }
        return code;

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

    /**
     * 设置物探点号
     */
    @Override
    public void setGPId() {
        String _gpId = m_basePInfo.exp_Num;
        initPointExp = _gpId;
        if (_gpId.length() != 0) {
            edtGpId.setText(_gpId);
        }
        if (_gpId.contains("T_")) {
            tv.setVisibility(View.VISIBLE);
        }
        firstGpId = _gpId;
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
            //保留三位小数
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
        return edtDepth.getText().toString();

    }

    /**
     * 设置物探点号状况
     */
    @Override
    public void setSituation() {
        String situation = m_basePInfo.situation;
        if (situation.length() != 0) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spSituation, situation);
        }
    }

    /**
     * 设置特征点
     */
    @Override
    public void setFeaturePoints() {
        String _featurePoint = m_basePInfo.feature;
        if (_featurePoint.length() != 0) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spFeaturePoints, _featurePoint);
        }
    }

    /**
     * 设置附属物
     */
    @Override
    public void setAppendant() {
        String _appendant = m_basePInfo.subsid;
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

    /**
     * 设置管点状态
     */
    @Override
    public void setState() {
        String _statue = m_basePInfo.state;
        if (_statue.length() != 0) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spState, _statue);
        }
    }

    /**
     * 设置高程
     */
    @Override
    public void setElevation() {
        String _evevation = m_basePInfo.surf_H;
        if (_evevation != null) {
            edtElevation.setText(_evevation + "");
        }

    }

    /**
     * 设置管偏
     */
    @Override
    public void setOffset() {
        String _offset_local = m_basePInfo.pipeOffset;
        if (_offset_local.length() != 0) {
            edtOffset.setText(_offset_local);
        }
    }

    /**
     * 设置建构筑物
     */
    @Override
    public void setBuildingStructures() {
        String _buildingStructures = m_basePInfo.buildingStructures;
        if (_buildingStructures.length() != 0) {
            edtBuildingStructures.setText(_buildingStructures);
        }
    }

    /**
     * 设置路名
     */
    @Override
    public void setRoadName() {
        String _roadName = m_basePInfo.road;
        if (_roadName.length() != 0) {
            edtRoadName.setText(_roadName);
        }
    }

    /**
     * 设置点备注
     */
    @Override
    public void setPointRemark() {
        String _pointRemark = m_basePInfo.remark;
        LogUtills.i(TAG, "pointRemark =" + _pointRemark);
        if (_pointRemark.length() != 0) {
            edtPointRemark.setText(_pointRemark);
        }

        if (_pointRemark.contains("直流地面")) {
            cbDc.setChecked(true);
        }
    }

    /**
     * 设置疑难问题
     */
    @Override
    public void setPuzzle() {
        String _puzzle = m_basePInfo.puzzle;
        if (_puzzle.length() != 0) {
            edtPuzzle.setText(_puzzle);
        }
    }

    /**
     * 设置管径
     */
    @Override
    public void setWellSize() {
        String _wellSize = m_basePInfo.wellSize;
        if (_wellSize.length() != 0) {
            edtWellSize.setText(_wellSize);
        }
    }

    /**
     * 设置井深
     */
    @Override
    public void setWellDepth() {
        String _wellDepth = m_basePInfo.wellDeep;
        if (!_wellDepth.isEmpty()) {
            double s = Double.parseDouble(_wellDepth);
            double temp = s * 100;
            edtWellDepth.setText(new DecimalFormat().format(temp).replace(",", ""));
        }
    }

    /**
     * 设置水深
     */
    @Override
    public void setWellWater() {
        String _wellWater = m_basePInfo.wellWater;
        if (!_wellWater.isEmpty()) {
            double s = Double.parseDouble(_wellWater);
            double temp = s * 100;
            edtWellWater.setText(new DecimalFormat().format(temp).replace(",", ""));
        }
    }

    /**
     * 设置淤泥
     */
    @Override
    public void setWellMud() {
        String _wellMud = m_basePInfo.wellMud;
        if (!_wellMud.isEmpty()) {
            double s = Double.parseDouble(_wellMud);
            double temp = s * 100;
            edtWellMud.setText(new DecimalFormat().format(temp).replace(",", ""));
        }
    }

    /**
     * 设置X坐标系
     */
    public void setX() {
        edtX.setText(String.valueOf(m_basePInfo.longitude));
    }

    /**
     * 设置Y坐标
     */
    public void setY() {
        edtY.setText(String.valueOf(m_basePInfo.latitude));
    }

    /**
     * 设置井盖材质
     */
    @Override
    public void setWellLidTexture() {
        String _wellLidTexture = m_basePInfo.wellCoverMaterial;
        if (_wellLidTexture.length() != 0) {
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spWellLidTexture, _wellLidTexture);
        }
    }

    /**
     * 设置井盖规格
     */
    @Override
    public void setWellLidSize() {
        String _wellLIdSize = m_basePInfo.wellCoverSize;
        if (_wellLIdSize.length() != 0) {
            edtWellLidSize.setText(_wellLIdSize);
        }
    }

    @Override
    public double getLongitude() {
        return m_basePInfo.longitude;
    }

    @Override
    public double getLatitude() {
        return m_basePInfo.latitude;
    }

    @Override
    public List<String> getPicturefromReSet() {
        List<String> _list = new ArrayList<>();
        String picName = m_basePInfo.picture;
        if (!picName.isEmpty()) {
            _list.addAll(Arrays.asList(picName.split("#")));
        }
        return _list;
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
                        getActivity()
                                .runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {//绑定视图
                                        ImageView i = (ImageView) view;
                                        i.setImageBitmap((Bitmap) data);
                                    }
                                });
                        return true;
                    } else if (view instanceof TextView) {
                        TextView _tv = (TextView) view;
                        _tv.setText(textRepresentation);
                        return true;
                    }

                    return false;
                }
            });

            getActivity().runOnUiThread(new Runnable() {
                //主线程绑定adapter刷新数据
                @Override
                public void run() {
                    m_gridView.setAdapter(simpleAdapter);
                    simpleAdapter.notifyDataSetChanged();
                }
            });
        } else {
            simpleAdapter.notifyDataSetChanged();
        }
    }

    private void initShowPicArea() {
        picNames = new ArrayList<>();
        imageItem = new ArrayList<>();
        picFiles = new ArrayList<>();
        m_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (m_listPicName.get(position) != null) {
                    //打开照片查看
                    if (picFiles.get(position) != null) {
                        File _file = new File(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH, m_listPicName.get(position));
                        //打开照片查看
//                        File file = picFiles.get(position);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri _uri;
                        if (Build.VERSION.SDK_INT >= 24) {
                            _uri = FileProvider.getUriForFile(getContext().getApplicationContext(), "com.app.pipelinesurvey", _file);
                        } else {
                            _uri = Uri.fromFile(_file);
                        }
                        intent.setDataAndType(_uri, CameraUtils.IMAGE_UNSPECIFIED);
                        startActivity(intent);
                    }
                }
            }
        });

        m_gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        });
    }

    //手机拍照回调
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap picBitmap = null;
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
                        _map.put("picName", pictureName);
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
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
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
