package com.app.pipelinesurvey.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.utills.LogUtills;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.mapping.MapControl;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by HaiRun on 2019/1/11.
 */

public class MyLocationListener implements LocationListener {
    private Context m_context;
    private MapControl m_mapControl;
    private Point2D m_pos = null;
    private NavigationPanelView m_NavigationPanel;
    private boolean m_isFirst = true;

    /**
     * 构造方法
     * @param context
     * @param mapControl
     * @param navigationPanelView
     */
    public MyLocationListener(Context context, MapControl mapControl,NavigationPanelView navigationPanelView) {
        this.m_context = context;
        this.m_mapControl = mapControl;
        this.m_NavigationPanel = navigationPanelView;
    }

    @Override
    public void onLocationChanged(Location location) {
        //纬度
        double _latitude = location.getLatitude();
        //经度
        double _longitude = location.getLongitude();
        //位置提供器
        String _provider = location.getProvider();
        Point2Ds m_Point2ds = new Point2Ds();
        m_Point2ds.add(new Point2D(_longitude, _latitude));
        //投影坐标系类
        PrjCoordSys _prjCoordSys =  m_mapControl.getMap().getPrjCoordSys();
        Boolean isOk = CoordSysTranslator.forward(m_Point2ds, m_mapControl.getMap().getPrjCoordSys());
        m_pos = m_Point2ds.getItem(0);
        //  将地图中指定点的地图坐标转换为像素坐标。
        Point _point = m_mapControl.getMap().mapToPixel(m_pos);
        m_NavigationPanel.setVisibility(View.VISIBLE);
        m_NavigationPanel.setPoint(_point);
        double[] scales = m_mapControl.getMap().getVisibleScales();
        m_mapControl.panTo(m_pos, 300);
        boolean result = m_mapControl.zoomTo(scales[scales.length - 1], 1000);
        m_mapControl.getMap().refresh();

            Geocoder gc = new Geocoder(m_context, Locale.getDefault());
            List<Address> locationList = null;
            try {
                locationList = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        //得到Address实例
            Address address = locationList.get(0);

            if (address == null) {
                return;
            }
        //得到国家名称
            String countryName = address.getCountryName();
            if (!TextUtils.isEmpty(countryName)) {
                LogUtills.i("国家= ", countryName);
            }
        //省
            String adminArea = address.getAdminArea();
            if (!TextUtils.isEmpty(adminArea)) {
                LogUtills.i("省份= ", adminArea);
            }

        //得到城市名称
            String locality = address.getLocality();
            if (!TextUtils.isEmpty(locality)) {
                LogUtills.i("城市= ", locality);
            }

            for (int i = 0; address.getAddressLine(i) != null; i++) {
                String addressLine = address.getAddressLine(i);
                if (!TextUtils.isEmpty(addressLine)) {
                    if (addressLine.contains(countryName)) {
                        addressLine = addressLine.replace(countryName, "");
                    }
                    if (addressLine.contains(adminArea)) {
                        addressLine = addressLine.replace(adminArea, "");
                    }
                    if (addressLine.contains(locality)) {
                        addressLine = addressLine.replace(locality, "");
                    }
                    if (!TextUtils.isEmpty(addressLine)) {
                        LogUtills.i("addressLine= ", addressLine);
                    }
                }
            }

        //得到周边信息
            String featureName = address.getFeatureName();
            if (!TextUtils.isEmpty(featureName)) {
                LogUtills.i("featureName= ", featureName);
            }
        }


        @Override
        public void onStatusChanged (String provider,int status, Bundle extras){

        }

        @Override
        public void onProviderEnabled (String provider){

        }

        @Override
        public void onProviderDisabled (String provider){

        }

}
