package com.app.pipelinesurvey.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.app.pipelinesurvey.R;

import java.util.List;

/**
 * 带hint的spinner适配器
 * Created by Kevin on 2018-05-15.
 */

public class LayersSimpleArraryAdapter extends ArrayAdapter {


    public LayersSimpleArraryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.setDropDownViewResource(R.layout.spinner_item_dropdown_style);
    }

//    @Override
//    public int getCount() {
//        int count = super.getCount();
//        return count > 0 ? count - 1 : count;
//    }
}
