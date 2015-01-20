package cn.scau.scautreasure.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


import com.gc.materialdesign.widgets.Dialog;

import cn.scau.scautreasure.R;

/**
 * Created by macroyep on 15/1/19.
 * Time:20:50
 */
public class AppProgress {
    static Dialog dialog;

    public static void show(Context context, String title, String msg, String cancelButton, Callback callback) {

        dialog = new Dialog(context, title, msg);

        dialog.show();
        View view = (View) LayoutInflater.from(context).inflate(R.layout.app_progress_view, null);
        dialog.addCustomView(view);
        dialog.getButtonAccept().setVisibility(View.GONE);
        dialog.getButtonCancel().setText(cancelButton.equals("") ? "取消" : cancelButton);
        dialog.setCloseOutside(false);
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
