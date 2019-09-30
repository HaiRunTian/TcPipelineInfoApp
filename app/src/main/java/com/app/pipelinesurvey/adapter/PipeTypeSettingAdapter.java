package com.app.pipelinesurvey.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.setting.PipeTypeStting;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;

import java.util.List;

/**
 * 线配置适配器
 * @author HaiRun
 * @time 2019/9/17.14:50
 */
public class PipeTypeSettingAdapter extends BaseAdapter {

    private List<PipeTypeStting> list;
    private Context context;

    public PipeTypeSettingAdapter(Context context, List<PipeTypeStting> list) {
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
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder  = new ViewHolder();
            convertView =  LayoutInflater.from(context).inflate(R.layout.item_setting_check,null);
            viewHolder.textView = convertView.findViewById(R.id.text);
            viewHolder.checkBox = convertView.findViewById(R.id.cb_setting);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(list.get(position).getPipeTyep());
        int i = list.get(position).getShow();

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues contentValues = new ContentValues();
                if (isChecked){
                    contentValues.put("show",1);
                }else {
                    contentValues.put("show",0);
                }
                contentValues.put("code",list.get(position).getCode());
                contentValues.put("prj_name",list.get(position).getPrjName());
                contentValues.put("pipetype",list.get(position).getPipeTyep());
                DatabaseHelpler.getInstance().update(SQLConfig.TABLE_NAME_PIPE_PRJ_SHOW,contentValues,"prj_name = ? and pipetype = ?",new String[]{SuperMapConfig.PROJECT_NAME,list.get(position).getPipeTyep()});
            }
        });

        viewHolder.checkBox.setChecked(i == 1?true:false);
        return convertView;
    }

    class ViewHolder{
        TextView textView;
        CheckBox checkBox;
    }
}
