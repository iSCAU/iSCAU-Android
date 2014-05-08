package cn.scau.scautreasure.ui;

import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import cn.scau.scautreasure.widget.RefreshActionItem;

/**
 * 可查询 Activity 的共享上层。
 */
@EActivity
public class CommonQueryActivity extends CommonActivity implements ServerOnChangeListener, RefreshActionItem.RefreshButtonListener{

    protected static final int QUERY_FOR_EDUSYS = 0;
    protected static final int QUERY_FOR_LIBRARY = 1;
    private int queryTarget = QUERY_FOR_EDUSYS;
    protected RefreshActionItem mRefreshActionItem;

    /**
     * 建立这个加载虚函数，具体由下层实现。
     */
    void loadData(Object... param){

    }

    /**
     * 检查查询目标账号的可用性，主要是检查
     *  有没有保存账号。
     */
    @AfterViews
    void checkAccountAvailable(){
        boolean startLoginActivity = false;
        if (queryTarget == QUERY_FOR_EDUSYS){
            startLoginActivity =  (app.eduSysPassword == null || app.eduSysPassword.equals(""));
        }else if (queryTarget == QUERY_FOR_LIBRARY){
            startLoginActivity =  (app.libPassword == null || app.libPassword.equals(""));
        }

        if (startLoginActivity) {
            int startTips = queryTarget == QUERY_FOR_EDUSYS ?
                    R.string.start_tips_edusys : R.string.start_tips_library;
            Login_.intent(this).startTips(getString(startTips)).start();
            this.finish();
        }
    }

    @UiThread
    void beforeLoadData() {
        mRefreshActionItem.startProgress();
    }

    @UiThread
    void afterLoadData(){
        mRefreshActionItem.stopProgress();
    }

    /**
     * 点击 Action Bar 的刷新事件。
     *
     * @param refreshActionItem
     */
    @Override
    public void onRefresh(RefreshActionItem refreshActionItem) {
        loadData();
    }

    @Override
    public void onChangeServer() {
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh_item, menu);
        MenuItem item = menu.findItem(R.id.refresh_button);
        mRefreshActionItem = (RefreshActionItem) item.getActionView();
        mRefreshActionItem.setMenuItem(item);
        mRefreshActionItem.setRefreshButtonListener(this);
        loadData();
        return true;
    }

    /**
     * 设置需要查询的目标。
     *
     * @param target
     */
    protected void setQueryTarget(int target){
        queryTarget = target;
    }

}
