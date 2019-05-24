package com.app.pipelinesurvey.view.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.app.BaseInfo.Data.PointFieldFactory;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.BaseInfo.Oper.OperNotifyer;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.bean.PipePointInfo;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.AlertDialogUtil;
import com.app.pipelinesurvey.utils.CameraUtils;
import com.app.pipelinesurvey.utils.ComTool;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.MyAlertDialog;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.iview.IDrawPipePointView;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLine;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.mapping.Layer;

import java.io.File;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by HaiRun on 2018-08-13 11:27.
 */

public class AddPointToLineActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener, IDrawPipePointView, AdapterView.OnItemLongClickListener {

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
    private Button btnAddPic; //添加照片按钮
    private GridView grdPicCon;//照片展示
    private int m_lineSmid = -1;
    private Uri fileUri;//用户头像拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    //private BaseFieldPInfos m_fieldPoint = null;
    //private String m_gpType = null;
    private double m_pointX = 0.0;
    private double m_pointY = 0.0;
    private Button btnSave;
    private File m_pictureName;
    private Bitmap picBitmap;//临时拍照图片
    private File resultImgFile;//最终生成的img文件
    private String pictureName = "";//照片全名xx.jpg
    private List<String> picNames;           //临时图片文件名数组
    private List<File> picFiles;             //临时图片文件数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器
    private String m_gpID;
    private int m_lastReSetSmid = -1;
    private EditText edtDepth;
    private String m_gpType;
    private int m_picIndex = 0;
    private String m_code;
    private String[] m_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point_to_line);
        initView();
        initValue();
        initID();
    }


    private void initID() {
        m_gpType = getIntent().getStringExtra("gpType");
        m_code = m_gpType.substring(m_gpType.length() - 1);
        m_num = ComTool.Ins().getPointNumber(m_code, false, "");
        edtGpId.setText(m_num[0]);
    }

    private void initValue() {
        String _gpType = getIntent().getStringExtra("gpType");
        tvTitle.setText("线中加点" + "(" + _gpType + ")");
        m_pointX = getIntent().getDoubleExtra("x", 0.0);
        m_pointY = getIntent().getDoubleExtra("y", 0.0);
        m_lineSmid = getIntent().getIntExtra("smId", -1);  //管线SMID
        featurePointsList = SpinnerDropdownListManager.getData("feature", _gpType);
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, featurePointsList);
        spFeaturePoints.setAdapter(m_adapter);
        appendantList = SpinnerDropdownListManager.getData("subsid", _gpType);
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appendantList);
        spAppendant.setAdapter(m_adapter);
        pointRemarkList = SpinnerDropdownListManager.getData("pointRemark", _gpType);
        situationList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.situation));
        stateList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.state));
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stateList);
        spState.setAdapter(m_adapter);
        wellLidTextureList = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.wellLidTexture));


    }

    //导入上一条记录集记录
    private void importData() {

        QueryParameter _parameter = new QueryParameter();
        _parameter.setAttributeFilter("code = '" + m_code + "'");
        _parameter.setCursorType(CursorType.STATIC);
        _parameter.setOrderBy(new String[]{"SmID asc"});
        Recordset _reSet = DataHandlerObserver.ins().QueryRecordsetByParameter(_parameter, true);
//        Layer _layer = DataHandlerObserver.ins().getPtLayerByName();
//        DatasetVector _datasetVector = (DatasetVector) _layer.getDataset();
////        int[] smid = new int[]{m_lastReSetSmid};
//        Recordset reSet = _datasetVector.getRecordset(false, CursorType.STATIC);
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
            ToastUtil.showShort(this, "您未录入过此类型管点数据");
        }

    }

    private void importDataToView(EditText view, String str) {
        if (str.length() > 0) {
            view.setText(str);
        } else view.setText("");

    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        spAppendant = (Spinner) findViewById(R.id.spAppendant);
        spAppendant.setOnItemSelectedListener(this);
        spFeaturePoints = (Spinner) findViewById(R.id.spFeaturePoints);
//                spPointRemark = findViewById(R.id.spPointRemark);
//                spPointRemark.setOnItemSelectedListener(this);
        linearAppendantPanel = (LinearLayout) findViewById(R.id.linearAppendantPanel);
        linearReturn = (LinearLayout) findViewById(R.id.linearReturn);
        linearReturn.setOnClickListener(this);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        edtPointRemark = (EditText) findViewById(R.id.edtPointRemark);
        imgvPointRemark = (ImageView) findViewById(R.id.imgvPointRemark);
        imgvPointRemark.setOnClickListener(this);
        layoutLineMark = (LinearLayout) findViewById(R.id.layoutPointMark);
        edtGpId = (EditText) findViewById(R.id.edtGpId);
        spSituation = (Spinner) findViewById(R.id.spSituation);
        edtWellSize = (EditText) findViewById(R.id.edtWellSize);
        edtWellDepth = (EditText) findViewById(R.id.edtWellDepth);
        edtWellWater = (EditText) findViewById(R.id.edtWellWater);
        edtWellMud = (EditText) findViewById(R.id.edtWellMud);
        spWellLidTexture = (Spinner) findViewById(R.id.spWellLidTexture);
        edtWellLidSize = (EditText) findViewById(R.id.edtWellLidSize);
        spState = (Spinner) findViewById(R.id.spState);
        edtOffset = (EditText) findViewById(R.id.edtOffset);
        edtElevation = (EditText) findViewById(R.id.edtElevation);
        edtBuildingStructures = (EditText) findViewById(R.id.edtBuildingStructures);
        edtRoadName = (EditText) findViewById(R.id.edtRoadName);
        edtPuzzle = (EditText) findViewById(R.id.edtPuzzle);
        layoutStartDirDepth = (LinearLayout) findViewById(R.id.layoutStartDirDepth);
        layoutEndDirDepth = (LinearLayout) findViewById(R.id.layoutEndDirDepth);
        edtStartDirDepth = (EditText) findViewById(R.id.edtStartDirDepth);
        edtEndDirDepth = (EditText) findViewById(R.id.edtEndDirDepth);
        btnPreviousOne = (Button) findViewById(R.id.btnPreviousOne);
        btnPreviousOne.setOnClickListener(this);
        edtDepth = (EditText) findViewById(R.id.edtDepth);
        btnAddPic = (Button) findViewById(R.id.btnAddPic);
        btnAddPic.setOnClickListener(this);
        grdPicCon = (GridView) findViewById(R.id.gridView1);
        grdPicCon.setOnItemLongClickListener(this);
        grdPicCon.setOnItemClickListener(this);
        btnSave = (Button) findViewById(R.id.btnRemove);
        btnSave.setOnClickListener(this);
        edtOffset.addTextChangedListener(m_watcher);
        spSituation.setOnItemSelectedListener(this);
        initTakePicArea();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spAppendant:
                String itemStr = spAppendant.getSelectedItem().toString();
                if (itemStr.contains("孔") || itemStr.contains("井") || itemStr.contains("篦")) {//附属物取“孔”“井”时铺开隐藏面板
                    if (linearAppendantPanel.getVisibility() != View.VISIBLE) {
                        linearAppendantPanel.setVisibility(View.VISIBLE);
                        if (animSwitch) {
                            m_animation = AnimationUtils.loadAnimation(this, R.anim.alpha_autotv_show);
                            linearAppendantPanel.startAnimation(m_animation);
                        }
                    }
                } else {
                    linearAppendantPanel.setVisibility(View.GONE);
                }
                animSwitch = true;
                break;

            case R.id.spSituation:
                edtPointRemark.setText(spSituation.getSelectedItem().toString().substring(2));
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
                break;
            //            case R.id.spPointRemark:
            //                edtPointRemark.setText(spPointRemark.getSelectedItem().toString());
            //                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearReturn:
//               DataHandlerObserver.ins().resetState();
                finish();
                break;
            case R.id.tvSubmit:
            case R.id.btnRemove:
                boolean _result = false;

                //判断重号
                if (ComTool.Ins().isSameNum(getGPId(), false)) {
                    ToastUtil.showShort(AddPointToLineActivity.this, "点号重复，请重新编号");
                    return;
                }
                _result = DataHandlerObserver.ins().createRecords2(generateBaseFieldInfo());
                if (!_result) {
                    ToastUtil.show(this.getBaseContext(), "保存点数据失败...", Toast.LENGTH_SHORT);
                    return;
                } else {
                    //查询刚添加点的记录集
                    Recordset _reSet = DataHandlerObserver.ins().queryRecordsetByExpNum(getGPId(), false);
                    if (!_reSet.isEmpty()) {
                        _reSet.moveFirst();
                        BaseFieldPInfos _centerPoint = BaseFieldPInfos.createFieldInfo(_reSet);
//                        int[] _ids = new int[]{m_lineSmid};
//
                        DatasetVector _lineDateset = (DatasetVector) DataHandlerObserver.ins().getTotalLrLayer().getDataset();
//                        Recordset _lineRecordset = _lineDateset.query(_ids, CursorType.DYNAMIC);
                        Recordset _lineRecordset = DataHandlerObserver.ins().queryRecordsetBySmid(m_lineSmid, false, true);
                        _lineRecordset.moveFirst();
                        Map<String, Object> _maps = OperNotifyer.getFieldMaps(_lineRecordset);
                        if (_maps.isEmpty()) {
                            ToastUtil.show(this, "发生未知错误，请重新选择，线中加点", Toast.LENGTH_SHORT);
                            return;
                        }
                        // LogUtills.i("_maps count "+ _maps.size());
                        GeoLine _geoLine = (GeoLine) _lineRecordset.getGeometry();

                        //当前数据的集合  返回此线几何对象中指定序号的子对象，以有序点集合的方式返回该子对象
                        Point2Ds _2dts = _geoLine.getPart(0);
                        if (_2dts.getCount() != 2) {
//                            LogUtills.e("获取的线对象错误....");
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
                            ToastUtil.showShort(this, "第一条线更新失败");
                            return;
                        }

                        _lineRecordset.edit();
                        _lineRecordset.setString("endExpNum", _centerPoint.exp_Num);
                        _lineRecordset.setDouble("endLongitude", _centerPoint.longitude);
                        _lineRecordset.setDouble("endLatitude", _centerPoint.latitude);
                        _lineRecordset.setString("endDeep", getDepth());  //改变终点埋深
                        _lineRecordset.setString("pipeLength", String.format("%.2f", _lineRecordset.getDouble("SmLength"))); //改变长度
                        if (!_lineRecordset.update()) {
                            ToastUtil.showShort(this, "第一条线长度更新失败");
                            return;
                        }
                        //第二条线记录集
                        Recordset _addNewRecordset = _lineDateset.getRecordset(true, CursorType.DYNAMIC);
                        _addNewRecordset.edit();
                        if(_addNewRecordset.addNew(new GeoLine(_endPt), _maps)){

                        }
                        if (!_addNewRecordset.update()) {
                            ToastUtil.showShort(this, "第二条线添加失败");
                        }
                        _addNewRecordset.edit();
                        _addNewRecordset.setInt32("sysid", _addNewRecordset.getID());
                        _addNewRecordset.setString("benExpNum", _centerPoint.exp_Num);
                        _addNewRecordset.setDouble("startLongitude", _centerPoint.longitude);
                        _addNewRecordset.setDouble("startLatitude", _centerPoint.latitude);
                        _addNewRecordset.setString("benDeep", getDepth()); //改变起点埋深
                        _addNewRecordset.setString("pipeLength", String.format("%.2f", _addNewRecordset.getDouble("SmLength"))); //改变长度
                        if (!_addNewRecordset.update()) {
                            ToastUtil.showShort(this, "第二条线长度更新失败");
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
                WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
                finish();
                break;
            case R.id.imgvPointRemark:
                _popupWindow = new ListPopupWindow(this);
                _popupWindow.setWidth(layoutLineMark.getWidth() - 5);
                m_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pointRemarkList);
                _popupWindow.setAdapter(m_adapter);
                _popupWindow.setAnchorView(edtPointRemark);
                _popupWindow.setModal(true);
                _popupWindow.setOnItemClickListener(this);
                _popupWindow.show();
                break;
            //导入上一个
            case R.id.btnPreviousOne:

                AlertDialogUtil.showDialog(this, "导入提示", "是否导入上一次输入的数据", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        importData();
                    }
                });

//                AlertDialogUtil.showDialog(this, "导入提示", "是否导入上次输入的数据", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();

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
        m_picIndex++;
        //照片名字
        m_pictureName = new File(SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.PROJECT_NAME + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH, edtGpId.getText().toString() + "_" + m_picIndex + ".jpg");
        fileUri = Uri.fromFile(m_pictureName);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);

    }

    private BaseFieldPInfos generateBaseFieldInfo() {

        BaseFieldPInfos _info = null;
        String gpType = getIntent().getStringExtra("gpType");
        String _layerType = gpType.trim().substring(gpType.length() - 1);
        _info = PointFieldFactory.CreateInfo(gpType.substring(0, 2));
        if (_info == null) return null;

        _info.pipeType = gpType;
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
        _info.state = getState();
        // _info.latitude
//        Date _currentTime = new Date();
        String date = DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT);
