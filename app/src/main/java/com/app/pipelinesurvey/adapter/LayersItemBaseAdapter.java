package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.app.pipelinesurvey.R;
import java.util.HashMap;
import java.util.List;

/**
 * 图层项适配器
 * Created by Kevin on 2018-05-31.
 */

public class LayersItemBaseAdapter extends BaseAdapter {
    private List<String> m_list;//文本项列表
    private static HashMap<Integer, Boolean> s_isSelected;//选中情况列表
    private Context m_context;//上下文
    private LayoutInflater m_inflater;

    public LayersItemBaseAdapter(List<String> list, Context context) {
        m_context = context;
        m_list = list;
        m_inflater = LayoutInflater.from(m_context);
        s_isSelected = new HashMap<Integer, Boolean>();
        initData();
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

    /*所有置为未选*/
    private void initData() {
        for (int i = 0; i < m_list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return s_isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        s_isSelected = isSelected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _holder;
        if (convertView == null) {
            _holder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.layout_layer_item, null, false);
            _holder.m_textView = convertView.findViewById(R.id.tvLayersItemText);
            _holder.m_checkBox = convertView.findViewById(R.id.cbLayersItem);
            convertView.setTag(_holder);
        } else {
            _holder = (ViewHolder) convertView.getTag();
        }
        _holder.m_textView.setText(m_list.get(position));
        _holder.m_checkBox.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static class  ViewHolder{
        public TextView m_textView;
        public CheckBox m_checkBox;
    }
}
