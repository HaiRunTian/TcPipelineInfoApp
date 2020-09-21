package com.app.pipelinesurvey.view.activity.linepoint;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.SQLUtils;

public class AddBasicsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSubmit;
    private EditText edtName;
    private EditText edtType;
    private EditText edtTypeCode;
    private TextView tvName;
    private EditText edtLineRemark;
    private String table;
    private String user;
    private int id;
    private String point;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_basics);

        table = getIntent().getStringExtra("table");
        user = getIntent().getStringExtra("name");
        point = getIntent().getStringExtra("point");
        name = getIntent().getStringExtra("type");
        initView();

        initData(table, user,point);
    }

    private void initData(String table, String user,String typeName) {
        //管类
        if (table.equals(SQLConfig.TABLE_NAME_PIPE_INFO)) {
            Cursor  _cursor = DatabaseHelpler.getInstance().query(table,
                    "where name = '" + user + "'");
            while (_cursor.moveToNext()) {
                id = _cursor.getInt(_cursor.getColumnIndex("id"));
                String name = _cursor.getString(_cursor.getColumnIndex("name"));
                String typename = _cursor.getString(_cursor.getColumnIndex("typename"));
                String code = _cursor.getString(_cursor.getColumnIndex("code"));
                String remark = _cursor.getString(_cursor.getColumnIndex("remark"));
                edtName.setText(name);
                edtType.setText(typename);
                edtTypeCode.setText(code);
                edtLineRemark.setText(remark);
            }
        }else {
            tvName.setText(name);
            //点特征或者附属物
            Cursor  _cursor = DatabaseHelpler.getInstance().query(table,
                    "where name = '" + user + "' and typename = '"+typeName+"'");
            while (_cursor.moveToNext()) {
                id = _cursor.getInt(_cursor.getColumnIndex("id"));
                String name = _cursor.getString(_cursor.getColumnIndex("name"));
                String typename = _cursor.getString(_cursor.getColumnIndex("typename"));
                String code = _cursor.getString(_cursor.getColumnIndex("code"));
                String remark = _cursor.getString(_cursor.getColumnIndex("remark"));
                edtName.setText(name);
                edtType.setText(typename);
                edtTypeCode.setText(code);
                edtLineRemark.setText(remark);
            }
        }
    }

    private void initView() {
        tvSubmit = ((TextView) findViewById(R.id.tvSubmit));
        tvSubmit.setOnClickListener(this);
        edtName = ((EditText) findViewById(R.id.edtName));
        edtType = ((EditText) findViewById(R.id.edtType));
        edtTypeCode = ((EditText) findViewById(R.id.edtTypeCode));
        edtLineRemark = ((EditText) findViewById(R.id.edtLineRemark));
        tvName = findViewById(R.id.tvName);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSubmit:
                ContentValues _values = new ContentValues();
                _values.put("name", edtName.getText().toString());
                _values.put("typename", edtType.getText().toString());
                _values.put("code", edtTypeCode.getText().toString());
                _values.put("remark", edtLineRemark.getText().toString());
                int i = SQLUtils.setLineInfo(table, id, _values);
                if (i==1){
                    Toast.makeText(this, "保存成功.....", Toast.LENGTH_SHORT).show();
                }else if (i==0){
                    Toast.makeText(this, "更新成功.....", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            default:
                break;
        }
    }
}
