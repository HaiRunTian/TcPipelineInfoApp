package com.app.pipelinesurvey.view.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.utils.ColorUtls;

import java.util.Locale;

/**
 * 取色器对话框
 * Created by Kevin on 2019-04-04.
 */

public class ColorPickerDialog extends Dialog implements ColorPickerView.OnColorChangedListener,
        View.OnClickListener, TextView.OnEditorActionListener{

    private ColorPickerView mColorPicker;
    private View mOldColor;
    private View mNewColor;
    private View mHexLayout;
    private EditText mEtHex;
    private boolean mHexValueEnabled = false;
    private ColorStateList mHexDefaultTextColor;
    private OnColorPickedListener mListener;
    private int up;
    public interface OnColorPickedListener {
        void onColorPicked(int color);
    }

    public ColorPickerDialog(Context context, OnColorPickedListener onColorPickedListener, int initialColor) {
        super(context);
        setUp(initialColor);
    }
    private void setUp(int color) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_color_picker, null);
        setContentView(layout);

        mColorPicker = (ColorPickerView) layout.findViewById(R.id.color_picker_view);
        mOldColor = layout.findViewById(R.id.old_color_panel);
        mNewColor = layout.findViewById(R.id.new_color_panel);

        mHexLayout = layout.findViewById(R.id.hex_layout);
        mEtHex = (EditText) layout.findViewById(R.id.et_hex);
        mEtHex.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mHexDefaultTextColor = mEtHex.getTextColors();

        //当点击软键盘上的【完成】按钮时触发监听
        mEtHex.setOnEditorActionListener(this);
        int padding = Math.round(mColorPicker.getDrawingOffset());
        layout.findViewById(R.id.preview_layout).setPadding(padding, 0, padding, 0);
        View mBtnCancel = layout.findViewById(R.id.tv_cancel);
        View mBtnConfirm = layout.findViewById(R.id.tv_confirm);
        mBtnCancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mColorPicker.setOnColorChangedListener(this);
        // 颜色预览色板上显示旧颜色
        mOldColor.setBackgroundColor(color);
        // 为ColorPickerView设置初始颜色
        mColorPicker.setColor(color, true);
    }




    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            String hexVal = mEtHex.getText().toString();
            if (hexVal.length() >= 0 || hexVal.length() < 7) {
                try {
                    int c = ColorUtls.convertToColorInt(hexVal);
                    mColorPicker.setColor(c, true);
                    mEtHex.setTextColor(mHexDefaultTextColor);
                } catch (IllegalArgumentException e) {
                    mEtHex.setTextColor(Color.RED);
                }
            } else {
                mEtHex.setTextColor(Color.RED);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onColorChanged(int color) {
        mNewColor.setBackgroundColor(color);
        if (mHexValueEnabled) updateHexValue(color);
    }

    public void setHexValueEnabled(boolean enable) {
        mHexValueEnabled = enable;
        if (enable) {
            mHexLayout.setVisibility(View.VISIBLE);
            updateHexLengthFilter();
            updateHexValue(getColor());
        } else
            mHexLayout.setVisibility(View.GONE);
    }

    public boolean getHexValueEnabled() {
        return mHexValueEnabled;
    }

    private void updateHexLengthFilter() {
        mEtHex.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
    }
    private void updateHexValue(int color) {
        mEtHex.setText(ColorUtls.convertToRGB(color).toUpperCase(Locale.getDefault()));
        mEtHex.setTextColor(mHexDefaultTextColor);
    }
    /**
     * 设置颜色改变的监听器
     *
     * @param listener 颜色改变监听器
     */
    public void setOnColorChangedListener(OnColorPickedListener listener) {
        mListener = listener;
    }

    public int getColor() {
        return mColorPicker.getColor();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_confirm) {
            if (mListener != null) {
                mListener.onColorPicked(((ColorDrawable) mNewColor.getBackground()).getColor());
            }
        }
        dismiss();
    }

    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt("old_color", ((ColorDrawable) mOldColor.getBackground()).getColor());
        state.putInt("new_color", ((ColorDrawable) mNewColor.getBackground()).getColor());
        return state;
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mOldColor.setBackgroundColor(savedInstanceState.getInt("old_color"));
        mColorPicker.setColor(savedInstanceState.getInt("new_color"), true);
    }

    public static class Builder {
        private Context context;
        private int initColor;
        private boolean hexValueEnabled;
        private OnColorPickedListener listener;

        public Builder(Context context, int initColor) {
            this.context = context;
            this.initColor = initColor;
            this.hexValueEnabled = true;
        }

        public Builder setHexValueEnabled(boolean hexValueEnabled) {
            this.hexValueEnabled = hexValueEnabled;
            return this;
        }

        public Builder setOnColorPickedListener(OnColorPickedListener listener) {
            this.listener = listener;
            return this;
        }

        public ColorPickerDialog build() {
            ColorPickerDialog dialog = new ColorPickerDialog(context, new OnColorPickedListener() {
                @Override
                public void onColorPicked(int color) {
                }
            }, initColor);
            dialog.setHexValueEnabled(hexValueEnabled);
            dialog.setOnColorChangedListener(listener);
            return dialog;
        }
    }
}
