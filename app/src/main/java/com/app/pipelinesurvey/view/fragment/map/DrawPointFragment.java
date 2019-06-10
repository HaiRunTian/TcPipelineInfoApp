package com.app.pipelinesurvey.view.fragment.map;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.PointFieldFactory;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.CameraUtils;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.InitWindowSize;
import com.app.pipelinesurvey.utils.MyAlertDialog;
import com.app.pipelinesurvey.utils.SymbolInfo;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.iview.IDrawPipePointView;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author HaiRun
 * @date
 */
public class DrawPointFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener, IDrawPipePointView, AdapterView.OnItemLongClickListener {
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
     * 特征点
     */
    private Spinner spFeaturePoints;
    /**
     * 附属物
     */
    private Spinner spAppendant;
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
     * 开始方向埋深
     */
    private EditText edtStartDirDepth;
    /**
     * 终止方向埋深
     */
    private EditText edtEndDirDepth;
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
     * 开始方向埋深布局
     */
//    private LinearLayout layoutStartDirDepth;
    /**
     * 终止方向埋深布局
     */
//    private LinearLayout layoutEndDirDepth;
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.activity_draw_pipe_point, container, false);
        initView(m_view);

        return m_view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValue();
        initID();
    }

    /**
     * 初始化管点编号ID
     */
    private void initID() {
        //管线种类和代码 排水-P
        String _gpType = getArguments().getString("gpType");
        //从第三位开始截取管类代码
        m_code = _gpType.substring(3);
        LogUtills.i("Code=" + m_code);
        //初始化管点编号
        m_num = ComTool.Ins().getPointNumber(m_code, false, "");
        //设置编号到view中
        edtGpId.setText(m_num[0]);
    }


    /**
     * 初始化数据
     */
    private void initValue() {
        String _gpType = getArguments().getString("gpType");
        tvTitle.setText("加点" + "(" + _gpType + ")");
        m_pointX = getArguments().getDouble("x", 0.0);
        m_pointY = getArguments().getDouble("y", 0.0);
        m_smId = getArguments().getInt("smid", -1);

        //点特征
        featurePointsList = SpinnerDropdownListManager.getData("feature", _gpType);
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, featurePointsList);
        spFeaturePoints.setAdapter(m_adapter);

        //附属物
        appendantList = SpinnerDropdownListManager.getData("subsid", _gpType);
        m_adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, appendantList);
        spAppendant.setAdapter(m_adapter);

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

    private void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        spAppendant = (Spinner) view.findViewById(R.id.spAppendant);
        spAppendant.setOnItemSelectedListener(this);
        spFeaturePoints = (Spinner) view.findViewById(R.id.spFeaturePoints);
