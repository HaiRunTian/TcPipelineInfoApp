package com.app.BaseInfo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.PublicKey;

/**
 * Created by Los on 2018-06-15 14:09.
 */

public abstract class BaseFieldInfos implements Parcelable {
    //提交人
    public String submitName = "";
    //数据集名称
    public String datasetName = "";
    /**
     * 管点类型
     */
    public POINTTYPE type = POINTTYPE.Type_None;

    /**
     *  流水号探测点	O-临时点	0	0	0								O
     */
    public int sysId = -1;
    public String code = "unKnown";
    public String shortCode = "";
    //分段表达式
    public double rangeExpression = 1;
    public String pipeType = "";
    public String Edit = "";
    public abstract boolean Init();


}
