package cn.scau.scautreasure.widget;

/**
 * Created by macroyep on 14/12/27.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;

import cn.scau.scautreasure.R;


public class AppToast {

    /**
     * 从底部显示toast,无事件
     *
     * @param activity
     * @param msg
     */
    public static void show(Activity activity, String msg, int h) {
        SnackBar snackBar = new SnackBar(activity, msg, h);
        snackBar.show();
    }

    public static void showWithIntent(Activity activity, String msg, String buttonText, View.OnClickListener onClickListener, int h) {
        SnackBar snackBar = new SnackBar(activity, msg, buttonText, h, onClickListener);
        snackBar.show();

    }


    private LinearLayout linearLayout;
    private Button button;

    public static void ok(Context context, String msg) {
        LayoutInflater myInflater = LayoutInflater.from(context);
        View view = myInflater.inflate(R.layout.toast_ok, null);

        Button button = (Button) view.findViewById(R.id.button);
        button.setText(msg);

        Toast mytoast = new Toast(context);

        mytoast.setView(view);
        mytoast.setDuration(Toast.LENGTH_SHORT);
        mytoast.show();
    }

    public static void error(Context context, String msg) {
        LayoutInflater myInflater = LayoutInflater.from(context);
        View view = myInflater.inflate(R.layout.toast_error, null);

        Button button = (Button) view.findViewById(R.id.button);
        button.setText(msg);

        Toast mytoast = new Toast(context);

        mytoast.setView(view);
        mytoast.setDuration(Toast.LENGTH_SHORT);
        mytoast.show();
    }

    public static void info(Context context, String msg) {
        LayoutInflater myInflater = LayoutInflater.from(context);
        View view = myInflater.inflate(R.layout.toast_info, null);

        Button button = (Button) view.findViewById(R.id.button);
        button.setText(msg);

        Toast mytoast = new Toast(context);

        mytoast.setView(view);
        mytoast.setDuration(Toast.LENGTH_SHORT);
        mytoast.show();
    }

}