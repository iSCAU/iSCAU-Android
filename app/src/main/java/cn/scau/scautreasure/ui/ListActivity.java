package cn.scau.scautreasure.ui;

import android.widget.BaseAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.widget.AppProgress;
import cn.scau.scautreasure.widget.RefreshActionItem;

/**
 * Created by macroyep on 15/1/19.
 * Time:21:51
 */
@EActivity
public class ListActivity extends BaseActivity {
    protected static final int QUERY_FOR_EDUSYS = 0;
    private int queryTarget = QUERY_FOR_EDUSYS;
    protected static final int QUERY_FOR_LIBRARY = 1;
    protected static final int QUERY_FOR_BOOK = 2;

    @ViewById
    ListView listView;

    protected ArrayList list;
    @ViewById
    PullToRefreshListView pullListView;

    protected BaseAdapter adapter;

    /**
     * 检查查询目标账号的可用性，主要是检查
     * 有没有保存账号。
     */
    @AfterViews
    void checkAccountAvailable() {
        if(this instanceof Food){
            return;
        }
        boolean startLoginActivity = false;
        if (queryTarget == QUERY_FOR_EDUSYS) {
            startLoginActivity = (app.eduSysPassword == null || app.eduSysPassword.equals(""));
        } else if (queryTarget == QUERY_FOR_LIBRARY) {
            startLoginActivity = (app.libPassword == null || app.libPassword.equals(""));
        }

        if (startLoginActivity) {
            int startTips = queryTarget == QUERY_FOR_EDUSYS ?
                    R.string.start_tips_edusys : R.string.start_tips_library;
            Login_.intent(this).startTips(getString(startTips)).start();
            this.finish();
        } else {
            if (queryTarget != QUERY_FOR_BOOK)
                loadData();
        }
    }

    @Background
    void loadData(Object... params) {

    }

    /**
     * 设置需要查询的目标。
     *
     * @param target
     */
    protected void setQueryTarget(int target) {
        queryTarget = target;
    }

    /**
     * 展示查询结果;
     */
    @UiThread
    void showSuccessResult() {
        ((ListView) listView).setAdapter(adapter);
    }

    @UiThread
    void beforeLoadData(String title, String msg, String btn_text, AppProgress.Callback callback) {
        AppProgress.show(this, title, msg, btn_text, callback);
    }

    @UiThread
    void afterLoadData() {
        AppProgress.hide();
    }
}
