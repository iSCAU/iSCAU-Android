package cn.scau.scautreasure.ui;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.PackageHelper;

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