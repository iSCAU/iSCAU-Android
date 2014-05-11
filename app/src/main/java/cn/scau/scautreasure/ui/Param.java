package cn.scau.scautreasure.ui;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import java.lang.reflect.Method;
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
@EActivity(R.layout.param)
public class Param extends CommonActivity implements ServerOnChangeListener {

    @App  AppContext app;
    @RestService EdusysApi api;
    @ViewById LinearLayout linear_parent;

    @Extra("target")
    String target;
    @Extra("targetActivity")
    String targetActivity;
    private List<ParamWidget> wheelList;
    private ArrayList<ParamModel> paramList;

    @AfterInject
    void init(){
        wheelList = new ArrayList<ParamWidget>();
        UIHelper.getDialog(R.string.tips_loading_params).show();
        loadData();
    }

    @Override
    @AfterViews
    void initActionBar(){
        super.initActionBar();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_params);
    }

    /**
     * 继续查询按钮点击事件;
     */
    @Click
    void btn_continue(){
        try {
            startNextActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过函数反射手段实例出目标的Activity并且传递参数，
     *  最后运行之
     *
     * @throws Exception
     */
    private void startNextActivity() throws Exception {
        Class cls =  Class.forName(targetActivity);

        Method buildMethod = cls.getMethod("intent", Context.class);
        Object obj = buildMethod.invoke(cls, this);

        Method putMethod   = obj.getClass().getMethod("value", ArrayList.class);
        obj = putMethod.invoke(obj, buildParamsValue());

        Method startMethod = obj.getClass().getMethod("start");
        startMethod.invoke(obj);
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
        paramWidget.setSeparatorVisable(View.VISIBLE);
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
                showErrorResult(this, e.getStatusCode().value(), this);
                return;
            }catch (Exception e){
                handleNoNetWorkError(getSherlockActivity());
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