package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.view.View;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.GoalModel;

/**
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class GoalAdapter extends QuickAdapter<GoalModel> {


    private final Context context;

    public GoalAdapter(Context context, int layoutResId, List<GoalModel> data) {
        super(context, layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseAdapterHelper baseAdapterHelper, GoalModel goalModel) {

        try {
            // check the lesson if restudy;
            int point = 0;
            if (goalModel.getFlag_restudy().trim().equals("")) {
                point = goalModel.getGoal() == null ? 0 : Integer.valueOf(goalModel.getGoal());
                baseAdapterHelper.setText(R.id.tv_lable_goal, context.getString(R.string.listitem_lable_goal));
            } else {
                point = goalModel.getGoal_restudy() == null ? 0 : Integer.valueOf(goalModel.getGoal_restudy());
                baseAdapterHelper.setText(R.id.tv_lable_goal, context.getString(R.string.listitem_lable_restudygoal));
            }

            // check the goal whether failed or not
            if (point < 60) {
                baseAdapterHelper.getView(R.id.tv_classname).setBackgroundColor(
                        context.getResources().getColor(R.color.goal_item_failed_color));
            } else {
                baseAdapterHelper.getView(R.id.tv_classname).setBackgroundColor(
                        context.getResources().getColor(R.color.goal_item_passed_color));
            }
        } catch (NumberFormatException e) {

        }


        baseAdapterHelper.setText(R.id.tv_classname, goalModel.getName())
                .setText(R.id.tv_goal, goalModel.getGoal())
                .setText(R.id.tv_year, goalModel.getYear())
                .setText(R.id.tv_team, goalModel.getTeam())
                .setText(R.id.tv_credit, goalModel.getCredit())
                .setText(R.id.tv_grade_point, goalModel.getGrade_point())
                .setText(R.id.tv_classify, goalModel.getClassify())
                .setText(R.id.tv_college_belong, goalModel.getCollege_belong())
                .setText(R.id.tv_classcode, goalModel.getCode())
                .setText(R.id.tv_college_hold, goalModel.getCollege_hold())
                .setText(R.id.tv_goal_exam, goalModel.getGoal_exam())
                .setText(R.id.tv_goal_regular, goalModel.getGoal_regular());


        // Transmit the view click to expand button
        baseAdapterHelper.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() != R.id.expandable_toggle_button) {
                    baseAdapterHelper.getView(R.id.expandable_toggle_button).performClick();
                }
            }
        });

    }
}
