package com.app.pipelinesurvey.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.view.activity.linepoint.AddBasicsActivity;

import java.util.List;

/**
 * 作者: LinShen on 2019-04-25.
 * 邮箱: 18475453284@163.com
 */

public class BasicsDapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private LayoutInflater m_inflater;
    private String table;

    public BasicsDapter(Context context ,List<String> list,String table){
        this.context = context;
        this.list = list;
        this.m_inflater = LayoutInflater.from(context);
        this.table = table;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView ==null){
            convertView = m_inflater.inflate(R.layout.basics_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) convertView.getTag());
        }
        final String name = list.get(position);
        viewHolder.tv.setText(name);


        viewHolder.tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog _dialog = new AlertDialog.Builder(context)
                        .setTitle("提示 ！")
                        .setMessage("确定删除 " + name + " 此文件吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] whereArgs = {name};
                                DatabaseHelpler.getInstance().delete(table,
                                        "name = ?", whereArgs);
                                list.remove(position);
                                notifyDataSetChanged();
                                ToastyUtil.showSuccessShort(context, "删除成功");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                _dialog.show();
                return true;
            }
        });

        viewHolder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddBasicsActivity.class);
                intent.putExtra("table",table);
                intent.putExtra("name",name);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        private TextView tv;
        ViewHolder(View view){
            tv = ((TextView) view.findViewById(R.id.tv));
        }
    }
}
