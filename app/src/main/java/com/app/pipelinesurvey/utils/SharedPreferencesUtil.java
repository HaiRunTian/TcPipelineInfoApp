package com.app.pipelinesurvey.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

/**
 * @author linshen on 2019-06-19.
 * 邮箱: 18475453284@163.com
 *轻量数据存储
 *
 */
public class SharedPreferencesUtil {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferencesUtil sharedPreferencesUtil;

    public SharedPreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences("password", Context.MODE_PRIVATE);
    }
    /**
     *单例
     */
    public static SharedPreferencesUtil getInstance(Context context){
        if (sharedPreferencesUtil ==null){
            sharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return sharedPreferencesUtil;
    }
    /**
     *设置第一次打开APP
     */
    public static Boolean getBoolean(String strKey, boolean strData) {
        Boolean result = sharedPreferences.getBoolean(strKey, strData);
        return result;
    }
    /**
     *设置第二次打开APP后改变状态
     */
    public static void putBoolean( String strKey, Boolean strData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.apply();
    }
    /**
     *读取数据
     */
    public static String getString(@NonNull String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    /**
     *读取数据
     */
    public static int getInt(@NonNull String key, Integer defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
    /**
     *保存数据
     */
    public static void putString(String strKey, String strData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }


    /**
     * 保存数据到SharedPreferences
     *
     * @param key   键
     * @param value 需要保存的数据
     * @return 保存结果
     */
    public static boolean saveValue(String key, Object value) {
        boolean result;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String type = value.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    editor.putLong(key, (Long) value);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) value);
                    break;
                case "String":
                    editor.putString(key, (String) value);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) value);
                    break;
                default:
                    break;
            }
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param key   键
     * @param value 需要保存的数据
     * @return 保存结果
     */
    public static boolean putData(String key, Object value) {
        boolean result;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String type = value.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    editor.putLong(key, (Long) value);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) value);
                    break;
                case "String":
                    editor.putString(key, (String) value);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) value);
                    break;
                default:
                    //                    Gson gson = new Gson();
                    //                    String json = gson.toJson(value);
                    //                    editor.putString(key, json);
                    break;
            }
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }
    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(String key) {
        return sharedPreferences.contains(key);
    }
    /**
     * 删除指定的数据
     * @param key
     */
    public static void deletekey(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
    /**
     * 清除所有的数据
     */
    public static void deleteObject() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
