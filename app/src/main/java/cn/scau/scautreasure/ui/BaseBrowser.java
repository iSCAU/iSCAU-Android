package cn.scau.scautreasure.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.NetworkHelper;
import cn.scau.scautreasure.widget.AppToast;

/**
 * Created by macroyep on 15/1/20.
 * Time:18:47
 */
@EActivity(R.layout.base_browser_layout)
@OptionsMenu(R.menu.menu_browser)
public class BaseBrowser extends BaseActivity implements ObservableScrollViewCallbacks {


    //浏览器标题
    @Extra("")
    String browser_title;

    @Extra("")
    String url;

    /**
     * 0是不允许缓存,1为允许缓存
     */
    @Extra("0")
    String allCache;


    @ViewById(R.id.webView)
    ObservableWebView webView;


    /**
     * 新开页面打开网址*
     *
     * @param title
     * @param url
     */
    @UiThread
    void toLink(String title, String url) {
        BaseBrowser_.intent(this).browser_title(title).url(url).allCache("0").start();
    }

    /**
     * 从网页拨打电话
     *
     * @param title
     * @param number
     */
    void phoneCall(String title, String number) {
        Log.i(title, number);

    }

    @AfterViews
    void initViews() {

        setTitleText(browser_title);
        setMoreButtonVisible(false);
        webView.setScrollViewCallbacks(this);
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
        });

        if (app.getAndroidSDKVersion() >= 17) {
            webView.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void onClickLink(String title, String url) {
                    System.out.println(title + "|" + url);
                    toLink(title, url);
                }

                @JavascriptInterface
                public void phoneCall(String title, String number) {
                    phoneCall(title, number);
                }

            }, "newview");
        } else {
            webView.addJavascriptInterface(new Object() {

                public void onClickLink(String title, String url) {
                    System.out.println(title + "|" + url);
                    toLink(title, url);
                }

                @JavascriptInterface
                public void phoneCall(String title, String number) {
                    phoneCall(title, number);
                }

            }, "newview");
        }
        webView.setWebViewClient(webViewClient);
        if (allCache == null)
            allCache = "0";
        if (allCache.equals("0")) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else if (allCache.equals("1")) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.setWebViewClient(webClient);

        if (app.net.getCurrentNetType(this) == NetworkHelper.NetworkType.NONE) {
            AppToast.show(this, "无网络连接", 0);
        } else {

            webView.loadUrl(url);
        }

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

    @ViewById(R.id.progressBarLayout)
    LinearLayout progressBarLayout;

    private WebViewClient webClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webView.setVisibility(View.GONE);
            progressBarLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBarLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

        }
    };

    /**
     * 分享
     */
    @OptionsItem(R.id.menu_share)
    void menu_share() {
        if (!getURL().equals(""))
            share(browser_title + " " + getURL());
        else
            AppToast.show(this, "网址为空,操作失败", 0);
    }

    /**
     * 取消操作*
     */
    @OptionsItem(R.id.menu_cancel)
    void menu_cancel() {


    }


    /**
     * 重新加载
     */
    @OptionsItem(R.id.menu_reload)
    void menu_reload() {
        if (app.net.getCurrentNetType(this) == NetworkHelper.NetworkType.NONE) {
            AppToast.show(this, "无网络连接", 0);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.reload();
        }
    }

    /**
     * 复制网址
     */
    @OptionsItem(R.id.menu_copy_url)
    void menu_copy_url() {
        System.out.println("复制网址:" + getURL());
        if (!getURL().equals(""))
            postToClipboard(getURL());
        else
            AppToast.show(this, "网址为空,操作失败", 0);
    }

    String getURL() {
        try {
            return webView.getUrl().replace("iscaucms.sinaapp.com", "app.huanongbao.com");
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 从浏览器打开
     */
    @OptionsItem(R.id.menu_out_open)
    void menu_out_open() {
        if (!getURL().equals("")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getURL()));
            startActivity(intent);
        } else {
            AppToast.show(this, "网址为空,操作失败", 0);
        }

    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
