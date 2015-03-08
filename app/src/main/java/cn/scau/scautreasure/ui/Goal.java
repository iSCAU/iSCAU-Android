package cn.scau.scautreasure.ui;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.springframework.web.client.HttpStatusCodeException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.GoalAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.GoalModel;
import cn.scau.scautreasure.util.StringUtil;
import cn.scau.scautreasure.widget.AppProgress;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_ALPHA;

@EActivity(R.layout.goal)
public class Goal extends ListActivity {

    @ViewById(R.id.average)
    TextView average;
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
        setTitleText("查成绩");
        setMoreButtonText("刷新");

        tips_empty = "正方上没有你的考试成绩";
        cacheHelper.setCacheKey("goal_" + StringUtil.join(value, "_"));
        list = cacheHelper.loadListFromCache();
        buildAndShowListViewAdapter();
    }


    void calculateGPA() {
        if (list == null)
            return;

        double total_credit_point = 0;
        double total_credit = 0;

        for (GoalModel g : (List<GoalModel>) list) {
            if("".equals(g.getCredit())||"".equals(g.getGrade_point())){
                continue;
            }else {
                double credit = Double.parseDouble(g.getCredit());
                double grade_point = Double.parseDouble(g.getGrade_point());
                total_credit += credit;
                total_credit_point += (double) credit * grade_point;
            }
        }

        // 一学期（学年）的平均绩点＝该学期（学年）全部学分绩点之和÷该学期（学年）所修学分之和
        double gpa = (double) total_credit_point / total_credit;
        showGPAInformation(gpa, total_credit_point);
    }

    @UiThread
    void showGPAInformation(double gpa, double total_credit_point) {
        StringBuilder subTitle = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.00");
        subTitle.append(getString(R.string.tv_gpa) + df.format(gpa));
        subTitle.append(" ");
        subTitle.append(getString(R.string.total_credit_point) + df.format(total_credit_point));
        average.setText(subTitle.toString());
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        beforeLoadData("正在刷新最新成绩", "请耐心等待,正方你懂的", "取消", new AppProgress.Callback() {
            @Override
            public void onCancel() {
                BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);
            }
        });
        try {
            ArrayList<String> param = value;
            list = api.getGoal(AppContext.userName, app.getEncodeEduSysPassword(), param.get(0), param.get(1)).getGoals();
            for (int i = 0; i < list.size(); i++)
                cacheHelper.writeListToCache(list);
            buildAndShowListViewAdapter();
        } catch (HttpStatusCodeException e) {
            Log.d("server_code", e.getStatusCode() + "");
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());

        } catch (Exception e) {
            handleNoNetWorkError();
        }
        afterLoadData();
    }

    private void buildAndShowListViewAdapter() {
        GoalAdapter listadapter = new GoalAdapter(this, R.layout.goal_listitem, list);
        adapter = UIHelper.buildEffectAdapter(listadapter, (AbsListView) listView, EXPANDABLE_ALPHA);
        showSuccessResult();
        calculateGPA();
    }

}