//        SimpleDateFormat _formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        android.icu.text.SimpleDateFormat _formatter = new android.icu.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String _dateString = _formatter.format(_currentTime);
        _info.exp_Date = date;//format1.format(new Date()).toString();
        _info.wellCoverMaterial = getWellLidTexture();
        _info.wellCoverSize = getWellLidSize();
        _info.buildingStructures = getBuildingStructures();
        //
        // GeoPoint _target = (GeoPoint) MyApplication.Ins().getMapControl().getCurrentGeometry();
        _info.longitude = m_pointX;
        _info.latitude = m_pointY;
        _info.surf_H = getElevation();
        Cursor _cursor1 = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO,"where Name = '" +SuperMapConfig.PROJECT_NAME + "'");
        if (_cursor1.moveToNext()) {
            _info.expGroup = _cursor1.getString(_cursor1.getColumnIndex("GroupNum"));
        }
        _info.remark = getPointRemark();
        _info.picture = getPictureName();
        _info.id = getGPId();
        _info.puzzle = getPuzzle();
        _info.situation = getSituation();
        _info.sysId = m_lastReSetSmid; //系统smid
        _info.picture = getPictureName();
        _info.symbol = getSymbol();
        _info.symbolExpression = _layerType + "-" + _info.symbol;
        //判断用户是否修改了管点编号
        if (!m_num[0].equals(getGPId())) {
            _info.serialNum = ComTool.Ins().getSerialNum(getGPId(), getSituation(), m_code);
        } else {
            _info.serialNum = Integer.parseInt(m_num[1]);
        }
        //LogUtills.i("Query symbolID..."+_info.symbolExpression);
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_DEFAULT_POINT_SETTING,
                new String[]{"symbolID", "scaleX", "scaleY"}, "name=?", new String[]{_info.symbolExpression.trim().toString()}, null, null, null);
        LogUtills.i("Sql:" + _cursor.getCount());
        if (_cursor.moveToNext()) {
            _info.symbolID = _cursor.getInt(_cursor.getColumnIndex("symbolID"));
            _info.symbolSizeX = _cursor.getDouble(_cursor.getColumnIndex("scaleX"));
            _info.symbolSizeY = _cursor.getDouble(_cursor.getColumnIndex("scaleY"));
        }

