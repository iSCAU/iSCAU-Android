package cn.scau.scautreasure.ui;

import android.view.MenuItem;

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

    protected RefreshActionItem mRefreshActionItem;

    /**
     * 建立这个加载虚函数，具体由下层实现。
     */
    void loadData(Object... param){

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

}
