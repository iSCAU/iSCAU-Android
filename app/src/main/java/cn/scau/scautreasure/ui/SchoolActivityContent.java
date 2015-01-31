package cn.scau.scautreasure.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.SchoolActivityListAdapter;
import cn.scau.scautreasure.helper.SchoolActivityHelper;
import cn.scau.scautreasure.model.SchoolActivityModel;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.SchoolActivityContentWebView;

@EActivity(R.layout.schoolactivity_content)
public class SchoolActivityContent extends BaseActivity {

    @ViewById
    TextView title;

    @ViewById
    SchoolActivityContentWebView content;

    @ViewById
    ScrollView scrollView;

    @Extra
    SchoolActivityModel model;


    @AfterViews
    void init() {
        setTitleText("活动内容");
        setMoreButtonText("分享");

        title.setText(model.getTitle());
        //time.setText(SchoolActivityHelper.getTimeText(model.getTime()));
        content.setContent(model.getContent());
    }

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
        AppToast.show(this, "你又乱点了,草泥马哦", 0);

    }
}
