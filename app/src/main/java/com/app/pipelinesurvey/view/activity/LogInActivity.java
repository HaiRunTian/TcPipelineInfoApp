package com.app.pipelinesurvey.view.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.view.iview.IGetNetTime;

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
        LimitByTimeUtil.ins().getTimeFromNet(this);

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
////                //判断软件使用时间
                if (NetUtils.isNetworkConnected(this) && nowTime.length() > 0){
                    //有网情况下用网络时间判断
                    if (!LimitByTimeUtil.ins().isEffectiveDate(nowTime)) {
                        ToastUtil.showShort(this, "软件试用期已经过期，请购买此软件");
                        return;
                    }
                }else {
                    //无网络用系统时间判断
                    if (!LimitByTimeUtil.ins().isEffectiveDate(DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT))) {
                        ToastUtil.showShort(this, "软件试用期已经过期，请购买此软件");
                        return;
                    }
                }
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
