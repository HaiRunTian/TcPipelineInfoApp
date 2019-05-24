package com.app.pipelinesurvey.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;


/**
 * Created by Kevin on 2018-09-18.
 */

public class PermissionUtils {

    private static String[] mPermissionList = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_SETTINGS
    };
    /**
     * 初始化权限
     *
     * @param aci
     */
    public static void initPermission(final Activity aci, final CallBack callBack) {
        AndPermission.with(aci)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //有权限
                        callBack.onGranted();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //没权限
                        callBack.onDenied();
                    }
                })
                .permission(mPermissionList)
                .start();
    }

    /**
     * 单个权限申请
     */
    public static void applyPermission(final Activity aci, final CallBack callBack, final String... permission) {
        AndPermission.with(aci)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //有权限
                        callBack.onGranted();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //没权限
                        callBack.onDenied();
                    }
                })
                .permission(permission)
                .start();
    }

    public static String[] getPermissions() {
        return mPermissionList;
    }

    public static boolean checkPermission(Context context, String permission) {
        return AndPermission.hasAlwaysDeniedPermission(context, permission);
    }

    public interface CallBack {
        void onGranted();

        void onDenied();
    }

    public static class PermissionHolder implements CallBack {


        @Override
        public void onGranted() {

        }

        @Override
        public void onDenied() {


        }
    }
}
