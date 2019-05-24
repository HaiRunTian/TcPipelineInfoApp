package com.app.pipelinesurvey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.LayersItemBaseAdapter;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.base.MyApplication;
import com.app.pipelinesurvey.config.SpinnerDropdownListManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
//import com.app.pipelinesurvey.view.fragment.MapFragment;
import com.app.utills.LogUtills;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Legend;
import com.supermap.mapping.MapControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LayersActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LayersItemBaseAdapter m_adapter;
    private List<String> m_list;
    private ArrayList<String> selectedItems;
    private int selectedNum;
    private Button btnSelectAll;
    private Button btnDesSelect;
    private Button btnSubmit;
    private TextView tvTitle;
    private ListView m_listView;
    private MapControl m_mapControl;
    private Layers m_layers;
    private List<String> m_layerAllName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layers);
        initWindowSize();
        initView();
        initData();
    }

    private void initData() {
        m_mapControl = WorkSpaceUtils.getInstance().getMapControl();
        m_layers = m_mapControl.getMap().getLayers();
        m_layerAllName = new ArrayList<>();
        Set<String> _setLayerName = new HashSet<>();
        for (int i = 0; i < m_layers.getCount()-1; i++) {


            String _name = m_layers.get(i).getName();
            m_layerAllName.add(_name);
            int _index = _name.indexOf("@");
            String _tempName = _name.substring(2, _index);
            if (_tempName.contains("Line") || _tempName.contains("Point") || _tempName.contains("map") || _tempName.contains("测量收点")) {
                continue;
            } else {
                _setLayerName.add(_tempName);
                LogUtills.i("加入list 图层名字 = ", _tempName);
            }
        }


        m_list = new ArrayList<>();
        selectedItems = new ArrayList<>();

        //遍历
        Iterator iterator = _setLayerName.iterator();
        while (iterator.hasNext()) {
            String it = (String) iterator.next();
            m_list.add((it));

        }


        selectedNum = m_list.size();
        m_adapter = new LayersItemBaseAdapter(m_list, this);
        m_listView.setAdapter(m_adapter);
    }

    private void initView() {
        btnSelectAll = (Button) findViewById(R.id.btnSelectAll);
        btnSelectAll.setOnClickListener(this);
        btnDesSelect = (Button) findViewById(R.id.btnDesSelect);
        btnDesSelect.setOnClickListener(this);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        m_listView = (ListView) findViewById(R.id.lvLayers);
        m_listView.setOnItemClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
    }

    private void initTestDate() {
       /* String fromWhere = getIntent().getStringExtra("from");
        if (fromWhere != null) {
            //管线中权属单位数据填充
            if (fromWhere.equals(DrawPipeLineActivity.class.getSimpleName())) {
                tvTitle.setText("权属单位列表");
                //当管类是电信时
                if (MapActivity.pipeType.equals("电信-D")) {
                    m_list = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.ownershipUnit));
                }
                //当管类是燃气煤气时
                if (MapActivity.pipeType.equals("煤气-M") || MapActivity.pipeType.equals("燃气-R")) {
                    m_list = SpinnerDropdownListManager.getData(getResources().getStringArray(R.array.ownershipUnit_rq_mq));
                }
            }
        } else {
            for (int i = 1; i <= 10; i++) {
                m_list.add("图层" + i);
            }
        }*/
    }

    private void initWindowSize() {
        WindowManager m = this.getWindowManager();
        //为获取屏幕宽、高
        Display d = m.getDefaultDisplay();
        //获取对话框当前的参数值
        WindowManager.LayoutParams p = this.getWindow().getAttributes();
        //高度设置为屏幕的1.0
        p.height = (int) (d.getHeight() * 0.75);
        //宽度设置为屏幕的0.8
        p.width = (int) (d.getWidth() * 0.75);
        //设置本身透明度
        p.alpha = 1.0f;
        //设置黑暗度
        p.dimAmount = 0.0f;
        this.getWindow().setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectAll:
                if (btnSelectAll.getText().toString().equals("全选")) {
                    btnSelectAll.setText("全不选");
                    for (int i = 0; i < m_list.size(); i++) {
                        LayersItemBaseAdapter.getIsSelected().put(i, true);
                        selectedItems.add(m_list.get(i));
                    }
                    selectedNum = selectedItems.size();
                    notifyDataChanged();
                } else {
                    btnSelectAll.setText("全选");
                    selectedItems.clear();
                    for (int i = 0; i < m_list.size(); i++) {
                        LayersItemBaseAdapter.getIsSelected().put(i, false);
                        selectedNum--;
                    }
                    notifyDataChanged();
                }
                break;
            case R.id.btnDesSelect:
                for (int i = 0; i < m_list.size(); i++) {
                    if (LayersItemBaseAdapter.getIsSelected().get(i)) {
                        LayersItemBaseAdapter.getIsSelected().put(i, false);
                        selectedItems.remove(m_list.get(i));
                        selectedNum--;
                    } else {
                        LayersItemBaseAdapter.getIsSelected().put(i, true);
                        selectedItems.add(m_list.get(i));
                        selectedNum++;
                    }
                    notifyDataChanged();
                }
                break;
            //设置图层是否显示
            case R.id.btnSubmit:

                List<String> _layerName = new ArrayList<>();
                for (int i = 0; i < selectedItems.size(); i++) {
                    String _baseName = selectedItems.get(i);

                    //点、线 图层 单值专题图 、标签专题图 层名字
                    String _baseLayerNameL = "L_" + _baseName + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME;
                    String _uniqueLayerNameL = "L_" + _baseName + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#2";
                    String _lableLayerNameL = "L_" + _baseName + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#1";

                    String _baseLayerNameP = "P_" + _baseName + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME;
                    String _uniqueLayerNameP = "P_" + _baseName + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#2";
                    String _lableLayerNameP = "P_" + _baseName + "@" + SuperMapConfig.DEFAULT_WORKSPACE_NAME + "#1";

                    _layerName.add(_baseLayerNameL);
                    _layerName.add(_uniqueLayerNameL);
                    _layerName.add(_lableLayerNameL);
                    _layerName.add(_baseLayerNameP);
                    _layerName.add(_uniqueLayerNameP);
                    _layerName.add(_lableLayerNameP);
                }

                showLayer(_layerName);


                m_mapControl.getMap().refresh();

                finish();
                break;

                default:break;
        }
    }

    /**
     *
     * @Author HaiRun
     * @Time 2019/4/16 . 11:36
     */
    private void showLayer(List<String> layerName) {

        //全部图层设为不可见
        for (String _s : m_layerAllName) {
            if (m_layers.contains(_s)){
                m_mapControl.getMap().getLayers().get(_s).setVisible(false);

            }
        }
        //设置选择的图层可见
        for (String _s : layerName) {
            if (m_layers.contains(_s)){
                m_mapControl.getMap().getLayers().get(_s).setVisible(true);

            }
        }


    }


    private void notifyDataChanged() {
        m_adapter.notifyDataSetChanged();
        ToastUtil.showShort(this, "一共选择了" + selectedNum + "项");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LayersItemBaseAdapter.ViewHolder _holder = (LayersItemBaseAdapter.ViewHolder) view.getTag();
        _holder.m_checkBox.toggle();
        LayersItemBaseAdapter.getIsSelected().put(position, _holder.m_checkBox.isChecked());
        if (_holder.m_checkBox.isChecked()) {
            selectedNum++;
            selectedItems.add(m_list.get(position));
        } else {
            selectedNum--;
            selectedItems.remove(m_list.get(position));
        }
        notifyDataChanged();
    }
}
