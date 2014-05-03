package cn.scau.scautreasure.ui;

import android.widget.AbsListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.ClassRoomAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.ServerOnChangeListener;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_SWING;

/**
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午6:15
 * Mail:  specialcyci@gmail.com
 */

@EActivity( R.layout.emptyclassroom )
public class EmptyClassRoom extends CommonActivity implements ServerOnChangeListener{

    @RestService
    EdusysApi api;
    @Extra("value")
    ArrayList<String> value;

    @AfterViews
    void init(){
        setTitle(R.string.title_emptyclassroom);
        setDataEmptyTips(R.string.tips_emptyclassroom_null);
    }

    @AfterViews
    void initView(){
        UIHelper.getDialog(R.string.loading_default).show();
        loadData(value);
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{
            ArrayList<String> param = (ArrayList<String>)params[0];
            list = api.getEmptyClassRoom(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server,
                    param.get(0), param.get(1), param.get(2), param.get(3), param.get(4),
                    param.get(5), param.get(6)).getClassRooms();
            buildListViewAdapter();
            showSuccessResult();

        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(), this);
        }
    }

    private void buildListViewAdapter(){
        ClassRoomAdapter cAdapter = new ClassRoomAdapter(getSherlockActivity(), R.layout.emptyclassroom_listitem, list);
        adapter = UIHelper.buildEffectAdapter(cAdapter, (AbsListView) listView,EXPANDABLE_SWING);
    }

    @Override
    public void onChangeServer() {
        UIHelper.getDialog(R.string.loading_default).show();
        loadData(value);
    }
}