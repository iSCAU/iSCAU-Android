package cn.scau.scautreasure.ui;

import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.IntroductionAdapter;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.IntroductionModel;
import cn.scau.scautreasure.util.TextUtil;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_ALPHA;

/**
 * 新生介绍，社区指南等.
 * User: special
 * Date: 13-8-26
 * Time: 下午11:35
 * Mail: specialcyci@gmail.com
 */
@EActivity( R.layout.introduction )
public class Introduction extends CommonActivity {

    @Bean
    TextUtil textUtil;

    @ViewById
    View listView;

    @Extra("target")
    String   target;
    @Extra("title")
    int      title;
    private BaseAdapter adapter;

    @AfterViews
    void init(){
        loadData();
        setTitle(title);
    }

    @UiThread
    void showContent(){
        ((ListView)listView).setAdapter(adapter);
    }

    @Background
    void loadData(Object... params) {
        String fileName = "introduction/" + target + ".json";
        String context  = textUtil.getFromAssets(fileName);
        List<IntroductionModel> introList = IntroductionModel.parse(context);
        buildListViewAdapter(introList);
        showContent();

    }

    private void buildListViewAdapter(List<IntroductionModel> introList){
        IntroductionAdapter introAdapter  = new IntroductionAdapter(getSherlockActivity(), R.layout.introduction_listitem, introList);
        adapter  = UIHelper.buildEffectAdapter(introAdapter, (AbsListView) listView,EXPANDABLE_ALPHA);
    }
}