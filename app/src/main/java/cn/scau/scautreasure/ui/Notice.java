package cn.scau.scautreasure.ui;

import android.view.View;
import android.widget.*;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.NoticeAdapter;
import cn.scau.scautreasure.api.NoticeApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.NoticeModel;
import cn.scau.scautreasure.widget.NoticeHeaderWidget_;
import com.devspark.appmsg.AppMsg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;
import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.*;

/**
 * 校园通知列表;
 * User: special
 * Date: 13-8-27
 * Time: 下午2:14
 * Mail: specialcyci@gmail.com
 */
@EFragment ( R.layout.notice )
public class Notice extends Common {

    @RestService
    NoticeApi api;

    @ViewById
    ImageView notice_iv;

    @ViewById( R.id.listView )
    PullToRefreshListView _listView;

    private int page;

    private int count;

    private NoticeAdapter listAdapter;

    @AfterViews
    void init(){
        getSherlockActivity().getSupportActionBar().hide();

        View header = NoticeHeaderWidget_.build(getSherlockActivity());

        _listView.setOnRefreshListener(onRefreshListener);
        _listView.setOnItemClickListener(onListViewItemClicked);
        _listView.getRefreshableView().addHeaderView(header);

        page = 1;
        UIHelper.getDialog(R.string.tips_notice_loading).show();
        loadData();
    }

    @Override
    public void onDestroyView() {
        getSherlockActivity().getSupportActionBar().show();
        super.onDestroyView();
    }

    /**
     * listView选中：
     */
    private AdapterView.OnItemClickListener onListViewItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
            app.Log("position : " + position );
            if(position != 1) {
                NoticeModel notice = (NoticeModel) parent.getAdapter().getItem(position);
                NoticeDetail_.intent(getSherlockActivity())
                        .title(notice.getTitle())
                        .time(notice.getTime())
                        .url(notice.getUrl())
                        .start();
            }
        }
    };

    /**
     * listView下拉刷新;
     */
    private PullToRefreshBase.OnRefreshListener onRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            if(page < Math.ceil(count/10)){
                page++;
                loadData();
            }else{
                AppMsg.makeText(getSherlockActivity(), R.string.tips_default_last, AppMsg.STYLE_CONFIRM).show();
                refreshView.setRefreshing(false);
            }
        }
    };

    @UiThread
    void showSuccessResult(NoticeModel.NoticeList l){

        // new search
        if(listAdapter == null){

            count = l.getCount();
            listAdapter = new NoticeAdapter(getSherlockActivity(), R.layout.notice_listitem,l.getNotice());

            ListView lv = _listView.getRefreshableView();
            adapter     = UIHelper.buildEffectAdapter(listAdapter, lv, ALPHA);

            _listView.setAdapter(listAdapter);
            UIHelper.getDialog().dismiss();
        }else{
            // next page;
            listAdapter.addAll(l.getNotice());
            _listView.onRefreshComplete();

            listAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {

        try{
            NoticeModel.NoticeList l = api.getList(page);
            showSuccessResult(l);
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }

    }
}