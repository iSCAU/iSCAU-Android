package cn.scau.scautreasure.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * User: special
 * Date: 13-8-29
 * Time: 上午10:43
 * Mail: specialcyci@gmail.com
 */
public class ClassTableAdapter extends PagerAdapter {

    private ArrayList<View> classListViews = new ArrayList<View>();

    public void setViewList(ArrayList<View> viewList) {
        this.classListViews.clear();
        this.classListViews.addAll(viewList);
    }

    @Override
    public int getCount() {
        return classListViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(classListViews.get(arg1));
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(classListViews.get(arg1),
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        return classListViews.get(arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }

}
