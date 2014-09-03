package cn.scau.scautreasure.ui;

import android.widget.TextView;

import com.umeng.fb.FeedbackAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.PackageHelper;

@EActivity(R.layout.about)
public class About extends CommonActivity {

    @ViewById
    TextView textView_appName;

    @Bean
    PackageHelper packageHelper;

    @AfterViews
    void init() {
        textView_appName.setText(getString(R.string.app_name) + " v" + packageHelper.getAppVersionName());
    }

    @Click
    void btn_advise() {
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.startFeedbackActivity();
    }
}
