package com.app.BaseInfo.Data;

import com.supermap.mapping.Action;

/**
 * Created by Los on 2018-06-21 19:09.
 * 地图动作类型
 */

public enum MAPACTIONTYPE {
    Action_None,
    Action_CreatePoint,
    Action_CreateLine,
    Action_AddPointInLine,
    Action_MeasereDistance,
    Action_QueryPoint,
    Action_QueryLine,
    Action_EditPointAttribute,
    Action_EditPointLocation,
    Action_EditLineAttribute,
    Action_GetStartPoint,
    Action_GetEndPoint,
    Action_MeasurePoint;
}


