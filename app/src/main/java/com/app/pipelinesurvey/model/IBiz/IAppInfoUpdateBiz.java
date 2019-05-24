package com.app.pipelinesurvey.model.IBiz;

import com.app.pipelinesurvey.model.Listener.AppInfoUpdateListener;

/**
 * Created by Kevin on 2018-06-22.
 */

public interface IAppInfoUpdateBiz {
     void getAppInfoFromDB(String tableName, String[] bindArgs, AppInfoUpdateListener updateListener);
}
