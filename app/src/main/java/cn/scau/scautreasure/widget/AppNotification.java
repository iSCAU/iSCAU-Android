package cn.scau.scautreasure.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.gc.materialdesign.widgets.Dialog;

import cn.scau.scautreasure.R;

/**
 * 一般的弹窗通知
 * Created by macroyep on 15/1/20.
 * Time:14:48
 */
public class AppNotification {
    static Dialog dialog;

    public static void show(Context context, String title, String msg, String cancelButtonText, final Callback callback) {

        dialog = new Dialog(context, title, msg);

        dialog.show();

        dialog.getButtonAccept().setVisibility(View.GONE);
        dialog.getButtonCancel().setText(cancelButtonText);
        dialog.setCloseOutside(false);

        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.onCancel();

            }
        });
    }

    public static void hide() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface Callback {
        public void onCancel();
    }
}
