package com.app.pipelinesurvey.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.pipelinesurvey.R;
import com.app.utills.LogUtills;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {


    private TextView tv_return;
    private WebView webView;
    private ProgressBar progressBar;
    private String url;
    private WebSettings settings;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    webView.goBack();
                }
                break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        url = getIntent().getStringExtra("url");
        initView();

    }
    @Override
    public void onResume() {
        super.onResume();
        //进度条
        setwebViewClient();
        initWebView();
        webView.loadUrl(url);
    }

    private void initWebView() {
        webView.loadUrl(url);
        settings = webView.getSettings();
        String string = settings.getUserAgentString();
        // 设置可以访问文件
        settings.setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUserAgentString(string + "; android_app/1.0.0");
        /**
         * 以下两条设置可以使页面适应手机屏幕的分辨率，完整的显示在屏幕上
         设置是否使用WebView推荐使用的窗口
         */
        settings.setUseWideViewPort(true);
        //设置WebView加载页面的模式
        settings.setLoadWithOverviewMode(true);
        //显示缩放按钮
        settings.setBuiltInZoomControls(true);
        //设定支持缩放
        settings.setSupportZoom(true);
        webView.requestFocusFromTouch();
        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    private void initView() {
        tv_return = ((TextView) findViewById(R.id.tv_return));
        tv_return.setOnClickListener(this);
        progressBar = ((ProgressBar) findViewById(R.id.progressBar));
        webView = ((WebView) findViewById(R.id.webView));
        webView.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_return:
                finish();
                break;
                default:break;
        }
    }
    private void setwebViewClient() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //WebView开始加载调用的方法
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                LogUtills.i("Page开始  " + url  + "   " + favicon);
            }
            //WebView加载完成会调用的方法
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                LogUtills.i("Page结束  " +url);
            }
        });
        setWebChromeClient();
    }

    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress ==100){
                    progressBar.setVisibility(View.GONE);
                    //progressBar.setProgress(newProgress);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    //设置加载进度
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            handler.sendEmptyMessage(1);
            return true;
        }
        return false;
    }
}
