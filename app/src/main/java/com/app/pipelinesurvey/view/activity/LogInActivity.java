package com.app.pipelinesurvey.view.activity;

import android.Manifest;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.utils.AESUtils;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.EncryptionAlgorithm;
import com.app.pipelinesurvey.utils.LicenseUtils;
import com.app.pipelinesurvey.utils.LimitByTimeUtil;
import com.app.pipelinesurvey.utils.NetUtils;
import com.app.pipelinesurvey.utils.PermissionUtils;
import com.app.pipelinesurvey.utils.RxDeviceTool;
import com.app.pipelinesurvey.utils.SharedPreferencesUtil;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.view.iview.IGetNetTime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Provider;

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
        PermissionUtils.initPermission(this, new PermissionUtils.PermissionHolder());
        initView();
        //请求网络时间
//        LimitByTimeUtil.ins().getTimeFromNet(this);

    }

    //注册序列号
    private void initRegister() {
        //注册序列号  加密判断
        String password = SharedPreferencesUtil.getString("password", "");
//        if (!password.isEmpty()) {
        //请求序列号
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Service.TELEPHONY_SERVICE);

        Method method = null;
        try {
            method = telephonyManager.getClass().getMethod("getDeviceId", int.class);
            //获取IMEI号
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //获取MEID号
            String meid = (String) method.invoke(telephonyManager, 1);
            if (meid == null) {
                meid = telephonyManager.getDeviceId();
            }

            if (meid == null) {
                meid = RxDeviceTool.getDeviceIdIMEI(LogInActivity.this);
            }
            String password1 = AESUtils.getInstance("1234567890123456").encrypt(meid).trim();
            String password2 = SharedPreferencesUtil.getString("password", "").trim();
            if (!TextUtils.equals(password1, password2)) {
                showMyStyleDialg(meid, password1);
            } else {
                startActivity();
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
                //配置本地许可
                LicenseUtils.ins().configLicense();
//                //判断软件使用时间
                if (NetUtils.isNetworkConnected(this) && nowTime.length() > 0) {
                    //有网情况下用网络时间判断
                    if (!LimitByTimeUtil.ins(LogInActivity.this).isEffectiveDate(nowTime)) {
                        ToastyUtil.showErrorLong(this, "软件试用期已经过期，请购买此软件");
                        return;
                    }
                } else {
                    //无网络用系统时间判断
                    if (!LimitByTimeUtil.ins(LogInActivity.this).isEffectiveDate(DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT))) {
                        ToastyUtil.showErrorLong(this, "软件试用期已经过期，请购买此软件");
                        return;
                    }
                }
                //注册序列号
                initRegister();
//                startActivity();
                break;
            case R.id.tvRememberPassword:
                cbRemeberPass.toggle();
                break;
            case R.id.tvReset:
                edtLogInId.setText("");
                edtPassword.setText("");
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
