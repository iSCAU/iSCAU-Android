package cn.scau.scautreasure.ui;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.devspark.appmsg.AppMsg;

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

import cn.scau.scautreasure.model.KeyValueModel;
import cn.scau.scautreasure.model.ParamModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.widget.AppProgress;

import cn.scau.scautreasure.widget.RichButton;


@EActivity(R.layout.param)
public class Param extends BaseActivity {

    @App
    AppContext app;
    @RestService
    EdusysApi api;
    @ViewById
    LinearLayout linear_parent;

    @Extra("target")
    String target;

    @Extra("targetActivity")
    String targetActivity;
    private List<RichButton> buttonList;
    private ArrayList<ParamModel> paramList;
    //是否开学
    private boolean isStartStudy = true;

    @AfterInject
    void init() {
        buttonList = new ArrayList<RichButton>();
        AppProgress.show(this, "加载条件", "第一次加载可能比较慢，请耐心等待", "取消", new AppProgress.Callback() {
            @Override
            public void onCancel() {
                finish();
            }
        });
        loadData();
    }

    @Extra
    String _title;

    @Override
    @AfterViews
    void initView() {
        setTitleText(_title);
        setMoreButtonText("下一步");
    }

    /**
     * 继续查询按钮点击事件;
     */
    @Click(R.id.more)
    void btn_continue() {
        try {
            startNextActivity();
                /*if(target.equals("emptyClassRoom")&&!isStartStudy){
                    AppMsg.makeText(this,"现在还没开学，无法查询空课室",AppMsg.STYLE_ALERT).show();
                }else{

            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过函数反射手段实例出目标的Activity并且传递参数，
     * 最后运行之
     *
     * @throws Exception
     */
    private void startNextActivity() throws Exception {
        Class cls = Class.forName(targetActivity);

        Method buildMethod = cls.getMethod("intent", Context.class);
        Object obj = buildMethod.invoke(cls, this);

        Method putMethod = obj.getClass().getMethod("value", ArrayList.class);
        obj = putMethod.invoke(obj, buildParamsValue());

        Method startMethod = obj.getClass().getMethod("start");
        startMethod.invoke(obj);
    }

    private ArrayList<String> buildParamsValue() {
        ArrayList<String> value = new ArrayList<String>();
        for (int i = 0; i < buttonList.size(); i++) {
            value.add(buttonList.get(i).getTv_subtitle().getText().toString());
            Log.i(getClass().getName(), buttonList.get(i).getTv_subtitle().getText().toString());
        }
        return value;
    }

    /**
     * 动态生成参数选择wheel控件;
     */
    @UiThread
    void showParams() {
        AppProgress.hide();
        buttonList.clear();
        for (ParamModel p : paramList) {
            List<KeyValueModel> keyList = new ArrayList<KeyValueModel>();
            for (String key : p.getValue()) {
                keyList.add(new KeyValueModel(key, 0));
            }
            RichButton richButton = new RichButton(this, keyList, callback);
            richButton.getTv_title().setText(p.getKey());
            richButton.getTv_subtitle().setText(p.getValue()[0]);
            buttonList.add(richButton);
            linear_parent.addView(richButton);

        }
    }

    private RichButton.Callback callback = new RichButton.Callback() {
        @Override
        public void afterItemClickListener(KeyValueModel model) {
            Log.i(getClass().getName(), model.toString());
        }
    };


    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        paramList = getCacheParamsList();
        // load param from cache, load from network if not existed
        if (paramList == null) {
            try {
                paramList = api.getParams(AppContext.userName, app.getEncodeEduSysPassword(), target).getParams();
                saveCacheParamsList(paramList);
            } catch (HttpStatusCodeException e) {
                showErrorResult(e.getStatusCode().value());
                hideProgressBar();
                return;
            } catch (Exception e) {
                handleNoNetWorkError();
                hideProgressBar();
            }
        }
        showParams();
    }

    @UiThread
    void hideProgressBar() {
        AppProgress.hide();
    }

    private ArrayList<ParamModel> getCacheParamsList() {
        CacheUtil cacheUtil = CacheUtil.get(this);
        String cacheKey = getCacheKey();
        return (ArrayList<ParamModel>) cacheUtil.getAsObject(cacheKey);
    }

    private void saveCacheParamsList(ArrayList<ParamModel> paramList) {
        CacheUtil cacheUtil = CacheUtil.get(this);
        if (paramList.size() != 0)
            cacheUtil.put(getCacheKey(), paramList, AppConstant.PARAMS_CACHE_TIME);
    }

    private String getCacheKey() {
        return "param_" + target;
    }


}