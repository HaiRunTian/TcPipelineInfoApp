package com.app.pipelinesurvey.view.widget;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.app.pipelinesurvey.R;

import java.lang.reflect.Parameter;

/**
 * @author HaiRun
 * @time 2019/6/6.13:46
 */
public class LoadingImgDialog {

    private Context context;
    private PopupWindow popupDialog;
    private LayoutInflater layoutInflater;
    private RelativeLayout layout;
    private RelativeLayout layout_bg;
    private int residBg;
    private View loading_dialog;
    /** 背景添加旋转动画效果，实现了转动动作   **/
    private RotateAnimation rotateAnim;
    /** 透明度动画效果  **/
    private AlphaAnimation alphaAnim_in;
    private AlphaAnimation alphaAnim_out;

    public LoadingImgDialog(Context context, int residBg) {
        layoutInflater = LayoutInflater.from(context);
        this.residBg = residBg;
        this.context = context;
    }
    private void initAnim() {
        rotateAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(2000);
        rotateAnim.setRepeatMode(Animation.RESTART);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.setInterpolator(new LinearInterpolator());
        alphaAnim_in = new AlphaAnimation(0f, 1f);
        alphaAnim_in.setFillAfter(true);
        alphaAnim_in.setDuration(200);
        alphaAnim_in.setInterpolator(new LinearInterpolator());
        alphaAnim_out = new AlphaAnimation(1f, 0f);
        alphaAnim_out.setFillAfter(true);
        alphaAnim_out.setDuration(100);
        alphaAnim_out.setInterpolator(new LinearInterpolator());

        /** 监听动作，动画结束时，隐藏LoadingColorDialog **/
        alphaAnim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                dismiss();
            }
        });
    }

    /**
     * 判断是否显示
     * @return
     */
    public boolean isShowing() {
        if (popupDialog != null && popupDialog.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * 显示
     */
    public void show() {
        dismiss();

        initAnim();

        layout = (RelativeLayout) layoutInflater.inflate(R.layout.view_loadingdialog, null);
        loading_dialog = (View) layout.findViewById(R.id.loading_dialog);
        loading_dialog.setBackgroundResource(residBg);

        layout_bg = (RelativeLayout) layout.findViewById(R.id.bgLayout);
        popupDialog = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        View parentView = ((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        popupDialog.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        layout_bg.startAnimation(alphaAnim_in);
        loading_dialog.startAnimation(rotateAnim);
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        if (popupDialog != null && popupDialog.isShowing()) {
            layout_bg.clearAnimation();
            loading_dialog.clearAnimation();
            popupDialog.dismiss();
        }
    }
}
