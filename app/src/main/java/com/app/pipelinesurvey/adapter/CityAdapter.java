package com.app.pipelinesurvey.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.SQLUtils;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.view.activity.linepoint.StandardActivity;


import java.util.List;

/**
 * Created by Kevin on 2019-03-14.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{


    private Context context;
    private List<String> List;
    private LayoutInflater mInflater;
    private  String pointsettingtablesymbol;
    private String linesettintable;
    private  int status;
    private String name;
    private String city;
    public void setContentlist(List<String> List) {
        this.List = List;
    }
    public CityAdapter(Context context, List<String> List) {
        this.context = context;
        this.List = List;
        this.mInflater =  LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.city_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (List.size()!=0){
            city = List.get(position);
            holder.textview.setText(city);
        }
        status = SQLUtils.getStatus(city);
        if (this.status ==1){
            holder.okImg.setVisibility(View.VISIBLE);
            holder.unImg.setVisibility(View.GONE);
        }else {
            holder.unImg.setVisibility(View.VISIBLE);
            holder.okImg.setVisibility(View.GONE);
        }

        holder.tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StandardActivity.class);
                intent.putExtra("name",List.get(position));
                context.startActivity(intent);
            }
        });

        name = holder.textview.getText().toString();
        Cursor query = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_STANDARD_INFO,"where name = '"+name+"'");
        while (query.moveToNext()){
            pointsettingtablesymbol = query.getString(query.getColumnIndex("pointsettingtablesymbol"));
            linesettintable = query.getString(query.getColumnIndex("linesettintable"));
        }
        holder.tv_city.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog _dialog = new AlertDialog.Builder(context)
                        .setTitle("提示 ！")
                        .setMessage("确定删除 "+name+" 此文件吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] whereArgs ={name};
                                DatabaseHelpler.getInstance().delete(SQLConfig.TABLE_NAME_STANDARD_INFO,
                                        "name = ?", whereArgs);
                                DatabaseHelpler.getInstance().execSQL("drop table "+pointsettingtablesymbol);
                                DatabaseHelpler.getInstance().execSQL("drop table "+linesettintable);
                                List.remove(position);
                                notifyDataSetChanged();
                                ToastyUtil.showSuccessShort(context,"删除成功");
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
     }
    @Override
    public int getItemCount() {
        if (List!=null){
            return List.size();
        }
        return 0;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private  RelativeLayout tv_city;
        private  TextView okImg;
        private  TextView unImg;
        private  TextView textview;
        public ViewHolder(View itemView) {
            super(itemView);
            textview  = (TextView) itemView.findViewById(R.id.textview);
            tv_city = ((RelativeLayout) itemView.findViewById(R.id.tv_city));
            okImg = ((TextView) itemView.findViewById(R.id.okImg));
            unImg = ((TextView) itemView.findViewById(R.id.unImg));
        }

    }
}
