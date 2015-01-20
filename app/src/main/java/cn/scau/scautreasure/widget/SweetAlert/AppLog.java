package cn.scau.scautreasure.widget.SweetAlert;

import android.content.Context;
import android.util.Log;

/**
 * 日志打印
 * Created by macroyep on 14/12/29.
 */
public class AppLog {
    /**
     * Info
     *
     * @param context
     * @param s
     */
    public static void i(Context context, String s) {
        Log.i(context.getClass().getName(), s);
    }

    /**
     * Error
     *
     * @param context
     * @param s
     */
    public static void e(Context context, String s) {
        Log.e(context.getClass().getName(), s);

    }
}
