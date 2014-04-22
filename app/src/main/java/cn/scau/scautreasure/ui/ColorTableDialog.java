package cn.scau.scautreasure.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import cn.scau.scautreasure.R;

public class ColorTableDialog extends AlertDialog {
    private static int[] color = new int[]{Color.argb(0xff,0x99,0x88,0xcc),Color.argb(0xff,0x55,0xaa,0x99), Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN,
            Color.LTGRAY, Color.MAGENTA, Color.RED, Color.argb(0xff,0xff,0xaa,0), Color.YELLOW};


    public ColorTableDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public interface ColorTableCallBack{
        void onColorChoose(int color);
    }

    public static int[] getColor(){
        return color;
    }

    private ColorTableCallBack callBack;
    private Context mContext;
    private int w = 30;
    private LinearLayout colorTable, colorTableLayout;
    private LinearLayout horizontal;
    private LinearLayout.LayoutParams mlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private TableLayout.LayoutParams tlp = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    private TableRow tableRow;

    public void setOnColorChoose(ColorTableCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classcolortable);
        colorTableLayout = (LinearLayout) findViewById(R.id.colortablelayout);
        colorTable = (LinearLayout) findViewById(R.id.colortable);
        colorTableLayout.setLayoutParams(flp);
        //colorTableLayout.setBackgroundColor(Color.WHITE);

        mlp.setMargins(0, 0, w / 2, w / 2);

        horizontal = new LinearLayout(mContext);
        horizontal.setLayoutParams(lp);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);
        tableRow = new TableRow(mContext);
        tableRow.setLayoutParams(tlp);

        int i;
        for (i = 0; i < color.length; i++) {
            Button bn = new Button(mContext);
            bn.setHeight(w);
            bn.setWidth(w);
            bn.setBackgroundColor(color[i]);
            bn.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    Drawable d = v.getBackground();
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        d.setAlpha(50);
                    else
                        d.setAlpha(255);
                    v.setBackgroundDrawable(d);
                    return false;

                }
            });
            bn.setTag(color[i]);
            bn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callBack != null)
                        callBack.onColorChoose((Integer)v.getTag());
                }
            });

            if (i % 4 != 3) horizontal.addView(bn, mlp);
            else horizontal.addView(bn);

            if (i % 4 == 3) {
                colorTable.addView(horizontal);

                horizontal = new LinearLayout(mContext);
                horizontal.setLayoutParams(lp);
                horizontal.setOrientation(LinearLayout.HORIZONTAL);

            }
        }
        if(i % 4 != 0){
            colorTable.addView(horizontal);

            horizontal = new LinearLayout(mContext);
            horizontal.setLayoutParams(lp);
            horizontal.setOrientation(LinearLayout.HORIZONTAL);
        }


    }

}

