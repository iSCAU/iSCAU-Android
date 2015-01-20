package cn.scau.scautreasure.helper;

import android.content.Context;


 import cn.scau.scautreasure.widget.SweetAlert.SweetAlertDialog;

/**
 * Created by macroyep on 15/1/18.
 * Time:14:21
 */
public class MsgHelper {
    private static SweetAlertDialog loadingDialog;

    public static void go(Context context, boolean status, String title, String subtitle, final Callback callback) {
        if (status) {
            if (loadingDialog == null) {
                loadingDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                loadingDialog.setTitleText(title);
                loadingDialog.setContentText(subtitle);
                loadingDialog.setCancelText("取消");
                loadingDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //取消进程
                        callback.cancelListener();
                        sweetAlertDialog.dismiss();
                    }
                });
                loadingDialog.show();
            } else {
                loadingDialog.show();
            }
        } else {
            if (loadingDialog != null)
                loadingDialog.dismiss();
        }
    }

    public interface Callback {
        public void cancelListener();
    }
}
