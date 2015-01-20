package cn.scau.scautreasure.ui;


import android.widget.AbsListView;


import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;

import cn.scau.scautreasure.adapter.BorrowedBookAdapter_;
import cn.scau.scautreasure.api.LibraryApi;
import cn.scau.scautreasure.helper.UIHelper;

import cn.scau.scautreasure.widget.AppProgress;
import cn.scau.scautreasure.widget.AppToast;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;


@EActivity(R.layout.borrowedbook)
public class BorrowedBook extends ListActivity {

    @RestService
    LibraryApi api;

    @Extra("target")
    int target;

    @AfterInject
    void initAfterInject() {
        setQueryTarget(QUERY_FOR_LIBRARY);
    }

    @Click(R.id.more)
    void refreshByButton() {
        loadData();
    }

    @AfterViews
    void init() {
        setTitleText(target == UIHelper.TARGET_FOR_NOW_BORROW ? "当前借阅" : "过去借阅");
        setMoreButtonText("刷新");
        setDataEmptyTips("当前借阅记录为空");
        loadData();
        cacheHelper.setCacheKey("borrowedBook_" + target);
        list = cacheHelper.loadListFromCache();
        buildAndShowListViewAdapter();
    }

    @UiThread
    void showReNewResult() {
        AppToast.show(this, "你已续借成功", 0);
    }

    /**
     * 续借网络请求;
     *
     * @param barCode
     * @param checkCode
     */
    @Background
    public void reNew(String barCode, String checkCode) {

        try {
            api.reNewBook(app.userName, app.getEncodeLibPassword(), barCode, checkCode);
            showReNewResult();
        } catch (HttpStatusCodeException e) {
            showErrorResult(e.getStatusCode().value());
        }

    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        beforeLoadData("正在查询", "", "取消", new AppProgress.Callback() {
            @Override
            public void onCancel() {

            }
        });
        try {
            if (target == UIHelper.TARGET_FOR_PAST_BORROW) {
                list = api.getHistoryBorrowedBooks(AppContext.userName, app.getEncodeLibPassword()).getBooks();
            } else {
                list = api.getNowBorrowedBooks(AppContext.userName, app.getEncodeLibPassword()).getBooks();
            }
            cacheHelper.writeListToCache(list);
            buildAndShowListViewAdapter();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 500) {
                handleServerError();
            } else {
                showErrorResult(e.getStatusCode().value());
            }
        } catch (Exception e) {
            handleNoNetWorkError();
        }
        afterLoadData();
    }

    private void buildAndShowListViewAdapter() {
        if (list == null)
            return;
        BorrowedBookAdapter_ bookadapter = BorrowedBookAdapter_.getInstance_(this);
        bookadapter.setParent(this);
        bookadapter.addAll(list);
        adapter = UIHelper.buildEffectAdapter(bookadapter, (AbsListView) listView, ALPHA);
        showSuccessResult();
    }

    void handleServerError() {
        AppToast.show(this, "图书馆服务器繁忙,稍候再试试", 0);
    }
}