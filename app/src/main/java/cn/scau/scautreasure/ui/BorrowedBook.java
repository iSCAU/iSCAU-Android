package cn.scau.scautreasure.ui;

import android.widget.AbsListView;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
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

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;

/**
 * 当前借阅书籍记录及过去借阅记录
 * User: special
 * Date: 13-8-20
 * Time: 上午11:36
 * Mail: specialcyci@gmail.com
 */
@EActivity( R.layout.borrowedbook )
public class BorrowedBook extends CommonActivity {

    @RestService
    LibraryApi api;

    @Extra("target")
    int target;

    @AfterViews
    void init(){
        setTitle(R.string.title_borrowedbook);
        setDataEmptyTips(R.string.tips_borrowedbook_null);
        UIHelper.getDialog(R.string.loading_borrowedbook).show();
        loadData();
    }

    @UiThread
    void showReNewResult(){
        AppMsg.makeText(getSherlockActivity(), R.string.tips_renew_success, AppMsg.STYLE_INFO).show();
    }

    /**
     * 续借网络请求;
     * @param barCode
     * @param checkCode
     */
    @Background
    public void reNew(String barCode,String checkCode){

        try{
            api.reNewBook( app.userName, app.getEncodeLibPassword(), barCode, checkCode );
            showReNewResult();
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }

    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {

        try{
            if ( target == UIHelper.TARGET_FOR_NOW_BORROW ) {
                list = api.getNowBorrowedBooks(AppContext.userName, app.getEncodeLibPassword()).getBooks();
            }else{
                list = api.getHistoryBorrowedBooks(AppContext.userName,app.getEncodeLibPassword()).getBooks();
            }
            buildListViewAdapter();
            showSuccessResult();
        }catch ( HttpStatusCodeException e ){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }catch ( Exception e){
            e.printStackTrace();
        }
    }

    private void buildListViewAdapter(){
        BorrowedBookAdapter_ bookadapter = BorrowedBookAdapter_.getInstance_(getSherlockActivity());
        bookadapter.setParent(this);
        bookadapter.addAll(list);
        adapter     = UIHelper.buildEffectAdapter(bookadapter, (AbsListView) listView,ALPHA);
    }
}