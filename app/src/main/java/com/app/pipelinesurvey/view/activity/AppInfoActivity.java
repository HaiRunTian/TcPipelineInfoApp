package com.app.pipelinesurvey.view.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.presenter.AppInfoUpdatePresenter;
import com.app.pipelinesurvey.utils.DateTimeUtil;
import com.app.pipelinesurvey.utils.GetVersionNum;
import com.app.pipelinesurvey.utils.LicenseUtils;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.view.iview.IAppInfoUpdateView;

public class AppInfoActivity extends BaseActivity implements IAppInfoUpdateView {
    private ProgressBar progressBar;
    private TextView tvLastUpdate;
    private TextView m_tvVersion;
    private TextView tvUserDate;
    private AppInfoUpdatePresenter m_presenter = new AppInfoUpdatePresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvLastUpdate = (TextView) findViewById(R.id.tvLastUpdate);
        tvUserDate = (TextView) findViewById(R.id.tvUserDate);
        m_tvVersion = $(R.id.tvVersionName);
        m_tvVersion.setText(GetVersionNum.ins().getLocalVersionName(this));
        findViewById(R.id.linearReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tvGetUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_presenter.update();
            }
        });

        int licenseStateDate = LicenseUtils.ins().getLicenseStateDate();
        tvUserDate.setText(licenseStateDate + "  天");
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.INVISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public String getTableName() {
        return SQLConfig.TABLE_NAME_APP_INFO;
    }

    @Override
    public String[] getBindArgs() {
        return null;
    }

    @Override
    public void showUpdatedSucceeded(Cursor cursor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLastUpdate.setText(DateTimeUtil.setCurrentTime(DateTimeUtil.FULL_DATE_TIME_FORMAT));
            }
        });
        ToastyUtil.showInfoShort(this,"当前信息为最新");
    }

    @Override
    public void showUpdatedFailed(String msg) {
        ToastyUtil.showErrorShort(this, msg);
    }
}
