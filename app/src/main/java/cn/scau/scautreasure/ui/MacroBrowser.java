package cn.scau.scautreasure.ui;

import android.support.v4.widget.SwipeRefreshLayout;

import android.widget.LinearLayout;


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
    //父layout
    @ViewById(R.id.container)
    LinearLayout container;

    MacroWebView macroWebView;


    SwipeRefreshLayout swipeRefreshLayout;


    @Extra
    String title = "正在加载...";


    @Extra
    String url = "";

    @Extra
    boolean isAjaxRefresh = true;


    @AfterViews
    void initBrowser() {
        setTitleText(title);
        swipeRefreshLayout = new SwipeRefreshLayout(this);
        macroWebView = new MacroWebView(this);
        macroWebView.setWebViewCallBack(this);
        macroWebView.setAjaxCallBack(this);
        swipeRefreshLayout.addView(macroWebView);
        container.addView(swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        macroWebView.loadUrl(url);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            hideProgress();
            if (isAjaxRefresh)
                macroWebView.loadUrl("javascript:load()");
            else
                macroWebView.loadUrl(url);
        }
    };

    /**
     * 防止...
     */
    @UiThread(delay = 10000)
    void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @UiThread
    @Override
    public void onAjaxStarting() {
        hideProgress();
        swipeRefreshLayout.setRefreshing(true);
    }

    @UiThread
    @Override
    public void onAjaxFinished() {

        swipeRefreshLayout.setRefreshing(false);
    }

    @UiThread
    @Override
    public void onPageStarting() {
        hideProgress();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onPageFinished() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
