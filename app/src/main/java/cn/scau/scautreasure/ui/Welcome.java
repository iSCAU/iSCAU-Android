package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.PackageHelper;

/**
 * User: special
 * Date: 13-9-28
 * Time: 下午12:36
 * Mail: specialcyci@gmail.com
 */
@EActivity(R.layout.welcome)
public class Welcome extends Activity {

    @App
    AppContext app;

    @Bean
    PackageHelper packageHelper;

    @ViewById
    TextView tv_version;

    @AfterViews
    void init() {
        String originText  = tv_version.getText().toString();
        String versionName = packageHelper.getAppVersionName();
        tv_version.setText(originText + " " + versionName);
        close();
    }

    @UiThread(delay = 2000)
    void close(){
        if(hasSetAccount()){
            Main_.intent(this).start();
        }else{
            Login_.intent(this).start();
        }
        finish();
    }

    private boolean hasSetAccount(){
        return app.userName != null &&
                ( app.eduSysPassword != null || app.libPassword != null || app.cardPassword != null);
    }
}