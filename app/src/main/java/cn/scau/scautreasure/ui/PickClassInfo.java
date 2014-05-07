package cn.scau.scautreasure.ui;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.PickClassAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import cn.scau.scautreasure.widget.RefreshActionItem;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_ALPHA;

/**
 * 选课情况;
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午5:29
 * Mail:  specialcyci@gmail.com
 */
@EActivity( R.layout.pickclassinfo )
public class PickClassInfo extends CommonActivity implements ServerOnChangeListener, RefreshActionItem.RefreshButtonListener {


    @RestService
    EdusysApi api;

    private RefreshActionItem mRefreshActionItem;

    @AfterViews
    void init(){
        setTitle(R.string.title_pickclassinfo);
        setDataEmptyTips(R.string.tips_pickclassinfo_null);
        setCacheKey("pickClassInfo");
        loadListFromCache();
        buildAndShowAdapter();
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        beforeLoadData();
        try{
            list = api.getPickClassInfo(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server).getPickclassinfos();
            writeListToCache();
            buildAndShowAdapter();
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(),this);
        }catch (Exception e){
            handleNoNetWorkError(getSherlockActivity());
        }
        afterLoadData();
    }

    @UiThread
    void beforeLoadData() {
        mRefreshActionItem.startProgress();
    }

    @UiThread
    void afterLoadData(){
        mRefreshActionItem.stopProgress();
    }

    @Override
    public void onChangeServer() {
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pickcourseinfo, menu);
        MenuItem item = menu.findItem(R.id.refresh_button);
        mRefreshActionItem = (RefreshActionItem) item.getActionView();
        mRefreshActionItem.setMenuItem(item);
        mRefreshActionItem.setRefreshButtonListener(this);
        loadData();
        return true;
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

    /**
     * 构建 Adapter ， 并且刷新显示。
     *
     */
    private void buildAndShowAdapter(){
        PickClassAdapter listadapter = new PickClassAdapter(
                getSherlockActivity(),
                R.layout.pickclassinfo_listitem, list);
        adapter = UIHelper.buildEffectAdapter(listadapter,
                (AbsListView) listView,EXPANDABLE_ALPHA);
        showSuccessResult();
    }

}