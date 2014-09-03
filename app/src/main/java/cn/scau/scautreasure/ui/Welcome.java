package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.AppConfig;
import cn.scau.scautreasure.AppConfig_;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.PackageHelper;
import cn.scau.scautreasure.helper.SplashHelper;
import cn.scau.scautreasure.model.SplashModel;
import cn.scau.scautreasure.service.FoodShopService_;

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
    @Pref
    cn.scau.scautreasure.AppConfig_ appConfig;
    boolean wantToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //拿外卖
        FoodShopService_.intent(this).start();
        if (isPad()){

            appConfig.isThePad().put(true);
        }

        SplashHelper splashHelper = new SplashHelper(getApplicationContext());
        SplashModel splashModel = splashHelper.getSuitableSplash();
        if (splashModel != null) {
            Intent splash = new Intent(this, Splash_.class);
            splash.putExtra("title", splashModel.getTitle());
            startActivity(splash);
            wantToExit = true;
            finish();
        } else {
            splashHelper.loadData();
        }
    }

    @AfterViews
    void init() {
        if (wantToExit) return;
        String originText = tv_version.getText().toString();
        String versionName = packageHelper.getAppVersionName();
        tv_version.setText(originText + " " + versionName);
        close();
    }

    @UiThread(delay = 2000)
    protected void close() {
        if (hasSetAccount()) {
            Main_.intent(this).start();
        } else {
            Login_.intent(this).runMainActivity(true).start();
        }
        finish();
    }

    private boolean hasSetAccount() {
        return app.userName != null &&
                (app.eduSysPassword != null || app.libPassword != null || app.cardPassword != null);
    }
    /**
     * 判断是否为平板
     *
     * @return
     */
    private boolean isPad() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // 屏幕宽度
        float screenWidth = display.getWidth();
        // 屏幕高度
        float screenHeight = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.0) {
            Log.i("设备类型:","平板--尺寸:"+String.valueOf(screenInches));
            return true;
        }
        Log.i("设备类型:","手机--尺寸:"+String.valueOf(screenInches));
        return false;
    }
}