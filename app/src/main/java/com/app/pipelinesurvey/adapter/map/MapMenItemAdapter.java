package com.app.pipelinesurvey.adapter.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.ItemInfo;

import java.util.List;

/**
 * @author HaiRun
 * @time 2019/7/12.11:25
 */
public class MapMenItemAdapter extends BaseAdapter {

    private List<ItemInfo> list;
    private Context context;

    public MapMenItemAdapter(List<ItemInfo> list, Context context) {
        this.list = list;
        this.context = context;
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
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View itemView = mInflater.inflate(R.layout.circle_menu_item, parent, false);
        initMenuItem(itemView, position);
        return itemView;
    }

    /**
     * 初始化菜单项
     *
     * @param itemView
     * @param position
     */
    private void initMenuItem(View itemView, int position) {
        // 获取数据项
        final ItemInfo item = (ItemInfo) getItem(position);
        ImageView iv = (ImageView) itemView.findViewById(R.id.iv_icon);
        TextView tv = (TextView) itemView.findViewById(R.id.tv_text);
        // 数据绑定
        iv.setImageResource(item.getImg());
        tv.setText(item.getText());
    }
}
