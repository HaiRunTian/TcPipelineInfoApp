package com.app.pipelinesurvey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.utils.AppUtil;
import com.app.pipelinesurvey.utils.GetVersionNum;
import com.app.pipelinesurvey.utils.PermissionUtils;

/** 
 *  @描述 GuideActivity app引导页
 *  @作者  Kevin
 *  @创建日期 2018/2/28  15:48.
 */
public class GuideActivity extends BaseActivity {
    //logo图标
    private ImageView m_imgvLogo;
    //图标过渡动画
    Animation _animation;
    private Handler m_handler = new Handler();
    private TextView m_tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //MyApplication.Ins().fixInputMethodManagerLeak(this);
//        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
//                .instanceField("android.view.inputmethod.InputMethodManager", "sInstance")
//                .instanceField("android.view.inputmethod.InputMethodManager", "mLastSrvView")
//                .instanceField("com.android.internal.policy.PhoneWindow$DecorView", "mContext")
//                .instanceField("android.support.v7.widget.SearchView$SearchAutoComplete", "mContext")
//                .build();
    }

    private void initView() {
        m_imgvLogo = (ImageView) findViewById(R.id.imgvLogo);
        AppUtil.handleClickAppIconInBackground(this);
        /*加载logo的渐变动画*/
        m_tvVersion = findViewById(R.id.tvVersion);
        m_tvVersion.setText(GetVersionNum.ins().getLocalVersionName(this));
        _animation = AnimationUtils.loadAnimation(this, R.anim.alpha_app_logo);
        _animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                m_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(GuideActivity.this,LogInActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //启动logo的动画效果
        m_imgvLogo.startAnimation(_animation);
    }
}
