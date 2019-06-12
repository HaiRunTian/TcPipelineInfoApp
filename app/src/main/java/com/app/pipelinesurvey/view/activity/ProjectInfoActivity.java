package com.app.pipelinesurvey.view.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.config.SharedPrefManager;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.PermissionUtils;
import com.app.pipelinesurvey.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *@author  HaiRun
 *
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
     *  在界面初始化后，用地图数据判断是新建项目还是打开原有项目，
     */
    private boolean mNewPrj;
    private final static int RESULCODE = 3;
    private String m_prjId;
    private Spinner spGroupIndex, spCityStand, spSeriNum;
    private EditText edtGroupName, edtLineL;
    private String[] m_local;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        PermissionUtils.initPermission(this,new PermissionUtils.PermissionHolder());
        initView();
        initValue();
    }

    private void initValue() {
        int _serialNum = -1;
        String _city = "";
        int _groupLocal = -1;
        int _pipeLength = -1;
        String _groupNum = "";
        int from = getIntent().getIntExtra("from", 2);


        //初始化城市标准sp
        Cursor _cursor1 = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO, "");
        List _list = new ArrayList();
        while (_cursor1.moveToNext()) {
            _list.add(_cursor1.getString(_cursor1.getColumnIndex("name")));
        }
        spCityStand.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item_text, _list));

        //初始化
        String[] m_local = getResources().getStringArray(R.array.exp_num_type);
        spGroupIndex.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item_text, m_local));

        //初始化
        String[] _seriaNum = getResources().getStringArray(R.array.seriNum);
        spSeriNum.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item_text, _seriaNum));
        if (from == 0) {
            //新建项目
            mNewPrj = true;
            edtGroupName.setText("A");
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
            String create_time = null, last_time = null, baseMapPath = null;
            edtProjName.setText(m_prjId);
            Cursor _cursor = DatabaseHelpler.getInstance()
                    .query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + m_prjId + "'");
            while (_cursor.moveToNext()) {
                create_time = _cursor.getString(_cursor.getColumnIndex("CteateTime"));
                baseMapPath = _cursor.getString(_cursor.getColumnIndex("BaseMapPath"));
                last_time = _cursor.getString(_cursor.getColumnIndex("UpdateTime1"));

                _groupNum = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
                _serialNum = _cursor.getInt(_cursor.getColumnIndex("SerialNum"));
                _city = _cursor.getString(_cursor.getColumnIndex("City"));
                _groupLocal = _cursor.getInt(_cursor.getColumnIndex("GroupLocal"));
                _pipeLength = _cursor.getInt(_cursor.getColumnIndex("PipeLength"));
            }

            tvProjectCreateTime.setText(create_time.length() > 0 ? create_time : "");
            tvLastestModifiedTime.setText(last_time.length() > 0 ? last_time : "");
            tvBaseMapPath.setText(baseMapPath.length() > 0 ? baseMapPath : "");
            edtGroupName.setText(_groupNum);
            edtLineL.setText(_pipeLength != -1 ? String.valueOf(_pipeLength) : "30");
            SpinnerDropdownListManager.setSpinnerItemSelectedByValue(spCityStand, _city);

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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReturn:
                finish();
                break;
            case R.id.tvConfig:
                break;
            //保存数据，打开地图，如果没有添加地图，默认打开地图
            case R.id.btnOpen:

                Intent _intent = new Intent(ProjectInfoActivity.this, MapActivity.class);
                _intent.putExtra("prjName", edtProjName.getText().toString().trim());

                if (mNewPrj) {
                    //新项目 //用户没有选择切图，默认打开谷歌地图
                    if (tvBaseMapPath.getText().toString().length() == 0) {
                        //类型 google代表谷歌地图 sci代表切片
                        _intent.putExtra("type", "google");
                        //baseMapPath = "http://map.baidu.com";
                        baseMapPath = "http://www.google.cn/maps";
                    } else {  //用户选择了地图切片
                        //类型 1代表谷歌地图 sci 代表切片
                        _intent.putExtra("type", "sci");
                    }
                    if (queryPrjName()){ return;}
                    saveDataToDB();
                } else {
                    //无用
                    _intent.putExtra("type", "3");
                    updataTime();
                }

                startActivity(_intent);
                finish();
                break;

            case R.id.tvAddBaseMap: {
                Intent _intent1 = new Intent(this, SelectBaseMapActivity.class);
                startActivityForResult(_intent1, RESULT_FILE);
            }

            break;

            //删除项目 //数据库删除 文件夹删除工作空间
            case R.id.btnDelete:
                AlertDialog.Builder _dialog = new AlertDialog.Builder(this);
                _dialog.setTitle("温馨提示");
                _dialog.setMessage("亲，删除工程前请确认已导出此工程数据，否则此工程数据会彻底被删除！");
                _dialog.setIcon(R.drawable.ic_warning);
                _dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] whereArgs = {edtProjName.getText().toString()};
                        DatabaseHelpler.getInstance().delete(SQLConfig.TABLE_NAME_PROJECT_INFO, "Name = ?", whereArgs);

                       /* String _wkName = edtProjName.getText().toString() + ".smwu";
                        String _uddName = edtProjName.getText().toString() + ".udd";
                        String _udpName = edtProjName.getText().toString() + ".udb";
//                        FileUtils.getInstance().deleteDir(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId);
                        FileUtils.getInstance().deleteFile(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId + "/" + _wkName);
                        FileUtils.getInstance().deleteFile(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId + "/" + _uddName);
                        FileUtils.getInstance().deleteFile(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId + "/" + _udpName);*/
