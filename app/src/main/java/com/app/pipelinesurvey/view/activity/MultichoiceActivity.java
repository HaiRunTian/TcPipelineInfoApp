package com.app.pipelinesurvey.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.LayersItemBaseAdapter;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.view.fragment.CitySelectFragment;
//import com.app.pipelinesurvey.view.fragment.MapFragment;

import java.util.ArrayList;
import java.util.List;

public class MultichoiceActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LayersItemBaseAdapter m_adapter;
    private List<String> m_list;
    private ArrayList<String> selectedItems;
    private int selectedNum;
    private Button btnSelectAll;
    private Button btnDesSelect;
    private Button btnSubmit;
    private TextView tvTitle;
    //    private Button btnCancel;
    private ListView m_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layers);
        initWindowSize();
        initView();
        initData();
    }

    private void initData() {
        m_list = new ArrayList<>();
        selectedItems = new ArrayList<>();
        initTestDate();
        m_adapter = new LayersItemBaseAdapter(m_list, this);
        m_listView.setAdapter(m_adapter);
    }

    private void initView() {
        btnSelectAll = (Button) findViewById(R.id.btnSelectAll);
        btnSelectAll.setOnClickListener(this);
        btnDesSelect = (Button) findViewById(R.id.btnDesSelect);
        btnDesSelect.setOnClickListener(this);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        //        btnCancel = findViewById(R.id.btnCancel);
        //        btnCancel.setOnClickListener(this);
        m_listView = (ListView) findViewById(R.id.lvLayers);
        m_listView.setOnItemClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
    }

    private void initTestDate() {
        String fromWhere = getIntent().getStringExtra("from");
      /*  if (fromWhere != null) {
            if (fromWhere.equals(DrawPipeLineActivity.class.getSimpleName())) {//管线中权属单位数据填充
                tvTitle.setText("权属单位列表");
                if (MapActivity.pipeType.equals("电信-D"))//当管类是电信时
                    m_list = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.ownershipUnit));
                if (MapActivity.pipeType.equals("煤气-M") || MapActivity.pipeType.equals("燃气-R"))//当管类是燃气煤气时
                    m_list = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.ownershipUnit_rq_mq));
            } else if (fromWhere.equals(CitySelectFragment.class.getSimpleName())) {
                tvTitle.setText("管类选择");
                m_list = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.pipeTypeModel));
            } else if (fromWhere.equals(CitySelectFragment.class.getSimpleName() + "0")) {
                tvTitle.setText("删除已有城市");
                Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_CITY_LIST,
                        new String[]{"city"}, null, null, null, null, null);
                while (_cursor.moveToNext()) {
                    String city = _cursor.getString(_cursor.getColumnIndex("city"));
                    if (!m_list.contains(city)) {
                        m_list.add(city);
                    }
                }
            }
        } else {
            for (int i = 1; i <= 10; i++) {
                m_list.add("图层" + i);
            }
        }*/
    }

    private void initWindowSize() {
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = this.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.75);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.75);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;      //设置黑暗度
        this.getWindow().setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectAll:
                if (btnSelectAll.getText().toString().equals("全选")) {
                    btnSelectAll.setText("全不选");
                    for (int i = 0; i < m_list.size(); i++) {
                        LayersItemBaseAdapter.getIsSelected().put(i, true);
                        selectedItems.add(m_list.get(i));
                    }
                    selectedNum = m_list.size();
                    notifyDataChanged();
                } else {
                    btnSelectAll.setText("全选");
                    selectedItems.clear();
                    for (int i = 0; i < m_list.size(); i++) {
                        LayersItemBaseAdapter.getIsSelected().put(i, false);
                        selectedNum--;
                    }
                    notifyDataChanged();
                }
                break;
            case R.id.btnDesSelect:
                for (int i = 0; i < m_list.size(); i++) {
                    if (LayersItemBaseAdapter.getIsSelected().get(i)) {
                        LayersItemBaseAdapter.getIsSelected().put(i, false);
                        selectedItems.remove(m_list.get(i));
                        selectedNum--;
                    } else {
                        LayersItemBaseAdapter.getIsSelected().put(i, true);
                        selectedItems.add(m_list.get(i));
                        selectedNum++;
                    }
                    notifyDataChanged();
                }
                break;
            case R.id.btnSubmit:
                Bundle _bundle = new Bundle();
                _bundle.putStringArrayList("data", selectedItems);
                setResult(1, new Intent().putExtras(_bundle));
                finish();
                break;
            //            case R.id.btnCancel:
            //                finish();
            //                break;

            default:break;
        }
    }

    private void notifyDataChanged() {
        m_adapter.notifyDataSetChanged();
        ToastUtil.showShort(this, "一共选择了" + selectedNum + "项");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LayersItemBaseAdapter.ViewHolder _holder = (LayersItemBaseAdapter.ViewHolder) view.getTag();
        _holder.m_checkBox.toggle();
        LayersItemBaseAdapter.getIsSelected().put(position, _holder.m_checkBox.isChecked());
        if (_holder.m_checkBox.isChecked()) {
            selectedNum++;
            selectedItems.add(m_list.get(position));
        } else {
            selectedNum--;
            selectedItems.remove(m_list.get(position));
        }
        notifyDataChanged();
    }
}
