package com.app.pipelinesurvey.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.DetectionInfo;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Charset;
import com.supermap.data.CursorType;
import com.supermap.data.DataConversion;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfos;
import com.supermap.data.Recordset;
import com.supermap.data.Workspace;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据导入导出成excel
 *
 * @Author HaiRun
 * @Time 2019/3/6.21:06
 */

public class ExportDataUtils {
    private Workspace m_workspace;
    private Context m_context;
    private String m_prjId;
    //管点
    private List<BaseFieldPInfos> m_pointListChild;
    // 管点
    private List<List<Object>> m_pointListGroup;
    //管线
    private List<BaseFieldLInfos> m_lineListChild;
    // 管线
    private List<List<Object>> m_lineListGroup;
    private List<String> m_excelFiledNameP;
    private List<String> m_excelFiledNameL;

    private int m_fileCount = 1;
    private String detectionMethod;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtil.show(m_context, "数据导出失败", Toast.LENGTH_LONG);
                    break;
                case 1:
                    ToastUtil.show(m_context, "数据导出成功", Toast.LENGTH_LONG);
                    break;
                default:
                    break;
            }
        }
    };

    public ExportDataUtils(Context context, Workspace workspace, String prjId) {
        this.m_workspace = workspace;
        this.m_context = context;
        this.m_prjId = prjId;
    }
    /**
     * 导出数据
     *
     * @Author HaiRun
     * @Time 2019/3/7 . 10:25
     */
    public void exportData() {
        final ProgressDialog dialog = new ProgressDialog(m_context);
        // 设置进度条的形式为圆形转动的进度条
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置是否可以通过点击Back键取消
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_dialog_icon);
        // 设置在点击Dialog外是否取消Dialog进度条
        dialog.setCanceledOnTouchOutside(false);
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("数据导出");
        dialog.setMessage("数据正在导出到手机根目录文件夹中<" + SuperMapConfig.APP_NAME + ">，请等待……");
        dialog.show();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //点数据集
                    DatasetVector _dsVectorP = (DatasetVector) DataHandlerObserver.ins().getTotalPtLayer().getDataset();
                    Recordset _reSetP = _dsVectorP.getRecordset(false, CursorType.STATIC);
                    //线数据集
                    DatasetVector _dsVectorL = (DatasetVector) DataHandlerObserver.ins().getTotalLrLayer().getDataset();
                    Recordset _reSetL = _dsVectorL.getRecordset(false, CursorType.STATIC);
                    LogUtills.i("_reSep count = " + _reSetL.getRecordCount());
                    //创建工程文件夹
                    String _prjFolder = SuperMapConfig.DEFAULT_DATA_PATH + m_prjId;
                    FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
                    //点线导出shp
