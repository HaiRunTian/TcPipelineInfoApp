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
 * 作者: LinShen on 2019-05-08.
 * 邮箱: 18475453284@163.com
 */

public class BasicsPointAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private LayoutInflater m_inflater;
    public BasicsPointAdapter(Context context, List<String>list){
        this.context = context;
        this.list = list;
        this.m_inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView ==null){
            convertView = m_inflater.inflate(R.layout.basics_point, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) convertView.getTag());
        }
        viewHolder.tv.setText(list.get(position));
        return convertView;
    }
    class ViewHolder{
        TextView tv;
        ViewHolder(View v){
            tv = ((TextView) v.findViewById(R.id.tv));

        }
    }
}
