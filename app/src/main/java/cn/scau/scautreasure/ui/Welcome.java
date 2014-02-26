package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.PackageHelper;
import org.androidannotations.annotations.*;

/**
 * User: special
 * Date: 13-9-28
 * Time: 下午12:36
 * Mail: specialcyci@gmail.com
 */
@EFragment(R.layout.welcome)
public class Welcome extends Common {

    @Bean
    PackageHelper packageHelper;

    @ViewById
    TextView tv_version;

    @AfterViews
    void init() {
        getSherlockActivity().getSupportActionBar().hide();
        String originText  = tv_version.getText().toString();
        String versionName = packageHelper.getAppVersionName();
        tv_version.setText(originText + " " + versionName);
    }

}