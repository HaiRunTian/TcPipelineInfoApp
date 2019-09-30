package com.app.pipelinesurvey.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.config.SharedPrefManager;
import com.app.pipelinesurvey.utils.AlertDialogUtil;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.view.fragment.HomePageFragment;
import com.app.pipelinesurvey.view.fragment.PersonalPageFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.app.pipelinesurvey.view.fragment.MapFragment;

/**
 * @描述 HomePageActivity 主页面
 * @作者 Kevin
 * @创建日期 2018-05-29  15:55.
 */
public class HomePageActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, TextWatcher {
    //标题
    private TextView tvTitle;
    //用户名
    private TextView tvLoginName;
    //标题栏
    private RelativeLayout layoutTitleBar;
    //底部radiobutton组
    private RadioGroup radioGroupBottom;
    private RadioButton rdbtnHome, rdbtnMap;
    //动画
    private Animation m_animation;
    private HomePageFragment homePageFragment;
    private FragmentManager m_manager;
    private FragmentTransaction m_transaction;
    private List<String> m_datalist = new ArrayList<>();
    // 返回的结果码
    private final static int REQUESTCODE = 1;
    private String currentUser;
    private SharedPrefManager _manager;
    private long intervalTime = 0;
    private String m_prjName;
    private RadioButton rb_home;
    private RadioButton rb_personal;
    private PersonalPageFragment personalPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initView();
//        initAllData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Map<String, Object> _map = MyApplication.quitPrompt(this, intervalTime);
//        if (_map.get("isQuit").equals(true)) {
//            super.onBackPressed();
//        } else {
//            intervalTime = (long) _map.get("intervalTime");
//        }
    }

    private void initData() {
        _manager = new SharedPrefManager(this, SharedPrefManager.FILE_USER);
        currentUser = (String) _manager.getSharedPreference("name", "");
        if (currentUser.length() == 0) {
            View _view = getLayoutInflater().inflate(R.layout.layout_edittext, null);
            final EditText _editText = _view.findViewById(R.id.edtRealName);
            final AlertDialog _dialog = AlertDialogUtil.showDialog(this, "温馨提示", "请完善个人信息",
                    R.mipmap.ic_login_id_32px, _editText, false, null);
            _dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentUser = _editText.getText().toString();
                    if (currentUser.length() == 0) {
                        ToastyUtil.showInfoShort(HomePageActivity.this, "真实姓名不能为空!");
                        return;
                    }
                    _manager.put("name", currentUser);
                    tvLoginName.setText(currentUser);
                    _dialog.dismiss();
                }
            });
        } else {
            tvLoginName.setText(currentUser);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void initView() {

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        layoutTitleBar = (RelativeLayout) findViewById(R.id.layoutTitleBar);
        radioGroupBottom = (RadioGroup) findViewById(R.id.radioGroupBottom);
        radioGroupBottom.setOnCheckedChangeListener(this);
        rdbtnHome = (RadioButton) findViewById(R.id.rdbtHome);
        rdbtnMap = (RadioButton) findViewById(R.id.rdbtMap);

        //2019年5月6日16:57:57
        rb_home = ((RadioButton) findViewById(R.id.rb_home));
        rb_home.setChecked(true);
        switchFragment(0,"");
        rb_personal = ((RadioButton) findViewById(R.id.rb_personal));
    }

    /**
     * 切换fragment
     *
     * @param id fragment名
     * @return null
     * @datetime 2018-05-16  15:24.
     */
    public void switchFragment(int id,String str) {
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
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                    m_transaction.add(R.id.layoutContainer, homePageFragment);
                } else {
                    hideFragment(m_transaction);
                    m_transaction.show(homePageFragment);
                }

                break;

            case 2:
                if (personalPageFragment == null) {
                    personalPageFragment = new PersonalPageFragment();
                    m_transaction.add(R.id.layoutContainer, personalPageFragment);
                } else {
                    hideFragment(m_transaction);
                    m_transaction.show(personalPageFragment);
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
        if (homePageFragment != null) {
            transaction.hide(homePageFragment);
        }
        if (personalPageFragment != null){
            transaction.hide(personalPageFragment);
        }

    }
    private void showTitleBar() {
        if (layoutTitleBar.getVisibility() != View.VISIBLE) {
            m_animation = AnimationUtils.loadAnimation(this, R.anim.alpha_autotv_show);
            layoutTitleBar.startAnimation(m_animation);
            layoutTitleBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideTitleBar() {
        m_animation = AnimationUtils.loadAnimation(this, R.anim.alpha_autotv_disapper);
        layoutTitleBar.startAnimation(m_animation);
        layoutTitleBar.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rdbtHome:
                switchFragment(0,"");
                showTitleBar();
                break;
            case R.id.rdbtMap:
                //默认工作空间
//                switchFragment(1,m_prjName);
                hideTitleBar();
                startActivity(new Intent(HomePageActivity.this,MapActivity.class));
                break;
            case R.id.rb_home:
                switchFragment(0,"");
                showTitleBar();
                break;
            case R.id.rb_personal:
                switchFragment(2,"");
                hideTitleBar();
                break;
            default:
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //        if (autoTvQuickSearch.getText().length() == 0) {
        //            tvSubmit.setText("取消");
        //        } else {
        //            tvSubmit.setText("搜索");
        //        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //新建工程Activity配置了地图后打开地图
        if (resultCode == 2){
            if (requestCode == REQUESTCODE){
//                LogUtills.i("onActivityResult","在Activity中");
                switchFragment(1,data.getStringExtra("proj_id"));
                m_prjName = data.getStringExtra("proj_id");
                rdbtnMap.setChecked(true);
            }
        }else if(resultCode == 3){//从工程列表查看工程信息，再打开工程
            if (requestCode == REQUESTCODE){
                String _projId = data.getStringExtra("proj_id");
//                LogUtills.i("HomePageActivity=="+_projId);
                m_prjName = data.getStringExtra("proj_id");
                switchFragment(1,_projId);
                rdbtnMap.setChecked(true);
            }
        }
    }

}
