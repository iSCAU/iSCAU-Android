package cn.scau.scautreasure.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.scau.scautreasure.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * User: special
 * Date: 13-8-29
 * Time: 下午1:28
 * Mail: specialcyci@gmail.com
 */
@EViewGroup(R.layout.classtable_tab )
public class ClassTabWidget extends LinearLayout{

    @ViewById
    ImageView iv_underline;

    @ViewById
    LinearLayout linear_tab;

    private Activity ctx;

    private int currentPosition = 0;

    private onTabChangeListener listener;

    public ClassTabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx          = (Activity) context;
    }

    @AfterViews
    void initView(){
        int count = linear_tab.getChildCount();
        for(int i = 0; i < count ;i ++) setTabTextStyle(i,false);
        for(int j = 0; j < count ;j ++) {
            View v = linear_tab.getChildAt(j);
            v.setOnClickListener(onTabClick);
            v.setTag(j);
        }
    }

    private OnClickListener onTabClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            changeWeekDay(position);
            listener.change(position);
        }
    };

    /**
     * true the underline control to the click position
     * @param position
     */
    public void changeWeekDay(int position){

        float offset = (float) (getWidth() - getPaddingLeft() - getPaddingRight()) / 7;

        System.out.println("offset : " + offset);
        LinearLayout.LayoutParams params = (LayoutParams) iv_underline.getLayoutParams();
        params.width = (int) offset;
        iv_underline.setLayoutParams(params);

        TranslateAnimation animation = new TranslateAnimation(
                offset * currentPosition ,position * offset,0,0);

        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setDuration(300);
        iv_underline.startAnimation(animation);

        setTabTextStyle(currentPosition,false);
        setTabTextStyle(position,true);
        currentPosition = position;
    }

    /**
     * set the tab text style according to if is clicked;
     * @param position
     * @param isClicked
     */
    public void setTabTextStyle(int position,boolean isClicked){

        LinearLayout parent = (LinearLayout) linear_tab.getChildAt(position);
        TextView     tv_eng = (TextView)     parent.getChildAt(0);
        TextView     tv_num = (TextView)     parent.getChildAt(1);

        if(isClicked){
            tv_eng.setTextAppearance(ctx,R.style.classTab_click_engtext);
            tv_num.setTextAppearance(ctx,R.style.classTab_click_numtext);
        }else{
            tv_eng.setTextAppearance(ctx,R.style.classTab_normal_engtext);
            tv_num.setTextAppearance(ctx,R.style.classTab_normal_numtext);
        }

    }

    public onTabChangeListener getListener() {
        return listener;
    }

    public void setListener(onTabChangeListener listener) {
        this.listener = listener;
    }

    public interface onTabChangeListener{
        void change(int posistion);
    }
}
