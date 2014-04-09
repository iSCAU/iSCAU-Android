package cn.scau.scautreasure.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import org.androidannotations.annotations.EBean;

/**
 * User: special
 * Date: 13-9-1
 * Time: 下午11:38
 * Mail: specialcyci@gmail.com
 */
@EBean
public class MetricsUtil {

    public int getScreenHeight(Activity ctx){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getScreenWidth(Activity ctx){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public Bitmap takeScreenShot(Activity activity){
        View view    = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap cache = view.getDrawingCache();

        // get the status bar's height;
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width  = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap shot = Bitmap.createBitmap(cache, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();

        return shot;
    }


}
