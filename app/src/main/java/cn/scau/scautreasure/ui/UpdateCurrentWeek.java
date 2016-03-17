package cn.scau.scautreasure.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.UpdateWeekAdapter;


@EActivity(R.layout.update_current_week)
public class UpdateCurrentWeek extends CommonActivity {
    //当前第几周
    @Extra("1")
    String current;
    @ViewById(R.id.currentWeek)
    TextView tv;
    @ViewById(R.id.listviews)
    ListView _listView;
    @AfterViews
    void  initViews(){
        setTitle("设置当前周");
        tv.setText("当前第"+current+"周");
        List list=new ArrayList();
        for (int i=1;i<23;i++){
            list.add(i);
        }
        _listView.setAdapter(new UpdateWeekAdapter(this,R.layout.select_week_dialog,list));
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getSherlockActivity(),"设置当前周为:第"+(i+1)+"周",Toast.LENGTH_LONG).show();
                //finish();
                backToClassTable(i+1);
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
