package cn.scau.scautreasure.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.UpdateWeekAdapter;

/**
 * Created by macroyep on 14/10/1.
 */
@EActivity(R.layout.update_current_week)
public class UpdateCurrentWeek extends BaseActivity {

    //当前第几周
    @Extra("1")
    String current;
    @ViewById(R.id.currentWeek)
    TextView tv;
    @ViewById(R.id.listviews)
    ListView _listView;
    List list;

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
        backToClassTable(Integer.parseInt(current));

    }

    @AfterViews
    void initViews() {
        setMoreButtonText("当前周");
        setTitleText("当前第" + current + "周");
        list = new ArrayList();
        for (int i = 1; i < 23; i++) {
            list.add(i);
        }
        _listView.setAdapter(new UpdateWeekAdapter(this, R.layout.select_week_dialog, list));
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                backToClassTable(Integer.parseInt(list.get(i).toString()));
            }
        });

    }

    void backToClassTable(int i) {
        Intent data = new Intent();
        data.putExtra("week", i);
        this.setResult(RESULT_OK, data);
        finish();
    }

}
