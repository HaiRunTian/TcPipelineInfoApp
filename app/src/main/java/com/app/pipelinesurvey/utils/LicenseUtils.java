package com.app.pipelinesurvey.utils;

import android.content.Context;
import android.util.Log;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Environment;
import com.supermap.data.LicenseStatus;
import com.supermap.data.LicenseType;

import java.io.IOException;
import java.util.Date;

import okhttp3.Request;

/**
 * Created by HaiRun on 2018/12/4.
 * 许可验证类
 */

public class LicenseUtils {

    private static LicenseUtils s_licenseUtils = null;

    public synchronized static LicenseUtils ins() {
        if (s_licenseUtils == null) {
            s_licenseUtils = new LicenseUtils();
        }
        return s_licenseUtils;
    }

    /**
     * 判断许可时间
     *
     * @author HaiRun
     * created at 2018/12/4 15:44
     */
    public boolean judgeLicese() {
        LicenseStatus _status = Environment.getLicenseStatus();
        if (!_status.isLicenseExsit()) {
            ToastUtil.show(MyApplication.Ins(), "许可不存在", 1);

            return false;

        } else {
            if (!_status.isLicenseValid()) {
                ToastUtil.show(MyApplication.Ins(), "许可证无效，请更新许可", 1);

                return false;
            } else { //许可存在，判断时间
                //许可证最后一天时间
                Date _endDate = _status.getExpireDate();
                long _endTime = _endDate.getTime();
                //当天时间
                long _toadyTime = System.currentTimeMillis();
                int _day = (int) ((_endTime - _toadyTime) / 1000 / 60 / 60 / 24);
                if (_day < 3) {
                    //如果软件试用时间低于三天，则跳出提示
                    ToastUtil.show(MyApplication.Ins(), "软件试用时间还有" + _day + "天,请跟技术员联系！", 3);
                }
                return true;
            }
        }
    }


    /**
     *  下载许可，暂时使用了怀阳高速许可证
     */
    public void downLoadLicense(Context context) {
        String url = "http://119.23.66.213:8080//hyrisk/file/pipelicense/SuperMapiMobileTrial.slm";
        String file = SuperMapConfig.LIC_PATH;
        OkHttpUtils.downloadAsync(url, file, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                LogUtills.i("TAG","downloadfailure");
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                LogUtills.i("TAG","downloadSuccess");
                Environment.initialization(context);

            }
        });
    }
}
