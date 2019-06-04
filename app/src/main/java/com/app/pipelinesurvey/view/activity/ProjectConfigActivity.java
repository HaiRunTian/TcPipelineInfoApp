package com.app.pipelinesurvey.view.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.bean.PipePointConfigInfo;
import com.app.pipelinesurvey.config.SharedPrefManager;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.view.fragment.PointAttrConfigFragment;
import com.app.pipelinesurvey.view.iview.IPointAttrConfigView;

public class ProjectConfigActivity extends BaseActivity implements IPointAttrConfigView {
    private EditText edtPipeType;
    private EditText edtAppendant;
    private EditText edtColor;
    private EditText edtScaleX;
    private EditText edtScaleY;
    private EditText edtAngle;
    private EditText edtSymbolID;
    private EditText edtStandard;
    private EditText edtMinScaleVisible;
    private EditText edtMaxScaleVisble;
    private EditText edtLineWidth;
    private EditText edtID;
    private int position;
    private String city;
    private String pipeType;
    private String previousAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_attr_config);
        initView();
        initData();
    }

    private void initData() {
        Intent _intent = getIntent();
        String fromWhere = _intent.getStringExtra("from");
        previousAction = fromWhere;
        if (fromWhere != null) {
            if (fromWhere.equals(PointAttrConfigFragment.ITEM_CLICK)) {//从点属性配置点击子项进入
                Bundle _bundle = _intent.getExtras();
                PipePointConfigInfo _info = (PipePointConfigInfo) _bundle.get("info");
                position = _bundle.getInt("position");
                if (_info != null) {
                    edtID.setText(String.valueOf(_info.getId()));
                    edtPipeType.setText(_info.getPipeType());
                    edtAppendant.setText(_info.getName());
                    edtColor.setText(_info.getColor());
                    edtScaleX.setText(String.valueOf(_info.getScaleX()));
                    edtScaleY.setText(String.valueOf(_info.getScaleY()));
                    edtAngle.setText(String.valueOf(_info.getAngle()));
                    edtSymbolID.setText(_info.getSymbolID());
                    edtStandard.setText(_info.getStandard());
                    edtMinScaleVisible.setText(String.valueOf(_info.getMinScaleVisible()));
                    edtMaxScaleVisble.setText(String.valueOf(_info.getMaxScaleVisble()));
                    edtLineWidth.setText(String.valueOf(_info.getLineWidth()));
                }
            } else if (fromWhere.equals(PointAttrConfigFragment.ADD_POINT_ATTR)) {//从点属性配置点击新增进入
                city = _intent.getStringExtra("city");
                pipeType = _intent.getStringExtra("pipeType");
                edtPipeType.setText(pipeType);
            }
        }

    }

    private void initView() {
        edtID = (EditText) findViewById(R.id.edtID);
        edtPipeType = (EditText) findViewById(R.id.edtPipeType);
        edtAppendant = (EditText) findViewById(R.id.edtAppendant);
        edtColor = (EditText) findViewById(R.id.edtColor);
        edtScaleX = (EditText) findViewById(R.id.edtScaleX);
        edtScaleY = (EditText) findViewById(R.id.edtScaleY);
        edtAngle = (EditText) findViewById(R.id.edtAngle);
        edtSymbolID = (EditText) findViewById(R.id.edtSymbolID);
        edtStandard = (EditText) findViewById(R.id.edtStandard);
        edtMinScaleVisible = (EditText) findViewById(R.id.edtMinScaleVisible);
        edtMaxScaleVisble = (EditText) findViewById(R.id.edtMaxScaleVisble);
        edtLineWidth = (EditText) findViewById(R.id.edtLineWidth);
        findViewById(R.id.linearReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tvSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousAction.equals(PointAttrConfigFragment.ITEM_CLICK)) {
//                    DatabaseHelpler.getInstance().update(SQLConfig.TABLE_NAME_POINT_CONFIG, getCurrentContentValues(),
//                            "id = ?", new String[]{edtID.getText().toString()});
                    ToastUtil.showShort(ProjectConfigActivity.this, "已保存");
                    updatePointAttrConfigInfo();
                } else if (previousAction.equals(PointAttrConfigFragment.ADD_POINT_ATTR)) {
                    ContentValues _values = getCurrentContentValues();
                    _values.put("city", city);
//                    DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_POINT_CONFIG,_values);
                    ToastUtil.showShort(ProjectConfigActivity.this,"保存成功");
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void updatePointAttrConfigInfo() {
        PointAttrConfigFragment.m_infoList.get(position).setName(getName());
        PointAttrConfigFragment.m_infoList.get(position).setColor(getColor());
        PointAttrConfigFragment.m_infoList.get(position).setScaleX(Double.parseDouble(getScaleX()));
        PointAttrConfigFragment.m_infoList.get(position).setScaleY(Double.parseDouble(getScaleY()));
        PointAttrConfigFragment.m_infoList.get(position).setAngle(Double.parseDouble(getAngle()));
        PointAttrConfigFragment.m_infoList.get(position).setSymbolID(getSymbolID());
        PointAttrConfigFragment.m_infoList.get(position).setStandard(getStandard());
        PointAttrConfigFragment.m_infoList.get(position).setMinScaleVisible(Double.parseDouble(getMinScaleVisible()));
        PointAttrConfigFragment.m_infoList.get(position).setMaxScaleVisble(Double.parseDouble(getMaxScaleVisble()));
        PointAttrConfigFragment.m_infoList.get(position).setLineWidth(Double.parseDouble(getLineWidth()));
        PointAttrConfigFragment.m_infoList.get(position).setPipeType(getPipeType());
    }

    //获取当前界面信息保存为一个配置点的contenvalues
    private ContentValues getCurrentContentValues() {
        ContentValues _values = new ContentValues();
        _values.put("name", getName());
        _values.put("color", getColor());
        _values.put("scaleX", getScaleX());
        _values.put("scaleY", getScaleY());
        _values.put("angle", getAngle());
        _values.put("symbolID", getSymbolID());
        _values.put("standard", getStandard());
        _values.put("minScaleVisible", getMinScaleVisible());
        _values.put("maxScaleVisble", getMaxScaleVisble());
        _values.put("lineWidth", getLineWidth());
        _values.put("pipe_type", getPipeType());
        return _values;
    }

    @Override
    public String getName() {
        return edtAppendant.getText().toString();
    }

    @Override
    public String getColor() {
        return edtColor.getText().toString();
    }

    @Override
    public String getScaleX() {
        return edtScaleX.getText().toString();
    }

    @Override
    public String getScaleY() {
        return edtScaleY.getText().toString();
    }

    @Override
    public String getAngle() {
        return edtAngle.getText().toString();
    }

    @Override
    public String getSymbolID() {
        return edtSymbolID.getText().toString();
    }

    @Override
    public String getStandard() {
        return edtStandard.getText().toString();
    }

    @Override
    public String getMinScaleVisible() {
        return edtMinScaleVisible.getText().toString();
    }

    @Override
    public String getMaxScaleVisble() {
        return edtMaxScaleVisble.getText().toString();
    }

    @Override
    public String getLineWidth() {
        return edtLineWidth.getText().toString();
    }

    @Override
    public String getPipeType() {
        return edtPipeType.getText().toString();
    }
}
