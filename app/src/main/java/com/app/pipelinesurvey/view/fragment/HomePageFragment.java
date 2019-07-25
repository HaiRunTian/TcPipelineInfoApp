package com.app.pipelinesurvey.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.adapter.ViewPagerAdapter;

import com.app.pipelinesurvey.utils.GlideImageLoader;
import com.app.pipelinesurvey.view.activity.AppInfoActivity;
import com.app.pipelinesurvey.view.activity.HelpActivity;
import com.app.pipelinesurvey.view.activity.linepoint.BasicsActivity;
import com.app.pipelinesurvey.view.activity.linepoint.SymbolicActivity;
import com.app.pipelinesurvey.view.activity.ProjectListActivity;

import com.app.pipelinesurvey.view.activity.WebViewActivity;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author HaiRun
 */

public class HomePageFragment extends Fragment implements View.OnClickListener, OnBannerClickListener {
    /**
     * 滑动页面控件
     */
    private ViewPager m_viewPager;
    /**
     * 装图片容器
     */
    private List<Integer> m_views;

    /**
     * 图片id数组
     */
    private int[] picsId = {R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5};

    private String list[] = {"公司简介", "荣誉资质", "组织架构", "商业模式", "天驰寄语", "公司业绩", "企业文化"};
    /**
     * //滑动页面控件适配器
     */
    private ViewPagerAdapter m_adapter;
    /**
     * 动态radiobutton
     */
    private RadioButton radioButton;
    /**
     * //radiobutton组
     */
    private RadioGroup radioGroup;

    private Button btnPrjList, btnSetting, btnAbout, btnNewSymbolic, btnBasics,btnHelp;
    /**
     * 返回的结果码
     */
    private final static int REQUESTCODE = 1;
    private ViewFlipper viewFlipper;
    private Banner banner;
    private TextView m_tvContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        viewFlipper = view.findViewById(R.id.viewFlipper);
        banner = ((Banner) view.findViewById(R.id.banner));

        btnHelp = ((Button) view.findViewById(R.id.btnHelp));
        btnHelp.setOnClickListener(this);
        btnBasics = view.findViewById(R.id.btnBasics);
        btnBasics.setOnClickListener(this);
        //符号
        btnNewSymbolic = view.findViewById(R.id.btnNewSymbolic);
        btnNewSymbolic.setOnClickListener(this);
        //工程列表
        btnPrjList = view.findViewById(R.id.btnPrjList);
        btnPrjList.setOnClickListener(this);
        //设定
        btnSetting = view.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);
        //关于
        btnAbout = view.findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        startBanner();
    }

    private void startBanner() {
        m_views = new ArrayList<>();
        //title
        List<String> bannerTitle = new ArrayList<>();
        bannerTitle.add("肖顺董事长应邀出席联合国教科文组织智慧城市论坛");
        bannerTitle.add("热烈祝贺城市黑臭水体整治专题讲座成功举办");
        bannerTitle.add("天驰2018年终总结大会胜利召开");
        bannerTitle.add("热烈欢迎广州水务协会领导莅临天驰交流指导");
        bannerTitle.add("天驰总经理亲赴番禺检查工作");

        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        for (int i = 0; i < picsId.length; i++) {
            m_views.add(picsId[i]);
        }
        banner.setImages(m_views);
        //设置点击事件
        banner.setOnBannerClickListener(this);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(bannerTitle);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(4000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }


    private void initData() {
        //小广告条
        for (int i = 0; i < list.length; i++) {
            final View lContent = View.inflate(getContext(), R.layout.item_flipper, null);
            m_tvContent = (TextView) lContent.findViewById(R.id.tv_content);
            m_tvContent.setText(list[i].toString());
            viewFlipper.addView(lContent);
        }

        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View _currentView = viewFlipper.getCurrentView();
                String url = "";
                switch (viewFlipper.getDisplayedChild()) {
                    case 0:
                        url = "http://www.020tc.cn/about-4.html";
                        break;
                    case 1:
                        url = "http://www.020tc.cn/hcategory.html";
                        break;
                    case 2:
                        url = "http://www.020tc.cn/about-5.html";
                        break;
                    case 3:
                        url = "http://www.020tc.cn/about-39.html";
                        break;
                    case 4:
                        url = "http://www.020tc.cn/about-38.html";
                        break;
                    case 5:
                        url = "http://www.020tc.cn/achievements.html";
                        break;
                    case 6:
                        url = "http://www.020tc.cn/culture.html";
                        break;
                    default:
                        url = "http://www.020tc.cn/indexCn.html";
                        break;
                }

                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
//                TextView _viewById = _currentView.findViewById(R.id.tv_content);
//                Toast.makeText(getActivity(), _viewById.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent _intent;
        switch (v.getId()) {
            case R.id.btnBasics:
//
                startActivity(new Intent(getActivity(), BasicsActivity.class));
                break;
            case R.id.btnPrjList:
                _intent = new Intent(getActivity(), ProjectListActivity.class);
                getActivity().startActivityForResult(_intent, REQUESTCODE);
                break;
            case R.id.btnSetting:
//
                UnzipFragment _fragment = new UnzipFragment();
                _fragment.show(getActivity().getSupportFragmentManager().beginTransaction(), "unzip");
                break;
            case R.id.btnAbout:
                startActivity(new Intent(getActivity(), AppInfoActivity.class));
                break;
            case R.id.btnNewSymbolic:
                startActivity(new Intent(getActivity(), SymbolicActivity.class));
                break;
            case R.id.btnHelp:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            if (requestCode == REQUESTCODE) {
//                LogUtills.i("onActivityResult","在fragment中");
            }
        }
    }


    @Override
    public void OnBannerClick(int position) {
        String url = null;
        switch (position) {
            case 1:
                url = "https://mp.weixin.qq.com/s/IL5GUlOYu0N87arsy3FqCA?tdsourcetag=s_pctim_aiomsg";
                break;
            case 2:
                url = "https://mp.weixin.qq.com/s/Qjmz4StrcYTRWYvKRp-m-w";
                break;
            case 3:
                url = "https://mp.weixin.qq.com/s/uct-7CtBJdARisE9joZrwQ";
                break;
            case 4:
                url = "https://mp.weixin.qq.com/s/jf6fFZf2XyQPuyhHpKcxeg";
                break;
            case 5:
                url = "http://www.020tc.cn/newspage-292-1.html";
                break;
            default:
                break;
        }
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
