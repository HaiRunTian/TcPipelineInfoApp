package com.app.BaseInfo.Oper;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;

import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.app.pipelinesurvey.view.activity.DrawPipePointActivity;
import com.app.utills.LogUtills;
import com.supermap.data.Geometry;
import com.supermap.data.Workspace;
import com.supermap.mapping.Action;
import com.supermap.mapping.GeometryAddedListener;
import com.supermap.mapping.GeometryEvent;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

import javax.xml.parsers.FactoryConfigurationError;

/**
 * Created by Los on 2018-06-19 11:47.
 */

public class MapOperation {

    private Layer m_lineLay       =   null;
    private Layer m_pointLay      =   null;
    private MapControl m_mapCtrl    =   null;
    private Workspace m_workSpace   =   null;
    private FragmentActivity m_Framactive = null;

    public  MapOperation(MapControl mapctrl,FragmentActivity activity){
        m_mapCtrl = mapctrl;
        m_workSpace = WorkSpaceUtils.getInstance().getWorkspace();

       /* m_mapCtrl.addGeometryAddedListener(new PointGeometryAdd());
        m_mapCtrl.setOnTouchListener(new GeometryOnTouchListner());
        m_Framactive = activity;*/
        //m_mapCtrl.getMap().getTrackingLayer();
    }

    class  GeometryOnTouchListner implements View.OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
//            LogUtills.i("。。。。set onTouch");
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP: {
//                    LogUtills.i("。。。。before set onTouch ACTION_UP");
                    if(m_mapCtrl.getAction().toString() != Action.CREATEPOINT.toString()) {return false;}
//                    LogUtills.i("。。。。set onTouch ACTION_UP");
                    float x = motionEvent.getRawX();
                    float y = motionEvent.getRawY();
                    Geometry _geo = m_mapCtrl.getCurrentGeometry();
                    if(_geo ==null ){
//                        LogUtills.i("create point get currentgeometry is null");
                    }else {
//                    LogUtills.i("create point get currentgeometry "+ _geo.getType()+", value "+_geo);

                        m_Framactive.startActivity(new Intent(m_Framactive, DrawPipePointActivity.class).
                                putExtra("gpType", "null"));
                    }


                }
            }
            return  false;
        }
    }
    class  PointGeometryAdd implements GeometryAddedListener {

        @Override
        public void geometryAdded(GeometryEvent geometryEvent) {
//            LogUtills.i("add layer :"+geometryEvent.getLayer()+",id: "+geometryEvent.getID());
        }
    }
    public boolean SetAction(Action action){
        if(m_lineLay == null || m_pointLay == null){
          return false;
        }
        m_mapCtrl.setAction(action);
        m_pointLay.setEditable(true);

        return  true;
    }

    private Map MapCurrent(){
        return m_mapCtrl.getMap();
    }

    public boolean SetCurrentLayer(String layerName){
        String layer_name = "";
        switch (layerName){
            case "排水-P":{
                layer_name = SuperMapConfig.Layer_Drainage;
            }break;
            case "雨水-Y":{
                layer_name = SuperMapConfig.Layer_Rain;
            }break;
            case "给水-J":{
                layer_name = SuperMapConfig.Layer_WaterSupply;
            }break;
            default: return false;
        }

        /*for (int i=0; i<m_map.getLayers().getCount();i++){
            LogUtills.i("layer name:"+m_map.getLayers().get(i).getName());
        }
        LogUtills.i("want to  find layer name:"+"L_"+layer_name+"@"+SuperMapConfig.DEFAULT_WORKSPACE_NAME);*/
        Layer _temp  = m_mapCtrl.getMap().getLayers().get("L_"+layer_name+"@"+SuperMapConfig.DEFAULT_WORKSPACE_NAME);
        if(_temp == null){
//            LogUtills.e("find the line "+layer_name+" layer faild");
            return false;
        }
        m_lineLay = _temp;
        _temp  =  m_mapCtrl.getMap().getLayers().get("P_"+layer_name+"@"+SuperMapConfig.DEFAULT_WORKSPACE_NAME);
        if(_temp == null){
//            LogUtills.e("find the point "+layer_name+" layer faild");
            return false;
        }
        m_pointLay = _temp;

//        LogUtills.i("current line layer: " + m_lineLay.getName() +", point layer: "+m_pointLay.getName());

        return true;
    }
}
