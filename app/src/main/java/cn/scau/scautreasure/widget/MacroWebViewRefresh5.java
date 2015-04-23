package cn.scau.scautreasure.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.scau.scautreasure.R;


/**
 * Created by macroyep on 4/23/15.
 */

public class MacroWebViewRefresh5 extends LinearLayout implements MacroWebView.AjaxCallBack {
    MacroWebView macroWebView;
    SwipeRefreshLayout swipeRefreshLayout;

    public MacroWebViewRefresh5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MacroWebViewRefresh5(Context context, String url) {
        this(context);
        swipeRefreshLayout = new SwipeRefreshLayout(context);
        macroWebView = new MacroWebView(context);
        macroWebView.loadUrl(url);
        swipeRefreshLayout.addView(macroWebView);
        addView(swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000); // 5秒后发送消息，停止刷新
            }


        });
    }

    public MacroWebViewRefresh5(Context context) {
        super(context);

    }

    public MacroWebViewRefresh5(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onAjaxStarting() {
        swipeRefreshLayout.setRefreshing(true);

    }

    @Override
    public void onAjaxFinished() {
        swipeRefreshLayout.setRefreshing(false);

    }


}
