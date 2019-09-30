package com.app.pipelinesurvey.view.activity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.LimitByTimeUtil;
import com.app.pipelinesurvey.utils.NetUtils;
import com.app.pipelinesurvey.utils.PermissionUtils;
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
//                //判断软件使用时间
               /* if (NetUtils.isNetworkConnected(this) && nowTime.length() > 0){
                    //有网情况下用网络时间判断
                    if (!LimitByTimeUtil.ins().isEffectiveDate(nowTime)) {
                        ToastyUtil.showErrorLong(this, "软件试用期已经过期，请购买此软件");
                        return;
                    }
                }else {
                    //无网络用系统时间判断
                    if (!LimitByTimeUtil.ins().isEffectiveDate(DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT))) {
                        ToastyUtil.showErrorLong(this, "软件试用期已经过期，请购买此软件");
                        return;
                    }
                }*/

                //注册序列号  加密判断
//                String password = SharedPreferencesUtil.getString("password", "");
//                if (password.isEmpty()){
//                    //请求序列号
//
////                    SharedPreferencesUtil.putString("password",);
//                }

              /*  TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Service.TELEPHONY_SERVICE);
                Method method = null;
                try {
                    method = telephonyManager.getClass().getMethod("getDeviceId", int.class);
                    //获取IMEI号
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    String imei1 = telephonyManager.getDeviceId();
                    String imei2 = (String) method.invoke(telephonyManager, 1);
//获取MEID号
                    String meid = (String) method.invoke(telephonyManager, 2);
                    Log.i("MainActivity","imei1 = " + imei1);
                    Log.i("MainActivity","imei2 = " + imei2);
                    Log.i("MainActivity","MEID = " + meid);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }*/

                startActivity(new Intent(this, HomePageActivity.class).putExtra("loginName", edtLogInId.getText().toString()));
                finish();
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
