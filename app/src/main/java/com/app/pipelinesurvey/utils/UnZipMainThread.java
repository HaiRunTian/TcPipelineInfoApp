package com.app.pipelinesurvey.utils;


import com.app.utills.LogUtills;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Enumeration;
import java.util.zip.ZipInputStream;

import static com.app.pipelinesurvey.utils.Decompressor.getRealFileName;


/**
 * Created by HaiRun on 2018/12/3.
 */

public class UnZipMainThread extends Thread {
    String zipFile;
    String folderPath;
    ZipProgressUtil.ZipListener listener;

    public UnZipMainThread(String zipFile, String folderPath, ZipProgressUtil.ZipListener listener) {
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
            long ziplength = getZipTrueSize(zipFile);
//            LogUtills.i("====文件的大小==", ziplength + "");
//            FileInputStream inputStream = new FileInputStream(zipFileString);
//            ZipInputStream inZip = new ZipInputStream(inputStream);
            zfile = new ZipFile(zipFile, "GBK");
            Enumeration zList = zfile.getEntries();
            ZipEntry ze = null;
            byte[] buf = new byte[1024];
            while (zList.hasMoreElements()) {
                ze = (ZipEntry) zList.nextElement();
                //列举的压缩文件里面的各个文件，判断是否为目录
                if (ze.isDirectory()) {
                    String dirstr = folderPath + ze.getName();
                    dirstr.trim();
                    File f = new File(dirstr);
                    f.mkdir();
                    continue;
                }
                OutputStream os = null;
                FileOutputStream fos = null;
                // ze.getName()会返回 script/start.script这样的，是为了返回实体的File
                File realFile = getRealFileName(folderPath, ze.getName());
                try {
                    fos = new FileOutputStream(realFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
                os = new BufferedOutputStream(fos);
                InputStream is = null;
                try {
                    is = new BufferedInputStream(zfile.getInputStream(ze));
                } catch (IOException e) {
                    e.printStackTrace();

                }
                int readLen = 0;
                //进行一些内容复制操作
                try {
                    while ((readLen = is.read(buf, 0, 1024)) != -1) {
                        os.write(buf, 0, readLen);
                        sumLength += readLen;
                        int progress = (int) ((sumLength * 100) / ziplength);
                        updateProgress(progress, listener);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.zipFail();

                }
                try {
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.zipFail();

                }
            }
            try {
                zfile.close();
            } catch (IOException e) {
                e.printStackTrace();
                listener.zipFail();
            }
            listener.zipSuccess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int lastProgress = 0;
    private void updateProgress(int progress, ZipProgressUtil.ZipListener listener2) {
        /** 因为会频繁的刷新,这里我只是进度>1%的时候才去显示 */
        if (progress > lastProgress) {
            lastProgress = progress;
            listener2.zipProgress(progress);
        }
    }

    /**
     * 获取压缩包解压后的内存大小
     *
     * @param filePath 文件路径
     * @return 返回内存long类型的值
     */
    public long getZipTrueSize(String filePath) {
        long size = 0;
        ZipFile f;
        try {
            f = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> en = f.getEntries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }
}
