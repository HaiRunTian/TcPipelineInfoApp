package com.app.pipelinesurvey.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.FileEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaiRun on 2018/11/29 0029.
 * 解压界面适配器
 */

public class UnZipAdapter extends BaseAdapter {
     Context m_context;
     LayoutInflater m_inflater;
    //存储文件名称
     List<FileEntity> m_list = null;
     FileEntity m_entity;
    public UnZipAdapter(Context context, List<FileEntity> list) {
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
        final ViewHolder holder;
        if (null == convertView) {
            convertView = m_inflater.inflate(R.layout.listview_item_unzip, null);
            holder = new ViewHolder();
            holder.tv = ((TextView) convertView.findViewById(R.id.tvFileName));
            holder.img = ((ImageView) convertView.findViewById(R.id.img));
            holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        m_entity = m_list.get(position);
            holder.tv.setText(m_entity.getFileName());
        if (m_entity.getFileType() == FileEntity.Type.FLODER){
            holder.img.setImageResource(R.mipmap.ic_folder_64px);
            holder.cb.setVisibility(View.GONE);
        }else {
            if (m_entity.getFileName().endsWith(".zip") || m_entity.getFileName().endsWith(".Zip")){
              holder.img.setImageResource(R.mipmap.ic_file_zip_64px);
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
