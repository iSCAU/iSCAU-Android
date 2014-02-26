package cn.scau.scautreasure.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.view.menu.MenuBuilder;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.util.MetricsUtil;

/**
 * Special reslide menu;
 *
 * User: special
 * Date: 13-9-1
 * Time: 下午11:10
 * Mail: specialcyci@gmail.com
 */
@EViewGroup ( R.layout.residemenu )
public class ResideMenu extends FrameLayout implements GestureDetector.OnGestureListener{

    @ViewById ImageView    iv_shadow;
    @ViewById LinearLayout layout_menu;
    @ViewById cn.scau.scautreasure.widget.ElasticScrollView_ sv_menu;
    @Bean     MetricsUtil  metricsUtil;
    @App      AppContext   app;

    private AnimatorSet scaleUp_shadow;
    private AnimatorSet scaleUp_activity;
    private AnimatorSet scaleDown_activity;
    private AnimatorSet scaleDown_shadow;
    /** the activity that view attach to */
    private SherlockFragmentActivity activity;
    /** the decorview of the activity    */
    private ViewGroup view_decor;
    /** the viewgroup of the activity    */
    private ViewGroup view_activity;
    private Stack<Menu>          stack_menu;
    /** menu of current menu's parent    */
    private Menu                 current_menu_parent;
    /** the user current click item      */
    private MenuItem             current_menu_item;
    private boolean              isMenuItemClick;
    /** the top level of the menu        */
    private MenuBuilder          menu;
    /** the flag of menu open status     */
    private boolean              isOpened;
    /** the menu item id of the submenu back option */
    private int                  menuBackId;
    private GestureDetector gestureDetector;
    private float shadow_ScaleX;
    /** the view which don't want to intercept touch event */
    private List<View> ignoredViews;

    public ResideMenu(Context context) {
        super(context);

    }

    /**
     * use the method to set up the activity which residemenu need to show;
     *
     * @param activity
     */
    public void attachToActivity(SherlockFragmentActivity activity){
        initValue(activity);
        setShadowScaleXByOrientation();
        buildAnimationSet();
    }

    private void initValue(SherlockFragmentActivity activity){
        this.activity   = activity;
        stack_menu      = new Stack<Menu>();
        gestureDetector = new GestureDetector(this);
        ignoredViews    = new ArrayList<View>();
        menu            = new MenuBuilder(activity);
        view_decor      = (ViewGroup)activity.getWindow().getDecorView();
        view_activity   = (ViewGroup) view_decor.getChildAt(0);
    }

