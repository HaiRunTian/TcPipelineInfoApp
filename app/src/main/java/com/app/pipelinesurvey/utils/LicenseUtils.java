package com.app.pipelinesurvey.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Environment;
import com.supermap.data.LicenseStatus;
import com.supermap.data.LicenseType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    public void judgeLicese(Context context) {
        LicenseStatus _status = Environment.getLicenseStatus();
        if (!_status.isLicenseExsit()) {
            MyAlertDialog.showAlertDialog(context, "提示", "软件没有获得使用许可，是否在线申请？"
                    , "确定", "取消", false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downLoadLicense(context);
                            dialog.dismiss();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

        } else {
            if (!_status.isLicenseValid()) {
                MyAlertDialog.showAlertDialog(context, "提示", "软件使用许可期限已过期，是否在线更新？"
                        , "确定", "取消", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downLoadLicense(context);
                                dialog.dismiss();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

            } else { //许可存在，判断时间
                //许可证最后一天时间
                Date _endDate = _status.getExpireDate();
                long _endTime = _endDate.getTime();
                //当天时间
                long _toadyTime = System.currentTimeMillis();
                int _day = (int) ((_endTime - _toadyTime) / 1000 / 60 / 60 / 24);
                if (_day < 7) {
                    //如果软件试用时间低于三天，则跳出提示
//                    ToastyUtil.showWarningShort(MyApplication.Ins(), "软件试用时间还有" + _day + "天,请跟技术员联系！");
                    MyAlertDialog.showAlertDialog(context, "提示", "软件试用时间还有" + _day + "天,是否马上连接网络更新软件使用期限？"
                            , "确定", "取消", false, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downLoadLicense(context);
                                    dialog.dismiss();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }

            }
        }
    }

    public int getLicenseStateDate() {
        int _day = 0;
        LicenseStatus _status = Environment.getLicenseStatus();
        if (_status.isLicenseValid()) {
            //许可证最后一天时间
            Date _endDate = _status.getExpireDate();
            long _endTime = _endDate.getTime();
            //当天时间
            long _toadyTime = System.currentTimeMillis();
             _day = (int) ((_endTime - _toadyTime) / 1000 / 60 / 60 / 24);
        }
        return _day;
    }

    /**
     * 下载许可，暂时使用了怀阳高速许可证
     */
    public void downLoadLicense(Context context) {
        String url = "http://119.23.66.213:8080/hyrisk/file/pipelicense/SuperMapiMobileTrial.slm";
        String file = SuperMapConfig.LIC_PATH;
        OkHttpUtils.downloadAsync(url, file, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                LogUtills.i("TAG", "downloadfailure");
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                LogUtills.i("TAG", "downloadSuccess");
                Environment.initialization(context);

            }
        });
    }

    /**
     * 配置许可文件
     */
    public boolean configLicense() {
        String license = SuperMapConfig.LIC_PATH + SuperMapConfig.LIC_NAME;
        File m_licenseFile = new File(license);
        if (!m_licenseFile.exists()) {
            InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.LIC_NAME);
            if (is != null) {
                return FileUtils.getInstance().copy(is, SuperMapConfig.FULL_LIC_PATH);
            }
        }
        return true;
    }
}
