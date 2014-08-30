package cn.scau.scautreasure.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.devspark.appmsg.AppMsg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.CardRecordAdapter;
import cn.scau.scautreasure.api.CardApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.CardRecordModel;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_SWING;

/**
 * User: special
 * Date: 13-9-21
 * Time: 下午3:55
 * Mail: specialcyci@gmail.com
 */
@EFragment(R.layout.card)
public class Card extends CommonFragment {

    @RestService
    CardApi api;
    @ViewById(R.id.listView)
    PullToRefreshListView pullListView;
    private CardRecordAdapter cardadapter;
    private SlideExpandableListAdapter exadapter;
    private int page;
    private int count;
    private ArrayList<String> startAndEndDate;
    /**
     * listView下拉刷新;
     */
    private PullToRefreshBase.OnRefreshListener onRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            if (page < Math.ceil(count / 10)) {
                page++;
                loadData(startAndEndDate);
            } else {
                AppMsg.makeText(getSherlockActivity(), R.string.tips_default_last, AppMsg.STYLE_CONFIRM).show();
                refreshView.setRefreshing(false);
            }
        }
    };
    private ActionBarActivity ctx;

    @AfterInject
    void init() {
        ctx = getSherlockActivity();
        setTitle(R.string.title_card);
        tips_empty = R.string.tips_card_null;
    }

    @AfterViews
    void initView() {
        getSherlockActivity().getSupportActionBar().show();
        pullListView.setOnRefreshListener(onRefreshListener);
        startAndEndDate = getArguments().getStringArrayList("startAndEndDate");
        UIHelper.getDialog(R.string.loading_default).show();
        loadData(startAndEndDate);
    }

    @UiThread
    void showSuccessResult(CardRecordModel.RecordList l) {

        // new search
        if (cardadapter == null) {
            count = l.getCount();
            setListViewAdapter(l.getRecords());
            String tips = getString(R.string.tips_card_record_count) + count;
            AppMsg.makeText(getSherlockActivity(), tips, AppMsg.STYLE_INFO).show();
            UIHelper.getDialog().dismiss();
        } else {
            // next page;
            cardadapter.addAll(l.getRecords());
            pullListView.onRefreshComplete();
            cardadapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
            pullListView.setRefreshing(false);
        }

        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setSubtitle(getString(R.string.title_sub_card) + l.getAmount());
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        try {

            ArrayList<String> param = (ArrayList<String>) params[0];
            CardRecordModel.RecordList records;
            if (param == null) {
                records = api.getToday(AppContext.userName, app.getEncodeCardPassword());
            } else {
                records = api.getHistory(AppContext.userName, app.getEncodeCardPassword(), param.get(0), param.get(1), page);
            }
            showSuccessResult(records);
        } catch (HttpStatusCodeException e) {
            showErrorResult(ctx, e.getStatusCode().value());
        }
    }


    private void setListViewAdapter(List<CardRecordModel> cards) {
        cardadapter = new CardRecordAdapter(getSherlockActivity(), R.layout.card_listitem, cards);
        ListView lv = pullListView.getRefreshableView();
        adapter = UIHelper.buildEffectAdapter(cardadapter, lv, EXPANDABLE_SWING);
        pullListView.setAdapter(adapter);
    }

}
