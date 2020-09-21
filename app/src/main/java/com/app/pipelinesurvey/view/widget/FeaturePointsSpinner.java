package com.app.pipelinesurvey.view.widget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.app.pipelinesurvey.adapter.map.PointSpinnerAdapter;
import com.app.pipelinesurvey.utils.MemoryCache;
import com.app.pipelinesurvey.utils.MemorySpinnerUtils;


import java.io.IOException;
import java.util.ArrayList;

/**
 * 点特征Spiner
 * @author HaiRun
 * @time 2019/7/11.14:33
 */
public class FeaturePointsSpinner extends AppCompatSpinner {

    private static final String KEY_DATA = "key_memory_spinner";

    private Context mContext;
    /**
     * 常用选项
     */
    private ArrayList<String> prepareList = new ArrayList<>();
    /**
     * 正常选项
     */
    private ArrayList<String> normalList = new ArrayList<>();
    private PointSpinnerAdapter memorySpinnerAdapter;
    private int memoryCount = 5;

    public FeaturePointsSpinner(Context context) {
        super(context);
        this.mContext = context;
    }

    public FeaturePointsSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        memorySpinnerAdapter
                = new PointSpinnerAdapter(context, attrs);
    }

    public FeaturePointsSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        memorySpinnerAdapter
                = new PointSpinnerAdapter(context, attrs);
    }

    /**
     * @param prepareList : 预设的memory选项，可控
     * @param normalList： 全部选项，不能为空
     */
    public void setData(ArrayList<String> prepareList, ArrayList<String> normalList) {
        if (prepareList != null) {
            this.prepareList = prepareList;
        }
        this.normalList = normalList;

        initData();
    }

    /**
     * 设置记住的选项数量，默认是5
     *
     * @param count
     */
    public void setMemoryCount(int count) {
        memoryCount = count;
    }

    private void initData() {

        final ArrayList<String> saveMemoryList = new ArrayList<>();
        String saveMemoryString = MemoryCache.get(mContext).getAsString(KEY_DATA);
        if (saveMemoryString != null) {
            try {
                @SuppressWarnings("unchecked")
                ArrayList<String> saveList = (ArrayList<String>)
                        MemorySpinnerUtils.String2SceneList(saveMemoryString);
                saveMemoryList.addAll(saveList);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (saveMemoryList.size() <= 0) {
            for (int i = 0; i < prepareList.size() && i < memoryCount; i++) {
                saveMemoryList.add(prepareList.get(i));
            }
        }
        memorySpinnerAdapter.addData(saveMemoryList);
        memorySpinnerAdapter.addData(normalList);

        this.setAdapter(memorySpinnerAdapter);

        this.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String select = (String) memorySpinnerAdapter.getItem(position);

                saveMemoryList.add(0, select);
                for (int i = 1; i < saveMemoryList.size(); i++) {
                    if (saveMemoryList.get(i).equals(select)) {
                        saveMemoryList.remove(i);
                    }
                }
                ArrayList<String> newMemoryList = saveMemoryList;
                if (saveMemoryList.size() > memoryCount) {
                    newMemoryList = MemorySpinnerUtils.cutString(saveMemoryList, 0, memoryCount);
                }

                memorySpinnerAdapter.setMemoryCount(newMemoryList.size());
                memorySpinnerAdapter.clear();
                memorySpinnerAdapter.addData(newMemoryList);
                memorySpinnerAdapter.addData(normalList);
                memorySpinnerAdapter.notifyDataSetChanged();
                spinner.setSelection(0);
                try {
                    String memoryString = MemorySpinnerUtils.SceneList2String(newMemoryList);
                    MemoryCache.get(mContext).put(KEY_DATA, memoryString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        super.setOnItemSelectedListener(listener);

    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
    }
}
