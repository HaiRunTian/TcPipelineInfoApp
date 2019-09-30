package com.app.pipelinesurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.PipeTypeParentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaiRun on 2018/12/22.
 */

public class PipeTypeExListViewAdapter extends BaseExpandableListAdapter {
    private Context m_context;
    private LayoutInflater m_inflater;
    private List<PipeTypeParentInfo> m_infoList = new ArrayList<>();

    public PipeTypeExListViewAdapter(Context context, ArrayList<PipeTypeParentInfo> infoList) {
        this.m_context = context;
        this.m_inflater = LayoutInflater.from(m_context);
        this.m_infoList = infoList;
    }

    // 刷新数据
    public void reflushData(ArrayList<PipeTypeParentInfo> datas) {
        this.m_infoList = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return m_infoList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return m_infoList.get(groupPosition).getChildInfoList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return m_infoList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return m_infoList.get(groupPosition).getChildInfoList().get(childPosition);
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
        ViewhloderParent _viewloderParent ;
        if (convertView == null){
            _viewloderParent = new ViewhloderParent();
            convertView = m_inflater.inflate(R.layout.item_ex_parent_listview,parent,false);
            _viewloderParent.textview = convertView.findViewById(R.id.pipeParentName);
            _viewloderParent.imageview = convertView.findViewById(R.id.imgView);
            convertView.setTag(_viewloderParent);
        }else {
            _viewloderParent = (ViewhloderParent) convertView.getTag();
        }
        if (isExpanded){
            _viewloderParent.imageview.setImageResource(R.mipmap.ic_btn_open2);
        }else _viewloderParent.imageview.setImageResource(R.mipmap.ic_btn_close);
        _viewloderParent.textview.setText(m_infoList.get(groupPosition).getName());
        return convertView;
    }

    class ViewhloderParent{
        TextView textview;
        ImageView imageview;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild _child;
        if (convertView == null){
            _child = new ViewHolderChild();
            convertView = m_inflater.inflate(R.layout.item_ex_child_listview,parent,false);
            _child.textview = convertView.findViewById(R.id.childPipeName);
            _child.cb = convertView.findViewById(R.id.cbChild);
            convertView.setTag(_child);
        }else {
            _child = (ViewHolderChild) convertView.getTag();
        }
        _child.textview.setText(m_infoList.get(groupPosition).getChildInfoList().get(childPosition).getName());
        _child.cb.setChecked(m_infoList.get(groupPosition).getChildInfoList().get(childPosition).isCheck());
        return convertView;
    }

    class  ViewHolderChild{
        TextView textview;
        CheckBox cb;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
