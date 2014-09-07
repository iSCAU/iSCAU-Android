package cn.scau.scautreasure.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.tjerkw.slideexpandable.library.WrapperListAdapterImpl;

import java.util.BitSet;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.ui.SchoolActivity;
import cn.scau.scautreasure.widget.SchoolActivityToggle;

/**
 * Created by stcdasqy on 2014-08-25.
 * webView跟原本的slideExpandable不是很适配，因为webView在需要一点时间来测量自己的width和
 * height，所以copy了一下原版的，修改了一点东西。
 * 仅适用于搭配校园活动中的下拉刷新控件。
 *
 * 列举修复自原生版本的部分：
 * ①增加getToggleFromTarget方法
 * ②修改了animateView，配合校园活动中的箭头变向
 * ③修改了ExpandCollapseAnimation中获取高度的部分，这里直接改用获取target下
 * 的webView的measureHeight。
 */
public class SchoolActivityExpandableListAdapter extends WrapperListAdapterImpl {
    /**
     * Reference to the last expanded list item.
     * Since lists are recycled this might be null if
     * though there is an expanded list item
     */
    private View lastOpen = null;
    /**
     * The position of the last expanded list item.
     * If -1 there is no list item expanded.
     * Otherwise it points to the position of the last expanded list item
     */
    private int lastOpenPosition = -1;

    /**
     * Default Animation duration
     * Set animation duration with @see setAnimationDuration
     */
    private int animationDuration = 330;

    /**
     * A list of positions of all list items that are expanded.
     * Normally only one is expanded. But a mode to expand
     * multiple will be added soon.
     * <p/>
     * If an item onj position x is open, its bit is set
     */
    private BitSet openItems = new BitSet();
    /**
     * We remember, for each collapsable view its height.
     * So we dont need to recalculate.
     * The height is calculated just before the view is drawn.
     */
    private final SparseIntArray viewHeights = new SparseIntArray(10);

    private int toggle_button_id;
    private int expandable_view_id;

    public SchoolActivityExpandableListAdapter(ListAdapter wrapped, int toggle_button_id, int expandable_view_id) {
        super(wrapped);
        this.toggle_button_id = toggle_button_id;
        this.expandable_view_id = expandable_view_id;
    }

    public SchoolActivityExpandableListAdapter(ListAdapter wrapped) {
        super(wrapped);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = wrapped.getView(position, view, viewGroup);
        enableFor(view, position);
        return view;
    }

    /**
     * This method is used to get the Button view that should
     * expand or collapse the Expandable View.
     * <br/>
     * Normally it will be implemented as:
     * <pre>
     * return parent.findViewById(R.id.expand_toggle_button)
     * </pre>
     * <p/>
     * A listener will be attached to the button which will
     * either expand or collapse the expandable view
     *
     * @param parent the list view item
     * @return a child of parent which is a button
     * @ensure return!=null
     * @see #getExpandableView(View)
     */
    public View getExpandToggleButton(View parent) {
        return parent.findViewById(toggle_button_id);
    }

    /**
     * This method is used to get the view that will be hidden
     * initially and expands or collapse when the ExpandToggleButton
     * is pressed @see getExpandToggleButton
     * <br/>
     * Normally it will be implemented as:
     * <pre>
     * return parent.findViewById(R.id.expandable)
     * </pre>
     *
     * @param parent the list view item
     * @return a child of parent which is a view (or often ViewGroup)
     * that can be collapsed and expanded
     * @ensure return!=null
     * @see #getExpandToggleButton(View)
     */
    public View getExpandableView(View parent) {
        return parent.findViewById(expandable_view_id);
    }

    public SchoolActivityToggle getToggleFromTarget(View target) {
        ViewParent vp = target.getParent();
        while (vp != null && vp instanceof View) {
            View v = ((View) vp).findViewById(toggle_button_id);
            if (v != null && v instanceof SchoolActivityToggle) {
                return (SchoolActivityToggle) v;
            }
        }
        return null;
    }

    /**
     * Gets the duration of the collapse animation in ms.
     * Default is 330ms. Override this method to change the default.
     *
     * @return the duration of the anim in ms
     */
    public int getAnimationDuration() {
        return animationDuration;
    }

    /**
     * Set's the Animation duration for the Expandable animation
     *
     * @param duration The duration as an integer in MS (duration > 0)
     * @throws IllegalArgumentException if parameter is less than zero
     */
    public void setAnimationDuration(int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration is less than zero");
        }

