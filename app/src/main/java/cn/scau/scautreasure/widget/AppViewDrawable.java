package cn.scau.scautreasure.widget;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by macroyep on 15/1/20.
 * Time:09:46
 */
public class AppViewDrawable {
    /**
     * @param textView
     * @param direction 0-3
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void build(TextView textView, int direction, int left, int top, int right, int bottom) {
        Drawable[] drawables = textView.getCompoundDrawables();
        drawables[direction].setBounds(left, top, right, bottom);
        textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}
