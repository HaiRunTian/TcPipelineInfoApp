package com.app.pipelinesurvey.utils;

import com.app.BaseInfo.Data.BaseFieldInfos;

import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.util.List;


/**
 * Created by HaiRun on 2018/12/3.
 */

public class ImportDataMainThread extends Thread {
    String zipFile;
    String folderPath;
    ImportDataProgressUtil.ImportListener listener;

    public ImportDataMainThread(String zipFile, ImportDataProgressUtil.ImportListener listener) {
        this.zipFile = zipFile;
        this.folderPath = folderPath;
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        ZipFile zfile = null;
        try {
            listener.zipStart();
            long sumLength = 0;
            // 获取解压之后文件的大小,用来计算解压的进度
            int[] ziplength = getZipTrueSize(zipFile);
            listener.zipProgress(ziplength);

//            //从Excel中读取点数据，存入list中 poi
//            List<BaseFieldInfos> _baseFieldPInfos = ExcelUtilsOfPoi.readExcelDataToBean(new File(zipFile), 0);
//            //从Excel中读取线数据，存入list中
//            List<BaseFieldInfos> _baseFieldLInfos = ExcelUtilsOfPoi.readExcelDataToBean(new File(zipFile), 1);
//

            //从Excel中读取点数据，存入list中 jxi
            List<BaseFieldInfos> _baseFieldPInfos = ExcelUtilsOfJxi.read2DB(new File(zipFile), 0);
            //从Excel中读取线数据，存入list中
            List<BaseFieldInfos> _baseFieldLInfos = ExcelUtilsOfJxi.read2DB(new File(zipFile), 1);
            boolean importData = ExportDataUtils.importData(_baseFieldPInfos, _baseFieldLInfos);
            if (importData) {
                listener.zipSuccess();
            } else {
                listener.zipFail();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        size[0] = ExcelUtilsOfJxi.getExcelRows(new File(filePath), 0);
        //线数量
        size[1] = ExcelUtilsOfJxi.getExcelRows(new File(filePath), 1);
        return size;
    }
}
