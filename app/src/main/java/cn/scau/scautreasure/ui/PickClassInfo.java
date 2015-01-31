package cn.scau.scautreasure.ui;

import android.view.View;
import android.widget.AbsListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.springframework.web.client.HttpStatusCodeException;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.PickClassAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.widget.AppProgress;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_ALPHA;

/**
 * 选课情况;
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午5:29
 * Mail:  specialcyci@gmail.com
 */
@EActivity(R.layout.pickclassinfo)
public class PickClassInfo extends ListActivity {


    @RestService
    EdusysApi api;

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
        loadData();
    }

    @AfterViews
    void init() {
        setTitleText("选课查询");
        setMoreButtonText("刷新");

        setDataEmptyTips("现在找不到你的选课情况");
        cacheHelper.setCacheKey("pickClassInfo");
        list = cacheHelper.loadListFromCache();
        buildAndShowAdapter();
        loadData();
    }


    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        beforeLoadData("正在刷新", "请耐心等待,正方你懂的", "取消", new AppProgress.Callback() {
            @Override
            public void onCancel() {
                BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);
            }
        });
        try {
            list = api.getPickClassInfo(AppContext.userName, app.getEncodeEduSysPassword()).getPickclassinfos();
            cacheHelper.writeListToCache(list);
            buildAndShowAdapter();
        } catch (HttpStatusCodeException e) {
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());

        } catch (Exception e) {
            handleNoNetWorkError();
        }
        afterLoadData();
    }

    /**
     * 构建 Adapter ， 并且刷新显示。
     */
    private void buildAndShowAdapter() {
        PickClassAdapter listadapter = new PickClassAdapter(
                this,
                R.layout.pickclassinfo_listitem, list);
        adapter = UIHelper.buildEffectAdapter(listadapter,
                (AbsListView) listView, EXPANDABLE_ALPHA);
        showSuccessResult();
    }

}