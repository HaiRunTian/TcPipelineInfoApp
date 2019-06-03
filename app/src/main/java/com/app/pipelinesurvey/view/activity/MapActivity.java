package com.app.pipelinesurvey.view.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.BaseInfo.Data.MAPACTIONTYPE2;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.BaseInfo.Oper.OperNotifyer;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.LayersSimpleArraryAdapter;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.location.BaiDuGPS;
import com.app.pipelinesurvey.location.BaseGPS;
import com.app.pipelinesurvey.location.GpsUtils;
import com.app.pipelinesurvey.location.NavigationPanelView;
import com.app.pipelinesurvey.utils.AssetsUtils;
import com.app.pipelinesurvey.utils.ExportDataUtils;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.LicenseUtils;
import com.app.pipelinesurvey.utils.PermissionUtils;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.fragment.map.DistanceMeasureFragment;
import com.app.pipelinesurvey.view.fragment.map.MeasuredPointFragment;
import com.app.pipelinesurvey.view.fragment.map.QueryPointLocalFragment;
import com.app.pipelinesurvey.view.fragment.map.WorkCountFragment;
import com.app.utills.LogUtills;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Environment;
import com.supermap.data.GeoCoordSys;
import com.supermap.data.Maps;
import com.supermap.data.Point2D;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Recordset;
import com.supermap.data.Size2D;
import com.supermap.data.SymbolLineLibrary;
import com.supermap.data.SymbolMarkerLibrary;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.Action;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapParameterChangedListener;
import com.supermap.mapping.MapView;
import com.supermap.mapping.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author HaiRun
 * @date 2018-08-30 11:00.
 * 地图界面类
 */

