package cn.scau.scautreasure.ui;

import android.widget.TextView;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.PackageHelper;

@EActivity(R.layout.about)
public class About extends BaseActivity {

    @ViewById
    TextView textView_appName;

    @Bean
    PackageHelper packageHelper;

    @AfterViews
    void init() {
        setTitleText("关于华农宝");
        textView_appName.setText(getString(R.string.app_name) + " v" + packageHelper.getAppVersionName());
    }


}
