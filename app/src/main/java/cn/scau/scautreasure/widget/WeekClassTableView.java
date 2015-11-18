package cn.scau.scautreasure.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.jar.Attributes;

/**
 * Created by xillkey on 2014/10/5.
 */
public class WeekClassTableView extends View implements View.OnTouchListener {
    private float baseX;
    private float baseY;
    private float lastX;
    private float lastY;
    private float tx;
    private float ty;
    private float curScale = 1;
    private float baseValue = 0;
    private Paint paint;

    public WeekClassTableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnTouchListener(this);
         paint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0, 0, 100, 100);
        paint.setColor(Color.RED);
        canvas.translate(baseX+tx,baseY+ty);
        Log.d("baseX",baseY+"");
        Log.d("baseY",baseY+"");
        canvas.scale(curScale,curScale);
        canvas.drawRect(rectF, paint);
        rectF.set(300,700,400,800);
        canvas.drawRect(rectF,paint);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getPointerCount() == 2) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
                if (baseValue == 0) {
                    baseValue = value;
                } else {
                    if (value - baseValue >= 10 || value - baseValue <= -10) {
                        float scale = (value / baseValue);// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
                        if(scale>1)
                        curScale *= 1.01;
                        else
                        curScale *= 0.99;
                        curScale = curScale > 3 ? 3 : curScale;
                        curScale = curScale < 1 ? 1 : curScale;
                    }
                }
                invalidate();
            }else if(event.getPointerCount() == 1) {

                   tx=event.getX()-lastX;
                   ty=event.getY()-lastY;

                invalidate();
            }

        }else if(event.getAction() == MotionEvent.ACTION_DOWN&&event.getPointerCount() == 1){
            lastX= event.getX();
            lastY= event.getY();
            Log.d("第一下","touch");
        }else if(event.getAction() == MotionEvent.ACTION_UP&&event.getPointerCount() == 1){
            baseX+=tx;
            baseY+=ty;
            tx=0;
            ty=0;
            invalidate();
            Log.d("离开","touch");
        }

        return true;
    }
}
