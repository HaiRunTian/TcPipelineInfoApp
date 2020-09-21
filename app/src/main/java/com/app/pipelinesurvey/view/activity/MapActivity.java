package com.app.pipelinesurvey.view.activity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.mbms.FileInfo;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.app.BaseInfo.Data.MAPACTIONTYPE2;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.BaseInfo.Oper.OperNotifyer;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.LayersSimpleArraryAdapter;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.bean.ItemInfo;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.location.BaiDuGPS;
import com.app.pipelinesurvey.location.BaseGPS;
import com.app.pipelinesurvey.location.GpsUtils;
import com.app.pipelinesurvey.location.NavigationPanelView;
import com.app.pipelinesurvey.utils.AlertDialogUtil;
import com.app.pipelinesurvey.utils.AssetsUtils;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.ImportDataProgressUtil;
import com.app.pipelinesurvey.utils.ThreadUtils;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.fragment.map.ExportDataFragment;
import com.app.pipelinesurvey.view.fragment.map.LayerSettingFragment;
import com.app.pipelinesurvey.view.fragment.map.MapSettingFragment;
import com.app.pipelinesurvey.view.fragment.map.mapbottom.MeasureAngleFragment;
import com.app.pipelinesurvey.view.fragment.map.mapbottom.MeasureAreaFragment;
import com.app.pipelinesurvey.view.fragment.map.mapbottom.MeasureDisFragment;
import com.app.pipelinesurvey.view.fragment.map.mapbottom.MeasureXYFragment;
import com.app.pipelinesurvey.view.fragment.map.mapbottom.MeasuredPointFragment;
import com.app.pipelinesurvey.view.fragment.map.mapbottom.QueryPointLocalFragment;
import com.app.pipelinesurvey.view.fragment.map.mapbottom.WorkCountFragment;
import com.app.utills.LogUtills;
import com.example.zhouwei.library.CustomPopWindow;
import com.supermap.data.Color;
import com.supermap.data.CursorType;
import com.supermap.data.DataConversion;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Environment;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.GeoCoordSys;
import com.supermap.data.Maps;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Recordset;
import com.supermap.data.SymbolLineLibrary;
import com.supermap.data.SymbolMarkerLibrary;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.Action;
import com.supermap.mapping.CallOut;
import com.supermap.mapping.CalloutAlignment;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingImage;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapParameterChangedListener;
import com.supermap.mapping.MapView;
import com.supermap.mapping.ScaleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HaiRun
 * @date 2018-08-30 11:00.
 * 地图界面类
 */

public class MapActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, View.OnTouchListener, CompoundButton.OnCheckedChangeListener {
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
     * 详细面板
     */
    private LinearLayout layoutContainer;
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
    private MeasureDisFragment measureDisFragment;
    /**
     * 面积测量fragment
     */
    private MeasureAreaFragment measureAreaFragment;
    /**
     * 角度测量fragment
     */
    private MeasureAngleFragment measureAngleFragment;
    /**
     * 角度测量fragment
     */
    private MeasureXYFragment measureXYFragment;
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
    private float startDegree = 0f;
    private Map m_map;
    private boolean m_start = true;
    /**
     * 打开工作空间数据进度条
     */
    private ProgressDialog m_progress;
    private String m_filePath;
    private String m_fileName;
    //    private LoadingImgDialog m_loadingImgDialog;
    private int[] array = new int[2];
    private String tvLabel = "";
    private final int KEY_QUERY_POINT = 0;
    private final int KEY_MEASURE_POINT = 1;
    private final int KEY_MEASURE_DIS = 2;
    private final int KEY_MEASURE_AREA = 3;
    private final int KEY_MEASURE_ANGE = 4;
    private final int KEY_MEASURE_XY = 5;
    private final int KEY_SUM_DATA = 6;
    private List<ItemInfo> itemInfos;
    private CustomPopWindow mCustomPopWindow;
    private EditText edtX;
    private EditText edtY;
    private Button btnQuery;
    private View layoutMeausre;
    private Switch aSwitch;
    private TextView tvAction;
    private ImageButton imgMap;
    private CallOut callOut;
    private final String CALLOUT = "measurePoint";
    private ScaleView scaleView;
    private CheckBox cbAdd;
    private CheckBox cbQuery;
    private View cbCon;
    private CheckBox cbAddLine;
    private CheckBox cbQueryLine;
    private View cbConLine;
    private int status;
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
                    setLogSheet();
                    break;
                case 3:
                    ToastyUtil.showErrorLong(MapActivity.this, msg.obj.toString());
                    LogUtills.e("初始化地图失败");
                    m_progress.setMessage(msg.obj.toString());
//                    m_progress.dismiss();
                    break;
                case 4:
                    initProgress();
                    //点表  读取excel 和生成数据集比较好耗时间 需要在子线程里进行
                    ImportDataProgressUtil.ImportData(m_filePath, new ImportDataProgressUtil.ImportListener() {
                        @Override
                        public void zipStart() {
                            m_handler.sendEmptyMessage(5);
                        }

                        @Override
                        public void zipSuccess() {
                            m_handler.sendEmptyMessage(6);
                        }

                        @Override
                        public void zipProgress(int[] progress) {
                            array = progress;
                            m_handler.sendEmptyMessage(7);
                        }

                        @Override
                        public void zipFail() {
                            m_handler.sendEmptyMessage(8);
                        }
                    });

