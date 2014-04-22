package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class ClassTableLine extends ImageView {

	private Context ctx;
	private boolean hasMeasure = false;
	private boolean hasClassInNoon = true;
	private boolean hasClassInNight = true;
	private int mes_h = 100,mes_w;
	private Paint pline = new Paint();
	private Paint rl_paint = new Paint(); // reg line paint
	private Paint textPaint = new Paint();
	private int cell_h,cell_w,cl_h,cl_w;
    private fitHeightCallBack callback;
    private Bitmap backgroundBitmap;

    public interface fitHeightCallBack{
        void onChanged(int height);
    }

	public ClassTableLine(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		ctx = context;
	}

	public ClassTableLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		ctx = context;
	}

    public void setBackgroundBitmap(Bitmap bm){
        backgroundBitmap = bm;
        //setImageBitmap(bm);
    }

    public void setHasClassInNoonAndNight(boolean hasClassInNoon,boolean hasClassInNight){
        this.hasClassInNight = hasClassInNight;
        this.hasClassInNoon = hasClassInNoon;
    }
	
	public void setWidth(int cw,int clw){
		cell_w = cw;
		cl_w = clw;
		if(checkvalid()) postInvalidate();
	}
	public void setHeight(int h){
		cell_h = h;
		cl_h = h;
		fitCellHeightToParent();
		if(checkvalid()) postInvalidate();
	}
	boolean checkvalid(){
		if(cell_w !=0 && cl_w !=0 && cell_h !=0){
			//postInvalidate();
            return true;
		}else return false;
	}
    void setCallback(fitHeightCallBack callback){
        this.callback = callback;
    }

	void init(){
		pline.setAntiAlias(true);
		pline.setColor(Color.BLUE);
        pline.setAlpha(100);
		pline.setStrokeWidth(1);
		pline.setStyle(Paint.Style.STROKE);
		
		rl_paint.setAntiAlias(true);
		rl_paint.setColor(Color.RED);
		rl_paint.setStrokeWidth(1);
		
		textPaint.setAntiAlias(true);
		//textPaint.setTextSize(20);
		textPaint.setColor(Color.BLACK);
		/*
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("com.my","view.onclick");
			}
		});*/
	}
	
	void fitCellHeightToParent(){
		Activity mActivity = (Activity)ctx;
		int windowH = mActivity.getWindowManager().getDefaultDisplay().getHeight();
		
		int[] xy = new int[2];
		this.getLocationOnScreen(xy);
		int nCell = 13;
		int viewH = windowH - xy[1];
		if(!hasClassInNoon) nCell--;
		if(!hasClassInNight) nCell -= 2;
		
		Log.d("abc","nCell = "+nCell);
		Log.d("abc","viewH = "+viewH);
		if(cell_h < viewH / nCell){
			cell_h = viewH / nCell;
            callback.onChanged(cell_h);
			//((WClassTable)ctx).setHeight(cell_h);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
        Log.d("com.my","EEEE+ondraw,"+cell_h+","+cell_w+","+cl_w+","+cl_h);
        if(!checkvalid()) return;
 		super.onDraw(canvas);
		drawLine(canvas);
		drawText(canvas);
	}
	void drawText(Canvas canvas){
		textPaint.setTextSize(cl_w/2);
		int startX = cl_w/4;
		canvas.drawText("上", startX, cell_h, textPaint);
		canvas.drawText("午", startX, cell_h*3, textPaint);
		int startY = cell_h*4;
		
		if(hasClassInNoon){
			//canvas.drawText("中",startX,cell_h*4.6f,textPaint);
			//canvas.drawText("午",startX,cell_h*5.5f,textPaint);
			startY += cell_h*2;
		}else{
			//canvas.drawText("中",startX,cell_h*4.4f,textPaint);
			//canvas.drawText("午",startX,cell_h*4.8f,textPaint);
			startY += cell_h;
		}
		canvas.drawText("下",startX,startY+cell_h,textPaint);
		canvas.drawText("午",startX,startY+cell_h*3,textPaint);
		startY += cell_h *4;
		if(hasClassInNight){
			canvas.drawText("晚",startX,startY+cell_h,textPaint);
			canvas.drawText("上",startX,startY+cell_h*2,textPaint);
		}else{
			canvas.drawText("晚",startX,startY+cell_h*0.4f,textPaint);
			canvas.drawText("上",startX,startY+cell_h,textPaint);
		}
	}
	
	void drawCross(Canvas canvas,float startX,float startY,
			int x,int y,float rx,float ry,Paint paint){
		int i;
		float sx = startX;
		startX += cell_w;
		for(i=0;i<x;i++){
			canvas.drawLine(startX, startY+ry*cell_h, 
					startX, startY+y*cell_h, paint);
			startX += cell_w;
		}
		startX = sx;
		startY += cell_h;
		for(i=0;i<y;i++){
			canvas.drawLine(startX+rx*cell_w, startY, 
					startX+(x+1-rx)*cell_w,startY, paint);
			startY += cell_h;
		}

		
	}
	
	
	void drawLine(Canvas canvas){
		//draw reg Line
		canvas.drawLine(cl_w, 0, cl_w, 13*cell_h, rl_paint);
		int startX = cl_w;
		int startY = 0;
		drawCross(canvas,startX,startY,6,4,0.1f,0.1f,pline);
		startY += 4*cell_h;
		
		//draw red line
		canvas.drawLine(0,startY,cl_w,startY,rl_paint);

		if(hasClassInNoon){
			drawCross(canvas,startX,startY,6,2,0.1f,0,pline);
			startY += 2*cell_h;
		}else{
			startY += 1*cell_h;
			canvas.drawLine(startX+0.1f*cell_w, startY, startX+6.9f*cell_w,
					startY, pline);
		}
		//draw red line
		canvas.drawLine(0, startY, cl_w, startY, rl_paint);
		
		drawCross(canvas,startX,startY,6,4,0.1f,0,pline);
		startY += 4*cell_h;
		
		//draw red line
		canvas.drawLine(0, startY, cl_w, startY, rl_paint);
		if(hasClassInNight){
			drawCross(canvas,startX,startY,6,3,0.1f,0,pline);
			startY += 3*cell_h;
			//canvas.drawLine(startX+0.1f*cell_w,startY,startX+6.9f*cell_w,startY,wl_paint);
		}else{
			startY += 1*cell_h;
			//canvas.drawLine(startX+0.1f*cell_w, startY, startX+6.9f*cell_w,startY, pline);
		}
		if(mes_h != startY){
			mes_h = startY;
			requestLayout();
		}
		//Log.d("abc","drawline "+mes_h);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(mes_w == 0) mes_w = getMeasuredWidth();
        Log.d("com.my","eeee+measure,"+mes_h+","+mes_w);
		//setScaleType(ScaleType.FIT_START);
		setMeasuredDimension(mes_w, mes_h);

        int width = backgroundBitmap.getWidth();
        int height = backgroundBitmap.getHeight();
        float scale = ((float) mes_w) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        backgroundBitmap = Bitmap.createBitmap(backgroundBitmap,0,0,width,height,
                matrix,true);

        setScaleType(ScaleType.MATRIX);
        setImageBitmap(backgroundBitmap);
	}

    public void setMes_w(int w){
        mes_w = w;
    }

	private void Logout(){
		Log.d("abc","cl_w="+cl_w+",cl_h="+cl_h+",cell_h="+cell_h+",cell_w="+cell_w);
	}

}