//                    exportDataToShp(_dsVectorP, _prjFolder);
//                    exportDataToShp(_dsVectorL, _prjFolder);
                    //点线数据集导出
                    reSetExportToExcel(_reSetP, _reSetL);
                    //判断文件夹里有多少个excel文件
                    if (FileUtils.getInstance().isDirExsit(_prjFolder + "/Excel")) {
                        File _file = new File(_prjFolder + "/Excel");
                        File[] _files = _file.listFiles();
                        for (int _i = 0; _i < _files.length; _i++) {
                            if (_files[_i].isFile()) {
                                m_fileCount++;
                            }
                        }
                    }

                    //数据库读取现场检测记录表数据
                    String sql = "select distinct(detection_date) from log_sheet";
                    Cursor cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_LOG_SHEET, "where original = '" + m_prjId + "'");
                    int serial = 0;
                    List<DetectionInfo> list = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        ++serial;
                        DetectionInfo info = new DetectionInfo();
                        info.setOriginal(m_prjId);
                        info.setSerialNum(String.valueOf(serial));
                        info.setApparatusCode2("");
                        info.setApparatusName2("");
                        String prjCode = cursor.getString(cursor.getColumnIndex("prj_code"));
                        info.setPrjCode(prjCode);
                        String prjName = cursor.getString(cursor.getColumnIndex("prj_name"));
                        info.setPrjName(prjName);
                        String prjSite = cursor.getString(cursor.getColumnIndex("prj_site"));
                        info.setPrjSite(prjSite);
                        String apparatusName1 = cursor.getString(cursor.getColumnIndex("apparatus_name1"));
                        info.setApparatusName1(apparatusName1);
                        String apparatusName2 = cursor.getString(cursor.getColumnIndex("apparatus_name2"));
                        info.setApparatusName2(apparatusName2);
                        String apparatusCode1 = cursor.getString(cursor.getColumnIndex("apparatus_code1"));
                        info.setApparatusCode1(apparatusCode1);
                        String apparatusCode2 = cursor.getString(cursor.getColumnIndex("apparatus_code2"));
                        info.setApparatusCode2(apparatusCode2);
                        String detectionStandard = cursor.getString(cursor.getColumnIndex("detection_standard"));
                        info.setDetectionStandard(detectionStandard);
                        String detectionDate = cursor.getString(cursor.getColumnIndex("detection_date"));
                        info.setDetectionDate(detectionDate);
                        detectionMethod = cursor.getString(cursor.getColumnIndex("detection_method"));
                        info.setDetectionMethod(detectionMethod);
                        String detectionMap = cursor.getString(cursor.getColumnIndex("detection_map"));
                        info.setDetectionMap(detectionMap);
                        String groupLeader = cursor.getString(cursor.getColumnIndex("group_leader"));
                        info.setGroupLeader(groupLeader);
                        String groupMumber1 = cursor.getString(cursor.getColumnIndex("group_mumber1"));
                        info.setGroupMember1(groupMumber1);
                        String groupMumber2 = cursor.getString(cursor.getColumnIndex("group_member2"));
                        info.setGroupMember2(groupMumber2);
                        String remark = cursor.getString(cursor.getColumnIndex("remark"));
                        info.setRemark(remark);
                        //查询数据集
                        String sqlPoint = "exp_Date = '" + detectionDate + "' order by serialNum asc";
                        Recordset reSetPoint = null;
                        Recordset reSetLine = null;
                        //测量 查询测量点数据集
                        if (detectionMethod.contains("探测") || detectionMethod.contains("RTK")) {
                            String sqlPoint2 = "exp_Date = '" + detectionDate + "' order by SmID asc";
                            reSetPoint = DataHandlerObserver.ins().QueryRecordsetMesureBySql(sqlPoint2, false);
                            String sqlLine = "measureStart = '1' and measureEnd = '1' and measureDate = '" + detectionDate + "'";
                            reSetLine = DataHandlerObserver.ins().QueryRecordsetBySql(sqlLine, false, false);
                        } else {
                            //物探
                            //设置点记录集的几点和终点
                            reSetPoint = DataHandlerObserver.ins().QueryRecordsetBySql(sqlPoint, true, false);
                            String sqlLine = "exp_Date = '" + detectionDate + "'";
                            reSetLine = DataHandlerObserver.ins().QueryRecordsetBySql(sqlLine, false, false);
                        }
                        String firstPoint = "";
                        String endPoint = "";
                        if (!reSetPoint.isEmpty()) {
                            reSetPoint.moveFirst();
                            firstPoint = reSetPoint.getString("exp_Num");
                            reSetPoint.moveLast();
                            endPoint = reSetPoint.getString("exp_Num");
                        }
                        info.setPointName(firstPoint + "—" + endPoint);

                        //设置当天线工作量 总长度
                        double lenth = 0.0d;
                        while (!reSetLine.isEOF()) {
                            lenth += Double.valueOf(reSetLine.getString("pipeLength"));
                            reSetLine.moveNext();
                        }
                        DecimalFormat df = new DecimalFormat("0.00");
                        String length = df.format(lenth);
                        info.setWorkload(length);
                        list.add(info);
                    }

                    if (cursor.getCount() != 0) {
                        FileUtils.getInstance().deleteFile(_prjFolder + "/Excel/" + m_prjId + ".xls");
                        ExcelUtilsOfPoi.initExcelLogSheet(_prjFolder + "/Excel/" + m_prjId + ".xls", list);
                    }
                    //  poi 库导出数据  初始化excel表格
                    ExcelUtilsOfPoi.initExcel(_prjFolder + "/Excel/" + m_prjId + "-" + String.valueOf(m_fileCount) + ".xls", m_excelFiledNameP, m_excelFiledNameL, _dsVectorP.getName(), _dsVectorL.getName());
                    //点 线导出excel 创建项目文件夹
                    ExcelUtilsOfPoi.writeObjListToExcel(0, m_pointListGroup, _prjFolder + "/Excel/" + m_prjId + "-" + String.valueOf(m_fileCount) + ".xls");
                    ExcelUtilsOfPoi.writeObjListToExcel(1, m_lineListGroup, _prjFolder + "/Excel/" + m_prjId + "-" + String.valueOf(m_fileCount) + ".xls");
                    handler.sendEmptyMessage(1);

                } catch (Exception e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                }
                dialog.cancel();
            }
        };
        thread.start();
    }

    /**
     * @auther HaiRun
     * created at 2018/7/25 8:55
     * 点、线recodSet导出excel
     */
    private void reSetExportToExcel(Recordset resetP, Recordset resetL) {
        //初始化点表excel字段名
        FieldInfos _fieldInfos = resetP.getFieldInfos();
        int _fileCountp = _fieldInfos.getCount();
        m_excelFiledNameP = new ArrayList<>();
        for (int _i = 0; _i < _fileCountp; _i++) {
            if (_fieldInfos.get(_i).isSystemField() || _fieldInfos.get(_i).getName().equals("SmUserID")) {
                continue;
            }
            m_excelFiledNameP.add(_fieldInfos.get(_i).getName());
        }
        //Point数据导出excel
        if (!resetP.isEmpty()) {
            resetP.moveFirst();
            m_pointListChild = new ArrayList<>();
            BaseFieldPInfos _pipePoint1 = null;
            while (!resetP.isEOF()) {
                _pipePoint1 = BaseFieldPInfos.createFieldInfo(resetP, 1);
                m_pointListChild.add(_pipePoint1);
                resetP.moveNext();
            }
            m_pointListGroup = new ArrayList<>();
            m_pointListChild.size();
            ArrayList<Object> list = null;
            Field[] _fields = null;
            String _type = "";
            for (BaseFieldPInfos _pipePoint : m_pointListChild) {
                list = new ArrayList<>();
                _fields = _pipePoint.getClass().getFields();
                for (int _i = 0; _i < _fields.length - 3; _i++) {
                    try {
                        _type = _fields[_i].getType().getCanonicalName();
                        switch (_type) {
                            case "double":
                            case "java.lang.String":
                            case "int":
                            case "com.app.BaseInfo.Data.POINTTYPE":
                                list.add(String.valueOf(_fields[_i].get(_pipePoint)));
                                break;
                            default:
                                break;
                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                m_pointListGroup.add(list);

            }
        }
        if (resetP != null) {
            resetP.close();
            resetP.dispose();
        }
        //初始化线表字段名
        FieldInfos _fieldInfosL = resetL.getFieldInfos();
        int _fileCount = _fieldInfosL.getCount();
        m_excelFiledNameL = new ArrayList<>();
        for (int _i = 0; _i < _fileCount; _i++) {
            if (_fieldInfosL.get(_i).isSystemField() || _fieldInfosL.get(_i).getName().equals("SmUserID"))
                continue;
            m_excelFiledNameL.add(_fieldInfosL.get(_i).getName());
        }
        //导出数据
        if (!resetL.isEmpty()) {
            resetL.moveFirst();
            m_lineListChild = new ArrayList<>();
            BaseFieldLInfos _pipeLine1 = null;
            while (!resetL.isEOF()) {
                _pipeLine1 = BaseFieldLInfos.createFieldInfo(resetL, 0);
                resetL.moveNext();
                m_lineListChild.add(_pipeLine1);
            }
            m_lineListGroup = new ArrayList<>();
            ArrayList<Object> list = null;
            Field[] _fields = null;
            String _type = "";
            for (BaseFieldLInfos _pipeLine : m_lineListChild) {
                list = new ArrayList<>();
                _fields = _pipeLine.getClass().getFields();
                for (int _i = 0; _i < _fields.length - 3; _i++) {
                    try {
                        _type = _fields[_i].getType().getCanonicalName();
                        switch (_type) {
                            case "double":
                            case "java.lang.String":
                            case "int":
                            case "com.app.BaseInfo.Data.POINTTYPE":
                                list.add(String.valueOf(_fields[_i].get(_pipeLine)));
                                break;
                            default:
                                break;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                m_lineListGroup.add(list);
            }
        }
        if (resetL != null) {
            resetL.close();
            resetL.dispose();
        }
    }

    /**
     * @auther HaiRun
     * created at 2018/7/25 8:54
     * 数据集以shp格式导出
     */
    private void exportDataToShp(Dataset dataset, String folderName) {
        File dbSaveFile = new File(folderName + "/Shp");
        if (!dbSaveFile.exists()) {
            dbSaveFile.mkdirs();
        }
        try {
            DataConversion.setConvertCharset(Charset.UTF8);
            boolean isOk = DataConversion.exportSHP(dbSaveFile + "/" + dataset.getName() + "_" + m_prjId + ".shp", dataset);
            if (!isOk) {
                LogUtills.i("shp导出失败  ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入数据
     *
     * @Author HaiRun
     * @Time 2019/3/7 . 10:26
     * 1.读取手机中的excel表 获取里面的数据
     * 2.根据或者到的数据，判断数据属于那一中管类，然后直接添加到这个管类数据集中
     */
    public static boolean importData(List<BaseFieldInfos> _baseFieldPInfos, List<BaseFieldInfos> baseFileLInfos) {
        long time = System.currentTimeMillis();
        LogUtills.i(time + "");
        //点导入
        BaseFieldPInfos _baseFieldPInfos1 = null;
        for (int i = 0; i < _baseFieldPInfos.size(); i++) {
            _baseFieldPInfos1 = (BaseFieldPInfos) _baseFieldPInfos.get(i);
            if (!DataHandlerObserver.ins().createRecords2(_baseFieldPInfos1)) {
                return false;
            }
        }

        //线导入
        BaseFieldLInfos _baseFieldLInfos1 = null;
        for (int i = 0; i < baseFileLInfos.size(); i++) {
            _baseFieldLInfos1 = (BaseFieldLInfos) baseFileLInfos.get(i);
            if (!DataHandlerObserver.ins().addRecords(_baseFieldLInfos1)) {
                return false;
            }
        }

        long endTime = System.currentTimeMillis();
        LogUtills.i(endTime - time + "");
        return true;
    }
}