//        _info.depth = getDepth();
     /*   _info.startDirDepth = a;
        _info.endDirDepth = a;*/
//        LogUtills.i("point complete generateBaseFieldInfo.....");
        return _info;
    }

    private void saveTemplet() {
        PipePointInfo.getPipePointInfo().setGPId(getGPId());
        PipePointInfo.getPipePointInfo().setSituation(getSituation());
        PipePointInfo.getPipePointInfo().setFeaturePoints(getFeaturePoints());
        PipePointInfo.getPipePointInfo().setAppendant(getAppendant());
        PipePointInfo.getPipePointInfo().setWellSize(getWellSize());
        PipePointInfo.getPipePointInfo().setWellDepth(getWellDepth());
        PipePointInfo.getPipePointInfo().setWellWater(getWellWater());
        PipePointInfo.getPipePointInfo().setWellMud(getWellMud());
        PipePointInfo.getPipePointInfo().setWellLidTexture(getWellLidTexture());
        PipePointInfo.getPipePointInfo().setWellLidSize(getWellLidSize());
        PipePointInfo.getPipePointInfo().setState(getState());
        PipePointInfo.getPipePointInfo().setElevation(getElevation());
        PipePointInfo.getPipePointInfo().setOffset(getOffset());
        PipePointInfo.getPipePointInfo().setBuildingStructures(getBuildingStructures());
        PipePointInfo.getPipePointInfo().setRoadName(getRoadName());
        PipePointInfo.getPipePointInfo().setPointRemark(getPointRemark());
        PipePointInfo.getPipePointInfo().setPuzzle(getPuzzle());
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
        String _temp = edtDepth.getText().toString();

        if (_temp.length() > 0) {
            int s = Integer.parseInt(_temp);
            DecimalFormat df2 = new DecimalFormat("###.00");
//        LogUtills.i("终点埋深", df2.format(s / 100d));
            if (_temp.length() > 2) {
//                LogUtills.i("终点埋深", df2.format(s / 100d));
                return df2.format(s / 100d);
            } else {
//                LogUtills.i("终点埋深", "0" + df2.format(s / 100d));
                return "0" + df2.format(s / 100d);
            }
        } else
            return "0";
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        MyAlertDialog.showAlertDialog(AddPointToLineActivity.this, "删除提示", "确定删除改照片？", "确定", "取消", true,
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


    //手机拍照回调
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO:
                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        picBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
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
                                _map.put("remark", pictureName);
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


    //刷新图片区域gridview
    private void refreshGridviewAdapter() {
        simpleAdapter = new SimpleAdapter(this, imageItem,
                R.layout.layout_griditem_addpic, new String[]{"itemImage", "remark"}, new int[]{R.id.imageView1, R.id.tvPicName});
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
                    TextView tv = (TextView) view;
                    tv.setText(textRepresentation);
                    return true;
                }

                return false;
            }
        });
        runOnUiThread(new Runnable() {//主线程绑定adapter刷新数据
            @Override
            public void run() {
                grdPicCon.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
            }
        });
    }

    //初始化拍照区域
    private void initTakePicArea() {
        picFiles = new ArrayList<>();
        picNames = new ArrayList<>();
        imageItem = new ArrayList<HashMap<String, Object>>();
//        grdPicCon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                viewPicture(position);
//            }
//        });
//        grdPicCon.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                MyAlertDialog.showAlertDialog(AddPointToLineActivity.this, "删除提示", "确定删除改照片？", "确定", "取消", true,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                FileUtils.getInstance().deleteFile(picFiles.get(position)); //从手机卡删除
//                                imageItem.remove(position);
//                                picNames.remove(position);
//                                picFiles.remove(position);
//                                refreshGridviewAdapter();
//                            }
//                        }, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                return true;
//            }
//        });
    }


    //查看图片
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
//            LogUtills.i("变化之前 =", String.valueOf(s));
            String _temp = edtElevation.getText().toString();
            if (_temp.length() == 0) {
                _temp = "0";
            }

            double str = Double.parseDouble(_temp);

            edtElevation.setText(String.format("%.3f", str));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            LogUtills.i("变化之中 =", String.valueOf(s));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
//


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_watcher = null;
    }
}
