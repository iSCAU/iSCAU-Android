package cn.scau.scautreasure.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import cn.scau.scautreasure.R;

/**
 * Created by stcdasqy on 14-4-21.
 */
public class WholeClassTableFrameLayout extends FrameLayout{

    public WholeClassTableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        ((ClassTableLinearLayout)findViewById(R.id.ll_out)).setParentWidth(width);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
