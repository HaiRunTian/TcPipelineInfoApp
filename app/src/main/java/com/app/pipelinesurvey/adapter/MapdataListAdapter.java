package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.pipelinesurvey.R;

/**
 * Created by Kevin on 2018/3/1.
 */

public class MapdataListAdapter extends BaseAdapter {
    private LayoutInflater m_inflater;

    public MapdataListAdapter(Context context) {
        m_inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _holder =  null;
        if (convertView == null) {
            convertView = m_inflater.inflate(R.layout.layout_mapdata_item, null);
            _holder = new ViewHolder();
            _holder.cbMapData = convertView.findViewById(R.id.cbMapdata);
            _holder.tvMapData = convertView.findViewById(R.id.tvMapName);
            convertView.setTag(_holder);
        } else {
            _holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvMapData;
        CheckBox cbMapData;
    }
}
