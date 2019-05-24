package com.app.pipelinesurvey.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.ConfigItemsBaseAdapter;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.view.fragment.CitySelectFragment;
import com.app.pipelinesurvey.view.fragment.PipeTypeFragment;
import com.app.pipelinesurvey.view.fragment.PointAttrConfigFragment;
import com.app.pipelinesurvey.view.fragment.SettingGroupfragment;
import com.app.pipelinesurvey.view.fragment.UnzipFragment;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView tvTitle;
    private TextView tvSubmit;
    private LinearLayout linearReturn;
    private ListView lvConfigItems;
    private List<String> cities;
    private ConfigItemsBaseAdapter m_adapter;
    private FragmentManager m_manager;
    private FragmentTransaction m_transaction;
    private CitySelectFragment m_citySelectFragment;
    private PointAttrConfigFragment m_pointAttrConfigFragment;
    private SettingGroupfragment m_settingGroupfragment;
    private UnzipFragment m_unzipFragment;
    private PipeTypeFragment m_pipeTypeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initValue();
    }

    private void initValue() {
        cities = new ArrayList<>();
//        cities.add("城市");
        cities.add("项目配置");
        cities.add("点属性配置");
        cities.add("线属性配置");
        cities.add("解压切片");
//        cities.add("管类配置");

        m_adapter = new ConfigItemsBaseAdapter(this, cities);
        lvConfigItems.setAdapter(m_adapter);
        lvConfigItems.setItemChecked(0,true);
        switchFragment(3);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("设置");
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSubmit.setText("");
        linearReturn = (LinearLayout) findViewById(R.id.linearReturn);
        linearReturn.setOnClickListener(this);
        lvConfigItems = (ListView) findViewById(R.id.lvConfigItems);
        lvConfigItems.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearReturn:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
//            case 0:
//                switchFragment(0);
//                break;
            case 0:
                switchFragment(3);
                break;
            case 1:
                switchFragment(1);
                break;
            case 2:
                switchFragment(2);
                ToastUtil.showShort(this, "当前点中" + cities.get(position));
                break;
            //解压
            case 3:
                switchFragment(4);
                break;
            //管类配置
//            case 5:
//                switchFragment(5);
//                break;
        }
    }

    public void switchFragment(int id) {
        if (m_manager == null) {
            m_manager = getSupportFragmentManager();
        }
        m_transaction = m_manager.beginTransaction();
        hideFragment(m_transaction);
        showFragment(id);
        m_transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (m_citySelectFragment != null) {
            transaction.hide(m_citySelectFragment);
        }
        if (m_pointAttrConfigFragment != null) {
            transaction.hide(m_pointAttrConfigFragment);
        }
        if(m_settingGroupfragment != null){
            transaction.hide(m_settingGroupfragment);

        }if (m_unzipFragment != null){
            transaction.hide(m_unzipFragment);
        }
        if (m_pipeTypeFragment != null){
            transaction.hide(m_pipeTypeFragment);
        }
    }

    private void showFragment(int id) {
        switch (id) {
            case 0:
                if (m_citySelectFragment == null) {
                    m_citySelectFragment = new CitySelectFragment();
                    m_transaction.add(R.id.layoutContainer, m_citySelectFragment);
                } else
                    m_transaction.show(m_citySelectFragment);
                break;
            case 1:
                if (m_pointAttrConfigFragment == null) {
                    m_pointAttrConfigFragment = new PointAttrConfigFragment();
                    m_transaction.add(R.id.layoutContainer, m_pointAttrConfigFragment);
                } else
                    m_transaction.show(m_pointAttrConfigFragment);
                break;

            case 2:
                if (m_pointAttrConfigFragment == null) {
                    m_pointAttrConfigFragment = new PointAttrConfigFragment();
                    m_transaction.add(R.id.layoutContainer, m_pointAttrConfigFragment);
                } else
                    m_transaction.show(m_pointAttrConfigFragment);
                break;

            case 3:
                if (m_settingGroupfragment == null) {
                    m_settingGroupfragment = new SettingGroupfragment();
                    m_transaction.add(R.id.layoutContainer, m_settingGroupfragment);
                } else
                    m_transaction.show(m_settingGroupfragment);
                break;

            case 4:
                if (m_unzipFragment == null) {
                    m_unzipFragment = new UnzipFragment();
                    m_transaction.add(R.id.layoutContainer, m_unzipFragment);
                } else
                    m_transaction.show(m_unzipFragment);
                break;

            case 5:
                if (m_pipeTypeFragment == null){
                    m_pipeTypeFragment = new PipeTypeFragment();
                    m_transaction.add(R.id.layoutContainer,m_pipeTypeFragment);
                }else m_transaction.show(m_pipeTypeFragment);
                break;

            default:
                break;
        }
    }
}
