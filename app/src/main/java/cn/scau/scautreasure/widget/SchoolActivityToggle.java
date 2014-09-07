package cn.scau.scautreasure.widget;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.scau.scautreasure.R;

/**
 * Created by stcdasqy on 2014-08-24.
 * 此类主要目的为了测量webView高度，配合一个copy from SlideExpandableListAdapter
 * 的自定义类，之前的SlideExpandableListAdapter下测量高度采用的是事先measure(0,0)的方法，
 * 但实证表面，webView控件无法通过这样的方式获得实际的高度。
 *
 * 这里采用的办法是将expandable的高度设置为0，在测量的时候置VISIBLE，
 * 才可以让webView获取自己的高度。
 */
public class SchoolActivityToggle extends LinearLayout {
    private OnClickListener expandableOnClickListener = null;
    private OnClickListener extraOnClickListener = null;
    private View mWebView_parent;
    private View cut_line;
    private View fake_toggle_triangle;
    private boolean hasDoneClick = true;
    private boolean firstTimeClick = true;

    public SchoolActivityToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasDoneClick) return;
                hasDoneClick = false;
                if (firstTimeClick) {
                    firstTimeClick = false;
                    //命令WebView测量自己的高度
                    mWebView_parent.setVisibility(VISIBLE);
                    //fake_toggle_triangle.setVisibility(VISIBLE);
                    //mWebView.measure(0,0);
                    //mWebView = null;
                    mWebView_parent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //fake_toggle_triangle.setVisibility(GONE);
                            LinearLayout.LayoutParams lp = (LayoutParams) mWebView_parent.getLayoutParams();
                            lp.height = LayoutParams.WRAP_CONTENT;
                            mWebView_parent.setLayoutParams(lp);
                            mWebView_parent.setVisibility(GONE);
                            SchoolActivityToggle.this.onClick();
                        }
                    }, 150);
                    mWebView_parent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hasDoneClick = true;
                        }
                    }, 330 + 150);
                } else {
                    SchoolActivityToggle.this.onClick();
                    mWebView_parent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hasDoneClick = true;
                        }
                    }, 330);
                }

            }
        });
    }

    public void controlWebView(View mWebView_parent, View fake_toggle_triangle) {
        this.mWebView_parent = mWebView_parent;
        this.fake_toggle_triangle = fake_toggle_triangle;
        cut_line = mWebView_parent.findViewById(R.id.cut_line);
    }

    public void setOnClickListener(OnClickListener l) {
        expandableOnClickListener = l;
    }

    public void setExtraOnClickListener(OnClickListener l) {
        extraOnClickListener = l;
    }

    public OnClickListener getExtraOnClickListener(){
        return extraOnClickListener;
    }

    void onClick() {
        if (expandableOnClickListener != null)
            expandableOnClickListener.onClick(this);
    }
}
