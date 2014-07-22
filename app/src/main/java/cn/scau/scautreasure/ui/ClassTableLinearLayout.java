package cn.scau.scautreasure.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import cn.scau.scautreasure.R;

public class ClassTableLinearLayout extends LinearLayout {

    private boolean hasClassInWeekDay = false;
    public interface ClickXYCallBack{
        void onXYChanged(int x,int y);
    }

    public interface WidthCallBack{
        void onSetWidth(int w);
    }

    public void setClickXYCallBack(ClickXYCallBack callback){
        this.clickXYCallBack = callback;
    }
    public void setWidthCallBack(WidthCallBack callback){
        this.widthCallBack = callback;
        //getParent().requestLayout();
    }

    private WidthCallBack widthCallBack;
    private ClickXYCallBack clickXYCallBack;
    private View parentView;
    private int parentWidth;



    public ClassTableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setParentView(View v){
        parentView = v;
        getParent().requestLayout();
    }

    public void setHasClassInWeekDay(boolean hasClass) {
        this.hasClassInWeekDay = hasClass;
        //getParent().requestLayout();
    }

    public void setParentWidth(int w){
        parentWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //ViewGroup parent = (ViewGroup) this.getParent();
        if(parentWidth == 0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        Log.d("com.my","parentWidth"+parentWidth);

        if(widthCallBack != null) widthCallBack.onSetWidth(parentWidth);

        int width;
        if (hasClassInWeekDay) {
            width = parentWidth;
            widthMeasureSpec =
                    MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST);
            //setMeasuredDimension(parentWidth,defaultHeight);
        } else {
            width = (int) (parentWidth * (11 / (1 + 10f / 7 * 5)));
            Log.d("LL mesure", "LL mesure " + width);
            widthMeasureSpec =
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
            //setMeasuredDimension(width, defaultHeight);
        }
        ((ClassTableLine)findViewById(R.id.iv)).setMes_w(width);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if(clickXYCallBack != null)
            clickXYCallBack.onXYChanged((int)ev.getX(), (int)ev.getY());
        return super.onInterceptTouchEvent(ev);
    }

}
