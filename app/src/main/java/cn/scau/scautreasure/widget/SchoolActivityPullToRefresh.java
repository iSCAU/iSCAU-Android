package cn.scau.scautreasure.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.scau.scautreasure.adapter.SchoolActivityListAdapter;
import cn.scau.scautreasure.helper.SchoolActivityHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.SchoolActivityModel;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_SWING;

/**
 * Created by stcdasqy on 2014-08-12.
 */
public class SchoolActivityPullToRefresh extends PullToRefreshListView {
    private SchoolActivityHeaderWidget header;
    private SchoolActivityHelper helper;
    private Context mContext;
    private String when;

    public SchoolActivityPullToRefresh(Context ctx, SchoolActivityHelper helper, String when) {
        super(ctx);
        mContext = ctx;
        this.helper = helper;
        this.when = when;
        header = SchoolActivityHeaderWidget_.build(ctx);
        header.setUp(when);
        getRefreshableView().addHeaderView(header);
    }

    public void buildSchoolActivityAdapter() {
        List<SchoolActivityModel> lists;
        if ("today".equals(when)) lists = helper.getToday();
        else if ("tomorrow".equals(when)) lists = helper.getTomorrow();
        else lists = helper.getLater();
        if (lists.size() == 0) {
            header.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lp = header.getLayoutParams();
            if (lp != null) {
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                header.setLayoutParams(lp);
            }
        } else {
            header.setVisibility(View.GONE);
            //ViewGroup.LayoutParams lp = header.getLayoutParams();
            header.gone();
        }
        ListView _listView = getRefreshableView();
        SchoolActivityListAdapter listAdapter = new SchoolActivityListAdapter(mContext, helper);
        listAdapter.addAll(lists);
        BaseAdapter _adapter = UIHelper.buildEffectAdapter(listAdapter, _listView, EXPANDABLE_SWING);
        setAdapter(_adapter);
    }
}
