package cn.scau.scautreasure.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.scau.scautreasure.R;

/**
 * Created by special on 14-5-7.
 */
public class RefreshIcon extends FrameLayout {

    private ImageView mRefreshButton;

    public RefreshIcon(Context context) {
        this(context, null);
    }

    public RefreshIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.refresh_action_item, this);
        mRefreshButton = (ImageView) findViewById(R.id.refresh_icon);

    }

    // Start to rotating the refresh icon.
    public void startProgress() {
        if (mRefreshButton.getAnimation() != null)
            return;
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setFillAfter(false);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        mRefreshButton.startAnimation(rotateAnimation);
    }

    // Clear the animations of refresh icon.
    public void stopProgress() {
        mRefreshButton.clearAnimation();
    }


    public void setButtonIcon(int rid) {
        mRefreshButton.setImageResource(rid);
    }


}
