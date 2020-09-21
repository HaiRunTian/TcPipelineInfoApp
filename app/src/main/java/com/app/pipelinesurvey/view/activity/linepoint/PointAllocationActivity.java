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
import com.app.pipelinesurvey.bean.Symbolbean;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.utils.ColorUtls;
import com.app.pipelinesurvey.view.fragment.map.SymbolDialogFragment;
import com.app.pipelinesurvey.view.widget.ColorPickerDialog;
import com.app.utills.LogUtills;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 符号设置-点的属性显示和设置
 * 2019年3月19日09:15:03
 */
public class PointAllocationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvReturn;
    private String pointTable;
    private String name;
    private TextView tvSubmit;
    private Cursor query;
    private int id;
    private EditText edtName;
    private EditText edColor;
    private EditText edtScaleX;
    private EditText edtScaleY;
    private EditText editAngle;
    private EditText editStandard;
    private EditText edtMinScaleVisible;
    private EditText edtMaxScaleVisble;
    private EditText edtLineWidth;
    private EditText edtTypeName;
    private EditText edtCity;
    private EditText edtSymbolID;
    private String pipename;
    private String m_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_allocation);
        EventBus.getDefault().register(this);
        initView();
        //获得表名
        pointTable = getIntent().getStringExtra("pointTable");
        //获得点击的值
        name = getIntent().getStringExtra("name");
        edtName.setText(name);
        pipename = getIntent().getStringExtra("pipename");
        edtTypeName.setText(pipename);
        String standard; standard = getIntent().getStringExtra("standard");
        editStandard.setText(standard);
        initData();
    }

    private void initData() {
        query = DatabaseHelpler.getInstance().query(
                pointTable, null, null, null, null, null, null);
        if (query.getCount()!=0){
            Cursor _cursor = DatabaseHelpler.getInstance().query(
                    pointTable, "where name like '%" + name + "%' and pipe_type like '%" + pipename + "%'");
            if (_cursor.getCount() != 0) {
                while (_cursor.moveToNext()) {
                    id = _cursor.getInt(_cursor.getColumnIndex("id"));
                    String color = _cursor.getString(_cursor.getColumnIndex("color"));
                    String scaleX = _cursor.getString(_cursor.getColumnIndex("scaleX"));
                    String scaleY = _cursor.getString(_cursor.getColumnIndex("scaleY"));
                    String angle = _cursor.getString(_cursor.getColumnIndex("angle"));
                    String symbolID = String.valueOf(_cursor.getInt(_cursor.getColumnIndex("symbolID")));
                    String minScaleVisible  = _cursor.getString(_cursor.getColumnIndex("minScaleVisible"));
                    String maxScaleVisible = _cursor.getString(_cursor.getColumnIndex("maxScaleVisible"));
                    String lineWidth = _cursor.getString(_cursor.getColumnIndex("lineWidth"));
                    String city = _cursor.getString(_cursor.getColumnIndex("city"));
                    edColor.setText(color);
                    edtScaleX.setText(scaleX);
                    edtScaleY.setText(scaleY);
                    editAngle.setText(angle);
                    edtSymbolID.setText(symbolID);
                    edtMinScaleVisible.setText(minScaleVisible);
                    edtMaxScaleVisble.setText(maxScaleVisible);
                    edtLineWidth.setText(lineWidth);
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
        edtName = ((EditText) findViewById(R.id.edtName));
        edColor = ((EditText) findViewById(R.id.edColor));
        edColor.setOnClickListener(this);
        edtScaleX = ((EditText) findViewById(R.id.edtScaleX));
        edtScaleY = ((EditText) findViewById(R.id.edtScaleY));
        editAngle = ((EditText) findViewById(R.id.editAngle));
        edtSymbolID = ((EditText) findViewById(R.id.edtSymbolID));
        edtSymbolID.setOnClickListener(this);
        editStandard = ((EditText) findViewById(R.id.editStandard));
        edtMinScaleVisible = ((EditText) findViewById(R.id.edtMinScaleVisible));
        edtMaxScaleVisble = ((EditText) findViewById(R.id.edtMaxScaleVisble));
        edtLineWidth = ((EditText) findViewById(R.id.edtLineWidth));
        edtTypeName = ((EditText) findViewById(R.id.edtTypeName));
        edtCity = ((EditText) findViewById(R.id.edtCity));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvReturn:finish();break;
            case R.id.edColor:
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

            case R.id.edtSymbolID:
                SymbolDialogFragment _fragment1 = new SymbolDialogFragment();
                _fragment1.show(getSupportFragmentManager().beginTransaction(),"fragment");

                break;
            case R.id.tvSubmit:
                query  = DatabaseHelpler.getInstance().query(pointTable,"where id="+id);
                LogUtills.i("B",pointTable+"--"+id);
                ContentValues _values = new ContentValues();
                _values.put("name",name);
                _values.put("color",edColor.getText().toString());
                _values.put("scaleX", edtScaleX.getText().toString());
                _values.put("scaleY", edtScaleY.getText().toString());
                _values.put("angle", editAngle.getText().toString());
                _values.put("symbolID", edtSymbolID.getText().toString());
                _values.put("standard", editStandard.getText().toString());
                _values.put("minScaleVisible", edtMinScaleVisible.getText().toString());
                _values.put("maxScaleVisible", edtMaxScaleVisble.getText().toString());
                _values.put("lineWidth", edtLineWidth.getText().toString());
                _values.put("pipe_type", pipename);
                _values.put("city", edtCity.getText().toString());

                if (query.getCount()==0) {
                    DatabaseHelpler.getInstance().insert(pointTable, _values);
                    Toast.makeText(this, "保存成功.....", Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseHelpler.getInstance().update(pointTable, _values, "id="+id, null);
                    Toast.makeText(this, "更新成功.....", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
                default:break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(Symbolbean symbolbean){
            edtSymbolID.setText(symbolbean.getSymId()+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
