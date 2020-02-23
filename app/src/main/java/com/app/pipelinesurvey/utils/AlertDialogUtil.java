package com.app.pipelinesurvey.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.app.pipelinesurvey.R;

/**
 * @author
 */

public class AlertDialogUtil {

    public static void showDialog(Context context, String title, String msg, boolean cancelable,
                                  DialogInterface.OnClickListener onOkClickListener) {
        AlertDialog _dialog = new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(cancelable)
                .setPositiveButton("确定", onOkClickListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        _dialog.show();
    }

    public static AlertDialog showDialog(Context context, String title, String msg, int iconId, View view,
                                         boolean cancelable, DialogInterface.OnClickListener onOkClickListener) {
        ViewGroup _viewGroup = (ViewGroup) view.getParent();
        if (_viewGroup != null) {
            _viewGroup.removeAllViews();
        }
        final AlertDialog _dialog = new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setIcon(iconId)
                .setView(view)
                .setCancelable(cancelable)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", onOkClickListener)
                .create();
        _dialog.show();
        return _dialog;
    }


    public static AlertDialog showDialog(Context context, String title, String msg,boolean cancelable, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogInterface.OnClickListener onClickListener3) {
        AlertDialog _dialog = new AlertDialog
                .Builder(context)
                .setIcon(R.mipmap.ic_logo)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(cancelable)
                .setPositiveButton("QQ",onClickListener)
                .setNegativeButton("管智绘", onClickListener2)
                .setNeutralButton("微信",onClickListener3)
                .create();
        _dialog.show();
        return _dialog;
    }
}
