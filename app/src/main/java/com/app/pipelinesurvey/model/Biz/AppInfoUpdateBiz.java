package com.app.pipelinesurvey.model.Biz;

import android.os.Handler;

import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.model.IBiz.IAppInfoUpdateBiz;
import com.app.pipelinesurvey.model.Listener.AppInfoUpdateListener;

/**
 * Created by Kevin on 2018-06-22.
 */

public class AppInfoUpdateBiz implements IAppInfoUpdateBiz {

    @Override
    public void getAppInfoFromDB(final String tableName, final String[] bindArgs, final AppInfoUpdateListener updateListener) {
        if (updateListener != null) {
            Handler _handler = new Handler();
            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateListener.updatedSucceeded(DatabaseHelpler.getInstance().rawQuery("select * from " + tableName, bindArgs));
                }
            }, 2000);
        }
    }
}
