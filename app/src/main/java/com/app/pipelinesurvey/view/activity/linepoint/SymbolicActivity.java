package com.app.pipelinesurvey.view.activity.linepoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.CityAdapter;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.SQLUtils;
import com.app.utills.LogUtills;

import java.util.ArrayList;

/**
 * 符号设置-标准设置
 * 2019年3月19日09:15:03
 */
public class SymbolicActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvReturn;
    private RecyclerView rv;
    private TextView tvNot;
    private TextView btnAdd;
    private ArrayList<String> list;
    private CityAdapter cityAdapter;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symbolic);
        initView();
        initData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        initData();
        LogUtills.i("onStart()" ,"onstart");
    }

    private void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        //获取到标准配置类 Standard_Info
        list = SQLUtils.getAll(SQLConfig.TABLE_NAME_STANDARD_INFO);
        for (String a:list){
            this.name =a;
        }
        if (list.size()!=0) {
            tvNot.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            cityAdapter = new CityAdapter(this, list);
            rv.setAdapter(cityAdapter);
            cityAdapter.notifyDataSetChanged();
        }else {
            rv.setVisibility(View.GONE);
            tvNot.setVisibility(View.VISIBLE);
        }
    }
    private void initView() {
        list = new ArrayList<>();
        rv = ((RecyclerView) findViewById(R.id.rv));
        tvNot = ((TextView) findViewById(R.id.tvNot));
        tvReturn = ((TextView) findViewById(R.id.tvReturn));
        tvReturn.setOnClickListener(this);
        btnAdd = ((TextView) findViewById(R.id.btnAdd));
        btnAdd.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                View view1 = View.inflate(this, R.layout.symbolic_standard_item, null);
                final EditText edit_add = view1.findViewById(R.id.edit_add);
                TextView tv_Cancel = (TextView) view1.findViewById(R.id.tv_Cancel);
                TextView tv_Add = (TextView) view1.findViewById(R.id.tv_Add);
                dialog.setView(view1)
                        .create();
                final AlertDialog show = dialog.show();
                tv_Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show.dismiss();
                    }
                });
                tv_Add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String aa = edit_add.getText().toString();
                        if (aa.length()!=0) {
                            if (aa.equals(name)) {
                                Toast.makeText(SymbolicActivity.this, "项目名称已存在....", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent intent = new Intent(SymbolicActivity.this, StandardActivity.class);
                                intent.putExtra("addName", edit_add.getText().toString());
                                startActivity(intent);
                                show.dismiss();
                            }
                        }else {
                            Toast.makeText(SymbolicActivity.this, "不能为空....", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.tvReturn:
                finish();
                break;
        }
    }
}
