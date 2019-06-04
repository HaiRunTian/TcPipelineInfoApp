package com.app.pipelinesurvey.view.fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SharedPrefManager;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.AlertDialogUtil;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.view.activity.MultichoiceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2018-06-25.
 */

public class CitySelectFragment extends Fragment implements AdapterView.OnItemSelectedListener,View.OnClickListener {
    private RadioGroup m_radioGroup;

    private Spinner spCity;
    private Button btnAddCity;
    private Button btnDeleteCity;
    private Button btnSubmit;
    private Button btnSelectPipeTypes;
    private EditText edtPipeTypes;
    private EditText edtCityName;
    private List<String> m_listCity = new ArrayList<>();
    private LinearLayout layoutPipeTypeConfig;
    private Animation m_animation;
    public static final int REQUEST_PIPE_TYPE = 1;
    public static final int REQUEST_DELTE_CITY = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_city_select, container, false);
        //        m_radioGroup = _view.findViewById(R.id.radioGroup);
        //        m_radioGroup.setOnCheckedChangeListener(this);
        spCity = _view.findViewById(R.id.spCity);
        spCity.setOnItemSelectedListener(this);
        btnAddCity = _view.findViewById(R.id.btnAddCity);
        btnAddCity.setOnClickListener(this);
        btnDeleteCity = _view.findViewById(R.id.btnDeleteCity);
        btnDeleteCity.setOnClickListener(this);
        btnSubmit = _view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnSelectPipeTypes = _view.findViewById(R.id.btnSelectPipeTypes);
        btnSelectPipeTypes.setOnClickListener(this);
        edtPipeTypes = _view.findViewById(R.id.edtPipeTypes);
        edtCityName = _view.findViewById(R.id.edtCityName);
        layoutPipeTypeConfig = _view.findViewById(R.id.layoutPipeTypeConfig);
        notifyDataChange();
        return _view;
    }

    private void notifyDataChange() {
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_CITY_LIST,
                new String[]{"city"}, null, null, null, null, null);
        m_listCity.clear();
        while (_cursor.moveToNext()) {
            String city = _cursor.getString(_cursor.getColumnIndex("city"));
            if (!m_listCity.contains(city)) {
                m_listCity.add(city);
            }
        }
        if (m_listCity.size() > 0) {
            ArrayAdapter<String> _adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, m_listCity);
            spCity.setAdapter(_adapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = spCity.getSelectedItem().toString();
        SharedPrefManager _manager = new SharedPrefManager(getActivity(),
                SharedPrefManager.FILE_CONFIG);
        _manager.put(SharedPrefManager.KEY_CITY, text);
        ToastUtil.showShort(getActivity(), "当前城市为:" + text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Intent _intent;
        switch (v.getId()) {
            case R.id.btnAddCity:
                if (btnAddCity.getText().equals("添加城市")) {
                    btnAddCity.setText("取消添加");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnAddCity.setCompoundDrawablesWithIntrinsicBounds(getActivity().getDrawable(R.mipmap.ic_sub_32px), null, null, null);
                    }
                    m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_autotv_show);
                    layoutPipeTypeConfig.startAnimation(m_animation);
                    layoutPipeTypeConfig.setVisibility(View.VISIBLE);
                } else {
                    btnAddCity.setText("添加城市");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnAddCity.setCompoundDrawablesWithIntrinsicBounds(getActivity().getDrawable(R.mipmap.ic_add2_32px), null, null, null);
                    }
                    m_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_autotv_disapper);
                    layoutPipeTypeConfig.startAnimation(m_animation);
                    layoutPipeTypeConfig.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btnDeleteCity:
                _intent = new Intent(getActivity(), MultichoiceActivity.class);
                _intent.putExtra("from", CitySelectFragment.class.getSimpleName() + "0");
                startActivityForResult(_intent, REQUEST_DELTE_CITY);
                break;
            case R.id.btnSelectPipeTypes:
                _intent = new Intent(getActivity(), MultichoiceActivity.class);
                _intent.putExtra("from", CitySelectFragment.class.getSimpleName());
                startActivityForResult(_intent, REQUEST_PIPE_TYPE);
                break;
            case R.id.btnSubmit:
                if (getCityName().length() == 0 || getPipeTypes().length() == 0) {
                    if (getCityName().length() == 0) {
                        edtCityName.setError("请输入城市名称");
                    }
                    if (getPipeTypes().length() == 0) {
                        edtPipeTypes.setError("请选择或手动输入管类");
                    }
                } else {
                    AlertDialogUtil.showDialog(getActivity(), "温馨提示", "提交新增的城市模板？",
                            false, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String cityName = getCityName();
                                    String pipeTypes = getPipeTypes();
                                    ContentValues _valuesCity = new ContentValues();
                                    _valuesCity.put("city", cityName);
                                    DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_CITY_LIST, _valuesCity);
                                    String[] _listPipeTypes = pipeTypes.split("\\+");
                                    for (String _pipeType : _listPipeTypes) {
                                        ContentValues _valuesPipeType = new ContentValues();
                                        _valuesPipeType.put("pipe_type", _pipeType);
                                        _valuesPipeType.put("city", cityName);
                                        DatabaseHelpler.getInstance().insert(SQLConfig.TABLE_NAME_PIPE_TYPE, _valuesPipeType);
                                    }
                                    ToastUtil.showShort(getActivity(), "提交成功");
                                    resetAll();
                                    notifyDataChange();
                                }
                            });
                }
                break;
                default:break;
        }
    }

    private void resetAll() {
        edtPipeTypes.setText("");
        edtCityName.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PIPE_TYPE:
                if (resultCode == 1) {
                    Bundle _bundle = data.getExtras();
                    ArrayList<String> _listPipeType = (ArrayList<String>) _bundle.get("data");
                    if (_listPipeType.size() > 0) {
                        String strList = "";
                        for (String item : _listPipeType) {
                            strList += item + "+";
                        }
                        strList = strList.substring(0, strList.length() - 1);
                        edtPipeTypes.setText(strList);
                    }
                }
                break;
            case REQUEST_DELTE_CITY:
                if (resultCode == 1) {
                    Bundle _bundle = data.getExtras();
                    final ArrayList<String> _listCity = (ArrayList<String>) _bundle.get("data");
                    if (_listCity.size() > 0) {
                        String cities = "";
                        for (String city : _listCity) {
                            cities += city + ",";
                        }
                        AlertDialogUtil.showDialog(getActivity(),
                                "温馨提示", "所选城市（" + cities + "）的管类、点配置、线配置将被删除？",
                                false, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        for (String item : _listCity) {
//                                            DatabaseHelpler.getInstance().delete(SQLConfig.TABLE_NAME_CITY_LIST,
//                                                    "city = ?", new String[]{item});
//                                            DatabaseHelpler.getInstance().delete(SQLConfig.TABLE_NAME_POINT_CONFIG,
//                                                    "city = ?", new String[]{item});
//                                            DatabaseHelpler.getInstance().delete(SQLConfig.TABLE_NAME_LINE_CONFIG,
//                                                    "city = ?", new String[]{item});
//                                        }
                                        ToastUtil.showShort(getActivity(), "删除成功");
                                        notifyDataChange();
                                    }
                                });
                    }
                }
                break;
                default:break;
        }
    }

    private String getCityName() {
        return edtCityName.getText().toString().trim();
    }

    private String getPipeTypes() {
        return edtPipeTypes.getText().toString().trim();
    }
}
