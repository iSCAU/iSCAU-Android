package cn.scau.scautreasure.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import cn.scau.scautreasure.R;

/**
 * 下拉刷新控件，主要测试了WebView，代码中已实现ListView下拉和上拉刷新，不过没有怎么测
 * 至于GridView、WebView等，代码中没有实现，不过很好拓展，在isReadyForPullUp() 和
 * isReadyForPullDown()这两个方法中加入响应的View的上下边界判断就OK了
 *
 * @author zhangshuo
 * @version 1.0
 */
public class PullToRefreshView extends RelativeLayout {

    /**
     * 手指滑动距离与控件移动距离的比例为2:1
     */
    static final float FRICTION = 2.0f;

    /**
     * 显示“下拉刷新”的状态
     */
    static final int PULL_TO_REFRESH = 0x0;
    /**
     * 显示“释放刷新”的状态
     */
    static final int RELEASE_TO_REFRESH = 0x1;
    /**
     * 用户通过下拉进入的刷新状态
     */
    static final int REFRESHING = 0x2;
    /**
     * 用户通过代码强制进入的刷新状态
     */
    static final int MANUAL_REFRESHING = 0x3;

    /**
     * 私有模式，不提供对外调用，
     * 仅用来标示“用户下拉刷新成功后，headerView显示在头部，当用户手指向上滑动时，将headerView跟随用户滑动向上滑动”
     * 及“用户上拉更多成功后，footerView显示在底部，当用户手指向下滑动时，将footerView跟随用户滑动向下滑动”这两个过程的模式
     */
    private static final int MODE_PULL_TO_SCROLL_HEADER_OR_FOOTER = 0x0;
    /**
     * 标示当前支持下拉刷新模式
     */
    public static final int MODE_PULL_DOWN_TO_REFRESH = 0x1;
    /**
     * 标示当前支持上拉更多模式
     */
    public static final int MODE_PULL_UP_TO_REFRESH = 0x2;
    /**
     * 标示当前支持下拉刷新和上拉更多两种模式
     */
    public static final int MODE_BOTH = 0x3;


    private Context context;
    /**
     * 滚动对象
     */
    private Scroller scroller;
    /**
     * 判断用户手指的移动距离是否足以响应为move
     */
    private int touchSlop;

    private float initialMotionY;
    private float lastMotionX;
    private float lastMotionY;
    private boolean isBeingDragged = false;

    /**
     * 记录headerView当前的状态
     */
    private int headerState = PULL_TO_REFRESH;
    /**
     * 记录footerView当前的状态
     */
    private int footerState = PULL_TO_REFRESH;
    /**
     * 当前所支持的模式
     */
    private int mode = MODE_PULL_DOWN_TO_REFRESH;
    /**
     * 当前处于的模式
     */
    private int currentMode;

    /**
     * 根据不同的mode，contentView所在父View的位置不同，下拉刷新时为1，上拉更多时为1，上拉下拉都支持时为2
     */
    private int index = 1;

    /**
     * 标示当处于刷新状态时，是否需要禁用滑动
     */
    private boolean disableScrollingWhileRefreshing = false;

    /**
     * 标示是否允许滑动刷新
     */
    private boolean isPullToRefreshEnabled = true;

    private LoadingLayout headerLayout;
    private LoadingLayout footerLayout;
    private int headerHeight;

    /**
     * 记录当处于刷新状态时，用户继续下拉的次数
     */
    private int pullWithRefreshingCount = 0;
    /**
     * 记录当处于加载更多状态时，用户继续上拉的次数
     */
    private int pullWithLoadingMoreCount = 0;

    /**
     * 刷新回调接口
     */
    private OnRefreshListener onRefreshListener;

    /**
     * 加载更多回调接口
     */
    private OnLoadMoreListener onLoadMoreListener;


