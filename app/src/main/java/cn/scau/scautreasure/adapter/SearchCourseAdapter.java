package cn.scau.scautreasure.adapter;
/**
 * 2014-3-17
 */
import android.content.Context;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.CourseModel;

public class SearchCourseAdapter extends QuickAdapter<CourseModel> {
    private Context context = null;

    public SearchCourseAdapter(Context context, int layoutResId, List<CourseModel> data) {
        super(context, layoutResId, data);
        this.context = context;

    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, CourseModel model) {
        baseAdapterHelper.setText(R.id.course_name,
                model.getCourseName() + "(" + model.getTeacher() + ")")
                .setText(R.id.course_property, model.getProperty())
                .setText(R.id.course_scroe, model.getScore())
                .setText(R.id.course_liked, String.valueOf(model.getLiked()) + " " + context.getString(R.string.ups))
                .setText(R.id.course_disliked, String.valueOf(model.getDisliked()) + " " + context.getString(R.string.downs));
    }
}
