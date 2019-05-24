package com.app.pipelinesurvey.location;

import android.app.Application;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;

import com.app.pipelinesurvey.base.MyApplication;
import com.supermap.data.Point2D;
import com.supermap.data.PrjCoordSys;
import com.supermap.mapping.MapControl;

/**
 * Created by Los on 2018-08-23 15:21.
 */

public interface BaseGPS {
     /**
      * 开始定位
      */
     void onStart();

     /**
      * 停止定位
      */
     void onStop();


     /**
      * 设置view
      * @param service
      */
     void setNavigationPanel(NavigationPanelView service);

     /**
      * 移动到定位点
      */
     void MoveToGPSLoaction();

     /**
      * 投影坐标系类
      * @param sys
      */
     void setPrjCoordSys(PrjCoordSys sys);

     /**
      * 设置MapControl类
      * @param map
      */
     void setMap(MapControl map);

     /**
      * 获取到 点
      * @return
      */
     Point2D getPoint2D();

     /**
      * 获取方向传感器
      * @return
      */
     SensorEventListener getSensorListener();
}
