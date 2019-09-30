package com.app.pipelinesurvey.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * OKhttp工具类
 *
 * @author Kevin
 * @date 2018/1/17
 */

public class OkHttpUtils {
    /**
     * 静态实例
     */
    private static OkHttpUtils m_Instance;
    /**
     * okhttpclient实例
     */
    private static OkHttpClient m_okHttpClient;
    /**
     * 因为我们请求数据一般都是子线程中请求，在这里我们使用了handler
     */
    private static Handler m_handler;

    private static final String TAG = "OkHttpUtils";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String m_url2;

    public OkHttpUtils() {

    }
    /**
     * 单例模式  获取OkHttp实例
     *
     * @return okhttp实例
     */
    public static OkHttpUtils getInstance() {
        if (m_Instance == null) {
            m_Instance = new OkHttpUtils();
            m_Instance.initConfig();
        }
        return m_Instance;
    }

    public void initConfig() {
        // 可以通过实现 Logger 接口更改日志保存位置
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        m_okHttpClient = new OkHttpClient();
        /**
         * 在这里直接设置连接超时.读取超时，写入超时
         */
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.addInterceptor(loggingInterceptor);
        m_okHttpClient = builder.build();

        /**
         * 初始化handler
         */
        m_handler = new Handler(Looper.getMainLooper());
    }


    //-------------------------异步的方式请求数据(get)--------------------------

    public static void getAsync(String url, InsertDataCallBack callBack) {
        getInstance().inner_getAsync(url, callBack);
    }

    /**
     * 内部逻辑请求的方法
     *
     * @param url
     * @param callBack
     * @return
     */
    private void inner_getAsync(String url, final InsertDataCallBack callBack) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        m_okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    deliverDataFailure(request, e, callBack);
                }
                deliverDataSuccess(result, callBack);
            }
        });
    }

    /**
     * 插入分发失败的时候调用
     *
     * @param request
     * @param e
     * @param callBack
     */
    private void deliverDataFailure(final Request request, final IOException e, final InsertDataCallBack callBack) {
        /**
         * 在这里使用异步处理
         */
        m_handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestFailure(request, e);
                }
            }
        });
    }

    /**
     * 插入分发成功的时候调用
     *
     * @param result
     * @param callBack
     */
    private void deliverDataSuccess(final String result, final InsertDataCallBack callBack) {
        /**
         * 在这里使用异步线程处理
         */
        m_handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.requestSuccess(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 查询分发失败的时候调用
     *
     * @param request
     * @param e
     * @param callBack
     */
    private void deliverDataFailure(final Request request, final IOException e, final QueryDataCallBack callBack) {
        /**
         * 在这里使用异步处理
         */
        m_handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestFailure(request, e);
                }
            }
        });
    }

    /**
     * 查询分发成功的时候调用
     *
     * @param result
     * @param callBack
     */
    private void deliverDataSuccess(final String result, final QueryDataCallBack callBack) {
        /**
         * 在这里使用异步线程处理
         */
        m_handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.requestSuccess(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 插入数据回调接口
     */
    public interface InsertDataCallBack {
        void requestFailure(Request request, IOException e);

        void requestSuccess(String result) throws Exception;
    }

    /**
     * 查询数据回调接口
     */
    public interface QueryDataCallBack {
        void requestFailure(Request request, IOException e);

        void requestSuccess(String jsonString) throws Exception;
    }

    //-------------------------提交表单--------------------------


    /*插入数据*/
    public static void postAsync(String url, String jsonString, InsertDataCallBack callBack) {
        getInstance().inner_postAsync(url, jsonString, callBack);
    }

    private void inner_postAsync(String url, String jsonString, final InsertDataCallBack callBack) {

        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        /**
         * 3.0之后版本
         */
        //结果返回
        // 请求对象
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        m_okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                deliverDataSuccess(result, callBack);
            }

        });
    }

    /*查询数据*/
    public static void postAsync(String url, String jsonString, QueryDataCallBack callBack) {
        getInstance().inner_postAsync(url, jsonString, callBack);
    }

    private void inner_postAsync(String url, String jsonString, final QueryDataCallBack callBack) {

        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        /**
         * 3.0之后版本
         */
        //结果返回
        // 请求对象
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        m_okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                deliverDataSuccess(result, callBack);
            }

        });
    }


    /**-------------------------文件下载--------------------------**/
    public static void downloadAsync(String url, String desDir, InsertDataCallBack callBack) {
        getInstance().inner_downloadAsync(url, desDir, callBack);
    }
    /**
     * 下载文件的内部逻辑处理类
     *
     * @param url      下载地址
     * @param desDir   目标地址
     * @param callBack
     */
    private void inner_downloadAsync(final String url, final String desDir, final InsertDataCallBack callBack) {

        try {
            URL _url = new URL(url);
            final Request request;
            request = new Request.Builder().url(_url).build();
            m_okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    deliverDataFailure(request, e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    /**
                     * 在这里进行文件的下载处理
                     */
                    InputStream inputStream = null;
                    FileOutputStream fileOutputStream = null;
                    try {
                        //文件名和目标地址
                        File file = new File(desDir, getFileName(url));
                        //把请求回来的response对象装换为字节流
                        inputStream = response.body().byteStream();
                        fileOutputStream = new FileOutputStream(file);
                        int len = 0;
                        byte[] bytes = new byte[2048];
                        //循环读取数据
                        while ((len = inputStream.read(bytes)) != -1) {
                            fileOutputStream.write(bytes, 0, len);
                        }
                        //关闭文件输出流
                        fileOutputStream.flush();
                        //调用分发数据成功的方法
                        deliverDataSuccess(file.getAbsolutePath(), callBack);
                    } catch (IOException e) {
                        //如果失败，调用此方法
                        deliverDataFailure(request, e, callBack);
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }

                    }
                }

            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件url获取文件的路径名字
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        int separatorIndex = url.lastIndexOf("/");
        String path = (separatorIndex < 0) ? url : url.substring(separatorIndex + 1, url.length());
        return path;
    }


    //
    //    //-------------------------同步的方式请求数据--------------------------
    //    /**
    //     * GET方式请求的内部逻辑处理方式
    //     *
    //     * @param url
    //     * @return Response
    //     */
    //    private Response inner_getSync(String url) {
    //        Request request = new Request.Builder()
    //                .url(url)
    //                .build();
    //        Response response = null;
    //        try {
    //            //同步请求返回的是response对象
    //            response = m_okHttpClient.newCall(request).execute();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        return response;
    //    }
    //
    //    /**
    //     * 对外提供的get方法获取response
    //     *
    //     * @param url 传入的地址
    //     * @return Response
    //     */
    //    public static Response getSync(String url) {
    //
    //        //通过获取到的实例来调用内部方法
    //        return m_Instance.inner_getSync(url);
    //    }
    //
    //    /**
    //     * 同步方法
    //     */
    //    private String inner_getSyncString(String url) {
    //        String result = null;
    //        try {
    //            /**
    //             * 把取得到的结果转为字符串，这里最好用string()
    //             */
    //            result = inner_getSync(url).body().string();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        return result;
    //    }
    //
    //    /**
    //     * 对外提供的同步获取String的方法
    //     *
    //     * @param url
    //     * @return
    //     */
    //    public static String getSyncString(String url) {
    //        return m_Instance.inner_getSyncString(url);
    //    }
}
