package cn.scau.scautreasure.widget;

import android.app.AlertDialog;

import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import cn.scau.scautreasure.R;

/**
 * Created by zzb on 2015-11-21.
 */
public class CheckcodeDialog extends AlertDialog {
    private Context mContext;
    private AlertDialog alertDialog;
    private Button btn;
    private View view;
    private int second=5;
    private boolean flag=true;

    public CheckcodeDialog(Context context) {
        super(context);
        this.mContext=context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkcode_dialog);

    }


}
