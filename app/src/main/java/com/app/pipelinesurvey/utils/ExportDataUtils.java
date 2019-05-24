package com.app.pipelinesurvey.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Oper.DataHandlerObserver;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.utills.LogUtills;
import com.supermap.data.Charset;
import com.supermap.data.CursorType;
import com.supermap.data.DataConversion;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.FieldInfos;
import com.supermap.data.Recordset;
import com.supermap.data.Workspace;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
        dialog.setMessage("数据正在导出到手机根目录文件夹中<天驰管调通>，请等待……");
        dialog.show();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //点数据集
                    DatasetVector _dsVectorP = (DatasetVector) DataHandlerObserver.ins().getTotalPtLayer().getDataset();
                    //去掉临时点
                    Recordset _reSetP = _dsVectorP.query("exp_Num Not like 'T_%'", CursorType.STATIC);

                    LogUtills.i("_reSep count = " + _reSetP.getRecordCount());
                    //线数据集
                    DatasetVector _dsVectorL = (DatasetVector) DataHandlerObserver.ins().getTotalLrLayer().getDataset();

                    //去掉临时线
                    Recordset _reSetL = _dsVectorL.query("endExpNum Not like 'T_%'", CursorType.STATIC);
                    LogUtills.i("_reSep count = " + _reSetL.getRecordCount());
                    //创建工程文件夹
                    String _prjFolder = SuperMapConfig.DEFAULT_DATA_PATH + m_prjId;
                    FileUtils.getInstance().mkdirs(SuperMapConfig.DEFAULT_DATA_PATH + m_prjId);


                    //点线导出shp
                    exportDataToShp(_dsVectorP, _prjFolder);
                    exportDataToShp(_dsVectorL, _prjFolder);
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
                    //初始化excel表格
                    ExcelUtils.initExcel(_prjFolder + "/Excel/" + m_prjId + "-" + String.valueOf(m_fileCount) + ".xls", m_excelFiledNameP, m_excelFiledNameL, _dsVectorP.getName(), _dsVectorL.getName());
                    //点 线导出excel 创建项目文件夹
                    ExcelUtils.writeObjListToExcel(0, m_pointListGroup, _prjFolder + "/Excel/" + m_prjId + "-" + String.valueOf(m_fileCount) + ".xls", m_context);
                    ExcelUtils.writeObjListToExcel(1, m_lineListGroup, _prjFolder + "/Excel/" + m_prjId + "-" + String.valueOf(m_fileCount) + ".xls", m_context);
                    ToastUtil.show(m_context, "数据导出成功", Toast.LENGTH_LONG);

                } catch (Exception e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
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
            for (BaseFieldPInfos _pipePoint : m_pointListChild) {
                list = new ArrayList<>();
                Field[] _fields = _pipePoint.getClass().getFields();
                for (int _i = 0; _i < _fields.length - 3; _i++) {
                    try {
                        String _name = _fields[_i].getName();
                        String _type = _fields[_i].getType().getCanonicalName();
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
        resetP.close();
        resetP.dispose();

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
            for (BaseFieldLInfos _pipeLine : m_lineListChild) {
                list = new ArrayList<>();
                Field[] _fields = _pipeLine.getClass().getFields();
                for (int _i = 0; _i < _fields.length - 3; _i++) {
                    try {
                        String _type = _fields[_i].getType().getCanonicalName();

                        switch (_type) {
                            case "double":
                            case "java.lang.String":
                            case "int":
                            case "com.app.BaseInfo.Data.POINTTYPE":
                                LogUtills.i("添加数据 java type = ", _fields[_i].getName() + "-----------" + _type);
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
        resetL.close();
        resetL.dispose();
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
    public void importData(File file) {
        //点表
        List<BaseFieldInfos> _baseFieldPInfos = ExcelUtils.read2DB(file, 0, m_context);
        for (int _i = 0; _i < _baseFieldPInfos.size(); _i++) {
            BaseFieldPInfos _baseFieldPInfos1 = (BaseFieldPInfos) _baseFieldPInfos.get(_i);
            if (!DataHandlerObserver.ins().createRecords2(_baseFieldPInfos1)) {
                ToastUtil.showShort(m_context, "点数据导入失败");
            }
        }

        //线表
        List<BaseFieldInfos> _baseFieldPInfos2 = ExcelUtils.read2DB(file, 1, m_context);
        for (int _i = 0; _i < _baseFieldPInfos2.size(); _i++) {
            BaseFieldLInfos _baseFieldLInfos1 = (BaseFieldLInfos) _baseFieldPInfos2.get(_i);
            if (!DataHandlerObserver.ins().addRecords(_baseFieldLInfos1)) {
                ToastUtil.showShort(m_context, "线数据导入失败");
            }
        }

        ToastUtil.showShort(m_context, "数据导入成功");
    }
}
