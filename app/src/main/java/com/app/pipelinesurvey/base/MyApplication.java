package com.app.pipelinesurvey.base;

import android.app.Application;
import android.content.Context;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.location.BaseGPS;
import com.app.pipelinesurvey.utils.AssetsUtils;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.PullXMLUtil;
import com.app.utills.LogUtills;
import com.caption.netmonitorlibrary.netStateLib.NetStateReceiver;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/*import com.supermap.mapping.view;*/

/**
 * @author
 */

public class MyApplication extends Application {
    private static Context context;
    public BaseGPS m_baseGPS = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //初始化数据库
        initDatabase();
        try {

            initConfig();
            SuperMapConfig.initFolders();
            //百度地图显示，定位用不到这个功能
//            SDKInitializer.initialize(getApplicationContext());

        } catch (Exception e) {
            e.printStackTrace();
            LogUtills.e("Application",e.getMessage());
        }

      /*  //动态注册网络变化广播
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            NetConnectionReceiver netBroadcastReceiver = new NetConnectionReceiver();
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }*/
        /*开启网络广播监听*/
        NetStateReceiver.registerNetworkStateReceiver(this);

    }



    public static Context Ins() {
        LogUtills.i("Application =",context.toString());
        return context;
    }


    private void initConfig() {
        AssetsUtils.init(context);
        if (configLicense()) {
//            Environment.setLicensePath(SuperMapConfig.LIC_PATH);
//            Environment.setTemporaryPath(SuperMapConfig.TEMP_PATH);
//            Environment.setWebCacheDirectory(SuperMapConfig.WEB_CACHE_PATH);
//            Environment.initialization(this);
        }
    }

    /**
     *    配置许可文件
     */
    private boolean configLicense() {
        String license = SuperMapConfig.LIC_PATH + SuperMapConfig.LIC_NAME;
        File m_licenseFile = new File(license);
        if (!m_licenseFile.exists()) {
            InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.LIC_NAME);
            if (is != null) {
                return  FileUtils.getInstance().copy(is, SuperMapConfig.FULL_LIC_PATH);
            }
        }
        return true;
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

//            SharedPrefManager _manager = new SharedPrefManager(getApplicationContext(),
//                    SharedPrefManager.FILE_CONFIG);
//            boolean isInited = (boolean) _manager.getSharedPreference(SharedPrefManager.KEY_IS_DB_INITED, false);
//           if (!isInited) {
//                    InitDatabase.init(getApplicationContext());
//                    _manager.put(SharedPrefManager.KEY_IS_DB_INITED, true);
//            }
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
