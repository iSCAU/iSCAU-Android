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

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.PickClassAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import cn.scau.scautreasure.util.CacheUtil;
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
    private static final String cacheKey = "pickClassInfo";

    @AfterViews
    void init(){
        setTitle(R.string.title_pickclassinfo);
        setDataEmptyTips(R.string.tips_pickclassinfo_null);
        loadCacheData();
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{
            list = api.getPickClassInfo(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server).getPickclassinfos();
            writeCacheData((ArrayList) list);
            buildAndShowAdapter(list);
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(),this);
        }catch (Exception e){
            handleNoNetWorkError(getSherlockActivity());
        }
        stopActionBarItemRefreing();
    }

    @Override
    public void onChangeServer() {
        UIHelper.getDialog(R.string.loading_pickclassinfo).show();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pickcourseinfo, menu);
        MenuItem item = menu.findItem(R.id.refresh_button);
        mRefreshActionItem = (RefreshActionItem) item.getActionView();
        mRefreshActionItem.setMenuItem(item);
        mRefreshActionItem.setRefreshButtonListener(this);
        mRefreshActionItem.startProgress();
        loadData();
        return true;
    }


    /**
     * 停止 Action Bar Item 滚动。
     */
    @UiThread
    void stopActionBarItemRefreing(){
        mRefreshActionItem.stopProgress();
    }

    /**
     * 点击 Action Bar 的刷新事件。
     *
     * @param refreshActionItem
     */
    @Override
    public void onRefresh(RefreshActionItem refreshActionItem) {
        mRefreshActionItem.startProgress();
        loadData();
    }


    /**
     * 构建 Adapter ， 并且刷新显示。
     * @param l
     */
    private void buildAndShowAdapter(List l){
        PickClassAdapter listadapter = new PickClassAdapter(getSherlockActivity(), R.layout.pickclassinfo_listitem, l);
        adapter = UIHelper.buildEffectAdapter(listadapter, (AbsListView) listView,EXPANDABLE_ALPHA);
        showSuccessResult();
    }

    /**
     * 从硬盘加载缓存， 并且将数据显示出来。
     */
    void loadCacheData(){
        CacheUtil cacheUtil = CacheUtil.get(getSherlockActivity());
        ArrayList l = ( ArrayList ) cacheUtil.getAsObject(cacheKey);
        if ( l != null)
            buildAndShowAdapter(l);
    }

    /**
     * 将数据写入到硬盘。
     *
     * @param list
     */
    @Background
    void writeCacheData(ArrayList list){
        CacheUtil cacheUtil = CacheUtil.get(getSherlockActivity());
        cacheUtil.put(cacheKey, list);
    }
}