                    break;
                case 5:
                    //开始初始化数据
                    m_progress.setMessage("数据正常读取统计中……");
                    m_progress.show();
                    break;
                case 6:
                    //数据导入成功
                    m_progress.setMessage("数据导入成功！");
                    m_progress.dismiss();
                    break;
                case 7:
                    //数据统计显示
                    m_progress.setMessage("导入的点数据有" + array[0] + "条，线数据有" + array[1] + "条");
                    break;
                case 8:
                    //数据导入失 败
                    m_progress.setMessage("数据导入失败！");
                    m_progress.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 设置好当天检测记录表
     *
     * @Params
     * @author :HaiRun
     * @date :2019/6/26  15:07
     */
    private void setLogSheet() {
        try {
            MapSettingFragment fragment = new MapSettingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("prj_name", m_prjId);
            bundle.putString("type", m_type);
            fragment.setArguments(bundle);
            fragment.show(getFragmentManager(), "MapSettingFragment");
        } catch (Exception e) {
            LogUtills.e(e.toString());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化超图相关控件
        try {
            Environment.setLicensePath(SuperMapConfig.LIC_PATH);
            Environment.setTemporaryPath(SuperMapConfig.TEMP_PATH);
            Environment.setWebCacheDirectory(SuperMapConfig.WEB_CACHE_PATH);
            Environment.initialization(this);
            setContentView(R.layout.fragment_map);
            EventBus.getDefault().register(this);
            initView();
            initData();
            //定位相关类初始化
            initLocal();
            //打开或者创建工作空间
            createOrOpenWorkspace();
        } catch (Exception e) {
            LogUtills.e(e.toString());
            ToastyUtil.showErrorLong(MapActivity.this, e.toString());
        }
    }

    /**
     * 地图切换到上次加点的位置
     */
    private void initPointMap(Workspace workspace) {
        try {
            DatasetVector _ds = (DatasetVector) workspace.getDatasources().get(0).getDatasets().get(0);
            if (_ds == null) {
                return;
            }
            Recordset _reSet = _ds.getRecordset(false, CursorType.STATIC);
            if (!_reSet.isEmpty()) {
                _reSet.moveLast();
                double _x = _reSet.getDouble("longitude");
                double _y = _reSet.getDouble("latitude");
                Point2D _point2D = new Point2D(_x, _y);
                m_map.setScale(1 / 100);
                m_map.setCenter(_point2D);
                m_map.refresh();
            } else {
                m_mapControl.getMap().viewEntire();
                LogUtills.i("全图显示");
            }
        } catch (Exception e) {
            LogUtills.e(e.toString());
        }
    }

    /**
     * 初始化定位需要到的类和控件
     */
    private void initLocal() {
        try {
            //传感器
            m_SensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            //显示方向的view
            m_NavigationPanelView = new NavigationPanelView(MapActivity.this);
            m_mapView.addView(m_NavigationPanelView);
            m_NavigationPanelView.setVisibility(View.GONE);
            //位置管理类
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        } catch (Exception e) {
            LogUtills.e(e.toString());
            ToastyUtil.showErrorLong(MapActivity.this, "初始化定位失败");
        }
    }
    private void initProgress() {
        m_progress.setTitle("数据导入");
    }

    /**
     * 新建或者打开工作空间
     */
    private void createOrOpenWorkspace() {
        m_progress.show();
        //线程池加载底图
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    m_workspace = new Workspace();
                    //工作空间别名
                    m_workspace.setCaption(m_prjId);
                    //工作空间描述
                    m_workspace.setDescription(m_prjId);
                    //工作空间连接类
                    WorkspaceConnectionInfo _wkInfo = m_workspace.getConnectionInfo();
                    //设置服务地址
                    _wkInfo.setServer(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId + "/" + m_prjId + ".smwu");
                    //工作空间名字
                    _wkInfo.setName(m_prjId);
                    //工作空间类型
                    _wkInfo.setType(WorkspaceType.SMWU);
                    //检测工作空间文件是否存在
                    boolean wkExist = FileUtils.getInstance().isFileExsit(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId + "/" + m_prjId + ".smwu");
                    //status 1=打开已有工作空间   0:新建工作空间
                    Message message = new Message();
                    if (status == 1) {
                        if (!wkExist) {
//                            ToastyUtil.showErrorShort(MapActivity.this, "项目源文件丢失，是否删除了项目文件夹");
                            message.what = 3;
                            message.obj = "line#439项目源文件丢失，是否删除了项目文件";
                            m_handler.sendMessage(message);
                        }
                        //打开工作空间
                        if (!m_workspace.open(_wkInfo)) {
                            ToastyUtil.showErrorShort(MapActivity.this, "工作空间打开失败");
                            message.what = 3;
                            message.obj = "line#446工作空间打开失败";
                            m_handler.sendMessage(message);
                            return;
                        }
                        WorkSpaceUtils.getInstance().setWorkSpace(m_workspace);
                        WorkSpaceUtils.getInstance().setMapControl(m_mapControl);
//                        addDatasetFileInfo();

                    } else {
                        //创建工作空间
                        if (!m_workspace.save()) {
                            ToastyUtil.showErrorShort(MapActivity.this, "workspace cteate fail");
                            message.what = 3;
                            message.obj = "line#459工作空间创建失败";
                            m_handler.sendMessage(message);
                            return;
                        }
                    }
                    //获取地图类
                    m_map = m_mapControl.getMap();
                    //地图和工作空间关联
                    m_map.setWorkspace(m_workspace);
                    //获取地图集合类
                    Maps _maps = m_workspace.getMaps();
                    //判断工作空间是否存在 存在则打开地图
                    if (status == 1) {
                        //判断数据源是否打开，否则打开数据源
                        m_handler.sendEmptyMessage(0);
                        //遍历数据源，打开数据源
                        for (int i = 0; i < m_workspace.getDatasources().getCount(); i++) {
                            if (!m_workspace.getDatasources().get(i).isOpened()) {
                                //打开数据源
                                m_workspace.getDatasources().open(m_workspace.getDatasources().get(i).getConnectionInfo());
                            }
                        }
                        //打开地图
                        if (_maps.getCount() == 1) {
                            if (!m_map.open(_maps.get(0))) {
                                message.what = 3;
                                message.obj = "line#489地图打开失败";
                                m_handler.sendMessage(message);
                                return;
                            }
                        }
                        m_map.setOverlapDisplayed(true);
                        //如果原来的工作空间是在线地图，则打开定位
                        if (m_workspace.getDatasources().getCount() == 2 && m_workspace.getDatasources().get(1).getDatasets().getCount() == 4) {
                            m_PrjCoordSys = m_workspace.getDatasources().get(1).getDatasets().get(0).getPrjCoordSys();
                            //谷歌地图定位初始化
                            initGps();
                        }

                    } else {
                        //工作空间不存在，开始新建
                        m_handler.sendEmptyMessage(1);
                        //导入符号库
                        importSymbol();
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
                        if (_datasource == null) {
                            message.what = 3;
                            message.obj = "line#529数据源为空";
                            m_handler.sendMessage(message);
                            return;
                        }
//                        m_workspace.save();
                        openUdbSource(message, _ds, _datasource);
                        //把工作空间类设置到工具类
                        WorkSpaceUtils.getInstance().setWorkSpace(m_workspace);
                        WorkSpaceUtils.getInstance().setMapControl(m_mapControl);
                        //创建数据集 生成专题图 加载到地图上 比较耗时
                        OperNotifyer.AddSystemLayers(_datasource);
                        //压盖
                        m_map.setAntialias(true);
                        m_map.setOverlapDisplayed(true);
                        m_map.save(m_prjId);
                        if ("google".equals(m_type)) {
                            initGps();
                        }
                    }

                    //设置工作空间名字
                    SuperMapConfig.setWorkspaceName(m_prjId);
                    if (DataHandlerObserver.ins() != null) {
                        DataHandlerObserver.ins("type").setContext(MapActivity.this);
                    } else {
                        DataHandlerObserver.ins().setContext(MapActivity.this);
                    }
                    m_mapControl.setAction(Action.PAN);
                    //保存工作空间
                    if (!m_workspace.save()) {
                        message.what = 11;
                        message.obj = "line#634工作空间保存失败";
                        m_handler.sendMessage(message);
                        return;
                    }
                    //地图定位到上次最后加点的位置
                    initPointMap(m_workspace);
                    //比例尺更新
                    setMapParamChangge();
                    m_handler.sendEmptyMessage(2);
                } catch (Exception e) {
                    LogUtills.e(TAG, e.getMessage());
                    Message message = new Message();
                    message.what = 3;
                    message.obj = e.toString();
                    m_handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 线表更新三个数据集
     * @Params :
     * @author :HaiRun
     * @date   :2020-08-24  17:14
     */
    private void addDatasetFileInfo() {
        DatasetVector lineDataset = (DatasetVector) m_workspace.getDatasources().get(0).getDatasets().get("L_All");
        FieldInfos fieldInfos = lineDataset.getFieldInfos();
        if (fieldInfos.getCount() < 58){
            String[] file = new String[]{"IsDrawNoteText","NoteX","NoteY"};
            FieldInfo info = null;
            for (int i = 0; i < file.length; i++) {
                info = new FieldInfo();
                info.setName(file[i]);
                info.setCaption(file[i]);
                info.setDefaultValue(file[i]);
                fieldInfos.add(info);
                LogUtills.i("create fileinfo :" + file[i]);
            }
        }
    }

    private void importSymbol() {
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
    }

    private void openUdbSource(Message message, Datasources _ds, Datasource _datasource) throws Exception {
        //打开地图sci缓存切片
        DatasourceConnectionInfo info = new DatasourceConnectionInfo();
        //数据源
        Datasource ds = null;
        if ("google".equals(m_type)) {
            // google地图
            info.setServer(baseMapPath);
            info.setEngineType(EngineType.GoogleMaps);
            info.setAlias("底图");
            info.setReadOnly(true);
            ds = _ds.open(info);
            if (ds == null) {
                message.what = 3;
                message.obj = "line#547谷歌数据源为空";
                m_handler.sendMessage(message);
                return ;
            }
            m_map.getLayers().add(ds.getDatasets().get(0), true);
            m_map.getLayers().add(ds.getDatasets().get(1), true);
            m_PrjCoordSys = ds.getDatasets().get(0).getPrjCoordSys();
            GeoCoordSys _geoCoordSys = m_PrjCoordSys.getGeoCoordSys();
        } else {
            if (baseMapPath.endsWith("sci")) {
                //sci切片
                info.setServer(baseMapPath);
                //设置数据源连接的引擎类型为REST 地图服务引擎类型
                info.setEngineType(EngineType.IMAGEPLUGINS);
                info.setAlias("底图");
                info.setReadOnly(true);
                ds = _ds.open(info);
                if (ds == null) {
                    message.what = 3;
                    message.obj = "line#566sci数据源为空";
                    m_handler.sendMessage(message);
                    return ;
                }
                Layer layer = m_map.getLayers().add(ds.getDatasets().get(0), true);
                layer.setCaption("底图");
                ds.getDatasets().get(0).setPrjCoordSys(new PrjCoordSys(PrjCoordSysType.PCS_USER_DEFINED));
            } else {
                //dwg导入
                boolean b = DataConversion.importDWG(baseMapPath, _datasource, false);

                if (!b) {
                    m_handler.sendEmptyMessage(3);
                    return ;
                }

                //获取cad数据集
                Dataset dataset = _datasource.getDatasets().get(0);
                if (dataset == null) {
                    message.what = 3;
                    message.obj = "line#586CAD数据源为空";
                    m_handler.sendMessage(message);
                    return ;
                }
                //设置投影坐标系
                dataset.setPrjCoordSys(new PrjCoordSys(PrjCoordSysType.PCS_USER_DEFINED));
                //图层
                Layer layer = m_map.getLayers().add(dataset, false);
                if (layer == null) {
                    message.what = 3;
                    message.obj = "line#597CAD图层为空";
                    m_handler.sendMessage(message);
                    return ;
                }
                //图层设置主题名
                layer.setCaption("底图");
                m_mapControl.getMap().viewEntire();
            }
        }
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
     * 设置地图比例尺
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
        try {
            //上个activity传过来的数据
            Intent _intent = getIntent();
            //项目名称
            m_prjId = _intent.getStringExtra("prjName");
            //类型 是谷歌在线地图还是切片
            m_type = _intent.getStringExtra("type");
            //状态 0 = 新建   1= 打开已建项目
            status = _intent.getIntExtra("status", 0);
            //当前项目名称全局
            SuperMapConfig.PROJECT_NAME = m_prjId;
            //查询数据库项目表，查询地图的路径
            Cursor _cursor1 = DatabaseHelpler.getInstance()
                    .query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + m_prjId + "'");
            while (_cursor1.moveToNext()) {
                baseMapPath = _cursor1.getString(_cursor1.getColumnIndex("BaseMapPath"));
                //当前城市标准名称
            }
            _cursor1.close();
            //通过城市名称查询此城市的管类
            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PIPE_PRJ_SHOW, "where prj_name = '" + SuperMapConfig.PROJECT_NAME + "' and show = 1");
            while (_cursor.moveToNext()) {
                String pipeType = _cursor.getString(_cursor.getColumnIndex("pipetype"));
                m_data_list.add(pipeType);
                LogUtills.i("pipeType = ", pipeType);
            }
            _cursor.close();
            m_adapter = new LayersSimpleArraryAdapter(MapActivity.this, R.layout.spinner_item_style_map, m_data_list);
            m_adapter.setDropDownViewResource(R.layout.spinner_item_dropdown_style);
            spLayers.setAdapter(m_adapter);
            spLayers.setSelection(0, true);
            //如果创建的项目不是谷歌在线地图，地图图层切换按钮就隐藏
            if (!baseMapPath.equals("http://www.google.cn/maps")) {
                imgMap.setVisibility(View.GONE);
                imgBtnLocation.setVisibility(View.GONE);
            } else {
                imgMap.setVisibility(View.VISIBLE);
                imgBtnLocation.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            LogUtills.e(e.toString());
            ToastyUtil.showErrorLong(MapActivity.this, "初始化数据失败");
        }
    }


    private void initView() {
        try {
            m_mapView = (MapView) findViewById(R.id.mapView);
            m_mapControl = m_mapView.getMapControl();
            scaleView = findViewById(R.id.scaleView);
            scaleView.setMapView(m_mapView);
            //地图标注
            callOut = new CallOut(this);
            ImageView view = new ImageView(this);
            callOut.setCustomize(true);
            view.setBackgroundResource(R.drawable.ic_map_callout_32px);
            callOut.setStyle(CalloutAlignment.BOTTOM);
            callOut.setContentView(view);
            callOut.setVisibility(View.GONE);
            m_mapView.addCallout(callOut, CALLOUT);

            spLayers = findViewById(R.id.spLayers);
            spLayers.setOnItemSelectedListener(this);
            imgBtnLocation = findViewById(R.id.imgBtnLocation);
            imgBtnLocation.setOnClickListener(this);
            layoutContainer = findViewById(R.id.layoutMapContainer);
            layoutContainer.setEnabled(false);
            m_mapScale = findViewById(R.id.txMapScale);
            tvAction = findViewById(R.id.tvAction);
            imgMap = findViewById(R.id.imgMapChange);
            //测量坐标
            layoutMeausre = findViewById(R.id.layoutMeausre);
            edtX = findViewById(R.id.edtX);
            edtY = findViewById(R.id.edtY);
            btnQuery = findViewById(R.id.btnQuery);
            aSwitch = findViewById(R.id.swOpen);
            aSwitch.setOnCheckedChangeListener(this);
            btnQuery.setOnClickListener(this);
            imgMap.setOnClickListener(this);
            tvAction.setOnClickListener(this);

            m_progress = new ProgressDialog(this);
            m_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_progress.setCancelable(true);
            m_progress.setCanceledOnTouchOutside(true);
            m_progress.setIcon(R.mipmap.ic_logo);
            m_progress.setTitle("工作空间");

            cbCon = findViewById(R.id.cb_con);
            cbCon.setVisibility(View.GONE);
            cbAdd = findViewById(R.id.cb_add);
            cbQuery = findViewById(R.id.cb_query);
            cbConLine = findViewById(R.id.cb_con_line);
            cbConLine.setVisibility(View.GONE);
            cbAddLine = findViewById(R.id.cb_add_line);
            cbQueryLine = findViewById(R.id.cb_query_line);

            cbAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_AddPoint);
                        if (cbQuery.isChecked()) {
                            cbQuery.setChecked(false);
                        }
                    }
                }
            });

            cbQuery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_QueryPoint);
                        if (cbAdd.isChecked()) {
                            cbAdd.setChecked(false);
                        }
                    }
                }
            });

            cbAddLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_AddLine);
                        if (cbQueryLine.isChecked()) {
                            cbQueryLine.setChecked(false);
                        }
                    }
                }
            });

            cbQueryLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_QueryLine);
                        if (cbAddLine.isChecked()) {
                            cbAddLine.setChecked(false);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            ToastyUtil.showErrorLong(MapActivity.this, "初始化布局失败");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //定位
            case R.id.imgBtnLocation:
                hideBottomPanel();
                try {
                    if (!GpsUtils.isOPen(MapActivity.this)) {
                        ToastyUtil.showInfoShort(MapActivity.this, "请先打开GPS定位功能");
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
                } catch (Exception e) {
                    LogUtills.e(e.toString());
                }
                break;
            //地图切换
            case R.id.imgMapChange: {
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
            hideBottomPanel();
            break;
            //功能按钮
            case R.id.tvAction:
                showPopMenu();
                break;
            case R.id.btnQuery:
                String x = edtX.getText().toString();
                String y = edtY.getText().toString();
                Point2D point2D = new Point2D(Double.valueOf(x), Double.valueOf(y));
                callOut.setLocation(point2D.getX(), point2D.getY());
                m_map.setCenter(point2D);
                m_map.refresh();
                break;
            default:
                break;
        }
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

    private void showPopMenu() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu, null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .create()
                .showAsDropDown(tvAction, 0, 20);
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     */
    private void handleLogic(View contentView) {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String labelText = "";
                    String itemText = "";
                    if (mCustomPopWindow != null) {
                        mCustomPopWindow.dissmiss();
                    }
                    String showContent = "";
                    switch (v.getId()) {
                        //加点
                        case R.id.menu_point:
                            hideBottomPanel();
                            if (DataHandlerObserver.ins().setAction(Action.PAN)) {
                                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_CreatePoint);
                                if (m_mapControl != null) {
                                    DataHandlerObserver.ins().setCurrentPipeType(spLayers.getSelectedItem().toString());
                                }
                            }
                            setCbLineConVisibityGone();
                            setCbConVisibityView();
                            itemText = "点";
                            break;
                        //加线
                        case R.id.menu_line:
                            hideBottomPanel();
                            if (DataHandlerObserver.ins().setAction(Action.PAN)) {
                                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_CreateLineStartPoint);
                            }
                            setCbConVisibityGone();
                            setCbLineConVisibityView();
                            itemText = "线";
                            break;
                        //管点定位
                        case R.id.menu_query:
                            itemText = "管点查询";
                            DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_None);
                            DataHandlerObserver.ins().setAction(Action.PAN);
                            if (itemText.equals(labelText)) {
                                toggleBottomPanel();
                            } else {
                                showBottomPanel();
                                switchFragment(KEY_QUERY_POINT);
                            }
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            break;
                        //线中加点
                        case R.id.menu_line_point:
                            DataHandlerObserver.ins().setAction(Action.PAN);
                            hideBottomPanel();
                            DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_AddPointInLine_FindLine);
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            itemText = "线中加点";
                            break;
                        //测量收点
                        case R.id.menu_measure_point:
                            itemText = "测量";
                            DataHandlerObserver.ins().setAction(Action.PAN);
                            if (itemText.equals(labelText)) {
                                toggleBottomPanel();
                            } else {
                                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_MeasurePoint);
                                showBottomPanel();
                                switchFragment(KEY_MEASURE_POINT);
                            }
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            break;
                        //测量距离
                        case R.id.menu_diatance:
                            itemText = "测距";
                            if (itemText.equals(labelText)) {
                                toggleBottomPanel();
                                DataHandlerObserver.ins().setAction(Action.PAN);
                            } else {
                                showBottomPanel();
                                switchFragment(KEY_MEASURE_DIS);
                                DataHandlerObserver.ins().setAction(Action.MEASURELENGTH);
                                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_MeasereDistance);
                            }
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            break;
                        //测量坐标系
                        case R.id.menu_xy:
                            itemText = "测量坐标";
                            DataHandlerObserver.ins().setAction(Action.PAN);
                            toggleBottomPanel2();
                            if (itemText.equals(labelText)) {
                                hideBottomPanel();
                            } else {
                                showLayoutAndCallout();
                            }
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            break;
                        //工足量统计
                        case R.id.menu_count:
                            itemText = "统计";
                            DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_None);
                            DataHandlerObserver.ins().setAction(Action.PAN);
                            if (itemText.equals(labelText)) {
                                toggleBottomPanel();
                            } else {
                                showBottomPanel();
                                switchFragment(KEY_SUM_DATA);
                            }
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            break;
                        //数据导出
                        case R.id.menu_export:
                            ExportDataFragment fragment2 = new ExportDataFragment();
                            fragment2.show(getFragmentManager(), "fragment");
