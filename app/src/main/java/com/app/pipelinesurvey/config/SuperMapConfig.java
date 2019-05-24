package com.app.pipelinesurvey.config;

import java.io.File;

/**
 * 超图配置
 * @author hairun
 */

public class SuperMapConfig {
    /**
     * sd卡根路径
     */

    public static String SDCARD = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     *  设为动态
     */
    public static final String MAP_DATA_PATH = SDCARD + "/SuperMap/data/map8/";
    /**
     *  许可文件目录
     */
    public static final String LIC_PATH = SDCARD + "/SuperMap/license/";
    /**
     *  临时文件目录
     */
    public static final String TEMP_PATH = SDCARD + "/SuperMap/temp/";
    /**
     *
     *网络缓存文件目录
     */
    public static final String WEB_CACHE_PATH = MAP_DATA_PATH;
    /**
     *  许可文件名
     */
    public static final String LIC_NAME = "SuperMapiMobileTrial.slm";
    /**
     *  许可文件名
     */
    public static final String FULL_LIC_PATH = LIC_PATH + LIC_NAME;
    /**
     *  默认工作空间文件名
     */
    public static  String DEFAULT_WORKSPACE_NAME = "TcPipeData";
    public static final String DEFAULT_DATA_PATH = SDCARD + "/天驰管调通/";
    public static final String DEFAULT_DATA_EXCEL_PATH = "Excel";
    public static final String DEFAULT_DATA_PICTURE_PATH =  "Picture";
    public static final String DEFAULT_DATA_SHP_PATH =  "Shp";
    public static final String DEFAULT_DATA_SYMBOL_PATH = DEFAULT_DATA_PATH;
    public static final String DEFAULT_DATA_SYMBOL_NAME = "MarkerLibrary2.sym";
    public static final String DEFAULT_DATA_SYMBOL_LINE_NAME ="LineLibrary.lsl";
    /**
     *   当前打开地图的名字 当前工程名称
     */
    public static  String PROJECT_NAME ="";

    /**
     * 当前城市标准名称
     */
    public static String  PROJECT_CITY_NAME = "广州";

    public static void initFolders() {
        String folders[] = new String[]{DEFAULT_DATA_PATH};
        for (String _folder : folders) {
            File f = new File(_folder);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }
    //图层配置
    public static final String Layer_Drainage = "排水_P";
    public static final String Layer_WaterSupply = "给水_J";
    public static final String Layer_Rain = "雨水_Y";
    public static final String Layer_Sewage = "污水_W";
    public static final String Layer_CoalGas = "煤气_M";
    public static final String Layer_Gas = "燃气_R";
    public static final String Layer_ElectricPower = "电力_L";
    public static final String Layer_StreetLamp = "路灯_S";
    public static final String Layer_CableTV = "有视_T";
    public static final String Layer_Telecom = "电信_D";
    public static final String Layer_Army = "军队_B";
    public static final String Layer_Traffic = "交通_X";
    public static final String Layer_Industry = "工业_G";
    public static final String Layer_Unknown = "不明_N";
    public static final String Layer_Other = "其它_Q";
    public static final String Layer_Point = "Point";
    public static final String Layer_Line = "Line";
    //测量收点的标注
    public static final String Layer_Measure = "测量收点";
    //临时点
    public static final String Layer_Temp = "临时_0";

    //线表
    public static final String Layer_Total = "All";


    //管点查询，范围设定
    public  static double Query_Buffer =5;

    //用户配置 组号 默认为空
    public static String User_Group_Index ="";
//    public static int User_Pipe_Length = 30 ;
    public static double User_Query_Point_Size = 3.0;


    /**
     *   全部数据集名称 包括临时点
     */
/*    public static List<String> getPointDsNameAll() {
*//*
        String[] arr = new String[]{Layer_Drainage, Layer_WaterSupply, Layer_Rain, Layer_Sewage, Layer_CoalGas, Layer_Gas, Layer_ElectricPower, Layer_StreetLamp
                , Layer_Telecom, Layer_CableTV, Layer_Army, Layer_Traffic, Layer_Industry, Layer_Unknown, Layer_Other,Layer_Temp};
*//*

        String[] arr = new String[]{Layer_Drainage};
        List<String> _list = SpinnerDropdownListManager.getData(arr);

        return _list;
    }*/

    //全部数据集名称 不包括临时点
//    public static List<String> getPointDsName() {
//       /* String[] arr = new String[]{Layer_Drainage, Layer_WaterSupply, Layer_Rain, Layer_Sewage, Layer_CoalGas, Layer_Gas, Layer_ElectricPower, Layer_StreetLamp
//                , Layer_Telecom, Layer_CableTV, Layer_Army, Layer_Traffic, Layer_Industry, Layer_Unknown, Layer_Other};*/
//
//        String[] arr = new String[]{Layer_Drainage};
//        List<String> _list = SpinnerDropdownListManager.getData(arr);
//
//        return _list;
//    }

    public static void setWorkspaceName(String string ){
        DEFAULT_WORKSPACE_NAME = string;
    }

}
