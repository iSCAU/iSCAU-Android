package cn.scau.scautreasure.helper;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by macroyep on 15/1/20.
 * Time:19:10
 */
public class AppUIMeasure {
    private static boolean hasMeasured = false;
    static int width = 0;
    static int height = 0;

    public static int[] build(final View view) {

        ViewTreeObserver vto = view.getViewTreeObserver();

        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (hasMeasured == false) {

                    height = view.getMeasuredHeight();
                    width = view.getMeasuredWidth();

                    hasMeasured = true;

                }
                return true;
            }
        });
        Log.i("底部工具栏:", width + "|" + height);
        return new int[]{width, height};
    }

    public static boolean isHasMeasured() {
        return hasMeasured;
    }

    public static void setHasMeasured(boolean hasMeasured) {
        AppUIMeasure.hasMeasured = hasMeasured;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        AppUIMeasure.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        AppUIMeasure.height = height;
    }
}
