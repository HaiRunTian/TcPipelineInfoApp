package com.app.pipelinesurvey.view.fragment.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.BaseInfo.Data.Line.WaterSupplyFieldLine;
import com.app.BaseInfo.Data.MAPACTIONTYPE;
import com.app.BaseInfo.Data.Point.WaterSupplyFieldPoint;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.BaseInfo.Oper.OperNotifyer;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.LayersSimpleArraryAdapter;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.activity.DrawPipeLineActivity;

import com.app.pipelinesurvey.view.activity.LayersActivity;
import com.app.utills.LogUtills;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EncodeType;
import com.supermap.data.EngineType;
import com.supermap.data.Environment;
import com.supermap.data.LicenseStatus;
import com.supermap.data.Point2D;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.Action;
import com.supermap.mapping.Layers;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapParameterChangedListener;
import com.supermap.mapping.MapView;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;

/**
 * Created by Kevin on 2018-05-16.
 */

public class MapFragment extends Fragment  {
   /* private Spinner spLayers;//图层选择
    private ImageButton imgBtnLocation;//定位
    private ImageButton imgBtnZoomIn;//放大
    private ImageButton imgBtnZoomOut;//缩小
    private LinearLayout layoutContainer;//详细面板
    private HorizontalScrollView layoutFuntionbar;//Mapcontrol功能栏
    private List<String> m_data_list = new ArrayList<>();//图层填充数据
    private LayersSimpleArraryAdapter m_adapter;//图层下拉适配
    private Animation m_animation;//动画
    private CheckBox cbFunctions;
    //    private RadioGroup radioGroup;
    //    private RadioButton tempRadioButton;
    private RadioButton rdBtnDrag, rdBtnDrawPoint, rdBtnDrawLine, rdbtnLayers,
            rdBtnPointInLine, rdBtnMeasuredPoint, rdBtnQueryPoint, rdBtnDistanceMeasure, rdBtnQueryLine, rdBtnMovePoint;
    private TextView tvLabel;
    private View view;
    private DrawPointFragment m_drawPointFragment;//绘制管点fragment
    private DrawLineFragment m_drawLineFragment;//绘制管线fragment
    private DrawPointInLineFragment m_drawPointInLineFragment;//线上加点fragment
    private MeasuredPointFragment m_measuredPointFragment;//测量收点fragment
    private QueryPointFragment m_queryPointFragment;//管点查询fragment
    private QueryLineFragment m_queryLineFragment; //管线查询fragment
    private DistanceMeasureFragment m_distanceMeasurementFragment;//距离测量fragment
    private FragmentManager m_manager;
    private FragmentTransaction m_transaction;
    private TextView m_mapScale;
    private LinearLayout layoutEmpty;
    public static String labelTag = "";//全局变量，功能标签
    public static String pipeType = "";//全局变量，管类
    public MapView m_mapView;
    public MapControl m_mapControl;
    public final static String PRJ_KEY = "prj_name";
    private String baseMapPath = null;


    private MapOperation m_mapOper = null;
    private String m_prjId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle _bundle = getArguments();
        //工程名称，可以直接查询数据库，获取工作空间名称，还有地图切片路径
        m_prjId = (String) _bundle.get(PRJ_KEY);
        Cursor _cursor = DatabaseHelpler.getInstance()
                .query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where proj_id = '" + m_prjId + "'");

        while (_cursor.moveToNext()) {
            baseMapPath = _cursor.getString(_cursor.getColumnIndex("basemap_path"));
//            LogUtills.i("baseMapPath = "+baseMapPath);
        }
//        LogUtills.i("Activity传值过来的 = "+ m_prjId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //获取fragment
    public static MapFragment newInstance(String str) {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PRJ_KEY, str);
        fragment.setArguments(bundle); //设置参数
        return fragment;
    }


    *//**
     * 检测许可是否可用
     *
     * @auther HaiRun
     * created at 2018/8/9 14:03
     *//*
    boolean licenseStatus() {
        LicenseManager _licenseManager = LicenseManager.getInstance();
        LicenseStatus _licenseStatus = _licenseManager.getLicenseStatus();
        boolean isUse = _licenseStatus.isLicenseValid(); //是否有效
        return isUse;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            LogUtills.i("init onCreateView");
            view = inflater.inflate(R.layout.fragment_map, container, false);
            m_mapView = (MapView) view.findViewById(R.id.mapView);
            m_mapControl = m_mapView.getMapControl();
            spLayers = view.findViewById(R.id.spLayers);
            spLayers.setOnItemSelectedListener(this);
            imgBtnLocation = view.findViewById(R.id.imgBtnLocation);
            imgBtnLocation.setOnClickListener(this);
            imgBtnZoomIn = view.findViewById(R.id.imgBtnZoomIn);
            imgBtnZoomIn.setOnClickListener(this);
            imgBtnZoomOut = view.findViewById(R.id.imgBtnZoomOut);
            imgBtnZoomOut.setOnClickListener(this);
            layoutContainer = view.findViewById(R.id.layoutMapContainer);
            cbFunctions = view.findViewById(R.id.cbFunctions);
            cbFunctions.setOnCheckedChangeListener(this);
            layoutFuntionbar = view.findViewById(R.id.layoutFuntionbar);
            m_mapScale = view.findViewById(R.id.txMapScale);
            //        radioGroup = view.findViewById(R.id.radioGroup);
            //        radioGroup.setOnCheckedChangeListener(this);
            cbFunctions.setChecked(true);
            tvLabel = view.findViewById(R.id.tvLabel);
            rdBtnDrag = view.findViewById(R.id.rdBtnDrag);
            rdBtnDrag.setOnClickListener(this);
            rdBtnDrawPoint = view.findViewById(R.id.rdBtnDrawPoint);
            rdBtnDrawPoint.setOnClickListener(this);
            rdBtnDrawLine = view.findViewById(R.id.rdBtnDrawLine);
            rdBtnDrawLine.setOnClickListener(this);
            rdBtnPointInLine = view.findViewById(R.id.rdBtnPointInLine);
            rdBtnPointInLine.setOnClickListener(this);
            rdBtnMeasuredPoint = view.findViewById(R.id.rdBtnMeasuredPoint);
            rdBtnMeasuredPoint.setOnClickListener(this);
            rdBtnDistanceMeasure = view.findViewById(R.id.rdBtnDistanceMeasure);
            rdBtnDistanceMeasure.setOnClickListener(this);
            rdBtnQueryPoint = view.findViewById(R.id.rdBtnQueryPoint);
            rdBtnQueryPoint.setOnClickListener(this);
            rdbtnLayers = view.findViewById(R.id.rdBtnLayers);
            rdbtnLayers.setOnClickListener(this);
            rdBtnQueryLine = view.findViewById(R.id.rdBtnQueryLine);
            rdBtnQueryLine.setOnClickListener(this);
            rdBtnMovePoint = view.findViewById(R.id.rdBtnMovePoint);
            rdBtnMovePoint.setOnClickListener(this);

            // openWorkspace();
        } catch (Exception e) {
            // Log.i("ss",e.toString());
            e.printStackTrace();
            LogUtills.e(e.toString());
        }


        *//*空白区域点击弹出加点加线面板测试代码*//*
       *//* layoutEmpty = view.findViewById(R.id.layoutEmpty);
        layoutEmpty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (tvLabel.getText().equals("管点")) {
                    startActivity(new Intent(getActivity(), DrawPipePointActivity.class).
                            putExtra("gpType", spLayers.getSelectedItem().toString()));
                }
                if (tvLabel.getText().equals("绘制管线")) {
                    startActivity(new Intent(getActivity(), DrawPipeLineActivity.class).
                            putExtra("gpType", spLayers.getSelectedItem().toString()));
                }
                if (tvLabel.getText().equals("线中加点")) {
                    startActivity(new Intent(getActivity(), DrawPipePointActivity.class).
                            putExtra("gpType", spLayers.getSelectedItem().toString()));
                }
                return false;
            }
        });*//*
        *//*--------------------测试代码完--------------------*//*
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtills.i("init onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initData();
        try {
            LogUtills.i("init onCreate...");
            if (!licenseStatus()) {
                Toast.makeText(getActivity(), "许可证不可使用，请联系技术员", Toast.LENGTH_LONG).show();
                return;
            }
            //打开默认工作空间或者指定项目空间
//            openWorkspace();
            createWorkspace();

        } catch (Exception e) {
            e.printStackTrace();
            LogUtills.e(e.toString());
        }
    }

    //创建工作空间
    private void createWorkspace() {
        //测试创建工作空间，数据源
        Workspace _wk = new Workspace(); // 没有关联的工作空间文件
        _wk.setCaption(m_prjId); //工作空间别名
        _wk.setDescription(m_prjId);
        WorkspaceConnectionInfo _wkInfo = _wk.getConnectionInfo();
        boolean exsit = FileUtils.getInstance().isFileExsit(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId+".smwu");
        if (!exsit) {
            _wkInfo.setServer(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId);// 创建path 为工作空间文件的路径
        } else {
//            _wkInfo.setServer(SuperMapConfig.DEFAULT_DATA_PATH + "测试.smwu");//打已有 path 为工作空间文件的路径
            _wkInfo.setServer(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId+".smwu");//打已有 path 为工作空间文件的路径
        }
//        _wkInfo.setName(m_prjId);
        _wkInfo.setType(WorkspaceType.SMWU);
        if (exsit) {
            _wk.open(_wkInfo);
            m_mapControl.getMap().setWorkspace(_wk);
            DatasourceConnectionInfo info = new DatasourceConnectionInfo();
            //设置别名
//            info.setAlias(m_prjId);
            //设置Server
            info.setServer(baseMapPath);
//            info.setServer(SuperMapConfig.DEFAULT_MAP_CACHE_PATH);
            //设置数据源连接的引擎类型为REST 地图服务引擎类型
            info.setEngineType(EngineType.IMAGEPLUGINS);
            //获取数据源集合
            Datasources datasources = _wk.getDatasources();
            //打开数据源
            Datasource ds = datasources.open(info);
            //m_mapControl.getMap();
            //添加到地图窗口中
            m_mapControl.getMap().getLayers().add(ds.getDatasets().get(0), true);
            m_mapControl.getMap().refresh();
            m_mapControl.setAction(Action.PAN);

            LogUtills.i("打开已有工作空间成功");
            LogUtills.i("工作空间名字 = "+_wk.getCaption());
            LogUtills.i("地图 = "+m_mapControl.getMap().getName());
        } else {
            try {
                boolean _open = _wk.save();       // 保存工作空间，如果文件不存在就会创建
                if (_open) {
                    LogUtills.i("工作空间创建成功了");
                    Datasources _datasources = _wk.getDatasources();
                    LogUtills.i("数据源集合数量 =" + String.valueOf(_datasources.getCount()));
                    //数据源连接信息类
                    DatasourceConnectionInfo _datasourceConnectionInfo = new DatasourceConnectionInfo();
                    _datasourceConnectionInfo.setAlias(m_prjId); //数据源别名
                    _datasourceConnectionInfo.setEngineType(EngineType.UDB);
                    _datasourceConnectionInfo.setServer(SuperMapConfig.DEFAULT_DATA_PATH +m_prjId +".udb");
                    //创建数据源
                    Datasource _datasource = _datasources.create(_datasourceConnectionInfo);
                    if (_datasource != null) {
                        LogUtills.i("数据源创建成功");
                    } else LogUtills.i("数据源创建失败");
                    Datasets _datasets = _datasource.getDatasets();
                    DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
                    String name = _datasets.getAvailableDatasetName(m_prjId);
                    datasetVectorInfo.setName(name);
                    datasetVectorInfo.setType(DatasetType.POINT);
                    datasetVectorInfo.setEncodeType(EncodeType.NONE);
                    // 创建矢量数据集
                    DatasetVector datasetVector = _datasets.create(datasetVectorInfo);
                    if (datasetVector != null) {
                        int _count = _wk.getDatasources().getCount();
                        LogUtills.i("数据源数量 = " + _count);
                        _wk.save();
                        m_mapControl.getMap().setWorkspace(_wk);
                        DatasourceConnectionInfo info = new DatasourceConnectionInfo();
                        //设置别名
//                        info.setAlias("1");
                        //设置Server
                        info.setServer(baseMapPath);
//                        info.setServer(SuperMapConfig.DEFAULT_MAP_CACHE_PATH);
                        //设置数据源连接的引擎类型为REST 地图服务引擎类型
                        info.setEngineType(EngineType.IMAGEPLUGINS);
                        //获取数据源集合
                        Datasources datasources = _wk.getDatasources();
                        //打开数据源
                        Datasource ds = datasources.open(info);
                        //m_mapControl.getMap();
                        //添加到地图窗口中
                        m_mapControl.getMap().getLayers().add(ds.getDatasets().get(0), true);
                        m_mapControl.getMap().refresh();
                        m_mapControl.setAction(Action.PAN);
                        LogUtills.i("创建并且打开了工作空间");
                    }
                } else LogUtills.i("工作空间创建失败了");
            } catch (Exception e) {
            }
        }
    }

    private void openWorkspace() {
        try {
            final ProgressDialog progress = new ProgressDialog(this.getContext());
            progress.setCancelable(false);
            progress.setMessage("数据加载中...");
            progress.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            progress.show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    LogUtills.i("....openWorkspace");
                    //打开工作空间
//                   WorkSpaceUtils.getInstance().openWorkspace();
                    progress.dismiss();
                    //默认显示长春地图
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Workspace wk = new Workspace();
                            //m_mapControl.getMap().setWorkspace(WorkSpaceUtils.getInstance().getWorkspace());
                           *//* boolean _result = m_mapControl.getMap().open(SuperMapConfig.DEFAULT_MAP_NAME);
                            m_mapControl.getMap().viewEntire();
                            LogUtills.i("地图打开成功，"+_result);*//*
                            //将地图显示控件和工作空间关联
                            m_mapControl.getMap().setWorkspace(WorkSpaceUtils.getInstance().getWorkspace());
                            DatasourceConnectionInfo info = new DatasourceConnectionInfo();
                            //设置别名
                            info.setAlias("map3333");
                            //设置Server
                            info.setServer(SuperMapConfig.DEFAULT_MAP_CACHE_PATH);
                            //设置数据源连接的引擎类型为REST 地图服务引擎类型
                            info.setEngineType(EngineType.IMAGEPLUGINS);

                            //获取数据源集合
                            Datasources datasources =WorkSpaceUtils.getInstance().getWorkspace().getDatasources();
                            //打开数据源
                            Datasource ds = datasources.open(info);
                            //m_mapControl.getMap();
                            //添加到地图窗口中
                            m_mapControl.getMap().getLayers().add(ds.getDatasets().get(0), true);
                            m_mapControl.getMap().refresh();
                            //全幅显示
                            m_mapControl.getMap().viewEntire();
                            LogUtills.i("....refresh map, name " + ds.getDatasets().get(0).getName());
                           WorkSpaceUtils.getInstance().setMapControl(m_mapControl);
                            //BaseFieldPInfos _info = new BaseFieldPInfos();
                            WaterSupplyFieldPoint _info = new WaterSupplyFieldPoint();
                            WaterSupplyFieldLine _infoL = new WaterSupplyFieldLine();
                          *//*  DatasetVector _ds = OperNotifyer.CreateDataset(WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0),_info);
                            OperNotifyer.AddFieldInfo(_ds,_info);*//*
                         *//*   OperNotifyer.CreateAndAddLayer(WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0),_infoL,m_mapControl.getMap());
                            OperNotifyer.CreateAndAddLayer(WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0),_info,m_mapControl.getMap());*//*

                            OperNotifyer.AddSystemLayers(WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0));
//                            DataHandlerObserver.ins().SetFragmentActivity(getActivity());

//                            Layers _layers = m_mapControl.getMap().getLayers();
//                            for (int i = 0; i < _layers.getCount(); i++) {
//                                LogUtills.i("MapFragment 图层名字 = ", _layers.get(i).getName());
//                            }
                            m_mapControl.setMapParamChangedListener(new MapParameterChangedListener() {

                                @Override
                                public void scaleChanged(double v) {
                                    if (m_mapControl.getMap() != null) {
                                        float _m = (float) (1.0 / v);
                                        LogUtills.i(String.valueOf(_m));
                                        m_mapScale.setText("比例尺 1 : " + String.format("%f", _m));
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
                            //m_mapOper = new MapOperation(m_mapControl,getActivity());

                        }
                    });
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtills.e(e.toString());
        }
    }

    public TextView getMapTextView() {
        return m_mapScale;
    }

    private void initData() {
        String[] layers = getResources().getStringArray(R.array.pipeType);
        for (String _layer : layers) {
            m_data_list.add(_layer);
        }
        m_adapter = new LayersSimpleArraryAdapter(getActivity(), R.layout.spinner_item_style, m_data_list);
        m_adapter.setDropDownViewResource(R.layout.spinner_item_dropdown_style);
        spLayers.setAdapter(m_adapter);
        //  spLayers.setSelection(m_data_list.size() - 1, true);
        spLayers.setSelection(0, true);
    }

    //控制底部面板显示
    private void showBottomPanel() {
        if (layoutContainer.getVisibility() == View.GONE) {
            layoutContainer.setVisibility(View.VISIBLE);
            m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_bottom_in);
            layoutContainer.startAnimation(m_animation);
        }
    }

    //隐藏底部面板
    private void hideBottomPanel() {
        if (layoutContainer.getVisibility() == View.VISIBLE) {
            m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_bottom_out);
            layoutContainer.startAnimation(m_animation);
            layoutContainer.setVisibility(View.GONE);
        }
    }

    //切换底部面板显示
    private void toggleBottomPanel() {
        if (layoutContainer.getVisibility() == View.VISIBLE) {
            m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_bottom_out);
            layoutContainer.startAnimation(m_animation);
            layoutContainer.setVisibility(View.GONE);
        } else {
            layoutContainer.setVisibility(View.VISIBLE);
            m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_bottom_in);
            layoutContainer.startAnimation(m_animation);
        }
    }

    //控制功能面板的显示
    private void setFuntionPanelVisibility() {
        if (layoutFuntionbar.getVisibility() == View.GONE) {
            layoutFuntionbar.setVisibility(View.VISIBLE);
            m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_autotv_show);
            layoutFuntionbar.startAnimation(m_animation);
        } else {
            m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_autotv_disapper);
            layoutFuntionbar.startAnimation(m_animation);
            layoutFuntionbar.setVisibility(View.GONE);
        }
    }

    *//**
     * 切换fragment
     *
     * @param id fragment名
     * @return null
     * @datetime 2018-05-16  15:24.
     *//*
    public void switchFragment(int id) {
        if (m_manager == null) {
            m_manager = getActivity().getSupportFragmentManager();
        }
        m_transaction = m_manager.beginTransaction();
        hideFragment(m_transaction);
        showFragment(id);
        m_transaction.commit();
    }

    *//**
     * 显示对应的fragment
     *
     * @param id fragment名
     * @return null
     * @datetime 2018-05-16  15:25.
     *//*
    private void showFragment(int id) {
        switch (id) {
            case 0://显示添加管点
                if (m_drawPointFragment == null) {
                    m_drawPointFragment = new DrawPointFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_drawPointFragment);
                } else
                    m_transaction.show(m_drawPointFragment);
                break;
            case 1://显示添加管线
                if (m_drawLineFragment == null) {
                    m_drawLineFragment = new DrawLineFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_drawLineFragment);
                } else
                    m_transaction.show(m_drawLineFragment);
                break;
            case 2://显示线上加点
                if (m_drawPointInLineFragment == null) {
                    m_drawPointInLineFragment = new DrawPointInLineFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_drawPointInLineFragment);
                } else
                    m_transaction.show(m_drawPointInLineFragment);
                break;
            case 3://显示测量收点
                if (m_measuredPointFragment == null) {
                    m_measuredPointFragment = new MeasuredPointFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_measuredPointFragment);
                } else
                    m_transaction.show(m_measuredPointFragment);
                break;
            case 4://显示查询管点
                if (m_queryPointFragment == null) {
                    m_queryPointFragment = new QueryPointFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_queryPointFragment);
                } else
                    m_transaction.show(m_queryPointFragment);
                break;


            case 5://显示距离测量
                if (m_distanceMeasurementFragment == null) {
                    m_distanceMeasurementFragment = new DistanceMeasureFragment();
                    m_transaction.add(R.id.layoutMapContainer, m_distanceMeasurementFragment);
                } else
                    m_transaction.show(m_distanceMeasurementFragment);
                break;
//            case 6://显示管线查询
//                if (m_queryLineFragment == null) {
//                    m_queryLineFragment = new QueryLineFragment();
//                    m_transaction.add(R.id.layoutMapContainer, m_queryLineFragment);
//                } else
//                    m_transaction.show(m_queryLineFragment);
//                break;

            default:
                break;
        }
    }

    *//**
     * 隐藏所有fragment
     *
     * @param transaction fragment事务
     * @return null
     * @datetime 2018-05-16  15:25.
     *//*
    private void hideFragment(FragmentTransaction transaction) {
        if (m_drawPointFragment != null) {
            transaction.hide(m_drawPointFragment);
        }
        if (m_drawLineFragment != null) {
            transaction.hide(m_drawLineFragment);
        }
        if (m_drawPointInLineFragment != null) {
            transaction.hide(m_drawPointInLineFragment);
        }
        if (m_measuredPointFragment != null) {
            transaction.hide(m_measuredPointFragment);
        }
        if (m_queryPointFragment != null) {
            transaction.hide(m_queryPointFragment);
        }
        if (m_drawPointFragment != null) {
            transaction.hide(m_drawPointFragment);
        }
        if (m_distanceMeasurementFragment != null) {
            transaction.hide(m_distanceMeasurementFragment);
        }
//        if (m_queryLineFragment != null) {
//
//            transaction.hide(m_queryLineFragment);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        String buttonText = "";//按钮文本
        String labelText = "";//标签文本
        if (v instanceof RadioButton) {//判断是否属于radiobutton
            buttonText = ((RadioButton) v).getText().toString();//当前激活按钮文本
        }
        labelText = tvLabel.getText().toString();//当前标签文本
        switch (v.getId()) {
            case R.id.imgBtnLocation:
                Toast.makeText(getActivity(), "暂待开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgBtnZoomIn:
                m_mapControl.getMap().zoom(1.5);
//                Toast.makeText(getActivity(), "放大", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgBtnZoomOut:
                m_mapControl.getMap().zoom(0.5);
//                Toast.makeText(getActivity(), "缩小", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rdBtnDrag:
                hideBottomPanel();
                m_mapControl.setAction(Action.PAN);
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE.Action_None);
//                Toast.makeText(getActivity(), "漫游设置成功", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rdBtnDrawPoint:
                hideBottomPanel();
                if (DataHandlerObserver.ins().setAction(Action.CREATEPOINT)) {
                    DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE.Action_CreatePoint);
//                    ToastUtil.show(this.getContext(), "创建管点设置成功", 3);
//                    return;
                }

//                rdBtnDrag.setChecked(true);
//                m_mapControl.setAction(Action.PAN);

                break;
            //加线
            case R.id.rdBtnDrawLine:
                hideBottomPanel();
                if (DataHandlerObserver.ins().setAction(Action.SELECT)) {
                    DataHandlerObserver.ins().addPointOfLine(1);
                    DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE.Action_CreateLine);
//                DataHandlerObserver.ins().setAction(Action.MULTI_SELECT);
                   WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
                }

                break;
            //线中加点
            case R.id.rdBtnPointInLine:
            *//*    if (buttonText.equals(labelText)) {
                    toggleBottomPanel();
                } else {
                    showBottomPanel();
                    switchFragment(2);
                }*//*

                DataHandlerObserver.ins().setAction(Action.SELECT);
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE.Action_AddPointInLine);

                break;
            //测量收点
            case R.id.rdBtnMeasuredPoint:
                if (buttonText.equals(labelText)) {
                    toggleBottomPanel();
                } else {
                    showBottomPanel();
                    switchFragment(3);
                }
                break;
            //测量距离
            case R.id.rdBtnDistanceMeasure:
                if (buttonText.equals(labelText)) {
                    toggleBottomPanel();
                } else {
                    showBottomPanel();
                    switchFragment(5);
                }
                DataHandlerObserver.ins().setAction(Action.MEASURELENGTH);

                break;
            //管点定位
            case R.id.rdBtnQueryPoint:
                if (buttonText.equals(labelText)) {
                    toggleBottomPanel();
                } else {
                    showBottomPanel();
                    switchFragment(4);
                }
//                DataHandlerObserver.ins().setAction(Action.SELECT);
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE.Action_QueryPoint);
                break;
            //图层
            case R.id.rdBtnLayers:
                startActivity(new Intent(getActivity(), LayersActivity.class));
                hideBottomPanel();
                break;
            //编辑管线
            case R.id.rdBtnQueryLine:
//                if (buttonText.equals(labelText)) {
//                    toggleBottomPanel();
//                } else {
//                    showBottomPanel();
//                    switchFragment(6);
//                }

                hideBottomPanel();
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE.Action_QueryLine);
                break;
            //移动管点
            case R.id.rdBtnMovePoint:
                m_mapControl.setAction(Action.PAN);
                hideBottomPanel();
                DataHandlerObserver.ins().setMapActionType(MAPACTIONTYPE.Action_EditPointLocation);
            default:
                break;
        }

        tvLabel.setText(buttonText);
        labelTag = tvLabel.getText().toString();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setFuntionPanelVisibility();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        //        tempRadioButton = view.findViewById(group.getCheckedRadioButtonId());//当前激活的radiobutton
        //        String buttonText = tempRadioButton.getText().toString();//当前激活按钮文本
        //        String labelText = tvLabel.getText().toString();//当前标签文本
        //        switch (tempRadioButton.getId()) {
        //            case R.id.rdBtnDrag:
        //                hideBottomPanel();
        //                break;
        //            case R.id.rdBtnDrawPoint:
        //                if (buttonText.equals(labelText)) {
        //                    hideBottomPanel();
        //                } else {
        //                    showBottomPanel();
        //                    switchFragment(0);
        //                }
        //                break;
        //            case R.id.rdBtnDrawLine:
        //                if (buttonText.equals(labelText)) {
        //                    hideBottomPanel();
        //                } else {
        //                    showBottomPanel();
        //                    switchFragment(1);
        //                }
        //                break;
        //            case R.id.rdBtnPointInLine:
        //                if (buttonText.equals(labelText)) {
        //                    hideBottomPanel();
        //                } else {
        //                    showBottomPanel();
        //                    switchFragment(2);
        //                }
        //                break;
        //            case R.id.rdBtnMeasuredPoint:
        //                if (buttonText.equals(labelText)) {
        //                    hideBottomPanel();
        //                } else {
        //                    showBottomPanel();
        //                    switchFragment(3);
        //                }
        //                break;
        //            case R.id.rdBtnQueryPoint:
        //                if (buttonText.equals(labelText)) {
        //                    hideBottomPanel();
        //                } else {
        //                    showBottomPanel();
        //                    switchFragment(4);
        //                }
        //                break;
        //            case R.id.rdBtnDistanceMeasure:
        //                if (buttonText.equals(labelText)) {
        //                    hideBottomPanel();
        //                } else {
        //                    showBottomPanel();
        //                    switchFragment(5);
        //                }
        //                break;
        //        }
        //        tvLabel.setText(tempRadioButton.getText().toString());//更新标签
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pipeType = spLayers.getSelectedItem().toString();
        // LogUtills.i("select the Pipe Type "+ pipeType);
        //if(!pipeType.equals("选择管类")) return;

        if (WorkSpaceUtils.getInstance().getMapControl() != null)
            DataHandlerObserver.ins().setCurrentPipeType(pipeType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtills.i("onPause------"+"释放资源");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtills.i("onStop------"+"释放资源");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtills.i("onDestroyView------"+"释放资源");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtills.i("onDestroy------"+"释放资源");
    }*/
}
