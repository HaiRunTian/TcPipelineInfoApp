package com.app.pipelinesurvey.view.activity.linepoint;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.view.fragment.LinePoint.AppendantFragment;
import com.app.pipelinesurvey.view.fragment.LinePoint.BasicsPointFragment;
import com.app.pipelinesurvey.view.fragment.LinePoint.FeaturePointFragment;


/**
 * 基础配置替换布局
 * @author
 */

public class BasicsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_return;
    private RadioButton tv_point;
    private RadioButton tv_adjunct;
    private RadioButton tv_feature;
    private FrameLayout fl;
    private FragmentManager m_manager;
    private FragmentTransaction m_transaction;
    private AppendantFragment appendantFragment;
    private BasicsPointFragment basicsPointFragment;
    private FeaturePointFragment featurePointFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basics);
        initView();
        tv_point.setChecked(true);
        switchFragment(0,"");
    }
    private void initView() {
        tv_return = ((TextView) findViewById(R.id.tv_return));
        tv_return.setOnClickListener(this);
        fl = ((FrameLayout) findViewById(R.id.fl));
        tv_point = ((RadioButton) findViewById(R.id.tv_point));
        tv_point.setChecked(true);
        tv_point.setOnClickListener(this);
        tv_adjunct = ((RadioButton) findViewById(R.id.tv_adjunct));
        tv_adjunct.setOnClickListener(this);
        tv_feature = ((RadioButton) findViewById(R.id.tv_feature));
        tv_feature.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_return:
                finish();
                break;
            case R.id.tv_point:
                switchFragment(0,"");
                break;
            case R.id.tv_adjunct:
                switchFragment(1,"");
                break;
            case R.id.tv_feature:
                switchFragment(2,"");
                break;
            default:break;
        }
    }
    /**
     * 切换fragment
     *
     * @param id fragment名
     * @return null
     * @datetime 2018-05-16  15:24.
     */
    public void switchFragment(int id,String str) {
        //开启事务
        if (m_manager == null) {
            m_manager = getSupportFragmentManager();
        }
        m_transaction = m_manager.beginTransaction();
        hideFragment(m_transaction);
        showFragment(id,str);
        m_transaction.commit();
    }
    /**
     * 显示对应的fragment
     *
     * @param id fragment名
     * @return null
     * @datetime 2018-05-16  15:25.
     */
    private void showFragment(int id,String prjName) {
        switch (id) {
            case 0:
                if (basicsPointFragment == null) {
                    basicsPointFragment = new BasicsPointFragment();
                    m_transaction.add(R.id.fl, basicsPointFragment);
                } else {
                    hideFragment(m_transaction);
                    m_transaction.show(basicsPointFragment);
                }
                break;
            case 1:
                if (appendantFragment == null) {
                    appendantFragment = new AppendantFragment();
                    m_transaction.add(R.id.fl, appendantFragment);
                } else {
                    hideFragment(m_transaction);
                    m_transaction.show(appendantFragment);
                }
                break;
            case 2:
                if (featurePointFragment == null) {
                    featurePointFragment = new FeaturePointFragment();
                    m_transaction.add(R.id.fl, featurePointFragment);
                } else {
                    hideFragment(m_transaction);
                    m_transaction.show(featurePointFragment);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏所有fragment
     *
     * @param transaction fragment事务
     * @return null
     * @datetime 2018-05-16  15:25.
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (appendantFragment != null) {
            transaction.hide(appendantFragment);
        }
        if (basicsPointFragment != null){
            transaction.hide(basicsPointFragment);
        }
        if (featurePointFragment != null){
            transaction.hide(featurePointFragment);
        }
    }
}
