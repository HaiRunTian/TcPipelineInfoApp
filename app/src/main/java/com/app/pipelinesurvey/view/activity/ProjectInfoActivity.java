package com.app.pipelinesurvey.view.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.config.SettingConfig;
import com.app.pipelinesurvey.config.SharedPrefManager;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.AlertDialogUtil;
import com.app.pipelinesurvey.utils.AssetsUtils;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.PermissionUtils;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.utills.LogUtills;
import com.supermap.data.Environment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HaiRun
 * <p>
 * 新建工程  配置地图  保存数据库
 */
public class ProjectInfoActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回
     */
    private TextView tvReturn;
    /**
     * 配置
     */
    private TextView tvConfig;
    /**
     * 删除
     */
    private Button btnDelete;
    /**
     * 打开项目
     */
    private Button btnOpen;
    /**
     * 项目名称
     */
    private EditText edtProjName;
    /**
     * 项目创建时间
     */
    private TextView tvProjectCreateTime;
    /**
     * 项目更新时间
     */
    private TextView tvLastestModifiedTime;
    /**
     * 添加底图按钮
     */
    private TextView tvAddBaseMap;
    private TextView tvBaseMapPath;
    private static final int RESULT_FILE = 1;
    private String baseMapPath;
    /**
     * 在界面初始化后，用地图数据判断是新建项目还是打开原有项目，
     */
    private boolean mNewPrj;
    private final static int RESULCODE = 3;
    private String m_prjId;
    private Spinner spGroupIndex, spCityStand, spSeriNum, spMode;
    private EditText edtGroupName, edtLineL;
    private String[] m_local;
    private List<String> list;
    private int from;
    private Button btnSetting;
    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        PermissionUtils.initPermission(this, new PermissionUtils.PermissionHolder());
        initView();
        initValue();
        initEvent();
    }

    private void initEvent() {
        spCityStand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SQLConfig.TABLE_DEFAULT_POINT_SETTING = "default_point_zhengben";
                SQLConfig.TABLE_DEFAULT_LINE_SETTING = "default_line_zhengben";
                //初始化城市标准名称
                SuperMapConfig.PROJECT_CITY_NAME = list.get(position);
                switch (position) {
                    //广州 天驰
                    case 0:
                        SQLConfig.TABLE_DEFAULT_POINT_SETTING = "default_point_setting";
                        SQLConfig.TABLE_DEFAULT_LINE_SETTING = "default_line_setting";
                        break;
                    //深圳
                    case 1:
                        SQLConfig.TABLE_DEFAULT_POINT_SETTING = "default_point_shenzhen";
                        SQLConfig.TABLE_DEFAULT_LINE_SETTING = "default_line_shenzhen";
                        break;
                    //惠州
                    case 2:
                        SQLConfig.TABLE_DEFAULT_POINT_SETTING = "default_point_huizhou";
                        SQLConfig.TABLE_DEFAULT_LINE_SETTING = "default_line_huizhou";
                        break;
                    //正本清源
                    case 3:
                        SQLConfig.TABLE_DEFAULT_POINT_SETTING = "default_point_zhengben";
                        SQLConfig.TABLE_DEFAULT_LINE_SETTING = "default_line_zhengben";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initValue() {
        int _serialNum = -1;
        String _city = "";
        int _groupLocal = -1;
        int _pipeLength = -1;
        String _groupNum = "";
        String mode = "";
        String psCheck = "";
        from = getIntent().getIntExtra("from", 2);
        //初始化城市标准
        Cursor _cursor1 = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO, "");
        list = new ArrayList();
        while (_cursor1.moveToNext()) {
            list.add(_cursor1.getString(_cursor1.getColumnIndex("name")));
            LogUtills.i(_cursor1.getString(_cursor1.getColumnIndex("name")));
        }
        _cursor1.close();
//        list.add("广州");
//        list.add("正本清源");
//        SQLConfig.TABLE_DEFAULT_POINT_SETTING = "default_point_zhengben";
//        SQLConfig.TABLE_DEFAULT_LINE_SETTING = "default_line_zhengben";
        spCityStand.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item_text, list));
        //初始化
        String[] m_local = getResources().getStringArray(R.array.exp_num_type);
        spGroupIndex.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item_text, m_local));
        //初始化
        String[] _seriaNum = getResources().getStringArray(R.array.seriNum);
        spSeriNum.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item_text, _seriaNum));
        //工程模式初始化
        String[] modes = getResources().getStringArray(R.array.mode);
        spMode.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item_text, modes));

        if (from == 0) {
            //新建项目
            mNewPrj = true;
            edtGroupName.setText("a");
            edtProjName.setText(DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_FORMAT + "-"));
            tvProjectCreateTime.setText(DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT));
            tvLastestModifiedTime.setText(DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT));
            edtLineL.setText(_pipeLength != -1 ? String.valueOf(_pipeLength) : "30");
        } else if (from == 1) {
            //打开已有项目
            mNewPrj = false;
            edtProjName.setFocusable(false);
            tvAddBaseMap.setEnabled(false);
            edtGroupName.setFocusable(false);
            edtLineL.setFocusable(false);
            tvAddBaseMap.setFocusable(false);
            m_prjId = getIntent().getStringExtra("proj_name");
            String create_time = null, last_time = null;
            edtProjName.setText(m_prjId);
            spMode.setEnabled(false);
            spCityStand.setEnabled(false);
            spGroupIndex.setEnabled(false);
            spSeriNum.setEnabled(false);
            aSwitch.setEnabled(false);

            //数据库查询
            Cursor _cursor = DatabaseHelpler.getInstance()
                    .query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + m_prjId + "'");
            while (_cursor.moveToNext()) {
                create_time = _cursor.getString(_cursor.getColumnIndex("CteateTime"));
                baseMapPath = _cursor.getString(_cursor.getColumnIndex("BaseMapPath"));
                last_time = _cursor.getString(_cursor.getColumnIndex("UpdateTime1"));
                //组号
                _groupNum = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
                //流水号长度
                _serialNum = _cursor.getInt(_cursor.getColumnIndex("SerialNum"));
                //城市
                _city = _cursor.getString(_cursor.getColumnIndex("City"));
                //组号位置
                _groupLocal = _cursor.getInt(_cursor.getColumnIndex("GroupLocal"));
                //管线长度
                _pipeLength = _cursor.getInt(_cursor.getColumnIndex("PipeLength"));
                //工程模式
                mode = _cursor.getString(_cursor.getColumnIndex("mode"));
                SuperMapConfig.PrjMode = mode;
                //排水外检
                psCheck = _cursor.getString(_cursor.getColumnIndex("PsCheck"));
                SuperMapConfig.PS_OUT_CHECK = psCheck;
            }
            _cursor.close();
            if ("1".equals(psCheck)) {
                aSwitch.setChecked(true);
            } else {
                aSwitch.setChecked(false);
            }
            tvProjectCreateTime.setText(create_time.length() > 0 ? create_time : "");
            tvLastestModifiedTime.setText(last_time.length() > 0 ? last_time : "");
            tvBaseMapPath.setText(baseMapPath.length() > 0 ? baseMapPath : "");
            edtGroupName.setText(_groupNum);
            edtLineL.setText(_pipeLength != -1 ? String.valueOf(_pipeLength) : "30");
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spCityStand, _city);
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spMode, mode);
            String _group = "";
            switch (_groupLocal) {
                case 1:
                    _group = m_local[0];
                    break;
                case 2:
                    _group = m_local[1];
                    break;
                case 3:
                    _group = m_local[2];
                    break;
                default:
                    break;
            }
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spGroupIndex, _group);
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spSeriNum, String.valueOf(_serialNum));
            aSwitch.setFocusable(false);
        }
    }

    private void initView() {
        tvProjectCreateTime = (TextView) findViewById(R.id.tvProjectCreateTime);
        tvLastestModifiedTime = (TextView) findViewById(R.id.tvLastestModifiedTime);
        tvAddBaseMap = (TextView) findViewById(R.id.tvAddBaseMap);
        tvBaseMapPath = (TextView) findViewById(R.id.tvBaseMapPath);
        tvAddBaseMap.setOnClickListener(this);
        tvReturn = (TextView) findViewById(R.id.tvReturn);
        tvReturn.setOnClickListener(this);
        tvConfig = (TextView) findViewById(R.id.tvConfig);
        tvConfig.setOnClickListener(this);
        edtProjName = (EditText) findViewById(R.id.edtProjectName);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        btnOpen = (Button) findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(this);
        spCityStand = findViewById(R.id.sp_city_stand);
        spGroupIndex = findViewById(R.id.sp_group_local);
        spSeriNum = findViewById(R.id.sp_serinum);
        edtGroupName = findViewById(R.id.edt_group);
        edtLineL = findViewById(R.id.edt_line_length);
        btnSetting = findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener(this);
        spMode = findViewById(R.id.sp_mode);
        aSwitch = findViewById(R.id.switch1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReturn:
                finish();
                break;
            //设置按钮
            case R.id.btn_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                intent.putExtra("status", from);
                intent.putExtra("prjName", edtProjName.getText().toString().trim());
                SuperMapConfig.PROJECT_NAME = edtProjName.getText().toString().trim();
                startActivity(intent);
                break;
            //保存数据，打开地图，如果没有添加地图，默认打开地图
            case R.id.btnOpen:
                try {
                    String prjName = edtProjName.getText().toString().trim();
                    if (prjName.contains("/")) {
                        ToastyUtil.showWarningLong(this, "工程名字不能有'/',请重新命名");
                        return;
                    }
                    //判断是否要插入点线配置数据
                    inserSettingSql(prjName);
                    Intent _intent = new Intent(ProjectInfoActivity.this, MapActivity.class);
                    _intent.putExtra("prjName", prjName);
                    if (mNewPrj) {
                        //新项目 //用户没有选择切图，默认打开谷歌地图
                        if (tvBaseMapPath.getText().toString().length() == 0) {
                            //类型 google代表谷歌地图 sci代表切片
                            _intent.putExtra("type", "google");
//                        baseMapPath = "http://map.baidu.com";
                            baseMapPath = "http://www.google.cn/maps?scale=2";
                        } else {  //用户选择了地图切片
                            //类型 1代表谷歌地图 sci 代表切片
                            _intent.putExtra("type", "sci");
                            _intent.putExtra("status", 0);
                        }
                        if (queryPrjName()) {
                            return;
                        }
                        saveDataToDB();
                        //全局，记住当前项目模式 常规 外检
                        SuperMapConfig.PrjMode = spMode.getSelectedItem().toString();

                    } else {
                        //无用 不是新建项目
                        _intent.putExtra("type", "3");
                        _intent.putExtra("status", 1);

                        if (!FileUtils.getInstance().isFileExsit(baseMapPath) && !baseMapPath.contains("http://www.google.cn/maps")) {
                            ToastyUtil.showWarningLong(this, "切片源文件不在，是否删除了切片文件夹");
                            return;
                        }
                        updataTime();
                    }
                    SuperMapConfig.GROUP_CODE = edtGroupName.getText().toString();
                    startActivity(_intent);

                    finish();
                } catch (Exception e) {
                    ToastyUtil.showErrorShort(this, e.toString());
                }
                break;

            case R.id.tvAddBaseMap: {
                AlertDialogUtil.showDialog(ProjectInfoActivity.this, "导入文件", "请选择导入的文件", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SuperMapConfig.FILE_PATH = SuperMapConfig.QQ_FILE_PATH;
                        startActivityForResult(new Intent(ProjectInfoActivity.this, SelectBaseMapActivity.class), RESULT_FILE);
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SuperMapConfig.FILE_PATH = SuperMapConfig.DEFAULT_DATA_PATH;
                        startActivityForResult(new Intent(ProjectInfoActivity.this, SelectBaseMapActivity.class), RESULT_FILE);
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SuperMapConfig.FILE_PATH = SuperMapConfig.WECHAT_FILE_PATH;
                        startActivityForResult(new Intent(ProjectInfoActivity.this, SelectBaseMapActivity.class), RESULT_FILE);
                        dialog.dismiss();
                    }
                });
//
            }

            break;
            //删除项目 //数据库删除 文件夹删除工作空间
            case R.id.btnDelete:
                AlertDialog.Builder _dialog = new AlertDialog.Builder(this);
                _dialog.setTitle("温馨提示");
                _dialog.setMessage("亲，删除工程前请确认已导出此工程数据并且已发送给项目经理，否则此工程数据会彻底被删除！");
                _dialog.setIcon(R.drawable.ic_warning);
                _dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] whereArgs = {edtProjName.getText().toString()};
                        DatabaseHelpler.getInstance().delete(SQLConfig.TABLE_NAME_PROJECT_INFO, "Name = ?", whereArgs);
                        ToastyUtil.showSuccessShort(ProjectInfoActivity.this, "删除成功");
                        setResult(1);
                        finish();
                    }
                });
                _dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                _dialog.create().show();
                break;
            default:
                break;
        }
    }

    /**
     * 查询数据库 ，如果项目名字相同，则不可创建此工程
     *
     * @return
     */
    private boolean queryPrjName() {

        Cursor _cursor = DatabaseHelpler.getInstance()
                .query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + edtProjName.getText().toString() + "'");
        if (_cursor.getCount() > 0) {
            ToastyUtil.showWarningShort(this, "工程名已存在，请重新命名工程名");
            return true;
        }
        if (FileUtils.getInstance().isDirExsit(SuperMapConfig.DEFAULT_DATA_PATH + edtProjName.getText().toString())) {
            ToastyUtil.showWarningShort(this, "手机中此工程名文件夹已存在，请重新命名工程名");
            return true;
        }
        return false;
    }

    /**
     * 更新工程最后更新时间
     *
     * @Author HaiRun
     * @Time 2019/4/25 . 17:39
     */
    private void updataTime() {
        ContentValues _contentValues = new ContentValues();
        _contentValues.put("UpdateTime1", DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT));
        DatabaseHelpler.getInstance().update(SQLConfig.TABLE_NAME_PROJECT_INFO, _contentValues, "Name = ?", new String[]{edtProjName.getText().toString()});
    }

    private void saveDataToDB() {
        String prjName = edtProjName.getText().toString().trim();
        SharedPrefManager _manager = new SharedPrefManager(this,
                SharedPrefManager.FILE_CONFIG);
        String city = (String) _manager.getSharedPreference(SharedPrefManager.KEY_CITY, "");
        ContentValues _values = new ContentValues();
        //工程项目名称，工作空间名称、数据源名称
        _values.put("Name", prjName);
        //未合并登录模块，置留
        _values.put("Creator", "");
        _values.put("CteateTime", tvProjectCreateTime.getText().toString().trim());
        _values.put("UpdateTime1", tvLastestModifiedTime.getText().toString().trim());
        _values.put("BaseMapPath", baseMapPath);

        _values.put("City", spCityStand.getSelectedItem().toString());
        _values.put("GroupNum", edtGroupName.getText().toString().trim());
        _values.put("PipeLength", edtLineL.getText().toString().trim());
        _values.put("SerialNum", spSeriNum.getSelectedItem().toString());
        _values.put("GroupLocal", spGroupIndex.getSelectedItemPosition() + 1);
        _values.put("mode", spMode.getSelectedItem().toString());

        //是否启用排水检测
        if (aSwitch.isChecked()) {
            _values.put("PsCheck", "1");
            SuperMapConfig.PS_OUT_CHECK = "1";
        } else {
            _values.put("PsCheck", "0");
        }
        DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_PROJECT_INFO, _values);

        if (!edtProjName.getText().toString().isEmpty()) {
            String priName = edtProjName.getText().toString().trim();
            //创建此工程路径的文件夹
            FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + priName);
            //创建照片文件夹picture
            FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + priName + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH);
            //创建excel文件夹Picture
            FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + priName + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
            //创建shp文件夹Shp
            FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + priName + "/" + SuperMapConfig.DEFAULT_DATA_SHP_PATH);
            //创建现场检测记录表文件夹
            FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + priName + "/" + SuperMapConfig.DEFAULT_DATA_RECORD);
            //复制现场检测记录表
            InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
            if (is != null) {
                FileUtils.getInstance().copy(is, SuperMapConfig.DEFAULT_DATA_PATH + priName + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
            }
         /*   if (aSwitch.isChecked()) {
                //创建排水检测记录表文件夹
                FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + priName + "/" + SuperMapConfig.DEFAULT_DATA_PS_RECORD);
                //复制排水检测记录表
                InputStream isPs = AssetsUtils.getInstance().open(SuperMapConfig.DEFAULT_DATA_PS_RECORD_NAME);
                if (isPs != null) {
                    FileUtils.getInstance().copy(isPs, SuperMapConfig.DEFAULT_DATA_PATH + priName + "/" + SuperMapConfig.DEFAULT_DATA_PS_RECORD + SuperMapConfig.DEFAULT_DATA_PS_RECORD_NAME);
                }
            }*/
        }
    }

    private void inserSettingSql(String prjName) {
        //插入点线配置表
        Cursor cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_POINT_SETTING, "where prj_name = '" + prjName + "'");
        //如果点线配置表没有，则插入
        if (cursor.getCount() == 0) {
            SettingConfig.ins().getPipeContentValues(prjName);
            SettingConfig.ins().getContentValues(prjName);
            SettingConfig.ins().getLineContentValues(prjName);

        }
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_FILE) {
            if (resultCode == RESULT_OK) {
                String _filePath = data.getStringExtra("filePath");
                String _fileName = data.getStringExtra("fileName");
                baseMapPath = _filePath;
                if (!_fileName.endsWith(".sci") && !_fileName.endsWith(".dwg")) {
                    ToastyUtil.showWarningShort(this, "请选择sci或者dwg的底图类型文件");
                    tvBaseMapPath.setText("");
                } else {
                    tvBaseMapPath.setText(baseMapPath);
                }
            }
        }
    }
}
