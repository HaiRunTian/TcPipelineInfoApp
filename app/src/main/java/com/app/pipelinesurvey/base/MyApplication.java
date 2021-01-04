package com.app.pipelinesurvey.base;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.crash.CrashHandler;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.location.BaseGPS;
import com.app.pipelinesurvey.utils.AssetsUtils;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.PullXMLUtil;
import com.app.pipelinesurvey.utils.SharedPreferencesUtil;
import com.app.utills.LogUtills;
import com.caption.netmonitorlibrary.netStateLib.NetStateReceiver;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*import com.supermap.mapping.view;*/

/**
 * @author
 */

public class MyApplication extends Application {
    private static Context context;
    private PendingIntent restartIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //初始化数据库
        initDatabase();
        collapse();
        try {
            initConfig();
            SuperMapConfig.initFolders();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtills.e("Application", e.getMessage());
        }


        /*开启网络广播监听*/
        NetStateReceiver.registerNetworkStateReceiver(this);
    }

    private void collapse() {
        CrashHandler.CrashUploader crashUploader = new CrashHandler.CrashUploader() {
            @Override
            public void uploadCrashMessage(ConcurrentHashMap<String, Object> info) {
                //这个地方自定义崩溃信息的上传
            }
        };

        // 以下用来捕获程序崩溃异常
        Intent intent = new Intent();
        intent.setClassName("com.owen.crashhandler", "com.owen.crashhandler.main.MainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        CrashHandler.getInstance().init(this, crashUploader, restartIntent);
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
    }


    public static Context Ins() {
        LogUtills.i("Application =", context.toString());
        return context;
    }

    private void initConfig() {
        AssetsUtils.init(context);
        SharedPreferencesUtil.getInstance(this);
    }

    /**
     * 初始化数据库建表  创建表格
     */
    private void initDatabase() {
        try {
            List<String> _listSQL = PullXMLUtil.parserXML2SqlList(getAssets()
                    .open("database.xml"), "sql_create_table", "sql_create");
            //创建数据库 创建表格
            DatabaseHelpler.getInstance(getApplicationContext(),
                    "PipeLineInfo.db", _listSQL).getWritableDatabase();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        if (context != null) {
            NetStateReceiver.unRegisterNetworkStateReceiver(context);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        super.onLowMemory();
    }

}
