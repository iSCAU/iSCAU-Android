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
 * 出于某些原因，这里需要安置两个OnClickListener，以及为了测量
 * webView的高度
 */
public class SchoolActivityToggle extends LinearLayout {
    private ArrayList<OnClickListener> mOnClickListener = new ArrayList<OnClickListener>();
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
                            for (int i = 0; i < mOnClickListener.size(); i++) {
                                mOnClickListener.get(i).onClick(SchoolActivityToggle.this);
                            }
                        }
                    }, 150);
                    mWebView_parent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hasDoneClick = true;
                        }
                    }, 330 + 150);
                } else {
                    for (int i = 0; i < mOnClickListener.size(); i++) {
                        mOnClickListener.get(i).onClick(SchoolActivityToggle.this);
                    }
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
        mOnClickListener.add(l);
    }
}
