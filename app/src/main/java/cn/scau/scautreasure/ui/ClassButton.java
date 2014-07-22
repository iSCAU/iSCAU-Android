package cn.scau.scautreasure.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.widget.Button;

import cn.scau.scautreasure.model.ClassModel;

public class ClassButton extends Button {

	int color = Color.BLUE;
	Paint paint = new Paint();
	StateListDrawable SLdrawable;
	BitmapDrawable defaultDrawable,clickDrawable,backDrawable;
	Bitmap defaultBitmap,clickBitmap,backBitmap;
	RectF rectF = new RectF();
	RectF rectb = new RectF();
	float radius = 10;
	float padding = 2.0f;
    int startNode,day,node_num;
    ClassModel myClassModel;
	public ClassButton(Context context) {
		super(context);
		init();
	}
    public void setClassModel(ClassModel classModel){
        this.myClassModel = classModel;
    }
    public ClassModel getClassModel(){
        return myClassModel;
    }
    public void setNode_num(int num){
        this.node_num = num;
    }
    public int getNode_num(){
        return node_num;
    }
    public void setStartNode(int startNode){
        this.startNode = startNode;
    }
    public int getStartNode(){
        return startNode;
    }
    public void setDay(int day){
        this.day = day;
    }
    public int getDay(){
        return day;
    }
	void init(){
		paint.setStrokeWidth(1);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);
		//paint.setColor(color);
		rectb.left=0;rectb.top=0;
		rectF.left=padding;rectF.top=padding;
	}
	public void setRadius(int r){
		radius = r;
	}
	public void setColor(int color){
		this.color = color;
		//requestLayout();
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = getWidth();
        int h = getHeight();
        if(w == 0) w = MeasureSpec.getSize(widthMeasureSpec);
        if(h == 0) h = MeasureSpec.getSize(heightMeasureSpec);

		if(w ==0 || h==0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        };
        Log.d("com.my","CCCC+"+w+","+h);
		rectb.bottom = h;rectb.right = w;
		rectF.bottom = h - padding;rectF.right = w - padding;
		paint.setColor(color);
		defaultBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		clickBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		backBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(backBitmap);
		paint.setAlpha(1);
		canvas.drawRect(rectb, paint);
		canvas = new Canvas(defaultBitmap);
		paint.setAlpha(200);
		canvas.drawRoundRect(rectF, radius, radius, paint);
		canvas = new Canvas(clickBitmap);
		paint.setAlpha(100);
		canvas.drawRoundRect(rectF, radius, radius, paint);
		defaultDrawable = new BitmapDrawable(defaultBitmap);
		clickDrawable = new BitmapDrawable(clickBitmap);

		SLdrawable = new StateListDrawable();
		SLdrawable.addState(new int[]{-android.R.attr.state_window_focused,
				android.R.attr.state_enabled}, defaultDrawable);
		SLdrawable.addState(new int[]{-android.R.attr.state_window_focused,
				-android.R.attr.state_enabled}, defaultDrawable);
		SLdrawable.addState(new int[]{android.R.attr.state_pressed}, clickDrawable);
		//this.setSelected(true);
		//SLdrawable.setAlpha(10);
		this.setBackgroundDrawable(SLdrawable);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if(w ==0 || h==0) return;

		Log.d("com.my","onSize:"+w+" "+h+" "+oldw+" "+oldh);
		
	}
	

}
