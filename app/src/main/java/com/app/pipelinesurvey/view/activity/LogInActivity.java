package com.app.pipelinesurvey.view.activity;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.AESUtils;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.DeviceIdUtil;
import com.app.pipelinesurvey.utils.LicenseUtils;
import com.app.pipelinesurvey.utils.LimitByTimeUtil;
import com.app.pipelinesurvey.utils.NetUtils;
import com.app.pipelinesurvey.utils.SharedPreferencesUtil;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.view.iview.IGetNetTime;
import com.app.utills.LogUtills;
import com.supermap.data.Environment;

import java.lang.reflect.Method;

/**
 * @描述 LogInActivity 登录页
 * @作者 Kevin
 * @创建日期 2018-05-29  15:56.
 */
public class LogInActivity extends BaseActivity implements View.OnClickListener, IGetNetTime {
    private TextView tvLogIn;
    private TextView tvReset;
    private CheckBox cbRemeberPass;
    private TextView tvRememberPass;
    private EditText edtLogInId;
    private EditText edtPassword;
    private String TAG = "INFO";
    private String nowTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //权限许可
        if (LicenseUtils.ins().configLicense()) {
            Environment.setLicensePath(SuperMapConfig.LIC_PATH);
            Environment.setTemporaryPath(SuperMapConfig.TEMP_PATH);
            Environment.setWebCacheDirectory(SuperMapConfig.WEB_CACHE_PATH);
            Environment.initialization(this);
        }
        //许可证过期，直接下载新的许可证
        LicenseUtils.ins().judgeLicese(this);
        //定义view
        initView();
        //请求网络时间
//        LimitByTimeUtil.ins(this).getTimeFromNet(this);
    }

    //注册序列号
    private void initRegister() {
        //注册序列号  加密判断
//        String password = SharedPreferencesUtil.getString("password", "");
        //请求序列号
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Service.TELEPHONY_SERVICE);
        Method method = null;
        try {
            //获取手机唯一识别码 40位长度
            String meid = DeviceIdUtil.getDeviceId(this);
            LogUtills.i("deviceId = " + meid);
            String password1 = AESUtils.getInstance("1234567890123456").encrypt(meid).trim();
            String password2 = SharedPreferencesUtil.getString("password", "").trim();
            if (!TextUtils.equals(password1, password2)) {
                showMyStyleDialg(meid, password1);
            } else {
                startActivity();
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastyUtil.showErrorLong(this, "申请注册号失败：" + e.toString());
        }

    }

    /**
     * @Params :提示用注册
     * @author :HaiRun
     * @date :2019/10/11  14:33
     */
    private void showMyStyleDialg(String str, String password) {
        View view = LayoutInflater.from(this).inflate(R.layout.register_layout, null);
        final EditText edtRegister = view.findViewById(R.id.edtRegister);
        final EditText edtNum = view.findViewById(R.id.edtNUm);
        edtRegister.setText(str);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(view)
                .setCancelable(false)
                .setPositiveButton("注册登录", null).setNegativeButton("复制验证码", null).create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edtNum.getText().toString().trim();
                if (TextUtils.equals(s, password)) {
                    ToastyUtil.showSuccessShort(LogInActivity.this, "注册成功");
                    SharedPreferencesUtil.putString("password", s);
                    startActivity();
                    dialog.dismiss();
                } else {
                    ToastyUtil.showErrorShort(LogInActivity.this, "注册失败");
                }
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = edtRegister.getText().toString().trim();
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(trim);
                ToastyUtil.showInfoShort(LogInActivity.this, "复制成功");
            }
        });

    }

    private void initView() {
        tvLogIn = (TextView) findViewById(R.id.tvLogIn);
        tvLogIn.setOnClickListener(this);
        tvReset = (TextView) findViewById(R.id.tvReset);
        tvReset.setOnClickListener(this);
        cbRemeberPass = (CheckBox) findViewById(R.id.cbRememberPassword);
        tvRememberPass = (TextView) findViewById(R.id.tvRememberPassword);
        tvRememberPass.setOnClickListener(this);
        tvRememberPass.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        edtLogInId = (EditText) findViewById(R.id.edtLogInId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogIn:
               /* if (NetUtils.isNetworkConnected(this) && nowTime.length() > 0) {
                    //有网情况下用网络时间判断
                    if (!LimitByTimeUtil.ins(LogInActivity.this).isEffectiveDate(nowTime)) {
                        ToastyUtil.showErrorLong(this, "软件试用期已经过期，请购买此软件");
                    } else {
                        //注册序列号
                        initRegister();
                    }
                } else {*/
                //无网络用系统时间判断
                if (!LimitByTimeUtil.ins(LogInActivity.this).isEffectiveDate(DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT))) {
                    ToastyUtil.showErrorLong(this, "软件试用期已经过期，请购买此软件");
                } else {
                    //注册序列号
                    initRegister();
                }
//                }
                edtLogInId.setText("");
                edtPassword.setText("");
//                startActivity();
                break;
            case R.id.tvRememberPassword:
                cbRemeberPass.toggle();
                break;
            case R.id.tvReset:

                break;
            default:
                break;
        }
    }

    private void startActivity() {
        startActivity(new Intent(this, HomePageActivity.class).putExtra("loginName", edtLogInId.getText().toString()));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void getNetTimeOk(String time) {
        nowTime = time;
    }

    @Override
    public void getNetTimeFail(String fail) {

    }
}
