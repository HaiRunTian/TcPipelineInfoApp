package com.app.pipelinesurvey.view.iview;

import android.database.Cursor;

/**
 * Created by Kevin on 2018-06-22.
 */

public interface IAppInfoUpdateView {
    void showProgressBar();

    void hideProgressBar();

    String getTableName();

    String[] getBindArgs();

    void showUpdatedSucceeded(Cursor cursor);

    void showUpdatedFailed(String msg);
}
