package cn.scau.scautreasure.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.SchoolActivityListAdapter;
import cn.scau.scautreasure.helper.SchoolActivityHelper;
import cn.scau.scautreasure.model.SchoolActivityModel;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.SchoolActivityContentWebView;

@EActivity(R.layout.schoolactivity_content)
@OptionsMenu(R.menu.menu_share)
public class SchoolActivityContent extends BaseActivity implements ObservableScrollViewCallbacks {

    @ViewById
    TextView title;

    @ViewById
    SchoolActivityContentWebView content;

    @ViewById
    ObservableScrollView scrollView;

    @Extra
    SchoolActivityModel model;

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

    @AfterViews
    void init() {
        scrollView.setScrollViewCallbacks(this);
        setTitleText("活动内容");
        setMoreButtonVisible(false);

        title.setText(model.getTitle());
        //time.setText(SchoolActivityHelper.getTimeText(model.getTime()));
        content.setContent(model.getContent());
    }

    String getUrl() {
        String url = "http://a.huanongbao.com/out.php?post=" + Base64.encodeToString(String.valueOf(model.getId()).getBytes(), Base64.DEFAULT).trim()
                + "&us=" + Base64.encodeToString(app.userName.getBytes(), Base64.DEFAULT).trim();
        return url;
    }

    @OptionsItem(R.id.menu_share)
    void share() {
        share(model.getTitle() + " " + getUrl());

    }

    @OptionsItem(R.id.menu_copy_url)
    void copy_url() {
        postToClipboard(getUrl());


    }

    @OptionsItem(R.id.menu_out_open)
    void out_open() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getUrl()));
        startActivity(intent);

    }

}
