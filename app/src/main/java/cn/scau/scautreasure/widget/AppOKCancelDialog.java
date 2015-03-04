package cn.scau.scautreasure.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.gc.materialdesign.widgets.Dialog;

/**
 * Created by macroyep on 15/1/21.
 * Time:20:55
 */
public class AppOKCancelDialog {
    static Dialog dialog;

    public static void show(Context context, String title, String msg, String okButton, String cancelButton, final Callback callback) {

        dismiss();
        dialog = new Dialog(context, title, msg);

        dialog.show();

        dialog.getButtonAccept().setText(okButton);
        dialog.getButtonCancel().setText(cancelButton);

        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.onCancel();
            }
        });
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onOk();
                dialog.dismiss();
            }
        });
    }

    public static void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface Callback {
        public void onCancel();

        public void onOk();
    }
}
