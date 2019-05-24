package com.app.pipelinesurvey.view.activity.linepoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.LineAdapter;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.SQLUtils;

import java.util.ArrayList;

/**
 * 符号设置-管线
 * 2019年3月19日09:15:03
 */
public class LineActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView line;
    private ArrayList<String> lineList;
    private LineAdapter lintAdapter;
    private TextView tvReturn;
    private String lineTable;
    private int status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        initView();
        lineTable = getIntent().getStringExtra("lineTable");
        String name = getIntent().getStringExtra("name");
        status = SQLUtils.getStatus(name);
        initData();
    }

    private void initData() {
        if (status==1) {
            lineList= SQLUtils.getLine(lineTable);
        }else {

            lineList= SQLUtils.getAll(SQLConfig.TABLE_NAME_PIPE_INFO);
        }
        lintAdapter = new LineAdapter(this, lineList,lineTable);
        line.setAdapter(lintAdapter);
        lintAdapter.notifyDataSetChanged();

        line.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = lineList.get(position);
                Intent intent = new Intent(LineActivity.this, LineAllocationActivity.class);
                intent.putExtra("lineTable", lineTable);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        lintAdapter.notifyDataSetChanged();
        super.onResume();
    }


    private void initView() {
        lineList = new ArrayList<>();
        line = ((ListView) findViewById(R.id.line));
        tvReturn =(TextView)findViewById(R.id.tvReturn);
        tvReturn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvReturn:finish();break;
        }
    }
}
