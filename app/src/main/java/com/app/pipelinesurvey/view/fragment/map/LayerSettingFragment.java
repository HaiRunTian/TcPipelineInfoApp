package com.app.pipelinesurvey.view.fragment.map;

import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.InitWindowSize;
import com.app.pipelinesurvey.utils.ToastyUtil;
import com.app.pipelinesurvey.utils.WorkSpaceUtils;
import com.supermap.mapping.Layers;

/**
 * @author HaiRun
 * @time 2019/8/21.17:22
 */
public class LayerSettingFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    private CheckBox cbPoint;
    private CheckBox cbLine;
    private CheckBox cbPointNum;
    private CheckBox cbLineNum;
    private CheckBox cbMapFirst;
    private CheckBox cbMapSecond;
    private Button btnSave;
    private CheckBox cbMapPaishui;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_layer_setting_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        cbPoint = view.findViewById(R.id.cb_point);
        cbLine = view.findViewById(R.id.cb_line);
        cbPointNum = view.findViewById(R.id.cb_point_num);
        cbLineNum = view.findViewById(R.id.cb_line_num);
        btnSave = view.findViewById(R.id.btn_config);
        cbMapFirst = view.findViewById(R.id.cb_map_first);
        cbMapSecond = view.findViewById(R.id.cb_map_second);
        btnSave.setOnClickListener(this);
        cbMapPaishui = view.findViewById(R.id.cb_map_paishui);
    }

    @Override
    public void onStart() {
        super.onStart();
        InitWindowSize.ins().initWindowSize(getActivity(), getDialog(), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            initValue();
        } catch (Exception e) {
            e.printStackTrace();
            ToastyUtil.showErrorShort(getActivity(),e.toString());
        }
    }

    /**
     * 初始化图层是否显示
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/8/23  13:39
     */
    private void initValue() {
        Layers layers = WorkSpaceUtils.getInstance().getMapControl().getMap().getLayers();
        //点标签专题图
        String pointThemeLayer = "P_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME + "#1";
        //点符号专题图
        String pointUnqueLayer = "P_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME + "#2";
        //线标签专题图
        String lineThemeLayer = "L_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME + "#1";
        //线单值专题图
        String lineUnqueLayer = "L_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME + "#2";
        //排水检测单值专题图
        String psLineUnqueLayer = "L_" + SuperMapConfig.Layer_PS + "@" + SuperMapConfig.PROJECT_NAME + "#1";

        cbPoint.setChecked(layers.get(pointUnqueLayer).isVisible());
        cbPointNum.setChecked(layers.get(pointThemeLayer).isVisible());
        cbLine.setChecked(layers.get(lineUnqueLayer).isVisible());
        cbLineNum.setChecked(layers.get(lineThemeLayer).isVisible());
        cbMapPaishui.setChecked(layers.get(psLineUnqueLayer).isVisible());
        if (layers.get(layers.getCount() - 1).getCaption().contains("底图") && layers.get(layers.getCount() - 2).getCaption().contains("底图")) {
            cbMapSecond.setVisibility(View.VISIBLE);
            cbMapFirst.setChecked(layers.get(layers.getCount() - 2).isVisible());
            cbMapSecond.setChecked(layers.get(layers.getCount() - 1).isVisible());
        } else {
            cbMapFirst.setChecked(layers.get(layers.getCount() - 1).isVisible());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_config:
                settingLayerVisible();
                break;

            default:
                break;
        }
    }

    /**
     * 根据cb是否选中显示图层
     */
    private void settingLayerVisible() {
        Layers layers = WorkSpaceUtils.getInstance().getMapControl().getMap().getLayers();

        //点图层
//        String pointLayer = "P_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME;
        //点标签专题图
        String pointThemeLayer = "P_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME + "#1";
        //点符号专题图
        String pointUnqueLayer = "P_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME + "#2";
        //线图层
//        String lineLayer = "L_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME;
        //线标签专题图
        String lineThemeLayer = "L_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME + "#1";
        //线单值专题图
        String lineUnqueLayer = "L_" + SuperMapConfig.Layer_Total + "@" + SuperMapConfig.PROJECT_NAME + "#2";
        //排水检测单值专题图
        String psLineUnqueLayer = "L_" + SuperMapConfig.Layer_PS + "@" + SuperMapConfig.PROJECT_NAME + "#1";

        layers.get(pointUnqueLayer).setVisible(cbPoint.isChecked());
        layers.get(lineUnqueLayer).setVisible(cbLine.isChecked());
        layers.get(pointThemeLayer).setVisible(cbPointNum.isChecked());
        layers.get(lineThemeLayer).setVisible(cbLineNum.isChecked());
        layers.get(psLineUnqueLayer).setVisible(cbMapPaishui.isChecked());
        if (layers.get(layers.getCount() - 1).getCaption().contains("底图") && layers.get(layers.getCount() - 2).getCaption().contains("底图")) {
            layers.get(layers.getCount() - 1).setVisible(cbMapFirst.isChecked());
            layers.get(layers.getCount() - 2).setVisible(cbMapSecond.isChecked());
        } else {
            layers.get(layers.getCount() - 1).setVisible(cbMapFirst.isChecked());
        }

        WorkSpaceUtils.getInstance().getMapControl().getMap().refresh();
        dismiss();
    }


}
