package com.app.pipelinesurvey.view.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.UnZipAdapter;
import com.app.pipelinesurvey.bean.FileEntity;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.pipelinesurvey.utils.FileUtils;
import com.app.pipelinesurvey.utils.InitWindowSize;
import com.app.pipelinesurvey.utils.ToastUtil;
import com.app.pipelinesurvey.utils.ZipProgressUtil;
import com.app.utills.LogUtills;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HaiRun
 *  2018/11/23 0023.
 */

public class UnzipFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private final String TAG = "UnzipFragment";
    private View m_rootView;
    private Button m_btnUnzip;
    private Button m_btnReturn;
//    private Button m_btnClose;
    private ListView m_listView;
    private TextView m_tvTitle;
    private List<FileEntity> m_list;
    private UnZipAdapter m_adapter;
    private String m_folderName;
    private int m_itemPosition = -1;
    private boolean m_isUnZip = false;
    private ProgressDialog m_progressDialog;
    private String m_msg;
    @SuppressLint("HandlerLeak")
    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (m_adapter == null) {
                        m_adapter = new UnZipAdapter(getActivity(), m_list);
                        m_listView.setAdapter(m_adapter);
                    } else {
                        m_adapter.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    m_adapter.notifyDataSetChanged();
                    break;
                //数据开始解压
                case 2:
                    m_progressDialog.show();
                    break;
                //数据解压成功
                case 3:
                    m_progressDialog.dismiss();
                    m_list = FileUtils.getInstance().findAllFile(SuperMapConfig.DEFAULT_DATA_PATH, m_list);
                    m_tvTitle.setText(SuperMapConfig.DEFAULT_DATA_PATH);
                    m_folderName = SuperMapConfig.DEFAULT_DATA_PATH;
                    m_btnReturn.setVisibility(View.VISIBLE);
                    m_adapter.notifyDataSetChanged();
                    break;
                //数据解压失败
                case 4:
                    m_progressDialog.setMessage("数据解压失败");
                    m_progressDialog.dismiss();
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        m_rootView = inflater.inflate(R.layout.fragment_unzip, container, false);
        initView();
        initData();
        return m_rootView;
    }

    private void initData() {
        m_list = new ArrayList<>();
//        SuperMapConfig.SDCARD = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
        m_list = FileUtils.getInstance().findAllFile(SuperMapConfig.SDCARD, m_list);
        //根目录
        m_folderName = SuperMapConfig.SDCARD;
        LogUtills.i(TAG, "" + m_list.size());
        m_adapter = new UnZipAdapter(getActivity(), m_list);
        m_listView.setAdapter(m_adapter);
        m_tvTitle.setText(SuperMapConfig.SDCARD);
        m_listView.setOnItemClickListener(this);
        m_listView.setOnItemLongClickListener(this);
    }

    private void initView() {
        m_btnUnzip = m_rootView.findViewById(R.id.btnUnZip);
        m_btnReturn = m_rootView.findViewById(R.id.btnReturn2);
        TextView tvReturn = m_rootView.findViewById(R.id.tvReturn);
        m_listView = m_rootView.findViewById(R.id.listView);
        m_tvTitle = m_rootView.findViewById(R.id.tvTitle2);

        TextView  tvTitle = m_rootView.findViewById(R.id.tvTitle);
        tvTitle.setText("解压切片");
        TextView tvColse = m_rootView.findViewById(R.id.tvConfig);
        tvColse.setText("关闭");

        m_btnUnzip.setOnClickListener(this);
        m_btnReturn.setOnClickListener(this);
        m_tvTitle.setOnClickListener(this);
        tvColse.setOnClickListener(this);
        tvReturn.setOnClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        InitWindowSize.ins().initWindowSize(getActivity(),getDialog());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_progressDialog = new ProgressDialog(getActivity());
        m_progressDialog.setCancelable(false);
        m_progressDialog.setMessage("数据正在解压中...");
        m_progressDialog.setCanceledOnTouchOutside(false);
        m_progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        m_progressDialog.setMax(100);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReturn2:
            case R.id.tvTitle:
                if (!m_folderName.equals(SuperMapConfig.SDCARD)) {
                    m_list = FileUtils.getInstance().findAllFile(m_folderName.substring(0, m_folderName.lastIndexOf("/")), m_list);
                    LogUtills.i(TAG, m_folderName.substring(0, m_folderName.lastIndexOf("/")));
                    m_folderName = m_folderName.substring(0, m_folderName.lastIndexOf("/"));
                    m_tvTitle.setText(m_folderName);
                    m_btnReturn.setVisibility(View.VISIBLE);
                    m_handler.sendEmptyMessage(0);
                } else {
                    ToastUtil.show(getActivity(), "已经是根目录了，不能再返回", 1);
                    m_btnReturn.setVisibility(View.GONE);
                }
                break;

            case R.id.btnUnZip:

                for (int i = 0; i < m_list.size(); i++) {
                    if (m_list.get(i).isCheck()) {
                        ZipProgressUtil.UnZipFile(m_list.get(i).getFilePath(), SuperMapConfig.DEFAULT_DATA_PATH, new ZipProgressUtil.ZipListener() {
                            @Override
                            public void zipStart() {
                                m_handler.sendEmptyMessage(2);
                            }

                            @Override
                            public void zipSuccess() {

                                m_handler.sendEmptyMessage(3);

                            }

                            @Override
                            public void zipProgress(int progress) {
                                m_progressDialog.setProgress(progress);

                            }

                            @Override
                            public void zipFail() {
                                m_handler.sendEmptyMessage(4);
                            }
                        });

                        break;
                    }
                }

                break;
            case R.id.tvReturn:
            case R.id.tvConfig:
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
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
            LogUtills.i(TAG, m_list.size() + "");
            m_tvTitle.setText(m_folderName);
            m_handler.sendEmptyMessage(0);
        } else if (m_folderName.endsWith(".zip") || m_folderName.endsWith(".ZIP")) {

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


    /**
     * 长按删除文件
     *
     * @author HaiRun
     * created at 2018/11/30 0030 下午 3:38
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog _dialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示 ！")
                .setMessage("确定删除此文件吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (m_list.get(position).getFileType() == FileEntity.Type.FILE) {
                            if (FileUtils.getInstance().deleteFile(m_list.get(position).getFilePath())) {
                                ToastUtil.show(getActivity(), "删除成功" + m_list.get(position).getFileName(), 1);
                                m_list.remove(position);
                                m_handler.sendEmptyMessage(0);
                            } else {
                                ToastUtil.show(getActivity(), "删除失败" + m_list.get(position).getFileName(), 1);
                            }
                        } else {
                            if (FileUtils.getInstance().deleteDir(m_list.get(position).getFilePath())) {
                                ToastUtil.show(getActivity(), "删除成功" + m_list.get(position).getFileName(), 1);
                                m_list.remove(position);
                                m_handler.sendEmptyMessage(0);
                            } else {
                                ToastUtil.show(getActivity(), "删除失败" + m_list.get(position).getFileName(), 1);
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        _dialog.show();


        return false;
    }
}
