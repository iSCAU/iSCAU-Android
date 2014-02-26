package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.view.View;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.ClassRoomModel;
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
public class ClassRoomAdapter extends QuickAdapter<ClassRoomModel> {


    public ClassRoomAdapter(Context context, int layoutResId, List<ClassRoomModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(final BaseAdapterHelper baseAdapterHelper, ClassRoomModel classRoomModel) {
        baseAdapterHelper.setText(R.id.tv_number,classRoomModel.getNumber())
                         .setText(R.id.tv_type, classRoomModel.getType())
                         .setText(R.id.tv_has_book,classRoomModel.getHas_book())
                         .setText(R.id.tv_seat_class, classRoomModel.getSeat_class())
                         .setText(R.id.tv_seat_exam, classRoomModel.getSeat_exam())
                         .setText(R.id.tv_area , classRoomModel.getArea());

        baseAdapterHelper.getView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() != R.id.expandable_toggle_button){
                    baseAdapterHelper.getView(R.id.expandable_toggle_button).performClick();
                }
            }
        });
    }
}