//                            DataHandlerObserver.ins().setAction(Action.PAN);
                            hideBottomPanel();
                            itemText = "导出";
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            break;
                        //数据导入
                        case R.id.menu_import:
                            DataHandlerObserver.ins().setAction(Action.PAN);
                            hideBottomPanel();
                            AlertDialogUtil.showDialog(MapActivity.this, "导入文件", "请选择导入的文件", false, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SuperMapConfig.FILE_PATH = SuperMapConfig.QQ_FILE_PATH;
                                    startActivityForResult(new Intent(MapActivity.this, SelectExcelActivity.class), 1);
                                    dialog.dismiss();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SuperMapConfig.FILE_PATH = SuperMapConfig.DEFAULT_DATA_PATH;
                                    startActivityForResult(new Intent(MapActivity.this, SelectExcelActivity.class), 1);
                                    dialog.dismiss();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SuperMapConfig.FILE_PATH = SuperMapConfig.WECHAT_FILE_PATH;
                                    startActivityForResult(new Intent(MapActivity.this, SelectExcelActivity.class), 1);
                                    dialog.dismiss();
                                }
                            });

                            itemText = "导入";
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            break;
                        //图层
                        case R.id.menu_layer:
                            DataHandlerObserver.ins().setAction(Action.PAN);
                            hideBottomPanel();
                            LayerSettingFragment fragment = new LayerSettingFragment();
                            fragment.show(getFragmentManager(), "layer");
                            itemText = "图层";
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            break;
                        //加入切片
                        case R.id.menu_change_type:
                            itemText = "切片";
                            AlertDialogUtil.showDialog(MapActivity.this, "导入文件", "请选择导入的文件", false, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SuperMapConfig.FILE_PATH = SuperMapConfig.QQ_FILE_PATH;
                                    startActivityForResult(new Intent(MapActivity.this, SelectBaseMapActivity.class), 2);
                                    dialog.dismiss();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SuperMapConfig.FILE_PATH = SuperMapConfig.DEFAULT_DATA_PATH;
                                    startActivityForResult(new Intent(MapActivity.this, SelectBaseMapActivity.class), 2);
                                    dialog.dismiss();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SuperMapConfig.FILE_PATH = SuperMapConfig.WECHAT_FILE_PATH;
                                    startActivityForResult(new Intent(MapActivity.this, SelectBaseMapActivity.class), 2);
                                    dialog.dismiss();
                                }
                            });
                            setCbConVisibityGone();
                            setCbLineConVisibityGone();
                            break;

                        default:
                            break;
                    }
                    labelText = itemText;
                    tvAction.setText(itemText);
                } catch (Exception e) {
                    ToastyUtil.showErrorLong(MapActivity.this, e.toString());
                    LogUtills.e(e.toString());
                }
            }
        };

        contentView.findViewById(R.id.menu_point).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_line).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_query).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_line_point).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_measure_point).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_diatance).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_xy).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_count).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_export).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_import).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_layer).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_change_type).setOnClickListener(listener);

    }

    /**
     * 设置底部view 和标注
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/17  14:04
     */
    private void showLayoutAndCallout() {
        callOut.setVisibility(View.VISIBLE);
        m_mapView.showCallOut();
        m_mapControl.setMagnifierEnabled(true);
        m_mapControl.setMagnifierCrossColor(new Color(255, 0, 0));
        m_mapControl.setMagnifierRadius(100);
        layoutMeausre.setVisibility(View.VISIBLE);
        DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE2.Action_MeasereXY);
    }

    /**
     * 控制底部面板显示
     */
    private void showBottomPanel() {
        if (layoutContainer.getVisibility() == View.GONE) {
            layoutContainer.setVisibility(View.VISIBLE);
            m_animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.translate_bottom_in);
            layoutContainer.startAnimation(m_animation);
        }

        if (layoutMeausre.getVisibility() == View.VISIBLE) {
            layoutMeausre.setVisibility(View.GONE);
            callOut.setVisibility(View.GONE);
            m_mapControl.setMagnifierEnabled(false);
            m_map.refresh();
        }
    }

    /**
     * 隐藏底部面板
     */
    private void hideBottomPanel() {
        if (layoutContainer.getVisibility() == View.VISIBLE) {
            m_animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.translate_bottom_out);
            layoutContainer.startAnimation(m_animation);
            layoutContainer.setVisibility(View.GONE);
        }
        if (layoutMeausre.getVisibility() == View.VISIBLE) {
            layoutMeausre.setVisibility(View.GONE);
            callOut.setVisibility(View.GONE);
            m_mapControl.setMagnifierEnabled(false);
            m_map.refresh();
        }
    }

    /**
     * 切换底部面板显示
     */
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

        if (layoutMeausre.getVisibility() == View.VISIBLE) {
            layoutMeausre.setVisibility(View.GONE);
            callOut.setVisibility(View.GONE);
            m_mapControl.setMagnifierEnabled(false);
            m_map.refresh();
        }

    }

    /**
     * 切换底部面板显示
     */
    private void toggleBottomPanel2() {
        if (layoutContainer.getVisibility() == View.VISIBLE) {
            m_animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.translate_bottom_out);
            layoutContainer.startAnimation(m_animation);
            layoutContainer.setVisibility(View.GONE);
            m_mapControl.setAction(Action.PAN);
        }

        if (layoutMeausre.getVisibility() == View.VISIBLE) {
            layoutMeausre.setVisibility(View.GONE);
            callOut.setVisibility(View.GONE);
            m_mapControl.setMagnifierEnabled(false);
            m_map.refresh();
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
            m_manager = getFragmentManager();
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
            //显示查询管点
            case 0:
                if (m_queryPointFragment == null) {
                    m_queryPointFragment = new QueryPointLocalFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_queryPointFragment);
                } else {
                    m_transaction.show(m_queryPointFragment);
                }
                break;
            //显示测量收点
            case 1:
                if (m_measuredPointFragment == null) {
                    m_measuredPointFragment = new MeasuredPointFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_measuredPointFragment);
                } else {
                    m_transaction.show(m_measuredPointFragment);
                }
                break;
            //显示距离测量
            case 2:
                if (measureDisFragment == null) {
                    measureDisFragment = new MeasureDisFragment();
                    m_transaction.add(R.id.layoutMapContainer, measureDisFragment);
                } else {
                    m_transaction.show(measureDisFragment);
                }
                break;
            //显示面积测量
            case 3:
                if (measureAreaFragment == null) {
                    measureAreaFragment = new MeasureAreaFragment();
                    m_transaction.add(R.id.layoutMapContainer, measureAreaFragment);
                } else {
                    m_transaction.show(measureAreaFragment);
                }
                break;
            //显示角度测量
            case 4:
                if (measureAngleFragment == null) {
                    measureAngleFragment = new MeasureAngleFragment();
                    m_transaction.add(R.id.layoutMapContainer, measureAngleFragment);
                } else {
                    m_transaction.show(measureAngleFragment);
                }
                break;
            //显示坐标系测量
            case 5:
                if (measureXYFragment == null) {
                    measureXYFragment = new MeasureXYFragment();
                    m_transaction.add(R.id.layoutMapContainer, measureXYFragment);
                } else {
                    m_transaction.show(measureXYFragment);
                }
                break;
            //显示工作量统计
            case 6:
                if (m_workCountFragment == null) {
                    m_workCountFragment = new WorkCountFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_workCountFragment);
                } else {
                    m_transaction.show(m_workCountFragment);
                }
                break;
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
        if (measureDisFragment != null) {
            transaction.hide(measureDisFragment);
        }
        if (measureAreaFragment != null) {
            transaction.hide(measureAreaFragment);
        }
        if (measureAngleFragment != null) {
            transaction.hide(measureAngleFragment);
        }
        if (measureXYFragment != null) {
            transaction.hide(measureXYFragment);
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

    /**
     * 界面闪退或者退出前，判断地图是否有修改，有修改则保存地图和工作空间
     */
    @Override
    protected void onPause() {
        super.onPause();
        LogUtills.i(TAG, "onPause()" + this.toString());
        //保存工作空间 防止闪退而忘记保存
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

        try {
            if (m_map.isModified() || m_workspace.isModified()) {
                m_map.save();
                m_workspace.save();
                LogUtills.e(TAG, "onStop() = save ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        LogUtills.i(TAG, "onStop()" + this.toString());
    }

    /**
     * 界面闪退或者退出前，判断地图是否有修改，有修改则保存地图和工作空间
     */
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
            EventBus.getDefault().unregister(this);
        }
        LogUtills.i(TAG, "onDestroy()" + this.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    m_filePath = data.getStringExtra("filePath");
                    m_fileName = data.getStringExtra("fileName");
                    m_handler.sendEmptyMessage(4);
                }
                break;
            case 2:
                //添加另外一个切片
                if (resultCode == RESULT_OK) {
                    String _filePath = data.getStringExtra("filePath");
                    String _fileName = data.getStringExtra("fileName");
                    m_handler.sendEmptyMessage(5);
                    LogUtills.i("filepath = " + _filePath + "-------_fileName" + _fileName);
                    ThreadUtils.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (_fileName.endsWith(".sci")) {
                                DatasourceConnectionInfo info = new DatasourceConnectionInfo();
                                info.setServer(_filePath);
                                //设置数据源连接的引擎类型为REST 地图服务引擎类型
                                info.setEngineType(EngineType.IMAGEPLUGINS);
                                info.setAlias("底图" + _fileName);
                                info.setReadOnly(true);
                                Datasource open = m_workspace.getDatasources().open(info);
                                if (open != null) {
                                    Layer layer = m_map.getLayers().add(open.getDatasets().get(0), false);
                                    if (layer != null) {
                                        LayerSettingImage layerSetting = (LayerSettingImage) layer.getAdditionalSetting();
                                        LogUtills.i("type = " + layerSetting.getType());
                                        layerSetting.setTransparent(true);
                                        layer.setAdditionalSetting(layerSetting);
                                        layer.setCaption("底图 2");
                                        m_map.getLayers().moveUp(m_map.getLayers().getCount() - 1);
                                        m_mapControl.getMap().refresh();
                                        LogUtills.i("add layer ok");
                                        m_handler.sendEmptyMessage(6);
                                    } else {
                                        LogUtills.i("add layer fail");
                                        m_handler.sendEmptyMessage(8);
                                    }

                                } else {
                                    LogUtills.i("add layer fail");
                                    m_handler.sendEmptyMessage(8);
                                }
                            } else {
                                try {
                                    boolean b = DataConversion.importDWG(_filePath, m_workspace.getDatasources().get(0), false);
                                    if (b) {
                                        Dataset dataset = m_workspace.getDatasources().get(0).getDatasets().get(_fileName.substring(0, _fileName.lastIndexOf(".")));
                                        if (dataset != null) {
                                            Layer layer = m_map.getLayers().add(dataset, false);
                                            if (layer != null) {
                                                layer.setCaption("底图 2");
                                                m_map.getLayers().moveUp(m_map.getLayers().getCount() - 1);
                                                m_mapControl.getMap().refresh();
                                                m_handler.sendEmptyMessage(6);
                                                LogUtills.i("add dwg  is ok");
                                            } else {
                                                LogUtills.e("aadd dwg  is fail ,layer is null");
                                                m_handler.sendEmptyMessage(8);
                                            }
                                        } else {
                                            LogUtills.e("add dwg  is fail ,dataset is null ");
                                            m_handler.sendEmptyMessage(8);
                                        }
                                    } else {
                                        LogUtills.e("import dwg failer ");
                                        m_handler.sendEmptyMessage(8);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogUtills.e(e.toString());
                                    m_handler.sendEmptyMessage(8);
                                }
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }

    }


    /**
     * 测量坐标系时的时间分发
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            //按下
            case MotionEvent.ACTION_DOWN:
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                double x = event.getX();
                double y = event.getY();
                LogUtills.i(TAG, "x = " + x + " ,y = " + y);
                Point2D point2D = m_map.pixelToMap(new Point((int) x, (int) y));
                DecimalFormat df = new DecimalFormat("0.00000");
                String x2 = df.format(point2D.getX());
                String y2 = df.format(point2D.getY());
                LogUtills.i(TAG, "Point2D x = " + x2 + " ,Point2D y = " + y2);
                edtX.setText(x2);
                edtY.setText(y2);
                break;
            //弹起
            case MotionEvent.ACTION_POINTER_UP:
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            LogUtills.i("选中");
            edtX.setEnabled(true);
            edtY.setEnabled(true);
        } else {
            LogUtills.i("未选中");
            edtX.setEnabled(false);
            edtY.setEnabled(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataXY2View(Point Point) {
        Point2D point2D = m_map.pixelToMap(Point);
        DecimalFormat df = new DecimalFormat("0.00000");
        String x2 = df.format(point2D.getX());
        String y2 = df.format(point2D.getY());
        LogUtills.i(TAG, "Point2D x = " + x2 + " ,Point2D y = " + y2);
        edtX.setText(x2);
        edtY.setText(y2);
        callOut.setLocation(point2D.getX(), point2D.getY());
//        m_mapView.addCallout(callOut);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setCbConVisibityGone() {
        cbCon.setVisibility(View.GONE);
        cbAdd.setChecked(false);
        cbQuery.setChecked(false);

    }

    private void setCbConVisibityView() {
        cbCon.setVisibility(View.VISIBLE);
    }

    private void setCbLineConVisibityGone() {
        cbConLine.setVisibility(View.GONE);
        cbAddLine.setChecked(false);
        cbQueryLine.setChecked(false);

    }

    private void setCbLineConVisibityView() {
        cbConLine.setVisibility(View.VISIBLE);
    }


}
