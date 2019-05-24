package com.app.pipelinesurvey.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.app.pipelinesurvey.base.BaseActivity;
import com.app.utills.LogUtills;

/**
 * @author HaiRun
 * @time 2019/4/24.9:16
 */

public class TextAcitvity extends BaseActivity {
    final String TAG = "TextActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtills.i(TAG,"onCreate()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtills.i(TAG,"onDestroy()");
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogUtills.i(TAG,"onStart()");
    }


    @Override
    protected void onStop() {
        super.onStop();
        LogUtills.i(TAG,"onStop()");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtills.i(TAG,"onBackPressed()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtills.i(TAG,"onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtills.i(TAG,"onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        LogUtills.i(TAG,"onRestart()");
    }

}
