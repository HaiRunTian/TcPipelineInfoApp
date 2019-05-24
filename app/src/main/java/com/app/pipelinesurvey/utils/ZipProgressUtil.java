package com.app.pipelinesurvey.utils;

/**
 * Created by HaiRun on 2018/12/3.
 */

public class ZipProgressUtil {
    /***
     * 解压通用方法
     *
     * @param zipFileString
     *            文件路径
     * @param outPathString
     *            解压路径
     * @param listener
     *            加压监听
     */
    public static void UnZipFile(final String zipFileString, final String outPathString, final ZipListener listener) {
        Thread zipThread = new UnZipMainThread(zipFileString, outPathString, listener);
        zipThread.start();
    }

    public interface ZipListener {
        /** 开始解压 */
        void zipStart();

        /** 解压成功 */
        void zipSuccess();

        /** 解压进度 */
        void zipProgress(int progress);

        /** 解压失败 */
        void zipFail();
    }
}
