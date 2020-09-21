package com.app.pipelinesurvey.view.activity.linepoint;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.utils.ColorUtls;
import com.app.pipelinesurvey.utils.SQLUtils;
import com.app.pipelinesurvey.view.widget.ColorPickerDialog;

/**
 * 符号设置-线的属性显示和设置
 * 2019年3月19日09:15:03
 */
public class LineAllocationActivity extends AppCompatActivity implements View.OnClickListener {

    private Cursor _cursor;
    private TextView tvReturn;

    private EditText edtSymbolID;
    private EditText edtName;
    private EditText edtLineWidth;
    private EditText edColor;
    private EditText edtType;
    private EditText edtTypeCode;
    private EditText edtLineRemark;
    private EditText edtCity;
    private EditText edtLineTextColor;
    private EditText edtPointTextColor;
    private TextView tvSubmit;
    private String lineTable;
    private String name;
    private int id;
    private Cursor query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_allocation);
        initView();
        //获得表名
        lineTable = getIntent().getStringExtra("lineTable");
        //获得点击的值
        name = getIntent().getStringExtra("name");
        edtType.setText(name);
        initData();
    }
    //配置好后就顯示配置
    private void initData() {
        query  = DatabaseHelpler.getInstance().query(
                lineTable, null, null, null, null, null, null);
        if (query.getCount()!=0) {
            _cursor = DatabaseHelpler.getInstance().query(
                    lineTable, "where typename = '" + name + "'");
            if (_cursor != null) {
                while (_cursor.moveToNext()) {
                    id = _cursor.getInt(_cursor.getColumnIndex("id"));
                    String lineName = _cursor.getString(_cursor.getColumnIndex("name"));
                    String width = _cursor.getString(_cursor.getColumnIndex("width"));
                    String color = _cursor.getString(_cursor.getColumnIndex("color"));
                    String symbolID = String.valueOf(_cursor.getInt(_cursor.getColumnIndex("symbolID")));
                    String typeCode = _cursor.getString(_cursor.getColumnIndex("typeCode"));
                    String remark = _cursor.getString(_cursor.getColumnIndex("remark"));
                    String city = _cursor.getString(_cursor.getColumnIndex("city"));
                    edColor.setText(color);
                    edtName.setText(lineName);
                    edtLineWidth.setText(width);
                    edtSymbolID.setText(symbolID);
                    edtTypeCode.setText(typeCode);
                    edtLineRemark.setText(remark);
                    edtCity.setText(city);
                }
            }
        }
    }
    private void initView() {
        tvReturn = ((TextView) findViewById(R.id.tvReturn));
        tvReturn.setOnClickListener(this);
        tvSubmit = ((TextView) findViewById(R.id.tvSubmit));
        tvSubmit.setOnClickListener(this);
        edtSymbolID = ((EditText)findViewById(R.id.edtSymbolID));
        edtName = ((EditText) findViewById(R.id.edtName));
        edtLineWidth = ((EditText) findViewById(R.id.edtLineWidth));
        edColor = ((EditText) findViewById(R.id.edColor));
        edColor.setOnClickListener(this);
        edtType = ((EditText) findViewById(R.id.edtType));
        edtTypeCode = ((EditText) findViewById(R.id.edtTypeCode));
        edtLineRemark = ((EditText) findViewById(R.id.edtLineRemark));
        edtCity = ((EditText) findViewById(R.id.edtCity));
        edtLineTextColor = ((EditText) findViewById(R.id.edtLineTextColor));
        edtPointTextColor = ((EditText) findViewById(R.id.edtPointTextColor));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvReturn:finish();break;
            case R.id.edColor:
                //顏色設置器
                ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, Color.BLACK);
                builder.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
                    @Override
                    public void onColorPicked(int color) {
                        String rgb = ColorUtls.convertToRGB(color);
                        edColor.setText("#"+rgb);
                    }
                });
                builder.build().show();
                break;
            case R.id.tvSubmit:
                ContentValues _values = new ContentValues();
                _values.put("name", edtName.getText().toString());
                _values.put("width", edtLineWidth.getText().toString());
                _values.put("color", edColor.getText().toString());
                _values.put("symbolID", edtSymbolID.getText().toString());
                _values.put("typename", name);
                _values.put("typeCode", edtTypeCode.getText().toString());
                _values.put("remark", edtLineRemark.getText().toString());
                _values.put("city", edtCity.getText().toString());
                int i = SQLUtils.setLineInfo(lineTable, id, _values);
                if (i==1){
                    Toast.makeText(this, "保存成功.....", Toast.LENGTH_SHORT).show();
                }else if (i==0){
                    Toast.makeText(this, "更新成功.....", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
                default:break;
        }
    }
}
