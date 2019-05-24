package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.utils.SQLUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2019-03-16.
 */

public class LineAdapter extends BaseAdapter {

    private LayoutInflater m_inflater;
    private List<String> m_list;
    private Context context;
    private int mess;
    private String lineTable;
    public LineAdapter(Context context, List<String> list, String lineTable) {
        this.context = context;
        this.m_list = list;
        this.m_inflater = LayoutInflater.from(context);
        this.mess = mess;
        this.lineTable = lineTable;
    }
    @Override
    public int getCount() {
        if (m_list!=null){
            return m_list.size();
        }
        return 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder _holder;
        if (convertView == null) {
            convertView = m_inflater.inflate(R.layout.layout_line_item, null);
            _holder = new ViewHolder(convertView);
            convertView.setTag(_holder);
        } else {
            _holder = ((ViewHolder) convertView.getTag());
        }

        ArrayList<String> list = SQLUtils.getLine(lineTable);
        if (list.contains(m_list.get(position))) {
            //显示完成
            _holder.okImg.setVisibility(View.VISIBLE);
            _holder.unImg.setVisibility(View.GONE);
        }else {
                //显示未完成
            _holder.unImg.setVisibility(View.VISIBLE);
            _holder.okImg.setVisibility(View.GONE);
        }
        _holder.lineList.setText(m_list.get(position));
        return convertView;
    }
    class ViewHolder{
        private  TextView lineList;
        private  TextView okImg;
        private  TextView unImg;
        ViewHolder(View view){
            lineList = ((TextView) view.findViewById(R.id.lineLift));
            okImg = ((TextView) view.findViewById(R.id.okImg));
            unImg = ((TextView) view.findViewById(R.id.unImg));
        }
    }
}
