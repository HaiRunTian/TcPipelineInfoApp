package com.app.pipelinesurvey.view.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.PointConfig;
import com.app.pipelinesurvey.config.SharedPrefManager;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.database.DatabaseHelpler;
import com.app.pipelinesurvey.database.SQLConfig;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.utills.LogUtills;

/**
 * Created by HaiRun on 2018-08-15 14:40.
 * 设置界面
 */

public class SettingGroupfragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private View m_rootView;
    private EditText m_edtGroup;
    private Button m_btnSubmit;
    private EditText m_edtLength;
    private RadioGroup m_radioGroup;
    private EditText m_edtPipeNum;
    private String m_group;
    private int m_local;
    private int m_numLength;
    private int m_pipeLength;
    private TextView m_tvSampleNum;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        m_rootView = inflater.inflate(R.layout.fragment_setting_group, container, false);
        initView();
        return m_rootView;
    }

    private void initView() {
        m_edtGroup = m_rootView.findViewById(R.id.edtGroup);
        m_edtLength = m_rootView.findViewById(R.id.edtPipeLength); // 管线长度
        m_btnSubmit = m_rootView.findViewById(R.id.btnSubmit2);
        m_tvSampleNum = m_rootView.findViewById(R.id.tvSample);
        m_btnSubmit.setOnClickListener(this);
        m_radioGroup = m_rootView.findViewById(R.id.rgbtn);
        m_radioGroup.setOnCheckedChangeListener(this);
        m_edtPipeNum = m_rootView.findViewById(R.id.edtPipeNum); //流水号位数

        //查询数据库  初始化view
        Cursor _cursor = DatabaseHelpler.getInstance().query(SQLConfig.TABLE_NAME_POINT_NAME_CONFIG);
        while (_cursor.moveToNext()) {
            //组名
            m_group = _cursor.getString(_cursor.getColumnIndex("GroupNum"));
            if (m_group != null) {
                m_edtGroup.setText(m_group);
            }
            //位置
            m_local = _cursor.getInt(_cursor.getColumnIndex("GroupLocal"));
            setRgCheckId(m_local);
            //流水号长度
            m_numLength = _cursor.getInt(_cursor.getColumnIndex("SerialNum"));
            m_edtPipeNum.setText(m_numLength + "");
            //管线长度
            m_pipeLength = _cursor.getInt(_cursor.getColumnIndex("PipeLength"));
            m_edtLength.setText(m_pipeLength + "");
        }

    }

    private void setRgCheckId(int local) {
        switch (local) {
            case 1:
                m_radioGroup.check(R.id.rbtn1);
                break;
            case 2:
                m_radioGroup.check(R.id.rbtn2);
                break;
            case 3:
                m_radioGroup.check(R.id.rbtn3);
                break;
            default:
                m_radioGroup.check(R.id.rbtn1);
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit2: {
                m_numLength = Integer.valueOf(m_edtPipeNum.getText().toString().trim());
                if (m_numLength > 5){
                    ToastUtil.show(getActivity(),"管点位数不可以超过5位",1);
                    return;
                }
                DatabaseHelpler.getInstance().update(SQLConfig.TABLE_NAME_POINT_NAME_CONFIG, getCurrentContentValues(),
                        "id = ?", new String[]{"1"});
//                new SharedPrefManager(getActivity(), SharedPrefManager.PIPT_LENGTH).put("data", Integer.valueOf(m_edtLength.getText().toString()));
                PointConfig.getPointConfig().setGroupName(m_edtGroup.getText().toString());
//                SuperMapConfig.User_Group_Index = m_edtGroup.getText().toString();
                PointConfig.getPointConfig().setPipeLength(Integer.valueOf(m_edtLength.getText().toString()));
                PointConfig.getPointConfig().setLocal(Integer.valueOf(m_local));
               // PointConfig.getPointConfig().setPointNumLenght(Integer.valueOf(m_edtPipeNum.getText().toString()));
                //SuperMapConfig.User_Pipe_Length = Integer.valueOf(m_edtLength.getText().toString());
                ToastUtil.show(getActivity(), "设置管线自定义成功", 1);

                m_tvSampleNum.setVisibility(View.VISIBLE);
                m_group = m_edtGroup.getText().toString();

                //示例
                if (m_local == 1) {
                    switch (m_numLength) {
                        case 1:
                            m_tvSampleNum.setText("示例：" +m_group + "J" + "1" );
                            break;
                        case 2:
                            m_tvSampleNum.setText("示例：" +m_group + "J" + "01" );
                            break;
                        case 3:
                            m_tvSampleNum.setText("示例：" +m_group + "J" + "001" );
                            break;
                        case 4:
                            m_tvSampleNum.setText("示例：" +m_group + "J" + "0001" );
                            break;
                        case 5:
                            m_tvSampleNum.setText("示例：" +m_group + "J" + "00001" );
                            break;
                        default:
                            break;
                    }
                } else if (m_local == 2) {
                    switch (m_numLength) {
                        case 1:
                            m_tvSampleNum.setText("示例：" + "J" +m_group+ "1" );
                            break;
                        case 2:
                            m_tvSampleNum.setText("示例：" + "J" +m_group+ "01" );
                            break;
                        case 3:
                            m_tvSampleNum.setText("示例：" + "J" +m_group+ "001" );
                            break;
                        case 4:
                            m_tvSampleNum.setText("示例：" + "J" +m_group+ "0001" );
                            break;
                        case 5:
                            m_tvSampleNum.setText("示例：" + "J" +m_group+ "00001" );
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (m_numLength) {
                        case 1:
                            m_tvSampleNum.setText("示例：" + "J" + "1" +m_group );
                            break;
                        case 2:
                            m_tvSampleNum.setText("示例：" + "J" + "01" +m_group );
                            break;
                        case 3:
                            m_tvSampleNum.setText("示例：" + "J" + "001" +m_group );
                            break;
                        case 4:
                            m_tvSampleNum.setText("示例：" + "J" + "0001" +m_group );
                            break;
                        case 5:
                            m_tvSampleNum.setText("示例：" + "J" + "00001" +m_group );
                            break;
                        default:
                            break;

                    }
                }

            }
            break;
            default:
                break;
        }

    }

    private ContentValues getCurrentContentValues() {
        ContentValues values = new ContentValues();
        values.put("GroupNum", m_edtGroup.getText().toString().trim());
        values.put("Grouplength", m_edtGroup.getText().toString().trim().length());
        values.put("Grouplocal", m_local);
        values.put("SerialNum", m_edtPipeNum.getText().toString().trim());
        values.put("PipeLength", m_edtLength.getText().toString().trim());
        return values;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            //位置在第一位
            case R.id.rbtn1:
//                LogUtills.i("SettingGroupFragment","1");
                m_local = 1;
                break;
            //位置在第二位
            case R.id.rbtn2:
//                LogUtills.i("SettingGroupFragment","2");
                m_local = 2;
                break;
            //位置在第三位
            case R.id.rbtn3:
//                LogUtills.i("SettingGroupFragment","3");
                m_local = 3;
                break;
            default:
                break;
        }
    }
}
