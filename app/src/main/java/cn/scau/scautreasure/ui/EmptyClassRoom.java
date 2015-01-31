package cn.scau.scautreasure.ui;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.ClassRoomAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.util.StringUtil;
import cn.scau.scautreasure.widget.AppProgress;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_SWING;


@EActivity(R.layout.emptyclassroom)
public class EmptyClassRoom extends ListActivity {

    @RestService
    EdusysApi api;
    @Extra("value")
    ArrayList<String> value;

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
        loadData();
    }

    @AfterViews
    void init() {
        setTitleText("空教室");
        setMoreButtonText("刷新");

        setDataEmptyTips("没有这样的空教室");
        cacheHelper.setCacheKey("emptyClassRoom_" + StringUtil.join(value, "_"));
        list = cacheHelper.loadListFromCache();
        buildAndShowListViewAdapter();
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
            ArrayList<String> param = value;
            for (int i = 0; i < param.size(); i++) {
            }
            list = api.getEmptyClassRoom(AppContext.userName, app.getEncodeEduSysPassword(),
                    param.get(0), param.get(1), param.get(2), param.get(3), param.get(4),
                    param.get(5), param.get(6)).getClassRooms();
            cacheHelper.writeListToCache(list);
            buildAndShowListViewAdapter();


        } catch (HttpStatusCodeException e) {
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            handleNoNetWorkError();

        }
        afterLoadData();
    }

    private void buildAndShowListViewAdapter() {
        ClassRoomAdapter cAdapter = new ClassRoomAdapter(this, R.layout.emptyclassroom_listitem, list);
        adapter = UIHelper.buildEffectAdapter(cAdapter, (AbsListView) listView, EXPANDABLE_SWING);
        showSuccessResult();
    }

}