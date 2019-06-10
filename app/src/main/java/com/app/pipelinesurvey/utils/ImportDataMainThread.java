package com.app.pipelinesurvey.utils;


import com.app.BaseInfo.Data.BaseFieldInfos;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import static com.app.pipelinesurvey.utils.Decompressor.getRealFileName;


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
            //从Excel中读取点数据，存入list中
            List<BaseFieldInfos> _baseFieldPInfos = ExcelUtils.read2DB(new File(zipFile), 0);
            //从Excel中读取线数据，存入list中
            List<BaseFieldInfos> _baseFieldLInfos = ExcelUtils.read2DB(new File(zipFile), 1);

            boolean importData = ExportDataUtils.importData(_baseFieldPInfos,_baseFieldLInfos);

            if (importData){
                listener.zipSuccess();
            }else {
                listener.zipFail();
            }

        } catch (Exception e) {
            e.printStackTrace();
            listener.zipFail();
        }
    }

    int lastProgress = 0;

  /*  //更新精度条
    private void updateProgress(int progress, ImportDataProgressUtil.ImportListener listener2) {
        *//** 因为会频繁的刷新,这里我只是进度>1%的时候才去显示 *//*
        if (progress > lastProgress) {
            lastProgress = progress;
            listener2.zipProgress(progress);
        }
    }*/

    /**
     * 获取excel数据量大小
     *
     * @param filePath 文件路径
     * @return 返回内存long类型的值
     */
    public int[] getZipTrueSize(String filePath) {
        int[] size = new int[2];
        size[0] = ExcelUtils.getExcelRows(new File(filePath), 0);

        //线表
        size[1] = ExcelUtils.getExcelRows(new File(filePath), 1);
        //线数量

        return size;
    }
}
