package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.Symbolbean;
import com.app.utills.LogUtills;
import com.bumptech.glide.Glide;
import com.supermap.data.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HaiRun
 * @time 2019/4/17.8:57
 */

public class SymbolAdapter extends RecyclerView.Adapter<SymbolAdapter.ViewHolder> {

    /**
     *  第一步 定义接口
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    /**
     *   第二步， 写一个公共的方法
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    private List<Symbolbean> m_list = new ArrayList<>();
    private Context m_context;
    public SymbolAdapter(List<Symbolbean> list) {
        m_list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (m_context == null) {
            m_context = parent.getContext();
        }
        View _view = LayoutInflater.from(m_context).inflate(R.layout.layout_item_symbol, parent, false);
        return new ViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Symbolbean _symbol = m_list.get(position);
        Glide.with(m_context).load(_symbol.getRsId()).into(holder.image);
        holder.type.setText(_symbol.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return m_list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image;
        TextView type;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            image = (ImageView) itemView.findViewById(R.id.cat_image);
            type = (TextView) itemView.findViewById(R.id.cat_type);
        }
    }
}
