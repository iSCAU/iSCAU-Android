package cn.scau.scautreasure.adapter;

import android.content.Context;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.GoalModel;
import cn.scau.scautreasure.model.PickClassModel;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

/**
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class PickClassAdapter extends QuickAdapter<PickClassModel> {


    public PickClassAdapter(Context context, int layoutResId, List<PickClassModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, PickClassModel pickClassModelModel) {
        baseAdapterHelper.setText(R.id.tv_classname,pickClassModelModel.getName())
                         .setText(R.id.tv_place, pickClassModelModel.getPlace())
                         .setText(R.id.tv_week_range,pickClassModelModel.getWeek_range())
                         .setText(R.id.tv_time, pickClassModelModel.getTime())
                         .setText(R.id.tv_place, pickClassModelModel.getPlace())
                         .setText(R.id.tv_name_teacher, pickClassModelModel.getName_teacher())
                         .setText(R.id.tv_campus, pickClassModelModel.getCampus())
                         .setText(R.id.tv_classify, pickClassModelModel.getClassify())
                         .setText(R.id.tv_hours_week, pickClassModelModel.getHours_week())
                         .setText(R.id.tv_college_belong,pickClassModelModel.getCollege_belong())
                         .setText(R.id.tv_teaching_material, pickClassModelModel.getTeaching_material());
    }
}
