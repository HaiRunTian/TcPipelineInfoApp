package com.app.pipelinesurvey.base;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.pipelinesurvey.utils.ActivityUtil;
import com.caption.netmonitorlibrary.netStateLib.NetChangeObserver;
import com.caption.netmonitorlibrary.netStateLib.NetStateReceiver;
import com.caption.netmonitorlibrary.netStateLib.NetUtils;

/**
 * @描述 BaseActivity 活动类基类
 * @作者 Kevin.
 * @创建日期 2018/2/28  15:45.
 */
public class BaseActivity extends Activity {

    /**
     * 网络观察者
     */
    protected NetChangeObserver mNetChangeObserver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtil.addActivity(this);
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

    /**
     * 网络断开的时候调用
     */
    protected void onNetworkDisConnected() { };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.removeActivity(this);
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
    }

    protected final <T> T $(int id) {
        return (T) findViewById(id);
    }

}

