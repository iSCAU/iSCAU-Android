package cn.scau.scautreasure.adapter;

import android.content.Context;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.ExamModel;
import cn.scau.scautreasure.model.GoalModel;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

/**
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class ExamAdapter extends QuickAdapter<ExamModel> {


    public ExamAdapter(Context context, int layoutResId, List<ExamModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, ExamModel examModel) {
        baseAdapterHelper.setText(R.id.tv_classname,examModel.getName())
                         .setText(R.id.tv_place, examModel.getPlace())
                         .setText(R.id.tv_time, examModel.getTime())
                         .setText(R.id.tv_seat_number, examModel.getSeat_number())
                         .setText(R.id.tv_campus, examModel.getCampus())
                         .setText(R.id.tv_form, examModel.getForm());
    }
}
