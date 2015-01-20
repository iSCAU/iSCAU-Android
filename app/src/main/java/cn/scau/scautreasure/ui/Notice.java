package cn.scau.scautreasure.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.devspark.appmsg.AppMsg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.NoticeAdapter;
import cn.scau.scautreasure.api.NoticeApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.NoticeModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.widget.NoticeHeaderWidget_;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;

/**
 * 校园通知列表;
 * User: special
 * Date: 13-8-27
 * Time: 下午2:14
 * Mail: specialcyci@gmail.com
 */
@EActivity(R.layout.notice)
public class Notice extends CommonActivity {
//
//    private final static String cacheKey = "notice_lastest_news";
//    @RestService
//    NoticeApi api;
//    @ViewById
//    ImageView notice_iv;
//    @ViewById(R.id.listView)
//    PullToRefreshListView _listView;
//    @ViewById
//    SwipeRefreshLayout swipe_refresh;
//    private int page;
//    private int count;
//    /**
//     * listView下拉刷新;
//     */
//    private PullToRefreshBase.OnRefreshListener onRefreshListener = new PullToRefreshBase.OnRefreshListener() {
//        @Override
//        public void onRefresh(PullToRefreshBase refreshView) {
//            if (page < Math.ceil(count / 10)) {
//                page++;
//                loadData();
//            } else {
//                AppMsg.makeText(getSherlockActivity(), R.string.tips_default_last, AppMsg.STYLE_CONFIRM).show();
//                refreshView.setRefreshing(false);
//            }
//        }
//    };
//    private NoticeAdapter listAdapter;
//    private boolean isFromCache = true;
//    /**
//     * listView选中：
//     */
//    private AdapterView.OnItemClickListener onListViewItemClicked = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
//            app.Log("position : " + position);
//            if (position != 1) {
//                NoticeModel notice = (NoticeModel) parent.getAdapter().getItem(position);
//                NoticeDetail_.intent(getSherlockActivity())
//                        .title(notice.getTitle())
//                        .time(notice.getTime())
//                        .url(notice.getUrl())
//                        .start();
//            }
//        }
//    };
//
//    @Override
//    void initActionBar() {
//        // 由于隐藏了标题栏，所以要覆盖初始化actionbar的函数
//        // 否则空指针
//    }
//
//    @AfterViews
//    void init() {
//
//        getSupportActionBar().hide();
//
//        View header = NoticeHeaderWidget_.build(getSherlockActivity());
//
//        _listView.setOnRefreshListener(onRefreshListener);
//        _listView.setOnItemClickListener(onListViewItemClicked);
//        _listView.getRefreshableView().addHeaderView(header);
//
//        setSwipeRefresh();
//        loadCacheData();
//
//        swipe_refresh.setRefreshing(true);
//        page = 1;
//        loadData();
//    }
//
//    private void setSwipeRefresh() {
//        swipe_refresh.setEnabled(false);
//        // 顶部刷新的样式
//        swipe_refresh.setColorScheme(R.color.swipe_refresh_1,
//                R.color.swipe_refresh_2,
//                R.color.swipe_refresh_3,
//                R.color.swipe_refresh_4);
//    }
//
//    // 加载缓存的通知，并且载入时显示；
//    void loadCacheData() {
//        CacheUtil cacheUtil = CacheUtil.get(getSherlockActivity());
//        NoticeModel.NoticeList l = (NoticeModel.NoticeList) cacheUtil.getAsObject(this.cacheKey);
//        if (l != null)
//            showSuccessResult(l);
//    }
//
//    @UiThread
//    void showSuccessResult(NoticeModel.NoticeList l) {
//
//        // new search
//        if (listAdapter == null) {
//
//            count = l.getCount();
//            listAdapter = new NoticeAdapter(getSherlockActivity(), R.layout.notice_listitem, l.getNotice());
//            ListView lv = _listView.getRefreshableView();
//            adapter = UIHelper.buildEffectAdapter(listAdapter, lv, ALPHA);
//            _listView.setAdapter(listAdapter);
//            swipe_refresh.setRefreshing(false);
//
//            if (isFromCache) {
//                // 置空adapter, 可以使得下面从网络加载数据后，自动清除
//                // 缓存的数据，置入从网络加载的数据；
//                listAdapter = null;
//                isFromCache = false;
//            }
//        } else {
//            // next page;
//            listAdapter.addAll(l.getNotice());
//            listAdapter.notifyDataSetChanged();
//            adapter.notifyDataSetChanged();
//        }
//
//        _listView.onRefreshComplete();
//    }
//
//    @Background(id = UIHelper.CANCEL_FLAG)
//    void loadData(Object... params) {
//
//        try {
//            NoticeModel.NoticeList l = api.getList(page);
//            cacheLastestNotice(l);
//            showSuccessResult(l);
//        } catch (HttpStatusCodeException e) {
//            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
//        } catch (Exception e) {
//            handleNoNetWorkError(getSherlockActivity());
//        }
//
//    }
//
//    // 缓存最新的通知信息;
//    @Background
//    void cacheLastestNotice(NoticeModel.NoticeList noticeList) {
//        // 只缓存最新的通知
//        if (page != 1)
//            return;
//        CacheUtil cacheUtil = CacheUtil.get(getSherlockActivity());
//        if (noticeList.getCount() != 0)
//            cacheUtil.put(this.cacheKey, noticeList);
//    }

}