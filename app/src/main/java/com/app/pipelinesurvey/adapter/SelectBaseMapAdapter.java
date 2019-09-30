package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.FileEntity;

import java.util.List;

/**
 * Created by HaiRun on 2018/12/1.
 */

public class SelectBaseMapAdapter extends BaseAdapter {

    Context m_context;
    LayoutInflater m_inflater;
    //存储文件名称
    List<FileEntity> m_list = null;
    FileEntity m_entity;
    public SelectBaseMapAdapter(Context context, List<FileEntity> list) {
        this.m_context=context;
        this.m_list = list;
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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SelectBaseMapAdapter.ViewHolder holder;
        if (null == convertView) {
            convertView = m_inflater.inflate(R.layout.listview_item_unzip, null);
            holder = new SelectBaseMapAdapter.ViewHolder();
            holder.tv = ((TextView) convertView.findViewById(R.id.tvFileName));
            holder.img = ((ImageView) convertView.findViewById(R.id.img));
            holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
            convertView.setTag(holder);
        } else {
            holder = (SelectBaseMapAdapter.ViewHolder) convertView.getTag();
        }

        m_entity = m_list.get(position);
        holder.tv.setText(m_entity.getFileName());
        if (m_entity.getFileType() == FileEntity.Type.FLODER){
            holder.img.setImageResource(R.mipmap.ic_folder_64px);
            holder.cb.setVisibility(View.GONE);
        }else {
            if (m_entity.getFileName().endsWith(".sci") || m_entity.getFileName().endsWith(".SCI") ||m_entity.getFileName().endsWith(".json") ){
                holder.img.setImageResource(R.mipmap.ic_map_base_map_48px);
                holder.cb.setVisibility(View.VISIBLE);
                holder.cb.setChecked(m_entity.isCheck());
            }else if (m_entity.getFileName().endsWith(".xls") || m_entity.getFileName().endsWith(".XLS")  ){
                holder.img.setImageResource(R.mipmap.ic_file_excel);
                holder.cb.setVisibility(View.VISIBLE);
                holder.cb.setChecked(m_entity.isCheck());
            }else {
                holder.img.setImageResource(R.mipmap.ic_file_64px);
                holder.cb.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView img;
        CheckBox cb;
        TextView tv;

    }

}
