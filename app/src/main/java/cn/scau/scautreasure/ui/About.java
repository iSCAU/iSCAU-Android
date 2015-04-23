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
        setMoreButtonText("分享");
        textView_appName.setText(getString(R.string.app_name) + " v" + packageHelper.getAppVersionName());
    }

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
//        share("邀请你使用华农宝,请到华农宝官方网站下载,网址:http://www.huanongbao.com");
        MacroBrowser_.intent(this).title("测试").url("http://www.baidu.com").start();
    }
}
