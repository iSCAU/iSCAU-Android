package cn.scau.scautreasure.ui;

import android.widget.AbsListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.PickClassAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.ServerOnChangeListener;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_ALPHA;

/**
 * 选课情况;
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午5:29
 * Mail:  specialcyci@gmail.com
 */
@EActivity( R.layout.pickclassinfo )
public class PickClassInfo extends CommonActivity implements ServerOnChangeListener{

    @RestService
    EdusysApi api;

    @AfterViews
    void init(){
        setTitle(R.string.title_pickclassinfo);
        setDataEmptyTips(R.string.tips_pickclassinfo_null);

        UIHelper.getDialog(R.string.loading_pickclassinfo).show();
        loadData();
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{
            list = api.getPickClassInfo(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server).getPickclassinfos();
            PickClassAdapter listadapter = new PickClassAdapter(getSherlockActivity(), R.layout.pickclassinfo_listitem, list);

            adapter = UIHelper.buildEffectAdapter(listadapter, (AbsListView) listView,EXPANDABLE_ALPHA);
            showSuccessResult();
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(),this);
        }
    }

    @Override
    public void onChangeServer() {
        UIHelper.getDialog(R.string.loading_pickclassinfo).show();
        loadData();
    }
}