package cn.scau.scautreasure.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.androidannotations.annotations.Bean;


/**
 * Created by macroyep on 4/23/15.
 */

public class MacroWebViewRefresh extends LinearLayout implements MacroWebView.AjaxCallBack {
    MacroWebView macroWebView;
    PullToRefreshView pullToRefreshView;

    public MacroWebViewRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MacroWebViewRefresh(Context context, String url) {
        this(context);
        pullToRefreshView = new PullToRefreshView(context);
        macroWebView = new MacroWebView(context);
        macroWebView.loadUrl(url);
        pullToRefreshView.addView(macroWebView);
        addView(pullToRefreshView);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                macroWebView.loadUrl("javascript:load()");
                System.out.println("刷新");
            }
        });
    }

    public MacroWebViewRefresh(Context context) {
        super(context);

    }

    public MacroWebViewRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onAjaxStarting() {
        pullToRefreshView.setRefreshing();

    }

    @Override
    public void onAjaxFinished() {
        pullToRefreshView.onRefreshComplete();
    }


}