    private void setShadowScaleXByOrientation(){
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadow_ScaleX = 0.5335f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadow_ScaleX = 0.56f;
        }
    }

    /**
     * set the menu from menu's xml ( R.menu.xxx );
     *
     * @param menu_id  the resource id of menu xml ( R.menu.xxx );
     */
    public void attachToMenu(int menu_id){
        activity.getSupportMenuInflater().inflate(menu_id, menu);
        current_menu_parent = menu;
    }

    /**
     * set the menu item id of which could be return the upper stage
     *
     * @param menuBackId
     */
    public void setBackMenuItemId(int menuBackId) {
        this.menuBackId = menuBackId;
    }

    /**
     * return back the menu to upper stage
     */
    public void popBackMenu(){
        if (!stack_menu.empty())
            showMenuItem(stack_menu.pop());
    }

    /**
     * we need the call the method before the menu show,beacuse the
     * padding of activity can't get at the moment of onCreateView();
     */
    private void setViewPadding(){
        this.setPadding(view_activity.getPaddingLeft(),
                        view_activity.getPaddingTop(),
                        view_activity.getPaddingRight(),
                        view_activity.getPaddingBottom());
    }

    /**
     * show the reside menu;
     */
    public void openMenu(){
        if(!isOpened){
            isOpened = true;
            showOpenMenuRelative();
        }
    }

    private void removeMenuLayout(){
        ViewGroup parent = ((ViewGroup) sv_menu.getParent());
        parent.removeView(sv_menu);
    }

    /**
     * close the reslide menu;
     */
    public void closeMenu(){
        if(isOpened){
            isOpened = false;
            scaleUp_activity.start();
        }
    }

    /**
     * return the flag of menu status;
     *
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    /**
     * if current menu was the top level menu , return true;
     * @return
     */
    public boolean isTopMenu(){
        return stack_menu.empty();
    }

    /**
     *  call the method relative to open menu;
     */
    private void showOpenMenuRelative(){
        setViewPadding();
        scaleDown_activity.start();
        // remove self if has not remove
        if (getParent() != null) view_decor.removeView(this);
        if (sv_menu.getParent() != null) removeMenuLayout();
        view_decor.addView(this, 0);
        view_decor.addView(sv_menu);
    }

    /**
     * the animation listener of the iv_screenshot;
     */
    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened){
                layout_menu.removeAllViews();
                showCurrentMenuDelay();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if(!isOpened){
                view_decor.removeView(ResideMenu.this);
                view_decor.removeView(sv_menu);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };


    /**
     * the animation listener of closing the menu
     */
    private Animator.AnimatorListener closeListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(isMenuItemClick && current_menu_item != null){
                activity.onOptionsItemSelected(current_menu_item);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    @UiThread
    void showCurrentMenuDelay(){
        showMenuItem(current_menu_parent);
    }

    /**
     * show the menu item on the UI;
     *
     * @param menu
     */
    private void showMenuItem(Menu menu){

        current_menu_parent = menu;
        layout_menu.removeAllViews();
        for(int i = 0; i < menu.size() ; i ++)
            createMenuItem(menu.getItem(i) ,i);
    }

    /**
     * the listener of menu item's click event;
     */
    private OnClickListener onMenuClickListener =  new OnClickListener() {
        @Override
        public void onClick(View view) {

            MenuItem menuItem = ((ResideMenuItem)view).getMenu();
            if(menuItem.hasSubMenu()){
                stack_menu.push(current_menu_parent);
                showMenuItem(menuItem.getSubMenu());
            }else{
                isMenuItemClick = true;
                current_menu_item = menuItem;
                if ( menuItem.getItemId() == menuBackId) {
                    popBackMenu();
                }else{
                    closeMenu();
                }
            }

        }
    };

    /**
     * create the view ResideMenuItem and add it to layout_menu;
     *
     * @param menu
     * @param menu_index the position of the menu;
     * @return
     */
    private ResideMenuItem createMenuItem(MenuItem menu,int menu_index){

        ResideMenuItem menuItem = ResideMenuItem_.build(activity);
        layout_menu.addView(menuItem);

        ViewHelper.setAlpha(menuItem, 0);
        menuItem.setMenu(menu);
        menuItem.setOnClickListener(onMenuClickListener);
        menuItem.setIconColor(AppConstant.IV_COLOR[ menu_index % AppConstant.IV_COLOR.length ]);

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(menuItem, "translationX", -100.f, 0.0f),
                ObjectAnimator.ofFloat(menuItem, "alpha", 0.0f, 1.0f)
        );

        scaleUp.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.anticipate_overshoot_interpolator));
        // with animation;
        scaleUp.setStartDelay(50 * menu_index);
        scaleUp.setDuration(400).start();

        return menuItem;
    }


    private void buildAnimationSet(){
        scaleUp_activity   = buildScaleUpAnimation(view_activity,1.0f,1.0f);
        scaleUp_shadow     = buildScaleUpAnimation(iv_shadow,1.0f,1.0f);
        scaleDown_activity = buildScaleDownAnimation(view_activity,0.5f,0.5f);
        scaleDown_shadow   = buildScaleDownAnimation(iv_shadow,shadow_ScaleX,0.59f);
        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.addListener(closeListener);
        scaleUp_activity.playTogether(scaleUp_shadow);
        scaleDown_shadow.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow);
    }

    /**
     * a helper method to build scale down animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target,float targetScaleX,float targetScaleY){

        // set the pivotX and pivotY to scale;
        int pivotX = (int) (metricsUtil.getScreenWidth(activity)  * 1.5);
        int pivotY = (int) (metricsUtil.getScreenHeight(activity) * 0.5);

        ViewHelper.setPivotX(target, pivotX);
        ViewHelper.setPivotY(target, pivotY);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        scaleDown.setDuration(250);
        return scaleDown;
    }

    /**
     * a helper method to build scale up animation;
     *
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target,float targetScaleX,float targetScaleY){

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleUp.setDuration(250);
        return scaleUp;
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

    //--------------------------------------------------------------------------
    //
    //  GestureListener
    //
    //--------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
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

        if(isInIgnoredView(motionEvent) || isInIgnoredView(motionEvent2))
            return false;

        int distanceX    = (int) (motionEvent2.getX() - motionEvent.getX());
        int distanceY    = (int) (motionEvent2.getY() - motionEvent.getY());
        int screenWidth  = (int) metricsUtil.getScreenWidth(activity);

        if(Math.abs(distanceY) > screenWidth * 0.3)
            return false;

        if(Math.abs(distanceX) > screenWidth * 0.3){
            if(distanceX > 0 && !isOpened ){
                // from left to right;
                openMenu();
            }else if(distanceX < 0 && isOpened){
                // from right th left;
                closeMenu();
            }
        }

        return false;
    }

}
