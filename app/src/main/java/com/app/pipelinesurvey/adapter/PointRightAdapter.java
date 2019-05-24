package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.utils.SQLUtils;
import com.app.utills.LogUtills;

import java.util.List;

/**
 * Created by Kevin on 2019-03-18.
 */

public class PointRightAdapter extends BaseAdapter {

    private LayoutInflater m_inflater;
    private List<String> m_list;
    private Context context;
    private int position;
    private String table;
    private String type;
    public PointRightAdapter(Context context, List<String> list,String table,String type) {
        this.context = context;
        this.m_list = list;
        this.m_inflater = LayoutInflater.from(context);
        this.table = table;
        this.type = type;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _holder;
        if (convertView == null){
            convertView = m_inflater.inflate(R.layout.layout_point_right_item, null);
            _holder = new ViewHolder(convertView);
            convertView.setTag(_holder);
        }else {
            _holder = ((ViewHolder) convertView.getTag());
        }
        _holder.pointLift.setText(m_list.get(position).toString());
        //拿到的是已配置好的管点附属物
        boolean pointAdjunct = SQLUtils.getPointAdjunct(table, type, m_list.get(position));
        LogUtills.i("A",table+"-----"+type+"-*---"+m_list.get(position) );
        if (pointAdjunct){
            //显示完成
            _holder.okImg.setVisibility(View.VISIBLE);
            _holder.unImg.setVisibility(View.GONE);
        }else {
            //显示未完成
            _holder.unImg.setVisibility(View.VISIBLE);
            _holder.okImg.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        private final TextView pointLift;
        private  TextView okImg;
        private  TextView unImg;
        ViewHolder(View view){
            pointLift = ((TextView) view.findViewById(R.id.pointLift));
            okImg = ((TextView) view.findViewById(R.id.okImg));
            unImg = ((TextView) view.findViewById(R.id.unImg));
        }
    }
    public void setSelectItem(int position) {
        this.position = position;
    }
}
