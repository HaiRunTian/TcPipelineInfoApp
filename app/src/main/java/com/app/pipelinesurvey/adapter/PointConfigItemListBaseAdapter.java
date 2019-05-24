package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.PipePointConfigInfo;

import java.util.List;

/**
 * Created by Kevin on 2018-06-27.
 */

public class PointConfigItemListBaseAdapter extends BaseAdapter {
    private List<PipePointConfigInfo> m_infoList;
    private LayoutInflater m_inflater;

    public PointConfigItemListBaseAdapter(Context context, List<PipePointConfigInfo> listInfo) {
        m_infoList = listInfo;
        m_inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return m_infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return m_infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _viewHolder;
        if (convertView == null) {
            _viewHolder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.layout_listview_item_point_config, null, false);
            _viewHolder.tvID = convertView.findViewById(R.id.tvID);
            _viewHolder.tvAppendant = convertView.findViewById(R.id.tvAppendant);
            _viewHolder.tvColor = convertView.findViewById(R.id.tvColor);
            convertView.setTag(_viewHolder);
        } else {
            _viewHolder = (ViewHolder) convertView.getTag();
        }
        _viewHolder.tvID.setText(String.valueOf(m_infoList.get(position).getId()));
        _viewHolder.tvAppendant.setText(m_infoList.get(position).getName());
        _viewHolder.tvColor.setText(m_infoList.get(position).getColor());
        return convertView;
    }

    class ViewHolder {
        private TextView tvID;
        private TextView tvAppendant;
        private TextView tvColor;
    }
}
