package cn.scau.scautreasure.ui;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.widget.AppToast;

/**
 * Created by macroyep on 15/1/20.
 * Time:18:47
 */
@EActivity(R.layout.base_browser_layout)
public class BaseBrowser extends BaseActivity {

    //浏览器标题
    @Extra("")
    String browser_title;

    @Extra("")
    String url;


    @ViewById(R.id.webView)
    WebView webView;


    @AfterViews
    void initViews() {
        setTitleText(browser_title);
        setMoreButtonText("更多");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
        AppToast.show(BaseBrowser.this, "你手真多,不要乱点,还没做呢.这里就是刷新啊,分享啊,从safari打开啦之类的", 0);
    }
}
