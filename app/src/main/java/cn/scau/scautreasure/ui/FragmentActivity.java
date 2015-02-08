package cn.scau.scautreasure.ui;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;


import com.devspark.appmsg.AppMsg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;


import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import org.androidannotations.annotations.rest.RestService;


import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;

import cn.scau.scautreasure.adapter.SchoolActivityPagerAdapter;
import cn.scau.scautreasure.api.SchoolActivityApi;
import cn.scau.scautreasure.helper.SchoolActivityHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.SchoolActivityModel;

import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.SchoolActivityPullToRefresh;
import cn.scau.scautreasure.widget.SchoolActivityTabWidget;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * 活动圈,
 */
@EFragment(R.layout.schoolactivity)
public class FragmentActivity extends BaseFragment {

    @RestService
    SchoolActivityApi api;

    @ViewById
    ViewPager pager;

    @ViewById
    cn.scau.scautreasure.widget.SchoolActivityTabWidget_ titles;
    @Bean
    SchoolActivityHelper helper;

    private ArrayList<View> listViews = new ArrayList<View>();
    private SchoolActivityPagerAdapter adapter;
    private SchoolActivityPullToRefresh today, tomorrow, later;
    private boolean alreadyInLoadData = false;
    private boolean isFirstTimeOpenActivity = true;

    @AfterViews
    void initViews() {
        if (!isAfterViews) {
            ((MaterialNavigationDrawer) this.getActivity()).getCurrentSection().setNotificationsText("");
            isAfterViews = true;
            System.out.println("活动圈");
            helper.initHelper(getActivity().getApplication());
            today = new SchoolActivityPullToRefresh(getActivity(), helper, "today");
            tomorrow = new SchoolActivityPullToRefresh(getActivity(), helper, "tomorrow");
            later = new SchoolActivityPullToRefresh(getActivity(), helper, "later");

            initPullToRefreshListView(today);
            initPullToRefreshListView(tomorrow);
            initPullToRefreshListView(later);

            adapter = new SchoolActivityPagerAdapter();
            adapter.setViewList(listViews);

            showTab();

            pager.setOffscreenPageLimit(3);
            pager.setOnPageChangeListener(onPageChangeListener);
            pager.setAdapter(adapter);

            showSchoolActivity();
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                demo();
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

    @UiThread(delay = 500)
    void demo() {
        today.setRefreshing();

    }


    void initPullToRefreshListView(PullToRefreshListView view) {
        view.getRefreshableView().setDivider(null);
        view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (alreadyInLoadData) {
                    refreshView.onRefreshComplete();
                } else {
                    loadData();
                }
            }
        });
        listViews.add(view);
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

    /**
     * 展示网络加载异常结果
     *
     * @param requestCode
     */
    @UiThread
    void showError(int requestCode) {
        if (requestCode == 404) {
            AppToast.error(getActivity(), "网路异常,连接不到服务器");
        } else {
            app.showError(requestCode, getActivity());
        }
    }

    @UiThread
    void showSchoolActivity() {
        today.buildSchoolActivityAdapter();
        tomorrow.buildSchoolActivityAdapter();
        later.buildSchoolActivityAdapter();
    }

    @UiThread
    void stopPullToRefreshListView() {
        if (today.isRefreshing()) today.onRefreshComplete();
        if (tomorrow.isRefreshing()) tomorrow.onRefreshComplete();
        if (later.isRefreshing()) later.onRefreshComplete();
    }


    /**
     * 线程从服务器加载校园活动，同时保存在本地数据库，再行操作;
     * 因为有三个pullToRefreshListView,所以要一个alreadyInLoadData这样的boolean来
     * 判断是否有重复刷新操作。
     */
    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        if (alreadyInLoadData) return;
        long lastUpdate = app.config.lastUpdated().get();
        if (!isFirstTimeOpenActivity && System.currentTimeMillis() / 1000 - lastUpdate < 10) {
            Log.e("frequently", "lastUpdate=" + lastUpdate + ",current=" + System.currentTimeMillis() / 1000);
            Log.e(getClass().getName(), "刷新频繁");
            stopPullToRefreshListView();
            return;
        }
        try {
            alreadyInLoadData = true;
            SchoolActivityModel.ActivityList lists = api.getSchoolActivity(lastUpdate);
            List<SchoolActivityModel> content = lists.getContent();
            if (content != null && content.size() != 0) {
                /*for (int i = 0; i < content.size(); i++) {
                    content.get(i).setIsNewOne(true);
                }*/
                helper.addSchoolActivity(lists);
                //helper.setLastUpdate(System.currentTimeMillis() / 1000);
                app.config.lastUpdated().put(System.currentTimeMillis() / 1000);
                app.config.lastRedPoint().put(System.currentTimeMillis() / 1000);
                showSchoolActivity();
            } else {
                Log.i(getClass().getName(), "无更新");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            handleNoNetWorkError(getActivity());
        } finally {
            if (isFirstTimeOpenActivity) isFirstTimeOpenActivity = false;
            alreadyInLoadData = false;
            stopPullToRefreshListView();
        }
    }

}
