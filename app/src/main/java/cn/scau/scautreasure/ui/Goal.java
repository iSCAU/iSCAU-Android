package cn.scau.scautreasure.ui;

import android.widget.AbsListView;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.GoalAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import cn.scau.scautreasure.model.GoalModel;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.*;

/**
 * 成绩查询;
 * User:  Special Leung
 * Date:  13-8-15
 * Time:  下午2:23
 * Mail:  specialcyci@gmail.com
 */
@EFragment(R.layout.goal)
public class Goal extends Common implements ServerOnChangeListener{

    @RestService
    EdusysApi api;
    private ArrayList<String> value;

    @AfterInject
    void init(){
        setTitle(R.string.title_goal);
        tips_empty = R.string.tips_goal_null;
    }

    @AfterViews
    void initView(){
        value = getArguments().getStringArrayList("value");
        UIHelper.getDialog(R.string.loading_default).show();
        loadData(value);
    }

    void calculateGPA(){
        double total_credit_point = 0;
        double total_credit       = 0;

        for(GoalModel g : (List <GoalModel>)list){
            double credit      = Double.parseDouble(g.getCredit());
            double grade_point = Double.parseDouble(g.getGrade_point());
            total_credit       +=  credit;
            total_credit_point +=  (double)credit * grade_point;
        }

        // 一学期（学年）的平均绩点＝该学期（学年）全部学分绩点之和÷该学期（学年）所修学分之和
        double gpa       = (double)total_credit_point / total_credit;
        showGPAInformation(gpa,total_credit_point);
    }

    @UiThread
    void showGPAInformation(double gpa,double total_credit_point){
        StringBuilder subTitle = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.00");
        subTitle.append(getString(R.string.tv_gpa) + df.format(gpa));
        subTitle.append(" ");
        subTitle.append(getString(R.string.total_credit_point)+ df.format(total_credit_point));
        getSherlockActivity().getSupportActionBar().setSubtitle(subTitle.toString());
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{
            ArrayList<String> param = (ArrayList<String>)params[0];
            list = api.getGoal(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server, param.get(0), param.get(1)).getGoals();
            buildListViewAdapter();
            showSuccessResult();
            calculateGPA();
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(),this);
        }
    }

    private void buildListViewAdapter(){
        GoalAdapter listadapter = new GoalAdapter(getSherlockActivity(), R.layout.goal_listitem, list);
        adapter = UIHelper.buildEffectAdapter(listadapter, (AbsListView) listView,EXPANDABLE_ALPHA);
    }

    @Override
    public void onChangeServer() {
        UIHelper.getDialog(R.string.loading_default).show();
        loadData(value);
    }
}