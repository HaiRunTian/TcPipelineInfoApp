package com.app.pipelinesurvey.model.Listener;

import android.database.Cursor;

/**
 * Created by Kevin on 2018-06-22.
 */

public interface AppInfoUpdateListener {
    void updatedSucceeded(Cursor cursor);

    void updatedFailed(String msg);
}
