package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.SplashHelper;

/**
 * User: special
 * Date: 13-9-28
 * Time: 下午12:36
 * Mail: specialcyci@gmail.com
 */
@EActivity(R.layout.splash)
public class Splash extends Activity {

    @App
    AppContext app;

    @ViewById
    ImageView splash;

    @ViewById
    View iSCAU_info;

    @Extra
    String title;

    boolean shouldFitXY = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (metrics.widthPixels > metrics.heightPixels) shouldFitXY = false;
    }

    @AfterViews
    void init() {
        if (title != null) {
            try {
                Log.d("splash",title);
                FileInputStream fis = openFileInput(SplashHelper.getFileName(title));
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                splash.setImageBitmap(bitmap);
                if (shouldFitXY) {
                    ViewGroup.LayoutParams lp = splash.getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    splash.setLayoutParams(lp);
                    splash.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    iSCAU_info.setVisibility(View.VISIBLE);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        close();
    }

    @UiThread(delay = 3000)
    void close() {
        if (hasSetAccount()) {
            Main_.intent(this).start();
        } else {
            Login_.intent(this).runMainActivity(true).start();
        }
        waitToFinish();
    }

    @Background(delay = 1000)
    void waitToFinish() {
        finish();
    }

    private boolean hasSetAccount() {
        return app.userName != null &&
                (app.eduSysPassword != null || app.libPassword != null || app.cardPassword != null);
    }
}