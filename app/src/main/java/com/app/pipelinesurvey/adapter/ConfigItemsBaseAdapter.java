package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pipelinesurvey.R;

import java.util.List;

/**
 * Created by Kevin on 2018-06-25.
 */

public class ConfigItemsBaseAdapter extends BaseAdapter {
    private LayoutInflater m_inflater;
    private List<String> m_list;

    public ConfigItemsBaseAdapter(Context context, List<String> list) {
        m_list = list;
        m_inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return m_list.size();
    }

    @Override
    public Object getItem(int position) {
        return m_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _holder;
        if (convertView == null) {
            _holder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.layout_listview_item, null);
            _holder.m_textView = convertView.findViewById(R.id.tvItemText);
            convertView.setTag(_holder);
        } else {
            _holder = (ViewHolder) convertView.getTag();
        }
        _holder.m_textView.setText(m_list.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView m_textView;
    }
}
