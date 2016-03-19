package cn.scau.scautreasure.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableWebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.web.client.HttpStatusCodeException;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.ClassModel;


/**
 * Created by zzb on 2016-3-19.
 */
@EActivity(R.layout.common_webview_layout)
public class BaseBrowser extends CommonQueryActivity  implements SwipeRefreshLayout.OnRefreshListener  {


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


    @ViewById(R.id.common_webview)
    ObservableWebView webView;

    @ViewById(R.id.common_swipe)
    SwipeRefreshLayout swipe_refresh;

    /**
     * 新开页面打开网址*
     *
     * @param title
     * @param url
     */
    @UiThread
    void toLink(String title, String url) {
        BaseBrowser_.intent(this).title(title).url(url).allowCache("0").start();
    }


    @SuppressLint("JavascriptInterface")
    @AfterViews
    void initViews() {

        //setTitleText(browser_title);

        //取消隐藏
        //webView.setScrollViewCallbacks(this);
        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(ua + ";app/iscau");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webView.getSettings().setGeolocationEnabled(true);
        //设置定位的数据库路径
        webView.getSettings().setGeolocationDatabasePath(dir);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onReceivedTitle(WebView view, String myTitle) {
                super.onReceivedTitle(view, title);
                if (!(myTitle == null || "".equals(myTitle.trim()))) {
                    title=myTitle;
                    setTitle(title);
                }
            }
        });

        if (app.getAndroidSDKVersion() >= 17) {
            webView.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void onClickLink(String title, String url) {
                    System.out.println(title + "|" + url);
                    toLink(title, url);
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

                public void onClickLink(String title, String url) {
                    System.out.println(title + "|" + url);
                    toLink(title, url);
                }

                public String getEduPassword() {
                    return app.getEncodeEduSysPassword();
                }

                public String getLibPassword() {
                    return app.getEncodeLibPassword();

                }

            }, "newview");
        }
        webView.setWebViewClient(webViewClient);
        if (allowCache == null)
            allowCache = "0";
        if (allowCache.equals("0")) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else if (allowCache.equals("1")) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.setWebViewClient(webClient);

        //加载
        webView.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    };

    public WebViewClient getWebClient() {
        return webClient;
    }

    public void setWebClient(WebViewClient webClient) {
        this.webClient = webClient;
    }

//    @ViewById(R.id.progressBarLayout)
//    LinearLayout progressBarLayout;

    private WebViewClient webClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webView.setVisibility(View.GONE);
//            progressBarLayout.setVisibility(View.VISIBLE);
            showRefresh();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            progressBarLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            closeRefresh();

        }
    };


    @UiThread
    void page_reload() {
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.reload();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRefresh() {
        //closeRefresh();
        page_reload();
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        System.out.println("刷新");
        beforeLoadData();
        page_reload();
        afterLoadData();
    }

    @UiThread
    void showRefresh() {
        swipe_refresh.setRefreshing(true);
    }

    @UiThread
    void closeRefresh() {
        swipe_refresh.setRefreshing(false);
    }
}
