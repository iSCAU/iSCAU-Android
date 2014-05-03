package cn.scau.scautreasure.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import cn.scau.scautreasure.model.ParamModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.widget.ParamWidget;
import cn.scau.scautreasure.widget.ParamWidget_;

/**
 * 条件选择
 * User:  Special Leung
 * Date:  13-8-15
 * Time:  下午4:13
 * Mail:  specialcyci@gmail.com
 */
@EFragment(R.layout.param)
public class Param extends Common implements ServerOnChangeListener {

    @App  AppContext app;
    @RestService EdusysApi api;
    @ViewById LinearLayout linear_parent;

    private String target;
    private String targetFragment;
    private List<ParamWidget> wheelList;
    private ArrayList<ParamModel> paramList;

    @AfterInject
    void init(){
        wheelList = new ArrayList<ParamWidget>();
        UIHelper.getDialog(R.string.tips_loading_params).show();
        getFragmentArguments();
        loadData();
    }

    private void getFragmentArguments(){
        target         = getArguments().getString("target");
        targetFragment = getArguments().getString("targetFragment");
    }

    @AfterViews
    void initActionBar(){
        super.initActionBar();
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setTitle(R.string.title_params);
    }

    /**
     * 继续查询按钮点击事件;
     */
    @Click
    void btn_continue(){
        try {
            startDetailFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startDetailFragment() throws Exception {
        Class cls =  Class.forName(targetFragment);
        Fragment fragment = (Fragment) cls.newInstance();
        UIHelper.addFragment(getSherlockActivity(),fragment,"value", buildParamsValue());
    }

    private ArrayList<String> buildParamsValue(){
        ArrayList<String> value = new ArrayList<String>();
        for(int i = 0 ; i < wheelList.size(); i++)
            value.add(wheelList.get(i).getSelectedParam());
        return value;
    }

    /**
     * 动态生成参数选择wheel控件;
     */
    @UiThread
    void showParams(){
        UIHelper.getDialog().dismiss();
        wheelList.clear();
        for(ParamModel p : paramList){
            ParamWidget paramWidget = buildParamViews(p.getKey(),p.getValue());
            linear_parent.addView(paramWidget);
        }
    }

    private ParamWidget buildParamViews(String key,String[] values){
        ParamWidget paramWidget = ParamWidget_.build(getSherlockActivity());
        paramWidget.initView(key,values,wheelList.size());
        wheelList.add(paramWidget);
        return paramWidget;
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        paramList = getCacheParamsList();
        // load param from cache, load from network if not existed
        if(paramList == null){
            try{
                paramList = api.getParams(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server, target).getParams();
                saveCacheParamsList(paramList);
            }catch (HttpStatusCodeException e){
                showErrorResult(parentActivity(), e.getStatusCode().value(), this);
                return;
            }
        }
        showParams();
    }

    private ArrayList<ParamModel> getCacheParamsList(){
        CacheUtil cacheUtil = CacheUtil.get(getSherlockActivity());
        String cacheKey = getCacheKey();
        return (ArrayList<ParamModel>) cacheUtil.getAsObject(cacheKey);
    }

    private void saveCacheParamsList(ArrayList<ParamModel> paramList){
        CacheUtil cacheUtil = CacheUtil.get(getSherlockActivity());
        if(paramList.size() != 0)
            cacheUtil.put(getCacheKey(),paramList, AppConstant.PARAMS_CACHE_TIME);
    }

    private String getCacheKey(){
        return "param_" + target;
    }

    @Override
    public void onChangeServer() {
        UIHelper.getDialog(R.string.tips_loading_params).show();
        loadData();
    }

}