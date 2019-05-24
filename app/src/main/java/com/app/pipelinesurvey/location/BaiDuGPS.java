package com.app.pipelinesurvey.location;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.utills.LogUtills;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

/**
 * Created by Los on 2018-08-23 15:23.
 */

public class BaiDuGPS implements BaseGPS {
    private LocationService locationService = null;
    private NavigationPanelView m_NavigationPanel = null;
    private SensorManager m_SensorManager = null;
    private PrjCoordSys m_priCoordSys = null;
    private MapControl m_mapCtrl = null;
    private Point2D m_pos = null;
    private String m_city;
    private String m_district;
    private String m_street;
    private String m_ddr;
    private boolean m_fist = false;

    public BaiDuGPS(Context context) {
//        locationService = MyApplication.getLocationService();
        locationService = new LocationService(context);
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener BaiduListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                try {
                    StringBuffer sb = new StringBuffer(256);
                    sb.append("time : ");
                    /**
                     * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                     * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                     */
                    sb.append(location.getTime());
                    // 定位类型
                    sb.append("\nlocType : ");
                    sb.append(location.getLocType());
                    // *****对应的定位类型说明*****
                    sb.append("\nlocType description : ");
                    sb.append(location.getLocTypeDescription());
                    // 纬度
                    sb.append("\nlatitude : ");
                    sb.append(location.getLatitude());
                    // 经度
                    sb.append("\nlontitude : ");
                    sb.append(location.getLongitude());
                    // 半径
                    sb.append("\nradius : ");
                    sb.append(location.getRadius());
                    // 国家码
                    sb.append("\nCountryCode : ");
                    sb.append(location.getCountryCode());
                    // 国家名称
                    sb.append("\nCountry : ");
                    sb.append(location.getCountry());
                    // 城市编码
                    sb.append("\ncitycode : ");
                    sb.append(location.getCityCode());
                    // 城市
                    sb.append("\ncity : ");
                    sb.append(location.getCity());
                    // 区
                    sb.append("\nDistrict : ");
                    sb.append(location.getDistrict());
                    // 街道
                    sb.append("\nStreet : ");
                    sb.append(location.getStreet());
                    // 地址信息
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    // *****返回用户室内外判断结果*****
                    sb.append("\nUserIndoorState: ");
                    sb.append(location.getUserIndoorState());
                    sb.append("\nDirection(not all devices have value): ");
                    // 方向
                    sb.append(location.getDirection());
                    sb.append("\nlocationdescribe: ");
                    // 位置语义化信息
                    sb.append(location.getLocationDescribe());
                    // POI信息
                    sb.append("\nPoi: ");
                    m_city = location.getCity();
                    m_district = location.getDistrict();
                    m_street = location.getStreet();
                    m_ddr = location.getAddrStr();
                    LogUtills.i("location", sb.toString());
//                LogUtills.i("地址信息",m_city+"\n"+m_district+"\n"+m_street+"\n"+m_ddr);

                    if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                        for (int i = 0; i < location.getPoiList().size(); i++) {
                            Poi poi = (Poi) location.getPoiList().get(i);
                            sb.append(poi.getName() + ";");
                        }
                    }
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    final Point2Ds m_Point2ds = new Point2Ds();
                    m_Point2ds.add(new Point2D(lng, lat));
                    //同一坐标系下点坐投影坐标系
                    if (!CoordSysTranslator.forward(m_Point2ds, m_priCoordSys)) {

                    }
                    m_pos = m_Point2ds.getItem(0);
                    Point _point = m_mapCtrl.getMap().mapToPixel(m_pos);
                    m_NavigationPanel.setPoint(_point);
                    double[] scales = m_mapCtrl.getMap().getVisibleScales();
                    if (!m_fist) {
                        m_mapCtrl.panTo(m_pos, 300);
                        m_mapCtrl.getMap().setScale(scales[scales.length - 3]);
//                        m_mapCtrl.zoomTo(scales[scales.length - 2],300);
                        m_fist = true;
                    }
                    m_mapCtrl.getMap().refresh();
                } catch (Exception e) {
                    LogUtills.e("GPS111", e.getMessage() + "\r" + e.toString());
                }

            }
        }
    };


    @Override
    public void onStart() {
        try {
            m_NavigationPanel.setVisibility(View.VISIBLE);
            if (locationService != null && locationService.isStart()) {
                return;
            }
            //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
//                locationService = MyApplication.getLocationService();
            locationService.registerListener(BaiduListener);
            //注册监听
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            locationService.start();// 定位SDK
        } catch (Exception e) {
            LogUtills.i("GPS2222", e.getMessage());
        }
    }

    @Override
    public void onStop() {
        if (locationService.isStart()) {
            m_fist = false;
            locationService.stop();
            m_NavigationPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void setNavigationPanel(NavigationPanelView service) {
        m_NavigationPanel = service;
    }

    @Override
    public void MoveToGPSLoaction() {
         /*Rectangle2D _rect =  new Rectangle2D(m_pos, new Size2D(2000,2000));
        m_map.setViewBounds(_rect);
       m_map.refresh();*/
    }


    @Override
    public SensorEventListener getSensorListener() {
        return m_BaiDuSensor;
    }

    @Override
    public void setPrjCoordSys(PrjCoordSys sys) {
        m_priCoordSys = sys;

    }

    @Override
    public void setMap(MapControl map) {
        m_mapCtrl = map;
    }

    @Override
    public Point2D getPoint2D() {
        return m_pos;
    }
///
//    public final SensorListener m_BaiDuSensor = new SensorListener() {
//
//        @Override
//        public void onSensorChanged(int sensor, float[] values) {
//            synchronized (this) {
//                m_NavigationPanel.setValue(values);
//                if (Math.abs(values[0] - 0.0f) < 1)   return;
//                if (m_NavigationPanel != null) {
//                    m_NavigationPanel.invalidate();
//                }
//            }
//        }
//        public void onAccuracyChanged(int sensor, int accuracy) {
//        }
//    };

    /**
     * 方向传感器
     */
    private SensorEventListener m_BaiDuSensor = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                synchronized (this) {
                    m_NavigationPanel.setValue(event.values);
                    if (Math.abs(event.values[0] - 0.0f) < 1) {
                        return;
                    }
                    if (m_NavigationPanel != null) {
                        m_NavigationPanel.invalidate();
                    }
                }
//                // 获取当前传感器获取到的角度
//                float degree = -event.values[0];
//                // 通过补间动画旋转角度 从上次的角度旋转
//                RotateAnimation ra = new RotateAnimation(startDegree, degree,
//                        Animation.RELATIVE_TO_SELF, 0.5f,
//                        Animation.RELATIVE_TO_SELF, 0.5f);
//                ra.setDuration(200);
//                iv_compass.startAnimation(ra);
//                // 记录当前旋转后的角度
//                startDegree = degree;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
