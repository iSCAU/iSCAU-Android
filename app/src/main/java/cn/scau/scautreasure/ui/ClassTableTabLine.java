package cn.scau.scautreasure.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ClassTableTabLine extends ImageView {

	private int cell_h,cell_w,cl_h,cl_w;
	private Paint lpaint,red_paint;
	
	public ClassTableTabLine(Context context) {
		super(context);
		init();
	}
	
	public void setWidth(int cw,int clw){
		cell_w = cw;
		cl_w = clw;
		prepare();
	}
	public void setHeight(int h){
		cell_h = h;
		cl_h = h;
		prepare();
	}
	
	void prepare(){
		if(cell_w !=0 && cl_w !=0 && cell_h !=0){
			requestLayout();
			postInvalidate();
		}
	}

	
	void init(){
		lpaint = new Paint();
		lpaint.setStrokeWidth(1);
		lpaint.setAntiAlias(true);
		lpaint.setColor(Color.BLACK);
		red_paint = new Paint();
		red_paint.setStrokeWidth(2);
		red_paint.setAntiAlias(true);
		red_paint.setColor(Color.RED);
	}
	public ClassTableTabLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ClassTableTabLine(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//canvas.drawColor(Color.BLUE);
		//drawLine(canvas);
		super.onDraw(canvas);
		//logout("class table tab",this);
		
		//draw red line
		canvas.drawLine(cl_w,cl_h*(float)0.1,cl_w,cl_h*2,red_paint);
		float start_x = cell_w + cl_w;
		for(int i=0;i<6;i++){
			canvas.drawLine(start_x,(float)0.1*cell_h,start_x,
					(float)0.9*cell_h,lpaint);
			start_x += cell_w;
		}
		
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(cell_w*7, cell_h+1);
	}
	
	public void logout(String tab,View v){
		Log.v(tab,tab + " Height : "+v.getHeight()+" Width :"+v.getWidth());
	}
	
	

}
