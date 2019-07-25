package com.app.pipelinesurvey.adapter.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.utils.MemorySpinnerUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * @author HaiRun
 * @time 2019/7/11.13:45
 */
public class PointSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<String> list;
    private int topCount;
    private int msItemTextColor;
    private float msItemTextSize;
    private int msDropTitleBackgroundColor;
    private String msDropTitleText = "常用";
    private int msDropTitleTextColor;
    private float msDropTitleTextSize;
    private int msDropItemBackgroundColor;
    private String msDropItemText = "全部选项";
    private int msDropItemTextColor;
    private float msDropItemTextSize;

    public PointSpinnerAdapter(Context context, AttributeSet attrs) {
        this.context = context;
        list = new ArrayList<>();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FeaturePointsSpinner);
        msItemTextColor = typedArray.getColor(R.styleable.FeaturePointsSpinner_msItemTextColor, 0xff000000);
        msItemTextSize = MemorySpinnerUtils.px2sp(context, typedArray.getDimension(R.styleable.FeaturePointsSpinner_msItemTextSize, 14));
        msDropTitleBackgroundColor = typedArray.getColor(R.styleable.FeaturePointsSpinner_msDropTitleBackgroundColor, 0xff3F51B5);
        msDropTitleText = typedArray.getString(R.styleable.FeaturePointsSpinner_msDropTitleText);
        msDropTitleTextColor = typedArray.getColor(R.styleable.FeaturePointsSpinner_msDropTitleTextColor, 0xff000000);
        msDropTitleTextSize = MemorySpinnerUtils.px2sp(context, typedArray.getDimension(R.styleable.FeaturePointsSpinner_msDropTitleTextSize, 12));
        msDropItemBackgroundColor = typedArray.getColor(R.styleable.FeaturePointsSpinner_msDropItemBackgroundColor, 0xffffffff);
        msDropItemText = typedArray.getString(R.styleable.FeaturePointsSpinner_msDropItemText);
        msDropItemTextColor = typedArray.getColor(R.styleable.FeaturePointsSpinner_msDropItemTextColor, 0xff000000);
        msDropItemTextSize = MemorySpinnerUtils.px2sp(context, typedArray.getDimension(R.styleable.FeaturePointsSpinner_msDropItemTextSize, 14));
        typedArray.recycle();
    }

    public void clear() {
        list.clear();
    }


    public void addData(ArrayList<String> list) {
        this.list.addAll(list);
    }

    public void setMemoryCount(int i) {
        topCount = i;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder viewHolder = null;

        if (viewHolder == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.point_spinner_item_view,
                    parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.tvSpItem = convertView.findViewById(R.id.tv_sp_item);
            viewHolder.tvSpItem.setTextColor(msItemTextColor);
            viewHolder.tvSpItem.setTextSize(msItemTextSize);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String item = (String) getItem(position);
        viewHolder.tvSpItem.setText(item);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sp_dropdown_view, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.tvSpTitle = convertView.findViewById(R.id.tv_sp_title);
            holder.tvSpTitle.setBackgroundColor(msDropTitleBackgroundColor);
            holder.tvSpTitle.setTextColor(msDropTitleTextColor);
            holder.tvSpTitle.setTextSize(msDropTitleTextSize);

            holder.tvSpItem = convertView.findViewById(R.id.tv_sp_item);
            holder.tvSpItem.setBackgroundColor(msDropItemBackgroundColor);
            holder.tvSpItem.setTextColor(msDropItemTextColor);
            holder.tvSpItem.setTextSize(msDropItemTextSize);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        String item = (String) getItem(position);
        holder.tvSpItem.setText(item);

        if (topCount == 0) {
            switch (position) {
                case 0:
                    //如果没有设置常用选项设置
                    holder.tvSpTitle.setVisibility(View.VISIBLE);
                    holder.tvSpItem.setText(msDropItemText);
                    break;
                default:
                    holder.tvSpTitle.setVisibility(View.GONE);
                    holder.tvSpTitle.setText("");
                    break;
            }
        } else {
            if (position == 0) {
                holder.tvSpTitle.setVisibility(View.VISIBLE);
                holder.tvSpTitle.setText(msDropTitleText);
            } else if (position == topCount) {
                holder.tvSpTitle.setVisibility(View.VISIBLE);
                holder.tvSpTitle.setText(msDropItemText);
            } else {
                holder.tvSpTitle.setVisibility(View.GONE);
                holder.tvSpTitle.setText("");
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvSpItem;
        TextView tvSpTitle;
    }
}
