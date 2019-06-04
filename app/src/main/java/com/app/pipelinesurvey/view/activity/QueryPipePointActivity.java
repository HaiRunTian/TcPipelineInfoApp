package com.app.pipelinesurvey.view.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
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
import com.app.BaseInfo.Data.MAPACTIONTYPE2;
import com.app.BaseInfo.Data.PointFieldFactory;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.CameraUtils;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.MyAlertDialog;
import com.app.pipelinesurvey.utils.PipeThemelabelUtil;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.view.iview.IDrawPipePointView;
import com.app.pipelinesurvey.view.iview.IQueryPipePointView;
import com.app.utills.LogUtills;
import com.supermap.data.Recordset;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hairun on 2018-07-10 14:24.
 * 查询管点
 * 编辑管点
 * 移动管点
 */

public class QueryPipePointActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, IQueryPipePointView, IDrawPipePointView {
    private TextView tvTitle;//标题
    private TextView tvSubmit;//提交
    private Button btnPreviousOne;//导入上一个数据
    //    private Spinner spPointRemark;//点备注
    private EditText edtGpId;//点号1
    private Spinner spSituation;//点号2
    private Spinner spAppendant;//附属物
    private Spinner spFeaturePoints;//特征点
    private Spinner spState;//状态
    private Spinner spWellLidTexture;//井盖材质
    private EditText edtWellSize;//井规格
    private EditText edtWellDepth;//井深度
    private EditText edtWellWater;//水深
    private EditText edtWellMud;//淤泥
    private EditText edtWellLidSize;//井盖规格
    private EditText edtElevation;//高程
    private EditText edtOffset;//管偏
    private EditText edtBuildingStructures;//建构筑物
    private EditText edtRoadName;//路名
    private EditText edtPointRemark;//点备注
    private EditText edtPuzzle;//疑难
    private EditText edtStartDirDepth;//开始方向埋深
    private EditText edtEndDirDepth;//终止方向埋深
    private ImageView imgvPointRemark;//下拉框按钮
    private LinearLayout linearAppendantPanel;//附属物相关面板
    private LinearLayout linearReturn;//返回
    private LinearLayout layoutLineMark;//管点备注布局
    private LinearLayout layoutStartDirDepth;//开始方向埋深布局
    private LinearLayout layoutEndDirDepth;//终止方向埋深布局
    private Button btnChangeStyle; //转换按钮 临时点转实点
    private Animation m_animation;//动画
    private List<String> featurePointsList;//特征点数据
    private List<String> appendantList;//附属物数据
    private List<String> situationList;//点状况数据
    private List<String> stateList;//状态数据
    private List<String> wellLidTextureList;//井盖材质数据
    private List<String> pointRemarkList;//点备注数据
    private ArrayAdapter<String> m_adapter;//
    private boolean animSwitch = false;//动画开关
    private ListPopupWindow _popupWindow;
    private BaseFieldPInfos m_basePInfo;
    private Button btnDelPoint;
    private Button btnMove;
    private Button btnOpenCamera; //开启相机
    //private DatasetVector m_dsV = null;
    //private Recordset m_reSet = null;
    private int[] m_smid;
    private Bundle m_bundle;
    //    private boolean isEidt = true;
    private Button btnSave;
    //    private ToggleButton togBtn;
    private boolean previousOne = false;
    private String firstGpId = ""; //物探点号，如果用户修改了点号类型，用于判断
    private List<String> m_listPicName;
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器
    private GridView m_gridView;
    private Button m_btnAddPic;
    private List<String> picNames;           //临时图片文件名数组
    private EditText edtDepth;
    private Uri fileUri;//用户头像拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    private File resultImgFile;//最终生成的img文件
    private String pictureName = "";//照片全名xx.jpg
    private List<File> picFiles;  //临时图片文件数组
    private int m_picIndex = 0; //照片名字自增
    private String m_gpType;
    private String m_code;
    private String[] m_num = new String[]{};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_pipe_point);
        initView();
        initValue();

    }


    private void initValue() {
        m_bundle = getIntent().getExtras();
        m_basePInfo = m_bundle.getParcelable("info");
        LogUtills.i("queryActivity " + m_basePInfo.toString());
        int _smId = m_bundle.getInt("smId", 0);
        m_smid = new int[]{_smId};
        m_code = m_basePInfo.pipeType.substring(m_basePInfo.pipeType.length() - 1);
        //如果是修改管点信息，则需要获取选中观点的类型来更改属性。考虑下拉框给用户选择管类，则必须考虑管类不同而导致的，附属物、管点类型的不同而做的变化
//        int _typeIndex = m_basePInfo.pipeType.indexOf("_") + 1;
        m_gpType = m_basePInfo.pipeType.replace("_", "-");
        LogUtills.i("Query Point Info Type: " + m_basePInfo.pipeType + ", Change Type:" + m_gpType);
        // m_gpType = m_bundle.getString("gpType");//m_basePInfo.shortCode;//m_basePInfo.pipeType.substring(2).replace("_", "-");
        tvTitle.setText("编辑点" + "(" + m_gpType + ")");
//        LogUtills.i("geoPointStyle", _gpType);
        //特征点
        featurePointsList = SpinnerDropdownListManager.getData("feature", m_gpType);
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, featurePointsList);
        spFeaturePoints.setAdapter(m_adapter);
        //附属物
        appendantList = SpinnerDropdownListManager.getData("subsid", m_gpType);
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appendantList);
        spAppendant.setAdapter(m_adapter);
        //管点备注
        pointRemarkList = SpinnerDropdownListManager.getData("pointRemark", m_gpType);
        //物探点号状态
        situationList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.situation));
        //管点状态
        stateList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.state));
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stateList);
        spState.setAdapter(m_adapter);
        //井盖材质
        wellLidTextureList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.wellLidTexture));
        setValueToView();

    }

    private void setValueToView() {
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
        m_listPicName = getPicturefromReSet();
        //从本地读取照片，放到gridView中
        if (m_listPicName.size() > 0 && !previousOne) {
            m_picIndex = m_listPicName.size();
            for (String _s : m_listPicName) {
                boolean isExsit = FileUtils.getInstance().isFileExsit(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH + "/" + _s);
                if (!isExsit) {
                    ToastUtil.show(QueryPipePointActivity.this, "此图片不存在 = " + _s, 2);
                    continue;
                }
//                Bitmap _bitmap = BitmapFactory.decodeFile(SuperMapConfig.DEFAULT_DATA_PICTURE_PATH + _s);
                Bitmap _bitmap = CameraUtils.getimage(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH + "/" + _s);
                HashMap<String, Object> _map = new HashMap<>();
                _map.put("itemImage", _bitmap);
                _map.put("picName", _s);
                imageItem.add(_map);
                refreshGridviewAdapter();
            }

        }


    }

    private void initView() {

        tvTitle = $(R.id.tvTitle);
        tvSubmit = $(R.id.tvSubmit);
        btnPreviousOne = $(R.id.btnPreviousOne);
        edtGpId = $(R.id.edtGpId);
        spSituation = $(R.id.spSituation);
        spAppendant = $(R.id.spAppendant);
        spFeaturePoints = $(R.id.spFeaturePoints);
        spState = $(R.id.spState);
        spWellLidTexture = $(R.id.spWellLidTexture);
        edtWellSize = $(R.id.edtWellSize);
        edtWellDepth = $(R.id.edtWellDepth);
        edtWellWater = $(R.id.edtWellWater);
        edtWellMud = $(R.id.edtWellMud);
        edtWellLidSize = $(R.id.edtWellLidSize);
        edtElevation = $(R.id.edtElevation);
        edtOffset = $(R.id.edtOffset);
        edtBuildingStructures = $(R.id.edtBuildingStructures);
        edtRoadName = $(R.id.edtRoadName);
        edtPointRemark = $(R.id.edtPointRemark);
        edtPuzzle = $(R.id.edtPuzzle);
        edtStartDirDepth = $(R.id.edtStartDirDepth);
        edtEndDirDepth = $(R.id.edtEndDirDepth);
        imgvPointRemark = $(R.id.imgvPointRemark);
        linearAppendantPanel = $(R.id.linearAppendantPanel);
        linearReturn = $(R.id.linearReturn);
        layoutLineMark = $(R.id.layoutPointMark);
        layoutStartDirDepth = $(R.id.layoutStartDirDepth);
        layoutEndDirDepth = $(R.id.layoutEndDirDepth);
        btnDelPoint = $(R.id.btnRemove);
        btnMove = $(R.id.btnMove);
        m_gridView = $(R.id.gridView1);
        m_btnAddPic = $(R.id.btnAddPic);
        btnSave = $(R.id.btnSave);
        edtDepth = $(R.id.edtDepth);
        btnChangeStyle = $(R.id.btnChangeStyle);
        btnOpenCamera = $(R.id.btnAddPic);
//        togBtn              = $(R.id.togBtn);

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
//        m_gridView.setOnItemClickListener(this);
//        m_gridView.setOnItemLongClickListener(this);

//        spWellLidTexture.setEnabled(false);
//        spState.setEnabled(false);
//        spSituation.setEnabled(false);
//        spFeaturePoints.setEnabled(false);
//        spAppendant.setEnabled(false);

//        togBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    setViewEdit(true);
//                }else {
//                    setViewEdit(false);
//                }
//            }
//        });


        spSituation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String _situation = spSituation.getSelectedItem().toString();
                if (!_situation.contains("正常")) {
                    edtPointRemark.setText(spSituation.getSelectedItem().toString().substring(2));
                    //判断最后一位是否是字母结尾
                    Pattern _pattern = Pattern.compile("[a-zA-Z]");
                    if (_pattern.matcher(getGPId().substring(getGPId().length() - 1)).find()) {
                        edtGpId.setText(getGPId().substring(0, getGPId().length() - 1) + spSituation.getSelectedItem().toString().substring(0, 1));
                    } else
                        edtGpId.setText(getGPId() + spSituation.getSelectedItem().toString().substring(0, 1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private BaseFieldPInfos generateBaseFieldInfo() {
        BaseFieldPInfos _info = null;
        try {
            String gpType = m_bundle.getString("gpType", "");
//        LogUtills.i("point begin generateBaseFieldInfo layer = " + gpType);";
            //管类更改地方
            String _layerType = gpType.trim().substring(gpType.length() - 1);

            _info = PointFieldFactory.CreateInfo(gpType.substring(0, 2));
            if (_info == null) {
                LogUtills.e(" Create BaseFieldPInfos Fail...");
                return null;
            }

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
            _info.exp_Date = m_basePInfo.exp_Date;//format1.format(new Date()).toString(); 调查日期
            _info.wellCoverMaterial = getWellLidTexture();
            _info.wellCoverSize = getWellLidSize();
            _info.buildingStructures = getBuildingStructures();
            _info.code = m_code;
            _info.longitude = getLongitude();
            _info.latitude = getLatitude();
            _info.surf_H = getElevation();
            _info.expGroup = m_basePInfo.expGroup;
            _info.remark = getPointRemark();
            _info.picture = getPictureName();  //照片名字
            _info.id = getGPId();
            _info.puzzle = getPuzzle();
            _info.situation = getSituation();
            _info.sysId = m_smid[0];
            _info.symbol = getSymbol();
            _info.symbolExpression = _layerType + "-" + _info.symbol;
            //是否是临时点转实点
            if (m_num.length == 2) {
                //判断用户是否修改了管点编号
                if (!m_num[0].equals(getGPId())) {
                    _info.serialNum = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
                } else {
                    _info.serialNum = Integer.parseInt(m_num[1]);
                }
            } else {
                _info.serialNum = m_basePInfo.serialNum;
            }

        } catch (Exception e) {
            LogUtills.i(e.getMessage());
        }
        return _info;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtil.cancelToast();
      /*  LogUtills.i("...........m_reSet:"+m_reSet.wasNull()+", isClosed"+m_reSet.isClosed()+"edit "+m_reSet.isReadOnly());
        if(!m_reSet.wasNull()){
          // if(m_reSet.isClosed()) m_reSet.close();
           m_reSet.dispose();
            LogUtills.i("...........m_reSet:");
        }*/
    }

    /*    @Override
        protected void onDestroy() {
            super.onDestroy();

            RefWatcher refWatcher = MyApplication.Ins().getRefWatcher(this);//1
            refWatcher.watch(this);

            ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
                    .instanceField("android.view.inputmethod.InputMethodManager", "sInstance")
                    .instanceField("android.view.inputmethod.InputMethodManager", "mLastSrvView")
                    .instanceField("com.android.internal.policy.PhoneWindow$DecorView", "mContext")
                    .instanceField("android.support.v7.widget.SearchView$SearchAutoComplete", "mContext")
                    .build();
        }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.linearReturn:
                finish();
                break;
            //提交 保存 如果修改了管类，则要删除原先点，
            case R.id.tvSubmit:
            case R.id.btnSave:
                boolean _result = false;
                //编辑保存数据数据
                _result = DataHandlerObserver.ins().editRecords(generateBaseFieldInfo());
                if (!_result) {
                    ToastUtil.show(this.getBaseContext(), "保存点数据失败", 2);
                    return;
                } else {
                    ToastUtil.show(this.getBaseContext(), "保存点数据成功", 2);
                    finish();
                }

                break;

            //删除点 删除线
            case R.id.btnRemove:
                int _smId = m_bundle.getInt("smId", 0);
                //点
                Recordset _reSet = DataHandlerObserver.ins().queryRecordsetBySmid(_smId, true, true);
                if (_reSet.isEmpty()) {
                    LogUtills.i("Delete Point Faild, ID=" + _smId);
                    return;
                }
                _reSet.edit();
                if (_reSet.delete()) {
                    _reSet.update();
                    ToastUtil.showShort(QueryPipePointActivity.this, "删除点成功");
                    finish();
                } else {
                    ToastUtil.showShort(QueryPipePointActivity.this, "删除点失败");
                }

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

                _reSet.close();
                _reSet.dispose();
                _resetL.close();
                _resetL.dispose();
                break;
            //导入上一个
            case R.id.btnPreviousOne:
                previousOne = true;
                setValueToView();
                break;

            //临时点转实点
            case R.id.btnChangeStyle:
                if (getGPId().contains("T_")) {
                    m_num = ComTool.Ins().getPointNumber(m_gpType.substring(m_gpType.length()-1), false, "");
                    String _endExpNum = getGPId();
                    edtGpId.setText(m_num[0]);
                    //更新线
                    Recordset _recordset = DataHandlerObserver.ins().QueryRecordsetBySql("endExpNum = '" + _endExpNum + "'", false, true);
                    if (!_recordset.isEmpty()) {
                        _recordset.edit();
                        while (!_recordset.isEOF()) {
                            _recordset.setString("endExpNum", m_num[0]);
                            _recordset.setString("pipeType", m_basePInfo.pipeType);
                            _recordset.setString("labelTag", _recordset.getString("labelTag").replace("临时", m_basePInfo.pipeType.substring(0, 2)));
                            _recordset.setDouble("rangeExpression", PipeThemelabelUtil.Ins().getThemeItemValue(m_basePInfo.pipeType.substring(0,2)));
                            _recordset.moveNext();
                        }
                    }
                    _recordset.close();
                    _recordset.dispose();
                }
                break;

            //移动管点
            case R.id.btnMove:
                //应先保存管点信息；弹出对话框，是否保存属性信息（坐标信息除外）
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_EditPointLocation);
                _smId = m_bundle.getInt("smId", 0);
                DataHandlerObserver.ins().addPointSmID(_smId);
                finish();
                break;
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
        m_picIndex++;
        File m_pictureName;
        m_pictureName = new File(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/Picture", edtGpId.getText().toString() + "_" + m_picIndex + ".jpg");
        fileUri = Uri.fromFile(m_pictureName);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);

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
        edtStartDirDepth.setEnabled(state);
        edtEndDirDepth.setEnabled(state);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * 设置物探点号
     */
    @Override
    public void setGPId() {
        String _gpId = m_basePInfo.exp_Num;
        if (_gpId.length() != 0) {
            edtGpId.setText(_gpId);
        }
        if (_gpId.contains("T_")) {
            btnChangeStyle.setVisibility(View.VISIBLE);
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
        return edtElevation.getText().toString();
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
        if (_appendant.contains("孔") || _appendant.contains("井") || _appendant.contains("篦")) {//附属物取“孔”“井”时铺开隐藏面板
            if (linearAppendantPanel.getVisibility() != View.VISIBLE) {
                linearAppendantPanel.setVisibility(View.VISIBLE);
                setWellSize();
                setWellDepth();
                setWellWater();
                setWellMud();
                setWellLidTexture();
                setWellLidSize();
                if (animSwitch) {
                    m_animation = AnimationUtils.loadAnimation(this, R.anim.alpha_autotv_show);
                    linearAppendantPanel.startAnimation(m_animation);
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
        if (_evevation.length() != 0) {
            edtElevation.setText(_evevation);
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
        String _data = m_basePInfo.exp_Date;
        String _statue = m_basePInfo.state;
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
        if (_wellDepth.length() != 0) {
            edtWellDepth.setText(_wellDepth);
        }
    }

    /**
     * 设置水深
     */
    @Override
    public void setWellWater() {
        String _wellWater = m_basePInfo.wellWater;
        if (_wellWater.length() != 0) {
            edtWellWater.setText(_wellWater);
        }
    }

    /**
     * 设置淤泥
     */
    @Override
    public void setWellMud() {
        String _wellMud = m_basePInfo.wellMud;
        if (_wellMud.length() != 0) {
            edtWellMud.setText(_wellMud);
        }
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
        int _i = _list.size();
        return _list;
    }

    //刷新图片区域gridview
    private void refreshGridviewAdapter() {
        simpleAdapter = new SimpleAdapter(this, imageItem,
                R.layout.layout_griditem_addpic, new String[]{"itemImage", "picName"}, new int[]{R.id.imageView1, R.id.tvPicName});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(final View view, final Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    runOnUiThread(new Runnable() {
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
        runOnUiThread(new Runnable() {//主线程绑定adapter刷新数据
            @Override
            public void run() {
                m_gridView.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
            }
        });
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//    }
//
//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        return false;
//    }

    private void initShowPicArea() {
        picNames = new ArrayList<>();
        imageItem = new ArrayList<>();
        picFiles = new ArrayList<>();
        m_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //                if (picFiles.get(position) != null) {
                //                    //打开照片查看
                //                    Intent intent = new Intent();
                //                    intent.setAction(Intent.ACTION_VIEW);
                //                    intent.setDataAndType(Uri.fromFile(picFiles.get(position)), CameraUtils.IMAGE_UNSPECIFIED);
                //                    startActivity(intent);
                //                }
                if (m_listPicName.get(position) != null) {
                    //打开照片查看
                    File _file = new File(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH, m_listPicName.get(position));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(_file), "image/*");
                    startActivity(intent);
                }
            }
        });

        m_gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                MyAlertDialog.showAlertDialog(QueryPipePointActivity.this, "删除提示", "确定删除改照片？", "确定", "取消", true,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FileUtils.getInstance().deleteFile(picFiles.get(position)); //从手机卡删除
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap picBitmap = null;
        try {
            switch (requestCode) {
                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO:
                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        picBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        picBitmap = CameraUtils.comp(picBitmap);
                        if (picBitmap != null) {
                            resultImgFile = new File(new URI(uri.toString()));
                            if (!FileUtils.isFileNameIllegal(resultImgFile.getName())) {
                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFile);
//                                if (_overLimit) {
//                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
//                                } else {
                                pictureName = resultImgFile.getName();//拍摄返回的图片name
                                picNames.add(pictureName);
                                picFiles.add(resultImgFile);
                                HashMap<String, Object> _map = new HashMap<>();
                                _map.put("itemImage", picBitmap);
                                _map.put("picName", pictureName);
                                imageItem.add(_map);
                                refreshGridviewAdapter();
                                //                                    m_patrolLogPresenter.uploadFile();
//                                }
                            } else {
                                ToastUtil.show(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            ToastUtil.show(this, "new safety log error e:=" + e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    //返回编号为
    public String getSymbol() {
        String _symbol = "";
        switch (m_gpType) {
            case "给水-J": {
                switch (getAppendant()) {
                    case "阀门":
                        _symbol = "阀门";
                        break;
                    case "消防栓":
                        _symbol = "消防栓";
                        break;
                    case "水表":
                    case "水表井":
                        _symbol = "水表";
                        break;
                    case "窨井":
                    case "阀门井":
                    case "消防井":
                    case "检修井":
                        _symbol = "窨井";
                        break;
                    case "放水口":
                        _symbol = "放水口";
                        break;
                    case "探测点":
                        switch (getFeaturePoints()) {
                            case "预留口":
                                _symbol = "预留口";
                                break;
                            case "变径":
                                _symbol = "变径";
                                break;
                            case "非普查区":
                            case "出测区":
                                _symbol = "出测区";
                                break;
                            case "出地":
                                _symbol = "出地";
                                break;
                            case "入户":
                                _symbol = "入户";
                                break;
                            default:
                                _symbol = "探测点";
                                break;
                        }
                        break;
                    default:
                        _symbol = "探测点";

                }
            }
            break;
            case "雨水-Y":
            case "污水-W":
            case "排水-P": {
                switch (getAppendant()) {
                    case "窨井":
                    case "检查井":
                    case "雨水井":
                    case "检修井":
                        _symbol = "窨井";
                        break;
                    case "接户井":
                        _symbol = "接户井";
                        break;
                    case "雨篦井":
                    case "污水篦":
                    case "雨水篦":
                    case "进水井":
                    case "雨水口":
                        _symbol = "雨水篦";
                        break;
                    case "闸阀井":
                        _symbol = "闸阀井";
                        break;
                    case "溢流井":
                        _symbol = "溢流井";
                        break;
                    case "透气井":
                        _symbol = "透气井";
                        break;
                    case "计量井":
                        _symbol = "计量井";
                        break;
                    case "拍门井":
                        _symbol = "拍门井";
                        break;
                    case "沉沙井":
                        _symbol = "沉沙井";
                        break;
                    case "检测井":
                    case "特殊功能井":
                        _symbol = "检测井";
                        break;
                    case "化粪池":
                        _symbol = "化粪池";
                        break;
                    case "水控闸":
                        _symbol = "水控闸";
                        break;
                    case "电控闸":
                        _symbol = "电控闸";
                        break;
                    case "手控闸":
                        _symbol = "手控闸";
                        break;
                    case "立管点":
                        _symbol = "立管点";
                        break;

                    case "探测点":
                        switch (getFeaturePoints()) {
                            case "预留口":
                                _symbol = "预留口";
                                break;
                            case "非普查区":
                            case "出测区":
                                _symbol = "出测区";
                                break;
                            case "进出水口":
                            case "出水口":
                            case "入水口":
                            case "进水口":
                                _symbol = "进出水口";
                                break;
                            case "户出":
                            case "入户":
                                _symbol = "入户";
                                break;
                            default:
                                _symbol = "探测点";
                                break;
                        }
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }

            }
            break;

            case "交通-X":
            case "电信-D": {
                switch (getAppendant()) {
                    case "人孔":
                    case "人孔井":
                        _symbol = "人孔井";
                        break;
                    case "手孔":
                    case "手孔井":
                        _symbol = "手孔井";
                        break;
                    case "接线箱":
                    case "交接箱":
                    case "配电箱":
                        _symbol = "接线箱";
                        break;
                    case "电话亭":
                        _symbol = "电话亭";
                        break;
                    case "监控器":
                        _symbol = "监控器";
                        break;
                    case "摄像头":
                        _symbol = "摄像头";
                        break;
                    case "红绿灯":
                        _symbol = "红绿灯";
                        break;
                    case "信号灯":
                        _symbol = "信号灯";
                        break;
                    case "探测点":
                        switch (getFeaturePoints()) {
                            case "预留口":
                                _symbol = "预留口";
                                break;
                            case "非普查区":
                            case "出测区":
                                _symbol = "出测区";
                                break;
                            case "上杆":
                                _symbol = "上杆";
                                break;
                            case "出地":
                                _symbol = "出地";
                                break;
                            case "入户":
                                _symbol = "入户";
                                break;
                            default:
                                _symbol = "探测点";
                                break;
                        }
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }

            }
            break;

            case "电力-L": {
                switch (getAppendant()) {
                    case "地灯":
                        _symbol = "地灯";
                        break;
                    case "窨井":
                    case "人孔":
                    case "人孔井":
                    case "检修井":
                        _symbol = "窨井";
                        break;
                    case "接线箱":
                    case "控制柜":
                    case "交接箱":
                        _symbol = "接线箱";
                        break;
                    case "变电箱":
                    case "变压箱":
                        _symbol = "变电箱";
                        break;
                    case "路灯":
                    case "路灯杆":
                        _symbol = "路灯";
                        break;
                    case "信号灯":
                        _symbol = "信号灯";
                        break;
                    case "探测点":
                        switch (getFeaturePoints()) {
                            case "信号灯":
                                _symbol = "信号灯";
                                break;
                            case "非普查区":
                            case "出测区":
                                _symbol = "出测区";
                                break;
                            case "上杆":
                                _symbol = "上杆";
                                break;
                            case "预留口":
                                _symbol = "预留口";
                                break;
                            case "出地":
                                _symbol = "出地";
                                break;
                            case "入户":
                                _symbol = "入户";
                                break;
                            default:
                                _symbol = "探测点";
                                break;
                        }
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
            }
            break;
            case "燃气-R":
            case "煤气-M": {
                switch (getAppendant()) {
                    case "阀门":
                        _symbol = "阀门";
                        break;
                    case "窨井":
                    case "阀门井":
                    case "检测井":
                    case "检修井":
                        _symbol = "窨井";
                        break;
                    case "放水器":
                        _symbol = "放水器";
                        break;
                    case "凝水缸":
                    case "抽水缸":
                        _symbol = "凝水缸";
                        break;
                    case "调压箱":
                        _symbol = "调压箱";
                        break;
                    case "探测点":
                        switch (getFeaturePoints()) {
                            case "预留口":
                                _symbol = "预留口";
                                break;
                            case "变径":
                                _symbol = "变径";
                                break;
                            case "非普查区":
                            case "出测区":
                                _symbol = "出测区";
                                break;
                            case "出地":
                                _symbol = "出地";
                                break;
                            case "入户":
                                _symbol = "入户";
                                break;
                            default:
                                _symbol = "探测点";
                                break;
                        }
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
            }
            break;

            case "路灯-S": {
                switch (getAppendant()) {
                    case "地灯":
                        _symbol = "地灯";
                        break;
                    case "人孔":
                    case "人孔井":
                    case "检修井":
                        _symbol = "人孔井";
                        break;
                    case "手孔":
                    case "手孔井":
                        _symbol = "手孔井";
                        break;
                    case "接线箱":
                    case "交接箱":
                    case "配电箱":
                        _symbol = "接线箱";
                        break;
                    case "路灯":
                    case "路灯杆":
                        _symbol = "路灯";
                        break;
                    case "探测点":
                        switch (getFeaturePoints()) {
                            case "预留口":
                                _symbol = "预留口";
                                break;
                            case "上杆":
                                _symbol = "上杆";
                                break;
                            case "非普查区":
                            case "出测区":
                                _symbol = "出测区";
                                break;
                            case "出地":
                                _symbol = "出地";
                                break;
                            case "入户":
                                _symbol = "入户";
                                break;
                            default:
                                _symbol = "探测点";
                                break;
                        }
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }

            }
            break;

            case "有视-T": {
                switch (getAppendant()) {
                    case "人孔":
                    case "人孔井":
                        _symbol = "人孔";
                        break;
                    case "手孔":
                    case "手孔井":
                        _symbol = "手孔";
                        break;
                    case "接线箱":
                        _symbol = "接线箱";
                        break;
                    case "探测点":
                        switch (getFeaturePoints()) {
                            case "预留口":
                                _symbol = "预留口";
                                break;
                            case "非普查区":
                            case "出测区":
                                _symbol = "出测区";
                                break;
                            case "上杆":
                                _symbol = "上杆";
                                break;
                            case "出地":
                                _symbol = "出地";
                                break;
                            case "入户":
                                _symbol = "入户";
                                break;
                            default:
                                _symbol = "探测点";
                                break;
                        }
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }

            }
            break;
            case "军队-B": {
                switch (getAppendant()) {
                    case "人孔井":
                        _symbol = "人孔井";
                        break;
                    case "手孔井":
                        _symbol = "手孔井";
                        break;
                    case "接线箱":
                        _symbol = "接线箱";
                        break;
                    case "探测点":
                        switch (getFeaturePoints()) {
                            case "预留口":
                                _symbol = "预留口";
                                break;
                            case "上杆":
                                _symbol = "上杆";
                                break;
                            case "非普查区":
                            case "出测区":
                                _symbol = "出测区";
                                break;
                            case "出地":
                                _symbol = "出地";
                                break;
                            case "入户":
                                _symbol = "入户";
                                break;
                            default:
                                _symbol = "探测点";
                                break;
                        }
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }
            }
            break;
            case "工业-G": {
                switch (getAppendant()) {
                    case "阀门":
                        _symbol = "阀门";
                        break;
                    case "窨井":
                    case "阀门井":
                    case "检测井":
                    case "检修井":
                        _symbol = "窨井";
                        break;
                    case "放水器":
                        _symbol = "放水器";
                        break;
                    case "调压箱":
                        _symbol = "调压箱";
                        break;
                    case "探测点":
                        switch (getFeaturePoints()) {
                            case "预留口":
                                _symbol = "预留口";
                                break;
                            case "变径":
                                _symbol = "变径";
                                break;
                            case "非普查区":
                            case "出测区":
                                _symbol = "出测区";
                                break;
                            case "出地":
                                _symbol = "出地";
                                break;
                            case "入户":
                                _symbol = "入户";
                                break;
                            default:
                                _symbol = "探测点";
                                break;
                        }
                        break;
                    default:
                        _symbol = "探测点";
                        break;
                }

            }
            break;
            case "不明-N":
            case "其它-Q": {
            }
            break;
            default:
                _symbol = "探测点";
                break;
        }

        return _symbol;
    }


}
