package cn.scau.scautreasure.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import cn.scau.scautreasure.R;

import java.util.List;

/**
 * User: special
 * Date: 13-10-13
 * Time: 下午7:29
 * Mail: specialcyci@gmail.com
 */
public class SpinnerDialog extends AlertDialog.Builder {

    private List<String> mList;
    private Context mContext;
    private String  mText;
    private int defaultPosition = 0;
    private DialogListener dialogListener;

    public SpinnerDialog(Context context, List<String> list) {
        super(context);
        mContext = context;
        mList = list;
    }

    public void setMessage(String text){
        mText = text;
    }

    public void setDefaultSelectPosition(int index){
        defaultPosition = index;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public AlertDialog.Builder createBuilder() {
        final Spinner mSpinner = new Spinner(mContext);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, mList);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(defaultPosition);
        setTitle(mText);
        setView(mSpinner);
        setNegativeButton(mContext.getString(R.string.btn_cancel), null);
        setPositiveButton(mContext.getString(R.string.btn_retry),
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    int n = mSpinner.getSelectedItemPosition();
                    dialogListener.select(n);
                }

            });
        return this;
    }

    public interface DialogListener {
        public void select(int n);
    }

}