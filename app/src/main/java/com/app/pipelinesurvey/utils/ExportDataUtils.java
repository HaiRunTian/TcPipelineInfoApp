package com.app.pipelinesurvey.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Oper.OperDataSet;
import com.app.BaseInfo.Oper.OperSql;
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
import com.supermap.mapping.CallOut;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<List<Object>> m_pointListGroup = new ArrayList<>();
    //管线
    private List<BaseFieldLInfos> m_lineListChild = new ArrayList<>();
    // 管线
    private List<List<Object>> m_lineListGroup = new ArrayList<>();
    // 排水检测管线
    private List<List<String>> psListGroup = new ArrayList<>();
    private List<String> m_excelFiledNameP;
    private List<String> m_excelFiledNameL;
    private String chenkMan;
    /**
     * 默认excel从1开始命名
     */
    private String detectionMethod;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    progressDialog.dismiss();
                    ToastyUtil.showErrorShort(m_context, "数据导出失败");
                    break;
                case 1:
                    progressDialog.dismiss();
                    ToastyUtil.showSuccessShort(m_context, "数据导出成功");
                    break;
                default:
                    break;
            }
        }
    };
    private ProgressDialog progressDialog;

    public ExportDataUtils(Context context) {
        this.m_workspace = WorkSpaceUtils.getInstance().getWorkspace();
        this.m_context = context;
        this.m_prjId = SuperMapConfig.PROJECT_NAME;
    }

    /**
     * 导出全部数据
     *
     * @Author HaiRun
     * @Time 2019/3/7 . 10:25
     */
    public void exportData() {
        progressDialog = new ProgressDialog(m_context);
        // 设置进度条的形式为圆形转动的进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置是否可以通过点击Back键取消
        progressDialog.setCancelable(true);
        progressDialog.setIcon(R.drawable.ic_dialog_icon);
        // 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setCanceledOnTouchOutside(false);
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        progressDialog.setTitle("数据导出");
        progressDialog.setMessage("数据正在导出到手机根目录文件夹中<" + SuperMapConfig.APP_NAME + ">，请等待……");
        progressDialog.show();
        ThreadUtils.execute(() -> {
            try {
                //点数据集 临时点去掉掉
                DatasetVector _dsVectorP = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("P_" + SuperMapConfig.Layer_Total);
                Recordset _reSetP = _dsVectorP.query("subsid != '临时点'", CursorType.STATIC);
                //线数据集
                DatasetVector _dsVectorL = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("L_" + SuperMapConfig.Layer_Total);
                Recordset _reSetL = _dsVectorL.query("pipeType Not like '临时%'", CursorType.STATIC);

                //创建工程相关文件夹
                String _prjFolder = SuperMapConfig.DEFAULT_DATA_PATH + m_prjId;
                FileUtils.getInstance().mkdirs(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
                FileUtils.getInstance().mkdirs(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD);
                //点线数据集转成bean并且添加到list
                reSetPtExportToBeans(_reSetP);
                reSetLrExportToBeans(_reSetL);
                //判断文件夹里有多少个excel文件
                int fileCount = FileUtils.getInstance().getFileIndexMax(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
                //文件夹+1,命名新的excel
                ++fileCount;
                //数据库读取现场检测记录表数据
                List<DetectionInfo> list = getDetectionInfos();
                //记录检查表记录不为0，导出
                if (!FileUtils.getInstance().isFileExsit(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME)) {
                    InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
                    if (is != null) {
                        FileUtils.getInstance().copy(is, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
                    }
                }
                if (list.size() != 0) {
                    ExcelUtilsOfPoi.initExcelLogSheet(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME, list);
                }
                //导出排水检测
                exportPsData(_prjFolder, fileCount);
                //  poi 库导出数据  初始化excel表格
                ExcelUtilsOfPoi.initExcel(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH + m_prjId + "-" + String.valueOf(fileCount) + ".xls",
                        m_excelFiledNameP, m_excelFiledNameL, "P_ALL", "L_All");
                //点 线导出excel 创建项目文件夹
                if (m_pointListGroup.size() > 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(0, m_pointListGroup, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }
                if (m_lineListGroup.size() > 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(1, m_lineListGroup, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }
                exportDataToShp(_dsVectorP, _prjFolder);
                exportDataToShp(_dsVectorL, _prjFolder);
                handler.sendEmptyMessage(1);
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
                //提示导出成功

                handler.sendEmptyMessage(0);
            }
        });
    }

    private void exportPsData(String _prjFolder, int fileCount) {
        //排水检测表导出 1代表用户启用了排水外检
        if ("1".equals(SuperMapConfig.PS_OUT_CHECK)) {
            //排水外检表
            DatasetVector dsVectorPS = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("L_" + SuperMapConfig.Layer_PS);
            if (dsVectorPS != null) {
                Recordset reSetPs = dsVectorPS.getRecordset(false, CursorType.STATIC);
                reSetPsExportToBeans(reSetPs);
            }
//                    if (!FileUtils.getInstance().isFileExsit(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_PS_RECORD + SuperMapConfig.DEFAULT_DATA_PS_RECORD_NAME)) {
            InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.DEFAULT_DATA_PS_RECORD_NAME);
            if (is != null) {
                FileUtils.getInstance().copy(is, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_PS_RECORD + String.valueOf(fileCount) + "-" + SuperMapConfig.DEFAULT_DATA_PS_RECORD_NAME);
            }
//                    }
            if (psListGroup.size() != 0) {
                ExcelUtilsOfPoi.initExcelPsSheet(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_PS_RECORD + String.valueOf(fileCount) + "-" + SuperMapConfig.DEFAULT_DATA_PS_RECORD_NAME, psListGroup);
            }
        }
    }

    /**
     * 把记录集数据导出到list中
     *
     * @Params :
     * @author :HaiRun
     * @date :2020/2/24  14:05
     */
    private void reSetPsExportToBeans(Recordset reSetPs) {
        int index = 1;
        //把需要的字段值，存到集合中
        if (!reSetPs.isEmpty()) {
            while (!reSetPs.isEOF()) {
                List<String> list = new ArrayList<String>();
                list.add(String.valueOf(index));
                index++;
                try {
                    list.add(reSetPs.getString("videoNumber"));
                    list.add(reSetPs.getString("benExpNum"));
                    list.add(reSetPs.getString("endExpNum"));
                    list.add(reSetPs.getString("benDeep"));
                    list.add(reSetPs.getString("endDeep"));
                    list.add(reSetPs.getString("flow"));
                    list.add(reSetPs.getString("material"));
                    list.add(reSetPs.getString("pipeSize"));
                    list.add(reSetPs.getString("pipeType"));
                    list.add(reSetPs.getString("wellNumber"));
                    list.add(reSetPs.getString("wellState"));
                    list.add(reSetPs.getString("flowState"));
                    list.add(reSetPs.getString("defectLength"));
                    list.add(reSetPs.getString("defectCode"));
                    list.add(reSetPs.getString("defectGrade"));
                    list.add(reSetPs.getString("checkMan"));
                    list.add(reSetPs.getString("checkWay"));
                    list.add(reSetPs.getString("checkLocal"));
                    list.add(reSetPs.getString("roadName"));
                    list.add(reSetPs.getString("exp_Date"));
                    list.add(reSetPs.getString("remark"));
                    chenkMan = reSetPs.getString("checkMan");
                } catch (Exception e) {
                    LogUtills.e(e.toString());
                }
                psListGroup.add(list);
                reSetPs.moveNext();
            }
        }

        if (reSetPs != null) {
            reSetPs.close();
            reSetPs.dispose();
        }
    }

    /**
     * 导出外检模式数据
     *
     * @Author HaiRun
     * @Time 2019/3/7 . 10:25
     */
    public void exportOutCheckData(String sql, String start, String end, String roadName) {
        progressDialog = new ProgressDialog(m_context);
        // 设置进度条的形式为圆形转动的进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置是否可以通过点击Back键取消
        progressDialog.setCancelable(true);
        progressDialog.setIcon(R.drawable.ic_dialog_icon);
        // 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setCanceledOnTouchOutside(false);
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        progressDialog.setTitle("数据导出");
        progressDialog.setMessage("数据正在导出到手机根目录文件夹中<" + SuperMapConfig.APP_NAME + ">，请等待……");
        progressDialog.show();

        ThreadUtils.execute(() -> {
            try {
                //点数据集 临时点去掉掉
                DatasetVector _dsVectorP = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("P_" + SuperMapConfig.Layer_Total);
                Recordset _reSetP = _dsVectorP.query(sql, CursorType.STATIC);
                //线数据集
                DatasetVector _dsVectorL = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("L_" + SuperMapConfig.Layer_Total);
                Recordset _reSetL = _dsVectorL.query("pipeType != '临时-O' and Edit like '外检%' and" + " (exp_Date between '"
                        + start + "' and '" + end + "')", CursorType.STATIC);
                //创建工程相关文件夹
                String _prjFolder = SuperMapConfig.DEFAULT_DATA_PATH + m_prjId;
                FileUtils.getInstance().mkdirs(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
                FileUtils.getInstance().mkdirs(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD);
                //点线数据集转成bean并且添加到list
                reSetPtExportToBeans(_reSetP);
                reSetLrExportToBeans(_reSetL);
                //判断文件夹里有多少个excel文件
                int fileCount = FileUtils.getInstance().getFileIndexMax(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
                //文件夹+1,命名新的excel
                ++fileCount;
                //数据库读取现场检测记录表数据
                List<DetectionInfo> list = getDetectionInfos();
                //记录检查表记录不为0，导出
                if (!FileUtils.getInstance().isFileExsit(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME)) {
                    InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
                    if (is != null) {
                        FileUtils.getInstance().copy(is, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
                    }
                }
                if (list.size() != 0) {
                    ExcelUtilsOfPoi.initExcelLogSheet(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME, list);
                }
                exportPsDataForDate(start, end, roadName, _prjFolder);
                //点线表头
                List<String> point = getPointTitles();
                List<String> line = getLineTitles();
                //初始化表格
                ExcelUtilsOfPoi.initExcelOfOut(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH + m_prjId + "-" + String.valueOf(fileCount) + ".xls",
                        m_excelFiledNameP, m_excelFiledNameL, point, line, "P_ALL", "L_All", "编辑点", "编辑线");

                //点 线导出excel 创建项目文件夹
                if (m_pointListGroup.size() > 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(0, m_pointListGroup, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }
                if (m_lineListGroup.size() > 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(1, m_lineListGroup, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }
                List<List<String>> points = OperSql.getSingleton().queryPoint(m_prjId, start, end);
                if (point != null || points.size() != 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(2, points, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }

                List<List<String>> lines = OperSql.getSingleton().queryLine(m_prjId, start, end);
                if (lines != null || lines.size() != 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(3, lines, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }

                //提示导出成功
                exportDataToShp(_dsVectorP, _prjFolder);
                exportDataToShp(_dsVectorL, _prjFolder);
                handler.sendEmptyMessage(1);
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 导出外检模式数据
     *
     * @Author HaiRun
     * @Time 2019/3/7 . 10:25
     */
    public void exportOutCheckData(String sql) {
        progressDialog = new ProgressDialog(m_context);
        // 设置进度条的形式为圆形转动的进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置是否可以通过点击Back键取消
        progressDialog.setCancelable(true);
        progressDialog.setIcon(R.drawable.ic_dialog_icon);
        // 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setCanceledOnTouchOutside(false);
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        progressDialog.setTitle("数据导出");
        progressDialog.setMessage("数据正在导出到手机根目录文件夹中<" + SuperMapConfig.APP_NAME + ">，请等待……");
        progressDialog.show();

        ThreadUtils.execute(() -> {
            try {
                //点数据集 临时点去掉掉
                DatasetVector _dsVectorP = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("P_" + SuperMapConfig.Layer_Total);
                Recordset _reSetP = _dsVectorP.query(sql, CursorType.STATIC);
                //线数据集
                DatasetVector _dsVectorL = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("L_" + SuperMapConfig.Layer_Total);
                Recordset _reSetL = _dsVectorL.query("pipeType != '临时-O'", CursorType.STATIC);
                //创建工程相关文件夹
                String _prjFolder = SuperMapConfig.DEFAULT_DATA_PATH + m_prjId;
                FileUtils.getInstance().mkdirs(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
                FileUtils.getInstance().mkdirs(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD);
                //点线数据集转成bean并且添加到list
                reSetPtExportToBeans(_reSetP);
                reSetLrExportToBeans(_reSetL);
                //判断文件夹里有多少个excel文件
                int fileCount = FileUtils.getInstance().getFileIndexMax(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
                //文件夹+1,命名新的excel
                ++fileCount;
                //数据库读取现场检测记录表数据
                List<DetectionInfo> list = getDetectionInfos();
                //记录检查表记录不为0，导出
                if (!FileUtils.getInstance().isFileExsit(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME)) {
                    InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
                    if (is != null) {
                        FileUtils.getInstance().copy(is, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
                    }
                }
                if (list.size() != 0) {
                    ExcelUtilsOfPoi.initExcelLogSheet(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME, list);
                }
                exportPsData(_prjFolder, fileCount);
                //点线表头
                List<String> point = getPointTitles();
                List<String> line = getLineTitles();
                //初始化表格
                ExcelUtilsOfPoi.initExcelOfOut(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH + m_prjId + "-" + String.valueOf(fileCount) + ".xls",
                        m_excelFiledNameP, m_excelFiledNameL, point, line, "P_ALL", "L_All", "编辑点", "编辑线");

                //点 线导出excel 创建项目文件夹
                if (m_pointListGroup.size() > 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(0, m_pointListGroup, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }
                if (m_lineListGroup.size() > 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(1, m_lineListGroup, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }
                List<List<String>> points = OperSql.getSingleton().queryPoint(m_prjId);
                if (point != null || points.size() != 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(2, points, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }

                List<List<String>> lines = OperSql.getSingleton().queryLine(m_prjId);
                if (lines != null || lines.size() != 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(3, lines, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }
                //提示导出成功
                handler.sendEmptyMessage(1);
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 外键模式线表头
     *
     * @Params :
     * @author :HaiRun
     * @date :2020/2/24  10:02
     */
    private List<String> getLineTitles() {
        List<String> line = new ArrayList<>();
        line.add("工程名字");
        line.add("起点");
        line.add("连接点");
        line.add("状态");
        line.add("时间");
        return line;
    }

    /**
     * 外键模式点表头
     *
     * @Params :
     * @author :HaiRun
     * @date :2020/2/24  10:02
     */
    private List<String> getPointTitles() {
        List<String> point = new ArrayList<>();
        point.add("工程名字");
        point.add("点号");
        point.add("X");
        point.add("Y");
        point.add("状态");
        point.add("时间");
        return point;
    }

    /**
     * 测量收点表字段
     * @Params :
     * @author :HaiRun
     * @date :2020-12-09  16:48
     */
    private List<String> getMeaserPointTitles() {
        List<String> point = new ArrayList<>();
        point.add("物探点号");
        point.add("测量点号");
        point.add("X");
        point.add("Y");
        point.add("时间");
        return point;
    }


    /**
     * 导出时间段数据
     *
     * @Author HaiRun
     * @Time 2019/3/7 . 10:25
     */
    public void exportData(String start, String end, String roadName) {
        progressDialog = new ProgressDialog(m_context);
        // 设置进度条的形式为圆形转动的进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置是否可以通过点击Back键取消
        progressDialog.setCancelable(true);
        progressDialog.setIcon(R.drawable.ic_dialog_icon);
        // 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setCanceledOnTouchOutside(false);
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        progressDialog.setTitle("数据导出");
        progressDialog.setMessage("数据正在导出到手机根目录文件夹中<" + SuperMapConfig.APP_NAME + ">，请等待……");
        progressDialog.show();
        ThreadUtils.execute(() -> {
            try {
                //点记录集
                DatasetVector _dsVectorP = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("P_" + SuperMapConfig.Layer_Total);
                Recordset _reSetP = _dsVectorP.query("(exp_Date between '" + start + "' and '" + end + "') and subsid != '临时点'", CursorType.STATIC);
                //线记录集
                DatasetVector _dsVectorL = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("L_" + SuperMapConfig.Layer_Total);
                Recordset _reSetL = _dsVectorL.query("(exp_Date between '" + start + "' and '" + end + "') and pipeType Not like '临时%'", CursorType.STATIC);
                //创建工程相关文件夹
                String _prjFolder = SuperMapConfig.DEFAULT_DATA_PATH + m_prjId;
                FileUtils.getInstance().mkdirs(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
                FileUtils.getInstance().mkdirs(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD);

                //点线数据集转成bean并且添加到list
                reSetPtExportToBeans(_reSetP);
                reSetLrExportToBeans(_reSetL);
                //判断文件夹里有多少个excel文件
                int fileCount = FileUtils.getInstance().getFileIndexMax(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH);
                //文件夹+1,命名新的excel
                ++fileCount;
                //数据库读取现场检测记录表数据
                List<DetectionInfo> list = getDetectionInfos();
                if (!FileUtils.getInstance().isFileExsit(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME)) {
                    InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
                    if (is != null) {
                        FileUtils.getInstance().copy(is, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME);
                    }
                }
                //记录检查表记录不为0，导出
                if (list.size() != 0) {
                    ExcelUtilsOfPoi.initExcelLogSheet(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_RECORD + SuperMapConfig.DEFAULT_DATA_RECORD_NAME, list);
                }
                exportPsDataForDate(start, end, roadName, _prjFolder);

                //  poi 库导出数据  初始化excel表格
                ExcelUtilsOfPoi.initExcel(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH + m_prjId + "-" + String.valueOf(fileCount) + ".xls",
                        m_excelFiledNameP, m_excelFiledNameL, "P_ALL", "L_All");
                //点 线导出excel 创建项目文件夹
                if (m_pointListGroup.size() > 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(0, m_pointListGroup, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }
                if (m_lineListGroup.size() > 0) {
                    ExcelUtilsOfPoi.writeObjListToExcel(1, m_lineListGroup, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH +
                            m_prjId + "-" + String.valueOf(fileCount) + ".xls");
                }
                //提示导出成功
                handler.sendEmptyMessage(1);
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
                handler.sendEmptyMessage(0);
            }
        });
    }

    private void exportPsDataForDate(String start, String end, String roadName, String _prjFolder) {
        //排水检测表导出 1代表用户启用了排水外检
        if ("1".equals(SuperMapConfig.PS_OUT_CHECK)) {
            //排水外检表
            DatasetVector dsVectorPS = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("P_" + SuperMapConfig.Layer_PS);
            if (dsVectorPS != null) {
                Recordset reSetPs = dsVectorPS.query("（exp_Date between '" + start + "' and '" + end + "'） and roadName = '" + roadName + "'", CursorType.STATIC);
                reSetPsExportToBeans(reSetPs);
            }
//                    if (!FileUtils.getInstance().isFileExsit(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_PS_RECORD + SuperMapConfig.DEFAULT_DATA_PS_RECORD_NAME)) {
            InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.DEFAULT_DATA_PS_RECORD_NAME);
            if (is != null) {
                FileUtils.getInstance().copy(is, _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_PS_RECORD + roadName + "-" + chenkMan + "-" + end + ".xls");
            }
//                    }
            if (psListGroup.size() != 0) {
                ExcelUtilsOfPoi.initExcelPsSheet(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_PS_RECORD + roadName + "-" + chenkMan + "-" + end + ".xls", psListGroup);
            }
        }
    }

    /**
     * 检查记录表导出
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/27  16:18
     */
    private List<DetectionInfo> getDetectionInfos() {
        //数据库读取现场检测记录表数据
        String sql = "select distinct(detection_date) from log_sheet";
        Cursor cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_LOG_SHEET, "where original = '" + m_prjId + "'");
        //序号
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
            String sqlPoint = "exp_Date = '" + detectionDate + "' order by SmID asc";
            Recordset reSetPoint = null;
            Recordset reSetLine = null;
            //测量 RTK  查询测量点数据集
            DatasetVector _dsVectorP = (DatasetVector) WorkSpaceUtils.getInstance()
                    .getWorkspace().getDatasources().get(0).getDatasets().get("P_" + SuperMapConfig.Layer_Total);
            DatasetVector _dsVectorL = (DatasetVector) WorkSpaceUtils.getInstance()
                    .getWorkspace().getDatasources().get(0).getDatasets().get("L_" + SuperMapConfig.Layer_Total);
            if (detectionMethod.contains("测量") || detectionMethod.contains("RTK")) {
                reSetPoint = _dsVectorP.query(sqlPoint, CursorType.STATIC);
                String sqlLine = "measureStart = '1' and measureEnd = '1' and measureDate = '" + detectionDate + "'";
                reSetLine = _dsVectorL.query(sqlLine, CursorType.STATIC);
            } else {
                //物探
                //设置点记录集的几点和终点
                reSetPoint = _dsVectorP.query(sqlPoint, CursorType.STATIC);
                String sqlLine = "exp_Date = '" + detectionDate + "'";
                reSetLine = _dsVectorL.query(sqlLine, CursorType.STATIC);
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
                lenth += Double.valueOf(reSetLine.getString("SmLength"));
                reSetLine.moveNext();
            }
            DecimalFormat df = new DecimalFormat("0.00");
            String length = df.format(lenth);
            info.setWorkload(length);
            if (!reSetPoint.isEmpty()) {
                list.add(info);
            }
        }
        return list;
    }

    /**
     * 点记录导出数据成bean，然后添加到list
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/27  15:46
     */
    private void reSetPtExportToBeans(Recordset resetP) {
        //初始化点表excel字段名
        FieldInfos _fieldInfos = resetP.getFieldInfos();
        int _fileCountp = _fieldInfos.getCount();
        //字段名集合
        m_excelFiledNameP = new ArrayList<>();
        for (int _i = 0; _i < _fileCountp; _i++) {
            //除去记录集中的系统字段和SmUserID
            if (_fieldInfos.get(_i).isSystemField() || _fieldInfos.get(_i).getName().equals("SmUserID")) {
                continue;
            }
            //把必要的字段添加到list，设置excel点表字段
            m_excelFiledNameP.add(_fieldInfos.get(_i).getName());
        }
        //Point数据导出excel
        if (!resetP.isEmpty()) {
            resetP.moveFirst();
            m_pointListChild = new ArrayList<>();
            BaseFieldPInfos _pipePoint1 = null;
            //把所有的记录集转成bean，添加到list
            while (!resetP.isEOF()) {
                _pipePoint1 = BaseFieldPInfos.createFieldInfo(resetP, 1);
                if (_pipePoint1 != null) {
                    m_pointListChild.add(_pipePoint1);
                }
                resetP.moveNext();
            }
            ArrayList<Object> list = null;
            Field[] _fields = null;
            String _type = "";
            for (BaseFieldPInfos _pipePoint : m_pointListChild) {
                list = new ArrayList<>();
                _fields = _pipePoint.getClass().getFields();
                //去掉三个是因为bean字段数据里有3个字段是系统默认字段
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
                        LogUtills.i(e.toString());
                    }
                }
                m_pointListGroup.add(list);
            }
        }
        if (resetP != null) {
            resetP.close();
            resetP.dispose();
        }
    }

    /**
     * 线记录导出数据成bean，然后添加到list
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/27  15:46
     */
    private void reSetLrExportToBeans(Recordset resetL) {
        //初始化线表字段名
        FieldInfos _fieldInfosL = resetL.getFieldInfos();
        int _fileCount = _fieldInfosL.getCount();
        m_excelFiledNameL = new ArrayList<>();
        for (int _i = 0; _i < _fileCount; _i++) {
            if (_fieldInfosL.get(_i).isSystemField() || _fieldInfosL.get(_i).getName().equals("SmUserID")) {
                continue;
            }
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
                if (_fieldInfosL != null) {
                    m_lineListChild.add(_pipeLine1);
                }
            }

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
                        LogUtills.i(e.toString());
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
     * 导出测量点号表格
     *
     * @Params :
     * @author :HaiRun
     * @date :2020-12-09  17:33
     */
    public void exportMeasureData(String start, String end) {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DatasetVector datasetMeasurePoint = (DatasetVector) WorkSpaceUtils.getInstance().getWorkspace().getDatasources().get(0).getDatasets().get("P_" + SuperMapConfig.Layer_Measure);
                    Recordset recordset = datasetMeasurePoint.query("exp_Date between '" + start + "' and '" + end + "' ", CursorType.STATIC);
                    if (!recordset.isEmpty()) {
                        //创建工程相关文件夹
                        String _prjFolder = SuperMapConfig.DEFAULT_DATA_PATH + m_prjId;
                        //创建测量点记录表
                        FileUtils.getInstance().mkdirs(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_MEASURE_PATH);
                        List<String> listTitle = getMeaserPointTitles();
                        List<List<String>> list = recordSetToMap(recordset);
                        //判断文件夹里有多少个excel文件
                        int fileCount = FileUtils.getInstance().getFileIndexMax(_prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_MEASURE_PATH);
                        //文件夹+1,命名新的excel
                        ++fileCount;
                        //  poi 库导出数据  初始化excel表格
                        String filePath = _prjFolder + "/" + SuperMapConfig.DEFAULT_DATA_MEASURE_PATH + m_prjId + "-" + String.valueOf(fileCount) + ".xls";
                        ExcelUtilsOfPoi.initExcelMeasure(filePath, listTitle, "测量点");
                        //导出数据表格
                        ExcelUtilsOfPoi.writeObjListToExcel(0, list, filePath);
                        ToastyUtil.showSuccessShort(m_context, "导出成功");
                    }

                } catch (Exception e) {
                    LogUtills.e(e.toString());
                    ToastyUtil.showErrorShort(m_context, "导出失败");
                }
            }
        });
    }

    /**
     * 测量点记录集转换成Map
     *
     * @Params :
     * @author :HaiRun
     * @date :2020-12-09  17:11
     */
    private List<List<String>> recordSetToMap(Recordset recordset) {
        List<List<String>> list = new ArrayList<>();
        List<String> list1 = null;
        while (!recordset.isEOF()) {
            list1 = new ArrayList<>();
            list1.add(recordset.getString("exp_Num"));
            list1.add(recordset.getString("id"));
            list1.add(String.valueOf(recordset.getDouble("SmX")));
            list1.add(String.valueOf(recordset.getDouble("SmY")));
            list1.add(recordset.getString("exp_Date"));
            list.add(list1);
            recordset.moveNext();
        }
        return list;
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
    public static boolean importData(List<Map<String, Object>> baseFieldPInfos, List<Map<String, Object>> baseFileLInfos) {
        long time = System.currentTimeMillis();
        LogUtills.i("StartTime", time + "");

        //点导入
        if (!OperDataSet.getSingleton().createPointSetByBean(baseFieldPInfos)) {
            return false;
        }
        //线导入
        if (!OperDataSet.getSingleton().createLineSetByBean(baseFileLInfos)) {
            return false;
        }
        long endTime = System.currentTimeMillis();
        LogUtills.i("CurTime", endTime - time + "");
        return true;
    }

    /**
     * 导入数据
     *
     * @Author HaiRun
     * @Time 2019/3/7 . 10:26
     * 1.读取手机中的excel表 获取里面的数据
     * 2.根据或者到的数据，判断数据属于那一中管类，然后直接添加到这个管类数据集中
     */
    public static boolean importData(List<Map<String, Object>> list) {
        //点导入
        if (!OperDataSet.getSingleton().createPointSetByMap(list)) {
            return false;
        }
        return true;
    }

}
