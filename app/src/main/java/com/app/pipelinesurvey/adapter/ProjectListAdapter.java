package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.view.activity.ProjectListActivity;

import java.util.List;

/**
 * Created by HaiRun on 2018-08-28 17:59.
 */

public class ProjectListAdapter extends BaseAdapter {
    private Context m_context;
    private List<String> m_list;

    public ProjectListAdapter(ProjectListActivity context, List<String> list) {
        m_context = context;
        m_list = list;
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
        ViewHolder _viewHolder = null;
        if (convertView == null) {
            _viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(m_context).inflate(R.layout.layout_prj_list_item, parent, false);
            _viewHolder._textView = convertView.findViewById(R.id.tvPrjName);
            convertView.setTag(_viewHolder);
        } else {
            _viewHolder = (ViewHolder) convertView.getTag();
        }
        _viewHolder._textView.setText(m_list.get(position));

        return convertView;
    }

    private class ViewHolder {
        TextView _textView;
    }
}
