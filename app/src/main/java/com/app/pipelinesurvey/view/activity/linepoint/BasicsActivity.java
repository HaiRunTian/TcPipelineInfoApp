package com.app.pipelinesurvey.view.activity.linepoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.BasicsDapter;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.SQLUtils;

import java.util.ArrayList;

public class BasicsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_return;
    private RadioButton tv_point;
    private RadioButton tv_adjunct;
    private RadioButton tv_feature;
    private GridView gridView;
    private TextView btnAdd;
    private ArrayList<String> list;
    private String table;
    private BasicsDapter dapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basics);
        initView();
        table = SQLConfig.TABLE_NAME_PIPE_INFO;
        initSql(table);
        initData(table);
    }

    private void initSql(String table) {
        //管點
        list = SQLUtils.getAll(table);
        initData(table);
    }

    private void initData(String table) {
        //设置进来就显示的第一个管类
        dapter = new BasicsDapter(this,list,table);
        gridView.setAdapter(dapter);
        dapter.notifyDataSetInvalidated();
    }

    private void initView() {
        list = new ArrayList<>();
        tv_return = ((TextView) findViewById(R.id.tv_return));
        tv_return.setOnClickListener(this);
        tv_point = ((RadioButton) findViewById(R.id.tv_point));
        tv_point.setChecked(true);
        tv_point.setOnClickListener(this);
        tv_adjunct = ((RadioButton) findViewById(R.id.tv_adjunct));
        tv_adjunct.setOnClickListener(this);
        tv_feature = ((RadioButton) findViewById(R.id.tv_feature));
        tv_feature.setOnClickListener(this);
        btnAdd = ((TextView) findViewById(R.id.btnAdd));
        btnAdd.setOnClickListener(this);
        gridView = ((GridView) findViewById(R.id.gridView));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_return:
                finish();
                break;
            case R.id.tv_point:
                list.clear();
                table= SQLConfig.TABLE_NAME_PIPE_INFO;
                initSql(table);
                dapter.notifyDataSetChanged();
                break;
            case R.id.tv_adjunct:
                list.clear();
                table= SQLConfig.TABLE_NAME_APPENDANT_INFO;
                initSql(table);
                dapter.notifyDataSetChanged();
                break;
            case R.id.tv_feature:
                list.clear();
                table= SQLConfig.TABLE_NAME_FEATURE_INFO;
                initSql(table);
                dapter.notifyDataSetChanged();
                break;
            case R.id.btnAdd:
                Intent intent = new Intent(this, AddBasicsActivity.class);
                intent.putExtra("table",table);
                startActivity(intent);
                break;
                default:break;
        }
    }

    @Override
    protected void onResume() {
        dapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onStart() {
        dapter.notifyDataSetChanged();
        super.onStart();
    }
}