public class MapActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private final String TAG = "MapActivity";
    /**
     * 图层选择
     */
    public static Spinner spLayers;
    /**
     * 定位
     */
    private ImageButton imgBtnLocation;

    /**
     * 放大
     */
    private ImageButton imgBtnZoomIn;
    /**
     * 缩小
     */
    private ImageButton imgBtnZoomOut;
    /**
     * 详细面板
     */
    private LinearLayout layoutContainer;
    /**
     * Mapcontrol功能栏
     */
    private ScrollView layoutFuntionbar;
    /**
     * 图层填充数据
     */
    private List<String> m_data_list = new ArrayList<>();
    /**
     * 图层下拉适配
     */
    private LayersSimpleArraryAdapter m_adapter;
    /**
     * 动画
     */
    private Animation m_animation;
    private CheckBox cbFunctions;
    private RadioButton rdBtnDrag, rdBtnDrawPoint, rdBtnDrawLine, rdbtnLayers,
            rdBtnPointInLine, rdBtnMeasuredPoint, rdBtnQueryPoint, rdBtnDistanceMeasure,
            rdBtnQueryLine, rdBtnMovePoint, rdBtnCount, rdBtnMap, rdBtnImport, rdBtnExport;
    private TextView tvLabel;
    /**
     * 工作空间
     */
    private Workspace m_workspace = null;
    /**
     * 测量收点fragment
     */
    private MeasuredPointFragment m_measuredPointFragment;
    /**
     * 管点查询fragment
     */
    private QueryPointLocalFragment m_queryPointFragment;
    /**
     * 距离测量fragment
     */
    private DistanceMeasureFragment m_distanceMeasurementFragment;
    /**
     * 工作量统计fragment
     */
    private WorkCountFragment m_workCountFragment;
    private FragmentManager m_manager;
    private FragmentTransaction m_transaction;
    /**
     * 比例尺
     */
    private TextView m_mapScale;
    /**
     * 管类
     */
    private String pipeType = "";
    private MapView m_mapView = null;
    private MapControl m_mapControl = null;
    private String baseMapPath = null;
    private String m_prjId;
    /**
     * 判断是网络地图还是本地切片
     */
    private String m_type;
    /**
     * 传感器
     */
    private SensorManager m_SensorManager;
    private NavigationPanelView m_NavigationPanelView;
    private BaseGPS m_GPS = null;
    public static PrjCoordSys m_PrjCoordSys;
    //位置管理类
    private LocationManager locationManager;
    private StringBuffer m_builder;
    private float startDegree = 0f;
    private Map m_map;
    private boolean m_start = true;
    private ProgressDialog m_progress;
    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @SuppressLint("HandlerLeak")
    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    m_progress.setMessage("正在打开工作空间……请稍等");
                    break;
                case 1:

                    m_progress.setMessage("正在创建新工作空间……请稍等");
                    break;
                case 2:
                    m_progress.setMessage("工作空间打开成功");

                    m_progress.dismiss();
                    break;
                case 3:
                    m_progress.setMessage("工作空间打开失败");
                    m_progress.dismiss();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.setLicensePath(SuperMapConfig.LIC_PATH);
        Environment.setTemporaryPath(SuperMapConfig.TEMP_PATH);
        Environment.setWebCacheDirectory(SuperMapConfig.WEB_CACHE_PATH);
        Environment.initialization(this);
        setContentView(R.layout.fragment_map);

        initView();
        initData();
        //判断许可是否可用
        if (!LicenseUtils.ins().judgeLicese()) {
            LicenseUtils.ins().downLoadLicense();
        }
        //定位相关类初始化
        initLocal();
        //打开或者创建工作空间
        createOrOpenWorkspace2();
        LogUtills.i(TAG, "onCteate()=" + this.toString());
    }


    /**
     * 地图切换到上次加点的位置
     */
    private void initPointMap() {
        DatasetVector _ds = (DatasetVector) DataHandlerObserver.ins().getTotalPtLayer().getDataset();
        Recordset _reSet = _ds.getRecordset(false, CursorType.STATIC);
        if (!_reSet.isEmpty()) {
            _reSet.moveLast();
            double _x = _reSet.getDouble("SmX");
            double _y = _reSet.getDouble("SmY");
            Point2D _point2D = new Point2D(_x, _y);
            double[] _scale = m_map.getVisibleScales();
            m_map.setScale(_scale[_scale.length - 2]);
            m_map.setCenter(_point2D);
            m_map.refresh();
        }
    }

    /**
     * 初始化定位需要到的类和控件
     */
    private void initLocal() {
        m_builder = new StringBuffer();
        //传感器
        m_SensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //显示方向的view
        m_NavigationPanelView = new NavigationPanelView(MapActivity.this);
        m_mapView.addView(m_NavigationPanelView);
        m_NavigationPanelView.setVisibility(View.GONE);
        //位置管理类
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //位置监听类
//        m_localtion = new MyLocationListener(this, m_mapControl, m_NavigationPanelView);
    }


    /**
     * 新建或者打开工作空间
     */
    private void createOrOpenWorkspace2() {
        m_progress.show();
        //创建工作空间，数据源
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    m_workspace = new Workspace();
                    //工作空间别名
                    m_workspace.setCaption(m_prjId);
                    m_workspace.setDescription(m_prjId);
                    //工作空间连接类
                    WorkspaceConnectionInfo _wkInfo = m_workspace.getConnectionInfo();
                    _wkInfo.setServer(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId + "/" + m_prjId + ".smwu");
                    _wkInfo.setName(m_prjId);
                    _wkInfo.setType(WorkspaceType.SMWU);
                    boolean wkExist = FileUtils.getInstance().isFileExsit(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId + "/" + m_prjId + ".smwu");

                    if (wkExist) {
                        if (!m_workspace.open(_wkInfo)) {
                            ToastUtil.showShort(MapActivity.this, "workspace open fail");
                            return;
                        }
                    } else {
                        //创建工作空间
                        if (!m_workspace.save()) {
                            ToastUtil.showShort(MapActivity.this, "workspace cteate fail");
                            return;
                        }
                    }
                    m_map = m_mapControl.getMap();
                    m_map.setWorkspace(m_workspace);
                    Maps _maps = m_workspace.getMaps();
                    //判断工作空间是否存在 存在则打开地图
                    if (wkExist) {
                        //判断数据源是否打开，否则打开数据源
                        m_handler.sendEmptyMessage(0);
                        for (int i = 0; i < m_workspace.getDatasources().getCount(); i++) {
                            if (!m_workspace.getDatasources().get(i).isOpened()) {
                                m_workspace.getDatasources().open(m_workspace.getDatasources().get(i).getConnectionInfo());
                            }
                        }
                        //打开地图
                        if (_maps.getCount() == 1) {
                            if (!m_map.open(_maps.get(0))) {
                                LogUtills.i("地图打开失败");
                            }
                        }
                        m_map.setOverlapDisplayed(true);
                        //如果原来的工作空间是在线地图，则打开定位
                        if (m_workspace.getDatasources().get(1).getAlias().equals("google")) {
                            m_PrjCoordSys = m_workspace.getDatasources().get(1).getDatasets().get(0).getPrjCoordSys();
                            //百度地图定位初始化
                            initGps();
                        }


                    } else {
                        //工作空间不存在，开始新建
                        m_handler.sendEmptyMessage(1);
                        //新建工作空间，导入新点、线符号库，可以放在Assest
                        SymbolMarkerLibrary _pointLibrary = m_workspace.getResources().getMarkerLibrary();
                        SymbolLineLibrary _lineLibrary = m_workspace.getResources().getLineLibrary();
                        //从文件夹中导入点线符号库到资源库中
                        if (configSymbol(SuperMapConfig.DEFAULT_DATA_SYMBOL_NAME) && configSymbol(SuperMapConfig.DEFAULT_DATA_SYMBOL_LINE_NAME)) {
                            if (!_pointLibrary.fromFile(SuperMapConfig.DEFAULT_DATA_SYMBOL_PATH + SuperMapConfig.DEFAULT_DATA_SYMBOL_NAME)) {
                                LogUtills.i("point11", "点符号库导入失败");
                            }
                            if (!_lineLibrary.fromFile(SuperMapConfig.DEFAULT_DATA_SYMBOL_PATH + SuperMapConfig.DEFAULT_DATA_SYMBOL_LINE_NAME)) {
                                LogUtills.i("point111", "线符号库导入失败");
                            }
                        }

                        //数据源的集合类
                        Datasources _ds = m_workspace.getDatasources();
                        //数据源连接信息类
                        DatasourceConnectionInfo _dsConInfo = new DatasourceConnectionInfo();
                        //数据源别名
                        _dsConInfo.setAlias(m_prjId);
                        _dsConInfo.setReadOnly(false);
                        _dsConInfo.setEngineType(EngineType.UDB);
                        _dsConInfo.setServer(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId + "/" + m_prjId + ".udb");
                        //创建数据源
                        Datasource _datasource = _ds.create(_dsConInfo);
                        m_workspace.save();
                        //打开地图sci缓存切片
                        DatasourceConnectionInfo info = new DatasourceConnectionInfo();
                        // 设置Server
                        if ("google".equals(m_type)) {
                            info.setServer(baseMapPath);
                            info.setEngineType(EngineType.GoogleMaps);
                            info.setAlias("google");
                        } else {
                            info.setServer(baseMapPath);
                            //设置数据源连接的引擎类型为REST 地图服务引擎类型
                            info.setEngineType(EngineType.IMAGEPLUGINS);
                            info.setAlias(m_prjId + "底图");
                        }
                        //打开数据源
                        info.setReadOnly(true);
                        Datasource ds = _ds.open(info);
                        m_workspace.save();
                        //待改进？ //获取对应数据集添加进
                        if ("google".equals(m_type)) {
                            m_map.getLayers().add(ds.getDatasets().get(0), true);
                            m_map.getLayers().add(ds.getDatasets().get(1), true);
                            m_PrjCoordSys = ds.getDatasets().get(0).getPrjCoordSys();
                            GeoCoordSys _geoCoordSys = m_PrjCoordSys.getGeoCoordSys();
                            LogUtills.i("地理坐标系 " + _geoCoordSys.getName() + " type = " + _geoCoordSys.getType() + "=====" + m_PrjCoordSys.getName() + "--" + m_PrjCoordSys.getType());
                        } else {
                            m_map.getLayers().add(ds.getDatasets().get(0), true);
                            ds.getDatasets().get(0).setPrjCoordSys(new PrjCoordSys(PrjCoordSysType.PCS_USER_DEFINED));
                        }
                        WorkSpaceUtils.getInstance().setWorkSpace(m_workspace);
                        WorkSpaceUtils.getInstance().setMapControl(m_mapControl);
                        //创建数据集 生成专题图 加载到地图上 比较耗时
                        OperNotifyer.AddSystemLayers(_datasource);
                        //压盖设置
//                        MapOverlapDisplayedOptions _options = m_map.getOverlapDisplayedOptions();
//                        _options.setAllowPointOverlap(false);
//                        _options.setAllowPointWithTextDisplay(true);
//                        _options.setAllowTextAndPointOverlap(true);
//                        _options.setAllowTextOverlap(true);
//                        _options.setAllowThemeGraduatedSymbolOverlap(true);
//                        _options.setAllowThemeGraphOverlap(false);
//                        _options.setOverlappedSpaceSize(new Size2D(5.0,5.0));
//                        m_map.setOverlapDisplayedOptions(_options);
                        m_map.setOverlapDisplayed(true);
                        m_map.save(m_prjId);

                        if ("google".equals(m_type)) {
                            initGps();
                        }

                    }

                    if (wkExist) {
                        WorkSpaceUtils.getInstance().setWorkSpace(m_workspace);
                        WorkSpaceUtils.getInstance().setMapControl(m_mapControl);
                        m_map.save();
                    }
                    //设置工作空间名字
                    SuperMapConfig.setWorkspaceName(m_prjId);
                    if (DataHandlerObserver.ins() != null) {
                        DataHandlerObserver.ins("type").SetContext(MapActivity.this);
                    } else {
                        DataHandlerObserver.ins().SetContext(MapActivity.this);
                    }
                    m_mapControl.setAction(Action.PAN);
                    //保存工作空间
                    if (!m_workspace.save()) {
                        return;
                    }
                    //地图定位到上次最后加点的位置
                    initPointMap();
                    //比例尺更新
                    setMapParamChangge();

                    m_handler.sendEmptyMessage(2);

                } catch (Exception e) {
                    LogUtills.e(TAG, e.getMessage());
                    m_handler.sendEmptyMessage(3);
                }
            }
        }).start();
    }

    private void initGps() {
        //百度地图定位初始化
        m_GPS = new BaiDuGPS(this);
        m_GPS.setNavigationPanel(m_NavigationPanelView);
        m_GPS.setPrjCoordSys(m_PrjCoordSys);
        m_GPS.setMap(m_mapControl);
        m_SensorManager.registerListener(m_GPS.getSensorListener(),
                m_SensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);


    }


    /**
     * 地图比例尺
     */
    private void setMapParamChangge() {
        m_mapControl.setMapParamChangedListener(new MapParameterChangedListener() {
            @Override
            public void scaleChanged(double v) {
                if (m_map != null) {
                    float _m = (float) (1.0 / v);
                    m_mapScale.setText("比例尺 1 : " + String.format("%.2f", _m));
                }
            }

            @Override
            public void boundsChanged(Point2D point2D) {

            }

            @Override
            public void angleChanged(double v) {

            }

            @Override
            public void sizeChanged(int i, int i1) {

            }
        });
    }

    /**
     * 查询数据库 管类加载
     */
    private void initData() {
        //图层显示
//        String[] layers = getResources().getStringArray(R.array.pipeType);
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_TYPE);
        while (_cursor.moveToNext()) {
            String pipeType = _cursor.getString(_cursor.getColumnIndex("pipe_type"));
            m_data_list.add(pipeType);
        }
        m_adapter = new LayersSimpleArraryAdapter(MapActivity.this, R.layout.spinner_item_style, m_data_list);
        m_adapter.setDropDownViewResource(R.layout.spinner_item_dropdown_style);
        spLayers.setAdapter(m_adapter);
        spLayers.setSelection(0, true);

        //上个activity传过来的数据
        Intent _intent = getIntent();
        m_prjId = _intent.getStringExtra("prjName");
        m_type = _intent.getStringExtra("type");
        //当前项目名称
        SuperMapConfig.PROJECT_NAME = m_prjId;
        Cursor _cursor1 = DatabaseHelpler.getInstance()
                .query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + m_prjId + "'");
        while (_cursor1.moveToNext()) {
            baseMapPath = _cursor1.getString(_cursor1.getColumnIndex("BaseMapPath"));

            //当前城市标准名称
            String city= _cursor1.getString(_cursor1.getColumnIndex("City"));
            SuperMapConfig.PROJECT_CITY_NAME = city;
        }
        //如果创建的项目不是谷歌在线地图，地图图层切换按钮就隐藏
        if (!baseMapPath.equals("http://www.google.cn/maps")) {
            rdBtnMap.setVisibility(View.GONE);
            imgBtnLocation.setVisibility(View.GONE);

        }else {
            rdBtnMap.setVisibility(View.VISIBLE);
            imgBtnLocation.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        try {
            m_mapView = (MapView) $(R.id.mapView);
            m_mapControl = m_mapView.getMapControl();
            spLayers = $(R.id.spLayers);
            spLayers.setOnItemSelectedListener(this);
            imgBtnLocation = $(R.id.imgBtnLocation);
            imgBtnLocation.setOnClickListener(this);
            imgBtnZoomIn = $(R.id.imgBtnZoomIn);
            imgBtnZoomIn.setOnClickListener(this);
            imgBtnZoomOut = $(R.id.imgBtnZoomOut);
            imgBtnZoomOut.setOnClickListener(this);
            layoutContainer = $(R.id.layoutMapContainer);
            cbFunctions = $(R.id.cbFunctions);
            cbFunctions.setOnClickListener(this);
            layoutFuntionbar = $(R.id.layoutFuntionbar);
            m_mapScale = $(R.id.txMapScale);
            tvLabel = $(R.id.tvLabel);
            rdBtnDrag = $(R.id.rdBtnDrag);
            rdBtnDrag.setOnClickListener(this);
            rdBtnDrawPoint = $(R.id.rdBtnDrawPoint);
            rdBtnDrawPoint.setOnClickListener(this);
            rdBtnDrawLine = $(R.id.rdBtnDrawLine);
            rdBtnDrawLine.setOnClickListener(this);
            rdBtnPointInLine = $(R.id.rdBtnPointInLine);
            rdBtnPointInLine.setOnClickListener(this);
            rdBtnMeasuredPoint = $(R.id.rdBtnMeasuredPoint);
            rdBtnMeasuredPoint.setOnClickListener(this);
            rdBtnDistanceMeasure = $(R.id.rdBtnDistanceMeasure);
            rdBtnDistanceMeasure.setOnClickListener(this);
            rdBtnQueryPoint = $(R.id.rdBtnQueryPoint);
            rdBtnQueryPoint.setOnClickListener(this);
            rdbtnLayers = $(R.id.rdBtnLayers);
            rdbtnLayers.setOnClickListener(this);
            rdBtnQueryLine = $(R.id.rdBtnQueryLine);
            rdBtnQueryLine.setOnClickListener(this);
            rdBtnMovePoint = $(R.id.rdBtnMovePoint);
            rdBtnMovePoint.setOnClickListener(this);
            rdBtnCount = $(R.id.rdBtnCount);
            rdBtnCount.setOnClickListener(this);
            rdBtnMap = $(R.id.rdBtnMap);
            rdBtnMap.setOnClickListener(this);
            rdBtnImport = $(R.id.rdBtnImport);
            rdBtnExport = $(R.id.rdBtnExport);
            rdBtnExport.setOnClickListener(this);
            rdBtnImport.setOnClickListener(this);
            m_progress = new ProgressDialog(this);
            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress.setCancelable(false);
            m_progress.setCanceledOnTouchOutside(false);
            m_progress.setIcon(R.mipmap.ic_logo);
            m_progress.setTitle("工作空间");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        //按钮文本
        String buttonText = "";
        //标签文本
        String labelText = "";
        //判断是否属于radiobutton
        if (v instanceof RadioButton) {
            //当前激活按钮文本
            buttonText = ((RadioButton) v).getText().toString();
        }
        //当前标签文本
        labelText = tvLabel.getText().toString();
        switch (v.getId()) {
            //导入数据
            case R.id.rdBtnImport:
                ///
//                SymbolDialogFragment _dialogFragment = new SymbolDialogFragment();
//                _dialogFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");

                startActivityForResult(new Intent(MapActivity.this, SelectExcelActivity.class), 1);
                break;

            //导出数据
            case R.id.rdBtnExport:
                new ExportDataUtils(MapActivity.this, m_workspace, m_prjId).exportData();

                break;

            //定位
            case R.id.imgBtnLocation:

                if (!GpsUtils.isOPen(MapActivity.this)){
                    ToastUtil.showShort(MapActivity.this,"请先打开GPS定位功能");
                    GpsUtils.openGPS(MapActivity.this);
                    return;
                }

                if (m_GPS != null && m_NavigationPanelView.getVisibility() != View.VISIBLE) {
                    m_GPS.onStart();
                    LogUtills.i("定位", "定位打开");
                } else {

                    m_GPS.onStop();
                    LogUtills.i("定位", "定位关闭");
                }
                break;
            case R.id.imgBtnZoomIn:
                m_map.zoom(1.5);
                DataHandlerObserver.ins().setAction(Action.PAN);

                break;
            case R.id.imgBtnZoomOut:
                m_map.zoom(0.5);
                DataHandlerObserver.ins().setAction(Action.PAN);

                break;
            case R.id.rdBtnDrag:
                hideBottomPanel();
                DataHandlerObserver.ins().setAction(Action.PAN);

                break;

            //加点
            case R.id.rdBtnDrawPoint:
                hideBottomPanel();
                if (DataHandlerObserver.ins().setAction(Action.PAN)) {

                    DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_CreatePoint);
                    if (m_mapControl != null) {
                        DataHandlerObserver.ins().setCurrentPipeType(spLayers.getSelectedItem().toString());
                    }
//                    return;
                }
                break;
            //加线
            case R.id.rdBtnDrawLine:
                hideBottomPanel();
                if (DataHandlerObserver.ins().setAction(Action.PAN)) {
                    DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);

                }

                break;
            //线中加点
            case R.id.rdBtnPointInLine:
                DataHandlerObserver.ins().setAction(Action.PAN);
                hideBottomPanel();
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_AddPointInLine_FindLine);
                break;
            //测量收点
            case R.id.rdBtnMeasuredPoint:
                DataHandlerObserver.ins().setAction(Action.PAN);
                if (buttonText.equals(labelText)) {
                    toggleBottomPanel();
                } else {
                    DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_MeasurePoint);
                    showBottomPanel();
                    switchFragment(3);
                }
                break;

            //测量距离
            case R.id.rdBtnDistanceMeasure:
                if (buttonText.equals(labelText)) {
                    toggleBottomPanel();
                    DataHandlerObserver.ins().setAction(Action.PAN);
                } else {
                    showBottomPanel();
                    switchFragment(5);
                    DataHandlerObserver.ins().setAction(Action.MEASURELENGTH);
                    DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_MeasereDistance);
                }
                break;

            //管点定位
            case R.id.rdBtnQueryPoint:
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_None);
                DataHandlerObserver.ins().setAction(Action.PAN);
                if (buttonText.equals(labelText)) {
                    toggleBottomPanel();
                } else {
                    showBottomPanel();
                    switchFragment(4);
                }
                break;

            //图层
            case R.id.rdBtnLayers:
                DataHandlerObserver.ins().setAction(Action.PAN);
                startActivity(new Intent(MapActivity.this, LayersActivity.class));
                hideBottomPanel();
                break;

            //编辑管线
            case R.id.rdBtnQueryLine:
                hideBottomPanel();
                break;

            //移动管点
            case R.id.rdBtnMovePoint:
                m_mapControl.setAction(Action.PAN);
                hideBottomPanel();
                break;

            case R.id.cbFunctions:
                DataHandlerObserver.ins().setAction(Action.PAN);
                setFuntionPanelVisibility();
                hideBottomPanel();
                break;

            //工作量统计
            case R.id.rdBtnCount:
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_None);
                DataHandlerObserver.ins().setAction(Action.PAN);
                if (buttonText.equals(labelText)) {
                    toggleBottomPanel();
                } else {
                    showBottomPanel();
                    switchFragment(7);
                }
                break;

            case R.id.rdBtnMap: {
                int _count = m_map.getLayers().getCount();
                if (baseMapPath.equals("http://www.google.cn/maps")) {
                    Layer _layer = m_map.getLayers().get(_count - 2);
                    if (_layer.isVisible()) {
                        _layer.setVisible(false);
                    } else {
                        _layer.setVisible(true);
                    }
                    m_map.refresh();
                }
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_None);
                DataHandlerObserver.ins().setAction(Action.PAN);
            }
            break;

            default:
                break;
        }

        tvLabel.setText(buttonText);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pipeType = spLayers.getSelectedItem().toString();
        //改变图层信息配置

        if (m_start == true) {
            m_start = false;
            return;
        }
        DataHandlerObserver.ins().setCurrentPipeType(spLayers.getSelectedItem().toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //控制底部面板显示
    private void showBottomPanel() {
        if (layoutContainer.getVisibility() == View.GONE) {
            layoutContainer.setVisibility(View.VISIBLE);
            m_animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.translate_bottom_in);
            layoutContainer.startAnimation(m_animation);
        }
    }

    //隐藏底部面板
    private void hideBottomPanel() {
        if (layoutContainer.getVisibility() == View.VISIBLE) {
            m_animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.translate_bottom_out);
            layoutContainer.startAnimation(m_animation);
            layoutContainer.setVisibility(View.GONE);
        }
    }

    //切换底部面板显示
    private void toggleBottomPanel() {
        if (layoutContainer.getVisibility() == View.VISIBLE) {
            m_animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.translate_bottom_out);
            layoutContainer.startAnimation(m_animation);
            layoutContainer.setVisibility(View.GONE);
            m_mapControl.setAction(Action.PAN);
        } else {
            layoutContainer.setVisibility(View.VISIBLE);
            m_animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.translate_bottom_in);
            layoutContainer.startAnimation(m_animation);
        }
    }

    //控制功能面板的显示
    private void setFuntionPanelVisibility() {
        if (layoutFuntionbar.getVisibility() == View.GONE) {
            layoutFuntionbar.setVisibility(View.VISIBLE);
            m_animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.alpha_autotv_show);
            layoutFuntionbar.startAnimation(m_animation);
        } else {
            m_animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.alpha_autotv_disapper);
            layoutFuntionbar.startAnimation(m_animation);
            layoutFuntionbar.setVisibility(View.GONE);
        }
    }

    /**
     * 切换fragment
     *
     * @param id fragment名
     * @return null
     * @datetime 2018-05-16  15:24.
     */
    public void switchFragment(int id) {
        if (m_manager == null) {
            m_manager = MapActivity.this.getSupportFragmentManager();
        }
        m_transaction = m_manager.beginTransaction();
        //隐藏fragemnt
        hideFragment(m_transaction);
        showFragment(id);
        m_transaction.commit();
    }

    /**
     * 显示对应的fragment
     *
     * @param id fragment名
     * @return null
     * @datetime 2018-05-16  15:25.
     */
    private void showFragment(int id) {
        switch (id) {


            //显示测量收点
            case 3:
                if (m_measuredPointFragment == null) {
                    m_measuredPointFragment = new MeasuredPointFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_measuredPointFragment);
                } else {
                    m_transaction.show(m_measuredPointFragment);
                }
                break;
            //显示查询管点
            case 4:
                if (m_queryPointFragment == null) {
                    m_queryPointFragment = new QueryPointLocalFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_queryPointFragment);
                } else {
                    m_transaction.show(m_queryPointFragment);
                }
                break;
            //显示距离测量
            case 5:
                if (m_distanceMeasurementFragment == null) {
                    m_distanceMeasurementFragment = new DistanceMeasureFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_distanceMeasurementFragment);
                } else {
                    m_transaction.show(m_distanceMeasurementFragment);
                }
                break;

            //显示工作量统计
            case 7:
                if (m_workCountFragment == null) {
                    m_workCountFragment = new WorkCountFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_workCountFragment);
                } else {
                    m_transaction.show(m_workCountFragment);
                }
                break;
            case 8: {

                break;
            }
            default:
                break;
        }
    }

    /**
     * 隐藏所有fragment
     *
     * @param transaction fragment事务
     * @return null
     * @datetime 2018-05-16  15:25.
     */
    private void hideFragment(FragmentTransaction transaction) {

        if (m_measuredPointFragment != null) {
            transaction.hide(m_measuredPointFragment);
        }

        if (m_queryPointFragment != null) {
            transaction.hide(m_queryPointFragment);
        }


        if (m_distanceMeasurementFragment != null) {
            transaction.hide(m_distanceMeasurementFragment);
        }


        if (m_workCountFragment != null) {
            transaction.hide(m_workCountFragment);
        }

    }

    /**
     * 从Assets里copy符号库复制到手机文件夹中
     */
    private boolean configSymbol(String symbolName) {
        String _file = SuperMapConfig.DEFAULT_DATA_SYMBOL_PATH + symbolName;
        File _symbol = new File(_file);
        if (!_symbol.exists()) {
            InputStream is = AssetsUtils.getInstance().open(symbolName);
            if (is != null) {
                return FileUtils.getInstance().copy(is, _file);
            }
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogUtills.i(TAG, "onStart()" + this.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtills.i(TAG, "onResume()" + this.toString());

    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtills.i(TAG, "onPause()" + this.toString());
        //保存工作空间 防止闪退而忘记保存onde
        try {
            if (m_map.isModified() || m_workspace.isModified()) {
                m_map.save();
                m_workspace.save();
                LogUtills.e(TAG, "onPause() = save ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtills.i(TAG, "onRestart()" + this.toString());
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (m_GPS != null) {
            m_SensorManager.unregisterListener(m_GPS.getSensorListener());
            m_GPS.onStop();
        }
        LogUtills.i(TAG, "onStop()" + this.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (m_map.isModified() || m_workspace.isModified()) {
                m_map.save();
                m_workspace.save();
                LogUtills.i(TAG, "onDestroy = save()");
            }
        } catch (Exception e) {
            LogUtills.i(TAG, "onDestroy" + e.getMessage());

        } finally {
            m_map.close();
            m_map.dispose();
            m_mapControl.dispose();
            m_mapControl = null;
            m_workspace.close();
            m_workspace.dispose();
        }
        LogUtills.i(TAG, "onDestroy()" + this.toString());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case RESULT_OK:
                    String _filePath = data.getStringExtra("filePath");
                    String _fileName = data.getStringExtra("fileName");
                    new ExportDataUtils(this, m_workspace, m_prjId).importData(new File(_filePath));
                    break;
                default:
                    break;
            }
        }
    }




/*    @Override
    public void onBackPressed() {
        LogUtills.e(TAG, "onBackPressed() ");
        super.onBackPressed();
//        try {
//            if (m_map.isModified() || m_workspace.isModified()) {
//                m_map.save();
//                m_workspace.save();
//                LogUtills.e(TAG, "onBackPressed = save()");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtills.e(TAG, "onBackPressed =" + e.getMessage());
//        }
//        if (m_GPS != null) {
//            m_SensorManager.unregisterListener(m_GPS.getSensorListener());
//        }
//        setResult(1);
//        finish();
    }*/

}
