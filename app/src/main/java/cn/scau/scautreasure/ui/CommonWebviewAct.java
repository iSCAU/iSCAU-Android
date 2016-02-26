package cn.scau.scautreasure.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.scau.scautreasure.R;

@EActivity(R.layout.common_webview_layout)
public class CommonWebviewAct extends CommonQueryActivity implements SwipeRefreshLayout.OnRefreshListener {


    private WebView webView;
    private boolean isSuccess = true;
    private boolean isFromRefresh = false;

    //浏览器标题
    @Extra("")
    String title = "";

    @Extra("")
    String url = "";

    /**
     * 0是不允许缓存,1为允许缓存
     */
    @Extra("0")
    String allowCache;

    private static final String APP_CACAHE_DIRNAME = "/webcache";


    @ViewById(R.id.common_swipe)
    SwipeRefreshLayout swipe_refresh;


    @AfterViews
    void init() {
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle("关闭");
        title = getIntent().getExtras().getString("title");
        url = getIntent().getExtras().getString("url");
        allowCache = getIntent().getExtras().getString("allowCache");
        swipe_refresh.setColorScheme(R.color.swipe_refresh_1,
                R.color.swipe_refresh_2,
                R.color.swipe_refresh_3,
                R.color.swipe_refresh_4);
        if (title != null) {
            actionBar.setTitle("关闭：" + title);
        }
        webView = (WebView) this.findViewById(R.id.common_webview);
        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(ua + ";app/iscau");
        //webView.setVisibility(View.GONE);
        webView.setBackgroundColor(getResources().getColor(R.color.bgcolor));
        swipe_refresh.setOnRefreshListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(cacheDirPath);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        loadWebView();

    }

    @UiThread(delay = 200)
    void showResult() {

        if (isSuccess) {
            webView.setVisibility(View.VISIBLE);
        }
    }

    @UiThread
    void toLink(String title, String url) {
        CommonWebviewAct_.intent(this).title(title).url(url).allowCache(allowCache).start();
    }

    @SuppressLint("JavascriptInterface")
    @UiThread
    void loadWebView() {
        showRefresh();
        if (isFromRefresh) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            isFromRefresh = false;

        } else {
            if (allowCache != null && allowCache.equals("0")) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            } else {
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showRefresh();
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                closeRefresh();
                showResult();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                getActionBar().setTitle("加载失败，请检查网络!");
                closeRefresh();
                isSuccess = false;
                webView.setVisibility(View.INVISIBLE);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });
        if (app.getAndroidSDKVersion() >= 17) {
            webView.addJavascriptInterface(new Object() {

                @JavascriptInterface
                public void onClickLink(String title, String url) {
                    System.out.println(title + "|" + url);
                    toNewWindow(title, url);
                }

                @JavascriptInterface
                public String getStudentId() {
                    return app.userName;
                }

                @JavascriptInterface
                public String getEduPassword() {
                    return app.getEncodeEduSysPassword();
                }

                @JavascriptInterface
                public String getLibPassword() {
                    return app.getEncodeLibPassword();

                }

            }, "newview");
        } else {
            webView.addJavascriptInterface(new Object() {
                public String getStudentId() {
                    return app.userName;

                }

                public String getEduPassword() {
                    return app.getEncodeEduSysPassword();
                }

                public String getLibPassword() {
                    return app.getEncodeLibPassword();

                }

            }, "newview");
        }
        webView.loadUrl(url);
    }


    @UiThread
    void showRefresh() {
        swipe_refresh.setRefreshing(true);
    }

    @UiThread
    void closeRefresh() {
        swipe_refresh.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onRefresh() {
        isFromRefresh = true;
        loadWebView();
    }

    /**
     * 新开页面打开网址*
     *
     * @param title
     * @param url
     */
    @UiThread
    void toNewWindow(String title, String url) {
        CommonWebviewAct_.intent(this).title(title).url(url).allowCache("0").start();
    }

    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
