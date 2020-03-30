package com.app.pipelinesurvey.config;

import java.io.File;

/**
 * 超图配置
 * @author HaiRun
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
    public static final String  LIC_PATH = SDCARD + "/SuperMap/pipelicense/";
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
    public static final String DEFAULT_DATA_PATH = SDCARD + "/管智绘/";
    public static final String DEFAULT_DATA_EXCEL_PATH = "Excel/";
    public static final String DEFAULT_DATA_PICTURE_PATH =  "Picture/";
    public static final String DEFAULT_DATA_SHP_PATH =  "Shp/";
    public static final String DEFAULT_DATA_SYMBOL_PATH = DEFAULT_DATA_PATH;
    public static final String DEFAULT_DATA_SYMBOL_NAME = "MarkerLibrary10.sym";
    public static final String DEFAULT_DATA_SYMBOL_LINE_NAME ="LineLibrary.lsl";
    public static final String APP_NAME ="管智绘";
    public static final String DEFAULT_DATA_RECORD = "检测记录表/";
    public static final String DEFAULT_DATA_PS_RECORD = "排水检测/";
    public static final String DEFAULT_DATA_RECORD_NAME = "现场检测记录表.xls";
    public static final String DEFAULT_DATA_PS_RECORD_NAME = "现场管道检测每日记录表.xls";
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

    //测量收点的标注
    public static final String Layer_Measure = "测量收点";
    //临时点
    public static final String Layer_Temp = "临时_0";
    //
    public static final String Layer_Total = "All";
    public static final String Layer_PS = "PS";
    //管点查询，范围设定
    public  static double Query_Buffer =5;
    //用户配置 组号 默认为空
    public static String User_Group_Index ="";
    //查询范围
    public static double User_Query_Point_Size = 3.0;
    /** 工程模式 1.常规 2.外检*/
    public static String PrjMode = "常规";
    public static final String OUTCHECK = "外检";
    //排水外检 1：是   0：否
    public static String PS_OUT_CHECK = "0";
    public static void setWorkspaceName(String string ){
        DEFAULT_WORKSPACE_NAME = string;
    }
    public static final String QQ_FILE_PATH = SDCARD + "/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv";
    public static final String WECHAT_FILE_PATH = SDCARD + "/tencent/MicroMsg/Download";
    public static  String FILE_PATH = "";
    public static String ROAD_NAME = "";
    public static String CHECK_LOCAL = "";
    public static String CHECK_MAN = "";
    public static String CHECK_WAY = "";

}
