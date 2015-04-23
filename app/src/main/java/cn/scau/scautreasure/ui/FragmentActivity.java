package cn.scau.scautreasure.ui;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.view.View;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;


import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


import java.util.ArrayList;

import cn.scau.scautreasure.R;

import cn.scau.scautreasure.adapter.SchoolActivityPagerAdapter;
import cn.scau.scautreasure.helper.SchoolActivityHelper;

import cn.scau.scautreasure.widget.MacroWebViewRefresh5;
import cn.scau.scautreasure.widget.SchoolActivityTabWidget;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * 活动圈,
 */
@EFragment(R.layout.schoolactivity)
public class FragmentActivity extends BaseFragment {

    @ViewById
    ViewPager pager;

    @ViewById
    cn.scau.scautreasure.widget.SchoolActivityTabWidget_ titles;
    @Bean
    SchoolActivityHelper helper;

    private ArrayList<View> browsers = new ArrayList<View>();
    private SchoolActivityPagerAdapter adapter;
    private MacroWebViewRefresh5 today, tomorrow, later;

    @AfterViews
    void initViews() {
        if (!isAfterViews) {
            ((MaterialNavigationDrawer) this.getActivity()).getCurrentSection().setNotificationsText("");
            isAfterViews = true;
            System.out.println("活动圈");
            helper.initHelper(getActivity().getApplication());
            today = new MacroWebViewRefresh5(getActivity(), "file:///android_asset/app/activity/today.html");
            tomorrow = new MacroWebViewRefresh5(getActivity(), "file:///android_asset/app/activity/index.html");
            later = new MacroWebViewRefresh5(getActivity(), "file:///android_asset/app/activity/index.html");


            browsers.add(today);
            browsers.add(tomorrow);
            browsers.add(later);


            adapter = new SchoolActivityPagerAdapter();
            adapter.setViewList(browsers);

            showTab();

            pager.setOffscreenPageLimit(3);
            pager.setOnPageChangeListener(onPageChangeListener);
            pager.setAdapter(adapter);

        }
    }

    /*
     * 这里因为种种原因要这么做，getWidth=0是没办法避免的事情，只好等待
     * 另外在titles成功changeTab到 <今天> 这个标签栏之后，再设置相应的listener。
     */
    @UiThread(delay = 30)
    void showTab() {
        if (titles.getWidth() == 0) showTab();
        else {
            titles.changeTab(0);
            titles.setListener(onTabChangeListener);
        }
    }


    /**
     * 标签的点击,同时viewPager设置到相应位置；
     */
    private SchoolActivityTabWidget.onTabChangeListener onTabChangeListener = new SchoolActivityTabWidget.onTabChangeListener() {
        @Override
        public void change(int posistion) {
            pager.setCurrentItem(posistion);
        }
    };

    /**
     * viewPager滑动监听,同时同步上方的tab位置;
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            titles.changeTab(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


}
