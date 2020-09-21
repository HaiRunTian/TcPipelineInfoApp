package com.app.pipelinesurvey.view.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.PointConfigItemListBaseAdapter;
import com.app.pipelinesurvey.bean.PipePointConfigInfo;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.view.activity.PointAttrConfigActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2018-06-25.
 * 点属性配置文件
 */

public class PointAttrConfigFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private ListView m_lvPointConfig;
    private Spinner spPipeType;
    private Button btnAddPointAtrr;
    private List<String> m_listPipeTypeList = new ArrayList<>();
    public static List<PipePointConfigInfo> m_infoList = new ArrayList<>();
    private PointConfigItemListBaseAdapter m_adapter;
    //    public static final String TABLE_GZ = "pipe_type_gz";
    //    public static final String TABLE_HZ = "pipe_type_hz";
//    public static final String CITY_GZ = "广州";
//    public static final String CITY_HZ = "惠州";
    private String currentCity;
    private static final int REQUEST_ADD_POINT_ATTR = 1;
    public static final String ITEM_CLICK = "ITEM_CLICK";
    public static final String ADD_POINT_ATTR = "ADD_POINT_ATTR";
    public static final int RESULT_OK = -1;

    @Override
    public void onHiddenChanged(boolean hidden) {
//        currentCity = (String) new SharedPrefManager(getActivity(),
//                SharedPrefManager.FILE_CONFIG).getSharedPreference(SharedPrefManager.KEY_CITY, "");
//        if (!currentCity.equals("")) {
//            bundlePipeType(SQLConfig.TABLE_NAME_POINT_LIST);
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_point_attr_config, container, false);
        m_lvPointConfig = _view.findViewById(R.id.lvPointConfig);
        m_lvPointConfig.setOnItemClickListener(this);
        spPipeType = _view.findViewById(R.id.spPipeType);
        spPipeType.setOnItemSelectedListener(this);
        btnAddPointAtrr = _view.findViewById(R.id.btnAddPointAtrr);
        btnAddPointAtrr.setOnClickListener(this);
        initData();
        return _view;
    }

    private void initData() {
//        currentCity = (String) new SharedPrefManager(getActivity(),
//                SharedPrefManager.FILE_CONFIG).getSharedPreference(SharedPrefManager.KEY_CITY, "");
//        if (currentCity.length() == 0) {
//            ToastyUtil.showShort(getActivity(), "请选择城市");
//        } else {
//            bundlePipeType(SQLConfig.TABLE_NAME_POINT_LIST);
//        }
    }

    private void bundlePipeType(String table_name) {
        Cursor _cursor = DatabaseHelpler.getInstance()
                .query(table_name);
        m_listPipeTypeList.clear();

        while (_cursor.moveToNext()) {
            String pipe_type = _cursor.getString(_cursor.getColumnIndex("point"));

            if (!m_listPipeTypeList.contains(pipe_type)) {
                m_listPipeTypeList.add(pipe_type);
            }
        }

        ArrayAdapter<String> _adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, m_listPipeTypeList);
        spPipeType.setAdapter(_adapter);

    }

    //点击查询信息
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle _bundle = new Bundle();
        _bundle.putSerializable("info", m_infoList.get(position));
        _bundle.putInt("position", position);
        _bundle.putString("from", PointAttrConfigFragment.ITEM_CLICK);
        Intent _intent = new Intent(getActivity(), PointAttrConfigActivity.class);
        _intent.putExtras(_bundle);
        startActivity(_intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        notifyDataChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddPointAtrr:
                String pipeType = spPipeType.getSelectedItem().toString();
                Intent _intent = new Intent(getActivity(), PointAttrConfigActivity.class);
                _intent.putExtra("from", PointAttrConfigFragment.ADD_POINT_ATTR);
                _intent.putExtra("pipeType", pipeType);
//                _intent.putExtra("city", currentCity);
                startActivityForResult(_intent, REQUEST_ADD_POINT_ATTR);
                break;

            default:
                break;
        }
    }

    private void notifyDataChanged() {
        String pipeType = spPipeType.getSelectedItem().toString();
        m_infoList.clear();
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_DEFAULT_POINT_SETTING,
                "where pipe_type = '" + pipeType + "'");
        while (_cursor.moveToNext()) {
            PipePointConfigInfo _info = new PipePointConfigInfo();
            _info.setId(_cursor.getInt(_cursor.getColumnIndex("id")));
            _info.setName(_cursor.getString(_cursor.getColumnIndex("name")).substring(2));
            _info.setColor(_cursor.getString(_cursor.getColumnIndex("color")));
            _info.setScaleX(_cursor.getDouble(_cursor.getColumnIndex("scaleX")));
            _info.setScaleY(_cursor.getDouble(_cursor.getColumnIndex("scaleY")));
            _info.setAngle(_cursor.getDouble(_cursor.getColumnIndex("angle")));
            _info.setSymbolID(_cursor.getString(_cursor.getColumnIndex("symbolID")));
            _info.setStandard(_cursor.getString(_cursor.getColumnIndex("standard")));
            _info.setMinScaleVisible(_cursor.getDouble(_cursor.getColumnIndex("minScaleVisible")));
            _info.setMaxScaleVisble(_cursor.getDouble(_cursor.getColumnIndex("maxScaleVisble")));
            _info.setLineWidth(_cursor.getDouble(_cursor.getColumnIndex("lineWidth")));
            _info.setPipeType(_cursor.getString(_cursor.getColumnIndex("pipe_type")));
            m_infoList.add(_info);
        }
        if (m_infoList.size() > 0) {
            m_adapter = new PointConfigItemListBaseAdapter(getActivity(), m_infoList);
            m_lvPointConfig.setAdapter(m_adapter);
        } else {
            m_adapter = new PointConfigItemListBaseAdapter(getActivity(), m_infoList);
            m_lvPointConfig.setAdapter(m_adapter);
            ToastyUtil.showInfoShort(getActivity(), "未检索到数据");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_POINT_ATTR:
                if (resultCode == RESULT_OK) {
                    notifyDataChanged();
                }
                break;
        }
    }
}
