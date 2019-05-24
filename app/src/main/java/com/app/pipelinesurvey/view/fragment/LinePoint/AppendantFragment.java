package com.app.pipelinesurvey.view.fragment.LinePoint;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.BasicsAppendantAdapter;
import com.app.pipelinesurvey.adapter.BasicsPointAdapter;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.SQLUtils;
import com.app.pipelinesurvey.view.activity.linepoint.AddBasicsActivity;

import java.util.ArrayList;

/**
 * 附属物
 * Created by linshen on 2019年5月7日14:25:04.
 */

public class AppendantFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView btnAdd;
    private ListView appendantt;
    private ListView point;

    private ArrayList<String> pointList;
    private ArrayList<String> adjunctList;
    private String pointTable = SQLConfig.TABLE_NAME_PIPE_INFO;
    private String appendantTable = SQLConfig.TABLE_NAME_APPENDANT_INFO;
    private BasicsPointAdapter basicsPointAdapter;
    private BasicsAppendantAdapter basicsAppendantAdapter;
    private String pointname;
    private String code;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.fragment_appendant_point, container, false);
            initView();
            initData();
            //设置进来就显示的第一个管类
            if (pointList.size()!=0) {
                pointname = pointList.get(0);
                listSql(pointname);
                adjubct(pointname);
            }
        }
        return view;
    }
    private void initData() {
        pointList= SQLUtils.getAll(pointTable);
        basicsPointAdapter = new BasicsPointAdapter(getActivity(), pointList);
        point.setAdapter(basicsPointAdapter);

        onItemClick();
    }

    private void onItemClick() {
        point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (((ListView)parent).getTag() != null){
                    ((View)((ListView)parent).getTag()).setBackgroundDrawable(null);
                }
                ((ListView)parent).setTag(view);
                view.setBackgroundColor(Color.parseColor("#ededed"));
                //获取到当前管类类型，然后感觉它来拿到它的附属物
                pointname = pointList.get(position);
                listSql(pointname);
                adjubct(pointname);
                basicsPointAdapter.notifyDataSetChanged();
            }
        });
    }
    private void adjubct(String pointname) {
        //右边适配器
        basicsAppendantAdapter = new BasicsAppendantAdapter(getActivity(), adjunctList,appendantTable,pointname);
        appendantt.setAdapter(basicsAppendantAdapter);
        basicsAppendantAdapter.notifyDataSetChanged();
    }
    private void listSql(String pointname) {
        adjunctList.clear();
        //得到点击的管类的附属物和特征点
        Cursor _cursor1 = DatabaseHelpler.getInstance().query(
                SQLConfig.TABLE_NAME_APPENDANT_INFO,
                "where typename = '" + pointname + "'");
        //把集合的数据先清空，获取到新的数据，然后唤醒适配器更新数据
        while (_cursor1.moveToNext()) {
            String  name = _cursor1.getString(_cursor1.getColumnIndex("name"));
            code = _cursor1.getString(_cursor1.getColumnIndex("code"));
            adjunctList.add(name);
        }
    }
    @Override
    public void onResume() {
        listSql(pointname);
        adjubct(pointname);
        basicsAppendantAdapter.notifyDataSetChanged();
        super.onResume();
    }
    private void initView() {
        pointList = new ArrayList<>();
        adjunctList = new ArrayList<>();
        appendantt = ((ListView) view.findViewById(R.id.lv_appendantt));
        point = ((ListView) view.findViewById(R.id.lv_point));
        btnAdd = ((TextView) view.findViewById(R.id.btnAdd));
        btnAdd.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:
                Intent intent = new Intent(getActivity(), AddBasicsActivity.class);
                intent.putExtra("table",appendantTable);
                intent.putExtra("pointname",pointname);
                intent.putExtra("code",code);
                startActivity(intent);
                break;
        }
    }
}
