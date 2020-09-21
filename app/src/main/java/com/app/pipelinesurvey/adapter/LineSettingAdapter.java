package com.app.pipelinesurvey.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.setting.FatherPoint;
import com.app.pipelinesurvey.config.SettingConfig;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.utills.LogUtills;

import java.util.List;
import java.util.Map;

/**
 * 线配置适配器
 *
 * @author HaiRun
 * @time 2019/9/17.14:50
 */
public class LineSettingAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<FatherPoint> list;
    private LayoutInflater inflater;

    public LineSettingAdapter(Context context, List<FatherPoint> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    // 刷新数据
    public void flashData(List<FatherPoint> datas) {
        this.list = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FatherViewHolder fatherViewHolder = null;
        if (convertView == null) {
            fatherViewHolder = new FatherViewHolder();
            convertView = inflater.inflate(R.layout.item_father, parent, false);
            fatherViewHolder.textView = convertView.findViewById(R.id.tv_father);
            fatherViewHolder.checkBox = convertView.findViewById(R.id.cb_father_setting);
            convertView.setTag(fatherViewHolder);
        } else {
            fatherViewHolder = (FatherViewHolder) convertView.getTag();
        }
        fatherViewHolder.textView.setText(list.get(groupPosition).getPipeType());
        fatherViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < list.get(groupPosition).getList().size(); i++) {
                        list.get(groupPosition).getList().get(i).setShow(1);
                    }
                } else {
                    for (int i = 0; i < list.get(groupPosition).getList().size(); i++) {
                        list.get(groupPosition).getList().get(i).setShow(0);
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class FatherViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.item_child, parent, false);
            childViewHolder.textView = convertView.findViewById(R.id.text);
            childViewHolder.checkBox = convertView.findViewById(R.id.cb_setting);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.textView.setText(list.get(groupPosition).getList().get(childPosition).getName());
        childViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues contentValues = new ContentValues();
                if (isChecked) {
                    contentValues.put(list.get(groupPosition).getList().get(childPosition).getSqlName(), 1);
                    list.get(groupPosition).getList().get(childPosition).setShow(1);
                } else {
                    contentValues.put(list.get(groupPosition).getList().get(childPosition).getSqlName(), 0);
                    list.get(groupPosition).getList().get(childPosition).setShow(0);
                }
                notifyDataSetChanged();
                DatabaseHelpler.getInstance().update(SQLConfig.TABLE_NAME_LINE_SETTING, contentValues, "prj_name = ? and pipetype = ?",
                        new String[]{SuperMapConfig.PROJECT_NAME, list.get(groupPosition).getPipeType()});
            }
        });

        childViewHolder.checkBox.setChecked(list.get(groupPosition).getList().get(childPosition).getShow() == 1 ? true : false);
        return convertView;
    }

    class ChildViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