    public PullToRefreshView(Context context) {
        super(context);
        init(context, null);
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @方法描述: 初始化方法
     * @作者:zhangshuo
     */
    private void init(Context context, AttributeSet attrs) {

        scroller = new Scroller(context);

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        this.context = context;

        this.addLoadingView();
    }

    /**
     * @方法描述: 根据当前模式设置，加载头部和底部布局
     * @作者:zhangshuo
     */
    public void addLoadingView() {

        String pullDownLabel = context
                .getString(R.string.pull_to_refresh_pull_down_label);
        String refreshingDownLabel = context
                .getString(R.string.pull_to_refresh_refreshing_down_label);
        String releaseDownLabel = context
                .getString(R.string.pull_to_refresh_release_down_label);
        String pullUpLabel = context
                .getString(R.string.pull_to_refresh_pull_up_label);
        String refreshingUpLabel = context
                .getString(R.string.pull_to_refresh_refreshing_up_label);
        String releaseUpLabel = context
                .getString(R.string.pull_to_refresh_release_up_label);

		/*加载头部和底部View*/
        if (mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH) {
            headerLayout = new LoadingLayout(context,
                    MODE_PULL_DOWN_TO_REFRESH, releaseDownLabel, pullDownLabel,
                    refreshingDownLabel);
            addView(headerLayout, 0, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            measureView(headerLayout);
            headerHeight = headerLayout.getMeasuredHeight();
        }
        if (mode == MODE_PULL_UP_TO_REFRESH || mode == MODE_BOTH) {
            footerLayout = new LoadingLayout(context, MODE_PULL_UP_TO_REFRESH,
                    releaseUpLabel, pullUpLabel, refreshingUpLabel);
            LayoutParams lp2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addView(footerLayout, lp2);
            measureView(footerLayout);
            headerHeight = footerLayout.getMeasuredHeight();
        }

		/*隐藏头部和底部View*/
        switch (mode) {
            case MODE_BOTH:
                index = 2;
                setPadding(0, -headerHeight, 0, -headerHeight);
                break;
            case MODE_PULL_UP_TO_REFRESH:
                index = 1;
                setPadding(0, 0, 0, -headerHeight);
                break;
            case MODE_PULL_DOWN_TO_REFRESH:
            default:
                index = 1;
                setPadding(0, -headerHeight, 0, 0);
                break;
        }

    }

    /**
     * 在头部和底部View添加完成后，重新布局，以避免在隐藏headerView和footerView时会把一部分内容（contentView）隐藏
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
        View contentView = null;
        LayoutParams lp1 = null;
        switch (mode) {
            case MODE_BOTH:
                contentView = this.getChildAt(index);
                lp1 = (LayoutParams) contentView.getLayoutParams();
                lp1.setMargins(0, headerHeight, 0, headerHeight);
                break;
            case MODE_PULL_UP_TO_REFRESH:
                contentView = this.getChildAt(index);
                lp1 = (LayoutParams) contentView.getLayoutParams();
                lp1.setMargins(0, 0, 0, headerHeight);
                break;
            case MODE_PULL_DOWN_TO_REFRESH:
            default:
                contentView = this.getChildAt(index);
                lp1 = (LayoutParams) contentView.getLayoutParams();
                lp1.setMargins(0, headerHeight, 0, 0);
                break;
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public final boolean onInterceptTouchEvent(MotionEvent event) {

        Log.e("Intercept", "start");

        if (!isPullToRefreshEnabled) {
            return false;
        }

        if ((isLoadingMore() || isRefreshing()) && disableScrollingWhileRefreshing) {
            return true;
        }

        final int action = event.getAction();

        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            isBeingDragged = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && isBeingDragged) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                Log.e("Intercept", "down");
                if (isReadyForPull()) {
                    lastMotionY = initialMotionY = event.getY();
                    lastMotionX = event.getX();
                    isBeingDragged = false;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.e("Intercept", "move");
                if (isReadyForPull()) {
                    final float y = event.getY();
                    final float dy = y - lastMotionY;
                    final float yDiff = Math.abs(dy);
                    final float xDiff = Math.abs(event.getX() - lastMotionX);

                    if (yDiff > touchSlop && yDiff > xDiff) {
                        if ((mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH)
                                && dy >= 0.0001f && isReadyForPullDown()) {
                        /*可以下拉刷新*/
                            lastMotionY = y;
                            isBeingDragged = true;
                            currentMode = MODE_PULL_DOWN_TO_REFRESH;
                        } else if ((mode == MODE_PULL_UP_TO_REFRESH || mode == MODE_BOTH)
                                && dy <= 0.0001f && isReadyForPullUp()) {
						/*可以上拉更多*/
                            lastMotionY = y;
                            isBeingDragged = true;
                            currentMode = MODE_PULL_UP_TO_REFRESH;
                        } else if ((isRefreshing() && getScrollY() < 0) || (isLoadingMore() && getScrollY() > 0)) {
						/*当前headerView或footerView处于显示状态，开启跟随手指滑动模式*/
                            lastMotionY = y;
                            isBeingDragged = true;
                            currentMode = MODE_PULL_TO_SCROLL_HEADER_OR_FOOTER;
                        }
                    }
                }
                break;
            }
        }
        return isBeingDragged;
    }

    @Override
    public final boolean onTouchEvent(MotionEvent event) {
        Log.e("Touch", "start");
        if (!isPullToRefreshEnabled) {
            return false;
        }

        if (isRefreshing() && disableScrollingWhileRefreshing) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN
                && event.getEdgeFlags() != 0) {
            return false;
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                Log.e("Touch", "down");
                if (isReadyForPull()) {
                    lastMotionY = initialMotionY = event.getY();
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.e("Touch", "move");
                if (isBeingDragged) {
                    lastMotionY = event.getY();
                    this.pullEvent();
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                Log.e("Touch", "up");
                if (isBeingDragged) {
                    isBeingDragged = false;

                    if (isRefreshing() && pullWithRefreshingCount == 0) {
                        pullWithRefreshingCount = 1;
                    }
                    if (isLoadingMore() && pullWithLoadingMoreCount == 0) {
                        pullWithLoadingMoreCount = 1;
                    }

                    switch (currentMode) {
                        case MODE_PULL_TO_SCROLL_HEADER_OR_FOOTER:
					/*将headerView和footerView隐藏*/
                            smoothScrollTo(0);
                            break;
                        case MODE_PULL_UP_TO_REFRESH:
					/*判断是否激活加载更多*/
                            if (footerState == RELEASE_TO_REFRESH && null != onLoadMoreListener) {
                                setLoadingMoreInternal(true);
                                onLoadMoreListener.onLoadMore();
                            } else {
                                smoothScrollTo(0);
                            }
                            break;
                        case MODE_PULL_DOWN_TO_REFRESH:
					/*判断是否激活刷新*/
                            if (headerState == RELEASE_TO_REFRESH && null != onRefreshListener) {
                                setRefreshingInternal(true);
                                onRefreshListener.onRefresh();
                            } else {
                                smoothScrollTo(0);
                            }
                            break;
                    }

                    return true;
                }
                break;
            }
        }

        return false;
    }

    /**
     * @return
     * @方法描述: 处理用户滑动的方法
     * @作者:zhangshuo
     */
    private boolean pullEvent() {

        final int newHeight;
        final int oldHeight = this.getScrollY();

        switch (currentMode) {
            case MODE_PULL_TO_SCROLL_HEADER_OR_FOOTER:
                newHeight = Math.round((initialMotionY - lastMotionY));
                break;
            case MODE_PULL_UP_TO_REFRESH:
                newHeight = Math.round(Math.max(initialMotionY - lastMotionY, 0)
                        / FRICTION);
                break;
            case MODE_PULL_DOWN_TO_REFRESH:
            default:
                newHeight = Math.round(Math.min(initialMotionY - lastMotionY, 0)
                        / FRICTION);
                break;
        }

        if (isRefreshing() && pullWithRefreshingCount == 0) {
			/*处于刷新状态下，第一次继续下拉，此时headerView已经显示在头部*/
            if ((-headerHeight + newHeight) < 0) {
                scrollTo(-headerHeight + newHeight);
            } else {
                scrollTo(0);
                if (((WebView) getChildAt(index)).getChildAt(0).getHeight() > getChildAt(index).getHeight()) {
                    getChildAt(index).scrollTo(0, newHeight - headerHeight);
                }
            }
        } else if (isLoadingMore() && pullWithLoadingMoreCount == 0) {
			/*处于刷新状态下，第一次继续下拉，此时headerView已经显示在头部*/
            if ((headerHeight + newHeight) > 0) {
                scrollTo(headerHeight + newHeight);
            } else {
                scrollTo(0);
                if (((WebView) getChildAt(index)).getChildAt(0).getHeight() > getChildAt(index).getHeight()) {
                    getChildAt(index).scrollTo(0, newHeight + headerHeight + ((WebView) getChildAt(index)).getChildAt(0).getHeight() - getChildAt(index).getHeight());
                }
            }
        } else {
            scrollTo(newHeight);
        }

        if (newHeight != 0) {

            switch (currentMode) {
                case MODE_PULL_UP_TO_REFRESH:
                    if (footerState == PULL_TO_REFRESH && headerHeight < Math.abs(newHeight)) {
                        footerState = RELEASE_TO_REFRESH;
                        footerLayout.releaseToRefresh();
                        return true;

                    } else if (footerState == RELEASE_TO_REFRESH
                            && headerHeight >= Math.abs(newHeight)) {
                        footerState = PULL_TO_REFRESH;
                        footerLayout.pullToRefresh();
                        return true;
                    }
                    break;
                case MODE_PULL_DOWN_TO_REFRESH:
                    if (headerState == PULL_TO_REFRESH && headerHeight < Math.abs(newHeight)) {
                        headerState = RELEASE_TO_REFRESH;
                        headerLayout.releaseToRefresh();
                        return true;

                    } else if (headerState == RELEASE_TO_REFRESH
                            && headerHeight >= Math.abs(newHeight)) {
                        headerState = PULL_TO_REFRESH;
                        headerLayout.pullToRefresh();
                        return true;
                    }
                    break;
            }

        }

        return oldHeight != newHeight;
    }

    /**
     * @return
     * @方法描述: 判断当前状态是否可以进行上拉更多或下拉刷新的滑动操作
     * @作者:zhangshuo
     */
    private boolean isReadyForPull() {
        switch (mode) {
            case MODE_PULL_DOWN_TO_REFRESH:
                return isReadyForPullDown();
            case MODE_PULL_UP_TO_REFRESH:
                return isReadyForPullUp();
            case MODE_BOTH:
                return isReadyForPullUp() || isReadyForPullDown();
        }
        return false;
    }

    /**
     * @return
     * @方法描述: 判断当前状态是否可以进行下拉刷新操作
     * @作者:zhangshuo
     */
    private boolean isReadyForPullDown() {
        // TODO Auto-generated method stub
        if (getChildCount() > 1) {
            Log.e("Ready--down", String.valueOf(getChildCount()));
            View childView = this.getChildAt(index);
            if (childView instanceof ListView) {
                int top = ((ListView) childView).getChildAt(0).getTop();
                int pad = ((ListView) childView).getListPaddingTop();
                if ((Math.abs(top - pad)) < 3
                        && ((ListView) childView).getFirstVisiblePosition() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (childView instanceof WebView) {
                Log.e("Ready--down", "WebView");
                if (((WebView) childView).getScrollY() == 0) {
                    return true;
                } else {
                    return false;
                }
            }

        }
        return false;
    }

    /**
     * @return
     * @方法描述:判断当前状态是否可以上拉更多的滑动操作
     * @作者:zhangshuo
     */
    private boolean isReadyForPullUp() {
        // TODO Auto-generated method stub
        if (getChildCount() > 1) {
            Log.e("Ready--up", String.valueOf(getChildCount()));
            View childView = this.getChildAt(index);
            if (childView instanceof ListView) {
                int top = ((ListView) childView).getChildAt(
                        ((ListView) childView).getCount()).getBottom();
                int pad = ((ListView) childView).getListPaddingBottom();
                if ((Math.abs(top - pad)) < 3
                        && ((ListView) childView).getFirstVisiblePosition() == ((ListView) childView)
                        .getCount()) {
                    return true;
                } else {
                    return false;
                }
            } else if (childView instanceof WebView) {
                Log.e("Ready--up", "WebView");
                int off = ((WebView) childView).getScrollY()
                        + ((WebView) childView).getHeight()
                        - ((WebView) childView).getChildAt(0).getHeight();
                if (off >= 0) {
                    return true;
                } else {
                    return false;
                }
            }

        }
        return false;
    }

    /**
     * @return
     * @方法描述: 是否允许上拉更多或下拉刷新的滑动操作
     * @作者:zhangshuo
     */
    public final boolean isPullToRefreshEnabled() {
        return isPullToRefreshEnabled;
    }

    /**
     * @return
     * @方法描述: 当处于刷新状态时，是否需要禁用滑动
     * @作者:zhangshuo
     */
    public final boolean isDisableScrollingWhileRefreshing() {
        return disableScrollingWhileRefreshing;
    }

    /**
     * @return
     * @方法描述: 当前正处于刷新中
     * @作者:zhangshuo
     */
    public final boolean isRefreshing() {
        return headerState == REFRESHING || headerState == MANUAL_REFRESHING;
    }

    /**
     * @return
     * @方法描述: 当前正处于加载更多中
     * @作者:zhangshuo
     */
    public final boolean isLoadingMore() {
        return footerState == REFRESHING || footerState == MANUAL_REFRESHING;
    }

    /**
     * @param disableScrollingWhileRefreshing
     * @方法描述: 设置当处于刷新状态时，是否需要禁用滑动
     * @作者:zhangshuo
     */
    public final void setDisableScrollingWhileRefreshing(
            boolean disableScrollingWhileRefreshing) {
        this.disableScrollingWhileRefreshing = disableScrollingWhileRefreshing;
    }

    /**
     * @方法描述: 结束刷新状态
     * @作者:zhangshuo
     */
    public final void onRefreshComplete() {
        if (headerState != PULL_TO_REFRESH) {
            resetHeader();
        }
        pullWithRefreshingCount = 0;
    }

    /**
     * @方法描述: 结束加载更多状态
     * @作者:zhangshuo
     */
    public final void onLoadMoreComplete() {
        if (footerState != PULL_TO_REFRESH) {
            resetFooter();
        }
        pullWithLoadingMoreCount = 0;
    }

    /**
     * @param enable
     * @方法描述: 设置否允许滑动刷新
     * @作者:zhangshuo
     */
    public final void setPullToRefreshEnabled(boolean enable) {
        this.isPullToRefreshEnabled = enable;
    }

    /**
     * @方法描述: 强制设置为刷新状态
     * @作者:zhangshuo
     */
    public final void setRefreshing() {
        this.setRefreshing(true);
    }

    /**
     * @方法描述: 强制设置为加载更多状态
     * @作者:zhangshuo
     */
    public final void setLoadingMore() {
        this.setLoadingMore(true);
    }

    /**
     * @param doScroll
     * @方法描述: 强制设置为刷新状态
     * @作者:zhangshuo
     */
    public final void setRefreshing(boolean doScroll) {
        if (!isRefreshing()) {
            setRefreshingInternal(doScroll);
            headerState = MANUAL_REFRESHING;
        }
    }

    /**
     * @param doScroll
     * @方法描述: 强制设置为加载更多状态
     * @作者:zhangshuo
     */
    public final void setLoadingMore(boolean doScroll) {
        if (!isLoadingMore()) {
            setLoadingMoreInternal(doScroll);
            footerState = MANUAL_REFRESHING;
        }
    }

    protected final int getCurrentMode() {
        return currentMode;
    }

    protected final LoadingLayout getFooterLayout() {
        return footerLayout;
    }

    protected final LoadingLayout getHeaderLayout() {
        return headerLayout;
    }

    protected final int getHeaderHeight() {
        return headerHeight;
    }

    protected final int getMode() {
        return mode;
    }

    /**
     * @方法描述: 重置headerView
     * @作者:zhangshuo
     */
    protected void resetHeader() {
        headerState = PULL_TO_REFRESH;
        isBeingDragged = false;

        if (null != headerLayout) {
            headerLayout.reset();
        }

        smoothScrollTo(0);
    }

    /**
     * @方法描述: 重置footerView
     * @作者:zhangshuo
     */
    protected void resetFooter() {
        footerState = PULL_TO_REFRESH;
        isBeingDragged = false;

        if (null != footerLayout) {
            footerLayout.reset();
        }

        smoothScrollTo(0);
    }

    /**
     * @param doScroll
     * @方法描述: 强制设置为刷新状态，并显示出headerView
     * @作者:zhangshuo
     */
    protected void setRefreshingInternal(boolean doScroll) {
        headerState = REFRESHING;
        pullWithRefreshingCount = 0;

        if (null != headerLayout) {
            headerLayout.refreshing();
        }

        if (doScroll) {
            smoothScrollTo(-headerHeight);
        }
    }

    /**
     * @param doScroll
     * @方法描述: 强制设置为加载更多状态，并显示出footerView
     * @作者:zhangshuo
     */
    protected void setLoadingMoreInternal(boolean doScroll) {
        footerState = REFRESHING;
        pullWithLoadingMoreCount = 0;

        if (null != footerLayout) {
            footerLayout.refreshing();
        }

        if (doScroll) {
            smoothScrollTo(headerHeight);
        }
    }

    protected final void scrollTo(int y) {
        scrollTo(0, y);
    }

    protected final void smoothScrollTo(int y) {

        scroller.startScroll(0, getScrollY(), 0, -(getScrollY() - y), 500);
        invalidate();

    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if (scroller.computeScrollOffset()) {
            scrollTo(0, this.scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * @param listener
     * @方法描述: 设置刷新回调接口
     * @作者:zhangshuo
     */
    public final void setOnRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }

    /**
     * @param listener
     * @方法描述: 设置加载更多回调接口
     * @作者:zhangshuo
     */
    public final void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    /**
     * @公司: 南京红松信息技术有限公司
     * @CLASS:OnRefreshListener
     * @描述: 刷新回调接口
     * @作者:zhangshuo
     * @版本:v1.0
     * @日期:2014年7月15日 上午11:59:50
     */
    public static interface OnRefreshListener {

        public void onRefresh();

    }

    /**
     * @公司: 南京红松信息技术有限公司
     * @CLASS:OnLoadMoreListener
     * @描述: 加载更多回调接口
     * @作者:zhangshuo
     * @版本:v1.0
     * @日期:2014年7月15日 下午12:00:06
     */
    public static interface OnLoadMoreListener {

        public void onLoadMore();

    }

}
