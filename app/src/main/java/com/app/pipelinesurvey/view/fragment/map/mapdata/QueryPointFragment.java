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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.AlertDialogUtil;
import com.app.pipelinesurvey.utils.CameraUtils;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.InitWindowSize;
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
import com.squareup.leakcanary.RefWatcher;
import com.supermap.data.Recordset;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
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
    private String initPointExp = "";
    private int _smId;
    /**
     * 第一次进来
     */
    private boolean firstInit = true;
    private EditText edtX;
    private EditText edtY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
     * @Params :
     * @author :HaiRun
     * @date   :2019/8/23  16:58
     */
    private void initlayoutView(View m_view) {
        View laayoutGpid = m_view.findViewById(R.id.layout_gpid);
        View layoutFeature = m_view.findViewById(R.id.layout_feature);
        View layoutAppendant = m_view.findViewById(R.id.layout_appendant);
        View layout_well_size = m_view.findViewById(R.id.layout_well_size);
        View layout_well_depth = m_view.findViewById(R.id.layout_well_depth);
        View layout_well_water = m_view.findViewById(R.id.layout_well_water);
        View layout_well_mud = m_view.findViewById(R.id.layout_well_mud);
        View layout_well_lid_texture = m_view.findViewById(R.id.layout_well_lid_texture);
        View layout_well_lid_size = m_view.findViewById(R.id.layout_well_lid_size);
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
        while (cursor.moveToNext()){
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
            layout_well_size.setVisibility(wellsize == 1?View.VISIBLE:View.GONE);
            layout_well_depth.setVisibility(welldepth == 1?View.VISIBLE:View.GONE);
            layout_well_water.setVisibility(wellwater == 1?View.VISIBLE:View.GONE);
            layout_well_mud.setVisibility(wellmud == 1?View.VISIBLE:View.GONE);
            layout_well_lid_texture.setVisibility(welllidtexture == 1?View.VISIBLE:View.GONE);
            layout_well_lid_size.setVisibility(welllidsize == 1?View.VISIBLE:View.GONE);
            layout_state.setVisibility(state == 1?View.VISIBLE:View.GONE);
            layout_elevation.setVisibility(elevation == 1?View.VISIBLE:View.GONE);
            layout_offset.setVisibility(offset == 1?View.VISIBLE:View.GONE);
            layout_building_structures.setVisibility(building == 1?View.VISIBLE:View.GONE);
            layout_road_name.setVisibility(roadname == 1?View.VISIBLE:View.GONE);
            layout_point_remark.setVisibility(pointremark == 1?View.VISIBLE:View.GONE);
            layout_puzzle.setVisibility(puzzle == 1?View.VISIBLE:View.GONE);
            layout_x.setVisibility(x == 1?View.VISIBLE:View.GONE);
            layout_y.setVisibility(y == 1?View.VISIBLE:View.GONE);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValue();
        EventBus.getDefault().register(this);
    }


    private void initValue() {
        m_bundle = getArguments();
        m_basePInfo = m_bundle.getParcelable("info");
        LogUtills.i("queryActivity " + m_basePInfo.toString());
        int _smId = m_bundle.getInt("smId", 0);
        m_smid = new int[]{_smId};
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

        TextView tvPointNum = view.findViewById(R.id.tvPointNum);
        setViewDrawable(tvPointNum);
        TextView tvAppendant = view.findViewById(R.id.tvAppendant);
        setViewDrawable(tvAppendant);
        TextView tvFeaturePoints = view.findViewById(R.id.tvFeaturePoints);
        setViewDrawable(tvFeaturePoints);
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
                _result = DataHandlerObserver.ins().editRecords(generateBaseFieldInfo());
                if (!_result) {
                    ToastyUtil.showErrorShort(getActivity(), "保存点数据失败");
                    return;
                } else {
                    if (!initPointExp.equals(getGPId())) {
                        updaLine();
                    }
                    getDialog().dismiss();
                }
                break;

            //删除点 删除线
            case R.id.btnRemove:
                AlertDialogUtil.showDialog(getActivity(), "警告提示！", "是否确定删除管点？", true, new DialogInterface.OnClickListener() {
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
                            ToastyUtil.showSuccessShort(getActivity(), "删除点成功");
                            getDialog().dismiss();

                            //线 此点作为终点
                            Recordset _resetL = DataHandlerObserver.ins().QueryRecordsetBySql("endExpNum = '" + getGPId() + "'", false, true);
                            if (!_resetL.isEmpty()) {
                                _resetL.deleteAll();
                                _resetL.update();
                            }

                            // 线 此点作为起点
                            _resetL = DataHandlerObserver.ins().QueryRecordsetBySql("benExpNum = '" + getGPId() + "'", false, true);
                            if (!_resetL.isEmpty()) {
                                _resetL.deleteAll();
                                _resetL.update();
                            }
                            _resetL.close();
                            _resetL.dispose();

                        } else {
                            ToastyUtil.showErrorShort(getActivity(), "删除点失败");
                        }

                        _reSet.close();
                        _reSet.dispose();
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
                DataHandlerObserver.ins().editRecords(generateBaseFieldInfo());
                //如果点号修改了，就更新线的起点与终点
                if (!initPointExp.equals(getGPId())) {
                    updaLine();
                }
                //应先保存管点信息；弹出对话框，是否保存属性信息（坐标信息除外）
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_EditPointLocation);
                _smId = m_bundle.getInt("smId", 0);
                DataHandlerObserver.ins().addPointSmID(_smId);
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

        Recordset reSetbenExpNum = DataHandlerObserver.ins().QueryRecordsetBySql("benExpNum = '" + initPointExp + "'", false, true);
        if (!reSetbenExpNum.isEmpty()) {
            reSetbenExpNum.moveFirst();
            while (!reSetbenExpNum.isEOF()) {
                reSetbenExpNum.edit();
                reSetbenExpNum.setString("benExpNum", getGPId());
                if (!reSetbenExpNum.update()) {
                    LogUtills.i("线起点更新失败");
                }
                reSetbenExpNum.moveNext();
            }
        }
        reSetbenExpNum.close();

        Recordset reSetendExpNum = DataHandlerObserver.ins().QueryRecordsetBySql("endExpNum = '" + initPointExp + "'", false, true);
        if (!reSetendExpNum.isEmpty()) {
            reSetendExpNum.moveFirst();
            while (!reSetendExpNum.isEOF()) {
                reSetendExpNum.edit();
                reSetendExpNum.setString("endExpNum", getGPId());
                if (!reSetendExpNum.update()) {
                    LogUtills.i("线终点更新失败");
                }
                reSetendExpNum.moveNext();
            }
        }
        reSetendExpNum.close();
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
            _info.shortCode = m_code;
            _info.exp_Num = getGPId();
            _info.pipeType = m_basePInfo.pipeType;
            _info.feature = getFeaturePoints();
            _info.subsid = getAppendant();
            _info.pipeOffset = getOffset();
            _info.wellSize = getWellSize();
            _info.wellDeep = getWellDepth();
            _info.wellWater = getWellWater();
            _info.wellMud = getWellMud();
            _info.road = getRoadName();
            _info.state = getState();
            _info.exp_Date = m_basePInfo.exp_Date;
            _info.wellCoverMaterial = getWellLidTexture();
            _info.wellCoverSize = getWellLidSize();
            _info.buildingStructures = getBuildingStructures();
            _info.code = m_code;
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
            _info.symbol = SymbolInfo.Ins().getSymbol(gpType, getAppendant(), getFeaturePoints());
            //如果管类代码有两位，截取第一位
            //管类代码 深圳和广州之前没区分
            if (_layerType.length() == 2 && (SuperMapConfig.PROJECT_CITY_NAME.equals("广州") || SuperMapConfig.PROJECT_CITY_NAME.equals("深圳"))){
                _layerType = _layerType.substring(0, 1);
            }
            _info.symbolExpression = _layerType + "-" + _info.symbol;
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

           /* //查询数据每个标准点配置表 专题图符号大小
            String tabName = SQLConfig.TABLE_DEFAULT_POINT_SETTING;
            Cursor _cursorStand = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO, "where name = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
            //查询此标准的点表名
            while (_cursorStand.moveToNext()) {
                tabName = _cursorStand.getString(_cursorStand.getColumnIndex("pointsettingtablesymbol"));
            }

            if (tabName.length() == 0) {
                LogUtills.i("begin " + this.getClass().getName() + "tabName = null ");
            }*/

            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_DEFAULT_POINT_SETTING,
                    new String[]{"symbolID", "scaleX", "scaleY"}, "name=?", new String[]{_info.symbolExpression.trim().toString()}, null, null, null);
            LogUtills.i("Sql:" + _cursor.getCount());
            if (_cursor.moveToNext()) {
                _info.symbolID = _cursor.getInt(_cursor.getColumnIndex("symbolID"));
                _info.symbolSizeX = _cursor.getDouble(_cursor.getColumnIndex("scaleX"));
                _info.symbolSizeY = _cursor.getDouble(_cursor.getColumnIndex("scaleY"));
            }

        } catch (Exception e) {
            LogUtills.i(e.getMessage());
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
            Uri fileUri = FileProvider.getUriForFile(getContext(), "com.app.pipelinesurvey", m_pictureName);
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
                if (itemStr.contains("孔") || itemStr.contains("井") || itemStr.contains("篦")) {
                    if (linearAppendantPanel.getVisibility() != View.VISIBLE) {
                        linearAppendantPanel.setVisibility(View.VISIBLE);
                        if (animSwitch) {
                            m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_autotv_show);
                            linearAppendantPanel.startAnimation(m_animation);
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
            return String.format("%.3f", d);
        } else {
            return "";
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
        if (s.endsWith("-")){
            s = s.substring(0,s.length()-1);
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
        if (_appendant.contains("孔") || _appendant.contains("井") || _appendant.contains("篦")) {
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
            int temp = (int) (s * 100);
            String depth = String.valueOf(temp);
            edtWellDepth.setText(depth);
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
            int temp = (int) (s * 100);
            String depth = String.valueOf(temp);
            edtWellWater.setText(depth);
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
            int temp = (int) (s * 100);
            String depth = String.valueOf(temp);
            edtWellMud.setText(depth);
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
        InitWindowSize.ins().initWindowSize(getActivity(), getDialog());
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
        if (itemStr.contains("孔") || itemStr.contains("井") || itemStr.contains("篦")) {
            if (linearAppendantPanel.getVisibility() != View.VISIBLE) {
                linearAppendantPanel.setVisibility(View.VISIBLE);
                if (animSwitch) {
                    m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_autotv_show);
                    linearAppendantPanel.startAnimation(m_animation);
                }
            }
        } else {
            linearAppendantPanel.setVisibility(View.GONE);
        }
        animSwitch = true;
    }
}
