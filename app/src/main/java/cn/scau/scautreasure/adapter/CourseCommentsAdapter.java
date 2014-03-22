package cn.scau.scautreasure.adapter;
/**
 * 2014-3-18
 */
import android.content.Context;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.CourseCommentModel;

public class CourseCommentsAdapter extends QuickAdapter<CourseCommentModel> {
    private Context context = null;

    public CourseCommentsAdapter(Context context, int layoutResId, List<CourseCommentModel> data) {
        super(context, layoutResId, data);
        this.context = context;
    }

    protected void convert(BaseAdapterHelper baseAdapterHelper, CourseCommentModel model) {
        baseAdapterHelper.setText(R.id.username, model.getUserName())
                .setText(R.id.time, model.getTime())
                .setText(R.id.comment, model.getComment())
                .setText(R.id.ishomework, model.getHasHomework() ? context.getString(R.string.str_have) : context.getString(R.string.str_none))
                .setText(R.id.ischeck, model.getIsCheck() ? context.getString(R.string.str_no) : context.getString(R.string.str_yes))
                .setText(R.id.isexam, model.getExam());

    }


}
