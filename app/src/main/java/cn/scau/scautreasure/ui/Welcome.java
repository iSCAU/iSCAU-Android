package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import cn.scau.scautreasure.helper.SplashHelper;
import cn.scau.scautreasure.model.SplashModel;

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

    boolean wantToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashHelper splashHelper = new SplashHelper(getApplicationContext());
        SplashModel splashModel = splashHelper.getSuitableSplash();
        if(splashModel!=null){
            Intent splash = new Intent(this,Splash_.class);
            splash.putExtra("title",splashModel.getTitle());
            startActivity(splash);
            wantToExit = true;
            finish();
        }else{
            splashHelper.loadData();
        }
    }

    @AfterViews
    void init() {
        if(wantToExit) return;
        String originText  = tv_version.getText().toString();
        String versionName = packageHelper.getAppVersionName();
        tv_version.setText(originText + " " + versionName);
        close();
    }

    @UiThread(delay = 2000)
    protected void close(){
        if(hasSetAccount()){
            Main_.intent(this).start();
        }else{
            Login_.intent(this).runMainActivity(true).start();
        }
        finish();
    }

    private boolean hasSetAccount(){
        return app.userName != null &&
                ( app.eduSysPassword != null || app.libPassword != null || app.cardPassword != null);
    }
}