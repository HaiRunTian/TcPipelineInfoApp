package com.app.pipelinesurvey.utils;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.utills.LogUtills;

import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * @author Created by HaiRun on 2018/12/3.
 */

public class ImportDataMainThread extends Thread {
    String zipFile;
    String folderPath;
    ImportDataProgressUtil.ImportListener listener;
    int type;

    public ImportDataMainThread(String zipFile, int type, ImportDataProgressUtil.ImportListener listener) {
        this.zipFile = zipFile;
        this.folderPath = folderPath;
        this.listener = listener;
        this.type = type;
    }

    @Override
    public void run() {
        super.run();
        ZipFile zfile = null;
        try {
            listener.zipStart();
            // 获取解压之后文件的大小,用来计算解压的进度
            int[] ziplength = getZipTrueSize(zipFile);
            listener.zipProgress(ziplength);
            //从Excel中读取点数据，存入list中 jxi
            List<Map<String, Object>> points = ExcelUtilsOfPoi.readExcelDataToBean(new File(zipFile), 0);
            boolean importData = false;
            if (type == 0) {
                //从Excel中读取线数据，存入list中
                List<Map<String, Object>> lines = ExcelUtilsOfPoi.readExcelDataToBean(new File(zipFile), 1);
                importData = ExportDataUtils.importData(points, lines);
            } else {
                importData = ExportDataUtils.importData(points);
            }
            if (importData) {
                listener.zipSuccess();
            } else {
                listener.zipFail();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtills.i("Importdata", e.toString());
            listener.zipFail();
        }
    }

    int lastProgress = 0;

    ///以后需要用到更新进度条
   /* //更新精度条
    private void updateProgress(int progress, ImportDataProgressUtil.ImportListener listener2) {
        * 因为会频繁的刷新,这里我只是进度>1%的时候才去显示
        if (progress > lastProgress) {
            lastProgress = progress;
            listener2.zipProgress(progress);
        }
    }
*/

    /**
     * 获取excel数据量大小
     *
     * @param filePath 文件路径
     * @return 返回内存long类型的值
     */
    public int[] getZipTrueSize(String filePath) {
        int[] size = new int[2];
        //点数量
        size[0] = ExcelUtilsOfPoi.getExcelRows(new File(filePath), 0);
        //线数量
        if (type == 0) {
            size[1] = ExcelUtilsOfPoi.getExcelRows(new File(filePath), 1);
        } else {
            size[1] = 0;
        }
        return size;
    }
}