//                        boolean _folder = FileUtils.getInstance().deleteDir(SuperMapConfig.DEFAULT_DATA_PATH + whereArgs[0]);

                        ToastUtil.show(ProjectInfoActivity.this, "删除成功", Toast.LENGTH_SHORT);
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
     * @return
     */
    private boolean queryPrjName() {

        Cursor _cursor = DatabaseHelpler.getInstance()
                .query(SQLConfig.TABLE_NAME_PROJECT_INFO, "where Name = '" + edtProjName.getText().toString() + "'");
        if (_cursor.getCount() > 0) {
            ToastUtil.show(this, "工程名已存在，请重新命名工程名", Toast.LENGTH_SHORT);
            return true;
        }
        if (FileUtils.getInstance().isDirExsit(SuperMapConfig.DEFAULT_DATA_PATH + edtProjName.getText().toString())){
            ToastUtil.show(this, "手机中此工程名文件夹已存在，请重新命名工程名", Toast.LENGTH_SHORT);
            return true;
        }


        return  false;
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
        SharedPrefManager _manager = new SharedPrefManager(this,
                SharedPrefManager.FILE_CONFIG);
        String city = (String) _manager.getSharedPreference(SharedPrefManager.KEY_CITY, "");
        ContentValues _values = new ContentValues();
        //工程项目名称，工作空间名称、数据源名称
        _values.put("Name", edtProjName.getText().toString().trim());
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
        DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_PROJECT_INFO, _values);

        if (!edtProjName.getText().toString().isEmpty()) {
            //创建此工程路径的文件夹
            FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + edtProjName.getText().toString());
            FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + edtProjName.getText().toString() + "/" + SuperMapConfig.DEFAULT_DATA_PICTURE_PATH);
            FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + edtProjName.getText().toString() + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
            FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + edtProjName.getText().toString() + "/" + SuperMapConfig.DEFAULT_DATA_SHP_PATH);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_FILE) {
            if (resultCode == RESULT_OK) {
                String _filePath = data.getStringExtra("filePath");
                String _fileName = data.getStringExtra("fileName");
                baseMapPath = _filePath;
                if (!_fileName.endsWith(".sci")) {
                    ToastUtil.showLong(this, "请选择sci的底图类型文件");
                    tvBaseMapPath.setText("");
                } else {
                    tvBaseMapPath.setText(baseMapPath);
                }
            }
        }
    }
}
