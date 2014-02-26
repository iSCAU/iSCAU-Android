package cn.scau.scautreasure.ui;

import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import cn.scau.scautreasure.util.MetricsUtil;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.umeng.analytics.MobclickAgent;
import org.androidannotations.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 辅助二级菜单左滑退出
 * (暂时没有启用!)
 *
 * User: special
 * Date: 13-9-26
 * Time: 下午5:25
 * Mail: specialcyci@gmail.com
 */
@EActivity
public class InjectedSherlockActivity extends SherlockActivity implements GestureDetector.OnGestureListener{

    @Bean
    MetricsUtil metricsUtil;

    private GestureDetector gestureDetector;
    private List<View> ignoredViews;

    @AfterViews
    void initGestureDetector(){
        ignoredViews    = new ArrayList<View>();
        gestureDetector = new GestureDetector(this);
    }

    @AfterViews
    void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @OptionsItem
    public void home(){
        finish();
    }

    /**
     * if there ware some view you don't want reside menu
     * to intercept their touch event,you can user the method
     * to set.
     *
     * @param v
     */
    public void addIgnoredView(View v){
        ignoredViews.add(v);
    }

    /**
     * remove the view from ignored view list;
     * @param v
     */
    public void removeIgnoredView(View v){
        ignoredViews.remove(v);
    }

    /**
     * clear the ignored view list;
     */
    public void clearIgnoredViewList(){
        ignoredViews.clear();
    }

    /**
     * if the motion evnent was relative to the view
     * which in ignored view list,return true;
     *
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        if(motionEvent2 == null || motionEvent == null) return false;
        if(isInIgnoredView(motionEvent) || isInIgnoredView(motionEvent2))
            return false;

        int distanceX    = (int) (motionEvent2.getX() - motionEvent.getX());
        int distanceY    = (int) (motionEvent2.getY() - motionEvent.getY());
        int screenWidth  = (int) metricsUtil.getScreenWidth(this);

        if(Math.abs(distanceY) > screenWidth * 0.3)
            return false;

        if(Math.abs(distanceX) > screenWidth * 0.3){
            if(distanceX > 0){
                // from left th right;
                finish();
            }
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
