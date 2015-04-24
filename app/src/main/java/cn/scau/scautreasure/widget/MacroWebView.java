package cn.scau.scautreasure.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.scau.scautreasure.helper.NetworkHelper;
import cn.scau.scautreasure.helper.WebViewCacheHelper;
import cn.scau.scautreasure.ui.MacroBrowser_;

/**
 * Created by macroyep on 4/23/15.
 */
public class MacroWebView extends WebView {
    private NetworkHelper networkHelper;
    private WebViewCacheHelper cacheHelper;
    protected WebSettings webSettings;
    private Context mContext;
    private WebViewCallBack webViewCallBack;
    private AjaxCallBack ajaxCallBack;
    //User-Agent
    private final String USER_AGENT_OPTION = "iscau.3.0+.android";

    //webview页面title
    private String title = "正在加载...";

    public MacroWebView(Context context) {
        super(context);
        this.cacheHelper = new WebViewCacheHelper(context);
        this.networkHelper = new NetworkHelper();
        this.webSettings = this.getSettings();
        this.mContext = context;
        this.webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString(webSettings.getUserAgentString() + ";" + USER_AGENT_OPTION);
        webSettings.setDatabaseEnabled(true);
        String dir = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);

        if (network()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        setWebViewClient(new MacroWebViewClient());

        setWebChromeClient(new MacroWebChromeClient());

        addJavascriptInterface(new JSAPINormal(), "JSAPI");
    }


    public WebViewCallBack getWebViewCallBack() {
        return webViewCallBack;
    }

    public void setWebViewCallBack(WebViewCallBack webViewCallBack) {
        this.webViewCallBack = webViewCallBack;
    }

    public AjaxCallBack getAjaxCallBack() {
        return ajaxCallBack;
    }

    public void setAjaxCallBack(AjaxCallBack ajaxCallBack) {
        this.ajaxCallBack = ajaxCallBack;
    }

    public MacroWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MacroWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    private class MacroWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (webViewCallBack != null)
                webViewCallBack.onPageStarting();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (webViewCallBack != null)
                webViewCallBack.onPageFinished();

        }
    }

    private class MacroWebChromeClient extends WebChromeClient {
        /**
         * 获取网页标题
         *
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }

    /**
     * 页面回调
     */
    public interface WebViewCallBack {
        void onPageStarting();

        void onPageFinished();

    }

    /**
     * ajax回调,一般用于进度条
     */
    public interface AjaxCallBack {
        void onAjaxStarting();

        void onAjaxFinished();
    }

    /**
     * android-js交互接口
     */
    class JSAPINormal {

        /**
         * 从新的窗口打开新的网址
         *
         * @param title
         * @param url
         */
        @JavascriptInterface
        public void newTab(String title, String url) {
            MacroBrowser_.intent(mContext).title(title).url(url).start();
        }

        /**
         * 原生显示toast
         *
         * @param text
         */
        @JavascriptInterface
        public void toast(final String text) {
            new Thread(new Runnable() {
                public void run() {
                    post(new Runnable() {
                        public void run() {
                            AppToast.show((Activity) mContext, text, 0);
                        }
                    });
                }
            }).start();
        }

        /**
         * 打开一个新的activity
         *
         * @param title
         */
        @JavascriptInterface
        public void activityStart(String title, String action) {
            if (webViewCallBack != null)
                ;
//                webViewCallBack.onCustomAction(title, action);
        }

        /**
         * 开启异步
         */
        @JavascriptInterface
        public void ajaxStart() {
            if (ajaxCallBack != null)
                ajaxCallBack.onAjaxStarting();
            else
                Log.e("ajax", "未设置回调");
        }

        @JavascriptInterface
        public void ajaxFinish() {
            if (ajaxCallBack != null)
                ajaxCallBack.onAjaxFinished();
            else
                Log.e("ajax", "未设置回调");
        }


        /**
         * 写json缓存
         *
         * @param cacheKey
         * @param string
         */
        @JavascriptInterface
        public void writeJsonStringCache(String cacheKey, String string) {

            cacheHelper.writeStringToCache(cacheKey, string);
        }

        /**
         * 定时缓存
         *
         * @param cacheKey
         * @param string
         * @param saveTime 单位秒
         */
        @JavascriptInterface
        public void writeJsonStringCacheWithTime(String cacheKey, String string, int saveTime) {
            cacheHelper.writeStringToCache(cacheKey, string, saveTime);
        }

        /**
         * 读取json缓存
         *
         * @param cacheKey
         * @return
         */
        @JavascriptInterface
        public String readJsonStringCache(String cacheKey) {
            return cacheHelper.readStringFromCache(cacheKey);
        }


        /**
         * 当前网络是否可用
         *
         * @return
         */
        @JavascriptInterface
        public boolean isNetworkOk() {
            return network();
        }


    }


    boolean network() {
        NetworkHelper.NetworkType type = networkHelper.getCurrentNetType(mContext);
        if (type == NetworkHelper.NetworkType.NONE) {
            return false;
        }
        return true;
    }
}
