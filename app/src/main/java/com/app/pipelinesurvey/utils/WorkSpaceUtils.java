package com.app.pipelinesurvey.utils;

import com.app.pipelinesurvey.location.BaseGPS;
import com.supermap.data.Workspace;
import com.supermap.mapping.MapControl;

/**
 * @Author HaiRun
 * @Time 2019/3/4.14:30
 */

public class WorkSpaceUtils {
    private static WorkSpaceUtils s_workSpaceUtils = null;
    private Workspace m_workspace;
    private MapControl m_mapControl;
    private BaseGPS m_baseGPS;

    public static synchronized WorkSpaceUtils getInstance() {
        // 这个方法比上面有所改进，不用每次都进行生成对象，只是第一次
        // 使用时生成实例，提高了效率！
        if (s_workSpaceUtils == null)
            s_workSpaceUtils = new WorkSpaceUtils();
        return s_workSpaceUtils;
    }

    public WorkSpaceUtils() {

    }

    public void setWorkSpace(Workspace workSpace) {
        this.m_workspace = workSpace;
    }

    public void setMapControl(MapControl mapControl) {
        this.m_mapControl = mapControl;
    }

    public Workspace getWorkspace() {
        return m_workspace;
    }

    public MapControl getMapControl() {
        return m_mapControl;
    }


    public void setGPS(BaseGPS baseGPS) {
        this.m_baseGPS = baseGPS;
    }

    public BaseGPS getGPS() {
        return m_baseGPS;
    }

    public boolean saveWorkspace() {

        try {
            if (m_mapControl.getMap().save() &&
                    m_workspace.save()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
