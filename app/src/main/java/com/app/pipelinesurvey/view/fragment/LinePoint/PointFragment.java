package com.app.pipelinesurvey.view.fragment.LinePoint;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.PointAdapter;
import com.app.pipelinesurvey.adapter.PointRightAdapter;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.SQLUtils;
import com.app.pipelinesurvey.view.activity.linepoint.PointAllocationActivity;


import java.util.ArrayList;

/**
 * 点
 * Created by linshen on 2019-03-15.
 */

public class PointFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ListView pointListView;
    private ListView adjunctListView;
    private ArrayList<String> pointList;
    private ArrayList<String> adjunctList;
    private PointAdapter pointAdapter;
    private PointRightAdapter adjunctAdapter;
    private String pipename;
    private String pointTable;
    private String standard;
    private TextView tv_save;
    private String name;
    private int status;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.fragment_point, container, false);
            initView();
            //拿到表名字
            pointTable = getActivity().getIntent().getStringExtra("point");
            standard = getActivity().getIntent().getStringExtra("standard");
            //查询状态
            name = getActivity().getIntent().getStringExtra("name");
            status = SQLUtils.getStatus(name);
            //数据查询
            initSQL();
            initData();
        }
        return view;
    }
    //根据状态码来进行表来获取
    private void initSQL() {
        if (status==1) {
            tv_save.setVisibility(View.GONE);
            pointList = SQLUtils.getPoint(pointTable);
        }else {
            pointList= SQLUtils.getAll(SQLConfig.TABLE_NAME_PIPE_INFO);
            tv_save.setVisibility(View.VISIBLE);
        }
    }
    private void initData() {
        //适配器
        pointAdapter = new PointAdapter(getActivity(), pointList,status);
        pointListView.setAdapter(pointAdapter);
        pointAdapter.notifyDataSetChanged();
        //设置进来就显示的第一个管类
        if (pointList.size()!=0) {
            pipename = pointList.get(0);
            listSql(pipename);
            adjubct(pipename);
        }
        //点击进行操作
        onItemClick();
    }
    private void onItemClick() {
        pointListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pointAdapter.setSelectItem(position);
                adjunctAdapter.setSelectItem(position);
                adjunctAdapter.notifyDataSetChanged();
                if (((ListView)parent).getTag() != null){
                    ((View)((ListView)parent).getTag()).setBackgroundDrawable(null);
                }
                ((ListView)parent).setTag(view);
                view.setBackgroundColor(Color.parseColor("#ededed"));
                //获取到当前管类类型，然后感觉它来拿到它的附属物
                pipename = pointList.get(position);
                listSql(pipename);
                adjubct(pipename);
                adjunctAdapter.notifyDataSetChanged();
            }
        });
        //点击进行跳转操作
        adjunctListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adjunctList.get(position);
                Intent intent = new Intent(getActivity(), PointAllocationActivity.class);
                intent.putExtra("pointTable", pointTable);
                intent.putExtra("name", name);
                intent.putExtra("pipename",pipename);
                intent.putExtra("standard",standard);
                startActivity(intent);
            }
        });
    }

    private void adjubct(String name) {
        //右边适配器
        adjunctAdapter = new PointRightAdapter(getActivity(), adjunctList,pointTable,name);
        adjunctListView.setAdapter(adjunctAdapter);
        adjunctAdapter.notifyDataSetChanged();
    }
    private void listSql(String pipename) {
        if (status==1) {
            adjunctList.clear();
            Cursor _cursor1 = DatabaseHelpler.getInstance().rawQuery("select name from "+ pointTable+" where pipe_type like '%"+pipename+"%'", null);
            if (_cursor1!=null) {
                while (_cursor1.moveToNext()) {
                    String point = _cursor1.getString(_cursor1.getColumnIndex("name"));
                    adjunctList.add(point);
                }
            }
        } else {
            adjunctList.clear();
            //得到点击的管类的附属物和特征点
            Cursor _cursor1 = DatabaseHelpler.getInstance().query(
                    SQLConfig.TABLE_NAME_APPENDANT_INFO,
                    "where typename = '" + pipename + "'");
            //把集合的数据先清空，获取到新的数据，然后唤醒适配器更新数据
            if (_cursor1.getCount() != 0) {
                while (_cursor1.moveToNext()) {
                    String  feature = _cursor1.getString(_cursor1.getColumnIndex("name"));
                    int isShowFearture = _cursor1.getInt(_cursor1.getColumnIndex("IsShowFearture"));
                    if (isShowFearture == 1) {
                        Cursor query = DatabaseHelpler.getInstance().query(
                                SQLConfig.TABLE_NAME_FEATURE_INFO,
                                "where typename = '" + pipename + "'");
                        while (query.moveToNext()) {
                            String name = query.getString(query.getColumnIndex("name"));
                            adjunctList.add(name);
                        }
                    } else {
                        adjunctList.add(feature);
                    }
                }
            } else {
                String typenameNull = "where typename = '' or typename is null";
                String name = "name";
                String showFearture = "IsShowFearture";
                querySQL(typenameNull, name, showFearture);
            }
        }
    }
    //查询特征点
    private void querySQL(String typenameNull,String name,String showFearture) {
        Cursor _cursor = DatabaseHelpler.getInstance().query(
                SQLConfig.TABLE_NAME_APPENDANT_INFO,typenameNull);
        while (_cursor.moveToNext()) {
            String type = _cursor.getString(_cursor.getColumnIndex(name));
            int isShowFearture = _cursor.getInt(_cursor.getColumnIndex(showFearture));
            if (isShowFearture==1){
                Cursor query = DatabaseHelpler.getInstance().query(
                        SQLConfig.TABLE_NAME_FEATURE_INFO,
                        typenameNull);
                while (query.moveToNext()) {
                    String m_name = query.getString(query.getColumnIndex(name));
                    adjunctList.add(m_name);
                }
            }else {
                adjunctList.add(type);
            }
        }
    }
    @Override
    public void onResume() {
        pointAdapter.notifyDataSetChanged();
        adjunctAdapter.notifyDataSetChanged();
        super.onResume();
    }
    private void initView() {
        pointList = new ArrayList<>();
        adjunctList = new ArrayList<>();
        tv_save = ((TextView) view.findViewById(R.id.tv_save));
        tv_save.setOnClickListener(this);
        pointListView = ((ListView)view.findViewById(R.id.pointLift));
        adjunctListView = ((ListView)view.findViewById(R.id.pointRight));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_save:
                AlertDialog();
                break;
            default:break;
        }
    }
    private void AlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("确定保存？")//标题
                .setMessage("保存后不能再进行编辑咯........")//内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        if(pointList.size()!=0) {
                            //这是确定保存后改变他的编辑状态，
                            ContentValues _values = new ContentValues();
                            _values.put("status", 1);
                            DatabaseHelpler.getInstance().update(SQLConfig.TABLE_NAME_STANDARD_INFO, _values, "name='" + standard + "'", null);
                            Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }else {
                            Toast.makeText(getActivity(), "如果没有配置请返回.......", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        alertDialog.show();
    }
}
