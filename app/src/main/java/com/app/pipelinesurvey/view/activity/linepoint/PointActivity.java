package com.app.pipelinesurvey.view.activity.linepoint;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.view.fragment.LinePoint.PointFragment;
import com.app.pipelinesurvey.view.fragment.LinePoint.lineFragment;

import java.util.ArrayList;

/**
 * 符号设置-管点
 * 2019年3月19日09:15:03
 */

public class PointActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView type_return;
    private RadioGroup rg_btn;
    private RadioButton type_point;
    private RadioButton type_line;
    private ArrayList<Fragment> fragments;
    private FrameLayout fragmentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_secondary_symbolic);
        initView();
        rg_btn.check(R.id.type_point);
        switchFragment(0);
        //设置点击的时候的切换Fragment
        SetOnCheckedChangeListener();
    }
    private void SetOnCheckedChangeListener() {
        rg_btn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.type_point:
                        switchFragment(0);
                        break;
                    case R.id.type_line:
                        switchFragment(1);
                        break;
                   default:break;
                }
            }
        });
    }
    private void switchFragment(int positions) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (i==positions){
                //显示fragment
                if (fragment.isAdded()){
                    //如果这个fragment已经被事务添加,显示
                    fragmentTransaction.show(fragment);
                }else{
                    //如果这个fragment没有被事务添加过,添加
                    fragmentTransaction.add(R.id.fragmentLayout,fragment);
                }
            }else{
                //隐藏fragment
                if (fragment.isAdded()){
                    //如果这个fragment已经被事务添加,隐藏
                    fragmentTransaction.hide(fragment);
                }
            }
        }
        //提交事务
        fragmentTransaction.commit();
    }
    private void initView() {
        type_return = ((TextView) findViewById(R.id.type_return));
        type_return.setOnClickListener(this);
        rg_btn = ((RadioGroup) findViewById(R.id.rg_btn));
        type_point = ((RadioButton) findViewById(R.id.type_point));
        type_line = ((RadioButton) findViewById(R.id.type_line));
        fragmentLayout = ((FrameLayout) findViewById(R.id.fragmentLayout));

        fragments = new ArrayList<>();
        fragments.add(new PointFragment());
        fragments.add(new lineFragment());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.type_return:
                finish();
                break;
            default:break;
        }
    }
}
