package cn.scau.scautreasure.adapter;

import android.content.Context;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.CourseModel;

/**
 * 搜到的图书列表适配器
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class SearchCourseAdapter extends QuickAdapter<CourseModel> {

    public SearchCourseAdapter(Context context, int layoutResId, List<CourseModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, CourseModel model) {
        //Log.d("abc",model.getCourseName()+" "+model.getTeacher()+" "+model.getProperty()+
        //" "+model.getScroe()+" "+model.getDisliked()+" "+model.getLiked());
        baseAdapterHelper.setText(R.id.course_name, model.getCourseName() + "(" +
                model.getTeacher() + ")")
                         .setText(R.id.course_property, model.getProperty())
                         .setText(R.id.course_score, model.getScore())
                         .setText(R.id.like_number, String.valueOf(model.getLiked()))
                         .setText(R.id.dislike_number, String.valueOf(model.getDisliked()));
    }
}
