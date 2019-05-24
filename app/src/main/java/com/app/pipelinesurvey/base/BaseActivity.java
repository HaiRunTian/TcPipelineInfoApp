package com.app.pipelinesurvey.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.app.pipelinesurvey.utils.ActivityUtil;
import com.app.pipelinesurvey.utils.PermissionUtils;
import com.app.utills.LogUtills;
import com.caption.netmonitorlibrary.netStateLib.NetChangeObserver;
import com.caption.netmonitorlibrary.netStateLib.NetStateReceiver;
import com.caption.netmonitorlibrary.netStateLib.NetUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @描述 BaseActivity 活动类基类
 * @作者 Kevin.
 * @创建日期 2018/2/28  15:45.
 */
public class BaseActivity extends AppCompatActivity {
    private static boolean mBackKeyPressed = false;//记录是否有首次按键
    /**
     * 网络观察者
     */
    protected NetChangeObserver mNetChangeObserver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtil.addActivity(this);
//        PermissionUtils.initPermission(this,new PermissionUtils.PermissionHolder());
        // 网络改变的一个回掉类
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                onNetworkDisConnected();
            }
        };
        //开启广播去监听 网络 改变事件
        NetStateReceiver.registerObserver(mNetChangeObserver);

//        Log.d("BaseActivity",getClass().getSimpleName()+"该活动已添加");
    }


    /**
     * 网络连接状态
     *
     * @param type 网络状态
     */
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    ;

    /**
     * 网络断开的时候调用
     */
    protected void onNetworkDisConnected() {

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.removeActivity(this);
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
//        Log.i("BaseActivity",getClass().getSimpleName()+"该活动已移除");
//        LogUtills.i("BaseActivity onDestroy()","onDestroy()");
    }

    protected final <T> T $(int id) {
        return (T) findViewById(id);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (!mBackKeyPressed) {
//            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            mBackKeyPressed = true;
//            //延时两秒，如果超出则擦错第一次按键记录
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    mBackKeyPressed = false;
//                }
//            }, 1500);
//        } else {
//            //退出程序
//            this.finish();
//            System.exit(0);
//        }
    }

}

