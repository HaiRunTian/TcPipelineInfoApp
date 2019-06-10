package com.app.pipelinesurvey.utils;

/**
 * Created by HaiRun on 2018/12/3.
 */

public class ImportDataProgressUtil {
    /***
     * 数据导入通用方法
     *
     * @param zipFileString
     *            数据路径
     * @param
     *
     * @param listener
     *            加压监听
     */
    public static void ImportData(final String zipFileString,  final ImportListener listener) {
        Thread zipThread = new ImportDataMainThread(zipFileString, listener);
        zipThread.start();
    }

    public interface ImportListener {
        /** 开始导入 */
        void zipStart();

        /** 导入成功 */
        void zipSuccess();

        /** 导入进度 */
        void zipProgress(int[] progress);

        /** 导入失败 */
        void zipFail();
    }
}
