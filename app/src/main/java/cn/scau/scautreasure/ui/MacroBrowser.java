package cn.scau.scautreasure.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.widget.MacroWebView;

/**
 * Created by macroyep on 4/23/15.
 */
@EActivity(R.layout.macro_browser)
//@OptionsMenu(R.menu.menu_browser)
public class MacroBrowser extends BaseActivity implements MacroWebView.WebViewCallBack, MacroWebView.AjaxCallBack {
    @ViewById(R.id.container)
    LinearLayout container;
    MacroWebView webView;

    @ViewById(R.id.progressBar)
    ProgressBarCircularIndeterminate progressBar;

    @Extra
    String title = "正在加载...";


    @Extra
    String url = "";

    @AfterViews
    void initBrowser() {
        setTitleText(title);

        webView = new MacroWebView(this);
        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView.setWebViewCallBack(this);
        webView.setAjaxCallBack(this);
        container.addView(webView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView.loadUrl(url);
    }

    @UiThread(delay = 10000)
    void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAjaxStarting() {

    }

    @Override
    public void onAjaxFinished() {

    }

    @Override
    public void onPageStarting() {
        hideProgress();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished() {
        progressBar.setVisibility(View.GONE);
    }
}
