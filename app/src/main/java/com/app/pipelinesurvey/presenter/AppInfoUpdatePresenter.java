package com.app.pipelinesurvey.presenter;

import android.database.Cursor;

import com.app.pipelinesurvey.model.Biz.AppInfoUpdateBiz;
import com.app.pipelinesurvey.model.IBiz.IAppInfoUpdateBiz;
import com.app.pipelinesurvey.model.Listener.AppInfoUpdateListener;
import com.app.pipelinesurvey.view.iview.IAppInfoUpdateView;

/**
 * Created by Kevin on 2018-06-22.
 */

public class AppInfoUpdatePresenter {
    IAppInfoUpdateView m_appInfoUpdateView;
    IAppInfoUpdateBiz m_appInfoUpdateBiz;

    public AppInfoUpdatePresenter(IAppInfoUpdateView appInfoUpdateView) {
        this.m_appInfoUpdateView = appInfoUpdateView;
        m_appInfoUpdateBiz = new AppInfoUpdateBiz();
    }

    public void update() {
        m_appInfoUpdateView.showProgressBar();
        m_appInfoUpdateBiz.getAppInfoFromDB(m_appInfoUpdateView.getTableName(),
                m_appInfoUpdateView.getBindArgs(), new AppInfoUpdateListener() {
            @Override
            public void updatedSucceeded(Cursor cursor) {
                if (cursor.moveToNext()) {
                    m_appInfoUpdateView.showUpdatedSucceeded(cursor);
                }
                m_appInfoUpdateView.hideProgressBar();
            }

            @Override
            public void updatedFailed(String msg) {
                m_appInfoUpdateView.showUpdatedFailed(msg);
                m_appInfoUpdateView.hideProgressBar();
            }
        });
    }
}