        animationDuration = duration;
    }

    /**
     * Check's if any position is currently Expanded
     * To collapse the open item @see collapseLastOpen
     *
     * @return boolean True if there is currently an item expanded, otherwise false
     */
    public boolean isAnyItemExpanded() {
        return (lastOpenPosition != -1) ? true : false;
    }

    public void enableFor(View parent, int position) {
        View more = getExpandToggleButton(parent);
        View itemToolbar = getExpandableView(parent);
        itemToolbar.measure(parent.getWidth(), parent.getHeight());

        enableFor(more, itemToolbar, position);
    }


    private void enableFor(final View button, final View target, final int position) {
        if (target == lastOpen && position != lastOpenPosition) {
            // lastOpen is recycled, so its reference is false
            lastOpen = null;
        }
        if (position == lastOpenPosition) {
            // re reference to the last view
            // so when can animate it when collapsed
            lastOpen = target;
        }
        int height = viewHeights.get(position, -1);
        if (height == -1) {
            viewHeights.put(position, target.getMeasuredHeight());
            updateExpandable(target, position);
        } else {
            updateExpandable(target, position);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Animation a = target.getAnimation();

                if (a != null && a.hasStarted() && !a.hasEnded()) {

                    a.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.performClick();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                } else {

                    target.setAnimation(null);

                    int type = target.getVisibility() == View.VISIBLE
                            ? ExpandCollapseAnimation.COLLAPSE
                            : ExpandCollapseAnimation.EXPAND;

                    // remember the state
                    if (type == ExpandCollapseAnimation.EXPAND) {
                        openItems.set(position, true);
                    } else {
                        openItems.set(position, false);
                    }
                    // check if we need to collapse a different view
                    if (type == ExpandCollapseAnimation.EXPAND) {
                        if (lastOpenPosition != -1 && lastOpenPosition != position) {
                            if (lastOpen != null) {
                                animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
                            }
                            openItems.set(lastOpenPosition, false);
                        }
                        lastOpen = target;
                        lastOpenPosition = position;
                    } else if (lastOpenPosition == position) {
                        lastOpenPosition = -1;
                    }
                    animateView(target, type);
                }
            }
        });
    }

    private void updateExpandable(View target, int position) {

        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) target.getLayoutParams();
        if (openItems.get(position)) {
            target.setVisibility(View.VISIBLE);
            params.bottomMargin = 0;
        } else {
            target.setVisibility(View.GONE);
            params.bottomMargin = 0 - viewHeights.get(position);
        }
    }

    /**
     * Performs either COLLAPSE or EXPAND animation on the target view
     *
     * @param target the view to animate
     * @param type   the animation type, either ExpandCollapseAnimation.COLLAPSE
     *               or ExpandCollapseAnimation.EXPAND
     */
    private void animateView(final View target, final int type) {
        SchoolActivityToggle toggle = getToggleFromTarget(target);
        if (toggle != null)
            toggle.getExtraOnClickListener().onClick(toggle);
        Animation anim = new ExpandCollapseAnimation(
                target,
                type
        );
        anim.setDuration(getAnimationDuration());
        target.startAnimation(anim);
    }


    /**
     * Closes the current open item.
     * If it is current visible it will be closed with an animation.
     *
     * @return true if an item was closed, false otherwise
     */
    public boolean collapseLastOpen() {
        if (isAnyItemExpanded()) {
            // if visible animate it out
            if (lastOpen != null) {
                animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
            }
            openItems.set(lastOpenPosition, false);
            lastOpenPosition = -1;
            return true;
        }
        return false;
    }

    public Parcelable onSaveInstanceState(Parcelable parcelable) {

        SavedState ss = new SavedState(parcelable);
        ss.lastOpenPosition = this.lastOpenPosition;
        ss.openItems = this.openItems;
        return ss;
    }

    public void onRestoreInstanceState(SavedState state) {

        this.lastOpenPosition = state.lastOpenPosition;
        this.openItems = state.openItems;
    }

    /**
     * Utility methods to read and write a bitset from and to a Parcel
     */
    private static BitSet readBitSet(Parcel src) {
        int cardinality = src.readInt();

        BitSet set = new BitSet();
        for (int i = 0; i < cardinality; i++) {
            set.set(src.readInt());
        }

        return set;
    }

    private static void writeBitSet(Parcel dest, BitSet set) {
        int nextSetBit = -1;

        dest.writeInt(set.cardinality());

        while ((nextSetBit = set.nextSetBit(nextSetBit + 1)) != -1) {
            dest.writeInt(nextSetBit);
        }
    }

    /**
     * The actual state class
     */
    static class SavedState extends View.BaseSavedState {
        public BitSet openItems = null;
        public int lastOpenPosition = -1;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            in.writeInt(lastOpenPosition);
            writeBitSet(in, openItems);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            lastOpenPosition = out.readInt();
            openItems = readBitSet(out);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    public class ExpandCollapseAnimation extends Animation {
        private View mAnimatedView;
        private int mEndHeight;
        private int mType;
        public final static int COLLAPSE = 1;
        public final static int EXPAND = 0;
        private LinearLayout.LayoutParams mLayoutParams;

        /**
         * Initializes expand collapse animation, has two types, collapse (1) and expand (0).
         *
         * @param view The view to animate
         * @param type The type of animation: 0 will expand from gone and 0 size to visible and layout size defined in xml.
         *             1 will collapse view and set to gone
         */
        public ExpandCollapseAnimation(View view, int type) {

            mAnimatedView = view;
            mEndHeight = mAnimatedView.getMeasuredHeight();
            int content_height = mAnimatedView.findViewById(R.id.content).getMeasuredHeight();
            if (content_height + 1 > mEndHeight) mEndHeight = content_height + 1;
            mLayoutParams = ((LinearLayout.LayoutParams) view.getLayoutParams());
            mType = type;
            if (mType == EXPAND) {

                mLayoutParams.bottomMargin = -mEndHeight;
            } else {

                mLayoutParams.bottomMargin = 0;
            }
            view.setVisibility(View.VISIBLE);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                if (mType == EXPAND) {
                    mLayoutParams.bottomMargin = -mEndHeight + (int) (mEndHeight * interpolatedTime);
                } else {
                    mLayoutParams.bottomMargin = -(int) (mEndHeight * interpolatedTime);
                }
                Log.d("ExpandCollapseAnimation", "anim height " + mLayoutParams.bottomMargin);
                mAnimatedView.requestLayout();
            } else {
                if (mType == EXPAND) {
                    mLayoutParams.bottomMargin = 0;
                    mAnimatedView.requestLayout();
                } else {
                    mLayoutParams.bottomMargin = -mEndHeight;
                    mAnimatedView.setVisibility(View.GONE);
                    mAnimatedView.requestLayout();
                }
            }
        }
    }

}
