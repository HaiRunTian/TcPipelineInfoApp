package com.app.pipelinesurvey.view.activity.linepoint;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.StandardInfo;
import com.app.pipelinesurvey.utils.SQLUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//标准配置
public class StandardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvReturn;
    private EditText edtPoint;
    private EditText edtName;
    private EditText edtAddTime;
    private EditText edtLine;
    private TextView tvSubmit;
    private EditText edtCreator;
    private TextView tvConfiguration;
    private String addName;
    private  int status;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        addName = getIntent().getStringExtra("addName");
        initView();
        initData();
    }

    private void initData() {
        if (addName!=null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            Date date = new Date(System.currentTimeMillis());
            String str = formatter.format(date);
            edtName.setText(addName);
            edtAddTime.setText(str);
        }else {
            name = getIntent().getStringExtra("name");
            StandardInfo standardInfo = SQLUtils.getStandardInfo(name);
            if (standardInfo!=null){
                tvSubmit.setVisibility(View.GONE);
                tvConfiguration.setVisibility(View.VISIBLE);
                edtName.setText(standardInfo.getName());
                edtCreator.setText(standardInfo.getCreator());
                edtAddTime.setText(standardInfo.getTime());
                edtPoint.setText(standardInfo.getPoint());
                edtLine.setText(standardInfo.getLine());
            }
        }
    }
    private void initView() {
        tvReturn = ((TextView) findViewById(R.id.tvReturn));
        tvReturn.setOnClickListener(this);
        tvSubmit = ((TextView) findViewById(R.id.tvSubmit));
        tvSubmit.setOnClickListener(this);
        edtName = ((EditText) findViewById(R.id.edtName));
        edtCreator = ((EditText) findViewById(R.id.edtCreator));
        edtAddTime = ((EditText) findViewById(R.id.edtAddTime));
        edtPoint = ((EditText) findViewById(R.id.edtPoint));
        edtPoint.setOnClickListener(this);
        edtLine = ((EditText) findViewById(R.id.edtLine));
        edtLine.setOnClickListener(this);
        tvConfiguration = ((TextView) findViewById(R.id.tvConfiguration));
        tvConfiguration.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvReturn:finish(); break;
            case R.id.tvSubmit:
                //拿到表名，然后判断是否存在
                ArrayList<String> table = SQLUtils.getStandardtable(edtPoint.getText().toString(), edtLine.getText().toString());

                if (table.contains(edtPoint.getText().toString())){
                    Toast.makeText(this, edtPoint.getText().toString()+"表名已存在....", Toast.LENGTH_SHORT).show();
                }else if (table.contains(edtLine.getText().toString())){
                    Toast.makeText(this, edtLine.getText().toString()+" 表名已存在....", Toast.LENGTH_SHORT).show();
                }else if (edtLine.getText().toString().equals(edtPoint.getText().toString())) {
                    Toast.makeText(this, " 两个表名不能表名相同....", Toast.LENGTH_SHORT).show();
                }else{
                int creator = edtCreator.getText().toString().length();
                int point = edtPoint.getText().toString().length();
                int line = edtLine.getText().toString().length();

                if (creator != 0 || point != 0 || line != 0) {
                    ContentValues _values = new ContentValues();
                    _values.put("name", edtName.getText().toString());
                    _values.put("creator", edtCreator.getText().toString());
                    _values.put("createtime", edtAddTime.getText().toString());
                    _values.put("pointsettingtablesymbol", edtPoint.getText().toString());
                    _values.put("linesettintable", edtLine.getText().toString());
                    _values.put("status",0);

                    int i = SQLUtils.setStandardtable( _values, edtPoint.getText().toString(), edtLine.getText().toString());
                    if (i==1){
                        Toast.makeText(this, "创建成功.....", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "创建失败.....", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(StandardActivity.this, PointActivity.class);
                    intent.putExtra("point", edtPoint.getText().toString());
                    intent.putExtra("line", edtLine.getText().toString());
                    intent.putExtra("standard", edtName.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "输入不能留空....", Toast.LENGTH_SHORT).show();
                }
                }
                break;
            case R.id.tvConfiguration:
                Intent intent = new Intent(StandardActivity.this, PointActivity.class);
                intent.putExtra("point",edtPoint.getText().toString());
                intent.putExtra("line",edtLine.getText().toString());
                intent.putExtra("standard",edtName.getText().toString());
                intent.putExtra("name",name);
                startActivity(intent);
                break;
                default:break;
        }
    }
}
