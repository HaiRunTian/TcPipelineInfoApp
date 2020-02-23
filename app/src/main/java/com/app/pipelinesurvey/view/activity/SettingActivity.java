package com.app.pipelinesurvey.view.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.config.SettingConfig;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.view.fragment.setting.LineSettingFragment;
import com.app.pipelinesurvey.view.fragment.setting.PipeTypeFragment;
import com.app.pipelinesurvey.view.fragment.setting.PointSettingFragment;
import com.app.utills.LogUtills;


/**
 * 点线设置 界面
 *
 * @author HaiRun
 * @time 2019/9/9.14:20
 */
public class SettingActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup rg;
    private FrameLayout frameLayout;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private PointSettingFragment pointSettingFragment;
    private LineSettingFragment lineSettingFragment;
    private PipeTypeFragment pipeTypeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
        initView();
        initValue();
    }

    /**
     * 初始化一些值
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/9/10  15:00
     */
    private void initValue() {
        Intent intent = getIntent();
        //状态码，判断是新建的项目还是已经有的项目
        int status = intent.getIntExtra("status", 0);
        String prjName = intent.getStringExtra("prjName");
        Cursor cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_POINT_SETTING,
                "where prj_name = '" + prjName + "'and city = '" + SuperMapConfig.PROJECT_CITY_NAME + "'");
        if (cursor.getCount() == 0) {
            //新建项目 则新插入表
            LogUtills.i("ddda", "0,重新插入");
            SettingConfig.ins().getPipeContentValues(prjName);
            SettingConfig.ins().getContentValues(prjName);
            SettingConfig.ins().getLineContentValues(prjName);
        }
        switchFragment(0);
    }


    /**
     * 初始化view
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/9/10  11:02
     */
    private void initView() {
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("设置");
        rg = findViewById(R.id.rg_btn);
        rg.check(0);
        rg.setOnCheckedChangeListener(this);
        frameLayout = findViewById(R.id.layoutContainer);
        TextView tvReturn = findViewById(R.id.tvReturn);
        tvReturn.setOnClickListener(this);
        TextView tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);


    }

    /**
     * 切换fragment
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/9/17  11:09
     */
    public void switchFragment(int id) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        showFragment(id);
        transaction.commit();
    }

    private void showFragment(int id) {
        switch (id) {
            case 0:
                if (pipeTypeFragment == null) {
                    pipeTypeFragment = new PipeTypeFragment();
                    transaction.add(R.id.layoutContainer, pipeTypeFragment);
                } else {
                    hideFragment(transaction);
                    transaction.show(pipeTypeFragment);
                }
                break;
            case 1:
                if (pointSettingFragment == null) {
                    pointSettingFragment = new PointSettingFragment();
                    transaction.add(R.id.layoutContainer, pointSettingFragment);
                } else {
                    hideFragment(transaction);
                    transaction.show(pointSettingFragment);
                }

                break;
            case 2:
                if (lineSettingFragment == null) {
                    lineSettingFragment = new LineSettingFragment();
                    transaction.add(R.id.layoutContainer, lineSettingFragment);
                } else {
                    hideFragment(transaction);
                    transaction.show(lineSettingFragment);
                }
                break;
            default:
                break;
        }
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (pipeTypeFragment != null) {
            transaction.hide(pipeTypeFragment);
        }

        if (pointSettingFragment != null) {
            transaction.hide(pointSettingFragment);
        }

        if (lineSettingFragment != null) {
            transaction.hide(lineSettingFragment);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * RadioGroup 选择改变事件
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/9/17  11:07
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_pipe_type:
                switchFragment(0);
                break;
            case R.id.rbtn_point:
                switchFragment(1);
//                ToastyUtil.showInfoShort(SettingActivity.this, "选择了0");
                break;
            case R.id.rbtn_line:
                switchFragment(2);
//                ToastyUtil.showInfoShort(SettingActivity.this, "选择了1");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReturn:
            case R.id.tvSubmit:
                finish();
                break;
            default:
                break;
        }
    }
}
