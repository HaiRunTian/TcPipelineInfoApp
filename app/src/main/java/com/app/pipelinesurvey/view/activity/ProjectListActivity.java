package com.app.pipelinesurvey.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.ProjectListAdapter;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.bean.FileEntity;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.utills.LogUtills;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述 ProjectListActivity 项目列表页
 * @作者 Kevin
 * @创建日期 2018-05-29  15:56.
 */
public class ProjectListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private TextView tvTitle;
    private ListView lvProjectList;
    private TextView tvReturn;
    private TextView tvNewProject;
    private List<String> list_Prj;
    private ProjectListAdapter m_listAdapter;
    private TextView tvNoPrj;
    private final static int REQUEST_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        initView();
//        initValue();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initValue();
    }

    private void initValue() {
        tvTitle.setText("项目列表" + "(" + getIntent().getStringExtra("model") + ")");
        list_Prj = new ArrayList<>();
        try {
            Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, null, null, null, null, null, "CteateTime desc");
            list_Prj.clear();
            while (_cursor.moveToNext()) {
                //工程名称，工作空间名称
                String _prjName = _cursor.getString(_cursor.getColumnIndex("Name"));
                list_Prj.add(_prjName);
            }
        } catch (Exception e) {
            LogUtills.w(e.getMessage().toString());
        }

        if (list_Prj.size() != 0) {
            tvTitle.setText("项目列表" + "(" + list_Prj.size() + ")");
            m_listAdapter = new ProjectListAdapter(ProjectListActivity.this, list_Prj);
            lvProjectList.setAdapter(m_listAdapter);
        } else {
            tvTitle.setText("项目列表(0)");
            tvNoPrj.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        tvNoPrj = (TextView) findViewById(R.id.tvNoPrj);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvReturn = (TextView) findViewById(R.id.tvReturn);
        tvReturn.setOnClickListener(this);
        tvNewProject = (TextView) findViewById(R.id.tvNewProject);
        tvNewProject.setOnClickListener(this);
        lvProjectList = (ListView) findViewById(R.id.lvProjectList);
        lvProjectList.setOnItemClickListener(this);
        lvProjectList.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReturn:
                finish();
                break;
            case R.id.tvNewProject:
                startActivityForResult(new Intent(this, ProjectInfoActivity.class)
                        .putExtra("from", 0), REQUEST_OK);

                break;
            default:
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, ProjectInfoActivity.class).
                putExtra("proj_name", lvProjectList.getSelectedItem().toString()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 点击item打开工程
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivityForResult(new Intent(this, ProjectInfoActivity.class).
                putExtra("proj_name", list_Prj.get(position))
                .putExtra("from", 1), REQUEST_OK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OK) {
            if (resultCode == 3) {
                m_listAdapter.notifyDataSetChanged();
                String _prjName = data.getStringExtra("proj_id");
                setResult(3, data);
                finish();
            } else if (resultCode == 1 || resultCode == 0) {
                try {
                    Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_PROJECT_INFO, null, null, null, null, null, null);
                    list_Prj.clear();
                    while (_cursor.moveToNext()) {
                        //工程名称，工作空间名称
                        String _prjName = _cursor.getString(_cursor.getColumnIndex("Name"));
                        list_Prj.add(_prjName);
                    }
                    if (list_Prj.size() == 0) {
                        tvNoPrj.setVisibility(View.VISIBLE);
                    } else {
                        tvNoPrj.setVisibility(View.GONE);
                        m_listAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    LogUtills.i(e.toString());
                }
            }
            tvTitle.setText("项目列表" + "(" + list_Prj.size() + ")");

        }
    }

    /**
     * 长按删除
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        final AlertDialog _dialog = new AlertDialog.Builder(this)
                .setTitle("提示 ！")
                .setMessage("确定删除此文件吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] whereArgs = {list_Prj.get(position)};
                        DatabaseHelpler.getInstance().delete(SQLConfig.TABLE_NAME_PROJECT_INFO, "Name = ?", whereArgs);
                       /* String _wkName = whereArgs[0] + ".smwu";
                        String _uddName = whereArgs[0] + ".udd";
                        String _udpName = whereArgs[0] + ".udb";
//                        FileUtils.getInstance().deleteDir(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId);
                        boolean _wk = FileUtils.getInstance().deleteFile(SuperMapConfig.DEFAULT_DATA_PATH + whereArgs[0] + "/" + _wkName);
                        boolean _udd = FileUtils.getInstance().deleteFile(SuperMapConfig.DEFAULT_DATA_PATH + whereArgs[0] + "/" + _uddName);
                        boolean _udp =FileUtils.getInstance().deleteFile(SuperMapConfig.DEFAULT_DATA_PATH + whereArgs[0] + "/" + _udpName);*/
//                        boolean _folder = FileUtils.getInstance().deleteDir(SuperMapConfig.DEFAULT_DATA_PATH + whereArgs[0]);
//                        if (_folder){
                        list_Prj.remove(position);
                        m_listAdapter.notifyDataSetChanged();
                        tvTitle.setText("项目列表" + "(" + list_Prj.size() + ")");
                        ToastyUtil.showSuccessShort(ProjectListActivity.this, "删除成功");
//                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        _dialog.show();
        return true;
    }


}
