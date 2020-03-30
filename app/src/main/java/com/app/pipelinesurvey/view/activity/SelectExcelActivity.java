package com.app.pipelinesurvey.view.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.SelectBaseMapAdapter;
import com.app.pipelinesurvey.base.BaseActivity;
import com.app.pipelinesurvey.bean.FileEntity;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.ToastyUtil;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by HaiRun on 2018/12/1.
 * excel数据导入
 */
public class SelectExcelActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView m_tvTitle;
    private TextView m_tvConfig;
    private ListView m_listView;
    private TextView m_tvReturn;
    private Button m_btnReturn;
    private List<FileEntity> m_list;
    private String m_folderName;
    private final String TAG = "SelectExcelActivity";
    private SelectBaseMapAdapter m_adapter;
    private int m_itemPosition = -1;

    @SuppressLint("HandlerLeak")
    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (m_adapter == null) {
                        m_adapter = new SelectBaseMapAdapter(SelectExcelActivity.this, m_list);
                        m_listView.setAdapter(m_adapter);
                    } else {
                        m_adapter.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    m_adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_basemap);
//        initWindowSize();
        initView();
        initData();
    }

    private void initWindowSize() {
        WindowManager m = this.getWindowManager();
        //为获取屏幕宽、高
        Display d = m.getDefaultDisplay();
        //获取对话框当前的参数值
        WindowManager.LayoutParams p = this.getWindow().getAttributes();
        //高度设置为屏幕的1.0
        p.height = (int) (d.getHeight() * 0.94);
        //宽度设置为屏幕的0.8
        p.width = (int) (d.getWidth() * 0.90);
        //设置本身透明度
        p.alpha = 1.0f;
        //设置黑暗度
        p.dimAmount = 0.0f;
        this.getWindow().setAttributes(p);
    }

    private void initData() {
        m_list = new ArrayList<>();
        //根目录
        m_folderName = SuperMapConfig.FILE_PATH;
        m_list = FileUtils.getInstance().findAllFile(m_folderName, m_list);
        m_adapter = new SelectBaseMapAdapter(this, m_list);
        m_listView.setAdapter(m_adapter);
        m_tvTitle.setText(m_folderName);
    }

    private void initView() {
        TextView _textView = $(R.id.tvTitle);
        _textView.setText("选择excel数据");
        m_tvTitle = $(R.id.tvTitle2);
        m_tvConfig = $(R.id.tvConfig);
        m_listView = $(R.id.listView);
        m_tvReturn = $(R.id.tvReturn);
        m_btnReturn = $(R.id.btnReturn);
        m_btnReturn.setOnClickListener(this);
        m_tvTitle.setOnClickListener(this);
        m_tvReturn.setOnClickListener(this);
        m_tvConfig.setOnClickListener(this);
        m_listView.setOnItemClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //返回上一个目录
            case R.id.btnReturn:
            case R.id.tvTitle2:
                if (!m_folderName.equals(SuperMapConfig.SDCARD)) {
                    m_list = FileUtils.getInstance().findAllFile(m_folderName.substring(0, m_folderName.lastIndexOf("/")), m_list);
                    m_folderName = m_folderName.substring(0, m_folderName.lastIndexOf("/"));
                    m_tvTitle.setText(m_folderName);
                    m_btnReturn.setVisibility(View.VISIBLE);
                    m_handler.sendEmptyMessage(0);
                } else {
                    ToastyUtil.showInfoShort(this, "已经是根目录了，不能再返回");
                    m_btnReturn.setVisibility(View.GONE);
                }
                break;
            //确定
            case R.id.tvConfig:

                        for (int i = 0; i < m_list.size(); i++) {
                            if (m_list.get(i).isCheck()) {
                                FileEntity _fileEntity = m_list.get(i);
                                String _filePath = _fileEntity.getFilePath();
                                String _fileName = _fileEntity.getFileName();
                                Intent _intent =new Intent();
                                _intent.putExtra("filePath",_filePath);
                                _intent.putExtra("fileName",_fileName);
                                setResult(RESULT_OK,_intent);
                                break;
                            }
                        }
                        finish();
                break;
            //返回关掉Acitity
            case R.id.tvReturn:
                    finish();
                break;
            default:
                break;

        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileEntity _fileEntity = m_list.get(position);
        m_folderName = _fileEntity.getFilePath();
        if (_fileEntity.getFileType() == FileEntity.Type.FLODER) {
            if (!m_folderName.equals(SuperMapConfig.SDCARD)) {
                m_btnReturn.setVisibility(View.VISIBLE);
            }
            m_list = FileUtils.getInstance().findAllFile(m_folderName, m_list);
            m_tvTitle.setText(m_folderName);
            m_handler.sendEmptyMessage(0);
        } else if (m_folderName.endsWith(".xls") || m_folderName.endsWith(".XLS")) {
            //未选择变选中
            if (m_itemPosition == -1) {
                m_list.get(position).setCheck(true);
                m_itemPosition = position;
            } else if (m_itemPosition == position) {
                for (FileEntity file : m_list) {
                    file.setCheck(false);

                }
                m_itemPosition = -1;
            } else if (m_itemPosition != position) {
                for (FileEntity file : m_list) {
                    file.setCheck(false);
                }
                _fileEntity.setCheck(true);
                m_itemPosition = position;
            }
            m_handler.sendEmptyMessage(0);
        }

    }
}