//                spPointRemark = findViewById(R.id.spPointRemark);
//                spPointRemark.setOnItemSelectedListener(this);
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
//        edtGpId = (EditText) findViewById(R.id.edtGpId);
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
//        layoutStartDirDepth = (LinearLayout) view.findViewById(R.id.layoutStartDirDepth);
//        layoutEndDirDepth = (LinearLayout) view.findViewById(R.id.layoutEndDirDepth);
        edtStartDirDepth = (EditText) view.findViewById(R.id.edtStartDirDepth);
        edtEndDirDepth = (EditText) view.findViewById(R.id.edtEndDirDepth);
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
//        edtOffset.addTextChangedListener(m_watcher);
        spSituation.setOnItemSelectedListener(this);
        initTakePicArea();
    }

    private void initTakePicArea() {
        picFiles = new ArrayList<>();
        picNames = new ArrayList<>();
        imageItem = new ArrayList<HashMap<String, Object>>();
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
                updateIdAndPointRemark();

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
                }else {
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
                            ToastUtil.showShort(getActivity(), "点号重复，请重新编号");
                            return;
                        }
                        _result = DataHandlerObserver.ins().createRecords2(generateBaseFieldInfo());
                        if (!_result) {
                            ToastUtil.showShort(getActivity(), "保存点数据失败...");
                            return;
                        }
                        WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
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
     * @auther HaiRun
     * created at 2018/7/24 10:29
     */
    private void openCamera() {

        //照片名字
        try {

            m_PicIndex++;
            m_pictureName = new File(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/Picture", edtGpId.getText().toString() + "_" + m_PicIndex + ".jpg");
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
        try {


            String gpType = getArguments().getString("gpType");
            String _layerType = gpType.trim().substring(3);
            _info = PointFieldFactory.CreateInfo(gpType.substring(0, 2));
            if (_info == null) {
                return null;
            }
            _info.pipeType = gpType;
            _info.shortCode = _layerType;
            _info.exp_Num = getGPId();
            _info.code = m_code;
            _info.feature = getFeaturePoints();
            _info.subsid = getAppendant();
            _info.pipeOffset = getOffset();
            _info.wellSize = getWellSize();
            _info.wellDeep = getWellDepth();
            _info.wellWater = getWellWater();
            _info.wellMud = getWellMud();
            _info.road = getRoadName();
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
            _info.remark = getPointRemark();
            _info.picture = getPictureName();
            //标签专题图表达式
            _info.id = getGPId();
            _info.puzzle = getPuzzle();
            _info.situation = getSituation();
            _info.sysId = m_smId;
            _info.depth = getDepth();
            _info.symbol = SymbolInfo.Ins().getSymbol(gpType, getAppendant(), getFeaturePoints());
            if (_layerType.length() == 2) {
                _layerType = _layerType.substring(0, 1);
            }
            _info.symbolExpression = _layerType + "-" + _info.symbol;
            LogUtills.i("symbolExpression", _info.symbolExpression);
            //判断用户是否修改了管点编号
            if (!m_num[0].equals(getGPId())) {
                _info.serialNum = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
            } else {
                _info.serialNum = Integer.parseInt(m_num[1]);
            }

            //查询数据每个标准点配置表 专题图符号大小
            String tabName = SQLConfig.TABLE_DEFAULT_POINT_SETTING;
            Cursor _cursorStand = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO, "where name = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
            //查询此标准的点表名
            while (_cursorStand.moveToNext()) {
                tabName = _cursorStand.getString(_cursorStand.getColumnIndex("pointsettingtablesymbol"));
            }

            if (tabName.length() == 0) {
                LogUtills.i("begin " + this.getClass().getName() + "tabName = null ");
            }

            Cursor _cursor2 = DatabaseHelpler.getInstance().query(tabName,
                    new String[]{"symbolID", "scaleX", "scaleY"}, "name=?", new String[]{_info.symbolExpression.trim().toString()}, null, null, null);


            LogUtills.i("Sql:" + _cursor2.getCount());
            if (_cursor2.moveToNext()) {
                _info.symbolID = _cursor2.getInt(_cursor2.getColumnIndex("symbolID"));
                _info.symbolSizeX = _cursor2.getDouble(_cursor2.getColumnIndex("scaleX"));
                _info.symbolSizeY = _cursor2.getDouble(_cursor2.getColumnIndex("scaleY"));
            }
        } catch (Exception e) {
            LogUtills.i(e.getMessage());
        }
        return _info;
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
            BaseFieldPInfos _baseFieldPInfos = BaseFieldPInfos.createFieldInfo(_reSet);
            spSituation.setSelection(situationList.indexOf(_baseFieldPInfos.situation));
            spAppendant.setSelection(appendantList.indexOf(_baseFieldPInfos.subsid));
            spFeaturePoints.setSelection(featurePointsList.indexOf(_baseFieldPInfos.feature));
            spState.setSelection(stateList.indexOf(_baseFieldPInfos.state));
            spWellLidTexture.setSelection(wellLidTextureList.indexOf(_baseFieldPInfos.wellCoverMaterial));
            importDataToView(edtWellSize, _baseFieldPInfos.wellCoverSize);
            importDataToView(edtWellDepth, _baseFieldPInfos.wellDeep);
            importDataToView(edtWellWater, _baseFieldPInfos.wellWater);
            importDataToView(edtWellMud, _baseFieldPInfos.wellMud);
            importDataToView(edtWellSize, _baseFieldPInfos.wellCoverSize);
            importDataToView(edtElevation, _baseFieldPInfos.surf_H);
            importDataToView(edtOffset, _baseFieldPInfos.pipeOffset);
            importDataToView(edtBuildingStructures, _baseFieldPInfos.buildingStructures);
            importDataToView(edtRoadName, _baseFieldPInfos.road);
            importDataToView(edtPointRemark, _baseFieldPInfos.remark);
            importDataToView(edtPuzzle, _baseFieldPInfos.puzzle);

        } else {
            ToastUtil.showShort(getActivity(), "您未录入过此类型管点数据");
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
        return edtPointRemark.getText().toString();
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
        return edtWellDepth.getText().toString();
    }

    @Override
    public String getWellWater() {
        return edtWellWater.getText().toString();
    }

    @Override
    public String getWellMud() {
        return edtWellMud.getText().toString();
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

                        pictureName = m_pictureName.getName();
                        picNames.add(pictureName);
                        picFiles.add(m_pictureName);
                        HashMap<String, Object> _map = new HashMap<>();
                        _map.put("itemImage", picBitmap);
                        _map.put("picName", pictureName);
                        imageItem.add(_map);
                        refreshGridviewAdapter();

                    } else {
                        ToastUtil.show(getActivity(), "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                    }

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LogUtills.e("camera", "new safety log error e:=" + e.getMessage().toString() + "");
            ToastUtil.show(getActivity(), "new safety log error e:=" + e.getMessage(), Toast.LENGTH_SHORT);
        }
    }


    /**
     *  刷新图片区域gridview
     */
    private void refreshGridviewAdapter() {
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
    }

}
